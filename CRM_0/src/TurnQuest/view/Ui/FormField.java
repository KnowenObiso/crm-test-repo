package TurnQuest.view.Ui;


public class FormField {
    String Ui_Id;
    String Session_Id;
    String name;
    String label;
    String required;
    String visible;
    private String validate;

    public FormField() {
        super();
    }

    public void setUi_Id(String id) {
        this.Ui_Id = id;
    }
    
    public String getUi_Id() {
        return Ui_Id;
    }
    public void setSession_Id(String id) {
        this.Session_Id = id;
    }
    
    public String getSession_Id() {
        return Session_Id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public Boolean getRequired() {
        return "Y".equalsIgnoreCase(required);
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getVisible() {
        return visible;
    }
    public void setValidate(String validate) {
        this.validate = validate;
    }
    public boolean isValidable(){ 
        return "Y".equalsIgnoreCase(validate);
    }
}
