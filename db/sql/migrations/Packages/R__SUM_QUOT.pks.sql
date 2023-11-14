--
-- SUM_QUOT  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM."SUM_QUOT" AS
    QUOTE_CODE    number;
    SHOW_RATES    varchar2(1);
    VALIDITY_DAYS    varchar2(5);
    VALIDITY_CLAUSE    varchar2(200);
    V_QUOTE_CODE      number;
    V_QUOT_CODE      number;
    function ORIENTATIONValidTrigger return boolean  ;
    function AfterPForm return boolean  ;
    function CF_1Formula return Char  ;
    function CF_subjectFormula return Char  ;
    function CF_CARRIERFormula return Char  ;
    function cf_prem_amt_labelformula(QUOT_CUR_SYMBOL in varchar2) return char  ;
    function F_rateFormatTrigger(v_showrates  IN VARCHAR2) return VARCHAR2;
    
END Sum_Quot; 
/