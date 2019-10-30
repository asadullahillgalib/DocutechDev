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
import io.naztech.nuxeoclient.model.invoice.bristanGroup.BristanGroupInvoiceDocuments;
import lombok.extern.slf4j.Slf4j;

/**
 * @author muhammad.tarek
 * @author asadullah.galib
 * @since 2019-10-02
 **/

@Named
@Slf4j
public class ScannedBristanGroupInvoiceUploadService extends AbstractScannedInvoidUploadService {

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

	@Value("${import.bristan.archiveFolder-path}")
	private String archiveFolderPath;

	@Value("${import.bristan.badFolder-path}")
	private String badfolderPath;

	@Value("${import.bristan.nuxeoFailedFolder-path}")
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
	private BristanGroupInvoiceDocuments getInvoiceFromXml(File file) throws IOException {
		try {
			byte[] bytes = Files.readAllBytes(file.toPath());
			Unmarshaller ums = JAXBContext.newInstance(BristanGroupInvoiceDocuments.class).createUnmarshaller();
			XMLStreamReader reader = XMLInputFactory.newInstance()
					.createXMLStreamReader(new ByteArrayInputStream(bytes));
			JAXBElement<BristanGroupInvoiceDocuments> jaob = ums.unmarshal(reader, BristanGroupInvoiceDocuments.class);
			BristanGroupInvoiceDocuments ob = jaob.getValue();
			return ob;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("Error processing file.", e.getCause());
		}

	}

	/**
	 * Converts Invoice to DocumentRequest
	 * 
	 * @param inv
	 * @return DocumentWrapper object
	 */
	private DocumentWrapper convertInvoiceToDocumentReq(BristanGroupInvoiceDocuments inv) {
		if (inv == null)
			return null;
		try {
			DocumentWrapper ob = DocumentWrapper.createWithName("Bristan Invoice", "BRISTAN");

			// Setting attributes of the document wrapper object
			ob.setTitle("Bristan");
			ob.setDescription("Invoice");
			ob.setPrefix("bristan:");
			ob.setRepoPath("/default-domain/workspaces/Sample Content/Invoice");
			ob.addAttribute("account_number", inv.getBristanGroupDocumentDefinition().getSupplierAccountNumber());
			ob.addAttribute("client_name", inv.getBristanGroupDocumentDefinition().getCustomerName());
			ob.addAttribute("company_address", inv.getBristanGroupDocumentDefinition().getSupplierAddress());
			ob.addAttribute("deliver_to", inv.getBristanGroupDocumentDefinition().getCustomerAddress());
			ob.addAttribute("delivery_details", inv.getBristanGroupDocumentDefinition().getCustomerAddress());
			ob.addAttribute("invoice_date", inv.getBristanGroupDocumentDefinition().getInvoiceDate());
			ob.addAttribute("invoice_number", inv.getBristanGroupDocumentDefinition().getInvoiceNumber());
			ob.addAttribute("invoice_to", inv.getBristanGroupDocumentDefinition().getDeliveryAddress());
			ob.addAttribute("net_invoice_total", bigDescimalToString(inv.getBristanGroupDocumentDefinition()
					.getNetInvoiceTotal().toString().replaceAll("£", "").trim()));
			ob.addAttribute("supplier_name", inv.getBristanGroupDocumentDefinition().getSupplierName());
			ob.addAttribute("order_id", inv.getBristanGroupDocumentDefinition().getOrderNo());
			ob.addAttribute("reference_no", inv.getBristanGroupDocumentDefinition().getRefNumber());
			ob.addAttribute("value_of_goods", bigDescimalToString(
					inv.getBristanGroupDocumentDefinition().getValueOfGoods().toString().replaceAll("£", "").trim()));
			ob.addAttribute("vat", bigDescimalToString(
					inv.getBristanGroupDocumentDefinition().getVat().toString().replaceAll("£", "").trim()));
			ob.addAttribute("vat_number", inv.getBristanGroupDocumentDefinition().getVatRegNumber());
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
		BristanGroupInvoiceDocuments result = null;

		try {
			result = getInvoiceFromXml(invoiceInfoXml);
		} catch (IOException ex) {

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

			tmplt.setClientName(result.getBristanGroupDocumentDefinition().getCustomerName());
			tmplt.setReceivedDocId(
					tmplt.getClientName() + "_" + result.getBristanGroupDocumentDefinition().getOrderNo());

			DocumentWrapper req = convertInvoiceToDocumentReq(result);
			tmplt.setSupplierName(req.getTitle());

			if (result.getBristanGroupDocumentDefinition().getCustomerName().equals(null)
					&& result.getBristanGroupDocumentDefinition().getSupplierName().equals(null)
					&& result.getBristanGroupDocumentDefinition().getNetInvoiceTotal().equals(null)) {

				log.error(invoiceInfoXml.getName() + " Moved to Bad Folder");

				moveToBadFolder(badfolderPath, invoiceInfoXml, attachments, strDate);

				tmplt.setIsSentToNuxeo(0);
				tmplt.setIsTemplateParsed(0);
				tmplt.setIsParsedSuccessful(0);

				isSuccess = 0;
			} else {
				isSuccess = 1;
				document.setAccountNumber(result.getBristanGroupDocumentDefinition().getSupplierAccountNumber());
				document.setCompanyAddress(result.getBristanGroupDocumentDefinition().getSupplierAddress());
				document.setContactNumber(
						Long.toString(result.getBristanGroupDocumentDefinition().getSupplierTelephoneNo()));
				document.setCustomerAddress(result.getBristanGroupDocumentDefinition().getCustomerAddress());
				document.setCustomerOrderNo(result.getBristanGroupDocumentDefinition().getOrderNo());
				document.setDeliverTo(result.getBristanGroupDocumentDefinition().getCustomerAddress());
				document.setDeliveryDetails(result.getBristanGroupDocumentDefinition().getCustomerAddress());
				document.setFaxNumber(result.getBristanGroupDocumentDefinition().getFaxNumber());
				document.setInvoiceAmount(
						nullHandlerDouble(bigDescimalToString(result.getBristanGroupDocumentDefinition()
								.getNetInvoiceTotal().toString().replaceAll("£", "").trim())));
				document.setInvoiceNumber(result.getBristanGroupDocumentDefinition().getInvoiceNumber());
				document.setInvoiceTo(result.getBristanGroupDocumentDefinition().getCustomerAddress());
				document.setVat(nullHandlerDouble(bigDescimalToString(
						result.getBristanGroupDocumentDefinition().getVat().toString().replaceAll("£", "").trim())));
				document.setTotalBeforeVat(nullHandlerDouble(bigDescimalToString(result
						.getBristanGroupDocumentDefinition().getValueOfGoods().toString().replaceAll("£", "").trim())));
				document.setVatNumber(result.getBristanGroupDocumentDefinition().getVatRegNumber());
				document.setNetInvoiceTotal(
						nullHandlerDouble(bigDescimalToString(result.getBristanGroupDocumentDefinition()
								.getNetInvoiceTotal().toString().replaceAll("£", "").trim())));

				try {
					document.setDespatchDate(new SimpleDateFormat("yyyy/MM/dd")
							.parse(dateFormatter(result.getBristanGroupDocumentDefinition().getInvoiceDate())));

					document.setDocumentDate(new SimpleDateFormat("yyyy/MM/dd")
							.parse(dateFormatter(result.getBristanGroupDocumentDefinition().getInvoiceDate())));

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
				for (int i = 0; i < result.getBristanGroupDocumentDefinition().getInvoiceTable().size(); i++) {

					docdetails.setItemQty(nullHandlerInteger(
							result.getBristanGroupDocumentDefinition().getInvoiceTable().get(i).getQuantity()));
					docdetails.setItemDescription(result.getBristanGroupDocumentDefinition().getInvoiceTable().get(i)
							.getPartNumberDescription());
					docdetails.setUnitPrice(nullHandlerDouble(
							result.getBristanGroupDocumentDefinition().getInvoiceTable().get(i).getUnit()));
					docdetails.setValueOfGoods(nullHandlerDouble(result.getBristanGroupDocumentDefinition()
							.getInvoiceTable().get(i).getValue().replaceAll("£", "").trim()));

					docdetails.setTotalPrice(nullHandlerDouble(result.getBristanGroupDocumentDefinition()
							.getInvoiceTable().get(i).getValue().replaceAll("£", "").trim()));
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
