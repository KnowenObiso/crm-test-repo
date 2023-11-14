package TurnQuest.view.models;
 

//-----------start entity's definition Currency --------------------//
//-----------@author dancan kavagi --------------------//

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

@Entity @Table(name = "tqc_currencies", schema = "tq_crm") 
public class Currency implements Serializable {
	  @Id  
	  @Column(name = "cur_code", nullable = false)   
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CurrencyNoGen")
  @GenericGenerator(name = "CurrencyNoGen", strategy = "TurnQuest.view.models.CurrencyNoGen") 
  private BigDecimal code;
	  @Column(name = "cur_symbol", length = 15, nullable = false) 
  private String	symbol;

	  @Column(name = "cur_desc", length = 80, nullable = false) 
  private String	desc;

	  @Column(name = "cur_rnd",  nullable = true) 
  private BigDecimal	rnd;

	  @Column(name = "cur_num_word", length = 50, nullable = true) 
  private String	numWord;

	  @Column(name = "cur_decimal_word", length = 50, nullable = true) 
  private String	decimalWord;



  public BigDecimal getCode() {
      return code;
  }

  public void setCode(BigDecimal code) {
      this.code = code;
  }

  public String getSymbol() {
      return symbol;
  }

  public void setSymbol(String symbol) {
      this.symbol = symbol;
  }

  public String getDesc() {
      return desc;
  }

  public void setDesc(String desc) {
      this.desc = desc;
  }

  public BigDecimal getRnd() {
      return rnd;
  }

  public void setRnd(BigDecimal rnd) {
      this.rnd = rnd;
  }

  public String getNumWord() {
      return numWord;
  }

  public void setNumWord(String numWord) {
      this.numWord = numWord;
  }

  public String getDecimalWord() {
      return decimalWord;
  }

  public void setDecimalWord(String decimalWord) {
      this.decimalWord = decimalWord;
  }


  @Override
  public String toString() {
      return "Currency [ code=" + code + ",symbol=" + symbol + ",desc=" + desc + ",rnd=" + rnd + ",numWord=" + numWord + ",decimalWord=" + decimalWord + " ]";
  }
}
//-----------end entity's definition Currency --------------------//