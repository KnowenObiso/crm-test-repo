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


public class DirectDebitHeader implements Serializable {

    private BigDecimal ddhCode;
    private BigDecimal ddhDDCode;
    private BigDecimal ddhClientCode;
    private BigDecimal ddhClientBbrCode;
    private BigDecimal ddhBbrBankCode;
    private String ddhClientShortDesc;
    private String ddhClientName;
    private String ddhClientBankAccNum;
    private String ddhBbrBranchName;
    private String ddhBbrShortDesc;
    private String ddhBbrRefCode;
    private BigDecimal ddhTotAmount;
    private String ddhStatus;
    private String ddhReceipted;
    private Date ddhFailDate;
    private String ddhFailUpdatedBy;
    private Date ddhFailUpdateDate;
    private String ddhFailRemarks;
    private Date ddhRelaunchDate;
    private Date ddhRelaunchStopDate;
    private String ddhRelaunchedBy;
    private String ddhRelaunchStoppedBy;
    private Date ddhInitialBookDate;
    private BigDecimal ddhPrevDdhCode;
    private String ddhAccHolder;

    private String bankBranch;

    public DirectDebitHeader() {
        super();
    }

    public void setDdhCode(BigDecimal ddhCode) {
        this.ddhCode = ddhCode;
    }

    public BigDecimal getDdhCode() {
        return ddhCode;
    }

    public void setDdhDDCode(BigDecimal ddhDDCode) {
        this.ddhDDCode = ddhDDCode;
    }

    public BigDecimal getDdhDDCode() {
        return ddhDDCode;
    }

    public void setDdhClientCode(BigDecimal ddhClientCode) {
        this.ddhClientCode = ddhClientCode;
    }

    public BigDecimal getDdhClientCode() {
        return ddhClientCode;
    }

    public void setDdhClientBbrCode(BigDecimal ddhClientBbrCode) {
        this.ddhClientBbrCode = ddhClientBbrCode;
    }

    public BigDecimal getDdhClientBbrCode() {
        return ddhClientBbrCode;
    }

    public void setDdhBbrBankCode(BigDecimal ddhBbrBankCode) {
        this.ddhBbrBankCode = ddhBbrBankCode;
    }

    public BigDecimal getDdhBbrBankCode() {
        return ddhBbrBankCode;
    }

    public void setDdhClientShortDesc(String ddhClientShortDesc) {
        this.ddhClientShortDesc = ddhClientShortDesc;
    }

    public String getDdhClientShortDesc() {
        return ddhClientShortDesc;
    }

    public void setDdhClientName(String ddhClientName) {
        this.ddhClientName = ddhClientName;
    }

    public String getDdhClientName() {
        return ddhClientName;
    }

    public void setDdhClientBankAccNum(String ddhClientBankAccNum) {
        this.ddhClientBankAccNum = ddhClientBankAccNum;
    }

    public String getDdhClientBankAccNum() {
        return ddhClientBankAccNum;
    }

    public void setDdhBbrBranchName(String ddhBbrBranchName) {
        this.ddhBbrBranchName = ddhBbrBranchName;
    }

    public String getDdhBbrBranchName() {
        return ddhBbrBranchName;
    }

    public void setDdhBbrShortDesc(String ddhBbrShortDesc) {
        this.ddhBbrShortDesc = ddhBbrShortDesc;
    }

    public String getDdhBbrShortDesc() {
        return ddhBbrShortDesc;
    }

    public void setDdhBbrRefCode(String ddhBbrRefCode) {
        this.ddhBbrRefCode = ddhBbrRefCode;
    }

    public String getDdhBbrRefCode() {
        return ddhBbrRefCode;
    }

    public void setDdhTotAmount(BigDecimal ddhTotAmount) {
        this.ddhTotAmount = ddhTotAmount;
    }

    public BigDecimal getDdhTotAmount() {
        return ddhTotAmount;
    }

    public void setDdhStatus(String ddhStatus) {
        this.ddhStatus = ddhStatus;
    }

    public String getDdhStatus() {
        return ddhStatus;
    }

    public void setDdhReceipted(String ddhReceipted) {
        this.ddhReceipted = ddhReceipted;
    }

    public String getDdhReceipted() {
        return ddhReceipted;
    }

    public void setDdhFailDate(Date ddhFailDate) {
        this.ddhFailDate = ddhFailDate;
    }

    public Date getDdhFailDate() {
        return ddhFailDate;
    }

    public void setDdhFailUpdatedBy(String ddhFailUpdatedBy) {
        this.ddhFailUpdatedBy = ddhFailUpdatedBy;
    }

    public String getDdhFailUpdatedBy() {
        return ddhFailUpdatedBy;
    }

    public void setDdhFailUpdateDate(Date ddhFailUpdateDate) {
        this.ddhFailUpdateDate = ddhFailUpdateDate;
    }

    public Date getDdhFailUpdateDate() {
        return ddhFailUpdateDate;
    }

    public void setDdhFailRemarks(String ddhFailRemarks) {
        this.ddhFailRemarks = ddhFailRemarks;
    }

    public String getDdhFailRemarks() {
        return ddhFailRemarks;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setDdhRelaunchDate(Date ddhRelaunchDate) {
        this.ddhRelaunchDate = ddhRelaunchDate;
    }

    public Date getDdhRelaunchDate() {
        return ddhRelaunchDate;
    }

    public void setDdhRelaunchStopDate(Date ddhRelaunchStopDate) {
        this.ddhRelaunchStopDate = ddhRelaunchStopDate;
    }

    public Date getDdhRelaunchStopDate() {
        return ddhRelaunchStopDate;
    }

    public void setDdhRelaunchedBy(String ddhRelaunchedBy) {
        this.ddhRelaunchedBy = ddhRelaunchedBy;
    }

    public String getDdhRelaunchedBy() {
        return ddhRelaunchedBy;
    }

    public void setDdhRelaunchStoppedBy(String ddhRelaunchStoppedBy) {
        this.ddhRelaunchStoppedBy = ddhRelaunchStoppedBy;
    }

    public String getDdhRelaunchStoppedBy() {
        return ddhRelaunchStoppedBy;
    }

    public void setDdhInitialBookDate(Date ddhInitialBookDate) {
        this.ddhInitialBookDate = ddhInitialBookDate;
    }

    public Date getDdhInitialBookDate() {
        return ddhInitialBookDate;
    }

    public void setDdhPrevDdhCode(BigDecimal ddhPrevDdhCode) {
        this.ddhPrevDdhCode = ddhPrevDdhCode;
    }

    public BigDecimal getDdhPrevDdhCode() {
        return ddhPrevDdhCode;
    }

    public void setDdhAccHolder(String ddhAccHolder) {
        this.ddhAccHolder = ddhAccHolder;
    }

    public String getDdhAccHolder() {
        return ddhAccHolder;
    }
}
