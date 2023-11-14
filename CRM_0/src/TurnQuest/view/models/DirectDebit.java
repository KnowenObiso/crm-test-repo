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


public class DirectDebit implements Serializable {

    private BigDecimal ddCode;
    private BigDecimal dddCode;
    private String ddRefNo;
    private String ddSentYn;
    private String ddAccountNum;
    private Date ddBookDate;
    private BigDecimal ddBbrCode;
    private String ddStatus;
    private String ddReceipted;
    private Date ddValueDate;
    private String ddRaisedBy;
    private Date ddDate;
    private BigDecimal ddBankCode;
    private String ddAuthBy;
    private Date ddAuthDate;
    private String ddAuthorized;
    private String ddDebitDay;

    private String bankBranch;
    private String policyNo;
    private String accountNo;
    private String sortCode;
    private BigDecimal amount;
    private String accName;
    private String narration;
    private String company;
    private String fail;
    private String failRemarks;
    private String bbRefCode;
    private String payFreq;
    private Date nextDueDate;
    private String ddRefNumber;
    private String ddBankBranch;
    private Date DD_RECEIPT_DATE; 
    private String DD_RECEIPT_NO;
    private String DD_ALLOC_STATUS;
    private BigDecimal DD_TOTAL_RECORDS;
    private BigDecimal DD_RECORDS_ALLOCATED;
    private BigDecimal DD_RECORDS_ALLOC_SUCCESS; 
    private BigDecimal DD_RECORDS_ALLOC_FAIL; 
    private BigDecimal DD_ALLOC_PERCENT; 
    private BigDecimal DD_ALLOC_SUCCESS_PERC; 
    private Date DD_EST_ALLOC_COMPL_DATE;
    private String DD_ALLOC_STATUS_CODE;

    public DirectDebit() {
        super();
    }

    public void setDdCode(BigDecimal ddCode) {
        this.ddCode = ddCode;
    }

    public BigDecimal getDdCode() {
        return ddCode;
    }

    public void setDdRefNo(String ddRefNo) {
        this.ddRefNo = ddRefNo;
    }

    public String getDdRefNo() {
        return ddRefNo;
    }

    public void setDdSentYn(String ddSentYn) {
        this.ddSentYn = ddSentYn;
    }

    public String getDdSentYn() {
        return ddSentYn;
    }

    public void setDdAccountNum(String ddAccountNum) {
        this.ddAccountNum = ddAccountNum;
    }

    public String getDdAccountNum() {
        return ddAccountNum;
    }

    public void setDdBookDate(Date ddBookDate) {
        this.ddBookDate = ddBookDate;
    }

    public Date getDdBookDate() {
        return ddBookDate;
    }

    public void setDdBbrCode(BigDecimal ddBbrCode) {
        this.ddBbrCode = ddBbrCode;
    }

    public BigDecimal getDdBbrCode() {
        return ddBbrCode;
    }

    public void setDdStatus(String ddStatus) {
        this.ddStatus = ddStatus;
    }

    public String getDdStatus() {
        return ddStatus;
    }

    public void setDdReceipted(String ddReceipted) {
        this.ddReceipted = ddReceipted;
    }

    public String getDdReceipted() {
        return ddReceipted;
    }

    public void setDdValueDate(Date ddValueDate) {
        this.ddValueDate = ddValueDate;
    }

    public Date getDdValueDate() {
        return ddValueDate;
    }

    public void setDdRaisedBy(String ddRaisedBy) {
        this.ddRaisedBy = ddRaisedBy;
    }

    public String getDdRaisedBy() {
        return ddRaisedBy;
    }

    public void setDdDate(Date ddDate) {
        this.ddDate = ddDate;
    }

    public Date getDdDate() {
        return ddDate;
    }

    public void setDdBankCode(BigDecimal ddBankCode) {
        this.ddBankCode = ddBankCode;
    }

    public BigDecimal getDdBankCode() {
        return ddBankCode;
    }

    public void setDdAuthBy(String ddAuthBy) {
        this.ddAuthBy = ddAuthBy;
    }

    public String getDdAuthBy() {
        return ddAuthBy;
    }

    public void setDdAuthDate(Date ddAuthDate) {
        this.ddAuthDate = ddAuthDate;
    }

    public Date getDdAuthDate() {
        return ddAuthDate;
    }

    public void setDdAuthorized(String ddAuthorized) {
        this.ddAuthorized = ddAuthorized;
    }

    public String getDdAuthorized() {
        return ddAuthorized;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getAccName() {
        return accName;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getNarration() {
        return narration;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public void setFail(String fail) {
        this.fail = fail;
    }

    public String getFail() {
        return fail;
    }

    public void setFailRemarks(String failRemarks) {
        this.failRemarks = failRemarks;
    }

    public String getFailRemarks() {
        return failRemarks;
    }

    public void setDddCode(BigDecimal dddCode) {
        this.dddCode = dddCode;
    }

    public BigDecimal getDddCode() {
        return dddCode;
    }

    public void setBbRefCode(String bbRefCode) {
        this.bbRefCode = bbRefCode;
    }

    public String getBbRefCode() {
        return bbRefCode;
    }

    public void setPayFreq(String payFreq) {
        this.payFreq = payFreq;
    }

    public String getPayFreq() {
        return payFreq;
    }


    public void setNextDueDate(Date nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    public Date getNextDueDate() {
        return nextDueDate;
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

    public void setDdDebitDay(String ddDebitDay) {
        this.ddDebitDay = ddDebitDay;
    }

    public String getDdDebitDay() {
        return ddDebitDay;
    }

    public void setDD_RECEIPT_DATE(Date DD_RECEIPT_DATE) {
        this.DD_RECEIPT_DATE = DD_RECEIPT_DATE;
    }

    public Date getDD_RECEIPT_DATE() {
        return DD_RECEIPT_DATE;
    }

    public void setDD_RECEIPT_NO(String DD_RECEIPT_NO) {
        this.DD_RECEIPT_NO = DD_RECEIPT_NO;
    }

    public String getDD_RECEIPT_NO() {
        return DD_RECEIPT_NO;
    }

    public void setDD_ALLOC_STATUS(String DD_ALLOC_STATUS) {
        this.DD_ALLOC_STATUS = DD_ALLOC_STATUS;
    }

    public String getDD_ALLOC_STATUS() {
        return DD_ALLOC_STATUS;
    }

    public void setDD_TOTAL_RECORDS(BigDecimal DD_TOTAL_RECORDS) {
        this.DD_TOTAL_RECORDS = DD_TOTAL_RECORDS;
    }

    public BigDecimal getDD_TOTAL_RECORDS() {
        return DD_TOTAL_RECORDS;
    }

    public void setDD_RECORDS_ALLOCATED(BigDecimal DD_RECORDS_ALLOCATED) {
        this.DD_RECORDS_ALLOCATED = DD_RECORDS_ALLOCATED;
    }

    public BigDecimal getDD_RECORDS_ALLOCATED() {
        return DD_RECORDS_ALLOCATED;
    }

    public void setDD_RECORDS_ALLOC_SUCCESS(BigDecimal DD_RECORDS_ALLOC_SUCCESS) {
        this.DD_RECORDS_ALLOC_SUCCESS = DD_RECORDS_ALLOC_SUCCESS;
    }

    public BigDecimal getDD_RECORDS_ALLOC_SUCCESS() {
        return DD_RECORDS_ALLOC_SUCCESS;
    }

    public void setDD_RECORDS_ALLOC_FAIL(BigDecimal DD_RECORDS_ALLOC_FAIL) {
        this.DD_RECORDS_ALLOC_FAIL = DD_RECORDS_ALLOC_FAIL;
    }

    public BigDecimal getDD_RECORDS_ALLOC_FAIL() {
        return DD_RECORDS_ALLOC_FAIL;
    }

    public void setDD_ALLOC_PERCENT(BigDecimal DD_ALLOC_PERCENT) {
        this.DD_ALLOC_PERCENT = DD_ALLOC_PERCENT;
    }

    public BigDecimal getDD_ALLOC_PERCENT() {
        return DD_ALLOC_PERCENT;
    }

    public void setDD_ALLOC_SUCCESS_PERC(BigDecimal DD_ALLOC_SUCCESS_PERC) {
        this.DD_ALLOC_SUCCESS_PERC = DD_ALLOC_SUCCESS_PERC;
    }

    public BigDecimal getDD_ALLOC_SUCCESS_PERC() {
        return DD_ALLOC_SUCCESS_PERC;
    }

    public void setDD_EST_ALLOC_COMPL_DATE(Date DD_EST_ALLOC_COMPL_DATE) {
        this.DD_EST_ALLOC_COMPL_DATE = DD_EST_ALLOC_COMPL_DATE;
    }

    public Date getDD_EST_ALLOC_COMPL_DATE() {
        return DD_EST_ALLOC_COMPL_DATE;
    }

    public void setDD_ALLOC_STATUS_CODE(String DD_ALLOC_STATUS_CODE) {
        this.DD_ALLOC_STATUS_CODE = DD_ALLOC_STATUS_CODE;
    }

    public String getDD_ALLOC_STATUS_CODE() {
        return DD_ALLOC_STATUS_CODE;
    }
}
