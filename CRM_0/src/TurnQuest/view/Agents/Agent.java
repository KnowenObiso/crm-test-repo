package TurnQuest.view.Agents;

import java.math.BigDecimal;

public class Agent {
    
    private BigDecimal AGNTY_CODE; 
    private String AGNTY_TYPE_SHT_DESC;
    private String AGNTY_TYPE;
    private BigDecimal AGNTY_ACT_CODE;
    private String ACT_ACCOUNT_TYPE;
    
    
    
    private BigDecimal  fa_agn_org_code;
    private String fa_agn_sht_desc;                
    private BigDecimal fa_team_code;
    private String fa_agn_name;
    private BigDecimal fa_agent_code;
    
    
    
    public Agent() {
        super();
    }

    public void setAGNTY_CODE(BigDecimal AGNTY_CODE) {
        this.AGNTY_CODE = AGNTY_CODE;
    }

    public BigDecimal getAGNTY_CODE() {
        return AGNTY_CODE;
    }

    public void setAGNTY_TYPE_SHT_DESC(String AGNTY_TYPE_SHT_DESC) {
        this.AGNTY_TYPE_SHT_DESC = AGNTY_TYPE_SHT_DESC;
    }

    public String getAGNTY_TYPE_SHT_DESC() {
        return AGNTY_TYPE_SHT_DESC;
    }

    public void setAGNTY_TYPE(String AGNTY_TYPE) {
        this.AGNTY_TYPE = AGNTY_TYPE;
    }

    public String getAGNTY_TYPE() {
        return AGNTY_TYPE;
    }

    public void setAGNTY_ACT_CODE(BigDecimal AGNTY_ACT_CODE) {
        this.AGNTY_ACT_CODE = AGNTY_ACT_CODE;
    }

    public BigDecimal getAGNTY_ACT_CODE() {
        return AGNTY_ACT_CODE;
    }

    public void setACT_ACCOUNT_TYPE(String ACT_ACCOUNT_TYPE) {
        this.ACT_ACCOUNT_TYPE = ACT_ACCOUNT_TYPE;
    }

    public String getACT_ACCOUNT_TYPE() {
        return ACT_ACCOUNT_TYPE;
    }

    public void setFa_agn_org_code(BigDecimal fa_agn_org_code) {
        this.fa_agn_org_code = fa_agn_org_code;
    }

    public BigDecimal getFa_agn_org_code() {
        return fa_agn_org_code;
    }

    public void setFa_agn_sht_desc(String fa_agn_sht_desc) {
        this.fa_agn_sht_desc = fa_agn_sht_desc;
    }

    public String getFa_agn_sht_desc() {
        return fa_agn_sht_desc;
    }

    public void setFa_team_code(BigDecimal fa_team_code) {
        this.fa_team_code = fa_team_code;
    }

    public BigDecimal getFa_team_code() {
        return fa_team_code;
    }

    public void setFa_agn_name(String fa_agn_name) {
        this.fa_agn_name = fa_agn_name;
    }

    public String getFa_agn_name() {
        return fa_agn_name;
    }

    public void setFa_agent_code(BigDecimal fa_agent_code) {
        this.fa_agent_code = fa_agent_code;
    }

    public BigDecimal getFa_agent_code() {
        return fa_agent_code;
    }
}
