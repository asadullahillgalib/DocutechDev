package io.naztech.nuxeoclient.controller;

import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.xml.sax.SAXException;

import com.itextpdf.text.DocumentException;


import io.naztech.nuxeoclient.service.ImagePdfParserService;
@Controller
public class ApacheTikaController {
	@Autowired
	private ImagePdfParserService img;
	
	//@Scheduled(fixedRate = 10000)
	public void apacheTika() throws IOException, SAXException, TikaException, DocumentException {
		img.pdfParser();
	}

}
