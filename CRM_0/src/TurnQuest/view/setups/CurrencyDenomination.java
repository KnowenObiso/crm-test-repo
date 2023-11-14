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

import java.util.Date;


/**
 * The CurrencyDenomination Bean. Includes properties and methods that map to a
 * given CurrencyDenomination.
 *
 * @author Frankline Ogongi
 */
public class CurrencyDenomination implements Serializable {

    /* Properties */
    private String code;
    private String currencyCode;
    private String value;
    private String name;
    private Date wef;

    /**
     * Default Constructor
     */
    public CurrencyDenomination() {
        super();
    }

    /* Methods */

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setWef(Date wef) {
        this.wef = wef;
    }

    public Date getWef() {
        return wef;
    }
}
