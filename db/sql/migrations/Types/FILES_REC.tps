--
-- FILES_REC  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."FILES_REC"                                          as object
        (file_path VARCHAR2(150),
        file_name VARCHAR2(50),
        DOCR_DISPATCH_DT    DATE,
        DOCR_DISPATCHABLE    VARCHAR2(3),
        DOCR_DISPATCHED      VARCHAR2(1),
        DOCR_POL_BATCH_NO    NUMBER,
        DOCR_ID              NUMBER); 
/