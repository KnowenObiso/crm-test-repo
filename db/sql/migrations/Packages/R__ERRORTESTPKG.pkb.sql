--
-- ERRORTESTPKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.errortestpkg as 
PROCEDURE RAISE_ERROR(v_msg IN VARCHAR2,v_err_no IN NUMBER:=NULL) IS
BEGIN
    IF v_err_no IS NULL THEN
        RAISE_APPLICATION_ERROR(-20037, v_msg||' - '||SQLERRM(SQLCODE));
    ELSE
        RAISE_APPLICATION_ERROR(v_err_no, v_msg||' - '||SQLERRM(SQLCODE));
    END IF;
END RAISE_ERROR;

procedure cc is 
v_pro_desc varchar2(35);
begin
     SELECT PROD_DESC
        INTO v_pro_desc
        FROM LMS_PRODUCTS
        WHERE PROD_code = PROD_code;
end;
procedure bb is

begin
    begin
        cc;
    exception
        when others then 
            raise_error('cc');
    end;
exception
    when others then 
        raise_error('bb');
end;
procedure aa is

begin
    begin
        bb;
    exception
        when others then 
            raise_error('bb in aa');
    end;
exception
    when others then 
        raise_error('aa');
end;
procedure to_call is 

begin
    aa;
exception
        WHEN NO_DATA_FOUND THEN 
            TQC_ERROR_MANAGER.RAISE_ERROR(100,
                                         text_in => 'Product not defined.'
                                          , value1_in      => 'EARN'
                                          , name2_in       => 'TABLE_NAME'
                                          , value2_in      => 'HRMS_PRODUCTS'
                                          , name4_in       => 'OWNER'
                                          , value4_in      => USER);
        WHEN TOO_MANY_ROWS THEN 
        --MESSAGE('TTRRRRRRR');PAUSE;
            TQC_ERROR_MANAGER.RAISE_ERROR(10001,text_in => 'Product duplicated'
                                          , value1_in      => 'EARN'
                                          , name2_in       => 'TABLE_NAME'
                                          , value2_in      => 'LMS_PRODUCTS'
                                          , name4_in       => 'OWNER'
                                          , value4_in      => USER);
        WHEN OTHERS THEN 
            TQC_ERROR_MANAGER.RAISE_UNANTICIPATED
                                                 ( text_in => 'Error getting product description',
                            name1_in       => 'PROD_TYPE'
                          , value1_in      => 'EARN'
                          , name2_in       => 'TABLE_NAME'
                          , value2_in      => 'LMS_PRODUCTS'
                          , name4_in       => 'OWNER'
                          , value4_in      => USER
                           );
end;
    
END; 
/