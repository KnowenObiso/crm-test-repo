--
-- PAD_AMOUNT  (Function) 
--
CREATE OR REPLACE FUNCTION TQ_CRM.pad_amount(v_amount NUMBER)
return varchar2
is
v_pad varchar2(2);
v_amt number;
begin

  select trunc(mod(v_amount, 1),2) into v_amt from dual;
  
      if v_amt = 0
    then
      v_pad :='00';
      
    else
      v_pad :=to_char(v_amt*100);

    end if;
    
    RETURN lpad(to_char(trunc(v_amount)||v_pad),13,'0');

end; 
 
/