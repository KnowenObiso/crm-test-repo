package TurnQuest.view.models;

import java.math.BigDecimal;

public class ProductClientAttribute {
    private BigDecimal prodClientAttrCode;
    private BigDecimal prodCode;
    private BigDecimal clientAttrCode;
    private String clientAttributeName;
    private String attributeRange;
    private String prodMinValue;
    private String prodMaxValue;
    private String prodFixedValue;
    private String colName;
    private String prompt;


    public void setProdCode(BigDecimal prodCode) {
        this.prodCode = prodCode;
    }

    public BigDecimal getProdCode() {
        return prodCode;
    }


    public void setProdMinValue(String prodMinValue) {
        this.prodMinValue = prodMinValue;
    }

    public String getProdMinValue() {
        return prodMinValue;
    }

    public void setProdMaxValue(String prodMaxValue) {
        this.prodMaxValue = prodMaxValue;
    }

    public String getProdMaxValue() {
        return prodMaxValue;
    }


    public void setProdClientAttrCode(BigDecimal prodClientAttrCode) {
        this.prodClientAttrCode = prodClientAttrCode;
    }

    public BigDecimal getProdClientAttrCode() {
        return prodClientAttrCode;
    }


    public void setClientAttributeName(String clientAttributeName) {
        this.clientAttributeName = clientAttributeName;
    }

    public String getClientAttributeName() {
        return clientAttributeName;
    }


    public void setProdFixedValue(String prodFixedValue) {
        this.prodFixedValue = prodFixedValue;
    }

    public String getProdFixedValue() {
        return prodFixedValue;
    }

    public void setClientAttrCode(BigDecimal clientAttrCode) {
        this.clientAttrCode = clientAttrCode;
    }

    public BigDecimal getClientAttrCode() {
        return clientAttrCode;
    }

    public void setAttributeRange(String attributeRange) {
        this.attributeRange = attributeRange;
    }

    public String getAttributeRange() {
        return attributeRange;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getColName() {
        return colName;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }
}
