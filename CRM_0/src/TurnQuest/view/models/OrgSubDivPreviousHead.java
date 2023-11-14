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


public class OrgSubDivPreviousHead implements SQLData, Serializable {

    private BigDecimal osdphCode;
    private String osdphOsdCode;
    private BigDecimal osdphOsdId;
    private BigDecimal osdphPrevAgnCode;
    private Date osdphWet;

    private String osdName;
    private String agencyName;

    private String SQLTypeName;

    public OrgSubDivPreviousHead() {
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
            if (SQLTypeName.equalsIgnoreCase("TQC_ORG_SUBDIV_PREV_HEADS_OBJ")) {
                stream.writeBigDecimal(osdphCode);
                stream.writeString(osdphOsdCode);
                stream.writeBigDecimal(osdphPrevAgnCode);
                stream.writeDate(osdphWet == null ? null :
                                 new java.sql.Date(osdphWet.getTime()));
                stream.writeBigDecimal(osdphOsdId);
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

    public void setOsdphCode(BigDecimal osdphCode) {
        this.osdphCode = osdphCode;
    }

    public BigDecimal getOsdphCode() {
        return osdphCode;
    }

    public void setOsdphOsdCode(String osdphOsdCode) {
        this.osdphOsdCode = osdphOsdCode;
    }

    public String getOsdphOsdCode() {
        return osdphOsdCode;
    }

    public void setOsdphPrevAgnCode(BigDecimal osdphPrevAgnCode) {
        this.osdphPrevAgnCode = osdphPrevAgnCode;
    }

    public BigDecimal getOsdphPrevAgnCode() {
        return osdphPrevAgnCode;
    }

    public void setOsdphWet(Date osdphWet) {
        this.osdphWet = osdphWet;
    }

    public Date getOsdphWet() {
        return osdphWet;
    }

    public void setOsdName(String osdName) {
        this.osdName = osdName;
    }

    public String getOsdName() {
        return osdName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setOsdphOsdId(BigDecimal osdphOsdId) {
        this.osdphOsdId = osdphOsdId;
    }

    public BigDecimal getOsdphOsdId() {
        return osdphOsdId;
    }
}
