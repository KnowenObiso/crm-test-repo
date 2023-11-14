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
 * The Country Bean. Includes properties and methods that map to a
 * given Country.
 *
 * @author Frankline Ogongi
 */
public class State implements Serializable {

    private BigDecimal stateCode;
    private BigDecimal stateCountryCode;
    private String stateShortDesc;
    private String stateName;

    /**
     * Default Class Constructor
     */
    public State() {
        super();
    }

    public void setStateCode(BigDecimal stateCode) {
        this.stateCode = stateCode;
    }

    public BigDecimal getStateCode() {
        return stateCode;
    }

    public void setStateCountryCode(BigDecimal stateCountryCode) {
        this.stateCountryCode = stateCountryCode;
    }

    public BigDecimal getStateCountryCode() {
        return stateCountryCode;
    }

    public void setStateShortDesc(String stateShortDesc) {
        this.stateShortDesc = stateShortDesc;
    }

    public String getStateShortDesc() {
        return stateShortDesc;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateName() {
        return stateName;
    }
}
