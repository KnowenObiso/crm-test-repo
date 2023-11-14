package TurnQuest.view.setups;

import java.io.Serializable;

public class BankTerritory implements Serializable {
    private String bankTerritoryName;
    private String bankTerritoryCode;
    private String bankTerritoryShtDesc;
    private String territoryBankCode;
    
    public BankTerritory() {
        super();
    }

    public void setBankTerritoryName(String bankTerritoryName) {
        this.bankTerritoryName = bankTerritoryName;
    }

    public String getBankTerritoryName() {
        return bankTerritoryName;
    }

    public void setBankTerritoryCode(String bankTerritoryCode) {
        this.bankTerritoryCode = bankTerritoryCode;
    }

    public String getBankTerritoryCode() {
        return bankTerritoryCode;
    }

    public void setBankTerritoryShtDesc(String bankTerritoryShtDesc) {
        this.bankTerritoryShtDesc = bankTerritoryShtDesc;
    }

    public String getBankTerritoryShtDesc() {
        return bankTerritoryShtDesc;
    }

    public void setTerritoryBankCode(String territoryBankCode) {
        this.territoryBankCode = territoryBankCode;
    }

    public String getTerritoryBankCode() {
        return territoryBankCode;
    }
}
