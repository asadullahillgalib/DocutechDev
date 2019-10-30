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
public class DocDetails {
	
	private static Map<String, String> sql2BeanMap = null;
	private static Map<String, String> rs2BeanMap = null;
	
	private Integer docDetailsId;
	private Integer docDetailsVer;
	private Integer partNo;
	private Integer itemQty;
	private Integer referenceNo;
	private String itemName;
	private String itemCode;
	private String rent;
	private String pack;
	private String itemDescription;
	private String propertyAddress;
	private Double trade;
	private Double unitPrice;
	private Double totalPrice;
	private Double netValue;
	private Double valueOfGoods;
	private Double insurancePremium;
	
	
	//foreign key (primary key of DOC table)
	private Integer docId;  
	
	private Date modifiedOn;
	
	
	public static final Map<String, String> getSql2BeanMap() {

		if (sql2BeanMap == null) {
			sql2BeanMap = new LinkedHashMap<String, String>();

			sql2BeanMap.put("@id_doc_details_key", "docDetailsId");
			sql2BeanMap.put("@id_doc_details_ver", "docDetailsVer");
			sql2BeanMap.put("@part_no", "partNo");
			sql2BeanMap.put("@item_qty", "itemQty");
			sql2BeanMap.put("@reference_no", "referenceNo");
			sql2BeanMap.put("@tx_item_name", "itemName");
			sql2BeanMap.put("@tx_item_code", "itemCode");
			sql2BeanMap.put("@tx_rent", "rent");
			sql2BeanMap.put("@tx_pack", "pack");
			sql2BeanMap.put("@tx_item_description", "itemDescription");
			sql2BeanMap.put("@tx_property_address", "propertyAddress");
			sql2BeanMap.put("@flt_trade", "trade");
			sql2BeanMap.put("@flt_unit_price", "unitPrice");
			sql2BeanMap.put("@flt_total_price", "totalPrice");
			sql2BeanMap.put("@flt_net_value", "netValue");
			sql2BeanMap.put("@flt_value_of_goods", "valueOfGoods");
			sql2BeanMap.put("@flt_insurance_premium", "insurancePremium");
			sql2BeanMap.put("@id_doc_key", "docId");
			sql2BeanMap.put("@dtt_mod", "modifiedOn");
		}

		return sql2BeanMap;
	}

	public static final Map<String, String> getRs2BeanMap() {

		if (rs2BeanMap == null) {
			rs2BeanMap = new LinkedHashMap<String, String>();

			rs2BeanMap.put("id_doc_details_key", "docDetailsId");
			rs2BeanMap.put("id_doc_details_ver", "docDetailsVer");
			rs2BeanMap.put("part_no", "partNo");
			rs2BeanMap.put("item_qty", "itemQty");
			rs2BeanMap.put("reference_no", "referenceNo");
			rs2BeanMap.put("tx_item_name", "itemName");
			rs2BeanMap.put("tx_item_code", "itemCode");
			rs2BeanMap.put("tx_rent", "rent");
			rs2BeanMap.put("tx_pack", "pack");
			rs2BeanMap.put("tx_item_description", "itemDescription");
			rs2BeanMap.put("tx_property_address", "propertyAddress");
			rs2BeanMap.put("flt_trade", "trade");
			rs2BeanMap.put("flt_unit_price", "unitPrice");
			rs2BeanMap.put("flt_total_price", "totalPrice");
			rs2BeanMap.put("flt_net_value", "netValue");
			rs2BeanMap.put("flt_value_of_goods", "valueOfGoods");
			rs2BeanMap.put("flt_insurance_premium", "insurancePremium");
			rs2BeanMap.put("id_doc_key", "docId");
			rs2BeanMap.put("dtt_mod", "modifiedOn");
		}

		return rs2BeanMap;
	}
	
}
