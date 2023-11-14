--
-- TQC_EMAIL_PKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_email_pkg
AS
   TYPE email_files_rec IS RECORD (
      file_path   VARCHAR2 (150),
      file_name   VARCHAR2 (50)
   );

   TYPE email_files_tab IS TABLE OF email_files_rec
      INDEX BY BINARY_INTEGER;

   v_attchment_rec   cesclob;
   v_attchment_tab   cesclobs;

   PROCEDURE create_email_msg (
      v_reciepient     IN       VARCHAR2,
      v_clnt_code      IN       NUMBER,
      v_agn_code       IN       NUMBER,
      v_quot_code      IN       NUMBER,
      v_quot_no        IN       VARCHAR2,
      v_pol_code       IN       NUMBER,
      v_pol_no         IN       VARCHAR2,
      v_clm_no         IN       VARCHAR2,
      v_msg_subj       IN       VARCHAR2,
      v_msgt_code      IN       NUMBER,
      v_files_tab      IN       email_files_tab,
      v_sys_code       IN       NUMBER,
      v_sys_mod        IN       VARCHAR2,
      v_user           IN       VARCHAR2,
      v_email_code     IN OUT   NUMBER,
      v_app_lvl        IN       VARCHAR2,
      v_usr_code       IN       NUMBER,
      v_to_send_date   IN       DATE
   );

   PROCEDURE send_email_msg (
      v_clnt_code           IN       NUMBER,
      v_agn_code            IN       NUMBER,
      v_quot_code           IN       NUMBER,
      v_pol_code            IN       NUMBER,
      v_pol_no              IN       VARCHAR2,
      v_clm_no              IN       VARCHAR2,
      v_email_addr          IN       VARCHAR2,
      v_msg_subj            IN       VARCHAR2,
      v_msg_text            IN       VARCHAR2,
      v_sys_code            IN       NUMBER,
      v_sys_mod             IN       VARCHAR2,
      v_files_tab           IN       email_files_tab,
      v_email_code          OUT      NUMBER,
      v_email_sender_addr   IN       VARCHAR2,
      v_to_send_date        IN       DATE
   );

   PROCEDURE load_attchmnt_from_url (v_eatt_code IN NUMBER, p_url IN VARCHAR2);

   PROCEDURE send_mail (v_email_code IN NUMBER);

   v_user            VARCHAR2 (35)
             := pkg_global_vars.get_pvarchar2 ('pkg_global_vars.pvg_username');

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        VARCHAR2 DEFAULT '',
      html_message   VARCHAR2 DEFAULT '',
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesfiles,
      clob_attach    cesclobs DEFAULT NULL,
      blob_attach    cesblobs DEFAULT NULL
   );

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        CLOB,
      html_message   VARCHAR2 DEFAULT '',
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesfiles,
      clob_attach    cesclobs DEFAULT NULL,
      blob_attach    cesblobs DEFAULT NULL
   );

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        VARCHAR2 DEFAULT '',
      html_message   CLOB,
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesfiles,
      clob_attach    cesclobs DEFAULT NULL,
      blob_attach    cesblobs DEFAULT NULL
   );

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        CLOB,
      html_message   CLOB,
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesfiles,
      clob_attach    cesclobs DEFAULT NULL,
      blob_attach    cesblobs DEFAULT NULL
   );

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        VARCHAR2 DEFAULT '',
      html_message   VARCHAR2 DEFAULT '',
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesclobs
   );

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        CLOB,
      html_message   VARCHAR2 DEFAULT '',
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesclobs
   );

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        VARCHAR2 DEFAULT '',
      html_message   CLOB,
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesclobs
   );

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        CLOB,
      html_message   CLOB,
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesclobs
   );

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        VARCHAR2 DEFAULT '',
      html_message   VARCHAR2 DEFAULT '',
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesblobs
   );

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        CLOB,
      html_message   VARCHAR2 DEFAULT '',
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesblobs
   );

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        VARCHAR2 DEFAULT '',
      html_message   CLOB,
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesblobs
   );

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        CLOB,
      html_message   CLOB,
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesblobs
   );

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        VARCHAR2 DEFAULT '',
      html_message   VARCHAR2 DEFAULT '',
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      filename1      VARCHAR2 DEFAULT NULL,
      filetype1      VARCHAR2 DEFAULT 'text/plain',
      filename2      VARCHAR2 DEFAULT NULL,
      filetype2      VARCHAR2 DEFAULT 'text/plain',
      filename3      VARCHAR2 DEFAULT NULL,
      filetype3      VARCHAR2 DEFAULT 'text/plain'
   );

   FUNCTION send_email (
      v_sendor      IN   VARCHAR2,
      v_recipient   IN   VARCHAR2,
      v_cc          IN   VARCHAR2,
      v_subject     IN   VARCHAR2,
      v_message     IN   VARCHAR2
   )
      RETURN NUMBER;
      
      PROCEDURE send_serv_req_mail (v_email_code IN NUMBER);
      PROCEDURE send_html_mail (v_email_code IN NUMBER);
   TYPE email_details_rec IS RECORD (
      emailsubject   VARCHAR2 (1000),
      emailbody      VARCHAR2 (1000)
   );

   TYPE email_details_cursor IS REF CURSOR
      RETURN email_details_rec;
   PROCEDURE get_email_details (
      v_email_details   OUT      email_details_cursor,
      v_claim_no        IN       VARCHAR2,
      v_email_user      IN       VARCHAR2,
      v_trans_no        IN       NUMBER DEFAULT NULL,
      v_type            IN       NUMBER DEFAULT NULL,
      v_from_dt         IN       DATE DEFAULT NULL,
      v_to_dt           IN       DATE DEFAULT NULL,
      v_asof_dt         IN       DATE DEFAULT NULL
   );
PROCEDURE storesentstatementlogs (
   v_email_to        IN   VARCHAR2,
   v_email_subject   IN   VARCHAR2,
   v_report_name     IN   VARCHAR2,
   v_email_from      IN   VARCHAR2,
   v_email_message   IN   VARCHAR2,
   v_email_code      IN   NUMBER,
   v_email_type      IN   NUMBER
);
PROCEDURE create_telematics_email_msg (
      v_reciepient     IN       VARCHAR2,
      v_clnt_code      IN       NUMBER,
      v_agn_code       IN       NUMBER,
      v_quot_code      IN       NUMBER,
      v_quot_no        IN       VARCHAR2,
      v_pol_code       IN       NUMBER,
      v_pol_no         IN       VARCHAR2,
      v_clm_no         IN       VARCHAR2,
      v_files_tab      IN       email_files_tab,
      v_sys_code       IN       NUMBER,
      v_sys_mod        IN       VARCHAR2,
      v_user           IN       VARCHAR2,
      v_email_code     IN OUT   NUMBER,
      v_app_lvl        IN       VARCHAR2,
      v_usr_code       IN       NUMBER,
      v_msg_subj       IN       VARCHAR2,
      v_email_body     IN       VARCHAR2,
      v_to_send_date   IN       DATE
   );
END tqc_email_pkg;
/