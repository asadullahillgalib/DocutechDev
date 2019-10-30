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
import io.naztech.nuxeoclient.service.ScannedItextOxGroupInvoiceUploadService;
import io.naztech.nuxeoclient.service.ScannedOxInvoiceUploadService;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
public class ScannedOXInvoiceUploadController {
	@Autowired
	private ScannedItextOxGroupInvoiceUploadService serv;
	
	@Autowired
	private ScannedOxInvoiceUploadService service;

	@Value("${import.ox.oxFolder-path}")
	private String oxFolderPath;

	//@Scheduled(fixedRate = 10000, initialDelay = 500)
	public void uploadNmbhDocument() throws IOException {
		File[] fileList = new File(oxFolderPath).listFiles();
		log.info("Scanning for files in OX folder in schedule; " + fileList);
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
	
	@Scheduled(fixedRate = 20000, initialDelay = 500)
	public void uploadNMBSDocument() throws IOException {
		File[] fileList = new File(oxFolderPath).listFiles();
		log.info("Scanning for files in OX folder in schedule; " + fileList);
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
			service.uploadDocument(xml, Arrays.asList(pdfFile), true);

		}
	}
}
