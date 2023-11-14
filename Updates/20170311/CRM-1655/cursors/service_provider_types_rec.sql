-- UPDATE THIS IN: - TQC_SETUPS_CURSOR  specification
   
   
   TYPE service_provider_types_rec IS RECORD (
      v_spt_code        tqc_service_provider_types.spt_code%TYPE,
      v_spt_sht_desc    tqc_service_provider_types.spt_sht_desc%TYPE,
      v_spt_name        tqc_service_provider_types.spt_name%TYPE,
      v_spt_status      tqc_service_provider_types.spt_code%TYPE,
      v_spt_whtx_rate   tqc_service_provider_types.spt_whtx_rate%TYPE,
      v_spt_vat_rate    tqc_service_provider_types.spt_vat_rate%TYPE,
      v_spt_suffixes    tqc_service_provider_types.spt_suffixes%TYPE
   );

   TYPE service_provider_types_ref IS REF CURSOR
      RETURN service_provider_types_rec;
