package server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServerLogin {
    private int port;
    private Set<ClientThread> clientThreads = new HashSet<>();
    Map<String,String> users = new HashMap<>();
    Map<String, InetAddress> loginUser = new HashMap<>();

    public ServerLogin(int port){
        this.port = port;
        File file= new File(System.getProperty("user.dir"), "user.csv");
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            String row;
            while ((row = fileReader.readLine()) != null) {
                String[] userField = row.split(",");
                users.put(userField[0], userField[1]);
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            String ip = socket.getLocalAddress().getHostAddress();
            System.out.print(ip);
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Chat Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
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
        clientThreads.remove(userThread);
    }
    Boolean isValidUser(String username, String password) {
        if (users.containsKey(username) && users.get(username).equals(password)) {
            return true;
        }
        return false;
    }

    boolean hasUsers() {
        return !this.users.isEmpty();
    }

    String printAllUser(String excludeUser) {
        StringBuilder message = new StringBuilder("/UNFETCH ");
        for (Map.Entry<String, InetAddress> entry : loginUser.entrySet()) {
            if (!entry.getKey().equals(excludeUser)) {
                message.append(entry.getKey()).append(",").append(entry.getValue()).append(" ");
            }
        }
        return message.toString();
    }
}

