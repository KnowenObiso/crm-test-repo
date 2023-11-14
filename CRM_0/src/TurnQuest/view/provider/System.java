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

package TurnQuest.view.provider;


import TurnQuest.view.models.OrgSubDivision;

import java.math.BigDecimal;

import java.util.Date;
import java.util.List;


/**
 * Bean for representing System records. Includes properties and methods that
 * map to a given System.
 *
 * @author Frankline Ogongi
 */
public class System {

    /* Properties */
    public static Object out;
    private String code;
    private String shortDesc;
    private String name;
    private String mainFormName;
    private String active;
    private String dbUsername;
    private String dbPassword;
    private String dbString;
    private String path;
    private String orgCode;
    private String agentMainFormName;
    private String kbaCode;
    private String signaturePath;
    private String sysTemplate;
    private String brn_code;
    private String brn_sht_desc;
    private String brn_reg_code;
    private String brn_name;
    private String brn_phy_addrs;
    private String brn_email_addrs;
    private String brn_post_addrs;
    private String brn_twn_code;
    private String brn_cou_code;
    private String brn_contact;
    private String brn_manager;
    private String brn_agn_code;
    private String brn_post_level;
    private String v_bbr_code;
    private String v_bbr_branch_name;
    private String v_bbr_sht_desc;
    private String v_bbr_ref_code;
    private String v_bbr_bnk_code;
    private String sms_code;
    private String sms_sys_code;
    private String sms_sys_module;
    private String sms_clnt_code;
    private String sms_agn_code;
    private String sms_pol_no;
    private String pol_current_status;
    private String sms_pol_code;
    private String sms_clm_no;
    private String sms_tel_no;
    private String sms_msg;
    private String sms_status;
    private String sms_prepared_by;
    private String sms_send_date;
    private List<OrgSubDivision> orgSubDivisions;

    private String nodeType;

    private BigDecimal CMT_CODE;
    private BigDecimal CMT_ACC_CODE;
    private Date CMT_DATE;
    private String RELATED_ACCOUNT;
    private String RELATED_EMAIL;

    private BigDecimal CMSG_CODE;
    private String CMSG_TYPE;
    private String CMSG_TYPE_DESC;
    private String CMSG_SUBJ;
    private String CMSG_BODY;
    private String CMSG_STATUS;
    private String CMSG_STATUS_DESC;

    private Date CMSG_DATE;
    private String CMSG_SEND_ALL;
    private String CMSG_SEND_ALL_DESC;
    private boolean selected;
    private String clientName;
    private String couCode;
    private String couZipCode;


    private BigDecimal cproCode;
    private BigDecimal cproProCode;
    private String cproShtDesc;
    private BigDecimal cproCmpCode;
    private String cproProShtDesc;
    private String cproProDesc;
    private String emailType;
    private String transLevel;

    private String divName;
    private Date datePrepared;

    /**
     * Default Class Constructor
     */
    public System() {
        super();
    }

    /* Methods */

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMainFormName(String mainFormName) {
        this.mainFormName = mainFormName;
    }

    public String getMainFormName() {
        return mainFormName;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getActive() {
        return active;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbString(String dbString) {
        this.dbString = dbString;
    }

    public String getDbString() {
        return dbString;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setAgentMainFormName(String agentMainFormName) {
        this.agentMainFormName = agentMainFormName;
    }

    public String getAgentMainFormName() {
        return agentMainFormName;
    }

    public void setKbaCode(String kbaCode) {
        this.kbaCode = kbaCode;
    }

    public String getKbaCode() {
        return kbaCode;
    }

    public void setSignaturePath(String signaturePath) {
        this.signaturePath = signaturePath;
    }

    public String getSignaturePath() {
        return signaturePath;
    }

    public void setSysTemplate(String sysTemplate) {
        this.sysTemplate = sysTemplate;
    }

    public String getSysTemplate() {
        return sysTemplate;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setOrgSubDivisions(List<OrgSubDivision> orgSubDivisions) {
        this.orgSubDivisions = orgSubDivisions;
    }

    public List<OrgSubDivision> getOrgSubDivisions() {
        return orgSubDivisions;
    }

    public void setCMT_CODE(BigDecimal CMT_CODE) {
        this.CMT_CODE = CMT_CODE;
    }

    public BigDecimal getCMT_CODE() {
        return CMT_CODE;
    }

    public void setCMT_ACC_CODE(BigDecimal CMT_ACC_CODE) {
        this.CMT_ACC_CODE = CMT_ACC_CODE;
    }

    public BigDecimal getCMT_ACC_CODE() {
        return CMT_ACC_CODE;
    }

    public void setCMT_DATE(Date CMT_DATE) {
        this.CMT_DATE = CMT_DATE;
    }

    public Date getCMT_DATE() {
        return CMT_DATE;
    }

    public void setRELATED_ACCOUNT(String RELATED_ACCOUNT) {
        this.RELATED_ACCOUNT = RELATED_ACCOUNT;
    }

    public String getRELATED_ACCOUNT() {
        return RELATED_ACCOUNT;
    }

    public void setRELATED_EMAIL(String RELATED_EMAIL) {
        this.RELATED_EMAIL = RELATED_EMAIL;
    }

    public String getRELATED_EMAIL() {
        return RELATED_EMAIL;
    }

    public void setCMSG_CODE(BigDecimal CMSG_CODE) {
        this.CMSG_CODE = CMSG_CODE;
    }

    public BigDecimal getCMSG_CODE() {
        return CMSG_CODE;
    }

    public void setCMSG_TYPE(String CMSG_TYPE) {
        this.CMSG_TYPE = CMSG_TYPE;
    }

    public String getCMSG_TYPE() {
        return CMSG_TYPE;
    }

    public void setCMSG_TYPE_DESC(String CMSG_TYPE_DESC) {
        this.CMSG_TYPE_DESC = CMSG_TYPE_DESC;
    }

    public String getCMSG_TYPE_DESC() {
        return CMSG_TYPE_DESC;
    }

    public void setCMSG_SUBJ(String CMSG_SUBJ) {
        this.CMSG_SUBJ = CMSG_SUBJ;
    }

    public String getCMSG_SUBJ() {
        return CMSG_SUBJ;
    }

    public void setCMSG_BODY(String CMSG_BODY) {
        this.CMSG_BODY = CMSG_BODY;
    }

    public String getCMSG_BODY() {
        return CMSG_BODY;
    }

    public void setCMSG_STATUS(String CMSG_STATUS) {
        this.CMSG_STATUS = CMSG_STATUS;
    }

    public String getCMSG_STATUS() {
        return CMSG_STATUS;
    }

    public void setCMSG_STATUS_DESC(String CMSG_STATUS_DESC) {
        this.CMSG_STATUS_DESC = CMSG_STATUS_DESC;
    }

    public String getCMSG_STATUS_DESC() {
        return CMSG_STATUS_DESC;
    }

    public void setCMSG_DATE(Date CMSG_DATE) {
        this.CMSG_DATE = CMSG_DATE;
    }

    public Date getCMSG_DATE() {
        return CMSG_DATE;
    }

    public void setCMSG_SEND_ALL(String CMSG_SEND_ALL) {
        this.CMSG_SEND_ALL = CMSG_SEND_ALL;
    }

    public String getCMSG_SEND_ALL() {
        return CMSG_SEND_ALL;
    }

    public void setCMSG_SEND_ALL_DESC(String CMSG_SEND_ALL_DESC) {
        this.CMSG_SEND_ALL_DESC = CMSG_SEND_ALL_DESC;
    }

    public String getCMSG_SEND_ALL_DESC() {
        return CMSG_SEND_ALL_DESC;
    }

    public static void setOut(Object out) {
        System.out = out;
    }

    public static Object getOut() {
        return out;
    }

    public void setBrn_code(String brn_code) {
        this.brn_code = brn_code;
    }

    public String getBrn_code() {
        return brn_code;
    }

    public void setBrn_sht_desc(String brn_sht_desc) {
        this.brn_sht_desc = brn_sht_desc;
    }

    public String getBrn_sht_desc() {
        return brn_sht_desc;
    }

    public void setBrn_reg_code(String brn_reg_code) {
        this.brn_reg_code = brn_reg_code;
    }

    public String getBrn_reg_code() {
        return brn_reg_code;
    }

    public void setBrn_name(String brn_name) {
        this.brn_name = brn_name;
    }

    public String getBrn_name() {
        return brn_name;
    }

    public void setBrn_phy_addrs(String brn_phy_addrs) {
        this.brn_phy_addrs = brn_phy_addrs;
    }

    public String getBrn_phy_addrs() {
        return brn_phy_addrs;
    }

    public void setBrn_email_addrs(String brn_email_addrs) {
        this.brn_email_addrs = brn_email_addrs;
    }

    public String getBrn_email_addrs() {
        return brn_email_addrs;
    }

    public void setBrn_post_addrs(String brn_post_addrs) {
        this.brn_post_addrs = brn_post_addrs;
    }

    public String getBrn_post_addrs() {
        return brn_post_addrs;
    }

    public void setBrn_twn_code(String brn_twn_code) {
        this.brn_twn_code = brn_twn_code;
    }

    public String getBrn_twn_code() {
        return brn_twn_code;
    }

    public void setBrn_cou_code(String brn_cou_code) {
        this.brn_cou_code = brn_cou_code;
    }

    public String getBrn_cou_code() {
        return brn_cou_code;
    }

    public void setBrn_contact(String brn_contact) {
        this.brn_contact = brn_contact;
    }

    public String getBrn_contact() {
        return brn_contact;
    }

    public void setBrn_manager(String brn_manager) {
        this.brn_manager = brn_manager;
    }

    public String getBrn_manager() {
        return brn_manager;
    }

    public void setBrn_agn_code(String brn_agn_code) {
        this.brn_agn_code = brn_agn_code;
    }

    public String getBrn_agn_code() {
        return brn_agn_code;
    }

    public void setBrn_post_level(String brn_post_level) {
        this.brn_post_level = brn_post_level;
    }

    public String getBrn_post_level() {
        return brn_post_level;
    }

    public void setV_bbr_code(String v_bbr_code) {
        this.v_bbr_code = v_bbr_code;
    }

    public String getV_bbr_code() {
        return v_bbr_code;
    }

    public void setV_bbr_branch_name(String v_bbr_branch_name) {
        this.v_bbr_branch_name = v_bbr_branch_name;
    }

    public String getV_bbr_branch_name() {
        return v_bbr_branch_name;
    }

    public void setV_bbr_sht_desc(String v_bbr_sht_desc) {
        this.v_bbr_sht_desc = v_bbr_sht_desc;
    }

    public String getV_bbr_sht_desc() {
        return v_bbr_sht_desc;
    }

    public void setV_bbr_ref_code(String v_bbr_ref_code) {
        this.v_bbr_ref_code = v_bbr_ref_code;
    }

    public String getV_bbr_ref_code() {
        return v_bbr_ref_code;
    }

    public void setV_bbr_bnk_code(String v_bbr_bnk_code) {
        this.v_bbr_bnk_code = v_bbr_bnk_code;
    }

    public String getV_bbr_bnk_code() {
        return v_bbr_bnk_code;
    }

    public void setSms_code(String sms_code) {
        this.sms_code = sms_code;
    }

    public String getSms_code() {
        return sms_code;
    }

    public void setSms_sys_code(String sms_sys_code) {
        this.sms_sys_code = sms_sys_code;
    }

    public String getSms_sys_code() {
        return sms_sys_code;
    }

    public void setSms_sys_module(String sms_sys_module) {
        this.sms_sys_module = sms_sys_module;
    }

    public String getSms_sys_module() {
        return sms_sys_module;
    }

    public void setSms_clnt_code(String sms_clnt_code) {
        this.sms_clnt_code = sms_clnt_code;
    }

    public String getSms_clnt_code() {
        return sms_clnt_code;
    }

    public void setSms_agn_code(String sms_agn_code) {
        this.sms_agn_code = sms_agn_code;
    }

    public String getSms_agn_code() {
        return sms_agn_code;
    }

    public void setSms_pol_no(String sms_pol_no) {
        this.sms_pol_no = sms_pol_no;
    }

    public String getSms_pol_no() {
        return sms_pol_no;
    }

    public void setPol_current_status(String pol_current_status) {
        this.pol_current_status = pol_current_status;
    }

    public String getPol_current_status() {
        return pol_current_status;
    }

    public void setSms_pol_code(String sms_pol_code) {
        this.sms_pol_code = sms_pol_code;
    }

    public String getSms_pol_code() {
        return sms_pol_code;
    }

    public void setSms_clm_no(String sms_clm_no) {
        this.sms_clm_no = sms_clm_no;
    }

    public String getSms_clm_no() {
        return sms_clm_no;
    }

    public void setSms_tel_no(String sms_tel_no) {
        this.sms_tel_no = sms_tel_no;
    }

    public String getSms_tel_no() {
        return sms_tel_no;
    }

    public void setSms_msg(String sms_msg) {
        this.sms_msg = sms_msg;
    }

    public String getSms_msg() {
        return sms_msg;
    }

    public void setSms_status(String sms_status) {
        this.sms_status = sms_status;
    }

    public String getSms_status() {
        return sms_status;
    }

    public void setSms_prepared_by(String sms_prepared_by) {
        this.sms_prepared_by = sms_prepared_by;
    }

    public String getSms_prepared_by() {
        return sms_prepared_by;
    }

    public void setSms_send_date(String sms_send_date) {
        this.sms_send_date = sms_send_date;
    }

    public String getSms_send_date() {
        return sms_send_date;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setCouCode(String couCode) {
        this.couCode = couCode;
    }

    public String getCouCode() {
        return couCode;
    }

    public void setCouZipCode(String couZipCode) {
        this.couZipCode = couZipCode;
    }

    public String getCouZipCode() {
        return couZipCode;
    }

    public void setCproCode(BigDecimal cproCode) {
        this.cproCode = cproCode;
    }

    public BigDecimal getCproCode() {
        return cproCode;
    }

    public void setCproProCode(BigDecimal cproProCode) {
        this.cproProCode = cproProCode;
    }

    public BigDecimal getCproProCode() {
        return cproProCode;
    }

    public void setCproShtDesc(String cproShtDesc) {
        this.cproShtDesc = cproShtDesc;
    }

    public String getCproShtDesc() {
        return cproShtDesc;
    }

    public void setCproCmpCode(BigDecimal cproCmpCode) {
        this.cproCmpCode = cproCmpCode;
    }

    public BigDecimal getCproCmpCode() {
        return cproCmpCode;
    }

    public void setCproProShtDesc(String cproProShtDesc) {
        this.cproProShtDesc = cproProShtDesc;
    }

    public String getCproProShtDesc() {
        return cproProShtDesc;
    }

    public void setCproProDesc(String cproProDesc) {
        this.cproProDesc = cproProDesc;
    }

    public String getCproProDesc() {
        return cproProDesc;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setTransLevel(String transLevel) {
        this.transLevel = transLevel;
    }

    public String getTransLevel() {
        return transLevel;
    }

    public void setDivName(String divName) {
        this.divName = divName;
    }

    public String getDivName() {
        return divName;
    }

    public void setDatePrepared(Date datePrepared) {
        this.datePrepared = datePrepared;
    }

    public Date getDatePrepared() {
        return datePrepared;
    }
}
