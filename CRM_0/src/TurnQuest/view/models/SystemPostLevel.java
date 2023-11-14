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


public class SystemPostLevel implements SQLData, Serializable {

    private BigDecimal sysplSysCode;
    private BigDecimal sysplCode;
    private String sysplShtDesc;
    private String sysplDesc;
    private BigDecimal sysplRanking;
    private Date sysplWef;

    private String SQLTypeName;

    public SystemPostLevel() {
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
            if (SQLTypeName.equalsIgnoreCase("TQC_SYS_POST_LEVELS_OBJ")) {
                stream.writeBigDecimal(sysplSysCode);
                stream.writeBigDecimal(sysplCode);
                stream.writeString(sysplShtDesc);
                stream.writeString(sysplDesc);
                stream.writeBigDecimal(sysplRanking);
                stream.writeDate(sysplWef == null ? null :
                                 new java.sql.Date(sysplWef.getTime()));
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

    public void setSysplSysCode(BigDecimal sysplSysCode) {
        this.sysplSysCode = sysplSysCode;
    }

    public BigDecimal getSysplSysCode() {
        return sysplSysCode;
    }

    public void setSysplCode(BigDecimal sysplCode) {
        this.sysplCode = sysplCode;
    }

    public BigDecimal getSysplCode() {
        return sysplCode;
    }

    public void setSysplShtDesc(String sysplShtDesc) {
        this.sysplShtDesc = sysplShtDesc;
    }

    public String getSysplShtDesc() {
        return sysplShtDesc;
    }

    public void setSysplDesc(String sysplDesc) {
        this.sysplDesc = sysplDesc;
    }

    public String getSysplDesc() {
        return sysplDesc;
    }

    public void setSysplRanking(BigDecimal sysplRanking) {
        this.sysplRanking = sysplRanking;
    }

    public BigDecimal getSysplRanking() {
        return sysplRanking;
    }

    public void setSysplWef(Date sysplWef) {
        this.sysplWef = sysplWef;
    }

    public Date getSysplWef() {
        return sysplWef;
    }
}
