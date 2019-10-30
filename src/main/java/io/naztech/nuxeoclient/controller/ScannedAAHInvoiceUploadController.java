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

import io.naztech.nuxeoclient.service.ScannedItextAAHInvoiceUploadService;
import lombok.extern.slf4j.Slf4j;

@Controller @Slf4j
public class ScannedAAHInvoiceUploadController {
	
	@Autowired
	private ScannedItextAAHInvoiceUploadService service;
	
	@Value("${import.aah.aahFolder-path}")
	private String aahFolderPath;

	@Value("${import.aah.nuxeoFailedFolder-path}")
	private String nuxeoFailedFolderPath;
	

	
	//@Scheduled(fixedRate = 10000, initialDelay = 500)
	public void uploadAAHImageDocument() throws IOException {
		File[] fileList = new File(aahFolderPath).listFiles();
		log.info("Scanning for files in AAH folder in schedule; " + fileList);
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
