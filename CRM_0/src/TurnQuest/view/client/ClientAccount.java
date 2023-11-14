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

package TurnQuest.view.client;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;


/**
 * The ClientAccount Bean. Includes properties and methods that map to a
 * given ClientAccount.
 *
 * @author Frankline Ogongi
 */
public class ClientAccount implements Serializable {

    private BigDecimal code;
    private String shortDesc;
    private String name;
    private BigDecimal clientCode;
    private String createdBy;
    private Date dateCreated;
    private String status;
    private String remarks;
    private Date wef;
    private Date wet;
    private BigDecimal bdivCode;
    private String brnName;
    private String divName;

    private BigDecimal clcoCode;
    private String clcoName;
    private String clcoPostAdd;
    private String clcoPhysAdd;
    private BigDecimal secCode;
    private String secName;
    private String contactName;
    private String contactTitle;
    private String contactSmsNo;
    private String contactEmail;

    /**
     * Default Class Constructor
     */
    public ClientAccount() {
        super();
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public BigDecimal getCode() {
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

    public void setClientCode(BigDecimal clientCode) {
        this.clientCode = clientCode;
    }

    public BigDecimal getClientCode() {
        return clientCode;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
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


    public void setBrnName(String brnName) {
        this.brnName = brnName;
    }

    public String getBrnName() {
        return brnName;
    }

    public void setDivName(String divName) {
        this.divName = divName;
    }

    public String getDivName() {
        return divName;
    }

    public void setBdivCode(BigDecimal bdivCode) {
        this.bdivCode = bdivCode;
    }

    public BigDecimal getBdivCode() {
        return bdivCode;
    }

    public void setClcoCode(BigDecimal clcoCode) {
        this.clcoCode = clcoCode;
    }

    public BigDecimal getClcoCode() {
        return clcoCode;
    }

    public void setClcoName(String clcoName) {
        this.clcoName = clcoName;
    }

    public String getClcoName() {
        return clcoName;
    }

    public void setClcoPostAdd(String clcoPostAdd) {
        this.clcoPostAdd = clcoPostAdd;
    }

    public String getClcoPostAdd() {
        return clcoPostAdd;
    }

    public void setClcoPhysAdd(String clcoPhysAdd) {
        this.clcoPhysAdd = clcoPhysAdd;
    }

    public String getClcoPhysAdd() {
        return clcoPhysAdd;
    }

    public void setSecCode(BigDecimal secCode) {
        this.secCode = secCode;
    }

    public BigDecimal getSecCode() {
        return secCode;
    }

    public void setSecName(String secName) {
        this.secName = secName;
    }

    public String getSecName() {
        return secName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactTitle(String contactTitle) {
        this.contactTitle = contactTitle;
    }

    public String getContactTitle() {
        return contactTitle;
    }

    public void setContactSmsNo(String contactSmsNo) {
        this.contactSmsNo = contactSmsNo;
    }

    public String getContactSmsNo() {
        return contactSmsNo;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactEmail() {
        return contactEmail;
    }
}
