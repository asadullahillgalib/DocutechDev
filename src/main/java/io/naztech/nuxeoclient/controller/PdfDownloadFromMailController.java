package io.naztech.nuxeoclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import io.naztech.nuxeoclient.service.AttachmentDownloadFromMailService;

@Controller
public class PdfDownloadFromMailController {
	@Autowired
	private AttachmentDownloadFromMailService serv;

	//@Scheduled(fixedRate = 10000, initialDelay = 500)
	public void downloadAttachments() {

		try {
			serv.downloadAttachments();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
