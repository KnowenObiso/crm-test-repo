package TurnQuest.view.Base;


import TurnQuest.view.Connect.DBConnector;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.http.HttpSession;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;

import org.json.JSONObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;


//import java.io.FileOutputStream


public class ChannelManager {
	public final String USER_AGENT = "Mozilla/5.0";
    public ChannelManager() {
        super();
    }
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    public static void main(String[] args) throws Exception {
        System.out.println("\nTesting 2 - Send Http POST request");
        Map clntDtls = new HashMap();
        ChannelManager http = new ChannelManager();
        System.out.println("\nTesting 2 - Send Http POST request");
        String sessionToken = http.urlCall();
        String value = "10025348";
        //String value = "1104006960";
        System.out.println(sessionToken);
        if (sessionToken != null) {
                System.out.println("get: "+http.getCustomerInfo(value));
        }
    }

    


    public String muleUrl() {
        String url = null;
        try {

            Context initCtx = new InitialContext();
            Context envCtx = (Context)initCtx.lookup("java:comp/env");
            url = (String)envCtx.lookup("chMan");

        } catch (Exception e) {
            e.printStackTrace();
            //  GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return url;
    }

    private String initializeSession() {
        String sessionToken = null;
        String muleloc = muleUrl();
        String url = muleloc + "/kcbSessionInit";
        URL obj = null;
        try {
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection)obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader b =
                new BufferedReader(new InputStreamReader(con.getInputStream()));
            String xmlString = "";
            StringBuffer xmlBuffer = new StringBuffer();
            while ((xmlString = b.readLine()) != null) {
                xmlBuffer.append(xmlString);
            }

            //print result
            xmlString = xmlBuffer.toString();
            if (xmlBuffer.length() > 0) {
                System.out.println("Got XML: " + xmlString);
                DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xmlString));
                Document doc = dBuilder.parse(is);

                doc.getDocumentElement().normalize();
                NodeList nList =
                    doc.getElementsByTagName("org.turnquest.kcb.SessionDetails");
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                        Element eElement = (Element)nNode;
                        sessionToken =
                                eElement.getElementsByTagName("sessionToken").item(0).getTextContent();

                    }


                }
            } else {
                System.out.println("No XML document received");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessionToken; 
    }

   public Map getCustomerInfo(String clientVal) {
        System.out.println("\nTesting 2 - Send Http POST request");
        Map m = new HashMap();
        System.out.println("\nTesting 2 - Send Http POST request");
        String sessionToken = urlCall();
        //String value = "10025348";
        //String value = "1104006960";
        System.out.println(sessionToken);
       String customer_url="http://172.17.74.131:63081/kcbCustomerInfo";
       String account_url="http://172.17.74.131:63081/kcbAccountInfo";
        if (sessionToken != null) {

            if (clientVal.length() >= 10) {
                System.out.println("Accoint Info Customer Details");
                m = customerInfo(account_url,sessionToken, clientVal);
            } else {
                System.out.println("Customer Details");
                m = customerInfo(customer_url,sessionToken, clientVal);
            }
        }

        return m;
    }
   
    public Map getIMCustomerInfo(String clientVal) {
         System.out.println("\nTesting 2 - Send Http POST request");
         Map m = new HashMap();
         System.out.println("\nTesting 2 - Send Http POST request");
         String sessionToken = imUrlCall();
       
         System.out.println(sessionToken);
         
        String BACIFDetailsService_url="http://192.168.205.26:1880/bcwsgateway/services/BACIFDetailsService";
       
         if (sessionToken != null) {

             if (clientVal.length() != 0) {
                 System.out.println("Client Details");
                 m = imClientInfo(BACIFDetailsService_url,sessionToken, clientVal);
             
             }
         }

         return m;
     }
   

   public String fetchBranchName(String brnCode) {
		OracleConnection conn = null;
		String branchName = null;
		try {

			DBConnector dbConnector = new DBConnector();
			conn = (OracleConnection) dbConnector.getDatabaseConnection();
			OracleCallableStatement callStmt = null;
			String query = "select brn_name from tqc_branches where BRN_CODE = ?";	
			callStmt = (OracleCallableStatement) conn.prepareCall(query);
			callStmt.setString(1, brnCode);
			OracleResultSet rs = (OracleResultSet) callStmt.executeQuery();

			while (rs.next()){
				branchName = rs.getString(1);
			}
			rs.close();
			callStmt.close();
			conn.close();
		} catch (Exception e) {
			GlobalCC.EXCEPTIONREPORTING(conn, e);
		}
		return branchName;
	}
    private String urlCall() {
        String retval = null;
        String muleloc = muleUrl();
        //String url = muleloc+"/kcbSessionInit";
        try {

            String url = "http://172.17.74.131:63081/kcbSessionInit";
            JSONObject response = sendPOSTRequest(url, null);

            System.out.println(response);
            System.out.println(response.getClass());
            System.out.println(response.getString("sessionToken"));
            retval = response.getString("sessionToken");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return retval;

    }
    
    private String imUrlCall() {
        String retval = null;
      
        try {

            String url = "http://192.168.205.26:1880/bcwsgateway/services/BACIFDetailsService";
            JSONObject response = sendPOSTRequest(url, null);

            System.out.println(response);
            System.out.println(response.getClass());
            System.out.println(response.getString("sessionToken"));
            retval = response.getString("sessionToken");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return retval;

    }
	private Map customerInfo( String url,String sessionToken, String clientVal) {
        Map m = new HashMap();
        try {
            //String muleloc = muleUrl();
            //String url = muleloc+"/kcbCustomerInfo";
           
            String urlParameters = null;
            urlParameters =
                    "sessionToken=" + sessionToken + "&value=" + clientVal;
            JSONObject rs = sendPOSTRequest(url, urlParameters);

            System.out.println(rs);
            m.put("customerId", rs.getString("customerId"));
            m.put("customerName", rs.getString("customerName"));
            m.put("customerAddress",rs.getString("customerAddress"));
            m.put("customerIdentifDoc",rs.getString("customerIdentifDoc"));
            m.put("emailAddress", rs.getString("emailAddress"));
            m.put("phoneNumber", rs.getString("phoneNumber"));
            m.put("offphone", rs.getString("offphone"));
			
            if (rs.getString("dateofbirth") != null) {
                m.put("dateofbirth", GlobalCC.parseCMDate(rs.getString("dateofbirth")));
            }

            if (rs.getString("gender") != null) {
                if (rs.getString("gender").equalsIgnoreCase("MALE")) {
                    m.put("gender", "M");
                } else {
                    m.put("gender", "F");
                }
            } else {
                m.put("gender", "M");
            }
            if (rs.getString("maritalstatus") != null) {
                if (rs.getString("maritalstatus").equalsIgnoreCase("MARRIED")) {
                    m.put("maritalstatus", "MR");
                } else if (rs.getString("maritalstatus").equalsIgnoreCase("SEPERATED")) {
                    m.put("maritalstatus", "SP");
                } else if (rs.getString("maritalstatus").equalsIgnoreCase("DIVORCED")) {
                    m.put("maritalstatus", "DV");
                } else if (rs.getString("maritalstatus").equalsIgnoreCase("SINGLE")) {
                    m.put("maritalstatus", "SN");
                } else if (rs.getString("maritalstatus").equalsIgnoreCase("WIDOWED")) {
                    m.put("maritalstatus", "WD");
                } else {
                    m.put("maritalstatus", "SN");
                }
            } else {
                m.put("maritalstatus", "SN");
            }
            m.put("occupation", rs.getString("occupation"));
            m.put("employersname", rs.getString("employersname"));
            
			String postAdd = "";
            if (rs.getString("street") != null && "".equals(rs.getString("street"))!=true ) {
                postAdd = rs.getString("street");
            }
			if (rs.getString("customerAddress") != null && "".equals(rs.getString("customerAddress"))!=true ) {
                postAdd = rs.getString("customerAddress");
            }
			if (rs.getString("postcode") != null && "".equals(rs.getString("postcode"))!=true ) {
               // postAdd = postAdd + "-" + rs.getString("postcode");
            }
            if (rs.getString("towncountry") != null && "".equals(rs.getString("towncountry"))!=true) {
               // postAdd = postAdd + "," + rs.getString("towncountry");
            }
            
            postAdd = postAdd.replace("null", "");
            m.put("postAdd", postAdd);
			
			m.put("towncountry", rs.getString("towncountry"));
			m.put("postcode", rs.getString("postcode"));
			String brnCode=rs.getString("accountofficer");
            m.put("accountofficer",brnCode);
            m.put("branchName",fetchBranchName(brnCode));
			
			String localref=rs.getString("localref");
			
            if (rs.getString("localref") != null) {
                if (rs.getString("localref").toUpperCase().toString().contains("INDIVIDUAL")) {
                    m.put("localref", "Individual");
                } else {
                    m.put("localref", "Corporate");
                }
				String emailAddress=GlobalCC.extractSubStr(localref,GlobalCC.EMAIL_REGX_EXT);
				String phoneNumber=GlobalCC.extractSubStr(localref,GlobalCC.PHONE_NO_EXT);
				m.put("emailAddress", emailAddress!=null ? emailAddress : rs.getString("emailAddress"));
				m.put("phoneNumber", phoneNumber!=null ? phoneNumber :rs.getString("phoneNumber"));
            } else {
                m.put("localref", "Individual");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return m;

    }


    private Map imClientInfo( String url,String sessionToken, String clientVal) {
    Map m = new HashMap();
    try {
       
        String urlParameters = null;
        urlParameters = "sessionToken=" + sessionToken + "&value=" + clientVal;
        JSONObject rs = sendPOSTRequest(url, urlParameters);

        System.out.println(rs);
        
        
        m.put("BRANCH_ID", rs.getString("BRANCH_ID"));
        m.put("BRANCH", rs.getString("BRANCH"));
        m.put("CIF", rs.getString("CIF"));
        m.put("NAME", rs.getString("NAME"));
        m.put("OCCUPATION", rs.getString("OCCUPATION"));
        m.put("NATIONAL_ID", rs.getString("NATIONAL_ID"));
        m.put("PASSPORT", rs.getString("PASSPORT"));
        m.put("PASSPORT_EXPIRY_DATE", rs.getString("PASSPORT_EXPIRY_DATE"));
        
        
      
        String EMAIL=GlobalCC.extractSubStr(GlobalCC.checkNullValues(rs.getString("EMAIL")),GlobalCC.EMAIL_REGX_EXT);
        m.put("EMAIL", EMAIL!=null ? EMAIL : rs.getString("EMAIL"));
      
        m.put("POST_CODE", rs.getString("POST_CODE"));
        m.put("ADDRESS", rs.getString("ADDRESS"));
        m.put("CITY", rs.getString("CITY"));
        m.put("COUNTRY", rs.getString("COUNTRY"));
        
        
        String COMMUNICATION_PHONE_NUMBER_1=GlobalCC.extractSubStr(GlobalCC.checkNullValues(rs.getString("COMMUNICATION_PHONE_NUMBER_1")),GlobalCC.PHONE_NO_EXT);
        m.put("COMMUNICATION_PHONE_NUMBER_1", COMMUNICATION_PHONE_NUMBER_1); 
         
         
        String MOBILE=GlobalCC.extractSubStr(GlobalCC.checkNullValues(rs.getString("MOBILE")),GlobalCC.PHONE_NO_EXT);
        m.put("MOBILE", MOBILE); 
        
         
        
      
        
        m.put("COMMUNICATION_TELEX_NUMBER", rs.getString("COMMUNICATION_TELEX_NUMBER"));
        m.put("FAX_NUMBER", rs.getString("FAX_NUMBER"));
        m.put("PAGER_NUMBER", rs.getString("PAGER_NUMBER"));
        
        
        
        
        String PERMANENT_EMAIL_ID=GlobalCC.extractSubStr(GlobalCC.checkNullValues(rs.getString("PERMANENT_EMAIL_ID")),GlobalCC.EMAIL_REGX_EXT);
        m.put("PERMANENT_EMAIL_ID", PERMANENT_EMAIL_ID!=null ? PERMANENT_EMAIL_ID : rs.getString("PERMANENT_EMAIL_ID"));
        
        
        
        m.put("PERMANENT_ADDRESS_1", rs.getString("PERMANENT_ADDRESS_1"));
        m.put("PERMANENT_ADDRESS_2", rs.getString("PERMANENT_ADDRESS_2"));
        m.put("PERMANENT_CITY_CODE", rs.getString("PERMANENT_CITY_CODE"));
        m.put("PERMANENT_COUNTRY_CODE", rs.getString("PERMANENT_COUNTRY_CODE"));
        
        
        String PERMANENT_PHONE_NUMBER_1=GlobalCC.extractSubStr(GlobalCC.checkNullValues(rs.getString("PERMANENT_PHONE_NUMBER_1")),GlobalCC.PHONE_NO_EXT);
         m.put("PERMANENT_PHONE_NUMBER_1", PERMANENT_PHONE_NUMBER_1); 
        
        
        String PERMANENT_PHONE_NUMBER_2=GlobalCC.extractSubStr(GlobalCC.checkNullValues(rs.getString("PERMANENT_PHONE_NUMBER_2")),GlobalCC.PHONE_NO_EXT);
         m.put("PERMANENT_PHONE_NUMBER_2", PERMANENT_PHONE_NUMBER_2); 
        
        
        m.put("PERMANENT_TELEX_NUMBER", rs.getString("PERMANENT_TELEX_NUMBER"));
        m.put("PERMANENT_FAX_NUMBER", rs.getString("PERMANENT_FAX_NUMBER"));
      
        
                    
        if (rs.getString("GENDER") != null) {
            if (rs.getString("GENDER").equalsIgnoreCase("MALE")) {
                m.put("GENDER", "M");
            } else {
                m.put("GENDER", "F");
            }
        } else {
            m.put("GENDER", "M");
        }
        
        
        if (rs.getString("DOB") != null) {
            m.put("DOB", GlobalCC.parseCMDate(rs.getString("DOB")));
        }


      
                           
     
    } catch (Exception e) {
        e.printStackTrace();
    }
    return m;

    }





    private JSONObject sendPOSTRequest(String urlString_,
                                       String urlParameters) {

        JSONObject jsonObnject = null;
        try {
            String data = new String();


            URL url = new URL(urlString_);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Accept", "*");


            // Send post request
            conn.setDoOutput(true);
            if (urlParameters != null) {
                DataOutputStream wr =
                    new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();
            }

            //conn.setDoOutput(true);
            //OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            //writer.write(data);
            //writer.flush();

            HttpURLConnection httpConn = (HttpURLConnection)conn;
            int responseCode = httpConn.getResponseCode();

            BufferedReader reader;
            System.out.println(responseCode);
            if (responseCode == 200) {
                reader =
                        new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            } else {
                reader =
                        new BufferedReader(new InputStreamReader(httpConn.getErrorStream()));
            }

            String inputLine = reader.readLine();
            reader.close();

            jsonObnject = new JSONObject(inputLine);

        } catch (Exception e) {
            e.printStackTrace();


        }
        return jsonObnject;
    }


    private void sendPost() throws Exception {


        String url = "http://10.176.18.182:63080/json/balance";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection)obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        String urlParameters = null;
        // urlParameters = "session=dsdsdsdsd&value=232";
        urlParameters = "corporate";
        //urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

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

        BufferedReader b =
            new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuffer response = new StringBuffer();
        String xmlString = "";
        StringBuffer xmlBuffer = new StringBuffer();
        while ((xmlString = b.readLine()) != null) {
            System.out.println(b.readLine());
            xmlBuffer.append(xmlString);
        }

        //print result
        System.out.println(response.toString());
        System.out.println(b);

        try {

            String city = null;
            String country = null;


            xmlString = xmlBuffer.toString();
            if (xmlBuffer.length() > 0) {

                System.out.println("Got XML: " + xmlString);
                DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xmlString));
                Document doc = dBuilder.parse(is);

                doc.getDocumentElement().normalize();
                NodeList nList =
                    doc.getElementsByTagName("training.soap.ip2geo.IPInformation");
                //System.out.println("City");
                // System.out.println(nList);
                for (int temp = 0; temp < nList.getLength(); temp++) {

                    Node nNode = nList.item(temp);
                    System.out.println("Node Type");
                    System.out.println(nNode.getNodeType());

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                        Element eElement = (Element)nNode;
                        city =
eElement.getElementsByTagName("city").item(0).getTextContent();
                        System.out.println(city);
                        country =
                                eElement.getElementsByTagName("country").item(0).getTextContent();
                        System.out.println(country);
                        /*if (eElement.getElementsByTagName("newRequest").item(0) != null) {
                              newRequest = eElement.getElementsByTagName("newRequest").item(0).getTextContent();
                              parameters = eElement.getElementsByTagName("parameters").item(0).getTextContent();
                              freeflow = eElement.getElementsByTagName("freeflow").item(0).getTextContent();
                              sessionId = eElement.getElementsByTagName("sessionId").item(0).getTextContent();
                              subscriberInput = eElement.getElementsByTagName("subscriberInput").item(0).getTextContent();
                              ussdString = subscriberInput;
                          } else {
                              ussdString = "end";
                          }*/
                        //createxml();
                    }

                    doc = dBuilder.newDocument();
                    Element rootElement = doc.createElement("response");
                    doc.appendChild(rootElement);


                }
            } else {
                System.out.println("No XML document received");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
