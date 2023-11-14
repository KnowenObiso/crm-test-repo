--
-- TQC_REPORT_PKG170915  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.TQC_REPORT_PKG170915 AS
/******************************************************************************
   NAME:       TQC_REPORT_PKG
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        9/9/2011             1. Created this package.
******************************************************************************/

  
DATE_FROM date;
    DATE_TO  date;
    P_BBR NUMBER;
    P_REF_NO VARCHAR2(25);
    P_CLNT_CODE NUMBER(15);
    P_POLICY_NO VARCHAR2(25);
    P_USR_CODE NUMBER;
    P_SYS_CODE NUMBER;
    P_PARAM_CODE NUMBER;
END TQC_REPORT_PKG170915; 
 
/