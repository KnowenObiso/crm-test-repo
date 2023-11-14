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

package TurnQuest.view.Clients;

import java.math.BigDecimal;

import java.util.Date;


/**
 * The Client Bean. Includes properties and methods that map to a
 * given Client.
 *
 * @author Frankline Ogongi
 */
public class client {

    private String othernames;
    private String surname;
    private String town;

    private String postalCode;
    private String postalAddress;
    private String country;
    private String telphoneOne;
    private String telphoneTwo;
    private String fax;
    private String clientID;
    private int agentCode;
    private Date previousLastLoginOn = new Date();
    private String fullname = "";

    private BigDecimal clientCode;

    private String shortDescription;

    private String idRegNumber;

    private String physicalAddress;

    private String email;
    private String smsTel;

    private String policyNumber;
    private String PINNumber;
    private String ZIPCode;

    public void setOthernames(String othernames) {
        this.othernames = othernames;
    }

    public String getOthernames() {
        return othernames;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getTown() {
        return town;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setTelphoneOne(String telphoneOne) {
        this.telphoneOne = telphoneOne;
    }

    public String getTelphoneOne() {
        return telphoneOne;
    }

    public void setTelphoneTwo(String telphoneTwo) {
        this.telphoneTwo = telphoneTwo;
    }

    public String getTelphoneTwo() {
        return telphoneTwo;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFax() {
        return fax;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setAgentCode(int agentCode) {
        this.agentCode = agentCode;
    }

    public int getAgentCode() {
        return agentCode;
    }

    public void setPreviousLastLoginOn(Date previousLastLoginOn) {
        this.previousLastLoginOn = previousLastLoginOn;
    }

    public Date getPreviousLastLoginOn() {
        return previousLastLoginOn;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setClientCode(BigDecimal clientCode) {
        this.clientCode = clientCode;
    }

    public BigDecimal getClientCode() {
        return clientCode;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setIdRegNumber(String idRegNumber) {
        this.idRegNumber = idRegNumber;
    }

    public String getIdRegNumber() {
        return idRegNumber;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setSmsTel(String smsTel) {
        this.smsTel = smsTel;
    }

    public String getSmsTel() {
        return smsTel;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPINNumber(String PINNumber) {
        this.PINNumber = PINNumber;
    }

    public String getPINNumber() {
        return PINNumber;
    }

    public void setZIPCode(String ZIPCode) {
        this.ZIPCode = ZIPCode;
    }

    public String getZIPCode() {
        return ZIPCode;
    }
}
