--
-- TQC_INCIDENCES  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_incidences
AS
   PROCEDURE raise_error (cmsg IN VARCHAR2, nerrno IN NUMBER := NULL)
   IS
   BEGIN
      IF nerrno IS NULL
      THEN
         raise_application_error (-20015, cmsg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         raise_application_error (nerrno, cmsg || ' - ' || SQLERRM (SQLCODE));
      END IF;
   END raise_error;

   PROCEDURE create_db_job (v_usersyscode IN NUMBER)
   IS
      vescalation_interval   NUMBER;
   BEGIN
      BEGIN
         IF v_usersyscode = 37
         THEN
            SELECT NVL (TO_NUMBER (param_value), 1)
              INTO vescalation_interval
              FROM tqc_parameters
             WHERE param_name = 'ESCALATION_INTERVAL';
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            raise_error
                ('THE SYSTEM COULD NOT FIND PARAMETER {ESCALATION_INTERVAL}.');
         WHEN OTHERS
         THEN
            raise_error
               ('A VALUE FOR THE SYSTEM PARAMETER {ESCALATION_INTERVAL} HAS TO BE DEFINED.'
               );
      END;

      DBMS_JOB.submit (v_job,
                       'GIN_INCIDENTS.AUTO_ESCALATION',
                       SYSDATE,
                       'SYSDATE + (' || vescalation_interval || ')'
                      );
      COMMIT;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('AUTO ESCALATION JOB COULD NOT BE CREATED..');
   END;

   PROCEDURE remove_job
   IS
   BEGIN
      DBMS_JOB.remove (v_job);
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('AUTO ESCALATION JOB COULD NOT BE REMOVED..');
   END;

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
   )
   IS
      v_brn_code       NUMBER;
      v_brn_sht_desc   VARCHAR2 (15);
      v_pol_batch_no   VARCHAR2 (30);
      v_policy_no      VARCHAR2 (30);
      v_endos_no       VARCHAR2 (30);
      v_seq            NUMBER;
      v_inc_desc2      VARCHAR2 (200);
      v_status_desc    VARCHAR2 (200);
      v_received_by    VARCHAR2 (30);
      v_address_to     VARCHAR2 (30);
      v_cnt            NUMBER         := 0;
      v_send_mail      VARCHAR2 (1);
      v_sendor         VARCHAR2 (70);
      v_recipient      VARCHAR2 (70);
      v_priority       VARCHAR2 (20);
      v_mailhost       VARCHAR2 (200);
      v_incs_code      NUMBER;
   BEGIN
      v_address_to := v_alloc_to;
      v_received_by := v_user;

      IF v_lvl = 'U'
      THEN
         IF v_usersyscode = 37
         THEN
            BEGIN
               SELECT pol_brn_code, pol_brn_sht_desc, pol_batch_no,
                      pol_policy_no, pol_ren_endos_no
                 INTO v_brn_code, v_brn_sht_desc, v_pol_batch_no,
                      v_policy_no, v_endos_no
                 FROM gin_policies
                WHERE pol_batch_no = v_batch;
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('ERROR RETRIEVING POLICY DETAILS..');
            END;
         END IF;

         BEGIN
            SELECT COUNT (1)
              INTO v_cnt
              FROM tqc_incidents
             WHERE inc_pol_code = v_pol_batch_no
               AND inc_inct_sht_desc = v_inc_type;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR DETERMINING DUPLICATE INCIDENCE..');
         END;

         IF NVL (v_cnt, 0) > 1
         THEN
            DELETE      tqc_incident_status
                  WHERE incs_inc_code =
                           (SELECT inc_code
                              FROM tqc_incidents
                             WHERE inc_pol_code = v_pol_batch_no
                               AND inc_inct_sht_desc = v_inc_type);

            DELETE      tqc_incidents
                  WHERE inc_pol_code = v_pol_batch_no
                    AND inc_inct_sht_desc = v_inc_type;
         END IF;

         v_inc_desc2 := 'P' || '::' || v_policy_no || '::' || v_inc_desc;
         v_status_desc :=
                        'P' || '::' || v_policy_no || '::' || v_status_remarks;
      ELSIF v_lvl = 'C'
      THEN
         BEGIN
            SELECT cmb_brn_code, cmb_brn_sht_desc, cmb_pol_batch_no,
                   cmb_pol_policy_no, cmb_pol_ren_endos_no
              INTO v_brn_code, v_brn_sht_desc, v_pol_batch_no,
                   v_policy_no, v_endos_no
              FROM gin_claim_master_bookings
             WHERE cmb_claim_no = v_clm_no;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR RETRIEVING CLAIM DETAILS..');
         END;

         BEGIN
            SELECT COUNT (1)
              INTO v_cnt
              FROM tqc_incidents
             WHERE inc_clm_no = v_clm_no AND inc_inct_sht_desc = v_inc_type;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR DETERMINING DUPLICATE INCIDENCE..');
         END;

         IF NVL (v_cnt, 0) > 1
         THEN
            DELETE      tqc_incident_status
                  WHERE incs_inc_code =
                           (SELECT inc_code
                              FROM tqc_incidents
                             WHERE inc_clm_no = v_clm_no
                               AND inc_inct_sht_desc = v_inc_type);

            DELETE      tqc_incidents
                  WHERE inc_clm_no = v_clm_no
                    AND inc_inct_sht_desc = v_inc_type;
         END IF;

         v_inc_desc2 := 'C' || '::' || v_clm_no || '::' || v_inc_desc;
         v_status_desc := 'C' || '::' || v_clm_no || '::' || v_status_remarks;
      ELSIF v_lvl = 'Q'
      THEN
         BEGIN
            SELECT quot_brn_code, quot_no
              INTO v_brn_code, v_brn_sht_desc
              FROM tq_gis.gin_quotations, tqc_branches
             WHERE quot_brn_code = brn_code AND quot_no = v_quo_no;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR RETRIEVING QUOTATION DETAILS..');
         END;

         BEGIN
            SELECT COUNT (1)
              INTO v_cnt
              FROM tqc_incidents
             WHERE inc_quot_no = v_quo_no AND inc_inct_sht_desc = v_inc_type;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR DETERMINING DUPLICATE INCIDENCE..');
         END;

         IF NVL (v_cnt, 0) > 1
         THEN
            DELETE      tqc_incident_status
                  WHERE incs_inc_code =
                           (SELECT inc_code
                              FROM tqc_incidents
                             WHERE inc_quot_no = v_quo_no
                               AND inc_inct_sht_desc = v_inc_type);

            DELETE      tqc_incidents
                  WHERE inc_quot_no = v_quo_no
                    AND inc_inct_sht_desc = v_inc_type;
         END IF;

         v_inc_desc2 := 'Q' || '::' || v_quo_no || '::' || v_inc_desc;
         v_status_desc := 'Q' || '::' || v_quo_no || '::' || v_status_remarks;
      ELSE
         raise_error ('ERROR INCIDENCE MODULE NOT DEFINED...');
      END IF;

      BEGIN
         SELECT COUNT (1)
           INTO v_cnt
           FROM tqc_dep_incident_types, tqc_inc_departments
          WHERE idep_code = depict_idep_code
            AND idep_brn_code = v_brn_code
            AND depict_inct_sht_desc = v_inc_type;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            raise_error (   'ERROR: INCIDENT TYPE '
                         || v_inc_type
                         || ' NOT DEFINED IN BRANCH '
                         || v_brn_sht_desc
                        );
         WHEN OTHERS
         THEN
            raise_error (   'ERROR DETERMINING IF INCIDENT TYPE '
                         || v_inc_type
                         || ' HAS BEEN DEFINED IN BRANCH '
                         || v_brn_sht_desc
                        );
      END;

      BEGIN
         SELECT COUNT (1)
           INTO v_cnt
           FROM tqc_dep_incident_types, tqc_inc_departments
          WHERE idep_code = depict_idep_code
            AND idep_brn_code = v_brn_code
            AND depict_inct_sht_desc = v_inc_type;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            raise_error (   'INCIDENT TYPE '
                         || v_inc_type
                         || ' IS NOT DEFINED IN BRANCH '
                         || v_brn_code
                        );
         WHEN OTHERS
         THEN
            raise_error (   'ERROR CHECKING IF INCIDENT TYPE '
                         || v_inc_type
                         || ' IS DEFINED IN BRANCH '
                         || v_brn_code
                        );
      END;

      BEGIN
         SELECT    TO_NUMBER (TO_CHAR (SYSDATE, 'RRRR'))
                || tqc_mail_code_seq.NEXTVAL
           INTO v_seq
           FROM DUAL;

--raise_error(' V_BRN_CODE='||v_brn_code||' V_INC_TYPE='||v_inc_type);
         INSERT INTO tqc_incidents
                     (inc_code, inc_inct_code, inc_inct_sht_desc, inc_date,
                      inc_idep_code, inc_addressed_to, inc_remarks,
                      inc_received_by, inc_brn_code, inc_pol_code,
                      inc_pol_policy_no, inc_pol_ren_endos_no, inc_clm_no,
                      inc_origin, incc_mailed, inc_quot_no, inc_idep_sht_desc,
                      inc_depict_code, inc_sht_desc, inc_send_email)
            SELECT v_seq, depict_inct_code, depict_inct_sht_desc, SYSDATE,
                   idep_code, v_address_to, v_inc_desc2, v_received_by,
                   v_brn_code, v_pol_batch_no, v_policy_no, v_endos_no,
                   v_clm_no, 'M', 'N', v_quo_no, NULL, depict_code,
                   v_inc_desc2, NVL (depict_send_email, 'N')
              FROM tqc_dep_incident_types, tqc_inc_departments
             WHERE idep_code = depict_idep_code
               AND idep_brn_code = v_brn_code
               AND depict_inct_sht_desc = v_inc_type;

         SELECT    TO_NUMBER (TO_CHAR (SYSDATE, 'RRRR'))
                || tqc_incs_code_seq.NEXTVAL
           INTO v_incs_code
           FROM DUAL;

         INSERT INTO tqc_incident_status
                     (incs_alloc_date, incs_alloc_to, incs_code,
                      incs_comments, incs_escalation_date, incs_exp_act_date,
                      incs_icty_by, incs_icty_code, incs_icty_sht_desc,
                      incs_inc_code, incs_acknowledged, incs_icty_date,
                      incs_priority, incs_referal_incs_code, incs_status,
                      incs_status_date, incs_escalate, incs_current_status)
            SELECT SYSDATE, v_alloc_to, v_incs_code, v_status_desc,
                   SYSDATE + NVL (depict_esc_days, 2),
                   SYSDATE + NVL (depict_close_days, 4), v_user, 11, 'O',
                   v_seq, 'N', SYSDATE, depict_priority_lvl, NULL, 'P',
                   SYSDATE, NVL (depict_escalate, 'N'), 'Y'
              FROM tqc_dep_incident_types, tqc_inc_departments
             WHERE idep_code = depict_idep_code
               AND idep_brn_code = v_brn_code
               AND depict_inct_sht_desc = v_inc_type;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            raise_error (   'ERROR: INCIDENT TYPE '
                         || v_inc_type
                         || ' NOT DEFINED IN BRANCH '
                         || v_brn_sht_desc
                        );
         WHEN OTHERS
         THEN
            raise_error ('ERROR CREATING INCIDENT..');
      END;

      BEGIN
         SELECT NVL (inc_send_email, 'N'),
                DECODE (incs_priority, 'H', 'HIGH', 'M', 'MEDIUM', 'LOW')
           INTO v_send_mail,
                v_priority
           FROM tqc_incident_status, tqc_incidents
          WHERE inc_code = incs_inc_code AND incs_code = v_incs_code;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_send_mail := 'N';
            v_priority := 'LOW';
         WHEN OTHERS
         THEN
            raise_error (   'ERROR PREPARING TO PROCESS EMAIL FOR INCIDENT..'
                         || v_incs_code
                        );
      END;

       /*BEGIN
              SELECT param_value
                INTO v_send_mail
                FROM TQC_PARAMETERS
               WHERE param_name = 'ENABLE_EMAIL_SENDING';
       EXCEPTION
             WHEN OTHERS THEN
             RAISE_ERROR('Error Parameter setup....');
       END;
      raise_error(v_send_mail);*/
      IF NVL (v_send_mail, 'N') = 'Y'
      THEN
         BEGIN
            SELECT param_value
              INTO v_mailhost
              FROM tqc_parameters
             WHERE param_name = 'MAILHOST';

            SELECT usr_email
              INTO v_sendor
              FROM tqc_users
             WHERE usr_username = v_user;

            SELECT usr_email
              INTO v_recipient
              FROM tqc_users
             WHERE usr_username = v_alloc_to;

            IF v_sendor IS NOT NULL AND v_recipient IS NOT NULL
            THEN
               sendmail (v_sendor,
                         v_recipient,
                            'YOU HAVE A '
                         || v_priority
                         || ' PRIORITY '
                         || v_inc_desc2
                         || ' INCIDENT ',
                         v_status_desc,
                         v_mailhost,
                         NULL
                        );
            END IF;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR SENDING INCIDENT EMAIL TO ' || v_recipient);
         END;
      END IF;
   END;

   PROCEDURE close_incident (
      v_lvl        IN   VARCHAR2,
      v_inc_type   IN   VARCHAR2,
      v_remarks    IN   VARCHAR2,
      v_batch      IN   NUMBER,
      v_clm_no     IN   VARCHAR2,
      v_quo_no     IN   VARCHAR2
   )
   IS
   BEGIN
      IF v_lvl = 'U'
      THEN
         BEGIN
            UPDATE tqc_incident_status
               SET incs_acknowledged = 'Y',
                   incs_status = 'C',
                   incs_status_date = SYSDATE,
                   incs_comments = incs_comments || '::' || v_remarks
             WHERE incs_inc_code IN (
                      SELECT inc_code
                        FROM tqc_incidents, tqc_dep_incident_types
                       WHERE inc_depict_code = depict_code
                         AND inc_pol_code = v_batch
                         AND incs_inc_code = inc_code
                         AND depict_inct_sht_desc = v_inc_type);
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               BEGIN
                  UPDATE tqc_incident_status
                     SET incs_acknowledged = 'Y',
                         incs_status = 'C',
                         incs_status_date = SYSDATE,
                         incs_comments = incs_comments || '::' || v_remarks
                   WHERE incs_inc_code IN (
                            SELECT inc_code
                              FROM tqc_incidents, tqc_dep_incident_types
                             WHERE inc_depict_code = depict_code
                               AND inc_ppr_code = v_batch
                               AND incs_inc_code = inc_code
                               AND depict_inct_sht_desc = v_inc_type);
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     NULL;
                  WHEN OTHERS
                  THEN
                     raise_error
                        ('THERE WAS AN ERROR IN LOGGING THIS PROPOSAL INCIDENCE APPROVAL'
                        );
               END;
            WHEN OTHERS
            THEN
               ROLLBACK;
               raise_error
                  ('THERE WAS AN ERROR IN LOGGING THIS POLICY INCIDENCE APPROVAL'
                  );
         END;
      ELSIF v_lvl = 'Q'
      THEN
         BEGIN
            UPDATE tqc_incident_status
               SET incs_acknowledged = 'Y',
                   incs_status = 'C',
                   incs_status_date = SYSDATE
             WHERE incs_inc_code IN (
                      SELECT inc_code
                        FROM tqc_incidents, tqc_dep_incident_types
                       WHERE inc_depict_code = depict_code
                         AND inc_quot_no = v_quo_no
                         AND incs_inc_code = inc_code
                         AND depict_inct_sht_desc = v_inc_type);
         EXCEPTION
            WHEN OTHERS
            THEN
               ROLLBACK;
               raise_error
                  ('THERE WAS AN ERROR IN LOGGING THIS POLICY INCIDENCE APPROVAL'
                  );
         END;
      ELSIF v_lvl = 'Q'
      THEN
         BEGIN
            UPDATE tqc_incident_status
               SET incs_acknowledged = 'Y',
                   incs_status = 'C',
                   incs_status_date = SYSDATE
             WHERE incs_inc_code IN (
                      SELECT inc_code
                        FROM tqc_incidents, tqc_dep_incident_types
                       WHERE inc_depict_code = depict_code
                         AND inc_clm_no = v_clm_no
                         AND incs_inc_code = inc_code
                         AND depict_inct_sht_desc = v_inc_type);
         EXCEPTION
            WHEN OTHERS
            THEN
               ROLLBACK;
               raise_error
                  ('THERE WAS AN ERROR IN LOGGING THIS POLICY INCIDENCE APPROVAL'
                  );
         END;
      END IF;
   END;

   PROCEDURE incidents (
      v_user          IN   VARCHAR2,
      v_alloc         IN   VARCHAR2,
      v_batch         IN   NUMBER,
      v_clm           IN   VARCHAR2,
      v_quote         IN   VARCHAR2,
      v_status        IN   VARCHAR2,
      v_usersyscode   IN   NUMBER
   )
   IS
      v_brn              NUMBER;
      v_dep              NUMBER;
      v_inc_code         NUMBER;
      v_inc_desc         VARCHAR2 (20);
      v_seq              NUMBER (10);
      ncnt               NUMBER (10);
      v_depict_code      NUMBER;
      v_inc_escalate     VARCHAR2 (2);
      v_inc_esc_days     NUMBER;
      v_inc_close_days   NUMBER;
   BEGIN
      IF v_usersyscode = 37
      THEN
         IF v_batch IS NOT NULL AND v_status = 'BA'
         THEN                      --at underwriting and before authorization
            BEGIN
               create_incident (v_user,
                                'U_AUTH',
                                'U/W READY FOR AUTHORIZATION',
                                'U/W READY FOR AUTHORIZATION',
                                v_user,
                                v_alloc,
                                'U',
                                v_batch,
                                v_clm,
                                v_quote,
                                v_status,
                                v_usersyscode
                               );
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('ERROR CREATING INCIDENT..');
            END;
         ELSIF v_batch IS NOT NULL AND v_status = 'A'
         THEN                        --at underwriting and after authorization
            BEGIN
               create_incident (v_user,
                                'UWDOCS',
                                'PRINT U/W DOCS',
                                'HAVE ALL U/W DOCS BEEN PRINTED',
                                v_user,
                                v_alloc,
                                'U',
                                v_batch,
                                v_clm,
                                v_quote,
                                v_status,
                                v_usersyscode
                               );
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('ERROR CREATING U/W DOCS INCIDENT..');
            END;

            BEGIN
               close_incident ('U',
                               'U_AUTH',
                               'AUTHORISED BY ' || v_user,
                               v_batch,
                               v_clm,
                               v_quote
                              );
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('ERROR CLOSING MAKE READY INCIDENT..');
            END;
         ELSIF v_quote IS NOT NULL AND v_status = 'QA'
         THEN
            BEGIN
               close_incident ('Q',
                               'Q_AUTH',
                               'AUTHORISED BY ' || v_user,
                               v_batch,
                               v_clm,
                               v_quote
                              );
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('ERROR CLOSING INCIDENT..');
            END;
         ELSIF v_quote IS NOT NULL AND v_status = 'QR'
         THEN
            BEGIN
               close_incident ('Q',
                               'Q_AUTH',
                               'MAKE READY REVOKED : ' || v_user,
                               v_batch,
                               v_clm,
                               v_quote
                              );
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('ERROR CLOSING INCIDENT..');
            END;
         ELSIF v_quote IS NOT NULL AND v_status = 'Q'
         THEN
            BEGIN
               create_incident (v_user,
                                'QUOTAPR',
                                'QUOTATION READY FOR AUTHORISATION',
                                'QUOTATION READY FOR AUTHORISATION',
                                v_user,
                                v_alloc,
                                'Q',
                                v_batch,
                                v_clm,
                                v_quote,
                                v_status,
                                v_usersyscode
                               );
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('ERROR CREATING INCIDENT..');
            END;
         ELSIF v_clm IS NOT NULL AND v_status = 'C'
         THEN
            BEGIN
               create_incident (v_user,
                                'CLMAPR',
                                'CLAIM READY FOR AUTHORISATION',
                                'CLAIM READY FOR AUTHORISATION',
                                v_user,
                                v_alloc,
                                'C',
                                v_batch,
                                v_clm,
                                v_quote,
                                v_status,
                                v_usersyscode
                               );
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('ERROR CREATING INCIDENT..');
            END;
         ELSIF v_clm IS NOT NULL AND v_status = 'CA'
         THEN
            BEGIN
               close_incident ('C',
                               'CLMAPR',
                               'AUTHORISED BY ' || v_user,
                               v_batch,
                               v_clm,
                               v_quote
                              );
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('ERROR CLOSING INCIDENT..');
            END;
         ELSIF v_clm IS NOT NULL AND v_status = 'CR'
         THEN                        --at claims and after receipting recovery
            BEGIN
               create_incident
                        (v_user,
                         'RCVRYRCT',
                         'RECEIPTED AND NEEDS A RECOVERY REGISTERED',
                         'A RECOVERY HAS BEEN RECEIPTED AND AWAITS BOOKING.',
                         v_user,
                         v_alloc,
                         'C',
                         v_batch,
                         v_clm,
                         v_quote,
                         v_status,
                         v_usersyscode
                        );
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('ERROR CREATING CLAIMS RECOVERY INCIDENT..');
            END;
         END IF;
      END IF;
   END;

   PROCEDURE incidents_bak (
      v_user     IN   VARCHAR2,
      v_alloc    IN   VARCHAR2,
      v_batch    IN   NUMBER,
      v_clm      IN   VARCHAR2,
      v_quote    IN   VARCHAR2,
      v_status   IN   VARCHAR2
   )
   IS
      v_brn              NUMBER;
      v_dep              NUMBER;
      v_inc_code         NUMBER;
      v_inc_desc         VARCHAR2 (20);
      v_seq              NUMBER (10);
      ncnt               NUMBER (10);
      v_depict_code      NUMBER;
      v_inc_escalate     VARCHAR2 (2);
      v_inc_esc_days     NUMBER;
      v_inc_close_days   NUMBER;

      CURSOR inc_cur (v_batch NUMBER)
      IS
         SELECT pol_batch_no, pol_brn_code, pol_brn_sht_desc, pol_policy_no,
                pol_ren_endos_no
           FROM gin_policies
          WHERE pol_batch_no = v_batch;

      CURSOR quo_cur (v_quote NUMBER)
      IS
         SELECT quot_brn_code, quot_no
           FROM tq_gis.gin_quotations
          WHERE quot_no = v_quote;

      CURSOR clm_cur (v_clm VARCHAR2)
      IS
         SELECT cmb_brn_code, cmb_claim_no
           FROM gin_claim_master_bookings
          WHERE cmb_claim_no = v_clm;

      CURSOR brn_cur (v_brn NUMBER, v_inc VARCHAR2)
      IS
         SELECT DISTINCT idep_code, idep_brn_code
                    FROM tqc_inc_departments, tqc_dep_incident_types
                   WHERE idep_code = depict_idep_code
                     AND idep_brn_code = v_brn
                     AND depict_inct_sht_desc = v_inc;

      CURSOR incidence_cur (v_inc VARCHAR2, v_brn NUMBER)
      IS
         SELECT depict_inct_code, depict_inct_sht_desc, idep_brn_code,
                depict_escalate, depict_esc_days, depict_close_days,
                depict_code
           FROM tqc_dep_incident_types, tqc_inc_departments
          WHERE idep_code = depict_idep_code
            AND depict_inct_sht_desc = v_inc
            AND idep_brn_code = v_brn;

      CURSOR incs_status_cur (param VARCHAR2)
      IS
         SELECT *
           FROM tqc_incident_status
          WHERE incs_inc_code IN (
                   SELECT inc_code
                     FROM tqc_incidents
                    WHERE     inc_inct_sht_desc IN
                                              ('QUOTAPR', 'CLMAPR', 'UWDOCS')
                          AND inc_clm_no = param
                       OR TO_CHAR (inc_pol_code) = (param)
                       OR TO_CHAR (inc_quot_no) = (param));
   BEGIN
      IF v_batch IS NOT NULL AND v_status = 'B'
      THEN
         FOR inc_rec IN inc_cur (v_batch)
         LOOP
            v_brn := inc_rec.pol_brn_code;

            FOR brn_cur_rec IN brn_cur (inc_rec.pol_brn_code, 'UWDOCS')
            LOOP
               v_dep := brn_cur_rec.idep_code;

               IF v_dep IS NULL
               THEN
                  raise_error
                     (   'THERE APPEARS TO BE NO INCIDENCE TYPE DEFINED FOR THAT DEPARTMENT ON POLICY '
                      || v_batch
                      || ' PLEASE HAVE IT DEFINED '
                     );
               END IF;
            END LOOP;

            FOR incidence_cur_rec IN incidence_cur ('UWDOCS', v_brn)
            LOOP
               BEGIN
                  v_depict_code := incidence_cur_rec.depict_code;
                  v_inc_code := incidence_cur_rec.depict_inct_code;
                  v_inc_desc := incidence_cur_rec.depict_inct_sht_desc;
                  v_inc_escalate :=
                                 NVL (incidence_cur_rec.depict_escalate, 'N');
                  v_inc_esc_days :=
                                   NVL (incidence_cur_rec.depict_esc_days, 2);
                  v_inc_close_days :=
                                 NVL (incidence_cur_rec.depict_close_days, 4);

                  IF v_inc_code IS NULL OR v_inc_desc IS NULL
                  THEN
                     raise_error
                        (   'THERE APPEARS TO BE NO INCIDENCE TYPE DEFINED FOR THAT BRANCH ON POLICY '
                         || v_batch
                         || ' PLEASE HAVE IT DEFINED '
                        );
                  END IF;
               END;
            END LOOP;

            ---raise_application_error(-20015,' brn '||v_brn||' v_dep '||v_dep||' incidence type '||v_inc_desc);
            SELECT COUNT (*)
              INTO ncnt
              FROM tqc_incidents
             WHERE inc_pol_code = v_batch;

            IF ncnt > 1
            THEN
               NULL;
            ELSE
               SELECT    TO_NUMBER (TO_CHAR (SYSDATE, 'RRRR'))
                      || tqc_mail_code_seq.NEXTVAL
                 INTO v_seq
                 FROM DUAL;

               INSERT INTO tqc_incidents
                           (inc_code, inc_inct_code, inc_inct_sht_desc,
                            inc_date, inc_idep_code, inc_addressed_to,
                            inc_remarks, inc_received_by, inc_brn_code,
                            inc_pol_code, inc_pol_policy_no,
                            inc_pol_ren_endos_no, inc_clm_no, inc_origin,
                            incc_mailed, inc_quot_no, inc_idep_sht_desc,
                            inc_depict_code,
                            inc_sht_desc
                           )
                    VALUES (v_seq, v_inc_code, v_inc_desc,
                            SYSDATE, v_dep, v_alloc,
                            'UNDERWRITING DOCUMENTS', v_user, v_brn,
                            inc_rec.pol_batch_no, inc_rec.pol_policy_no,
                            inc_rec.pol_ren_endos_no, v_clm, 'M',
                            'N', v_quote, NULL,
                            v_depict_code,
                            inc_rec.pol_policy_no || ' UNDERWRITING DOCUMENTS'
                           );

               INSERT INTO tqc_incident_status
                           (incs_alloc_date, incs_alloc_to,
                            incs_code,
                            incs_comments,
                            incs_escalation_date,
                            incs_exp_act_date, incs_icty_by, incs_icty_code,
                            incs_icty_sht_desc, incs_inc_code,
                            incs_acknowledged, incs_icty_date, incs_priority,
                            incs_referal_incs_code, incs_status,
                            incs_status_date, incs_escalate,
                            incs_current_status
                           )
                    VALUES (SYSDATE, v_alloc,
                               TO_NUMBER (TO_CHAR (SYSDATE, 'RRRR'))
                            || tqc_incs_code_seq.NEXTVAL,
                               'PRINTED ALL U/W DOCS FOR ['
                            || inc_rec.pol_policy_no
                            || '::'
                            || inc_rec.pol_ren_endos_no
                            || ']?',
                            SYSDATE + v_inc_esc_days,
                            SYSDATE + v_inc_close_days, v_user, 1,
                            'O', v_seq,
                            'N', SYSDATE, 'M',
                            NULL, 'P',
                            SYSDATE, v_inc_escalate,
                            'Y'
                           );
            END IF;
         END LOOP;
      ELSIF v_batch IS NOT NULL AND v_status = 'BA'
      THEN
         FOR incs_status_rec IN incs_status_cur (v_batch)
         LOOP
            BEGIN
               IF incs_status_rec.incs_acknowledged = 'Y'
               THEN
                  NULL;
               ELSE
                  UPDATE tqc_incident_status
                     SET incs_acknowledged = 'Y'
                   WHERE incs_code = incs_status_rec.incs_code;

                  INSERT INTO tqc_incident_status
                              (incs_alloc_date, incs_alloc_to,
                               incs_code,
                               incs_comments,
                               incs_escalation_date, incs_exp_act_date,
                               incs_icty_by, incs_icty_code,
                               incs_icty_sht_desc, incs_inc_code,
                               incs_acknowledged, incs_icty_date,
                               incs_priority, incs_referal_incs_code,
                               incs_status, incs_status_date, incs_from_trans
                              )
                       VALUES (SYSDATE, incs_status_rec.incs_icty_by,
                                  TO_NUMBER (TO_CHAR (SYSDATE, 'RRRR'))
                               || tqc_incs_code_seq.NEXTVAL,
                               'THE POLICY HAS BEEN AUTHORISED.',
                               TRUNC (SYSDATE), TRUNC (SYSDATE),
                               v_user, 2,
                               'C', incs_status_rec.incs_inc_code,
                               'Y', SYSDATE,
                               'M', incs_status_rec.incs_code,
                               'C', SYSDATE, 'Y'
                              );
               END IF;
            EXCEPTION
               WHEN OTHERS
               THEN
                  ROLLBACK;
                  raise_error
                     ('THERE WAS AN ERROR IN LOGGING THIS POLICY INCIDENCE APPROVAL'
                     );
            END;
         END LOOP;
      ELSIF v_quote IS NOT NULL AND v_status = 'QA'
      THEN
         FOR incs_status_rec IN incs_status_cur (v_quote)
         LOOP
            BEGIN
               IF incs_status_rec.incs_acknowledged = 'Y'
               THEN
                  NULL;
               ELSE
                  UPDATE tqc_incident_status
                     SET incs_acknowledged = 'Y'
                   WHERE incs_code = incs_status_rec.incs_code;

                  INSERT INTO tqc_incident_status
                              (incs_alloc_date, incs_alloc_to,
                               incs_code,
                               incs_comments,
                               incs_escalation_date, incs_exp_act_date,
                               incs_icty_by, incs_icty_code,
                               incs_icty_sht_desc, incs_inc_code,
                               incs_acknowledged, incs_icty_date,
                               incs_priority, incs_referal_incs_code,
                               incs_status, incs_status_date, incs_from_trans
                              )
                       VALUES (SYSDATE, incs_status_rec.incs_icty_by,
                                  TO_NUMBER (TO_CHAR (SYSDATE, 'RRRR'))
                               || tqc_incs_code_seq.NEXTVAL,
                               'THE QUOTATION HAS BEEN AUTHORISED.',
                               TRUNC (SYSDATE), TRUNC (SYSDATE),
                               v_user, 2,
                               'C', incs_status_rec.incs_inc_code,
                               'Y', SYSDATE,
                               'M', incs_status_rec.incs_code,
                               'C', SYSDATE, 'Y'
                              );
               END IF;
            EXCEPTION
               WHEN OTHERS
               THEN
                  ROLLBACK;
                  raise_error
                     ('THERE WAS AN ERROR IN LOGGING THIS QUOTATION INCIDENCE APPROVAL'
                     );
            END;

            COMMIT;
         END LOOP;
      ELSIF v_quote IS NOT NULL AND v_status = 'Q'
      THEN
         FOR quo_rec IN quo_cur (v_quote)
         LOOP
            v_brn := quo_rec.quot_brn_code;

            FOR brn_cur_rec IN brn_cur (quo_rec.quot_brn_code, 'QUOTAPR')
            LOOP
               v_dep := brn_cur_rec.idep_code;

               IF v_dep IS NULL
               THEN
                  raise_error
                     (   'THERE APPEARS TO BE NO INCIDENCE TYPE DEFINED FOR THAT DEPARTMENT ON QUOTATION '
                      || v_quote
                      || ' PLEASE HAVE IT DEFINED '
                     );
               END IF;
            END LOOP;

            FOR incidence_cur_rec IN incidence_cur ('QUOTAPR', v_brn)
            LOOP
               BEGIN
                  v_inc_code := incidence_cur_rec.depict_inct_code;
                  v_inc_desc := incidence_cur_rec.depict_inct_sht_desc;

                  IF v_inc_code IS NULL OR v_inc_desc IS NULL
                  THEN
                     raise_error
                        (   'THERE APPEARS TO BE NO INCIDENCE TYPE DEFINED FOR THAT BRANCH ON QUOTATION '
                         || v_quote
                         || ' PLEASE HAVE IT DEFINED '
                        );
                  END IF;
               END;
            END LOOP;

            ncnt := 0;

            SELECT COUNT (*)
              INTO ncnt
              FROM tqc_incidents
             WHERE inc_quot_no = v_quote;

            IF ncnt > 1
            THEN
               NULL;
            ELSE
               SELECT    TO_NUMBER (TO_CHAR (SYSDATE, 'RRRR'))
                      || tqc_mail_code_seq.NEXTVAL
                 INTO v_seq
                 FROM DUAL;

               INSERT INTO tqc_incidents
                           (inc_code, inc_inct_code, inc_inct_sht_desc,
                            inc_date, inc_idep_code, inc_addressed_to,
                            inc_remarks,
                            inc_received_by, inc_brn_code, inc_pol_code,
                            inc_pol_policy_no, inc_pol_ren_endos_no,
                            inc_clm_no, inc_origin, incc_mailed, inc_quot_no,
                            inc_idep_sht_desc
                           )
                    VALUES (v_seq, v_inc_code, v_inc_desc,
                            SYSDATE, v_dep, v_alloc,
                            'THIS IS AN INCIDENCE CONCERNING APPROVAL OF QUOTATION',
                            v_user, v_brn, v_batch,
                            NULL, NULL,
                            v_clm, 'M', 'N', v_quote,
                            NULL
                           );

               DBMS_OUTPUT.put_line (   'INCIDENCE FOR'
                                     || quo_rec.quot_no
                                     || 'IN TQC_INCIDENTS'
                                    );

               INSERT INTO tqc_incident_status
                           (incs_alloc_date, incs_alloc_to,
                            incs_code,
                            incs_comments,
                            incs_escalation_date, incs_exp_act_date,
                            incs_icty_by, incs_icty_code, incs_icty_sht_desc,
                            incs_inc_code, incs_acknowledged, incs_icty_date,
                            incs_priority, incs_referal_incs_code,
                            incs_status, incs_status_date, incs_from_trans
                           )
                    VALUES (SYSDATE, v_alloc,
                               TO_NUMBER (TO_CHAR (SYSDATE, 'RRRR'))
                            || tqc_incs_code_seq.NEXTVAL,
                            'THE SYSTEM WILL CHECK THIS QUOTATION AS HAVING BEEN SENT FOR APPROVAL. YOUR REPLY WILL EITHER MARK IT AS CLOSED AS DONE(APPROVED) OR CLOSED AS DECLINED(NOT APPROVED).',
                            TRUNC (SYSDATE), TRUNC (SYSDATE),
                            v_user, 1, 'O',
                            v_seq, 'N', SYSDATE,
                            'M', NULL,
                            'P', SYSDATE, 'Y'
                           );
            END IF;

            DBMS_OUTPUT.put_line (   'INCIDENCE FOR'
                                  || quo_rec.quot_brn_code
                                  || 'IN TQC_INCIDENT_STATUS'
                                 );
         END LOOP;
      ELSIF v_clm IS NOT NULL AND v_status = 'C'
      THEN
         FOR clm_rec IN clm_cur (v_clm)
         LOOP
            v_brn := clm_rec.cmb_brn_code;

            FOR brn_cur_rec IN brn_cur (clm_rec.cmb_brn_code, 'CLMAPR')
            LOOP
               v_dep := brn_cur_rec.idep_code;

               IF v_dep IS NULL
               THEN
                  raise_error
                     (   'THERE APPEARS TO BE NO INCIDENCE TYPE DEFINED FOR THAT DEPARTMENT ON CLAIM '
                      || v_clm
                      || ' PLEASE HAVE IT DEFINED '
                     );
               END IF;
            END LOOP;

            FOR incidence_cur_rec IN incidence_cur ('CLMAPR', v_brn)
            LOOP
               BEGIN
                  v_inc_code := incidence_cur_rec.depict_inct_code;
                  v_inc_desc := incidence_cur_rec.depict_inct_sht_desc;

                  IF v_inc_code IS NULL OR v_inc_desc IS NULL
                  THEN
                     raise_error
                        (   'THERE APPEARS TO BE NO INCIDENCE TYPE DEFINED FOR THAT BRANCH ON CLAIM '
                         || v_clm
                         || ' PLEASE HAVE IT DEFINED '
                        );
                  END IF;
               END;
            END LOOP;

            ncnt := 0;

            SELECT COUNT (*)
              INTO ncnt
              FROM tqc_incidents
             WHERE inc_clm_no = v_clm;

            IF ncnt > 1
            THEN
               NULL;
            ELSE
               SELECT    TO_NUMBER (TO_CHAR (SYSDATE, 'RRRR'))
                      || tqc_mail_code_seq.NEXTVAL
                 INTO v_seq
                 FROM DUAL;

               INSERT INTO tqc_incidents
                           (inc_code, inc_inct_code, inc_inct_sht_desc,
                            inc_date, inc_idep_code, inc_addressed_to,
                            inc_remarks,
                            inc_received_by, inc_brn_code, inc_pol_code,
                            inc_pol_policy_no, inc_pol_ren_endos_no,
                            inc_clm_no, inc_origin, incc_mailed, inc_quot_no,
                            inc_idep_sht_desc
                           )
                    VALUES (v_seq, v_inc_code, v_inc_desc,
                            SYSDATE, v_dep, v_alloc,
                            'THIS IS AN INCIDENCE CONCERNING APPROVAL OF A CLAIM',
                            v_user, v_brn, v_batch,
                            NULL, NULL,
                            v_clm, 'M', 'N', v_quote,
                            NULL
                           );

               DBMS_OUTPUT.put_line (   'INCIDENCE FOR'
                                     || clm_rec.cmb_claim_no
                                     || 'IN TQC_INCIDENTS'
                                    );

               INSERT INTO tqc_incident_status
                           (incs_alloc_date, incs_alloc_to,
                            incs_code,
                            incs_comments,
                            incs_escalation_date, incs_exp_act_date,
                            incs_icty_by, incs_icty_code, incs_icty_sht_desc,
                            incs_inc_code, incs_acknowledged, incs_icty_date,
                            incs_priority, incs_referal_incs_code,
                            incs_status, incs_status_date, incs_from_trans
                           )
                    VALUES (SYSDATE, v_alloc,
                               TO_NUMBER (TO_CHAR (SYSDATE, 'RRRR'))
                            || tqc_incs_code_seq.NEXTVAL,
                            'THE SYSTEM WILL CHECK THIS QUOTATION AS HAVING BEEN SENT FOR APPROVAL. YOUR REPLY WILL EITHER MARK IT AS CLOSED AS DONE(APPROVED) OR CLOSED AS DECLINED(NOT APPROVED).',
                            TRUNC (SYSDATE), TRUNC (SYSDATE),
                            v_user, 1, 'O',
                            v_seq, 'N', SYSDATE,
                            'M', NULL,
                            'P', SYSDATE, 'Y'
                           );
            END IF;

            DBMS_OUTPUT.put_line (   'INCIDENCE FOR'
                                  || clm_rec.cmb_brn_code
                                  || 'IN TQC_INCIDENT_STATUS'
                                 );
         END LOOP;
      ELSIF v_clm IS NOT NULL AND v_status = 'CA'
      THEN
         FOR incs_status_rec IN incs_status_cur (v_clm)
         LOOP
            BEGIN
               IF incs_status_rec.incs_acknowledged = 'Y'
               THEN
                  NULL;
               ELSE
                  UPDATE tqc_incident_status
                     SET incs_acknowledged = 'Y'
                   WHERE incs_code = incs_status_rec.incs_code;

                  INSERT INTO tqc_incident_status
                              (incs_alloc_date, incs_alloc_to,
                               incs_code,
                               incs_comments,
                               incs_escalation_date, incs_exp_act_date,
                               incs_icty_by, incs_icty_code,
                               incs_icty_sht_desc, incs_inc_code,
                               incs_acknowledged, incs_icty_date,
                               incs_priority, incs_referal_incs_code,
                               incs_status, incs_status_date, incs_from_trans
                              )
                       VALUES (SYSDATE, incs_status_rec.incs_icty_by,
                                  TO_NUMBER (TO_CHAR (SYSDATE, 'RRRR'))
                               || tqc_incs_code_seq.NEXTVAL,
                               'THE CLAIM HAS BEEN AUTHORISED.',
                               TRUNC (SYSDATE), TRUNC (SYSDATE),
                               v_user, 2,
                               'C', incs_status_rec.incs_inc_code,
                               'Y', SYSDATE,
                               'M', incs_status_rec.incs_code,
                               'C', SYSDATE, 'Y'
                              );
               END IF;
            EXCEPTION
               WHEN OTHERS
               THEN
                  ROLLBACK;
                  raise_error
                     ('THERE WAS AN ERROR IN LOGGING THIS CLAIM INCIDENCE APPROVAL'
                     );
            END;

            COMMIT;
         END LOOP;
      END IF;
   END;

   PROCEDURE escal_incidence (
      nincscode   IN   NUMBER,
      v_esc_to    IN   VARCHAR2,
      v_remarks        VARCHAR2 DEFAULT ' '
   )
   IS
      v_inc_desc      VARCHAR2 (200);
      v_status_desc   VARCHAR2 (200);
      v_send_mail     VARCHAR2 (1);
      v_sendor        VARCHAR2 (70);
      v_recipient     VARCHAR2 (70);
      v_priority      VARCHAR2 (20);
      v_mailhost      VARCHAR2 (200);
      v_esc_from      VARCHAR2 (30);
      v_incs_code     NUMBER;
   BEGIN
      SELECT TO_NUMBER (TO_CHAR (SYSDATE, 'RRRR'))
             || tqc_incs_code_seq.NEXTVAL
        INTO v_incs_code
        FROM DUAL;

      INSERT INTO tqc_incident_status
                  (incs_code, incs_inc_code, incs_icty_by, incs_status_date,
                   incs_icty_date, incs_alloc_date, incs_exp_act_date,
                   incs_escalation_date, incs_icty_code,
                   incs_referal_incs_code, incs_icty_sht_desc, incs_comments,
                   incs_alloc_to, incs_acknowledged, incs_priority,
                   incs_status, incs_from_trans, incs_escalate,
                   incs_current_status)
         SELECT v_incs_code, incs_inc_code, incs_icty_by, SYSDATE,
                incs_icty_date, SYSDATE, incs_exp_act_date,
                incs_escalation_date + NVL (depict_esc_days, 0),
                incs_icty_code, incs_code, incs_icty_sht_desc, incs_comments,
                v_esc_to, 'N', incs_priority, incs_status, incs_from_trans,
                incs_escalate, 'Y'
           FROM tqc_incidents, tqc_incident_status, tqc_dep_incident_types
          WHERE incs_inc_code = inc_code
            AND depict_code = inc_depict_code
            AND incs_code = nincscode;

--             returning incs_comments,decode(incs_priority ,'H','HIGH'
--                                                                                  ,'M','MEDIUM'
--                                                                                        ,'LOW')
--             into v_status_desc,v_priority;
      UPDATE tqc_incident_status
         SET incs_acknowledged = 'Y',
             incs_status = 'R',
             incs_status_date = SYSDATE,
             incs_comments = incs_comments || '::' || v_remarks
       WHERE incs_code = nincscode;

      --returning incs_alloc_to into v_esc_from;
      BEGIN
         SELECT incs_comments,
                DECODE (incs_priority, 'H', 'HIGH', 'M', 'MEDIUM', 'LOW')
           INTO v_status_desc,
                v_priority
           FROM tqc_incident_status
          WHERE incs_code = v_incs_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('ERROR DETERMINING SEND EMAIL FLAG..');
      END;

      BEGIN
         SELECT NVL (inc_send_email, 'N'), inc_sht_desc, incs_alloc_to
           INTO v_send_mail, v_inc_desc, v_esc_from
           FROM tqc_incidents, tqc_incident_status
          WHERE incs_inc_code = inc_code AND incs_code = nincscode;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('ERROR DETERMINING SEND EMAIL FLAG..');
      END;

--raise_error('1232');
      IF NVL (v_send_mail, 'N') = 'Y'
      THEN
         BEGIN
            SELECT param_value
              INTO v_mailhost
              FROM tqc_parameters
             WHERE param_name = 'MAILHOST';

            SELECT usr_email
              INTO v_sendor
              FROM tqc_users
             WHERE usr_username = v_esc_from;

            SELECT usr_email
              INTO v_recipient
              FROM tqc_users
             WHERE usr_username = v_esc_to;

            IF v_sendor IS NOT NULL AND v_recipient IS NOT NULL
            THEN
               sendmail (v_sendor,
                         v_recipient,
                            'YOU HAVE A '
                         || v_priority
                         || ' PRIORITY '
                         || v_inc_desc
                         || ' INCIDENT ',
                         v_status_desc,
                         v_mailhost,
                         NULL
                        );
            END IF;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR SENDING INCIDENT EMAIL TO ' || v_recipient);
         END;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_application_error (-20001, nincscode || SQLERRM);
   END;

   PROCEDURE auto_escalation
   IS
      v_errmsg   VARCHAR2 (200);
      v_esc_to   VARCHAR2 (30);

      CURSOR incs_to_esc
      IS
         SELECT incs_code, inc_sht_desc, incs_alloc_to
           FROM tqc_incidents, tqc_incident_status
          WHERE incs_inc_code = inc_code
            AND NVL (incs_escalate, 'N') = 'Y'
            AND NVL (incs_current_status, 'Y') = 'Y'
            AND incs_escalation_date < SYSDATE
            AND NVL (incs_status, 'P') = 'P';
   BEGIN
      FOR recs IN incs_to_esc
      LOOP
         BEGIN
            BEGIN
               SELECT usr_per_rank_id
                 INTO v_esc_to
                 FROM tqc_users
                WHERE usr_username = recs.incs_alloc_to;
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_application_error
                               (-20001,
                                   'ERROR GETTING SUPERIOR DEFINED FOR USER '
                                || recs.incs_alloc_to
                               );
            END;

            IF v_esc_to IS NULL
            THEN
               raise_application_error (-20001,
                                           'USER '
                                        || recs.incs_alloc_to
                                        || ' DOES NOT HAVE A SUPERIOR DEFINED..'
                                       );
            END IF;

            BEGIN
               escal_incidence
                          (recs.incs_code,
                           v_esc_to,
                           'ESCALATED BY TURNQUEST. INCIDENCE STILL PENDING.'
                          );
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_application_error (-20001,
                                              'ERROR ESCALATING INCIDENCE '
                                           || recs.inc_sht_desc
                                           || ' FROM '
                                           || recs.incs_alloc_to
                                           || ' TO '
                                           || v_esc_to
                                           || ' '
                                           || SQLERRM
                                          );
            END;
         EXCEPTION
            WHEN OTHERS
            THEN
               ROLLBACK;
               v_errmsg := SQLERRM;

               INSERT INTO tq_gis.gin_gis_errors
                           (gerror_code, gerror_module,
                            gerror_object, gerror_desc
                           )
                    VALUES (gin_gerror_code_seq.NEXTVAL, 'INCIDENCE',
                            'GIN_INCIDENTS.AUTO_ESCALATION', v_errmsg
                           );
         END;

         COMMIT;
      END LOOP;
   END;
END tqc_incidences; 
/