--
-- QC_SERV_PROV_ACCOUNTS_TRG  (Trigger) 
--
CREATE OR REPLACE TRIGGER TQ_CRM.QC_SERV_PROV_ACCOUNTS_TRG 
AFTER DELETE OR INSERT OR UPDATE
ON TQ_CRM.TQC_SERVICE_PROVIDERS 
REFERENCING NEW AS New OLD AS Old
FOR EACH ROW
DECLARE
tmpVar NUMBER;
/******************************************************************************
   NAME:       QC_SERV_PROV_ACCOUNTS_TRG
   PURPOSE:   TO SYNCHONIZE TQC_SERVICE_PROVIDERS WITH TQC_ACCOUNTS

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        4/11/2011             1. Created this trigger.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     QC_SERV_PROV_ACCOUNTS_TRG
      Sysdate:         4/11/2011
      Date and Time:   4/11/2011, 12:07:06 PM, and 4/11/2011 12:07:06 PM
      Username:         (set in TOAD Options, Proc Templates)
      Table Name:      TQC_SERVICE_PROVIDERS (set in the "New PL/SQL Object" dialog)
      Trigger Options:  (set in the "New PL/SQL Object" dialog)
******************************************************************************/
BEGIN
  IF INSERTING THEN 
    insert into TQC_ACCOUNTS (ACC_CODE, ACC_TYPE, ACC_TYPE_CODE)
    VALUES (tqc_ACC_CODE_seq.nextval,'SPR',:NEW.SPR_CODE);
  ELSIF UPDATING THEN
    IF :NEW.SPR_CODE != :OLD.SPR_CODE THEN 
        UPDATE TQC_ACCOUNTS SET ACC_TYPE_CODE = :NEW.SPR_CODE
        WHERE ACC_TYPE_CODE = :OLD.SPR_CODE
        AND ACC_TYPE = 'SPR';
     END IF;
  ELSIF DELETING THEN
    DELETE TQC_ACCOUNTS 
    WHERE ACC_TYPE_CODE = :OLD.SPR_CODE
    AND ACC_TYPE = 'SPR';
  END IF;

   EXCEPTION
     WHEN OTHERS THEN
       -- Consider logging the error and then re-raise
       RAISE;
END ;
/