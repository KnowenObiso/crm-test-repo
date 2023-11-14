-- 
-- Non Foreign Key Constraints for Table TQC_WEB_PRODUCT_DETAILS 
-- 
ALTER TABLE TQ_CRM.TQC_WEB_PRODUCT_DETAILS ADD (
  CONSTRAINT TQC_WEB_PRODUCT_DETAILS_PK
  PRIMARY KEY
  (TWPD_CLNT_CODE, TWPD_TWP_CODE)
  USING INDEX TQ_CRM.TQC_WEB_PRODUCT_DETAILS_PK
  ENABLE VALIDATE);

ALTER TABLE TQ_CRM.TQC_WEB_PRODUCT_DETAILS ADD (
  CONSTRAINT TQC_WEB_USER_DETAILS_UK
  UNIQUE (TWPD_USR_CODE, TWPD_TWP_CODE, TWPD_CLNT_CODE)
  USING INDEX TQ_CRM.TQC_WEB_USER_DETAILS_UK
  ENABLE VALIDATE);