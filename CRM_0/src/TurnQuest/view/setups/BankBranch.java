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

import java.sql.Blob;

import java.util.Date;


/**
 * The BankBranch Bean. Includes properties and methods that map to a
 * given BankBranch.
 *
 * @author Frankline Ogongi
 */
public class BankBranch implements Serializable {

    /* Properties */
    private String branchCode;
    private String branchBankCode;
    private String branchName;
    private String remarks;
    private String shortDesc;
    private String refCode;
    private String EFTSupported;
    private String DDSupported;
    private Date dateCreated;
    private String createdBy;
    private String physicalAddress;
    private String postalAddress;
    private String KBACode;
    private Blob logo;
    private String bankTerritoryCode;
    private String bankTerritoryName;
    private String countryCode;
    private String branchEmail;
    private String branchPersonName;
    private String branchPersonEmail;
    private String branchPersonPhone;

    /**
     * Default Constructor
     */


    public BankBranch() {
        super();
    }

    /* Methods */


    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchBankCode(String branchBankCode) {
        this.branchBankCode = branchBankCode;
    }

    public String getBranchBankCode() {
        return branchBankCode;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchName() {
        return branchName;
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

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public String getRefCode() {
        return refCode;
    }

    public void setEFTSupported(String EFTSupported) {
        this.EFTSupported = EFTSupported;
    }

    public String getEFTSupported() {
        return EFTSupported;
    }

    public void setDDSupported(String DDSupported) {
        this.DDSupported = DDSupported;
    }

    public String getDDSupported() {
        return DDSupported;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setKBACode(String KBACode) {
        this.KBACode = KBACode;
    }

    public String getKBACode() {
        return KBACode;
    }

    public void setLogo(Blob logo) {
        this.logo = logo;
    }

    public Blob getLogo() {
        return logo;
    }

    public void setBankTerritoryCode(String bankTownCode) {
        this.bankTerritoryCode = bankTownCode;
    }

    public String getBankTerritoryCode() {
        return bankTerritoryCode;
    }

    public void setBankTerritoryName(String bankTownName) {
        this.bankTerritoryName = bankTownName;
    }

    public String getBankTerritoryName() {
        return bankTerritoryName;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }
    public void setBranchPersonPhone(String branchPersonPhone) {
        this.branchPersonPhone = branchPersonPhone;
    }

    public String getBranchPersonPhone() {
        return branchPersonPhone;
    }

    public void setBranchPersonEmail(String branchPersonEmail) {
        this.branchPersonEmail = branchPersonEmail;
    }

    public String getBranchPersonEmail() {
        return branchPersonEmail;
    }

    public void setBranchPersonName(String branchPersonName) {
        this.branchPersonName = branchPersonName;
    }

    public String getBranchPersonName() {
        return branchPersonName;
    }

    public void setBranchEmail(String branchEmail) {
        this.branchEmail = branchEmail;
    }

    public String getBranchEmail() {
        return branchEmail;
    }
}
