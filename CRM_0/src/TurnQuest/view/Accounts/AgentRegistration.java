package TurnQuest.view.Accounts;

import java.math.BigDecimal;

import java.sql.Date;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;


public class AgentRegistration implements SQLData {
    private String sql_type = "TQ_CRM.TQC_AGENCY_REGISTRATION_OBJ";
    private BigDecimal AREG_CODE;
    private BigDecimal AREG_AGN_CODE;
    private BigDecimal AREG_YEAR;
    private String AREG_REG_NO;
    private Date AREG_WEF;
    private Date AREG_WET;
    private String AREG_ACCEPTED;

    public String getSQLTypeName() {
        return "TQ_CRM.TQC_AGENCY_REGISTRATION_OBJ";
    }

    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        sql_type = typeName;
        AREG_CODE = stream.readBigDecimal();
        AREG_AGN_CODE = stream.readBigDecimal();
        AREG_YEAR = stream.readBigDecimal();
        AREG_REG_NO = stream.readString();
        AREG_WEF = stream.readDate();
        AREG_WET = stream.readDate();
        AREG_ACCEPTED = stream.readString();
    }

    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeBigDecimal(AREG_CODE);
        stream.writeBigDecimal(AREG_AGN_CODE);
        stream.writeBigDecimal(AREG_YEAR);
        stream.writeString(AREG_REG_NO);
        stream.writeDate(AREG_WEF);
        stream.writeDate(AREG_WET);
        stream.writeString(AREG_ACCEPTED);
    }

    public void setAREG_CODE(BigDecimal AREG_CODE) {
        this.AREG_CODE = AREG_CODE;
    }

    public BigDecimal getAREG_CODE() {
        return AREG_CODE;
    }

    public void setAREG_AGN_CODE(BigDecimal AREG_AGN_CODE) {
        this.AREG_AGN_CODE = AREG_AGN_CODE;
    }

    public BigDecimal getAREG_AGN_CODE() {
        return AREG_AGN_CODE;
    }

    public void setAREG_YEAR(BigDecimal AREG_YEAR) {
        this.AREG_YEAR = AREG_YEAR;
    }

    public BigDecimal getAREG_YEAR() {
        return AREG_YEAR;
    }

    public void setAREG_REG_NO(String AREG_REG_NO) {
        this.AREG_REG_NO = AREG_REG_NO;
    }

    public String getAREG_REG_NO() {
        return AREG_REG_NO;
    }

    public void setAREG_WEF(Date AREG_WEF) {
        this.AREG_WEF = AREG_WEF;
    }

    public Date getAREG_WEF() {
        return AREG_WEF;
    }

    public void setAREG_WET(Date AREG_WET) {
        this.AREG_WET = AREG_WET;
    }

    public Date getAREG_WET() {
        return AREG_WET;
    }

    public void setAREG_ACCEPTED(String AREG_ACCEPTED) {
        this.AREG_ACCEPTED = AREG_ACCEPTED;
    }

    public String getAREG_ACCEPTED() {
        return AREG_ACCEPTED;
    }

}
