--
-- PREV_INCI  (Function) 
--
CREATE OR REPLACE FUNCTION TQ_CRM."PREV_INCI" (v_date IN DATE, v_sys_code IN NUMBER, v_user IN VARCHAR2)
RETURN number is
v_previous NUMBER := 0;

BEGIN
	select sum(decode(INCS_ICTY_SHT_DESC,'OPEN',1,'O',1,'R',1,0)) into v_previous
	from tqc_incident_status, tqc_incidents, tqc_incident_types
	where INC_CODE = INCS_INC_CODE
	and INC_INCT_CODE = INCT_CODE
	and INCs_SYS_CODE = decode(v_sys_code,-2000,INCs_SYS_CODE,v_sys_code)
	AND INC_RECEIVED_BY = decode(v_user,'ALL',INC_RECEIVED_BY,v_user)
	and trunc(INCS_ICTY_DATE) < v_date;
	return (v_previous);
END ;

 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
/