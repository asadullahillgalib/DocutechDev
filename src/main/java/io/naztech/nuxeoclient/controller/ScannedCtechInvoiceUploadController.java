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

import io.naztech.nuxeoclient.service.ScannedCtechInvoiceUploadService;
import io.naztech.nuxeoclient.service.ScannedItextCtechInvoiceUploadService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ScannedCtechInvoiceUploadController {
	@Autowired
	private ScannedItextCtechInvoiceUploadService serv;

	@Autowired
	private ScannedCtechInvoiceUploadService servCtech;

	@Value("${import.ctech.ctechFolder-path}")
	private String ctechFolderPath;

	// Scheduler for itex
	// @Scheduled(fixedRate = 10000, initialDelay = 500)
	public void uploadNmbhDocument() throws IOException {
		File[] fileList = new File(ctechFolderPath).listFiles();
		log.info("Scanning for files in Bristan folder in schedule; " + fileList);
		if (fileList == null || fileList.length < 1)
			return;

		// Getting all the xml files in the hotfolder
		List<File> pdflist = Arrays.asList(fileList).parallelStream()
				.filter(file -> "pdf".equals(FilenameUtils.getExtension(file.getName()))).collect(Collectors.toList());

		// Getting all the pdf files of the corresponding xml files in the hotfolder
		for (File pdf : pdflist) {
			serv.uploadDocument(Arrays.asList(pdf), true);
		}
	}

	// Scheduler for abby
	@Scheduled(fixedRate = 20000, initialDelay = 500)
	public void uploadCtechDocument() throws IOException {
		File[] fileList = new File(ctechFolderPath).listFiles();
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
				servCtech.uploadDocument(xml, Arrays.asList(pdfFile), true);
		}
	}
}
