package TurnQuest.view.messaging;

import java.math.BigDecimal;

public class MessageBean {
    private BigDecimal msgtCode;
    private String msgtShtDesc;
    private String msgtMsg;
    private String msgtSysModule;
    private String msgtType;


    private BigDecimal countryCode;
    private String couShtDesc;
    private String CounntryName;


    private BigDecimal stateCode;
    private BigDecimal counCode;
    private String stateShtDesc;
    private String stateName;

    public void setMsgtCode(BigDecimal msgtCode) {
        this.msgtCode = msgtCode;
    }

    public BigDecimal getMsgtCode() {
        return msgtCode;
    }

    public void setMsgtShtDesc(String msgtShtDesc) {
        this.msgtShtDesc = msgtShtDesc;
    }

    public String getMsgtShtDesc() {
        return msgtShtDesc;
    }

    public void setMsgtMsg(String msgtMsg) {
        this.msgtMsg = msgtMsg;
    }

    public String getMsgtMsg() {
        return msgtMsg;
    }

    public void setMsgtSysModule(String msgtSysModule) {
        this.msgtSysModule = msgtSysModule;
    }

    public String getMsgtSysModule() {
        return msgtSysModule;
    }

    public void setMsgtType(String msgtType) {
        this.msgtType = msgtType;
    }

    public String getMsgtType() {
        return msgtType;
    }

    public void setCountryCode(BigDecimal countryCode) {
        this.countryCode = countryCode;
    }

    public BigDecimal getCountryCode() {
        return countryCode;
    }

    public void setCouShtDesc(String couShtDesc) {
        this.couShtDesc = couShtDesc;
    }

    public String getCouShtDesc() {
        return couShtDesc;
    }

    public void setCounntryName(String CounntryName) {
        this.CounntryName = CounntryName;
    }

    public String getCounntryName() {
        return CounntryName;
    }

    public void setStateCode(BigDecimal stateCode) {
        this.stateCode = stateCode;
    }

    public BigDecimal getStateCode() {
        return stateCode;
    }

    public void setCounCode(BigDecimal counCode) {
        this.counCode = counCode;
    }

    public BigDecimal getCounCode() {
        return counCode;
    }

    public void setStateShtDesc(String stateShtDesc) {
        this.stateShtDesc = stateShtDesc;
    }

    public String getStateShtDesc() {
        return stateShtDesc;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateName() {
        return stateName;
    }
}
