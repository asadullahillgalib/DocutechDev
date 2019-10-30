package io.naztech.nuxeoclient.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.qoppa.pdf.PDFException;

import io.naztech.nuxeoclient.service.OCRImageRecognitionService;

@Controller
public class OCRImageRecognitionController {
	@Autowired
	private OCRImageRecognitionService serv;
	
	//@Scheduled(fixedRate = 10000, initialDelay = 500)
	public void OCRImageRecognition() throws PDFException, IOException {
		serv.imageRecognition();
	}

}
