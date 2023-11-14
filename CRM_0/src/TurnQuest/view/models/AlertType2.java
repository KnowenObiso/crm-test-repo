package TurnQuest.view.models;


import TurnQuest.view.Base.GlobalCC;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.SQLData;
import java.sql.SQLInput;
import java.sql.SQLOutput;


public class AlertType2 implements Serializable, SQLData {
    private BigDecimal ALRT_CODE;
    private String ALRT_TYPE;
    private BigDecimal ALRT_SYS_CODE;
    private String ALRT_EMAIL;
    private String ALRT_SMS;
    private String sysName;
    private String SQLTypeName;

    public AlertType2() {
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
            if (SQLTypeName.equalsIgnoreCase("TQC_ALERT_TYPES_OBJ")) {
                stream.writeBigDecimal(ALRT_CODE);
                stream.writeString(ALRT_TYPE);
                stream.writeBigDecimal(ALRT_SYS_CODE);
                stream.writeString(ALRT_EMAIL);
                stream.writeString(ALRT_SMS);

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
}
