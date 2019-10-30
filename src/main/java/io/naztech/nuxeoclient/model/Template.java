package io.naztech.nuxeoclient.model;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Asadullah.galib
 * @since 2019-09-16
 **/
public class Template {

	private static Map<String, String> sql2BeanMap = null;
	private static Map<String, String> rs2BeanMap = null;

	private Integer templateId;
	private Integer templateVer;

	private Integer isSentToNuxeo;
	private Integer isTemplateParsed;
	private Integer isParsedSuccessful;
	private String docType;

	private String pdfType;
	private String processorType;
	private String clientName;
	private String supplierName;
	private String docSource;
	private String receivedDocId;
	
	/* Template Exception */
	private String errorIssue;
	private String errorIssueSource;

	private Date modifiedOn;

	/* Start getters and setters */

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Integer getTemplateVer() {
		return templateVer;
	}

	public void setTemplateVer(Integer templateVer) {
		this.templateVer = templateVer;
	}

	public Integer getIsSentToNuxeo() {
		return isSentToNuxeo;
	}

	public void setIsSentToNuxeo(Integer isSentToNuxeo) {
		this.isSentToNuxeo = isSentToNuxeo;
	}

	public Integer getIsTemplateParsed() {
		return isTemplateParsed;
	}

	public void setIsTemplateParsed(Integer isTemplateParsed) {
		this.isTemplateParsed = isTemplateParsed;
	}

	public Integer getIsParsedSuccessful() {
		return isParsedSuccessful;
	}

	public void setIsParsedSuccessful(Integer isParsedSuccessful) {
		this.isParsedSuccessful = isParsedSuccessful;
	}
	
	public void setDocType(String doctype) {
		this.docType=doctype;
	}
	
	public String getDocType() {
		return docType;
	}
	public String getPdfType() {
		return pdfType;
	}

	public void setPdfType(String pdfType) {
		this.pdfType = pdfType;
	}

	public String getProcessorType() {
		return processorType;
	}

	public void setProcessorType(String processorType) {
		this.processorType = processorType;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getDocSource() {
		return docSource;
	}

	public void setDocSource(String docSource) {
		this.docSource = docSource;
	}

	public String getReceivedDocId() {
		return receivedDocId;
	}

	public void setReceivedDocId(String receivedDocId) {
		this.receivedDocId = receivedDocId;
	}

	public String getErrorIssue() {
		return errorIssue;
	}

	public void setErrorIssue(String errorIssue) {
		this.errorIssue = errorIssue;
	}

	public String getErrorIssueSource() {
		return errorIssueSource;
	}

	public void setErrorIssueSource(String errorIssueSource) {
		this.errorIssueSource = errorIssueSource;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	/* End getters and setters */

	public static final Map<String, String> getSql2BeanMap() {

		if (sql2BeanMap == null) {
			sql2BeanMap = new LinkedHashMap<String, String>();

			sql2BeanMap.put("@id_template_key", "templateId");
			sql2BeanMap.put("@id_template_ver", "templateVer");
			sql2BeanMap.put("@is_send_to_nuxeo", "isSentToNuxeo");
			sql2BeanMap.put("@is_template_prepared", "isTemplateParsed");
			sql2BeanMap.put("@is_parsed_successfully", "isParsedSuccessful");
			sql2BeanMap.put("@tx_doc_type", "docType");
			sql2BeanMap.put("@tx_pdf_type", "pdfType");
			sql2BeanMap.put("@tx_processor_type", "processorType");
			sql2BeanMap.put("@tx_client_name", "clientName");
			sql2BeanMap.put("@tx_supplier_name", "supplierName");
			sql2BeanMap.put("@tx_doc_source", "docSource");
			sql2BeanMap.put("@tx_received_doc_id", "receivedDocId");
			sql2BeanMap.put("@dtt_mod", "modifiedOn");
			
			/* Template Exception */
			sql2BeanMap.put("@tx_error_issue", "errorIssue");
			sql2BeanMap.put("@tx_error_issue_source", "errorIssueSource");
		}

		return sql2BeanMap;
	}

	public static final Map<String, String> getRs2BeanMap() {

		if (rs2BeanMap == null) {
			rs2BeanMap = new LinkedHashMap<String, String>();

			rs2BeanMap.put("id_template_key", "templateId");
			rs2BeanMap.put("id_template_ver", "templateVer");
			rs2BeanMap.put("is_send_to_nuxeo", "isSentToNuxeo");
			rs2BeanMap.put("is_template_prepared", "isTemplateParsed");
			rs2BeanMap.put("is_parsed_successfully", "isParsedSuccessful");
			rs2BeanMap.put("tx_pdf_type", "pdfType");
			rs2BeanMap.put("tx_doc_type", "docType");
			rs2BeanMap.put("tx_processor_type", "processorType");
			rs2BeanMap.put("tx_client_name", "clientName");
			rs2BeanMap.put("tx_supplier_name", "supplierName");
			rs2BeanMap.put("tx_doc_source", "docSource");
			rs2BeanMap.put("tx_received_doc_id", "receivedDocId");
			rs2BeanMap.put("dtt_mod", "modifiedOn");
			
			/* Template Exception */
			rs2BeanMap.put("tx_error_issue", "errorIssue");
			rs2BeanMap.put("tx_error_issue_source", "errorIssueSource");
		}

		return rs2BeanMap;
	}

}