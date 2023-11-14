--
-- GET_ROUND_PARAMETERS  (Procedure) 
--
CREATE OR REPLACE PROCEDURE TQ_CRM."GET_ROUND_PARAMETERS" (v_cur_code IN NUMBER,v_rnd_next OUT NUMBER,v_dec OUT NUMBER) IS
BEGIN
	SELECT NVL(CUR_RND,1),NVL(CUR_RND,2)
        INTO v_rnd_next,v_dec
	FROM tqc_currencies
	WHERE CUR_CODE = v_cur_code;
END ;

 
 
 
 
 
 
 
 
 
 
 
 
 
 
/