package TurnQuest.view.Usr;

import java.math.BigDecimal;

public class UserGrpType {
    
    private Integer code;
    private String shortDesc;
    private String typeId;
    private String groupType;
    private String idSerialFormat;
    
    private BigDecimal teamLeader;
    private BigDecimal branchCode;
    private String branchName;
    
    public UserGrpType() {
        super();
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setIdSerialFormat(String idSerialFormat) {
        this.idSerialFormat = idSerialFormat;
    }

    public String getIdSerialFormat() {
        return idSerialFormat;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setTeamLeader(BigDecimal teamLeader) {
        this.teamLeader = teamLeader;
    }

    public BigDecimal getTeamLeader() {
        return teamLeader;
    }

    public void setBranchCode(BigDecimal branchCode) {
        this.branchCode = branchCode;
    }

    public BigDecimal getBranchCode() {
        return branchCode;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchName() {
        return branchName;
    }
}
