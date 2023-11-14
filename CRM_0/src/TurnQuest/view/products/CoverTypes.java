package TurnQuest.view.products;

import java.math.BigDecimal;


public class CoverTypes {
    public BigDecimal coverTypeCode;
    public String description;
    public String coverTypeDesc;
    public String coverTypeDetails;

    public void setCoverTypeCode(BigDecimal coverTypeCode) {
        this.coverTypeCode = coverTypeCode;
    }

    public BigDecimal getCoverTypeCode() {
        return coverTypeCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCoverTypeDesc(String coverTypeDesc) {
        this.coverTypeDesc = coverTypeDesc;
    }

    public String getCoverTypeDesc() {
        return coverTypeDesc;
    }

    public void setCoverTypeDetails(String coverTypeDetails) {
        this.coverTypeDetails = coverTypeDetails;
    }

    public String getCoverTypeDetails() {
        return coverTypeDetails;
    }
}
