package TurnQuest.view.ecm;


public class EcmBean {
    public EcmBean() {
        super();
    }
    private String name;
    private String actualName;
    private String modifiedBy;
    private String versionLabel;
    private String id;
    private String mimeType;
    private String dateCreated;
    private String rdCode;
    private String rdDesc;
    
    //Agent commission
    private String comDocName;
    private String comDocActualName;
    private String comDocModifiedBy;
    private String comDocVersionLabel;
    private String comDocId;
    private String comDocMimeType;
    private String comDocDateCreated;

    public void setActualName(String actualName) {
        this.actualName = actualName;
    }

    public String getActualName() {
        return actualName;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setVersionLabel(String versionLabel) {
        this.versionLabel = versionLabel;
    }

    public String getVersionLabel() {
        return versionLabel;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setComDocName(String comDocName) {
        this.comDocName = comDocName;
    }

    public String getComDocName() {
        return comDocName;
    }

    public void setComDocActualName(String comDocActualName) {
        this.comDocActualName = comDocActualName;
    }

    public String getComDocActualName() {
        return comDocActualName;
    }

    public void setComDocModifiedBy(String comDocModifiedBy) {
        this.comDocModifiedBy = comDocModifiedBy;
    }

    public String getComDocModifiedBy() {
        return comDocModifiedBy;
    }

    public void setComDocVersionLabel(String comDocVersionLabel) {
        this.comDocVersionLabel = comDocVersionLabel;
    }

    public String getComDocVersionLabel() {
        return comDocVersionLabel;
    }

    public void setComDocId(String comDocId) {
        this.comDocId = comDocId;
    }

    public String getComDocId() {
        return comDocId;
    }

    public void setComDocMimeType(String comDocMimeType) {
        this.comDocMimeType = comDocMimeType;
    }

    public String getComDocMimeType() {
        return comDocMimeType;
    }

    public void setComDocDateCreated(String comDocDateCreated) {
        this.comDocDateCreated = comDocDateCreated;
    }

    public String getComDocDateCreated() {
        return comDocDateCreated;
    }

    public void setRdCode(String rdCode) {
        this.rdCode = rdCode;
    }

    public String getRdCode() {
        return rdCode;
    }

    public void setRdDesc(String rdDesc) {
        this.rdDesc = rdDesc;
    }

    public String getRdDesc() {
        return rdDesc;
    }
}
