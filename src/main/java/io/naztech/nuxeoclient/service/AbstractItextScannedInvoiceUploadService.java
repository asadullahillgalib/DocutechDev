package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.duallab.pdf2data.result.ParsingResult;

public abstract class AbstractItextScannedInvoiceUploadService {
	public abstract void uploadDocument(List<File> attachments, boolean flag) throws IOException;

	protected void moveToArchive(String archiveFolderPath, List<File> attachments, String strDate) throws IOException {
		String archive = archiveFolderPath + File.separator + strDate;
		File arcDir = new File(archive);
		if (!arcDir.exists())
			arcDir.mkdir();
		for (File doc : attachments) {
			if (!new File(archive + File.separator + doc.getName()).exists())
				Files.copy(Paths.get(doc.getAbsolutePath()), Paths.get(archive + File.separator + doc.getName()));
		}
	}

	protected void moveToBadFolder(String badfolderPath, List<File> attachments, String strDate) throws IOException {
		String badFolderPath = badfolderPath + File.separator + strDate;
		File badDir = new File(badFolderPath);
		if (!badDir.exists())
			badDir.mkdir();

		for (File doc : attachments) {
			if (!new File(badFolderPath + File.separator + doc.getName()).exists())
				Files.move(Paths.get(doc.getAbsolutePath()), Paths.get(badFolderPath + File.separator + doc.getName()),
						StandardCopyOption.REPLACE_EXISTING);
		}
	}

	protected void moveToNuxeoFailed(String nuxeoFailedFolderPath, List<File> attachments, String strDate)
			throws IOException {
		String nuxeoFailed = nuxeoFailedFolderPath + File.separator + strDate;
		File nuxDir = new File(nuxeoFailed);
		if (!nuxDir.exists())
			nuxDir.mkdir();

		for (File doc : attachments) {
			if (!new File(nuxeoFailed + File.separator + doc.getName()).exists())
				Files.move(Paths.get(doc.getAbsolutePath()), Paths.get(nuxeoFailed + File.separator + doc.getName()),
						StandardCopyOption.REPLACE_EXISTING);
		}
	}

	protected ClassLoader getClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (SecurityException ex) {
		}

		if (cl == null)
			cl = ClassLoader.getSystemClassLoader();
		return cl;
	}

	protected String dateFormatter(String date) {

		List<SimpleDateFormat> knownPatterns = new ArrayList<SimpleDateFormat>();
		knownPatterns.add(new SimpleDateFormat("dd.MM.yyyy"));
		knownPatterns.add(new SimpleDateFormat("dd MMM yyyy"));
		// SimpleDateFormat parseFormat = new SimpleDateFormat("dd.MM.yyyy");
		// SimpleDateFormat parseFormat1 = new SimpleDateFormat("dd MMM yyyy");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

		for (SimpleDateFormat df : knownPatterns) {

			try {
				return formatter.format(df.parse(date)).toString();
			} catch (ParseException e) {

				e.printStackTrace();
				return "1970/01/01";

			}
		}

		return null;
	}

	protected String valueToStringOrEmpty(ParsingResult inv, String key) {
		Object value = inv.getResults(key);
		return value == null ? null : value.toString().replaceAll("\\[", "").replaceAll("\\]", "");
	}

	protected String valueToDateOrDefault(ParsingResult inv, String key) {
		Object value = inv.getResults(key);
		return value == null ? "1970/01/01" : value.toString().replaceAll("\\[", "").replaceAll("\\]", "");
	}
}
