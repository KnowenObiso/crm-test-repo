--
-- TQC_INCIDENCES  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_incidences
IS
   PROCEDURE incidents (
      v_user          IN   VARCHAR2,
      v_alloc         IN   VARCHAR2,
      v_batch         IN   NUMBER,
      v_clm           IN   VARCHAR2,
      v_quote         IN   VARCHAR2,
      v_status        IN   VARCHAR2,
      v_usersyscode   IN   NUMBER
   );

   PROCEDURE escal_incidence (
      nincscode   IN   NUMBER,
      v_esc_to    IN   VARCHAR2,
      v_remarks        VARCHAR2 DEFAULT ' '
   );

   PROCEDURE auto_escalation;

   PROCEDURE create_db_job (v_usersyscode IN NUMBER);

   PROCEDURE remove_job;

   v_job   NUMBER;

   PROCEDURE create_incident (
      v_user             IN   VARCHAR2,
      v_inc_type         IN   VARCHAR2,
      v_inc_desc         IN   VARCHAR2,
      v_status_remarks   IN   VARCHAR2,
      v_from             IN   VARCHAR2,
      v_alloc_to         IN   VARCHAR2,
      v_lvl              IN   VARCHAR2,
      v_batch            IN   NUMBER,
      v_clm_no           IN   VARCHAR2,
      v_quo_no           IN   VARCHAR2,
      v_status           IN   VARCHAR2,
      v_usersyscode      IN   NUMBER
   );

   PROCEDURE close_incident (
      v_lvl        IN   VARCHAR2,
      v_inc_type   IN   VARCHAR2,
      v_remarks    IN   VARCHAR2,
      v_batch      IN   NUMBER,
      v_clm_no     IN   VARCHAR2,
      v_quo_no     IN   VARCHAR2
   );
END tqc_incidences; 
/