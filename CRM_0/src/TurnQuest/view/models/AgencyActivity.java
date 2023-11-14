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


public class AgencyActivity implements SQLData, Serializable {

    private BigDecimal aacCode;
    private BigDecimal aacActyCode;
    private Date aacWef;
    private Date aacEstimateWet;
    private Date aacActualWet;
    private String aacRemarks;
    private BigDecimal aacAgnCode;
    private BigDecimal aacClientCode;
    private BigDecimal aacSprCode;
    private BigDecimal aacSysCode;
    private BigDecimal aacMktrAgnCode;
    private String aacReasnsforActivity;
    private String aacType;
    private String aacActivityByType; //code
    private String aacActivityTypeName; //name
    private BigDecimal aacActivityByCode;


    private String activityDesc;
    private String clientName;
    private String agencyName;
    private String marketerAgencyName;
    private String providerName;
    private String systemName;

    private String participActivityDesc;
    private BigDecimal participId;
    private String participActType;
    private String participCode;
    private BigDecimal participByCode;
    private String participName;


    private String SQLTypeName;

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
            if (SQLTypeName.equalsIgnoreCase("TQC_AGENCY_ACTIVITIES_OBJ")) {
                stream.writeBigDecimal(aacCode);
                stream.writeBigDecimal(aacActyCode);
                stream.writeDate(aacWef == null ? null :
                                 new java.sql.Date(aacWef.getTime()));
                stream.writeDate(aacEstimateWet == null ? null :
                                 new java.sql.Date(aacEstimateWet.getTime()));
                stream.writeDate(aacActualWet == null ? null :
                                 new java.sql.Date(aacActualWet.getTime()));
                stream.writeString(aacRemarks);
                stream.writeBigDecimal(aacAgnCode);
                stream.writeString(aacActivityByType);
                stream.writeString(aacReasnsforActivity);
                stream.writeBigDecimal(aacSysCode);
                // stream.writeBigDecimal( aacMktrAgnCode );
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

    public AgencyActivity() {
        super();
    }

    public void setAacCode(BigDecimal aacCode) {
        this.aacCode = aacCode;
    }

    public BigDecimal getAacCode() {
        return aacCode;
    }

    public void setAacActyCode(BigDecimal aacActyCode) {
        this.aacActyCode = aacActyCode;
    }

    public BigDecimal getAacActyCode() {
        return aacActyCode;
    }

    public void setAacWef(Date aacWef) {
        this.aacWef = aacWef;
    }

    public Date getAacWef() {
        return aacWef;
    }

    public void setAacEstimateWet(Date aacEstimateWet) {
        this.aacEstimateWet = aacEstimateWet;
    }

    public Date getAacEstimateWet() {
        return aacEstimateWet;
    }

    public void setAacActualWet(Date aacActualWet) {
        this.aacActualWet = aacActualWet;
    }

    public Date getAacActualWet() {
        return aacActualWet;
    }

    public void setAacRemarks(String aacRemarks) {
        this.aacRemarks = aacRemarks;
    }

    public String getAacRemarks() {
        return aacRemarks;
    }

    public void setAacAgnCode(BigDecimal aacAgnCode) {
        this.aacAgnCode = aacAgnCode;
    }

    public BigDecimal getAacAgnCode() {
        return aacAgnCode;
    }

    public void setAacClientCode(BigDecimal aacClientCode) {
        this.aacClientCode = aacClientCode;
    }

    public BigDecimal getAacClientCode() {
        return aacClientCode;
    }

    public void setAacSprCode(BigDecimal aacSprCode) {
        this.aacSprCode = aacSprCode;
    }

    public BigDecimal getAacSprCode() {
        return aacSprCode;
    }

    public void setAacSysCode(BigDecimal aacSysCode) {
        this.aacSysCode = aacSysCode;
    }

    public BigDecimal getAacSysCode() {
        return aacSysCode;
    }

    public void setAacMktrAgnCode(BigDecimal aacMktrAgnCode) {
        this.aacMktrAgnCode = aacMktrAgnCode;
    }

    public BigDecimal getAacMktrAgnCode() {
        return aacMktrAgnCode;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public String getActivityDesc() {
        return activityDesc;
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

    public void setMarketerAgencyName(String marketerAgencyName) {
        this.marketerAgencyName = marketerAgencyName;
    }

    public String getMarketerAgencyName() {
        return marketerAgencyName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setAacReasnsforActivity(String aacReasnsforActivity) {
        this.aacReasnsforActivity = aacReasnsforActivity;
    }

    public String getAacReasnsforActivity() {
        return aacReasnsforActivity;
    }

    public void setAacActivityByType(String aacActivityByType) {
        this.aacActivityByType = aacActivityByType;
    }

    public String getAacActivityByType() {
        return aacActivityByType;
    }

    public void setParticipActivityDesc(String participActivityDesc) {
        this.participActivityDesc = participActivityDesc;
    }

    public String getParticipActivityDesc() {
        return participActivityDesc;
    }

    public void setParticipId(BigDecimal participActivityCode) {
        this.participId = participActivityCode;
    }

    public BigDecimal getParticipId() {
        return participId;
    }

    public void setParticipActType(String participActType) {
        this.participActType = participActType;
    }

    public String getParticipActType() {
        return participActType;
    }

    public void setParticipCode(String participCode) {
        this.participCode = participCode;
    }

    public String getParticipCode() {
        return participCode;
    }

    public void setParticipName(String participName) {
        this.participName = participName;
    }

    public String getParticipName() {
        return participName;
    }

    public void setParticipByCode(BigDecimal participByCode) {
        this.participByCode = participByCode;
    }

    public BigDecimal getParticipByCode() {
        return participByCode;
    }

    public void setAacActivityTypeName(String aacActivityTypeName) {
        this.aacActivityTypeName = aacActivityTypeName;
    }

    public String getAacActivityTypeName() {
        return aacActivityTypeName;
    }

    public void setAacType(String aacType) {
        this.aacType = aacType;
    }

    public String getAacType() {
        return aacType;
    }

    public void setAacActivityByCode(BigDecimal aacActivityByCode) {
        this.aacActivityByCode = aacActivityByCode;
    }

    public BigDecimal getAacActivityByCode() {
        return aacActivityByCode;
    }
}
