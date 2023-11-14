package TurnQuest.view.Accounts;


import java.math.BigDecimal;

import java.sql.Date;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;


public class WebUser implements SQLData {

    private BigDecimal ACCC_CODE;
    private BigDecimal ACCC_AGN_CODE;
    private String ACCC_NAME;
    private String ACCC_OTHER_NAMES;
    private Date ACCC_DOB;
    private String ACCC_TEL;
    private String ACCC_EMAIL_ADDR;
    private String ACCC_SMS_TEL;
    private String ACCC_USERNAME;
    private String ACCC_PWD;
    private String ACCC_LOGIN_ALLOWED;
    private Date ACCC_PWD_CHANGED;
    private String ACCC_PWD_RESET;
    private Date ACCC_DT_CREATED;
    private String ACCC_STATUS;
    private BigDecimal ACCC_LOGIN_ATTEMPTS;
    private String ACCC_PERSONEL_RANK;
    private Date ACCC_LAST_LOGIN_DATE;
    private String ACCC_CREATED_BY;
    private String sqltype = "TQ_CRM.TQC_ACCOUNT_CONTACTS_OBJ";

    public String getSQLTypeName() {
        return sqltype;
    }

    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        sqltype = typeName;
        ACCC_CODE = stream.readBigDecimal();
        ACCC_AGN_CODE = stream.readBigDecimal();
        ACCC_NAME = stream.readString();
        ACCC_OTHER_NAMES = stream.readString();
        ACCC_DOB = stream.readDate();
        ACCC_TEL = stream.readString();
        ACCC_EMAIL_ADDR = stream.readString();
        ACCC_SMS_TEL = stream.readString();
        ACCC_USERNAME = stream.readString();
        ACCC_PWD = stream.readString();
        ACCC_LOGIN_ALLOWED = stream.readString();
        ACCC_PWD_CHANGED = stream.readDate();
        ACCC_PWD_RESET = stream.readString();
        ACCC_DT_CREATED = stream.readDate();
        ACCC_STATUS = stream.readString();
        ACCC_LOGIN_ATTEMPTS = stream.readBigDecimal();
        ACCC_PERSONEL_RANK = stream.readString();
        ACCC_LAST_LOGIN_DATE = stream.readDate();
        ACCC_CREATED_BY = stream.readString();

    }

    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeBigDecimal(ACCC_CODE);
        stream.writeBigDecimal(ACCC_AGN_CODE);
        stream.writeString(ACCC_NAME);
        stream.writeString(ACCC_OTHER_NAMES);
        stream.writeDate(ACCC_DOB);
        stream.writeString(ACCC_TEL);
        stream.writeString(ACCC_EMAIL_ADDR);
        stream.writeString(ACCC_SMS_TEL);
        stream.writeString(ACCC_USERNAME);
        stream.writeString(ACCC_PWD);
        stream.writeString(ACCC_LOGIN_ALLOWED);
        stream.writeDate(ACCC_PWD_CHANGED);
        stream.writeString(ACCC_PWD_RESET);
        stream.writeDate(ACCC_DT_CREATED);
        stream.writeString(ACCC_STATUS);
        stream.writeBigDecimal(ACCC_LOGIN_ATTEMPTS);
        stream.writeString(ACCC_PERSONEL_RANK);
        stream.writeDate(ACCC_LAST_LOGIN_DATE);
        stream.writeString(ACCC_CREATED_BY);
    }

    public void setACCC_CODE(BigDecimal ACCC_CODE) {
        this.ACCC_CODE = ACCC_CODE;
    }

    public BigDecimal getACCC_CODE() {
        return ACCC_CODE;
    }

    public void setACCC_AGN_CODE(BigDecimal ACCC_AGN_CODE) {
        this.ACCC_AGN_CODE = ACCC_AGN_CODE;
    }

    public BigDecimal getACCC_AGN_CODE() {
        return ACCC_AGN_CODE;
    }

    public void setACCC_NAME(String ACCC_NAME) {
        this.ACCC_NAME = ACCC_NAME;
    }

    public String getACCC_NAME() {
        return ACCC_NAME;
    }

    public void setACCC_OTHER_NAMES(String ACCC_OTHER_NAMES) {
        this.ACCC_OTHER_NAMES = ACCC_OTHER_NAMES;
    }

    public String getACCC_OTHER_NAMES() {
        return ACCC_OTHER_NAMES;
    }

    public void setACCC_DOB(Date ACCC_DOB) {
        this.ACCC_DOB = ACCC_DOB;
    }

    public Date getACCC_DOB() {
        return ACCC_DOB;
    }

    public void setACCC_TEL(String ACCC_TEL) {
        this.ACCC_TEL = ACCC_TEL;
    }

    public String getACCC_TEL() {
        return ACCC_TEL;
    }

    public void setACCC_EMAIL_ADDR(String ACCC_EMAIL_ADDR) {
        this.ACCC_EMAIL_ADDR = ACCC_EMAIL_ADDR;
    }

    public String getACCC_EMAIL_ADDR() {
        return ACCC_EMAIL_ADDR;
    }

    public void setACCC_SMS_TEL(String ACCC_SMS_TEL) {
        this.ACCC_SMS_TEL = ACCC_SMS_TEL;
    }

    public String getACCC_SMS_TEL() {
        return ACCC_SMS_TEL;
    }

    public void setACCC_USERNAME(String ACCC_USERNAME) {
        this.ACCC_USERNAME = ACCC_USERNAME;
    }

    public String getACCC_USERNAME() {
        return ACCC_USERNAME;
    }

    public void setACCC_PWD(String ACCC_PWD) {
        this.ACCC_PWD = ACCC_PWD;
    }

    public String getACCC_PWD() {
        return ACCC_PWD;
    }

    public void setACCC_LOGIN_ALLOWED(String ACCC_LOGIN_ALLOWED) {
        this.ACCC_LOGIN_ALLOWED = ACCC_LOGIN_ALLOWED;
    }

    public String getACCC_LOGIN_ALLOWED() {
        return ACCC_LOGIN_ALLOWED;
    }

    public void setACCC_PWD_CHANGED(Date ACCC_PWD_CHANGED) {
        this.ACCC_PWD_CHANGED = ACCC_PWD_CHANGED;
    }

    public Date getACCC_PWD_CHANGED() {
        return ACCC_PWD_CHANGED;
    }

    public void setACCC_PWD_RESET(String ACCC_PWD_RESET) {
        this.ACCC_PWD_RESET = ACCC_PWD_RESET;
    }

    public String getACCC_PWD_RESET() {
        return ACCC_PWD_RESET;
    }

    public void setACCC_DT_CREATED(Date ACCC_DT_CREATED) {
        this.ACCC_DT_CREATED = ACCC_DT_CREATED;
    }

    public Date getACCC_DT_CREATED() {
        return ACCC_DT_CREATED;
    }

    public void setACCC_STATUS(String ACCC_STATUS) {
        this.ACCC_STATUS = ACCC_STATUS;
    }

    public String getACCC_STATUS() {
        return ACCC_STATUS;
    }

    public void setACCC_LOGIN_ATTEMPTS(BigDecimal ACCC_LOGIN_ATTEMPTS) {
        this.ACCC_LOGIN_ATTEMPTS = ACCC_LOGIN_ATTEMPTS;
    }

    public BigDecimal getACCC_LOGIN_ATTEMPTS() {
        return ACCC_LOGIN_ATTEMPTS;
    }

    public void setACCC_PERSONEL_RANK(String ACCC_PERSONEL_RANK) {
        this.ACCC_PERSONEL_RANK = ACCC_PERSONEL_RANK;
    }

    public String getACCC_PERSONEL_RANK() {
        return ACCC_PERSONEL_RANK;
    }

    public void setACCC_LAST_LOGIN_DATE(Date ACCC_LAST_LOGIN_DATE) {
        this.ACCC_LAST_LOGIN_DATE = ACCC_LAST_LOGIN_DATE;
    }

    public Date getACCC_LAST_LOGIN_DATE() {
        return ACCC_LAST_LOGIN_DATE;
    }

    public void setACCC_CREATED_BY(String ACCC_CREATED_BY) {
        this.ACCC_CREATED_BY = ACCC_CREATED_BY;
    }

    public String getACCC_CREATED_BY() {
        return ACCC_CREATED_BY;
    }
}
