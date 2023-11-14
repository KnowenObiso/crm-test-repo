--
-- TQC_AGNT_ACCOUNTS_TRG  (Trigger) 
--
CREATE OR REPLACE TRIGGER TQ_CRM.TQC_AGNT_ACCOUNTS_TRG 
AFTER DELETE OR INSERT OR UPDATE
ON TQ_CRM.TQC_AGENCIES 
REFERENCING NEW AS New OLD AS Old
FOR EACH ROW
DECLARE
tmpVar NUMBER;
/******************************************************************************
   NAME:    TQ_CRM.TQC_AGNT_ACCOUNTS_TRG   
   PURPOSE: TO SYCNCHRONIZE TQC_AGENCIES WITH TQC_ACCOUNTS   

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        4/11/2011             1. Created this trigger.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     
      Sysdate:         4/11/2011
      Date and Time:   4/11/2011, 11:46:41 AM, and 4/11/2011 11:46:41 AM
      Username:         (set in TOAD Options, Proc Templates)
      Table Name:      TQC_AGENCIES (set in the "New PL/SQL Object" dialog)
      Trigger Options:  (set in the "New PL/SQL Object" dialog)
******************************************************************************/
BEGIN
  IF INSERTING THEN 
    insert into TQC_ACCOUNTS (ACC_CODE, ACC_TYPE, ACC_TYPE_CODE)
    VALUES (tqc_ACC_CODE_seq.nextval,'A',:NEW.AGN_CODE);
  ELSIF UPDATING THEN
    IF :NEW.AGN_CODE != :OLD.AGN_CODE THEN 
        UPDATE TQC_ACCOUNTS SET ACC_TYPE_CODE = :NEW.AGN_CODE
        WHERE ACC_TYPE_CODE = :OLD.AGN_CODE
        AND ACC_TYPE = 'A';
     END IF;
  ELSIF DELETING THEN
    DELETE TQC_ACCOUNTS 
    WHERE ACC_TYPE_CODE = :OLD.AGN_CODE
    AND ACC_TYPE = 'A';
  END IF;

   EXCEPTION
     WHEN OTHERS THEN
       -- Consider logging the error and then re-raise
       RAISE;
END ;
/