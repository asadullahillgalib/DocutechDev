package io.naztech.nuxeoclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import io.naztech.nuxeoclient.service.CompanyNameIdentificationService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class CompanyNameIdentificationController {
	@Autowired
	private CompanyNameIdentificationService serv;
	
	//@Scheduled(fixedRate = 300000)
	public void companyIdentification() {
		try {
			serv.logoDetection();
		} catch (Exception e) {
			log.error("Has Exceptions"+e);
		}
	}

}
