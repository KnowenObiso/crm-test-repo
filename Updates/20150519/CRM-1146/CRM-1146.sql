--TQC_SETUPS_CURSOR.managers
 PROCEDURE managers (v_managers_ref OUT managers_ref)
   IS
   BEGIN
      OPEN v_managers_ref FOR
        /* SELECT   agn_code, agn_sht_desc, agn_name, twn_name
             FROM tqc_agencies, tqc_towns
            WHERE agn_twn_code = twn_code(+)
              AND agn_act_code IN (SELECT act_code
                                     FROM tqc_account_types
                                    WHERE act_type_id IN ('IA', 'BM'))
              AND agn_status = 'ACTIVE'*/
              SELECT USR_CODE, USR_CODE,USR_NAME, NULL
              FROM TQC_USERS
              WHERE USR_TYPE = 'U'
         UNION ALL
         SELECT   NULL, NULL, 'NONE', 'NONE'
             FROM DUAL
         ORDER BY 2 NULLS FIRST;
   END managers;