--
-- POST_GIS_RCT  (Procedure) 
--
CREATE OR REPLACE PROCEDURE TQ_CRM.post_gis_rct (
      v_rct_rec                 Fms_Interfaces_Pkg.type_receipts_table,
      v_tran_mapping   IN OUT   Fms_Interfaces_Pkg.tran_mapping_type,
      v_rct_type                VARCHAR2,
      v_user_name               VARCHAR2,
      v_cancel                  VARCHAR2 DEFAULT 'N' ,
      v_mtran_ref_no            VARCHAR2 DEFAULT NULL
   )
   IS
      v_errmsg              VARCHAR2 (1500);
      v_count               NUMBER          := 0;
      v_ggt_trans_no        NUMBER;
      v_mtran_no            NUMBER;
      v_amount              NUMBER          := 0;
      v_rrc_code            NUMBER;
      v_rec_count           NUMBER;
      v_cancel_rct          VARCHAR2 (1)    := v_cancel;
      v_allocate_amt        NUMBER          := 0;
      v_bal_amt             NUMBER          := 0;
      v_dr_comm_due         NUMBER          := 0;
      v_diff                NUMBER          := 0;
      v_comm_alloc_amt      NUMBER          := 0;
      v_brn_code            NUMBER;
      v_agnt_code           NUMBER;
      v_prev_mtran_no       NUMBER;
      v_prev_mtran_ref_no   VARCHAR2 (30);
      v_tran_type           VARCHAR2 (10);
      v_rnd                 NUMBER;
      v_bcur_rnd            NUMBER;
      v_cur_rate            NUMBER;
      v_param               VARCHAR2(5); 
      v_appr_comm           NUMBER;              
      v_amt_to_com           NUMBER;   
      CURSOR cur_commissions (v_prv_mtran_no IN NUMBER)
      IS
         SELECT *
           FROM GIN_COMMISSION_PYMTS
          WHERE cop_cr_mtran_no = v_prv_mtran_no;
   BEGIN
      /*
      ************************************
      USE V_TRAN_MAPPING(i).TMAP_COMM_ALLOC
      **************************************
      */
      
      --Raise_error ('V_RCT_TYPE='||v_rct_type);
      BEGIN
             SELECT GIN_PARAMETERS_PKG.get_param_varchar('COMM_PAID_ON_FULL_DEBIT') INTO v_param FROM DUAL;
      EXCEPTION
             WHEN OTHERS THEN
                 RAISE_ERROR('Error fetching commission payment parameter...');
      END;
      IF v_rct_type IN ('JV', 'DBT')
      THEN
         SELECT ggt_trans_no_seq.NEXTVAL
           INTO v_ggt_trans_no
           FROM DUAL;

         INSERT INTO GIN_GIS_TRANSACTIONS
                     (ggt_trans_no, ggt_btr_trans_code,
                      ggt_done_by, ggt_done_date, ggt_uw_clm_tran,
                      ggt_trans_date, ggt_effective_date
                     )
              VALUES (v_ggt_trans_no, DECODE (v_cancel_rct, 'Y', 'CN', 'RC'),
                      v_user_name, TRUNC (SYSDATE), 'A',
                      TRUNC (SYSDATE), TRUNC (SYSDATE)
                     );
--RAISE_ERROR('TOP='||v_param||';'||v_appr_comm||';'||v_allocate_amt);
         FOR j IN 1 .. v_rct_rec.COUNT
         LOOP
            IF v_cancel = 'Y'
            THEN
               -- MAKE SURE THAT THE RCT 2B CANCELLED ALREADY EXISTS
               SELECT COUNT ('X')
                 INTO v_rec_count
                 FROM GIN_MASTER_TRANSACTIONS
                WHERE mtran_ref_no = v_rct_rec (j).rct_brh_rct_code
                AND mtran_rct_code = v_rct_rec (j).rct_no;

               IF NVL (v_rec_count, 0) = 0
               THEN       --DELETE THE  GIN_GIS_TRANSACTION REC CREATED ABOVE;
                  DELETE FROM GIN_GIS_TRANSACTIONS
                        WHERE ggt_trans_no = v_ggt_trans_no;

                  EXIT;
               END IF;
            END IF;

            v_cur_rate :=
               Get_Currexch_Rate (v_rct_rec (j).rct_cur_code,
                                  v_rnd,
                                  v_bcur_rnd
                                 );

            SELECT TO_NUMBER (   TO_CHAR (SYSDATE, 'YYYY')
                              || gin_mtran_no_seq.NEXTVAL
                             )
              INTO v_mtran_no
              FROM DUAL;

            INSERT INTO GIN_MASTER_TRANSACTIONS
                        (mtran_no, mtran_doc_date, mtran_date,
                         mtran_ref_no,
                         mtran_other_ref,
                         mtran_tran_type,
                         mtran_dc,
                         mtran_control_acc,
                         mtran_client_type,
                         mtran_client_code,
                         mtran_amount, mtran_balance,
                         mtran_authorised, mtran_net_amt, mtran_posting_date,
                         mtran_ggt_tran_no, mtran_prp_code,
                         mtran_btr_trans_code, mtran_origin,
                         mtran_cur_code,
                         mtran_cur_symbol,
                         mtran_brn_code,
                         mtran_brn_sht_desc,
                         mtran_posted_by,
                         mtran_cur_rate,mtran_client_polcy_no,mtran_narrations,
                         mtran_rct_code             --,
                        --MTRAN_OTHER_REF1
                        )
--PRP CODE = DECODE( v_rct_rec(j).RCT_ACC_TYPE,'D',v_rct_rec(j).RCT_ACT_CODE,NULL)
                 VALUES (v_mtran_no, v_rct_rec (j).rct_date, TRUNC (SYSDATE),
                         DECODE (v_cancel_rct,
                                 'Y', v_rct_rec (j).rct_brh_rct_code || '/CN',
                                 v_rct_rec (j).rct_brh_rct_code
                                ),
                         v_rct_rec (j).rct_paymt_memo,
                         DECODE (v_cancel_rct, 'Y', 'CN', 'RC'),
                         DECODE (v_cancel_rct, 'Y', 'D', 'C'),
                         v_rct_rec (j).rct_act_sht_desc,
                         v_rct_rec (j).rct_acct_type_id,
                         v_rct_rec (j).rct_act_code,            --acc sht desc
                         v_rct_rec (j).rct_amount, v_rct_rec (j).rct_amount,
                         'Y', v_rct_rec (j).rct_amount, v_rct_rec (j).rct_date,
                         v_ggt_trans_no, NULL,
                         DECODE (v_cancel_rct, 'Y', 'CN', 'RC'), 'U',
                         v_rct_rec (j).rct_cur_code,
                         tqc_interfaces_pkg.currencysymbol
                                                    (v_rct_rec (j).rct_cur_code
                                                    ),
                         v_rct_rec (j).rct_brh_code,
                         Fms_Interfaces_Pkg.brh_name
                                                   (v_rct_rec (j).rct_brh_code,
                                                    'S'
                                                   ),
                         tqc_interfaces_pkg.username
                                                 (v_rct_rec (j).rct_captured_by
                                                 ),
                         v_cur_rate, gin_interfaces_pkg.getPolicyNo(v_mtran_ref_no),v_rct_rec (j).rct_desc,
                         v_rct_rec (j).rct_no                --,
                        --v_rct_rec(j).RCT_REF
                        );
  
            v_brn_code := v_rct_rec (j).rct_brh_code;
            v_agnt_code := v_rct_rec (j).rct_act_code;
            
         
         --RAISE_ERROR('TOP='||v_param||';'||v_appr_comm||';'||v_allocate_amt);
            IF v_cancel != 'Y'
            THEN
               FOR i IN 1 .. v_tran_mapping.COUNT
               LOOP
               --raise_error ('V_RCT_MAPPING ='||v_tran_mapping (i).tmap_mtran_no);
                  IF NVL (v_tran_mapping (i).tmap_amount, 0) != 0
                  THEN
                     BEGIN
                        SELECT ABS (NVL (mtran_balance, 0)),
                                 ABS (NVL (mtran_commission, 0))
                               - (  ABS (NVL (mtran_comm_paid, 0))
                                  + ABS (NVL (mtran_comm_payable, 0))
                                 ),
                                 ABS (NVL (mtran_balance, 0)) + ABS (NVL (mtran_commission, 0))
                               - (  ABS (NVL (mtran_comm_paid, 0))
                                  + ABS (NVL (mtran_comm_payable, 0))
                                 ),
                               mtran_tran_type
                          INTO v_bal_amt,
                               v_dr_comm_due,
                               v_amt_to_com,
                               v_tran_type
                          FROM GIN_MASTER_TRANSACTIONS
                         WHERE mtran_no = v_tran_mapping (i).tmap_mtran_no;
                     EXCEPTION
                        WHEN NO_DATA_FOUND
                        THEN
                           RAISE_APPLICATION_ERROR
                                          (-20001,
                                              'MTRN REC ( '
                                           || v_tran_mapping (i).tmap_mtran_no
                                           || ' ) NOT FOUND'
                                          );
                        WHEN OTHERS
                        THEN
                           RAISE_APPLICATION_ERROR (-20001,
                                                    SQLERRM (SQLCODE));
                     END;
   --    RAISE_ERROR('QWWWERE='|| v_bal_amt||';'||  v_dr_comm_due||';'||v_tran_mapping (i).tmap_mtran_no);           
                    IF NVL(v_param,'N') = 'Y'  THEN
                         IF ABS (v_bal_amt) > NVL (v_tran_mapping (i).tmap_amount, 0)
                         THEN
                            v_allocate_amt := NVL (v_tran_mapping (i).tmap_amount, 0);
                         ELSE
                            v_allocate_amt := ABS (v_bal_amt);
                         END IF;
                    ELSIF NVL(v_param,'N') = 'N'  AND v_tran_mapping (i).tmap_inc_comm = 'Y' THEN
                        IF ABS(v_bal_amt) = NVL (v_tran_mapping (i).tmap_amount, 0) THEN
                                v_appr_comm := ((NVL (v_tran_mapping (i).tmap_amount, 0)/v_amt_to_com) * v_dr_comm_due);
                                v_allocate_amt := NVL (v_tran_mapping (i).tmap_amount, 0) - v_appr_comm;
                        ELSE
                                v_appr_comm := ((NVL (v_tran_mapping (i).tmap_amount, 0)/v_amt_to_com) * v_dr_comm_due);
                                v_allocate_amt := ABS(NVL (v_tran_mapping (i).tmap_amount, 0)) - v_appr_comm;
                        END IF;
--RAISE_ERROR('TRT='||v_param||';'||v_appr_comm||';'||v_allocate_amt||';'||v_dr_comm_due||';'||v_tran_mapping (i).tmap_amount||';'||v_bal_amt||';'||v_amt_to_com);
                    END IF;

--                     IF ABS (v_bal_amt) >
--                                       NVL (v_tran_mapping (i).tmap_amount, 0)
--                     THEN
--                        v_allocate_amt :=
--                                      NVL (v_tran_mapping (i).tmap_amount, 0);
--                     ELSE
--                        v_allocate_amt := ABS (v_bal_amt);
--                     END IF;

                     ----------

                     --                         gin_tran_mapping
--                         --
-- RAISE_APPLICATION_ERROR(-20004,V_TRAN_MAPPING(i).TMAP_MTRAN_REF_NO ||'--'||
--                                           V_TRAN_MAPPING(i).TMAP_MTRAN_NO ||'--'||
--                                           V_TRAN_MAPPING(i).TMAP_CHECK_MTRAN_REF_NO ||'--'||
--                                           v_allocate_amt);

                     Gis_Accounts_Utilities.knock_trans
                                   (v_mtran_no,
                                    v_tran_mapping (i).tmap_mtran_ref_no,
                                    v_tran_mapping (i).tmap_mtran_no,
                                    v_tran_mapping (i).tmap_check_mtran_ref_no,
                                    v_rct_rec (j).rct_date,
                                    v_allocate_amt
                                   );
                     v_diff :=
                          ABS (NVL (v_tran_mapping (i).tmap_amount, 0))
                        - v_allocate_amt;

--- RAISE_APPLICATION_ERROR(-20001,v_allocate_amt||' = '||v_diff||' = '||v_tran_type||' = '||V_TRAN_MAPPING(i).TMAP_COMM_ALLOC||' = '||V_TRAN_MAPPING(i).TMAP_INC_COMM);
                     IF     v_dr_comm_due != 0
                        AND v_diff > 0
                        AND v_tran_mapping (i).tmap_inc_comm = 'Y'
                     THEN
                        IF ABS (v_diff) > v_dr_comm_due
                        THEN
                           v_comm_alloc_amt := v_dr_comm_due;
                        ELSE
                           v_comm_alloc_amt := ABS (v_diff);
                        END IF;

                        Gis_Accounts_Utilities.update_commission
                                   (v_mtran_no,
                                    v_tran_mapping (i).tmap_mtran_ref_no,
                                    v_tran_mapping (i).tmap_mtran_no,
                                    v_tran_mapping (i).tmap_check_mtran_ref_no,
                                    v_dr_comm_due,
                                    v_diff,
                                    v_brn_code,
                                    v_agnt_code,
                                    v_rct_rec (j).rct_cur_code
                                   );
                     /*Gis_Accounts_Utilities.UPDATE_COMMISSION(v_mtran_no,
                                                   V_TRAN_MAPPING(i).TMAP_MTRAN_REF_NO,
                                                   V_TRAN_MAPPING(i).TMAP_MTRAN_NO,
                                                   V_TRAN_MAPPING(i).TMAP_CHECK_MTRAN_REF_NO,
                                                   v_comm_alloc_amt,
                                                   v_brn_code,
                                                   v_agnt_code,
                                                   v_rct_rec(j).RCT_CUR_CODE);*/
                     ELSIF     NVL (v_tran_type, 'NONE') = 'BAL'
                           AND NVL (v_tran_mapping (i).tmap_comm_alloc, 0) !=
                                                                             0
                     THEN     --AND V_TRAN_MAPPING(i).TMAP_INC_COMM = 'Y' THEN
--RAISE_APPLICATION_ERROR(-20001,v_diff||' = '||V_TRAN_MAPPING(i).TMAP_COMM_ALLOC||' = '||V_TRAN_MAPPING(i).TMAP_INC_COMM);
                           --IF ABS(v_diff) > NVL(V_TRAN_MAPPING(i).TMAP_COMM_ALLOC,0) THEN
                        v_comm_alloc_amt :=
                                  NVL (v_tran_mapping (i).tmap_comm_alloc, 0);

                        --ELSE
                        -- v_comm_alloc_amt  := ABS(v_diff);
                        --END IF;
                        UPDATE GIN_MASTER_TRANSACTIONS
                           SET mtran_commission =
                                    NVL (mtran_commission, 0)
                                  +   DECODE (SIGN (NVL (mtran_commission, 0)),
                                              -1, -1,
                                              1
                                             )
                                    * v_comm_alloc_amt
                         WHERE mtran_no = v_tran_mapping (i).tmap_mtran_no;

                        Gis_Accounts_Utilities.update_commission
                                   (v_mtran_no,
                                    v_tran_mapping (i).tmap_mtran_ref_no,
                                    v_tran_mapping (i).tmap_mtran_no,
                                    v_tran_mapping (i).tmap_check_mtran_ref_no,
                                    v_comm_alloc_amt,
                                    v_comm_alloc_amt,
                                    v_brn_code,
                                    v_agnt_code,
                                    v_rct_rec (j).rct_cur_code
                                   );
                     END IF;

                     /*UPDATE   GIN_MASTER_TRANSACTIONS
                     SET MTRAN_BALANCE = NVL(MTRAN_BALANCE,0) - (SIGN(NVL(MTRAN_BALANCE,0)) * ABS(V_TRAN_MAPPING(i).TMAP_AMOUNT))
                        MTRAN_SETTLEMENT = NVL(MTRAN_SETTLEMENT + (SIGN(NVL(MTRAN_BALANCE,0)) * ABS(V_TRAN_MAPPING(i).TMAP_AMOUNT))
                     WHERE MTRAN_NO = V_TRAN_MAPPING(i).TMAP_MTRAN_NO;
                     INSERT INTO GIN_TRAN_MAPPING
                        (
                        TMAP_MTRAN_NO,
                        TMAP_MTRAN_REF_NO,
                        TMAP_CHECK_MTRAN_NO ,
                        TMAP_AMOUNT,
                        TMAP_DRCR,
                        TMAP_CHECK_MTRAN_REF_NO,
                        TMAP_POSTING_DATE
                        )
                     VALUES
                        (
                        v_mtran_no,
                        V_TRAN_MAPPING(i).TMAP_MTRAN_REF_NO,--rct code
                        V_TRAN_MAPPING(i).TMAP_MTRAN_NO,
                        V_TRAN_MAPPING(i).TMAP_AMOUNT,
                        DECODE (v_cancel_rct,'Y', 'D', 'C'),
                        V_TRAN_MAPPING(i).TMAP_CHECK_MTRAN_REF_NO,--****
                        trunc(sysdate)
                        );*/
                     v_amount := v_amount + v_tran_mapping (i).tmap_amount;
                  END IF;
               END LOOP;
            ELSE
               BEGIN
                  SELECT mtran_no, mtran_ref_no
                    INTO v_prev_mtran_no, v_prev_mtran_ref_no
                    FROM GIN_MASTER_TRANSACTIONS
                   WHERE mtran_ref_no = v_rct_rec (j).rct_brh_rct_code
                     AND mtran_client_code = v_rct_rec (j).rct_act_code
                     AND mtran_client_type = v_rct_rec (j).rct_acct_type_id
                     AND mtran_rct_code = v_rct_rec (j).rct_no;
               EXCEPTION
                  WHEN TOO_MANY_ROWS
                  THEN
                     RAISE_APPLICATION_ERROR
                                            (-20001,
                                                'DUPLICATE RECEIPT FOUND..:-'
                                             || v_rct_rec (j).rct_brh_rct_code
                                             || ' - '
                                             || SQLERRM (SQLCODE)
                                            );
                  WHEN OTHERS
                  THEN
                     RAISE_APPLICATION_ERROR (-20001,
                                              'ERROR..:-' || SQLERRM (SQLCODE)
                                             );
               END;

               --IF   nvl(v_rct_rec (j).rct_bank_charge_amount+ v_rct_rec (j).rct_client_charge_amount,0) IS NULL OR 
                --nvl(v_rct_rec (j).rct_bank_charge_amount+ v_rct_rec (j).rct_client_charge_amount,0) != 0----NVL(v_rct_rec (j).rct_bank_charge_amount,0) != 0
               IF   NVL( v_rct_rec (j).rct_client_charge_amount,0) IS NULL OR  NVL(v_rct_rec (j).rct_client_charge_amount,0) != 0
               THEN
                  INSERT INTO GIN_MASTER_TRANSACTIONS
                              (mtran_no,
                               mtran_doc_date, mtran_date,
                               mtran_ref_no,
                               mtran_other_ref, mtran_tran_type, mtran_dc,
                               mtran_control_acc,
                               mtran_client_type,
                               mtran_client_code,
                               mtran_amount,
                               mtran_balance, mtran_authorised,
                               mtran_net_amt,
                               mtran_posting_date, mtran_ggt_tran_no,
                               mtran_prp_code, mtran_btr_trans_code,
                               mtran_origin, mtran_cur_code,
                               mtran_cur_symbol,
                               mtran_brn_code,
                               mtran_brn_sht_desc,
                               mtran_posted_by,
                               mtran_cur_rate, mtran_client_polcy_no,
                               mtran_rct_code
                              )
                       VALUES (TO_NUMBER (   TO_CHAR (SYSDATE, 'YYYY')
                                          || gin_mtran_no_seq.NEXTVAL
                                         ),
                               v_rct_rec (j).rct_date, v_rct_rec (j).rct_date,
                               v_rct_rec (j).rct_brh_rct_code || '/CN',
                               v_rct_rec (j).rct_paymt_memo, 'BNK', 'D',
                               v_rct_rec (j).rct_act_sht_desc,
                               v_rct_rec (j).rct_acct_type_id,
                               v_rct_rec (j).rct_act_code,
                               v_rct_rec (j).rct_client_charge_amount,
                               v_rct_rec (j).rct_client_charge_amount, 'Y',
                               v_rct_rec (j).rct_client_charge_amount,
                               v_rct_rec (j).rct_date, v_ggt_trans_no,
                               NULL, 'BNK',
                               'U', v_rct_rec (j).rct_cur_code,
                               tqc_interfaces_pkg.currencysymbol
                                                    (v_rct_rec (j).rct_cur_code
                                                    ),
                               v_rct_rec (j).rct_brh_code,
                               fms_interfaces_pkg.brh_name
                                                   (v_rct_rec (j).rct_brh_code,
                                                    'S'
                                                   ),
                               tqc_interfaces_pkg.username
                                                 (v_rct_rec (j).rct_captured_by
                                                 ),
                               v_cur_rate, 'BANK CHARGE',
                               v_rct_rec (j).rct_no
                              );
               END IF;

               Gis_Accounts_Utilities.undo_trans_allocation (v_prev_mtran_no);

               FOR cur_coms IN cur_commissions (v_prev_mtran_no)
               LOOP
                  IF NVL (cur_coms.cop_net_comm, 0) != 0
                  THEN
                     Gis_Accounts_Utilities.update_commission
                                               (cur_coms.cop_cr_mtran_no,
                                                cur_coms.cop_cr_ref_no,
                                                cur_coms.cop_dr_mtran_no,
                                                cur_coms.cop_dr_ref_no,
                                                -NVL (cur_coms.cop_net_comm,
                                                      0),
                                                cur_coms.cop_brn_code,
                                                cur_coms.cop_agn_code,
                                                v_rct_rec (j).rct_cur_code
                                               );
                  END IF;
               END LOOP;

--***************************************************** WHY IS THIS HERE
--RAISE_APPLICATION_ERROR (-20001,'{}');
--****************************************************
               SELECT ABS (NVL (mtran_balance, 0)),
                        ABS (NVL (mtran_commission, 0))
                      - (  ABS (NVL (mtran_comm_paid, 0))
                         + ABS (NVL (mtran_comm_payable, 0))
                        )
                 INTO v_bal_amt,
                      v_dr_comm_due
                 FROM GIN_MASTER_TRANSACTIONS
                WHERE mtran_no = v_prev_mtran_no;

               IF ABS (v_bal_amt) > NVL (v_rct_rec (j).rct_amount, 0)
               THEN
                  v_allocate_amt := NVL (v_rct_rec (j).rct_amount, 0);
               ELSE
                  v_allocate_amt := ABS (v_bal_amt);
               END IF;

               Gis_Accounts_Utilities.knock_trans
                                            (v_prev_mtran_no,
                                             v_prev_mtran_ref_no,
                                             v_mtran_no,
                                                v_rct_rec (j).rct_brh_rct_code
                                             || '/CN',
                                             v_rct_rec (j).rct_date,
                                             v_allocate_amt
                                            );
            END IF;
         END LOOP;
      ELSE
         FOR j IN 1 .. v_rct_rec.COUNT
         LOOP
            FOR i IN 1 .. v_tran_mapping.COUNT
            LOOP
               IF v_cancel = 'Y'
               THEN
                  SELECT COUNT ('X')
                    INTO v_rec_count
                    FROM GIN_RECOVERY_RCTS
                   WHERE rrc_rct_no = v_rct_rec (j).rct_no;

                  IF v_count = 0
                  THEN
                     v_cancel_rct := 'N';
                  END IF;
               END IF;

               SELECT NVL (MAX (rrc_code), 0) + 1
                 INTO v_rrc_code
                 FROM GIN_RECOVERY_RCTS;

               --RAISE_APPLICATION_ERROR (-20001,'V_MTRAN_REF = '||V_TRAN_MAPPING(i).tmap_check_mtran_ref_no);
               INSERT INTO GIN_RECOVERY_RCTS
                           (rrc_code,
                            rrc_claim_no,
                            rrc_recovery_type,
                            rrc_drcr,
                            rrc_amount,
                            rrc_received_from,
                            rrc_received_by,
                            rrc_receved_date, rrc_remarks,
                            rrc_rct_no, rrc_brh_code,
                            rrc_receipt_no
                           )
                    VALUES (v_rrc_code,
                            v_tran_mapping (i).tmap_check_mtran_ref_no,
                                                               --RRC_CLAIM_NO,
                            v_rct_rec (j).rct_acct_type_id,
                            DECODE (v_cancel_rct, 'Y', 'D', 'N', 'C'),
                            v_tran_mapping (i).tmap_amount,
                            v_rct_rec (j).rct_paid_by,
                            v_rct_rec (j).rct_captured_by,
                            v_rct_rec (j).rct_date, v_rct_rec (j).rct_desc,
                            v_rct_rec (j).rct_no, v_rct_rec (j).rct_brh_code,
                            v_tran_mapping (i).tmap_mtran_ref_no
                           );
            /*
                           (v_mtran_no,
                  TRUNC(SYSDATE),v_rct_rec(j).RCT_BRH_RCT_CODE,
                  v_rct_rec(j).RCT_PAYMT_MEMO, 'RC',
                  'C' ,v_rct_rec(j).RCT_ACT_CODE,
                  v_rct_rec(j).RCT_ACCT_TYPE_ID,v_rct_rec(j).RCT_ACT_CODE, --acc sht desc
                  v_rct_rec(j).RCT_AMOUNT,v_rct_rec(j).RCT_AMOUNT,
                  'Y',v_rct_rec(j).RCT_AMOUNT,
                  TRUNC(SYSDATE),v_ggt_trans_no,
                  NULL, 'RC',
                  'U',  v_rct_rec(j).RCT_CUR_CODE,
                  TQC_INTERFACES_PKG.CURRENCYSYMBOL(v_rct_rec(j).RCT_CUR_CODE),v_rct_rec(j).RCT_BRH_CODE,
                  FMS_INTERFACES_PKG.BRH_NAME(v_rct_rec(j).RCT_BRH_CODE, 'S'),
                  TQC_INTERFACES_PKG.UserName(v_rct_rec(j).RCT_CAPTURED_BY)
                  );
                                 v_mtran_no,
                  V_TRAN_MAPPING(i).TMAP_MTRAN_REF_NO,--rct code
                  V_TRAN_MAPPING(i).TMAP_MTRAN_NO,
                  V_TRAN_MAPPING(i).TMAP_AMOUNT,
                  'C',
                  V_TRAN_MAPPING(i).TMAP_CHECK_MTRAN_REF_NO,--****
                  trunc(sysdate)*/
            END LOOP;
         END LOOP;
      END IF;
     -- commit;
   EXCEPTION
      WHEN OTHERS
      THEN
         RAISE_APPLICATION_ERROR (-20001,
                                  'POSTING GIS RCT :-' || SQLERRM (SQLCODE)
                                 );
   END post_gis_rct; 
 
 
 
 
 
/