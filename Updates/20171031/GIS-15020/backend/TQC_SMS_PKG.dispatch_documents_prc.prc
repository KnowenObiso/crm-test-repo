--SPEC
PROCEDURE dispatch_documents_prc(
      v_addedit              VARCHAR2,
      v_alrt_code              NUMBER DEFAULT NULL,
      v_sht_desc              VARCHAR2 DEFAULT NULL,
      v_doc_desc              VARCHAR2 DEFAULT NULL,
      v_disptc_doc_code    VARCHAR2
);
--BODY
PROCEDURE dispatch_documents_prc(
      v_addedit              VARCHAR2,
      v_alrt_code              NUMBER DEFAULT NULL,
      v_sht_desc              VARCHAR2 DEFAULT NULL,
      v_doc_desc              VARCHAR2 DEFAULT NULL,
      v_disptc_doc_code    VARCHAR2
)
IS
BEGIN
    IF(v_addedit  = 'A')
    THEN
        BEGIN
            insert into tq_gis.gin_dispatch_attachments (da_code, da_alert_code, da_sht_desc, da_desc, da_dispatch_doc_code)
            values(tq_gis.da_code_seq.nextval , v_alrt_code, v_sht_desc, v_doc_desc,  v_disptc_doc_code);
         EXCEPTION
        WHEN OTHERS
        THEN
            raise_error('Error saving document details: '|| sqlerrm(sqlcode));
        END;
    ELSIF(v_addedit = 'D')
    THEN
        BEGIN
--        RAISE_ERROR('v_addedit:'||v_addedit);
            delete from tq_gis.gin_dispatch_attachments where da_dispatch_doc_code =  v_disptc_doc_code;
        EXCEPTION
        WHEN OTHERS
        THEN
            raise_error('Error deleting: '|| sqlerrm(sqlcode));
        END;
    END IF;
END;