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

package TurnQuest.view.Accounts;


import java.math.BigDecimal;

import java.util.Date;
import java.util.List;


public class AccountType {
    public AccountType() {
        super();
    }

    private String id;
    private String cmapping;
    private String smapping;
    private BigDecimal wrate;
    private BigDecimal orate;
    private BigDecimal crate;
    private String acformat; // a/c short  description
    private BigDecimal vatrate;
    private String year;
    private String regno;
    private Date wef;
    private Date wet;
    private String accepted;
    private BigDecimal graceperiod;
    private String name;
    private String qualification;
    private BigDecimal shareholding;
    private String designation;
    private String phyaddress;
    private String postaddress;
    private String idno;
    private String email;
    private String telno;
    private String username;
    private String personalrank;
    private String createdby;
    private Date datecreated;
    private String allowlogin;
    private String status;
    private String reset;
    private BigDecimal ACT_CODE;
    private String ACT_ACCOUNT_TYPE;
    private String ACT_TYPE_SHT_DESC;
    private List<AccountValues> produces;
    private String Type;
    private String accountFormat;
    private String odl_code;
    private String rank;
    private String mgrNoInFix;
    private BigDecimal spr_code;
    private String spr_sht_desc;
    private String spr_name;
    private boolean selected;
    private String telNo;

    private BigDecimal branchAgnCode;
    private BigDecimal brnCode;
    private String BranchName;
    private String agentCode;
    private String brnShtDesc;
    private String phoneNumber;


    private BigDecimal bruCode;
    private BigDecimal bruBrnCode;
    private String bruShtDesc;
    private String bruName;


    private String braShtDesc;
    private String agnShtDesc;
    private String principleDirecor;
    private String nationality;
    private String country;
    
    private String accountGroups;    
    
    private String COVERAGE_AREA;
    private String SPOKE;
    private String SBU;
    private BigDecimal COVERAGE_CODE;
    private BigDecimal SPOKE_CODE;
    private BigDecimal SBU_CODE;
    
    private BigDecimal ACT_COMMISION_LEVY_RATE;
    private String regionName;
    private String regionCode;
    

    private String entShtDesc;
    private String entName;
    private String entPin; 
    private BigDecimal entCode;
    private String entPosatalAddress;
    private String entEmailAddress;
    private String entSmsTel;

    private String agnName;
    private String agnPin; 
    private String agnEmail;
    private String agnTel; 
    private String agnBnkAccNo;
    private String relation;
    private String newRelation;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCmapping(String cmapping) {
        this.cmapping = cmapping;
    }

    public String getCmapping() {
        return cmapping;
    }

    public void setSmapping(String smapping) {
        this.smapping = smapping;
    }

    public String getSmapping() {
        return smapping;
    }

    public void setWrate(BigDecimal wrate) {
        this.wrate = wrate;
    }

    public BigDecimal getWrate() {
        return wrate;
    }

    public void setOrate(BigDecimal orate) {
        this.orate = orate;
    }

    public BigDecimal getOrate() {
        return orate;
    }

    public void setCrate(BigDecimal crate) {
        this.crate = crate;
    }

    public BigDecimal getCrate() {
        return crate;
    }

    public void setAcformat(String acformat) {
        this.acformat = acformat;
    }

    public String getAcformat() {
        return acformat;
    }


    public void setVatrate(BigDecimal vatrate) {
        this.vatrate = vatrate;
    }

    public BigDecimal getVatrate() {
        return vatrate;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public String getRegno() {
        return regno;
    }

    public void setWef(Date wef) {
        this.wef = wef;
    }

    public Date getWef() {
        return wef;
    }

    public void setWet(Date wet) {
        this.wet = wet;
    }

    public Date getWet() {
        return wet;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setGraceperiod(BigDecimal graceperiod) {
        this.graceperiod = graceperiod;
    }

    public BigDecimal getGraceperiod() {
        return graceperiod;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getQualification() {
        return qualification;
    }

    public void setShareholding(BigDecimal shareholding) {
        this.shareholding = shareholding;
    }

    public BigDecimal getShareholding() {
        return shareholding;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDesignation() {
        return designation;
    }

    public void setPhyaddress(String phyaddress) {
        this.phyaddress = phyaddress;
    }

    public String getPhyaddress() {
        return phyaddress;
    }

    public void setPostaddress(String postaddress) {
        this.postaddress = postaddress;
    }

    public String getPostaddress() {
        return postaddress;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getIdno() {
        return idno;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setTelno(String telno) {
        this.telno = telno;
    }

    public String getTelno() {
        return telno;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPersonalrank(String personalrank) {
        this.personalrank = personalrank;
    }

    public String getPersonalrank() {
        return personalrank;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setAllowlogin(String allowlogin) {
        this.allowlogin = allowlogin;
    }

    public String getAllowlogin() {
        return allowlogin;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setReset(String reset) {
        this.reset = reset;
    }

    public String getReset() {
        return reset;
    }

    public void setDatecreated(Date datecreated) {
        this.datecreated = datecreated;
    }

    public Date getDatecreated() {
        return datecreated;
    }

    public void setACT_CODE(BigDecimal ACT_CODE) {
        this.ACT_CODE = ACT_CODE;
    }

    public BigDecimal getACT_CODE() {
        return ACT_CODE;
    }

    public void setProduces(List<AccountValues> produces) {
        this.produces = produces;
    }

    public List<AccountValues> getProduces() {
        return produces;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getType() {
        return Type;
    }

    public void setAccountFormat(String accountFormat) {
        this.accountFormat = accountFormat;
    }

    public String getAccountFormat() {
        return accountFormat;
    }

    public void setOdl_code(String odl_code) {
        this.odl_code = odl_code;
    }

    public String getOdl_code() {
        return odl_code;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRank() {
        return rank;
    }

    public void setMgrNoInFix(String mgrNoInFix) {
        this.mgrNoInFix = mgrNoInFix;
    }

    public String getMgrNoInFix() {
        return mgrNoInFix;
    }

    public void setSpr_code(BigDecimal spr_code) {
        this.spr_code = spr_code;
    }

    public BigDecimal getSpr_code() {
        return spr_code;
    }

    public void setSpr_sht_desc(String spr_sht_desc) {
        this.spr_sht_desc = spr_sht_desc;
    }

    public String getSpr_sht_desc() {
        return spr_sht_desc;
    }

    public void setSpr_name(String spr_name) {
        this.spr_name = spr_name;
    }

    public String getSpr_name() {
        return spr_name;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setBranchAgnCode(BigDecimal branchAgnCode) {
        this.branchAgnCode = branchAgnCode;
    }

    public BigDecimal getBranchAgnCode() {
        return branchAgnCode;
    }

    public void setBrnCode(BigDecimal brnCode) {
        this.brnCode = brnCode;
    }

    public BigDecimal getBrnCode() {
        return brnCode;
    }

    public void setBranchName(String BranchName) {
        this.BranchName = BranchName;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setBrnShtDesc(String brnShtDesc) {
        this.brnShtDesc = brnShtDesc;
    }

    public String getBrnShtDesc() {
        return brnShtDesc;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setBruCode(BigDecimal bruCode) {
        this.bruCode = bruCode;
    }

    public BigDecimal getBruCode() {
        return bruCode;
    }

    public void setBruBrnCode(BigDecimal bruBrnCode) {
        this.bruBrnCode = bruBrnCode;
    }

    public BigDecimal getBruBrnCode() {
        return bruBrnCode;
    }

    public void setBruShtDesc(String bruShtDesc) {
        this.bruShtDesc = bruShtDesc;
    }

    public String getBruShtDesc() {
        return bruShtDesc;
    }

    public void setBruName(String bruName) {
        this.bruName = bruName;
    }

    public String getBruName() {
        return bruName;
    }

    public void setBraShtDesc(String braShtDesc) {
        this.braShtDesc = braShtDesc;
    }

    public String getBraShtDesc() {
        return braShtDesc;
    }

    public void setAgnShtDesc(String agnShtDesc) {
        this.agnShtDesc = agnShtDesc;
    }

    public String getAgnShtDesc() {
        return agnShtDesc;
    }

    public void setPrincipleDirecor(String principleDirecor) {
        this.principleDirecor = principleDirecor;
    }

    public String getPrincipleDirecor() {
        return principleDirecor;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNationality() {
        return nationality;
    }

    public void setAccountGroups(String accountGroups) {
        this.accountGroups = accountGroups;
    }

    public String getAccountGroups() {
        return accountGroups;
    }

    public void setCOVERAGE_AREA(String COVERAGE_AREA) {
        this.COVERAGE_AREA = COVERAGE_AREA;
    }

    public String getCOVERAGE_AREA() {
        return COVERAGE_AREA;
    }

    public void setSPOKE(String SPOKE) {
        this.SPOKE = SPOKE;
    }

    public String getSPOKE() {
        return SPOKE;
    }

    public void setSBU(String SBU) {
        this.SBU = SBU;
    }

    public String getSBU() {
        return SBU;
    }

    public void setCOVERAGE_CODE(BigDecimal COVERAGE_CODE) {
        this.COVERAGE_CODE = COVERAGE_CODE;
    }

    public BigDecimal getCOVERAGE_CODE() {
        return COVERAGE_CODE;
    }

    public void setSPOKE_CODE(BigDecimal SPOKE_CODE) {
        this.SPOKE_CODE = SPOKE_CODE;
    }

    public BigDecimal getSPOKE_CODE() {
        return SPOKE_CODE;
    }

    public void setSBU_CODE(BigDecimal SBU_CODE) {
        this.SBU_CODE = SBU_CODE;
    }

    public BigDecimal getSBU_CODE() {
        return SBU_CODE;
    }

    public void setACT_COMMISION_LEVY_RATE(BigDecimal ACT_COMMISION_LEVY_RATE) {
        this.ACT_COMMISION_LEVY_RATE = ACT_COMMISION_LEVY_RATE;
    }

    public BigDecimal getACT_COMMISION_LEVY_RATE() {
        return ACT_COMMISION_LEVY_RATE;
    }

    public void setACT_ACCOUNT_TYPE(String ACT_ACCOUNT_TYPE) {
        this.ACT_ACCOUNT_TYPE = ACT_ACCOUNT_TYPE;
    }

    public String getACT_ACCOUNT_TYPE() {
        return ACT_ACCOUNT_TYPE;
    }

    public void setACT_TYPE_SHT_DESC(String ACT_TYPE_SHT_DESC) {
        this.ACT_TYPE_SHT_DESC = ACT_TYPE_SHT_DESC;
    }

    public String getACT_TYPE_SHT_DESC() {
        return ACT_TYPE_SHT_DESC;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }



    public void setEntShtDesc(String entShtDesc) {
        this.entShtDesc = entShtDesc;
    }

    public String getEntShtDesc() {
        return entShtDesc;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntPin(String entPin) {
        this.entPin = entPin;
    }

    public String getEntPin() {
        return entPin;
    }

    public void setEntCode(BigDecimal entCode) {
        this.entCode = entCode;
    }

    public BigDecimal getEntCode() {
        return entCode;
    }

    public void setAgnName(String agnName) {
        this.agnName = agnName;
    }

    public String getAgnName() {
        return agnName;
    }

    public void setAgnPin(String agnPin) {
        this.agnPin = agnPin;
    }

    public String getAgnPin() {
        return agnPin;
    }

    public void setAgnEmail(String agnEmail) {
        this.agnEmail = agnEmail;
    }

    public String getAgnEmail() {
        return agnEmail;
    }

    public void setAgnTel(String agnTel) {
        this.agnTel = agnTel;
    }

    public String getAgnTel() {
        return agnTel;
    }

    public void setAgnBnkAccNo(String agnBnkAccNo) {
        this.agnBnkAccNo = agnBnkAccNo;
    }

    public String getAgnBnkAccNo() {
        return agnBnkAccNo;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getRelation() {
        return relation;
    }

    public void setNewRelation(String newRelation) {
        this.newRelation = newRelation;
    }

    public String getNewRelation() {
        return newRelation;
    }

    public void setEntPosatalAddress(String entPosatalAddress) {
        this.entPosatalAddress = entPosatalAddress;
    }

    public String getEntPosatalAddress() {
        return entPosatalAddress;
    }

    public void setEntEmailAddress(String entEmailAddress) {
        this.entEmailAddress = entEmailAddress;
    }

    public String getEntEmailAddress() {
        return entEmailAddress;
    }

    public void setEntSmsTel(String entSmsTel) {
        this.entSmsTel = entSmsTel;
    }

    public String getEntSmsTel() {
        return entSmsTel;
    }
}
