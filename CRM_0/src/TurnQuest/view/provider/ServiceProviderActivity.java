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

/**
 * Bean for representing Service Provider Activity records. Includes properties
 * and methods that map to a given ServiceProviderActivity.
 *
 * @author Frankline Ogongi
 */
public class ServiceProviderActivity {

    /* Properties */
    private String code;
    private String providerTypeCode;
    private String providerTypeShortDesc;
    private String serviceProviderCode;
    private String serviceProviderShortDesc;
    private String mainAct;
    private String description;

    private String providerTypeName;
    private String providerName;

    /**
     * Default Class Constructor
     */
    public ServiceProviderActivity() {
        super();
    }

    /* Methods */

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setProviderTypeCode(String providerTypeCode) {
        this.providerTypeCode = providerTypeCode;
    }

    public String getProviderTypeCode() {
        return providerTypeCode;
    }

    public void setProviderTypeShortDesc(String providerTypeShortDesc) {
        this.providerTypeShortDesc = providerTypeShortDesc;
    }

    public String getProviderTypeShortDesc() {
        return providerTypeShortDesc;
    }

    public void setServiceProviderCode(String serviceProviderCode) {
        this.serviceProviderCode = serviceProviderCode;
    }

    public String getServiceProviderCode() {
        return serviceProviderCode;
    }

    public void setServiceProviderShortDesc(String serviceProviderShortDesc) {
        this.serviceProviderShortDesc = serviceProviderShortDesc;
    }

    public String getServiceProviderShortDesc() {
        return serviceProviderShortDesc;
    }

    public void setMainAct(String mainAct) {
        this.mainAct = mainAct;
    }

    public String getMainAct() {
        return mainAct;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setProviderTypeName(String providerTypeName) {
        this.providerTypeName = providerTypeName;
    }

    public String getProviderTypeName() {
        return providerTypeName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderName() {
        return providerName;
    }
}
