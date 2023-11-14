--	tqc_sms_pkg.send_instant_sms

PROCEDURE send_instant_sms(v_sms_code NUMBER)
IS
   esendex_username   CONSTANT VARCHAR2 (40)   := 'EagleAfrica';
   -- 'your_username';
   esendex_password   CONSTANT VARCHAR2 (40)   := 'Efide@gleAfr1';
   -- 'your_password';
   esendex_account    CONSTANT VARCHAR2 (40)   := '+254724090528';
   -- 'your_account';--
   vrequest                    UTL_HTTP.req;
   vposttext                   VARCHAR2 (500);
   vresponse                   UTL_HTTP.resp;
   vresponsetext               VARCHAR2 (2000);
   verrortext                  VARCHAR2 (2000);
   v_url                       VARCHAR2 (2000);
   v_username                  VARCHAR2 (100);
   v_password                  VARCHAR2 (100);
   v_source                    VARCHAR2 (100);
   v_cnt                       NUMBER;
   v_success_msg               VARCHAR2(50);
   v_msg_id                     VARCHAR2(20);

   CURSOR cur_sms
   IS
      SELECT *
        FROM tqc_sms_messages
       WHERE sms_code = v_sms_code;
BEGIN
   SELECT SUBSTR (tss_url, 1, 2008), tss_username, tss_password, tss_source
    INTO v_url, v_username, v_password, v_source
     FROM tqc_system_sms;
     
     
    v_msg_id := LPAD(v_sms_code, 8, '0');

   FOR cur_sms_rec IN cur_sms
   LOOP
    begin
   
----------------------------------------------------------------------------
   -- Build text for the post action.
   -- For a field description, see
   -- http://www.esendex.com/secure/messenger/formpost/SendSMS.aspx
   ----------------------------------------------------------------------------
      vposttext :=
            'messageID='
         || utl_url.ESCAPE (v_msg_id, TRUE) 
         || CHR (38)
         || 'user='
         --|| utl_url.ESCAPE (esendex_username, TRUE)
         || utl_url.ESCAPE (v_username, TRUE)
         || CHR (38)
         || 'pass='
         || utl_url.ESCAPE (v_password, TRUE)
         --|| utl_url.ESCAPE (esendex_password, TRUE)
         || CHR (38)
         || 'shortCode='
         || utl_url.ESCAPE (v_source, TRUE)
         --|| utl_url.ESCAPE (esendex_account, TRUE)
         || CHR (38)
         || 'MSISDN='
         --|| utl_url.ESCAPE (precipient, TRUE)
         || utl_url.ESCAPE (cur_sms_rec.sms_tel_no, TRUE)
         || CHR (38)
         || 'MESSAGE='
         || utl_url.ESCAPE (cur_sms_rec.sms_msg, TRUE);
      -- || utl_url.ESCAPE (pbody, TRUE);
      DBMS_OUTPUT.put_line ('vPostText=' || vposttext);
      
      --raise_error(vposttext);
----------------------------------------------------------------------------

      -- if you need to set a proxy, uncomment next line.
----------------------------------------------------------------------------

      /* Utl_Http.set_proxy('proxy.it.my-company.com', 'my-company.com'); */
----------------------------------------------------------------------------
-- Send SMS through the Esendex SMS service.
----------------------------------------------------------------------------
--    vRequest := Utl_Http.begin_request
--                  ( url    => 'http://www.esendex.com/secure/messenger/formpost/SendSMS.aspx'
--                  , method => 'POST'
--                  );

      --   vrequest :=
--      UTL_HTTP.begin_request
--         (url         => 'http://172.16.4.84:9501/api?action=sendmessage&username=c2buser&password=c2buser123&recipient=0725954981&messagetype=SMS:TEXT&messagedata=HIISMSINAWORK',
--          method      => 'POST'
--         );
--v_url:=replace('http://172.16.4.84:9501/api?action=sendmessage&username=c2buser&password=c2buser123&recipient='|| cur_sms_rec.sms_tel_no ||'&messagetype=SMS:TEXT&messagedata='||cur_sms_rec.sms_msg,' ','%20');
--raise_error(v_url);
    v_url := REPLACE(v_url, '[USERNAME]', v_username);
    v_url := REPLACE(v_url, '[PASSWORD]', v_password);
    v_url := REPLACE(v_url, '[SOURCE]', v_source);
    v_url := REPLACE(v_url, '[MESSAGEID]', v_msg_id);
    
    v_url := REPLACE(v_url, '[DESTINATION]', replace(cur_sms_rec.sms_tel_no,'+',''));
    v_url := REPLACE(v_url, '[MESSAGE]', cur_sms_rec.sms_msg);
     v_url:=REPLACE(v_url,' ','%20');

--raise_error(v_url);
      vrequest :=
         UTL_HTTP.begin_request
            (
              v_url,
             method      => 'POST'
            );
            
      UTL_HTTP.set_header (r          => vrequest,
                           NAME       => 'Content-Type',
                           VALUE      => 'application/x-www-form-urlencoded'
                          );
      UTL_HTTP.set_header (r          => vrequest,
                           NAME       => 'Content-Length',
                           VALUE      => LENGTH (vposttext)
                          );
      UTL_HTTP.write_text (r => vrequest, DATA => vposttext);
      vresponse := UTL_HTTP.get_response (vrequest);
      
     
      
      DBMS_OUTPUT.put_line ('status_code=' || vresponse.status_code);

      IF vresponse.status_code = '200'
      THEN
         UTL_HTTP.read_text (vresponse, vresponsetext);
         
         UPDATE tqc_sms_messages
            SET sms_status = 'OK',SMS_SEND_DATE = SYSDATE--,SMS_SENT_RESPONSE=verrortext
          WHERE sms_code = cur_sms_rec.sms_code;
      ELSE
         verrortext :=
               'HTTP status: '
            || vresponse.status_code
            || '-'
            || vresponse.reason_phrase;
            
            
             UPDATE tqc_sms_messages
            SET  sms_sent_response=verrortext
          WHERE sms_code = cur_sms_rec.sms_code;
         raise_application_error (-20001,
                                  'Sending SMS failed with ' || verrortext
                                 );
      END IF;

      --
      UTL_HTTP.end_response (vresponse);
      --
   --   DBMS_OUTPUT.put_line ('vErrorText=' || verrortext);
 
       -- IF verrortext IS NOT NULL
      
       
      
        
     
      v_cnt:=NVL(v_cnt,0)+1;
      exception 
      when others then
      NULL;--RAISE;
      end;
   END LOOP;
   RETURN;
  IF NVL(v_cnt,0)=0 THEN
    RETURN;
  END IF;
  
-- exception
--  when others then
--    raise_error('error sending sms...'||SQLERRM(SQLCODE));  
   
END;