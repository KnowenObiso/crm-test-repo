--
-- TQC_CLNT_ACCOUNTS_TRG  (Trigger) 
--
CREATE OR REPLACE TRIGGER TQ_CRM.TQC_CLNT_ACCOUNTS_TRG 
AFTER DELETE OR INSERT OR UPDATE
ON TQ_CRM.TQC_CLIENTS 
REFERENCING NEW AS New OLD AS Old
FOR EACH ROW
DECLARE
tmpVar NUMBER;
/******************************************************************************
   NAME:  TQC_ACCOUNTS_TRG     
   PURPOSE:    TO SYNC TQC_ACCOUNTS WITH TQC_CLIENTS

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        4/11/2011             1. Created this trigger.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     
      Sysdate:         4/11/2011
      Date and Time:   4/11/2011, 11:06:30 AM, and 4/11/2011 11:06:30 AM
      Username:         (set in TOAD Options, Proc Templates)
      Table Name:      TQC_CLIENTS (set in the "New PL/SQL Object" dialog)
      Trigger Options:  (set in the "New PL/SQL Object" dialog)
******************************************************************************/
BEGIN
  IF INSERTING THEN 
    insert into TQC_ACCOUNTS (ACC_CODE, ACC_TYPE, ACC_TYPE_CODE)
    VALUES (tqc_ACC_CODE_seq.nextval,'D',:NEW.CLNT_CODE);
  ELSIF UPDATING THEN
    IF :NEW.CLNT_CODE != :OLD.CLNT_CODE THEN 
        UPDATE TQC_ACCOUNTS SET ACC_TYPE_CODE = :NEW.CLNT_CODE
        WHERE ACC_TYPE_CODE = :OLD.CLNT_CODE
        AND ACC_TYPE = 'D';
     END IF;
  ELSIF DELETING THEN
    DELETE TQC_ACCOUNTS 
    WHERE ACC_TYPE_CODE = :OLD.CLNT_CODE
    AND ACC_TYPE = 'D';
  END IF;

   EXCEPTION
     WHEN OTHERS THEN
       -- Consider logging the error and then re-raise
       RAISE;
END ;
/