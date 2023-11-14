--
-- TQC_MEMOS_DBPKG130815  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.Tqc_Memos_Dbpkg130815 AS
  /******************************************************************************
     NAME:       tqc_memos_pkg
     PURPOSE:
  
     REVISIONS:
     Ver        Date        Author           Description
     ---------  ----------  ---------------  ------------------------------------
     1.0        10/29/2007             1. Created this package.
  ******************************************************************************/

  PROCEDURE Populate_Memo_Details(v_MTYP_CODE NUMBER, v_COMEM_CODE NUMBER);

  PROCEDURE Del_Memo_Details(v_COMEM_CODE NUMBER);
  FUNCTION PROCESS_GIS_POL_MEMO(v_pol_batch_no IN NUMBER,
                                v_claim_no     IN VARCHAR2,
                                v_gcc_code     IN NUMBER,
                                v_raw_txt      IN VARCHAR2,
                                v_app_lvl      IN VARCHAR2) RETURN VARCHAR2;

END Tqc_Memos_Dbpkg130815; 
 
/