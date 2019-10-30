package io.naztech.nuxeoclient.model;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Data;


/**
 * @author Abdullah.Kafi
 * @since 2019-09-17
 **/
@Data
public class Customer {
	
	private static Map<String, String> sql2BeanMap = null;
	private static Map<String, String> rs2BeanMap = null;
	
	private Integer customerId;
	private Integer customerVer;
	private String customerNumber;
	private String contactNumber;
	private String customerOrderNo;
	private String customerAddress;
	
	//foreign key (primary key of DOC table)
	private Integer docId;  
	
	private Date modifiedOn;
	
	
	public static final Map<String, String> getSql2BeanMap() {

		if (sql2BeanMap == null) {
			sql2BeanMap = new LinkedHashMap<String, String>();

			sql2BeanMap.put("@id_customer_key", "customerId");
			sql2BeanMap.put("@id_customer_ver", "customerVer");
			sql2BeanMap.put("@tx_customer_number", "customerNumber");
			sql2BeanMap.put("@tx_contact_number", "contactNumber");
			sql2BeanMap.put("@tx_customer_order_no", "customerOrderNo");
			sql2BeanMap.put("@tx_customer_address", "customerAddress");
			sql2BeanMap.put("@id_doc_key", "docId");
			sql2BeanMap.put("@dtt_mod", "modifiedOn");
		}

		return sql2BeanMap;
	}

	public static final Map<String, String> getRs2BeanMap() {

		if (rs2BeanMap == null) {
			rs2BeanMap = new LinkedHashMap<String, String>();

			rs2BeanMap.put("id_customer_key", "customerId");
			rs2BeanMap.put("id_customer_ver", "customerVer");
			rs2BeanMap.put("tx_customer_number", "customerNumber");
			rs2BeanMap.put("tx_contact_number", "contactNumber");
			rs2BeanMap.put("tx_customer_order_no", "customerOrderNo");
			rs2BeanMap.put("tx_customer_address", "customerAddress");
			rs2BeanMap.put("id_doc_key", "docId");
			rs2BeanMap.put("dtt_mod", "modifiedOn");
		}

		return rs2BeanMap;
	}
	
}
