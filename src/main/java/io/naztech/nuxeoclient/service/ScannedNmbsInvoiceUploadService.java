package io.naztech.nuxeoclient.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.nuxeo.client.NuxeoClient;
import org.nuxeo.client.objects.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import io.naztech.nuxeoclient.constants.Constants;
import io.naztech.nuxeoclient.model.DocumentWrapper;
import io.naztech.nuxeoclient.model.invoice.nmbs.Documents;
import lombok.extern.slf4j.Slf4j;

@Named
@Slf4j
public class ScannedNmbsInvoiceUploadService extends AbstractScannedInvoidUploadService {
	@Autowired
	private NuxeoClient nuxeoClient;
	@Autowired
	NuxeoClientService serv;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private DocumentService documentService;

	@Autowired
	private DocDetailsService docDetailsService;

	@Value("${import.nmbs.archiveFolder-path}")
	private String archiveFolderPath;

	@Value("${import.nmbs.badFolder-path}")
	private String badfolderPath;

	@Value("${import.nmbs.nuxeoFailedFolder-path}")
	private String nuxeoFailedFolderPath;

	@Value("${archive.enabled}")
	private boolean enabled;

	/**
	 * Constructs a FestoolInvoice object from the xml file
	 * 
	 * @param file
	 * @return FestoolInvoice Object
	 * @throws IOException
	 */

	private String supplierName = "National Merchant Buying Society Limited(NMBS)";
	// private String supplierAddress = "10 Merus Court, Meridian Business Park,
	// Leicester LE29 1RJ";

	private Documents getInvoiceFromXml(File file) {
		try {
			byte[] bytes = Files.readAllBytes(file.toPath());
			Unmarshaller ums = JAXBContext.newInstance(Documents.class).createUnmarshaller();
			XMLStreamReader reader = XMLInputFactory.newInstance()
					.createXMLStreamReader(new ByteArrayInputStream(bytes));
			JAXBElement<Documents> jaob = ums.unmarshal(reader, Documents.class);
			Documents ob = jaob.getValue();
			return ob;
		} catch (Exception e) {
			log.warn("Failed to parse XML", e);
			return null;
		}

	}

	/**
	 * Converts Invoice to DocumentRequest
	 * 
	 * @param inv
	 * @return DocumentWrapper object
	 */
	private DocumentWrapper convertInvoiceToDocumentReq(Documents inv) {
		if (inv == null)
			return null;
		try {
			DocumentWrapper ob = DocumentWrapper.createWithName("NmbsInvoice", "NMBS");
			ob.setTitle("Nmbs");
			ob.setDescription("Invoice");
			ob.setPrefix("nmbs:");
			ob.setRepoPath("/default-domain/workspaces/Sample Content/Invoice");
			ob.addAttribute("client_name", inv.getNmbsDocumentDefinition().getCustomerName());
			ob.addAttribute("company_address", inv.getNmbsDocumentDefinition().getCompanyAddress());
			ob.addAttribute("customer_order_no", inv.getNmbsDocumentDefinition().getOrderNumber());
			ob.addAttribute("deliver_to", inv.getNmbsDocumentDefinition().getDeliveryAddress());
			ob.addAttribute("delivery_note_no", Long.toString(inv.getNmbsDocumentDefinition().getDeliveryNote()));
			ob.addAttribute("invoice_date", inv.getNmbsDocumentDefinition().getInvoiceDate());
			ob.addAttribute("invoice_to", Long.toString(inv.getNmbsDocumentDefinition().getInvoiceNumber()));
			ob.addAttribute("net_invoice_total",
					bigDescimalToString(inv.getNmbsDocumentDefinition().getNetInvoiceTotal().toString()));
			ob.addAttribute("supplier_name", supplierName);
			ob.addAttribute("supplier_number", inv.getNmbsDocumentDefinition().getSupplierTelephoneNo().toString());
			ob.addAttribute("vat", bigDescimalToString(inv.getNmbsDocumentDefinition().getVat().toString()));
			ob.addAttribute("voucher_number", inv.getNmbsDocumentDefinition().getVoucherNumber());
			return ob;
		} catch (Exception e) {
			log.warn("Failed to convert to Document Wrapper Object", e);
			return null;
		}
	}

	/**
	 * Uploads the file to the Nuxeo server and moves the file from hotfolder to
	 * archive
	 * 
	 * @param invoiceInfoXml
	 * @param attachments
	 * @return request
	 * @throws IOException
	 */
	@Override
	public void uploadDocument(File invoiceInfoXml, List<File> attachments, boolean flag) throws IOException {
		int isSuccess = 0;

		LocalDate date = LocalDate.now();
		String strDate = date.toString();

		io.naztech.nuxeoclient.model.Template tmplt = new io.naztech.nuxeoclient.model.Template();
		io.naztech.nuxeoclient.model.Document document = new io.naztech.nuxeoclient.model.Document();
		io.naztech.nuxeoclient.model.DocDetails docdetails = new io.naztech.nuxeoclient.model.DocDetails();

		if (enabled && flag) {
			moveToArchive(archiveFolderPath, invoiceInfoXml, attachments, strDate);
		}
		serv.setNuxeoClient(nuxeoClient);
		Documents result = null;
		try {
			result = getInvoiceFromXml(invoiceInfoXml);
		} catch (Exception ex) {
			tmplt.setErrorIssue(ex.getCause().toString());
			log.error("Error parsing attachment: {}", ex.getMessage());
		}
		String name = attachments.get(0).getName();
		if (name.contains("EMAIL")) {
			tmplt.setDocSource("EMAIL");
		} else if (name.contains("FTP")) {
			tmplt.setDocSource("FTP");
		}
		tmplt.setPdfType("TEXT");
		tmplt.setProcessorType("ABBYY");
		if (null != result) {
			tmplt.setClientName(result.getNmbsDocumentDefinition().getCustomerName());
			tmplt.setReceivedDocId(tmplt.getClientName() + "_" + result.getNmbsDocumentDefinition().getOrderNumber());
			DocumentWrapper req = convertInvoiceToDocumentReq(result);
			tmplt.setSupplierName(req.getTitle());
			if (result.getNmbsDocumentDefinition().getCustomerName().equals(null) && supplierName.equals(null)
					&& result.getNmbsDocumentDefinition().getNetInvoiceTotal().equals(null)) {
				log.error(invoiceInfoXml.getName() + " Moved to Bad Folder");
				moveToBadFolder(badfolderPath, invoiceInfoXml, attachments, strDate);
				tmplt.setIsSentToNuxeo(0);
				tmplt.setIsTemplateParsed(0);
				tmplt.setIsParsedSuccessful(0);
				isSuccess = 0;
			} else {
				isSuccess = 1;
				// supplierName
				document.setCompanyAddress(result.getNmbsDocumentDefinition().getCompanyAddress().toString());
				document.setContactNumber(result.getNmbsDocumentDefinition().getSupplierTelephoneNo().toString());
				document.setInvoiceNumber(Long.toString(result.getNmbsDocumentDefinition().getInvoiceNumber()));
				document.setVoucherNumber(result.getNmbsDocumentDefinition().getVoucherNumber());
				document.setFaxNumber(result.getNmbsDocumentDefinition().getFaxNumber());
				document.setCustomerOrderNo(result.getNmbsDocumentDefinition().getOrderNumber());
				document.setAccountNumber(result.getNmbsDocumentDefinition().getAccountNumber());

				document.setDeliveryNote(Long.toString(result.getNmbsDocumentDefinition().getDeliveryNote()));
				// customerName
				document.setInvoiceTo(result.getNmbsDocumentDefinition().getCustomerAddress());
				document.setDeliveryDetails(result.getNmbsDocumentDefinition().getCustomerAddress());

				document.setCustomerAddress(result.getNmbsDocumentDefinition().getCustomerAddress());
				document.setDeliverTo(result.getNmbsDocumentDefinition().getDeliveryAddress());
				document.setItemCode(result.getNmbsDocumentDefinition().getItemCode());
				document.setValueOfGoods(nullHandlerDouble(
						bigDescimalToString(result.getNmbsDocumentDefinition().getValueOfGoods().toString())));
				document.setVat(
						nullHandlerDouble(bigDescimalToString(result.getNmbsDocumentDefinition().getVat().toString())));
				document.setInvoiceAmount(
						nullHandlerDouble(result.getNmbsDocumentDefinition().getValueOfGoods().toString()));
				document.setTotalBeforeVat(
						nullHandlerDouble(result.getNmbsDocumentDefinition().getValueOfGoods().toString()));
				document.setNetInvoiceTotal(nullHandlerDouble(
						bigDescimalToString(result.getNmbsDocumentDefinition().getNetInvoiceTotal().toString())));
				document.setVatNumber(Long.toString(result.getNmbsDocumentDefinition().getVatRegNumber()));
				try {
					document.setDespatchDate(new SimpleDateFormat("yyyy/MM/dd")
							.parse(dateFormatter(result.getNmbsDocumentDefinition().getInvoiceDate())));
					document.setDocumentDate(new SimpleDateFormat("yyyy/MM/dd")
							.parse(dateFormatter(result.getNmbsDocumentDefinition().getInvoiceDate())));

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// Uploading the file to Nuxeo Server
			try {
				if (isSuccess == 1) {
					req.setFiles(attachments);
					Document res = serv.createDocument(req);
					if (res == null && flag) {
						tmplt.setIsSentToNuxeo(0);
						moveToNuxeoFailed(nuxeoFailedFolderPath, invoiceInfoXml, attachments, strDate);
						log.error(attachments.get(0).getName() + " Moved to NuxeoFailed Folder");
					} else {
						tmplt.setIsSentToNuxeo(1);
						tmplt.setIsTemplateParsed(1);
						tmplt.setIsParsedSuccessful(1);
						log.info("Successfully Uploaded Document Into Nuxeo: " + attachments.get(0).getName());
						Files.delete(Paths.get(invoiceInfoXml.getAbsolutePath()));
						for (File doc : attachments) {
							Files.delete(Paths.get(doc.getAbsolutePath()));
						}
					}
				}
			} catch (Exception e) {
				log.error("Moved to Nuxeo Failed ", e);
				tmplt.setIsSentToNuxeo(0);
				moveToNuxeoFailed(nuxeoFailedFolderPath, invoiceInfoXml, attachments, strDate);
				tmplt.setErrorIssue(e.getMessage());
				tmplt.setErrorIssueSource("NuxeoFailed");
			}
		}
		Optional<io.naztech.nuxeoclient.model.Template> tmp = templateService
				.processAction("NEW", tmplt, Constants.TEMPLATE).stream().findFirst();

		if (tmp.isPresent()) {

			Optional<io.naztech.nuxeoclient.model.Document> documentTemp = documentService
					.processAction("NEW", document, Constants.DOC).stream().findFirst();

			if (documentTemp.isPresent()) {

				io.naztech.nuxeoclient.model.Document documentData = documentTemp.get();
				documentData.setDocId(documentTemp.get().getDocId());
				documentService.processAction("NEW", documentData, Constants.CUSTOMER);
				for (int i = 0; i < result.getNmbsDocumentDefinition().getInvoiceTable().size(); i++) {

					docdetails.setNetValue(bigDecimalToDouble(
							result.getNmbsDocumentDefinition().getInvoiceTable().get(i).getAMOUNT()));
					docdetails.setItemDescription(
							result.getNmbsDocumentDefinition().getInvoiceTable().get(i).getDESCRIPTION());
					docdetails.setUnitPrice(nullHandlerDouble(
							result.getNmbsDocumentDefinition().getInvoiceTable().get(i).getPRICE().split(" ")[0]
									.trim()));
					docdetails
							.setItemCode(result.getNmbsDocumentDefinition().getInvoiceTable().get(i).getPRODUCTCODE());
					docdetails.setItemQty(nullHandlerInteger(
							result.getNmbsDocumentDefinition().getInvoiceTable().get(i).getQTYWEIGHT().split(" ")[0]
									.trim()));
					docdetails.setDocId(documentTemp.get().getDocId());
					docDetailsService.processAction("NEW", docdetails, Constants.DOC_DETAILS);

				}
			}
		}
	}
}
