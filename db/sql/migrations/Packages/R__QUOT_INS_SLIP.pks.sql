--
-- QUOT_INS_SLIP  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM."QUOT_INS_SLIP" AS
    BATCHNO    number;
    P_NOF_ITEMS    number;
    V_QUOT_CODE   number;
    V_AGN_CODE    VARCHAR2(30);
    CP_insurer    varchar2(200);
    function ORIENTATIONValidTrigger return boolean  ;
    function AfterPForm return boolean  ;
    function CF_insurerFormula return Char  ;
    function CF_onbehalfFormula return Char  ;
    function CF_CARRIERFormula return varchar2  ;
    function CF_CARRIER1Formula return Char  ;
    Function CP_insurer_p return varchar2;
END quot_ins_slip; 
/