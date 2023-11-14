--
-- TQC_AGENCY_REFEREES_OBJ  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."TQC_AGENCY_REFEREES_OBJ"                                          AS OBJECT
(
  AREF_CODE              NUMBER                 ,
  AREF_NAME              VARCHAR2(50 BYTE),
  AREF_PHYSICAL_ADDRESS  VARCHAR2(50 BYTE),
  AREF_POSTAL_ADDRESS    VARCHAR2(50 BYTE),
  AREF_TWN_CODE          NUMBER,
  AREF_COU_CODE          NUMBER,
  AREF_EMAIL_ADDRESS     VARCHAR2(50 BYTE),
  AREF_TEL               VARCHAR2(50 BYTE),
  AREF_ID_NO             VARCHAR2(50 BYTE),
  AREF_AGN_CODE          NUMBER
); 
/