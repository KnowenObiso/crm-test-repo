/* Start Entity's definition AccountType 
 @author dancan kavagi  */
package TurnQuest.view.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Blob;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity @Table(name = "tqc_account_types", schema = "TQ_CRM") 
public class AccountType implements Serializable {
	  @Id  
	  @Column(name = "act_code", nullable = false)   
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AccountTypeNoGen")
    @GenericGenerator(name = "AccountTypeNoGen", strategy = "TurnQuest.view.models.AccountTypeNoGen") 
    private BigDecimal code;
	  @Column(name = "act_account_type", length = 50, nullable = true) 
    private String	accountType;

	  @Column(name = "act_type_sht_desc", length = 60, nullable = true) 
    private String	typeShtDesc;

	  @Column(name = "act_wthtx_rate", length = 22, nullable = true) 
    private BigDecimal	wthtxRate;

	  @Column(name = "act_type_id", length = 2, nullable = false) 
    private String	typeId;

	  @Column(name = "act_comm_reserve_rate", length = 22, nullable = true) 
    private BigDecimal	commReserveRate;

	  @Column(name = "act_max_adv_amt", length = 22, nullable = true) 
    private BigDecimal	maxAdvAmt;

	  @Column(name = "act_max_adv_rpymt_prd", length = 22, nullable = true) 
    private BigDecimal	maxAdvRpymtPrd;

	  @Column(name = "act_rcpts_include_comm", length = 1, nullable = true) 
    private String	rcptsIncludeComm;

	  @Column(name = "act_overide_rate", length = 22, nullable = true) 
    private BigDecimal	overideRate;

	  @Column(name = "act_id_serial_format", length = 60, nullable = true) 
    private String	idSerialFormat;

	  @Column(name = "act_vat_rate", length = 22, nullable = true) 
    private BigDecimal	vatRate;

	  @Column(name = "act_format", length = 100, nullable = true) 
    private String	format;

	  @Column(name = "act_odl_code", length = 10, nullable = true) 
    private String	odlCode;

	  @Column(name = "act_no_gen_code", length = 15, nullable = true) 
    private String	noGenCode;

	  @Column(name = "act_commision_levy_rate", length = 22, nullable = true) 
    private BigDecimal	commisionLevyRate;

	  @Column(name = "act_suffix", length = 20, nullable = true) 
    private String	suffix;

	  @Column(name = "act_next_no", length = 22, nullable = true) 
    private BigDecimal	nextNo;

	  @Column(name = "act_payment_detail", length = 22, nullable = true) 
    private BigDecimal	paymentDetail;



    public BigDecimal getCode() {
        return code;
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getTypeShtDesc() {
        return typeShtDesc;
    }

    public void setTypeShtDesc(String typeShtDesc) {
        this.typeShtDesc = typeShtDesc;
    }

    public BigDecimal getWthtxRate() {
        return wthtxRate;
    }

    public void setWthtxRate(BigDecimal wthtxRate) {
        this.wthtxRate = wthtxRate;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public BigDecimal getCommReserveRate() {
        return commReserveRate;
    }

    public void setCommReserveRate(BigDecimal commReserveRate) {
        this.commReserveRate = commReserveRate;
    }

    public BigDecimal getMaxAdvAmt() {
        return maxAdvAmt;
    }

    public void setMaxAdvAmt(BigDecimal maxAdvAmt) {
        this.maxAdvAmt = maxAdvAmt;
    }

    public BigDecimal getMaxAdvRpymtPrd() {
        return maxAdvRpymtPrd;
    }

    public void setMaxAdvRpymtPrd(BigDecimal maxAdvRpymtPrd) {
        this.maxAdvRpymtPrd = maxAdvRpymtPrd;
    }

    public String getRcptsIncludeComm() {
        return rcptsIncludeComm;
    }

    public void setRcptsIncludeComm(String rcptsIncludeComm) {
        this.rcptsIncludeComm = rcptsIncludeComm;
    }

    public BigDecimal getOverideRate() {
        return overideRate;
    }

    public void setOverideRate(BigDecimal overideRate) {
        this.overideRate = overideRate;
    }

    public String getIdSerialFormat() {
        return idSerialFormat;
    }

    public void setIdSerialFormat(String idSerialFormat) {
        this.idSerialFormat = idSerialFormat;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getOdlCode() {
        return odlCode;
    }

    public void setOdlCode(String odlCode) {
        this.odlCode = odlCode;
    }

    public String getNoGenCode() {
        return noGenCode;
    }

    public void setNoGenCode(String noGenCode) {
        this.noGenCode = noGenCode;
    }

    public BigDecimal getCommisionLevyRate() {
        return commisionLevyRate;
    }

    public void setCommisionLevyRate(BigDecimal commisionLevyRate) {
        this.commisionLevyRate = commisionLevyRate;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public BigDecimal getNextNo() {
        return nextNo;
    }

    public void setNextNo(BigDecimal nextNo) {
        this.nextNo = nextNo;
    }

    public BigDecimal getPaymentDetail() {
        return paymentDetail;
    }

    public void setPaymentDetail(BigDecimal paymentDetail) {
        this.paymentDetail = paymentDetail;
    }


    @Override
    public String toString() {
        return "AccountType [ code=" + code + ",accountType=" + accountType + ",typeShtDesc=" + typeShtDesc + ",wthtxRate=" + wthtxRate + ",typeId=" + typeId + ",commReserveRate=" + commReserveRate + ",maxAdvAmt=" + maxAdvAmt + ",maxAdvRpymtPrd=" + maxAdvRpymtPrd + ",rcptsIncludeComm=" + rcptsIncludeComm + ",overideRate=" + overideRate + ",idSerialFormat=" + idSerialFormat + ",vatRate=" + vatRate + ",format=" + format + ",odlCode=" + odlCode + ",noGenCode=" + noGenCode + ",commisionLevyRate=" + commisionLevyRate + ",suffix=" + suffix + ",nextNo=" + nextNo + ",paymentDetail=" + paymentDetail + " ]";
    }
}
/* end entity's definition AccountType */