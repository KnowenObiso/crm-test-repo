--
-- ERRORTESTPKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.errortestpkg as 
procedure to_call;
/*HOW TO CALL
DECLARE
    
BEGIN
ERRORTESTPKG.TO_CALL;
EXCEPTION
WHEN OTHERS THEN 
    DECLARE
        v_error TQC_ERROR_MANAGER.error_info_rt;
    BEGIN
    TQC_ERROR_MANAGER.GET_ERROR_INFO(v_error);
    DBMS_OUTPUT.PUT_LINE(v_error.text);
    DBMS_OUTPUT.PUT_LINE(v_error.system_error_message);
    DBMS_OUTPUT.PUT_LINE(v_error.error_stack);
    --DBMS_OUTPUT.PUT_LINE(v_error.call_stack);
    END;
END;*/
end; 
/