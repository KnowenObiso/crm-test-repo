package TurnQuest.view.Accounts;

import java.math.BigDecimal;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;


public class AgDirector implements SQLData {
    private String sqltype = "TQ_CRM.TQC_AGENCY_DIRECTORS_OBJ";
    private BigDecimal ADIR_CODE;
    private BigDecimal ADIR_AGN_CODE;
    private BigDecimal ADIR_YEAR;
    private String ADIR_NAME;
    private String ADIR_QUALIFICATIONS;
    private BigDecimal ADIR_PCT_HOLDG;
    private String ADIR_DESIGNATION;
    private String ADIR_PHONE_NUMBER;
    private String ADIR_PRINCIPLE_DIR;
    private BigDecimal ADIR_NATIONALITY;


    public void setADIR_CODE(BigDecimal ADIR_CODE) {
        this.ADIR_CODE = ADIR_CODE;
    }

    public BigDecimal getADIR_CODE() {
        return ADIR_CODE;
    }

    public void setADIR_AGN_CODE(BigDecimal ADIR_AGN_CODE) {
        this.ADIR_AGN_CODE = ADIR_AGN_CODE;
    }

    public BigDecimal getADIR_AGN_CODE() {
        return ADIR_AGN_CODE;
    }

    public void setADIR_YEAR(BigDecimal ADIR_YEAR) {
        this.ADIR_YEAR = ADIR_YEAR;
    }

    public BigDecimal getADIR_YEAR() {
        return ADIR_YEAR;
    }

    public void setADIR_NAME(String ADIR_NAME) {
        this.ADIR_NAME = ADIR_NAME;
    }

    public String getADIR_NAME() {
        return ADIR_NAME;
    }

    public void setADIR_QUALIFICATIONS(String ADIR_QUALIFICATIONS) {
        this.ADIR_QUALIFICATIONS = ADIR_QUALIFICATIONS;
    }

    public String getADIR_QUALIFICATIONS() {
        return ADIR_QUALIFICATIONS;
    }

    public void setADIR_PCT_HOLDG(BigDecimal ADIR_PCT_HOLDG) {
        this.ADIR_PCT_HOLDG = ADIR_PCT_HOLDG;
    }

    public BigDecimal getADIR_PCT_HOLDG() {
        return ADIR_PCT_HOLDG;
    }

    public void setADIR_DESIGNATION(String ADIR_DESIGNATION) {
        this.ADIR_DESIGNATION = ADIR_DESIGNATION;
    }

    public String getADIR_DESIGNATION() {
        return ADIR_DESIGNATION;
    }

    public String getSQLTypeName() {
        return sqltype;
    }

    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        sqltype = typeName;
        ADIR_CODE = stream.readBigDecimal();
        ADIR_AGN_CODE = stream.readBigDecimal();
        ADIR_YEAR = stream.readBigDecimal();
        ADIR_NAME = stream.readString();
        ADIR_QUALIFICATIONS = stream.readString();
        ADIR_PCT_HOLDG = stream.readBigDecimal();
        ADIR_DESIGNATION = stream.readString();
        ADIR_PHONE_NUMBER = stream.readString();
        ADIR_PRINCIPLE_DIR = stream.readString();
        ADIR_NATIONALITY = stream.readBigDecimal();
    }

    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeBigDecimal(ADIR_CODE);
        stream.writeBigDecimal(ADIR_AGN_CODE);
        stream.writeBigDecimal(ADIR_YEAR);
        stream.writeString(ADIR_NAME);
        stream.writeString(ADIR_QUALIFICATIONS);
        stream.writeBigDecimal(ADIR_PCT_HOLDG);
        stream.writeString(ADIR_DESIGNATION);
        stream.writeString(ADIR_PHONE_NUMBER);
        stream.writeString(ADIR_PRINCIPLE_DIR);
        stream.writeBigDecimal(ADIR_NATIONALITY);
    }

    public void setADIR_PHONE_NUMBER(String ADIR_PHONE_NUMBER) {
        this.ADIR_PHONE_NUMBER = ADIR_PHONE_NUMBER;
    }

    public String getADIR_PHONE_NUMBER() {
        return ADIR_PHONE_NUMBER;
    }

    public void setADIR_PRINCIPLE_DIR(String ADIR_PRINCIPLE_DIR) {
        this.ADIR_PRINCIPLE_DIR = ADIR_PRINCIPLE_DIR;
    }

    public String getADIR_PRINCIPLE_DIR() {
        return ADIR_PRINCIPLE_DIR;
    }


    public void setADIR_NATIONALITY(BigDecimal ADIR_NATIONALITY) {
        this.ADIR_NATIONALITY = ADIR_NATIONALITY;
    }

    public BigDecimal getADIR_NATIONALITY() {
        return ADIR_NATIONALITY;
    }
}
