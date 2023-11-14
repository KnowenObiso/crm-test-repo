package TurnQuest.view.setups;

import java.math.BigDecimal;

public class PrintServer {
    private BigDecimal code;
    private String name;
    private String filter;
    private String uri;
    private String filter_command;
    private String sec_username;
    private String sec_password;
    private String sec_auth_type;
    private String sec_encrpt_type;
    private String proxy_host;
    private String proxy_port;
    private String proxy_username;
    private String proxy_pasword;
    private String proxy_authen_type;

    public PrintServer() {
        super();
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public BigDecimal getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return filter;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setFilter_command(String filter_command) {
        this.filter_command = filter_command;
    }

    public String getFilter_command() {
        return filter_command;
    }

    public void setSec_username(String sec_username) {
        this.sec_username = sec_username;
    }

    public String getSec_username() {
        return sec_username;
    }

    public void setSec_password(String sec_password) {
        this.sec_password = sec_password;
    }

    public String getSec_password() {
        return sec_password;
    }

    public void setSec_auth_type(String sec_auth_type) {
        this.sec_auth_type = sec_auth_type;
    }

    public String getSec_auth_type() {
        return sec_auth_type;
    }

    public void setSec_encrpt_type(String sec_encrpt_type) {
        this.sec_encrpt_type = sec_encrpt_type;
    }

    public String getSec_encrpt_type() {
        return sec_encrpt_type;
    }

    public void setProxy_host(String proxy_host) {
        this.proxy_host = proxy_host;
    }

    public String getProxy_host() {
        return proxy_host;
    }

    public void setProxy_port(String proxy_port) {
        this.proxy_port = proxy_port;
    }

    public String getProxy_port() {
        return proxy_port;
    }

    public void setProxy_username(String proxy_username) {
        this.proxy_username = proxy_username;
    }

    public String getProxy_username() {
        return proxy_username;
    }

    public void setProxy_pasword(String proxy_pasword) {
        this.proxy_pasword = proxy_pasword;
    }

    public String getProxy_pasword() {
        return proxy_pasword;
    }

    public void setProxy_authen_type(String proxy_authen_type) {
        this.proxy_authen_type = proxy_authen_type;
    }

    public String getProxy_authen_type() {
        return proxy_authen_type;
    }
}
