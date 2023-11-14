--
-- TQC_ESCALATION_PKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_escalation_pkg
AS
  PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL);
   FUNCTION getworkingdays (v_date_from DATE, v_date_to DATE)
      RETURN NUMBER;
END tqc_escalation_pkg; 
/