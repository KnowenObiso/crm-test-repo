-- REPLACE   tqc_clients_pkg.getPreferedCommMode  

	FUNCTION getPreferedCommMode(v_clnt_code IN NUMBER, commMode IN VARCHAR2) 
        RETURN VARCHAR2
   IS
    v_mode      VARCHAR2(100);
   BEGIN
        
    IF commMode = 'TEL' THEN
        SELECT CLNT_TEL
        INTO v_mode
        FROM TQC_CLIENTS
        WHERE CLNT_CODE=v_clnt_code;
        
    ELSIF commMode = 'EMAIL' THEN
        SELECT CLNT_EMAIL_ADDRS
        INTO v_mode
        FROM TQC_CLIENTS
        WHERE CLNT_CODE=v_clnt_code;
    ELSIF commMode = 'PHONE' THEN
        SELECT CLNT_TEL2
        INTO v_mode
        FROM TQC_CLIENTS
        WHERE CLNT_CODE=v_clnt_code;
    ELSIF commMode = 'LETTER' THEN
        SELECT CLNT_POSTAL_ADDRS
        INTO v_mode
        FROM TQC_CLIENTS
        WHERE CLNT_CODE=v_clnt_code;
    END IF;
    
    RETURN  v_mode;
    
   EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RETURN NULL;
        
        WHEN OTHERS THEN
            raise_error('Error getting commucation mode '||SQLERRM(SQLCODE));
   END;