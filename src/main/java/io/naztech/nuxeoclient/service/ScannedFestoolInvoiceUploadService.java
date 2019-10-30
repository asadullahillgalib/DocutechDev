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
import io.naztech.nuxeoclient.model.invoice.festool.FestoolInvoiceDocument;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Asadullah.Galib
 * @since 2019-09-29
 **/

@Named
@Slf4j
public class ScannedFestoolInvoiceUploadService extends AbstractScannedInvoidUploadService {

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

	@Value("${import.festool.archiveFolder-path}")
	private String archiveFolderPath;

	@Value("${import.festool.badFolder-path}")
	private String badfolderPath;

	@Value("${import.festool.nuxeoFailedFolder-path}")
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
	private FestoolInvoiceDocument getInvoiceFromXml(File file) throws IOException {
		try {
			byte[] bytes = Files.readAllBytes(file.toPath());
			Unmarshaller ums = JAXBContext.newInstance(FestoolInvoiceDocument.class).createUnmarshaller();
			XMLStreamReader reader = XMLInputFactory.newInstance()
					.createXMLStreamReader(new ByteArrayInputStream(bytes));
			JAXBElement<FestoolInvoiceDocument> jaob = ums.unmarshal(reader, FestoolInvoiceDocument.class);
			FestoolInvoiceDocument ob = jaob.getValue();
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
	private DocumentWrapper convertInvoiceToDocumentReq(FestoolInvoiceDocument inv) {
		if (inv == null)
			return null;
		try {
			DocumentWrapper ob = DocumentWrapper.createWithName("Festool Invoice", "FESTOOL");

			// Setting attributes of the document wrapper object
			ob.setTitle("Festool");
			ob.setDescription("Invoice");
			ob.setPrefix("festool:");
			ob.setRepoPath("/default-domain/workspaces/Sample Content/Invoice");
			ob.addAttribute("client_name", inv.getFestoolDocumentDefinition().getCustomerName());
			ob.addAttribute("company_address", inv.getFestoolDocumentDefinition().getSupplierAddress());
			ob.addAttribute("currency", inv.getFestoolDocumentDefinition().getCurrency());
			ob.addAttribute("customer_address", inv.getFestoolDocumentDefinition().getCustomerAddress());
			ob.addAttribute("customer_number", Long.toString(inv.getFestoolDocumentDefinition().getCustomerNumber()));
			ob.addAttribute("customer_order_no", Long.toString(inv.getFestoolDocumentDefinition().getOrderNo()));
			ob.addAttribute("delivery_details", inv.getFestoolDocumentDefinition().getCustomerAddress());
			ob.addAttribute("delivery_note_no", Long.toString(inv.getFestoolDocumentDefinition().getDeliveryNoteNo()));
			ob.addAttribute("document_no", Long.toString(inv.getFestoolDocumentDefinition().getDocumentNumber()));
			ob.addAttribute("invoice_amount",
					bigDescimalToString(inv.getFestoolDocumentDefinition().getNetInvoiceTotal().toString()));
			ob.addAttribute("invoice_date", inv.getFestoolDocumentDefinition().getInvoiceDate());
			ob.addAttribute("invoice_number", Long.toString(inv.getFestoolDocumentDefinition().getCustomerNumber()));
			ob.addAttribute("net_invoice_total",
					bigDescimalToString(inv.getFestoolDocumentDefinition().getNetInvoiceTotal().toString()));
			ob.addAttribute("supplier_name", inv.getFestoolDocumentDefinition().getSupplierName());
			ob.addAttribute("value_of_goods",
					bigDescimalToString(inv.getFestoolDocumentDefinition().getValueOfGoods().toString()));
			ob.addAttribute("vat", bigDescimalToString(inv.getFestoolDocumentDefinition().getVat().toString()));
			ob.addAttribute("vat_number", inv.getFestoolDocumentDefinition().getVatRegNumber());
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
		FestoolInvoiceDocument result = null;

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
		tmplt.setDocType("Invoice");

		if (null != result) {

			tmplt.setClientName(result.getFestoolDocumentDefinition().getCustomerName());
			tmplt.setReceivedDocId(
					tmplt.getClientName() + "_" + Long.toString(result.getFestoolDocumentDefinition().getOrderNo()));

			DocumentWrapper req = convertInvoiceToDocumentReq(result);
			tmplt.setSupplierName(req.getTitle());

			if (result.getFestoolDocumentDefinition().getCustomerName().equals(null)
					&& result.getFestoolDocumentDefinition().getSupplierName().equals(null)
					&& result.getFestoolDocumentDefinition().getNetInvoiceTotal().equals(null)) {

				log.error(invoiceInfoXml.getName() + " Moved to Bad Folder");

				moveToBadFolder(badfolderPath, invoiceInfoXml, attachments, strDate);

				tmplt.setIsSentToNuxeo(0);
				tmplt.setIsTemplateParsed(0);
				tmplt.setIsParsedSuccessful(0);

				isSuccess = 0;
			} else {
				isSuccess = 1;
				document.setAccountNumber(Long.toString(result.getFestoolDocumentDefinition().getCustomerNumber()));
				// document.setAssociationNumber(valueToStringOrEmpty(result, "associationNo"));
				// document.setSupplierNumber(nullHandlerInteger(valueToStringOrEmpty(result,
				// "supplierNumber")));
				document.setCompanyAddress(result.getFestoolDocumentDefinition().getSupplierAddress());
				document.setContactNumber(result.getFestoolDocumentDefinition().getSupplierTelephoneNo());
				document.setCurrency(result.getFestoolDocumentDefinition().getCurrency());
				document.setCustomerAddress(result.getFestoolDocumentDefinition().getCustomerAddress());
				document.setCustomerNumber(Long.toString(result.getFestoolDocumentDefinition().getCustomerNumber()));
				document.setCustomerOrderNo(Long.toString(result.getFestoolDocumentDefinition().getOrderNo()));
				document.setDeliverTo(result.getFestoolDocumentDefinition().getCustomerAddress());
				document.setDeliveryDetails(result.getFestoolDocumentDefinition().getCustomerAddress());
				// document.setDeliveryNote(valueToStringOrEmpty(result, "deliveryNo"));
				document.setDeliveryNoteNo(
						nullHandlerInteger(Long.toString(result.getFestoolDocumentDefinition().getOrderNo())));
				document.setFaxNumber(result.getFestoolDocumentDefinition().getFaxNumber());
				document.setInvoiceAmount(nullHandlerDouble(
						bigDescimalToString(result.getFestoolDocumentDefinition().getNetInvoiceTotal().toString())));
				document.setInvoiceNumber(Long.toString(result.getFestoolDocumentDefinition().getCustomerNumber()));
				document.setInvoiceTo(result.getFestoolDocumentDefinition().getCustomerAddress());
				document.setVat(nullHandlerDouble(
						bigDescimalToString(result.getFestoolDocumentDefinition().getVat().toString())));
				document.setTotalBeforeVat(nullHandlerDouble(
						bigDescimalToString(result.getFestoolDocumentDefinition().getValueOfGoods().toString())));
				document.setVatNumber(result.getFestoolDocumentDefinition().getVatRegNumber());
				document.setNetInvoiceTotal(nullHandlerDouble(
						bigDescimalToString(result.getFestoolDocumentDefinition().getNetInvoiceTotal().toString())));
				// document.setVoucherNumber(valueToStringOrEmpty(result, "voucherNumber"));

				try {
					document.setDespatchDate(new SimpleDateFormat("yyyy/MM/dd")
							.parse(dateFormatter(result.getFestoolDocumentDefinition().getInvoiceDate())));
//					document.setDespatchDate(
//							new SimpleDateFormat("yyyy/MM/dd").parse(result.getFestoolDocumentDefinition().getInvoiceDate()));

					document.setDocumentDate(new SimpleDateFormat("yyyy/MM/dd")
							.parse(dateFormatter(result.getFestoolDocumentDefinition().getInvoiceDate())));
					// document.setDocumentDate(
					// new
					// SimpleDateFormat("yyyy/MM/dd").parse(result.getFestoolDocumentDefinition().getInvoiceDate()));

					// docDetails table

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
				for (int i = 0; i < result.getFestoolDocumentDefinition().getInvoiceTable().size(); i++) {
					docdetails.setItemQty(nullHandlerInteger(
							result.getFestoolDocumentDefinition().getInvoiceTable().get(i).getQty()));
					docdetails.setPartNo(nullHandlerInteger(
							result.getFestoolDocumentDefinition().getInvoiceTable().get(i).getPartNo()));
					// docdetails.setReferenceNo(referenceNo);
//			docdetails.setTrade(trade);
					docdetails.setUnitPrice(nullHandlerDouble(
							result.getFestoolDocumentDefinition().getInvoiceTable().get(i).getUnitPrice()));
//			docdetails.setNetValue(netValue);
					docdetails.setValueOfGoods(nullHandlerDouble(
							result.getFestoolDocumentDefinition().getInvoiceTable().get(i).getTotal()));
//			docdetails.setInsurancePremium(insurancePremium);
//			docdetails.setItemCode(valueToStringOrEmpty(result, "itemCode"));
					docdetails.setItemDescription(
							result.getFestoolDocumentDefinition().getInvoiceTable().get(i).getDesciption());
//			docdetails.setItemName(valueToStringOrEmpty(result, "itemName"));
//			docdetails.setOrderId(valueToStringOrEmpty(result, "customerOrderNo"));
//			docdetails.setPack(valueToStringOrEmpty(result, "pack"));
//			docdetails.setPaymentDetails(valueToStringOrEmpty(result, "paymentDetails"));
//			docdetails.setPropertyAddress(valueToStringOrEmpty(result, "propertyAddress"));
//			docdetails.setRent(valueToStringOrEmpty(result, "rent"));

					docdetails.setTotalPrice(nullHandlerDouble(
							result.getFestoolDocumentDefinition().getInvoiceTable().get(i).getTotal()));
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
