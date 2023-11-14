--
-- CHECK_JBPM_CONST  (Trigger) 
--
CREATE OR REPLACE TRIGGER TQ_CRM.CHECK_JBPM_CONST 
   BEFORE DELETE
   ON TQ_CRM.JBPM4_EXECUTION    REFERENCING NEW AS NEW OLD AS OLD
   FOR EACH ROW
DECLARE
   v_count    NUMBER;
   v_ended    VARCHAR2 (30);
   v_ended2   VARCHAR2 (30);
BEGIN
   BEGIN
      SELECT state_
        INTO v_ended
        FROM jbpm4_hist_procinst
       WHERE id_ = :OLD.id_;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         BEGIN
            SELECT state_
              INTO v_ended2
              FROM jbpm4_execution
             WHERE id_ = :OLD.id_;
         EXCEPTION
            WHEN OTHERS
            THEN
               NULL;
         END;
      WHEN OTHERS
      THEN
         NULL;
   END;

   IF (v_ended = 'active') OR (v_ended2 = 'active-concurrent')
   THEN
      SELECT COUNT (1)
        INTO v_count
        FROM jbpm4_task
       WHERE execution_ = :OLD.id_;

      IF (v_count > 0)
      THEN
         raise_application_error (-20015,
                                  'Cannot delete when child record exist....'
                                 );
      END IF;
   END IF;
EXCEPTION
   WHEN OTHERS
   THEN
      raise_application_error (-20015,
                               'Error occured while deleting Workflow Record'
                              );
END;
/