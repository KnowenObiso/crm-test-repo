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


public class MessageTemplate implements Serializable, SQLData {

    private BigDecimal msgtCode;
    private String msgtShtDesc;
    private String msgtMsg;
    private BigDecimal msgtSysCode;
    private String msgtSysModule;
    private BigDecimal msgtSysProd;
    private String msgtSysProdName;
    private String msgtType;
    private String msgtCreatedBy;
    private String msgtUpdatedBy;
    private String msgtSubject;


    private String SQLTypeName;

    public MessageTemplate() {
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
            if (SQLTypeName.equalsIgnoreCase("TQC_MSG_TEMPLATES_OBJ")) {
                stream.writeBigDecimal(msgtCode);
                stream.writeString(msgtShtDesc);
                stream.writeString(msgtMsg);
                stream.writeBigDecimal(msgtSysCode);
                stream.writeString(msgtSysModule);
                stream.writeString(msgtType);
                stream.writeString(msgtCreatedBy);
                stream.writeString(msgtUpdatedBy); 
                stream.writeBigDecimal(msgtSysProd);
                stream.writeString(msgtSubject);
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

    public void setMsgtCode(BigDecimal msgtCode) {
        this.msgtCode = msgtCode;
    }

    public BigDecimal getMsgtCode() {
        return msgtCode;
    }

    public void setMsgtShtDesc(String msgtShtDesc) {
        this.msgtShtDesc = msgtShtDesc;
    }

    public String getMsgtShtDesc() {
        return msgtShtDesc;
    }

    public void setMsgtMsg(String msgtMsg) {
        this.msgtMsg = msgtMsg;
    }

    public String getMsgtMsg() {
        return msgtMsg;
    }

    public void setMsgtSysCode(BigDecimal msgtSysCode) {
        this.msgtSysCode = msgtSysCode;
    }

    public BigDecimal getMsgtSysCode() {
        return msgtSysCode;
    }

    public void setMsgtSysModule(String msgtSysModule) {
        this.msgtSysModule = msgtSysModule;
    }

    public String getMsgtSysModule() {
        return msgtSysModule;
    }

    public void setMsgtType(String msgtType) {
        this.msgtType = msgtType;
    }

    public String getMsgtType() {
        return msgtType;
    }

    public void setMsgtCreatedBy(String msgtCreatedBy) {
        this.msgtCreatedBy = msgtCreatedBy;
    }

    public String getMsgtCreatedBy() {
        return msgtCreatedBy;
    }

    public void setMsgtUpdatedBy(String msgtUpdatedBy) {
        this.msgtUpdatedBy = msgtUpdatedBy;
    }

    public String getMsgtUpdatedBy() {
        return msgtUpdatedBy;
    }

    public void setMsgtSysProd(BigDecimal msgtSysProd) {
        this.msgtSysProd = msgtSysProd;
    }

    public BigDecimal getMsgtSysProd() {
        return msgtSysProd;
    }

    public void setMsgtSysProdName(String msgtSysProdName) {
        this.msgtSysProdName = msgtSysProdName;
    }

    public String getMsgtSysProdName() {
        return msgtSysProdName;
    }

    public void setMsgtSubject(String msgtSubject) {
        this.msgtSubject = msgtSubject;
    }

    public String getMsgtSubject() {
        return msgtSubject;
    }
}
