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

/**
 * The AgencyHoldingCompany Bean. Includes properties and methods that map to a
 * given AgencyHoldingCompany.
 *
 * @author Frankline Ogongi
 */
public class AgencyHoldingCompany implements Serializable {

    /* Properties */
    private String code;
    private String name;
    private String postAdd;
    private String phyAdd;
    private String telNumber;
    private String mobNumber;
    private String contactPerson;

    /**
     * Default Constructor
     */
    public AgencyHoldingCompany() {
        super();
    }

    /* Methods */

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPostAdd(String postAdd) {
        this.postAdd = postAdd;
    }

    public String getPostAdd() {
        return postAdd;
    }

    public void setPhyAdd(String phyAdd) {
        this.phyAdd = phyAdd;
    }

    public String getPhyAdd() {
        return phyAdd;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setMobNumber(String mobNumber) {
        this.mobNumber = mobNumber;
    }

    public String getMobNumber() {
        return mobNumber;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPerson() {
        return contactPerson;
    }
}
