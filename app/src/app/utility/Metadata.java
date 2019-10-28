package app.utility;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public class Metadata {
    public String getHostIP() {
        return hostIP;
    }

    public void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }

    private String hostIP;
    private static final Metadata instance = new Metadata();
    private Map<String, JFrame> connectUsers;
    public void setUsername(String username) {
        this.username = username;
    }
    public static Metadata getInstance() {
        return instance;
    }
    private String username;
    private Metadata() {
        username = "";
        connectUsers = new HashMap<>();
    }
    public String getUsername() {
        return username;
    }

    public void addConnectedUser(String username, JFrame frame) {
        connectUsers.put(username, frame);
    }
    public Boolean containUser(String username) {
        return connectUsers.containsKey(username);
    }
    public JFrame findFrame(String username) {
        return connectUsers.get(username);
    }
    public void removeConnectedUser(String username) {
        connectUsers.remove(username);
    }

    public void disposeAllFrame() {
        for (Map.Entry<String, JFrame> entry : connectUsers.entrySet()) {
            JFrame frame = entry.getValue();
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }

}

