package TurnQuest.view.Activities;

import java.io.InputStream;

import java.math.BigDecimal;


public class Note {
    private BigDecimal noteCode;
    private BigDecimal noteAcccountCode;
    private BigDecimal noteContactCode;
    private String noteSubject;
    private String noteDescription;
    private BigDecimal noteActivityCode;
    private String relatedAccount;
    private String noteAttachmentName;
    private String noteAttachmentType;
    private InputStream noteAttachmentFile;

    public void setNoteSubject(String noteSubject) {
        this.noteSubject = noteSubject;
    }

    public String getNoteSubject() {
        return noteSubject;
    }

    public void setRelatedAccount(String relatedAccount) {
        this.relatedAccount = relatedAccount;
    }

    public String getRelatedAccount() {
        return relatedAccount;
    }

    public void setNoteCode(BigDecimal noteCode) {
        this.noteCode = noteCode;
    }

    public BigDecimal getNoteCode() {
        return noteCode;
    }

    public void setNoteAcccountCode(BigDecimal noteAcccountCode) {
        this.noteAcccountCode = noteAcccountCode;
    }

    public BigDecimal getNoteAcccountCode() {
        return noteAcccountCode;
    }

    public void setNoteContactCode(BigDecimal noteContactCode) {
        this.noteContactCode = noteContactCode;
    }

    public BigDecimal getNoteContactCode() {
        return noteContactCode;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteActivityCode(BigDecimal noteActivityCode) {
        this.noteActivityCode = noteActivityCode;
    }

    public BigDecimal getNoteActivityCode() {
        return noteActivityCode;
    }

    public void setNoteAttachmentName(String noteAttachmentName) {
        this.noteAttachmentName = noteAttachmentName;
    }

    public String getNoteAttachmentName() {
        return noteAttachmentName;
    }

    public void setNoteAttachmentType(String noteAttachmentType) {
        this.noteAttachmentType = noteAttachmentType;
    }

    public String getNoteAttachmentType() {
        return noteAttachmentType;
    }

    public void setNoteAttachmentFile(InputStream noteAttachmentFile) {
        this.noteAttachmentFile = noteAttachmentFile;
    }

    public InputStream getNoteAttachmentFile() {
        return noteAttachmentFile;
    }
}
