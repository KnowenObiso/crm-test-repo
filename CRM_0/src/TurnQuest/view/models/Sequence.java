package TurnQuest.view.models;

import java.math.BigDecimal;

public class Sequence {
    private BigDecimal tseq_code;
    private String tseq_type;
    private String tseq_no_type;
    private BigDecimal tseq_brn;
    private BigDecimal tseq_uwyr;
    private BigDecimal tseq_next_no;
    private String branch_name;
    private BigDecimal org_code;
    private String clientType;

    public Sequence() {
        super();
    }


    public void setTseq_code(BigDecimal tseq_code) {
        this.tseq_code = tseq_code;
    }

    public BigDecimal getTseq_code() {
        return tseq_code;
    }

    public void setTseq_type(String tseq_type) {
        this.tseq_type = tseq_type;
    }

    public String getTseq_type() {
        return tseq_type;
    }

    public void setTseq_no_type(String tseq_no_type) {
        this.tseq_no_type = tseq_no_type;
    }

    public String getTseq_no_type() {
        return tseq_no_type;
    }

    public void setTseq_brn(BigDecimal tseq_brn) {
        this.tseq_brn = tseq_brn;
    }

    public BigDecimal getTseq_brn() {
        return tseq_brn;
    }

    public void setTseq_uwyr(BigDecimal tseq_uwyr) {
        this.tseq_uwyr = tseq_uwyr;
    }

    public BigDecimal getTseq_uwyr() {
        return tseq_uwyr;
    }

    public void setTseq_next_no(BigDecimal tseq_next_no) {
        this.tseq_next_no = tseq_next_no;
    }

    public BigDecimal getTseq_next_no() {
        return tseq_next_no;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setOrg_code(BigDecimal org_code) {
        this.org_code = org_code;
    }

    public BigDecimal getOrg_code() {
        return org_code;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientType() {
        return clientType;
    }
}
