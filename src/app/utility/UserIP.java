package app.utility;

import java.net.InetAddress;

public class UserIP {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public UserIP(String username, InetAddress inetAddress) {
        this.username = username;
        this.inetAddress = inetAddress;
    }

    private String username;
    private InetAddress inetAddress;

}
