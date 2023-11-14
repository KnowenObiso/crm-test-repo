package TurnQuest.view.Login;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.ISession;
import TurnQuest.view.Base.Util;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.ldap.ActiveDirectory;
import TurnQuest.view.models.Branch;

import java.io.File;
import java.io.FileOutputStream;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import java.util.Random;

import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.nav.RichCommandLink;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.commons.dbutils.DbUtils;


public class Index {


    private RichInputText sysUsername;
    private RichInputText sysPassword;
    private RichOutputLabel resetLabel;
    private RichInputText resetPass;
    private RichOutputLabel confirmLabel;
    private RichInputText confirmPass;
    private RichCommandButton logBtn;
    private RichCommandButton rstBtn;
    private RichOutputLabel passLabel;
    private RichCommandButton cnclBtn;

    private RichInputText password;
    private RichInputText username;
    private RichSelectOneChoice txtSecurityQuestions;
    private RichInputText txtSecurityAnswer;
    private RichOutputLabel txtSecuritylabel;
    private RichOutputLabel txtSecurityAnswerLabel;
    private RichCommandButton txtCheck;
    private RichCommandLink forgotPass;

    private RichOutputLabel verifCodeLabel;
    private RichInputText verifCode;
    private RichOutputLabel usernameLabel;
    private RichCommandButton subVerifCode;
    String vName = null;

    public Index() {


    }

    public Map checkUserSystem(String username) {
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        Map map = new HashMap();
        conn = datahandler.getDatabaseConnection();
        int rset = 0;
        OracleCallableStatement callStmt2 = null;
        String login_message = null;
        try {
            callStmt2 =
                    (OracleCallableStatement)conn.prepareCall("begin ? := tqc_web_pkg.user_sys_authenticate(?,?,?,?,?,?); end;");

            // LDAPConnector ldap = new LDAPConnector();
            // success =ldap.userAuthenticate(login_username, login_password);
            //  if ( success!=null){
            //  if (success.equalsIgnoreCase("success")) {
            callStmt2.setString(2, username.toUpperCase());
            callStmt2.registerOutParameter(3, OracleTypes.VARCHAR); //Error Msg
            callStmt2.registerOutParameter(4,
                                           OracleTypes.DATE); //Last login Return Value
            callStmt2.registerOutParameter(5,
                                           OracleTypes.INTEGER); // UserCode Return value
            callStmt2.registerOutParameter(6,
                                           OracleTypes.VARCHAR); //UserName Return Value
            callStmt2.registerOutParameter(1,
                                           OracleTypes.INTEGER); //Return value
            callStmt2.setInt(7, GlobalCC.sysCode);
            callStmt2.execute();
            rset = callStmt2.getInt(1);
            map.put("rset", rset);
            login_message = callStmt2.getString(3);
            map.put("login_message", login_message);
            conn.commit();
            conn.close();
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;
        }
    }


    public String CreateAlert(String jobName) {
        String success = null;
        OracleConnection conn = null;
        try {

            conn = new DBConnector().getDatabaseConnection();
            CallableStatement cst3 = null;
            String Complete =
                "BEGIN tqc_setups_pkg.create_alerts(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);END;";
            cst3 = conn.prepareCall(Complete);
            cst3.setString(1, jobName);
            cst3.setString(2, jobName);

            cst3.setTimestamp(3, new Timestamp(new Date().getTime()));
            cst3.setTimestamp(4, null);
            cst3.setString(5, null);
            cst3.setObject(6, 0);
            cst3.setString(7, null);
            cst3.setString(8, null);
            cst3.setString(9, null);
            cst3.setString(10, null);

            cst3.setString(11, "A");
            cst3.setString(12, null); //(String)txtJobTemplate.getValue());
            cst3.setString(13, null);
            cst3.setString(14, null);
            cst3.setString(15, "A");
            cst3.setString(16, null);
            cst3.setString(17, "I");
            cst3.setString(18, null);
            cst3.setString(19, null);
            cst3.setString(20, null);
            cst3.execute();
            cst3.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }


    protected String getSaltString() {
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 5) {
            int index = (int)(rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    public String verifyConfirmationCode() {


        String query = "SELECT USR_NAME FROM TQC_USERS WHERE USR_SMS_CODE =?";
        OracleCallableStatement cst = null;

        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        try {

            conn = datahandler.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setString(1, verifCode.getValue().toString());
            OracleResultSet rs = (OracleResultSet)cst.executeQuery();


            if (rs.next()) {
                vName = rs.getString(1);


                if (vName != null) {

                    GlobalCC.errorValueNotEntered("Please reset your password::");
                    resetLabel.setRendered(true); //resetLabel.setRendered(true);
                    resetPass.setRendered(true); //resetPass.setRendered(true);
                    confirmLabel.setRendered(true); //confirmLabel.setRendered(true);
                    confirmPass.setRendered(true); //confirmPass.setRendered(true);
                    passLabel.setValue("Old Password");
                    passLabel.setRendered(true);
                    sysPassword.setRendered(true);
                    logBtn.setRendered(false); //logBtn.setRendered(false);
                    rstBtn.setRendered(true);
                    cnclBtn.setRendered(true); //cnclBtn.setRendered(true);
                    txtCheck.setRendered(false);


                    verifCodeLabel.setRendered(false);
                    verifCode.setRendered(false);
                    subVerifCode.setRendered(false);
                }

            } else {
                GlobalCC.errorValueNotEntered("Invalid verification code," +
                                              "\n" +
                        "Check your SMS/Email!");
            }
        } catch (Exception e) {
            TurnQuest.view.Base.GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return null;
    }

    public String UserAuthenticate() {


        String userName = null;
        if (sysUsername.getValue().toString() != null) {
            userName = sysUsername.getValue().toString();
        }


        int rset = 0;
        String domain;
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        String login_username =
            GlobalCC.checkNullValues(sysUsername.getValue());
        String login_password =
            GlobalCC.checkNullValues(sysPassword.getValue());

        String success = null;
        String login_message = null;
        OracleCallableStatement cst = null;
        //
        try {
            conn = datahandler.getDatabaseConnection();
            // Check for Email Reading Job
            //        String jobName = "INCOMING_EMAIL";
            //        String query = "begin ? := TQC_SETUPS_CURSOR.get_alerts(?); end;";
            //
            //           cst = (OracleCallableStatement)conn.prepareCall(query);
            //            cst.registerOutParameter(1, OracleTypes.CURSOR);
            //            cst.setString(2, jobName);
            //            cst.execute();
            //            OracleResultSet rst = (OracleResultSet)cst.getObject(1);
            //            int k = 0;
            //            while (rst.next()) {
            //                k++;
            //            }
            //            if (k == 0) {
            //                //Create Alert
            //                CreateAlert(jobName);
            //
            //            }
            //BirthdayAlert bDay = new BirthdayAlert();
            // bDay.makeTrigger();
            //BirthdayAlert bDay = new BirthdayAlert();
            // bDay.makeTrigger();

            if (resetPass.isRendered()) {

                resetUserPassword();
                return null;
            }

            if ((login_username == null) || (login_password == null)) {
                String message = "Cannot Login. Error in Username/Password";
                GlobalCC.errorValueNotEntered(message);
                return null;
            } else {

                login_username = login_username.toUpperCase();
                Map usermap = new HashMap();
                String loginType = loginType();

                if (loginType.equalsIgnoreCase("LDAP")) {
                    usermap =
                            isLdapUserCredentialsOk(login_username, login_password,
                                                    loginType); //check
                } else {
                    usermap =
                            isUserCredentialsOk(login_username, login_password,
                                                loginType); //check
                }

                conn.commit();
                conn.close();

                // login_username=login_username.toUpperCase();
                findSingleSign0n();
                if (usermap != null) {
                    if (usermap.get("rset") != null) {
                        try {
                            rset =
Integer.parseInt(usermap.get("rset").toString());
                        } catch (NumberFormatException e) {
                            GlobalCC.INFORMATIONREPORTING("NUMBER FORMAT EXCEPTION::Error  occured  while Authenticating User:");
                        }
                    }
                }
                if (rset == 1) {

                } else if (rset == 2) { //Access Denied
                    failedLogin(login_username,new BigDecimal( GlobalCC.sysCode));
                    GlobalCC.errorValueNotEntered("ACCESS DENIED::Incorrect  username / password:" +
                                                  usermap.get("login_message"));
                    return null;
                } else if (rset ==
                           3) { // Wrong username and password does not exist
                          // failedLogin(login_username,new BigDecimal( GlobalCC.sysCode));
                    GlobalCC.errorValueNotEntered("ACCESS DENIED::Incorrect  username / password:" +
                                                  usermap.get("login_message"));
                    return null;
                } else if (rset == 7) {
                    GlobalCC.showPopup("popContintue");
                    return null;
                } else if (rset ==
                           0) { // Wrong username and password does not exist
                    return null;

                } else if (rset == 4) {
                    String securityMand = null;
                    TurnQuest.view.Base.Util util =
                        new TurnQuest.view.Base.Util();
                    securityMand =
                            util.getParameterValue("SECURITY_QUESTION_MAND");
                    if (securityMand.equals("Y")) {
                        txtSecuritylabel.setRendered(true);
                        txtSecurityQuestions.setRendered(true);
                        txtSecurityAnswerLabel.setRendered(true);
                        txtSecurityAnswer.setRendered(true);
                        logBtn.setRendered(false);
                        txtCheck.setRendered(true);
                    } else {
                        String val =
                            (String)GlobalCC.getSysParamValue("RESET_PASSWORD_CODE");

                        if ("N".equalsIgnoreCase(val)) {
                            GlobalCC.errorValueNotEntered("Please reset your password::");
                            resetLabel.setRendered(true); //resetLabel.setRendered(true);
                            resetPass.setRendered(true); //resetPass.setRendered(true);
                            confirmLabel.setRendered(true); //confirmLabel.setRendered(true);
                            confirmPass.setRendered(true); //confirmPass.setRendered(true);
                            passLabel.setValue("Old Password");
                            logBtn.setRendered(false); //logBtn.setRendered(false);
                            rstBtn.setRendered(true); //rstBtn.setRendered(true);
                            cnclBtn.setRendered(true); //cnclBtn.setRendered(true);
                            txtCheck.setRendered(false);

                        } else {

                            /*GlobalCC.sendSms("Sms Code: ",
                                         "Verification code is : " +
                                         getSaltString(), userName);*/
                            usernameLabel.setRendered(false);
                            sysUsername.setRendered(false);
                            passLabel.setRendered(false);
                            sysPassword.setRendered(false);

                            verifCodeLabel.setRendered(true);
                            verifCode.setRendered(true);
                            subVerifCode.setRendered(true); //rstBtn.setRendered(true);

                            cnclBtn.setRendered(false); //cnclBtn.setRendered(true);
                            txtCheck.setRendered(false);
                            forgotPass.setRendered(false);
                            logBtn.setRendered(false);


                        }

                    }
                    return null;
                } else if (rset ==
                           7) { // Password about to expire but login. expiry
                           failedLogin(login_username,new BigDecimal( GlobalCC.sysCode));
                    GlobalCC.errorValueNotEntered("ACCESS DENIED::Incorrect  username / password:" +
                                                  usermap.get("login_message"));
                    return null;

                } else //Password problem
                {
                    failedLogin(login_username,new BigDecimal( GlobalCC.sysCode));
                    GlobalCC.errorValueNotEntered("ACCESS DENIED::Incorrect  username / password:" +
                                                  usermap.get("login_message"));

                    return null;
                }
                String message = findLandPage();
                if (message == null) {
                    return null;
                }

                //    username.setValue(null);
                //     password.setValue(null);

                // FacesContext fc = FacesContext.getCurrentInstance();
                // UIViewRoot viewRoot = fc.getApplication().getViewHandler().createView(fc, "/home.jspx");
                //  fc.setViewRoot(viewRoot);
                //   fc.renderResponse();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;

    }

    /* public String sysParameters() {
        //OBSOLETE CODE
        getParameter("AGENCY_EMAIL_MANDATORY", "AGENCY_EMAIL_MANDATORY");
        getParameter("AGENCY_COUNTY_MANDATORY", "AGENCY_COUNTY_MANDATORY");
        getParameter("AGENCY_TOWN_MANDATORY", "AGENCY_TOWN_MANDATORY");
        getParameter("SACCO_VISIBLE", "SACCO_VISIBLE");
        //
        getParameter("STATE_REQUIRED", "STATE_REQUIRED");
        getParameter("CREDIT_RATING_REQUIRED", "CREDIT_RATING_REQUIRED");
        getParameter("MANAGER_LABEL", "MANAGER");
        getParameter("DIVISIONS_APPLIC", "DIVISIONS_APPLIC");
        getParameter("ID_NO_LABEL", "ID_NO_LABEL");
        getParameter("PAYROLL_NO_LABEL", "PAYROLL_NO_LABEL");
        getParameter("ID_NO_FORMAT", "ID_NO_FORMAT");
        getParameter("CHANNEL_MANAGER", "CHANNEL_MANAGER");
        getParameter("DISTRICT", "DISTRICT");
        getParameter("PIN_OR_PASS_MAND", "PIN_OR_PASS_MAND");
        //CLIENT PARAMETERS
        getParameter("CLIENT", "CLIENT");
        getParameter("CLIENT_CONTACT_DEBTOR", "CLIENT_CONTACT_DEBTOR");
        getParameter("CLIENT_PIN_MANDATORY", "CLIENT_PIN_MANDATORY");
        getParameter("CLIENT_BRANCH_MANDATORY", "CLIENT_BRANCH_MANDATORY");
        getParameter("CLIENT_OCCUPATION_MANDATORY", "CLIENT_OCCUPATION_MANDATORY");
        getParameter("CLIENT_COUNTRY_MANDATORY", "CLIENT_COUNTRY_MANDATORY");
        getParameter("GSM_OR_INTERNATIONAL_MAND", "GSM_OR_INTERNATIONAL_MAND");
        getParameter("MULE_URL", "MULE_URL");

        getParameter("SERVICE_DESK_DMS_ENABLED", "SERVICE_DESK_DMS_ENABLED");

        checkDefaultSite(); // Parameterised the Default Site
        checkIfDivisionApplicable();
        checkIfClientDOBRequired();
        checkIfRelationshRequired();
        checkIfClientSMSRequired();
        checkIfTelephone();
        checkIfEmailRequired();
        checkIfPhysicalAddrRequired();
        checkIfPostalAddrRequired();
        getUserDefaults();
        establishmentLogo();

        FacesContext context=FacesContext.getCurrentInstance();
        if(context!=null)
        {
            HttpSession session = (HttpSession)context.getExternalContext().getSession(true);
            if(session!=null){
                session.setAttribute("DOB_REQUIRED", null);
                session.setAttribute("SMS_REQUIRED", null);
                session.setAttribute("EMAIL_REQUIRED", null);
                session.setAttribute("TELEPHONE_REQUIRED", null);
                session.setAttribute("RELATIONSHIP_OFFICER_REQUIRED", null);
                if(!GlobalCC.isEmptyBD(session.getAttribute("orgCode"))) {
                       getOrgType(new Integer(session.getAttribute("orgCode").toString()), "ORG_TYPE");
                 }
            }
        }
        //  restartAllStoppedJobs();
        return null;
    }*/


    public Map isUserCredentialsOk(String username, String password,
                                   String loginType) {
        DBConnector datahandler = new DBConnector();
        OracleConnection conn;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement callStmt = null;
        Map map = new HashMap();
        String login_message = null;
        int rset = 0;
        String jobquery =
            "begin ? := tqc_web_pkg.user_authenticate(?,?,?,?,?,?,?); end;";
        if (username.equalsIgnoreCase(password)) {
            String message = "Password Cannot Be The Same As Username";
            GlobalCC.errorValueNotEntered(message);
            return null;
        }

        if (conn == null) {
            String message =
                "Cannot connect to the server. Check your server connection";
            GlobalCC.errorValueNotEntered(message);
            return null;
        }


        //bind the variables
        try {


            callStmt = (OracleCallableStatement)conn.prepareCall(jobquery);
            callStmt.setString(2, username.toUpperCase());
            callStmt.setString(3, password);
            callStmt.registerOutParameter(4, OracleTypes.VARCHAR); //Error Msg
            callStmt.registerOutParameter(5,
                                          OracleTypes.DATE); //Last login Return Value
            callStmt.registerOutParameter(6,
                                          OracleTypes.VARCHAR); // UserCode Return value
            callStmt.registerOutParameter(7,
                                          OracleTypes.VARCHAR); //UserName Return Value
            callStmt.setInt(8, GlobalCC.sysCode);

            callStmt.registerOutParameter(1,
                                          OracleTypes.INTEGER); //Return value
            callStmt.execute();
            rset = callStmt.getInt(1);
            map.put("rset", rset);
            login_message = callStmt.getString(4);
            map.put("login_message", login_message);
            Date loginDt = callStmt.getDate(5);
            BigDecimal usrCode = callStmt.getBigDecimal(6);
            callStmt.close();
            conn.commit();
            if ((rset == 1) ||
                (rset == 7)) { //'Login Successfull' or 'Your password is due to expire in X days'
                ExternalContext ectx =
                    FacesContext.getCurrentInstance().getExternalContext();
                HttpServletResponse response =
                    (HttpServletResponse)ectx.getResponse();
                HttpSession session = (HttpSession)ectx.getSession(true);
                session.removeAttribute("back_url");
                session.setAttribute("LoginDate", loginDt);
                session.setAttribute("loginInfo", login_message);
                session.setAttribute("Username", username.toUpperCase());
                session.setAttribute("UserCode", usrCode);
                session.setAttribute("SIGN_IN_MODE", loginType);
                ISession.sysParameters(session);

                HttpServletRequest request =
                    (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
                String remoteAddr = request.getRemoteAddr();
                String userAgent = request.getHeader("User-Agent");
                UserAgentParser userAgentParser =
                    new UserAgentParser(userAgent);

                String DfltBranchDiv =
                    "begin tqc_web_pkg.check_multiple_logins(?,?,?,?); end;";
                callStmt =
                        (OracleCallableStatement)conn.prepareCall(DfltBranchDiv);
                callStmt.setString(1,
                                   (String)session.getAttribute("Username"));
                callStmt.setString(2, remoteAddr);
                callStmt.setInt(3, 0);
                callStmt.setString(4, userAgentParser.getBrowserName());
                callStmt.execute();
                callStmt.close();
                session.setAttribute("hostIp", remoteAddr);
            } 
            if ((rset == 1)) { //'Login Successfull'
                FacesContext.getCurrentInstance().getExternalContext().redirect("home.jspx");
            }
            conn.close();
            return map;
            // Get the user default branch
        } catch (Exception exception) {
            GlobalCC.EXCEPTIONREPORTING(conn, exception);
        }
        return null;
    }

    public Map isLdapUserCredentialsOk(String username, String password,
                                       String loginType) {

        String newuser = getDomainName();
        newuser = username.concat(newuser);


        Map map = new HashMap();
        String ldapAdServer = null;
        OracleConnection conn = null;
        try {

            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();
            OracleCallableStatement callStmt = null;
            OracleCallableStatement callStmt1 = null;
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.getParameter(?); end;";
            callStmt = (OracleCallableStatement)conn.prepareCall(query1);
            callStmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            callStmt.setString(2, "LDAP_SERVER");

            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);
            while (rs.next()) {

                ldapAdServer = rs.getString(1);

            }
            callStmt.close();
            rs.close();
            Hashtable<String, Object> env = new Hashtable<String, Object>();
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.INITIAL_CONTEXT_FACTORY,
                    "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldap://" + ldapAdServer);
            DirContext ctx = new InitialDirContext(env);


            ctx =
ActiveDirectory.getConnection(newuser, password, null, ldapAdServer);
            // session.setAttribute("ldapCtx", ctx);

            //ctx = new InitialDirContext(env);


            String login_message = null;
            int rset = 0;
            String jobquery =
                "begin ? := tqc_web_pkg.ldap_authenticate(?,?,?,?,?,?); end;";


            if (conn == null) {
                String message =
                    "Cannot connect to the server. Check your server connection";
                GlobalCC.errorValueNotEntered(message);
                return null;
            }


            //bind the variables

            callStmt = (OracleCallableStatement)conn.prepareCall(jobquery);
            callStmt.setString(2, username.toUpperCase());
            callStmt.registerOutParameter(3, OracleTypes.VARCHAR); //Error Msg
            callStmt.registerOutParameter(4,
                                          OracleTypes.DATE); //Last login Return Value
            callStmt.registerOutParameter(5,
                                          OracleTypes.VARCHAR); // UserCode Return value
            callStmt.registerOutParameter(6,
                                          OracleTypes.VARCHAR); //UserName Return Value
            callStmt.setInt(7, GlobalCC.sysCode);
            callStmt.registerOutParameter(1,
                                          OracleTypes.INTEGER); //Return value
            callStmt.execute();
            rset = callStmt.getInt(1);
            map.put("rset", rset);
            login_message = callStmt.getString(3);
            map.put("login_message", login_message);

            Date loginDt = callStmt.getDate(4);
            BigDecimal usrCode = callStmt.getBigDecimal(5);


            callStmt.close();
            conn.commit();


            // Get the user default branch
            //conn.commit();
            if (rset == 1) {

                FacesContext context = FacesContext.getCurrentInstance();
                ExternalContext ectx =
                    FacesContext.getCurrentInstance().getExternalContext();
                HttpServletResponse response =
                    (HttpServletResponse)ectx.getResponse();
                HttpSession session = (HttpSession)ectx.getSession(false);
                session.invalidate();
                session = (HttpSession)ectx.getSession(true);
                context.responseComplete();
                session.removeAttribute("back_url");
                session.setAttribute("LoginDate", loginDt);
                session.setAttribute("Username", username.toUpperCase());
                session.setAttribute("UserCode", usrCode);
                session.setAttribute("SIGN_IN_MODE", loginType);
                ISession.sysParameters(session);
                HttpServletRequest request =
                    (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
                String remoteAddr = request.getRemoteAddr();
                String userAgent = request.getHeader("User-Agent");
                UserAgentParser userAgentParser =
                    new UserAgentParser(userAgent);

                String DfltBranchDiv =
                    "begin  tqc_web_pkg.check_multiple_logins(?,?,?,?); end;";
                callStmt1 =
                        (OracleCallableStatement)conn.prepareCall(DfltBranchDiv);
                callStmt1.setString(1,
                                    (String)session.getAttribute("Username"));
                callStmt1.setString(2, remoteAddr);
                callStmt1.setInt(3, 0);
                callStmt1.setString(4, userAgentParser.getBrowserName());
                callStmt1.execute();
                session.setAttribute("hostIp", remoteAddr);
                FacesContext.getCurrentInstance().getExternalContext().redirect("home.jspx");
                callStmt1.close();
                conn.close();

                int rset2 = 0;
                Map maps = new HashMap();
                maps = checkUserSystem(username);
                // checks if user is assigned that system
                if (maps != null) {
                    if (maps.get("rset") != null) {
                        try {
                            rset2 =
                                    Integer.parseInt(maps.get("rset").toString());
                        } catch (NumberFormatException e) {
                            GlobalCC.INFORMATIONREPORTING("NUMBER FORMAT EXCEPTION::Error  occured  while Authenticating User:");
                        }
                    }
                } else {
                    return null;
                }

                if (rset2 == 1) { // Access allowed to the system
                    return null;
                } else if (rset2 == 2) {
                    GlobalCC.errorValueNotEntered("Access Denied to the selected System");

                    return null;
                    //TODO
                } else if (rset2 == 3) { // xist
                    GlobalCC.errorValueNotEntered(maps.get("login_message") ==
                                                  null ? null :
                                                  maps.get("login_message").toString());
                    return null;
                }
            }


        } catch (Exception exception) {

            exception.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING("Invalid Login Credentials" +
                                        exception.getMessage());

            // GlobalCC.EXCEPTIONREPORTING(conn, exception);
            return map;
        }
        return map;

    }
    /*  public Map isLdapUserCredentialsOk(String username, String password,String loginType) {
        String newuser = getDomainName();
        newuser = username.concat(newuser);
        String ldapAdServer = null;
        OracleConnection conn = null;
        try {
            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();
            OracleCallableStatement callStmt = null;
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.getParameter(?); end;";

            callStmt = (OracleCallableStatement)conn.prepareCall(query1);
            callStmt.registerOutParameter(1, -10);
            callStmt.setString(2, "LDAP_SERVER");

            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);
            while (rs.next()) {
                ldapAdServer = rs.getString(1);
            }
            callStmt.close();
            rs.close();
            Hashtable<String, Object> env = new Hashtable();
            env.put("java.naming.security.authentication", "simple");
            env.put("java.naming.factory.initial",
                    "com.sun.jndi.ldap.LdapCtxFactory");

            env.put("java.naming.provider.url", "ldap://" + ldapAdServer);
            DirContext ctx = new InitialDirContext(env);
            String[] myUsername = newuser.split("@");


            String[] ldapDomain = null;
            if (myUsername.length > 1) {
                ldapDomain = myUsername[0].split("\\.");
            }
            ldapDomain = myUsername[0].split("\\.");
            if (ldapDomain == null) {
                SearchResult srLdapUser =
                    findAccountByAccountName(ctx, ldapAdServer, newuser);

                String fullDN = null;
                fullDN = srLdapUser.getNameInNamespace();
                ctx.close();
                ctx = null;

                env.put("java.naming.security.authentication", "simple");
                env.put("java.naming.security.principal", fullDN);
                env.put("java.naming.security.credentials", password);
            }
            Map map = new HashMap();
            String login_message = null;
            int rset = 0;
            String jobquery =
                "begin ? := tqc_web_pkg.ldap_authenticate(?,?,?,?,?,?); end;";
            if (conn == null) {
                String message =
                    "Cannot connect to the server. Check your server connection";

                GlobalCC.errorValueNotEntered(message);
                return null;
            }
            callStmt = (OracleCallableStatement)conn.prepareCall(jobquery);
            callStmt.setString(2, username.toUpperCase());
            callStmt.registerOutParameter(3, 12);
            callStmt.registerOutParameter(4, 91);

            callStmt.registerOutParameter(5, 12);

            callStmt.registerOutParameter(6, 12);

            callStmt.setInt(7, 27);
            callStmt.registerOutParameter(1, 4);

            callStmt.execute();
            rset = callStmt.getInt(1);
            map.put("rset", Integer.valueOf(rset));
            login_message = callStmt.getString(3);
            map.put("login_message", login_message);
            Date loginDt = callStmt.getDate(4);
            BigDecimal usrCode =callStmt.getBigDecimal(5);

            callStmt.close();
            conn.commit();
            if(rset==1){

                FacesContext context = FacesContext.getCurrentInstance();
                 ExternalContext ectx =FacesContext.getCurrentInstance().getExternalContext();
               HttpServletResponse response = (HttpServletResponse)ectx.getResponse();
                 HttpSession session = (HttpSession)ectx.getSession(false);
                session.invalidate();
                session = (HttpSession)ectx.getSession(true);
                context.responseComplete();

                session.setAttribute("LoginDate",loginDt);
                session.setAttribute("Username", username.toUpperCase());
                session.setAttribute("UserCode", usrCode);
                session.setAttribute("SIGN_IN_MODE", loginType);
                HttpServletRequest request =  (HttpServletRequest)FacesContext.getCurrentInstance()
                                                        .getExternalContext() .getRequest();
                String remoteAddr = request.getRemoteAddr();
                String userAgent = request.getHeader("User-Agent");
                UserAgentParser userAgentParser = new UserAgentParser(userAgent);

                String DfltBranchDiv = "begin  tqc_web_pkg.check_multiple_logins(?,?,?,?); end;";
                callStmt = (OracleCallableStatement)conn.prepareCall(DfltBranchDiv);
                callStmt.setString(1, (String)session.getAttribute("Username"));
                callStmt.setString(2, remoteAddr);
                callStmt.setInt(3, 0);
                callStmt.setString(4,userAgentParser.getBrowserName() );
                callStmt.execute();
                session.setAttribute("hostIp", remoteAddr);
                FacesContext.getCurrentInstance().getExternalContext().redirect("home.jspx");
            int rset2 = 0;
            Map maps = new HashMap();
            maps = checkUserSystem(username);
            // checks if user is assigned that system
            if (maps != null) {
                if (maps.get("rset") != null) {
                    try {
                        rset2 = Integer.parseInt(maps.get("rset").toString());
                    } catch (NumberFormatException e) {
                        GlobalCC.INFORMATIONREPORTING("NUMBER FORMAT EXCEPTION::Error  occured  while Authenticating User:");
                    }
                }
            } else {
                return null;
            }

            if (rset2 == 1) { // Access allowed to the system

                //sysParameters();

                return null;


            } else if (rset2 == 2) {
                GlobalCC.errorValueNotEntered("Access Denied to the selected System");

                return null;
                //TODO
            } else if (rset2 == 3) { // xist
                GlobalCC.errorValueNotEntered(maps.get("login_message") ==
                                              null ? null :
                                              maps.get("login_message").toString());
                return null;
            }
            }

            return map;
        } catch (Exception exception) {
            GlobalCC.EXCEPTIONREPORTING("Invalid Login Credentials");
            exception.printStackTrace();
        }
        return null;
    }
*/

    public String loginType() {


        String loginType = "NORMAL";
        OracleConnection conn = null;
        try {
            DBConnector datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();
            OracleCallableStatement callStmt = null;
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.getParameter(?); end;";
            callStmt = (OracleCallableStatement)conn.prepareCall(query1);
            callStmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            callStmt.setString(2, "SIGN_IN_MODE");

            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);
            while (rs.next()) {

                loginType = rs.getString(1);

            }
            callStmt.close();


            rs.close();
            conn.close();


        } catch (Exception exception) {

            GlobalCC.EXCEPTIONREPORTING(conn, exception);
            return loginType;
        }
        return loginType;

    }

    public SearchResult findAccountByAccountName(DirContext ctx,
                                                 String ldapSearchBase,
                                                 String accountName) throws NamingException {

        String searchFilter =
            "(&(objectClass=person)(uid=" + accountName + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> results =
            ctx.search(ldapSearchBase, searchFilter, searchControls);
        SearchResult searchResult = null;
        if (results.hasMoreElements()) {
            searchResult = (SearchResult)results.nextElement();
            Attributes attributes = searchResult.getAttributes();

            //make sure there is not another item available, there should be only 1 match
            if (results.hasMoreElements()) {
                System.err.println("Matched multiple users for the accountName: " +
                                   accountName);
                return null;
            }

        }

        return searchResult;
    }

    public String getUserDefaults() {
        HttpSession session =
            (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

        String query =
            "begin ? := tqc_roles_cursor.get_user_defaults(?); end;";
        CallableStatement cst = null;
        DBConnector handler = new DBConnector();
        OracleConnection connection = null;
        Branch branch = new Branch();
        try {
            if (session.getAttribute("UserCode") != null) {
                connection = handler.getDatabaseConnection();

                cst = connection.prepareCall(query);
                cst.registerOutParameter(1, OracleTypes.CURSOR);
                cst.setBigDecimal(2,
                                  new BigDecimal(session.getAttribute("UserCode").toString()));
                cst.execute();

                OracleResultSet rs = (OracleResultSet)cst.getObject(1);
                int k = 0;
                while (rs.next()) {

                    branch.setOrgCode(rs.getBigDecimal(1));
                    branch.setOrgName(rs.getString(2));
                    branch.setRegCode(rs.getString(3));
                    branch.setRegionName(rs.getString(4));
                    branch.setCode(rs.getBigDecimal(5));
                    branch.setName(rs.getString(6));
                    // branch.setCurrencyCode(rs.getString(7));
                    // branch.setCurrencyDesc(rs.getString(8));
                    branch.setCouCode(rs.getBigDecimal(7));
                    branch.setCouName(rs.getString(8));
                    branch.setAdminRgnType(rs.getString(9));
                    session.setAttribute("orgCode", branch.getOrgCode());
                    session.setAttribute("orgName", branch.getOrgName());
                    session.setAttribute("DEFAULT_BRANCH_CODE",
                                         branch.getCode());
                    session.setAttribute("DEFAULT_BRANCH_NAME",
                                         branch.getName());
                    session.setAttribute("userDefaultReg",
                                         branch.getRegCode());
                    session.setAttribute("COUNTRY_CODE", branch.getCouCode());
                    session.setAttribute("COUNTRY_NAME", branch.getCouName());
                    session.setAttribute("ADMIN_REG_TYPE",
                                         branch.getAdminRgnType());
                    session.setAttribute("zipCode", rs.getString(11));
                    k++;
                }

                if (k == 0) {
                    GlobalCC.INFORMATIONREPORTING("User Default Branch Not Defined.Please set default Branch as soon as you LOGIN  ::<b>Click Ok To continue<b>");

                } else {
                    cst.close();
                    rs.close();
                    connection.close();

                    Date today = new Date();
                    String currentYear =
                        GlobalCC.parseYearDate(today.toString());
                    String currentPeriod =
                        GlobalCC.parseMonth(today.toString()).toUpperCase();
                    session.setAttribute("yerYear", currentYear);
                    session.setAttribute("period", currentPeriod);


                }

            } else {
                GlobalCC.INFORMATIONREPORTING("Error Getting  user defaults:: ");
                return null;


            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
            return null;
        }

        return null;

    }

    public String checkIfDivisionApplicable() {

        HttpSession session =
            (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

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
                "begin ? := TQC_SETUPS_CURSOR.checkif_DIVISIONS_APPLIC(); end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);


            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            while (rs.next()) {
                session.setAttribute("div_applicable", rs.getString(1));


            }

            if (session.getAttribute("div_applicable") == null) {
                GlobalCC.INFORMATIONREPORTING("Ensure that The Parameter :: 'DIVISIONS_APPLIC' is set To 'Y'  or 'N'");
                return null;
            }
            rs.close();

            stmt.close();
            connection.commit();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    /*public String getParameter(String param_name, String sessionName) {
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        String sessionVal=(String)session.getAttribute(sessionName);
        if(sessionVal==null || "".equals(sessionVal))
        {
            try {
                connection = dbConnector.getDatabaseConnection();
                String query1 =
                    "begin ? := TQC_SETUPS_CURSOR.getParameter(?); end;";
                stmt = (OracleCallableStatement)connection.prepareCall(query1);
                stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
                stmt.setString(2, param_name);

                stmt.execute();
                rs = (OracleResultSet)stmt.getObject(1);
                while (rs.next()) {
                    session.setAttribute(sessionName, rs.getString(1));
                    System.out.println("session["+sessionName+"]="+ rs.getString(1));
                }
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }finally{
                DbUtils.closeQuietly(connection, stmt, rs);
            }
        }

        return null;
    }*/

    public String getOrgType(Integer orgCode, String sessionName) {

        HttpSession session =
            (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;

        try {

            connection = dbConnector.getDatabaseConnection();
            String query1 = "begin ? := TQC_SETUPS_CURSOR.getOrgType(?); end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.registerOutParameter(1, OracleTypes.VARCHAR);
            stmt.setInt(2, orgCode);

            stmt.execute();

            session.setAttribute(sessionName, stmt.getString(1));

            stmt.close();
            connection.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(connection, stmt, null);
        }
        return null;
    }


    public String resetUserPassword() {

        String login_username =
            GlobalCC.checkNullValues(sysUsername.getValue());
        String login_password =
            GlobalCC.checkNullValues(sysPassword.getValue());
        String resetPassVal = GlobalCC.checkNullValues(resetPass.getValue());
        String confirmPassVal =
            GlobalCC.checkNullValues(confirmPass.getValue());

        if ((login_username == null) || (login_password == null)) {
            String message = "Cannot Login.Error in Username/Password";
            GlobalCC.errorValueNotEntered(message);
            return null;
        } else {

            if (!resetPassVal.equals(confirmPassVal)) {
                String message = "Error In Confirmation Password";
                GlobalCC.errorValueNotEntered(message);

                return null;
            }

            if (login_username.equalsIgnoreCase(resetPassVal)) {
                String message = "Password Cannot Be The Same As Username";
                GlobalCC.errorValueNotEntered(message);
                return null;
            }

            DBConnector datahandler = new DBConnector();
            OracleConnection conn;
            conn = datahandler.getDatabaseConnection();

            if (conn == null) {
                String message =
                    "Cannot connect to the server. Check your server connection";
                GlobalCC.errorValueNotEntered(message);
                return null;
            }


            try {
                int rset = 0;

                OracleCallableStatement callStmt = null;
                String jobquery =
                    "begin ? := tqc_web_pkg.user_authenticate(?,?,?,?,?,?,?,?,?,?,?); end;";
                callStmt = (OracleCallableStatement)conn.prepareCall(jobquery);
                //bind the variables
                callStmt.registerOutParameter(1,
                                              OracleTypes.INTEGER); //Return value
                callStmt.setString(2, login_username);
                callStmt.setString(3, login_password);
                callStmt.setString(4, "Y");
                callStmt.setString(5, resetPassVal);
                callStmt.setString(6, confirmPassVal);
                callStmt.registerOutParameter(7,
                                              OracleTypes.VARCHAR); //Error Msg
                callStmt.registerOutParameter(8,
                                              OracleTypes.DATE); //Last login Return Value
                callStmt.registerOutParameter(9,
                                              OracleTypes.INTEGER); // User Code Return value
                callStmt.registerOutParameter(10,
                                              OracleTypes.VARCHAR); //User Name Return Value
                callStmt.setInt(11, GlobalCC.sysCode);
                callStmt.setString(12, null); //reset code

                callStmt.execute();

                //callStmt.execute();
                rset = callStmt.getInt(1);
                String login_message = callStmt.getString(7);
                callStmt.close();


                if (rset == 1) { // Access allowed


                    resetPass.setValue(null);
                    confirmPass.setValue(null);
                    sysPassword.setValue(null);
                    sysPassword.setRendered(true);
                    usernameLabel.setRendered(true);
                    passLabel.setRendered(true);
                    sysUsername.setRendered(true);


                    GlobalCC.sysInformation("Password Successfully Reset");


                    //FacesContext fc = FacesContext.getCurrentInstance();
                    // UIViewRoot viewRoot = fc.getApplication().getViewHandler().createView(fc, "/home.jspx");
                    //fc.setViewRoot(viewRoot);
                    //fc.renderResponse();


                    actionCancelReset();
                    return null;

                } else if (rset == 2) { //Access Denied
                    GlobalCC.errorValueNotEntered(login_message);

                } else if (rset == 4) //Password problem
                {
                    String securityMand = null;
                    Util util = new Util();
                    securityMand =
                            util.getParameterValue("SECURITY_QUESTION_MAND");
                    if (securityMand.equals("Y")) {
                        txtSecuritylabel.setRendered(true);
                        txtSecurityQuestions.setRendered(true);
                        txtSecurityAnswerLabel.setRendered(true);
                        txtSecurityAnswer.setRendered(true);
                        txtCheck.setRendered(true);
                    } else {
                        GlobalCC.errorValueNotEntered(login_message);
                        resetLabel.setRendered(true);
                        resetPass.setRendered(true);
                        confirmLabel.setRendered(true);
                        confirmPass.setRendered(true);
                        passLabel.setValue("Old Password");
                        logBtn.setRendered(false);
                        rstBtn.setRendered(true);
                        cnclBtn.setRendered(true);
                        txtCheck.setRendered(false);


                    }
                } else {
                    GlobalCC.errorValueNotEntered(login_message);
                }
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }


        return null;
    }

    public String actionCancelReset() {

        logBtn.setVisible(true);
        logBtn.setRendered(true);
        logBtn.setDisabled(false);
        sysUsername.setValue(null);
        sysPassword.setValue(null);
        passLabel.setValue("Password");
        passLabel.setVisible(true);
        forgotPass.setRendered(true);
        sysUsername.setVisible(true);
        sysPassword.setVisible(true);
        sysUsername.setRendered(true);
        sysPassword.setRendered(true);
        sysUsername.setDisabled(false);
        sysPassword.setDisabled(false);
        FacesContext fc = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot =
            fc.getApplication().getViewHandler().createView(fc, "/index.jspx");
        fc.setViewRoot(viewRoot);
        fc.renderResponse();

        return null;
    }

    public void setPassword(RichInputText password) {
        this.password = password;
    }

    public RichInputText getPassword() {
        return password;
    }

    public void setUsername(RichInputText username) {
        this.username = username;
    }

    public RichInputText getUsername() {
        return username;
    }


    public void setSysUsername(RichInputText sysUsername) {
        this.sysUsername = sysUsername;
    }

    public RichInputText getSysUsername() {
        return sysUsername;
    }

    public void setSysPassword(RichInputText sysPassword) {
        this.sysPassword = sysPassword;
    }

    public RichInputText getSysPassword() {
        return sysPassword;
    }

    public void setResetLabel(RichOutputLabel resetLabel) {
        this.resetLabel = resetLabel;
    }

    public RichOutputLabel getResetLabel() {
        return resetLabel;
    }

    public void setResetPass(RichInputText resetPass) {
        this.resetPass = resetPass;
    }

    public RichInputText getResetPass() {
        return resetPass;
    }

    public void setConfirmLabel(RichOutputLabel confirmLabel) {
        this.confirmLabel = confirmLabel;
    }

    public RichOutputLabel getConfirmLabel() {
        return confirmLabel;
    }

    public void setConfirmPass(RichInputText confirmPass) {
        this.confirmPass = confirmPass;
    }

    public RichInputText getConfirmPass() {
        return confirmPass;
    }

    public void setLogBtn(RichCommandButton logBtn) {
        this.logBtn = logBtn;
    }

    public RichCommandButton getLogBtn() {
        return logBtn;
    }

    public void setRstBtn(RichCommandButton rstBtn) {
        this.rstBtn = rstBtn;
    }

    public RichCommandButton getRstBtn() {
        return rstBtn;
    }

    public void setPassLabel(RichOutputLabel passLabel) {
        this.passLabel = passLabel;
    }

    public RichOutputLabel getPassLabel() {
        return passLabel;
    }

    public void setCnclBtn(RichCommandButton cnclBtn) {
        this.cnclBtn = cnclBtn;
    }

    public RichCommandButton getCnclBtn() {
        return cnclBtn;
    }


    /*public String actionSystemLogout() {

        LOVDAO lov = new LOVDAO();
        lov.reInitializeVariables();


        try {
            HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.invalidate();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        FacesContext fc = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot =
            fc.getApplication().getViewHandler().createView(fc, "/index.jspx");
        fc.setViewRoot(viewRoot);
        fc.renderResponse();

        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext ectx = ctx.getExternalContext();
        HttpSession session = (HttpSession)ectx.getSession(false);

        if (session != null) {
            session.invalidate();
        }


        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.jspx");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }

        // Tell faces that we're handling the navigation from here and that there
        // is no need for a new context to be created
        ctx.responseComplete();
        return null;
    }*/

    public String actionSystemLogout() {
        System.out.println("Am logging out!");
        try {
            HttpServletRequest request =
                (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
            HttpServletResponse response =
                (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
            ExternalContext ectx =
                FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession)ectx.getSession(false);
            if (session != null) {
                GlobalCC.logoutUser();
            }

            Cookie cookie = null;
            Cookie[] cookieArray = request.getCookies();

            if (cookieArray != null) {
                for (int i = 0; i < cookieArray.length; i++) {
                    cookie = cookieArray[i];
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }


            if (session != null) {
                Enumeration keys = session.getAttributeNames();
                while (keys.hasMoreElements()) {
                    String key = (String)keys.nextElement();
                    session.removeAttribute(key);
                }
                session.invalidate();
            }
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.jspx");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String establishmentLogo() {
        HttpSession session =
            (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

        OracleConnection conn = null;
        File myfile = null;
        try {

            DBConnector ds = new DBConnector();

            conn = ds.getDatabaseConnection();
            OracleCallableStatement callStmt = null;

            String hrQuery = null;

            hrQuery =
                    "SELECT ORG_RPT_LOGO FROM TQC_ORGANIZATIONS WHERE ORG_CODE = " +
                    session.getAttribute("orgCode") + " ";
            callStmt = (OracleCallableStatement)conn.prepareCall(hrQuery);
            //bind the variables

            OracleResultSet rs = (OracleResultSet)callStmt.executeQuery();

            String strBarCodeImage =
                "/images/" + session.getAttribute("orgCode") + ".gif";
            FacesContext context = FacesContext.getCurrentInstance();

            ServletContext sc =
                (ServletContext)context.getExternalContext().getContext();
            strBarCodeImage = sc.getRealPath(strBarCodeImage);
            myfile = new File(strBarCodeImage);
            if (myfile.length() == 0) {


                while (rs.next()) {
                    byte barray[] = rs.getBytes(1);
                    FileOutputStream out =
                        new FileOutputStream(strBarCodeImage);
                    out.write(barray);
                    out.close();
                }

                myfile = new File(strBarCodeImage);

                rs.close();

                callStmt.close();
            }


            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }


    public String findSingleSign0n() {
        HttpSession session =
            (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

        String success = null;
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement callStmt;
        try {
            callStmt =
                    (OracleCallableStatement)conn.prepareCall("begin HRMS_Settings_Pkg.System_param_val(?,?); end;");
            callStmt.setString(1, "SINGLE_SIGN_ON");
            callStmt.registerOutParameter(2, OracleTypes.VARCHAR);
            callStmt.execute();
            session.setAttribute("singleSignon", callStmt.getString(2));
            callStmt.close();
            conn.commit();
            conn.close();
            success = "Y";
        } catch (Exception e) {
            // GlobalCC GlobalCC = new GlobalCC();
            //  GlobalCC.EXCEPTIONREPORTING(e);
        }
        return success;
    }

    public String findLandPage() {
        HttpSession session =
            (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

        String success = null;
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        conn = datahandler.getDatabaseConnection();
        OracleCallableStatement callStmt;
        try {
            callStmt =
                    (OracleCallableStatement)conn.prepareCall("begin HRMS_Settings_Pkg.System_param_val(?,?); end;");
            callStmt.setString(1, "LAND_SCREEN");
            callStmt.registerOutParameter(2, OracleTypes.VARCHAR);
            callStmt.execute();
            if (callStmt.getString(2).equalsIgnoreCase("NULL")) {
                session.setAttribute("LAND_SCREEN", null);
            } else {
                session.setAttribute("LAND_SCREEN", callStmt.getString(2));
            }
            // session.setAttribute("LAND_SCREEN", callStmt.getString(2));
            callStmt.close();
            conn.commit();
            conn.close();
            success = "Y";
        } catch (Exception e) {
            GlobalCC GlobalCC = new GlobalCC();
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return success;
    }

    public void setTxtSecurityQuestions(RichSelectOneChoice txtSecurityQuestions) {
        this.txtSecurityQuestions = txtSecurityQuestions;
    }

    public RichSelectOneChoice getTxtSecurityQuestions() {
        return txtSecurityQuestions;
    }

    public void setTxtSecurityAnswer(RichInputText txtSecurityAnswer) {
        this.txtSecurityAnswer = txtSecurityAnswer;
    }

    public RichInputText getTxtSecurityAnswer() {
        return txtSecurityAnswer;
    }

    public void setTxtSecuritylabel(RichOutputLabel txtSecuritylabel) {
        this.txtSecuritylabel = txtSecuritylabel;
    }

    public RichOutputLabel getTxtSecuritylabel() {
        return txtSecuritylabel;
    }

    public void setTxtSecurityAnswerLabel(RichOutputLabel txtSecurityAnswerLabel) {
        this.txtSecurityAnswerLabel = txtSecurityAnswerLabel;
    }

    public RichOutputLabel getTxtSecurityAnswerLabel() {
        return txtSecurityAnswerLabel;
    }

    public void setTxtCheck(RichCommandButton txtCheck) {
        this.txtCheck = txtCheck;
    }

    public RichCommandButton getTxtCheck() {
        return txtCheck;
    }


    public String checkSecurityQuestion() {
        if (txtSecurityQuestions.getValue() == null) {
            GlobalCC.INFORMATIONREPORTING("Please select a security Question");
            return null;
        }
        if (txtSecurityAnswer.getValue() == null) {
            GlobalCC.INFORMATIONREPORTING("Please select a security answer");
            return null;
        }
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {

            cst =
conn.prepareCall("begin ? := tqc_web_pkg.checksecurityquestion(?,?,?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setObject(2, sysUsername.getValue());
            cst.setObject(3, txtSecurityQuestions.getValue());
            cst.setObject(4, txtSecurityAnswer.getValue());
            cst.execute();
            delink = cst.getString(1);
            if (delink.equals("Y")) {
                GlobalCC.errorValueNotEntered("Please reset your password");
                resetLabel.setRendered(true);
                resetPass.setRendered(true);
                confirmLabel.setRendered(true);
                confirmPass.setRendered(true);
                passLabel.setValue("Old Password");
                sysPassword.setRendered(false);
                passLabel.setRendered(false);
                logBtn.setRendered(false);
                rstBtn.setRendered(true);
                cnclBtn.setRendered(true);
                txtCheck.setRendered(false);
                txtSecuritylabel.setRendered(false);
                txtSecurityQuestions.setRendered(false);
                txtSecurityAnswerLabel.setRendered(false);
                txtSecurityAnswer.setRendered(false);
            } else {
                cnclBtn.setRendered(true);
                GlobalCC.INFORMATIONREPORTING("That security question or answer is wrong.Please contact administrator or try again");
            }

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(e);
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
        return null;
    }

    public String getDomainName() {


        String domainName = null;
        OracleConnection conn = null;
        try {
            DBConnector datahandler = new DBConnector();

            conn = datahandler.getLoginConnection();
            OracleCallableStatement callStmt = null;
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.getParameter(?); end;";
            callStmt = (OracleCallableStatement)conn.prepareCall(query1);
            callStmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            callStmt.setString(2, "DOMAIN");

            callStmt.execute();
            OracleResultSet rs = (OracleResultSet)callStmt.getObject(1);
            while (rs.next()) {
                //   session.setAttribute("SIGN_IN_MODE", rs.getString(1));
                domainName = rs.getString(1);

            }
            callStmt.close();


            rs.close();
            conn.close();


        } catch (Exception exception) {

            GlobalCC.EXCEPTIONREPORTING(conn, exception);
            // return loginType;
        }
        return domainName;

    }

    public String forgotPassword() {
        txtSecuritylabel.setRendered(true);
        txtSecurityQuestions.setRendered(true);
        txtSecurityAnswerLabel.setRendered(true);
        txtSecurityAnswer.setRendered(true);
        txtCheck.setRendered(true);
        forgotPass.setRendered(false);
        passLabel.setVisible(false);
        sysPassword.setVisible(false);
        sysPassword.setValue("password");
        logBtn.setRendered(false);
        /*resetLabel.setRendered(true);
    resetPass.setRendered(true);
    confirmLabel.setRendered(true);
    confirmPass.setRendered(true);


    //passLabel.setValue("Old Password");

    rstBtn.setRendered(true);
    cnclBtn.setRendered(true);*/
        // txtCheck.setRendered(false);
        return null;
    }

    public void setForgotPass(RichCommandLink forgotPass) {
        this.forgotPass = forgotPass;
    }

    public RichCommandLink getForgotPass() {
        return forgotPass;
    }


    public boolean isNotLdapRequired() {
        String type = loginType();
        if (type.equalsIgnoreCase("LDAP")) {
            return false;
        } else {
            return true;
        }
    }

    public String showResetPwd() {
        sysPassword.setValue(null);
        //show
        resetLabel.setRendered(true); //resetLabel.setRendered(true);
        resetPass.setRendered(true); //resetPass.setRendered(true);
        confirmLabel.setRendered(true); //confirmLabel.setRendered(true);
        confirmPass.setRendered(true); //confirmPass.setRendered(true);
        passLabel.setValue("Old Password");
        //toggle reset with login
        logBtn.setRendered(false); //logBtn.setRendered(false);
        rstBtn.setRendered(true); //rstBtn.setRendered(true);
        cnclBtn.setRendered(true); //cnclBtn.setRendered(true);
        return null;
    }

    public void failedLogin(String v_username, BigDecimal v_system_code
                               ) {
           OracleConnection conn = null;
           HttpServletRequest request1 =
                           (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
                       String remoteAddr1 = request1.getRemoteAddr();
                       String userAgent1 = request1.getHeader("User-Agent");
                       UserAgentParser userAgentParser1 =
                           new UserAgentParser(userAgent1);
           try {
                           
               DBConnector datahandler = new DBConnector();
               conn = datahandler.getDatabaseConnection();
               OracleCallableStatement callStmt = null;            
               String query1 =
                   "begin TQC_WEB_PKG.user_failed_login(?,?,?,?); end;";
               callStmt = (OracleCallableStatement)conn.prepareCall(query1);
               callStmt.setString(1,v_username);
               callStmt.setBigDecimal(2, v_system_code);           
               callStmt.setString(3,userAgentParser1.getBrowserName());
               callStmt.setString(4, remoteAddr1);                        
               callStmt.execute();

           } catch (Exception e) {
               GlobalCC.EXCEPTIONREPORTING(e);
           }
       }

    public String goHome() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("home.jspx");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;
    }

    public void setVerifCodeLabel(RichOutputLabel verifCodeLabel) {
        this.verifCodeLabel = verifCodeLabel;
    }

    public RichOutputLabel getVerifCodeLabel() {
        return verifCodeLabel;
    }

    public void setVerifCode(RichInputText verifCode) {
        this.verifCode = verifCode;
    }

    public RichInputText getVerifCode() {
        return verifCode;
    }

    public void setUsernameLabel(RichOutputLabel usernameLabel) {
        this.usernameLabel = usernameLabel;
    }

    public RichOutputLabel getUsernameLabel() {
        return usernameLabel;
    }

    public void setSubVerifCode(RichCommandButton subVerifCode) {
        this.subVerifCode = subVerifCode;
    }

    public RichCommandButton getSubVerifCode() {
        return subVerifCode;
    }
}
