--
-- TQC_INCIDENTS_WEB_PKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.TQC_INCIDENTS_WEB_PKG AS

  PROCEDURE RAISE_ERROR(v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL) IS
  BEGIN
    IF v_err_no IS NULL THEN
      RAISE_APPLICATION_ERROR(-20037, v_msg || ' - ' || SQLERRM(SQLCODE));
    ELSE
      RAISE_APPLICATION_ERROR(v_err_no, v_msg || ' - ' || SQLERRM(SQLCODE));
    END IF;
  END RAISE_ERROR;
  PROCEDURE create_incident(v_inc_code          NUMBER,
                            v_INC_BRN_CODE      NUMBER,
                            v_INC_DEP_CODE      NUMBER,
                            v_INC_INCT_CODE     number,
                            v_INC_INCT_SHT_DESC varchar2,
                            v_INC_ORIGIN        varchar2,
                            v_INC_ADDRESSED_TO  varchar2,
                            v_INC_RECEIVED_BY   varchar2,
                            v_INC_REMARKS       varchar2,
                            v_INC_POL_CODE      number default null,
                            v_INC_POL_POLICY_NO varchar2,
                            v_INC_CLM_NO        varchar2 default null,
                            v_INC_QUOT_NO       varchar2,
                            v_INC_DEPICT_CODE   number,
                            v_INC_IDEP_SHT_DESC varchar2,
                            v_tran_type         VARCHAR2) IS
    v_new_code     NUMBER;
    v_user         VARCHAR2(100) := pkg_global_vars.get_pvarchar2('pkg_global_vars.pvg_username');
    v_INC_SHT_DESC varchar2(100);
  BEGIN
    /*IF v_user IS NULL THEN
      RAISE_ERROR('System could not determine the user logged on...');
    END IF;*/
    BEGIN
      SELECT INCT_DESC
        INTO v_INC_SHT_DESC
        FROM TQC_INCIDENT_TYPES
       WHERE INCT_CODE = v_INC_INCT_CODE;
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_ERROR('Error accessing in sht desc...');
    END;
    IF v_tran_type = 'N' then
      BEGIN
        INSERT INTO TQC_INCIDENTS
          (INC_CODE,
           INC_BRN_CODE,
           INC_DEP_CODE,
           INC_INCT_CODE,
           INC_INCT_SHT_DESC,
           INC_DATE,
           INC_ORIGIN,
           INC_ADDRESSED_TO,
           INC_RECEIVED_BY,
           INC_REMARKS,
           INC_POL_CODE,
           INC_POL_POLICY_NO,
           INC_CLM_NO,
           INC_QUOT_NO,
           INC_SHT_DESC,
           INC_DEPICT_CODE,
           INC_IDEP_SHT_DESC,
           INC_IDEP_CODE)
        VALUES
          (v_inc_code,
           v_INC_BRN_CODE,
           v_INC_DEP_CODE,
           v_INC_INCT_CODE,
           v_INC_INCT_SHT_DESC,
           TRUNC(SYSDATE),
           v_INC_ORIGIN,
           'KIM',
           v_INC_RECEIVED_BY,
           v_INC_REMARKS,
           v_INC_POL_CODE,
           v_INC_POL_POLICY_NO,
           v_INC_CLM_NO,
           v_INC_QUOT_NO,
           v_INC_SHT_DESC,
           v_INC_DEPICT_CODE,
           v_INC_IDEP_SHT_DESC,
           v_INC_DEP_CODE);
        commit;
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Error Creating Incident Record....');
      END;
    ELSE
      BEGIN
        UPDATE TQC_INCIDENTS
           SET INC_BRN_CODE      = NVL(v_INC_BRN_CODE, INC_BRN_CODE),
               INC_DEP_CODE      = NVL(v_INC_DEP_CODE, INC_DEP_CODE),
               INC_INCT_CODE     = nvl(v_INC_INCT_CODE, INC_INCT_CODE),
               INC_INCT_SHT_DESC = nvl(v_INC_INCT_SHT_DESC,
                                       INC_INCT_SHT_DESC),
               INC_ORIGIN        = NVL(v_INC_ORIGIN, INC_ORIGIN),
               INC_ADDRESSED_TO  = NVL(v_INC_ADDRESSED_TO, INC_ADDRESSED_TO),
               INC_RECEIVED_BY   = nvl(v_INC_RECEIVED_BY, INC_RECEIVED_BY),
               INC_REMARKS       = nvl(v_INC_REMARKS, INC_REMARKS),
               INC_POL_CODE      = nvl(v_INC_POL_CODE, INC_POL_CODE),
               INC_POL_POLICY_NO = nvl(v_INC_POL_POLICY_NO,
                                       INC_POL_POLICY_NO),
               INC_CLM_NO        = nvl(v_INC_CLM_NO, INC_CLM_NO),
               INC_QUOT_NO       = NVL(v_INC_QUOT_NO, INC_QUOT_NO),
               INC_SHT_DESC      = NVL(v_INC_SHT_DESC, INC_SHT_DESC),
               INC_DEPICT_CODE   = NVL(v_INC_DEPICT_CODE, INC_DEPICT_CODE),
               INC_IDEP_SHT_DESC = NVL(v_INC_IDEP_SHT_DESC,
                                       INC_IDEP_SHT_DESC),
               INC_IDEP_CODE     = NVL(v_INC_DEP_CODE, INC_IDEP_CODE)
         WHERE INC_CODE = v_inc_code;
        commit;
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Error updating Incident Record....');
      END;
    END IF;
  END;
  PROCEDURE delete_incident(v_inc_code NUMBER) IS
  BEGIN
    DELETE FROM TQC_INCIDENTS WHERE INC_CODE = v_inc_code;
    COMMIT;
  EXCEPTION
    WHEN OTHERS THEN
      RAISE_ERROR('Error Deleting Incident Record....');
  END;
  PROCEDURE update_incident_status(v_inc_code NUMBER, v_status VARCHAR2) IS
  BEGIN
    UPDATE TQC_INCIDENT_STATUS
       SET INCS_ACKNOWLEDGED = 'Y',
           INCS_STATUS       = v_status,
           INCS_STATUS_DATE  = TRUNC(SYSDATE)
     WHERE INCS_CODE = v_inc_code;
    commit;
  EXCEPTION
    WHEN OTHERS THEN
      RAISE_ERROR('Error updating Incident Record....');
  END;
  PROCEDURE create_referral_record(v_inc_code     NUMBER,
                                   v_old_inc_code NUMBER) IS
    --v_user VARCHAR2(100):=pkg_global_vars.get_pvarchar2 ('pkg_global_vars.pvg_username');
  BEGIN
    begin
      --RAISE_ERROR('COUNT DOWN'||v_inc_code);
      INSERT INTO TQC_INCIDENT_STATUS
        (INCS_CODE,
         INCS_REFERAL_INCS_CODE,
         INCS_INC_CODE,
         INCS_ICTY_CODE,
         INCS_ICTY_SHT_DESC,
         INCS_PRIORITY,
         INCS_COMMENTS,
         INCS_ICTY_BY,
         INCS_STATUS,
         INCS_ACKNOWLEDGED,
         INCS_ICTY_DATE,
         INCS_STATUS_DATE,
         INCS_EXP_ACT_DATE)
        SELECT v_inc_code,
               INCS_CODE,
               INCS_INC_CODE,
               INCS_ICTY_CODE,
               INCS_ICTY_SHT_DESC,
               INCS_PRIORITY,
               INCS_COMMENTS,
               'KIM',
               'C',
               'Y',
               SYSDATE,
               trunc(SYSDATE),
               INCS_EXP_ACT_DATE
          FROM TQC_INCIDENT_STATUS
         WHERE INCS_CODE = v_old_inc_code;
      COMMIT;
    EXCEPTION
      when others then
        raise_error('Unable to create a incident referal record..');
    END;
    BEGIN
      UPDATE TQC_INCIDENT_STATUS
         SET INCS_STATUS = 'R'
       WHERE INCS_CODE = v_inc_code;
      COMMIT;
    EXCEPTION
      when others then
        raise_error('Unable to update status of original incidence..');
    END;
  
  end;
  PROCEDURE updateIncidence(v_INCS_CODE            NUMBER,
                            v_INCS_INC_CODE        NUMBER,
                            v_INCS_ICTY_CODE       NUMBER,
                            v_INCS_ALLOC_TO        VARCHAR2,
                            v_INCS_ALLOC_DATE      varchar2,
                            v_INCS_EXP_ACT_DATE    varchar2,
                            v_INCS_ESCALATION_DATE varchar2,
                            v_INCS_PRIORITY        VARCHAR2,
                            v_INCS_STATUS          VARCHAR2,
                            v_INCS_COMMENTS        VARCHAR2) IS
    v_user               VARCHAR2(100) := pkg_global_vars.get_pvarchar2('pkg_global_vars.pvg_username');
    v_INCS_ICTY_SHT_DESC VARCHAR2(30);
    v_INCS_ICTY_DATE     DATE;
    v_INCS_ICTY_BY       VARCHAR2(100);
  BEGIN
    BEGIN
      SELECT ICTY_SHT_DESC
        INTO v_INCS_ICTY_SHT_DESC
        FROM TQC_INCI_ACTION_TYPES
       WHERE ICTY_CODE = v_INCS_ICTY_CODE;
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_ERROR('Error....1');
    END;
    raise_error('=' || v_INCS_INC_CODE || v_INCS_ICTY_CODE);
    BEGIN
      INSERT INTO TQC_INCIDENT_STATUS
        (INCS_CODE,
         INCS_INC_CODE,
         INCS_ICTY_CODE,
         INCS_ICTY_SHT_DESC,
         INCS_ICTY_DATE,
         INCS_ICTY_BY,
         INCS_ALLOC_TO,
         INCS_ALLOC_DATE,
         INCS_EXP_ACT_DATE,
         INCS_ESCALATION_DATE,
         INCS_PRIORITY,
         INCS_COMMENTS,
         INCS_STATUS_DATE)
      VALUES
        (v_INCS_CODE,
         v_INCS_INC_CODE,
         v_INCS_ICTY_CODE,
         v_INCS_ICTY_SHT_DESC,
         TRUNC(SYSDATE),
         'KIM',
         v_INCS_ALLOC_TO,
         v_INCS_ALLOC_DATE,
         v_INCS_EXP_ACT_DATE,
         v_INCS_ESCALATION_DATE,
         v_INCS_PRIORITY,
         v_INCS_COMMENTS,
         TRUNC(SYSDATE));
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_ERROR('Error Creating Incident status record....');
    END;
  END;
  PROCEDURE save_incidence_type(v_action    VARCHAR,
                                v_inct_code NUMBER,
                                v_id        VARCHAR2,
                                v_desc      VARCHAR2,
                                v_owner     VARCHAR2)
  
   IS
  BEGIN
    IF v_action = 'A' THEN
      BEGIN
        INSERT INTO TQC_INCIDENT_TYPES
          (INCT_CODE, INCT_SHT_DESC, INCT_DESC, INCT_OWNER)
        VALUES
          (TQC_INCT_CODE_SEQ.NEXTVAL, v_id, v_desc, v_owner);
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Error Inserting Incidence Type');
      END;
    
    ELSIF v_action = 'E' THEN
      BEGIN
        UPDATE TQC_INCIDENT_TYPES
           SET INCT_SHT_DESC = v_id,
               INCT_DESC     = v_desc,
               INCT_OWNER    = v_owner
         WHERE INCT_CODE = v_inct_code;
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Error Inserting Incidence Type');
      END;
    
    ELSIF v_action = 'D' THEN
      BEGIN
        DELETE FROM TQC_INCIDENT_TYPES WHERE INCT_CODE = v_inct_code;
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Error Inserting Incidence Type');
      END;
    
    ELSE
      RAISE_ERROR('Invalid Option Selected.');
    
    END IF;
  END;

  PROCEDURE save_incidence_action(v_action    VARCHAR,
                                  v_inca_code NUMBER,
                                  v_id        VARCHAR2,
                                  v_desc      VARCHAR2)
  
   IS
  BEGIN
    IF v_action = 'A' THEN
      BEGIN
        INSERT INTO TQC_INCI_ACTION_TYPES
          (ICTY_CODE, ICTY_SHT_DESC, ICTY_DESC, ICTY_DATE)
        VALUES
          (TQC_ICTY_CODE_SEQ.NEXTVAL, v_id, v_desc, TRUNC(SYSDATE));
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Error Inserting Incidence Action/status');
      END;
    
    ELSIF v_action = 'E' THEN
      BEGIN
        UPDATE TQC_INCI_ACTION_TYPES
           SET ICTY_SHT_DESC = v_id, ICTY_DESC = v_desc
         WHERE ICTY_CODE = v_inca_code;
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Error Inserting Incidence Action/status');
      END;
    
    ELSIF v_action = 'D' THEN
      BEGIN
        DELETE FROM TQC_INCI_ACTION_TYPES WHERE ICTY_CODE = v_inca_code;
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Error Inserting Incidence Action/status');
      END;
    
    ELSE
      RAISE_ERROR('Invalid Option Selected.');
    
    END IF;
  END;

  PROCEDURE save_incidence_dept(v_action    VARCHAR,
                                v_incd_code NUMBER,
                                v_id        VARCHAR2,
                                v_desc      VARCHAR2,
                                v_brn_code  NUMBER,
                                v_sys_code  NUMBER)
  
   IS
  BEGIN
    IF v_action = 'A' THEN
      BEGIN
        INSERT INTO TQC_INC_DEPARTMENTS
          (IDEP_CODE,
           IDEP_SHT_DESC,
           IDEP_DESC,
           IDEP_SYS_CODE,
           IDEP_BRN_CODE)
        VALUES
          (TQC_IDEP_CODE_SEQ.NEXTVAL, v_id, v_desc, v_sys_code, v_brn_code);
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Error Inserting Incidence Department');
      END;
    
    ELSIF v_action = 'E' THEN
      BEGIN
        UPDATE TQC_INC_DEPARTMENTS
           SET IDEP_SHT_DESC = v_id,
               IDEP_DESC     = v_desc,
               IDEP_BRN_CODE = v_brn_code
         WHERE IDEP_CODE = v_incd_code;
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Error Inserting Incidence Department');
      END;
    
    ELSIF v_action = 'D' THEN
      BEGIN
        DELETE FROM TQC_INC_DEPARTMENTS WHERE IDEP_CODE = v_incd_code;
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Error Inserting Incidence Department');
      END;
    
    ELSE
      RAISE_ERROR('Invalid Option Selected.');
    
    END IF;
  END;

  PROCEDURE save_dept_incidence_type(v_action        VARCHAR,
                                     v_dicnt_code    NUMBER,
                                     v_inct_code     NUMBER,
                                     v_inct_sht_desc VARCHAR2,
                                     v_idep_code     NUMBER,
                                     v_send_mail     VARCHAR2,
                                     v_priority_lvl  VARCHAR2,
                                     v_esc           VARCHAR2,
                                     v_esc_days      NUMBER,
                                     v_close_days    NUMBER)
  
   IS
  BEGIN
    IF v_action = 'A' THEN
      BEGIN
        INSERT INTO TQC_DEP_INCIDENT_TYPES
          (DEPICT_CODE,
           DEPICT_INCT_CODE,
           DEPICT_INCT_SHT_DESC,
           DEPICT_IDEP_CODE,
           DEPICT_PRIORITY_LVL,
           DEPICT_ESC_DAYS,
           DEPICT_CLOSE_DAYS,
           DEPICT_ESCALATE,
           DEPICT_SEND_EMAIL)
        VALUES
          (TQC_DEPICT_CODE_SEQ.NEXTVAL,
           v_inct_code,
           v_inct_sht_desc,
           v_idep_code,
           v_priority_lvl,
           v_esc_days,
           v_close_days,
           v_esc,
           v_send_mail);
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Error Inserting Department Incidence Type');
      END;
    
    ELSIF v_action = 'E' THEN
      BEGIN
        UPDATE TQC_DEP_INCIDENT_TYPES
           SET DEPICT_INCT_CODE    = v_inct_code,
               DEPICT_PRIORITY_LVL = v_priority_lvl,
               DEPICT_ESC_DAYS     = v_esc_days,
               DEPICT_CLOSE_DAYS   = v_close_days,
               DEPICT_ESCALATE     = v_esc,
               DEPICT_SEND_EMAIL   = v_send_mail
         WHERE DEPICT_CODE = v_dicnt_code;
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Error Inserting  Department Incidence Type');
      END;
    
    ELSIF v_action = 'D' THEN
      BEGIN
        DELETE FROM TQC_DEP_INCIDENT_TYPES
         WHERE DEPICT_CODE = v_dicnt_code;
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Error Inserting Department Incidence Type');
      END;
    
    ELSE
      RAISE_ERROR('Invalid Option Selected.');
    
    END IF;
  END;

END TQC_INCIDENTS_WEB_PKG; 
/