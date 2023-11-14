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

package TurnQuest.view.Accounts;

import java.math.BigDecimal;

/**
 * The AgencyClass Bean. Includes properties and methods that map to a
 * given AgencyClass.
 *
 * @author Frankline Ogongi
 */
public class AgencyClass {

    private String code;
    private String description;


    private BigDecimal agentCode;
    private BigDecimal accountCode;
    private String agntShtDesc;
    private String agentName;
    private String physicalAddress;
    private String postalAddress;
    private String emailAddress;
    private String agntSubagnt;
    private BigDecimal mainAgnt_code;
    private String AccountTypes;

    /**
     * Default Class Constructor
     */
    public AgencyClass() {
        super();
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDecsription() {
        return description;
    }

    public void setAgentCode(BigDecimal agentCode) {
        this.agentCode = agentCode;
    }

    public BigDecimal getAgentCode() {
        return agentCode;
    }

    public void setAccountCode(BigDecimal accountCode) {
        this.accountCode = accountCode;
    }

    public BigDecimal getAccountCode() {
        return accountCode;
    }

    public void setAgntShtDesc(String agntShtDesc) {
        this.agntShtDesc = agntShtDesc;
    }

    public String getAgntShtDesc() {
        return agntShtDesc;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentName() {
        return agentName;
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

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setAgntSubagnt(String agntSubagnt) {
        this.agntSubagnt = agntSubagnt;
    }

    public String getAgntSubagnt() {
        return agntSubagnt;
    }

    public void setMainAgnt_code(BigDecimal mainAgnt_code) {
        this.mainAgnt_code = mainAgnt_code;
    }

    public BigDecimal getMainAgnt_code() {
        return mainAgnt_code;
    }

    public void setAccountTypes(String AccountTypes) {
        this.AccountTypes = AccountTypes;
    }

    public String getAccountTypes() {
        return AccountTypes;
    }
}
