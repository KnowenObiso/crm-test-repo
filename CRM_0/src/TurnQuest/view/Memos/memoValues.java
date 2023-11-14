package TurnQuest.view.Memos;

import java.math.BigDecimal;

public class memoValues {
    public memoValues() {
        super();
    }

    /**
     * Memo Types
     */
    private BigDecimal mtypCode;
    private BigDecimal mtypSysCode;
    private String mtypMemoType;
    private String appAreaCode;
    private String appAreaDesc;

    private String mtypStatus;
    private BigDecimal mtypSubCode;
    private String sclDesc;
    private String type;
    private String name;
    private String applLvl;


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setMtypCode(BigDecimal mtypCode) {
        this.mtypCode = mtypCode;
    }

    public BigDecimal getMtypCode() {
        return mtypCode;
    }

    public void setMtypSysCode(BigDecimal mtypSysCode) {
        this.mtypSysCode = mtypSysCode;
    }

    public BigDecimal getMtypSysCode() {
        return mtypSysCode;
    }

    public void setMtypMemoType(String mtypMemoType) {
        this.mtypMemoType = mtypMemoType;
    }

    public String getMtypMemoType() {
        return mtypMemoType;
    }


    public void setMtypStatus(String mtypStatus) {
        this.mtypStatus = mtypStatus;
    }

    public String getMtypStatus() {
        return mtypStatus;
    }

    public void setMtypSubCode(BigDecimal mtypSubCode) {
        this.mtypSubCode = mtypSubCode;
    }

    public BigDecimal getMtypSubCode() {
        return mtypSubCode;
    }

    public void setSclDesc(String sclDesc) {
        this.sclDesc = sclDesc;
    }

    public String getSclDesc() {
        return sclDesc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAppAreaCode(String appAreaCode) {
        this.appAreaCode = appAreaCode;
    }

    public String getAppAreaCode() {
        return appAreaCode;
    }

    public void setAppAreaDesc(String appAreaDesc) {
        this.appAreaDesc = appAreaDesc;
    }

    public String getAppAreaDesc() {
        return appAreaDesc;
    }

    public void setApplLvl(String applLvl) {
        this.applLvl = applLvl;
    }

    public String getApplLvl() {
        return applLvl;
    }
}
