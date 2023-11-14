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


public class HolidayDefinition implements SQLData, Serializable {

    private String thdDesc;
    private BigDecimal thdDay;
    private BigDecimal thdMonth;
    private String thdStatus;
    private BigDecimal thdCouCode;

    private String dayName;
    private String monthName;

    private String SQLTypeName;

    public HolidayDefinition() {
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
            if (SQLTypeName.equalsIgnoreCase("TQC_HOLIDAYS_DEFINITIONS_OBJ")) {
                stream.writeString(thdDesc);
                stream.writeBigDecimal(thdDay);
                stream.writeBigDecimal(thdMonth);
                stream.writeString(thdStatus);
                stream.writeBigDecimal(thdCouCode);
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

    public void setThdDesc(String thdDesc) {
        this.thdDesc = thdDesc;
    }

    public String getThdDesc() {
        return thdDesc;
    }

    public void setThdDay(BigDecimal thdDay) {
        this.thdDay = thdDay;
    }

    public BigDecimal getThdDay() {
        return thdDay;
    }

    public void setThdMonth(BigDecimal thdMonth) {
        this.thdMonth = thdMonth;
    }

    public BigDecimal getThdMonth() {
        return thdMonth;
    }

    public void setThdStatus(String thdStatus) {
        this.thdStatus = thdStatus;
    }

    public String getThdStatus() {
        return thdStatus;
    }

    public void setThdCouCode(BigDecimal thdCouCode) {
        this.thdCouCode = thdCouCode;
    }

    public BigDecimal getThdCouCode() {
        return thdCouCode;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDayName() {
        return dayName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getMonthName() {
        return monthName;
    }
}
