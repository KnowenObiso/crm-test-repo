package TurnQuest.view.bpm;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BPMUser {


    private static ConcurrentMap concurrentMap = null;

    public BPMUser(String username) {
        super();
        concurrentMap = new ConcurrentHashMap();
        concurrentMap.put("Username", username);
    }


    static String getUsername() {
        return (String)concurrentMap.get("Username");
    }
}