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
 * The ServiceProviderType Bean. Includes properties and methods that map to a
 * given ServiceProviderType.
 *
 * @author Frankline Ogongi
 */
public class ServiceProviderType implements Serializable {

    /* Providers */
    private String code;
    private BigDecimal sptCode;
    private String shortDesc;
    private String name;
    private String status;
    private String whtxRate;
    private String vatRate;
    private int suffix;
    private String smsmessage;
    private String emailmessage;
    private String smsDefault;
    private String emailDefault;

    private BigDecimal smsCode;
    private BigDecimal emailCode;
    private BigDecimal reportDays;


    /**
     * Default Constructor
     */
    public ServiceProviderType() {
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

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setWhtxRate(String whtxRate) {
        this.whtxRate = whtxRate;
    }

    public String getWhtxRate() {
        return whtxRate;
    }

    public void setVatRate(String vatRate) {
        this.vatRate = vatRate;
    }

    public String getVatRate() {
        return vatRate;
    }

    public void setSmsmessage(String smsmessage) {
        this.smsmessage = smsmessage;
    }

    public String getSmsmessage() {
        return smsmessage;
    }

    public void setEmailmessage(String emailmessage) {
        this.emailmessage = emailmessage;
    }

    public String getEmailmessage() {
        return emailmessage;
    }

    public void setSmsDefault(String smsDefault) {
        this.smsDefault = smsDefault;
    }

    public String getSmsDefault() {
        return smsDefault;
    }

    public void setEmailDefault(String emailDefault) {
        this.emailDefault = emailDefault;
    }

    public String getEmailDefault() {
        return emailDefault;
    }

    public void setSmsCode(BigDecimal smsCode) {
        this.smsCode = smsCode;
    }

    public BigDecimal getSmsCode() {
        return smsCode;
    }

    public void setEmailCode(BigDecimal emailCode) {
        this.emailCode = emailCode;
    }

    public BigDecimal getEmailCode() {
        return emailCode;
    }

    public void setReportDays(BigDecimal reportDays) {
        this.reportDays = reportDays;
    }

    public BigDecimal getReportDays() {
        return reportDays;
    }

    public void setSuffix(int suffix) {
        this.suffix = suffix;
    }

    public int getSuffix() {
        return suffix;
    }

    public void setSptCode(BigDecimal sptCode) {
        this.sptCode = sptCode;
    }

    public BigDecimal getSptCode() {
        return sptCode;
    }
}
