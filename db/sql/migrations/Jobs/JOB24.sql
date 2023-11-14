--
-- JOB24  (Job) 
--
DECLARE
  X NUMBER;
  user_name varchar2(30);
BEGIN
  select user into user_name from dual;
  execute immediate 'alter session set current_schema = TQ_CRM';
  BEGIN
    SYS.DBMS_JOB.SUBMIT
    ( job       => X 
     ,what      => 'TQ_GIS.aki_reports_pkg.excecute_aki_reports;'
     ,next_date => TRUNC(SYSDATE+1)
     ,interval  => 'TRUNC(SYSDATE+1)'
     ,no_parse  => TRUE
    );
    execute immediate 'alter session set current_schema = ' || user_name ;
  EXCEPTION
    WHEN OTHERS THEN 
      execute immediate 'alter session set current_schema = ' || user_name ;
      RAISE;
  END;
  COMMIT;
END;
/