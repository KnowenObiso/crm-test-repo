package TurnQuest.view.client;

import java.math.BigDecimal;

public class ClientEmployerContact {
    
    private BigDecimal code;
    private BigDecimal clntCode;
    
    private BigDecimal employerCode;
    private String employerName;
    
    private String name;
    private String title;
    private String phoneNumber;
    private String emailAddress;
    
    public ClientEmployerContact() {
        super();
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public BigDecimal getCode() {
        return code;
    }

    public void setClntCode(BigDecimal clntCode) {
        this.clntCode = clntCode;
    }

    public BigDecimal getClntCode() {
        return clntCode;
    }

    public void setEmployerCode(BigDecimal employerCode) {
        this.employerCode = employerCode;
    }

    public BigDecimal getEmployerCode() {
        return employerCode;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}
