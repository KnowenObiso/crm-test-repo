--
-- FNC_MENU_REPLACE  (Procedure) 
--
CREATE OR REPLACE PROCEDURE TQ_CRM."FNC_MENU_REPLACE" (nSyscode IN NUMBER)  IS

CURSOR cur_menu IS SELECT menu_sys_code, menu_name, menu_label, menu_type, menu_obj_code, menu_menu_code
	   			   		  			    FROM TQC_MENU  WHERE menu_sys_code = nSyscode ORDER BY menu_code, menu_menu_code, menu_type DESC;

CURSOR cur_menu_menu(cMenuname IN VARCHAR2 ) IS SELECT menu_name FROM TQC_MENU
	   						   	  		   	 		   			 	  		   		  		   			  WHERE menu_code IN (SELECT menu_menu_code FROM TQC_MENU
																										  					   	  		  				 		   WHERE menu_name = cMenuname AND menu_sys_code = nSyscode);
v_insertstatmt1		   		  		 		   	VARCHAR2(500);
v_insertstatmt2 	   		  		 		   	VARCHAR2(500);
c_colmenu_code 			  		 		   	NUMBER;

BEGIN

	 FOR a IN cur_menu LOOP
	     SELECT TQC_MENU_CODE_SEQ.NEXTVAL INTO c_colmenu_code FROM dual ;

	 	 FOR b IN cur_menu_menu(a.menu_name) LOOP

			SELECT a.menu_sys_code||', ''' ||a.menu_name||''', '''||a.menu_label||''', '''||a.menu_type||''', '||DECODE(NVL(a.menu_obj_code,-1),-1,'NULL',a.menu_obj_code)||', ' || '(SELECT MENU_CODE FROM TQC_MENU WHERE MENU_NAME = '''||b.menu_name||''' AND MENU_SYS_CODE = 27)'||');',
			'INSERT INTO TQC_MENU (MENU_CODE, MENU_SYS_CODE, MENU_NAME, MENU_LABEL, MENU_TYPE, MENU_OBJ_CODE, MENU_MENU_CODE) VALUES(TQC_MENU_CODE_SEQ.NEXTVAL, '
			INTO v_insertstatmt1,  v_insertstatmt2
			FROM dual;
			DBMS_OUTPUT.PUT_LINE(v_insertstatmt2);
			DBMS_OUTPUT.PUT_LINE(v_insertstatmt1);

		END LOOP;
	END LOOP;
EXCEPTION
	WHEN OTHERS THEN
	   RAISE_APPLICATION_ERROR(-20015, SQLERRM(SQLCODE));
END;

 
 
 
 
 
 
 
 
 
 
 
 
 
 
/