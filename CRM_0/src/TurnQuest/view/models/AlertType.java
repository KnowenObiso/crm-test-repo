package TurnQuest.view.models;


import java.math.BigDecimal;


public class AlertType {
    private BigDecimal ALRT_CODE;
    private String ALRT_TYPE;
    private BigDecimal ALRT_SYS_CODE;
    private String ALRT_EMAIL;
    private String ALRT_SMS;
    private String sysName;
    private String SQLTypeName;
    private String alrtshtDesc;

    private String alertScreen;
    private BigDecimal alertEmailMsgCode;
    private BigDecimal alertSmsMsgtCode;
    private BigDecimal alertGrpUsrCode;
    private String alertEmail;
    private String alertSms;
    private String grpUsers;
    //da_code, da_alert_code, da_dispatch_doc_code, da_sht_desc, da_desc
    private BigDecimal docCode;
    private BigDecimal dispatchDocCode;
    private String docShtDesc;
    private String docDescription;
    private BigDecimal daAlertCode;
    private String dispatchDocRptDesc;
    

    private String alertCheckAlert;

    public AlertType() {
        super();
    }


    public void setSQLTypeName(String SQLTypeName) {
        this.SQLTypeName = SQLTypeName;
    }

    public String getSQLTypeName() {
        return SQLTypeName;
    }

    public void setALRT_CODE(BigDecimal ALRT_CODE) {
        this.ALRT_CODE = ALRT_CODE;
    }

    public BigDecimal getALRT_CODE() {
        return ALRT_CODE;
    }

    public void setALRT_TYPE(String ALRT_TYPE) {
        this.ALRT_TYPE = ALRT_TYPE;
    }

    public String getALRT_TYPE() {
        return ALRT_TYPE;
    }

    public void setALRT_SYS_CODE(BigDecimal ALRT_SYS_CODE) {
        this.ALRT_SYS_CODE = ALRT_SYS_CODE;
    }

    public BigDecimal getALRT_SYS_CODE() {
        return ALRT_SYS_CODE;
    }

    public void setALRT_EMAIL(String ALRT_EMAIL) {
        this.ALRT_EMAIL = ALRT_EMAIL;
    }

    public String getALRT_EMAIL() {
        return ALRT_EMAIL;
    }

    public void setALRT_SMS(String ALRT_SMS) {
        this.ALRT_SMS = ALRT_SMS;
    }

    public String getALRT_SMS() {
        return ALRT_SMS;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysName() {
        return sysName;
    }

    public void setAlertGrpUsrCode(BigDecimal alertGrpUsrCode) {
        this.alertGrpUsrCode = alertGrpUsrCode;
    }

    public BigDecimal getAlertGrpUsrCode() {
        return alertGrpUsrCode;
    }

    public void setAlertScreen(String alertScreen) {
        this.alertScreen = alertScreen;
    }

    public String getAlertScreen() {
        return alertScreen;
    }

    public void setAlertEmailMsgCode(BigDecimal alertEmailMsgCode) {
        this.alertEmailMsgCode = alertEmailMsgCode;
    }

    public BigDecimal getAlertEmailMsgCode() {
        return alertEmailMsgCode;
    }

    public void setAlertSmsMsgtCode(BigDecimal alertSmsMsgtCode) {
        this.alertSmsMsgtCode = alertSmsMsgtCode;
    }

    public BigDecimal getAlertSmsMsgtCode() {
        return alertSmsMsgtCode;
    }

    public void setAlertEmail(String alertEmail) {
        this.alertEmail = alertEmail;
    }

    public String getAlertEmail() {
        return alertEmail;
    }

    public void setAlertSms(String alertSms) {
        this.alertSms = alertSms;
    }

    public String getAlertSms() {
        return alertSms;
    }

    public void setGrpUsers(String grpUsers) {
        this.grpUsers = grpUsers;
    }

    public String getGrpUsers() {
        return grpUsers;
    }

    public void setAlertCheckAlert(String alertCheckAlert) {
        this.alertCheckAlert = alertCheckAlert;
    }

    public String getAlertCheckAlert() {
        return alertCheckAlert;
    }

    public void setAlrtshtDesc(String alrtshtDesc) {
        this.alrtshtDesc = alrtshtDesc;
    }

    public String getAlrtshtDesc() {
        return alrtshtDesc;
    }

    public void setDocCode(BigDecimal docCode) {
        this.docCode = docCode;
    }

    public BigDecimal getDocCode() {
        return docCode;
    }

    public void setDispatchDocCode(BigDecimal dispatchDocCode) {
        this.dispatchDocCode = dispatchDocCode;
    }

    public BigDecimal getDispatchDocCode() {
        return dispatchDocCode;
    }

    public void setDocShtDesc(String docShtDesc) {
        this.docShtDesc = docShtDesc;
    }

    public String getDocShtDesc() {
        return docShtDesc;
    }

    public void setDocDescription(String docDescription) {
        this.docDescription = docDescription;
    }

    public String getDocDescription() {
        return docDescription;
    }

    public void setDaAlertCode(BigDecimal daAlertCode) {
        this.daAlertCode = daAlertCode;
    }

    public BigDecimal getDaAlertCode() {
        return daAlertCode;
    }

    public void setDispatchDocRptDesc(String dispatchDocRptDesc) {
        this.dispatchDocRptDesc = dispatchDocRptDesc;
    }

    public String getDispatchDocRptDesc() {
        return dispatchDocRptDesc;
    }
}
