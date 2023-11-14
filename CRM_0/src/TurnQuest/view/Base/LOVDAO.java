package TurnQuest.view.Base;


import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.commons.dbutils.DbUtils;


public class LOVDAO {
    public LOVDAO() {
    }
    public static boolean loggedIn = false;
    public static BigDecimal clientCode;
    public static String clientDesc;
    public static String ClientFullname;
    public static BigDecimal CountryCode;
    public static String CountryShtDesc;
    public static BigDecimal TownCode;
    public static String TownName;
    public static String PostalZIPCode;
    public static BigDecimal SectorCode;
    public static String SectorName;
    public static BigDecimal DomicileCountry;
    public static BigDecimal branchCode;
    public static String branchName;
    private BigDecimal processCode;
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public String reInitializeVariables() {
        /*FacesContext fc = FacesContext.getCurrentInstance();
        if (fc.getExternalContext().getSessionMap().containsKey("CSVtoADFTable")) {

          fc.getExternalContext().getSessionMap().remove("CSVtoADFTable");
        }
      FacesContext facesContext = FacesContext.getCurrentInstance();

                // some date in the past
               facesContext.getExternalContext().getSessionMap().remove("oracle.adf.controller.pageFlowCache");
    // facesContext.getExternalContext().getSessionMap().remove("ORA_adf_sessionScope");
     // facesContext.getExternalContext().getSessionMap().remove("org.apache.myfaces.trinidadinternal.application.StateManagerImp.ACTIVE_PAGE_STATE");
      facesContext.getExternalContext().getSessionMap().remove("oracle.adf.controller.sessionBasedScopes");

        Map<String,Object> map = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        java.lang.System.out.println(map.size());
      int k = 0;


      Set<String> set = facesContext.getExternalContext().getSessionMap().keySet();
      Object[] obj = set.toArray();
      String values = null;
      while(k<map.size()){
       // java.lang.System.out.println(obj[k]);
        values = obj[k].toString();
          if(values.contains("VIEW_CACHE")){
            java.lang.System.out.println(obj[k]);
            facesContext.getExternalContext().getSessionMap().remove(values);
          }
        k++;
      }
      k=0;
      map = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
      set = facesContext.getExternalContext().getSessionMap().keySet();
      obj = set.toArray();
      while(k<map.size()){
        java.lang.System.out.println(obj[k]);
        values = obj[k].toString();
        k++;
      }*/
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc.getExternalContext().getSessionMap().containsKey("CSVtoADFTable")) {

            fc.getExternalContext().getSessionMap().remove("CSVtoADFTable");
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getRequestMap().size();
        int v = 0;
        Map<String, Object> map2 =
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        java.lang.System.out.println(map2.size());

        Set<String> set2 =
            context.getExternalContext().getRequestMap().keySet();
        Object[] obj2 = set2.toArray();
        String values2 = null;
        while (v < map2.size()) {
            //java.lang.System.out.println("REQUEST");
            //java.lang.System.out.println(obj2[v]);
            values2 = obj2[v].toString();
            if (!values2.contains(".") && !values2.contains("_") &&
                !values2.contains("ADF")) {
                //java.lang.System.out.println(obj2[v]);
                context.getExternalContext().getRequestMap().remove(values2);
            }
            v++;
        }


        FacesContext facesContext = FacesContext.getCurrentInstance();

        // some date in the past
        facesContext.getExternalContext().getSessionMap().remove("oracle.adf.controller.pageFlowCache");
        facesContext.getExternalContext().getSessionMap().remove("ORA_adf_sessionScope");
        facesContext.getExternalContext().getSessionMap().remove("org.apache.myfaces.trinidadinternal.application.StateManagerImp.ACTIVE_PAGE_STATE");
        facesContext.getExternalContext().getSessionMap().remove("oracle.adf.controller.sessionBasedScopes");

        Map<String, Object> map =
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        java.lang.System.out.println(map.size());
        int k = 0;


        Set<String> set =
            facesContext.getExternalContext().getSessionMap().keySet();
        Object[] obj = set.toArray();
        String values = null;
        while (k < map.size() -1) {
            // java.lang.System.out.println(obj[k]);
            values = obj[k].toString();
            if (values.contains("VIEW_CACHE")) {
                //java.lang.System.out.println(obj[k]);
                facesContext.getExternalContext().getSessionMap().remove(values);
            }
            k++;
        }
        k = 0;
        map =
FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        set = facesContext.getExternalContext().getSessionMap().keySet();
        obj = set.toArray();
        System.err.println("OBJ SIZE: " + obj.length);
        System.err.println("MAP SIZE:" +map.size());
        while (k < map.size()) {
            //java.lang.System.out.println(obj[k]);
            
            try {
                if(obj[k].toString() !=null){
                        values = obj[k].toString();
                    }
            } catch(Exception e){}
           
            k++;
        }
        session.removeAttribute("fromHome");
        session.removeAttribute("fromTracking");
        session.removeAttribute("multiAccount");
        session.removeAttribute("mobilePrefix");
        session.removeAttribute("deployment");
        session.removeAttribute("jpdlName");
        session.removeAttribute("activity");
        session.removeAttribute("manager");
        session.removeAttribute("refNo");
        session.removeAttribute("bankCode");
        session.removeAttribute("bbrCode");
        session.removeAttribute("query");
        session.removeAttribute("actyCode");
        session.removeAttribute("bnkShtDesc");
        session.removeAttribute("ddFileDesc");
        session.removeAttribute("processCode");
        session.removeAttribute("sysUserCode");
        session.removeAttribute("processRoleCode");
        session.removeAttribute("sysCode");
        session.removeAttribute("regCode");
        session.removeAttribute("REGCode");
        session.removeAttribute("tsrCode");
        session.removeAttribute("accountName");
        session.removeAttribute("hhId");
        session.removeAttribute("odlType");
        //session.removeAttribute("orgCode");
        session.removeAttribute("ORGCode");
        session.removeAttribute("BRNCode");
        //session.removeAttribute("branchCode");
        session.removeAttribute("roleCode");
        session.removeAttribute("accountTypeCode");
        session.removeAttribute("serviceProviderTypeCode");
        session.removeAttribute("serviceProviderCode");
        session.removeAttribute("ClientCode");
        session.removeAttribute("msgTmpCode");
        session.removeAttribute("clientCode");
        session.removeAttribute("bankCode");
        session.removeAttribute("currencyCode");
        session.removeAttribute("countryCode");
        session.removeAttribute("couZipCode");
        session.removeAttribute("townCode");
        session.removeAttribute("COUCode");
        session.removeAttribute("couCode");
        session.setAttribute("ScreenPosition", 0);
        session.removeAttribute("sprsaCode");
        session.removeAttribute("genYear");
        session.removeAttribute("searchDate");
        session.removeAttribute("ddCode");
        session.removeAttribute("ddReceipted");
        session.removeAttribute("ddBookDate");
        session.removeAttribute("dddCode");
        session.removeAttribute("ddStatus");
        session.removeAttribute("status");
        session.removeAttribute("ddhCode");
        session.removeAttribute("SEARCHED");
        session.removeAttribute("vSPtaCode");
        session.removeAttribute("vSptCode");
        session.removeAttribute("addAction");
        session.removeAttribute("sysCode");
        session.removeAttribute("dltCode");
        session.removeAttribute("agnCode");
        session.removeAttribute("osdId");
        session.removeAttribute("osdCode");
        session.removeAttribute("osdDltCode");
        session.removeAttribute("dltDesc");
        session.removeAttribute("osdOdlCode");
        session.removeAttribute("odlDesc");
        session.removeAttribute("sysplCode");
        session.removeAttribute("spostCode");
        session.removeAttribute("odlRanking");
        session.removeAttribute("osdName1");
        session.removeAttribute("ClientAccountCode");
        session.removeAttribute("otherNames");
        session.removeAttribute("surNames");
        session.removeAttribute("count");
        session.removeAttribute("saveStatus");
        session.removeAttribute("activity_code");
        session.removeAttribute("participId");
        session.removeAttribute("myTrigger");
        session.removeAttribute("mptCode");
        session.removeAttribute("grpUserCode");
        session.removeAttribute("MEMO_CLASS_REQUIRED");
        session.removeAttribute("memType");
        session.removeAttribute("memoCode");
        session.removeAttribute("memdetCode");
        session.removeAttribute("grpCode");
        session.removeAttribute("grpdCode");
        session.removeAttribute("searchCriteria");
        session.removeAttribute("searchCriteriaValue");
        session.removeAttribute("searchCriteria2");
        session.removeAttribute("sectorName");
        session.removeAttribute("sectorCode");
        session.removeAttribute("searchClntStatus");
        session.removeAttribute("searchClntType");
        session.removeAttribute("_search");
        session.removeAttribute("client_count");
        session.removeAttribute("idUserCreatedDate");
        session.removeAttribute("usrGrpDate");
        session.removeAttribute("MY_ORG_CODE");
        session.removeAttribute("alertType");
        session.removeAttribute("date");
        session.removeAttribute("adminRegionCode");
        session.removeAttribute("ACTIVITY_CODE");
        session.removeAttribute("braCode");
        session.removeAttribute("ACTIVITY_CODE");
        session.removeAttribute("CAMPAIGN_CODE");
        session.removeAttribute("cmsgCode");
        session.removeAttribute("messageType");
        session.removeAttribute("NOTE_CODE");
        session.removeAttribute("Lead_code");
        session.removeAttribute("SEARCHCRITERIA");
        session.removeAttribute("PRODUCTCODE");
        session.removeAttribute("PRODUCT_ATTRIBUTE_CODE");
        session.removeAttribute("INPUTTYPE");
        session.removeAttribute("REPORT_GROUP_CODE");
        session.removeAttribute("SYSTEM_APP_AREA_CODE");
        session.removeAttribute("rptCode");
        session.removeAttribute("agencyCode");
        session.removeAttribute("providerName");
        session.removeAttribute("clntCode");
        session.removeAttribute("type");
        session.removeAttribute("agentFromAccountCode");
        session.removeAttribute("agentToAccountCode");
        session.setAttribute("msg", "all");
        session.removeAttribute("sclCode");
        session.removeAttribute("sprCode");

        session.removeAttribute("entityPin");
        session.removeAttribute("entityName");
        session.removeAttribute("entityShortDesc");
        session.removeAttribute("entityCode");
        session.setAttribute("applicableArea","S");
        session.removeAttribute("productCode");
        session.removeAttribute("prodUmbrella");
        

        return null;
    }

    public String resetPageButtons() {
        session.removeAttribute("MM");
        session.removeAttribute("KK");
        return null;
    }

    public List<LOV> findInitial() {

        List<LOV> products = new ArrayList<LOV>();

        LOV product = new LOV();
        products.add(product);

        // try {
        //     FacesContext.getCurrentInstance().getExternalContext().redirect("http://10.176.18.66:7000:7000/alfresco/download/attach/workspace/SpacesStore/JOSE/ple_5559-42.pdf?guest=true");
        //   } catch (IOException e) {
        //  }
        return products;

    }

    public List<LOV> findAllClients() {

        String query = "begin ? := tqc_clients_pkg.get_clients(); end;";
        OracleCallableStatement cst = null;
        List<LOV> users = new ArrayList<LOV>();
        try {
            DBConnector datahandler = new DBConnector();
            OracleConnection conn;
            conn = datahandler.getDatabaseConnection();

            cst = (OracleCallableStatement)conn.prepareCall(query);

            cst.registerOutParameter(1,
                                     OracleTypes.CURSOR); //authorization code
            cst.execute();

            OracleResultSet rs = (OracleResultSet)cst.getObject(1);

            users = new ArrayList<LOV>();
            int k;
            k = 0;
            while (rs.next()) {
                LOV client1 = new LOV();
                client1.setClientCode(rs.getBigDecimal(1));
                client1.setPINNumber(rs.getString(2));
                client1.setPostalAddress(rs.getString(3));
                client1.setTelphoneOne(rs.getString(4));
                client1.setOthernames(rs.getString(5));
                client1.setFullname(rs.getString(6));
                client1.setIdRegNumber(rs.getString(7));
                client1.setShortDescription(rs.getString(8));
                client1.setZIPCode(rs.getString(9));

                users.add(client1);
            }

            rs.close();
            conn.close();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }


        return users;


    }

    public List<LOV> findReportQuery() {
        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement cst = null;
        List<LOV> queryData = new ArrayList<LOV>();
        String rptCode = (String)session.getAttribute("rptCode");
        if (session.getAttribute("filter") == null) {
            return queryData;
        }
        try {
            String query = GlobalCC.reportParameters(rptCode, "LOV", null);
            cst = (OracleCallableStatement)conn.prepareCall(query);
            OracleResultSet rs = null;
            rs = (OracleResultSet)cst.executeQuery();
            System.out.println(query);
            while (rs.next()) {
                LOV reg = new LOV();
                System.out.println(rs.getString(1));
                reg.setValueCode(rs.getString(1));
                reg.setValueDesc(rs.getString(2));
                queryData.add(reg);
            }
            rs.close();
            conn.commit();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return queryData;

    }

    public void setClientCode(BigDecimal clientCode) {
        LOVDAO.clientCode = clientCode;
    }

    public BigDecimal getClientCode() {
        return clientCode;
    }

    public void setClientDesc(String clientDesc) {
        LOVDAO.clientDesc = clientDesc;
    }

    public String getClientDesc() {
        return clientDesc;
    }

    public void setClientFullname(String ClientFullname) {
        LOVDAO.ClientFullname = ClientFullname;
    }

    public String getClientFullname() {
        return ClientFullname;
    }

    public void setCountryCode(BigDecimal CountryCode) {
        LOVDAO.CountryCode = CountryCode;
    }

    public BigDecimal getCountryCode() {
        return CountryCode;
    }

    public void setCountryShtDesc(String CountryShtDesc) {
        LOVDAO.CountryShtDesc = CountryShtDesc;
    }

    public String getCountryShtDesc() {
        return CountryShtDesc;
    }

    public void setTownCode(BigDecimal TownCode) {
        LOVDAO.TownCode = TownCode;
    }

    public BigDecimal getTownCode() {
        return TownCode;
    }

    public void setPostalZIPCode(String PostalZIPCode) {
        LOVDAO.PostalZIPCode = PostalZIPCode;
    }

    public String getPostalZIPCode() {
        return PostalZIPCode;
    }

    public void setSectorCode(BigDecimal SectorCode) {
        LOVDAO.SectorCode = SectorCode;
    }

    public BigDecimal getSectorCode() {
        return SectorCode;
    }

    public void setSectorName(String SectorName) {
        LOVDAO.SectorName = SectorName;
    }

    public String getSectorName() {
        return SectorName;
    }

    public void setDomicileCountry(BigDecimal DomicileCountry) {
        LOVDAO.DomicileCountry = DomicileCountry;
    }

    public BigDecimal getDomicileCountry() {
        return DomicileCountry;
    }

    public static void setBranchCode(BigDecimal branchCode) {
        LOVDAO.branchCode = branchCode;
    }

    public static BigDecimal getBranchCode() {
        return branchCode;
    }

    public static void setBranchName(String branchName) {
        LOVDAO.branchName = branchName;
    }

    public static String getBranchName() {
        return branchName;
    }

    public static void setTownName(String TownName) {
        LOVDAO.TownName = TownName;
    }

    public static String getTownName() {
        return TownName;
    }

    public void setProcessCode(BigDecimal processCode) {
        session.setAttribute("processCode", processCode);
        this.processCode = processCode;
    }

    public BigDecimal getProcessCode() {
        return (BigDecimal)session.getAttribute("processCode");
    }

    private String getReportQuery() {
        DBConnector datahandler = new DBConnector();
        Connection conn;
        conn = datahandler.getDatabaseConnection();
        CallableStatement statement = null;
        String queryReturn = "";
        String query =
            "begin ? :=  TQC_WEB_CURSOR.get_report_param_qry(?,?); end;";
        try {

            statement = conn.prepareCall(query);
            statement.registerOutParameter(1, OracleTypes.VARCHAR);
            statement.setString(2, (String)session.getAttribute("rptCode"));
            statement.setString(3,
                                (String)session.getAttribute("rpt_param_name"));
            statement.execute();
            queryReturn = statement.getString(1);
            statement.close();
            conn.close();
        } catch (SQLException e) {
            String message = e.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          message,
                                                                          message));
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, statement, null);
        }
        return queryReturn;
    }

    private int countOccurrences(String haystack, char needle, int i) {
        return ((i = haystack.indexOf(needle, i)) == -1) ? 0 :
               1 + countOccurrences(haystack, needle, i + 1);
    }

    public List<LOV> findQueryParameters(String search) {
        List<LOV> statements = null;
        DBConnector datahandler = new DBConnector();
        Connection conn = null;
        conn = datahandler.getDatabaseConnection();
        CallableStatement statement = null;
        String query = getReportQuery();
        ResultSet rst = null;
        String sKey = GlobalCC.checkNullValues(search);
        sKey = sKey != null ? sKey : "";
        System.out.println("query: " + query);

        statements = new ArrayList<LOV>();
        if (query == null || query.length() == 0)
            return statements;
        int qOccurences = countOccurrences(query, '?', 1);
        try {
            statement = conn.prepareCall(query);
            if (qOccurences == 1) {
                statement.setString(1, "%" + sKey + "%");
            } else if (qOccurences == 2) {
                statement.setString(1, "%" + sKey + "%");
                statement.setString(2,
                                    (String)session.getAttribute("rptParentValue"));
            }
            rst = statement.executeQuery();

            while (rst.next()) {
                LOV lov = new LOV();
                lov.setCode(rst.getString(1));
                lov.setValue(rst.getString(2));
                statements.add(lov);
                if (statements.size() == 40) {
                    session.setAttribute("RecordExist",
                                         "More Record Exists....Please Refine Your Search");
                    return statements;
                }
            }
            session.setAttribute("RecordExist", null);
            rst.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, statement, rst);
        }
        return statements;
    }

    public String reloadVals() {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc.getExternalContext().getSessionMap().containsKey("CSVtoADFTable")) {

            fc.getExternalContext().getSessionMap().remove("CSVtoADFTable");
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getRequestMap().size();
        int v = 0;
        Map<String, Object> map2 =
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        Set<String> set2 =
            context.getExternalContext().getRequestMap().keySet();
        Object[] obj2 = set2.toArray();
        String values2 = null;
        while (v < map2.size()) {
            values2 = obj2[v].toString();
            if (!values2.contains(".") && !values2.contains("_") &&
                !values2.contains("ADF")) {
                context.getExternalContext().getRequestMap().remove(values2);
            }
            v++;
        }


        FacesContext facesContext = FacesContext.getCurrentInstance();

        // some date in the past
        facesContext.getExternalContext().getSessionMap().remove("oracle.adf.controller.pageFlowCache");
        facesContext.getExternalContext().getSessionMap().remove("ORA_adf_sessionScope");
        facesContext.getExternalContext().getSessionMap().remove("org.apache.myfaces.trinidadinternal.application.StateManagerImp.ACTIVE_PAGE_STATE");
        facesContext.getExternalContext().getSessionMap().remove("oracle.adf.controller.sessionBasedScopes");

        Map<String, Object> map =
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        int k = 0;


        Set<String> set =
            facesContext.getExternalContext().getSessionMap().keySet();
        Object[] obj = set.toArray();
        String values = null;
        while (k < map.size()) {
            values = obj[k].toString();
            if (values.contains("VIEW_CACHE")) {
                facesContext.getExternalContext().getSessionMap().remove(values);
            }
            k++;
        }
        return null;
    }

    public String reinitializeVariables() {
        session.removeAttribute("clbCode");
        session.removeAttribute("authorizeStatus");
        session.removeAttribute("agcpTotalPolicies");
        session.removeAttribute("agcpTotalPremium");
        session.removeAttribute("IncentiveTranstype");
        session.removeAttribute("agcpCode");
        session.removeAttribute("QuoProdType");
        session.removeAttribute("lauchData");
        session.removeAttribute("prpInstCodeFrom");
        session.removeAttribute("prpInstCodeFrom");
        session.removeAttribute("ChangeType");
        session.removeAttribute("payPointChangeType");
        session.removeAttribute("payMethodChangeType");
        session.removeAttribute("newPayMthd");
        session.removeAttribute("refAgentCode");
        session.removeAttribute("selectedChekVal");
        session.removeAttribute("payMethodDSelected");
        session.removeAttribute("enmassCPM");
        session.removeAttribute("selectPrem");
        session.removeAttribute("selectAssigned");
        session.removeAttribute("assignusrusername");
        session.removeAttribute("assignusername");
        session.setAttribute("commSubAccType", "N");
        session.setAttribute("CommRatesSetup", "N");
        session.setAttribute("OverrideAppliedOn", "N");
        session.removeAttribute("rejectType");
        session.removeAttribute("mdbcode");
        session.removeAttribute("v_spr_code");
        session.removeAttribute("premiumTerm");
        session.removeAttribute("contrCode");
        session.removeAttribute("riskPremDate");
        session.removeAttribute("riskPremStatus");
        session.removeAttribute("prodInvestAllPrem");
        session.removeAttribute("gender");
        session.removeAttribute("prosalEdit");
        session.removeAttribute("endrlapseTypeDesc");
        session.removeAttribute("endrlapseType");
        session.removeAttribute("clientIdNo");
        session.removeAttribute("poesarate");
        session.removeAttribute("poerate");
        session.setAttribute("reinsuranceDone", "N");
        session.setAttribute("medicalsDone", "N");
        session.removeAttribute("rpcCode");
        session.removeAttribute("rpsCode");
        session.removeAttribute("clientTransSectd");
        session.removeAttribute("clientTransType");
        session.removeAttribute("pensType");
        session.setAttribute("endrTypeonEdit", "N");
        session.removeAttribute("PaybutSelected");
        session.setAttribute("butPaySelected", "N");
        session.setAttribute("toPrint", "N");
        session.setAttribute("prodCancellation", "N");
        session.setAttribute("reDirect", "N");
        session.removeAttribute("budgetStatus");
        session.removeAttribute("policyProposalNo");
        session.removeAttribute("premTrackCode");
        session.setAttribute("changeANB", "N");
        session.setAttribute("grpLifeCover", "N");
        session.removeAttribute("polProdType");
        session.removeAttribute("keepSaConstant");
        session.removeAttribute("cpvVouvherNo");
        session.removeAttribute("cpvCode");
        session.removeAttribute("terminationDate");
        session.removeAttribute("reinstateAgent");
        session.removeAttribute("agentLoadCode");
        session.removeAttribute("Reader");
        session.removeAttribute("ValLegth");
        session.removeAttribute("marketerCode");
        session.setAttribute("EndrJointAgn", "N");
        session.setAttribute("jointEndrAgn", "N");
        session.removeAttribute("jointEndrCode");
        session.removeAttribute("agentTitle");
        session.removeAttribute("agentCode3");
        session.setAttribute("editJointEndr", "N");
        session.removeAttribute("editJoint");
        session.setAttribute("jointSelct", "N");
        session.removeAttribute("agentCode2");
        session.setAttribute("jointAgnSelect", "N");
        session.setAttribute("jointAgentSelected", "N");
        session.setAttribute("prodCancelType", "N");
        session.setAttribute("premRefundType", "N");
        session.setAttribute("addRemoveLifRider", "N");
        session.setAttribute("computePremActv", "N");
        session.removeAttribute("claimChargeCode");
        session.removeAttribute("claimChargeApp");
        session.setAttribute("viewUnderProp", "N");
        session.setAttribute("retrnAftaView", "N");
        session.removeAttribute("rowchecked");
        session.removeAttribute("rowDataSelected");
        session.removeAttribute("postPonment");
        session.removeAttribute("uwReject");
        session.removeAttribute("vtransno");
        session.removeAttribute("vtopnumber");
        session.removeAttribute("matutiyEdit");
        session.removeAttribute("agentCodeVal");
        session.removeAttribute("agentBruCode");
        session.removeAttribute("branchUnitCode");
        session.removeAttribute("policyFee");
        session.removeAttribute("surrenderAllocToPol");
        session.removeAttribute("matAuthorized");
        session.removeAttribute("addEdit");
        session.removeAttribute("code");
        session.removeAttribute("seqCode");
        session.removeAttribute("paramCode");
        session.removeAttribute("authClaim");
        session.removeAttribute("editMode");
        session.removeAttribute("executionID");
        session.removeAttribute("lapseDate");
        session.removeAttribute("surrDate");
        session.removeAttribute("totBonusAmt");
        session.removeAttribute("loanableAmt");
        session.removeAttribute("surrValue");
        session.removeAttribute("newLoan");
        session.removeAttribute("intrCompDate");
        session.removeAttribute("transactionDate");
        session.setAttribute("receiptRefund", false);
        session.removeAttribute("dateReported");
        session.removeAttribute("deathDate");
        session.removeAttribute("disabilityDate");
        session.removeAttribute("withdrawalDate");
        session.setAttribute("death", false);
        session.removeAttribute("dateThree");
        session.setAttribute("disability", false);
        session.setAttribute("withdrawal", false);
        session.setAttribute("authorised", false);
        session.setAttribute("set", false);
        session.setAttribute("notSet", true);
        session.removeAttribute("medIDNo");
        session.removeAttribute("cmlCode");
        session.removeAttribute("sprCode");
        session.removeAttribute("searchCriteria");
        session.removeAttribute("ddcCode");
        session.removeAttribute("cscCode");
        session.removeAttribute("causShtDesc");
        session.removeAttribute("causCode");
        session.removeAttribute("prpTown");
        session.removeAttribute("prpPostalAddress");
        session.removeAttribute("prpName");
        session.removeAttribute("prpIdRegNo");
        session.removeAttribute("causation");
        session.removeAttribute("voucherNumber");
        session.removeAttribute("cNotCode");
        session.removeAttribute("claimNumber");
        session.removeAttribute("recptOPRCode");
        session.removeAttribute("lnNo");
        session.setAttribute("premiumCard", false);
        session.setAttribute("endorse", false);
        session.removeAttribute("lifeRider");
        session.removeAttribute("sumAssured");
        session.removeAttribute("premium");
        session.removeAttribute("month");
        session.removeAttribute("year");
        session.removeAttribute("popNsCode");
        session.removeAttribute("commLifeClassID");
        session.removeAttribute("cadCode");
        session.removeAttribute("process");
        session.removeAttribute("formerProcess");
        session.removeAttribute("sysModule");
        session.removeAttribute("transType");
        session.removeAttribute("policyProposal");
        session.removeAttribute("docType");
        session.removeAttribute("transCode");
        session.removeAttribute("transNo");
        session.removeAttribute("transactionNumber");
        session.removeAttribute("pprCode");
        session.removeAttribute("cancelType");
        session.setAttribute("resetValue", false);
        session.removeAttribute("originatingPage");
        session.removeAttribute("msgTempCode");
        session.removeAttribute("msgTempDesc");
        session.removeAttribute("msgTempShtDesc");
        session.removeAttribute("msgType");
        session.removeAttribute("proposaleffectiveDate");
        session.removeAttribute("effectiveDate");
        session.removeAttribute("policyNumber");
        session.removeAttribute("SmokerCode");
        session.removeAttribute("SmokerDesc");
        session.removeAttribute("HIVTakenCode");
        session.removeAttribute("HIVTakenDesc");
        session.removeAttribute("payModeCode");
        session.removeAttribute("payModeDesc");
        session.removeAttribute("payFreqCode");
        session.removeAttribute("payFreqDesc");
        session.removeAttribute("bankBranch");
        session.removeAttribute("bankBranchCode");
        session.removeAttribute("prpInstName");
        session.removeAttribute("prpInstCode");
        session.removeAttribute("policyProposalCode");
        session.removeAttribute("endorsementCode");
        session.removeAttribute("yesNoValues");
        session.removeAttribute("endorseType");
        session.removeAttribute("confirmMessage");
        session.removeAttribute("sysBranchCode");
        session.removeAttribute("sysBranchDesc");
        session.removeAttribute("agentCode");
        session.removeAttribute("agentName");
        session.removeAttribute("agentShtDesc");
        session.removeAttribute("chkOffAgent");
        session.removeAttribute("chOffDesc");
        session.removeAttribute("ckOffDeptShtDesc");
        session.removeAttribute("surrCode");
        session.removeAttribute("surrDesc");
        session.setAttribute("viewTrans", false);
        session.setAttribute("enquiryMode", false);
        session.setAttribute("policy", false);
        session.setAttribute("proposal", false);
        session.removeAttribute("productType");
        session.removeAttribute("popCode");
        session.removeAttribute("popDesc");
        session.removeAttribute("popShtDesc");
        session.removeAttribute("pMasDesc");
        session.removeAttribute("pMasCode");
        session.removeAttribute("pMasShtDesc");
        session.removeAttribute("productDesc");
        session.removeAttribute("productShtDesc");
        session.removeAttribute("productCode");
        session.setAttribute("jointQuote", false);
        session.removeAttribute("quoteTrans");
        session.removeAttribute("quoteCode");
        session.removeAttribute("quoteNumber");
        session.removeAttribute("quoteProdCode");
        session.setAttribute("quoteWizard", 1);
        session.removeAttribute("recptOPRBalance");
        session.setAttribute("enProposal", false);
        session.setAttribute("newProposal", false);
        session.setAttribute("editProposal", false);
        session.setAttribute("conProposal", false);
        session.setAttribute("canProposal", false);
        session.setAttribute("reinProposal", false);
        session.removeAttribute("proposalNumberAndClient");
        session.removeAttribute("proposalNumber");
        session.removeAttribute("proposalClient");
        session.setAttribute("medPayPol", false);
        session.setAttribute("medFacil", false);
        session.setAttribute("newClaimtrans", false);
        session.setAttribute("editClaimTrans", false);
        session.setAttribute("viewClaimTrans", false);
        session.setAttribute("premiumValue", false);
        session.setAttribute("sumAssuredValue", false);
        session.setAttribute("coverPremiumValue", false);
        session.setAttribute("coverSumAssuredValue", false);
        session.setAttribute("viewSource", false);
        //session.setAttribute("passToMarketing", "Yes");
        session.removeAttribute("countryCode");
        session.removeAttribute("countryShtDesc");
        session.removeAttribute("domicileCountry");
        session.removeAttribute("jointOccupationCode");
        session.removeAttribute("occupationCode");
        session.removeAttribute("jointOccupationDesc");
        session.removeAttribute("occupationDesc");
        session.removeAttribute("jointOccupationlifeClass");
        session.removeAttribute("occupationlifeClass");
        session.setAttribute("clientexists", false);
        session.removeAttribute("prpCode");
        session.removeAttribute("clientName");
        session.removeAttribute("joinAssuredClientName");
        session.removeAttribute("jointAssuredprpCode");
        session.removeAttribute("assuredClientName");
        session.removeAttribute("assuredPrpCode");
        session.removeAttribute("lifeAssuredPrpCode");
        session.removeAttribute("ClientCode");
        session.removeAttribute("clientCode");
        session.removeAttribute("sectorCode");
        session.removeAttribute("sectorName");
        session.removeAttribute("branchName");
        session.removeAttribute("branchCode");
        session.removeAttribute("townName");
        session.removeAttribute("townCode");
        session.removeAttribute("postalZIPCode");
        session.removeAttribute("dependantTypeCode");
        session.removeAttribute("dependantTypeDesc");
        session.removeAttribute("telDependantCode");
        session.removeAttribute("telCoverTypeCode");
        session.removeAttribute("quoteCovtCode");
        session.setAttribute("loadCover", true);
        session.removeAttribute("itemCvtCode");
        session.removeAttribute("cvtShtDesc");
        session.removeAttribute("cvtDesc");
        session.removeAttribute("cvtCode");
        session.removeAttribute("pctCode");
        session.removeAttribute("dlCode");
        session.removeAttribute("mtgCode");
        session.removeAttribute("revisionNumber");
        session.setAttribute("ScreenPosition", 0);
        session.removeAttribute("ProcessShtDesc");
        session.removeAttribute("ProcessAreaShtDesc");
        session.removeAttribute("ProcessSubAShtDesc");
        session.removeAttribute("debitCredit");
        session.removeAttribute("formerProcessShtDesc");
        session.removeAttribute("formerProcessAreaShtDesc");
        session.removeAttribute("formerProcessSubAShtDesc");
        session.removeAttribute("formerdebitCredit");
        session.removeAttribute("formerauthAmount");
        session.removeAttribute("authAmount");
        session.removeAttribute("loanable");
        session.removeAttribute("dtyCode");
        session.removeAttribute("mortgage");
        session.removeAttribute("actCode");
        session.removeAttribute("actId");
        session.removeAttribute("annfdCode");
        session.removeAttribute("dialogName");
        session.removeAttribute("endorseType");
        session.removeAttribute("newEndorseType");
        session.removeAttribute("transfer_type");
        session.removeAttribute("transferType");
        session.removeAttribute("transfer_desc");
        session.removeAttribute("transferDesc");
        session.removeAttribute("from_pol_code");
        session.removeAttribute("to_pol_code");
        session.removeAttribute("fromPolCode");
        session.removeAttribute("toPolCode");
        session.removeAttribute("propTypeFrom");
        session.removeAttribute("propTypeTo");
        session.removeAttribute("drcr");
        session.removeAttribute("opr_code");
        session.removeAttribute("oprCode");
        session.removeAttribute("opprCode");
        session.removeAttribute("proposalPolicyFrom");
        session.removeAttribute("proposal_no_from");
        session.removeAttribute("prop_type_from");
        session.removeAttribute("receipt_no");
        session.removeAttribute("receiptNo");
        session.removeAttribute("receipt_date");
        session.removeAttribute("receiptDate");
        session.removeAttribute("groupPolicy");
        session.removeAttribute("childAge");
        session.removeAttribute("prodGenProp");
        session.removeAttribute("poridsCode");
        session.removeAttribute("pcvtCode");
        session.removeAttribute("clntCode");
        session.setAttribute("useTerm", "");
        session.removeAttribute("status");
        session.removeAttribute("totPrem");
        session.setAttribute("computed", "N");
        session.setAttribute("genTerm", false);
        session.setAttribute("lcf", true);
        session.removeAttribute("ttCode");
        session.removeAttribute("rateCode");
        session.removeAttribute("anb");
        session.setAttribute("manLcf", false);
        session.removeAttribute("term");
        session.setAttribute("nonMedical", "Y");
        session.removeAttribute("batchedBy");
        session.removeAttribute("btrCode");
        session.removeAttribute("retAge");
        session.removeAttribute("grntPrd");
        session.removeAttribute("polCode");
        session.removeAttribute("polPrpCode");
        session.removeAttribute("prodCode");
        session.removeAttribute("policyNo");
        session.removeAttribute("nextRIDate");
        session.removeAttribute("grpLifeRider");
        session.removeAttribute("escAllowed");
        session.removeAttribute("unitLinked");
        session.removeAttribute("calcTrmRtAge");
        session.removeAttribute("quizone");
        session.removeAttribute("quiztwo");
        session.removeAttribute("reinstOsPremComputed");
        session.removeAttribute("investAllPremium");
        session.removeAttribute("cancelType");
        session.removeAttribute("lapseAllPolicies");
        session.removeAttribute("lmcCode");
        session.removeAttribute("freqCode");
        session.removeAttribute("loadType");
        session.removeAttribute("facType");
        session.removeAttribute("facreType");
        session.removeAttribute("termFrom");
        session.removeAttribute("termTo");
        session.removeAttribute("sourcePage");
        session.removeAttribute("budgetType");
        session.removeAttribute("btsCode");
        session.removeAttribute("delete");
        session.removeAttribute("reinvestOption");
        session.removeAttribute("reinvesTerm");
        session.removeAttribute("reinvestAmount");
        session.removeAttribute("boSpreadAmount");
        session.removeAttribute("polStatus");
        session.removeAttribute("withOsLoan");
        session.removeAttribute("paymentStatus");
        session.removeAttribute("ppmCode");
        session.removeAttribute("waiveInstallments");
        session.removeAttribute("claimProcessed");
        session.removeAttribute("ACC");
        session.removeAttribute("stateCode");
        session.removeAttribute("coinsure");
        session.removeAttribute("createFromCrm");
        session.removeAttribute("showMessage");
        session.removeAttribute("prodRateGender");
        session.removeAttribute("prodInvestAllPrem");
        session.removeAttribute("gender");
        session.removeAttribute("lbrType");
        session.removeAttribute("editCover");
        session.removeAttribute("surrenderType");
        session.removeAttribute("dateForm");
        session.removeAttribute("dateTo");
        session.removeAttribute("lfmCode");
        session.removeAttribute("limit");
        session.removeAttribute("lfdCode");
        session.removeAttribute("lfiCode");
        session.removeAttribute("lfiShortDesc");
        session.removeAttribute("polANB");
        session.removeAttribute("date");
        session.removeAttribute("withinSystem");
        session.removeAttribute("maturityOption");
        session.removeAttribute("importSuccess");
        session.removeAttribute("lsfCode");
        session.removeAttribute("processArea");
        session.removeAttribute("rateType");
        session.removeAttribute("surrValFormular");
        session.removeAttribute("coverCode");
        session.removeAttribute("transProcess");
        session.removeAttribute("agnCode");
        session.removeAttribute("bstCode");
        session.removeAttribute("monthTo");
        session.removeAttribute("brnCode");
        session.removeAttribute("proposalExists");
        session.removeAttribute("Name");
        session.removeAttribute("clientCode");
        session.removeAttribute("clientID");
        session.removeAttribute("propolNo");
        session.removeAttribute("clientDob");
        session.removeAttribute("clientPhone");
        session.removeAttribute("clientId");
        session.removeAttribute("reasonType");
        session.removeAttribute("changeANB");
        session.removeAttribute("clientType");
        session.removeAttribute("TACode");
        session.removeAttribute("ASCode");
        session.removeAttribute("TAShortDesc");
        session.removeAttribute("UwYr");
        session.removeAttribute("TPRDCode");
        session.removeAttribute("TRTGCode");
        session.removeAttribute("TRTCode");
        session.removeAttribute("RDCode");
        session.removeAttribute("taxType");
        session.removeAttribute("branchOption");
        session.removeAttribute("withWaiver");
        session.removeAttribute("pcfsCode");
        session.removeAttribute("authorizer");
        session.removeAttribute("laltCode");
        session.removeAttribute("CTUW");
        session.removeAttribute("surName");
        session.removeAttribute("otherNames");
        session.removeAttribute("shortDesc");
        session.removeAttribute("dateCreated");
        session.removeAttribute("laltType");
        session.removeAttribute("quoDate");
        session.removeAttribute("product");
        session.removeAttribute("period");
        session.removeAttribute("source");
        session.removeAttribute("qopnsCode");
        session.removeAttribute("qproCode");
        session.removeAttribute("qproProdCode");
        session.removeAttribute("qcvtCvtCode");
        session.removeAttribute("qcvtCode");
        session.removeAttribute("topnsCode");
        session.removeAttribute("topnsAnnCode");
        session.removeAttribute("populated");
        session.removeAttribute("accountType");
        session.setAttribute("filter", "Y");
        session.removeAttribute("paymentType");
        session.removeAttribute("lifeAssuredClientName");
        session.removeAttribute("prodMaturityOption");
        session.removeAttribute("crmInquiry");
        session.removeAttribute("contraType");
        session.removeAttribute("criteria");
        session.removeAttribute("launch");
        session.removeAttribute("causType");
        session.removeAttribute("claimDate");
        session.removeAttribute("dispalayInvSA");
        session.removeAttribute("chequeNo");
        session.removeAttribute("selectedValue");
        session.removeAttribute("componentId");
        session.removeAttribute("selectMode");
        session.removeAttribute("occupType");
        session.removeAttribute("showProdOption");
        session.removeAttribute("adminRegType");
        session.removeAttribute("regionCode");
        session.removeAttribute("endorseAuthorized");
        session.removeAttribute("endorsementType");
        session.removeAttribute("endorseAgnCode");
        session.removeAttribute("grpType");
        session.removeAttribute("actProccess");
        session.removeAttribute("polType");
        session.removeAttribute("cpeType");
        session.removeAttribute("peCode");
        session.removeAttribute("pctType");
        session.removeAttribute("hasProdOption");
        session.removeAttribute("annPymtType");
        session.removeAttribute("annuitySource");
        session.removeAttribute("sprShortDesc");
        session.removeAttribute("prodInterestType");
        session.removeAttribute("dobPopCode");
        session.removeAttribute("dtyCode");
        session.removeAttribute("podCode");
        session.removeAttribute("dobCode");
        session.removeAttribute("relationCode");
        session.removeAttribute("ldrDrcrVal");
        session.removeAttribute("lenSubType");
        session.removeAttribute("agnCodeToPay");
        session.removeAttribute("isrenewal");
        session.removeAttribute("separateBussPosting");
        session.removeAttribute("butPayAmt");
        session.removeAttribute("butpayReason");
        session.removeAttribute("butpayallowed");
        session.removeAttribute("OverrideAppliedOn");
        session.removeAttribute("actTypeId");
        session.removeAttribute("userDefinedPenalty");
        session.removeAttribute("quotPol");
        session.removeAttribute("ovProdCode");
        session.removeAttribute("invRtprodType");
        session.removeAttribute("prodToReplicate");
        session.removeAttribute("replicating");
        session.removeAttribute("popCodeReplicate");
        session.removeAttribute("prodReplicateTo");
        session.removeAttribute("prodCVTCode");
        session.removeAttribute("prodCode");
        session.removeAttribute("prodReplicateTo");
        session.removeAttribute("popCode");
        session.removeAttribute("pctCode");
        session.removeAttribute("productCode");
        session.removeAttribute("surrAllocToProposalNo");
        session.removeAttribute("payFreqDesc");
        session.removeAttribute("showDependantNo");
        session.removeAttribute("proposalCode");
        session.removeAttribute("assuredSelected");
        session.removeAttribute("selectedManbpCode");
        session.removeAttribute("prpCodeCon");
        session.removeAttribute("subStragnCode");
        session.removeAttribute("proposalNoSelectedVal");
        session.removeAttribute("policylNoSelectedVal");
        session.removeAttribute("budgetLevel");
        session.removeAttribute("budgetLevel1");
        session.removeAttribute("branchAgentCode");
        session.removeAttribute("branchAgenciesCode");
        session.removeAttribute("ltdCode");
        session.removeAttribute("bankSelect");
        session.removeAttribute("ldtTransNo");
        session.removeAttribute("agentCode");
        session.removeAttribute("aodbCode");
        session.removeAttribute("transNo");
        session.removeAttribute("newEditAgent");
        session.removeAttribute("enquireAgent");
        session.removeAttribute("transRender");
        session.removeAttribute("transSelected");
        session.removeAttribute("loanProcessType");
        session.removeAttribute("narrativeAtImport");
        session.removeAttribute("accountNoAtImport1");
        session.removeAttribute("termSource");
        session.removeAttribute("lapBrnCode");
        session.removeAttribute("lairCode");
        session.removeAttribute("cancellationBy");
        session.removeAttribute("lenSubTypeVal");
        session.removeAttribute("polClient");
        session.removeAttribute("prdCode");
        session.removeAttribute("smsPol");
        session.removeAttribute("newProp");
        session.removeAttribute("prodAllowRenwal");
        session.removeAttribute("clientPolNo");
        session.removeAttribute("ecmPolNo");
        session.removeAttribute("newProdChange");
        session.removeAttribute("prodChngEndrCode");
        session.removeAttribute("calcInvValue");
        session.removeAttribute("CaPolEffectiveDate");
        session.removeAttribute("pclCode");
        session.removeAttribute("pclButpayReason");
        session.removeAttribute("pclButPayAmt");
        session.removeAttribute("ecmProductCode");
        session.removeAttribute("lgrCode");
        session.removeAttribute("nmsoCode");
        session.removeAttribute("viewMyticket");
        session.removeAttribute("polLoadingType");
        session.removeAttribute("clientLoadingType");
        session.removeAttribute("agnLoadingType");
        session.removeAttribute("loanPolCode");
        session.removeAttribute("drtCode");
        session.removeAttribute("ecmAgnName");
        session.removeAttribute("dependantCode");
        session.removeAttribute("dependantEdittable");
        session.removeAttribute("agenBenCode");
        session.removeAttribute("polRefNo");
        session.removeAttribute("templateControler");
        session.removeAttribute("clientShtDesc");
        session.removeAttribute("doneBy");
        session.removeAttribute("payee");
        session.removeAttribute("consolidBranch");
        session.removeAttribute("reinComputePrem");
        session.removeAttribute("lawtCode");
        session.removeAttribute("lrrCode");
        session.removeAttribute("lawCode");
        session.removeAttribute("lawtActCode");
        session.removeAttribute("lawLrrCode");
        session.removeAttribute("prmCode");
        session.removeAttribute("edCode");
        session.removeAttribute("itemAccCode");
        session.removeAttribute("bbrRefCode");
        session.removeAttribute("endrBankAccNo");
        session.removeAttribute("endrBankAccName");
        session.setAttribute("docBatched", "no");
        session.removeAttribute("reinParam");
        session.removeAttribute("ReinProcess");
        session.removeAttribute("ReinProcessArea");
        session.removeAttribute("ReinProcessSubArea");
        session.removeAttribute("RateFacType");
        session.removeAttribute("InvestmentRider");
        session.removeAttribute("InvriderAllowed");
        session.removeAttribute("clmNoSearch");
        session.removeAttribute("clmPolNoSearch");
        session.removeAttribute("clmClientSearch");
        session.removeAttribute("clmCausSearch");
        session.removeAttribute("sactCode");
        session.removeAttribute("postPoneReasonCode");
        session.removeAttribute("osprem");
        session.removeAttribute("prem_alloc_from");
        session.removeAttribute("loan_alloc_from");
        session.removeAttribute("pens_alloc_from");
        session.removeAttribute("competitionDesc");
        session.removeAttribute("introCode");


        //        session.removeAttribute("activityName");
        //        session.removeAttribute("ticketID");
        //        session.removeAttribute("processInstance");
        //session.removeAttribute("agentNameSrch");
        //session.removeAttribute("agentShtDescSrch");
        if (session.getAttribute("resetSessionMap") != null) {
            if (session.getAttribute("resetSessionMap").toString().equalsIgnoreCase("Y")) {
                GlobalCC.resetSessionMap();
            }
        } else {
            GlobalCC.resetSessionMap();
        }
        //Make sure u remove this one, so that it is explicitly set when calling this function
        session.removeAttribute("resetSessionMap");

        ////System.out.println("Free Memory"+Runtime.getRuntime().runFinalization());
        return null;
    }
}
