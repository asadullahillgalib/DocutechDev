package io.naztech.nuxeoclient.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.naztech.nuxeoclient.constants.ActionType;
import io.naztech.nuxeoclient.constants.Constants;
import io.naztech.nuxeoclient.model.DocDetails;
import io.naztech.nuxeoclient.model.Document;
import io.naztech.nuxeoclient.service.DocDetailsService;

/**
 * @author rokybul.rayhan
 * @since 2019-10-6
 */
@RestController
public class DocDetailsController {

	@Autowired
	DocDetailsService docDetailsService;

	private DocDetails docdetails;
	
	private Document docu;

	@GetMapping("/getDoc")
	public List<Document> getDoc() throws Exception {
		List<Document> list = docDetailsService.DocProcessAction(ActionType.SELECT.toString(), docu, Constants.DOC);
		return list;
	}

	@PostMapping("/docInsert")
	public List<Document> InsertDoc(@RequestBody Document docu) {
		List<Document> list = docDetailsService.DocProcessAction(ActionType.NEW.toString(), docu, Constants.DOC);
		return list;
	}

	@PutMapping("/docUpdate")
	public List<Document> UpdateDoc(@RequestBody Document docu) {
		List<Document> list = docDetailsService.DocProcessAction(ActionType.UPDATE.toString(), docu, Constants.DOC);
		return list;
	}

	@GetMapping("/getDocDetails")
	public List<DocDetails> getDocDetails() throws Exception {
		List<DocDetails> list = docDetailsService.processAction(ActionType.SELECT.toString(), docdetails,
				Constants.DOC_DETAILS);
		return list;
	}

	@PostMapping("/docDetailsInsert")
	public List<DocDetails> InsetDocDetails(@RequestBody DocDetails docDetails) {
		List<DocDetails> list = docDetailsService.processAction(ActionType.NEW.toString(), docDetails,
				Constants.DOC_DETAILS);
		return list;
	}

	@PutMapping("/docDetailsUpdate")
	public List<DocDetails> UpdateDocDetails(@RequestBody DocDetails docDetails) {
		List<DocDetails> list = docDetailsService.processAction(ActionType.UPDATE.toString(), docDetails,
				Constants.DOC_DETAILS);
		return list;
	}
}