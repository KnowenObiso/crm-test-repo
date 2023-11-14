insert into tqc_form_fields
   (fm_code, fm_module, fm_field_name, fm_mandatory, fm_field_label, 
    fm_visible, fm_screen_name, fm_disabled, fm_lms_mandatory, fm_gis_mandatory, 
    fm_group_id, fm_ui_id, fm_session_id, fm_group_ordering, fm_validate)
 values
   (146, 'ADMINISTRATION', 'USER_PERSONAL_RANK', 'N', 'Personnel Rank: ', 
    'Y', 'users.jspx', 'N', 'Y', 'N', 
    null, 'txtPersonalRank', null, null, 'Y');
    
insert into tqc_form_fields
   (fm_code, fm_module, fm_field_name, fm_mandatory, fm_field_label, 
    fm_visible, fm_screen_name, fm_disabled, fm_lms_mandatory, fm_gis_mandatory, 
    fm_group_id, fm_ui_id, fm_session_id, fm_group_ordering, fm_validate)
 values
   (147, 'ADMINISTRATION', 'USER_PERSONNEL', 'N', 'Personnel : ', 
    'Y', 'users.jspx', 'N', 'Y', 'N', 
    null, 'txtPersonnelName', null, null, 'Y');
    
insert into tqc_form_fields
   (fm_code, fm_module, fm_field_name, fm_mandatory, fm_field_label, 
    fm_visible, fm_screen_name, fm_disabled, fm_lms_mandatory, fm_gis_mandatory, 
    fm_group_id, fm_ui_id, fm_session_id, fm_group_ordering, fm_validate)
 values
   (148, 'ADMINISTRATION', 'USER_ACCOUNT_MANAGER', 'N', 'Account Manager? : ', 
    'Y', 'users.jspx', 'N', 'Y', 'N', 
    null, 'slctAccManager', null, null, 'Y');
    
insert into tqc_form_fields
   (fm_code, fm_module, fm_field_name, fm_mandatory, fm_field_label, 
    fm_visible, fm_screen_name, fm_disabled, fm_lms_mandatory, fm_gis_mandatory, 
    fm_group_id, fm_ui_id, fm_session_id, fm_group_ordering, fm_validate)
 values
   (149, 'ADMINISTRATION', 'USER_PHONE_NUMBER', 'N', 'Phone Number : ', 
    'Y', 'users.jspx', 'N', 'Y', 'N', 
    null, 'txtPhoneNumber', null, null, 'Y');
    
insert into tqc_form_fields
   (fm_code, fm_module, fm_field_name, fm_mandatory, fm_field_label, 
    fm_visible, fm_screen_name, fm_disabled, fm_lms_mandatory, fm_gis_mandatory, 
    fm_group_id, fm_ui_id, fm_session_id, fm_group_ordering, fm_validate)
 values
   (150, 'ADMINISTRATION', 'USER_SECURITY_QUESTIONS', 'N', 'Security Questions : ', 
    'Y', 'users.jspx', 'N', 'Y', 'N', 
    null, 'slctSecurityQuestion', null, null, 'Y');
    
insert into tqc_form_fields
   (fm_code, fm_module, fm_field_name, fm_mandatory, fm_field_label, 
    fm_visible, fm_screen_name, fm_disabled, fm_lms_mandatory, fm_gis_mandatory, 
    fm_group_id, fm_ui_id, fm_session_id, fm_group_ordering, fm_validate)
 values
   (151, 'ADMINISTRATION', 'USER_SECURITY_ANSWER', 'N', 'Security Answer : ', 
    'Y', 'users.jspx', 'N', 'Y', 'N', 
    null, 'txtSecurityAnswer', null, null, 'Y');
commit;