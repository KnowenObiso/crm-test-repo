--
-- ITEM_TRANSFER_REC  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."ITEM_TRANSFER_REC"                                          AS OBJECT
(             
                ITT_CODE               NUMBER,
                ITT_ITEM_CODE          NUMBER,
                ITT_TT_CODE            NUMBER,
                ITT_ITEM_NAME          VARCHAR2(100),
                ITT_ITEM_TYPE          VARCHAR2(20),
                ITT_ITEM_SHT_DESC          VARCHAR2(100)
) 
/