-- 
-- Non Foreign Key Constraints for Table TQC_SYS_MOD_SUBUNITS_DETAILS 
-- 
ALTER TABLE TQ_CRM.TQC_SYS_MOD_SUBUNITS_DETAILS ADD (
  CONSTRAINT TQC_SYS_MOD_SUBUNITS_DETAI_C01
  CHECK (TSMSD_TYPE IN ('INPUT_TEXT','NUMBER','TEXT','DATE','OPTION','TABLE','CHECK'))
  ENABLE VALIDATE);

ALTER TABLE TQ_CRM.TQC_SYS_MOD_SUBUNITS_DETAILS ADD (
  CONSTRAINT TQC_SYS_MOD_SUBUNITS_DETAILSPK
  PRIMARY KEY
  (TSMSD_CODE)
  USING INDEX TQ_CRM.TQC_SYS_MOD_SUBUNITS_DETAILSPK
  ENABLE VALIDATE);