package TurnQuest.view.models;


import TurnQuest.view.Base.GlobalCC;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.SQLData;
import java.sql.SQLInput;
import java.sql.SQLOutput;


public class ClientAttribute1 implements Serializable, SQLData {
    private BigDecimal TCA_CODE;
    private String TCA_ATTRIBUTE_NAME;
    private String TCA_ATT_DESC;
    private String TCA_PROMPT;
    private String TCA_ATT_RANGE;
    private String TCA_ATT_TYPE_INPUT;
    private String tableName;
    private String colName;

    private String SQLTypeName;

    public ClientAttribute1() {
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
            if (SQLTypeName.equalsIgnoreCase("TQC_CLIENT_ATTRIBUTES_OBJ")) {

                stream.writeBigDecimal(TCA_CODE);
                stream.writeString(TCA_ATTRIBUTE_NAME);
                stream.writeString(TCA_ATT_DESC);
                stream.writeString(TCA_PROMPT);
                stream.writeString(TCA_ATT_RANGE);
                stream.writeString(TCA_ATT_TYPE_INPUT);
                stream.writeString(tableName);
                stream.writeString(colName);


            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
    }

    public void setTCA_CODE(BigDecimal TCA_CODE) {
        this.TCA_CODE = TCA_CODE;
    }

    public BigDecimal getTCA_CODE() {
        return TCA_CODE;
    }

    public void setTCA_ATTRIBUTE_NAME(String TCA_ATTRIBUTE_NAME) {
        this.TCA_ATTRIBUTE_NAME = TCA_ATTRIBUTE_NAME;
    }

    public String getTCA_ATTRIBUTE_NAME() {
        return TCA_ATTRIBUTE_NAME;
    }

    public void setTCA_ATT_DESC(String TCA_ATT_DESC) {
        this.TCA_ATT_DESC = TCA_ATT_DESC;
    }

    public String getTCA_ATT_DESC() {
        return TCA_ATT_DESC;
    }

    public void setTCA_PROMPT(String TCA_PROMPT) {
        this.TCA_PROMPT = TCA_PROMPT;
    }

    public String getTCA_PROMPT() {
        return TCA_PROMPT;
    }

    public void setTCA_ATT_RANGE(String TCA_ATT_RANGE) {
        this.TCA_ATT_RANGE = TCA_ATT_RANGE;
    }

    public String getTCA_ATT_RANGE() {
        return TCA_ATT_RANGE;
    }

    public void setTCA_ATT_TYPE_INPUT(String TCA_ATT_TYPE_INPUT) {
        this.TCA_ATT_TYPE_INPUT = TCA_ATT_TYPE_INPUT;
    }

    public String getTCA_ATT_TYPE_INPUT() {
        return TCA_ATT_TYPE_INPUT;
    }

    public void setSQLTypeName(String SQLTypeName) {
        this.SQLTypeName = SQLTypeName;
    }

    public String getSQLTypeName() {
        return SQLTypeName;
    }


    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getColName() {
        return colName;
    }
}
