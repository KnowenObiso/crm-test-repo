SET DEFINE OFF;
INSERT INTO tqc_sys_process_areas
            (sprca_code, sprca_sprc_code, sprca_area, sprca_sht_desc,
             sprca_visible
            )
     VALUES (6087, 580, 'USER GROUPS', 'SAUG',
             'Y'
            );
INSERT INTO tqc_sys_process_sub_areas
            (sprsa_code, sprsa_sprca_code, sprsa_sprc_code, sprsa_sub_area,
             sprsa_type, sprsa_sht_desc, sprsa_default_usr_code, sprsa_visible
            )
     VALUES (8222, 6087, 580, 'USER GROUPS ACCESS',
             'C', 'SAUGA', NULL, 'Y'
            );
COMMIT ;


SET DEFINE OFF;
INSERT INTO tqc_sys_process_areas
            (sprca_code, sprca_sprc_code, sprca_area, sprca_sht_desc,
             sprca_visible
            )
     VALUES (6088, 580, 'USER GROUP CATEGORIES', 'SAUGC',
             'Y'
            );
INSERT INTO tqc_sys_process_sub_areas
            (sprsa_code, sprsa_sprca_code, sprsa_sprc_code, sprsa_sub_area,
             sprsa_type, sprsa_sht_desc, sprsa_default_usr_code, sprsa_visible
            )
     VALUES (8223, 6088, 580, 'USER GROUP CATEGORIES ACCESS',
             'C', 'SAUGCA', NULL, 'Y'
            );
COMMIT ;