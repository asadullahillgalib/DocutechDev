
/****** Object:  StoredProcedure [nDOCUTECH].[SEL_doc]    Script Date: 9/11/2019 6:11:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


	CREATE PROC [nDOCUTECH].[SEL_doc]
	  @tx_action_name				varchar(32)				= NULL

	, @is_active					int						= 1
	, @id_env_key					int						= NULL 	
	, @id_user_mod_key				int						= NULL 	
	, @dtt_mod						datetime				= NULL 
	
	, @id_state_key					int						= NULL 	
	, @id_action_key				int						= NULL 	
	, @dtt_valid_from				datetime				= NULL
	, @dtt_valid_to					datetime				= NULL
	
	, @dtt_as_at					datetime				= NULL
	, @dtt_last_refresh				datetime				= NULL

	, @id_doc_key 			    int 					 	= NULL 	
	, @id_doc_ver 			    int 						= NULL 	
	, @supplier_number 			int 						= NULL
	, @tx_company_address 		varchar(256)				= NULL
	, @tx_invoice_to 			varchar(256)				= NULL
	, @tx_deliver_to 			varchar(256)				= NULL
	, @tx_currency 				varchar(8)					= NULL
	, @receipt_number 			int 						= NULL
	, @document_number 			int 						= NULL
	, @dt_document				date 						= NULL
	, @tx_association_number 	varchar(256)				= NULL
	, @recipient_of_invoice 	int 						= NULL
	, @vat_number 				int 						= NULL
	, @invoice_number 			int 						= NULL
	, @tx_account_number 		varchar(256)				= NULL
	, @tx_voucher_number 		varchar(256)				= NULL
	, @tx_fax_number 			varchar(128)				= NULL
	, @dt_despatch 				date 						= NULL
	, @delivery_note_no 		int  						= NULL
	, @tx_order_id 				varchar(256)				= NULL
	, @tx_delivery_note 		varchar(256)				= NULL
	, @tx_delivery_details 		varchar(256)				= NULL
	, @flt_net_invoice_total 	float(8)					= NULL
	, @flt_vat 					float(8)					= NULL
	, @flt_vat_rate 			float(8)					= NULL
	, @flt_vat_payable 			float(8)					= NULL
	, @flt_invoice_amount 		float(8)					= NULL
	, @flt_total_before_vat 	float(8)					= NULL
	, @flt_total_amount_due 	float(8)					= NULL
	, @tx_payment_details 		varchar(256)				= NULL
	, @flt_discount 			float(8)					= NULL


AS

BEGIN
	
	IF (@tx_action_name = 'SELECT')
	BEGIN
		SELECT tx_rs_type = 'RS_TYPE_TEMPLATE'
		, *	
		FROM	nDOCUTECH.T_DOC
		WHERE	id_doc_key			= ISNULL(@id_doc_key, id_doc_key)
		AND		tx_invoice_number	= ISNULL(@tx_invoice_number, tx_invoice_number)	
		
	END

END                    
