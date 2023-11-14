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

package TurnQuest.view.Usr;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;


/**
 * The Personnel Bean. Includes properties and methods that map to a
 * given Personnel.
 *
 * @author Frankline Ogongi
 */
public class Personnel implements Serializable {

    private String perEsCode;
    private String perNpCode;
    private BigDecimal perAuthorizedByConId;
    private BigDecimal perVpappId;
    private BigDecimal perCouId;
    private String perReligionCode;
    private String perEoCode;
    private BigDecimal perStatusId;
    private BigDecimal perId;
    private String perNo;
    private String perFirstName;
    private String perMiddleName;
    private String perLastName;
    private String perGender;
    private String perMaritalStatus;
    private Date perDateOfBirth;
    private Date perWef;
    private String perLoaded;
    private String conCode;
    private String perFullNames;

    /**
     * Default Class Constructor
     */
    public Personnel() {
        super();
    }

    public void setPerEsCode(String perEsCode) {
        this.perEsCode = perEsCode;
    }

    public String getPerEsCode() {
        return perEsCode;
    }

    public void setPerNpCode(String perNpCode) {
        this.perNpCode = perNpCode;
    }

    public String getPerNpCode() {
        return perNpCode;
    }

    public void setPerAuthorizedByConId(BigDecimal perAuthorizedByConId) {
        this.perAuthorizedByConId = perAuthorizedByConId;
    }

    public BigDecimal getPerAuthorizedByConId() {
        return perAuthorizedByConId;
    }

    public void setPerVpappId(BigDecimal perVpappId) {
        this.perVpappId = perVpappId;
    }

    public BigDecimal getPerVpappId() {
        return perVpappId;
    }

    public void setPerCouId(BigDecimal perCouId) {
        this.perCouId = perCouId;
    }

    public BigDecimal getPerCouId() {
        return perCouId;
    }

    public void setPerReligionCode(String perReligionCode) {
        this.perReligionCode = perReligionCode;
    }

    public String getPerReligionCode() {
        return perReligionCode;
    }

    public void setPerEoCode(String perEoCode) {
        this.perEoCode = perEoCode;
    }

    public String getPerEoCode() {
        return perEoCode;
    }

    public void setPerStatusId(BigDecimal perStatusId) {
        this.perStatusId = perStatusId;
    }

    public BigDecimal getPerStatusId() {
        return perStatusId;
    }

    public void setPerId(BigDecimal perId) {
        this.perId = perId;
    }

    public BigDecimal getPerId() {
        return perId;
    }

    public void setPerNo(String perNo) {
        this.perNo = perNo;
    }

    public String getPerNo() {
        return perNo;
    }

    public void setPerFirstName(String perFirstName) {
        this.perFirstName = perFirstName;
    }

    public String getPerFirstName() {
        return perFirstName;
    }

    public void setPerMiddleName(String perMiddleName) {
        this.perMiddleName = perMiddleName;
    }

    public String getPerMiddleName() {
        return perMiddleName;
    }

    public void setPerLastName(String perLastName) {
        this.perLastName = perLastName;
    }

    public String getPerLastName() {
        return perLastName;
    }

    public void setPerGender(String perGender) {
        this.perGender = perGender;
    }

    public String getPerGender() {
        return perGender;
    }

    public void setPerMaritalStatus(String perMaritalStatus) {
        this.perMaritalStatus = perMaritalStatus;
    }

    public String getPerMaritalStatus() {
        return perMaritalStatus;
    }

    public void setPerDateOfBirth(Date perDateOfBirth) {
        this.perDateOfBirth = perDateOfBirth;
    }

    public Date getPerDateOfBirth() {
        return perDateOfBirth;
    }

    public void setPerWef(Date perWef) {
        this.perWef = perWef;
    }

    public Date getPerWef() {
        return perWef;
    }

    public void setPerLoaded(String perLoaded) {
        this.perLoaded = perLoaded;
    }

    public String getPerLoaded() {
        return perLoaded;
    }

    public void setPerFullNames(String perFullNames) {
        this.perFullNames = perFullNames;
    }

    public String getPerFullNames() {
        return perFullNames;
    }

    public void setConCode(String conCode) {
        this.conCode = conCode;
    }

    public String getConCode() {
        return conCode;
    }
}
