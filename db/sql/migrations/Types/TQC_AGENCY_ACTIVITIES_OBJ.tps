--
-- TQC_AGENCY_ACTIVITIES_OBJ  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."TQC_AGENCY_ACTIVITIES_OBJ"                                          AS OBJECT (
   aac_code            NUMBER (15),
   aac_acty_code       NUMBER (8),
   aac_wef             DATE,
   aac_estimate_wet    DATE,
   aac_actual_wet      DATE,
   aac_remarks         VARCHAR2 (500 BYTE),
   aac_agn_code        NUMBER (8),
   aac_sys_code        NUMBER (8),  
   aac_reasons         varchar2(300),
   aac_act_type        varchar2(300)  
); 
/