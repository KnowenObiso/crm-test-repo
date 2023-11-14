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

package TurnQuest.view.provider;

import java.math.BigDecimal;

import java.util.Date;


/**
 * The ServiceProvider Bean. Includes properties and methods that map to a
 * given ServiceProvider.
 *
 * @author Frankline Ogongi
 */
public class ServiceProvider {

    /* Properties */
    private String code;
    private String shortDesc;
    private String name;
    private String physicalAddress;
    private String postalAddress;
    private String townCode;
    private String countryCode;
    private String providerTypeCode;
    private String phone;
    private String fax;
    private String email;
    private String title;
    private String zip;
    private Date wef;
    private Date wet;
    private String contact;
    private String aimsCode;
    private String bankBranchCode;
    private String bankAccNo;
    private String createdBy;
    private Date dateCreated;
    private String statusRemarks;
    private String status;
    private String PINNumber;
    private String trsOccupation;
    private String proffBody;
    private String PIN;
    private String docPhone;
    private String docEmail;

    private String countryName;
    private String townName;
    private BigDecimal bankCode;
    private String bankName;
    private String bankBranchName;
    private String inhouse;

    private String prefix;
    private BigDecimal mptCode;
    private BigDecimal mptpCode;
    private String smsNumber;
    private String invoiceNumber;
    private BigDecimal clientCode;
    private String clientName;

    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private String contactTel;


    private BigDecimal sprBpnCode;
    private String bpnName;
    private String telPay;
    private String defaultProvider;
    
    private String regNo;
    private String postalcode;
    
    private String idType;
    private String idNo;              
    
    
    
    
    
    private String provContacCode;
    private String provName;
    private String provTitle;
    private String provOfficeTelNo;
    private String provMobileNo;
    private String provEmail;
    private String provServiceProvider;
    private String provCode;

    /**
     * Default Class Constructor
     */
    public ServiceProvider() {
        super();
    }

    /* Methods */

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

    public void setTownCode(String townCode) {
        this.townCode = townCode;
    }

    public String getTownCode() {
        return townCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setProviderTypeCode(String providerTypeCode) {
        this.providerTypeCode = providerTypeCode;
    }

    public String getProviderTypeCode() {
        return providerTypeCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFax() {
        return fax;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getZip() {
        return zip;
    }

    public void setWef(Date wef) {
        this.wef = wef;
    }

    public Date getWef() {
        return wef;
    }

    public void setWet(Date wet) {
        this.wet = wet;
    }

    public Date getWet() {
        return wet;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }

    public void setAimsCode(String aimsCode) {
        this.aimsCode = aimsCode;
    }

    public String getAimsCode() {
        return aimsCode;
    }

    public void setBankBranchCode(String bankBranchCode) {
        this.bankBranchCode = bankBranchCode;
    }

    public String getBankBranchCode() {
        return bankBranchCode;
    }

    public void setBankAccNo(String bankAccNo) {
        this.bankAccNo = bankAccNo;
    }

    public String getBankAccNo() {
        return bankAccNo;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setStatusRemarks(String statusRemarks) {
        this.statusRemarks = statusRemarks;
    }

    public String getStatusRemarks() {
        return statusRemarks;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setPINNumber(String PINNumber) {
        this.PINNumber = PINNumber;
    }

    public String getPINNumber() {
        return PINNumber;
    }

    public void setTrsOccupation(String trsOccupation) {
        this.trsOccupation = trsOccupation;
    }

    public String getTrsOccupation() {
        return trsOccupation;
    }

    public void setProffBody(String proffBody) {
        this.proffBody = proffBody;
    }

    public String getProffBody() {
        return proffBody;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public String getPIN() {
        return PIN;
    }

    public void setDocPhone(String docPhone) {
        this.docPhone = docPhone;
    }

    public String getDocPhone() {
        return docPhone;
    }

    public void setDocEmail(String docEmail) {
        this.docEmail = docEmail;
    }

    public String getDocEmail() {
        return docEmail;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getTownName() {
        return townName;
    }

    public void setBankCode(BigDecimal bankCode) {
        this.bankCode = bankCode;
    }

    public BigDecimal getBankCode() {
        return bankCode;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setInhouse(String inhouse) {
        this.inhouse = inhouse;
    }

    public String getInhouse() {
        return inhouse;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setMptCode(BigDecimal mptCode) {
        this.mptCode = mptCode;
    }

    public BigDecimal getMptCode() {
        return mptCode;
    }

    public void setMptpCode(BigDecimal mptpCode) {
        this.mptpCode = mptpCode;
    }

    public BigDecimal getMptpCode() {
        return mptpCode;
    }

    public void setSmsNumber(String smsNumber) {
        this.smsNumber = smsNumber;
    }

    public String getSmsNumber() {
        return smsNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setClientCode(BigDecimal clientCode) {
        this.clientCode = clientCode;
    }

    public BigDecimal getClientCode() {
        return clientCode;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setSprBpnCode(BigDecimal sprBpnCode) {
        this.sprBpnCode = sprBpnCode;
    }

    public BigDecimal getSprBpnCode() {
        return sprBpnCode;
    }

    public void setBpnName(String bpnName) {
        this.bpnName = bpnName;
    }

    public String getBpnName() {
        return bpnName;
    }

    public void setTelPay(String telPay) {
        this.telPay = telPay;
    }

    public String getTelPay() {
        return telPay;
    }

    public void setDefaultProvider(String defaultProvider) {
        this.defaultProvider = defaultProvider;
    }

    public String getDefaultProvider() {
        return defaultProvider;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setProvContacCode(String provContacCode) {
        this.provContacCode = provContacCode;
    }

    public String getProvContacCode() {
        return provContacCode;
    }

    public void setProvName(String provName) {
        this.provName = provName;
    }

    public String getProvName() {
        return provName;
    }

    public void setProvTitle(String provTitle) {
        this.provTitle = provTitle;
    }

    public String getProvTitle() {
        return provTitle;
    }

    public void setProvOfficeTelNo(String provOfficeTelNo) {
        this.provOfficeTelNo = provOfficeTelNo;
    }

    public String getProvOfficeTelNo() {
        return provOfficeTelNo;
    }

    public void setProvMobileNo(String provMobileNo) {
        this.provMobileNo = provMobileNo;
    }

    public String getProvMobileNo() {
        return provMobileNo;
    }

    public void setProvEmail(String provEmail) {
        this.provEmail = provEmail;
    }

    public String getProvEmail() {
        return provEmail;
    }

    

    public void setProvServiceProvider(String provServiceProvider) {
        this.provServiceProvider = provServiceProvider;
    }

    public String getProvServiceProvider() {
        return provServiceProvider;
    }

    public void setProvCode(String provCode) {
        this.provCode = provCode;
    }

    public String getProvCode() {
        return provCode;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIdNo() {
        return idNo;
    }
}
