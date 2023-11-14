--
-- TQC_INCIDENTS_PKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.Tqc_Incidents_Pkg IS
  PROCEDURE incidents(v_user         IN VARCHAR2,
                      v_alloc        IN VARCHAR2,
                      v_batch        IN NUMBER,
                      v_clm          IN VARCHAR2,
                      v_quote        IN VARCHAR2,
                      v_status       IN VARCHAR2,
                      v_usersyscode  IN NUMBER,
                      v_receipt_dt   IN DATE DEFAULT SYSDATE,
                      v_act_days     IN NUMBER DEFAULT NULL,
                      v_priority_lvl IN VARCHAR2 DEFAULT NULL);

  PROCEDURE escal_incidence(nincscode IN NUMBER,
                            v_esc_to  IN VARCHAR2,
                            v_remarks VARCHAR2 DEFAULT ' ');

  PROCEDURE auto_escalation;
  PROCEDURE auto_reminders;

  PROCEDURE create_db_job(v_usersyscode IN NUMBER);

  PROCEDURE remove_job;

  v_job NUMBER;
  FUNCTION get_incident_dflt_usr(v_inc_type IN VARCHAR2,
                                 v_brn_code IN NUMBER) RETURN VARCHAR2;
  PROCEDURE create_incident(v_user           IN VARCHAR2,
                            v_inc_type       IN VARCHAR2,
                            v_inc_desc       IN VARCHAR2,
                            v_status_remarks IN VARCHAR2,
                            v_from           IN VARCHAR2,
                            v_alloc_to       IN VARCHAR2,
                            v_lvl            IN VARCHAR2,
                            v_batch          IN NUMBER,
                            v_clm_no         IN VARCHAR2,
                            v_quo_no         IN VARCHAR2,
                            v_status         IN VARCHAR2,
                            v_usersyscode    IN NUMBER,
                            v_receipt_dt     IN DATE DEFAULT SYSDATE,
                            v_act_days       IN NUMBER DEFAULT NULL,
                            v_priority_lvl   IN VARCHAR2 DEFAULT NULL);

  PROCEDURE close_incident(v_lvl      IN VARCHAR2,
                           v_inc_type IN VARCHAR2,
                           v_remarks  IN VARCHAR2,
                           v_batch    IN NUMBER,
                           v_clm_no   IN VARCHAR2,
                           v_quo_no   IN VARCHAR2);
END Tqc_Incidents_Pkg; 
/