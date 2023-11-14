package TurnQuest.view.models;


import java.math.BigDecimal;

import java.util.Date;


public class Alert {
    private BigDecimal code;
    private BigDecimal alrt_code;
    private BigDecimal sys_code;
    private BigDecimal agn_code;
    private BigDecimal clnt_code;
    private String description;
    private Date date;
    private BigDecimal period;
    private BigDecimal user_code;
    private String dest_type;
    private BigDecimal dest_code;
    private BigDecimal msgt_code;
    private String agency_name;
    private String template_sht_desc;
    private String acc_name;
    private String SQLTypeName;
    private String status;
    private String short_desc;


    public void setSQLTypeName(String SQLTypeName) {
        this.SQLTypeName = SQLTypeName;
    }

    public String getSQLTypeName() {
        return SQLTypeName;
    }

    public Alert() {
        super();
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public BigDecimal getCode() {
        return code;
    }

    public void setAlrt_code(BigDecimal alrt_code) {
        this.alrt_code = alrt_code;
    }

    public BigDecimal getAlrt_code() {
        return alrt_code;
    }

    public void setSys_code(BigDecimal sys_code) {
        this.sys_code = sys_code;
    }

    public BigDecimal getSys_code() {
        return sys_code;
    }

    public void setAgn_code(BigDecimal agn_code) {
        this.agn_code = agn_code;
    }

    public BigDecimal getAgn_code() {
        return agn_code;
    }

    public void setClnt_code(BigDecimal clnt_code) {
        this.clnt_code = clnt_code;
    }

    public BigDecimal getClnt_code() {
        return clnt_code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setPeriod(BigDecimal period) {
        this.period = period;
    }

    public BigDecimal getPeriod() {
        return period;
    }

    public void setUser_code(BigDecimal user_code) {
        this.user_code = user_code;
    }

    public BigDecimal getUser_code() {
        return user_code;
    }

    public void setDest_type(String dest_type) {
        this.dest_type = dest_type;
    }

    public String getDest_type() {
        return dest_type;
    }

    public void setDest_code(BigDecimal dest_code) {
        this.dest_code = dest_code;
    }

    public BigDecimal getDest_code() {
        return dest_code;
    }

    public void setMsgt_code(BigDecimal msgt_code) {
        this.msgt_code = msgt_code;
    }

    public BigDecimal getMsgt_code() {
        return msgt_code;
    }

    public void setAgency_name(String agency_name) {
        this.agency_name = agency_name;
    }

    public String getAgency_name() {
        return agency_name;
    }

    public void setTemplate_sht_desc(String template_sht_desc) {
        this.template_sht_desc = template_sht_desc;
    }

    public String getTemplate_sht_desc() {
        return template_sht_desc;
    }

    public void setAcc_name(String acc_name) {
        this.acc_name = acc_name;
    }

    public String getAcc_name() {
        return acc_name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public String getShort_desc() {
        return short_desc;
    }
}
