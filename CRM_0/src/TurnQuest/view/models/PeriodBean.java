
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


/**
 * The Period Bean. Includes properties and methods that map to a
 * given Period.
 *
 * @author Frankline Ogongi
 */
public class PeriodBean implements Serializable {

    private String period;
    private BigDecimal branchCode;
    private String year;
    private Date wefDate;
    private Date wetDate;
    private BigDecimal counter;
    private String state;
    private String start;
    private BigDecimal orgCode;
    private BigDecimal currPeriod;
    private BigDecimal curPeriod;

    private String nodeType;
    private String myCustomDesc; // Used for outputing various fields on a single line
    private String branchCurrencyCode;

    private String branchName;
    private String orgName;

    /**
     * Default Class Constructor
     */
    public PeriodBean() {
        super();
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }

    public void setBranchCode(BigDecimal branchCode) {
        this.branchCode = branchCode;
    }

    public BigDecimal getBranchCode() {
        return branchCode;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }

    public void setWefDate(Date wefDate) {
        this.wefDate = wefDate;
    }

    public Date getWefDate() {
        return wefDate;
    }

    public void setWetDate(Date wetDate) {
        this.wetDate = wetDate;
    }

    public Date getWetDate() {
        return wetDate;
    }

    public void setCounter(BigDecimal counter) {
        this.counter = counter;
    }

    public BigDecimal getCounter() {
        return counter;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStart() {
        return start;
    }

    public void setOrgCode(BigDecimal orgCode) {
        this.orgCode = orgCode;
    }

    public BigDecimal getOrgCode() {
        return orgCode;
    }

    public void setCurrPeriod(BigDecimal currPeriod) {
        this.currPeriod = currPeriod;
    }

    public BigDecimal getCurrPeriod() {
        return currPeriod;
    }

    public void setCurPeriod(BigDecimal curPeriod) {
        this.curPeriod = curPeriod;
    }

    public BigDecimal getCurPeriod() {
        return curPeriod;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setMyCustomDesc(String myCustomDesc) {
        this.myCustomDesc = myCustomDesc;
    }

    public String getMyCustomDesc() {
        return myCustomDesc;
    }

    public void setBranchCurrencyCode(String branchCurrencyCode) {
        this.branchCurrencyCode = branchCurrencyCode;
    }

    public String getBranchCurrencyCode() {
        return branchCurrencyCode;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgName() {
        return orgName;
    }
}

