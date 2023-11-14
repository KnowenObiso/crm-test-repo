-- 
-- Non Foreign Key Constraints for Table TQC_SYS_PRCSS_SUBAREA_LVLS 
-- 
ALTER TABLE TQ_CRM.TQC_SYS_PRCSS_SUBAREA_LVLS ADD (
  CONSTRAINT SPSAL_APPROVER_CHK
  CHECK (SPSAL_APPROVER_TYPE IN ('POST','RANK','USER','UGRP'))
  ENABLE VALIDATE);

ALTER TABLE TQ_CRM.TQC_SYS_PRCSS_SUBAREA_LVLS ADD (
  CONSTRAINT SPSAL_PK
  PRIMARY KEY
  (SPSAL_CODE)
  USING INDEX TQ_CRM.SPSAL_PK
  ENABLE VALIDATE);