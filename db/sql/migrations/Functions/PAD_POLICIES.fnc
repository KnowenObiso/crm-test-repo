--
-- PAD_POLICIES  (Function) 
--
CREATE OR REPLACE FUNCTION TQ_CRM.PAD_POLICIES(v_text VARCHAR2) 
RETURN VARCHAR2
IS
X NUMBER;
CNT NUMBER;
retVal VARCHAR2(100);
v_pol1 VARCHAR2(21);
CURSOR A 
IS
   select regexp_substr(v_text,'[^/]+', 1, level) AS TEST from dual
       connect by regexp_substr(v_text, '[^/]+', 1, level) is not null;
    BEGIN
    
     
      FOR C IN A
      LOOP
         IF NVL(X,0) = 0
         THEN
         retVal :=RPAD(C.TEST,15,'X');
         v_pol1 :=RPAD(C.TEST,15,'X');
         ELSIF LENGTH(TRIM(C.TEST))!=0 AND NVL(X,0) = 1 
         THEN
          retVal :=retVal||RPAD(C.TEST,15,'X')||LPAD(' ',5,'X')||'0';
         ELSIF LENGTH(TRIM(C.TEST))!=0 AND NVL(X,0) = 2   
         THEN
          retVal :=retVal||RPAD(C.TEST,15,'X')||LPAD(' ',6,'X');
         ELSE
            retVal :=retVal||RPAD(C.TEST,15,'X')||LPAD('0',6,'X');
         END IF;
         
         X := NVL(X,0)+1;
      
      END LOOP;
      
     -- RAISE_APPLICATION_ERROR(-20001,X);
      
      IF X =  1
      THEN
       v_pol1 :=v_pol1||LPAD('0',6,'X');
       retVal := retVal||v_pol1||LPAD('0',20,' ')||LPAD('0',25,' ');
     
      ELSIF  X =  2
      THEN
      
         retVal := retVal||LPAD('0',20,' ')||LPAD('0',25,' ');
      
      ELSIF  X =  3
      THEN
         retVal := retVal||LPAD('0',25,' ');
         
      ELSIF  X =  4
      THEN
         retVal := retVal;
      
      END IF; 
      
      
      RETURN REPLACE(retVal,'X',' ');
    END; 
 
/