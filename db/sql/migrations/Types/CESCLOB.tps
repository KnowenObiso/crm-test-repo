--
-- CESCLOB  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."CESCLOB"                                          as object (
   vclob clob,
   fileName varchar2(256),
   mimeType varchar2(256)
); 
/