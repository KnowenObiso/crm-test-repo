--
-- CESBLOB  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."CESBLOB"                                          as object (
   vblob blob,
   fileName varchar2(256),
   mimeType varchar2(256)
); 
/