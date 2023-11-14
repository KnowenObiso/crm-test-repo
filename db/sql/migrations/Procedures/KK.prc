--
-- KK  (Procedure) 
--
CREATE OR REPLACE PROCEDURE TQ_CRM.kk IS
        tmpVar NUMBER;
        v_pro_desc number;--varchar2(100);
        
    BEGIN
    ff;
        /*SELECT PROD_DESC
        INTO v_pro_desc
        FROM LMS_PRODUCTS
        WHERE PROD_code = 2008102;*/
    EXCEPTION
        WHEN NO_DATA_FOUND THEN 
            TQC_ERROR_MANAGER.RAISE_ERROR(100,
                                         text_in => 'Product not defined22222222222222.'
                                          , value1_in      => 'EARN'
                                          , name2_in       => 'TABLE_NAME'
                                          , value2_in      => 'HRMS_PRODUCTS'
                                          , name4_in       => 'OWNER'
                                          , value4_in      => USER);
        WHEN TOO_MANY_ROWS THEN 
        --MESSAGE('TTRRRRRRR');PAUSE;
            TQC_ERROR_MANAGER.RAISE_ERROR(10001,text_in => 'Product duplicated222222222222222'
                                          , value1_in      => 'EARN'
                                          , name2_in       => 'TABLE_NAME'
                                          , value2_in      => 'LMS_PRODUCTS'
                                          , name4_in       => 'OWNER'
                                          , value4_in      => USER);
        WHEN OTHERS THEN 
            TQC_ERROR_MANAGER.RAISE_UNANTICIPATED
                                                 ( text_in => 'Error getting product description222222222222222',
                            name1_in       => 'PROD_TYPE'
                          , value1_in      => 'EARN'
                          , name2_in       => 'TABLE_NAME'
                          , value2_in      => 'LMS_PRODUCTS'
                          , name4_in       => 'OWNER'
                          , value4_in      => USER
                           );
    END kk; 
 
/