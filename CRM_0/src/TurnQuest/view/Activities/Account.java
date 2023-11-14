package TurnQuest.view.Activities;

import java.math.BigDecimal;

public class Account {
    private BigDecimal accountCode;
    private String accountType;
    private BigDecimal accountTypeCode;
    private String accountName;
    private String accountEmail;


    public void setAccountCode(BigDecimal accountCode) {
        this.accountCode = accountCode;
    }

    public BigDecimal getAccountCode() {
        return accountCode;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountTypeCode(BigDecimal accountTypeCode) {
        this.accountTypeCode = accountTypeCode;
    }

    public BigDecimal getAccountTypeCode() {
        return accountTypeCode;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAccountEmail() {
        return accountEmail;
    }


}
