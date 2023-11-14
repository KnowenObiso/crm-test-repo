package TurnQuest.view.client;

public class Client {
    String code;
    String name;
    String pin;
    
    public Client() {
        super();
    }
    public Client(String code,String name,String pin) {
        super();
        this.code=code;
        this.name=name;
        this.pin=name;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }

    public String getCaption() {
        String caption=pin;
        caption=caption.concat(" - ");
        caption=caption.concat(name);
        return caption;
    }
}
