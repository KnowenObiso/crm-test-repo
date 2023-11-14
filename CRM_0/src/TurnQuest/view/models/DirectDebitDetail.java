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

package TurnQuest.view.models;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;


public class DirectDebitDetail implements Serializable {

    private BigDecimal dddCode;
    private BigDecimal dddDdhCode;
    private BigDecimal dddDdCode;
    private BigDecimal dddSysCode;
    private BigDecimal dddPolCode;
    private BigDecimal dddPolPrpCode;
    private String dddPolProposalNo;
    private String dddPolPolicyNo;
    private String dddOtherIdentifier;
    private BigDecimal dddAmount;
    private String dddRemarks;
    private Date dddStartDate;
    private Date dddStopDate;
    private String dddStatus;
    private Date dddFailDate;
    private String dddReceipted;
    private String dddFailUpdatedBy;
    private Date dddFailUpdateDate;
    private BigDecimal dddPprCode;
    private String dddPolType;
    private String dddReceiptedBy;
    private String dddReceiptNo;
    private Date dddReceiptDate;
    private Date dddReceiptedDate;
    private String dddFailRemarks;
    private Date dddRelaunchDate;
    private Date dddRelaunchStopDate;
    private String dddRelaunchedBy;
    private String dddRelaunchStoppedBy;
    private String ddRefNumber;
    private String ddBankBranch;
    private BigDecimal dddBankAmount;

    //fetch LMS products
    private BigDecimal PROD_CODE;
    private String PROD_DESC, PROD_UMBRELLA;
    private BigDecimal PROD_CLA_CODE;
    private String CLA_DESC, PROD_TYPE;

    public DirectDebitDetail() {
        super();
    }

    public void setDddCode(BigDecimal dddCode) {
        this.dddCode = dddCode;
    }

    public BigDecimal getDddCode() {
        return dddCode;
    }

    public void setDddDdhCode(BigDecimal dddDdhCode) {
        this.dddDdhCode = dddDdhCode;
    }

    public BigDecimal getDddDdhCode() {
        return dddDdhCode;
    }

    public void setDddDdCode(BigDecimal dddDdCode) {
        this.dddDdCode = dddDdCode;
    }

    public BigDecimal getDddDdCode() {
        return dddDdCode;
    }

    public void setDddSysCode(BigDecimal dddSysCode) {
        this.dddSysCode = dddSysCode;
    }

    public BigDecimal getDddSysCode() {
        return dddSysCode;
    }

    public void setDddPolCode(BigDecimal dddPolCode) {
        this.dddPolCode = dddPolCode;
    }

    public BigDecimal getDddPolCode() {
        return dddPolCode;
    }

    public void setDddPolPrpCode(BigDecimal dddPolPrpCode) {
        this.dddPolPrpCode = dddPolPrpCode;
    }

    public BigDecimal getDddPolPrpCode() {
        return dddPolPrpCode;
    }

    public void setDddPolProposalNo(String dddPolProposalNo) {
        this.dddPolProposalNo = dddPolProposalNo;
    }

    public String getDddPolProposalNo() {
        return dddPolProposalNo;
    }

    public void setDddPolPolicyNo(String dddPolPolicyNo) {
        this.dddPolPolicyNo = dddPolPolicyNo;
    }

    public String getDddPolPolicyNo() {
        return dddPolPolicyNo;
    }

    public void setDddOtherIdentifier(String dddOtherIdentifier) {
        this.dddOtherIdentifier = dddOtherIdentifier;
    }

    public String getDddOtherIdentifier() {
        return dddOtherIdentifier;
    }

    public void setDddAmount(BigDecimal dddAmount) {
        this.dddAmount = dddAmount;
    }

    public BigDecimal getDddAmount() {
        return dddAmount;
    }

    public void setDddRemarks(String dddRemarks) {
        this.dddRemarks = dddRemarks;
    }

    public String getDddRemarks() {
        return dddRemarks;
    }

    public void setDddStartDate(Date dddStartDate) {
        this.dddStartDate = dddStartDate;
    }

    public Date getDddStartDate() {
        return dddStartDate;
    }

    public void setDddStopDate(Date dddStopDate) {
        this.dddStopDate = dddStopDate;
    }

    public Date getDddStopDate() {
        return dddStopDate;
    }

    public void setDddStatus(String dddStatus) {
        this.dddStatus = dddStatus;
    }

    public String getDddStatus() {
        return dddStatus;
    }

    public void setDddFailDate(Date dddFailDate) {
        this.dddFailDate = dddFailDate;
    }

    public Date getDddFailDate() {
        return dddFailDate;
    }

    public void setDddReceipted(String dddReceipted) {
        this.dddReceipted = dddReceipted;
    }

    public String getDddReceipted() {
        return dddReceipted;
    }

    public void setDddFailUpdatedBy(String dddFailUpdatedBy) {
        this.dddFailUpdatedBy = dddFailUpdatedBy;
    }

    public String getDddFailUpdatedBy() {
        return dddFailUpdatedBy;
    }

    public void setDddFailUpdateDate(Date dddFailUpdateDate) {
        this.dddFailUpdateDate = dddFailUpdateDate;
    }

    public Date getDddFailUpdateDate() {
        return dddFailUpdateDate;
    }

    public void setDddPprCode(BigDecimal dddPprCode) {
        this.dddPprCode = dddPprCode;
    }

    public BigDecimal getDddPprCode() {
        return dddPprCode;
    }

    public void setDddPolType(String dddPolType) {
        this.dddPolType = dddPolType;
    }

    public String getDddPolType() {
        return dddPolType;
    }

    public void setDddReceiptedBy(String dddReceiptedBy) {
        this.dddReceiptedBy = dddReceiptedBy;
    }

    public String getDddReceiptedBy() {
        return dddReceiptedBy;
    }

    public void setDddReceiptNo(String dddReceiptNo) {
        this.dddReceiptNo = dddReceiptNo;
    }

    public String getDddReceiptNo() {
        return dddReceiptNo;
    }

    public void setDddReceiptDate(Date dddReceiptDate) {
        this.dddReceiptDate = dddReceiptDate;
    }

    public Date getDddReceiptDate() {
        return dddReceiptDate;
    }

    public void setDddReceiptedDate(Date dddReceiptedDate) {
        this.dddReceiptedDate = dddReceiptedDate;
    }

    public Date getDddReceiptedDate() {
        return dddReceiptedDate;
    }

    public void setDddFailRemarks(String dddFailRemarks) {
        this.dddFailRemarks = dddFailRemarks;
    }

    public String getDddFailRemarks() {
        return dddFailRemarks;
    }

    public void setDddRelaunchDate(Date dddRelaunchDate) {
        this.dddRelaunchDate = dddRelaunchDate;
    }

    public Date getDddRelaunchDate() {
        return dddRelaunchDate;
    }

    public void setDddRelaunchStopDate(Date dddRelaunchStopDate) {
        this.dddRelaunchStopDate = dddRelaunchStopDate;
    }

    public Date getDddRelaunchStopDate() {
        return dddRelaunchStopDate;
    }

    public void setDddRelaunchedBy(String dddRelaunchedBy) {
        this.dddRelaunchedBy = dddRelaunchedBy;
    }

    public String getDddRelaunchedBy() {
        return dddRelaunchedBy;
    }

    public void setDddRelaunchStoppedBy(String dddRelaunchStoppedBy) {
        this.dddRelaunchStoppedBy = dddRelaunchStoppedBy;
    }

    public String getDddRelaunchStoppedBy() {
        return dddRelaunchStoppedBy;
    }

    public void setDdRefNumber(String ddRefNumber) {
        this.ddRefNumber = ddRefNumber;
    }

    public String getDdRefNumber() {
        return ddRefNumber;
    }

    public void setDdBankBranch(String ddBankBranch) {
        this.ddBankBranch = ddBankBranch;
    }

    public String getDdBankBranch() {
        return ddBankBranch;
    }

    public void setDddBankAmount(BigDecimal dddBankAmount) {
        this.dddBankAmount = dddBankAmount;
    }

    public BigDecimal getDddBankAmount() {
        return dddBankAmount;
    }

    public void setPROD_CODE(BigDecimal PROD_CODE) {
        this.PROD_CODE = PROD_CODE;
    }

    public BigDecimal getPROD_CODE() {
        return PROD_CODE;
    }

    public void setPROD_DESC(String PROD_DESC) {
        this.PROD_DESC = PROD_DESC;
    }

    public String getPROD_DESC() {
        return PROD_DESC;
    }

    public void setPROD_UMBRELLA(String PROD_UMBRELLA) {
        this.PROD_UMBRELLA = PROD_UMBRELLA;
    }

    public String getPROD_UMBRELLA() {
        return PROD_UMBRELLA;
    }

    public void setPROD_CLA_CODE(BigDecimal PROD_CLA_CODE) {
        this.PROD_CLA_CODE = PROD_CLA_CODE;
    }

    public BigDecimal getPROD_CLA_CODE() {
        return PROD_CLA_CODE;
    }

    public void setCLA_DESC(String CLA_DESC) {
        this.CLA_DESC = CLA_DESC;
    }

    public String getCLA_DESC() {
        return CLA_DESC;
    }

    public void setPROD_TYPE(String PROD_TYPE) {
        this.PROD_TYPE = PROD_TYPE;
    }

    public String getPROD_TYPE() {
        return PROD_TYPE;
    }
}
