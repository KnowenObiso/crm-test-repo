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

import java.math.BigDecimal;

/**
 * The Country Bean. Includes properties and methods that map to a
 * given Country.
 *
 * @author Frankline Ogongi
 */
public class Country extends Nation {

    /* Properties */
    private BigDecimal couCode;
    private String couShortDesc;
    private String couName;
    private BigDecimal couBaseCurr;
    private String couNationality;
    private BigDecimal couZipCode;
    private String currencySymbol;
    private String currencyDesc;
    private String administrativeType;
    private String adminTypeMandatory;
    private String couShengen;
    private String couEmbCode;
    private String couCurrSerial;


    /**
     * Default Constructor
     */
    public Country() {
        super();
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencyDesc(String currencyDesc) {
        this.currencyDesc = currencyDesc;
    }

    public String getCurrencyDesc() {
        return currencyDesc;
    }

    public void setCouCode(BigDecimal couCode) {
        this.couCode = couCode;
    }

    public BigDecimal getCouCode() {
        return couCode;
    }

    public void setCouShortDesc(String couShortDesc) {
        this.couShortDesc = couShortDesc;
    }

    public String getCouShortDesc() {
        return couShortDesc;
    }

    public void setCouName(String couName) {
        this.couName = couName;
    }

    public String getCouName() {
        return couName;
    }

    public void setCouBaseCurr(BigDecimal couBaseCurr) {
        this.couBaseCurr = couBaseCurr;
    }

    public BigDecimal getCouBaseCurr() {
        return couBaseCurr;
    }

    public void setCouNationality(String couNationality) {
        this.couNationality = couNationality;
    }

    public String getCouNationality() {
        return couNationality;
    }

    public void setCouZipCode(BigDecimal couZipCode) {
        this.couZipCode = couZipCode;
    }

    public BigDecimal getCouZipCode() {
        return couZipCode;
    }

    public void setAdministrativeType(String administrativeType) {
        this.administrativeType = administrativeType;
    }

    public String getAdministrativeType() {
        return administrativeType;
    }

    public void setAdminTypeMandatory(String adminTypeMandatory) {
        this.adminTypeMandatory = adminTypeMandatory;
    }

    public String getAdminTypeMandatory() {
        return adminTypeMandatory;
    }

    public void setCouShengen(String couShengen) {
        this.couShengen = couShengen;
    }

    public String getCouShengen() {
        return couShengen;
    }

    public void setCouEmbCode(String couEmbCode) {
        this.couEmbCode = couEmbCode;
    }

    public String getCouEmbCode() {
        return couEmbCode;
    }

    public void setCouCurrSerial(String couCurrSerial) {
        this.couCurrSerial = couCurrSerial;
    }

    public String getCouCurrSerial() {
        return couCurrSerial;
    }
}
