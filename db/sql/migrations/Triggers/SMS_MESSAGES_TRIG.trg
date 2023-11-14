--
-- SMS_MESSAGES_TRIG  (Trigger) 
--
CREATE OR REPLACE TRIGGER TQ_CRM.SMS_MESSAGES_TRIG
   BEFORE DELETE OR INSERT OR UPDATE OF sms_msg
   ON tq_crm.tqc_sms_messages
   REFERENCING NEW AS NEW OLD AS OLD
   FOR EACH ROW
DISABLE
DECLARE
   v_msgt_msg   VARCHAR2 (2000);
BEGIN
--   v_msgt_msg := :NEW.sms_msg;
   raise_error ('v_msgt_msg =='||:NEW.sms_msg||'=='||:NEW.SMS_TEL_NO);
END;
/