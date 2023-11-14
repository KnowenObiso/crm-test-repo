--
-- TQC_PARAMETERS_PKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM."TQC_PARAMETERS_PKG" AS
 FUNCTION get_param_varchar(v_param IN VARCHAR2) RETURN VARCHAR2;
 FUNCTION get_param_NUMBER(v_param IN VARCHAR2) RETURN NUMBER;
 FUNCTION get_param_date(v_param IN VARCHAR2) RETURN DATE;
 FUNCTION get_org_type(v_sys_code IN NUMBER) RETURN VARCHAR2;
 FUNCTION GET_ACTIVE_SYSTEMS(v_sys_code IN NUMBER) RETURN VARCHAR2;
 FUNCTION get_param_varchar2(v_param IN VARCHAR2) RETURN VARCHAR2;
END Tqc_Parameters_Pkg; 
/