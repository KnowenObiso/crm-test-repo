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


public class OrgDivisionLevelType implements SQLData, Serializable {

    private String dltCode;
    private BigDecimal dltSysCode;
    private String dltDesc;
    private BigDecimal dltActCode;
    private BigDecimal dltHeadActCode;
    private String dltType;
    private BigDecimal dltIntCode;
    private String dltPayIntermediary;

    private String accountTypeName;
    private String headAccountTypeName;
    private String agencyIntermediary;
    
    private BigDecimal divLocationCode;
    private String divLocationType;
    private String divLocationName;
    

    private String SQLTypeName;
    private BigDecimal code;

    public OrgDivisionLevelType() {
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
            if (SQLTypeName.equalsIgnoreCase("TQC_ORG_DIV_LEVELS_TYPE_OBJ")) {
                stream.writeString(dltCode);
                stream.writeBigDecimal(dltSysCode);
                stream.writeString(dltDesc);
                stream.writeBigDecimal(dltActCode);
                stream.writeBigDecimal(dltHeadActCode);
                stream.writeString(dltType);
                stream.writeBigDecimal(dltIntCode);
                stream.writeString(dltPayIntermediary);
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

    public void setDltCode(String dltCode) {
        this.dltCode = dltCode;
    }

    public String getDltCode() {
        return dltCode;
    }

    public void setDltSysCode(BigDecimal dltSysCode) {
        this.dltSysCode = dltSysCode;
    }

    public BigDecimal getDltSysCode() {
        return dltSysCode;
    }

    public void setDltDesc(String dltDesc) {
        this.dltDesc = dltDesc;
    }

    public String getDltDesc() {
        return dltDesc;
    }

    public void setDltActCode(BigDecimal dltActCode) {
        this.dltActCode = dltActCode;
    }

    public BigDecimal getDltActCode() {
        return dltActCode;
    }

    public void setAccountTypeName(String accountTypeName) {
        this.accountTypeName = accountTypeName;
    }

    public String getAccountTypeName() {
        return accountTypeName;
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public BigDecimal getCode() {
        return code;
    }

    public void setDltType(String dltType) {
        this.dltType = dltType;
    }

    public String getDltType() {
        return dltType;
    }

    public void setDltHeadActCode(BigDecimal dltHeadActCode) {
        this.dltHeadActCode = dltHeadActCode;
    }

    public BigDecimal getDltHeadActCode() {
        return dltHeadActCode;
    }

    public void setHeadAccountTypeName(String headAccountTypeName) {
        this.headAccountTypeName = headAccountTypeName;
    }

    public String getHeadAccountTypeName() {
        return headAccountTypeName;
    }

    public void setDltIntCode(BigDecimal dltIntCode) {
        this.dltIntCode = dltIntCode;
    }

    public BigDecimal getDltIntCode() {
        return dltIntCode;
    }

    public void setDltPayIntermediary(String dltPayIntermediary) {
        this.dltPayIntermediary = dltPayIntermediary;
    }

    public String getDltPayIntermediary() {
        return dltPayIntermediary;
    }

    public void setAgencyIntermediary(String agencyIntermediary) {
        this.agencyIntermediary = agencyIntermediary;
    }

    public String getAgencyIntermediary() {
        return agencyIntermediary;
    }

    public void setDivLocationCode(BigDecimal divLocationCode) {
        this.divLocationCode = divLocationCode;
    }

    public BigDecimal getDivLocationCode() {
        return divLocationCode;
    }

    public void setDivLocationType(String divLocationType) {
        this.divLocationType = divLocationType;
    }

    public String getDivLocationType() {
        return divLocationType;
    }

    public void setDivLocationName(String divLocationName) {
        this.divLocationName = divLocationName;
    }

    public String getDivLocationName() {
        return divLocationName;
    }
}
