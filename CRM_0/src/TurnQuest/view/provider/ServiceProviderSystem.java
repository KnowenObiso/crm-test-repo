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

package TurnQuest.view.provider;

import java.util.Date;

/**
 * Bean for representing ServiceProviderSystem records. Includes properties
 * and methods that map to a given ServiceProviderSystem.
 *
 * @author Frankline Ogongi
 */
public class ServiceProviderSystem {

    /* Properties */
    private String code;
    private String serviceProviderCode;
    private String systemCode;
    private Date wef;
    private Date wet;
    private String createdBy;

    /**
     * Default Class Constructor
     */
    public ServiceProviderSystem() {
        super();
    }

    /* Methods */

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setServiceProviderCode(String serviceProviderCode) {
        this.serviceProviderCode = serviceProviderCode;
    }

    public String getServiceProviderCode() {
        return serviceProviderCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getSystemCode() {
        return systemCode;
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

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }
}
