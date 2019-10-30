package io.naztech.nuxeoclient.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import io.naztech.nuxeoclient.service.DownloadFilesFromFtpServerService;

@Controller
public class DownloadFilesFromFtpController {
	@Autowired
	private DownloadFilesFromFtpServerService serv;
	
	//@Scheduled(fixedRate = 10000, initialDelay = 500)
	public void fileDownloadFromFtp() throws IOException {
		serv.filesDownloadFromFtp();
	}

}
