package io.naztech.nuxeoclient.constants;

/**
 * @author Md. Mahbub Hasan Mohiuddin
 * @since 2019-09-03
 **/
public enum SPName {
	
	DOCUTECH("nDOCUTECH"),
	/* Stored procedure name */
	SEL_DOCUMENT("SEL_document"),
	ACT_TEMPLATE("ACT_template"),
	ACT_TEMPLATE_EXCEPTION("ACT_template_exception"),
	ACT_CUSTOMER("ACT_customer"),
	ACT_DOC("ACT_doc"),
	ACT_DOC_DETAILS("ACT_doc_details"),
	SEL_DOC_DETAILS("SEL_doc_details"),
	SEL_DOC("SEL_doc"),
	
	/* Resultset type */
	RS_TYPE_DOCUMENT("RS_TYPE_DOCUMENT"),
	RS_TYPE_TEMPLATE_EXCEPTION("RS_TYPE_TEMPLATE_EXCEPTION"),
	RS_TYPE_TEMPLATE("RS_TYPE_TEMPLATE"),
	RS_TYPE_CUSTOMER("RS_TYPE_CUSTOMER"),
	RS_TYPE_DOC("RS_TYPE_DOC"),
	RS_TYPE_DOC_DETAILS("RS_TYPE_DOC_DETAILS");

	private final String storeProcedureName;

	private SPName(String storeProcedureName) {
		this.storeProcedureName = storeProcedureName;
	}

	@Override
	public String toString() {
		return storeProcedureName;
	}

}
