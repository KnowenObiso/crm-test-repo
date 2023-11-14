package TurnQuest.view.Usr;

import java.math.BigDecimal;

public class MyAccount {
    
    private BigDecimal code;
    
    private BigDecimal grptUsrCode;
    
    private String type;
    private String accType;
    private BigDecimal agnCode;
    private String agnName;
    private boolean select;
    
    public MyAccount(){
        super();
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public BigDecimal getCode() {
        return code;
    }

    public void setGrptUsrCode(BigDecimal grptUsrCode) {
        this.grptUsrCode = grptUsrCode;
    }

    public BigDecimal getGrptUsrCode() {
        return grptUsrCode;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setAgnCode(BigDecimal agnCode) {
        this.agnCode = agnCode;
    }

    public BigDecimal getAgnCode() {
        return agnCode;
    }

    public void setAgnName(String agnName) {
        this.agnName = agnName;
    }

    public String getAgnName() {
        return agnName;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getAccType() {
        return accType;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isSelect() {
        return select;
    }
}
