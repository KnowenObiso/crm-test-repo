/* Start Entity's definition ClientType 
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

@Entity @Table(name = "tqc_client_types", schema = "TQ_CRM") 
public class ClientType implements Serializable {
	  @Id  
	  @Column(name = "clnty_code", nullable = false)   
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ClientTypeNoGen")
    @GenericGenerator(name = "ClientTypeNoGen", strategy = "TurnQuest.view.models.ClientTypeNoGen") 
    private BigDecimal code;
	  @Column(name = "clnty_clnt_type", length = 200, nullable = true) 
    private String	clntType;

	  @Column(name = "clnty_category", length = 40, nullable = true) 
    private String	category;

	  @Column(name = "clnty_type", length = 100, nullable = false) 
    private String	type;

	  @Column(name = "clnty_person", length = 1, nullable = true) 
    private String	person;

	  @Column(name = "clnty_desc", length = 100, nullable = true) 
    private String	desc;



    public BigDecimal getCode() {
        return code;
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public String getClntType() {
        return clntType;
    }

    public void setClntType(String clntType) {
        this.clntType = clntType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    @Override
    public String toString() {
        return "ClientType [ code=" + code + ",clntType=" + clntType + ",category=" + category + ",type=" + type + ",person=" + person + ",desc=" + desc + " ]";
    }
}
/* end entity's definition ClientType */