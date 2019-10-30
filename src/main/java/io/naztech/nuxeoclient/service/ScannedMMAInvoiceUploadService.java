//package io.naztech.nuxeoclient.service;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import javax.inject.Named;
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBElement;
//import javax.xml.bind.Unmarshaller;
//import javax.xml.stream.XMLInputFactory;
//import javax.xml.stream.XMLStreamReader;
//
//import org.nuxeo.client.NuxeoClient;
//import org.nuxeo.client.objects.Document;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import io.naztech.nuxeoclient.constants.Constants;
//import io.naztech.nuxeoclient.model.DocumentWrapper;
//import io.naztech.nuxeoclient.model.invoice.MMA.Documents;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author Asadullah.Galib
// * @since 2019-10-21
// **/
//
//@Named
//@Slf4j
//public class ScannedMMAInvoiceUploadService extends AbstractScannedInvoidUploadService {
//
//	@Autowired
//	private NuxeoClient nuxeoClient;
//	@Autowired
//	NuxeoClientService serv;
//
//	@Autowired
//	private TemplateService templateService;
//
//	@Autowired
//	private DocumentService documentService;
//
//	@Autowired
//	private DocDetailsService docDetailsService;
//
//	@Value("${import.festool.archiveFolder-path}")
//	private String archiveFolderPath;
//
//	@Value("${import.festool.badFolder-path}")
//	private String badfolderPath;
//
//	@Value("${import.festool.nuxeoFailedFolder-path}")
//	private String nuxeoFailedFolderPath;
//
//	@Value("${archive.enabled}")
//	private boolean enabled;
//
//	/**
//	 * Constructs a FestoolInvoice object from the xml file
//	 * 
//	 * @param file
//	 * @return FestoolInvoice Object
//	 * @throws IOException
//	 */
//	private Documents getInvoiceFromXml(File file) throws IOException {
//		try {
//			byte[] bytes = Files.readAllBytes(file.toPath());
//			Unmarshaller ums = JAXBContext.newInstance(Documents.class).createUnmarshaller();
//			XMLStreamReader reader = XMLInputFactory.newInstance()
//					.createXMLStreamReader(new ByteArrayInputStream(bytes));
//			JAXBElement<Documents> jaob = ums.unmarshal(reader, Documents.class);
//			Documents ob = jaob.getValue();
//			return ob;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new IOException("Error processing file.", e.getCause());
//		}
//
//	}
//
//	/**
//	 * Converts Invoice to DocumentRequest
//	 * 
//	 * @param inv
//	 * @return DocumentWrapper object
//	 */
//	private DocumentWrapper convertInvoiceToDocumentReq(Documents inv) {
//		if (inv == null)
//			return null;
//		try {
//			DocumentWrapper ob = DocumentWrapper.createWithName("Festool Invoice", "FESTOOL");
//			
////			inv.getMMADocumentDefinition().getAccountNumber()
////			inv.getMMADocumentDefinition().getCompanyAddress()
////			inv.getMMADocumentDefinition().getCompanyName()
////			inv.getMMADocumentDefinition().getCustomerAddress()
////			inv.getMMADocumentDefinition().getCustomerName()
////			inv.getMMADocumentDefinition().getEmail()
////			inv.getMMADocumentDefinition().getFaxNumber()
////			inv.getMMADocumentDefinition().getInsurancePremium()
////			inv.getMMADocumentDefinition().getPropertyAddress()
////			inv.getMMADocumentDefinition().getRentAdvance()
////			inv.getMMADocumentDefinition().getSupplierAddress()
////			inv.getMMADocumentDefinition().getSupplierName()
////			inv.getMMADocumentDefinition().getSupplierTelephoneNo()
////			inv.getMMADocumentDefinition().getTotalAmountDue()
////			inv.getMMADocumentDefinition().getVat()
////			inv.getMMADocumentDefinition().getVatNumber()
//
//
//			// Setting attributes of the document wrapper object
//			ob.setTitle("Festool");
//			ob.setDescription("Invoice");
//			ob.setPrefix("festool:");
//			ob.setRepoPath("/default-domain/workspaces/Sample Content/Invoice");
//			ob.addAttribute("client_name", inv.getMMADocumentDefinition().getCustomerName());
//			ob.addAttribute("company_address", inv.getMMADocumentDefinition().getSupplierAddress());
//		//	ob.addAttribute("currency", inv.getMMADocumentDefinition().getCurrency());
//			ob.addAttribute("customer_address", inv.getMMADocumentDefinition().getCustomerAddress());
//		//	ob.addAttribute("customer_number", Long.toString(inv.getMMADocumentDefinition().getCustomerNumber()));
//		//	ob.addAttribute("customer_order_no", Long.toString(inv.getMMADocumentDefinition().getOrderNo()));
//			ob.addAttribute("delivery_details", inv.getMMADocumentDefinition().getCustomerAddress());
//		//	ob.addAttribute("delivery_note_no", Long.toString(inv.getMMADocumentDefinition().getDeliveryNoteNo()));
//		//	ob.addAttribute("document_no", Long.toString(inv.getMMADocumentDefinition().getDocumentNumber()));
////			ob.addAttribute("invoice_amount",
////					bigDescimalToString(inv.getMMADocumentDefinition().getNetInvoiceTotal().toString()));
//		//	ob.addAttribute("invoice_date", inv.getMMADocumentDefinition().getInvoiceDate());
//		//	ob.addAttribute("invoice_number", Long.toString(inv.getMMADocumentDefinition().getCustomerNumber()));
////			ob.addAttribute("net_invoice_total",
////					bigDescimalToString(inv.getMMADocumentDefinition().getNetInvoiceTotal().toString()));
//			ob.addAttribute("supplier_name", inv.getMMADocumentDefinition().getSupplierName());
////			ob.addAttribute("value_of_goods",
////					bigDescimalToString(inv.getMMADocumentDefinition().getValueOfGoods().toString()));
//			ob.addAttribute("vat", bigDescimalToString(inv.getMMADocumentDefinition().getVat().toString()));
//			//ob.addAttribute("vat_number", inv.getMMADocumentDefinition().getVatRegNumber());
//			return ob;
//		} catch (Exception e) {
//			log.warn("Failed to convert to Document Wrapper Object", e);
//			return null;
//		}
//
//	}
//
//	/**
//	 * Uploads the file to the Nuxeo server and moves the file from hotfolder to
//	 * archive
//	 * 
//	 * @param invoiceInfoXml
//	 * @param attachments
//	 * @return request
//	 * @throws IOException
//	 */
//	@Override
//	public void uploadDocument(File invoiceInfoXml, List<File> attachments, boolean flag) throws IOException {
//		int isSuccess = 0;
//		LocalDate date = LocalDate.now();
//		String strDate = date.toString();
//
//		io.naztech.nuxeoclient.model.Template tmplt = new io.naztech.nuxeoclient.model.Template();
//		io.naztech.nuxeoclient.model.Document document = new io.naztech.nuxeoclient.model.Document();
//		io.naztech.nuxeoclient.model.DocDetails docdetails = new io.naztech.nuxeoclient.model.DocDetails();
//		if (enabled && flag) {
//			moveToArchive(archiveFolderPath, invoiceInfoXml, attachments, strDate);
//		}
//		serv.setNuxeoClient(nuxeoClient);
//		Documents result = null;
//
//		try {
//			result = getInvoiceFromXml(invoiceInfoXml);
//		} catch (IOException ex) {
//
//			tmplt.setErrorIssue(ex.getCause().toString());
//
//			log.error("Error parsing attachment: {}", ex.getMessage());
//		}
//
//		String name = attachments.get(0).getName();
//		if (name.contains("EMAIL")) {
//			tmplt.setDocSource("EMAIL");
//		} else if (name.contains("FTP")) {
//			tmplt.setDocSource("FTP");
//		}
//
//		tmplt.setPdfType("TEXT");
//		tmplt.setProcessorType("ABBYY");
//
//		if (null != result) {
//
//			tmplt.setClientName(result.getMMADocumentDefinition().getCustomerName());
////			tmplt.setReceivedDocId(
////					tmplt.getClientName() + "_" + Long.toString(result.getMMADocumentDefinition().getOrderNo()));
//
//			DocumentWrapper req = convertInvoiceToDocumentReq(result);
//			tmplt.setSupplierName(req.getTitle());
//
//			if (result.getMMADocumentDefinition().getCustomerName().equals(null))
////					&& result.getMMADocumentDefinition().getSupplierName().equals(null)
////					&& result.getMMADocumentDefinition().getNetInvoiceTotal().equals(null)) {
//
//				log.error(invoiceInfoXml.getName() + " Moved to Bad Folder");
//
//				moveToBadFolder(badfolderPath, invoiceInfoXml, attachments, strDate);
//
//				tmplt.setIsSentToNuxeo(0);
//				tmplt.setIsTemplateParsed(0);
//				tmplt.setIsParsedSuccessful(0);
//
//				isSuccess = 0;
//			} else {
//				isSuccess = 1;
//				document.setAccountNumber(Long.toString(result.getMMADocumentDefinition().getAccountNumber()));
//				// document.setAssociationNumber(valueToStringOrEmpty(result, "associationNo"));
//				// document.setSupplierNumber(nullHandlerInteger(valueToStringOrEmpty(result,
//				// "supplierNumber")));
//				document.setCompanyAddress(result.getMMADocumentDefinition().getSupplierAddress());
//				document.setContactNumber(result.getMMADocumentDefinition().getSupplierTelephoneNo());
//				//document.setCurrency(result.getMMADocumentDefinition().getCurrency());
//				document.setCustomerAddress(result.getMMADocumentDefinition().getCustomerAddress());
//				//document.setCustomerNumber(Long.toString(result.getMMADocumentDefinition().getCustomerNumber()));
//				//document.setCustomerOrderNo(Long.toString(result.getMMADocumentDefinition().getOrderNo()));
//				document.setDeliverTo(result.getMMADocumentDefinition().getCustomerAddress());
//				document.setDeliveryDetails(result.getMMADocumentDefinition().getCustomerAddress());
//				// document.setDeliveryNote(valueToStringOrEmpty(result, "deliveryNo"));
//			//	document.setDeliveryNoteNo(
//						//nullHandlerInteger(Long.toString(result.getMMADocumentDefinition().getOrderNo())));
//				document.setFaxNumber(result.getMMADocumentDefinition().getFaxNumber());
//				//document.setInvoiceAmount(nullHandlerDouble(
//						//bigDescimalToString(result.getMMADocumentDefinition().getNetInvoiceTotal().toString())));
//				//document.setInvoiceNumber(Long.toString(result.getMMADocumentDefinition().getCustomerNumber()));
//				document.setInvoiceTo(result.getMMADocumentDefinition().getCustomerAddress());
//				document.setVat(nullHandlerDouble(
//						bigDescimalToString(result.getMMADocumentDefinition().getVat().toString())));
//				//document.setTotalBeforeVat(nullHandlerDouble(
//					//	bigDescimalToString(result.getMMADocumentDefinition().getValueOfGoods().toString())));
//				//document.setVatNumber(result.getMMADocumentDefinition().getVatRegNumber());
//				//document.setNetInvoiceTotal(nullHandlerDouble(
//					//	bigDescimalToString(result.getMMADocumentDefinition().getNetInvoiceTotal().toString())));
//				// document.setVoucherNumber(valueToStringOrEmpty(result, "voucherNumber"));
//
//				try {
//					//document.setDespatchDate(new SimpleDateFormat("yyyy/MM/dd")
//						//	.parse(dateFormatter(result.getMMADocumentDefinition().getInvoiceDate())));
////					document.setDespatchDate(
////							new SimpleDateFormat("yyyy/MM/dd").parse(result.getFestoolDocumentDefinition().getInvoiceDate()));
//
////					document.setDocumentDate(new SimpleDateFormat("yyyy/MM/dd")
////							.parse(dateFormatter(result.getMMADocumentDefinition().getInvoiceDate())));
//					// document.setDocumentDate(
//					// new
//					// SimpleDateFormat("yyyy/MM/dd").parse(result.getFestoolDocumentDefinition().getInvoiceDate()));
//
//					// docDetails table
//
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//
//			// Uploading the file to Nuxeo Server
//			try {
//
//				if (isSuccess == 1) {
//
//					//req.setFiles(attachments);
//
//					//Document res = serv.createDocument(req);
//
//					if (res == null && flag) {
//
//						tmplt.setIsSentToNuxeo(0);
//
//						moveToNuxeoFailed(nuxeoFailedFolderPath, invoiceInfoXml, attachments, strDate);
//
//						log.error(attachments.get(0).getName() + " Moved to NuxeoFailed Folder");
//					} else {
//
//						tmplt.setIsSentToNuxeo(1);
//						tmplt.setIsTemplateParsed(1);
//						tmplt.setIsParsedSuccessful(1);
//
//						log.info("Successfully Uploaded Document Into Nuxeo: " + attachments.get(0).getName());
//						Files.delete(Paths.get(invoiceInfoXml.getAbsolutePath()));
//						for (File doc : attachments) {
//							Files.delete(Paths.get(doc.getAbsolutePath()));
//
//						}
//
//					}
//				}
//			} catch (Exception e) {
//				log.error("Moved to Nuxeo Failed ", e);
//
//				tmplt.setIsSentToNuxeo(0);
//
//				moveToNuxeoFailed(nuxeoFailedFolderPath, invoiceInfoXml, attachments, strDate);
//				tmplt.setErrorIssue(e.getMessage());
//				tmplt.setErrorIssueSource("NuxeoFailed");
//			}
//		}
//
//		Optional<io.naztech.nuxeoclient.model.Template> tmp = templateService
//				.processAction("NEW", tmplt, Constants.TEMPLATE).stream().findFirst();
//
//		if (tmp.isPresent()) {
//
//			Optional<io.naztech.nuxeoclient.model.Document> documentTemp = documentService
//					.processAction("NEW", document, Constants.DOC).stream().findFirst();
//
//			if (documentTemp.isPresent()) {
//
//				io.naztech.nuxeoclient.model.Document documentData = documentTemp.get();
//				documentData.setDocId(documentTemp.get().getDocId());
//				documentService.processAction("NEW", documentData, Constants.CUSTOMER);
//				for (int i = 0; i < result.getMMADocumentDefinition().getInvoiceTable().size(); i++) {
//					docdetails.setItemQty(nullHandlerInteger(
//							result.getMMADocumentDefinition().getInvoiceTable().get(i).getQty()));
//					docdetails.setPartNo(nullHandlerInteger(
//							result.getMMADocumentDefinition().getInvoiceTable().get(i).getPartNo()));
//					// docdetails.setReferenceNo(referenceNo);
////			docdetails.setTrade(trade);
//					docdetails.setUnitPrice(nullHandlerDouble(
//							result.getMMADocumentDefinition().getInvoiceTable().get(i).getUnitPrice()));
////			docdetails.setNetValue(netValue);
//					docdetails.setValueOfGoods(nullHandlerDouble(
//							result.getMMADocumentDefinition().getInvoiceTable().get(i).getTotal()));
////			docdetails.setInsurancePremium(insurancePremium);
////			docdetails.setItemCode(valueToStringOrEmpty(result, "itemCode"));
//					docdetails.setItemDescription(
//							result.getMMADocumentDefinition().getInvoiceTable().get(i).getDesciption());
////			docdetails.setItemName(valueToStringOrEmpty(result, "itemName"));
////			docdetails.setOrderId(valueToStringOrEmpty(result, "customerOrderNo"));
////			docdetails.setPack(valueToStringOrEmpty(result, "pack"));
////			docdetails.setPaymentDetails(valueToStringOrEmpty(result, "paymentDetails"));
////			docdetails.setPropertyAddress(valueToStringOrEmpty(result, "propertyAddress"));
////			docdetails.setRent(valueToStringOrEmpty(result, "rent"));
//
//					docdetails.setTotalPrice(nullHandlerDouble(
//							result.getMMADocumentDefinition().getInvoiceTable().get(i).getTotal()));
//					docdetails.setDocId(documentTemp.get().getDocId());
//					docDetailsService.processAction("NEW", docdetails, Constants.DOC_DETAILS);
//
//				}
//			}
//
//			else {
//				log.info("Document Data is not available");
//			}
//		}
//	}
//}
