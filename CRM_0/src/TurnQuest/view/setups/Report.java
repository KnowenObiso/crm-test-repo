package TurnQuest.view.setups;

import java.io.Serializable;

import java.math.BigDecimal;


public class Report implements Serializable {

    private BigDecimal srmCode;
    private String srmName;
    private String srmDesc;
    private BigDecimal srmSysCode;
    private String srmSysName;

    private BigDecimal rsmCode;
    private String rsmName;
    private String rsmDesc;


    private BigDecimal rptCode;
    private String rptName;
    private String rptDesc;
    private String rptActive;
    private boolean selected;

    public Report() {
        super();
    }


    public void setSrmName(String srmName) {
        this.srmName = srmName;
    }

    public String getSrmName() {
        return srmName;
    }

    public void setSrmDesc(String srmDesc) {
        this.srmDesc = srmDesc;
    }

    public String getSrmDesc() {
        return srmDesc;
    }


    public void setSrmSysName(String srmSysName) {
        this.srmSysName = srmSysName;
    }

    public String getSrmSysName() {
        return srmSysName;
    }

    public void setSrmCode(BigDecimal srmCode) {
        this.srmCode = srmCode;
    }

    public BigDecimal getSrmCode() {
        return srmCode;
    }

    public void setSrmSysCode(BigDecimal srmSysCode) {
        this.srmSysCode = srmSysCode;
    }

    public BigDecimal getSrmSysCode() {
        return srmSysCode;
    }

    public void setRsmCode(BigDecimal rsmCode) {
        this.rsmCode = rsmCode;
    }

    public BigDecimal getRsmCode() {
        return rsmCode;
    }

    public void setRsmName(String rsmName) {
        this.rsmName = rsmName;
    }

    public String getRsmName() {
        return rsmName;
    }

    public void setRsmDesc(String rsmDesc) {
        this.rsmDesc = rsmDesc;
    }

    public String getRsmDesc() {
        return rsmDesc;
    }

    public void setRptCode(BigDecimal rptCode) {
        this.rptCode = rptCode;
    }

    public BigDecimal getRptCode() {
        return rptCode;
    }

    public void setRptName(String rptName) {
        this.rptName = rptName;
    }

    public String getRptName() {
        return rptName;
    }

    public void setRptDesc(String rptDesc) {
        this.rptDesc = rptDesc;
    }

    public String getRptDesc() {
        return rptDesc;
    }

    public void setRptActive(String rptActive) {
        this.rptActive = rptActive;
    }

    public String getRptActive() {
        return rptActive;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}
