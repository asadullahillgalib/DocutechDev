package io.naztech.nuxeoclient.constants;

/**
 * @author Md. Mahbub Hasan Mohiuddin
 * @since 2019-09-03
 **/
public enum ContentType {

	MULTI("MULTI"),
	STATUS("STATUS"),
	MULTI_MESSAGE("MULTI_MESSAGE");

	private final String contentTypeName;

	private ContentType(String contentTypeName) {
		this.contentTypeName = contentTypeName;
	}

	@Override
	public String toString() {
		return contentTypeName;
	}
}
