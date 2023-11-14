--
-- TQC_PARAMETERS_PKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.Tqc_Parameters_Pkg AS
  PROCEDURE RAISE_ERROR(cmsg IN VARCHAR2, nerrno IN NUMBER := NULL) IS
  BEGIN
    IF nerrno IS NULL THEN
      RAISE_APPLICATION_ERROR(-20015, cmsg || ' - ' || SQLERRM(SQLCODE));
    ELSE
      RAISE_APPLICATION_ERROR(nerrno, cmsg || ' - ' || SQLERRM(SQLCODE));
    END IF;
  END RAISE_ERROR;
  FUNCTION get_param_varchar(v_param IN VARCHAR2) RETURN VARCHAR2 IS
    v_param_value VARCHAR2(5000);
  BEGIN
  
    SELECT PARAM_VALUE
      INTO v_param_value
      FROM TQC_PARAMETERS
     WHERE PARAM_NAME = v_param;
    RETURN(v_param_value);
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RAISE_ERROR('Parameter ' || v_param || ' not found ');
    WHEN TOO_MANY_ROWS THEN
      RAISE_ERROR('More than one instance of Parameter ' || v_param ||
                  ' found ');
    WHEN OTHERS THEN
      RAISE_ERROR('Error getting the parameter value set for ' || v_param);
  END;
  FUNCTION get_param_NUMBER(v_param IN VARCHAR2) RETURN NUMBER IS
    v_param_value NUMBER;
  BEGIN
    SELECT TO_NUMBER(PARAM_VALUE)
      INTO v_param_value
      FROM TQC_PARAMETERS
     WHERE PARAM_NAME = v_param;
    RETURN(v_param_value);
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RAISE_ERROR('Parameter ' || v_param || ' not found ');
    WHEN TOO_MANY_ROWS THEN
      RAISE_ERROR('More than one instance of Parameter ' || v_param ||
                  ' found ');
    WHEN OTHERS THEN
      RAISE_ERROR('Error getting the parameter value set for ' || v_param);
  END;
  FUNCTION get_param_date(v_param IN VARCHAR2) RETURN DATE IS
    v_param_value DATE;
  BEGIN
    SELECT TO_DATE(PARAM_VALUE)
      INTO v_param_value
      FROM TQC_PARAMETERS
     WHERE PARAM_NAME = v_param;
    RETURN(v_param_value);
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RAISE_ERROR('Parameter ' || v_param || ' not found ');
    WHEN TOO_MANY_ROWS THEN
      RAISE_ERROR('More than one instance of Parameter ' || v_param ||
                  ' found ');
    WHEN OTHERS THEN
      RAISE_ERROR('Error getting the parameter value set for ' || v_param);
  END;
  FUNCTION get_org_type(v_sys_code IN NUMBER) RETURN VARCHAR2 IS
    v_org_type VARCHAR2(5);
  BEGIN
    SELECT ORG_TYPE
      INTO v_org_type
      FROM TQC_ORGANIZATIONS, TQC_SYSTEMS
     WHERE ORG_CODE = SYS_ORG_CODE
       AND SYS_CODE = v_sys_code;
    RETURN(v_org_type);
  END;
  FUNCTION GET_ACTIVE_SYSTEMS(v_sys_code IN NUMBER) RETURN VARCHAR2 IS
    v_sys_active VARCHAR2(1);
  BEGIN
  BEGIN
    SELECT SYS_ACTIVE
      INTO v_sys_active
      FROM  TQC_SYSTEMS
     WHERE  SYS_CODE = v_sys_code;
     EXCEPTION WHEN OTHERS
     THEN
       v_sys_active:='N';
     END;
    RETURN(v_sys_active);
  END;
  FUNCTION get_param_varchar2(v_param IN VARCHAR2) RETURN VARCHAR2 IS
	v_param_value  VARCHAR2(500);
    BEGIN

            SELECT PARAM_VALUE
            INTO v_param_value
            FROM TQC_PARAMETERS
            WHERE PARAM_NAME = v_param;
            RETURN(v_param_value);
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
           NULL;
        WHEN TOO_MANY_ROWS THEN
            RAISE_ERROR('More than one instance of Parameter '||v_param||' found ');
        WHEN OTHERS THEN
            RAISE_ERROR('Error getting the parameter value set for '||v_param);
    END;
END Tqc_Parameters_Pkg; 
/