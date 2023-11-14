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


public class SysProcessSubAreaLevel implements SQLData, Serializable {

    private BigDecimal spsalCode;
    private BigDecimal spsalSprsaCode;
    private BigDecimal spsalSpsatCode;
    private BigDecimal spsalLevel;
    private String spsalApproverType;
    private BigDecimal spsalApproverId;
    private String userName;

    private String SQLTypeName;

    public SysProcessSubAreaLevel() {
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
            if (SQLTypeName.equalsIgnoreCase("TQC_SYS_PRCSS_SUBAREA_LVLS_OBJ")) {
                stream.writeBigDecimal(spsalCode);
                stream.writeBigDecimal(spsalSprsaCode);
                stream.writeBigDecimal(spsalSpsatCode);
                stream.writeBigDecimal(spsalLevel);
                stream.writeString(spsalApproverType);
                stream.writeBigDecimal(spsalApproverId);
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

    public void setSpsalCode(BigDecimal spsalCode) {
        this.spsalCode = spsalCode;
    }

    public BigDecimal getSpsalCode() {
        return spsalCode;
    }

    public void setSpsalSprsaCode(BigDecimal spsalSprsaCode) {
        this.spsalSprsaCode = spsalSprsaCode;
    }

    public BigDecimal getSpsalSprsaCode() {
        return spsalSprsaCode;
    }

    public void setSpsalSpsatCode(BigDecimal spsalSpsatCode) {
        this.spsalSpsatCode = spsalSpsatCode;
    }

    public BigDecimal getSpsalSpsatCode() {
        return spsalSpsatCode;
    }

    public void setSpsalLevel(BigDecimal spsalLevel) {
        this.spsalLevel = spsalLevel;
    }

    public BigDecimal getSpsalLevel() {
        return spsalLevel;
    }

    public void setSpsalApproverType(String spsalApproverType) {
        this.spsalApproverType = spsalApproverType;
    }

    public String getSpsalApproverType() {
        return spsalApproverType;
    }

    public void setSpsalApproverId(BigDecimal spsalApproverId) {
        this.spsalApproverId = spsalApproverId;
    }

    public BigDecimal getSpsalApproverId() {
        return spsalApproverId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
