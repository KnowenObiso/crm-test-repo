package TurnQuest.view.Leads;

import java.math.BigDecimal;

import java.sql.Date;


public class LeadComment {
    private BigDecimal leadCommentCode;
    private String leadComment;
    private Date leadCommentDate;
    private String user_name;
    private String leadCommentDisposition;
    

    public void setLeadCommentCode(BigDecimal leadCommentCode) {
        this.leadCommentCode = leadCommentCode;
    }

    public BigDecimal getLeadCommentCode() {
        return leadCommentCode;
    }

    public void setLeadComment(String leadComment) {
        this.leadComment = leadComment;
    }

    public String getLeadComment() {
        return leadComment;
    }

    public void setLeadCommentDate(Date leadCommentDate) {
        this.leadCommentDate = leadCommentDate;
    }

    public Date getLeadCommentDate() {
        return leadCommentDate;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setLeadCommentDisposition(String leadCommentDisposition) {
        this.leadCommentDisposition = leadCommentDisposition;
    }

    public String getLeadCommentDisposition() {
        return leadCommentDisposition;
    }
}
