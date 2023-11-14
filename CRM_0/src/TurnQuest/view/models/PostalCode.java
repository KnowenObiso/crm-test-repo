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


public class PostalCode implements SQLData, Serializable {

    private BigDecimal pstCode;
    private BigDecimal pstTownCode;
    private String pstDesc;
    private String pstZipCode;

    private String SQLTypeName;

    public PostalCode() {
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
            if (SQLTypeName.equalsIgnoreCase("TQC_POSTAL_CODES_OBJ")) {
                stream.writeBigDecimal(pstCode);
                stream.writeBigDecimal(pstTownCode);
                stream.writeString(pstDesc);
                stream.writeString(pstZipCode);
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

    public void setPstCode(BigDecimal pstCode) {
        this.pstCode = pstCode;
    }

    public BigDecimal getPstCode() {
        return pstCode;
    }

    public void setPstTownCode(BigDecimal pstTownCode) {
        this.pstTownCode = pstTownCode;
    }

    public BigDecimal getPstTownCode() {
        return pstTownCode;
    }

    public void setPstDesc(String pstDesc) {
        this.pstDesc = pstDesc;
    }

    public String getPstDesc() {
        return pstDesc;
    }

    public void setPstZipCode(String pstZipCode) {
        this.pstZipCode = pstZipCode;
    }

    public String getPstZipCode() {
        return pstZipCode;
    }
}
