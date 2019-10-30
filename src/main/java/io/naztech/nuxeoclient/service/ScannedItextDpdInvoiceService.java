package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.inject.Named;

import org.nuxeo.client.NuxeoClient;
import org.nuxeo.client.objects.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.duallab.pdf2data.Pdf2DataExtractor;
import com.duallab.pdf2data.result.ParsingResult;
import com.duallab.pdf2data.template.Template;
import com.itextpdf.licensekey.LicenseKey;

import io.naztech.nuxeoclient.constants.Constants;
import io.naztech.nuxeoclient.model.DocumentWrapper;
import lombok.extern.slf4j.Slf4j;

@Named
@Slf4j
public class ScannedItextDpdInvoiceService extends AbstractItextScannedInvoiceUploadService {

	@Autowired
	private NuxeoClient nuxeoClient;
	@Autowired
	NuxeoClientService serv;

	@Autowired
	private DocumentService documentService;

	@Autowired
	private TemplateService templateService;

	@Value("${import.template.dpdTemplate-path}")
	private String dpdTemplatePath;

	@Value("${import.dpd.archiveFolder-path}")
	private String archiveFolderPath;

	@Value("${import.dpd.badFolder-path}")
	private String badfolderPath;

	@Value("${import.dpd.nuxeoFailedFolder-path}")
	private String nuxeoFailedFolderPath;

	@Value("${archive.enabled}")
	private boolean enabled;

	/**
	 * Constructs a NmbsInvoice object from the xml file
	 * 
	 * @param file
	 * @return NmbsInvoice Object
	 * @throws IOException
	 */
	private ParsingResult getResultFromPdf(File file) throws IOException {

		LicenseKey.loadLicenseFile(getClassLoader().getResource("itextkey1567936395276_0.xml").getPath());
		Template template;
		try {
			template = Pdf2DataExtractor.parseTemplateFromPDF(dpdTemplatePath);
			Pdf2DataExtractor extractor = new Pdf2DataExtractor(template);
			ParsingResult result = extractor.recognize(file.getAbsolutePath());
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	private DocumentWrapper convertInvoiceToDocumentReq(ParsingResult inv) {
		if (inv == null)
			return null;
		try {
			DocumentWrapper ob = DocumentWrapper.createWithName("Dpd", "DPD");

			// Setting attributes of the document wrapper object
			ob.setTitle("Dpd");
			ob.setDescription("Invoice");
			ob.setPrefix("dpd:");
			ob.setRepoPath("/default-domain/workspaces/Sample Content/Invoice");
			ob.addAttribute("account_no", valueToStringOrEmpty(inv, "AccountNo"));
			ob.addAttribute("customer_address", valueToStringOrEmpty(inv, "CustomerAddress"));
			ob.addAttribute("customer_name", valueToStringOrEmpty(inv, "CustomerName"));
			ob.addAttribute("email", valueToStringOrEmpty(inv, "Email"));
			ob.addAttribute("invoice_date", valueToStringOrEmpty(inv, "InvoiceDate"));
			ob.addAttribute("invoice_no", valueToStringOrEmpty(inv, "InvoiceNo"));
			ob.addAttribute("payment_account_no", valueToStringOrEmpty(inv, "PaymentAccountNo"));
			ob.addAttribute("sort_code", valueToStringOrEmpty(inv, "SortCode"));
			ob.addAttribute("total_remittance", valueToStringOrEmpty(inv, "TotalRemittance"));
			ob.addAttribute("vat_no", valueToStringOrEmpty(inv, "VatNumber"));

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
	public void uploadDocument(List<File> attachments, boolean flag) throws IOException {
		int isSuccess = 0;
		String resultErrors = null;
		LocalDate date = LocalDate.now();
		String strDate = date.toString();

		io.naztech.nuxeoclient.model.Template tmplt = new io.naztech.nuxeoclient.model.Template();
		io.naztech.nuxeoclient.model.Document document = new io.naztech.nuxeoclient.model.Document();

		if (enabled && flag) {
			moveToArchive(archiveFolderPath, attachments, strDate);
		}

		serv.setNuxeoClient(nuxeoClient);

		ParsingResult result = null;

		try {
			result = getResultFromPdf(attachments.get(0));
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
		tmplt.setProcessorType("iTEXT");

		if (null != result) {

			try {
				resultErrors = getErrorsMessage(result, "errorsMessage");
			} catch (NoSuchFieldException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}

			if (null != resultErrors) {
				tmplt.setErrorIssue(resultErrors);
			}

			tmplt.setClientName(valueToStringOrEmpty(result, "clientName"));
			tmplt.setReceivedDocId(tmplt.getClientName() + "_" + valueToStringOrEmpty(result, "invoiceNumber"));

			DocumentWrapper req = convertInvoiceToDocumentReq(result);
			tmplt.setSupplierName(req.getTitle());

			if (result.getErrorsNumber() >= 3) {

				log.error(attachments.get(0).getName() + " Moved to Bad Folder");

				moveToBadFolder(badfolderPath, attachments, strDate);

				tmplt.setIsSentToNuxeo(0);
				tmplt.setIsTemplateParsed(0);
				tmplt.setIsParsedSuccessful(0);

				isSuccess = 0;
			} else {
				isSuccess = 1;
				document.setAccountNumber(valueToStringOrEmpty(result, "accountNumber"));
				document.setAssociationNumber(valueToStringOrEmpty(result, "associationNo"));
				// document.setSupplierNumber(Integer.parseInt(valueToStringOrEmpty(result,
				// "supplierNumber")));
				document.setCompanyAddress(valueToStringOrEmpty(result, "companyAddress"));
				document.setContactNumber(valueToStringOrEmpty(result, "contactNumber"));
				document.setCurrency(valueToStringOrEmpty(result, "currency"));
				document.setCustomerAddress(valueToStringOrEmpty(result, "customerAddress"));
				document.setCustomerNumber(valueToStringOrEmpty(result, "customerNo"));
				document.setCustomerOrderNo(valueToStringOrEmpty(result, "customerOrderNo"));
				document.setDeliverTo(valueToStringOrEmpty(result, "customerAddress"));
				document.setDeliveryDetails(valueToStringOrEmpty(result, "customerAddress"));
				document.setDeliveryNote(valueToStringOrEmpty(result, "deliveryNo"));
				// document.setDeliveryNoteNo(Integer.parseInt(valueToStringOrEmpty(result,
				// "deliveryNoteNo")));
				document.setFaxNumber(valueToStringOrEmpty(result, "faxNumber"));
				// document.setInvoiceAmount(Double.parseDouble(valueToStringOrEmpty(result,
				// "invoiceAmount")));
				document.setInvoiceNumber(valueToStringOrEmpty(result, "supplierNumber"));
				document.setInvoiceTo(valueToStringOrEmpty(result, "invoiceTo"));
				document.setItemCode(valueToStringOrEmpty(result, "itemCode"));
				document.setItemDescription(valueToStringOrEmpty(result, "itemDescription"));
				document.setItemName(valueToStringOrEmpty(result, "itemName"));
				if (valueToStringOrEmpty(result, "netInvoiceTotal").isEmpty()) {
					document.setNetInvoiceTotal(0.0);
				} else {
					document.setNetInvoiceTotal(Double.parseDouble(valueToStringOrEmpty(result, "netInvoiceTotal")));
				}
				document.setOrderId(valueToStringOrEmpty(result, "customerOrderNo"));
				document.setPack(valueToStringOrEmpty(result, "pack"));
				document.setPaymentDetails(valueToStringOrEmpty(result, "paymentDetails"));
				document.setPropertyAddress(valueToStringOrEmpty(result, "propertyAddress"));
				document.setRent(valueToStringOrEmpty(result, "rent"));
				// document.setTotalBeforeVat(Double.parseDouble(valueToStringOrEmpty(result,
				// "totalBeforeVat")));
				if (valueToStringOrEmpty(result, "netInvoiceTotal").isEmpty()) {
					document.setTotalPrice(0.0);
				} else {
					document.setTotalPrice(Double.parseDouble(valueToStringOrEmpty(result, "netInvoiceTotal")));
				}
				if (valueToStringOrEmpty(result, "vat").isEmpty()) {
					document.setVat(0.0);
					document.setVatPayable(0.0);
				} else {
					document.setVat(Double.parseDouble(valueToStringOrEmpty(result, "vat")));
					document.setVatPayable(Double.parseDouble(valueToStringOrEmpty(result, "vat")));
				}

				// document.setVatNumber(Integer.parseInt(valueToStringOrEmpty(result,
				// "vatNumber")));
				document.setVoucherNumber(valueToStringOrEmpty(result, "voucherNumber"));

				try {
					document.setDespatchDate(
							new SimpleDateFormat("yyyy/MM/dd").parse(valueToDateOrDefault(result, "despatchDate")));
					document.setDocumentDate(new SimpleDateFormat("yyyy/MM/dd")
							.parse(dateFormatter(valueToDateOrDefault(result, "invoiceDate"))));
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

						moveToNuxeoFailed(nuxeoFailedFolderPath, attachments, strDate);

						log.error(attachments.get(0).getName() + " Moved to NuxeoFailed Folder");
					} else {

						tmplt.setIsSentToNuxeo(1);
						tmplt.setIsTemplateParsed(1);
						tmplt.setIsParsedSuccessful(1);

						log.info("Successfully Uploaded Document Into Nuxeo: " + attachments.get(0).getName());

						for (File doc : attachments) {
							Files.delete(Paths.get(doc.getAbsolutePath()));
						}
					}
				}
			} catch (Exception e) {
				log.error("Moved to Nuxeo Failed ", e);

				tmplt.setIsSentToNuxeo(0);

				moveToNuxeoFailed(nuxeoFailedFolderPath, attachments, strDate);
				tmplt.setErrorIssue(e.getMessage());
				tmplt.setErrorIssueSource("NuxeoFailed");
			}
		}

		Optional<io.naztech.nuxeoclient.model.Template> tmp = templateService
				.processAction("NEW", tmplt, Constants.TEMPLATE).stream().findFirst();

		if (tmp.isPresent()) {
			io.naztech.nuxeoclient.model.Template data = tmp.get();
			// documentService.processAction("NEW", document, Constants.DOC);
			Optional<io.naztech.nuxeoclient.model.Document> documentTemp = documentService
					.processAction("NEW", document, Constants.DOC).stream().findFirst();

			if (documentTemp.isPresent()) {

				io.naztech.nuxeoclient.model.Document documentData = documentTemp.get();
				documentData.setDocId(documentTemp.get().getDocId());
				documentService.processAction("NEW", documentData, Constants.CUSTOMER);
				documentService.processAction("NEW", documentData, Constants.DOC_DETAILS);
			}

			if ((isSuccess == 0) && (null != resultErrors)) {

				data.setErrorIssue(tmplt.getErrorIssue());
				data.setErrorIssueSource("ParseFailed");

				templateService.processAction("NEW", data, Constants.EXCEPTION);
			}
		}

		// Move uploaded files to another directory
		return;
	}

	// Access private field by using filed name.
	private String getErrorsMessage(ParsingResult book, String fieldName)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		Field field = book.getClass().getDeclaredField(fieldName);
		if (Modifier.isPrivate(field.getModifiers())) {
			field.setAccessible(true);
			log.info(fieldName + " : " + field.get(book));

			return field.get(book).toString();
		}

		return null;
	}

}
