--
-- TQC_FORMS_PKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.TQC_FORMS_PKG IS
   FUNCTION get_field_varchar (v_col IN VARCHAR2, v_field IN VARCHAR2)
      RETURN VARCHAR2;
FUNCTION field_visible ( v_field IN VARCHAR2)
      RETURN VARCHAR2;
      
FUNCTION field_label ( v_field IN VARCHAR2)
      RETURN VARCHAR2;
      
FUNCTION field_disabled ( v_field IN VARCHAR2)
      RETURN VARCHAR2;
FUNCTION field_screen ( v_field IN VARCHAR2)
      RETURN VARCHAR2;
   FUNCTION field_module ( v_field IN VARCHAR2)
      RETURN VARCHAR2;
    FUNCTION field_required ( v_field IN VARCHAR2, v_sys_code IN NUMBER DEFAULT NULL)
      RETURN VARCHAR2; 
  FUNCTION get_group_fields( v_group_id IN VARCHAR2, v_sys_code IN NUMBER DEFAULT NULL)
      RETURN SYS_REFCURSOR; 
 FUNCTION check_required ( v_msg OUT VARCHAR2,v_field IN VARCHAR2, v_val IN VARCHAR2)
      RETURN VARCHAR2;                     
END TQC_FORMS_PKG; 
/