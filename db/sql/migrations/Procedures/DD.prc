--
-- DD  (Procedure) 
--
CREATE OR REPLACE PROCEDURE TQ_CRM.dd IS
        tmpVar NUMBER;
        v_pro_desc number;--varchar2(100);
        
    BEGIN
    
    
    
    kk;
        /*SELECT PROD_DESC
        INTO v_pro_desc
        FROM LMS_PRODUCTS
        WHERE PROD_code = 2008102;*/
    EXCEPTION
        WHEN NO_DATA_FOUND THEN 
            TQC_ERROR_MANAGER.RAISE_ERROR(100,
                                         text_in => 'Product not defined333333333333333.'
                                          , value1_in      => 'EARN'
                                          , name2_in       => 'TABLE_NAME'
                                          , value2_in      => 'LMS_PRODUCTS'
                                          , name4_in       => 'OWNER'
                                          , value4_in      => USER);
        WHEN TOO_MANY_ROWS THEN 
        --MESSAGE('TTRRRRRRR');PAUSE;
            TQC_ERROR_MANAGER.RAISE_ERROR(10001,text_in => 'Product duplicated333333333333333333'
                                          , value1_in      => 'EARN'
                                          , name2_in       => 'TABLE_NAME'
                                          , value2_in      => 'LMS_PRODUCTS'
                                          , name4_in       => 'OWNER'
                                          , value4_in      => USER);
        WHEN OTHERS THEN 
            TQC_ERROR_MANAGER.RAISE_UNANTICIPATED
                                                 ( text_in => 'Error getting product description33333333333333',
                            name1_in       => 'PROD_TYPE'
                          , value1_in      => 'EARN'
                          , name2_in       => 'TABLE_NAME'
                          , value2_in      => 'LMS_PRODUCTS'
                          , name4_in       => 'OWNER'
                          , value4_in      => USER
                           );
    END dd; 
 
/