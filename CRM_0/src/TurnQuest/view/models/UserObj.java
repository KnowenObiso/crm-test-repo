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


public class UserObj implements Serializable {

    private BigDecimal usrCode;
    private String usrUsername;
    private String usrName;
    private Date usrLastDate;
    private Date usrDateCreated;
    private String usrStatus;
    private String usrPassword;
    private String usrCreatedBy;
    private String usrEmail;
    private BigDecimal usrPerRankId;
    private String usrPerRankShortDesc;
    private BigDecimal usrPerId;
    private String usrPersonelRank;
    private Date usrPasswordChanged;
    private String usrPasswordReset;
    private BigDecimal usrLoginAttempts;
    private String usrSign;
    private String usrRef;
    private String usrType;
    private String usrSignature;
    private String usrAccountManager;

    public UserObj() {
        super();
    }

    public void setUsrCode(BigDecimal usrCode) {
        this.usrCode = usrCode;
    }

    public BigDecimal getUsrCode() {
        return usrCode;
    }

    public void setUsrUsername(String usrUsername) {
        this.usrUsername = usrUsername;
    }

    public String getUsrUsername() {
        return usrUsername;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getUsrName() {
        return usrName;
    }

    public void setUsrLastDate(Date usrLastDate) {
        this.usrLastDate = usrLastDate;
    }

    public Date getUsrLastDate() {
        return usrLastDate;
    }

    public void setUsrDateCreated(Date usrDateCreated) {
        this.usrDateCreated = usrDateCreated;
    }

    public Date getUsrDateCreated() {
        return usrDateCreated;
    }

    public void setUsrStatus(String usrStatus) {
        this.usrStatus = usrStatus;
    }

    public String getUsrStatus() {
        return usrStatus;
    }

    public void setUsrPassword(String usrPassword) {
        this.usrPassword = usrPassword;
    }

    public String getUsrPassword() {
        return usrPassword;
    }

    public void setUsrCreatedBy(String usrCreatedBy) {
        this.usrCreatedBy = usrCreatedBy;
    }

    public String getUsrCreatedBy() {
        return usrCreatedBy;
    }

    public void setUsrEmail(String usrEmail) {
        this.usrEmail = usrEmail;
    }

    public String getUsrEmail() {
        return usrEmail;
    }

    public void setUsrPerRankId(BigDecimal usrPerRankId) {
        this.usrPerRankId = usrPerRankId;
    }

    public BigDecimal getUsrPerRankId() {
        return usrPerRankId;
    }

    public void setUsrPerRankShortDesc(String usrPerRankShortDesc) {
        this.usrPerRankShortDesc = usrPerRankShortDesc;
    }

    public String getUsrPerRankShortDesc() {
        return usrPerRankShortDesc;
    }

    public void setUsrPerId(BigDecimal usrPerId) {
        this.usrPerId = usrPerId;
    }

    public BigDecimal getUsrPerId() {
        return usrPerId;
    }

    public void setUsrPersonelRank(String usrPersonelRank) {
        this.usrPersonelRank = usrPersonelRank;
    }

    public String getUsrPersonelRank() {
        return usrPersonelRank;
    }

    public void setUsrPasswordChanged(Date usrPasswordChanged) {
        this.usrPasswordChanged = usrPasswordChanged;
    }

    public Date getUsrPasswordChanged() {
        return usrPasswordChanged;
    }

    public void setUsrPasswordReset(String usrPasswordReset) {
        this.usrPasswordReset = usrPasswordReset;
    }

    public String getUsrPasswordReset() {
        return usrPasswordReset;
    }

    public void setUsrLoginAttempts(BigDecimal usrLoginAttempts) {
        this.usrLoginAttempts = usrLoginAttempts;
    }

    public BigDecimal getUsrLoginAttempts() {
        return usrLoginAttempts;
    }

    public void setUsrSign(String usrSign) {
        this.usrSign = usrSign;
    }

    public String getUsrSign() {
        return usrSign;
    }

    public void setUsrRef(String usrRef) {
        this.usrRef = usrRef;
    }

    public String getUsrRef() {
        return usrRef;
    }

    public void setUsrType(String usrType) {
        this.usrType = usrType;
    }

    public String getUsrType() {
        return usrType;
    }

    public void setUsrSignature(String usrSignature) {
        this.usrSignature = usrSignature;
    }

    public String getUsrSignature() {
        return usrSignature;
    }

    public void setUsrAccountManager(String usrAccountManager) {
        this.usrAccountManager = usrAccountManager;
    }

    public String getUsrAccountManager() {
        return usrAccountManager;
    }
}
