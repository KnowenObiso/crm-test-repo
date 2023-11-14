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
 * The CurrencyRate Bean. Includes properties and methods that map to a
 * given CurrencyRate.
 *
 * @author Frankline Ogongi
 */
public class CurrencyRate implements Serializable {

    /* Properties */
    private String code;
    private String currencyCode;
    private String currRate;
    private String currDate;
    private String baseCurrencyCode;
    private String currencyDesc;

    private String baseCurrencyName;
    private String crtWefDate;
    private String crtWetDate;
    
    private String currDate1;
    private String crtWefDate1;
    private String crtWetDate1;
    private String crtCreatedDate;
    private String crtCreatedBy;
    private String crtUpdatedDate;
    private String crtUpdatedBy;
    private String crtAuthorized;

    /**
     * Default Constructor
     */
    public CurrencyRate() {
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

    public void setCurrRate(String currRate) {
        this.currRate = currRate;
    }

    public String getCurrRate() {
        return currRate;
    }

  

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public void setCurrencyDesc(String currencyDesc) {
        this.currencyDesc = currencyDesc;
    }

    public String getCurrencyDesc() {
        return currencyDesc;
    }

    public void setBaseCurrencyName(String baseCurrencyName) {
        this.baseCurrencyName = baseCurrencyName;
    }

    public String getBaseCurrencyName() {
        return baseCurrencyName;
    }

    

    public void setCurrDate1(String currDate1) {
        this.currDate1 = currDate1;
    }

    public String getCurrDate1() {
        return currDate1;
    }

    public void setCrtWefDate1(String crtWefDate1) {
        this.crtWefDate1 = crtWefDate1;
    }

    public String getCrtWefDate1() {
        return crtWefDate1;
    }

    public void setCrtWetDate1(String crtWetDate1) {
        this.crtWetDate1 = crtWetDate1;
    }

    public String getCrtWetDate1() {
        return crtWetDate1;
    }

    public void setCurrDate(String currDate) {
        this.currDate = currDate;
    }

    public String getCurrDate() {
        return currDate;
    }

    public void setCrtWefDate(String crtWefDate) {
        this.crtWefDate = crtWefDate;
    }

    public String getCrtWefDate() {
        return crtWefDate;
    }

    public void setCrtWetDate(String crtWetDate) {
        this.crtWetDate = crtWetDate;
    }

    public String getCrtWetDate() {
        return crtWetDate;
    }

    public void setCrtCreatedDate(String crtCreatedDate) {
        this.crtCreatedDate = crtCreatedDate;
    }

    public String getCrtCreatedDate() {
        return crtCreatedDate;
    }

    public void setCrtCreatedBy(String crtCreatedBy) {
        this.crtCreatedBy = crtCreatedBy;
    }

    public String getCrtCreatedBy() {
        return crtCreatedBy;
    }

    public void setCrtUpdatedDate(String crtUpdatedDate) {
        this.crtUpdatedDate = crtUpdatedDate;
    }

    public String getCrtUpdatedDate() {
        return crtUpdatedDate;
    }

    public void setCrtUpdatedBy(String crtUpdatedBy) {
        this.crtUpdatedBy = crtUpdatedBy;
    }

    public String getCrtUpdatedBy() {
        return crtUpdatedBy;
    }

    public void setCrtAuthorized(String crtAuthorized) {
        this.crtAuthorized = crtAuthorized;
    }

    public String getCrtAuthorized() {
        return crtAuthorized;
    }
}
