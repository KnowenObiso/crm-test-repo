package TurnQuest.view.dao;

public class EcmBean {
    private String name;
    private String modifiedBy;
    private String dateCreated;
    private String versionLabel;
    private String id;
    private String mimeType;
    private String actualName;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateCreated() {
        return dateCreated;
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

    public void setActualName(String actualName) {
        this.actualName = actualName;
    }

    public String getActualName() {
        return actualName;
    }
}
