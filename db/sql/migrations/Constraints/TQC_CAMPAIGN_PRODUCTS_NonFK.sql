-- 
-- Non Foreign Key Constraints for Table TQC_CAMPAIGN_PRODUCTS 
-- 
ALTER TABLE TQ_CRM.TQC_CAMPAIGN_PRODUCTS ADD (
  CONSTRAINT TQC_CAMPAIGN_PRODUCTS_PK
  PRIMARY KEY
  (CPRO_CODE, CPRO_PRO_CODE)
  USING INDEX TQ_CRM.TQC_CAMPAIGN_PRODUCTS_PK
  ENABLE VALIDATE);