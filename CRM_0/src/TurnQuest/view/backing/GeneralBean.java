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

package TurnQuest.view.backing;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;


public class GeneralBean implements Serializable {

    private BigDecimal sysCode;
    private BigDecimal polCode;
    private BigDecimal pprCode;
    private BigDecimal prpCode;
    private BigDecimal clientCode;
    private String clientShortDesc;
    private String clientName;
    private String clientBankAccNum;
    private BigDecimal clientBbrCode;
    private BigDecimal bbrBankCode;
    private String bbrBranchName;
    private String bbrShortDesc;
    private String bbrRefCode;
    private String polType;
    private String proposalNum;
    private String policyNum;
    private BigDecimal premAmount;
    private String remarks;
    private BigDecimal prevDdhCode;
    private Date bookDate;
    private String accountHolder;
    private String payType;
    private String loanNo;

    public GeneralBean() {
        super();
    }

    public void setSysCode(BigDecimal sysCode) {
        this.sysCode = sysCode;
    }

    public BigDecimal getSysCode() {
        return sysCode;
    }

    public void setPolCode(BigDecimal polCode) {
        this.polCode = polCode;
    }

    public BigDecimal getPolCode() {
        return polCode;
    }

    public void setPprCode(BigDecimal pprCode) {
        this.pprCode = pprCode;
    }

    public BigDecimal getPprCode() {
        return pprCode;
    }

    public void setPrpCode(BigDecimal prpCode) {
        this.prpCode = prpCode;
    }

    public BigDecimal getPrpCode() {
        return prpCode;
    }

    public void setClientCode(BigDecimal clientCode) {
        this.clientCode = clientCode;
    }

    public BigDecimal getClientCode() {
        return clientCode;
    }

    public void setClientShortDesc(String clientShortDesc) {
        this.clientShortDesc = clientShortDesc;
    }

    public String getClientShortDesc() {
        return clientShortDesc;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientBankAccNum(String clientBankAccNum) {
        this.clientBankAccNum = clientBankAccNum;
    }

    public String getClientBankAccNum() {
        return clientBankAccNum;
    }

    public void setClientBbrCode(BigDecimal clientBbrCode) {
        this.clientBbrCode = clientBbrCode;
    }

    public BigDecimal getClientBbrCode() {
        return clientBbrCode;
    }

    public void setBbrBankCode(BigDecimal bbrBankCode) {
        this.bbrBankCode = bbrBankCode;
    }

    public BigDecimal getBbrBankCode() {
        return bbrBankCode;
    }

    public void setBbrBranchName(String bbrBranchName) {
        this.bbrBranchName = bbrBranchName;
    }

    public String getBbrBranchName() {
        return bbrBranchName;
    }

    public void setBbrShortDesc(String bbrShortDesc) {
        this.bbrShortDesc = bbrShortDesc;
    }

    public String getBbrShortDesc() {
        return bbrShortDesc;
    }

    public void setBbrRefCode(String bbrRefCode) {
        this.bbrRefCode = bbrRefCode;
    }

    public String getBbrRefCode() {
        return bbrRefCode;
    }

    public void setPolType(String polType) {
        this.polType = polType;
    }

    public String getPolType() {
        return polType;
    }

    public void setProposalNum(String proposalNum) {
        this.proposalNum = proposalNum;
    }

    public String getProposalNum() {
        return proposalNum;
    }

    public void setPolicyNum(String policyNum) {
        this.policyNum = policyNum;
    }

    public String getPolicyNum() {
        return policyNum;
    }

    public void setPremAmount(BigDecimal premAmount) {
        this.premAmount = premAmount;
    }

    public BigDecimal getPremAmount() {
        return premAmount;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setPrevDdhCode(BigDecimal prevDdhCode) {
        this.prevDdhCode = prevDdhCode;
    }

    public BigDecimal getPrevDdhCode() {
        return prevDdhCode;
    }

    public void setBookDate(Date bookDate) {
        this.bookDate = bookDate;
    }

    public Date getBookDate() {
        return bookDate;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayType() {
        return payType;
    }

    public void setLoanNo(String loanNo) {
        this.loanNo = loanNo;
    }

    public String getLoanNo() {
        return loanNo;
    }
}
