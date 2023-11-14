--
-- TQC_ENTITIES_PKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_entities_pkg
AS
   /******************************************************************************
      NAME:       TQC_ENTITIES_PKG
      PURPOSE:

      REVISIONS:
      Ver        Date        Author           Description
      ---------  ----------  ---------------  ------------------------------------
      1.0        18/11/2016             1. Created this package.
   ******************************************************************************/
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
   );
END tqc_entities_pkg; 
/