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
import io.naztech.nuxeoclient.model.invoice.aps.ApsInvoiceDocument;
import lombok.extern.slf4j.Slf4j;

@Named
@Slf4j
public class ScannedApsInvoiceUploadService extends AbstractScannedInvoidUploadService {
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

	@Value("${import.aps.archiveFolder-path}")
	private String archiveFolderPath;

	@Value("${import.aps.badFolder-path}")
	private String badfolderPath;

	@Value("${import.aps.nuxeoFailedFolder-path}")
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
	private ApsInvoiceDocument getInvoiceFromXml(File file) {
		try {
			byte[] bytes = Files.readAllBytes(file.toPath());
			Unmarshaller ums = JAXBContext.newInstance(ApsInvoiceDocument.class).createUnmarshaller();
			XMLStreamReader reader = XMLInputFactory.newInstance()
					.createXMLStreamReader(new ByteArrayInputStream(bytes));
			JAXBElement<ApsInvoiceDocument> jaob = ums.unmarshal(reader, ApsInvoiceDocument.class);
			ApsInvoiceDocument ob = jaob.getValue();
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
	private DocumentWrapper convertInvoiceToDocumentReq(ApsInvoiceDocument inv) {
		if (inv == null)
			return null;
		try {
			DocumentWrapper ob = DocumentWrapper.createWithName("ApsInvoice", "APS");
			ob.setTitle("Aps");
			ob.setDescription("Invoice");
			ob.setPrefix("aps:");
			ob.setRepoPath("/default-domain/workspaces/Sample Content/Invoice");
			ob.addAttribute("carriage_net", "NULL");
			ob.addAttribute("client_name", inv.getAPSDocumentDefinition().getCustomerName());
			ob.addAttribute("company_address", inv.getAPSDocumentDefinition().getSupplierAddress());
			ob.addAttribute("customer_address", inv.getAPSDocumentDefinition().getCustomerAddress());
			ob.addAttribute("customer_order_no", inv.getAPSDocumentDefinition().getOrderNumber());
			ob.addAttribute("delivery_to", inv.getAPSDocumentDefinition().getDeliverAddress());
			ob.addAttribute("discount", inv.getAPSDocumentDefinition().getDiscount());
			ob.addAttribute("invoice_number", Long.toString(inv.getAPSDocumentDefinition().getInvoiceNumber()));
			ob.addAttribute("net_invoice_total",
					bigDescimalToString(inv.getAPSDocumentDefinition().getNetInvoiceTotal().toString()));
			ob.addAttribute("net_value", inv.getAPSDocumentDefinition().getValueOfGoods());
			ob.addAttribute("reference_no", inv.getAPSDocumentDefinition().getReferenceNumber());
			ob.addAttribute("supplier_name", inv.getAPSDocumentDefinition().getSupplierName());
			ob.addAttribute("supplier_number", inv.getAPSDocumentDefinition().getSupplierTelephoneNo());
			ob.addAttribute("vat", inv.getAPSDocumentDefinition().getVat().split("£")[1].trim());
			ob.addAttribute("vat_number", inv.getAPSDocumentDefinition().getVatRegNumber().split(":")[1].trim());
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
		ApsInvoiceDocument result = null;
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
			tmplt.setClientName(result.getAPSDocumentDefinition().getCustomerName());
			tmplt.setReceivedDocId(tmplt.getClientName() + "_" + result.getAPSDocumentDefinition().getOrderNumber());
			DocumentWrapper req = convertInvoiceToDocumentReq(result);
			tmplt.setSupplierName(req.getTitle());
			if (result.getAPSDocumentDefinition().getCustomerName().equals(null)
					&& result.getAPSDocumentDefinition().getSupplierName().equals(null)
					&& result.getAPSDocumentDefinition().getNetInvoiceTotal().equals(null)) {
				log.error(invoiceInfoXml.getName() + " Moved to Bad Folder");
				moveToBadFolder(badfolderPath, invoiceInfoXml, attachments, strDate);
				tmplt.setIsSentToNuxeo(0);
				tmplt.setIsTemplateParsed(0);
				tmplt.setIsParsedSuccessful(0);
				isSuccess = 0;
			} else {
				isSuccess = 1;
				document.setCompanyAddress(result.getAPSDocumentDefinition().getSupplierAddress());
				document.setCustomerAddress(result.getAPSDocumentDefinition().getCustomerAddress());
				document.setInvoiceNumber(Long.toString(result.getAPSDocumentDefinition().getInvoiceNumber()));
				document.setReferenceNo(result.getAPSDocumentDefinition().getReferenceNumber().toString());
				document.setCustomerOrderNo(result.getAPSDocumentDefinition().getOrderNumber());
				document.setDiscount(
						nullHandlerDouble(result.getAPSDocumentDefinition().getDiscount().split("£")[1].trim()));
				document.setVat(nullHandlerDouble(result.getAPSDocumentDefinition().getVat().split("£")[1].trim()));
				document.setNetInvoiceTotal(
						nullHandlerDouble(result.getAPSDocumentDefinition().getNetInvoiceTotal().toString()));
				document.setAccountNumber(Long.toString(result.getAPSDocumentDefinition().getAccountNumber()));
				document.setValueOfGoods(
						nullHandlerDouble(result.getAPSDocumentDefinition().getValueOfGoods().toString()));
				document.setSupplierNumber(result.getAPSDocumentDefinition().getSupplierTelephoneNo());
				document.setVatNumber(result.getAPSDocumentDefinition().getVatRegNumber().split(":")[1].trim());
				document.setDeliverTo(result.getAPSDocumentDefinition().getDeliverAddress());
				document.setDeliveryDetails(result.getAPSDocumentDefinition().getCustomerAddress());
				document.setInvoiceTo(result.getAPSDocumentDefinition().getCustomerAddress());
				document.setInvoiceAmount(
						nullHandlerDouble(result.getAPSDocumentDefinition().getValueOfGoods().toString()));
				document.setTotalBeforeVat(
						nullHandlerDouble(result.getAPSDocumentDefinition().getValueOfGoods().toString()));
				try {
					document.setDespatchDate(new SimpleDateFormat("yyyy/MM/dd")
							.parse(dateFormatter(result.getAPSDocumentDefinition().getInvoiceDate())));
					document.setDocumentDate(new SimpleDateFormat("yyyy/MM/dd")
							.parse(dateFormatter(result.getAPSDocumentDefinition().getInvoiceDate())));

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
				for (int i = 0; i < result.getAPSDocumentDefinition().getInvoiceTable().size(); i++) {
					docdetails.setUnitPrice(nullHandlerDouble(
							result.getAPSDocumentDefinition().getInvoiceTable().get(i).getUnitPrice().toString()));
					docdetails.setItemQty(nullHandlerInteger(result.getAPSDocumentDefinition().getInvoiceTable().get(i)
							.getQty().toString().split("\\.")[0]));
					docdetails.setNetValue(nullHandlerDouble(
							result.getAPSDocumentDefinition().getInvoiceTable().get(i).getNetAmt().toString()));
					docdetails.setItemCode(result.getAPSDocumentDefinition().getInvoiceTable().get(i).getStockCode());
					docdetails.setItemDescription(
							result.getAPSDocumentDefinition().getInvoiceTable().get(i).getDescription());
					docdetails.setDocId(documentTemp.get().getDocId());
					docDetailsService.processAction("NEW", docdetails, Constants.DOC_DETAILS);

				}
			}

			else {
				log.info("Document Data is not available");
			}

		}
	}

}
