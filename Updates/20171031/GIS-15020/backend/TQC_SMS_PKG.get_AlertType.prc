--SPEC
PROCEDURE get_AlertType(    
                    v_refcur  OUT  alert_type_ref,
                    v_alrt_sys_code in NUMBER
                    ) IS;

--BODY
  PROCEDURE get_AlertType(    
                                v_refcur  OUT  alert_type_ref,
                                v_alrt_sys_code in NUMBER
                                ) IS
 BEGIN
 --Raise_error('org code '||v_alrt_sys_code);
    OPEN v_refcur FOR
     SELECT ALRT_CODE, ALRT_TYPE, ALRT_SYS_CODE, ALRT_EMAIL, ALRT_SMS,SYS_NAME ,
     ALRT_SCREEN,ALRT_EMAIL_MSGT_CODE,ALRT_SMS_MSGT_CODE,ALRT_GRP_USR_CODE,
     A.MSGT_MSG EMAIL,B.MSGT_MSG SMS,USR_NAME GRP_USER,
     ALRT_CHECK_ALERT,alrt_sht_desc
    FROM TQC_ALERT_TYPES,TQC_SYSTEMS ,
    TQC_MSG_TEMPLATES A,TQC_MSG_TEMPLATES B,TQC_USERS
    WHERE ALRT_SYS_CODE=SYS_CODE
    AND ALRT_EMAIL_MSGT_CODE=A.MSGT_CODE(+)
    AND ALRT_SMS_MSGT_CODE=B.MSGT_CODE(+) 
    AND ALRT_GRP_USR_CODE = USR_CODE(+)
    AND ALRT_SYS_CODE=v_alrt_sys_code;
    
 END ;
