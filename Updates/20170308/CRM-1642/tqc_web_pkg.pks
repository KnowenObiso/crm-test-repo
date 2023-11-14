FUNCTION get_usr_tckt_dtls (
      v_user       IN   VARCHAR2,
      v_clnt_code        NUMBER DEFAULT NULL,
      v_agn_code        NUMBER DEFAULT NULL,
      v_spr_code        VARCHAR2 DEFAULT NULL,
      v_syscode            NUMBER DEFAULT NULL,
      v_cla_type        VARCHAR2 DEFAULT NULL,
      v_tkt_type VARCHAR2 DEFAULT NULL
   )RETURN tqc_tickets_ref;

