package TurnQuest.view.client;

import java.sql.Date;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;


public class AgentLoadingTab implements SQLData {

    private String sql_type = "TQC_AGNT_LOADING_OBJ";
    private String agentAccountCode;
    private String agentAccountType;
    private String AgentAccountName;
    private String agentPhysicalAddress;
    private String agentPostalAddress;
    private String agentTownName;
    private String agentRegCode;
    private String agentContactPerson;
    private String agentTelNumber;
    private String agentFaxNumber;
    private String agentEmailAddress;
    private Date agentDateCreated;
    private Date agentCeckDate;
    private String branchName;


    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        sql_type = typeName;
        agentAccountCode = stream.readString();
        agentAccountType = stream.readString();
        AgentAccountName = stream.readString();
        agentPhysicalAddress = stream.readString();
        agentPostalAddress = stream.readString();
        agentTownName = stream.readString();
        agentRegCode = stream.readString();
        agentContactPerson = stream.readString();
        agentTelNumber = stream.readString();
        agentFaxNumber = stream.readString();
        agentEmailAddress = stream.readString();
        agentDateCreated = stream.readDate();
        agentCeckDate = stream.readDate();
        branchName = stream.readString();

    }

    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeString(agentAccountCode);
        stream.writeString(agentAccountType);
        stream.writeString(AgentAccountName);
        stream.writeString(agentPhysicalAddress);
        stream.writeString(agentPostalAddress);
        stream.writeString(agentTownName);
        stream.writeString(agentRegCode);
        stream.writeString(agentContactPerson);
        stream.writeString(agentTelNumber);
        stream.writeString(agentFaxNumber);
        stream.writeString(agentEmailAddress);
        stream.writeDate(agentDateCreated);
        stream.writeDate(agentCeckDate);
        stream.writeString(branchName);
    }

    public String getSQLTypeName() {
        return sql_type;
    }

    public void setAgentPhysicalAddress(String agentPhysicalAddress) {
        this.agentPhysicalAddress = agentPhysicalAddress;
    }

    public String getAgentPhysicalAddress() {
        return agentPhysicalAddress;
    }

    public void setAgentPostalAddress(String agentPostalAddress) {
        this.agentPostalAddress = agentPostalAddress;
    }

    public String getAgentPostalAddress() {
        return agentPostalAddress;
    }


    public void setAgentEmailAddress(String agentEmailAddress) {
        this.agentEmailAddress = agentEmailAddress;
    }

    public String getAgentEmailAddress() {
        return agentEmailAddress;
    }

    public void setAgentContactPerson(String agentContactPerson) {
        this.agentContactPerson = agentContactPerson;
    }

    public String getAgentContactPerson() {
        return agentContactPerson;
    }


    public void setAgentDateCreated(Date agentDateCreated) {
        this.agentDateCreated = agentDateCreated;
    }

    public Date getAgentDateCreated() {
        return agentDateCreated;
    }


    public void setAgentAccountType(String agentAccountType) {
        this.agentAccountType = agentAccountType;
    }

    public String getAgentAccountType() {
        return agentAccountType;
    }

    public void setAgentAccountName(String AgentAccountName) {
        this.AgentAccountName = AgentAccountName;
    }

    public String getAgentAccountName() {
        return AgentAccountName;
    }

    public void setAgentTownName(String agentTownName) {
        this.agentTownName = agentTownName;
    }

    public String getAgentTownName() {
        return agentTownName;
    }

    public void setAgentRegCode(String agentRegCode) {
        this.agentRegCode = agentRegCode;
    }

    public String getAgentRegCode() {
        return agentRegCode;
    }

    public void setAgentTelNumber(String agentTelNumber) {
        this.agentTelNumber = agentTelNumber;
    }

    public String getAgentTelNumber() {
        return agentTelNumber;
    }

    public void setAgentFaxNumber(String agentFaxNumber) {
        this.agentFaxNumber = agentFaxNumber;
    }

    public String getAgentFaxNumber() {
        return agentFaxNumber;
    }

    public void setAgentCeckDate(Date agentCeckDate) {
        this.agentCeckDate = agentCeckDate;
    }

    public Date getAgentCeckDate() {
        return agentCeckDate;
    }

    public void setAgentAccountCode(String agentAccountCode) {
        this.agentAccountCode = agentAccountCode;
    }

    public String getAgentAccountCode() {
        return agentAccountCode;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchName() {
        return branchName;
    }
}

