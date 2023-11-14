package TurnQuest.view.portalsetups;

import java.math.BigDecimal;

public class SubClass {
    public SubClass() {
        super();
    }
    private BigDecimal sclCode;
    private String sclDesc;
    private String sclShtDesc;
    private String sclWebSclDescription;

    public void setSclCode(BigDecimal sclCode) {
        this.sclCode = sclCode;
    }

    public BigDecimal getSclCode() {
        return sclCode;
    }

    public void setSclDesc(String sclDesc) {
        this.sclDesc = sclDesc;
    }

    public String getSclDesc() {
        return sclDesc;
    }

    public void setSclShtDesc(String sclShtDesc) {
        this.sclShtDesc = sclShtDesc;
    }

    public String getSclShtDesc() {
        return sclShtDesc;
    }

    public void setSclWebSclDescription(String sclWebSclDescription) {
        this.sclWebSclDescription = sclWebSclDescription;
    }

    public String getSclWebSclDescription() {
        return sclWebSclDescription;
    }


}
