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
import io.naztech.nuxeoclient.model.invoice.ctech.Documents;
import lombok.extern.slf4j.Slf4j;

@Named
@Slf4j
public class ScannedCtechInvoiceUploadService extends AbstractScannedInvoidUploadService {
	@Autowired
	private NuxeoClient nuxeoClient;
	@Autowired
	NuxeoClientService serv;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private DocumentService documentService;

	@Value("${import.ctech.archiveFolder-path}")
	private String archiveFolderPath;

	@Value("${import.ctech.badFolder-path}")
	private String badfolderPath;

	@Value("${import.ctech.nuxeoFailedFolder-path}")
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
			DocumentWrapper ob = DocumentWrapper.createWithName("CtechInvoice", "CTEC");
			ob.setTitle("Ctech");
			ob.setDescription("Invoice");
			ob.setPrefix("ctec:");
			ob.setRepoPath("/default-domain/workspaces/Sample Content/Invoice");
			ob.addAttribute("account_number", inv.getCTECHDocumentDefinition().getAccountNumber());
			ob.addAttribute("client_name", inv.getCTECHDocumentDefinition().getCustomerName());
			ob.addAttribute("company_address", inv.getCTECHDocumentDefinition().getSupplierAddress());
			ob.addAttribute("contact_number", inv.getCTECHDocumentDefinition().getSupplierTelephoneNo());
			ob.addAttribute("customer_address", inv.getCTECHDocumentDefinition().getCustomerAddress());
			ob.addAttribute("email_address", inv.getCTECHDocumentDefinition().getEmail());
			ob.addAttribute("invoice_date", inv.getCTECHDocumentDefinition().getInvoiceDate());
			ob.addAttribute("payment_details",inv.getCTECHDocumentDefinition().getCustomerAddress());
			ob.addAttribute("reference_no", inv.getCTECHDocumentDefinition().getReferenceNo());
			ob.addAttribute("supplier_name", inv.getCTECHDocumentDefinition().getSupplierName());
			ob.addAttribute("total_amount_due", inv.getCTECHDocumentDefinition().getTotalAmountDue());
			
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
		String customerName=result.getCTECHDocumentDefinition().getCustomerName();

		if (null != result) {
			
			tmplt.setClientName(result.getCTECHDocumentDefinition().getCustomerName());
			tmplt.setReceivedDocId(tmplt.getClientName() + "_" + result.getCTECHDocumentDefinition().getReferenceNo());
			
			DocumentWrapper req = convertInvoiceToDocumentReq(result);
			tmplt.setSupplierName(req.getTitle());
			
			if (result.getCTECHDocumentDefinition().getCustomerName().equals(null)
					&& result.getCTECHDocumentDefinition().getSupplierName().equals(null)
					&& result.getCTECHDocumentDefinition().getTotalAmountDue().equals(null)) {
				log.error(invoiceInfoXml.getName() + " Moved to Bad Folder");
				moveToBadFolder(badfolderPath, invoiceInfoXml, attachments, strDate);
				tmplt.setIsSentToNuxeo(0);
				tmplt.setIsTemplateParsed(0);
				tmplt.setIsParsedSuccessful(0);
				isSuccess = 0;
			} else {
				isSuccess = 1;
				document.setCompanyAddress(result.getCTECHDocumentDefinition().getSupplierAddress());
				document.setSupplierNumber(Long.toString(result.getCTECHDocumentDefinition().getSupplierTelephoneNo()));
				document.setCustomerAddress(result.getCTECHDocumentDefinition().getCustomerAddress());
				document.setReferenceNo(Short.toString(result.getCTECHDocumentDefinition().getReferenceNo()));
				document.setAccountNumber(Long.toString(result.getCTECHDocumentDefinition().getAccountNumber()));
				document.setTotalAmountDue(nullHandlerDouble(
						result.getCTECHDocumentDefinition().getTotalAmountDue().toString().replaceAll(",", "").trim()));
				document.setNetInvoiceTotal(nullHandlerDouble(
						result.getCTECHDocumentDefinition().getTotalAmountDue().toString().replaceAll(",", "").trim()));
				document.setSupplierNumber(Long.toString(result.getCTECHDocumentDefinition().getSupplierTelephoneNo()));
				document.setDeliverTo(result.getCTECHDocumentDefinition().getCustomerAddress());
				document.setInvoiceTo(result.getCTECHDocumentDefinition().getCustomerAddress());
				document.setDeliveryDetails(result.getCTECHDocumentDefinition().getCustomerName());
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
			try {
				for (int i = 0; i < result.getCTECHDocumentDefinition().getInvoiceTable().size(); i++) {
					document.setInvoiceNumber(
							Long.toString(result.getCTECHDocumentDefinition().getInvoiceTable().get(i).getInvoice()));
					//System.out.println(result.getCTECHDocumentDefinition().getInvoiceTable().get(i).getDebit().replaceAll(",","").trim().split(".", 1)[1].trim());
					document.setInvoiceAmount(nullHandlerDouble(result.getCTECHDocumentDefinition().getInvoiceTable().get(i).getDebit().replaceAll(",","").trim()));
//					document.setInvoiceAmount(Double.parseDouble(result.getCTECHDocumentDefinition().getInvoiceTable()
//							.get(i).getDebit().toString().replaceAll(",", "").trim().toString()));
					document.setDespatchDate(new SimpleDateFormat("yyyy/MM/dd").parse(
							dateFormatter(result.getCTECHDocumentDefinition().getInvoiceTable().get(i).getDate())));
					document.setDocumentDate(new SimpleDateFormat("yyyy/MM/dd").parse(
							dateFormatter(result.getCTECHDocumentDefinition().getInvoiceTable().get(i).getDate())));
					document.setOrderId(result.getCTECHDocumentDefinition().getInvoiceTable().get(i).getPO());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Optional<io.naztech.nuxeoclient.model.Document> documentTemp = documentService
					.processAction("NEW", document, Constants.DOC).stream().findFirst();
			if (documentTemp.isPresent()) {
				io.naztech.nuxeoclient.model.Document documentData = documentTemp.get();
				documentData.setDocId(documentTemp.get().getDocId());
				documentService.processAction("NEW", documentData, Constants.CUSTOMER);
			}
		}
	}

}