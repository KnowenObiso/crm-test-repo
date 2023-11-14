package TurnQuest.view.Base;

import java.math.BigDecimal;

import java.util.Date;


public class sys {
    public sys() {
        super();
    }
    private BigDecimal usysCode;
    private BigDecimal sysCode;
    private String sysShtDesc;
    private Date usysWef;
    private String sysDesc;
    private Date usysWet;
    private String syspath;
    private BigDecimal usrCode;

    public void setUsysCode(BigDecimal usysCode) {
        this.usysCode = usysCode;
    }

    public BigDecimal getUsysCode() {
        return usysCode;
    }

    public void setSysCode(BigDecimal sysCode) {
        this.sysCode = sysCode;
    }

    public BigDecimal getSysCode() {
        return sysCode;
    }

    public void setSysShtDesc(String sysShtDesc) {
        this.sysShtDesc = sysShtDesc;
    }

    public String getSysShtDesc() {
        return sysShtDesc;
    }

    public void setUsysWef(Date usysWef) {
        this.usysWef = usysWef;
    }

    public Date getUsysWef() {
        return usysWef;
    }

    public void setSysDesc(String sysDesc) {
        this.sysDesc = sysDesc;
    }

    public String getSysDesc() {
        return sysDesc;
    }

    public void setUsysWet(Date usysWet) {
        this.usysWet = usysWet;
    }

    public Date getUsysWet() {
        return usysWet;
    }

    public void setSyspath(String syspath) {
        this.syspath = syspath;
    }

    public String getSyspath() {
        return syspath;
    }

    public void setUsrCode(BigDecimal usrCode) {
        this.usrCode = usrCode;
    }

    public BigDecimal getUsrCode() {
        return usrCode;
    }
}
