--
-- TQC_SYSTEMS_TRGG  (Trigger) 
--
CREATE OR REPLACE TRIGGER TQ_CRM.TQC_SYSTEMS_TRGG 
BEFORE DELETE
ON TQ_CRM.TQC_SYSTEMS REFERENCING NEW AS NEW OLD AS OLD
DECLARE
tmpVar NUMBER;
/******************************************************************************
   NAME:
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        18/06/2007             1. Created this trigger.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:
      Sysdate:         18/06/2007
      Date and Time:   18/06/2007, 2:44:04 PM, and 18/06/2007 2:44:04 PM
      Username:         (set in TOAD Options, Proc Templates)
      Table Name:       (set in the "New PL/SQL Object" dialog)
      Trigger Options:  (set in the "New PL/SQL Object" dialog)
******************************************************************************/
BEGIN
   RAISE_APPLICATION_ERROR(-20001,'NO DELETE ALLOWED FOR SYSTEMS');

   EXCEPTION
     WHEN OTHERS THEN
       -- Consider logging the error and then re-raise
       RAISE;
END ;
/