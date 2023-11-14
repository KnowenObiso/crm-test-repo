package TurnQuest.view.setups;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Date;


public class Departments implements Serializable {

    private BigDecimal depCode;
    private String depShtDesc;
    private String depName;
    private Date depWef;
    private Date depWet;
    private BigDecimal depUsrCode;
    private String userName;
    private String usersName;

    public Departments() {
        super();
    }


    public void setDepCode(BigDecimal depCode) {
        this.depCode = depCode;
    }

    public BigDecimal getDepCode() {
        return depCode;
    }

    public void setDepShtDesc(String depShtDesc) {
        this.depShtDesc = depShtDesc;
    }

    public String getDepShtDesc() {
        return depShtDesc;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepWef(Date depWef) {
        this.depWef = depWef;
    }

    public Date getDepWef() {
        return depWef;
    }

    public void setDepWet(Date depWet) {
        this.depWet = depWet;
    }

    public Date getDepWet() {
        return depWet;
    }

    public void setDepUsrCode(BigDecimal depUsrCode) {
        this.depUsrCode = depUsrCode;
    }

    public BigDecimal getDepUsrCode() {
        return depUsrCode;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUsersName(String usersName) {
        this.usersName = usersName;
    }

    public String getUsersName() {
        return usersName;
    }
}
