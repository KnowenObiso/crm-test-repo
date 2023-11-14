package TurnQuest.view.client;

import java.sql.Date;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;


public class ClientLoadingTab implements SQLData {

    private String sql_type = "TQC_CLNT_LOADING_OBJ";
    private String cln_clnt_sht_desc;
    private String clientTownname;
    private String cln_clnt_name;
    private String cln_clnt_other_names;
    private String cln_clnt_physical_addrs;
    private String cln_clnt_postal_addrs;
    private String cln_clnt_tel;
    private String cln_clnt_tel2;
    private String faxNumber;
    private String emailAddress;
    private String cln_clnt_id_reg_no;
    private Date cln_clnt_dob;
    private String clientType;

    public void setCln_clnt_name(String cln_clnt_name) {
        this.cln_clnt_name = cln_clnt_name;
    }

    public String getCln_clnt_name() {
        return cln_clnt_name;
    }

    public void setCln_clnt_other_names(String cln_clnt_other_names) {
        this.cln_clnt_other_names = cln_clnt_other_names;
    }

    public String getCln_clnt_other_names() {
        return cln_clnt_other_names;
    }

    public void setCln_clnt_id_reg_no(String cln_clnt_id_reg_no) {
        this.cln_clnt_id_reg_no = cln_clnt_id_reg_no;
    }

    public String getCln_clnt_id_reg_no() {
        return cln_clnt_id_reg_no;
    }

    public void setCln_clnt_dob(Date cln_clnt_dob) {
        this.cln_clnt_dob = cln_clnt_dob;
    }

    public Date getCln_clnt_dob() {
        return cln_clnt_dob;
    }


    public void setCln_clnt_physical_addrs(String cln_clnt_physical_addrs) {
        this.cln_clnt_physical_addrs = cln_clnt_physical_addrs;
    }

    public String getCln_clnt_physical_addrs() {
        return cln_clnt_physical_addrs;
    }

    public void setCln_clnt_postal_addrs(String cln_clnt_postal_addrs) {
        this.cln_clnt_postal_addrs = cln_clnt_postal_addrs;
    }

    public String getCln_clnt_postal_addrs() {
        return cln_clnt_postal_addrs;
    }


    public void setCln_clnt_tel(String cln_clnt_tel) {
        this.cln_clnt_tel = cln_clnt_tel;
    }

    public String getCln_clnt_tel() {
        return cln_clnt_tel;
    }


    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        sql_type = typeName;
        cln_clnt_sht_desc = stream.readString();
        clientTownname = stream.readString();
        cln_clnt_name = stream.readString();
        cln_clnt_other_names = stream.readString();
        cln_clnt_physical_addrs = stream.readString();
        cln_clnt_postal_addrs = stream.readString();
        cln_clnt_tel = stream.readString();
        cln_clnt_tel2 = stream.readString();
        faxNumber = stream.readString();
        emailAddress = stream.readString();
        cln_clnt_id_reg_no = stream.readString();
        cln_clnt_dob = stream.readDate();
        clientType = stream.readString();

    }

    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeString(cln_clnt_sht_desc);
        stream.writeString(clientTownname);
        stream.writeString(cln_clnt_name);
        stream.writeString(cln_clnt_other_names);
        stream.writeString(cln_clnt_physical_addrs);
        stream.writeString(cln_clnt_postal_addrs);
        stream.writeString(cln_clnt_tel);
        stream.writeString(cln_clnt_tel2);
        stream.writeString(faxNumber);
        stream.writeString(emailAddress);
        stream.writeString(cln_clnt_id_reg_no);
        stream.writeDate(cln_clnt_dob);
        stream.writeString(clientType);

    }

    public String getSQLTypeName() {
        return sql_type;
    }


    public void setCln_clnt_sht_desc(String cln_clnt_sht_desc) {
        this.cln_clnt_sht_desc = cln_clnt_sht_desc;
    }

    public String getCln_clnt_sht_desc() {
        return cln_clnt_sht_desc;
    }

    public void setClientTownname(String clientTownname) {
        this.clientTownname = clientTownname;
    }

    public String getClientTownname() {
        return clientTownname;
    }

    public void setCln_clnt_tel2(String cln_clnt_tel2) {
        this.cln_clnt_tel2 = cln_clnt_tel2;
    }

    public String getCln_clnt_tel2() {
        return cln_clnt_tel2;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientType() {
        return clientType;
    }
}

