package TurnQuest.view.setups;

public class District {
    /* Bean Properties */
    private String code;
    private String countryCode;
    private String shortDesc;
    private String name;
    private String STSCode;

    /**
     * Default Constructor
     */
    public District() {
        super();
    }

    /* Bean Methods */

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void setSTSCode(String STSCode) {
        this.STSCode = STSCode;
    }

    public String getSTSCode() {
        return STSCode;
    }


}
