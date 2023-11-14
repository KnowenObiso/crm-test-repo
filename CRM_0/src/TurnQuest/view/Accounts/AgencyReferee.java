package TurnQuest.view.Accounts;

import java.math.BigDecimal;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;


public class AgencyReferee implements SQLData {
    private String sqlType = "TQ_CRM.TQC_AGENCY_REFEREES_OBJ";
    private BigDecimal AREF_CODE;
    private String AREF_NAME;
    private String AREF_PHYSICAL_ADDRESS;
    private String AREF_POSTAL_ADDRESS;
    private BigDecimal AREF_TWN_CODE;
    private BigDecimal AREF_COU_CODE;
    private String AREF_EMAIL_ADDRESS;
    private String AREF_TEL;
    private String AREF_ID_NO;
    private BigDecimal AREF_AGN_CODE;

    public void setAREF_CODE(BigDecimal AREF_CODE) {
        this.AREF_CODE = AREF_CODE;
    }

    public BigDecimal getAREF_CODE() {
        return AREF_CODE;
    }

    public void setAREF_NAME(String AREF_NAME) {
        this.AREF_NAME = AREF_NAME;
    }

    public String getAREF_NAME() {
        return AREF_NAME;
    }

    public void setAREF_PHYSICAL_ADDRESS(String AREF_PHYSICAL_ADDRESS) {
        this.AREF_PHYSICAL_ADDRESS = AREF_PHYSICAL_ADDRESS;
    }

    public String getAREF_PHYSICAL_ADDRESS() {
        return AREF_PHYSICAL_ADDRESS;
    }

    public void setAREF_POSTAL_ADDRESS(String AREF_POSTAL_ADDRESS) {
        this.AREF_POSTAL_ADDRESS = AREF_POSTAL_ADDRESS;
    }

    public String getAREF_POSTAL_ADDRESS() {
        return AREF_POSTAL_ADDRESS;
    }

    public void setAREF_TWN_CODE(BigDecimal AREF_TWN_CODE) {
        this.AREF_TWN_CODE = AREF_TWN_CODE;
    }

    public BigDecimal getAREF_TWN_CODE() {
        return AREF_TWN_CODE;
    }

    public void setAREF_COU_CODE(BigDecimal AREF_COU_CODE) {
        this.AREF_COU_CODE = AREF_COU_CODE;
    }

    public BigDecimal getAREF_COU_CODE() {
        return AREF_COU_CODE;
    }

    public void setAREF_EMAIL_ADDRESS(String AREF_EMAIL_ADDRESS) {
        this.AREF_EMAIL_ADDRESS = AREF_EMAIL_ADDRESS;
    }

    public String getAREF_EMAIL_ADDRESS() {
        return AREF_EMAIL_ADDRESS;
    }

    public void setAREF_TEL(String AREF_TEL) {
        this.AREF_TEL = AREF_TEL;
    }

    public String getAREF_TEL() {
        return AREF_TEL;
    }

    public void setAREF_ID_NO(String AREF_ID_NO) {
        this.AREF_ID_NO = AREF_ID_NO;
    }

    public String getAREF_ID_NO() {
        return AREF_ID_NO;
    }

    public void setAREF_AGN_CODE(BigDecimal AREF_AGN_CODE) {
        this.AREF_AGN_CODE = AREF_AGN_CODE;
    }

    public BigDecimal getAREF_AGN_CODE() {
        return AREF_AGN_CODE;
    }

    public String getSQLTypeName() {
        return sqlType;
    }

    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        AREF_CODE = stream.readBigDecimal();
        AREF_NAME = stream.readString();
        AREF_PHYSICAL_ADDRESS = stream.readString();
        AREF_POSTAL_ADDRESS = stream.readString();
        AREF_TWN_CODE = stream.readBigDecimal();
        AREF_COU_CODE = stream.readBigDecimal();
        AREF_EMAIL_ADDRESS = stream.readString();
        AREF_TEL = stream.readString();
        AREF_ID_NO = stream.readString();
        AREF_AGN_CODE = stream.readBigDecimal();
    }

    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeBigDecimal(AREF_CODE);
        stream.writeString(AREF_NAME);
        stream.writeString(AREF_PHYSICAL_ADDRESS);
        stream.writeString(AREF_POSTAL_ADDRESS);
        stream.writeBigDecimal(AREF_TWN_CODE);
        stream.writeBigDecimal(AREF_CODE);
        stream.writeString(AREF_EMAIL_ADDRESS);
        stream.writeString(AREF_TEL);
        stream.writeString(AREF_ID_NO);
        stream.writeBigDecimal(AREF_AGN_CODE);
    }
}
