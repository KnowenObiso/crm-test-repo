--
-- TQC_INCIDENTS_WEB_PKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.TQC_INCIDENTS_WEB_PKG AS
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
                            v_tran_type         VARCHAR2);
  PROCEDURE delete_incident(v_inc_code NUMBER);
  PROCEDURE update_incident_status(v_inc_code NUMBER, v_status VARCHAR2);
  PROCEDURE create_referral_record(v_inc_code     NUMBER,
                                   v_old_inc_code NUMBER);
  PROCEDURE updateIncidence(v_INCS_CODE            NUMBER,
                            v_INCS_INC_CODE        NUMBER,
                            v_INCS_ICTY_CODE       NUMBER,
                            v_INCS_ALLOC_TO        VARCHAR2,
                            v_INCS_ALLOC_DATE      varchar2,
                            v_INCS_EXP_ACT_DATE    varchar2,
                            v_INCS_ESCALATION_DATE varchar2,
                            v_INCS_PRIORITY        VARCHAR2,
                            v_INCS_STATUS          VARCHAR2,
                            v_INCS_COMMENTS        VARCHAR2);
  PROCEDURE save_incidence_type(v_action    VARCHAR,
                                v_inct_code NUMBER,
                                v_id        VARCHAR2,
                                v_desc      VARCHAR2,
                                v_owner     VARCHAR2);
  PROCEDURE save_incidence_action(v_action    VARCHAR,
                                  v_inca_code NUMBER,
                                  v_id        VARCHAR2,
                                  v_desc      VARCHAR2);
  PROCEDURE save_incidence_dept(v_action    VARCHAR,
                                v_incd_code NUMBER,
                                v_id        VARCHAR2,
                                v_desc      VARCHAR2,
                                v_brn_code  NUMBER,
                                v_sys_code  NUMBER);
  PROCEDURE save_dept_incidence_type(v_action        VARCHAR,
                                     v_dicnt_code    NUMBER,
                                     v_inct_code     NUMBER,
                                     v_inct_sht_desc VARCHAR2,
                                     v_idep_code     NUMBER,
                                     v_send_mail     VARCHAR2,
                                     v_priority_lvl  VARCHAR2,
                                     v_esc           VARCHAR2,
                                     v_esc_days      NUMBER,
                                     v_close_days    NUMBER);
END TQC_INCIDENTS_WEB_PKG; 
/