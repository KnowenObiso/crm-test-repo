--
-- TQC_MEMOS_DBPKG010416  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_memos_dbpkg010416
AS
   /******************************************************************************
      NAME:       tqc_memos_pkg
      PURPOSE:

      REVISIONS:
      Ver        Date        Author           Description
      ---------  ----------  ---------------  ------------------------------------
      1.0        10/29/2007             1. Created this package.
   ******************************************************************************/
   PROCEDURE populate_memo_details (v_mtyp_code NUMBER, v_comem_code NUMBER);

   PROCEDURE del_memo_details (v_comem_code NUMBER);

   FUNCTION process_gis_pol_memo (
      v_pol_batch_no   IN   NUMBER,
      v_claim_no       IN   VARCHAR2,
      v_raw_txt        IN   VARCHAR2,
      v_app_lvl        IN   VARCHAR2,
      v_gcc_code       IN   NUMBER DEFAULT NULL,
      v_csd_code       IN   NUMBER DEFAULT NULL
   )
      RETURN VARCHAR2;
END tqc_memos_dbpkg010416; 
 
/