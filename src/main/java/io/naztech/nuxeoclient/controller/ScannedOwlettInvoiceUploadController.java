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

import io.naztech.nuxeoclient.service.ScannedItextOwlettInvoiceUploadService;
import io.naztech.nuxeoclient.service.ScannedOwlettInvoiceUploadService;
import lombok.extern.slf4j.Slf4j;

@Controller @Slf4j
public class ScannedOwlettInvoiceUploadController {
	@Autowired
	private ScannedOwlettInvoiceUploadService servOwlett;
	
	@Autowired
	private ScannedItextOwlettInvoiceUploadService service;
	
	@Value("${import.owlett.owlettFolder-path}")
	private String owlettFolderPath;

	@Value("${import.owlett.nuxeoFailedFolder-path}")
	private String nuxeoFailedFolderPath;
	
	//@Scheduled(fixedRate = 10000, initialDelay = 500)
	public void uploadOwlettDocument() throws IOException {
		File[] fileList = new File(owlettFolderPath).listFiles();
		log.info("Scanning for files in Owlett folder in schedule; " + fileList);
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
		servOwlett.uploadDocument(xml, Arrays.asList(pdfFile), true);

		}
	}
	//@Scheduled(fixedRate = 10000, initialDelay = 500)
	public void uploadOwlettImageDocument() throws IOException {
		File[] fileList = new File(owlettFolderPath).listFiles();
		log.info("Scanning for files in Nmbs folder in schedule; " + fileList);
		if (fileList == null || fileList.length < 1)
			return;

		// Getting all the xml files in the hotfolder
		List<File> pdflist = Arrays.asList(fileList).parallelStream()
				.filter(file -> "pdf".equals(FilenameUtils.getExtension(file.getName()))).collect(Collectors.toList());

		// Getting all the pdf files of the corresponding xml files in the hotfolder
		for (File pdf : pdflist) {

			service.uploadDocument(Arrays.asList(pdf), true);

		}
	}
}
