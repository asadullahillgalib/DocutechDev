package io.naztech.nuxeoclient.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.nuxeo.client.NuxeoClient;
import org.nuxeo.client.objects.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.naztech.nuxeoclient.model.DocumentWrapper;
import io.naztech.nuxeoclient.model.invoice.owlett.OwlettInvoiceDocument;
import lombok.extern.slf4j.Slf4j;
@Service @Slf4j
public class ScannedOwlettInvoiceUploadService extends AbstractScannedInvoidUploadService {
	@Autowired
	private NuxeoClient nuxeoClient;
	@Autowired
	NuxeoClientService serv;

	@Value("${import.owlett.archiveFolder-path}")
	private String archiveFolderPath;

	@Value("${import.owlett.badFolder-path}")
	private String badfolderPath;

	@Value("${import.owlett.nuxeoFailedFolder-path}")
	private String nuxeoFailedFolderPath;

	@Value("${archive.enabled}")
	private boolean enabled;
	
	private OwlettInvoiceDocument getInvoiceFromXml(File file) {
		try {
			byte[] bytes = Files.readAllBytes(file.toPath());
			Unmarshaller ums = JAXBContext.newInstance(OwlettInvoiceDocument.class).createUnmarshaller();
			XMLStreamReader reader = XMLInputFactory.newInstance()
					.createXMLStreamReader(new ByteArrayInputStream(bytes));
			JAXBElement<OwlettInvoiceDocument> jaob = ums.unmarshal(reader, OwlettInvoiceDocument.class);
			OwlettInvoiceDocument ob = jaob.getValue();
			return ob;
		} catch (Exception e) {
			log.warn("Failed to parse XML", e);
			return null;
		}

	}
	
	private DocumentWrapper convertInvoiceToDocumentReq(OwlettInvoiceDocument inv) {
		if (inv == null)
			return null;
		try {
			DocumentWrapper ob = DocumentWrapper.createWithName("OwlettInvoice", "Owlett");

			// Setting attributes of the document wrapper object
			ob.setTitle("Owlett");
			ob.setDescription("");
			ob.setPrefix("owlett:");
			ob.setRepoPath("/default-domain/workspaces/Sample Content/Invoice");
			ob.addAttribute("invoice_no", inv.getOwlettDocumentDefinition().getInvoiceNo());
			ob.addAttribute("address", inv.getOwlettDocumentDefinition().getAddress());
			ob.addAttribute("customer_order_no", inv.getOwlettDocumentDefinition().getCustomerOrderNo());
			ob.addAttribute("date_of_invoice", inv.getOwlettDocumentDefinition().getDateOfInvoice());
			ob.addAttribute("tax_point_date", inv.getOwlettDocumentDefinition().getTaxPointDate());
			ob.addAttribute("invoice_total", inv.getOwlettDocumentDefinition().getInvoiceTotal().toString());
			ob.addAttribute("customer_ac_no", inv.getOwlettDocumentDefinition().getCustomerACNo());
			ob.addAttribute("despatch_date", inv.getOwlettDocumentDefinition().getDespatchDate());
			return ob;
		} catch (Exception e) {
			log.warn("Failed to convert to Document Wrapper Object", e);
			return null;
		}

	}
	
	@Override
	public void uploadDocument(File invoiceInfoXml, List<File> attachments, boolean flag) throws IOException {
		LocalDate date = LocalDate.now();
		String strDate = date.toString();
		if (enabled && flag) {
			moveToArchive(archiveFolderPath, invoiceInfoXml, attachments, strDate);
		}
		serv.setNuxeoClient(nuxeoClient);
		DocumentWrapper req = convertInvoiceToDocumentReq(getInvoiceFromXml(invoiceInfoXml));
		if (req == null) {
			log.error(invoiceInfoXml.getName() + " Moved to Bad Folder");
			moveToBadFolder(badfolderPath, invoiceInfoXml, attachments, strDate);
			return;
		}
		req.setFiles(attachments);
		// Uploading the file to Nuxeo Server
		try {
			Document res = serv.createDocument(req);

			if (res == null && flag) {
				log.error(invoiceInfoXml.getName() + " Moved to NuxeoFailed Folder");
				moveToNuxeoFailed(nuxeoFailedFolderPath, invoiceInfoXml, attachments, strDate);
				return;
			}
			log.info("Successfully Uploaded Document Into Nuxeo: " + invoiceInfoXml.getName());

		} catch (Exception e) {
			log.error("Moved to Nuxeo Failed ", e);
			moveToNuxeoFailed(nuxeoFailedFolderPath, invoiceInfoXml, attachments, strDate);
			return;

		}
		Files.delete(Paths.get(invoiceInfoXml.getAbsolutePath()));
		for (File doc : attachments) {
			Files.delete(Paths.get(doc.getAbsolutePath()));
		}

		// Move uploaded files to another directory
		return;
	}

}
