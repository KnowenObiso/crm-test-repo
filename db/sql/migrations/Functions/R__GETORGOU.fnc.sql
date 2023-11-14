CREATE OR REPLACE FUNCTION tq_crm.getorgou (
   v_org_code   IN   tqc_organizations.org_code%TYPE
)
   RETURN tqc_organizations.org_code%TYPE
IS
   v_org_code_ou     tqc_organizations.org_code%TYPE;
   v_org_otyp_code   tqc_organizations.org_otyp_code%TYPE;
BEGIN
   BEGIN
      SELECT org_otyp_code
        INTO v_org_otyp_code
        FROM tqc_organizations
       WHERE org_code = v_org_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         v_org_code_ou := NULL;
   END;

   IF v_org_otyp_code = 3
   THEN
      BEGIN
         SELECT org_org_code
           INTO v_org_code_ou
           FROM tqc_organizations
          WHERE org_code = v_org_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            v_org_code_ou := NULL;
      END;
   ELSIF v_org_otyp_code = 2
   THEN
      v_org_code_ou := v_org_code;
   ELSIF v_org_otyp_code = 1
   THEN
      v_org_code_ou := v_org_code;
   ELSE
      v_org_code_ou := NULL;
   END IF;

   RETURN v_org_code_ou;
EXCEPTION
   WHEN OTHERS
   THEN
      v_org_code_ou := NULL;
END;
/