--Update the following procedure spec in: TQC_SETUPS_PKG specification

PROCEDURE service_provider_types_prc (
      v_add_edit        IN   VARCHAR2,
      v_spt_code        IN   tqc_service_provider_types.spt_code%TYPE,
      v_spt_sht_desc    IN   tqc_service_provider_types.spt_sht_desc%TYPE,
      v_spt_name        IN   tqc_service_provider_types.spt_name%TYPE,
      v_spt_status      IN   tqc_service_provider_types.spt_status%TYPE,
      v_spt_whtx_rate   IN   tqc_service_provider_types.spt_whtx_rate%TYPE,
      v_spt_vat_rate    IN   tqc_service_provider_types.spt_vat_rate%TYPE,
      v_spt_suffixes    IN   tqc_service_provider_types.spt_suffixes%TYPE
   );