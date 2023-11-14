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


//import fms.view.categories.CategoryBean;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.List;


/**
 * The Branch Bean. Includes properties and methods that map to a
 * given Branch.
 *
 * @author Frankline Ogongi
 */
public class Branch implements Serializable {

    private BigDecimal code;
    private String name;
    private String shortDesc;
    private String currencyCode;
    private String currencySymbol;
    private String regOrgCode;
    private String regCode;
    private String orgName;
    private BigDecimal couCode;
    private String couName;
    private String adminRgnType;


    private BigDecimal orgCode;
    private String regionName;

    private String currencyDesc;

    private String nodeType;
    private List<YearBean> yearList;
    //private List<CategoryBean> categoryList;

    /**
     * Default Class Constructor
     */
    public Branch() {
        super();
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public BigDecimal getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setRegOrgCode(String regOrgCode) {
        this.regOrgCode = regOrgCode;
    }

    public String getRegOrgCode() {
        return regOrgCode;
    }

    public void setRegCode(String regCode) {
        this.regCode = regCode;
    }

    public String getRegCode() {
        return regCode;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setYearList(List<YearBean> yearList) {
        this.yearList = yearList;
    }

    public List<YearBean> getYearList() {
        return yearList;
    }

    /*  public void setCategoryList(List<CategoryBean> categoryList) {
        this.categoryList = categoryList;
    }

    public List<CategoryBean> getCategoryList() {
        return categoryList;
    } */

    public void setOrgCode(BigDecimal orgCode) {
        this.orgCode = orgCode;
    }

    public BigDecimal getOrgCode() {
        return orgCode;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setCurrencyDesc(String currencyDesc) {
        this.currencyDesc = currencyDesc;
    }

    public String getCurrencyDesc() {
        return currencyDesc;
    }

    public void setCouCode(BigDecimal couCode) {
        this.couCode = couCode;
    }

    public BigDecimal getCouCode() {
        return couCode;
    }

    public void setCouName(String couName) {
        this.couName = couName;
    }

    public String getCouName() {
        return couName;
    }

    public void setAdminRgnType(String adminRgnType) {
        this.adminRgnType = adminRgnType;
    }

    public String getAdminRgnType() {
        return adminRgnType;
    }
}

