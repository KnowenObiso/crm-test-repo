package TurnQuest.view.ecm;

public class EcmProps {
    private String name;
    private Object value;

    public EcmProps(String name, Object value) {
        super();
        this.name = name;
        this.value = value;
    }

    public String getName(){
        return name;
    } 
    
    public Object getValue(){
        return value;
    }
}
