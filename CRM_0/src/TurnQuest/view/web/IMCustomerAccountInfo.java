package TurnQuest.view.web;

import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.HashMap;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;

import org.apache.commons.dbutils.DbUtils;

import org.json.JSONObject;
import org.json.XML;


public class IMCustomerAccountInfo
{     
  
  HttpSession session =
      (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
      private final String USER_AGENT = "Mozilla/5.0";
      
      public IMCustomerAccountInfo()
      {
        super();
      }
  
      
          public static HashMap<String,String> fetch() throws Exception {  
                  CbaCustomerAccountInfo http = new CbaCustomerAccountInfo(); 
                  return http.customerInfo( "KE0010001", "703011"); 
          } 
          public HashMap<String,String> customerInfo( String CompanyCode, String  CustomerNumber) {
                           HashMap<String,String> m = new HashMap();
                           DBConnector dbCon = new DBConnector();
                   OracleConnection conn = null;
                   OracleCallableStatement stmt = null; 
                   OracleResultSet r=null;
                  try {
                     
                      String urlParameters = null;//"CompanyCode=KE0010001&CustomerNumber=702303";
                      urlParameters = "?"+(CompanyCode!=null? ("CompanyCode=" + CompanyCode + "&"):"") +"CustomerNumber=" + CustomerNumber;
                      String url=getCustomerUrl();
                      JSONObject info = sendGet(url+urlParameters);;
                      JSONObject rs=info.getJSONObject("CustomerDetail");
                       if(rs!=null)
                       {
                         m.put("CLNT_OLD_SHT_DESC", CustomerNumber);
                         m.put("CLNT_FULLNAME", GlobalCC.jsonValue(rs.getString("Name")));
                         m.put("CLNT_REF_NO", GlobalCC.jsonValue(rs.get("Mnemonic")));
                         m.put("CLNT_TITLE", GlobalCC.jsonValue(rs.getString("Title")));
                         m.put("CLNT_COMPANY_CODE", GlobalCC.jsonValue(rs.getString("CompanyCode"))); 
                         m.put("CLNT_COU_SHT_DESC", GlobalCC.jsonValue(rs.getString("Residence"))); 
                         m.put("CLNT_EMAIL_ADDRS", GlobalCC.jsonValue(rs.getString("CustEmailId")));
                         m.put("CLNT_BRN_SHT_DESC", GlobalCC.jsonValue(rs.getString("Branch")));
                         m.put("CLNT_PHYSICAL_ADDRS", GlobalCC.jsonValue(rs.getString("Street")));
                         m.put("CLNT_POSTAL_ADDRS", rs.getString("Street")+"-"+rs.getString("PostCode"));
                         m.put("CLNT_POSTAL_CODE", GlobalCC.jsonValue(rs.getString("PostCode")));
                         m.put("CLNT_TEL", GlobalCC.jsonValue( rs.getString("CustMobPhone")));
                         m.put("CLNT_SMS_TEL",GlobalCC.jsonValue( rs.getString("CustMobPhone")));
                         m.put("CLNT_ID_REG_NO", GlobalCC.jsonValue(rs.getString("LegalIdNo")));

                         //missing
                         m.put("CLNT_PASSPORT_NO", null); 
                         m.put("CLNT_GENDER", null);  //inputs 'M' or 'F'
                         
                         String dob=GlobalCC.jsonValue(rs.getString("DateOfBirth"));
                         m.put("CLNT_DOB",GlobalCC.convertDate(dob,"yyyyMMdd","yyyy/MM/dd"));  
                         
                         m.put("CLNT_TYPE", "Individual");
                         String name = m.get("CLNT_FULLNAME");
                         if ( name != null ) {
                              if(name.toUpperCase().contains("LTD")||name.toUpperCase().contains("LIMITED"))
                              {
                                       m.put("CLNT_TYPE", "Corporate");
                              } 
                         }   
                         try {
                             
                             DBConnector dbConnector = new DBConnector();
                             conn = (OracleConnection) dbConnector.getDatabaseConnection();
                             String query="SELECT brn_code,brn_name,cou_code,cou_name,cou_zip_code,pst_twn_code,twn_name \n" + 
                                                         "   FROM  tqc_branches,tqc_countries,tqc_postal_codes,tqc_towns \n" + 
                                                         "   WHERE brn_sht_desc=':brn_sht_desc' \n" + 
                                                         "   AND cou_sht_desc=':cou_sht_desc' \n" + 
                                                         "   AND ( pst_twn_code=twn_code) AND ROWNUM=1";
                                           
                             query=query.replaceAll(":brn_sht_desc", m.get("CLNT_BRN_SHT_DESC"));
                             query=query.replaceAll(":cou_sht_desc", m.get("CLNT_COU_SHT_DESC"));
                             query=query.replaceAll(":pst_zip_code", m.get("CLNT_POSTAL_CODE"));
                             
                             stmt = (OracleCallableStatement)conn.prepareCall(query); 
                             r = (OracleResultSet)stmt.executeQuery(); 

                                 if (r.next()) {  
                                        m.put("CLNT_BRN_CODE", GlobalCC.checkNullValues(r.getString("brn_code")));
                                        m.put("CLNT_BRN_NAME", GlobalCC.checkNullValues(r.getString("brn_name")));
                                        m.put("CLNT_COU_ZIP_CODE", GlobalCC.checkNullValues(r.getString("cou_zip_code")));
                                        m.put("CLNT_COU_CODE",  GlobalCC.checkNullValues(r.getString("cou_code")));
                                        m.put("CLNT_COU_NAME",  GlobalCC.checkNullValues(r.getString("cou_name")));
                                        m.put("CLNT_TWN_CODE",  GlobalCC.checkNullValues(r.getString("pst_twn_code"))); 
                                        m.put("CLNT_TWN_NAME",  GlobalCC.checkNullValues(r.getString("twn_name")));  
                                        String sms=GlobalCC.checkNullValues(m.get("CLNT_SMS_TEL"));
                                        String couZipCode = GlobalCC.checkNullValues(m.get("CLNT_COU_ZIP_CODE"));
                                        if(sms != null && couZipCode!= null )
                                        { 
                                            if(sms.startsWith(couZipCode))
                                            {
                                                    m.put("CLNT_SMS_TEL","+"+sms);
                                                    m.put("CLNT_TEL","+"+sms);
                                            } 
                                        }
                                    }
                         } catch (Exception e) {
                             GlobalCC.EXCEPTIONREPORTING(conn, e);
                         }finally{
                             DbUtils.closeQuietly(conn, stmt, r);
                         }  
                       }  
                  } catch (Exception e) {
                     GlobalCC.EXCEPTIONREPORTING(e);
                  } 
                  return m; 
              } 
    public HashMap<String,String> imClientInfo(  String ClientNumber ) {
             HashMap<String,String> m = new HashMap();
            
             
            try {
               
                String urlParameters = null;
                urlParameters = "?" +"CIF=" + ClientNumber;
               
                String url=getClientUrl();
               // JSONObject info = sendGet(url+urlParameters);
                JSONObject info = sendPost(url,urlParameters);
                JSONObject rs=info.getJSONObject("soap:Envelope");
                 if(rs!=null)
                 {
                    
                    
                     
                      m.put("CIF", GlobalCC.jsonValue(rs.getString("CIF")));
                     m.put("OCCUPATION", GlobalCC.jsonValue(rs.getString("OCCUPATION")));
                     m.put("NATIONAL_ID", GlobalCC.jsonValue(rs.getString("NATIONAL_ID")));
                     m.put("POST_CODE", GlobalCC.jsonValue(rs.getString("POST_CODE")));
                     m.put("NAME", GlobalCC.jsonValue(rs.getString("NAME")));
                     m.put("EMAIL", GlobalCC.jsonValue(rs.getString("EMAIL")));
                     m.put("CITY", GlobalCC.jsonValue(rs.getString("CITY")));
                     m.put("COUNTRY", GlobalCC.jsonValue(rs.getString("COUNTRY")));
                     m.put("MOBILE", GlobalCC.jsonValue(rs.getString("MOBILE")));
                     m.put("PERMANENT_ADDRESS_1", GlobalCC.jsonValue(rs.getString("PERMANENT_ADDRESS_1")));
                  
                  
                 }  
            } catch (Exception e) {
               GlobalCC.EXCEPTIONREPORTING(e);
            } 
            return m; 
        } 
    
    
          public String getCustomerUrl() {
            String value = null; 
            value=GlobalCC.checkNullValues(session.getAttribute("CUSTOMER_INFO_URL"));
            if(value==null)
            {
                value=(String) GlobalCC.getSysParamValue("CUSTOMER_INFO_URL");
                value=GlobalCC.checkNullValues(value);
                session.setAttribute("CUSTOMER_INFO_URL",value);
            }
            return value;
          }
          
    public String getClientUrl() {
      String value = null; 
      value=GlobalCC.checkNullValues(session.getAttribute("CLIENT_INFO_URL"));
      if(value==null)
      {
          value=(String) GlobalCC.getSysParamValue("CLIENT_INFO_URL");
          value=GlobalCC.checkNullValues(value);
          session.setAttribute("CLIENT_INFO_URL",value);
      }
      return value;
    }
          
          // HTTP POST request
    private JSONObject sendPost(String url,String urlParameters) throws Exception {
            JSONObject jsonObject=null;
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    //add reuqest header
                    con.setRequestMethod("POST");
                    con.setRequestProperty("User-Agent", USER_AGENT);
                    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
     
                    
                    // Send post request
                    con.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(urlParameters);
                    wr.flush();
                    wr.close();

                    int responseCode = con.getResponseCode();
                    System.out.println("\nSending 'POST' request to URL : " + url);
                    System.out.println("Post parameters : " + urlParameters);
                    System.out.println("Response Code : " + responseCode);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                    }
                    in.close(); 
                    //print result
                    System.out.println("output: [begin]"+response.toString()+"[end]");
                    //Used: org.json.XML class from java-json.jar
                    jsonObject = XML.toJSONObject(response.toString());
                    return jsonObject; 
            }
    private JSONObject sendGet(String url) throws Exception {
                     JSONObject jsonObject=null;
                    //String url = "http://www.google.com/search?q=mkyong";
                    System.out.println("\nSending 'GET' request to URL : " + url);
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    // optional default is GET
                    con.setRequestMethod("GET");

                    //add request header
                    con.setRequestProperty("User-Agent", USER_AGENT);

                    int responseCode = con.getResponseCode();
                    
                    System.out.println("Response Code : " + responseCode);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                    }
                    in.close();

                    //print result
                   
                    //Used: org.json.XML class from java-json.jar
                    String values= response.toString();
                    values = values.replaceAll("\\+", "");//this will fix bug for +254725365441 where the number is automically converted to scientific format e.g. 2.54722860655E11
                    System.out.println("output: [begin]"+response.toString()+"[end]");
                    jsonObject = XML.toJSONObject(values);
                    return jsonObject;

            }
}

/*
 
 SET DEFINE OFF;
Insert into TQC_PARAMETERS
   (PARAM_CODE, PARAM_NAME, PARAM_VALUE, PARAM_STATUS, PARAM_DESC)
 Values
   (2017022403, 'CLIENT_INFO_URL', 'http://10.176.18.178/cba/CustomerInfo.asmx', 'ACTIVE', 'THIS PARAMETER INDICATES CLIENT info webservice url');
COMMIT;

 
 SupplierData
http://www.webservicex.net/medicareSupplier.asmx?WSDL
http://www.webservicex.net/medicareSupplier.asmx?op=GetSupplierByZipCode
http://ws.cdyne.com/phoneverify/phoneverify.asmx?op=CheckPhoneNumber?wsdl
http://ws.cdyne.com/phoneverify/phoneverify.asmx?op=CheckPhoneNumber


 
 */
