package io.naztech.nuxeoclient.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import io.naztech.nuxeoclient.service.ScannedFestoolInvoiceUploadService;
import io.naztech.nuxeoclient.service.ScannedItextFestoolInvoiceService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ScannedFestoolInvoiceUploadController {

	@Autowired
	private ScannedFestoolInvoiceUploadService servFestool;
	
	@Autowired
	private ScannedItextFestoolInvoiceService servItextFestool;

	@Value("${import.festool.festoolFolder-path}")
	private String festoolFolderPath;

	@Value("${import.festool.nuxeoFailedFolder-path}")
	private String nuxeoFailedFolderPath;

	// Method for scanning Festool documents
	//@Scheduled(fixedRate = 10000, initialDelay = 500)
	public void uploadFestoolDocument() throws IOException {
		File[] fileList = new File(festoolFolderPath).listFiles();
		log.info("Scanning for files in Festool folder in schedule; " + fileList);
		if (fileList == null || fileList.length < 1)
			return;

		// Getting all the xml files in the hotfolder
		List<File> pdfList = Arrays.asList(fileList).stream()
				.filter(file -> "pdf".equals(FilenameUtils.getExtension(file.getName()))).collect(Collectors.toList());

		// Getting all the pdf files of the corresponding xml files in the hotfolder
		for (File pdf : pdfList) {
			servItextFestool.uploadDocument(Arrays.asList(pdf), true);
		}
	}
	
	@Scheduled(fixedRate = 20000, initialDelay = 500)
	public void uploadNMBSDocument() throws IOException {
		File[] fileList = new File(festoolFolderPath).listFiles();
		log.info("Scanning for files in Festool folder in schedule; " + fileList);
		if (fileList == null || fileList.length < 2)
		return;

		// Getting all the xml files in the hotfolder
		List<File> xmllist = Arrays.asList(fileList).parallelStream()
		.filter(file -> "xml".equals(FilenameUtils.getExtension(file.getName()))).collect(Collectors.toList());

		// Getting all the pdf files of the corresponding xml files in the hotfolder
		for (File xml : xmllist) {
		String pdf = xml.getAbsolutePath().replaceAll("\\.xml", "\\.pdf");
		File pdfFile = new File(pdf);
		if (pdfFile.exists())
			servFestool.uploadDocument(xml, Arrays.asList(pdfFile), true);

		}
	}
}
