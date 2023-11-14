package TurnQuest.view.Memos;

import java.math.BigDecimal;

import java.util.List;


public class Memo {
    public Memo() {
        super();
    }

    /**
     * Systems
     */
    private BigDecimal sysCode;
    private String sysShortDesc;
    private String sysName;

    /**
     * Memo Types
     */
    private BigDecimal mtypCode;
    private BigDecimal mtypSysCode;
    private String mtypMemoType;
    private String mtypApplLvl;
    private String mtypStatus;
    private BigDecimal mtypSubCode;


    private String sclDesc;
    private BigDecimal sclCode;

    private BigDecimal memdetCode;
    private String memdetContent;
    private BigDecimal memdetMemoCode;


    private List<memoValues> produces;

    private BigDecimal memoCode;
    private String memoSubject;
    private BigDecimal memoMtypCode;

    private String type;
    private String name;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setProduces(List<memoValues> produces) {
        this.produces = produces;
    }

    public List<memoValues> getProduces() {
        return produces;
    }

    public void setSysCode(BigDecimal sysCode) {
        this.sysCode = sysCode;
    }

    public BigDecimal getSysCode() {
        return sysCode;
    }

    public void setSysShortDesc(String sysShortDesc) {
        this.sysShortDesc = sysShortDesc;
    }

    public String getSysShortDesc() {
        return sysShortDesc;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysName() {
        return sysName;
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

    public void setMtypApplLvl(String mtypApplLvl) {
        this.mtypApplLvl = mtypApplLvl;
    }

    public String getMtypApplLvl() {
        return mtypApplLvl;
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

    public void setSclCode(BigDecimal sclCode) {
        this.sclCode = sclCode;
    }

    public BigDecimal getSclCode() {
        return sclCode;
    }

    public void setMemoCode(BigDecimal memoCode) {
        this.memoCode = memoCode;
    }

    public BigDecimal getMemoCode() {
        return memoCode;
    }

    public void setMemoSubject(String memoSubject) {
        this.memoSubject = memoSubject;
    }

    public String getMemoSubject() {
        return memoSubject;
    }

    public void setMemoMtypCode(BigDecimal memoMtypCode) {
        this.memoMtypCode = memoMtypCode;
    }

    public BigDecimal getMemoMtypCode() {
        return memoMtypCode;
    }

    public void setMemdetCode(BigDecimal memdetCode) {
        this.memdetCode = memdetCode;
    }

    public BigDecimal getMemdetCode() {
        return memdetCode;
    }

    public void setMemdetContent(String memdetContent) {
        this.memdetContent = memdetContent;
    }

    public String getMemdetContent() {
        return memdetContent;
    }

    public void setMemdetMemoCode(BigDecimal memdetMemoCode) {
        this.memdetMemoCode = memdetMemoCode;
    }

    public BigDecimal getMemdetMemoCode() {
        return memdetMemoCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
