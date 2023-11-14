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

package TurnQuest.view.Base;


import TurnQuest.view.Connect.Authorization;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.utilities.ParameterFinder;

import java.math.BigDecimal;

import java.net.URLEncoder;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashMap;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.commons.dbutils.DbUtils;


public class Rendering {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private static RichSelectBooleanCheckbox clientForLMS = null;
    private static RichSelectBooleanCheckbox clientForGIS = null;

    public Rendering() {
        super();
    }

    public String getEstabLogo() {
        if (session.getAttribute("orgCode") == null) {
            return "/images/compLogo.gif";
        } else {
            return "/images/" + session.getAttribute("orgCode").toString() +
                ".gif";
        }
    }

    public boolean isClientId() {
        String site = "NONE";
        site = new Util().getDefaultSite();
        System.out.println("THIS IS DONE" + site);
        return site.equalsIgnoreCase("ID_NUMBER_REQUIRED");
    }

    public boolean isRelationShipMan() {
        String site = "NONE";
        site = new Util().getRelationShipMand();
        if (site.equals("Y")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isClientContactDebtor() {
        String type = "Y";
        if (session.getAttribute("CLIENT_CONTACT_DEBTOR") != null) {
            type = session.getAttribute("CLIENT_CONTACT_DEBTOR").toString();
        }
        if (type.equalsIgnoreCase("Y")) {
            return true;
        } else {
            return false;
        }
    }

    public String getClientContactDebtorLabel() {
        String type = "Debtor Account : ";
        if (session.getAttribute("CLIENT_CONTACT_DEBTOR") != null) {
            type = session.getAttribute("CLIENT_CONTACT_DEBTOR").toString();
        }
        if (type.equalsIgnoreCase("Y")) {
            return "Debtor Account : ";
        } else {
            return "Sector : ";
        }
    }

    public String getPostalEmailAddLabel() {
        String type = "Y";
        if (session.getAttribute("CLIENT") != null) {
            type = session.getAttribute("CLIENT").toString();
        }
        if (type.equalsIgnoreCase("EAGLE")) {
            return "Email Address : ";
        } else {
            System.out.println("Client " + type);
            return "Postal Address : ";

        }
    }

    public String getPysicalSmsLabel() {
        String type = "Y";
        if (session.getAttribute("CLIENT") != null) {
            type = session.getAttribute("CLIENT").toString();
        }
        if (type.equalsIgnoreCase("EAGLE")) {
            return "SMS No : ";
        } else {
            System.out.println("Client " + type);
            return "Physical Address : ";

        }
    }

    public boolean isLdapRequired() {
        String type = "NORMAL";
        if (session.getAttribute("SIGN_IN_MODE") != null) {
            type = session.getAttribute("SIGN_IN_MODE").toString();
        }
        if (type.equalsIgnoreCase("LDAP")) {
            return true;
        } else {
            return false;
        }
    }


    private String loginType() {
        String loginType = "NORMAL";
        OracleConnection conn = null;
        OracleCallableStatement callStmt = null;
        OracleResultSet rs = null;
        try {
            DBConnector datahandler = new DBConnector();

            conn = datahandler.getLoginConnection();

            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.getParameter(?); end;";
            callStmt = (OracleCallableStatement)conn.prepareCall(query1);
            callStmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            callStmt.setString(2, "SIGN_IN_MODE");

            callStmt.execute();
            rs = (OracleResultSet)callStmt.getObject(1);
            while (rs.next()) {
                //session.setAttribute("SIGN_IN_MODE", rs.getString(1));
                loginType = rs.getString(1);

            }
            callStmt.close();


            rs.close();
            conn.close();


        } catch (Exception exception) {

            GlobalCC.EXCEPTIONREPORTING(conn, exception);
            return loginType;
        } finally {
            DbUtils.closeQuietly(conn, callStmt, rs);
        }
        return loginType;

    }

    public boolean isNotLdapRequired() {
        String type = loginType();
        if (type.equalsIgnoreCase("LDAP")) {
            return false;
        } else {
            return true;
        }
    }


    public boolean isRegNumber() {
        String site = "NONE";
        site = new Util().getRegNumber();
        System.out.println("THIS IS REG_NUMBER_REQ" + site);
        if (site.toString().equalsIgnoreCase("Y")) {
            session.setAttribute("regReq", "mandatory");
            return true;

        } else {
            session.setAttribute("regReq", null);
            return false;

        }
    }

    public boolean isRenTimes() {
        String site = "NONE";
        site = new Util().getRenTimes();
        if (site.toString().equalsIgnoreCase("Y")) {
            return true;
        } else {
            return false;
        }
    }

    public String getPinValue() {
        String site = "NONE";
        site = new Util().getPinValue();
        if (site == "NONE") {
            return "PIN Number";
        } else {
            return site;
        }
    }

    public String getStateNameValue() {
        String site = "NONE";
        if (GlobalCC.checkNullValues(session.getAttribute("CLIENT")) != null) {
            site = session.getAttribute("CLIENT").toString();
        }


        if (site.equalsIgnoreCase("GLAICO")) {
            return "Region :";
        } else {
            return "State Name :";
        }
    }

    public String getDDFileName() {
        String ddFileName = "DirectDebitFile";

        if (session.getAttribute("ddFileDesc") != null) {
            ddFileName = session.getAttribute("ddFileDesc").toString();
        }
        return ddFileName;
    }

    public boolean isRelationShipOfficer() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "RELATIONSHIP OFFICER REQUIRED");
            cst.execute();
            delink = cst.getString(1);
            if (delink != null && delink.equals("Y")) {
                return true;

            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return false;
    }

    
        
    public boolean isMobilePrefix() {
        DBConnector myConn = new DBConnector();
        Connection conn = null;
        String delink = "N";
        CallableStatement cst = null;
        try {
            conn = myConn.getDatabaseConnection();
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "MOBILE_PREFIX");
            cst.execute();
            delink = GlobalCC.checkNullValues(cst.getString(1));
        } catch (SQLException e) {
            System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return "Y".equals(delink);
    }
           
           
           
    public boolean isSmsPrefix() {
        DBConnector myConn = new DBConnector();
        Connection conn = null;
        String delink = "N";
        CallableStatement cst = null;
        try {
            conn = myConn.getDatabaseConnection();
            cst =
    conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "SMS_PREFIX");
            cst.execute();
            delink = GlobalCC.checkNullValues(cst.getString(1));
        } catch (SQLException e) {
            System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return "Y".equals(delink);
    }

    public String getMobileChar() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "6";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "MOBILE_PREFIX");
            cst.execute();
            delink = cst.getString(1);
            if (delink != null && delink.equals("Y")) {
                return "6";

            } else {
                return "10";
            }
        } catch (SQLException e) {
            System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
    }

    public boolean isAgencyRelationShipManager() {
        int accountType;
        if (session.getAttribute("accountTypeCode") != null) {
            accountType =
                    Integer.parseInt(session.getAttribute("accountTypeCode").toString());
        } else {
            accountType = 0;
        }
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "RELATIONSHIP OFFICER REQUIRED");
            cst.execute();
            delink = cst.getString(1);
            if ((delink != null && delink.equals("Y")) && accountType == 2 ||
                accountType == 3 || accountType == 16) {
                return true;

            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return false;
    }

    public boolean isCooperatePin() {
        String site = "NONE";
        site = new Util().getDefaultSite();
        return site.equalsIgnoreCase("Y");
    }

    public String getTemplate() {
        if (session.getAttribute("template") == null) {
            return "/templates/coreTemplate.jspx";
        } else {
            return (String)session.getAttribute("template");
        }
    }

    public String getSysUserCode() {

        if (session.getAttribute("sysUserCode") == null)
            return null;
        else
            return session.getAttribute("sysUserCode").toString();

    }

    public boolean isSetups() {
        if (session.getAttribute("MM") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("MM");
            if (sm.equalsIgnoreCase("setups")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isAdministration() {

        if (session.getAttribute("KK") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("KK");
            if (sm.equalsIgnoreCase("administration")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isServiceRequests() {

        if (session.getAttribute("MM") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("MM");
            if (sm.equalsIgnoreCase("serviceDesk")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isServiceReq() {

        if (session.getAttribute("KK") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("KK");
            if (sm.equalsIgnoreCase("serviceReq")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isCampaigns() {

        if (session.getAttribute("MM") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("MM");
            if (sm.equalsIgnoreCase("CampaignManagement")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isSystemReports() {

        if (session.getAttribute("KK") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("KK");
            if (sm.equalsIgnoreCase("SystemReports")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isActivityManagement() {

        if (session.getAttribute("KK") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("KK");
            if (sm.equalsIgnoreCase("ActivityManagement")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isLeadManagement() {

        if (session.getAttribute("KK") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("KK");
            if (sm.equalsIgnoreCase("LeadManagement")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isAccountsManagement() {

        if (session.getAttribute("KK") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("KK");
            if (sm.equalsIgnoreCase("AccountsManagement")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isPortalSetUps() {

        if (session.getAttribute("MM") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("MM");
            if (sm.equalsIgnoreCase("portalSetUps")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isOrganizationalSetups() {

        if (session.getAttribute("KK") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("KK");
            if (sm.equalsIgnoreCase("OrganizationalSetups")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isOrganizationalSetupsGl() {

        if (session.getAttribute("MM") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("MM");
            if (sm.equalsIgnoreCase("OrganizationalSetupsGL")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isEcmRequired() {


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        String ret = "N";
        try {


            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := Tqc_Parameters_Pkg.get_param_varchar(?); end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.registerOutParameter(1, OracleTypes.VARCHAR);
            stmt.setString(2, "DMS_ENABLED");
            stmt.execute();
            ret = stmt.getString(1);
            stmt.close();
            connection.commit();
            connection.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();

        }
        return ret.equalsIgnoreCase("Y");
    }

    public boolean isOrganizationalParameters() {
        if (session.getAttribute("KK") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("KK");
            if (sm.equalsIgnoreCase("OrganizParameters")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isMessaging() {

        if (session.getAttribute("KK") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("KK");
            if (sm.equalsIgnoreCase("Messaging")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isCampaignManagement() {

        if (session.getAttribute("KK") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("KK");
            if (sm.equalsIgnoreCase("Campaignmng")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isBankSetups() {

        if (session.getAttribute("KK") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("KK");
            if (sm.equalsIgnoreCase("BankSetups")) {
                return true;
            } else {
                return false;
            }
        }
    }


    /**
     * This function is used to determine whether or not to show Kenyan variables
     * or Nigerian/Offshore variables. This is because some content within the
     * application may be more appropriate to offshore sites than the Kenyan sites.
     *
     * Values can include KENYA or NIGERIA|OTHER|OFFSHORE|
     */
    public boolean isLocalSite() {
        String val = (String)GlobalCC.getSysParamValue("DEFAULT_SITE");
        return "KENYA".equalsIgnoreCase(val);
    }
    
    public boolean isClientNOtGN(){
        String val = (String)GlobalCC.getSysParamValue("TOGGLE_ORG_TAB_NAMES");
        if("N".equalsIgnoreCase(val)){
                return true;
            }
       return false;
    }
    
    public boolean isClientGN(){
        String val = (String)GlobalCC.getSysParamValue("TOGGLE_ORG_TAB_NAMES");
        if("Y".equalsIgnoreCase(val)){
                return true;
            }
        return false;
    }
    
    public boolean isSalesOffAgencyNameShown(){
        String val = (String)GlobalCC.getSysParamValue("TOGGLE_ORG_TAB_NAMES");
        if("Y".equalsIgnoreCase(val)){
                return true;
            }
        return false;
    }
    
    public boolean isUserCategoriesEnable(){
        String val = (String)GlobalCC.getSysParamValue("USER_GROUP_CATEGORIES_ENABLEB");
        if("Y".equalsIgnoreCase(val)){
                return true;
            }
        return false;
    }

    public boolean isDistrict() {
        if (session.getAttribute("DISTRICT") == null) {
            return false;
        } else {
            String district = session.getAttribute("DISTRICT").toString();
            if (district.equalsIgnoreCase("Y")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isBroker() {
        if (session.getAttribute("ORG_TYPE") == null) {
            return true;
        } else {
            String district = session.getAttribute("ORG_TYPE").toString();
            if (district.equalsIgnoreCase("BRK")) {
                return false;
            } else {
                return true;
            }
        }
    }

    public String getDefaultAdminUnitSingular() {
        if (session.getAttribute("ADMIN_REG_TYPE") == null)
            return null;
        return GlobalCC.formatAdminUnitSingular(session.getAttribute("ADMIN_REG_TYPE"));
    }

    public String getManagerLabel() {
        if (session.getAttribute("MANAGER") == null)
            return "Manager :";
        return session.getAttribute("MANAGER").toString();


    }

    public String getPayrollLabel() {
        if (session.getAttribute("PAYROLL_NO_LABEL") == null)
            return "Payroll No :";
        return session.getAttribute("PAYROLL_NO_LABEL").toString();


    }

    public String getIdNoLabel() {
        if (session.getAttribute("ID_NO_LABEL") == null)
            return "ID/Registration No :";
        return session.getAttribute("ID_NO_LABEL").toString();
    }

    public String getDefaultAdminUnitPlural() {
        if (session.getAttribute("ADMIN_REG_TYPE") == null)
            return null;
        return GlobalCC.formatAdminUnitPlural(session.getAttribute("ADMIN_REG_TYPE"));


    }

    public void checkIfClientDOBRequired() {


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        int status = 0;
        int rankStatus = 0;
        try {


            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.checkif_dob_required(); end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);


            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            while (rs.next()) {
                session.setAttribute("DOB_REQUIRED", rs.getString(1));

            }
            rs.close();

            stmt.close();
            connection.commit();
            connection.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
        }
    }

    public void checkIfRelationshRequired() {


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;

        try {


            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? :=TQC_PARAMETERS_PKG.get_param_varchar('RELATIONSHIP_OFFICER_REQUIRED'); end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.registerOutParameter(1, OracleTypes.VARCHAR);
            stmt.execute();
            String value = stmt.getString(1);

            session.setAttribute("RELATIONSHIP_OFFICER_REQUIRED", value);
            stmt.close();
            connection.commit();
            connection.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
        }
    }

    public void checkIfClientSMSRequired() {


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        int status = 0;
        int rankStatus = 0;
        try {


            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.checkif_sms_required(); end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);


            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            while (rs.next()) {
                session.setAttribute("SMS_REQUIRED", rs.getString(1));

            }
            rs.close();

            stmt.close();
            connection.commit();
            connection.close();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
        }
    }

    public boolean isCreditRatingRequired() {
        String val =
            (String)GlobalCC.getSysParamValue("CREDIT_RATING_REQUIRED");
        return ("Y".equalsIgnoreCase(val));
    }

    public void checkIfClientDefaultDOB() {
        String val =
            (String)GlobalCC.getSysParamValue("CLIENT_DEFAULT_DOB_DATE");
        session.setAttribute("DEFAULT_DATE", val);
    }

    public void checkIfEmailRequired() {
        String val = (String)GlobalCC.getSysParamValue("EMAIL_REQUIRED");
        session.setAttribute("EMAIL_REQUIRED", val);
    }

    public void checkIfTelephone() {
        String val = (String)GlobalCC.getSysParamValue("TELEPHONE_REQUIRED");
        session.setAttribute("TELEPHONE_REQUIRED", val);
    }

    public void checkIfMemoClassAplic() {
        String val = (String)GlobalCC.getSysParamValue("MEMO_CLASS_REQUIRED");
        session.setAttribute("MEMO_CLASS_REQUIRED", val);
    }

    public void checkIfPhysicalAddrRequired() {
        String val =
            (String)GlobalCC.getSysParamValue("PHYSICAL_ADDRESS_REQUIRED");
        session.setAttribute("PHYSICAL_ADDRESS_REQUIRED", val);
    }

    public void checkIfPostalAddrRequired() {
        String val =
            (String)GlobalCC.getSysParamValue("POSTAL_ADDRESS_REQUIRED");
        session.setAttribute("POSTAL_ADDRESS_REQUIRED", val);
    }

    public boolean isPHYSICAL_ADDRESS_REQUIRED() {
        String val =
            (String)GlobalCC.getSysParamValue("PHYSICAL_ADDRESS_REQUIRED");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isPOSTAL_ADDRESS_REQUIRED() {
        String val =
            (String)GlobalCC.getSysParamValue("POSTAL_ADDRESS_REQUIRED");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isBankRegionManager() {
        String val = (String)GlobalCC.getSysParamValue("BANK_REGION_MANAGER");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isDivisionApplicable() {
        String val = (String)GlobalCC.getSysParamValue("DIVISIONS_APPLIC");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isMemoApplicable() {
        return true;
    }

    public boolean isDOBRequired() {
        String val = (String)GlobalCC.getSysParamValue("CLIENT_DOB_REQUIRED");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isSMSRequired() {
        String val = (String)GlobalCC.getSysParamValue("CLIENT_SMS_REQUIRED");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isSMS2Required() {
        String val =
            (String)GlobalCC.getSysParamValue("CLIENT_SMS_2_REQUIRED");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isSMSVisible() {
        String val = (String)GlobalCC.getSysParamValue("CLIENT_SMS_VISIBLE");
        return ("Y".equalsIgnoreCase(val));
    }


    public String getSMS_NO_FORMAT() {
        String val = (String)GlobalCC.getSysParamValue("SMS_NO_FORMAT");
        if (val != null) {
            return val.trim();
        }
        return val;
    }

    public boolean isSMS2Visible() {
        String val = (String)GlobalCC.getSysParamValue("CLIENT_SMS_2_VISIBLE");
        return ("Y".equalsIgnoreCase(val));
    }


    public boolean isRelationshipOfficerRequired() {
        String val =
            (String)GlobalCC.getSysParamValue("RELATIONSHIP_OFFICER_REQUIRED");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isTelephoneRequired() {
        String val = (String)GlobalCC.getSysParamValue("TELEPHONE_REQUIRED");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isOccupationRequired() {
        String val = (String)GlobalCC.getSysParamValue("OCCUPATION");
        return ("Y".equalsIgnoreCase(val));
    }


    public boolean isEmailRequired() {
        String val = (String)GlobalCC.getSysParamValue("EMAIL_REQUIRED");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isStateRequired() {
        String val = (String)GlobalCC.getSysParamValue("STATE_REQUIRED");
        return ("Y".equalsIgnoreCase(val));

    }

    public boolean isWhichSearch() {
        if (session.getAttribute("_search") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("_search");
            if (sm.equalsIgnoreCase("addGroup")) {
                return true;
            } else {
                return false;
            }
        }
    }
    
    

    public boolean isRenderHoldingCompany() {
        if (session.getAttribute("HoldingCompany") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("HoldingCompany");
            if (sm.equalsIgnoreCase("N")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isImportSuccess() {
        String success = (String)session.getAttribute("importSuccess");
        if (success == null) {
            return true;
        } else {
            if (success.equalsIgnoreCase("Y")) {
                return false;
            } else {
                return true;
            }
        }
    }

    public boolean isIdPassportMandatory() {
        String val = (String)GlobalCC.getSysParamValue("ID_OR_PASSPORT_MAND");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isID_OR_PASSPORT_MAND() {
        String val = (String)GlobalCC.getSysParamValue("ID_OR_PASSPORT_MAND");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isPIN_OR_PASS_MAND() {
        String val = (String)GlobalCC.getSysParamValue("PIN_OR_PASS_MAND");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean getInhouseIdPinMand() {
        String val =
            (String)GlobalCC.getSysParamValue("INHOUSE_AGENTS_ID_PIN_BRN");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isDriverExpNumber() {
        String val = (String)GlobalCC.getSysParamValue("DRIVER_EXP_NUMBER");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isAgencyPinMand() {
        String val = (String)GlobalCC.getSysParamValue("AGENCY_PIN_MAND");
        return ("Y".equalsIgnoreCase(val));
    }


    public boolean isRegCodeMand() {
        String val = (String)GlobalCC.getSysParamValue("REG_CODE_MANDATORY");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isBankBranchAccountMand() {
        String val =
            (String)GlobalCC.getSysParamValue("BANK_BRANCH_ACCOUNT_MAND");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isLocationApplicable() {
        String site = "NONE";
        site = new Util().getShowRegion();
        if (site.equals("Y")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isStatesMandatory() {
        String val = (String)GlobalCC.getSysParamValue("STATE_MANDATORY");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isTownMandatory() {
        String val = (String)GlobalCC.getSysParamValue("TOWN_MANDATORY");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isInvoiceMand() {
        String val =
            (String)GlobalCC.getSysParamValue("INVOICE_NUMBER_MANDATORY");
        return ("Y".equalsIgnoreCase(val));
    }

    public String getFms1() {
        if (session.getAttribute("systemMap") != null) {
            HashMap map = (HashMap)session.getAttribute("systemMap");
            if (map != null) {
                Object path = map.get("1");
                if (path != null) {
                    return path.toString() + "home.jspx";
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

    }


    public String getFms2() {
        if (session.getAttribute("systemMap") != null) {
            HashMap map = (HashMap)session.getAttribute("systemMap");
            if (map != null) {
                Object path = map.get("40");
                if (path != null) {
                    return path.toString() + "home.jspx";
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public String getFms2Active() {
        if (session.getAttribute("systems") != null) {
            HashMap map = (HashMap)session.getAttribute("systems");
            if (map != null) {
                Object path = map.get("40");
                if (path != null) {
                    return path.toString() + "home.jspx";
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public String getCrm() {
        if (session.getAttribute("systemMap") != null) {
            HashMap map = (HashMap)session.getAttribute("systemMap");
            if (map != null) {
                Object path = map.get("0");
                if (path != null) {
                    return path.toString() + "home.jspx";
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public String getHrmsActive() {
        if (session.getAttribute("systems") != null) {
            HashMap map = (HashMap)session.getAttribute("systems");
            if (map != null) {
                Object path = map.get("28");
                if (path != null) {
                    return path.toString() + "home.jspx";
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String getCrmActive() {
        if (session.getAttribute("systems") != null) {
            HashMap map = (HashMap)session.getAttribute("systems");
            if (map != null) {
                Object path = map.get("0");
                if (path != null) {
                    return path.toString() + "home.jspx";
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String getFms1Active() {
        if (session.getAttribute("systems") != null) {
            HashMap map = (HashMap)session.getAttribute("systems");
            if (map != null) {
                Object path = map.get("1");
                if (path != null) {
                    return path.toString() + "home.jspx";
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public String getLmsActive() {

        if (session.getAttribute("systems") != null) {
            HashMap map = (HashMap)session.getAttribute("systems");
            if (map != null) {
                Object path = map.get("27");
                if (path != null) {
                    return path.toString() + "home.jspx";
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String getLmsOrdActive() {
        if (session.getAttribute("systems") != null) {
            HashMap map = (HashMap)session.getAttribute("systems");
            if (map != null) {
                Object path = map.get("26");
                if (path != null) {
                    return path.toString() + "home.jspx";
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String getGisActive() {
        if (session.getAttribute("systems") != null) {
            HashMap map = (HashMap)session.getAttribute("systems");
            if (map != null) {
                Object path = map.get("37");
                if (path != null) {
                    return path.toString() + "home.jspx";
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public String getLms() {
        if (session.getAttribute("systemMap") != null) {
            HashMap map = (HashMap)session.getAttribute("systemMap");
            if (map != null) {
                Object path = map.get("27");
                if (path != null) {
                    return path.toString() + "home.jspx";
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public String getLmsOrd() {
        if (session.getAttribute("systemMap") != null) {
            HashMap map = (HashMap)session.getAttribute("systemMap");
            if (map != null) {
                Object path = map.get("26");
                if (path != null) {
                    return path.toString() + "home.jspx";
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public String getLmsUrl() {
        if (session.getAttribute("systemMap") != null) {
            HashMap map = (HashMap)session.getAttribute("systemMap");
            if (map != null) {
                String path = (String)map.get("27");
                return path;
            }
        }
        return null;
    }

    public String getCrmUrl() {
        if (session.getAttribute("systemMap") != null) {
            HashMap map = (HashMap)session.getAttribute("systemMap");
            if (map != null) {
                String path = (String)map.get("0");
                return path;
            }
        }
        return null;
    }

    public String getGroup() {
        if (session.getAttribute("systemMap") != null) {
            HashMap map = (HashMap)session.getAttribute("systemMap");
            if (map != null) {
                Object path = map.get("27");
                if (path != null) {
                    return path.toString() + "lmsmain.jspx";
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public String getGroupActive() {
        if (session.getAttribute("systems") != null) {
            HashMap map = (HashMap)session.getAttribute("systems");
            if (map != null) {
                Object path = map.get("27");
                if (path != null) {
                    return path.toString() + "lmsmain.jspx";
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

    }


    public boolean isLeaveAllow() {
        if (session.getAttribute("leaveAll") != null) {
            if (session.getAttribute("leaveAll").toString().equalsIgnoreCase("Y")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public String getGis() {
        if (session.getAttribute("systemMap") != null) {
            HashMap map = (HashMap)session.getAttribute("systemMap");
            if (map != null) {
                BigDecimal sys = new BigDecimal(37);
                Object path = map.get("37");
                if (path != null) {
                    return path.toString() + "home.jspx";
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public String getHrms() {
        if (session.getAttribute("systemMap") != null) {
            HashMap map = (HashMap)session.getAttribute("systemMap");
            if (map != null) {
                Object path = map.get("28");
                if (path != null) {
                    return path.toString() + "home.jspx";
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public String getSelfService() {
        if (session.getAttribute("systemMap") != null) {
            HashMap map = (HashMap)session.getAttribute("systemMap");
            if (map != null) {
                Object path = map.get("30");
                if (path != null) {
                    return path.toString() + "land.jspx";
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public BigDecimal getLoginUser() {
        return (BigDecimal)session.getAttribute("UserCode");
    }

    public boolean isSingleSignon() {
        String val = (String)GlobalCC.getSysParamValue("SINGLESIGNON");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isSELF_SERVICE() {
        String val = (String)GlobalCC.getSysParamValue("SELF_SERVICE");
        return ("Y".equalsIgnoreCase(val));
    }

    public String getMessage() {
        if (session.getAttribute("messsage") != null) {
            return (String)session.getAttribute("messsage");
        } else {
            return null;
        }

    }

    public String getInlineMsgColor() {
        if (FacesContext.getCurrentInstance().getMessages(null).hasNext()) {
            int fm =
                FacesContext.getCurrentInstance().getMessages(null).next().getSeverity().compareTo(FacesMessage.SEVERITY_INFO);
            if (fm == 0) {
                return "Blue";
            } else {
                return "Red";
            }
        } else {
            return "Red";
        }
    }

    public String getLandScreen() {
        return "DEFAULT";
        /*if (session.getAttribute("LAND_SCREEN") == null) {
            return "DEFAULT";
        } else {
            return session.getAttribute("LAND_SCREEN").toString().toUpperCase();
        }*/
    }

    public boolean isAccountEmailMand() {
        String val =
            (String)GlobalCC.getSysParamValue("AGENCY_EMAIL_MANDATORY");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isCountryMandatory() {
        String val =
            (String)GlobalCC.getSysParamValue("CLIENT_COUNTRY_MANDATORY");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isAgencyCountyMand() {

        String val =
            (String)GlobalCC.getSysParamValue("AGENCY_COUNTY_MANDATORY");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isAgencyTowmMand() {
        String val =
            (String)GlobalCC.getSysParamValue("AGENCY_TOWN_MANDATORY");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isAgencyBranchUnitMandatory() {
        String val =
            (String)GlobalCC.getSysParamValue("AGENCY_BRANCH_UNIT_MANDATORY");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isAgencyMaritalStatusMandatory() {
        String val =
            (String)GlobalCC.getSysParamValue("AGENCY_MARITAL_STATUS_MAND");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isAgencyQualificationMandatory() {
        String val =
            (String)GlobalCC.getSysParamValue("AGENCY_QUALIFICATION_MAND");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isAgencyCommStartDateMandatory() {
        String val =
            (String)GlobalCC.getSysParamValue("AGENT_COM_START_DATE_MAND");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isAgencyDOBMandatory() {
        String val = (String)GlobalCC.getSysParamValue("AGENT_DOB_MANDATORY");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isClientCountryMandatory() {
        String val =
            (String)GlobalCC.getSysParamValue("CLIENT_COUNTRY_MANDATORY");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isClientBranchMandatory() {
        String val =
            (String)GlobalCC.getSysParamValue("CLIENT_BRANCH_MANDATORY");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isClientPinMandatory() {
        String val = (String)GlobalCC.getSysParamValue("CLIENT_PIN_MANDATORY");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isDivisionApplicableVal() {
        String val = (String)GlobalCC.getSysParamValue("DIVISIONS_APPLIC");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isRequests() {
        String requests;
        if (session.getAttribute("fromHome") != null) {
            requests = session.getAttribute("fromHome").toString();
        } else {
            requests = "NONE";
        }
        if (session.getAttribute("fromTracking") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("fromTracking");
            if (sm.equalsIgnoreCase("Y") && requests == "NONE") {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isServiceReqPolicyNoApp() {
        String accountType =
            GlobalCC.checkNullValues(session.getAttribute("accountType"));
        if ("C".equalsIgnoreCase(accountType) &&
            isSERVICE_POLICY_NO_VISIBLE()) {
            return true;
        }
        return false;
    }

    public boolean isRequestsDtls() {

        if (session.getAttribute("fromTracking") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("fromTracking");
            if (sm.equalsIgnoreCase("Y")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isSaccoDetails() {
        String val = (String)GlobalCC.getSysParamValue("SACCO_VISIBLE");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isRequestsHome() {
        //    if(session.getAttribute("fromTracking")==null)
        //    {
        //          return false;
        //        }
        if (session.getAttribute("fromHome") == null) {
            return false;
        } else {
            String sm = (String)session.getAttribute("fromHome");
            if (sm.equalsIgnoreCase("Y")) {
                return true;
            } else {
                return false;
            }
        }
    }


    public boolean isChecked() {
        String val = (String)GlobalCC.getSysParamValue("CHECKED_VISIBLE");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isThreeTier() {

        String val = (String)GlobalCC.getSysParamValue("X_TIER_COMM_SETUP");
        return ("3".equalsIgnoreCase(val));
    }

    public boolean isFourTier() {
        String val = (String)GlobalCC.getSysParamValue("X_TIER_COMM_SETUP");
        return ("4".equalsIgnoreCase(val));
    }

    public boolean isChannelManager() {
        String val = (String)GlobalCC.getSysParamValue("CHANNEL_MANAGER");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isNewRolesScreenSet() {
        String val = (String)GlobalCC.getSysParamValue("USE_NEW_ROLES_SCREEN");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isCUSTOMER_COMPANY_APP() {
        String val = (String)GlobalCC.getSysParamValue("CUSTOMER_COMPANY_APP");
        return ("Y".equalsIgnoreCase(val));
    }


    public String getSystemDateFormat() {
        String val = (String)GlobalCC.getSysParamValue("SYSDATEFORMAT");
        if (val != null) {
            return val;
        }
        return "dd-MMM-yyyy";
    }

    public String getBranchShtDesc() {

        String auto = "N";
        ParameterFinder finder = new ParameterFinder();
        try {

            auto = finder.getParameterVal("BRANCH_SHT_DESC_AUTO");

        } catch (Exception e) {
            e.printStackTrace();
            auto = "N";

        }
        return auto;

    }

    public boolean isBarchShtAuto() {

        boolean auto = false;
        ParameterFinder finder = new ParameterFinder();
        try {

            if (finder.getParameterVal("BRANCH_SHT_DESC_AUTO").equalsIgnoreCase("Y")) {
                auto = true;
            }


        } catch (Exception e) {
            e.printStackTrace();

        }
        return auto;

    }

    public String getBranchUnitShtDesc() {

        String auto = "N";
        ParameterFinder finder = new ParameterFinder();
        try {

            auto = finder.getParameterVal("BRU_UNT_SHT_DESC");
        } catch (Exception e) {
            e.printStackTrace();
            auto = "N";
        }
        return auto;

    }

    public boolean isBranchUnitShtAuto() {
        String val = (String)GlobalCC.getSysParamValue("BRU_UNT_SHT_DESC");
        return ("Y".equalsIgnoreCase(val));

    }

    public String getIdReg() {
        String site = "NONE";
        site = new Util().getIdRegNumber();
        if (site == "NONE") {
            return "Id/Registration No.:";
        } else {
            return site;
        }
    }

    public boolean isDlMand() {
        String site = "NONE";
        site = new Util().getDlMand();
        if (site.equals("Y")) {
            return true;
        } else {
            return false;
        }
    }

    public String getORGCode() {

        if (session.getAttribute("ORGCode") == null)
            return null;
        else
            return session.getAttribute("ORGCode").toString();

    }

    public String getSessionUrl() {
        String url = null;
        try {
            if (session != null) {
                String sn = ISession.encodeSession(session, null);
                if (sn != null && "".equals(sn) != true) {
                    url = URLEncoder.encode(sn, "UTF-8");
                }
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return url;
    }

    public boolean isCertAuto() {
        String val = (String)GlobalCC.getSysParamValue("CERT_AUTO_SIGNING");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isBANK_TERRITORY_APPLICABLE() {
        String val =
            (String)GlobalCC.getSysParamValue("BANK_TERRITORY_APPLICABLE");
        return ("Y".equalsIgnoreCase(val));
    }

    public static String getFieldLabel(String name) {
        return getFieldAttribute("fm_field_label", name);
    }

    public static boolean isVisibleField(String name) {
        return "Y".equalsIgnoreCase(getFieldAttribute("fm_visible", name));
    }

    public static boolean isDisabledField(String name) {
        return "Y".equalsIgnoreCase(getFieldAttribute("fm_disabled", name));
    }

    public static boolean isMandatoryField(String name) {

        String screenName = getScreenName(name);
        //TICKET CRM-1290 TO CHANGE THIS BLOCK REFER TO THIS TICKET.
        HttpSession s =
            (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (s != null && (("clients.jspx".equalsIgnoreCase(screenName))
        || ("organization.jspx".equalsIgnoreCase(screenName)))) {
            String status =
                GlobalCC.checkNullValues(s.getAttribute("client_account_status"));
            String action =
                GlobalCC.checkNullValues(s.getAttribute("clientAccountAction"));
            if (("INACTIVE".equalsIgnoreCase(status)) &&
                ("E".equalsIgnoreCase(action))) {
                return false;
            }
        }
        //END TICKET CRM-1290
        /* */
        Rendering renderer = new Rendering();
        if ((!renderer.isClientForDisabled()) &&
            renderer.isClientForVisible() &&
            ("clients.jspx".equalsIgnoreCase(screenName)) ||  (("clients.jspx".equalsIgnoreCase(screenName))
        || ("organization.jspx".equalsIgnoreCase(screenName)))) {
            if (clientForGIS != null && clientForLMS != null) {
                if (clientForGIS.isSelected() && clientForLMS.isSelected()) {
                    return "Y".equalsIgnoreCase(getFieldAttribute("fm_gis_mandatory",
                                                                  name)) ||
                        "Y".equalsIgnoreCase(getFieldAttribute("fm_lms_mandatory",
                                                               name));
                } else if (renderer.clientForGIS.isSelected()) {

                    return "Y".equalsIgnoreCase(getFieldAttribute("fm_gis_mandatory",
                                                                  name));
                } else if (renderer.clientForLMS.isSelected()) {
                    return "Y".equalsIgnoreCase(getFieldAttribute("fm_lms_mandatory",
                                                                  name));
                }
            }
        }
        return "Y".equalsIgnoreCase(getFieldAttribute("fm_mandatory", name));
    }

    public static String getFieldAttribute(String attribute, String name) {

        HttpSession session = null;
        String value = null;
        try {
            session =
                    (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            if (session != null) {
                value =
                        GlobalCC.checkNullValues(session.getAttribute(attribute +
                                                                      "." +
                                                                      name));
            }
            if (value == null) {
                if (GlobalCC.tableExists("tqc_form_fields")) {
                    String sql =
                        "SELECT " + attribute + " FROM tqc_form_fields WHERE UPPER(fm_field_name) = UPPER('" +
                        name + "') AND ROWNUM=1";

                    value = IQuery.fetchString(sql);
                    if (value == null) {
                        GlobalCC.EXCEPTIONREPORTING("tqc_form_fields.fm_field_name='" +
                                                    name + "' not found!");
                    }
                    if (session != null && value != null) {
                        session.setAttribute(attribute + "." + name, value);
                    }
                    System.out.println(attribute + "." + name + "=" + value);
                }
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return value;
    }

    public static String getScreenName(String name) {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        ResultSet rs = null;
        String value = null;

        try {
            HttpSession s =
                (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            if (s != null) {
                value =
                        GlobalCC.checkNullValues(s.getAttribute("fm_screen_name[" +
                                                                name + "]"));
            }
            if (value == null) {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                if (GlobalCC.tableExists("tqc_form_fields")) {
                    String query =
                        "SELECT fm_screen_name FROM tqc_form_fields WHERE UPPER(fm_field_name) = UPPER(?) AND ROWNUM=1";
                    stmt = (OracleCallableStatement)conn.prepareCall(query);
                    stmt.setString(1, name);
                    rs = stmt.executeQuery();
                    value =
                            GlobalCC.checkNullValues(rs.next() ? rs.getString("fm_screen_name") :
                                                     null);
                    if (value == null) {
                        GlobalCC.EXCEPTIONREPORTING("tqc_form_fields.fm_field_name='" +
                                                    name + "' not found!");
                    }
                    if (s != null && value != null) {
                        s.setAttribute("fm_screen_name[" + name + "]", value);
                    }
                }
            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return value;
    }

    public String getClientIdRegNoLabel() {
        return Rendering.getFieldLabel("CLIENT_ID_REG_NO");
    }

    public boolean isClientIDRegNoVisible() {
        return Rendering.isVisibleField("CLIENT_ID_REG_NO");
    }

    public boolean isClientIDRegNoRequired() {
        return Rendering.isMandatoryField("CLIENT_ID_REG_NO");
    }

    public boolean isClientPassportRequired() {
        return Rendering.isMandatoryField("CLIENT_PASSPORT_NO");
    }

    public boolean isClientTownNameVisible() {
        return Rendering.isVisibleField("CLIENT_TOWN_NAME");
    }

    //--CLIENT_LEGAL_SHT

    public boolean isCLIENT_LEGAL_SHT_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_LEGAL_SHT");
    }

    public boolean isCLIENT_LEGAL_SHT_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_LEGAL_SHT");
    }

    public boolean isCLIENT_LEGAL_SHT_DISABLED() {
        return Rendering.isDisabledField("CLIENT_LEGAL_SHT");
    }

    public String getCLIENT_LEGAL_SHT_LABEL() {
        return Rendering.getFieldLabel("CLIENT_LEGAL_SHT");
    }
    //--CLIENT_CREDIT_ALLOWED

    public boolean isCLIENT_CREDIT_ALLOWED_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_CREDIT_ALLOWED");
    }

    public boolean isCLIENT_CREDIT_ALLOWED_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_CREDIT_ALLOWED");
    }

    public boolean isCLIENT_CREDIT_ALLOWED_DISABLED() {
        return Rendering.isDisabledField("CLIENT_CREDIT_ALLOWED");
    }

    public String getCLIENT_CREDIT_ALLOWED_LABEL() {
        return Rendering.getFieldLabel("CLIENT_CREDIT_ALLOWED");
    }
    //--CLIENT_CONTACT_NAME

    public boolean isCLIENT_CONTACT_NAME_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_CONTACT_NAME");
    }

    public boolean isCLIENT_CONTACT_NAME_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_CONTACT_NAME");
    }

    public boolean isCLIENT_CONTACT_NAME_DISABLED() {
        return Rendering.isDisabledField("CLIENT_CONTACT_NAME");
    }
    public boolean isCLIENT_CONVERT_TO(){
            String val = (String)GlobalCC.getSysParamValue("CLIENT");
             if(val.equalsIgnoreCase("CUSTODIAN"))                
              return true;   
             return false;
        }
    
    public boolean isPROSPECT_CONVERT_TO(){
            String val = (String)GlobalCC.getSysParamValue("CLIENT");
             if(val.equalsIgnoreCase("CUSTODIAN"))                
              return true;   
             return false;
        }

    public String getCLIENT_CONTACT_NAME_LABEL() {
        return Rendering.getFieldLabel("CLIENT_CONTACT_NAME");
    }
    //--CLIENT_CONTACT_TITLE

    public boolean isCLIENT_CONTACT_TITLE_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_CONTACT_TITLE");
    }

    public boolean isCLIENT_CONTACT_TITLE_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_CONTACT_TITLE");
    }

    public boolean isCLIENT_CONTACT_TITLE_DISABLED() {
        return Rendering.isDisabledField("CLIENT_CONTACT_TITLE");
    }

    public String getCLIENT_CONTACT_TITLE_LABEL() {
        return Rendering.getFieldLabel("CLIENT_CONTACT_TITLE");
    }
    //--CLIENT_CONTACT_SMSNO

    public boolean isCLIENT_CONTACT_SMSNO_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_CONTACT_SMSNO");
    }

    public boolean isCLIENT_CONTACT_SMSNO_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_CONTACT_SMSNO");
    }

    public boolean isCLIENT_CONTACT_SMSNO_DISABLED() {
        return Rendering.isDisabledField("CLIENT_CONTACT_SMSNO");
    }

    public String getCLIENT_CONTACT_SMSNO_LABEL() {
        return Rendering.getFieldLabel("CLIENT_CONTACT_SMSNO");
    }

    //--CLIENT_CONTACT_EMAIL

    public boolean isClientContactEmailVisible() {
        return Rendering.isVisibleField("CLIENT_CONTACT_EMAIL");
    }

    public boolean isCLIENT_CONTACT_EMAIL_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_CONTACT_EMAIL");
    }

    public boolean isCLIENT_CONTACT_EMAIL_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_CONTACT_EMAIL");
    }

    public boolean isCLIENT_CONTACT_EMAIL_DISABLED() {
        return Rendering.isDisabledField("CLIENT_CONTACT_EMAIL");
    }

    public String getCLIENT_CONTACT_EMAIL_LABEL() {
        return Rendering.getFieldLabel("CLIENT_CONTACT_EMAIL");
    }

    //--CLIENT_FULLNAME

    public boolean isCLIENT_FULLNAME_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_FULLNAME");
    }

    public boolean isCLIENT_FULLNAME_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_FULLNAME");
    }

    public boolean isCLIENT_FULLNAME_DISABLED() {
        return Rendering.isDisabledField("CLIENT_FULLNAME");
    }

    public String getCLIENT_FULLNAME_LABEL() {
        return Rendering.getFieldLabel("CLIENT_FULLNAME");
    }
    //--CLIENT_SURNAME

    public boolean isCLIENT_SURNAME_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_SURNAME");
    }

    public boolean isCLIENT_SURNAME_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_SURNAME");
    }

    public boolean isCLIENT_SURNAME_DISABLED() {
        return Rendering.isDisabledField("CLIENT_SURNAME");
    }

    public String getCLIENT_SURNAME_LABEL() {
        return Rendering.getFieldLabel("CLIENT_SURNAME");
    }

    //--CLIENT_OTHER_NAMES

    public boolean isCLIENT_OTHER_NAMES_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_OTHER_NAMES");
    }

    public boolean isCLIENT_OTHER_NAMES_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_OTHER_NAMES");
    }

    public boolean isCLIENT_OTHER_NAMES_DISABLED() {
        return Rendering.isDisabledField("CLIENT_OTHER_NAMES");
    }

    public String getCLIENT_OTHER_NAMES_LABEL() {
        return Rendering.getFieldLabel("CLIENT_OTHER_NAMES");
    }


    public boolean isClientSectorVisible() {
        return Rendering.isVisibleField("CLIENT_SECTOR");
    }

    public boolean isClientSpecializationVisible() {
        return Rendering.isVisibleField("CLIENT_SPECIALIZATION");
    }

    public boolean isClientOccupationVisible() {
        return Rendering.isVisibleField("CLIENT_OCCUPATION");
    }

    public boolean isClientSectorDisabled() {
        return Rendering.isDisabledField("CLIENT_SECTOR");
    }

    public boolean isClientOccupationDisabled() {
        return Rendering.isDisabledField("CLIENT_OCCUPATION");
    }

    public boolean isClientSpecializationDisabled() {
        return Rendering.isDisabledField("CLIENT_SPECIALIZATION");
    }

    public boolean isClientSpecializationRequired() {
        return Rendering.isMandatoryField("CLIENT_SPECIALIZATION");
    }

    public boolean isClientSectorRequired() {
        return Rendering.isMandatoryField("CLIENT_SECTOR");
    }

    public boolean isClientOccupationRequired() {
        return Rendering.isMandatoryField("CLIENT_OCCUPATION");
    }

    public boolean isClientCellNosRequired() {
        return Rendering.isMandatoryField("CLIENT_CELLNOS");
    }

    public boolean isPinAndPassMandatory() {
        String val = (String)GlobalCC.getSysParamValue("PIN_AND_PASS_MAND");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isAGENT_POSTAL_ADDRESS_REQUIRED() {
        return Rendering.isMandatoryField("AGENT_POSTAL_ADDRESS");
    }

    public boolean isClientForVisible() {
        return Rendering.isVisibleField("CLIENT_FOR");
    }

    public boolean isClientForDisabled() {
        return Rendering.isDisabledField("CLIENT_FOR");
    }

    public void setClientForLMS(RichSelectBooleanCheckbox clientForLMS) {
        this.clientForLMS = clientForLMS;
    }

    public RichSelectBooleanCheckbox getClientForLMS() {
        return clientForLMS;
    }

    public void setClientForGIS(RichSelectBooleanCheckbox clientForGIS) {
        this.clientForGIS = clientForGIS;
    }

    public RichSelectBooleanCheckbox getClientForGIS() {
        return clientForGIS;
    }
    //--CLIENT_ANNIVERSARY

    public boolean isCLIENT_ANNIVERSARY_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_ANNIVERSARY");
    }

    public boolean isCLIENT_ANNIVERSARY_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_ANNIVERSARY");
    }

    public boolean isCLIENT_ANNIVERSARY_DISABLED() {
        return Rendering.isDisabledField("CLIENT_ANNIVERSARY");
    }

    public String getCLIENT_ANNIVERSARY_LABEL() {
        return Rendering.getFieldLabel("CLIENT_ANNIVERSARY");
    }
    //--CLIENT_DIVISION

    public boolean isCLIENT_DIVISION_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_DIVISION");
    }

    public boolean isCLIENT_DIVISION_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_DIVISION");
    }

    public boolean isCLIENT_DIVISION_DISABLED() {
        return Rendering.isDisabledField("CLIENT_DIVISION");
    }

    public String getCLIENT_DIVISION_LABEL() {
        return Rendering.getFieldLabel("CLIENT_DIVISION");
    }
    //----CLIENT_COUNTRY----

    public boolean isCLIENT_COUNTRY_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_COUNTRY");
    }

    public boolean isCLIENT_COUNTRY_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_COUNTRY");
    }

    public boolean isCLIENT_COUNTRY_DISABLED() {
        return Rendering.isDisabledField("CLIENT_COUNTRY");
    }

    public String getCLIENT_COUNTRY_LABEL() {
        return Rendering.getFieldLabel("CLIENT_COUNTRY");
    }
    //----CLIENT_ID_REG_NO----

    public boolean isCLIENT_ID_REG_NO_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_ID_REG_NO");
    }

    public boolean isCLIENT_ID_REG_NO_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_ID_REG_NO");
    }

    public boolean isCLIENT_ID_REG_NO_DISABLED() {
        return Rendering.isDisabledField("CLIENT_ID_REG_NO");
    }

    public String getCLIENT_ID_REG_NO_LABEL() {
        return Rendering.getFieldLabel("CLIENT_ID_REG_NO");
    }
    //----CLIENT_BRANCH----

    public boolean isCLIENT_BRANCH_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_BRANCH");
    }

    public boolean isCLIENT_BRANCH_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_BRANCH");
    }

    public boolean isCLIENT_BRANCH_DISABLED() {
        return Rendering.isDisabledField("CLIENT_BRANCH");
    }

    public String getCLIENT_BRANCH_LABEL() {
        return Rendering.getFieldLabel("CLIENT_BRANCH");
    }
    //----CLIENT_DL----

    public boolean isCLIENT_DL_ISSUE_DATE_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_DL_ISSUE_DATE");
    }

    public boolean isCLIENT_DL_ISSUE_DATE_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_DL_ISSUE_DATE");
    }

    public boolean isCLIENT_DL_ISSUE_DATE_DISABLED() {
        return Rendering.isDisabledField("CLIENT_DL_ISSUE_DATE");
    }

    public String getCLIENT_DL_ISSUE_DATE_LABEL() {
        return Rendering.getFieldLabel("CLIENT_DL_ISSUE_DATE");
    }
    //----CLIENT_EMAIL----

    public boolean isCLIENT_EMAIL_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_EMAIL");
    }

    public boolean isCLIENT_EMAIL_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_EMAIL");
    }

    public boolean isCLIENT_EMAIL_DISABLED() {
        return Rendering.isDisabledField("CLIENT_EMAIL");
    }

    public String getCLIENT_EMAIL_LABEL() {
        return Rendering.getFieldLabel("CLIENT_EMAIL");
    }
    //----CLIENT_PIN----

    public boolean isCLIENT_PIN_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_PIN");
    }


    public boolean isMANAGER_ALLOWED() {
        return Rendering.isMandatoryField("MANAGER_ALLOWED");
    }
    
    public boolean isCLIENT_PIN_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_PIN");
    }

    public boolean isCLIENT_PIN_DISABLED() {
        return Rendering.isDisabledField("CLIENT_PIN");
    }

    public String getCLIENT_PIN_LABEL() {
        return Rendering.getFieldLabel("CLIENT_PIN");
    }
    //----CLIENT_TOWN----

    public boolean isCLIENT_TOWN_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_TOWN");
    }

    public boolean isCLIENT_TOWN_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_TOWN");
    }

    public boolean isCLIENT_TOWN_DISABLED() {
        return Rendering.isDisabledField("CLIENT_TOWN");
    }

    public String getCLIENT_TOWN_LABEL() {
        return Rendering.getFieldLabel("CLIENT_TOWN");
    }
    //----CLIENT_STATE----

    public boolean isCLIENT_STATE_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_STATE");
    }

    public boolean isCLIENT_STATE_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_STATE");
    }

    public boolean isCLIENT_STATE_DISABLED() {
        return Rendering.isDisabledField("CLIENT_STATE");
    }

    public String getCLIENT_STATE_LABEL() {
        return Rendering.getFieldLabel("CLIENT_STATE");
    }
    //--Relationship Officer----

    public boolean isCLIENT_RELATIONSHIP_OFFICER_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_RELATIONSHIP_OFFICER");
    }

    public boolean isCLIENT_RELATIONSHIP_OFFICER_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_RELATIONSHIP_OFFICER");
    }

    public boolean isCLIENT_RELATIONSHIP_OFFICER_DISABLED() {
        return Rendering.isDisabledField("CLIENT_RELATIONSHIP_OFFICER");
    }

    public String getCLIENT_RELATIONSHIP_OFFICER_LABEL() {
        return Rendering.getFieldLabel("CLIENT_RELATIONSHIP_OFFICER");
    }
    //--DATE OF BIRTH

    public boolean isCLIENT_DOB_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_DOB");
    }

    public boolean isCLIENT_DOB_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_DOB");
    }

    public boolean isCLIENT_DOB_DISABLED() {
        return Rendering.isDisabledField("CLIENT_DOB");
    }

    public String getCLIENT_DOB_LABEL() {
        return Rendering.getFieldLabel("CLIENT_DOB");
    }
    //--CLIENT_OCCUPATION

    public boolean isCLIENT_OCCUPATION_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_OCCUPATION");
    }

    public boolean isCLIENT_OCCUPATION_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_OCCUPATION");
    }

    public boolean isCLIENT_OCCUPATION_DISABLED() {
        return Rendering.isDisabledField("CLIENT_OCCUPATION");
    }

    public String getCLIENT_OCCUPATION_LABEL() {
        return Rendering.getFieldLabel("CLIENT_OCCUPATION");
    }
    //--CLIENT_TELEPHONE

    public boolean isCLIENT_TELEPHONE_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_TELEPHONE");
    }

    public boolean isCLIENT_TELEPHONE_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_TELEPHONE");
    }

    public boolean isCLIENT_TELEPHONE_DISABLED() {
        return Rendering.isDisabledField("CLIENT_TELEPHONE");
    }

    public String getCLIENT_TELEPHONE_LABEL() {
        return Rendering.getFieldLabel("CLIENT_TELEPHONE");
    }
    //--CLIENT_SMS

    public boolean isCLIENT_SMS_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_SMS");
    }

    public boolean isCLIENT_SMS_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_SMS");
    }

    public boolean isCLIENT_SMS_DISABLED() {
        return Rendering.isDisabledField("CLIENT_SMS");
    }

    public String getCLIENT_SMS_LABEL() {
        return Rendering.getFieldLabel("CLIENT_SMS");
    }
    //--CLIENT_SMS_2

    public boolean isCLIENT_SMS_2_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_SMS_2");
    }

    public boolean isCLIENT_SMS_2_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_SMS_2");
    }

    public boolean isCLIENT_SMS_2_DISABLED() {
        return Rendering.isDisabledField("CLIENT_SMS_2");
    }

    public String getCLIENT_SMS_2_LABEL() {
        return Rendering.getFieldLabel("CLIENT_SMS_2");
    }
    //--CLIENT_PHYSICAL_ADDRESS

    public boolean isCLIENT_PHYSICAL_ADDRESS_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_PHYSICAL_ADDRESS");
    }

    public boolean isCLIENT_PHYSICAL_ADDRESS_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_PHYSICAL_ADDRESS");
    }

    public boolean isCLIENT_PHYSICAL_ADDRESS_DISABLED() {
        return Rendering.isDisabledField("CLIENT_PHYSICAL_ADDRESS");
    }

    public String getCLIENT_PHYSICAL_ADDRESS_LABEL() {
        return Rendering.getFieldLabel("CLIENT_PHYSICAL_ADDRESS");
    }
    //--CLIENT_CREDIT_RATING

    public boolean isCLIENT_CREDIT_RATING_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_CREDIT_RATING");
    }

    public boolean isCLIENT_CREDIT_RATING_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_CREDIT_RATING");
    }

    public boolean isCLIENT_CREDIT_RATING_DISABLED() {
        return Rendering.isDisabledField("CLIENT_CREDIT_RATING");
    }

    public String getCLIENT_CREDIT_RATING_LABEL() {
        return Rendering.getFieldLabel("CLIENT_CREDIT_RATING");
    }
    //--CLIENT_POSTAL_ADDRESS

    public boolean isCLIENT_POSTAL_ADDRESS_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_POSTAL_ADDRESS");
    }

    public boolean isCLIENT_WET_REQUIRED() {
        return Rendering.isMandatoryField("CLNT_WET");
    }

    public boolean isCLIENT_POSTAL_ADDRESS_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_POSTAL_ADDRESS");
    }

    public boolean isCLIENT_WET_VISIBLE() {
        return Rendering.isVisibleField("CLNT_WET");
    }


    public boolean isCLIENT_WET_DISABLED() {
        return Rendering.isDisabledField("CLNT_WET");
    }

    public boolean isCLIENT_POSTAL_ADDRESS_DISABLED() {
        return Rendering.isDisabledField("CLIENT_POSTAL_ADDRESS");
    }

    public String getCLIENT_POSTAL_ADDRESS_LABEL() {
        return Rendering.getFieldLabel("CLIENT_POSTAL_ADDRESS");
    }

    public String getCLIENT_WET_LABEL() {
        return Rendering.getFieldLabel("CLNT_WET");
    }

    //--CLIENT_SECTOR

    public boolean isCLIENT_SECTOR_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_SECTOR");
    }

    public boolean isCLIENT_SECTOR_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_SECTOR");
    }

    public boolean isCLIENT_SECTOR_DISABLED() {
        return Rendering.isDisabledField("CLIENT_SECTOR");
    }

    public String getCLIENT_SECTOR_LABEL() {
        return Rendering.getFieldLabel("CLIENT_SECTOR");
    }
    //--CLIENT_SPECIALIZATION

    public boolean isCLIENT_SPECIALIZATION_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_SPECIALIZATION");
    }

    public boolean isCLIENT_SPECIALIZATION_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_SPECIALIZATION");
    }

    public boolean isCLIENT_SPECIALIZATION_DISABLED() {
        return Rendering.isDisabledField("CLIENT_SPECIALIZATION");
    }

    public String getCLIENT_SPECIALIZATION_LABEL() {
        return Rendering.getFieldLabel("CLIENT_SPECIALIZATION");
    }
    //--CLIENT_PASSPORT

    public boolean isCLIENT_PASSPORT_NO_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_PASSPORT_NO");
    }

    public boolean isCLIENT_PASSPORT_NO_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_PASSPORT_NO");
    }

    public boolean isCLIENT_PASSPORT_NO_DISABLED() {
        return Rendering.isDisabledField("CLIENT_PASSPORT_NO");
    }

    public String getCLIENT_PASSPORT_NO_LABEL() {
        return Rendering.getFieldLabel("CLIENT_PASSPORT_NO");
    }
    //--CLIENT_CATEGORY

    public boolean isCLIENT_CATEGORY_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_CATEGORY");
    }

    public boolean isCLIENT_CATEGORY_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_CATEGORY");
    }

    public boolean isCLIENT_CATEGORY_DISABLED() {
        return Rendering.isDisabledField("CLIENT_CATEGORY");
    }

    public String getCLIENT_CATEGORY_LABEL() {
        return Rendering.getFieldLabel("CLIENT_CATEGORY");
    }

    //--CLIENT_GENDER

    public boolean isCLIENT_GENDER_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_GENDER");
    }

    public boolean isCLIENT_GENDER_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_GENDER");
    }

    public boolean isCLIENT_GENDER_DISABLED() {
        return Rendering.isDisabledField("CLIENT_GENDER");
    }

    public String getCLIENT_GENDER_LABEL() {
        return Rendering.getFieldLabel("CLIENT_GENDER");
    }

    //--USER_SUB_ACCOUNT_TYPE

    public boolean isUSER_SUB_ACCOUNT_TYPE_REQUIRED() {
        return Rendering.isMandatoryField("USER_SUB_ACCOUNT_TYPE");
    }

    public boolean isUSER_SUB_ACCOUNT_TYPE_VISIBLE() {
        return Rendering.isVisibleField("USER_SUB_ACCOUNT_TYPE");
    }

    public boolean isUSER_SUB_ACCOUNT_TYPE_DISABLED() {
        return Rendering.isDisabledField("USER_SUB_ACCOUNT_TYPE");
    }

    public String getUSER_SUB_ACCOUNT_TYPE_LABEL() {
        return Rendering.getFieldLabel("USER_SUB_ACCOUNT_TYPE");
    }
    //---AGENT_DIVISION_NAME---

    public boolean isAGENT_DIVISION_NAME_REQUIRED() {
        return Rendering.isMandatoryField("AGENT_DIVISION_NAME");
    }

    public boolean isAGENT_DIVISION_NAME_VISIBLE() {
        return Rendering.isVisibleField("AGENT_DIVISION_NAME");
    }

    public boolean isAGENT_DIVISION_NAME_DISABLED() {
        return Rendering.isDisabledField("AGENT_DIVISION_NAME");
    }

    public String getAGENT_DIVISION_NAME_LABEL() {
        return Rendering.getFieldLabel("AGENT_DIVISION_NAME");
    }
    //---AGENT_SBU---

    public boolean isAGENT_SBU_REQUIRED() {
        return Rendering.isMandatoryField("AGENT_SBU");
    }

    public boolean isAGENT_SBU_VISIBLE() {
        return Rendering.isVisibleField("AGENT_SBU");
    }

    public boolean isAGENT_SBU_DISABLED() {
        return Rendering.isDisabledField("AGENT_SBU");
    }

    public String getAGENT_SBU_LABEL() {
        return Rendering.getFieldLabel("AGENT_SBU");
    }

    //---AGENT_REGION---

    public boolean isAGENT_REGION_REQUIRED() {
        return Rendering.isMandatoryField("AGENT_REGION");
    }

    public boolean isAGENT_REGION_VISIBLE() {
        return Rendering.isVisibleField("AGENT_REGION");
    }

    public boolean isAGENT_REGION_DISABLED() {
        return Rendering.isDisabledField("AGENT_REGION");
    }

    public String getAGENT_REGION_LABEL() {
        return Rendering.getFieldLabel("AGENT_REGION");
    }
    //--BANK_BRANCH_EMAIL----------

    public boolean isBBR_EMAIL_REQUIRED() {
        return Rendering.isMandatoryField("BBR_EMAIL");
    }

    public boolean isBBR_EMAIL_VISIBLE() {
        return Rendering.isVisibleField("BBR_EMAIL");
    }

    public boolean isBBR_EMAIL_DISABLED() {
        return Rendering.isDisabledField("BBR_EMAIL");
    }

    public String getBBR_EMAIL_LABEL() {
        return Rendering.getFieldLabel("BBR_EMAIL");
    }
    //--BANK_BRANCH_PERSON_NAME---

    public boolean isBBR_PERSON_NAME_REQUIRED() {
        return Rendering.isMandatoryField("BBR_PERSON_NAME");
    }

    public boolean isBBR_PERSON_NAME_VISIBLE() {
        return Rendering.isVisibleField("BBR_PERSON_NAME");
    }

    public boolean isBBR_PERSON_NAME_DISABLED() {
        return Rendering.isDisabledField("BBR_PERSON_NAME");
    }

    public String getBBR_PERSON_NAME_LABEL() {
        return Rendering.getFieldLabel("BBR_PERSON_NAME");
    }
    //--BANK_BRANCH_CONTACT_PERSON_PHONE--

    public boolean isBBR_PERSON_PHONE_REQUIRED() {
        return Rendering.isMandatoryField("BBR_PERSON_PHONE");
    }

    public boolean isBBR_PERSON_PHONE_VISIBLE() {
        return Rendering.isVisibleField("BBR_PERSON_PHONE");
    }

    public boolean isBBR_PERSON_PHONE_DISABLED() {
        return Rendering.isDisabledField("BBR_PERSON_PHONE");
    }

    public String getBBR_PERSON_PHONE_LABEL() {
        return Rendering.getFieldLabel("BBR_PERSON_PHONE");
    }
    //--BANK_BRANCH_CONTACT_PERSON_EMAIL--

    public boolean isBBR_PERSON_EMAIL_REQUIRED() {
        return Rendering.isMandatoryField("BBR_PERSON_EMAIL");
    }

    public boolean isBBR_PERSON_EMAIL_VISIBLE() {
        return Rendering.isVisibleField("BBR_PERSON_EMAIL");
    }

    public boolean isBBR_PERSON_EMAIL_DISABLED() {
        return Rendering.isDisabledField("BBR_PERSON_EMAIL");
    }

    public String getBBR_PERSON_EMAIL_LABEL() {
        return Rendering.getFieldLabel("BBR_PERSON_EMAIL");
    }
    //--REGION_UNIT_DEPEDENCY-----

    public boolean isREGION_UNIT_DEPEDENCY() {
        String val =
            (String)GlobalCC.getSysParamValue("REGION_UNIT_DEPENDENCY");
        return ("Y".equalsIgnoreCase(val));
    }
    // Organization Transfers

    public boolean isREGION_TRANSFER() {
        String val = (String)session.getAttribute("tt_trans_type");
        return ("ROT".equalsIgnoreCase(val));
    }

    public boolean isBRANCH_TRANSFER() {
        String val = (String)session.getAttribute("tt_trans_type");
        return ("BRT".equalsIgnoreCase(val));
    }

    public boolean isAGENCY_TRANSFER() {
        String val = (String)session.getAttribute("tt_trans_type");
        return ("ABT".equalsIgnoreCase(val));
    }

    public boolean isUNIT_TRANSFER() {
        String val = (String)session.getAttribute("tt_trans_type");
        return ("UAT".equalsIgnoreCase(val));
    }

    public boolean isAGENT_TRANSFER() {
        String val = (String)session.getAttribute("tt_trans_type");
        return ("AUT".equalsIgnoreCase(val));
    }

    public String getTRANSFER_TYPE_TITLE() {
        String val = (String)session.getAttribute("tt_trans_type");
        if ("ROT".equalsIgnoreCase(val)) {
            return "Organization";
        }
        if ("BRT".equalsIgnoreCase(val)) {
            return "Region";
        }
        if ("ABT".equalsIgnoreCase(val)) {
            return "Branch";
        }
        if ("UAT".equalsIgnoreCase(val)) {
            return "Agency";
        }
        if ("AUT".equalsIgnoreCase(val)) {
            return "Unit";
        }
        if ("UBT".equalsIgnoreCase(val)) {
            return "Branch";
        }
        return null;
    }

    public String getTRANSFER_ITEM_TYPE_TITLE() {
        String val = (String)session.getAttribute("tt_trans_type");

        if ("ROT".equalsIgnoreCase(val)) {
            return "Region";
        }
        if ("BRT".equalsIgnoreCase(val)) {
            return "Branch";
        }
        if ("ABT".equalsIgnoreCase(val)) {
            return "Agency";
        }
        if ("UAT".equalsIgnoreCase(val)) {
            return "Unit";
        }
        if ("AUT".equalsIgnoreCase(val)) {
            return "Agent";
        }
        if ("UBT".equalsIgnoreCase(val)) {
            return "Unit";
        }
        return null;
    }

    public boolean isTRANSFER_ITEM_FROM_VISIBLE() {
        String val = (String)session.getAttribute("tt_trans_type");

        if ("ROT".equalsIgnoreCase(val)) {
            return true;
        }
        if ("BRT".equalsIgnoreCase(val)) {
            return true;
        }
        if ("ABT".equalsIgnoreCase(val)) {
            String v =
                (String)GlobalCC.getSysParamValue("TRX_BRANCHES_FETCH_ALL");
            return !("Y".equalsIgnoreCase(v));
        }
        if ("UAT".equalsIgnoreCase(val)) {
            return true;
        }
        if ("AUT".equalsIgnoreCase(val)) {
            return true;
        }
        
        if ("UBT".equalsIgnoreCase(val)) {
            return true;
        }
        return true;
    }

    public boolean isORG_BANK_ACCOUNT_APP() {
        String val = (String)GlobalCC.getSysParamValue("ORG_BANK_ACCOUNT_APP");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isUSER_MAKER_CHECKER_APP() {
        String val =
            (String)GlobalCC.getSysParamValue("USER_MAKER_CHECKER_APP");
        return ("Y".equalsIgnoreCase(val));
    }
    //--AGENT_TAX_AUTHORITY--

    public boolean isAGENT_TAX_AUTHORITY_REQUIRED() {
        return Rendering.isMandatoryField("AGENT_TAX_AUTHORITY");
    }

    public boolean isAGENT_TAX_AUTHORITY_VISIBLE() {
        return Rendering.isVisibleField("AGENT_TAX_AUTHORITY");
    }

    public boolean isAGENT_TAX_AUTHORITY_DISABLED() {
        return Rendering.isDisabledField("AGENT_TAX_AUTHORITY");
    }

    public String getAGENT_TAX_AUTHORITY_LABEL() {
        return Rendering.getFieldLabel("AGENT_TAX_AUTHORITY");
    }

    public boolean isDomicileCountryVisible() {
        return Rendering.isVisibleField("CLIENT_DOMICILE_COUNTRY");
    }

    public boolean isCheckedByComplianceOfficerVisible() {
        return Rendering.isVisibleField("CLIENT_CHECKED_BY_OFFICER");
    }
    //--CLIENT_DOMICILE_COUNTRY--

    public boolean isCLIENT_DOMICILE_COUNTRY_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_DOMICILE_COUNTRY");
    }

    public boolean isCLIENT_DOMICILE_COUNTRY_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_DOMICILE_COUNTRY");
    }

    public boolean isCLIENT_DOMICILE_COUNTRY_DISABLED() {
        return Rendering.isDisabledField("CLIENT_DOMICILE_COUNTRY");
    }

    public String getCLIENT_DOMICILE_COUNTRY_LABEL() {
        return Rendering.getFieldLabel("CLIENT_DOMICILE_COUNTRY");
    }

    //--CLIENT_CHECKED_BY_OFFICER--

    public boolean isCLIENT_CHECKED_BY_OFFICER_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_CHECKED_BY_OFFICER");
    }

    public boolean isCLIENT_CHECKED_BY_OFFICER_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_CHECKED_BY_OFFICER");
    }

    public boolean isCLIENT_CHECKED_BY_OFFICER_DISABLED() {
        return Rendering.isDisabledField("CLIENT_CHECKED_BY_OFFICER");
    }

    public String getCLIENT_CHECKED_BY_OFFICER_LABEL() {
        return Rendering.getFieldLabel("CLIENT_CHECKED_BY_OFFICER");
    }


    //--TRX_BRANCHES_FETCH_ALL-----

    public boolean isTRX_BRANCHES_FETCH_ALL() {
        String val =
            (String)GlobalCC.getSysParamValue("TRX_BRANCHES_FETCH_ALL");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isROLE_MAKER_CHECKER_APP() {
        String val =
            (String)GlobalCC.getSysParamValue("ROLE_MAKER_CHECKER_APP");
        return ("Y".equalsIgnoreCase(val));
    }

    //--AGENT_RELATIONSHIP_OFFICER

    public boolean isAGENT_RELATIONSHIP_OFFICER_REQUIRED() {
        int accountType;
        if (session.getAttribute("accountTypeCode") != null) {
            accountType =
                    Integer.parseInt(session.getAttribute("accountTypeCode").toString());
        } else {
            accountType = 0;
        }
        boolean relation =
            Rendering.isMandatoryField("AGENT_RELATIONSHIP_OFFICER");
        if (relation &&
            (accountType == 2 || accountType == 3 || accountType == 16)) {
            return true;
        }
        return false;
    }

    public boolean isAGENT_RELATIONSHIP_OFFICER_VISIBLE() {
        return Rendering.isVisibleField("AGENT_RELATIONSHIP_OFFICER");
    }

    public boolean isAGENT_RELATIONSHIP_OFFICER_DISABLED() {
        return Rendering.isDisabledField("AGENT_RELATIONSHIP_OFFICER");
    }

    public String getAGENT_RELATIONSHIP_OFFICER_LABEL() {
        return Rendering.getFieldLabel("AGENT_RELATIONSHIP_OFFICER");
    }
    //--AGENT_DATE_OF_BIRTH--

    public boolean isAGENT_DATE_OF_BIRTH_REQUIRED() {
        return Rendering.isMandatoryField("AGENT_DATE_OF_BIRTH");
    }

    public boolean isAGENT_DATE_OF_BIRTH_VISIBLE() {
        return Rendering.isVisibleField("AGENT_DATE_OF_BIRTH");
    }

    public boolean isAGENT_DATE_OF_BIRTH_DISABLED() {
        return Rendering.isDisabledField("AGENT_DATE_OF_BIRTH");
    }

    public String getAGENT_DATE_OF_BIRTH_LABEL() {
        return Rendering.getFieldLabel("AGENT_DATE_OF_BIRTH");
    }
    //--CLIENT_POSTAL_CODE--

    public boolean isCLIENT_POSTAL_CODE_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_POSTAL_CODE");
    }

    public boolean isCLIENT_POSTAL_CODE_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_POSTAL_CODE");
    }

    public boolean isCLIENT_POSTAL_CODE_DISABLED() {
        return Rendering.isDisabledField("CLIENT_POSTAL_CODE");
    }

    public String getCLIENT_POSTAL_CODE_LABEL() {
        return Rendering.getFieldLabel("CLIENT_POSTAL_CODE");
    }
    //----BRANCHES_REF------

    public boolean isBRANCHES_REF_REQUIRED() {
        return Rendering.isMandatoryField("BRANCHES_REF");
    }

    public boolean isBRANCHES_REF_VISIBLE() {
        return Rendering.isVisibleField("BRANCHES_REF");
    }

    public boolean isBRANCHES_REF_DISABLED() {
        return Rendering.isDisabledField("BRANCHES_REF");
    }

    public String getBRANCHES_REF_LABEL() {
        return Rendering.getFieldLabel("BRANCHES_REF");
    }
    //--DD_INSTALL_DATE--

    public boolean isDD_INSTALL_DATE_REQUIRED() {
        return Rendering.isMandatoryField("DD_INSTALL_DATE");
    }

    public boolean isDD_INSTALL_DATE_VISIBLE() {
        return Rendering.isVisibleField("DD_INSTALL_DATE");
    }

    public boolean isDD_INSTALL_DATE_DISABLED() {
        return Rendering.isDisabledField("DD_INSTALL_DATE");
    }

    public String getDD_INSTALL_DATE_LABEL() {
        return Rendering.getFieldLabel("DD_INSTALL_DATE");
    }
    //--AGENT_PIN--

    public boolean isAGENT_PIN_REQUIRED() {
        return Rendering.isMandatoryField("AGENT_PIN");
    }

    public boolean isAGENT_PIN_VISIBLE() {
        return Rendering.isVisibleField("AGENT_PIN");
    }

    public boolean isAGENT_PIN_DISABLED() {
        return Rendering.isDisabledField("AGENT_PIN");
    }

    public String getAGENT_PIN_LABEL() {
        return Rendering.getFieldLabel("AGENT_PIN");
    }
    //--CLIENT_BANK_ACCOUNT--

    public boolean isCLIENT_BANK_ACCOUNT_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_BANK_ACCOUNT");
    }

    public boolean isCLIENT_BANK_ACCOUNT_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_BANK_ACCOUNT");
    }

    public boolean isCLIENT_BANK_ACCOUNT_DISABLED() {
        return Rendering.isDisabledField("CLIENT_BANK_ACCOUNT");
    }

    public String getCLIENT_BANK_ACCOUNT_LABEL() {
        return Rendering.getFieldLabel("CLIENT_BANK_ACCOUNT");
    }
    //--CLIENT_BANK_ACCOUNT_NO--

    public boolean isCLIENT_BANK_ACCOUNT_NO_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_BANK_ACCOUNT_NO");
    }

    public boolean isCLIENT_BANK_ACCOUNT_NO_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_BANK_ACCOUNT_NO");
    }

    public boolean isCLIENT_BANK_ACCOUNT_NO_DISABLED() {
        return Rendering.isDisabledField("CLIENT_BANK_ACCOUNT_NO");
    }

    public String getCLIENT_BANK_ACCOUNT_NO_LABEL() {
        return Rendering.getFieldLabel("CLIENT_BANK_ACCOUNT_NO");
    }
    //--CLIENT_BANK_BRANCH--

    public boolean isCLIENT_BANK_BRANCH_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_BANK_BRANCH");
    }

    public boolean isCLIENT_BANK_BRANCH_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_BANK_BRANCH");
    }

    public boolean isCLIENT_BANK_BRANCH_DISABLED() {
        return Rendering.isDisabledField("CLIENT_BANK_BRANCH");
    }

    public String getCLIENT_BANK_BRANCH_LABEL() {
        return Rendering.getFieldLabel("CLIENT_BANK_BRANCH");
    }

    public boolean isMultiBankAccPerClientApp() {
        String val =
            (String)GlobalCC.getSysParamValue("MULTI_BANK_ACC_PER_CLIENT");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isMultiBankAccPerAccTypeApp() {
        String val =
            (String)GlobalCC.getSysParamValue("MULTI_BANK_ACC_PER_ACC_TYPE");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isMultiBankAccPerSpApp() {
        String val =
            (String)GlobalCC.getSysParamValue("MULTI_BANK_ACC_PER_SP");
        return ("Y".equalsIgnoreCase(val));
    }
    
    // --- MULTI_EMPLOYER
    public boolean isMultiEmployerPerClientApp(){
        String val = 
            GlobalCC.getSysParamValue("MULTI_EMPLOYER_PER_CLIENT");
        return "Y".equalsIgnoreCase(val);
    }

    //--AGENT_IBAN--

    public boolean isAGENT_IBAN_REQUIRED() {
        return Rendering.isMandatoryField("AGENT_IBAN");
    }

    public boolean isAGENT_IBAN_VISIBLE() {
        return Rendering.isVisibleField("AGENT_IBAN");
    }

    public boolean isAGENT_IBAN_DISABLED() {
        return Rendering.isDisabledField("AGENT_IBAN");
    }

    public String getAGENT_IBAN_LABEL() {
        return Rendering.getFieldLabel("AGENT_IBAN");
    }
    //--SP_IBAN--

    public boolean isSP_IBAN_REQUIRED() {
        return Rendering.isMandatoryField("SP_IBAN");
    }

    public boolean isSP_IBAN_VISIBLE() {
        return Rendering.isVisibleField("SP_IBAN");
    }

    public boolean isSP_IBAN_DISABLED() {
        return Rendering.isDisabledField("SP_IBAN");
    }

    public String getSP_IBAN_LABEL() {
        return Rendering.getFieldLabel("SP_IBAN");
    }
    //--CLIENT_IBAN--

    public boolean isCLIENT_IBAN_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_IBAN");
    }

    public boolean isCLIENT_IBAN_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_IBAN");
    }

    public boolean isCLIENT_IBAN_DISABLED() {
        return Rendering.isDisabledField("CLIENT_IBAN");
    }

    public String getCLIENT_IBAN_LABEL() {
        return Rendering.getFieldLabel("CLIENT_IBAN");
    }
    //--SP_PIN--

    public boolean isSP_PIN_REQUIRED() {
        return Rendering.isMandatoryField("SP_PIN");
    }
    
    public boolean isSP_PIN_VISIBLE() {
        return Rendering.isVisibleField("SP_PIN");
    }
    
    //bill
    public boolean isSHT_DESC_REQUIRED() {
        return Rendering.isMandatoryField("SHT_DESC");
    }
    
    
    public boolean isSHT_DESC_DISABLED() {
        return Rendering.isDisabledField("SHT_DESC");
    }
    
    
    public boolean isSHT_DESCC_REQUIRED() {
        return Rendering.isMandatoryField("SHT_DESCC");
    }
    
    
    public boolean isSHT_DESCC_DISABLED() {
        return Rendering.isDisabledField("SHT_DESCC");
    }

   

    public boolean isSP_PIN_DISABLED() {
        return Rendering.isDisabledField("SP_PIN");
    }

    public String getSP_PIN_LABEL() {
        return Rendering.getFieldLabel("SP_PIN");
    }
    //--CLIENT_CELLNOS--

    public boolean isCLIENT_CELLNOS_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_CELLNOS");
    }

    public boolean isCLIENT_CELLNOS_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_CELLNOS");
    }

    public boolean isCLIENT_CELLNOS_DISABLED() {
        return Rendering.isDisabledField("CLIENT_CELLNOS");
    }

    public String getCLIENT_CELLNOS_LABEL() {
        return Rendering.getFieldLabel("CLIENT_CELLNOS");
    }
    //--CLIENT_INT_TELNO--

    public boolean isCLIENT_INT_TELNO_REQUIRED() {
        return Rendering.isMandatoryField("CLIENT_INT_TELNO");
    }

    public boolean isCLIENT_INT_TELNO_VISIBLE() {
        return Rendering.isVisibleField("CLIENT_INT_TELNO");
    }

    public boolean isCLIENT_INT_TELNO_DISABLED() {
        return Rendering.isDisabledField("CLIENT_INT_TELNO");
    }

    public String getCLIENT_INT_TELNO_LABEL() {
        return Rendering.getFieldLabel("CLIENT_INT_TELNO");
    }
    //--BNK_ACC_MAX_NO--

    public boolean isBNK_ACC_MAX_NO_REQUIRED() {
        return Rendering.isMandatoryField("BNK_ACC_MAX_NO");
    }

    public boolean isBNK_ACC_MAX_NO_VISIBLE() {
        return Rendering.isVisibleField("BNK_ACC_MAX_NO");
    }

    public boolean isBNK_ACC_MAX_NO_DISABLED() {
        return Rendering.isDisabledField("BNK_ACC_MAX_NO");
    }

    public String getBNK_ACC_MAX_NO_LABEL() {
        return Rendering.getFieldLabel("BNK_ACC_MAX_NO");
    }
    //--BNK_ACC_MIN_NO--

    public boolean isBNK_ACC_MIN_NO_REQUIRED() {
        return Rendering.isMandatoryField("BNK_ACC_MIN_NO");
    }

    public boolean isBNK_ACC_MIN_NO_VISIBLE() {
        return Rendering.isVisibleField("BNK_ACC_MIN_NO");
    }

    public boolean isBNK_ACC_MIN_NO_DISABLED() {
        return Rendering.isDisabledField("BNK_ACC_MIN_NO");
    }

    public String getBNK_ACC_MIN_NO_LABEL() {
        return Rendering.getFieldLabel("BNK_ACC_MIN_NO");
    }
    //--BNK_ACC_NO--

    public boolean isBNK_ACC_NO_REQUIRED() {
        return Rendering.isMandatoryField("BNK_ACC_NO");
    }

    public boolean isBNK_ACC_NO_VISIBLE() {
        return Rendering.isVisibleField("BNK_ACC_NO");
    }

    public boolean isBNK_ACC_NO_DISABLED() {
        return Rendering.isDisabledField("BNK_ACC_NO");
    }

    public String getBNK_ACC_NO_LABEL() {
        return Rendering.getFieldLabel("BNK_ACC_NO");
    }
    //--BNK_COU_CODE--

    public boolean isBNK_COU_CODE_REQUIRED() {
        return Rendering.isMandatoryField("BNK_COU_CODE");
    }

    public boolean isBNK_COU_CODE_VISIBLE() {
        return Rendering.isVisibleField("BNK_COU_CODE");
    }

    public boolean isBNK_COU_CODE_DISABLED() {
        return Rendering.isDisabledField("BNK_COU_CODE");
    }

    public String getBNK_COU_CODE_LABEL() {
        return Rendering.getFieldLabel("BNK_COU_CODE");
    }
    //--SERVICE_POLICY_NO--

    public boolean isSERVICE_POLICY_NO_REQUIRED() {
        return Rendering.isMandatoryField("SERVICE_POLICY_NO");
    }

    public boolean isSERVICE_POLICY_NO_VISIBLE() {
        return Rendering.isVisibleField("SERVICE_POLICY_NO");
    }

    public boolean isSERVICE_POLICY_NO_DISABLED() {
        return Rendering.isDisabledField("SERVICE_POLICY_NO");
    }

    public String getSERVICE_POLICY_NO_LABEL() {
        return Rendering.getFieldLabel("SERVICE_POLICY_NO");
    }
    //--SERVICE_RECEIVE_DATE--

    public boolean isSERVICE_RECEIVE_DATE_REQUIRED() {
        return Rendering.isMandatoryField("SERVICE_RECEIVE_DATE");
    }

    public boolean isSERVICE_RECEIVE_DATE_VISIBLE() {
        return Rendering.isVisibleField("SERVICE_RECEIVE_DATE");
    }

    public boolean isSERVICE_RECEIVE_DATE_DISABLED() {
        return Rendering.isDisabledField("SERVICE_RECEIVE_DATE");
    }

    public String getSERVICE_RECEIVE_DATE_LABEL() {
        return Rendering.getFieldLabel("SERVICE_RECEIVE_DATE");
    }
    //--AGENT_EMAIL--

    public boolean isAGENT_EMAIL_REQUIRED() {
        return Rendering.isMandatoryField("AGENT_EMAIL");
    }

    public boolean isAGENT_EMAIL_VISIBLE() {
        return Rendering.isVisibleField("AGENT_EMAIL");
    }

    public boolean isAGENT_EMAIL_DISABLED() {
        return Rendering.isDisabledField("AGENT_EMAIL");
    }

    public String getAGENT_EMAIL_LABEL() {
        return Rendering.getFieldLabel("AGENT_EMAIL");
    }

    public boolean isClientFullNameVisible() {
        return Rendering.isVisibleField("CLIENT_FULLNAME");
    }

    public boolean isClientSurNameVisible() {
        return Rendering.isVisibleField("CLIENT_SURNAME");
    }

    public boolean isClientOtherNamesVisible() {
        return Rendering.isVisibleField("CLIENT_OTHER_NAMES");
    }

    public boolean isClientFullNameDisabled() {
        return Rendering.isDisabledField("CLIENT_FULLNAME");
    }

    public boolean isClientSurNameDisabled() {
        return Rendering.isDisabledField("CLIENT_SURNAME");
    }

    public boolean isClientOtherNamesDisabled() {
        return Rendering.isDisabledField("CLIENT_OTHER_NAMES");
    }

    public boolean isPIN_OR_PASSPORT_MAND() {
        String val = GlobalCC.getSysParamValue((String)"PIN_OR_PASSPORT_MAND");
        return "Y".equalsIgnoreCase(val);
    }

    public boolean isShowSystemLabels() {
        String val = GlobalCC.getSysParamValue((String)"SHOW_SYSTEM_LABELS");
        return "Y".equalsIgnoreCase(val);
    }

    public String getADMIN_AUTO_RESET_PWD() {
        String val = (String)GlobalCC.getSysParamValue("ADMIN_AUTO_RESET_PWD");
        return val;
    }

    public String getADMIN_AUTO_CREATE_PWD() {
        String val =
            (String)GlobalCC.getSysParamValue("ADMIN_AUTO_CREATE_PWD");
        return val;
    }

    public boolean isUSER_PWD_DISABLED() {
        String reset =
            (String)GlobalCC.getSysParamValue("ADMIN_AUTO_RESET_PWD");
        String mode = (String)GlobalCC.getSysParamValue("SIGN_IN_MODE");

        return "LDAP".equalsIgnoreCase(mode) || "Y".equalsIgnoreCase(reset);


    }


    public boolean isShowEntityScreen() {
        String val =
            GlobalCC.checkNullValues(session.getAttribute("returnPage"));

        if (val == null) {

            return false;
        }

        if ("showEntities".equalsIgnoreCase(val)) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isTICKETS_APP() {
        String val = (String)GlobalCC.getSysParamValue("TICKETS_APP");
        return ("Y".equalsIgnoreCase(val));
    }

    public boolean isSERVICE_REQ_APP() {
        String val = (String)GlobalCC.getSysParamValue("SERVICE_REQ_APP");
        return ("Y".equalsIgnoreCase(val));
    }


    public String getCLIENT_WEF_LABEL() {
        return Rendering.getFieldLabel("CLNT_WEF");
    }

    public boolean isCLIENT_WEF_REQUIRED() {
        return Rendering.isMandatoryField("CLNT_WEF");
    }


    public boolean isCLIENT_WEF_VISIBLE() {
        return Rendering.isVisibleField("CLNT_WEF");
    }


    public boolean isCLIENT_WEF_DISABLED() {
        return Rendering.isDisabledField("CLNT_WEF");
    }


    public String getCLNT_FAX_LABEL() {
        return Rendering.getFieldLabel("CLNT_FAX");
    }

    public boolean isCLNT_FAX_REQUIRED() {
        return Rendering.isMandatoryField("CLNT_FAX");
    }


    public boolean isCLNT_FAX_VISIBLE() {
        return Rendering.isVisibleField("CLNT_FAX");
    }


    public boolean isCLNT_FAX_DISABLED() {
        return Rendering.isDisabledField("CLNT_FAX");
    }


    public String getCLNT_PROJECTED_BUSINESS_LABEL() {
        return Rendering.getFieldLabel("CLNT_PROJECTED_BUSINESS");
    }

    public boolean isCLNT_PROJECTED_BUSINESS_REQUIRED() {
        return Rendering.isMandatoryField("CLNT_PROJECTED_BUSINESS");
    }


    public boolean isCLNT_PROJECTED_BUSINESS_VISIBLE() {
        return Rendering.isVisibleField("CLNT_PROJECTED_BUSINESS");
    }


    public boolean isCLNT_PROJECTED_BUSINESS_DISABLED() {
        return Rendering.isDisabledField("CLNT_PROJECTED_BUSINESS");
    }


    public String getCLNT_DOMICILE_COUNTRIES_LABEL() {
        return Rendering.getFieldLabel("CLNT_DOMICILE_COUNTRIES");
    }

    public boolean isCLNT_DOMICILE_COUNTRIES_REQUIRED() {
        return Rendering.isMandatoryField("CLNT_DOMICILE_COUNTRIES");
    }


    public boolean isCLNT_DOMICILE_COUNTRIES_VISIBLE() {
        return Rendering.isVisibleField("CLNT_DOMICILE_COUNTRIES");
    }


    public boolean isCLNT_DOMICILE_COUNTRIES_DISABLED() {
        return Rendering.isDisabledField("CLNT_DOMICILE_COUNTRIES");
    }

    //----CLIENT_COUNTRY----

    public boolean isCLIENT_TITLE_REQUIRED() {
        return Rendering.isMandatoryField("CLNT_TITLE");
    }

    public boolean isCLIENT_TITLE_VISIBLE() {
        return Rendering.isVisibleField("CLNT_TITLE");
    }

    public boolean isCLIENT_TITLE_DISABLED() {
        return Rendering.isDisabledField("CLNT_TITLE");
    }

    public String getCLIENT_TITLE_LABEL() {
        return Rendering.getFieldLabel("CLNT_TITLE");
    }


    //----CLIENT_COUNTRY----

    public boolean isCLNT_OLD_ACCNT_NO_REQUIRED() {
        return Rendering.isMandatoryField("CLNT_OLD_ACCNT_NO");
    }

    public boolean isCLNT_OLD_ACCNT_NO_VISIBLE() {
        return Rendering.isVisibleField("CLNT_OLD_ACCNT_NO");
    }

    public boolean isCLNT_OLD_ACCNT_NO_DISABLED() {
        return Rendering.isDisabledField("CLNT_OLD_ACCNT_NO");
    }

    public String getCLNT_OLD_ACCNT_NO_LABEL() {
        return Rendering.getFieldLabel("CLNT_OLD_ACCNT_NO");
    }



    //-- Render SystemProduct on the MessagesTemplate --

    public boolean isSystemProductRendered() {
        String sysShortDesc = (String)session.getAttribute("sysShortDesc");
        String sysCode = (String)session.getAttribute("sysCode");

        return (sysShortDesc.equalsIgnoreCase("LMS") &&
                sysCode.equals("27")) ||
            (sysShortDesc.equalsIgnoreCase("GIS") && sysCode.equals("37"));
    }
    
    
    public boolean isClientsBankAccountsDisabled() {
        
        Authorization auth = new Authorization();
        String process = "CLNT";
        String processArea = "CLNTCBA";
        String processSubArea = "CLNTCBAA";
        String AccessGranted =auth.checkUserRights(process, processArea, processSubArea, null,null);
        boolean returnValue=false;
        
        if (AccessGranted.equalsIgnoreCase("Y") != true) {
          
          returnValue= false;
        
        }else{
        
      returnValue= true;
            
        }
        
        return returnValue;
    }

    public boolean isAnnotatedDoc() {
        return "Y".equals(GlobalCC.getSysParamValue("DMS_ANNOTATED_DOC")); 
    }
    public boolean isIprsApp() {
        String val = GlobalCC.getSysParamValue("IPRS_APP");
        return "Y".equals(val);
    }
}
