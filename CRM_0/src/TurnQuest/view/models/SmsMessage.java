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

package TurnQuest.view.models;


import TurnQuest.view.Base.GlobalCC;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.SQLData;
import java.sql.SQLInput;
import java.sql.SQLOutput;

import java.util.Date;


public class SmsMessage implements Serializable, SQLData {

    private BigDecimal smsCode;
    private BigDecimal smsSysCode;
    private String smsSysModule;
    private BigDecimal smsClientCode;
    private BigDecimal smsAgnCode;
    private BigDecimal smsPolCode;
    private String smsPolNo;
    private String smsClmNo;
    private String smsTelNo;
    private String smsMsg;
    private String smsStatus;
    private String smsPreparedBy;
    private Date smsPreparedDate;
    private Date smsSendDate;
    private BigDecimal smsQuotCode;
    private String smsQuotNo;
    private BigDecimal smsUsrCode;

    private String systemName;
    private String clientName;
    private String agencyName;

    private String SQLTypeName;

    public SmsMessage() {
        super();
    }

    /**
     * Populates this object with data read from the database.
     *
     * @param stream The SQLInput object from which to read the data for the
     * value that is being custom mapped
     * @param typeName the SQL type name of the value on the data stream
     */
    public void readSQL(SQLInput stream, String typeName) {
    }

    /**
     * Writes this object to the given SQL data stream, converting it back to
     * its SQL value in the data source.
     *
     * @param stream the SQLOutput object to which to write the data for the
     * value that was custom mapped
     */
    public void writeSQL(SQLOutput stream) {
        try {
            if (SQLTypeName.equalsIgnoreCase("TQC_SMS_MESSAGES_OBJ")) {
                stream.writeBigDecimal(smsCode);
                stream.writeBigDecimal(smsSysCode);
                stream.writeString(smsSysModule);
                stream.writeBigDecimal(smsClientCode);
                stream.writeBigDecimal(smsAgnCode);
                stream.writeBigDecimal(smsPolCode);
                stream.writeString(smsPolNo);
                stream.writeString(smsClmNo);
                stream.writeString(smsTelNo);
                stream.writeString(smsMsg);
                stream.writeString(smsStatus);
                stream.writeString(smsPreparedBy);
                stream.writeDate(smsPreparedDate == null ? null :
                                 new java.sql.Date(smsPreparedDate.getTime()));
                stream.writeDate(smsSendDate == null ? null :
                                 new java.sql.Date(smsSendDate.getTime()));
                stream.writeBigDecimal(smsQuotCode);
                stream.writeString(smsQuotNo);
                stream.writeBigDecimal(smsUsrCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
    }

    public void setSQLTypeName(String SQLTypeName) {
        this.SQLTypeName = SQLTypeName;
    }

    public String getSQLTypeName() {
        return SQLTypeName;
    }

    public void setSmsCode(BigDecimal smsCode) {
        this.smsCode = smsCode;
    }

    public BigDecimal getSmsCode() {
        return smsCode;
    }

    public void setSmsSysCode(BigDecimal smsSysCode) {
        this.smsSysCode = smsSysCode;
    }

    public BigDecimal getSmsSysCode() {
        return smsSysCode;
    }

    public void setSmsSysModule(String smsSysModule) {
        this.smsSysModule = smsSysModule;
    }

    public String getSmsSysModule() {
        return smsSysModule;
    }

    public void setSmsClientCode(BigDecimal smsClientCode) {
        this.smsClientCode = smsClientCode;
    }

    public BigDecimal getSmsClientCode() {
        return smsClientCode;
    }

    public void setSmsAgnCode(BigDecimal smsAgnCode) {
        this.smsAgnCode = smsAgnCode;
    }

    public BigDecimal getSmsAgnCode() {
        return smsAgnCode;
    }

    public void setSmsPolCode(BigDecimal smsPolCode) {
        this.smsPolCode = smsPolCode;
    }

    public BigDecimal getSmsPolCode() {
        return smsPolCode;
    }

    public void setSmsPolNo(String smsPolNo) {
        this.smsPolNo = smsPolNo;
    }

    public String getSmsPolNo() {
        return smsPolNo;
    }

    public void setSmsClmNo(String smsClmNo) {
        this.smsClmNo = smsClmNo;
    }

    public String getSmsClmNo() {
        return smsClmNo;
    }

    public void setSmsTelNo(String smsTelNo) {
        this.smsTelNo = smsTelNo;
    }

    public String getSmsTelNo() {
        return smsTelNo;
    }

    public void setSmsMsg(String smsMsg) {
        this.smsMsg = smsMsg;
    }

    public String getSmsMsg() {
        return smsMsg;
    }

    public void setSmsStatus(String smsStatus) {
        this.smsStatus = smsStatus;
    }

    public String getSmsStatus() {
        return smsStatus;
    }

    public void setSmsPreparedBy(String smsPreparedBy) {
        this.smsPreparedBy = smsPreparedBy;
    }

    public String getSmsPreparedBy() {
        return smsPreparedBy;
    }

    public void setSmsPreparedDate(Date smsPreparedDate) {
        this.smsPreparedDate = smsPreparedDate;
    }

    public Date getSmsPreparedDate() {
        return smsPreparedDate;
    }

    public void setSmsSendDate(Date smsSendDate) {
        this.smsSendDate = smsSendDate;
    }

    public Date getSmsSendDate() {
        return smsSendDate;
    }

    public void setSmsQuotCode(BigDecimal smsQuotCode) {
        this.smsQuotCode = smsQuotCode;
    }

    public BigDecimal getSmsQuotCode() {
        return smsQuotCode;
    }

    public void setSmsQuotNo(String smsQuotNo) {
        this.smsQuotNo = smsQuotNo;
    }

    public String getSmsQuotNo() {
        return smsQuotNo;
    }

    public void setSmsUsrCode(BigDecimal smsUsrCode) {
        this.smsUsrCode = smsUsrCode;
    }

    public BigDecimal getSmsUsrCode() {
        return smsUsrCode;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getAgencyName() {
        return agencyName;
    }
}
