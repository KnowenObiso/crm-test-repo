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
import java.util.List;


public class OrgSubDivision implements SQLData, Serializable {

    private String osdCode;
    private String osdParentOsdCode;
    private BigDecimal osdId;
    private BigDecimal osdParentOsdId;
    private String osdDltCode;
    private String osdOdlCode;
    private String osdName;
    private String osdStatus;
    private Date osdWef;
    private Date osdWet;
    private BigDecimal osdDivHeadAgnCode;
    private BigDecimal osdSysCode;
    private BigDecimal odlRanking;
    private String dltDesc;
    private String odlDesc;
    private String odlType;
    private String agentName;
    private BigDecimal brnCode;
    private BigDecimal regCode;
    private String osdPostLevel;
    private String osdManagerAllowed;
    private String osdOverCommAllowed;
    private BigDecimal osdLocationCode;
    private String osdLocation;
    
    private BigDecimal odh_code;
    private String odh_osd_code;
    private BigDecimal odh_osd_div_head_agn_code;
    private Date odh_wef_date;
    private Date odh_wet_date;
    private String odh_agn_name;
    private String odh_agn_sht_desc;

    private List<OrgSubDivision> orgSubDivisions;

    private String nodeType;

    private String SQLTypeName;

    public OrgSubDivision() {
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
            if (SQLTypeName.equalsIgnoreCase("TQC_ORG_SUBDIVISIONS_OBJ")) {
                stream.writeString(osdCode);
                stream.writeString(osdParentOsdCode);
                stream.writeString(osdDltCode);
                stream.writeString(osdOdlCode);
                stream.writeString(osdName);
                stream.writeDate(osdWef == null ? null : new java.sql.Date(osdWef.getTime()));      
                stream.writeBigDecimal(osdDivHeadAgnCode);
                stream.writeBigDecimal(osdSysCode);
                stream.writeBigDecimal(brnCode);
                stream.writeBigDecimal(regCode);
                stream.writeString(osdPostLevel);
                stream.writeString(osdManagerAllowed);
                stream.writeString(osdOverCommAllowed);
                stream.writeBigDecimal(osdId);
                stream.writeBigDecimal(osdParentOsdId);
                stream.writeDate(osdWet == null ? null : new java.sql.Date(osdWet.getTime()));      
                stream.writeString(osdStatus);
                stream.writeBigDecimal(osdLocationCode);
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

    public void setOsdCode(String osdCode) {
        this.osdCode = osdCode;
    }

    public String getOsdCode() {
        return osdCode;
    }

    public void setOsdParentOsdCode(String osdParentOsdCode) {
        this.osdParentOsdCode = osdParentOsdCode;
    }

    public String getOsdParentOsdCode() {
        return osdParentOsdCode;
    }

    public void setOsdDltCode(String osdDltCode) {
        this.osdDltCode = osdDltCode;
    }

    public String getOsdDltCode() {
        return osdDltCode;
    }

    public void setOsdOdlCode(String osdOdlCode) {
        this.osdOdlCode = osdOdlCode;
    }

    public String getOsdOdlCode() {
        return osdOdlCode;
    }

    public void setOsdName(String osdName) {
        this.osdName = osdName;
    }

    public String getOsdName() {
        return osdName;
    }

    public void setOsdWef(Date osdWef) {
        this.osdWef = osdWef;
    }

    public Date getOsdWef() {
        return osdWef;
    }

    public void setOsdDivHeadAgnCode(BigDecimal osdDivHeadAgnCode) {
        this.osdDivHeadAgnCode = osdDivHeadAgnCode;
    }

    public BigDecimal getOsdDivHeadAgnCode() {
        return osdDivHeadAgnCode;
    }

    public void setOsdSysCode(BigDecimal osdSysCode) {
        this.osdSysCode = osdSysCode;
    }

    public BigDecimal getOsdSysCode() {
        return osdSysCode;
    }

    public void setOrgSubDivisions(List<OrgSubDivision> orgSubDivisions) {
        this.orgSubDivisions = orgSubDivisions;
    }

    public List<OrgSubDivision> getOrgSubDivisions() {
        return orgSubDivisions;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setDltDesc(String dltDesc) {
        this.dltDesc = dltDesc;
    }

    public String getDltDesc() {
        return dltDesc;
    }

    public void setOdlDesc(String odlDesc) {
        this.odlDesc = odlDesc;
    }

    public String getOdlDesc() {
        return odlDesc;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentName() {
        return agentName;
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

    public void setBrnCode(BigDecimal brnCode) {
        this.brnCode = brnCode;
    }

    public BigDecimal getBrnCode() {
        return brnCode;
    }

    public void setRegCode(BigDecimal regCode) {
        this.regCode = regCode;
    }

    public BigDecimal getRegCode() {
        return regCode;
    }

    public void setOsdPostLevel(String osdPostLevel) {
        this.osdPostLevel = osdPostLevel;
    }

    public String getOsdPostLevel() {
        return osdPostLevel;
    }

    public void setOsdManagerAllowed(String osdManagerAllowed) {
        this.osdManagerAllowed = osdManagerAllowed;
    }

    public String getOsdManagerAllowed() {
        return osdManagerAllowed;
    }

    public void setOsdOverCommAllowed(String osdOverCommAllowed) {
        this.osdOverCommAllowed = osdOverCommAllowed;
    }

    public String getOsdOverCommAllowed() {
        return osdOverCommAllowed;
    }

    public void setOsdId(BigDecimal osdId) {
        this.osdId = osdId;
    }

    public BigDecimal getOsdId() {
        return osdId;
    }

    public void setOsdParentOsdId(BigDecimal osdParentOsdId) {
        this.osdParentOsdId = osdParentOsdId;
    }

    public BigDecimal getOsdParentOsdId() {
        return osdParentOsdId;
    }

    public void setOsdStatus(String osdStatus) {
        this.osdStatus = osdStatus;
    }

    public String getOsdStatus() {
        return osdStatus;
    }

    public void setOsdWet(Date osdWet) {
        this.osdWet = osdWet;
    }

    public Date getOsdWet() {
        return osdWet;
    }

    public void setOsdLocationCode(BigDecimal osdLocationCode) {
        this.osdLocationCode = osdLocationCode;
    }

    public BigDecimal getOsdLocationCode() {
        return osdLocationCode;
    }

    public void setOsdLocation(String osdLocation) {
        this.osdLocation = osdLocation;
    }

    public String getOsdLocation() {
        return osdLocation;
    }

    public void setOdh_code(BigDecimal odh_code) {
        this.odh_code = odh_code;
    }

    public BigDecimal getOdh_code() {
        return odh_code;
    }

    public void setOdh_osd_code(String odh_osd_code) {
        this.odh_osd_code = odh_osd_code;
    }

    public String getOdh_osd_code() {
        return odh_osd_code;
    }

    public void setOdh_osd_div_head_agn_code(BigDecimal odh_osd_div_head_agn_code) {
        this.odh_osd_div_head_agn_code = odh_osd_div_head_agn_code;
    }

    public BigDecimal getOdh_osd_div_head_agn_code() {
        return odh_osd_div_head_agn_code;
    }

    public void setOdh_wef_date(Date odh_wef_date) {
        this.odh_wef_date = odh_wef_date;
    }

    public Date getOdh_wef_date() {
        return odh_wef_date;
    }

    public void setOdh_wet_date(Date odh_wet_date) {
        this.odh_wet_date = odh_wet_date;
    }

    public Date getOdh_wet_date() {
        return odh_wet_date;
    }

    public void setOdh_agn_name(String odh_agn_name) {
        this.odh_agn_name = odh_agn_name;
    }

    public String getOdh_agn_name() {
        return odh_agn_name;
    }

    public void setOdh_agn_sht_desc(String odh_agn_sht_desc) {
        this.odh_agn_sht_desc = odh_agn_sht_desc;
    }

    public String getOdh_agn_sht_desc() {
        return odh_agn_sht_desc;
    }
}
