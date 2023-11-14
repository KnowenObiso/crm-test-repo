-- 
-- Non Foreign Key Constraints for Table TQC_PRINT_SERVERS 
-- 
ALTER TABLE TQ_CRM.TQC_PRINT_SERVERS ADD (
  CONSTRAINT TQC_PRINTERS_PK
  PRIMARY KEY
  (SERV_CODE)
  USING INDEX TQ_CRM.TQC_PRINTERS_PK
  ENABLE VALIDATE);