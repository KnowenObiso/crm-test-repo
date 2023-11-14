1. Update the attached messageTemplate.jspx file
2. In the attached TurnQuest/view/folder, update the following files as specified:
	i. In the backing/ folder:
		- AlertBean.class
		- MessageTemplateBacking.class
	ii. base/ folder:
		- Rendering.class
		
	iii. dao/ folder:
		- MessageProduct.class
		- MessageProduct.xml
		- MessagingDAO.class
		- MessagingDAO.xml
		
	iv. models/ folder:
		- AlertType.class
		- AlertType.xml
		- MessageTemplate.class
		- MessageTemplate.xml
		
	v. pageDefs/ folder:
		- messageTemplatePageDef.xml
		
3. In the attached backend folder, update the following procedures and functions as specified:
	- TQC_SETUPS_CURSOR.get_em_template into TQC_SETUPS_CURSOR package
	- TQC_SETUPS_CURSOR.get_em_template_by_type into TQC_SETUPS_CURSOR package
	- TQC_SETUPS_PKG.messagetemplates_prc into TQC_SETUPS_PKG package
	- TQC_SMS_PKG.dispatch_documents_prc into TQC_SMS_PKG package.
	- TQC_SMS_PKG.get_AlertType into TQC_SMS_PKG package
	
4. In the scripts folder, run the following attached scripts backend:
	- DA_CODE_SEQ.sql
	- msgt_created_by.sql
	- msgt_prod_code.sql
	- msgt_prod_name.sql
	- msgt_updated_by.sql
	
5. In the types folder, update the following types as specified:
	- TQC_SETUPS_CURSOR.email_templates_rec into TQC_SETUPS_CURSOR package spec
	- TQC_SMS_PKG.alert_type_rec intoTQC_SMS_PKG package spec