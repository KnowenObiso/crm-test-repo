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


public class AgencySystem implements SQLData, Serializable {

    private BigDecimal asysSysCode;
    private BigDecimal asysAgnCode;
    private Date asysWef;
    private Date asysWet;
    private String asysComment;
    private String asysShtDesc;
    private String asysOsdCode;
    private BigDecimal asysOsdId;

    private BigDecimal sysCode;
    private String sysShtDesc;
    private String sysName;
    private String sysActive;

    private String subdivisionName;

    private String SQLTypeName;

    public AgencySystem() {
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
            if (SQLTypeName.equalsIgnoreCase("TQC_AGENCY_SYSTEMS_OBJ")) {
                stream.writeBigDecimal(asysSysCode);
                stream.writeBigDecimal(asysAgnCode);
                stream.writeDate(asysWef == null ? null :
                                 new java.sql.Date(asysWef.getTime()));
                stream.writeDate(asysWet == null ? null :
                                 new java.sql.Date(asysWet.getTime()));
                stream.writeString(asysComment);
                stream.writeString(asysOsdCode);
                stream.writeBigDecimal(asysOsdId);
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

    public void setAsysSysCode(BigDecimal asysSysCode) {
        this.asysSysCode = asysSysCode;
    }

    public BigDecimal getAsysSysCode() {
        return asysSysCode;
    }

    public void setAsysAgnCode(BigDecimal asysAgnCode) {
        this.asysAgnCode = asysAgnCode;
    }

    public BigDecimal getAsysAgnCode() {
        return asysAgnCode;
    }

    public void setAsysWef(Date asysWef) {
        this.asysWef = asysWef;
    }

    public Date getAsysWef() {
        return asysWef;
    }

    public void setAsysWet(Date asysWet) {
        this.asysWet = asysWet;
    }

    public Date getAsysWet() {
        return asysWet;
    }

    public void setAsysComment(String asysComment) {
        this.asysComment = asysComment;
    }

    public String getAsysComment() {
        return asysComment;
    }

    public void setAsysOsdCode(String asysOsdCode) {
        this.asysOsdCode = asysOsdCode;
    }

    public String getAsysOsdCode() {
        return asysOsdCode;
    }

    public void setSysCode(BigDecimal sysCode) {
        this.sysCode = sysCode;
    }

    public BigDecimal getSysCode() {
        return sysCode;
    }

    public void setSysShtDesc(String sysShtDesc) {
        this.sysShtDesc = sysShtDesc;
    }

    public String getSysShtDesc() {
        return sysShtDesc;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysActive(String sysActive) {
        this.sysActive = sysActive;
    }

    public String getSysActive() {
        return sysActive;
    }

    public void setSubdivisionName(String subdivisionName) {
        this.subdivisionName = subdivisionName;
    }

    public String getSubdivisionName() {
        return subdivisionName;
    }

    public void setAsysOsdId(BigDecimal asysOsdId) {
        this.asysOsdId = asysOsdId;
    }

    public BigDecimal getAsysOsdId() {
        return asysOsdId;
    }

    public void setAsysShtDesc(String asysShtDesc) {
        this.asysShtDesc = asysShtDesc;
    }

    public String getAsysShtDesc() {
        return asysShtDesc;
    }
}
