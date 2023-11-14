//-----------start entity's definition ExchangeRate --------------------//
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity @Table(name = "tqc_currency_rates", schema = "tq_crm") 
public class ExchangeRate implements Serializable {
	  @Id  
	  @Column(name = "crt_code", nullable = false)   
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ExchangeRateNoGen")
    @GenericGenerator(name = "ExchangeRateNoGen", strategy = "TurnQuest.view.models.ExchangeRateNoGen") 
    private BigDecimal code;
	  @Column(name = "crt_cur_code",  nullable = false) 
    private BigDecimal	curCode;

	  @Column(name = "crt_rate",  nullable = false) 
    private BigDecimal	rate;

	  @Column(name = "crt_date",  nullable = false) 
    private Timestamp	date;

	  @Column(name = "crt_base_cur_code",  nullable = true) 
    private BigDecimal	baseCurCode;

	  @Column(name = "crt_wef",  nullable = true) 
    private Timestamp	wef;

	  @Column(name = "crt_wet",  nullable = true) 
    private Timestamp	wet;

	  @Column(name = "crt_created_by", length = 50, nullable = true) 
    private String	createdBy;

	  @Column(name = "crt_updated_by", length = 50, nullable = true) 
    private String	updatedBy;

	  @Column(name = "crt_created_date",  nullable = true) 
    private Date	createdDate;

	  @Column(name = "crt_updated_date",  nullable = true) 
    private Date	updatedDate; 
	  
	@ManyToOne
	@JoinColumn(name="crt_cur_code", insertable=false, updatable=false)   
    private Currency      currency;

    public BigDecimal getCode() {
        return code;
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public BigDecimal getCurCode() {
        return curCode;
    }

    public void setCurCode(BigDecimal curCode) {
        this.curCode = curCode;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public BigDecimal getBaseCurCode() {
        return baseCurCode;
    }

    public void setBaseCurCode(BigDecimal baseCurCode) {
        this.baseCurCode = baseCurCode;
    }

    public Timestamp getWef() {
        return wef;
    }

    public void setWef(Timestamp wef) {
        this.wef = wef;
    }

    public Timestamp getWet() {
        return wet;
    }

    public void setWet(Timestamp wet) {
        this.wet = wet;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
    
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return "ExchangeRate [ code=" + code + ",curCode=" + curCode + ",rate=" + rate + ",date=" + date + ",baseCurCode=" + baseCurCode + ",wef=" + wef + ",wet=" + wet + ",createdBy=" + createdBy + ",updatedBy=" + updatedBy + ",createdDate=" + createdDate + ",updatedDate=" + updatedDate + ",currency=" + currency + " ]";
    }

   
}
//-----------end entity's definition ExchangeRate --------------------//