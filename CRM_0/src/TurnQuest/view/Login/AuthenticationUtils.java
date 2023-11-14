package TurnQuest.view.Login;


import TurnQuest.view.Connect.DBConnector;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleTypes;


public class AuthenticationUtils {
    
    public  boolean checkAuth(String page, HttpSession session){
        return true;
        /* [OBSOLETE] by dan. this method is obsolete.
    if(page.equalsIgnoreCase("index")) return true;
      if(page.equalsIgnoreCase("home")) return true;
       if(session==null)
        return false;
        String Rights = null;
        setSession(page,session);
        Rights =CheckUserRights(session);
        if(Rights.equalsIgnoreCase("N")){
          session.setAttribute("ErrorMessage", "You Don't have rights to Access the module....");
        }
        else{
          session.setAttribute("ErrorMessage", "");
        }
        return Rights.equalsIgnoreCase("Y");*/
    }
    
  public String CheckUserRights( HttpSession session) {
      String ans = "N";
      try {

          DBConnector myConn = new DBConnector();
          Connection conn = myConn.getLoginConnection();
          CallableStatement cst = null;

          String connectionPackage =
              "begin ? := tqc_interfaces_pkg.check_user_rights(?,?,?,?,?,?,?); end;";
          cst = conn.prepareCall(connectionPackage);

          cst.registerOutParameter(1, OracleTypes.VARCHAR);
          cst.setString(2, (String)session.getAttribute("Username"));
          cst.setInt(3, 0);
          cst.setString(4, (String)session.getAttribute("ProcessShtDesc"));
          cst.setString(5,
                        (String)session.getAttribute("ProcessAreaShtDesc"));
          cst.setString(6,
                        (String)session.getAttribute("ProcessSubAShtDesc"));
          cst.setObject(7,session.getAttribute("SumAssured"));
          cst.setString(8, (String)session.getAttribute("DebitCredit"));
        

          cst.execute();
          ans = cst.getString(1);
          cst.close();
          conn.close();


      } catch (Exception e) {
          e.printStackTrace();
      }
      return ans;

  }
  
  
  
    
    public String checkSession(HttpSession session, HttpServletRequest request) {
       String ans = "N";
       
       try
       {
          
           
           String userAgent = request.getHeader("User-Agent");
           UserAgentParser userAgentParser = new UserAgentParser(userAgent);
         DBConnector myConn = new DBConnector();
         Connection conn = myConn.getLoginConnection();
         CallableStatement cst = null;
         String connectionPackage = "begin ? := tqc_web_pkg.check_session(?,?,?,?); end;";
         cst = conn.prepareCall(connectionPackage);
         cst.registerOutParameter(1, 12);
         cst.setString(2, (String)session.getAttribute("Username"));
         cst.setString(3, (String)session.getAttribute("hostIp"));
         cst.setInt(4, 0);
         cst.setString(5,userAgentParser.getBrowserName() );
         cst.execute();
         ans = cst.getString(1);
         cst.close();
         conn.close();
       }
       catch (Exception e)
       {
         e.printStackTrace();
       }
       return ans;
     }
    
    
   
    
    private void setSession(String page,HttpSession session){
        
      		if (page.equalsIgnoreCase("leadSources")){
          session.setAttribute("ProcessShtDesc",   "LP");
          session.setAttribute("ProcessAreaShtDesc", "LPLS");
          session.setAttribute("ProcessSubAShtDesc", "LPLSA");
        }
        else  if(page.equalsIgnoreCase("leadStatuses")){
             session.setAttribute("ProcessShtDesc","LP");
             session.setAttribute("ProcessAreaShtDesc", "LPS");
             session.setAttribute("ProcessSubAShtDesc", "LPSA");
         }
		else  if(page.equalsIgnoreCase("users")){
             session.setAttribute("ProcessShtDesc","SA");
             session.setAttribute("ProcessAreaShtDesc", "SAU");
             session.setAttribute("ProcessSubAShtDesc", "SAUA");
         }
		else  if(page.equalsIgnoreCase("clients")){
             session.setAttribute("ProcessShtDesc","AMA");
             session.setAttribute("ProcessAreaShtDesc", "AMAC");
             session.setAttribute("ProcessSubAShtDesc", "AMCA");
         }
        else  if(page.equalsIgnoreCase("clientAiico")){
        session.setAttribute("ProcessShtDesc","AMA");
        session.setAttribute("ProcessAreaShtDesc", "AMAC");
        session.setAttribute("ProcessSubAShtDesc", "AMCA");
        }
		else  if(page.equalsIgnoreCase("holdingCompanies")){
             session.setAttribute("ProcessShtDesc","AMA");
             session.setAttribute("ProcessAreaShtDesc", "AMHC");
             session.setAttribute("ProcessSubAShtDesc", "AMHCA");
         }
		else  if(page.equalsIgnoreCase("banksDefinition")){
             session.setAttribute("ProcessShtDesc","BS");
             session.setAttribute("ProcessAreaShtDesc", "BSB");
             session.setAttribute("ProcessSubAShtDesc", "BSBA");
         }
		else  if(page.equalsIgnoreCase("agencyClasses")){
             session.setAttribute("ProcessShtDesc","AMA");
             session.setAttribute("ProcessAreaShtDesc", "AMC");
             session.setAttribute("ProcessSubAShtDesc", "AMACA");
         }
		else  if(page.equalsIgnoreCase("agentLoading")){
             session.setAttribute("ProcessShtDesc","AMA");
             session.setAttribute("ProcessAreaShtDesc", "AMC");
             session.setAttribute("ProcessSubAShtDesc", "AMACA");
         }
		else  if(page.equalsIgnoreCase("currencyRatesDefinition")){
             session.setAttribute("ProcessShtDesc","OP");
             session.setAttribute("ProcessAreaShtDesc", "OPCR");
             session.setAttribute("ProcessSubAShtDesc", "OPCRA");
         }
		else  if(page.equalsIgnoreCase("currencyDefinitions")){
             session.setAttribute("ProcessShtDesc","OP");
             session.setAttribute("ProcessAreaShtDesc", "OPC");
             session.setAttribute("ProcessSubAShtDesc", "OPCA");
         }
		else  if(page.equalsIgnoreCase("sectors")){
             session.setAttribute("ProcessShtDesc","AMA");
             session.setAttribute("ProcessAreaShtDesc", "AMS");
             session.setAttribute("ProcessSubAShtDesc", "AMSA");
         }
		 else  if(page.equalsIgnoreCase("serviceProviderTypes")){
             session.setAttribute("ProcessShtDesc","AMA");
             session.setAttribute("ProcessAreaShtDesc", "AMSPT");
             session.setAttribute("ProcessSubAShtDesc", "AMSPTA");
         }
		else  if(page.equalsIgnoreCase("countries")){
             session.setAttribute("ProcessShtDesc","OS");
             session.setAttribute("ProcessAreaShtDesc", "OSC");
             session.setAttribute("ProcessSubAShtDesc", "OSCA");
         }
		else  if(page.equalsIgnoreCase("userParameters")){
             session.setAttribute("ProcessShtDesc","OP");
             session.setAttribute("ProcessAreaShtDesc", "OPUP");
             session.setAttribute("ProcessSubAShtDesc", "OPUPA");
         }
		else  if(page.equalsIgnoreCase("sysLabel")){
             session.setAttribute("ProcessShtDesc","OP");
             session.setAttribute("ProcessAreaShtDesc", "OPUP");
             session.setAttribute("ProcessSubAShtDesc", "OPUPA");
         }
		else  if(page.equalsIgnoreCase("accounts")){
             session.setAttribute("ProcessShtDesc","AMA");
             session.setAttribute("ProcessAreaShtDesc", "AMA");
             session.setAttribute("ProcessSubAShtDesc", "AMAA");
         }
		else  if(page.equalsIgnoreCase("accounttypes")){
             session.setAttribute("ProcessShtDesc","AMA");
             session.setAttribute("ProcessAreaShtDesc", "AMT");
             session.setAttribute("ProcessSubAShtDesc", "AMTA");
         }
		 else  if(page.equalsIgnoreCase("consolidateAccounts")){
             session.setAttribute("ProcessShtDesc","AMA");
             session.setAttribute("ProcessAreaShtDesc", "AMT");
             session.setAttribute("ProcessSubAShtDesc", "AMTA");
         }
		else  if(page.equalsIgnoreCase("ClientTypes")){
             session.setAttribute("ProcessShtDesc","AMA");
             session.setAttribute("ProcessAreaShtDesc", "AMT");
             session.setAttribute("ProcessSubAShtDesc", "AMTA");
         }
		else  if(page.equalsIgnoreCase("organization")){
             session.setAttribute("ProcessShtDesc","OS");
             session.setAttribute("ProcessAreaShtDesc", "OSO");
             session.setAttribute("ProcessSubAShtDesc", "OSOA");
         }
		else  if(page.equalsIgnoreCase("requiredDocuments")){
             session.setAttribute("ProcessShtDesc","OP");
             session.setAttribute("ProcessAreaShtDesc", "OPRD");
             session.setAttribute("ProcessSubAShtDesc", "OPRDA");
         }
		 else  if(page.equalsIgnoreCase("serviceProviders")){
             session.setAttribute("ProcessShtDesc","AMA");
             session.setAttribute("ProcessAreaShtDesc", "AMSP");
             session.setAttribute("ProcessSubAShtDesc", "AMSPA");
         }
		else  if(page.equalsIgnoreCase("sysProcessRoles")){
             session.setAttribute("ProcessShtDesc","SA");
             session.setAttribute("ProcessAreaShtDesc", "SAR");
             session.setAttribute("ProcessSubAShtDesc", "SARA");
         }
		else  if(page.equalsIgnoreCase("modSubUnits")){
             session.setAttribute("ProcessShtDesc","SA");
             session.setAttribute("ProcessAreaShtDesc", "SAR");
             session.setAttribute("ProcessSubAShtDesc", "SARA");
         }
		else  if(page.equalsIgnoreCase("webProducts")){
             session.setAttribute("ProcessShtDesc","SA");
             session.setAttribute("ProcessAreaShtDesc", "SAR");
             session.setAttribute("ProcessSubAShtDesc", "SARA");
         }
		 else  if(page.equalsIgnoreCase("SubClassDescSetUp")){
             session.setAttribute("ProcessShtDesc","SA");
             session.setAttribute("ProcessAreaShtDesc", "SAR");
             session.setAttribute("ProcessSubAShtDesc", "SARA");
         }
		else  if(page.equalsIgnoreCase("paymentModes")){
             session.setAttribute("ProcessShtDesc","OP");
             session.setAttribute("ProcessAreaShtDesc", "OP");
             session.setAttribute("ProcessSubAShtDesc", "OPA");
         }
		else  if(page.equalsIgnoreCase("prospectsDefinitions")){
             session.setAttribute("ProcessShtDesc","AMA");
             session.setAttribute("ProcessAreaShtDesc", "AMP");
             session.setAttribute("ProcessSubAShtDesc", "AMPA");
         }
		 else  if(page.equalsIgnoreCase("clientTitles")){
             session.setAttribute("ProcessShtDesc","AMA");
             session.setAttribute("ProcessAreaShtDesc", "AMACT");
             session.setAttribute("ProcessSubAShtDesc", "AMACTA");
         }
		else  if(page.equalsIgnoreCase("memoSetup")){
             session.setAttribute("ProcessShtDesc","M");
             session.setAttribute("ProcessAreaShtDesc", "MMS");
             session.setAttribute("ProcessSubAShtDesc", "MMSA");
         }
		else  if(page.equalsIgnoreCase("updateDirectDebits")){
             session.setAttribute("ProcessShtDesc","BS");
             session.setAttribute("ProcessAreaShtDesc", "BSDD");
             session.setAttribute("ProcessSubAShtDesc", "BSDDA");
         }
		else  if(page.equalsIgnoreCase("bankBranches")){
             session.setAttribute("ProcessShtDesc","BS");
             session.setAttribute("ProcessAreaShtDesc", "BSDD");
             session.setAttribute("ProcessSubAShtDesc", "BSDDA");
         }
		 else  if(page.equalsIgnoreCase("DDfailRemarksSetups")){
             session.setAttribute("ProcessShtDesc","BS");
             session.setAttribute("ProcessAreaShtDesc", "BSDD");
             session.setAttribute("ProcessSubAShtDesc", "BSDDA");
         }
		else  if(page.equalsIgnoreCase("messageTemplate")){
             session.setAttribute("ProcessShtDesc","M");
             session.setAttribute("ProcessAreaShtDesc", "MMT");
             session.setAttribute("ProcessSubAShtDesc", "MMTA");
         }
		else  if(page.equalsIgnoreCase("singleMessage")){
             session.setAttribute("ProcessShtDesc","M");
             session.setAttribute("ProcessAreaShtDesc", "MSM");
             session.setAttribute("ProcessSubAShtDesc", "MSMA");
         }
		else  if(page.equalsIgnoreCase("incomingMessage")){
             session.setAttribute("ProcessShtDesc","M");
             session.setAttribute("ProcessAreaShtDesc", "MIM");
             session.setAttribute("ProcessSubAShtDesc", "MIMA");
         }
		 else  if(page.equalsIgnoreCase("incidenceSetups")){
             session.setAttribute("ProcessShtDesc","M");
             session.setAttribute("ProcessAreaShtDesc", "MIS");
             session.setAttribute("ProcessSubAShtDesc", "MISA");
         }
		else  if(page.equalsIgnoreCase("hierarchies")){
             session.setAttribute("ProcessShtDesc","OS");
             session.setAttribute("ProcessAreaShtDesc", "OSH");
             session.setAttribute("ProcessSubAShtDesc", "OSHA");
         }
		else  if(page.equalsIgnoreCase("countryHolidays")){
             session.setAttribute("ProcessShtDesc","OS");
             session.setAttribute("ProcessAreaShtDesc", "OSCH");
             session.setAttribute("ProcessSubAShtDesc", "OSCHA");
         }
		else  if(page.equalsIgnoreCase("activityTypes")){
             session.setAttribute("ProcessShtDesc","AM");
             session.setAttribute("ProcessAreaShtDesc", "AMAT");
             session.setAttribute("ProcessSubAShtDesc", "AMATA");
         }
		 else  if(page.equalsIgnoreCase("postLevels")){
             session.setAttribute("ProcessShtDesc","OS");
             session.setAttribute("ProcessAreaShtDesc", "OSP");
             session.setAttribute("ProcessSubAShtDesc", "OSPA");
         }
		else  if(page.equalsIgnoreCase("ClaimsPayMode")){
             session.setAttribute("ProcessShtDesc","OP");
             session.setAttribute("ProcessAreaShtDesc", "OPPM");
             session.setAttribute("ProcessSubAShtDesc", "OPPMA");
         }
		else  if(page.equalsIgnoreCase("mobileServiceProviders")){
             session.setAttribute("ProcessShtDesc","M");
             session.setAttribute("ProcessAreaShtDesc", "MMSP");
             session.setAttribute("ProcessSubAShtDesc", "MMSPA");
         }
		else  if(page.equalsIgnoreCase("printerSetups")){
             session.setAttribute("ProcessShtDesc","OP");
             session.setAttribute("ProcessAreaShtDesc", "OPPS");
             session.setAttribute("ProcessSubAShtDesc", "OPPSA");
         }
		 else  if(page.equalsIgnoreCase("systemApplicableAreas")){
             session.setAttribute("ProcessShtDesc","OP");
             session.setAttribute("ProcessAreaShtDesc", "OPPS");
             session.setAttribute("ProcessSubAShtDesc", "OPPSA");
         }
		else  if(page.equalsIgnoreCase("sequences")){
             session.setAttribute("ProcessShtDesc","OP");
             session.setAttribute("ProcessAreaShtDesc", "OPS");
             session.setAttribute("ProcessSubAShtDesc", "OPSA");
         }
		else  if(page.equalsIgnoreCase("alerts")){
             session.setAttribute("ProcessShtDesc","M");
             session.setAttribute("ProcessAreaShtDesc", "ALT");
             session.setAttribute("ProcessSubAShtDesc", "ALTA");
         }
		else  if(page.equalsIgnoreCase("products")){
             session.setAttribute("ProcessShtDesc","CM");
             session.setAttribute("ProcessAreaShtDesc", "CMPA");
             session.setAttribute("ProcessSubAShtDesc", "CMPAA");
         }
		 else  if(page.equalsIgnoreCase("clientAttributesDefinations")){
             session.setAttribute("ProcessShtDesc","CM");
             session.setAttribute("ProcessAreaShtDesc", "CMCA");
             session.setAttribute("ProcessSubAShtDesc", "CMCAA");
         }
		else  if(page.equalsIgnoreCase("leads")){
             session.setAttribute("ProcessShtDesc","LP");
             session.setAttribute("ProcessAreaShtDesc", "LPL");
             session.setAttribute("ProcessSubAShtDesc", "LPLA");
         }
		else  if(page.equalsIgnoreCase("Activities")){
             session.setAttribute("ProcessShtDesc","AM");
             session.setAttribute("ProcessAreaShtDesc", "AMA");
             session.setAttribute("ProcessSubAShtDesc", "AMAA");
         }
		else  if(page.equalsIgnoreCase("CampaignManagement")){
             session.setAttribute("ProcessShtDesc","CM");
             session.setAttribute("ProcessAreaShtDesc", "CMC");
             session.setAttribute("ProcessSubAShtDesc", "CMCA");
         }
		 else  if(page.equalsIgnoreCase("activityStatuses")){
             session.setAttribute("ProcessShtDesc","AM");
             session.setAttribute("ProcessAreaShtDesc", "AMAS");
             session.setAttribute("ProcessSubAShtDesc", "AMASA");
         }
		else  if(page.equalsIgnoreCase("PriorityLevels")){
             session.setAttribute("ProcessShtDesc","AM");
             session.setAttribute("ProcessAreaShtDesc", "AMPL");
             session.setAttribute("ProcessSubAShtDesc", "AMPLA");
         }
		else  if(page.equalsIgnoreCase("reportGroups")){
             session.setAttribute("ProcessShtDesc","SR");
             session.setAttribute("ProcessAreaShtDesc", "SRRG");
             session.setAttribute("ProcessSubAShtDesc", "SRRGA");
         }
		else  if(page.equalsIgnoreCase("sysReports")){
             session.setAttribute("ProcessShtDesc","SR");
             session.setAttribute("ProcessAreaShtDesc", "SRR");
             session.setAttribute("ProcessSubAShtDesc", "SRRA");
         }
		else  if(page.equalsIgnoreCase("smsSettings")){
             session.setAttribute("ProcessShtDesc","M");
             session.setAttribute("ProcessAreaShtDesc", "SST");
             session.setAttribute("ProcessSubAShtDesc", "SSTA");
         }
		else  if(page.equalsIgnoreCase("MobileSmsMessages")){
             session.setAttribute("ProcessShtDesc","M");
             session.setAttribute("ProcessAreaShtDesc", "ATM");
             session.setAttribute("ProcessSubAShtDesc", "ATMA");
         }
		else  if(page.equalsIgnoreCase("ClientLoading")){
             session.setAttribute("ProcessShtDesc","AMA");
             session.setAttribute("ProcessAreaShtDesc", "AMAC");
             session.setAttribute("ProcessSubAShtDesc", "AMCA");
         }
        else  if(page.equalsIgnoreCase("scheduler")){
        session.setAttribute("ProcessShtDesc","M");
        session.setAttribute("ProcessAreaShtDesc", "SCH");
        session.setAttribute("ProcessSubAShtDesc", "SCHA");
        }
         
                        else  if(page.equalsIgnoreCase("runningJobs")){
        session.setAttribute("ProcessShtDesc","M");
        session.setAttribute("ProcessAreaShtDesc", "RNJ");
        session.setAttribute("ProcessSubAShtDesc", "RNJA");
        }
         
                        else  if(page.equalsIgnoreCase("mailSettings")){
        session.setAttribute("ProcessShtDesc","M");
        session.setAttribute("ProcessAreaShtDesc", "MST");
        session.setAttribute("ProcessSubAShtDesc", "MSTA");
        }
         
                        else  if(page.equalsIgnoreCase("MobileSmsMessages")){
        session.setAttribute("ProcessShtDesc","M");
        session.setAttribute("ProcessAreaShtDesc", "ATM");
        session.setAttribute("ProcessSubAShtDesc", "ATMA");
        }
         
                        else  if(page.equalsIgnoreCase("smsHistory")){
        session.setAttribute("ProcessShtDesc","M");
        session.setAttribute("ProcessAreaShtDesc", "SHS");
        session.setAttribute("ProcessSubAShtDesc", "SHSA");
        }
         
                        else  if(page.equalsIgnoreCase("emailHistory")){
        session.setAttribute("ProcessShtDesc","M");
        session.setAttribute("ProcessAreaShtDesc", "EMS");
        session.setAttribute("ProcessSubAShtDesc", "EMSA");
        }
         
                        else  if(page.equalsIgnoreCase("serviceRequests")){
        session.setAttribute("ProcessShtDesc","SR");
        session.setAttribute("ProcessAreaShtDesc", "SRD");
        session.setAttribute("ProcessSubAShtDesc", "SRDA");
        }
         
                        else  if(page.equalsIgnoreCase("requestTrack")){
        session.setAttribute("ProcessShtDesc","SR");
        session.setAttribute("ProcessAreaShtDesc", "RQT");
        session.setAttribute("ProcessSubAShtDesc", "RQTA");
        }
         
                        else  if(page.equalsIgnoreCase("reqHist")){
        session.setAttribute("ProcessShtDesc","SR");
        session.setAttribute("ProcessAreaShtDesc", "RQH");
        session.setAttribute("ProcessSubAShtDesc", "RQHA");
        }
         
                        else  if(page.equalsIgnoreCase("knowledgeBase")){
        session.setAttribute("ProcessShtDesc","SR");
        session.setAttribute("ProcessAreaShtDesc", "KNB");
        session.setAttribute("ProcessSubAShtDesc", "KNBA");
        }
         else  if(page.equalsIgnoreCase("msgParameters")){
        session.setAttribute("ProcessShtDesc","SR");
        session.setAttribute("ProcessAreaShtDesc", "KWD");
        session.setAttribute("ProcessSubAShtDesc", "KWDA");
        }
         
                        else  if(page.equalsIgnoreCase("escalations")){
        session.setAttribute("ProcessShtDesc","SR");
        session.setAttribute("ProcessAreaShtDesc", "ESC");
        session.setAttribute("ProcessSubAShtDesc", "ESCA");
        }
         
                        else  if(page.equalsIgnoreCase("servReqCat")){
        session.setAttribute("ProcessShtDesc","SQ");
        session.setAttribute("ProcessAreaShtDesc", "RCT");
        session.setAttribute("ProcessSubAShtDesc", "RCTA");
        }
         
                
		
    }
    
    
    
   
   
}
