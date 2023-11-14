package TurnQuest.view.dao;


public class EcmProps {
    private String name;
    private Object value;

    public EcmProps(String name, Object value) {
        super();
        this.name = name;
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
