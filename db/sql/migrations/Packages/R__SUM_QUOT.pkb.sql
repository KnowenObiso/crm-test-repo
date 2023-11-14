--
-- SUM_QUOT  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM."SUM_QUOT" AS

function ORIENTATIONValidTrigger return boolean is
begin
  
  return (TRUE);
end;

function AfterPForm return boolean is
begin
  
  return (TRUE);
end;
function F_rateFormatTrigger(v_showrates  IN VARCHAR2) return VARCHAR2 is
begin
  
  if nvl(v_showrates,'Y') = 'Y' then
      return 'Y';
  else
      return 'N';
  end if;
end F_rateFormatTrigger;

function CF_1Formula return Char is
begin
  RETURN('NB: Quote is valid upto a maximum of '||VALIDITY_DAYS||' days from the date it is communicated. '||VALIDITY_CLAUSE);
end;

function CF_subjectFormula return Char is
begin
  return('''Subject to completion of the Premium Non-default Declaration Form''');
end;

function CF_CARRIERFormula return Char is
begin
  IF Tqc_Interfaces_Pkg.get_org_type(37) = 'INS' THEN 
        RETURN('Agency :');
    ELSE
        RETURN('Carrier :');
    END IF;
end;

function cf_prem_amt_labelformula(QUOT_CUR_SYMBOL in varchar2) return char is
    v_text VARCHAR2(100);
begin
    v_text := 'PREMIUM ('||QUOT_CUR_SYMBOL||')';
    
  return(v_text);
end;

--Functions to refer Oracle report placeholders--

END Sum_Quot ; 
/