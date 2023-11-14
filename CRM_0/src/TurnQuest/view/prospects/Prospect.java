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

package TurnQuest.view.prospects;


import TurnQuest.view.Base.GlobalCC;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.SQLData;
import java.sql.SQLInput;
import java.sql.SQLOutput;

import java.util.Date;


public class Prospect implements SQLData, Serializable {

    private BigDecimal prsCode;
    private String prsSurname;
    private String prsPhysicalAddress;
    private String prsPostalAddress;
    private String prsOtherNames;
    private String prsTelNo;
    private String prsMobileNo;
    private String prsEmailAddress;
    private String prsIdRegNo;
    private Date prsDob;
    private String prsPin;
    private BigDecimal prsTownCode;
    private BigDecimal prsCountryCode;
    private BigDecimal prsPostalCode;

    private String countryName;
    private String townName;
    private String proDesc;

    private String SQLTypeName;
    private String type;
    private String prsContact;
    private String prsContactTel;

    public Prospect() {
        super();
    }

    /**
     * Populates this object with data read from the database.
     *
     * @param stream The SQLInput object from which to read the data for the
     * value that is being custom mapped
     * @param typeName the SQL type name of the value on the data stream
     */
    public void readSQL(SQLInput stream, String typeName) {
    }

    /**
     * Writes this object to the given SQL data stream, converting it back to
     * its SQL value in the data source.
     *
     * @param stream the SQLOutput object to which to write the data for the
     * value that was custom mapped
     */
    public void writeSQL(SQLOutput stream) {
        try {
            if (SQLTypeName.equalsIgnoreCase("TQC_PROSPECTS_OBJ")) {
                stream.writeBigDecimal(prsCode);
                stream.writeString(prsSurname);
                stream.writeString(prsPhysicalAddress);
                stream.writeString(prsPostalAddress);
                stream.writeString(prsOtherNames);
                stream.writeString(prsTelNo);
                stream.writeString(prsMobileNo);
                stream.writeString(prsEmailAddress);
                stream.writeString(prsIdRegNo);
                stream.writeDate(prsDob == null ? null :
                                 new java.sql.Date(prsDob.getTime()));
                stream.writeString(prsPin);
                stream.writeBigDecimal(prsTownCode);
                stream.writeBigDecimal(prsCountryCode);
                stream.writeBigDecimal(prsPostalCode);
                stream.writeString(type);
                stream.writeString(prsContact);
                stream.writeString(prsContactTel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
    }

    public void setSQLTypeName(String SQLTypeName) {
        this.SQLTypeName = SQLTypeName;
    }

    public String getSQLTypeName() {
        return SQLTypeName;
    }

    public void setPrsCode(BigDecimal prsCode) {
        this.prsCode = prsCode;
    }

    public BigDecimal getPrsCode() {
        return prsCode;
    }

    public void setPrsPhysicalAddress(String prsPhysicalAddress) {
        this.prsPhysicalAddress = prsPhysicalAddress;
    }

    public String getPrsPhysicalAddress() {
        return prsPhysicalAddress;
    }

    public void setPrsPostalAddress(String prsPostalAddress) {
        this.prsPostalAddress = prsPostalAddress;
    }

    public String getPrsPostalAddress() {
        return prsPostalAddress;
    }

    public void setPrsSurname(String prsSurname) {
        this.prsSurname = prsSurname;
    }

    public String getPrsSurname() {
        return prsSurname;
    }

    public void setPrsOtherNames(String prsOtherNames) {
        this.prsOtherNames = prsOtherNames;
    }

    public String getPrsOtherNames() {
        return prsOtherNames;
    }

    public void setPrsTelNo(String prsTelNo) {
        this.prsTelNo = prsTelNo;
    }

    public String getPrsTelNo() {
        return prsTelNo;
    }

    public void setPrsMobileNo(String prsMobileNo) {
        this.prsMobileNo = prsMobileNo;
    }

    public String getPrsMobileNo() {
        return prsMobileNo;
    }

    public void setPrsEmailAddress(String prsEmailAddress) {
        this.prsEmailAddress = prsEmailAddress;
    }

    public String getPrsEmailAddress() {
        return prsEmailAddress;
    }

    public void setPrsIdRegNo(String prsIdRegNo) {
        this.prsIdRegNo = prsIdRegNo;
    }

    public String getPrsIdRegNo() {
        return prsIdRegNo;
    }

    public void setPrsDob(Date prsDob) {
        this.prsDob = prsDob;
    }

    public Date getPrsDob() {
        return prsDob;
    }

    public void setPrsPin(String prsPin) {
        this.prsPin = prsPin;
    }

    public String getPrsPin() {
        return prsPin;
    }

    public void setPrsTownCode(BigDecimal prsTownCode) {
        this.prsTownCode = prsTownCode;
    }

    public BigDecimal getPrsTownCode() {
        return prsTownCode;
    }

    public void setPrsCountryCode(BigDecimal prsCountryCode) {
        this.prsCountryCode = prsCountryCode;
    }

    public BigDecimal getPrsCountryCode() {
        return prsCountryCode;
    }

    public void setPrsPostalCode(BigDecimal prsPostalCode) {
        this.prsPostalCode = prsPostalCode;
    }

    public BigDecimal getPrsPostalCode() {
        return prsPostalCode;
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

    public void setProDesc(String proDesc) {
        this.proDesc = proDesc;
    }

    public String getProDesc() {
        return proDesc;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setPrsContact(String prsContact) {
        this.prsContact = prsContact;
    }

    public String getPrsContact() {
        return prsContact;
    }

    public void setPrsContactTel(String prsContactTel) {
        this.prsContactTel = prsContactTel;
    }

    public String getPrsContactTel() {
        return prsContactTel;
    }
}
