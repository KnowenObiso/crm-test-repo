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


public class UserSystem implements SQLData, Serializable {

    private BigDecimal usysCode;
    private BigDecimal usysUsrCode;
    private BigDecimal usysSysCode;
    private Date usysWef;
    private Date usysWet;
    private BigDecimal usysSpostCode;

    private BigDecimal sysCode;
    private String sysShtDesc;
    private String sysName;
    private String sysActive;

    private String spostDesc;

    private BigDecimal clcoCode;
    private BigDecimal clntCode;
    private String clcoName;
    private String clcoPostAddress;
    private String clcoPhysAddress;
    private BigDecimal clcoSecCode;

    private String SQLTypeName;

    public UserSystem() {
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
            if (SQLTypeName.equalsIgnoreCase("TQC_USER_SYSTEMS_OBJ")) {
                stream.writeBigDecimal(usysCode);
                stream.writeBigDecimal(usysUsrCode);
                stream.writeBigDecimal(usysSysCode);
                stream.writeDate(usysWef == null ? null :
                                 new java.sql.Date(usysWef.getTime()));
                stream.writeDate(usysWet == null ? null :
                                 new java.sql.Date(usysWet.getTime()));
                stream.writeBigDecimal(usysSpostCode);
            } else if (SQLTypeName.equalsIgnoreCase("TQC_CLIENT_CONTACTS_OBJ")) {
                stream.writeBigDecimal(clcoCode);
                stream.writeBigDecimal(clntCode);
                stream.writeString(clcoName);
                stream.writeString(clcoPostAddress);
                stream.writeString(clcoPhysAddress);
                stream.writeBigDecimal(clcoSecCode);
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

    public void setUsysCode(BigDecimal usysCode) {
        this.usysCode = usysCode;
    }

    public BigDecimal getUsysCode() {
        return usysCode;
    }

    public void setUsysUsrCode(BigDecimal usysUsrCode) {
        this.usysUsrCode = usysUsrCode;
    }

    public BigDecimal getUsysUsrCode() {
        return usysUsrCode;
    }

    public void setUsysSysCode(BigDecimal usysSysCode) {
        this.usysSysCode = usysSysCode;
    }

    public BigDecimal getUsysSysCode() {
        return usysSysCode;
    }

    public void setUsysWef(Date usysWef) {
        this.usysWef = usysWef;
    }

    public Date getUsysWef() {
        return usysWef;
    }

    public void setUsysWet(Date usysWet) {
        this.usysWet = usysWet;
    }

    public Date getUsysWet() {
        return usysWet;
    }

    public void setUsysSpostCode(BigDecimal usysSpostCode) {
        this.usysSpostCode = usysSpostCode;
    }

    public BigDecimal getUsysSpostCode() {
        return usysSpostCode;
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

    public void setSpostDesc(String spostDesc) {
        this.spostDesc = spostDesc;
    }

    public String getSpostDesc() {
        return spostDesc;
    }

    public void setClcoCode(BigDecimal clcoCode) {
        this.clcoCode = clcoCode;
    }

    public BigDecimal getClcoCode() {
        return clcoCode;
    }

    public void setClntCode(BigDecimal clntCode) {
        this.clntCode = clntCode;
    }

    public BigDecimal getClntCode() {
        return clntCode;
    }

    public void setClcoName(String clcoName) {
        this.clcoName = clcoName;
    }

    public String getClcoName() {
        return clcoName;
    }

    public void setClcoPostAddress(String clcoPostAddress) {
        this.clcoPostAddress = clcoPostAddress;
    }

    public String getClcoPostAddress() {
        return clcoPostAddress;
    }

    public void setClcoPhysAddress(String clcoPhysAddress) {
        this.clcoPhysAddress = clcoPhysAddress;
    }

    public String getClcoPhysAddress() {
        return clcoPhysAddress;
    }

    public void setClcoSecCode(BigDecimal clcoSecCode) {
        this.clcoSecCode = clcoSecCode;
    }

    public BigDecimal getClcoSecCode() {
        return clcoSecCode;
    }
}
