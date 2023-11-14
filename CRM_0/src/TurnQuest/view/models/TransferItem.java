//-----------start entity's definition TransferItem --------------------//
//-----------@author dancan kavagi --------------------//
package TurnQuest.view.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Blob;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity @Table(name = "tqc_transfers_items", schema = "tq_crm") 
public class TransferItem implements Serializable {
	  @Id  
	  @Column(name = "tti_code", nullable = false)   
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TransferItemNoGen")
    @GenericGenerator(name = "TransferItemNoGen", strategy = "TurnQuest.view.models.TransferItemNoGen") 
    private BigDecimal code;
	  @Column(name = "tti_tt_code", length = 32, nullable = false) 
    private BigDecimal	ttCode;

	  @Column(name = "tti_item_code", length = 22, nullable = false) 
    private BigDecimal	itemCode;

	  @Column(name = "tti_item_name", length = 80, nullable = false) 
    private String	itemName;

	  @Column(name = "tti_item_sht_desc", length = 80, nullable = false) 
    private String	itemShtDesc;

	  @Column(name = "tti_item_type", length = 3, nullable = false) 
    private String	itemType;

	  @Column(name = "tti_trans_date",  nullable = true) 
    private Date	transDate;

	  @Column(name = "tti_trans_to", length = 22, nullable = true) 
    private BigDecimal	transTo;

	  @Column(name = "tti_trans_from", length = 22, nullable = true) 
    private BigDecimal	transFrom;

	  @Column(name = "tti_done_by", length = 80, nullable = true) 
    private String	doneBy;

	  @Column(name = "tti_done_date",  nullable = true) 
    private Date	doneDate ;

	  @Column(name = "tti_authorized", length = 80, nullable = true) 
    private String	authorized;

	  @Column(name = "tti_authorized_by", length = 80, nullable = true) 
    private String	authorizedBy;

	  @Column(name = "tti_authorized_date",  nullable = true) 
    private Date	authorizedDate;



    public BigDecimal getCode() {
        return code;
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public BigDecimal getTtCode() {
        return ttCode;
    }

    public void setTtCode(BigDecimal ttCode) {
        this.ttCode = ttCode;
    }

    public BigDecimal getItemCode() {
        return itemCode;
    }

    public void setItemCode(BigDecimal itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemShtDesc() {
        return itemShtDesc;
    }

    public void setItemShtDesc(String itemShtDesc) {
        this.itemShtDesc = itemShtDesc;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public BigDecimal getTransTo() {
        return transTo;
    }

    public void setTransTo(BigDecimal transTo) {
        this.transTo = transTo;
    }

    public BigDecimal getTransFrom() {
        return transFrom;
    }

    public void setTransFrom(BigDecimal transFrom) {
        this.transFrom = transFrom;
    }

    public String getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(String doneBy) {
        this.doneBy = doneBy;
    }

    public Date getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(Date doneDate) {
        this.doneDate = doneDate;
    }

    public String getAuthorized() {
        return authorized;
    }

    public void setAuthorized(String authorized) {
        this.authorized = authorized;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public Date getAuthorizedDate() {
        return authorizedDate;
    }

    public void setAuthorizedDate(Date authorizedDate) {
        this.authorizedDate = authorizedDate;
    }


    @Override
    public String toString() {
        return "TransferItem [ code=" + code + ",ttCode=" + ttCode + ",itemCode=" + itemCode + ",itemName=" + itemName + ",itemShtDesc=" + itemShtDesc + ",itemType=" + itemType + ",transDate=" + transDate + ",transTo=" + transTo + ",transFrom=" + transFrom + ",doneBy=" + doneBy + ",doneDate=" + doneDate + ",authorized=" + authorized + ",authorizedBy=" + authorizedBy + ",authorizedDate=" + authorizedDate + " ]";
    }
}
//-----------end entity's definition TransferItem --------------------//