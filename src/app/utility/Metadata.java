package app.utility;

public class Metadata {
    private static final Metadata instance = new Metadata();

    public void setUsername(String username) {
        this.username = username;
    }
    public static Metadata getInstance() {
        return instance;
    }
    private String username;
    private Metadata() {
        username = "";
    }
    public String getUsername() {
        return username;
    }

}

