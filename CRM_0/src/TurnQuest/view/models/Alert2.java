package TurnQuest.view.models;


import TurnQuest.view.Base.GlobalCC;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.SQLData;
import java.sql.SQLInput;
import java.sql.SQLOutput;


public class Alert2 implements Serializable, SQLData {

    private String SQLTypeName;

    private BigDecimal ALRTS_CODE;
    private BigDecimal ALRTS_ALRT_CODE;
    private BigDecimal ALRTS_SYS_CODE;
    private BigDecimal ALRTS_AGN_CODE;
    private BigDecimal ALRTS_CLNT_CODE;
    private String ALRTS_DESCRIPTION;
    private String ALRTS_DATE;
    private BigDecimal ALRTS_PERIOD;
    private BigDecimal ALRTS_USER_CODE;
    private String ALRTS_DEST_TYPE;
    private BigDecimal ALRTS_DEST_CODE;
    private BigDecimal ALRTS_MSGT_CODE;
    private String ALRTS_STATUS;
    private String ALRTS_SHT_DESC;

    public Alert2() {
        super();
    }

    /**
     * Populates this object with data read from the database.
     *
     * @param stream The SQLInput object from which to read the data for the
     * value that is being custom mapped
     * @param typeName the SQL type name of the value on the data stream
     */
    public void readSQL(SQLInput stream, String typeName) {
    }

    /**
     * Writes this object to the given SQL data stream, converting it back to
     * its SQL value in the data source.
     *
     * @param stream the SQLOutput object to which to write the data for the
     * value that was custom mapped
     */
    public void writeSQL(SQLOutput stream) {
        try {
            if (SQLTypeName.equalsIgnoreCase("TQC_ALERTS_OBJ")) {
                stream.writeBigDecimal(ALRTS_CODE);
                stream.writeBigDecimal(ALRTS_ALRT_CODE);
                stream.writeBigDecimal(ALRTS_SYS_CODE);
                stream.writeBigDecimal(ALRTS_AGN_CODE);
                stream.writeBigDecimal(ALRTS_CLNT_CODE);
                stream.writeString(ALRTS_DESCRIPTION);
                stream.writeString(ALRTS_DATE);
                stream.writeBigDecimal(ALRTS_PERIOD);
                stream.writeBigDecimal(ALRTS_USER_CODE);
                stream.writeString(ALRTS_DEST_TYPE);
                stream.writeBigDecimal(ALRTS_DEST_CODE);
                stream.writeBigDecimal(ALRTS_MSGT_CODE);
                stream.writeString(ALRTS_STATUS);
                stream.writeString(ALRTS_SHT_DESC);

            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
    }

    public void setSQLTypeName(String SQLTypeName) {
        this.SQLTypeName = SQLTypeName;
    }

    public String getSQLTypeName() {
        return SQLTypeName;
    }


    public void setALRTS_ALRT_CODE(BigDecimal ALRTS_ALRT_CODE) {
        this.ALRTS_ALRT_CODE = ALRTS_ALRT_CODE;
    }

    public BigDecimal getALRTS_ALRT_CODE() {
        return ALRTS_ALRT_CODE;
    }

    public void setALRTS_SYS_CODE(BigDecimal ALRTS_SYS_CODE) {
        this.ALRTS_SYS_CODE = ALRTS_SYS_CODE;
    }

    public BigDecimal getALRTS_SYS_CODE() {
        return ALRTS_SYS_CODE;
    }

    public void setALRTS_AGN_CODE(BigDecimal ALRTS_AGN_CODE) {
        this.ALRTS_AGN_CODE = ALRTS_AGN_CODE;
    }

    public BigDecimal getALRTS_AGN_CODE() {
        return ALRTS_AGN_CODE;
    }

    public void setALRTS_CLNT_CODE(BigDecimal ALRTS_CLNT_CODE) {
        this.ALRTS_CLNT_CODE = ALRTS_CLNT_CODE;
    }

    public BigDecimal getALRTS_CLNT_CODE() {
        return ALRTS_CLNT_CODE;
    }

    public void setALRTS_DESCRIPTION(String ALRTS_DESCRIPTION) {
        this.ALRTS_DESCRIPTION = ALRTS_DESCRIPTION;
    }

    public String getALRTS_DESCRIPTION() {
        return ALRTS_DESCRIPTION;
    }

    public void setALRTS_DATE(String ALRTS_DATE) {
        this.ALRTS_DATE = ALRTS_DATE;
    }

    public String getALRTS_DATE() {
        return ALRTS_DATE;
    }

    public void setALRTS_PERIOD(BigDecimal ALRTS_PERIOD) {
        this.ALRTS_PERIOD = ALRTS_PERIOD;
    }

    public BigDecimal getALRTS_PERIOD() {
        return ALRTS_PERIOD;
    }

    public void setALRTS_USER_CODE(BigDecimal ALRTS_USER_CODE) {
        this.ALRTS_USER_CODE = ALRTS_USER_CODE;
    }

    public BigDecimal getALRTS_USER_CODE() {
        return ALRTS_USER_CODE;
    }

    public void setALRTS_DEST_TYPE(String ALRTS_DEST_TYPE) {
        this.ALRTS_DEST_TYPE = ALRTS_DEST_TYPE;
    }

    public String getALRTS_DEST_TYPE() {
        return ALRTS_DEST_TYPE;
    }

    public void setALRTS_DEST_CODE(BigDecimal ALRTS_DEST_CODE) {
        this.ALRTS_DEST_CODE = ALRTS_DEST_CODE;
    }

    public BigDecimal getALRTS_DEST_CODE() {
        return ALRTS_DEST_CODE;
    }

    public void setALRTS_MSGT_CODE(BigDecimal ALRTS_MSGT_CODE) {
        this.ALRTS_MSGT_CODE = ALRTS_MSGT_CODE;
    }

    public BigDecimal getALRTS_MSGT_CODE() {
        return ALRTS_MSGT_CODE;
    }

    public void setALRTS_CODE(BigDecimal ALRTS_CODE) {
        this.ALRTS_CODE = ALRTS_CODE;
    }

    public BigDecimal getALRTS_CODE() {
        return ALRTS_CODE;
    }

    public void setALRTS_STATUS(String ALRTS_STATUS) {
        this.ALRTS_STATUS = ALRTS_STATUS;
    }

    public String getALRTS_STATUS() {
        return ALRTS_STATUS;
    }

    public void setALRTS_SHT_DESC(String ALRTS_SHT_DESC) {
        this.ALRTS_SHT_DESC = ALRTS_SHT_DESC;
    }

    public String getALRTS_SHT_DESC() {
        return ALRTS_SHT_DESC;
    }
}
