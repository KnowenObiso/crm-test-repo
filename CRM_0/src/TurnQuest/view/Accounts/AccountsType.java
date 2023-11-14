/*
* Copyright (c) 2010 TurnKey Africa Ltd. All Rights Reserved.
*
* This software is the confidential and proprietary information of TurnKey
* Africa Ltd. ("Confidential Information"). You shall not disclose such
* Confidential Information and shall use it only in accordance with the terms
* of the license agreement you entered into with TurnKey Africa Ltd.
*
* TURNKEY AFRICA MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY
* OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
* TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
* PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TURNKEY AFRICA SHALL NOT BE LIABLE
* FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
* DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
*/

package TurnQuest.view.Accounts;

import java.math.BigDecimal;

import java.sql.Date;


/**
 * The AccountsType Bean. Includes properties and methods that map to a
 * given AccountsType.
 *
 * @author Frankline Ogongi
 */
public class AccountsType {

    private String code;
    private String accountType;
    private String accountTypeShortDesc;
    private String wthtxRate;
    private String typeId;
    private String commReserveRate;
    private String maxAdvAmt;
    private String maxAdvRepaymentPrd;
    private String rcptsIncludeComm;
    private String overideRate;
    private String idSerialFormat;
    private String vatRate;

    // Accounts Fields
    private String accNumber;
    private String accName;

    private String agentname;
    private BigDecimal agentCode;
    private String agentShtDesc;
    private BigDecimal sysCode;
    private String sysName;
    private String sysShtDesc;


    private BigDecimal sprCode;
    private String sprShtDesc;
    private String sprName;
    private String sprPhysicalAddress;
    private String sprPostalAddress;
    private String sprPhone;
    private String sprFax;
    private String sprEmail;
    private String sprCreatedBy;
    private Date sprDateCreated;
    private String sprStatusRemarks;
    private String sprStatus;
    private String sprPinNumber;
    private String sprMobileNumber;
    private String sprInhouse;
    private String sprSmsNumber;
    
    
    //Mode and frequncy
    private String cpmShtDesc;
    private String cpmDesc;

    public AccountsType() {
        super();
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountTypeShortDesc(String accountTypeShortDesc) {
        this.accountTypeShortDesc = accountTypeShortDesc;
    }

    public String getAccountTypeShortDesc() {
        return accountTypeShortDesc;
    }

    public void setWthtxRate(String wthtxRate) {
        this.wthtxRate = wthtxRate;
    }

    public String getWthtxRate() {
        return wthtxRate;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setCommReserveRate(String commReserveRate) {
        this.commReserveRate = commReserveRate;
    }

    public String getCommReserveRate() {
        return commReserveRate;
    }

    public void setMaxAdvAmt(String maxAdvAmt) {
        this.maxAdvAmt = maxAdvAmt;
    }

    public String getMaxAdvAmt() {
        return maxAdvAmt;
    }

    public void setMaxAdvRepaymentPrd(String maxAdvRepaymentPrd) {
        this.maxAdvRepaymentPrd = maxAdvRepaymentPrd;
    }

    public String getMaxAdvRepaymentPrd() {
        return maxAdvRepaymentPrd;
    }

    public void setRcptsIncludeComm(String rcptsIncludeComm) {
        this.rcptsIncludeComm = rcptsIncludeComm;
    }

    public String getRcptsIncludeComm() {
        return rcptsIncludeComm;
    }

    public void setOverideRate(String overideRate) {
        this.overideRate = overideRate;
    }

    public String getOverideRate() {
        return overideRate;
    }

    public void setIdSerialFormat(String idSerialFormat) {
        this.idSerialFormat = idSerialFormat;
    }

    public String getIdSerialFormat() {
        return idSerialFormat;
    }

    public void setVatRate(String vatRate) {
        this.vatRate = vatRate;
    }

    public String getVatRate() {
        return vatRate;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getAccName() {
        return accName;
    }

    public void setAgentname(String agentname) {
        this.agentname = agentname;
    }

    public String getAgentname() {
        return agentname;
    }

    public void setAgentCode(BigDecimal agentCode) {
        this.agentCode = agentCode;
    }

    public BigDecimal getAgentCode() {
        return agentCode;
    }

    public void setAgentShtDesc(String agentShtDesc) {
        this.agentShtDesc = agentShtDesc;
    }

    public String getAgentShtDesc() {
        return agentShtDesc;
    }

    public void setSysCode(BigDecimal sysCode) {
        this.sysCode = sysCode;
    }

    public BigDecimal getSysCode() {
        return sysCode;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysShtDesc(String sysShtDesc) {
        this.sysShtDesc = sysShtDesc;
    }

    public String getSysShtDesc() {
        return sysShtDesc;
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

    public void setSprPhone(String sprPhone) {
        this.sprPhone = sprPhone;
    }

    public String getSprPhone() {
        return sprPhone;
    }

    public void setSprFax(String sprFax) {
        this.sprFax = sprFax;
    }

    public String getSprFax() {
        return sprFax;
    }

    public void setSprEmail(String sprEmail) {
        this.sprEmail = sprEmail;
    }

    public String getSprEmail() {
        return sprEmail;
    }

    public void setSprCreatedBy(String sprCreatedBy) {
        this.sprCreatedBy = sprCreatedBy;
    }

    public String getSprCreatedBy() {
        return sprCreatedBy;
    }

    public void setSprDateCreated(Date sprDateCreated) {
        this.sprDateCreated = sprDateCreated;
    }

    public Date getSprDateCreated() {
        return sprDateCreated;
    }

    public void setSprStatusRemarks(String sprStatusRemarks) {
        this.sprStatusRemarks = sprStatusRemarks;
    }

    public String getSprStatusRemarks() {
        return sprStatusRemarks;
    }

    public void setSprStatus(String sprStatus) {
        this.sprStatus = sprStatus;
    }

    public String getSprStatus() {
        return sprStatus;
    }

    public void setSprPinNumber(String sprPinNumber) {
        this.sprPinNumber = sprPinNumber;
    }

    public String getSprPinNumber() {
        return sprPinNumber;
    }

    public void setSprMobileNumber(String sprMobileNumber) {
        this.sprMobileNumber = sprMobileNumber;
    }

    public String getSprMobileNumber() {
        return sprMobileNumber;
    }

    public void setSprInhouse(String sprInhouse) {
        this.sprInhouse = sprInhouse;
    }

    public String getSprInhouse() {
        return sprInhouse;
    }

    public void setSprSmsNumber(String sprSmsNumber) {
        this.sprSmsNumber = sprSmsNumber;
    }

    public String getSprSmsNumber() {
        return sprSmsNumber;
    }

    public void setCpmShtDesc(String cpmShtDesc) {
        this.cpmShtDesc = cpmShtDesc;
    }

    public String getCpmShtDesc() {
        return cpmShtDesc;
    }

    public void setCpmDesc(String cpmDesc) {
        this.cpmDesc = cpmDesc;
    }

    public String getCpmDesc() {
        return cpmDesc;
    }
}
