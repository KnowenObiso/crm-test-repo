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

import java.math.BigDecimal;


/**
 * The Sector Bean. Includes properties and methods that map to a
 * given Sector.
 *
 * @author Frankline Ogongi
 */
public class Sector implements Serializable {

    /* Properties */
    private String code;
    private String shortDesc;
    private String name;
    private BigDecimal AgentCode;
    private BigDecimal AgentAccountCode;
    private String agentShtDesc;
    private String agentName;
    private BigDecimal occupationCode;

    /**
     * Default Constructor
     */


    public Sector() {
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

    public void setAgentCode(BigDecimal AgentCode) {
        this.AgentCode = AgentCode;
    }

    public BigDecimal getAgentCode() {
        return AgentCode;
    }

    public void setAgentAccountCode(BigDecimal AgentAccountCode) {
        this.AgentAccountCode = AgentAccountCode;
    }

    public BigDecimal getAgentAccountCode() {
        return AgentAccountCode;
    }

    public void setAgentShtDesc(String agentShtDesc) {
        this.agentShtDesc = agentShtDesc;
    }

    public String getAgentShtDesc() {
        return agentShtDesc;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setOccupationCode(BigDecimal occupationCode) {
        this.occupationCode = occupationCode;
    }

    public BigDecimal getOccupationCode() {
        return occupationCode;
    }
}
