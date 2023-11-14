--
-- PKG_GLOBAL_VARS  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM."PKG_GLOBAL_VARS" IS
PROCEDURE set_pvar(p_varname_txt VARCHAR2, p_val NUMBER) IS
BEGIN
    pvg_cursor_num := DBMS_SQL.OPEN_CURSOR;
    DBMS_SQL.PARSE(pvg_cursor_num, 'BEGIN ' ||p_varname_txt || ' := :the_value; END;',DBMS_SQL.NATIVE);
    DBMS_SQL.BIND_VARIABLE(pvg_cursor_num, ':the_value',p_val);
    pvg_temp_num := DBMS_SQL.EXECUTE(pvg_cursor_num);
    DBMS_SQL.CLOSE_CURSOR(pvg_cursor_num);
END set_pvar;
PROCEDURE set_pvar(p_varname_txt VARCHAR2, p_val VARCHAR2) IS
BEGIN
    pvg_cursor_num := DBMS_SQL.OPEN_CURSOR;
    DBMS_SQL.PARSE(pvg_cursor_num, 'BEGIN ' ||p_varname_txt || ' := :the_value; END;',DBMS_SQL.NATIVE);
    DBMS_SQL.BIND_VARIABLE(pvg_cursor_num, ':the_value',p_val);
    pvg_temp_num := DBMS_SQL.EXECUTE(pvg_cursor_num);
    DBMS_SQL.CLOSE_CURSOR(pvg_cursor_num);
END set_pvar;
PROCEDURE set_pvar(p_varname_txt VARCHAR2, p_val DATE) IS
BEGIN
    pvg_cursor_num := DBMS_SQL.OPEN_CURSOR;
    DBMS_SQL.PARSE(pvg_cursor_num, 'BEGIN ' ||p_varname_txt || ' := :the_value; END;',DBMS_SQL.NATIVE);
    DBMS_SQL.BIND_VARIABLE(pvg_cursor_num, ':the_value',p_val);
    pvg_temp_num := DBMS_SQL.EXECUTE(pvg_cursor_num);
    DBMS_SQL.CLOSE_CURSOR(pvg_cursor_num);
END set_pvar;
FUNCTION get_pnumber(p_varname_txt VARCHAR2) RETURN NUMBER IS
    lv_value_num NUMBER;
BEGIN
    pvg_cursor_num := DBMS_SQL.OPEN_CURSOR;
    DBMS_SQL.PARSE(pvg_cursor_num,'BEGIN :the_value := ' ||p_varname_txt ||    ';END;',DBMS_SQL.NATIVE);
    DBMS_SQL.BIND_VARIABLE(pvg_cursor_num, 'the_value',lv_value_num);
    pvg_temp_num := DBMS_SQL.EXECUTE(pvg_cursor_num);
    DBMS_SQL.VARIABLE_VALUE(pvg_cursor_num, 'the_value',lv_value_num);
    DBMS_SQL.CLOSE_CURSOR(pvg_cursor_num);
    RETURN lv_value_num;
END get_pnumber;
FUNCTION get_pvarchar2(p_varname_txt VARCHAR2) RETURN VARCHAR2 IS
    lv_value_txt VARCHAR2(100);
BEGIN
    pvg_cursor_num := DBMS_SQL.OPEN_CURSOR;
    DBMS_SQL.PARSE(pvg_cursor_num,'BEGIN :the_value := ' ||p_varname_txt ||    ';END;',DBMS_SQL.NATIVE);
    DBMS_SQL.BIND_VARIABLE(pvg_cursor_num, 'the_value',lv_value_txt,100);
    pvg_temp_num := DBMS_SQL.EXECUTE(pvg_cursor_num);
    DBMS_SQL.VARIABLE_VALUE(pvg_cursor_num, 'the_value',lv_value_txt);
    DBMS_SQL.CLOSE_CURSOR(pvg_cursor_num);
    RETURN lv_value_txt;
END get_pvarchar2;
FUNCTION get_pdate(p_varname_txt VARCHAR2) RETURN DATE IS
    lv_value_date DATE;
BEGIN
    pvg_cursor_num := DBMS_SQL.OPEN_CURSOR;
    DBMS_SQL.PARSE(pvg_cursor_num,'BEGIN :the_value := ' ||p_varname_txt ||    ';END;',DBMS_SQL.NATIVE);
    DBMS_SQL.BIND_VARIABLE(pvg_cursor_num, 'the_value',lv_value_date,100);
    pvg_temp_num := DBMS_SQL.EXECUTE(pvg_cursor_num);
    DBMS_SQL.VARIABLE_VALUE(pvg_cursor_num, 'the_value',lv_value_date);
    DBMS_SQL.CLOSE_CURSOR(pvg_cursor_num);
    RETURN lv_value_date;
END get_pdate;
FUNCTION get_param_number(p_varname_txt VARCHAR2) RETURN NUMBER IS
    lv_value_num NUMBER;
BEGIN
     SELECT PARAM_VALUE INTO lv_value_num
     FROM LMS_PARAMETERS
     WHERE PARAM_NAME = p_varname_txt;
     RETURN lv_value_num;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
         RAISE_APPLICATION_ERROR(-20100,'The Parameter '||p_varname_txt||' is not specified in System Wide Parameter Setups');
    WHEN OTHERS THEN
         RAISE_APPLICATION_ERROR(-20100,'Error retrieving '||p_varname_txt||' '||SQLERRM(SQLCODE));
END get_param_number;
FUNCTION get_param_varchar2(p_varname_txt VARCHAR2) RETURN VARCHAR2 IS
    lv_value_txt VARCHAR2(100);
BEGIN
    SELECT PARAM_VALUE INTO lv_value_txt
    FROM LMS_PARAMETERS
    WHERE PARAM_NAME = p_varname_txT;
    RETURN lv_value_txt;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
         RAISE_APPLICATION_ERROR(-20100,'The Parameter '||p_varname_txt||' is not specified in System Wide Parameter Setups');
    WHEN OTHERS THEN
         RAISE_APPLICATION_ERROR(-20100,'Error retrieving '||p_varname_txt||' '||SQLERRM(SQLCODE));
END get_param_varchar2;
FUNCTION get_param_date(p_varname_txt VARCHAR2) RETURN DATE IS
    lv_value_date DATE;
BEGIN
    SELECT PARAM_VALUE INTO lv_value_date
    FROM LMS_PARAMETERS
    WHERE PARAM_NAME = p_varname_txt;
    RETURN lv_value_date;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
         RAISE_APPLICATION_ERROR(-20100,'The Parameter '||p_varname_txt||' is not specified in System Wide Parameter Setups');
    WHEN OTHERS THEN
         RAISE_APPLICATION_ERROR(-20100,'Error retrieving '||p_varname_txt||' '||SQLERRM(SQLCODE));
END get_param_date;
END  Pkg_Global_Vars; 
/