

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
import java.util.List;


/**
 * The Year Bean. Includes properties and methods that map to a
 * given Year.
 *
 * @author Frankline Ogongi
 */
public class YearBean implements Serializable {

    private String year;
    private BigDecimal branchCode;
    private Date wefDate;
    private Date wetDate;
    private String state;
    private BigDecimal periods;
    private String balance;
    private String trn;

    private String orgCode;
    private String nodeType;
    private String myCustomDesc; // Used for outputing various fields on a single line
    private List<PeriodBean> periodList;

    /**
     * Default Class Constructor
     */
    public YearBean() {
        super();
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }

    public void setBranchCode(BigDecimal branchCode) {
        this.branchCode = branchCode;
    }

    public BigDecimal getBranchCode() {
        return branchCode;
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

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setPeriods(BigDecimal periods) {
        this.periods = periods;
    }

    public BigDecimal getPeriods() {
        return periods;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalance() {
        return balance;
    }

    public void setTrn(String trn) {
        this.trn = trn;
    }

    public String getTrn() {
        return trn;
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

    public void setPeriodList(List<PeriodBean> periodList) {
        this.periodList = periodList;
    }

    public List<PeriodBean> getPeriodList() {
        return periodList;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgCode() {
        return orgCode;
    }
}

