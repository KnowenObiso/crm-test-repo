package TurnQuest.view.products;

import java.math.BigDecimal;

import java.util.HashSet;
import java.util.Set;


public class Product {
    public Product() {
        super();
    }


    private BigDecimal productCode;
    private String productShtDesc;
    private String productDesc;
    private String productType;
    private BigDecimal popCode;
    private String popShtDesc;
    private String popDesc;

    private BigDecimal pMasCode;
    private String pMasShtDesc;
    private String pMasDesc;

    private BigDecimal ProductSubClassCode;
    private String coverTypeDesc;
    private String description;
    private String coverTypeDetails;
    private BigDecimal coverTypeCode;
    private String productDetails;
    private String productExpiryPeriod;
    private Set<CoverTypes> covertypes = new HashSet<CoverTypes>(0);

    //subClasses
    public BigDecimal subClassCode;
    public String subClassDesc;

    private String bindName;
    private BigDecimal bindCode;

    public void setProductCode(BigDecimal productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getProductCode() {
        return productCode;
    }

    public void setProductShtDesc(String productShtDesc) {
        this.productShtDesc = productShtDesc;
    }

    public String getProductShtDesc() {
        return productShtDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductType() {
        return productType;
    }

    public void setPopCode(BigDecimal popCode) {
        this.popCode = popCode;
    }

    public BigDecimal getPopCode() {
        return popCode;
    }

    public void setPopShtDesc(String popShtDesc) {
        this.popShtDesc = popShtDesc;
    }

    public String getPopShtDesc() {
        return popShtDesc;
    }

    public void setPopDesc(String popDesc) {
        this.popDesc = popDesc;
    }

    public String getPopDesc() {
        return popDesc;
    }

    public void setPMasCode(BigDecimal pMasCode) {
        this.pMasCode = pMasCode;
    }

    public BigDecimal getPMasCode() {
        return pMasCode;
    }

    public void setPMasShtDesc(String pMasShtDesc) {
        this.pMasShtDesc = pMasShtDesc;
    }

    public String getPMasShtDesc() {
        return pMasShtDesc;
    }

    public void setPMasDesc(String pMasDesc) {
        this.pMasDesc = pMasDesc;
    }

    public String getPMasDesc() {
        return pMasDesc;
    }

    public void setProductSubClassCode(BigDecimal ProductSubClassCode) {
        this.ProductSubClassCode = ProductSubClassCode;
    }

    public BigDecimal getProductSubClassCode() {
        return ProductSubClassCode;
    }

    public void setCoverTypeDesc(String coverTypeDesc) {
        this.coverTypeDesc = coverTypeDesc;
    }

    public String getCoverTypeDesc() {
        return coverTypeDesc;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCoverTypeDetails(String coverTypeDetails) {
        this.coverTypeDetails = coverTypeDetails;
    }

    public String getCoverTypeDetails() {
        return coverTypeDetails;
    }

    public void setCoverTypeCode(BigDecimal coverTypeCode) {
        this.coverTypeCode = coverTypeCode;
    }

    public BigDecimal getCoverTypeCode() {
        return coverTypeCode;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductExpiryPeriod(String productExpiryPeriod) {
        this.productExpiryPeriod = productExpiryPeriod;
    }

    public String getProductExpiryPeriod() {
        return productExpiryPeriod;
    }

    public void setCovertypes(Set<CoverTypes> covertypes) {
        this.covertypes = covertypes;
    }

    public Set<CoverTypes> getCovertypes() {
        return covertypes;
    }

    public void setSubClassCode(BigDecimal subClassCode) {
        this.subClassCode = subClassCode;
    }

    public BigDecimal getSubClassCode() {
        return subClassCode;
    }

    public void setSubClassDesc(String subClassDesc) {
        this.subClassDesc = subClassDesc;
    }

    public String getSubClassDesc() {
        return subClassDesc;
    }

    public void setBindName(String bindName) {
        this.bindName = bindName;
    }

    public String getBindName() {
        return bindName;
    }

    public void setBindCode(BigDecimal bindCode) {
        this.bindCode = bindCode;
    }

    public BigDecimal getBindCode() {
        return bindCode;
    }
}
