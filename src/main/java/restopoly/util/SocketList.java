package restopoly.util;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Krystian.Graczyk on 05.01.16.
 */
public class SocketList {
    private static final SocketList INSTANCE = new SocketList();

    public static SocketList getInstance() {
        return INSTANCE;
    }

    HashMap<String, PlayerWebSocket> members = new HashMap<>();

    public void join(String hostname, PlayerWebSocket socket) {
        members.put(hostname,socket);
    }

    public void part(String hostname) {
        members.remove(hostname);
    }

    public void writeAllMembers(String message) {
        for (PlayerWebSocket member : members.values()) {
            member.session.getRemote().sendStringByFuture(message);
        }
    }

    public void writeSpecificMember(String hostname, String message) {
        System.out.println("Hostname: " +hostname + " | SessionList: " + members.keySet());
        PlayerWebSocket member = members.get(hostname);
        try {
            member.session.getRemote().sendString(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}