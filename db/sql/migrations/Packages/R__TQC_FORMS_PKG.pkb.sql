--
-- TQC_FORMS_PKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.TQC_FORMS_PKG AS
  PROCEDURE raise_error(v_msg IN VARCHAR2, nerrno IN NUMBER := NULL) IS
  BEGIN
    IF nerrno IS NULL THEN
      RAISE_APPLICATION_ERROR(-20015, v_msg || ' - ' || SQLERRM(SQLCODE));
    ELSE
      RAISE_APPLICATION_ERROR(nerrno, v_msg || ' - ' || SQLERRM(SQLCODE));
    END IF;
  END raise_error;

  FUNCTION get_field_varchar (v_col IN VARCHAR2, v_field IN VARCHAR2)
      RETURN VARCHAR2
   IS
      v_fm_mandatory     VARCHAR2 (1 BYTE);
      v_fm_field_label   VARCHAR2 (200 BYTE);
      v_fm_visible       VARCHAR2 (1 BYTE);
   BEGIN
      BEGIN
         SELECT fm_mandatory, fm_field_label, fm_visible
           INTO v_fm_mandatory, v_fm_field_label, v_fm_visible
           FROM tqc_form_fields
          WHERE fm_field_name = v_field;

         IF NVL (v_col, '*') = 'R'
         THEN
            RETURN (v_fm_mandatory);
         ELSIF NVL (v_col, '*') = 'L'
         THEN
            RETURN (v_fm_field_label);
         ELSIF NVL (v_col, '*') = 'V'
         THEN
            RETURN (v_fm_visible);
         END IF;

         RETURN NULL;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            RAISE_ERROR ('Field ' || v_field || ' not found ');
         WHEN TOO_MANY_ROWS
         THEN
            RAISE_ERROR (
               'More than one instance of Field ' || v_field || ' found ');
         WHEN OTHERS
         THEN
            RAISE_ERROR (
               'Error getting the Field' || v_field || ' Error: ' || SQLERRM);
      END;

      RETURN NULL;
   END;
   FUNCTION field_visible ( v_field IN VARCHAR2)
      RETURN VARCHAR2
   IS
      v_return       VARCHAR2 (20 BYTE):=Null;
   BEGIN
       BEGIN
          SELECT fm_visible
               INTO v_return
               FROM tqc_form_fields
              WHERE fm_field_name = v_field; 
              RETURN v_return;
          EXCEPTION
             WHEN NO_DATA_FOUND
             THEN
                raise_error ('Field ' || v_field || ' not found ');
             WHEN TOO_MANY_ROWS
             THEN
                raise_error (
                   'More than one instance of Field ' || v_field || ' found ');
             WHEN OTHERS
             THEN
                raise_error (
                   'Error getting the Field' || v_field || ' Error: ' || SQLERRM);
           END;
    RETURN v_return; 
   END;
    FUNCTION field_label ( v_field IN VARCHAR2)
      RETURN VARCHAR2
   IS
      v_return       VARCHAR2 (20 BYTE):=Null;
   BEGIN
       BEGIN
          SELECT fm_field_label
               INTO v_return
               FROM tqc_form_fields
              WHERE fm_field_name = v_field; 
              RETURN v_return;
          EXCEPTION
             WHEN NO_DATA_FOUND
             THEN
                raise_error ('Field ' || v_field || ' not found ');
             WHEN TOO_MANY_ROWS
             THEN
                raise_error (
                   'More than one instance of Field ' || v_field || ' found ');
             WHEN OTHERS
             THEN
                raise_error (
                   'Error getting the Field' || v_field || ' Error: ' || SQLERRM);
           END;
    RETURN v_return; 
   END;
    FUNCTION field_disabled ( v_field IN VARCHAR2)
      RETURN VARCHAR2
   IS
      v_return       VARCHAR2 (20 BYTE):=Null;
   BEGIN
       BEGIN
          SELECT fm_disabled
               INTO v_return
               FROM tqc_form_fields
              WHERE fm_field_name = v_field; 
              RETURN v_return;
          EXCEPTION
             WHEN NO_DATA_FOUND
             THEN
                raise_error ('Field ' || v_field || ' not found ');
             WHEN TOO_MANY_ROWS
             THEN
                raise_error (
                   'More than one instance of Field ' || v_field || ' found ');
             WHEN OTHERS
             THEN
                raise_error (
                   'Error getting the Field' || v_field || ' Error: ' || SQLERRM);
           END;
    RETURN v_return; 
   END;
   FUNCTION field_screen ( v_field IN VARCHAR2)
      RETURN VARCHAR2
   IS
      v_return       VARCHAR2 (20 BYTE):=Null;
   BEGIN
       BEGIN
          SELECT fm_screen_name
               INTO v_return
               FROM tqc_form_fields
              WHERE fm_field_name = v_field; 
              RETURN v_return;
          EXCEPTION
             WHEN NO_DATA_FOUND
             THEN
                raise_error ('Field ' || v_field || ' not found ');
             WHEN TOO_MANY_ROWS
             THEN
                raise_error (
                   'More than one instance of Field ' || v_field || ' found ');
             WHEN OTHERS
             THEN
                raise_error (
                   'Error getting the Field' || v_field || ' Error: ' || SQLERRM);
           END;
    RETURN v_return; 
   END;
   FUNCTION field_module ( v_field IN VARCHAR2)
      RETURN VARCHAR2
   IS
      v_return       VARCHAR2 (20 BYTE):=Null;
   BEGIN
       BEGIN
          SELECT fm_module
               INTO v_return
               FROM tqc_form_fields
              WHERE fm_field_name = v_field; 
              RETURN v_return;
          EXCEPTION
             WHEN NO_DATA_FOUND
             THEN
                raise_error ('Field ' || v_field || ' not found ');
             WHEN TOO_MANY_ROWS
             THEN
                raise_error (
                   'More than one instance of Field ' || v_field || ' found ');
             WHEN OTHERS
             THEN
                raise_error (
                   'Error getting the Field' || v_field || ' Error: ' || SQLERRM);
           END;
    RETURN v_return; 
   END;
    FUNCTION field_required ( v_field IN VARCHAR2, v_sys_code IN NUMBER DEFAULT NULL)
      RETURN VARCHAR2
   IS
      v_return       VARCHAR2 (20 BYTE):=Null;
   BEGIN
       BEGIN
          SELECT DECODE(v_sys_code,37,fm_gis_mandatory,27,fm_lms_mandatory,fm_mandatory)  
               INTO v_return
               FROM tqc_form_fields
              WHERE fm_field_name = v_field; 
              RETURN v_return;
          EXCEPTION
             WHEN NO_DATA_FOUND
             THEN
                raise_error ('Field ' || v_field || ' not found ');
             WHEN TOO_MANY_ROWS
             THEN
                raise_error (
                   'More than one instance of Field ' || v_field || ' found ');
             WHEN OTHERS
             THEN
                raise_error (
                   'Error getting the Field' || v_field || ' Error: ' || SQLERRM);
           END;
    RETURN v_return; 
   END;
  FUNCTION get_group_fields( v_group_id IN VARCHAR2, v_sys_code IN NUMBER DEFAULT NULL)
      RETURN SYS_REFCURSOR
   IS
      v_rc  SYS_REFCURSOR;
   BEGIN
      OPEN v_rc for
          SELECT fm_code,fm_ui_id,fm_session_id, fm_module, fm_field_name, DECODE(v_sys_code,37,fm_gis_mandatory,27,fm_lms_mandatory,fm_mandatory) fm_mandatory, fm_field_label, fm_visible, fm_screen_name, fm_disabled ,fm_validate
             FROM tqc_form_fields
              WHERE fm_group_id = v_group_id 
              order by fm_group_ordering;
    RETURN v_rc; 
   END;
FUNCTION check_required ( v_msg OUT VARCHAR2,v_field IN VARCHAR2, v_val IN VARCHAR2)
      RETURN VARCHAR2
   IS
      v_label       VARCHAR2 (250 BYTE):= Null; 
      v_required       VARCHAR2 (1 BYTE) :=  Null; 
   BEGIN
         v_required := tqc_forms_pkg.field_required ( v_field, NULL );
         v_label := tqc_forms_pkg.field_label(v_field);
         v_label := replace (v_label,':','');
         v_label := trim (v_label);
         ---raise_error ( 'v_val: ' ||v_val);
         
         IF NVL(v_required,'N')='Y' AND NVL(LENGTH(v_val),0)=0 THEN
            v_msg := 'Error Value Missing: Enter the '||v_label||'!';
            RETURN 'F';
        END IF; 
    RETURN 'S'; 
   END;      
END TQC_FORMS_PKG; 
/