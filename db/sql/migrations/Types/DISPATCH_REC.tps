--
-- DISPATCH_REC  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."DISPATCH_REC"                                          as object
        (DOCR_DISPATCH_DT    DATE,
        DOCR_DISPATCHABLE    VARCHAR2(3),
        DOCR_DISPATCHED      VARCHAR2(1),
        DOCR_POL_BATCH_NO    NUMBER,
        DOCR_ID              NUMBER); 
/