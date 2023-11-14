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

package TurnQuest.view.setups;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Blob;

import java.util.Date;


/**
 * The Bank Bean. Includes properties and methods that map to a
 * given Bank.
 *
 * @author Frankline Ogongi
 */
public class Bank implements Serializable {

    /* Properties */
    private String bankCode;
    private String bankName;
    private String remarks;
    private String shortDesc;
    private String DDRCode;
    private String DDFormatDesc;
    private String forwardingBankCode;
    private String KBACode;

    private String bbrBranchName;
    private BigDecimal bbrCode;
    private String bctNum;
    private String fwdBankName;
    private String ddReportFormat;
    private String ddrReportDesc;
    private String eftSupported;
    private String classType;
    private BigDecimal characterNo;
    private String negotiatedBank;
    private BigDecimal administativeAmnt;
    private Blob logo;
    private Date bnkWef;
    private Date bnkWet;
    private String bnkStatus;
    private String characterMaxNo;
    private String characterMinNo;
    private String countryCode;
    private String countryName;
    
    /**
     * Default Constructor
     */
    public Bank() {
        super();
    }

    /* Methods */

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setDDRCode(String DDRCode) {
        this.DDRCode = DDRCode;
    }

    public String getDDRCode() {
        return DDRCode;
    }

    public void setDDFormatDesc(String DDFormatDesc) {
        this.DDFormatDesc = DDFormatDesc;
    }

    public String getDDFormatDesc() {
        return DDFormatDesc;
    }

    public void setForwardingBankCode(String forwardingBankCode) {
        this.forwardingBankCode = forwardingBankCode;
    }

    public String getForwardingBankCode() {
        return forwardingBankCode;
    }

    public void setKBACode(String KBACode) {
        this.KBACode = KBACode;
    }

    public String getKBACode() {
        return KBACode;
    }

    public void setBbrBranchName(String bbrBranchName) {
        this.bbrBranchName = bbrBranchName;
    }

    public String getBbrBranchName() {
        return bbrBranchName;
    }

    public void setBbrCode(BigDecimal bbrCode) {
        this.bbrCode = bbrCode;
    }

    public BigDecimal getBbrCode() {
        return bbrCode;
    }

    public void setBctNum(String bctNum) {
        this.bctNum = bctNum;
    }

    public String getBctNum() {
        return bctNum;
    }

    public void setFwdBankName(String fwdBankName) {
        this.fwdBankName = fwdBankName;
    }

    public String getFwdBankName() {
        return fwdBankName;
    }

    public void setDdReportFormat(String ddReportFormat) {
        this.ddReportFormat = ddReportFormat;
    }

    public String getDdReportFormat() {
        return ddReportFormat;
    }

    public void setDdrReportDesc(String ddrReportDesc) {
        this.ddrReportDesc = ddrReportDesc;
    }

    public String getDdrReportDesc() {
        return ddrReportDesc;
    }

    public void setEftSupported(String eftSupported) {
        this.eftSupported = eftSupported;
    }

    public String getEftSupported() {
        return eftSupported;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getClassType() {
        return classType;
    }

    public void setCharacterNo(BigDecimal characterNo) {
        this.characterNo = characterNo;
    }

    public BigDecimal getCharacterNo() {
        return characterNo;
    }

    public void setNegotiatedBank(String negotiatedBank) {
        this.negotiatedBank = negotiatedBank;
    }

    public String getNegotiatedBank() {
        return negotiatedBank;
    }

    public void setAdministativeAmnt(BigDecimal administativeAmnt) {
        this.administativeAmnt = administativeAmnt;
    }

    public BigDecimal getAdministativeAmnt() {
        return administativeAmnt;
    }

    public void setLogo(Blob logo) {
        this.logo = logo;
    }

    public Blob getLogo() {
        return logo;
    }

    public void setBnkWef(Date bnkWef) {
        this.bnkWef = bnkWef;
    }

    public Date getBnkWef() {
        return bnkWef;
    }

    public void setBnkWet(Date bnkWet) {
        this.bnkWet = bnkWet;
    }

    public Date getBnkWet() {
        return bnkWet;
    }

    public void setBnkStatus(String bnkStatus) {
        this.bnkStatus = bnkStatus;
    }

    public String getBnkStatus() {
        return bnkStatus;
    }

    public void setCharacterMaxNo(String characterMaxNo) {
        this.characterMaxNo = characterMaxNo;
    }

    public String getCharacterMaxNo() {
        return characterMaxNo;
    }

    public void setCharacterMinNo(String characterMinNo) {
        this.characterMinNo = characterMinNo;
    }

    public String getCharacterMinNo() {
        return characterMinNo;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryName() {
        return countryName;
    }
}
