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


public class SysProcessSubAreaLimit implements SQLData, Serializable {

    private BigDecimal spsatCode;
    private BigDecimal spsatSprsaCode;
    private BigDecimal spsatNoOfLevel;
    private BigDecimal spsatMinLimit;
    private BigDecimal spsatMaxLimit;

    private String SQLTypeName;

    public SysProcessSubAreaLimit() {
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
            if (SQLTypeName.equalsIgnoreCase("TQC_SYS_PRCSS_SUBAREA_LMTS_OBJ")) {
                stream.writeBigDecimal(spsatCode);
                stream.writeBigDecimal(spsatSprsaCode);
                stream.writeBigDecimal(spsatNoOfLevel);
                stream.writeBigDecimal(spsatMinLimit);
                stream.writeBigDecimal(spsatMaxLimit);
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

    public void setSpsatCode(BigDecimal spsatCode) {
        this.spsatCode = spsatCode;
    }

    public BigDecimal getSpsatCode() {
        return spsatCode;
    }

    public void setSpsatSprsaCode(BigDecimal spsatSprsaCode) {
        this.spsatSprsaCode = spsatSprsaCode;
    }

    public BigDecimal getSpsatSprsaCode() {
        return spsatSprsaCode;
    }

    public void setSpsatNoOfLevel(BigDecimal spsatNoOfLevel) {
        this.spsatNoOfLevel = spsatNoOfLevel;
    }

    public BigDecimal getSpsatNoOfLevel() {
        return spsatNoOfLevel;
    }

    public void setSpsatMinLimit(BigDecimal spsatMinLimit) {
        this.spsatMinLimit = spsatMinLimit;
    }

    public BigDecimal getSpsatMinLimit() {
        return spsatMinLimit;
    }

    public void setSpsatMaxLimit(BigDecimal spsatMaxLimit) {
        this.spsatMaxLimit = spsatMaxLimit;
    }

    public BigDecimal getSpsatMaxLimit() {
        return spsatMaxLimit;
    }
}
