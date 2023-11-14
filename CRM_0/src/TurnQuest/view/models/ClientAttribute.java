package TurnQuest.view.models;

import java.math.BigDecimal;

public class ClientAttribute {
    private BigDecimal TCA_CODE;
    private String TCA_ATTRIBUTE_NAME;
    private String TCA_ATT_DESC;
    private String TCA_PROMPT;
    private String TCA_ATT_RANGE;
    private String TCA_ATT_TYPE_INPUT;
    private String tableName;
    private String colName;
    private String colDescription;


    public ClientAttribute() {
        super();
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

    public void setColDescription(String colDescription) {
        this.colDescription = colDescription;
    }

    public String getColDescription() {
        return colDescription;
    }
}
