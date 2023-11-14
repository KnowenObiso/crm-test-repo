--
-- TQC_ERROR_INSTANCE_IR  (Trigger) 
--
CREATE OR REPLACE TRIGGER TQ_CRM.TQC_ERROR_INSTANCE_IR 
   BEFORE INSERT
   ON TQ_CRM.TQC_ERROR_INSTANCE
   REFERENCING OLD AS old NEW AS new
   FOR EACH ROW
BEGIN
   :new.ERRI_created_on := SYSDATE;
   :new.ERRI_created_by := SYS_CONTEXT ('USERENV', 'SESSION_USER');

   :new.ERRI_environment_info :=
         'instance: '
      || SYS_CONTEXT ('USERENV', 'INSTANCE')
      --|| '/'
      --|| SYS_CONTEXT ('USERENV', 'INSTANCE_NAME')
      --|| CHR (10)
      || 'db_name: '
      || SYS_CONTEXT ('USERENV', 'DB_NAME')
      || CHR (10)
      || 'db_domain: '
      || SYS_CONTEXT ('USERENV', 'DB_DOMAIN')
      || CHR (10)
      --|| 'host: '
      --|| SYS_CONTEXT ('USERENV', 'SERVER_HOST')
      --|| CHR (10)
      --|| 'service_name: '
      --|| SYS_CONTEXT ('USERENV', 'SERVICE_NAME')
      --|| CHR (10)
      || '--'
      || CHR (10)
      || 'session_user: '
      || SYS_CONTEXT ('USERENV', 'SESSION_USER')
      || CHR (10)
      || 'session_id: '
      || SYS_CONTEXT ('USERENV', 'SESSIONID')
      || CHR (10)
      || '--'
      || CHR (10)
      || 'host: '
      || SYS_CONTEXT ('USERENV', 'HOST')
      || CHR (10)
      || 'ip_address: '
      || SYS_CONTEXT ('USERENV', 'IP_ADDRESS')
      || CHR (10)
      || 'os_user: '
      || SYS_CONTEXT ('USERENV', 'OS_USER')
      || CHR (10)
      || '--'
      || CHR (10)
      --|| 'module: '
      --|| SYS_CONTEXT ('USERENV', 'MODULE')
      --|| CHR (10)
      --|| 'action: '
      --|| SYS_CONTEXT ('USERENV', 'ACTION')
     -- || CHR (10)
      || 'client_identifier: '
      || SYS_CONTEXT ('USERENV', 'CLIENT_IDENTIFIER')
      || CHR (10)
      || 'client_info: '
      || SYS_CONTEXT ('USERENV', 'CLIENT_INFO')
      || CHR (10)
      || '--'
      || CHR (10)
      || 'bg_job_id: '
      || SYS_CONTEXT ('USERENV', 'BG_JOB_ID')
      || CHR (10)
      || 'fg_job_id: '
      || SYS_CONTEXT ('USERENV', 'FG_JOB_ID')
      || CHR (10);
END;
/