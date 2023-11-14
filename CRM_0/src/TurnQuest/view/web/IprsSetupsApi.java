/*
* Copyright (c) 2010 TurnKey Africa Ltd. All Rights Reserved.
*
* This software is the confidential and proprietary information of TurnKey
* Africa Ltd. ("Confidential Information"). You shall not disclose such
* Confidential Information and shall use it only in accordance with the terms
* of the license agreement you entered into with TurnKey Africa Ltd.
*
* TURNKEY AFRICA MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY
* OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
* TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
* PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TURNKEY AFRICA SHALL NOT BE LIABLE
* FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
* DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
*/

package TurnQuest.view.web;


import TurnQuest.view.Accounts.WebUser;
import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.Ui.FormUi;
import TurnQuest.view.models.PostalCode;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.math.BigDecimal;

import java.sql.SQLException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.ActionEvent;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.servlet.http.HttpSession;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichDialog;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectItem;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.commons.dbutils.DbUtils;
import org.apache.myfaces.trinidad.component.UIXGroup;
import org.apache.myfaces.trinidad.convert.DateTimeConverter;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONObject;
import org.json.XML;


/**
 * The base backing bean for all the setups related pages. Includes properties
 * and methods that map to given  UI components in the relevant pages within
 * the setups section/menu.
 *
 * @author Dancan Kavagi
 */
public class IprsSetupsApi {

    private HttpSession session = null;

    private String[] iprs_Fields =
    { "idno", "pinno", "passport", "dob", "firstname", "surname", "othernames",
      "gender","fetch_type", "validated_by" ,"validated_dt"};
    private String[] iprs_Map =
    { "ID_Number", "Pin", "Passport", "Date_of_Birth", "First_Name", "Surname",
      "Other_Name", "Gender","Fetch_Type", "Validated_By" ,"Validated_Dt" };

    public static String IPRS_SQL_DATE_FMT = "MM/DD/YYYY HH12:MI:SS AM";

    public static String IPRS_JAVA_DATE_FMT = "MM/dd/yyyy hh:mm:ss a";

    /**
     * Default Constructor
     */

    public IprsSetupsApi(HttpSession _session) {
        session = _session;
    }

    public String clearFields() {
        clearTqFields();
        clearIprsFields();
        return null;
    }

    public void clearTqFields() {

        for (int i = 0; i < iprs_Fields.length; i++) {
            session.setAttribute("tq_" + iprs_Fields[i], "");
        }
    }

    public void clearIprsFields() {

        for (int i = 0; i < iprs_Fields.length; i++) {
            session.setAttribute("iprs_" + iprs_Fields[i], "");
        }
    }

    public String load(String type, BigDecimal code, RichDialog iprsDlg) {
        session.setAttribute("Tq_Account_Code", code);
        session.setAttribute("Iprs_Account_Type", type);
        clearFields();
        popTQFields(iprsDlg);
        popIprsFields(iprsDlg);
        GlobalCC.refreshUI(iprsDlg);
        GlobalCC.showPop("pt1:iprsCompPop");
        return null;
    }

    public String popTQFields(UIComponent iprsDlg) { 
        String type =
            GlobalCC.checkNullValues(session.getAttribute("Iprs_Account_Type"));
        BigDecimal code =
            GlobalCC.checkBDNullValues(session.getAttribute("Tq_Account_Code"));
        String query = "SELECT * FROM (SELECT \n" +
            "        clnt_code code,\n" +
            "        NULL firstname, \n" +
            "        clnt_other_names othernames, \n" +
            "        clnt_surname surname,\n" +
            "        clnt_id_reg_no idno, \n" +
            "        TO_CHAR(clnt_dob, '" + IprsSetupsApi.IPRS_SQL_DATE_FMT +
            "') dob, \n" +
            "        clnt_pin pinno,\n" +
            "        clnt_gender gender, \n" +
            "        clnt_passport_no passport,\n" +
            "        clnt_iprs_validated iprs_validated,\n" +
            "        'C' type\n" +
            "   FROM tqc_clients \n" +
            "UNION ALL\n" +
            "SELECT  \n" +
            "	     agn_code code,\n" +
            "        agn_contact_person firstname, \n" +
            "        NULL othernames, \n" +
            "        NULL surname,\n" +
            "        agn_id_no idno, \n" +
            "        TO_CHAR(agn_date_created, '" +
            IprsSetupsApi.IPRS_SQL_DATE_FMT + "') dob, \n" +
            "        agn_pin pinno,\n" +
            "        agn_gender gender, \n" +
            "        agn_id_no passport,\n" +
            "        agn_iprs_validated iprs_validated,\n" +
            "        'A' type\n" +
            "FROM tqc_agencies \n" +
            "UNION ALL\n" +
            "SELECT  spr_code code,\n" +
            "        spr_contact_person firstname, \n" +
            "        NULL othernames, \n" +
            "        NULL surname,\n" +
            "        spr_id_no idno, \n" +
            "        TO_CHAR(spr_date_created, '" +
            IprsSetupsApi.IPRS_SQL_DATE_FMT + "') dob, \n" +
            "        spr_pin pinno,\n" +
            "        spr_gender gender, \n" +
            "        spr_id_no passport,\n" +
            "        spr_iprs_validated iprs_validated,\n" +
            "        'SP' type\n" +
            "FROM tqc_service_providers) WHERE type = '" + type +
            "' and code = " + code;

        if (type != null) {
            Map vals = GlobalCC.fetchObject(query);
            if (!vals.isEmpty()) {
                String validated =
                    GlobalCC.checkNullValues(vals.get("iprs_validated"));
                session.setAttribute("tq_iprs_validated", validated);
                
                List<String> names = new ArrayList<String>();
                
                for (int i = 0; i < iprs_Fields.length; i++) {
                    String val =
                        GlobalCC.checkNullValues(vals.get(iprs_Fields[i].toLowerCase()));
                    session.setAttribute("tq_" + iprs_Fields[i],
                                         val != null ? val : "");
                    UIComponent ui = GlobalCC.getUIComponent(iprsDlg, "tq_" + iprs_Fields[i]);
                    if(ui!= null){
                        ((RichInputText)ui).setValue(val);
                        GlobalCC.refreshUI(ui);
                    }
                    if (val != null && "firstname".equals(iprs_Fields[i])) {
                        names.add(val);
                    }
                    if (val != null && "surname".equals(iprs_Fields[i])) {
                        names.add(val);
                    }
                    if (val != null && "othernames".equals(iprs_Fields[i])) {
                        names.add(val);
                    }
                }
                UIComponent txtFullName = GlobalCC.getUIComponent(iprsDlg,"tq_fullnames");
                if(txtFullName != null){
                    ((RichInputText)txtFullName).setValue(GlobalCC.join(names, " "));
                    GlobalCC.refreshUI(txtFullName);
                } 
            }
            GlobalCC.refreshUI(iprsDlg);
        }
        return null;
    }

    public String popIprsFields(UIComponent iprsDlg) {

        String type =
            GlobalCC.checkNullValues(session.getAttribute("Iprs_Account_Type"));
        BigDecimal code =
            GlobalCC.checkBDNullValues(session.getAttribute("Tq_Account_Code"));

        String idno =
            GlobalCC.checkNullValues(session.getAttribute("tq_idno"));
        String passport =
            GlobalCC.checkNullValues(session.getAttribute("tq_passport"));

        if ((idno != null) || (passport != null)) {
            List<String> filts = new ArrayList<String>();

            if (idno != null) {
                filts.add("iprs_idno ='" + idno + "'");
            }
            if (passport != null) {
                filts.add("iprs_passport ='" + passport + "'");
            }

            String query =
                "SELECT iprs_idno idno,iprs_pinno pinno,iprs_passport passport,TO_CHAR(iprs_dob, '" +
                IprsSetupsApi.IPRS_SQL_DATE_FMT +
                "')  dob,iprs_firstname firstName,iprs_surname surname,iprs_otherNames otherNames,iprs_gender gender" +
                " FROM tqc_iprs_data WHERE " + GlobalCC.join(filts, " AND ");
            System.out.println("query: " + query);

            Map vals = GlobalCC.fetchObject(query);

            if (vals.isEmpty()) {
                if ((passport != null) && (idno == null)) {
                    vals = GetDataByPassport(passport);
                } else if (idno != null) {
                    vals = GetDataByIdCard(idno);
                }
            }
            List<String> names = new ArrayList<String>();
            for (int i = 0; i < iprs_Fields.length; i++) {
                String val =
                    GlobalCC.checkNullValues(vals.get(iprs_Fields[i]));
                session.setAttribute("iprs_" + iprs_Fields[i],
                                     val != null ? val : "");
                UIComponent ui = GlobalCC.getUIComponent(iprsDlg,"iprs_" + iprs_Fields[i]);
                if(ui!=null){
                    ((RichInputText)ui).setValue(val);
                    GlobalCC.refreshUI(ui);
                } 
                 
                if (val != null && "firstname".equals(iprs_Fields[i])) {
                    names.add(val);
                }
                if (val != null && "surname".equals(iprs_Fields[i])) {
                    names.add(val);
                }
                if (val != null && "othernames".equals(iprs_Fields[i])) {
                    names.add(val);
                } 
            }  
            UIComponent txtFullName = GlobalCC.getUIComponent(iprsDlg,"iprs_fullnames");
            if(txtFullName!=null){
                ((RichInputText)txtFullName).setValue(GlobalCC.join(names, " "));
                GlobalCC.refreshUI(txtFullName);
            } 
        }
        System.out.println("session: " + session);
        return null;
    }

    public Map GetDataByIdCard(String idno) {

        String success = "";
        Map m = new HashMap<String, String>();

        try {
            String submit =
                "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "    <Body>\n" +
                "        <GetDataByIdCard xmlns=\"http://tempuri.org/\">\n" +
                "            <log>[user]</log>\n" +
                "            <pass>[password]</pass>\n" +
                "            <id_number>[idno]</id_number>\n" +
                "            <serial_number></serial_number>\n" +
                "        </GetDataByIdCard>\n" +
                "    </Body>\n" +
                "</Envelope>";

            submit = submit.replace("[idno]", idno != null ? idno : "");

            String soapAction =
                "\"http://tempuri.org/IServiceIPRS/GetDataByIdCard\"";

            session.setAttribute("Fetch_Type","I");  //by id

            m = SendIprsRequest(submit, soapAction, "GetDataByIdCardResult");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return m;
    }


    public Map GetDataByPassport(String passport) {

        String success = "";
        Map m = new HashMap<String, String>();
        try {
            String submit =
                "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "    <Body>\n" +
                "        <GetDataByPassport xmlns=\"http://tempuri.org/\">\n" +
                "            <log>[user]</log>\n" +
                "            <pass>[password]</pass>\n" +
                "            <id_number></id_number>\n" +
                "            <passport_number>[passport]</passport_number>\n" +
                "        </GetDataByPassport>\n" +
                "    </Body>\n" +
                "</Envelope>";

            submit =
                    submit.replace("[passport]", passport != null ? passport :
                                                        "");

            String soapAction =
                "\"http://tempuri.org/IServiceIPRS/GetDataByPassport\"";
            
            session.setAttribute("Fetch_Type","P"); //passport
            
            m = SendIprsRequest(submit, soapAction, "GetDataByPassportResult");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return m;
    }

    private Map SendIprsRequest(String xmlString, String soapAction,
                                String n) {
        String success = "";
        Map m = new HashMap<String, String>();
        String xmlBuff = new String(xmlString);
        String vals = null;
        String szUrls = GlobalCC.getSysParamValue("IPRS_URLS");
        String user = GlobalCC.getSysParamValue("IPRS_USER");
        String password = GlobalCC.getSysParamValue("IPRS_PASSWORD");
        String live_url = GlobalCC.checkNullValues(session.getAttribute("iprs_live_url"));
        xmlBuff = xmlBuff.replace("[user]", user); 
        xmlBuff = xmlBuff.replace("[password]", password);
        
        if(live_url!=null && szUrls != null) {
            szUrls = live_url +";"+szUrls;
        }
         
        String[] urls = szUrls!= null ? szUrls.split(";"): null;
        int fetched = 0;
        for(String url : urls ){
              try{ 
                        int StatusCode = 0 ;
                        
                        System.out.println("Using url: "+url);
                        HttpClient client = new DefaultHttpClient();
                        HttpPost post = new HttpPost(url);
                        // add header
                        post.setHeader("User-Agent", "Mozilla/5.0");
                        post.setHeader("Accept", " text/xml");
                        post.setHeader("Content-type", " text/xml");
                        post.setHeader("SOAPAction", soapAction);
            
                        HttpEntity entity = new ByteArrayEntity(xmlBuff.getBytes("UTF-8"));
                        post.setEntity(entity);
            
                        HttpResponse response = null;
                        response = client.execute(post);
                        
                        StatusCode = response.getStatusLine().getStatusCode();
            
                        System.out.println("\nSending 'POST' request to URL : " + url);
                        System.out.println("Post parameters : " + post.getEntity());
                        System.out.println("Response Code : " +StatusCode);
                        
                        if(StatusCode != 200) {
                            continue;
                        }
                        
                        BufferedReader rd =
                            new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                        
                        StringBuffer result = new StringBuffer();
                        String line = "";
                        while ((line = rd.readLine()) != null) {
                            line = line.replaceAll("&lt;", "<");
                            result.append(line);
                        }
                        vals = GlobalCC.checkNullValues(result.toString());
                        
                        if(vals != null && StatusCode == 200){
                            session.setAttribute("iprs_live_url",url);//save the working one
                            fetched++;
                            break;
                        }
                } catch (Exception e) {
                    session.removeAttribute("iprs_live_url");
                    e.printStackTrace(); 
                } 
         }
        if( fetched == 0){
            GlobalCC.EXCEPTIONREPORTING("IPRS connection failed!");
            return m;
        }
      try {    
            System.out.println("dirty response: " + vals);
            if (vals != null){
                    // cleaning
                    vals =
                vals.replaceAll("\\sxmlns(\\:\\w+)?\\=\"http\\:[\\w\\.\\/\\-]+\"", "");
                    vals =
                vals.replaceAll("\\si(\\:[\\w]+)?\\=\"(true|false)\"", "");
                    vals = vals.replaceAll("\\s\\/\\>", "/>");
                    vals = vals.replaceAll("\\<[\\w]+\\:", "<");
                    vals = vals.replaceAll("\\<\\/[\\w]+\\:", "</");
                    System.out.println("clean response: " + vals);
                
                    int found = vals.split(n, -1).length - 1;
                    if (vals.length() > 0 && found == 2) {
                        int a =
                            vals.indexOf("<" + n + ">") + ("<" + n + ">").length();
                        int b = vals.indexOf("</" + n + ">");
                        String output = vals.substring(a, b);
                        JSONObject o = XML.toJSONObject(output);
                        if (o != null) {
                            for (int i = 0; i < iprs_Fields.length; i++) {
                                String val = GlobalCC.getJsonValue(o, iprs_Map[i]);
                                m.put(iprs_Fields[i], val);
                            }
                            String idno =
                                GlobalCC.checkNullValues(session.getAttribute("tq_idno"));
                            String passport =
                                GlobalCC.checkNullValues(session.getAttribute("tq_passport"));
                            String fetchType =
                                GlobalCC.checkNullValues(session.getAttribute("Fetch_Type")); 
                            String validatedBy =
                                GlobalCC.checkNullValues(session.getAttribute("Username"));
    
                            if (idno != null) {
                                m.put("idno", idno);
                            }
                            if (passport != null) {
                                m.put("passport", passport);
                            }
                            if (fetchType != null) {
                                m.put("fetch_type", fetchType);
                            }
                            if (validatedBy != null) {
                                m.put("validated_by", validatedBy);
                            }
                            
                            m.put("ErrorCode",
                                  GlobalCC.getJsonValue(o, "ErrorCode"));
                            m.put("ErrorMessage",
                                  GlobalCC.getJsonValue(o, "ErrorMessage"));
                            m.put("ErrorOcurred",
                                  GlobalCC.getJsonValue(o, "ErrorOcurred"));
                            System.out.println("iprs_data: " + m);
                            if ("false".equals(m.get("ErrorOcurred"))) {
                
                                String query = "insert into tqc_iprs_data\n" +
                                    "   (iprs_code, iprs_idno, iprs_pinno, iprs_passport, iprs_dob, \n" +
                                    "    iprs_firstname, iprs_surname, iprs_othernames, iprs_gender,iprs_fetch_type, iprs_validated_by, iprs_validated_dt)\n" +
                                    " values\n" +
                                    "   (tqc_iprs_code_seq.nextval, :v_idno, :v_pinno, :v_passport, to_date(:v_dob, '" +
                                    IprsSetupsApi.IPRS_SQL_DATE_FMT + "'), \n" +
                                    "    :v_firstname, :v_surname, :v_othernames, :v_gender, :v_fetch_type, :v_validated_by, sysdate)";
                
                                for (int i = 0; i < iprs_Fields.length; i++) {
                                    String val =
                                        GlobalCC.checkNullValues(m.get(iprs_Fields[i]));
                                    query =
                                            query.replaceAll("\\:v_" + iprs_Fields[i],
                                                             val != null ?
                                                             "'" + val + "'" :
                                                             "null");
                                } 
                                GlobalCC.executeSql(query);
                            }
                        }
                 }
            } 
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return m;
    }

    public String SaveIprsData(Map m) {
        return null;
    }

    public String saveTQFields(RichDialog iprsDlg) {

        String type =
            GlobalCC.checkNullValues(session.getAttribute("Iprs_Account_Type"));
        BigDecimal code =
            GlobalCC.checkBDNullValues(session.getAttribute("Tq_Account_Code"));
        String idno =
            GlobalCC.checkNullValues(session.getAttribute("iprs_idno"));
        String passport =
            GlobalCC.checkNullValues(session.getAttribute("iprs_passport"));

        String query = null;

        if (code != null && type != null) {
            String otherNames =
                GlobalCC.checkNullValues(session.getAttribute("iprs_othernames"));
            String firstName =
                GlobalCC.checkNullValues(session.getAttribute("iprs_firstname"));
            if (otherNames != null) {
                if (firstName != null) {
                    otherNames = firstName + " " + otherNames;
                }
            }
            if ("C".equals(type)) {

                query =
                        "UPDATE tqc_clients set " + "clnt_id_reg_no = NVL(:v_idno,clnt_id_reg_no)," +
                        "clnt_pin = NVL(:v_pinno,clnt_pin)," +
                        "clnt_passport_no = NVL(:v_passport,clnt_passport_no)," +
                        "clnt_dob = TO_DATE(:v_dob, '" +
                        IprsSetupsApi.IPRS_SQL_DATE_FMT + "')," +
                        "clnt_surname = NVL(:v_surname,clnt_surname)," +
                        "clnt_other_names  = NVL(:v_othernames,clnt_other_names)," +
                        "clnt_gender = NVL(:v_gender,clnt_gender)," +
                        "clnt_iprs_validated = 'Y'," +
                        "clnt_validation_source = 'IPRS'" +
                        " WHERE clnt_code = " + code.toString();

                query =
                        query.replaceAll(":v_othernames", (otherNames != null ? "'" +
                                                           otherNames + "'" :
                                                           "null"));

            } else if ("A".equals(type)) {

                String doc_type = null;

                if (passport != null && idno == null) {
                    doc_type = "P";
                }

                if (idno != null && passport == null) {
                    doc_type = "I";
                }
                query =
                        "UPDATE tqc_agencies set " + "agn_id_no = NVL(:v_idno,agn_id_no)," +
                        "agn_pin = NVL(:v_pinno,agn_pin)," + "agn_id_no_doc_used = NVL(" +
                        (doc_type != null ? "'" + doc_type + "'" : "null") +
                        ",agn_id_no_doc_used)," +
                        "agn_passport_no = NVL(:v_passport,agn_passport_no)," +
                        "agn_date_created = TO_DATE(:v_dob, '" +
                        IprsSetupsApi.IPRS_SQL_DATE_FMT + "')," +
                        "agn_contact_person = replace(NVL(:v_firstname,'') ||' '|| NVL(:v_surname,'') || NVL(:v_othernames,''),'  ',' ')," +
                        "agn_gender = NVL(:v_gender,agn_gender)," +
                        "agn_iprs_validated = 'Y'," +
                        "agn_validation_source = 'IPRS'" +
                        " WHERE agn_code= " + code.toString();
            } else if ("SP".equals(type)) {
                query =
                        "UPDATE tqc_service_providers set " + "spr_id_no = NVL(:v_idno,spr_id_no)," +
                        "spr_pin = NVL(:v_pinno,spr_pin)," +
                        "spr_passport_no = NVL(:v_passport,spr_passport_no)," +
                        "spr_date_created = TO_DATE(:v_dob, '" +
                        IprsSetupsApi.IPRS_SQL_DATE_FMT + "')," +
                        "spr_contact_person = replace(NVL(:v_firstname,'') ||' '|| NVL(:v_surname,'') || NVL(:v_othernames,''),'  ',' ')," +
                        "spr_gender = NVL(:v_gender,spr_gender)," +
                        "spr_iprs_validated = 'Y'," +
                        "spr_validation_source = 'IPRS'" +
                        " WHERE spr_code= " + code.toString();
            }
            if (query != null) {
                for (int i = 0; i < iprs_Fields.length; i++) {
                    String val =
                        GlobalCC.checkNullValues(session.getAttribute("iprs_" +
                                                                      iprs_Fields[i]));
                    query =
                            query.replaceAll(":v_" + iprs_Fields[i], (val != null ?
                                                                      "'" +
                                                                      val +
                                                                      "'" :
                                                                      "null"));
                }
                System.out.println("query: " + query);
                if (!GlobalCC.executeSql(query)) {
                    GlobalCC.EXCEPTIONREPORTING("Unable to Updated TQ Records!");
                    return "failed";
                }
                popTQFields(iprsDlg);
                
                GlobalCC.INFORMATIONREPORTING("TQ Records Updated Successully!");
            }
        }
        return "success";
    }
    
    /*public String actionLoadIprsAgentDtls() {
	   session.setAttribute("Iprs_Account_Type", "A");
	   new IprsSetupsApi().refreshIPRSFields();
	   GlobalCC.showPop("pt1:iprsCompPop");
	   return null;
   }
   public String actionLoadIprsSPDtls() {
	   session.setAttribute("Iprs_Account_Type", "SP");
	   new IprsSetupsApi().refreshIPRSFields();
	   GlobalCC.showPop("pt1:iprsCompPop");
	   return null;
   }*/


}
