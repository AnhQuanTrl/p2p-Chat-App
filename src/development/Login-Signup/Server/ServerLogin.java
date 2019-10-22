package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ServerLogin {
    private int port = 7000;
    private Set<ClientThread> clientThreads = new HashSet<ClientThread>();
    private Set<String> userNames = new HashSet<String>();

    public ServerLogin(int port){
        this.port = port;
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Chat Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");
                ClientThread newUser = new ClientThread(socket, this);
                clientThreads.add(newUser);
                newUser.start();
            }
        } catch(IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ServerLogin server = new ServerLogin(7000);
        server.execute();
    }
    void broadcast(String message, ClientThread excludeThread) {
        for (ClientThread client : clientThreads) {
            if (client != excludeThread) {
                client.sendMessage(message);
            }
        }
    }


    void removeUser(ClientThread userThread) {
        boolean removed = userNames.remove(userThread.getUserName());
        if (removed) {
            clientThreads.remove(userThread);
            System.out.println("The user " + userThread.getUserName() + " quitted");
        }
    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }


    ClientThread findThread(String userName) {
        for (ClientThread user : clientThreads) {
            System.out.println(user.getUserName());
            if (user.getUserName().contains(userName)) {
                return user;
            }
        }
        return null;
    }
}

