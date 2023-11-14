--
-- TQC_ENTITIES_PKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_entities_pkg
AS
   /******************************************************************************
    NAME: TQC_ENTITIES_CURSOR
    PURPOSE:

      REVISIONS:
      Ver        Date        Author           Description
      ---------  ----------  ---------------  ------------------------------------
      1.0        5/6/2016             1. Created this package body.
   ******************************************************************************/
   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL)
   IS
   BEGIN
      IF v_err_no IS NULL
      THEN
         raise_application_error (-20015,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         raise_application_error (v_err_no,
                                  v_msg || ' - ' || SQLERRM (SQLCODE)
                                 );
      END IF;
   END raise_error;

   PROCEDURE entities_prc (
      v_add_edit               IN       VARCHAR2,
      v_new_ent_code           IN OUT   tqc_entities.ent_code%TYPE,
      v_ent_sht_desc           IN       tqc_entities.ent_sht_desc%TYPE,
      v_ent_name               IN       tqc_entities.ent_name%TYPE,
      v_ent_pin                IN       tqc_entities.ent_pin%TYPE,
      v_ent_postal_address     IN       tqc_entities.ent_postal_address%TYPE,
      v_ent_physical_address   IN       tqc_entities.ent_physical_address%TYPE,
      v_ent_tel1               IN       tqc_entities.ent_tel1%TYPE,
      v_ent_email_address      IN       tqc_entities.ent_email_address%TYPE
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         SELECT tqc_ent_code_seq.NEXTVAL
           INTO v_new_ent_code
           FROM DUAL;



         INSERT INTO tqc_entities
                     (ent_code, ent_sht_desc, ent_name, ent_pin,
                      ent_postal_address, ent_physical_address,
                      ent_tel1, ent_email_address, ent_status, ent_wef
                     )
              VALUES (v_new_ent_code, v_ent_sht_desc, v_ent_name, v_ent_pin,
                      v_ent_postal_address, v_ent_physical_address,
                      v_ent_tel1, v_ent_email_address, 'A', SYSDATE
                     );
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_entities
            SET ent_sht_desc = v_ent_sht_desc,
                ent_name = v_ent_name,
                ent_pin = v_ent_pin,
                ent_postal_address = v_ent_postal_address,
                ent_physical_address = v_ent_physical_address,
                ent_tel1 = v_ent_tel1,
                ent_email_address = v_ent_email_address
          WHERE ent_code = v_new_ent_code;
      ELSIF v_add_edit = 'D'
      THEN
         UPDATE tqc_entities
            SET ent_status = 'A',
                ent_wet = SYSDATE
          WHERE ent_code = v_new_ent_code;
      END IF;
   END;
END tqc_entities_pkg; 
/