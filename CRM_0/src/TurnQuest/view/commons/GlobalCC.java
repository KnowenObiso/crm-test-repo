package TurnQuest.view.commons;


import TurnQuest.view.Connect.DBConnector;

import java.awt.Dimension;

import java.io.IOException;
import java.io.InputStream;

import java.math.BigDecimal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichMessage;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.apps.xdo.delivery.DeliveryManager;
import oracle.apps.xdo.delivery.DeliveryPropertyDefinitions;
import oracle.apps.xdo.delivery.DeliveryRequest;

import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class GlobalCC {
    private static RichOutputLabel globalConfirmMsgValue;

    public GlobalCC() {
    }

    public static final String emailFrom = "";
    public static final String mailhost = "";

    protected static String dialogName;

    public static final int sysCode = 0;
    private static String userName = null;
    public static final String databaseError =
        "ERROR CONNECTING TO DATABASE: CONTACT YOUR SYSTEM ADMINISTRATOR";
    public static final String incorectLogin =
        "INVALID USERNAME AND/OR PASSWORD";
    public static final String manualProposalparameterError =
        "MISSING MANUAL/AUTOMATIC PARAMETER ERROR: CONTACT YOUR SYSTEM ADMINISTRATOR";

    public static final String selectTrans = "SELECT TRANSACTION TYPE";
    public static final String enterEffectiveDate = "ENTER THE EFFECTIVE DATE";
    public static final String selectProposal = "SELECT PROPOSAL";
    public static final String selectPolicy = "SELECT POLICY";
    public static final String selectPropPol = "SELECT PROPOSAL/POLICY";
    public static final String selectProduct = "SELECT PRODUCT";
    public static final String selectBranch = "SELECT BRANCH";
    public static final String selectClient = "SELECT CLIENT";
    public static final String selectOccupation = "SELECT OCCUPATION";
    public static final String enterAgeNextBirthday =
        "ENTER THE AGE NEXT BIRTHDAY(ANB)";
    public static final String selectPremiumMask = "SELECT THE PREMIUM MASK";
    public static final String selectProductOption =
        "SELECT THE PRODUCT OPTION";
    public static final String enterPremiumSumAssured =
        "ENTER THE PREMIUM OR THE SUM ASSURED";

    public static final String selectAgent = "SELECT AGENT";
    public static final String selectAssured = "SELECT ASSURED";
    public static final String selectlifeAssured = "SELECT LIFE ASSURED";
    public static final String selectPayMode = "SELECT PAYMENT METHOD";
    public static final String selectPayFrequency = "SELECT PAYMENT FREQUENCY";
    public static final String enterTerm = "ENTER PRODUCT TERM";


    public static final String success = "SUCCESS";
    public static final String failure = "FAILURE";
    public static final BigDecimal zeroValue = new BigDecimal(0);


    public static final String claimProcess = "CLM";
    public static final String loanProcess = "LOAN";

    protected static int screenWidth;
    protected static int screenHeight;

    private static RichMessage more;
    private static RichMessage errorMsg;
    private static RichMessage errCode;
    private static RichMessage errName;
    private static RichMessage errText;
    private static RichMessage syserrCode;
    private static RichMessage sysErrMsg;
    private static RichMessage rcmdendation;
    private static RichMessage errStack;
    private static RichMessage callStack;
    private static RichMessage envirment;
    private static RichPopup errorPop;

    public static OracleConnection getDatabaseConnection() {
        OracleConnection conn = null;

        try {
            String connectionString = null;
            Context initCtx = new InitialContext();
            Context envCtx = (Context)initCtx.lookup("java:comp/env");
            connectionString = (String)envCtx.lookup("conn");
            DataSource ds = (DataSource)envCtx.lookup(connectionString);

            conn = (OracleConnection)ds.getConnection();

            //userVaraibleInitialization(conn);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
        }


        return conn;
    }

    public static String applicationEmail(String sender, String receiver,
                                          String cc, String subject,
                                          String content) {

        String mailhost = null;

        OracleConnection conn;
        conn = getDatabaseConnection();
        String hrQuery3 =
            " SELECT PARAM_VALUE FROM TQC_PARAMETERS WHERE PARAM_NAME = 'MAILHOST'";
        OracleCallableStatement callStmt3 = null;
        try {
            callStmt3 = (OracleCallableStatement)conn.prepareCall(hrQuery3);
            OracleResultSet rs = (OracleResultSet)callStmt3.executeQuery();
            while (rs.next()) {
                mailhost = rs.getString(1);
            }
            if (mailhost == null) {
                GlobalCC.errorValueNotEntered("MAILHOST Not Defined Please Check");
                return null;
            }
            callStmt3.close();
            conn.close();

            // create delivery manager instance
            DeliveryManager dm = new DeliveryManager();
            // create a delivery request
            DeliveryRequest req =
                dm.createRequest(DeliveryManager.TYPE_SMTP_EMAIL);

            // set email subject
            req.addProperty(DeliveryPropertyDefinitions.SMTP_SUBJECT, subject);
            // set SMTP server host
            req.addProperty(DeliveryPropertyDefinitions.SMTP_HOST, mailhost);
            // set the sender email address
            req.addProperty(DeliveryPropertyDefinitions.SMTP_FROM, sender);
            // set the destination email address
            req.addProperty(DeliveryPropertyDefinitions.SMTP_TO_RECIPIENTS,
                            receiver);
            req.addProperty(DeliveryPropertyDefinitions.SMTP_CC_RECIPIENTS,
                            cc);
            // set the document to deliver
            // req.addProperty(DeliveryPropertyDefinitions.SMTP_CONTENT_TYPE, "text/html");
            // set the document file name appeared in the email
            // req.addProperty(DeliveryPropertyDefinitions.CONTENT, content);


            req.setDocument(content, "UTF-8");
            // submit the request
            req.submit();
            // close the request
            req.close();
        } catch (Exception e) {
            e.printStackTrace();
            EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public static java.sql.Date extractDate(RichInputDate component) {
        java.sql.Date val = null;
        try {
            val = new java.sql.Date(((Date)component.getValue()).getTime());
        } catch (Exception ex) {
            val = null;
        }
        return val;
    }

    public static String EXCEPTIONREPORTING(OracleConnection conn,
                                            Exception e) {
        
        
        e.printStackTrace();
        
        String errorMess = e.getMessage();
        if (errorMess == null) {
            errorMess = "Null Error Exception";
        }
        if (errorMess.contains("TQC_ERROR_MANAGER")) {
            try {
                if (conn != null) {
                    conn.commit();
                    String ckQuery =
                        "begin tqc_error_manager.get_error_info(?,?,?,?,?,?,?,?,?); end;";

                    OracleCallableStatement cst = null;

                    cst = (OracleCallableStatement)conn.prepareCall(ckQuery);
                    cst.registerOutParameter(1, OracleTypes.INTEGER);
                    cst.registerOutParameter(2, OracleTypes.VARCHAR);
                    cst.registerOutParameter(3, OracleTypes.VARCHAR);
                    cst.registerOutParameter(4, OracleTypes.INTEGER);
                    cst.registerOutParameter(5, OracleTypes.VARCHAR);
                    cst.registerOutParameter(6, OracleTypes.VARCHAR);
                    cst.registerOutParameter(7, OracleTypes.VARCHAR);
                    cst.registerOutParameter(8, OracleTypes.VARCHAR);
                    cst.registerOutParameter(9, OracleTypes.VARCHAR);
                    cst.execute();

                    if (cst.getBigDecimal(1) == null) {
                        nonerrorPkg(errorMess);
                        ExtendedRenderKitService erkService =
                            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                               ExtendedRenderKitService.class);
                        FacesContext context =
                            FacesContext.getCurrentInstance();
                        String cID = errorPop.getClientId(context);
                        erkService.addScript(FacesContext.getCurrentInstance(),
                                             "var hints = {autodismissNever:true}; " +
                                             "AdfPage.PAGE.findComponent('" +
                                             cID + "').show(hints);");
                    } else {
                        errorPkg(cst.getBigDecimal(1), cst.getString(2),
                                 cst.getString(3), cst.getBigDecimal(4),
                                 cst.getString(5), cst.getString(6),
                                 cst.getString(7), cst.getString(8),
                                 cst.getString(9));
                        ExtendedRenderKitService erkService =
                            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                               ExtendedRenderKitService.class);
                        FacesContext context =
                            FacesContext.getCurrentInstance();
                        String cID = errorPop.getClientId(context);
                        erkService.addScript(FacesContext.getCurrentInstance(),
                                             "var hints = {autodismissNever:true}; " +
                                             "AdfPage.PAGE.findComponent('" +
                                             cID + "').show(hints);");
                    }
                    cst.close();
                    conn.commit();
                    conn.close();
                } else {
                    String message = errorMess;
                    FacesContext.getCurrentInstance().addMessage(null,
                                                                 new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                                  message,
                                                                                  message));
                }


            } catch (Exception f) {
                String message = f.getMessage();
                FacesContext.getCurrentInstance().addMessage(null,
                                                             new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                              message,
                                                                              message));
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          errorMess,
                                                                          errorMess));

        }

        return null;
    }

    /**
     * Error For Raising Exception
     * @param exception
     * @return
     */
    public static String EXCEPTIONREPORTING(Exception exception) {

        exception.printStackTrace();
        String errMessage = exception.getMessage();
        if (FacesContext.getCurrentInstance() == null) {

            exception.printStackTrace();

        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          errMessage,
                                                                          errMessage));
        }

        return null;
    }

    public static String EXCEPTIONREPORTING(String exception) {


        if (FacesContext.getCurrentInstance() != null) {


            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          exception,
                                                                          exception));
        }

        return null;
    }

    /**
     * Error For Raising Information to Users.
     * @return
     */
    public static String INFORMATIONREPORTING(String errMessage) {
        if (FacesContext.getCurrentInstance() == null) {


        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                                          errMessage,
                                                                          errMessage));
        }

        return null;
    }


    /**
     *Function to raise error for Mandatory Values
     * @param errMessage
     * @return
     */

    public static String errorValueNotEntered(String errMessage) {
        if (FacesContext.getCurrentInstance() == null) {


        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          errMessage,
                                                                          errMessage));
        }

        return null;
    }


    public static String sysInformation(String errMessage) {
        if (FacesContext.getCurrentInstance() == null) {


        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                                          errMessage,
                                                                          errMessage));
        }

        return null;
    }


    /**
     *Function to Check for null Values
     * @param objName
     * @return
     */
    public static String checkNullValues(Object objName) {

        String objectValue = null;
        if (objName == null) {
            return null;

        } else {
            objectValue = objName.toString();
        }
        return objectValue;
    }

    /**
     *Function to ParseDate
     * @param somedate
     * @return
     */
    public static String parseDate(Object someDate) {

        Date date;
        date = null;
        String dateString;
        dateString = null;
        if (someDate == null)
            return null;
        String theDate = someDate != null ? someDate.toString() : null;

        SimpleDateFormat sdf1 =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");


        try {
            date = sdf1.parse(theDate);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        dateString = sdf2.format(date);

        return dateString;
    }

    public static String parseNormalDate(String somedate) {

        Date date;
        date = null;
        String dateString;
        dateString = null;
        SimpleDateFormat sdf1 =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");


        try {
            date = sdf1.parse(somedate);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        dateString = sdf2.format(date);

        return dateString;
    }

    /**
     *Function to Parse Month
     * @param somedate
     * @return
     */
    public static String parseMonthDate(String somedate) {

        Date date;
        date = null;
        String dateString;
        dateString = null;
        SimpleDateFormat sdf1 =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM");


        try {
            date = sdf1.parse(somedate);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        dateString = sdf2.format(date);

        return dateString;
    }


    /**
     *Function to Parse Year
     * @param somedate
     * @return
     */
    public static String parseYearDate(String somedate) {

        Date date;
        date = null;
        String dateString;
        dateString = null;
        SimpleDateFormat sdf1 =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");


        try {
            date = sdf1.parse(somedate);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        dateString = sdf2.format(date);

        return dateString;
    }

    public static String parseMonth(String somedate) {

        Date date;
        date = null;
        String dateString;
        dateString = null;
        SimpleDateFormat sdf1 =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MMM");


        try {
            date = sdf1.parse(somedate);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        dateString = sdf2.format(date);

        return dateString;
    }

    /**
     *Function to ParseDate(Updating date after Change in interface)
     * @param somedate
     * @return string
     */
    public static String upDateParseDate(String somedate) {

        Date date;
        date = null;
        String dateString;
        dateString = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MMMM/yyyy");


        try {
            date = sdf1.parse(somedate);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                          e.getMessage(),
                                                                          e.getMessage()));
        }
        dateString = sdf2.format(date);

        return dateString;
    }


    /**
     *Function to Decode various Payment Frequencies
     * @param freq
     * @return
     */
    public static String decodePaymentFrequency(String freq) {

        String payFreq = null;
        if (freq == null) {

        } else if (freq.equalsIgnoreCase("M")) {
            payFreq = "MONTHLY";
        } else if (freq.equalsIgnoreCase("Q")) {
            payFreq = "QUARTERLY";
        } else if (freq.equalsIgnoreCase("S")) {
            payFreq = "SEMI-ANNUALY";
        } else if (freq.equalsIgnoreCase("A")) {
            payFreq = "ANNUALLY";
        } else if (freq.equalsIgnoreCase("F")) {
            payFreq = "SINGLE PREMIUM";
        }

        return payFreq;

    }

    public static String decodeUserType(String usrType) {

        String type = null;
        if (usrType == null) {

        } else if (usrType.equalsIgnoreCase("U")) {
            type = "User";
        } else if (usrType.equalsIgnoreCase("G")) {
            type = "Group";
        }

        return type;

    }

    public static String decodeUserStatus(String usrStatus) {

        String type = null;
        if (usrStatus == null) {

        } else if (usrStatus.equalsIgnoreCase("A")) {
            type = "Active";
        } else {
            type = "InActive";
        }

        return type;

    }

    public static String decodeReset(String reset) {

        String type = null;
        if (reset == null) {

        } else if (reset.equalsIgnoreCase("N")) {
            type = "No";
        } else {
            type = "Yes";
        }

        return type;

    }

    /**
     *Function to Decode beneficiaries
     * @param ben
     * @return
     */
    public static String decodeBeneficiary(String ben) {

        String returnVal = null;
        if (ben == null) {

        } else if (ben.equalsIgnoreCase("B")) {
            returnVal = "Beneficiary";
        } else {
            returnVal = "Contigent Beneficiary";
        }

        return returnVal;
    }

    public static String decodeSex(String sex) {

        String returnVal = null;
        if (sex == null) {

        } else if (sex.equalsIgnoreCase("M")) {
            returnVal = "Male";
        } else {
            returnVal = "Female";
        }

        return returnVal;
    }

    public static String decodeYesNo(String Value) {

        String yesNoVal = null;
        if (Value == null) {

        } else if (Value.equalsIgnoreCase("Y")) {
            yesNoVal = "Yes";
        } else {
            yesNoVal = "No";
        }

        return yesNoVal;
    }

    /**
     *Function to Decode the various Payment Modes
     * @param mode
     * @return
     */
    public static String decodePaymentMode(String mode) {

        String payMode = null;
        if (mode == null) {

        } else if (mode.equalsIgnoreCase("C")) {
            payMode = "CASH";
        } else if (mode.equalsIgnoreCase("Q")) {
            payMode = "CHEQUE";
        } else if (mode.equalsIgnoreCase("S")) {
            payMode = "STANDING ORDER";
        } else if (mode.equalsIgnoreCase("K")) {
            payMode = "CHECKOFF";
        } else if (mode.equalsIgnoreCase("R")) {
            payMode = "STAFF REMITANCE";
        } else if (mode.equalsIgnoreCase("DD")) {
            payMode = "DIRECT DEBIT";
        }

        return payMode;

    }


    /**
     *Funtion to Decode Y(Yes) or N(No) Values
     * @param val
     * @return
     */
    public static String decodeYesNoValues(String val) {

        String retunVal = null;
        if (val == null) {

        } else if (val.equalsIgnoreCase("Y")) {
            retunVal = "YES";
        } else if (val.equalsIgnoreCase("N")) {
            retunVal = "NO";
        }

        return retunVal;
    }


    /**
     *Function to decode the various Endorsement Statuses
     * @param status
     * @return
     */
    public static String decodeEndorsementStatuses(String status) {

        String decodedStatus = null;
        if (status == null) {

        } else if (status.equalsIgnoreCase("D")) {
            decodedStatus = "Draft";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("A")) {
            decodedStatus = "Active";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("E")) {
            decodedStatus = "Endorsed";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("RN")) {
            decodedStatus = "Renewed";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("C")) {
            decodedStatus = "Cancelled";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("T")) {
            decodedStatus = "Contra'd";
            return decodedStatus;
        }
        return null;


    }

    public static String decodeMedicalsStatuses(String status) {

        String decodedStatus = null;
        if (status == null) {

        } else if (status.equalsIgnoreCase("E")) {
            decodedStatus = "Excempted";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("Y")) {
            decodedStatus = "Yes";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("N")) {
            decodedStatus = "No";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("C")) {
            decodedStatus = "Cancelled";
            return decodedStatus;
        }

        return decodedStatus;


    }


    /**
     *Function to Decode the various Policy Statuses
     * @param status
     * @return
     */
    public static String decodePolicyStatuses(String status) {

        String decodedStatus = null;
        if (status == null) {

        } else if (status.equalsIgnoreCase("D")) {
            decodedStatus = "Draft";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("A")) {
            decodedStatus = "Active";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("L")) {
            decodedStatus = "Lapsed";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("R")) {
            decodedStatus = "Reinstated";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("C")) {
            decodedStatus = "Cancelled";
            return decodedStatus;
        } else if (status.equalsIgnoreCase("J")) {
            decodedStatus = "Rejected/Declined";
            return decodedStatus;
        }
        return null;


    }

    /**
     * Get Netxt Day
     */
    public void nextDay() {
        Date currentNextDate = new Date();
        Date currentPreviousDate = new Date();


        try {

            Calendar cal = Calendar.getInstance();
            cal.setTime(currentNextDate);
            cal.add(Calendar.MONTH, 2);
            cal.getTime();

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(currentPreviousDate);
            cal2.add(Calendar.MONTH, -2);
            cal2.getTime();


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
    }


    public static String nonerrorPkg(String message) {

        errText.setMessage("<html><b>ERROR NAME:</b> </html>" + message + " ");
        sysErrMsg.setMessage("<html><b>SYSTEM ERROR MESSAGE:</b>" + message +
                             " ");
        return null;
    }

    public static String errorPkg(BigDecimal errorCode, String errNameVal,
                                  String errTextVal, BigDecimal syserrCodeVal,
                                  String sysErrMsgVal, String rcmendVal,
                                  String errStackVal, String callStackVal,
                                  String enviVal) {
        String errCodeVal = null;
        String sysErrCodeValue = null;
        if (errorCode == null) {

        } else {
            errCodeVal = errorCode.toString();
        }
        if (syserrCodeVal != null) {
            sysErrCodeValue = syserrCodeVal.toString();
        }
        errCode.setMessage("<html><b>ERROR CODE: </b> </html>" + errCodeVal +
                           " ");
        errName.setMessage("<html><b>ERROR NAME: </b> </html>" + errNameVal +
                           " ");
        errText.setMessage("<html><b>ERROR TEXT: </b> </html>" + errTextVal +
                           " ");
        syserrCode.setMessage("<html><b>SYSTEM ERROR CODE: </b> </html>" +
                              sysErrCodeValue + " ");
        sysErrMsg.setMessage("<html><b>SYSTEM ERROR MESSAGE: </b> </html>" +
                             sysErrMsgVal + " ");
        rcmdendation.setMessage("<html><b>RECOMMENDATION: </b> </html>" +
                                rcmendVal + " ");
        errStack.setMessage("<html><b>ERROR STACK:</b> </html> " +
                            errStackVal + " ");
        callStack.setMessage("<html><b>ERROR CALL STACK:</b> </html> " +
                             callStackVal + " ");
        envirment.setMessage("<html><b>ENVIRONMENT:</b> </html> " + enviVal +
                             " ");
        return null;
    }


    public void setMore(RichMessage more) {
        this.more = more;
    }

    public RichMessage getMore() {
        return more;
    }

    public void setErrorMsg(RichMessage errorMsg) {
        this.errorMsg = errorMsg;
    }

    public RichMessage getErrorMsg() {
        return errorMsg;
    }

    public void setErrCode(RichMessage errCode) {
        this.errCode = errCode;
    }

    public RichMessage getErrCode() {
        return errCode;
    }

    public void setErrName(RichMessage errName) {
        this.errName = errName;
    }

    public RichMessage getErrName() {
        return errName;
    }

    public void setErrText(RichMessage errText) {
        this.errText = errText;
    }

    public RichMessage getErrText() {
        return errText;
    }

    public void setSyserrCode(RichMessage syserrCode) {
        this.syserrCode = syserrCode;
    }

    public RichMessage getSyserrCode() {
        return syserrCode;
    }

    public void setSysErrMsg(RichMessage sysErrMsg) {
        this.sysErrMsg = sysErrMsg;
    }

    public RichMessage getSysErrMsg() {
        return sysErrMsg;
    }

    public void setRcmdendation(RichMessage rcmdendation) {
        this.rcmdendation = rcmdendation;
    }

    public RichMessage getRcmdendation() {
        return rcmdendation;
    }

    public void setErrStack(RichMessage errStack) {
        this.errStack = errStack;
    }

    public RichMessage getErrStack() {
        return errStack;
    }

    public void setCallStack(RichMessage callStack) {
        this.callStack = callStack;
    }

    public RichMessage getCallStack() {
        return callStack;
    }

    public void setEnvirment(RichMessage envirment) {
        this.envirment = envirment;
    }

    public RichMessage getEnvirment() {
        return envirment;
    }


    public void setErrorPop(RichPopup errorPop) {
        GlobalCC.errorPop = errorPop;
    }

    public RichPopup getErrorPop() {
        return errorPop;
    }

    public static void CONFIRMACTION(String confirmMessage) {

        globalConfirmMsgValue.setValue(confirmMessage);

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "attrs:globalConfirmDialogPop" +
                             "').show(hints);");
    }

    public Dimension getJPEGDimension(InputStream fis) throws IOException {


        // check for SOI marker
        if (fis.read() != 255 || fis.read() != 216)
            throw new RuntimeException("SOI (Start Of Image) marker 0xff 0xd8 missing");

        Dimension d = null;

        while (fis.read() == 255) {
            int marker = fis.read();
            int len = fis.read() << 8 | fis.read();

            if (marker == 192) {
                fis.skip(1);

                int height = fis.read() << 8 | fis.read();
                int width = fis.read() << 8 | fis.read();

                d = new Dimension(width, height);
                break;
            }

            fis.skip(len - 2);
        }

        fis.close();

        return d;
    }

    public static Boolean validateUploadedImg(UploadedFile file,
                                              String imgName) {
        // Add event code here...
        UploadedFile _file = file;


        if (_file.getContentType().equalsIgnoreCase("image/jpeg") ||
            _file.getContentType().equalsIgnoreCase("image/gif") ||
            _file.getContentType().equalsIgnoreCase("image/png")) {


            InputStream Reader;

            try {
                long Val = _file.getLength();

                if (Val == 0) {

                    return false;
                }
                Reader = _file.getInputStream();
                double w = 0.00;
                double h = 0.00;
                GlobalCC xv = new GlobalCC();
                Dimension d = xv.getJPEGDimension(Reader);
                if (d != null) {
                    w = d.getWidth();
                    h = d.getHeight();


                }
                if (w <= 150.00 && h <= 150.00) {

                    return true;

                } else {
                    GlobalCC.INFORMATIONREPORTING("ERROR:" + imgName +
                                                  " cannot be Saved   :: Image size greater than '150 x 150 px' ");

                    return false;
                }
                // InsertGroupImage(Reader, Val);
            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.INFORMATIONREPORTING("OOPS! ERROR: OCCURED WHILE SAVING" +
                                              imgName +
                                              " :Try Uploading it Again ");
                return false;
            }

        } else {

            //GlobalCC.refreshUI(uploadOrgGrpImg);
            GlobalCC.INFORMATIONREPORTING("ERROR:" + imgName +
                                          " cannot be saved  :: Ensure the file  type is  .jpg/.gif/.png ");
            return false;
        }
    }

    public static Boolean validateUploadedImgFile(InputStream file,
                                                  String imgName,
                                                  String content) {
        // Add event code here...
        // UploadedFile _file = file;


        if (content.equalsIgnoreCase("image/jpeg") ||
            content.equalsIgnoreCase("image/gif") ||
            content.equalsIgnoreCase("image/png")) {


            try {

                double w = 0.00;
                double h = 0.00;
                GlobalCC xv = new GlobalCC();
                Dimension d = xv.getJPEGDimension(file);
                if (d != null) {
                    w = d.getWidth();
                    h = d.getHeight();


                }
                if (w <= 150.00 && h <= 150.00) {

                    return true;

                } else {
                    GlobalCC.INFORMATIONREPORTING("ERROR:" + imgName +
                                                  " cannot be Saved   :: Image size greater than '150 x 150 px' ");

                    return false;
                }
                // InsertGroupImage(Reader, Val);
            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.INFORMATIONREPORTING("OOPS! ERROR: OCCURED WHILE SAVING" +
                                              imgName +
                                              " :Try Uploading it Again ");
                return false;
            }

        } else {

            //GlobalCC.refreshUI(uploadOrgGrpImg);
            GlobalCC.INFORMATIONREPORTING("ERROR:" + imgName +
                                          " cannot be save  :: Ensure the file  type is  .jpg/.gif/.png ");
            return false;
        }
    }


    public void setGlobalConfirmMsgValue(RichOutputLabel globalConfirmMsgValue) {
        this.globalConfirmMsgValue = globalConfirmMsgValue;
    }

    public RichOutputLabel getGlobalConfirmMsgValue() {
        return globalConfirmMsgValue;
    }

    public static void deselectAll(RichTable table,
                                   RichSelectBooleanCheckbox columnBinding,
                                   String attribute,
                                   RichCommandButton selectDeselectButton) {

        if (checkIfAnyTableRowselected(table, attribute)) {
            int rowcount = table.getRowCount();


            for (int i = 0; i < rowcount; i++) {
                table.setRowIndex(i);
                Object key = table.getRowKey();
                table.setRowKey(key);


                columnBinding.setSelected(false);
                GlobalCC.refreshUI(columnBinding);
                selectDeselectButton.setText("Select All");
                GlobalCC.refreshUI(selectDeselectButton);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record(s) selected::");
        }


    }

    public static boolean checkIfAnyTableRowselected(RichTable table,
                                                     String attribute) {


        int rowcount = table.getRowCount();

        int count = 0;
        for (int i = 0; i < rowcount; i++) {
            table.setRowIndex(i);
            Object key = table.getRowKey();
            table.setRowKey(key);
            JUCtrlHierNodeBinding nodeBinding =
                (JUCtrlHierNodeBinding)table.getRowData();
            if (nodeBinding != null) {
                if (nodeBinding.getAttribute(attribute) != null) {
                    if (nodeBinding.getAttribute(attribute).toString().equalsIgnoreCase("true")) {
                        count = count + 1;
                    }
                }
            }
        }

        if (count > 0) {
            return true;
        } else {
            return false;
        }

    }

    public static void selectAll(RichTable table,
                                 RichSelectBooleanCheckbox columnBinding,
                                 RichCommandButton selectDeselectButton) {


        int rowcount = table.getRowCount();


        for (int i = 0; i < rowcount; i++) {
            table.setRowIndex(i);
            Object key = table.getRowKey();
            table.setRowKey(key);
            columnBinding.setSelected(true);
            GlobalCC.refreshUI(columnBinding);
            selectDeselectButton.setText("Unselect All");
            refreshUI(selectDeselectButton);
        }


    }

    public static void dismissPopUp(String templateId, String popUpId) {
        if (templateId != null & popUpId != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" + templateId +
                                 ":" + popUpId + "').hide(hints);");
        }
    }

    public static void showPopUp(String templateId, String popUpId) {
        if (templateId != null & popUpId != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" + templateId +
                                 ":" + popUpId + "').show(hints);");
        }
    }

    public static boolean validateWefWetValues(Object wef, Object wet) {
        if (wef != null && wet == null)
            return true;
        if (wef == null && wet == null)
            return true;

        if (wef == null && wet != null)
            return false;
        //when none of the values is null
        Date someWef = (Date)wef;
        Date someWet = (Date)wet;
        if (someWef.compareTo(someWet) <= 0)
            return true;
        return false;
    }


    public static String getBusinessDate(String ddBookDate) {
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := Direct_Debit_Pkg.Get_business_date(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        String failDate = null;

        try {
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (ddBookDate != null) {
                statement.registerOutParameter(1, OracleTypes.VARCHAR);
                statement.setString(2, ddBookDate);
                statement.setBigDecimal(3,
                                        new BigDecimal("4")); // 4 is hardcoded on the V3 form
                statement.execute();
                failDate = statement.getString(1);
                statement.close();
                connection.close();
            } else {
                return null;
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return failDate;
    }

    public static String accessDenied() {
        String errMessage =
            "Access Denied.Insufficient Rights.Contact your Systems Administrator";
        if (FacesContext.getCurrentInstance() == null) {


        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                                          errMessage,
                                                                          errMessage));
        }

        return null;
    }

    public static String getEmailFrom() {


        DBConnector dbConnector = new DBConnector();
        String query =
            "SELECT TQC_PARAMETERS_PKG.get_param_varchar('EMAILS_FROM') from dual";
        Statement statement = null;
        OracleConnection connection = null;
        String emailFrom = null;

        try {
            connection = dbConnector.getDatabaseConnection();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                emailFrom = rs.getString(1);
            }
            statement.close();
            connection.close();
        }

        catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }


        return emailFrom;

    }

    public static String formatAdminUnitPlural(Object adminUnitSymbol) {
        if (adminUnitSymbol == null)

            return "Administrative Units";
        else if (adminUnitSymbol.toString().equalsIgnoreCase("S"))
            return "States";
        else if (adminUnitSymbol.toString().equalsIgnoreCase("C"))
            return "Counties";
        if (adminUnitSymbol.toString().equalsIgnoreCase("P"))
            return "Provinces";
        return null;
    }

    public static String formatAdminUnitSingular(Object adminUnitSymbol) {
        if (adminUnitSymbol == null)

            return "Administrative Unit";
        else if (adminUnitSymbol.toString().equalsIgnoreCase("S"))
            return "State";
        else if (adminUnitSymbol.toString().equalsIgnoreCase("C"))
            return "County";
        else if (adminUnitSymbol.toString().equalsIgnoreCase("P"))
            return "Province";
        return null;
    }

    public static HashMap getLoggedInUserDetails() {

        HashMap userDetailsMap = new HashMap();
        OracleConnection conn = getDatabaseConnection();

        String query = "begin TQC_ROLES_CURSOR.getFullUserDetails(?,?);end;";
        OracleCallableStatement stmt = null;
        ;


        try {
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.setObject(1, userName);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();
            ResultSet rs = (ResultSet)stmt.getObject(2);
            while (rs.next()) {
                userDetailsMap.put("UserCode", rs.getString(1));
                userDetailsMap.put("UserName", rs.getString(2));
                userDetailsMap.put("UserFullName", rs.getString(3));
                userDetailsMap.put("UserEmail", rs.getString(4));
                userDetailsMap.put("UserPersonnelRank", rs.getString(5));
                userDetailsMap.put("UserDateCreated", rs.getString(6));
                userDetailsMap.put("UserType", rs.getString(7));
                userDetailsMap.put("UserStatus", rs.getString(8));
                userDetailsMap.put("UserPasswordReset", rs.getString(9));
                userDetailsMap.put("UserPersonnelId", rs.getString(10));

            }
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }

        return userDetailsMap;
    }


    public static void setUserName(String userName) {
        GlobalCC.userName = userName;
    }

    public static String getUserName() {
        return userName;
    }
  public static void refreshUI(UIComponent o) {
      try{
          AdfFacesContext ctx=AdfFacesContext.getCurrentInstance();
          if(ctx!=null && o!=null){
              ctx.addPartialTarget(o);
          }
      }catch(Exception e){
         TurnQuest.view.Base.GlobalCC.EXCEPTIONREPORTING(e);
      }
  }
    public static BigDecimal checkBDNullValues(Object objName) {

        BigDecimal objectValue = null;
        if (objName == null) {

        } else {
            String val = null;
            val = objName.toString();
            if (val != null) {
                if(val.matches("[0-9\\.]+")) {
                    objectValue = new BigDecimal(val);
                }
            }
        }
        return objectValue;
    }
}

