package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.asprise.ocr.Ocr;
import com.asprise.util.pdf.PDFReader;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CompanyNameIdentificationService {
	private static String festoolInboxPath = "C:\\Users\\asadullah.galib\\hotfolder\\Festool\\";
	private static String nmbsInboxPath = "C:\\Users\\asadullah.galib\\hotfolder\\Nmbs\\";
	private static String oxgroupInboxpath = "C:\\Users\\asadullah.galib\\hotfolder\\ox\\";
	private static String mailInbox = "C:\\\\Users\\\\asadullah.galib\\\\hotfolder\\itext\\";
	private static String apsInboxPath = "C:\\Users\\asadullah.galib\\hotfolder\\Aps\\";
	private static String bristanInboxPath = "C:\\Users\\asadullah.galib\\hotfolder\\BristanGroup\\";
	private static String dpdInboxPath = "C:\\Users\\asadullah.galib\\hotfolder\\Dpd\\";
	private static String ctechInboxPath = "C:\\Users\\asadullah.galib\\hotfolder\\ctech\\";
	private static String blakladerInboxPath = "C:\\Users\\asadullah.galib\\hotfolder\\Blaklader\\";

	public void logoDetection() throws Exception {
		List<File> festoolAttachments = new ArrayList<File>();
		List<File> nmbsAttachments = new ArrayList<File>();
		List<File> oxAttachments = new ArrayList<File>();
		List<File> apsAttachments = new ArrayList<File>();
		List<File> bristanAttachments = new ArrayList<File>();
		List<File> dpdAttachments = new ArrayList<File>();
		List<File> ctechAttachments = new ArrayList<File>();
		List<File> blakladerAttachments = new ArrayList<File>();

		File[] fileList = new File(mailInbox).listFiles();
		log.info("Scanning for files in Itext Inbox folder");
		if (fileList == null || fileList.length < 1) {
			log.info("No New File in Itext inbox folder");
			return;
		}
		for (File pdf : fileList) {

			PDFReader reader = new PDFReader((new File(mailInbox + pdf.getName())));
			reader.open();
			int pages = reader.getNumberOfPages();
			for (int i = 0; i < pages; i++) {
				Ocr.setUp();
				Ocr ocr = new Ocr();
				ocr.startEngine("eng", Ocr.SPEED_FASTEST);
				String text = ocr.recognize(new File[] { new File(mailInbox + pdf.getName()) }, Ocr.RECOGNIZE_TYPE_ALL,
						Ocr.OUTPUT_FORMAT_PLAINTEXT, 0, null);
				if (text.contains("FEST-J].")) {
					festoolAttachments.add(pdf);
				}
				if (text.contains("INDEPENDENTS")) {
					nmbsAttachments.add(pdf);
				}

				if (text.contains("sales@oxgroup.co.uk")) {
					oxAttachments.add(pdf);
				}

				if (text.contains("BristanGroup")) {
					bristanAttachments.add(pdf);
				}

				if (text.contains("APS")) {
					apsAttachments.add(pdf);
				}

				if (reader.extractTextFromPage(i).contains("C-Tec London Ltd")) {
					ctechAttachments.add(pdf);
				}

				if (reader.extractTextFromPage(i).contains("Blaklader Workwear Ltd")) {
					blakladerAttachments.add(pdf);
				}

				if (reader.extractTextFromPage(i).contains("dpdlocal.co.uk")) {
					dpdAttachments.add(pdf);
				}

				ocr.stopEngine();

			}
			reader.close();
		}
		moveToFestoolFolderPath(festoolAttachments);
		moveToNMBSFolderPath(nmbsAttachments);
		moveToOXGroupFolderPath(oxAttachments);
		moveToAPSGroupFolderPath(apsAttachments);
		moveToDpdGroupFolderPath(dpdAttachments);
		moveToBristanGroupFolderPath(bristanAttachments);
		moveToBlakladerGroupFolderPath(blakladerAttachments);
		moveToCtechGroupFolderPath(ctechAttachments);

	}

	private void moveToFestoolFolderPath(List<File> attachments) {
		String festoolInbox = festoolInboxPath + File.separator;
		File abbDir = new File(festoolInbox);
		if (!abbDir.exists())
			abbDir.mkdir();

		for (File doc : attachments) {
			if (!new File(festoolInbox + File.separator + doc.getName()).exists())
				try {
					java.nio.file.Files.move(Paths.get(doc.getAbsolutePath()),
							Paths.get(festoolInbox + File.separator + doc.getName()),
							StandardCopyOption.REPLACE_EXISTING);
					log.info(doc.getName() + " File moved to festool Inbox");
				} catch (IOException e) {
					log.error(doc.getName() + " File couldn't move to festool Inbox" + e);
				}
		}
	}

	private void moveToNMBSFolderPath(List<File> attachments) {
		String nmbsInbox = nmbsInboxPath + File.separator;
		File itextDir = new File(nmbsInbox);
		if (!itextDir.exists())
			itextDir.mkdir();

		for (File doc : attachments) {
			if (!new File(nmbsInbox + File.separator + doc.getName()).exists())
				try {
					java.nio.file.Files.move(Paths.get(doc.getAbsolutePath()),
							Paths.get(nmbsInbox + File.separator + doc.getName()), StandardCopyOption.REPLACE_EXISTING);

					log.info(doc.getName() + " File moved to nmbs Inbox");

				} catch (IOException e) {

					log.error(doc.getName() + " File couldn't move to nmbs Inbox" + e);

				}
		}
	}

	private void moveToAPSGroupFolderPath(List<File> attachments) {
		String apsInbox = apsInboxPath + File.separator;
		File abbDir = new File(apsInbox);
		if (!abbDir.exists())
			abbDir.mkdir();

		for (File doc : attachments) {
			if (!new File(apsInbox + File.separator + doc.getName()).exists())
				try {
					java.nio.file.Files.move(Paths.get(doc.getAbsolutePath()),
							Paths.get(apsInbox + File.separator + doc.getName()), StandardCopyOption.REPLACE_EXISTING);
					log.info(doc.getName() + " File moved to APS Inbox");
				} catch (IOException e) {
					log.error(doc.getName() + " File couldn't move to APS Inbox" + e);
				}
		}
	}

	private void moveToDpdGroupFolderPath(List<File> attachments) {
		String dpdInbox = dpdInboxPath + File.separator;
		File abbDir = new File(dpdInbox);
		if (!abbDir.exists())
			abbDir.mkdir();

		for (File doc : attachments) {
			if (!new File(dpdInbox + File.separator + doc.getName()).exists())
				try {
					java.nio.file.Files.move(Paths.get(doc.getAbsolutePath()),
							Paths.get(dpdInbox + File.separator + doc.getName()), StandardCopyOption.REPLACE_EXISTING);
					log.info(doc.getName() + " File moved to DPD Inbox");
				} catch (IOException e) {
					log.error(doc.getName() + " File couldn't move to DPD Inbox" + e);
				}
		}
	}

	private void moveToBristanGroupFolderPath(List<File> attachments) {
		String bristanInbox = bristanInboxPath + File.separator;
		File abbDir = new File(bristanInbox);
		if (!abbDir.exists())
			abbDir.mkdir();

		for (File doc : attachments) {
			if (!new File(bristanInbox + File.separator + doc.getName()).exists())
				try {
					java.nio.file.Files.move(Paths.get(doc.getAbsolutePath()),
							Paths.get(bristanInbox + File.separator + doc.getName()),
							StandardCopyOption.REPLACE_EXISTING);
					log.info(doc.getName() + " File moved to Bristan Inbox");
				} catch (IOException e) {
					log.error(doc.getName() + " File couldn't move to Bristan Inbox" + e);
				}
		}
	}

	private void moveToBlakladerGroupFolderPath(List<File> attachments) {
		String blakladerInbox = blakladerInboxPath + File.separator;
		File abbDir = new File(blakladerInbox);
		if (!abbDir.exists())
			abbDir.mkdir();

		for (File doc : attachments) {
			if (!new File(blakladerInbox + File.separator + doc.getName()).exists())
				try {
					java.nio.file.Files.move(Paths.get(doc.getAbsolutePath()),
							Paths.get(blakladerInbox + File.separator + doc.getName()),
							StandardCopyOption.REPLACE_EXISTING);
					log.info(doc.getName() + " File moved to Blaklader Inbox");
				} catch (IOException e) {
					log.error(doc.getName() + " File couldn't move to Blaklader Inbox" + e);
				}
		}
	}

	private void moveToCtechGroupFolderPath(List<File> attachments) {
		String ctechInbox = ctechInboxPath + File.separator;
		File abbDir = new File(ctechInbox);
		if (!abbDir.exists())
			abbDir.mkdir();

		for (File doc : attachments) {
			if (!new File(ctechInbox + File.separator + doc.getName()).exists())
				try {
					java.nio.file.Files.move(Paths.get(doc.getAbsolutePath()),
							Paths.get(ctechInbox + File.separator + doc.getName()),
							StandardCopyOption.REPLACE_EXISTING);
					log.info(doc.getName() + " File moved to C-Tech Inbox");
				} catch (IOException e) {
					log.error(doc.getName() + " File couldn't move to C-Tech Inbox" + e);
				}
		}
	}

	private void moveToOXGroupFolderPath(List<File> attachments) {
		String oxInbox = oxgroupInboxpath + File.separator;
		File abbDir = new File(oxInbox);
		if (!abbDir.exists())
			abbDir.mkdir();

		for (File doc : attachments) {
			if (!new File(oxInbox + File.separator + doc.getName()).exists())
				try {
					java.nio.file.Files.move(Paths.get(doc.getAbsolutePath()),
							Paths.get(oxInbox + File.separator + doc.getName()), StandardCopyOption.REPLACE_EXISTING);
					log.info(doc.getName() + " File moved to OX Inbox");
				} catch (IOException e) {
					log.error(doc.getName() + " File couldn't move to OX Inbox" + e);
				}
		}
	}

}
