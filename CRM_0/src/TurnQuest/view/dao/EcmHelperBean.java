package TurnQuest.view.dao;


public class EcmHelperBean {

    private String reportcode;
    private String pkey;
    private String reportname;
    private String aspect;
    private String docType;
    private String sdtcode;

    public EcmHelperBean(String reportcode, String pkey, String reportname,
                         String aspect, String docType, String sdtcode) {
        super();
        this.reportcode = reportcode;
        this.pkey = pkey;
        this.reportname = reportname;
        this.aspect = aspect;
        this.docType = docType;
        this.sdtcode = sdtcode;
    }


    public void setReportcode(String reportcode) {
        this.reportcode = reportcode;
    }

    public String getReportcode() {
        return reportcode;
    }

    public void setPkey(String pkey) {
        this.pkey = pkey;
    }

    public String getPkey() {
        return pkey;
    }

    public void setReportname(String reportname) {
        this.reportname = reportname;
    }

    public String getReportname() {
        return reportname;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public String getAspect() {
        return aspect;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocType() {
        return docType;
    }

    public void setSdtcode(String sdtcode) {
        this.sdtcode = sdtcode;
    }

    public String getSdtcode() {
        return sdtcode;
    }
}
