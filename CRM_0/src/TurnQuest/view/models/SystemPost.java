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


public class SystemPost implements SQLData, Serializable {

    private BigDecimal spostSysCode;
    private BigDecimal spostSysplCode;
    private BigDecimal spostParentSpostCode;
    private BigDecimal spostCode;
    private String spostShtDesc;
    private String spostDesc;
    private String spostRemarks;
    private Date spostWef;
    private BigDecimal spostBrnCode;
    private String spostSubdivOsdCode;
    private BigDecimal spostUsrCode;
    private BigDecimal BBR_CODE;
    private BigDecimal BBR_BNK_CODE;
    private String BBR_BRANCH_NAME;
    private String BBR_REMARKS;
    private String BBR_SHT_DESC;
    private BigDecimal BBR_REF_CODE;
    private String BBR_EFT_SUPPORTED;
    private String BBR_DD_SUPPORTED;
    //private Date     BBR_DATE_CREATED;
    private String BBR_CREATED_BY;
    private String BBR_PHYSICAL_ADDRS;
    private List<SystemPost> systemPostList;
    private Date BBR_DATE_CREATED;
    private String SQLTypeName;
    private BigDecimal BBB_CODE;
    private BigDecimal BBB_BRN_CODE;
    private String BBB_BRN_SHT_DESC;
    private BigDecimal BBB_BRN_REG_CODE;
    private String BBB_BRN_NAME;
    private String BBB_BRN_PHY_ADDRS;
    private BigDecimal BBB_BBR_CODE;
    private BigDecimal BBB_BBR_BNK_CODE;
    private String BBB_BBR_BRANCH_NAME;
    private String BBB_BBR_SHT_DESC;
    private String BBB_BBR_PHYSICAL_ADDRS;

    public SystemPost() {
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
            if (SQLTypeName.equalsIgnoreCase("TQC_SYS_POSTS_OBJ")) {
                stream.writeBigDecimal(spostSysCode);
                stream.writeBigDecimal(spostSysplCode);
                stream.writeBigDecimal(spostParentSpostCode);
                stream.writeBigDecimal(spostCode);
                stream.writeString(spostShtDesc);
                stream.writeString(spostDesc);
                stream.writeString(spostRemarks);
                stream.writeDate(spostWef == null ? null :
                                 new java.sql.Date(spostWef.getTime()));
                stream.writeBigDecimal(spostBrnCode);
                stream.writeString(spostSubdivOsdCode);
                stream.writeBigDecimal(spostUsrCode);
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

    public void setSpostSysCode(BigDecimal spostSysCode) {
        this.spostSysCode = spostSysCode;
    }

    public BigDecimal getSpostSysCode() {
        return spostSysCode;
    }

    public void setSpostSysplCode(BigDecimal spostSysplCode) {
        this.spostSysplCode = spostSysplCode;
    }

    public BigDecimal getSpostSysplCode() {
        return spostSysplCode;
    }

    public void setSpostParentSpostCode(BigDecimal spostParentSpostCode) {
        this.spostParentSpostCode = spostParentSpostCode;
    }

    public BigDecimal getSpostParentSpostCode() {
        return spostParentSpostCode;
    }

    public void setSpostCode(BigDecimal spostCode) {
        this.spostCode = spostCode;
    }

    public BigDecimal getSpostCode() {
        return spostCode;
    }

    public void setSpostShtDesc(String spostShtDesc) {
        this.spostShtDesc = spostShtDesc;
    }

    public String getSpostShtDesc() {
        return spostShtDesc;
    }

    public void setSpostDesc(String spostDesc) {
        this.spostDesc = spostDesc;
    }

    public String getSpostDesc() {
        return spostDesc;
    }

    public void setSpostRemarks(String spostRemarks) {
        this.spostRemarks = spostRemarks;
    }

    public String getSpostRemarks() {
        return spostRemarks;
    }

    public void setSpostWef(Date spostWef) {
        this.spostWef = spostWef;
    }

    public Date getSpostWef() {
        return spostWef;
    }

    public void setSpostBrnCode(BigDecimal spostBrnCode) {
        this.spostBrnCode = spostBrnCode;
    }

    public BigDecimal getSpostBrnCode() {
        return spostBrnCode;
    }

    public void setSpostSubdivOsdCode(String spostSubdivOsdCode) {
        this.spostSubdivOsdCode = spostSubdivOsdCode;
    }

    public String getSpostSubdivOsdCode() {
        return spostSubdivOsdCode;
    }

    public void setSpostUsrCode(BigDecimal spostUsrCode) {
        this.spostUsrCode = spostUsrCode;
    }

    public BigDecimal getSpostUsrCode() {
        return spostUsrCode;
    }

    public void setSystemPostList(List<SystemPost> systemPostList) {
        this.systemPostList = systemPostList;
    }

    public List<SystemPost> getSystemPostList() {
        return systemPostList;
    }

    public void setBBR_CODE(BigDecimal BBR_CODE) {
        this.BBR_CODE = BBR_CODE;
    }

    public BigDecimal getBBR_CODE() {
        return BBR_CODE;
    }

    public void setBBR_BNK_CODE(BigDecimal BBR_BNK_CODE) {
        this.BBR_BNK_CODE = BBR_BNK_CODE;
    }

    public BigDecimal getBBR_BNK_CODE() {
        return BBR_BNK_CODE;
    }

    public void setBBR_BRANCH_NAME(String BBR_BRANCH_NAME) {
        this.BBR_BRANCH_NAME = BBR_BRANCH_NAME;
    }

    public String getBBR_BRANCH_NAME() {
        return BBR_BRANCH_NAME;
    }

    public void setBBR_REMARKS(String BBR_REMARKS) {
        this.BBR_REMARKS = BBR_REMARKS;
    }

    public String getBBR_REMARKS() {
        return BBR_REMARKS;
    }

    public void setBBR_SHT_DESC(String BBR_SHT_DESC) {
        this.BBR_SHT_DESC = BBR_SHT_DESC;
    }

    public String getBBR_SHT_DESC() {
        return BBR_SHT_DESC;
    }

    public void setBBR_REF_CODE(BigDecimal BBR_REF_CODE) {
        this.BBR_REF_CODE = BBR_REF_CODE;
    }

    public BigDecimal getBBR_REF_CODE() {
        return BBR_REF_CODE;
    }

    public void setBBR_EFT_SUPPORTED(String BBR_EFT_SUPPORTED) {
        this.BBR_EFT_SUPPORTED = BBR_EFT_SUPPORTED;
    }

    public String getBBR_EFT_SUPPORTED() {
        return BBR_EFT_SUPPORTED;
    }

    public void setBBR_DD_SUPPORTED(String BBR_DD_SUPPORTED) {
        this.BBR_DD_SUPPORTED = BBR_DD_SUPPORTED;
    }

    public String getBBR_DD_SUPPORTED() {
        return BBR_DD_SUPPORTED;
    }


    public void setBBR_CREATED_BY(String BBR_CREATED_BY) {
        this.BBR_CREATED_BY = BBR_CREATED_BY;
    }

    public String getBBR_CREATED_BY() {
        return BBR_CREATED_BY;
    }

    public void setBBR_PHYSICAL_ADDRS(String BBR_PHYSICAL_ADDRS) {
        this.BBR_PHYSICAL_ADDRS = BBR_PHYSICAL_ADDRS;
    }

    public String getBBR_PHYSICAL_ADDRS() {
        return BBR_PHYSICAL_ADDRS;
    }

    public void setBBR_DATE_CREATED(Date BBR_DATE_CREATED) {
        this.BBR_DATE_CREATED = BBR_DATE_CREATED;
    }

    public Date getBBR_DATE_CREATED() {
        return BBR_DATE_CREATED;
    }

    public void setBBB_CODE(BigDecimal BBB_CODE) {
        this.BBB_CODE = BBB_CODE;
    }

    public BigDecimal getBBB_CODE() {
        return BBB_CODE;
    }

    public void setBBB_BRN_CODE(BigDecimal BBB_BRN_CODE) {
        this.BBB_BRN_CODE = BBB_BRN_CODE;
    }

    public BigDecimal getBBB_BRN_CODE() {
        return BBB_BRN_CODE;
    }

    public void setBBB_BRN_SHT_DESC(String BBB_BRN_SHT_DESC) {
        this.BBB_BRN_SHT_DESC = BBB_BRN_SHT_DESC;
    }

    public String getBBB_BRN_SHT_DESC() {
        return BBB_BRN_SHT_DESC;
    }

    public void setBBB_BRN_REG_CODE(BigDecimal BBB_BRN_REG_CODE) {
        this.BBB_BRN_REG_CODE = BBB_BRN_REG_CODE;
    }

    public BigDecimal getBBB_BRN_REG_CODE() {
        return BBB_BRN_REG_CODE;
    }

    public void setBBB_BRN_NAME(String BBB_BRN_NAME) {
        this.BBB_BRN_NAME = BBB_BRN_NAME;
    }

    public String getBBB_BRN_NAME() {
        return BBB_BRN_NAME;
    }

    public void setBBB_BRN_PHY_ADDRS(String BBB_BRN_PHY_ADDRS) {
        this.BBB_BRN_PHY_ADDRS = BBB_BRN_PHY_ADDRS;
    }

    public String getBBB_BRN_PHY_ADDRS() {
        return BBB_BRN_PHY_ADDRS;
    }

    public void setBBB_BBR_CODE(BigDecimal BBB_BBR_CODE) {
        this.BBB_BBR_CODE = BBB_BBR_CODE;
    }

    public BigDecimal getBBB_BBR_CODE() {
        return BBB_BBR_CODE;
    }

    public void setBBB_BBR_BNK_CODE(BigDecimal BBB_BBR_BNK_CODE) {
        this.BBB_BBR_BNK_CODE = BBB_BBR_BNK_CODE;
    }

    public BigDecimal getBBB_BBR_BNK_CODE() {
        return BBB_BBR_BNK_CODE;
    }

    public void setBBB_BBR_BRANCH_NAME(String BBB_BBR_BRANCH_NAME) {
        this.BBB_BBR_BRANCH_NAME = BBB_BBR_BRANCH_NAME;
    }

    public String getBBB_BBR_BRANCH_NAME() {
        return BBB_BBR_BRANCH_NAME;
    }

    public void setBBB_BBR_SHT_DESC(String BBB_BBR_SHT_DESC) {
        this.BBB_BBR_SHT_DESC = BBB_BBR_SHT_DESC;
    }

    public String getBBB_BBR_SHT_DESC() {
        return BBB_BBR_SHT_DESC;
    }

    public void setBBB_BBR_PHYSICAL_ADDRS(String BBB_BBR_PHYSICAL_ADDRS) {
        this.BBB_BBR_PHYSICAL_ADDRS = BBB_BBR_PHYSICAL_ADDRS;
    }

    public String getBBB_BBR_PHYSICAL_ADDRS() {
        return BBB_BBR_PHYSICAL_ADDRS;
    }
}
