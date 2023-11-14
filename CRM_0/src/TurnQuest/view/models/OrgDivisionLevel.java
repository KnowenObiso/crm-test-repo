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


public class OrgDivisionLevel implements SQLData, Serializable {

    private String odlcode;
    private String odlDltCode;
    private String odlDesc;
    private BigDecimal odlRanking;
    private String odlType;

    private String SQLTypeName;

    public OrgDivisionLevel() {
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
            if (SQLTypeName.equalsIgnoreCase("TQC_ORG_DIVISION_LEVELS_OBJ")) {
                stream.writeString(odlcode);
                stream.writeString(odlDltCode);
                stream.writeString(odlDesc);
                stream.writeBigDecimal(odlRanking);
                stream.writeString(odlType);
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

    public void setOdlcode(String odlcode) {
        this.odlcode = odlcode;
    }

    public String getOdlcode() {
        return odlcode;
    }

    public void setOdlDltCode(String odlDltCode) {
        this.odlDltCode = odlDltCode;
    }

    public String getOdlDltCode() {
        return odlDltCode;
    }

    public void setOdlDesc(String odlDesc) {
        this.odlDesc = odlDesc;
    }

    public String getOdlDesc() {
        return odlDesc;
    }

    public void setOdlRanking(BigDecimal odlRanking) {
        this.odlRanking = odlRanking;
    }

    public BigDecimal getOdlRanking() {
        return odlRanking;
    }

    public void setOdlType(String odlType) {
        this.odlType = odlType;
    }

    public String getOdlType() {
        return odlType;
    }
}
