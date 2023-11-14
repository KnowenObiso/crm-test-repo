package TurnQuest.view.dao;

import java.math.BigDecimal;

public class MessageProduct {
    
    private BigDecimal prodCode;
    private String prodDesc;
    
    public MessageProduct() {
        super();
    }

    public void setProdCode(BigDecimal prodCode) {
        this.prodCode = prodCode;
    }

    public BigDecimal getProdCode() {
        return prodCode;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public String getProdDesc() {
        return prodDesc;
    }
}
