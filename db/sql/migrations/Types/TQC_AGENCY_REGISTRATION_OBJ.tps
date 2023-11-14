--
-- TQC_AGENCY_REGISTRATION_OBJ  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."TQC_AGENCY_REGISTRATION_OBJ"                                          AS OBJECT
(
  AREG_CODE      NUMBER(8)                      ,
  AREG_AGN_CODE  NUMBER(8)                      ,
  AREG_YEAR      NUMBER(4)                      ,
  AREG_REG_NO    VARCHAR2(35 BYTE)              ,
  AREG_WEF       DATE                           ,
  AREG_WET       DATE                           ,
  AREG_ACCEPTED  VARCHAR2(1 BYTE)               
) 
/