package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ServerLogin {
    private int port;
    private Set<ClientThread> clientThreads = new HashSet<>();
    Map<String,String> users = new HashMap<>();
    Map<String, InetAddress> loginUser = new HashMap<>();

    public ServerLogin(int port){
        this.port = port;
        File file= new File(System.getProperty("user.dir"), "user.csv");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
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

    String printAllFriends(String username) {
        File file= new File(System.getProperty("user.dir"), "user.csv");
        StringBuilder message = new StringBuilder("/UNFETCH ");
        List<String> friends = new ArrayList<>();
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            String row;
            while ((row = fileReader.readLine()) != null) {
                String[] userField = row.split(",");
                System.out.println(row);
                if (userField[0].equals(username) && userField.length >= 3) {
                    List<String> clist = Arrays.asList(userField);
                    friends = clist.subList(2, clist.size());
                    break;
                }
            }
            fileReader.close();
            if (friends != null) {
                for (String friend: friends) {
                    message.append(friend).append(",");
                    if (loginUser.get(friend) != null) message.append(loginUser.get(friend));
                    message.append(" ");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(message.toString());
        return message.toString();
    }
    Boolean addFriendIfAlreadyExist(String source, String target) {
        File file= new File(System.getProperty("user.dir"), "user.csv");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            String row;
            List<String> list = new ArrayList<>();
            while ((row = fileReader.readLine()) != null) {
                list.add(row);
                String[] userField = row.split(",");
                if (userField[0].equals(source)) {
                    List clist = Arrays.asList(userField);
                    List friends = clist.subList(2, clist.size());
                    if (friends == null || !friends.contains(target)) {
                        StringBuilder stringBuilder = new StringBuilder(row);
                        stringBuilder.append(",").append(target);
                        list.set(list.size() - 1, stringBuilder.toString());
                    } else {
                        return false;
                    }
                }
            }
            fileReader.close();
            FileWriter fileWriter = new FileWriter(file);
            for (String element : list) {
                fileWriter.write(element);
                fileWriter.write("\n");
            }
            fileWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    Set<String> searchUser(String query) {
        Set<String> set = users.keySet().stream().filter(s -> s.contains(query)).collect(Collectors.toSet());
        return set;
    }
}

