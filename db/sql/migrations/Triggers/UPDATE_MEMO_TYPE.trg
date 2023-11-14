--
-- UPDATE_MEMO_TYPE  (Trigger) 
--
CREATE OR REPLACE TRIGGER TQ_CRM.UPDATE_MEMO_TYPE AFTER INSERT OR UPDATE OF COMEM_MTYP_CODE ON TQ_CRM.TQC_COMPANY_MEMOS
REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DISABLE
WHEN (
NEW.COMEM_MTYP_CODE  IS NOT NULL
      )
BEGIN
  IF INSERTING THEN
     Tqc_Memos_Dbpkg.Populate_Memo_Details (:NEW.COMEM_MTYP_CODE,
                                                                     :NEW.COMEM_CODE);
  ELSIF UPDATING THEN
     IF :OLD.COMEM_MTYP_CODE != :NEW.COMEM_MTYP_CODE THEN
         Tqc_Memos_Dbpkg.Del_Memo_Details (:OLD.COMEM_CODE);
         Tqc_Memos_Dbpkg.Populate_Memo_Details (:NEW.COMEM_MTYP_CODE,
                                                                           :OLD.COMEM_CODE);
     END IF;
   ELSE
      NULL;
END IF;
END;
/