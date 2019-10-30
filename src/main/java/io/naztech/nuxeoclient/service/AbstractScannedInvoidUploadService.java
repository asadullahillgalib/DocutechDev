package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j

public abstract class AbstractScannedInvoidUploadService {
	
	public abstract void uploadDocument(File invoiceInfoXml, List<File> attachments, boolean flag) throws IOException;
	
	protected void moveToArchive(String archiveFolderPath, File invoiceInfoXml, List<File> attachments, String strDate) throws IOException {
		String archive = archiveFolderPath + File.separator + strDate;
		File arcDir = new File(archive);
		if (!arcDir.exists())
			arcDir.mkdir();
		if (!new File(archive + File.separator + invoiceInfoXml.getName()).exists())
			Files.copy(Paths.get(invoiceInfoXml.getAbsolutePath()),
					Paths.get(archive + File.separator + invoiceInfoXml.getName()));

		for (File doc : attachments) {
			if (!new File(archive + File.separator + doc.getName()).exists())
				Files.copy(Paths.get(doc.getAbsolutePath()), Paths.get(archive + File.separator + doc.getName()));
		}
	}

	protected void moveToBadFolder(String badfolderPath, File invoiceInfoXml, List<File> attachments, String strDate) throws IOException {
		String badFolderPath = badfolderPath + File.separator + strDate;
		File badDir = new File(badFolderPath);
		if (!badDir.exists())
			badDir.mkdir();

		if (!new File(badFolderPath + File.separator + invoiceInfoXml.getName()).exists())
			Files.move(Paths.get(invoiceInfoXml.getAbsolutePath()),
					Paths.get(badFolderPath + File.separator + invoiceInfoXml.getName()),
					StandardCopyOption.REPLACE_EXISTING);

		for (File doc : attachments) {
			if (!new File(badFolderPath + File.separator + doc.getName()).exists())
				Files.move(Paths.get(doc.getAbsolutePath()), Paths.get(badFolderPath + File.separator + doc.getName()),
						StandardCopyOption.REPLACE_EXISTING);
		}
	}

	protected void moveToNuxeoFailed(String nuxeoFailedFolderPath, File invoiceInfoXml, List<File> attachments, String strDate) throws IOException {
		String nuxeoFailed = nuxeoFailedFolderPath + File.separator + strDate;
		File nuxDir = new File(nuxeoFailed);
		if (!nuxDir.exists())
			nuxDir.mkdir();

		if (!new File(nuxeoFailed + File.separator + invoiceInfoXml.getName()).exists())
			Files.move(Paths.get(invoiceInfoXml.getAbsolutePath()),
					Paths.get(nuxeoFailed + File.separator + invoiceInfoXml.getName()),
					StandardCopyOption.REPLACE_EXISTING);

		for (File doc : attachments) {
			if (!new File(nuxeoFailed + File.separator + doc.getName()).exists())
				Files.move(Paths.get(doc.getAbsolutePath()), Paths.get(nuxeoFailed + File.separator + doc.getName()),
						StandardCopyOption.REPLACE_EXISTING);
		}
	}
	
	protected String dateFormatter(String date) {
		if (date.length() < 10) {
		String[] parts = null;
		if (date.contains("/")) parts = date.split("/");
		else if (date.contains(".")) parts = date.split(".");
		else if (date.contains(" ")) parts = date.split(" ");

		if(parts.length > 2 && parts[2].trim().length() == 2) {
		date = parts[0] + "/" + parts[1] + "/20" + parts[2]; 
		}
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		List<SimpleDateFormat> knownPatterns = new ArrayList<SimpleDateFormat>();
		knownPatterns.add(new SimpleDateFormat("dd.MM.yyyy"));
		knownPatterns.add(new SimpleDateFormat("dd MMM yyyy"));
		knownPatterns.add(new SimpleDateFormat("dd MM yyyy"));
		knownPatterns.add(new SimpleDateFormat("dd/MM/yyyy"));
		Exception ex = null;
		for (SimpleDateFormat df : knownPatterns) {
		try {
		return formatter.format(df.parse(date)).toString();
		} catch (ParseException e) {
		ex = e;
		}
		}
		if (ex != null) log.warn("Failed to parse date " + date, ex);
		return "1970/01/01";
		}
	
	protected String bigDescimalToString(String bdValue) {
		BigDecimal bd=new BigDecimal(bdValue);
		if(bd.toString().equals(null)) {
			return "NULL";
		}
		else
		return bd.toString();
	}
	
	protected Double bigDecimalToDouble(BigDecimal bd) {
		if(bd!=null) {
		return bd.doubleValue();
		}
		else
			return 0.0;
	}
	
	protected Double nullHandlerDouble(String flt) {
		DecimalFormat df = new DecimalFormat("#.0");
		if (flt.equals(null)||flt.equals("")) {
			return (Double) 0.0;
		}
		else if(flt.equals("Each"))
			return 1.0;
		
		else {
			return Double.valueOf(df.format(Double.parseDouble(flt)));}
		
	}
	
	protected Integer nullHandlerInteger(String in) {
		if (in.equals(null)||in.equals("")) {
			return 0;
		}
		else
			return Integer.parseInt(in);
		
	}
}
