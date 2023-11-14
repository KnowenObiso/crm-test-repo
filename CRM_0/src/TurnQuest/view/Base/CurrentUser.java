package TurnQuest.view.Base;

import java.math.BigDecimal;

import java.util.Date;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;


public class CurrentUser {
    private BigDecimal UserCode;
    private String Username;
    private Date LoginDate;

    public CurrentUser() {
    }
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);


    public BigDecimal getUserCode() {
        return (BigDecimal)session.getAttribute("UserCode");
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getUsername() {
        return (String)session.getAttribute("Username");
    }


    public void setLoginDate(Date LoginDate) {
        this.LoginDate = LoginDate;
    }

    public Date getLoginDate() {
        return (Date)session.getAttribute("LoginDate");
    }


    public void setUserCode(BigDecimal UserCode) {
        this.UserCode = UserCode;
    }
    
}
