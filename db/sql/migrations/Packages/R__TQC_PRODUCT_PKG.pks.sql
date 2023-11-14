/* This object may not be sorted properly in the script due to cirular references. */
--
-- TQC_PRODUCT_PKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_product_pkg
AS
/******************************************************************************
   NAME:       TQC_PRODUCT_PKG
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        25-Jan-11             1. Created this package.
******************************************************************************/
   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL);

   FUNCTION myfunction (param1 IN NUMBER)
      RETURN NUMBER;

   PROCEDURE myprocedure (param1 IN NUMBER);

   PROCEDURE productattribute_prc (
      v_addedit               VARCHAR2,
      v_proatt_tab   IN       tqc_product_attributes_tab,
      v_err          OUT      VARCHAR2
   );

   TYPE product_rec IS RECORD (
      tpa_code             tqc_product_attributes.tpa_code%TYPE,
      tpa_system           tqc_product_attributes.tpa_system%TYPE,
      tpa_prod_code        tqc_product_attributes.tpa_prod_code%TYPE,
      tpa_prod_shtdesc     tqc_product_attributes.tpa_prod_shtdesc%TYPE,
      tpa_prod_desc        tqc_product_attributes.tpa_prod_desc%TYPE,
      tpa_prod_narration   tqc_product_attributes.tpa_prod_narration%TYPE,
      sys_name             tqc_systems.sys_name%TYPE
   );

   TYPE product_ref IS REF CURSOR
      RETURN product_rec;

   PROCEDURE get_productattributes (v_refcur OUT product_ref);

   TYPE sysproduct_rec IS RECORD (
      code          NUMBER,
      shortdesc     VARCHAR2 (100),
      description   VARCHAR2 (300)
   );

   TYPE sysproduct_ref IS REF CURSOR
      RETURN sysproduct_rec;

   PROCEDURE get_systemproducts (
      v_sys_code   IN       NUMBER,
      v_refcur     OUT      sysproduct_ref
   );

   TYPE clientattribute_rec IS RECORD (
      tca_code             tqc_client_attributes.tca_code%TYPE,
      tca_attribute_name   tqc_client_attributes.tca_attribute_name%TYPE,
      tca_att_desc         tqc_client_attributes.tca_att_desc%TYPE,
      tca_prompt           tqc_client_attributes.tca_prompt%TYPE,
      tca_att_range        tqc_client_attributes.tca_att_range%TYPE,
      tca_att_type_input   tqc_client_attributes.tca_att_type_input%TYPE,
      tca_table_name       tqc_client_attributes.tca_table_name%TYPE,
      tca_col_name         tqc_client_attributes.tca_col_name%TYPE,
      tca_col_alias        tqc_client_attributes.tca_col_name%TYPE
   );

   TYPE clientattribute_ref IS REF CURSOR
      RETURN clientattribute_rec;

   PROCEDURE clientattribute_prc (
      v_addedit               VARCHAR2,
      v_proatt_tab   IN       tqc_client_attributes_tab,
      v_err          OUT      VARCHAR2
   );

   PROCEDURE get_clientattributes (v_refcur OUT clientattribute_ref);

   PROCEDURE get_undefinedclientattributes (
      v_tpa_code   IN       tqc_product_attributes.tpa_code%TYPE,
      v_refcur     OUT      clientattribute_ref
   );

   PROCEDURE productclient_prc (
      v_add_edit          VARCHAR2,
      v_pca_code          tqc_product_client_attributes.pca_code%TYPE
            DEFAULT NULL,
      v_pca_tpa_code      tqc_product_client_attributes.pca_tpa_code%TYPE
            DEFAULT NULL,
      v_pca_tca_code      tqc_product_client_attributes.pca_tca_code%TYPE
            DEFAULT NULL,
      v_pca_min_value     tqc_product_client_attributes.pca_min_value%TYPE
            DEFAULT NULL,
      v_pca_max_value     tqc_product_client_attributes.pca_max_value%TYPE
            DEFAULT NULL,
      v_pca_fixed_value   tqc_product_client_attributes.pca_fixed_value%TYPE
            DEFAULT NULL
   );

   TYPE prodclient_rec IS RECORD (
      pca_code             tqc_product_client_attributes.pca_code%TYPE,
      pca_tpa_code         tqc_product_client_attributes.pca_tpa_code%TYPE,
      pca_tca_code         tqc_product_client_attributes.pca_tca_code%TYPE,
      tca_attribute_name   tqc_client_attributes.tca_attribute_name%TYPE,
      tca_att_range        tqc_client_attributes.tca_att_range%TYPE,
      pca_min_value        tqc_product_client_attributes.pca_min_value%TYPE,
      pca_max_value        tqc_product_client_attributes.pca_max_value%TYPE,
      pca_fixed_value      tqc_product_client_attributes.pca_fixed_value%TYPE,
      tca_col_name         tqc_client_attributes.tca_col_name%TYPE,
      tca_prompt           tqc_client_attributes.tca_prompt%TYPE
   );

   TYPE prodclient_ref IS REF CURSOR
      RETURN prodclient_rec;

   FUNCTION get_clients_per_product (v_col IN VARCHAR2)
      RETURN tqc_clients_pkg.clients_ref;

   PROCEDURE get_prod_client_attributes (
      v_tpa_code               tqc_product_attributes.tpa_code%TYPE,
      v_prodclient_cur   OUT   prodclient_ref
   );

   TYPE client_att_column_rec IS RECORD (
      column_name    dba_tab_columns.column_name%TYPE,
      column_alias   dba_tab_columns.column_name%TYPE,
      table_name     dba_tab_columns.table_name%TYPE
   );

   TYPE client_att_column_ref IS REF CURSOR
      RETURN client_att_column_rec;

   FUNCTION getclientattcolumns
      RETURN client_att_column_ref;

   TYPE prod_att_column_rec IS RECORD (
      column_name       dba_tab_columns.column_name%TYPE,
      tca_att_range     tqc_client_attributes.tca_att_range%TYPE,
      min_value         tqc_product_client_attributes.pca_min_value%TYPE,
      max_value         tqc_product_client_attributes.pca_max_value%TYPE,
      pca_fixed_value   tqc_product_client_attributes.pca_fixed_value%TYPE,
      table_name        dba_tab_columns.table_name%TYPE
   );

   TYPE prod_att_column_ref IS REF CURSOR
      RETURN prod_att_column_rec;

   FUNCTION getproductcolumns (v_tpacode tqc_product_attributes.tpa_code%TYPE)
      RETURN prod_att_column_ref;
END tqc_product_pkg; 
/