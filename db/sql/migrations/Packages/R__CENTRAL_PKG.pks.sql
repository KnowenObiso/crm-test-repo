--
-- CENTRAL_PKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.CENTRAL_PKG AS
/******************************************************************************
   NAME:       CENTRAL_PKG
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        1/13/2014             1. Created this package.
******************************************************************************/

  TYPE systems_rec IS RECORD(
  SYS_CODE              SYSTEMS_VIEW.SYS_CODE%TYPE,
  SYS_SHT_DESC          SYSTEMS_VIEW.SYS_SHT_DESC%TYPE,
  SYS_NAME              SYSTEMS_VIEW.SYS_NAME%TYPE);
  
  TYPE systems_ref IS REF CURSOR RETURN systems_rec;
  FUNCTION getSystems RETURN systems_ref;
  
                            
  FUNCTION user_authenticate(vusername IN VARCHAR2,
	                      	vpwd IN VARCHAR2,
	                      	vmsg OUT VARCHAR2,
	                      	lastlogindate OUT DATE,
	                      	usercode OUT NUMBER,
	                      	username OUT VARCHAR2,
                            v_sys_code IN NUMBER,
                            v_sysPath   OUT VARCHAR2) RETURN INTEGER ;
                            
                            
  FUNCTION user_authenticate(vusername IN VARCHAR2,
	                      	vpwd IN VARCHAR2,
							v_pwd_reentry IN VARCHAR2,
							v_new_pwd1 IN VARCHAR2,
							v_new_pwd2 IN VARCHAR2,
	                      	vmsg OUT VARCHAR2,
	                      	lastlogindate OUT DATE,
	                      	usercode OUT NUMBER,
	                      	username OUT VARCHAR2,
                            v_sys_code IN NUMBER,
                            v_sysPath   OUT VARCHAR2) RETURN INTEGER ;
                            
  
FUNCTION get_param_varchar(v_param IN VARCHAR2,v_couCode IN NUMBER) RETURN VARCHAR2;
FUNCTION get_param_NUMBER(v_param IN VARCHAR2,v_couCode IN NUMBER) RETURN NUMBER;
FUNCTION get_param_date(v_param IN VARCHAR2,v_couCode IN NUMBER) RETURN DATE;

END CENTRAL_PKG; 
/