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
 * The Location Bean. Includes properties and methods that map to a
 * given Location.
 *
 * @author Frankline Ogongi
 */
public class Location implements Serializable {

    /* Bean Properties */
    private String code;
    private String townCode;
    private String shortDesc;
    private String name;
    private String landmark;

    /**
     * Default Constructor
     */
    public Location() {
        super();
    }

    /* Bean Methods */

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setTownCode(String townCode) {
        this.townCode = townCode;
    }

    public String getTownCode() {
        return townCode;
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

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getLandmark() {
        return landmark;
    }
}
