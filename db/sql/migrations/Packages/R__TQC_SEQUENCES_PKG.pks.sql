/* This object may not be sorted properly in the script due to cirular references. */
--
-- TQC_SEQUENCES_PKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_sequences_pkg
AS
   FUNCTION get_number_format (
      v_number_type   IN   VARCHAR2,
      v_brn_code      IN   NUMBER,
      v_uw_yr         IN   NUMBER,
      v_clnt_type     IN   VARCHAR2,
      v_format        IN   VARCHAR2,
      v_surname       IN   VARCHAR2 DEFAULT NULL,
      v_other_names   IN   VARCHAR2 DEFAULT NULL
   )
      RETURN VARCHAR2;

   PROCEDURE get_seq (
      v_seq_type      IN       VARCHAR2,
      v_seq_no_type   IN       VARCHAR2,
      v_brn_code      IN       NUMBER,
      v_uw_yr         IN       NUMBER,
      v_clnt_type     IN       VARCHAR2,
      v_number_seq    OUT      NUMBER
   );

   PROCEDURE tqc_sequences_prc (
      v_add_edit         IN       VARCHAR2,
      v_code                      tqc_sequences.tseq_code%TYPE,
      v_type                      tqc_sequences.tseq_type%TYPE,
      v_no_type                   tqc_sequences.tseq_no_type%TYPE,
      v_brn                       tqc_sequences.tseq_brn%TYPE,
      v_uwyr                      tqc_sequences.tseq_uwyr%TYPE,
      v_next_no                   tqc_sequences.tseq_next_no%TYPE,
      v_err              OUT      VARCHAR2,
      v_org_code                  NUMBER,
      v_tseq_clnt_type            tqc_sequences.tseq_clnt_type%TYPE
            DEFAULT NULL
   );

   TYPE sequence_rec IS RECORD (
      tseq_code        tqc_sequences.tseq_code%TYPE,
      tseq_type        tqc_sequences.tseq_type%TYPE,
      tseq_no_type     tqc_sequences.tseq_no_type%TYPE,
      tseq_brn         tqc_sequences.tseq_brn%TYPE,
      tseq_uwyr        tqc_sequences.tseq_uwyr%TYPE,
      tseq_next_no     tqc_sequences.tseq_next_no%TYPE,
      branchname       VARCHAR2 (300),
      tseq_org_code    tqc_sequences.tseq_org_code%TYPE,
      tseq_clnt_type   tqc_sequences.tseq_clnt_type%TYPE
   );

   TYPE sequence_ref IS REF CURSOR
      RETURN sequence_rec;

   PROCEDURE get_sequences (v_refcur OUT sequence_ref);
END tqc_sequences_pkg; 
/