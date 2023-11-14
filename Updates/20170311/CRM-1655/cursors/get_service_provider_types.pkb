 --Update the procedure in: TQC_SETUPS_CURSOR body
 
 
 PROCEDURE get_service_provider_types (
      v_service_provider_types_ref   OUT   service_provider_types_ref
   )
   IS
   BEGIN
      OPEN v_service_provider_types_ref FOR
         SELECT   spt_code, spt_sht_desc, spt_name, spt_status,
                  spt_whtx_rate, spt_vat_rate,spt_suffixes
                  
             FROM tqc_service_provider_types
         ORDER BY spt_name;
   END get_service_provider_types;

   