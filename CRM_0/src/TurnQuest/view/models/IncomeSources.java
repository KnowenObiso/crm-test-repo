package TurnQuest.view.models;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
@Entity @Table(name = "tqc_client_income_source", schema = "tq_crm") 
public class IncomeSources implements Serializable {
    @Id
    @Column(name = "income_code", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "ClientIncomeSourcesNoGen")
    @GenericGenerator(name = "ClientIncomeSourcesNoGen",
                      strategy = "TurnQuest.view.models.ClientIncomeSourcesNoGen")
    private BigDecimal code;
    @Column(name = "income_clnt_code", nullable = true)
    private BigDecimal clientCode;

    @Column(name = "income_name", nullable = true)
    private String incomeName;

    @Column(name = "income_type", nullable = true)
    private String incomeType;

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public BigDecimal getCode() {
        return code;
    }

    public void setClientCode(BigDecimal clientCode) {
        this.clientCode = clientCode;
    }

    public BigDecimal getClientCode() {
        return clientCode;
    }

    public void setIncomeName(String incomeName) {
        this.incomeName = incomeName;
    }

    public String getIncomeName() {
        return incomeName;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }

    public String getIncomeType() {
        return incomeType;
    }

    @Override
    public String toString() {
        return "IncomeSources [ code=" + code + ",clientCode=" + clientCode +
            ",incomeName=" + incomeName + ",incomeType=" + incomeType + " ]";
    }
}
