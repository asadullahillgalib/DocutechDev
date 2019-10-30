package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import org.nuxeo.client.NuxeoClient;
import org.nuxeo.client.objects.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.duallab.pdf2data.Pdf2DataExtractor;
import com.duallab.pdf2data.result.ParsingResult;
import com.duallab.pdf2data.template.Template;
import com.itextpdf.licensekey.LicenseKey;

import io.naztech.nuxeoclient.model.DocumentWrapper;
import lombok.extern.slf4j.Slf4j;
@Named @Slf4j
public class ScannedItextOwlettInvoiceUploadService extends AbstractItextScannedInvoiceUploadService {
	@Autowired
	private NuxeoClient nuxeoClient;
	@Autowired
	NuxeoClientService serv;
	
	@Value("${import.template.owlettTemplate-path}")
	private String owlettTemplatePath;
	
	@Value("${import.owlett.archiveFolder-path}")
	private String archiveFolderPath;

	@Value("${import.owlett.badFolder-path}")
	private String badfolderPath;

	@Value("${import.owlett.nuxeoFailedFolder-path}")
	private String nuxeoFailedFolderPath;
	
	@Value("${import.owlett.owlettImageFolder-path}")
	private String owlettImageFolderPath;

	@Value("${archive.enabled}")
	private boolean enabled;
	

	/**
	 * Constructs a FestoolInvoice object from the xml file
	 * 
	 * @param file
	 * @return FestoolInvoice Object
	 * @throws IOException
	 */
	private ParsingResult getResultFromPdf(File file) {
		
		LicenseKey.loadLicenseFile(getClassLoader().getResource("itextkey1560918702869_0.xml").getPath());
		Template template;
		try {
			template = Pdf2DataExtractor.parseTemplateFromPDF(owlettTemplatePath);
			Pdf2DataExtractor extractor = new Pdf2DataExtractor(template);
			ParsingResult result = extractor.recognize(file.getAbsolutePath());
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
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
			DocumentWrapper ob = DocumentWrapper.createWithName("OwlettInvoice", "Owlett");

			// Setting attributes of the document wrapper object
			ob.setTitle("Owlett");
			ob.setDescription("");
			ob.setPrefix("owlett:");
			ob.setRepoPath("/default-domain/workspaces/Sample Content/Invoice");
			ob.addAttribute("invoice_no", valueToStringOrEmpty(inv, "InvoiceNo"));
			ob.addAttribute("address", valueToStringOrEmpty(inv, "Address"));
			ob.addAttribute("customer_order_no", valueToStringOrEmpty(inv, "CustomerOrderNo"));
			ob.addAttribute("date_of_invoice", valueToStringOrEmpty(inv, "DateOfInvoice"));
			ob.addAttribute("tax_point_date", valueToStringOrEmpty(inv, "TaxPointDate"));
			ob.addAttribute("invoice_total", valueToStringOrEmpty(inv, "InvoiceTotal"));
			ob.addAttribute("customer_ac_no", valueToStringOrEmpty(inv, "CustomerACNo"));
			ob.addAttribute("despatch_date", valueToStringOrEmpty(inv, "DespatchDate"));
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
		// TODO Auto-generated method stub
		LocalDate date = LocalDate.now();
		String strDate = date.toString();
		if (enabled && flag) {
			moveToArchive(archiveFolderPath, attachments, strDate);
		}
		serv.setNuxeoClient(nuxeoClient);
		DocumentWrapper req = convertInvoiceToDocumentReq(getResultFromPdf(attachments.get(0)));
		if (req == null) {
			log.error(attachments.get(0).getName() + " Moved to Bad Folder");
			moveToBadFolder(badfolderPath, attachments, strDate);
			return;
		}
		req.setFiles(Arrays.asList(new File(owlettImageFolderPath + File.separator + attachments.get(0).getName())));
		// Uploading the file to Nuxeo Server
		try {
			Document res = serv.createDocument(req);

			if (res == null && flag) {
				log.error(attachments.get(0).getName() + " Moved to NuxeoFailed Folder");
				moveToNuxeoFailed(nuxeoFailedFolderPath, attachments, strDate);
				return;
			}
			log.info("Successfully Uploaded Document Into Nuxeo: " + attachments.get(0).getName());

		} catch (Exception e) {
			log.error("Moved to Nuxeo Failed ", e);
			moveToNuxeoFailed(nuxeoFailedFolderPath, attachments, strDate);
			return;

		}
		for (File doc : attachments) {
			Files.delete(Paths.get(doc.getAbsolutePath()));
		}

		// Move uploaded files to another directory
		return;
		
	}
}

