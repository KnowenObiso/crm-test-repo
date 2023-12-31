-- 
-- Non Foreign Key Constraints for Table TQC_OBJECT_TYPES 
-- 
ALTER TABLE TQ_CRM.TQC_OBJECT_TYPES ADD (
  CONSTRAINT TQC_OBJECT_TYPE_PK
  PRIMARY KEY
  (OT_CODE)
  USING INDEX TQ_CRM.TQC_OBJECT_TYPE_PK
  ENABLE VALIDATE);

ALTER TABLE TQ_CRM.TQC_OBJECT_TYPES ADD (
  CONSTRAINT TQC_OBJECT_TYPE_UK
  UNIQUE (OT_SHT_DESC)
  USING INDEX TQ_CRM.TQC_OBJECT_TYPE_UK
  ENABLE VALIDATE);