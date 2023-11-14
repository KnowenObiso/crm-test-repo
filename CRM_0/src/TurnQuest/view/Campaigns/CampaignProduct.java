package TurnQuest.view.Campaigns;

public class CampaignProduct {
    private String productCode;
    private String productShortDesc;
    private String prodDesc;
    private String prodSystem;
    private boolean checked;

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductShortDesc(String productShortDesc) {
        this.productShortDesc = productShortDesc;
    }

    public String getProductShortDesc() {
        return productShortDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdSystem(String prodSystem) {
        this.prodSystem = prodSystem;
    }

    public String getProdSystem() {
        return prodSystem;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }
}
