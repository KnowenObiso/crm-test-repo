-- 
-- Non Foreign Key Constraints for Table TQC_ORG_SUBDIV_PREV_HEADS 
-- 
ALTER TABLE TQ_CRM.TQC_ORG_SUBDIV_PREV_HEADS ADD (
  CONSTRAINT OSDPH_PK
  PRIMARY KEY
  (OSDPH_CODE)
  USING INDEX TQ_CRM.OSDPH_PK
  ENABLE VALIDATE);