//-----------start entity's definition SourceOfIncome --------------------//
//-----------@author dancan kavagi --------------------//
package TurnQuest.view.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Blob;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity @Table(name = "tqc_income_sources", schema = "tq_crm") 
public class SourceOfIncome implements Serializable {
	  @Id  
	  @Column(name = "income_code", nullable = false)   
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SourceOfIncomeNoGen")
    @GenericGenerator(name = "SourceOfIncomeNoGen", strategy = "TurnQuest.view.models.SourceOfIncomeNoGen") 
    private BigDecimal code;
	  @Column(name = "income_name", length = 100, nullable = true) 
    private String	name;

	  @Column(name = "income_sht_desc", length = 100, nullable = true) 
    private String	shtDesc;



    public BigDecimal getCode() {
        return code;
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShtDesc() {
        return shtDesc;
    }

    public void setShtDesc(String shtDesc) {
        this.shtDesc = shtDesc;
    }


    @Override
    public String toString() {
        return "SourceOfIncome [ code=" + code + ",name=" + name + ",shtDesc=" + shtDesc + " ]";
    }
}
//-----------end entity's definition SourceOfIncome --------------------//
