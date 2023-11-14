package TurnQuest.view.setups;

import java.io.Serializable;

import java.math.BigDecimal;


public class AdministrativeRegion implements Serializable {


    private BigDecimal regCode;
    private String regShortDesc;
    private String regName;
    private BigDecimal regCouCode;

    public AdministrativeRegion() {
        super();
    }

    public void setRegCode(BigDecimal regCode) {
        this.regCode = regCode;
    }

    public BigDecimal getRegCode() {
        return regCode;
    }

    public void setRegShortDesc(String regShortDesc) {
        this.regShortDesc = regShortDesc;
    }

    public String getRegShortDesc() {
        return regShortDesc;
    }

    public void setRegName(String regName) {
        this.regName = regName;
    }

    public String getRegName() {
        return regName;
    }

    public void setRegCouCode(BigDecimal regCouCode) {
        this.regCouCode = regCouCode;
    }

    public BigDecimal getRegCouCode() {
        return regCouCode;
    }
}
