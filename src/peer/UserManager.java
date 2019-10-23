package peer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserManager {
    private static final UserManager instance = new UserManager();
    private List<String> users;
    private UserManager() {
        users = Collections.synchronizedList(new ArrayList<>(25));
    }
    public void addUsers(String user) {
        users.add(user);
    }
    public void removeUsers(String user) {
        users.remove(user);
    }
    public Boolean alreadyConnected(String user) {
        return users.contains(user);
    }
    public static UserManager getInstance() {
        return instance;
    }
}
