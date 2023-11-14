package TurnQuest.view.Alerts;


public class TestAllerts {
    public static void main(String[] args) {
        TestAllerts test = new TestAllerts();
        test.execute();
    }

    public String execute() {
        SystemAlerts test = new SystemAlerts();
        test.execute(null);

        return null;
    }
}
