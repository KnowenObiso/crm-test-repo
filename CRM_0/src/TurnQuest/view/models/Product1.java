package TurnQuest.view.models;


import TurnQuest.view.Base.GlobalCC;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.SQLData;
import java.sql.SQLInput;
import java.sql.SQLOutput;


public class Product1 implements Serializable, SQLData {
    private BigDecimal TPA_CODE;
    private BigDecimal TPA_SYSTEM;
    private BigDecimal TPA_PROD_CODE;
    private String TPA_PROD_SHTDESC;
    private String TPA_PROD_DESC;
    private String TPA_PROD_NARRATION;

    private String SQLTypeName;

    public Product1() {
        super();
    }

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
            if (SQLTypeName.equalsIgnoreCase("TQC_PRODUCT_ATTRIBUTES_OBJ")) {

                stream.writeBigDecimal(TPA_CODE);
                stream.writeBigDecimal(TPA_SYSTEM);
                stream.writeBigDecimal(TPA_PROD_CODE);
                stream.writeString(TPA_PROD_SHTDESC);
                stream.writeString(TPA_PROD_DESC);
                stream.writeString(TPA_PROD_NARRATION);

            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
    }

    public void setTPA_CODE(BigDecimal TPA_CODE) {
        this.TPA_CODE = TPA_CODE;
    }

    public BigDecimal getTPA_CODE() {
        return TPA_CODE;
    }

    public void setTPA_SYSTEM(BigDecimal TPA_SYSTEM) {
        this.TPA_SYSTEM = TPA_SYSTEM;
    }

    public BigDecimal getTPA_SYSTEM() {
        return TPA_SYSTEM;
    }

    public void setTPA_PROD_CODE(BigDecimal TPA_PROD_CODE) {
        this.TPA_PROD_CODE = TPA_PROD_CODE;
    }

    public BigDecimal getTPA_PROD_CODE() {
        return TPA_PROD_CODE;
    }

    public void setTPA_PROD_SHTDESC(String TPA_PROD_SHTDESC) {
        this.TPA_PROD_SHTDESC = TPA_PROD_SHTDESC;
    }

    public String getTPA_PROD_SHTDESC() {
        return TPA_PROD_SHTDESC;
    }

    public void setTPA_PROD_DESC(String TPA_PROD_DESC) {
        this.TPA_PROD_DESC = TPA_PROD_DESC;
    }

    public String getTPA_PROD_DESC() {
        return TPA_PROD_DESC;
    }

    public void setTPA_PROD_NARRATION(String TPA_PROD_NARRATION) {
        this.TPA_PROD_NARRATION = TPA_PROD_NARRATION;
    }

    public String getTPA_PROD_NARRATION() {
        return TPA_PROD_NARRATION;
    }

    public void setSQLTypeName(String SQLTypeName) {
        this.SQLTypeName = SQLTypeName;
    }

    public String getSQLTypeName() {
        return SQLTypeName;
    }
}
