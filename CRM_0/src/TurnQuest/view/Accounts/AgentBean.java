package TurnQuest.view.Accounts;

import java.math.BigDecimal;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;


public class AgentBean implements SQLData {

    private String sql_type = "TQC_AGENCIES_TAB";
    private BigDecimal AGN_CODE;
    private BigDecimal AGN_ACT_CODE;
    private String AGN_SHT_DESC;
    private String AGN_NAME;
    private String AGN_PHYSICAL_ADDRESS;
    private String AGN_POSTAL_ADDRESS;
    private BigDecimal AGN_TWN_CODE;
    private BigDecimal AGN_COU_CODE;
    private String AGN_EMAIL_ADDRESS;
    private String AGN_WEB_ADDRESS;
    private String AGN_ZIP;
    private String AGN_CONTACT_PERSON;
    private String AGN_CONTACT_TITLE;
    private String AGN_TEL1;
    private String AGN_TEL2;
    private String AGN_FAX;
    private String AGN_ACC_NO;
    private String AGN_PIN;
    private BigDecimal AGN_AGENT_COMMISSION;
    private String AGN_CREDIT_ALLOWED;
    private BigDecimal AGN_AGENT_WHT_TAX;
    private String AGN_PRINT_DBNOTE;
    private String AGN_STATUS;
    private String AGN_DATE_CREATED;
    private String AGN_CREATED_BY;
    private String AGN_REG_CODE;
    private BigDecimal AGN_COMM_RESERVE_RATE;
    private BigDecimal AGN_ANNUAL_BUDGET;
    private String AGN_STATUS_EFF_DATE;
    private BigDecimal AGN_CREDIT_PERIOD;
    private String AGN_COMM_STAT_EFF_DT;
    private String AGN_COMM_STATUS_DT;
    private String AGN_COMM_ALLOWED;
    private String AGN_CHECKED;
    private String AGN_CHECKED_BY;
    private String AGN_CHECK_DATE;
    private String AGN_COMP_COMM_ARREARS;
    private String AGN_REINSURER;
    private BigDecimal AGN_BRN_CODE;
    private String AGN_TOWN;
    private String AGN_COUNTRY;
    private String AGN_STATUS_DESC;
    private String AGN_ID_NO;
    private String AGN_CON_CODE;
    private BigDecimal AGN_AGN_CODE;
    private String AGN_SMS_TEL;
    private BigDecimal AGN_AHC_CODE;
    private BigDecimal AGN_SEC_CODE;
    private String AGN_AGNC_CLASS_CODE;
    private String AGN_EXPIRY_DATE;
    private String AGN_LICENSE_NO;
    private String AGN_RUNOFF;
    private String AGN_LICENSED;
    private BigDecimal AGN_LICENSE_GRACE_PR;
    private String AGN_OLD_ACC_NO;
    private String AGN_STATUS_REMARKS;


    public String getSQLTypeName() {
        return "TQ_CRM.TQC_AGENCIES_OBJ";
    }

    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        sql_type = typeName;
        AGN_CODE = stream.readBigDecimal();
        AGN_ACT_CODE = stream.readBigDecimal();
        AGN_SHT_DESC = stream.readString();
        AGN_NAME = stream.readString();
        AGN_PHYSICAL_ADDRESS = stream.readString();
        AGN_POSTAL_ADDRESS = stream.readString();
        AGN_TWN_CODE = stream.readBigDecimal();
        AGN_COU_CODE = stream.readBigDecimal();
        AGN_EMAIL_ADDRESS = stream.readString();
        AGN_WEB_ADDRESS = stream.readString();
        AGN_ZIP = stream.readString();
        AGN_CONTACT_PERSON = stream.readString();
        AGN_CONTACT_TITLE = stream.readString();
        AGN_TEL1 = stream.readString();
        AGN_TEL2 = stream.readString();
        AGN_FAX = stream.readString();
        AGN_ACC_NO = stream.readString();
        AGN_PIN = stream.readString();
        AGN_AGENT_COMMISSION = stream.readBigDecimal();
        AGN_CREDIT_ALLOWED = stream.readString();
        AGN_AGENT_WHT_TAX = stream.readBigDecimal();
        AGN_PRINT_DBNOTE = stream.readString();
        AGN_STATUS = stream.readString();
        AGN_DATE_CREATED = stream.readString();
        AGN_CREATED_BY = stream.readString();
        AGN_REG_CODE = stream.readString();
        AGN_COMM_RESERVE_RATE = stream.readBigDecimal();
        AGN_ANNUAL_BUDGET = stream.readBigDecimal();
        AGN_STATUS_EFF_DATE = stream.readString();
        AGN_CREDIT_PERIOD = stream.readBigDecimal();
        AGN_COMM_STAT_EFF_DT = stream.readString();
        AGN_COMM_STATUS_DT = stream.readString();
        AGN_COMM_ALLOWED = stream.readString();
        AGN_CHECKED = stream.readString();
        AGN_CHECKED_BY = stream.readString();
        AGN_CHECK_DATE = stream.readString();
        AGN_COMP_COMM_ARREARS = stream.readString();
        AGN_REINSURER = stream.readString();
        AGN_BRN_CODE = stream.readBigDecimal();
        AGN_TOWN = stream.readString();
        AGN_COUNTRY = stream.readString();
        AGN_STATUS_DESC = stream.readString();
        AGN_ID_NO = stream.readString();
        AGN_CON_CODE = stream.readString();
        AGN_AGN_CODE = stream.readBigDecimal();
        AGN_SMS_TEL = stream.readString();
        AGN_AHC_CODE = stream.readBigDecimal();
        AGN_SEC_CODE = stream.readBigDecimal();
        AGN_AGNC_CLASS_CODE = stream.readString();
        AGN_EXPIRY_DATE = stream.readString();
        AGN_LICENSE_NO = stream.readString();
        AGN_RUNOFF = stream.readString();
        AGN_LICENSED = stream.readString();
        AGN_LICENSE_GRACE_PR = stream.readBigDecimal();
        AGN_OLD_ACC_NO = stream.readString();
        AGN_STATUS_REMARKS = stream.readString();

    }

    public void writeSQL(SQLOutput stream) throws SQLException {

        stream.writeBigDecimal(AGN_CODE);
        stream.writeBigDecimal(AGN_ACT_CODE);
        stream.writeString(AGN_SHT_DESC);
        stream.writeString(AGN_NAME);
        stream.writeString(AGN_PHYSICAL_ADDRESS);
        stream.writeString(AGN_POSTAL_ADDRESS);
        stream.writeBigDecimal(AGN_TWN_CODE);
        stream.writeBigDecimal(AGN_COU_CODE);
        stream.writeString(AGN_EMAIL_ADDRESS);
        stream.writeString(AGN_WEB_ADDRESS);
        stream.writeString(AGN_ZIP);
        stream.writeString(AGN_CONTACT_PERSON);
        stream.writeString(AGN_CONTACT_TITLE);
        stream.writeString(AGN_TEL1);
        stream.writeString(AGN_TEL2);
        stream.writeString(AGN_FAX);
        stream.writeString(AGN_ACC_NO);
        stream.writeString(AGN_PIN);
        stream.writeBigDecimal(AGN_AGENT_COMMISSION);
        stream.writeString(AGN_CREDIT_ALLOWED);
        stream.writeBigDecimal(AGN_AGENT_WHT_TAX);
        stream.writeString(AGN_PRINT_DBNOTE);
        stream.writeString(AGN_STATUS);
        stream.writeString(AGN_DATE_CREATED);
        stream.writeString(AGN_CREATED_BY);
        stream.writeString(AGN_REG_CODE);
        stream.writeBigDecimal(AGN_COMM_RESERVE_RATE);
        stream.writeBigDecimal(AGN_ANNUAL_BUDGET);
        stream.writeString(AGN_STATUS_EFF_DATE);
        stream.writeBigDecimal(AGN_CREDIT_PERIOD);
        stream.writeString(AGN_COMM_STAT_EFF_DT);
        stream.writeString(AGN_COMM_STATUS_DT);
        stream.writeString(AGN_COMM_ALLOWED);
        stream.writeString(AGN_CHECKED);
        stream.writeString(AGN_CHECKED_BY);
        stream.writeString(AGN_CHECK_DATE);
        stream.writeString(AGN_COMP_COMM_ARREARS);
        stream.writeString(AGN_REINSURER);
        stream.writeBigDecimal(AGN_BRN_CODE);
        stream.writeString(AGN_TOWN);
        stream.writeString(AGN_COUNTRY);
        stream.writeString(AGN_STATUS_DESC);
        stream.writeString(AGN_ID_NO);
        stream.writeString(AGN_CON_CODE);
        stream.writeBigDecimal(AGN_AGN_CODE);
        stream.writeString(AGN_SMS_TEL);
        stream.writeBigDecimal(AGN_AHC_CODE);
        stream.writeBigDecimal(AGN_SEC_CODE);
        stream.writeString(AGN_AGNC_CLASS_CODE);
        stream.writeString(AGN_EXPIRY_DATE);
        stream.writeString(AGN_LICENSE_NO);
        stream.writeString(AGN_RUNOFF);
        stream.writeString(AGN_LICENSED);
        stream.writeBigDecimal(AGN_LICENSE_GRACE_PR);
        stream.writeString(AGN_OLD_ACC_NO);
        stream.writeString(AGN_STATUS_REMARKS);

    }

    public void setAGN_CODE(BigDecimal AGN_CODE) {
        this.AGN_CODE = AGN_CODE;
    }

    public BigDecimal getAGN_CODE() {
        return AGN_CODE;
    }

    public void setAGN_ACT_CODE(BigDecimal AGN_ACT_CODE) {
        this.AGN_ACT_CODE = AGN_ACT_CODE;
    }

    public BigDecimal getAGN_ACT_CODE() {
        return AGN_ACT_CODE;
    }

    public void setAGN_SHT_DESC(String AGN_SHT_DESC) {
        this.AGN_SHT_DESC = AGN_SHT_DESC;
    }

    public String getAGN_SHT_DESC() {
        return AGN_SHT_DESC;
    }

    public void setAGN_NAME(String AGN_NAME) {
        this.AGN_NAME = AGN_NAME;
    }

    public String getAGN_NAME() {
        return AGN_NAME;
    }

    public void setAGN_PHYSICAL_ADDRESS(String AGN_PHYSICAL_ADDRESS) {
        this.AGN_PHYSICAL_ADDRESS = AGN_PHYSICAL_ADDRESS;
    }

    public String getAGN_PHYSICAL_ADDRESS() {
        return AGN_PHYSICAL_ADDRESS;
    }

    public void setAGN_POSTAL_ADDRESS(String AGN_POSTAL_ADDRESS) {
        this.AGN_POSTAL_ADDRESS = AGN_POSTAL_ADDRESS;
    }

    public String getAGN_POSTAL_ADDRESS() {
        return AGN_POSTAL_ADDRESS;
    }

    public void setAGN_TWN_CODE(BigDecimal AGN_TWN_CODE) {
        this.AGN_TWN_CODE = AGN_TWN_CODE;
    }

    public BigDecimal getAGN_TWN_CODE() {
        return AGN_TWN_CODE;
    }

    public void setAGN_COU_CODE(BigDecimal AGN_COU_CODE) {
        this.AGN_COU_CODE = AGN_COU_CODE;
    }

    public BigDecimal getAGN_COU_CODE() {
        return AGN_COU_CODE;
    }

    public void setAGN_EMAIL_ADDRESS(String AGN_EMAIL_ADDRESS) {
        this.AGN_EMAIL_ADDRESS = AGN_EMAIL_ADDRESS;
    }

    public String getAGN_EMAIL_ADDRESS() {
        return AGN_EMAIL_ADDRESS;
    }

    public void setAGN_WEB_ADDRESS(String AGN_WEB_ADDRESS) {
        this.AGN_WEB_ADDRESS = AGN_WEB_ADDRESS;
    }

    public String getAGN_WEB_ADDRESS() {
        return AGN_WEB_ADDRESS;
    }

    public void setAGN_ZIP(String AGN_ZIP) {
        this.AGN_ZIP = AGN_ZIP;
    }

    public String getAGN_ZIP() {
        return AGN_ZIP;
    }

    public void setAGN_CONTACT_PERSON(String AGN_CONTACT_PERSON) {
        this.AGN_CONTACT_PERSON = AGN_CONTACT_PERSON;
    }

    public String getAGN_CONTACT_PERSON() {
        return AGN_CONTACT_PERSON;
    }

    public void setAGN_CONTACT_TITLE(String AGN_CONTACT_TITLE) {
        this.AGN_CONTACT_TITLE = AGN_CONTACT_TITLE;
    }

    public String getAGN_CONTACT_TITLE() {
        return AGN_CONTACT_TITLE;
    }

    public void setAGN_TEL1(String AGN_TEL1) {
        this.AGN_TEL1 = AGN_TEL1;
    }

    public String getAGN_TEL1() {
        return AGN_TEL1;
    }

    public void setAGN_TEL2(String AGN_TEL2) {
        this.AGN_TEL2 = AGN_TEL2;
    }

    public String getAGN_TEL2() {
        return AGN_TEL2;
    }

    public void setAGN_FAX(String AGN_FAX) {
        this.AGN_FAX = AGN_FAX;
    }

    public String getAGN_FAX() {
        return AGN_FAX;
    }

    public void setAGN_ACC_NO(String AGN_ACC_NO) {
        this.AGN_ACC_NO = AGN_ACC_NO;
    }

    public String getAGN_ACC_NO() {
        return AGN_ACC_NO;
    }

    public void setAGN_PIN(String AGN_PIN) {
        this.AGN_PIN = AGN_PIN;
    }

    public String getAGN_PIN() {
        return AGN_PIN;
    }

    public void setAGN_AGENT_COMMISSION(BigDecimal AGN_AGENT_COMMISSION) {
        this.AGN_AGENT_COMMISSION = AGN_AGENT_COMMISSION;
    }

    public BigDecimal getAGN_AGENT_COMMISSION() {
        return AGN_AGENT_COMMISSION;
    }

    public void setAGN_CREDIT_ALLOWED(String AGN_CREDIT_ALLOWED) {
        this.AGN_CREDIT_ALLOWED = AGN_CREDIT_ALLOWED;
    }

    public String getAGN_CREDIT_ALLOWED() {
        return AGN_CREDIT_ALLOWED;
    }

    public void setAGN_AGENT_WHT_TAX(BigDecimal AGN_AGENT_WHT_TAX) {
        this.AGN_AGENT_WHT_TAX = AGN_AGENT_WHT_TAX;
    }

    public BigDecimal getAGN_AGENT_WHT_TAX() {
        return AGN_AGENT_WHT_TAX;
    }

    public void setAGN_PRINT_DBNOTE(String AGN_PRINT_DBNOTE) {
        this.AGN_PRINT_DBNOTE = AGN_PRINT_DBNOTE;
    }

    public String getAGN_PRINT_DBNOTE() {
        return AGN_PRINT_DBNOTE;
    }

    public void setAGN_STATUS(String AGN_STATUS) {
        this.AGN_STATUS = AGN_STATUS;
    }

    public String getAGN_STATUS() {
        return AGN_STATUS;
    }

    public void setAGN_DATE_CREATED(String AGN_DATE_CREATED) {
        this.AGN_DATE_CREATED = AGN_DATE_CREATED;
    }

    public String getAGN_DATE_CREATED() {
        return AGN_DATE_CREATED;
    }

    public void setAGN_CREATED_BY(String AGN_CREATED_BY) {
        this.AGN_CREATED_BY = AGN_CREATED_BY;
    }

    public String getAGN_CREATED_BY() {
        return AGN_CREATED_BY;
    }

    public void setAGN_REG_CODE(String AGN_REG_CODE) {
        this.AGN_REG_CODE = AGN_REG_CODE;
    }

    public String getAGN_REG_CODE() {
        return AGN_REG_CODE;
    }

    public void setAGN_COMM_RESERVE_RATE(BigDecimal AGN_COMM_RESERVE_RATE) {
        this.AGN_COMM_RESERVE_RATE = AGN_COMM_RESERVE_RATE;
    }

    public BigDecimal getAGN_COMM_RESERVE_RATE() {
        return AGN_COMM_RESERVE_RATE;
    }

    public void setAGN_ANNUAL_BUDGET(BigDecimal AGN_ANNUAL_BUDGET) {
        this.AGN_ANNUAL_BUDGET = AGN_ANNUAL_BUDGET;
    }

    public BigDecimal getAGN_ANNUAL_BUDGET() {
        return AGN_ANNUAL_BUDGET;
    }

    public void setAGN_STATUS_EFF_DATE(String AGN_STATUS_EFF_DATE) {
        this.AGN_STATUS_EFF_DATE = AGN_STATUS_EFF_DATE;
    }

    public String getAGN_STATUS_EFF_DATE() {
        return AGN_STATUS_EFF_DATE;
    }

    public void setAGN_CREDIT_PERIOD(BigDecimal AGN_CREDIT_PERIOD) {
        this.AGN_CREDIT_PERIOD = AGN_CREDIT_PERIOD;
    }

    public BigDecimal getAGN_CREDIT_PERIOD() {
        return AGN_CREDIT_PERIOD;
    }

    public void setAGN_COMM_STAT_EFF_DT(String AGN_COMM_STAT_EFF_DT) {
        this.AGN_COMM_STAT_EFF_DT = AGN_COMM_STAT_EFF_DT;
    }

    public String getAGN_COMM_STAT_EFF_DT() {
        return AGN_COMM_STAT_EFF_DT;
    }

    public void setAGN_COMM_STATUS_DT(String AGN_COMM_STATUS_DT) {
        this.AGN_COMM_STATUS_DT = AGN_COMM_STATUS_DT;
    }

    public String getAGN_COMM_STATUS_DT() {
        return AGN_COMM_STATUS_DT;
    }

    public void setAGN_COMM_ALLOWED(String AGN_COMM_ALLOWED) {
        this.AGN_COMM_ALLOWED = AGN_COMM_ALLOWED;
    }

    public String getAGN_COMM_ALLOWED() {
        return AGN_COMM_ALLOWED;
    }

    public void setAGN_CHECKED(String AGN_CHECKED) {
        this.AGN_CHECKED = AGN_CHECKED;
    }

    public String getAGN_CHECKED() {
        return AGN_CHECKED;
    }

    public void setAGN_CHECKED_BY(String AGN_CHECKED_BY) {
        this.AGN_CHECKED_BY = AGN_CHECKED_BY;
    }

    public String getAGN_CHECKED_BY() {
        return AGN_CHECKED_BY;
    }

    public void setAGN_CHECK_DATE(String AGN_CHECK_DATE) {
        this.AGN_CHECK_DATE = AGN_CHECK_DATE;
    }

    public String getAGN_CHECK_DATE() {
        return AGN_CHECK_DATE;
    }

    public void setAGN_COMP_COMM_ARREARS(String AGN_COMP_COMM_ARREARS) {
        this.AGN_COMP_COMM_ARREARS = AGN_COMP_COMM_ARREARS;
    }

    public String getAGN_COMP_COMM_ARREARS() {
        return AGN_COMP_COMM_ARREARS;
    }

    public void setAGN_REINSURER(String AGN_REINSURER) {
        this.AGN_REINSURER = AGN_REINSURER;
    }

    public String getAGN_REINSURER() {
        return AGN_REINSURER;
    }

    public void setAGN_BRN_CODE(BigDecimal AGN_BRN_CODE) {
        this.AGN_BRN_CODE = AGN_BRN_CODE;
    }

    public BigDecimal getAGN_BRN_CODE() {
        return AGN_BRN_CODE;
    }

    public void setAGN_TOWN(String AGN_TOWN) {
        this.AGN_TOWN = AGN_TOWN;
    }

    public String getAGN_TOWN() {
        return AGN_TOWN;
    }

    public void setAGN_COUNTRY(String AGN_COUNTRY) {
        this.AGN_COUNTRY = AGN_COUNTRY;
    }

    public String getAGN_COUNTRY() {
        return AGN_COUNTRY;
    }

    public void setAGN_STATUS_DESC(String AGN_STATUS_DESC) {
        this.AGN_STATUS_DESC = AGN_STATUS_DESC;
    }

    public String getAGN_STATUS_DESC() {
        return AGN_STATUS_DESC;
    }

    public void setAGN_ID_NO(String AGN_ID_NO) {
        this.AGN_ID_NO = AGN_ID_NO;
    }

    public String getAGN_ID_NO() {
        return AGN_ID_NO;
    }

    public void setAGN_CON_CODE(String AGN_CON_CODE) {
        this.AGN_CON_CODE = AGN_CON_CODE;
    }

    public String getAGN_CON_CODE() {
        return AGN_CON_CODE;
    }

    public void setAGN_AGN_CODE(BigDecimal AGN_AGN_CODE) {
        this.AGN_AGN_CODE = AGN_AGN_CODE;
    }

    public BigDecimal getAGN_AGN_CODE() {
        return AGN_AGN_CODE;
    }

    public void setAGN_SMS_TEL(String AGN_SMS_TEL) {
        this.AGN_SMS_TEL = AGN_SMS_TEL;
    }

    public String getAGN_SMS_TEL() {
        return AGN_SMS_TEL;
    }

    public void setAGN_AHC_CODE(BigDecimal AGN_AHC_CODE) {
        this.AGN_AHC_CODE = AGN_AHC_CODE;
    }

    public BigDecimal getAGN_AHC_CODE() {
        return AGN_AHC_CODE;
    }

    public void setAGN_SEC_CODE(BigDecimal AGN_SEC_CODE) {
        this.AGN_SEC_CODE = AGN_SEC_CODE;
    }

    public BigDecimal getAGN_SEC_CODE() {
        return AGN_SEC_CODE;
    }

    public void setAGN_AGNC_CLASS_CODE(String AGN_AGNC_CLASS_CODE) {
        this.AGN_AGNC_CLASS_CODE = AGN_AGNC_CLASS_CODE;
    }

    public String getAGN_AGNC_CLASS_CODE() {
        return AGN_AGNC_CLASS_CODE;
    }

    public void setAGN_EXPIRY_DATE(String AGN_EXPIRY_DATE) {
        this.AGN_EXPIRY_DATE = AGN_EXPIRY_DATE;
    }

    public String getAGN_EXPIRY_DATE() {
        return AGN_EXPIRY_DATE;
    }

    public void setAGN_LICENSE_NO(String AGN_LICENSE_NO) {
        this.AGN_LICENSE_NO = AGN_LICENSE_NO;
    }

    public String getAGN_LICENSE_NO() {
        return AGN_LICENSE_NO;
    }

    public void setAGN_RUNOFF(String AGN_RUNOFF) {
        this.AGN_RUNOFF = AGN_RUNOFF;
    }

    public String getAGN_RUNOFF() {
        return AGN_RUNOFF;
    }

    public void setAGN_LICENSED(String AGN_LICENSED) {
        this.AGN_LICENSED = AGN_LICENSED;
    }

    public String getAGN_LICENSED() {
        return AGN_LICENSED;
    }

    public void setAGN_LICENSE_GRACE_PR(BigDecimal AGN_LICENSE_GRACE_PR) {
        this.AGN_LICENSE_GRACE_PR = AGN_LICENSE_GRACE_PR;
    }

    public BigDecimal getAGN_LICENSE_GRACE_PR() {
        return AGN_LICENSE_GRACE_PR;
    }

    public void setAGN_OLD_ACC_NO(String AGN_OLD_ACC_NO) {
        this.AGN_OLD_ACC_NO = AGN_OLD_ACC_NO;
    }

    public String getAGN_OLD_ACC_NO() {
        return AGN_OLD_ACC_NO;
    }

    public void setAGN_STATUS_REMARKS(String AGN_STATUS_REMARKS) {
        this.AGN_STATUS_REMARKS = AGN_STATUS_REMARKS;
    }

    public String getAGN_STATUS_REMARKS() {
        return AGN_STATUS_REMARKS;
    }
}
