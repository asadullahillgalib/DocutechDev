USE [DOCUTECH_DEV]
GO
/****** Object:  StoredProcedure [nDOCUTECH].[SEL_template]    Script Date: 30/10/2019 14:21:21 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


	ALTER PROC [nDOCUTECH].[SEL_template]
	  @tx_action_name				varchar(32)					= NULL
	, @id_template_key 				int 					 	= NULL
	, @id_template_ver 				int 						= NULL
	, @is_active 					int 						= NULL
	, @id_env_key 					int 						= NULL
	, @id_user_mod_key 				int 						= NULL
	, @dtt_mod 						datetime 					= NULL
	, @id_event_key 				int 						= NULL
	, @id_state_key 				int 						= NULL
	, @id_action_key 				int 						= NULL
	, @is_send_to_nuxeo				int 						= NULL
	, @is_template_prepared 		int 						= NULL
	, @is_parsed_successfully		int 						= NULL
	, @tx_pdf_type 					varchar(8) 					= NULL
	, @tx_processor_type			varchar(8)					= NULL
	, @tx_client_name 				varchar(64)					= NULL
	, @tx_supplier_name 			varchar(64)					= NULL
	, @tx_doc_source 				varchar(64)					= NULL
	, @tx_received_doc_id 			varchar(64)					= NULL
	, @tx_doc_type 					varchar(256)				= NULL


AS

BEGIN
	
	IF (@tx_action_name = 'SELECT')
	BEGIN
		SELECT tx_rs_type = 'RS_TYPE_TEMPLATE'
		, *
		, 'OK'
		FROM nDOCUTECH.T_TEMPLATE
	END

END
