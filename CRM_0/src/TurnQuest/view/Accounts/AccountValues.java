package TurnQuest.view.Accounts;

import java.math.BigDecimal;

public class AccountValues {

    public AccountValues() {
        super();
    }
    private BigDecimal AccCode;
    private String id;
    private String Name;
    private String Type;

    public void setAccCode(BigDecimal AccCode) {
        this.AccCode = AccCode;
    }

    public BigDecimal getAccCode() {
        return AccCode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getType() {
        return Type;
    }
}
