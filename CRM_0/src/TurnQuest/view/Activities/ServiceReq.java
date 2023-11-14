package TurnQuest.view.Activities;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.Date;


public class ServiceReq {
    public ServiceReq() {
        super();
    }

    private BigDecimal tsrcCode;
    private String tsrcName;
    private BigDecimal tsrcValidity;
    private String proposalNo;
    private String policyNumber;
    private BigDecimal sumAssured;
    private BigDecimal polCode;
    private String urlString;
    private BigDecimal usrCode;
    private String usrName;
    private BigDecimal count;

    private BigDecimal tsrCode;
    private String tsrType;
    private String accType;
    private BigDecimal accCode;
    private Timestamp tsrDate;
    private BigDecimal assignee;    
    private String ownType;
    private BigDecimal ownerCode;
    private String status;
    private Date dueDate;
    private Date resDate;
    private String summary;
    private String desc;
    private String solution;
    private String accTypeDesc;
    private String accountName;
    private String ownerType;
    private String owner;
    private String assigneeDesc;

    private String mailType;
    private String mailServerName;
    private String mailHost;
    private String mailUsername;
    private String mailPass;
    private String mailPort;
    private String mailEmail;
    private String mailInOut;
    private boolean secure;

    private BigDecimal code;
    private BigDecimal sysCode;
    private String type;
    private String editable;
    private String name;

    private String tssDesc;
    private String tssUrl;
    private String tssUsername;
    private String tssPassword;
    private String tssSource;
    private BigDecimal tssCode;
    private String tssDefault;
    private String tsrMode;

    private BigDecimal brnCode;
    private String brnShtDesc;
    private BigDecimal regCode;
    private String brnName;
    private String requestRefNumber;

    private BigDecimal depCode;
    private String depShtDesc;
    private String depName;
    private Date depWef;
    private Date depWet;

    private String tsrcType;
    private String tsrcDefault;

    private BigDecimal srtCode;
    private String srtShtDesc;
    private String srtDesc;
    private String comments;


    private String agntShtDesc;
    private BigDecimal agntCode;
    private String agntName;
    private BigDecimal agntBrnCode;
    private String agntBrnName;
    private String agntCommAllowed;


    private BigDecimal sprCode;
    private String sprShtDesc;
    private String sprName;
    private String sprPhysicalAddress;
    private String sprPostalAddress;
    private String sprPhone;
    private String sprEmail;
    private String sprPhoneNumber;
    private String sprSmsNumber;

    private String agnPhysicalAddress;
    private String agnEmailAddress;
    private String agnSmsNumber;
    private String agnDefCommMode;


    private BigDecimal tsrSrtCode;


    private BigDecimal sridCode;
    private String sridName;
    private String sridTelephone;
    private String sridEmailAddress;
    private String sridPhysicalAddress;
    private String srisIdNumber;


    private BigDecimal copAgnCode;
    private String agnName;
    private Date copDate;
    private String copCrRefNumber;
    private String copdrRefNumber;
    private BigDecimal copCommAmt;
    private BigDecimal copWhdtaxAmt;
    private BigDecimal copNetComm;
    private BigDecimal copCurrCode;
    private String authorised;
    private String authorisedBy;
    private String copPaid;
    private Date copPaidChqDate;
    private String copPaidChqNo;
    private String clientName;
    private String feeDesc;
    private BigDecimal providerFee;
    
    private BigDecimal srcCode;
    private String srcClientComment;
    private String srcSolution;
    private BigDecimal srcTsrCode;
    
    private String endrCode;
    
    private String TSR_POLICY_NO;
    private String TSR_ACCOUNT_NAME;
    private Date TSR_RECEIVE_DATE;
    private String tsr_reporter;
    private Date tsrCapturedate;
    
    private String closedByAssignee;
    private String closedByReporter;
    private String closedBy;

    public void setTsrcCode(BigDecimal tsrcCode) {
        this.tsrcCode = tsrcCode;
    }

    public BigDecimal getTsrcCode() {
        return tsrcCode;
    }

    public void setTsrcName(String tsrcName) {
        this.tsrcName = tsrcName;
    }

    public String getTsrcName() {
        return tsrcName;
    }

    public void setTsrcValidity(BigDecimal tsrcValidity) {
        this.tsrcValidity = tsrcValidity;
    }

    public BigDecimal getTsrcValidity() {
        return tsrcValidity;
    }

    public void setProposalNo(String proposalNo) {
        this.proposalNo = proposalNo;
    }

    public String getProposalNo() {
        return proposalNo;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setSumAssured(BigDecimal sumAssured) {
        this.sumAssured = sumAssured;
    }

    public BigDecimal getSumAssured() {
        return sumAssured;
    }

    public void setPolCode(BigDecimal polCode) {
        this.polCode = polCode;
    }

    public BigDecimal getPolCode() {
        return polCode;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setTsrCode(BigDecimal tsrCode) {
        this.tsrCode = tsrCode;
    }

    public BigDecimal getTsrCode() {
        return tsrCode;
    }

    public void setTsrType(String tsrType) {
        this.tsrType = tsrType;
    }

    public String getTsrType() {
        return tsrType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccCode(BigDecimal accCode) {
        this.accCode = accCode;
    }

    public BigDecimal getAccCode() {
        return accCode;
    }

    public void setTsrDate(Timestamp tsrDate) {
        this.tsrDate = tsrDate;
    }

    public Timestamp getTsrDate() {
        return tsrDate;
    }

    public void setAssignee(BigDecimal assignee) {
        this.assignee = assignee;
    }

    public BigDecimal getAssignee() {
        return assignee;
    }

    public void setOwnType(String ownType) {
        this.ownType = ownType;
    }

    public String getOwnType() {
        return ownType;
    }

    public void setOwnerCode(BigDecimal ownerCode) {
        this.ownerCode = ownerCode;
    }

    public BigDecimal getOwnerCode() {
        return ownerCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setResDate(Date resDate) {
        this.resDate = resDate;
    }

    public Date getResDate() {
        return resDate;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getSolution() {
        return solution;
    }

    public void setAccTypeDesc(String accTypeDesc) {
        this.accTypeDesc = accTypeDesc;
    }

    public String getAccTypeDesc() {
        return accTypeDesc;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setAssigneeDesc(String assigneeDesc) {
        this.assigneeDesc = assigneeDesc;
    }

    public String getAssigneeDesc() {
        return assigneeDesc;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setMailType(String mailType) {
        this.mailType = mailType;
    }

    public String getMailType() {
        return mailType;
    }

    public void setMailServerName(String mailServerName) {
        this.mailServerName = mailServerName;
    }

    public String getMailServerName() {
        return mailServerName;
    }

    public void setMailHost(String mailHost) {
        this.mailHost = mailHost;
    }

    public String getMailHost() {
        return mailHost;
    }

    public void setMailUsername(String mailUsername) {
        this.mailUsername = mailUsername;
    }

    public String getMailUsername() {
        return mailUsername;
    }

    public void setMailPass(String mailPass) {
        this.mailPass = mailPass;
    }

    public String getMailPass() {
        return mailPass;
    }

    public void setMailPort(String mailPort) {
        this.mailPort = mailPort;
    }

    public String getMailPort() {
        return mailPort;
    }

    public void setMailEmail(String mailEmail) {
        this.mailEmail = mailEmail;
    }

    public String getMailEmail() {
        return mailEmail;
    }

    public void setMailInOut(String mailInOut) {
        this.mailInOut = mailInOut;
    }

    public String getMailInOut() {
        return mailInOut;
    }

    public void setUsrCode(BigDecimal usrCode) {
        this.usrCode = usrCode;
    }

    public BigDecimal getUsrCode() {
        return usrCode;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getUsrName() {
        return usrName;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean getSecure() {
        return secure;
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public BigDecimal getCode() {
        return code;
    }

    public void setSysCode(BigDecimal sysCode) {
        this.sysCode = sysCode;
    }

    public BigDecimal getSysCode() {
        return sysCode;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getEditable() {
        return editable;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTssDesc(String tssDesc) {
        this.tssDesc = tssDesc;
    }

    public String getTssDesc() {
        return tssDesc;
    }

    public void setTssUrl(String tssUrl) {
        this.tssUrl = tssUrl;
    }

    public String getTssUrl() {
        return tssUrl;
    }

    public void setTssUsername(String tssUsername) {
        this.tssUsername = tssUsername;
    }

    public String getTssUsername() {
        return tssUsername;
    }

    public void setTssPassword(String tssPassword) {
        this.tssPassword = tssPassword;
    }

    public String getTssPassword() {
        return tssPassword;
    }

    public void setTssSource(String tssSource) {
        this.tssSource = tssSource;
    }

    public String getTssSource() {
        return tssSource;
    }

    public void setTssCode(BigDecimal tssCode) {
        this.tssCode = tssCode;
    }

    public BigDecimal getTssCode() {
        return tssCode;
    }

    public void setTssDefault(String tssDefault) {
        this.tssDefault = tssDefault;
    }

    public String getTssDefault() {
        return tssDefault;
    }

    public void setTsrMode(String tsrMode) {
        this.tsrMode = tsrMode;
    }

    public String getTsrMode() {
        return tsrMode;
    }

    public void setBrnCode(BigDecimal brnCode) {
        this.brnCode = brnCode;
    }

    public BigDecimal getBrnCode() {
        return brnCode;
    }

    public void setBrnShtDesc(String brnShtDesc) {
        this.brnShtDesc = brnShtDesc;
    }

    public String getBrnShtDesc() {
        return brnShtDesc;
    }

    public void setRegCode(BigDecimal regCode) {
        this.regCode = regCode;
    }

    public BigDecimal getRegCode() {
        return regCode;
    }

    public void setBrnName(String brnName) {
        this.brnName = brnName;
    }

    public String getBrnName() {
        return brnName;
    }

    public void setRequestRefNumber(String requestRefNumber) {
        this.requestRefNumber = requestRefNumber;
    }

    public String getRequestRefNumber() {
        return requestRefNumber;
    }

    public void setDepCode(BigDecimal depCode) {
        this.depCode = depCode;
    }

    public BigDecimal getDepCode() {
        return depCode;
    }

    public void setDepShtDesc(String depShtDesc) {
        this.depShtDesc = depShtDesc;
    }

    public String getDepShtDesc() {
        return depShtDesc;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepWef(Date depWef) {
        this.depWef = depWef;
    }

    public Date getDepWef() {
        return depWef;
    }

    public void setDepWet(Date depWet) {
        this.depWet = depWet;
    }

    public Date getDepWet() {
        return depWet;
    }

    public void setTsrcType(String tsrcType) {
        this.tsrcType = tsrcType;
    }

    public String getTsrcType() {
        return tsrcType;
    }

    public void setTsrcDefault(String tsrcDefault) {
        this.tsrcDefault = tsrcDefault;
    }

    public String getTsrcDefault() {
        return tsrcDefault;
    }

    public void setSrtCode(BigDecimal srtCode) {
        this.srtCode = srtCode;
    }

    public BigDecimal getSrtCode() {
        return srtCode;
    }

    public void setSrtShtDesc(String srtShtDesc) {
        this.srtShtDesc = srtShtDesc;
    }

    public String getSrtShtDesc() {
        return srtShtDesc;
    }

    public void setSrtDesc(String srtDesc) {
        this.srtDesc = srtDesc;
    }

    public String getSrtDesc() {
        return srtDesc;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }

    public void setAgntShtDesc(String agntShtDesc) {
        this.agntShtDesc = agntShtDesc;
    }

    public String getAgntShtDesc() {
        return agntShtDesc;
    }

    public void setAgntCode(BigDecimal agntCode) {
        this.agntCode = agntCode;
    }

    public BigDecimal getAgntCode() {
        return agntCode;
    }

    public void setAgntName(String agntName) {
        this.agntName = agntName;
    }

    public String getAgntName() {
        return agntName;
    }

    public void setAgntBrnCode(BigDecimal agntBrnCode) {
        this.agntBrnCode = agntBrnCode;
    }

    public BigDecimal getAgntBrnCode() {
        return agntBrnCode;
    }

    public void setAgntBrnName(String agntBrnName) {
        this.agntBrnName = agntBrnName;
    }

    public String getAgntBrnName() {
        return agntBrnName;
    }

    public void setAgntCommAllowed(String agntCommAllowed) {
        this.agntCommAllowed = agntCommAllowed;
    }

    public String getAgntCommAllowed() {
        return agntCommAllowed;
    }

    public void setSprPhone(String sprPhone) {
        this.sprPhone = sprPhone;
    }

    public String getSprPhone() {
        return sprPhone;
    }

    public void setSprCode(BigDecimal sprCode) {
        this.sprCode = sprCode;
    }

    public BigDecimal getSprCode() {
        return sprCode;
    }

    public void setSprShtDesc(String sprShtDesc) {
        this.sprShtDesc = sprShtDesc;
    }

    public String getSprShtDesc() {
        return sprShtDesc;
    }

    public void setSprName(String sprName) {
        this.sprName = sprName;
    }

    public String getSprName() {
        return sprName;
    }

    public void setSprPhysicalAddress(String sprPhysicalAddress) {
        this.sprPhysicalAddress = sprPhysicalAddress;
    }

    public String getSprPhysicalAddress() {
        return sprPhysicalAddress;
    }

    public void setSprPostalAddress(String sprPostalAddress) {
        this.sprPostalAddress = sprPostalAddress;
    }

    public String getSprPostalAddress() {
        return sprPostalAddress;
    }

    public void setSprEmail(String sprEmail) {
        this.sprEmail = sprEmail;
    }

    public String getSprEmail() {
        return sprEmail;
    }

    public void setSprPhoneNumber(String sprPhoneNumber) {
        this.sprPhoneNumber = sprPhoneNumber;
    }

    public String getSprPhoneNumber() {
        return sprPhoneNumber;
    }

    public void setSprSmsNumber(String sprSmsNumber) {
        this.sprSmsNumber = sprSmsNumber;
    }

    public String getSprSmsNumber() {
        return sprSmsNumber;
    }

    public void setAgnPhysicalAddress(String agnPhysicalAddress) {
        this.agnPhysicalAddress = agnPhysicalAddress;
    }

    public String getAgnPhysicalAddress() {
        return agnPhysicalAddress;
    }

    public void setAgnEmailAddress(String agnEmailAddress) {
        this.agnEmailAddress = agnEmailAddress;
    }

    public String getAgnEmailAddress() {
        return agnEmailAddress;
    }

    public void setAgnSmsNumber(String agnSmsNumber) {
        this.agnSmsNumber = agnSmsNumber;
    }

    public String getAgnSmsNumber() {
        return agnSmsNumber;
    }

    public void setTsrSrtCode(BigDecimal tsrSrtCode) {
        this.tsrSrtCode = tsrSrtCode;
    }

    public BigDecimal getTsrSrtCode() {
        return tsrSrtCode;
    }

    public void setSridCode(BigDecimal sridCode) {
        this.sridCode = sridCode;
    }

    public BigDecimal getSridCode() {
        return sridCode;
    }

    public void setSridName(String sridName) {
        this.sridName = sridName;
    }

    public String getSridName() {
        return sridName;
    }

    public void setSridTelephone(String sridTelephone) {
        this.sridTelephone = sridTelephone;
    }

    public String getSridTelephone() {
        return sridTelephone;
    }

    public void setSridEmailAddress(String sridEmailAddress) {
        this.sridEmailAddress = sridEmailAddress;
    }

    public String getSridEmailAddress() {
        return sridEmailAddress;
    }

    public void setSridPhysicalAddress(String sridPhysicalAddress) {
        this.sridPhysicalAddress = sridPhysicalAddress;
    }

    public String getSridPhysicalAddress() {
        return sridPhysicalAddress;
    }

    public void setSrisIdNumber(String srisIdNumber) {
        this.srisIdNumber = srisIdNumber;
    }

    public String getSrisIdNumber() {
        return srisIdNumber;
    }

    public void setCopAgnCode(BigDecimal copAgnCode) {
        this.copAgnCode = copAgnCode;
    }

    public BigDecimal getCopAgnCode() {
        return copAgnCode;
    }

    public void setAgnName(String agnName) {
        this.agnName = agnName;
    }

    public String getAgnName() {
        return agnName;
    }

    public void setCopDate(Date copDate) {
        this.copDate = copDate;
    }

    public Date getCopDate() {
        return copDate;
    }

    public void setCopCrRefNumber(String copCrRefNumber) {
        this.copCrRefNumber = copCrRefNumber;
    }

    public String getCopCrRefNumber() {
        return copCrRefNumber;
    }

    public void setCopdrRefNumber(String copdrRefNumber) {
        this.copdrRefNumber = copdrRefNumber;
    }

    public String getCopdrRefNumber() {
        return copdrRefNumber;
    }

    public void setCopCommAmt(BigDecimal copCommAmt) {
        this.copCommAmt = copCommAmt;
    }

    public BigDecimal getCopCommAmt() {
        return copCommAmt;
    }

    public void setCopWhdtaxAmt(BigDecimal copWhdtaxAmt) {
        this.copWhdtaxAmt = copWhdtaxAmt;
    }

    public BigDecimal getCopWhdtaxAmt() {
        return copWhdtaxAmt;
    }

    public void setCopNetComm(BigDecimal copNetComm) {
        this.copNetComm = copNetComm;
    }

    public BigDecimal getCopNetComm() {
        return copNetComm;
    }

    public void setCopCurrCode(BigDecimal copCurrCode) {
        this.copCurrCode = copCurrCode;
    }

    public BigDecimal getCopCurrCode() {
        return copCurrCode;
    }

    public void setAuthorised(String authorised) {
        this.authorised = authorised;
    }

    public String getAuthorised() {
        return authorised;
    }

    public void setAuthorisedBy(String authorisedBy) {
        this.authorisedBy = authorisedBy;
    }

    public String getAuthorisedBy() {
        return authorisedBy;
    }


    public void setCopPaidChqDate(Date copPaidChqDate) {
        this.copPaidChqDate = copPaidChqDate;
    }

    public Date getCopPaidChqDate() {
        return copPaidChqDate;
    }

    public void setCopPaidChqNo(String copPaidChqNo) {
        this.copPaidChqNo = copPaidChqNo;
    }

    public String getCopPaidChqNo() {
        return copPaidChqNo;
    }

    public void setCopPaid(String copPaid) {
        this.copPaid = copPaid;
    }

    public String getCopPaid() {
        return copPaid;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setProviderFee(BigDecimal providerFee) {
        this.providerFee = providerFee;
    }

    public BigDecimal getProviderFee() {
        return providerFee;
    }

    public void setFeeDesc(String feeDesc) {
        this.feeDesc = feeDesc;
    }

    public String getFeeDesc() {
        return feeDesc;
    }

    public void setSrcCode(BigDecimal srcCode) {
        this.srcCode = srcCode;
    }

    public BigDecimal getSrcCode() {
        return srcCode;
    }

    public void setSrcClientComment(String srcClientComment) {
        this.srcClientComment = srcClientComment;
    }

    public String getSrcClientComment() {
        return srcClientComment;
    }

    public void setSrcSolution(String srcSolution) {
        this.srcSolution = srcSolution;
    }

    public String getSrcSolution() {
        return srcSolution;
    }

    public void setSrcTsrCode(BigDecimal srcTsrCode) {
        this.srcTsrCode = srcTsrCode;
    }

    public BigDecimal getSrcTsrCode() {
        return srcTsrCode;
    }

    public void setEndrCode(String endrCode) {
        this.endrCode = endrCode;
    }

    public String getEndrCode() {
        return endrCode;
    }

  public void setTSR_POLICY_NO(String TSR_POLICY_NO)
  {
    this.TSR_POLICY_NO = TSR_POLICY_NO;
  }

  public String getTSR_POLICY_NO()
  {
    return TSR_POLICY_NO;
  }

  public void setTSR_ACCOUNT_NAME(String TSR_ACCOUNT_NAME)
  {
    this.TSR_ACCOUNT_NAME = TSR_ACCOUNT_NAME;
  }

  public String getTSR_ACCOUNT_NAME()
  {
    return TSR_ACCOUNT_NAME;
  }

    public void setTSR_RECEIVE_DATE(Date TSR_RECEIVE_DATE) {
        this.TSR_RECEIVE_DATE = TSR_RECEIVE_DATE;
    }

    public Date getTSR_RECEIVE_DATE() {
        return TSR_RECEIVE_DATE;
    }

    public void setTsr_reporter(String tsr_reporter) {
        this.tsr_reporter = tsr_reporter;
    }

    public String getTsr_reporter() {
        return tsr_reporter;
    }

    public void setTsrCapturedate(Date tsrCapturedate) {
        this.tsrCapturedate = tsrCapturedate;
    }

    public Date getTsrCapturedate() {
        return tsrCapturedate;
    }


    public void setAgnDefCommMode(String agnDefCommMode) {
        this.agnDefCommMode = agnDefCommMode;
    }

    public String getAgnDefCommMode() {
        return agnDefCommMode;
    }


    public void setClosedByAssignee(String closedByAssignee) {
        this.closedByAssignee = closedByAssignee;
    }

    public String getClosedByAssignee() {
        return closedByAssignee;
    }

    public void setClosedByReporter(String closedByReporter) {
        this.closedByReporter = closedByReporter;
    }

    public String getClosedByReporter() {
        return closedByReporter;
    }

    public void setClosedBy(String closedBy) {
        this.closedBy = closedBy;
    }
    
    public String getClosedBy() {
        return closedBy;
    }
}
