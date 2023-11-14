package TurnQuest.view.models;

import java.math.BigDecimal;


public class Product {
    private BigDecimal TPA_CODE;
    private BigDecimal TPA_SYSTEM;
    private BigDecimal TPA_PROD_CODE;
    private String TPA_PROD_SHTDESC;
    private String TPA_PROD_DESC;
    private String TPA_PROD_NARRATION;
    private String SYSNAME;

    public Product() {
        super();
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

    public void setSYSNAME(String SYSNAME) {
        this.SYSNAME = SYSNAME;
    }

    public String getSYSNAME() {
        return SYSNAME;
    }
}
