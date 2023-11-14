/* This object may not be sorted properly in the script due to cirular references. */
--
-- TQC_PRODUCT_PKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.TQC_PRODUCT_PKG AS
/******************************************************************************
   NAME:       TQC_PRODUCT_PKG
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        25-Jan-11             1. Created this package body.
******************************************************************************/

 
PROCEDURE RAISE_ERROR(v_msg IN VARCHAR2,v_err_no IN NUMBER:=NULL) IS
	BEGIN
		IF v_err_no IS NULL THEN
			RAISE_APPLICATION_ERROR(-20033, v_msg||' - '||SQLERRM(SQLCODE));
		ELSE
			RAISE_APPLICATION_ERROR(v_err_no, v_msg||' - '||SQLERRM(SQLCODE));
		END IF;
	END RAISE_ERROR;
 FUNCTION MyFunction(Param1 IN NUMBER) RETURN NUMBER IS
  BEGIN
    RETURN Param1;
  END;

  PROCEDURE MyProcedure(Param1 IN NUMBER) IS
    TmpVar NUMBER;
  BEGIN
    TmpVar := Param1;
  END;
  
  PROCEDURE productAttribute_prc(v_addEdit  VARCHAR2, v_proAtt_tab IN TQC_PRODUCT_ATTRIBUTES_TAB, v_err OUT VARCHAR2) IS        
        v_count     NUMBER;
        
        BEGIN
          
          IF v_proAtt_tab.COUNT = 0 THEN 
              raise_error('Error occured. No Product  Provided : '||SQLERRM(SQLCODE)); 
          END IF;
          
          FOR I IN 1..v_proAtt_tab.COUNT LOOP   
                
              IF v_addEdit = 'A' THEN 
               
                    INSERT INTO TQC_PRODUCT_ATTRIBUTES (
                    TPA_CODE, 
                    TPA_SYSTEM,
                    TPA_PROD_CODE, 
                    TPA_PROD_SHTDESC,
                    TPA_PROD_DESC,
                    TPA_PROD_NARRATION
              )
                 
              
              
               VALUES (TQ_CRM.TPA_CODE_SEQ.NEXTVAL, 
                       v_proAtt_tab(I).TPA_SYSTEM,
                       v_proAtt_tab(I).TPA_PROD_CODE,
                       v_proAtt_tab(I).TPA_PROD_SHTDESC,
                       v_proAtt_tab(I).TPA_PROD_DESC,
                       v_proAtt_tab(I).TPA_PROD_NARRATION                 
                      );
                      
              ELSIF v_addEdit = 'E' THEN 
                  UPDATE TQC_PRODUCT_ATTRIBUTES SET 
                    
                    TPA_SYSTEM=v_proAtt_tab(I).TPA_SYSTEM,
                    TPA_PROD_CODE=v_proAtt_tab(I).TPA_PROD_CODE, 
                    TPA_PROD_SHTDESC=v_proAtt_tab(I).TPA_PROD_SHTDESC,
                    TPA_PROD_DESC=v_proAtt_tab(I).TPA_PROD_DESC,
                    TPA_PROD_NARRATION=v_proAtt_tab(I).TPA_PROD_NARRATION
                      
                  WHERE TPA_CODE = v_proAtt_tab(I).TPA_CODE;
                  
              ELSIF  v_addEdit = 'D' THEN 
                  DELETE TQC_PRODUCT_ATTRIBUTES
                  WHERE TPA_CODE = v_proAtt_tab(I).TPA_CODE;
              END IF;              

                
          END LOOP;
      EXCEPTION
          WHEN OTHERS THEN
          raise_error('Error Manipulating PRODUCT ATTRIBUTES!');
        
      END productAttribute_prc;  

  PROCEDURE get_ProductAttributes(  
                                v_refcur  OUT  product_ref
                                ) IS
  v_sytem Number;                              
                                
 BEGIN
-- Raise_error('org code '||v_org_code);


 OPEN v_refcur FOR
    
     SELECT TPA_CODE, TPA_SYSTEM, TPA_PROD_CODE, TPA_PROD_SHTDESC, TPA_PROD_DESC, TPA_PROD_NARRATION,SYS_NAME
            
    FROM TQC_PRODUCT_ATTRIBUTES,TQC_SYSTEMS 
    WHERE  TPA_SYSTEM=SYS_CODE(+);
   
    
 END ;
 
   PROCEDURE get_SystemProducts( v_sys_code IN number , 
                                v_refcur  OUT  sysProduct_ref
                                ) IS
  v_sytem Number;
     
 BEGIN
-- Raise_error('org code '||v_org_code);

    IF v_sys_code =37 THEN

        OPEN v_refcur FOR
             SELECT PRO_CODE AS CODE, PRO_SHT_DESC AS SHORTDESC , PRO_DESC AS  DESCRIPTION FROM GIN_PRODUCTS  ;
  
       
  
    ELSIF v_sys_code =27 THEN
  
          OPEN v_refcur FOR
             SELECT  PROD_CODE AS CODE, PROD_SHT_DESC AS SHORTDESC , PROD_DESC AS  DESCRIPTION FROM LMS_PRODUCTS;
             
         
     END IF;
     
  
   
    
    
 END ;

PROCEDURE clientAttribute_prc(v_addEdit  VARCHAR2, v_proAtt_tab IN TQC_CLIENT_ATTRIBUTES_TAB, v_err OUT VARCHAR2) IS        
        v_count     NUMBER;
        
        BEGIN
          
          IF v_proAtt_tab.COUNT = 0 THEN 
              raise_error('Error occured. No Attribute  Provided : '||SQLERRM(SQLCODE)); 
          END IF;
          
          FOR I IN 1..v_proAtt_tab.COUNT LOOP   
                
              IF v_addEdit = 'A' THEN 
               
                    INSERT INTO TQC_CLIENT_ATTRIBUTES (
                    TCA_CODE,
                    TCA_ATTRIBUTE_NAME,
                    TCA_ATT_DESC,
                    TCA_PROMPT,
                    TCA_ATT_RANGE,
                    TCA_ATT_TYPE_INPUT,
                    TCA_TABLE_NAME,
                    TCA_COL_NAME
              )
                  VALUES (
                       TQ_CRM.TCA_CODE_SEQ.NEXTVAL, 
                       v_proAtt_tab(I).TCA_ATTRIBUTE_NAME,
                       v_proAtt_tab(I).TCA_ATT_DESC,
                       v_proAtt_tab(I).TCA_PROMPT,
                       v_proAtt_tab(I).TCA_ATT_RANGE,
                       v_proAtt_tab(I).TCA_ATT_TYPE_INPUT,
                       v_proAtt_tab(I).TCA_TABLE_NAME,
                       v_proAtt_tab(I).TCA_COL_NAME
                                        
                      );
                      
              ELSIF v_addEdit = 'E' THEN 
                  UPDATE TQC_CLIENT_ATTRIBUTES SET 
                    TCA_ATTRIBUTE_NAME= v_proAtt_tab(I).TCA_ATTRIBUTE_NAME ,
                    TCA_ATT_DESC =v_proAtt_tab(I).TCA_ATT_DESC ,
                    TCA_PROMPT =v_proAtt_tab(I).TCA_PROMPT ,
                    TCA_ATT_RANGE=v_proAtt_tab(I).TCA_ATT_RANGE,
                    TCA_ATT_TYPE_INPUT=v_proAtt_tab(I).TCA_ATT_TYPE_INPUT,
                    TCA_TABLE_NAME= v_proAtt_tab(I).TCA_TABLE_NAME,
                     TCA_COL_NAME = v_proAtt_tab(I).TCA_COL_NAME
                   
                      
                  WHERE TCA_CODE = v_proAtt_tab(I).TCA_CODE;
                  
              ELSIF  v_addEdit = 'D' THEN 
                  DELETE TQC_CLIENT_ATTRIBUTES
                  WHERE TCA_CODE = v_proAtt_tab(I).TCA_CODE;
              END IF;              

                
          END LOOP;
      EXCEPTION
          WHEN OTHERS THEN
          raise_error('Error Manipulating Attributes!');
        
      END clientAttribute_prc;  
  PROCEDURE get_ClientAttributes(  
                                v_refcur  OUT  clientAttribute_ref
                                ) IS
  v_sytem Number;                              
                                
 BEGIN
-- Raise_error('org code '||v_org_code);


 OPEN v_refcur FOR
    
   SELECT TCA_CODE, TCA_ATTRIBUTE_NAME, TCA_ATT_DESC, TCA_PROMPT, TCA_ATT_RANGE, TCA_ATT_TYPE_INPUT,
   TCA_TABLE_NAME,TCA_COL_NAME,LTRIM(REPLACE(REPLACE(TCA_COL_NAME,'CLNT',''),'_',' ')) shtDesc            
    FROM TQC_client_ATTRIBUTES  ;
    
 END get_ClientAttributes;
 
 
   PROCEDURE get_UndefinedClientAttributes( v_tpa_code IN TQC_PRODUCT_ATTRIBUTES.TPA_CODE%TYPE, 
                                v_refcur  OUT  clientAttribute_ref
                                )
            IS               
                                
 BEGIN
-- Raise_error('org code '||v_org_code);


 OPEN v_refcur FOR
    
   SELECT TCA_CODE, TCA_ATTRIBUTE_NAME, TCA_ATT_DESC, TCA_PROMPT, TCA_ATT_RANGE, TCA_ATT_TYPE_INPUT,
   TCA_TABLE_NAME ,TCA_COL_NAME ,LTRIM(REPLACE(REPLACE(TCA_COL_NAME,'CLNT',''),'_',' ')) shtDesc
   FROM TQC_client_ATTRIBUTES  
   WHERE TCA_CODE NOT IN(SELECT PCA_TCA_CODE FROM TQC_PRODUCT_CLIENT_ATTRIBUTES WHERE PCA_TPA_CODE=v_tpa_code);
            
    
    
 END get_UndefinedClientAttributes;
 
 PROCEDURE  productClient_prc(
      v_add_edit  VARCHAR2,
      v_PCA_CODE    TQC_PRODUCT_CLIENT_ATTRIBUTES.PCA_CODE%TYPE default null,
      v_PCA_TPA_CODE TQC_PRODUCT_CLIENT_ATTRIBUTES.PCA_TPA_CODE%TYPE default null,
      v_PCA_TCA_CODE  TQC_PRODUCT_CLIENT_ATTRIBUTES.PCA_TCA_CODE%TYPE   default null,
      v_PCA_MIN_VALUE    TQC_PRODUCT_CLIENT_ATTRIBUTES.PCA_MIN_VALUE%TYPE   default null,
       v_PCA_MAX_VALUE    TQC_PRODUCT_CLIENT_ATTRIBUTES.PCA_MAX_VALUE%TYPE   default null,
       v_PCA_FIXED_VALUE  TQC_PRODUCT_CLIENT_ATTRIBUTES.PCA_FIXED_VALUE%TYPE   default null
      
      )
      IS
 
  BEGIN
  
 
    IF v_add_edit = 'A' THEN 
    BEGIN  
        INSERT INTO  TQC_PRODUCT_CLIENT_ATTRIBUTES(
         PCA_CODE, PCA_TPA_CODE, PCA_TCA_CODE, PCA_MIN_VALUE, PCA_MAX_VALUE, PCA_FIXED_VALUE
            )
        
          VALUES(
             TQC_PCA_CODE_SEQ.NEXTVAL,v_PCA_TPA_CODE,v_PCA_TCA_CODE,v_PCA_MIN_VALUE,v_PCA_MAX_VALUE,v_PCA_FIXED_VALUE
        
             );
         
           EXCEPTION 
            WHEN  OTHERS THEN
            RAISE_ERROR('AN ERROR OCCURED WHILE TRYING TO ADD THE RECORD');
        
         
     END;
    
    
    
    ELSIF v_add_edit = 'E' THEN 
    BEGIN
        UPDATE  TQC_PRODUCT_CLIENT_ATTRIBUTES SET 
             PCA_TPA_CODE=NVL(v_PCA_TPA_CODE,PCA_TPA_CODE),
             PCA_TCA_CODE=NVL(v_PCA_TCA_CODE,PCA_TCA_CODE),
             PCA_MIN_VALUE=NVL(v_PCA_MIN_VALUE, PCA_MIN_VALUE),
             PCA_MAX_VALUE=NVL(v_PCA_MAX_VALUE,PCA_MAX_VALUE),
             PCA_FIXED_VALUE=NVL(v_PCA_FIXED_VALUE,PCA_FIXED_VALUE)
              
        WHERE PCA_CODE =v_PCA_CODE;
        EXCEPTION  
         WHEN OTHERS THEN
        RAISE_ERROR('AN ERROR OCCURED WHILE TRYING TO EDIT THE RECORD');
         
     END;
     
     
        ELSIF v_add_edit = 'D' THEN 
        BEGIN
        DELETE TQC_PRODUCT_CLIENT_ATTRIBUTES
           WHERE PCA_CODE =v_PCA_CODE;
          EXCEPTION   
                WHEN OTHERS THEN
                RAISE_ERROR('AN ERROR OCCURED WHILE TRYING TO DELETE THE RECORD');
       
        END;
       
    END IF;
    
  
  END productClient_prc;
  
  
  PROCEDURE get_prod_client_attributes( 
    v_tpa_code            TQC_PRODUCT_ATTRIBUTES.TPA_CODE%TYPE,
    v_prodClient_cur OUT prodClient_ref)
    
    IS
    BEGIN
        OPEN v_prodClient_cur FOR
        SELECT PCA_CODE, PCA_TPA_CODE, PCA_TCA_CODE,TCA_ATTRIBUTE_NAME,TCA_ATT_RANGE,  PCA_MIN_VALUE,
         PCA_MAX_VALUE, PCA_FIXED_VALUE,TCA_COL_NAME,TCA_PROMPT
         FROM TQC_CLIENT_ATTRIBUTES,TQC_PRODUCT_ATTRIBUTES, TQC_PRODUCT_CLIENT_ATTRIBUTES
         WHERE PCA_TCA_CODE=TCA_CODE
         AND PCA_TPA_CODE=TPA_CODE
         AND PCA_TPA_CODE=v_tpa_code;
    END get_prod_client_attributes;
 
FUNCTION get_clients_per_product(v_col IN varchar2) RETURN TQC_CLIENTS_PKG.clients_ref IS
     var_str VARCHAR2(10000);
        TYPE clientDetails IS REF CURSOR;
        clients_rec  clientDetails;
    BEGIN
    --RAISE_ERROR('v_col ' ||v_col);
    /*
    OPEN v_Client_cur FOR
        SELECT CLNT_CODE, CLNT_PIN, CLNT_POSTAL_ADDRS, CLNT_TEL, CLNT_OTHER_NAMES, CLNT_NAME , CLNT_ID_REG_NO,CLNT_SHT_DESC,CLNT_ZIP_CODE
        FROM TQC_CLIENTS--TQC_PRODUCT_ATTRIBUTES
        ORDER BY CLNT_NAME; */
    var_str := 'SELECT  CLNT_CODE, CLNT_PIN, CLNT_POSTAL_ADDRS, CLNT_TEL, CLNT_OTHER_NAMES, CLNT_NAME , 
    CLNT_ID_REG_NO,CLNT_SHT_DESC,CLNT_ZIP_CODE FROM TQC_CLIENTS ' || v_col; 
        
    ---  RAISE_ERROR( v_col);
    OPEN clients_rec FOR var_str;  
    RETURN clients_rec;  
        
        
  END get_clients_per_product;

FUNCTION getClientAttColumns RETURN  Client_Att_Column_ref IS
    v_cursor Client_Att_Column_ref;
BEGIN
    OPEN v_cursor FOR
     select column_name,LTRIM(REPLACE(REPLACE(column_name,'CLNT',''),'_',' ')) shtDesc,table_name
    from dba_tab_columns 
    where table_name='TQC_CLIENTS' 
    AND LTRIM(REPLACE(REPLACE(column_name,'CLNT',''),'_',' ')) != 'CODE'
    order by shtDesc; 
    RETURN v_cursor;
END getClientAttColumns;

FUNCTION getProductColumns(v_tpaCode    TQC_PRODUCT_ATTRIBUTES.TPA_CODE%TYPE) RETURN prod_Att_Column_ref IS
    v_cursor    prod_Att_Column_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT TCA_COL_NAME,TCA_ATT_RANGE,PCA_MIN_VALUE,PCA_MAX_VALUE,PCA_FIXED_VALUE,TCA_TABLE_NAME
    FROM TQC_PRODUCT_CLIENT_ATTRIBUTES,TQC_CLIENT_ATTRIBUTES
    WHERE PCA_TPA_CODE = v_tpaCode
    AND TCA_CODE = PCA_TCA_CODE;
    RETURN v_cursor;
END; 
END TQC_PRODUCT_PKG; 
/