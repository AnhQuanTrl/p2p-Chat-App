package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

public class ServerLogin {
    private int port;
    private Set<ClientThread> clientThreads = new HashSet<>();
    Map<String,String> users = Collections.synchronizedMap(new HashMap<>());
    Map<String, Set<String>> friends = Collections.synchronizedMap(new HashMap<>());
    private File userFile;
    private File friendFile;
    Map<String, InetAddress> loginUser = new HashMap<>();
    public ServerLogin(int port){
        this.port = port;
        try {
            userFile = new File(System.getProperty("user.dir"), "user.csv");
            if (!userFile.exists()) {
                userFile.createNewFile();
            }
            friendFile = new File(System.getProperty("user.dir"), "friend.csv");
            if (!friendFile.exists()) {
                friendFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadCSV() {
        try (BufferedReader userFileReader = new BufferedReader(new FileReader(userFile));
             BufferedReader friendFileReader = new BufferedReader(new FileReader(friendFile))) {
            String row;
            while ((row = userFileReader.readLine()) != null) {
                String[] userField = row.split(",");
                users.put(userField[0], userField[1]);
            }
            while ((row = friendFileReader.readLine()) != null) {
                String[] friendField = row.split(",");
                Set<String> value = new HashSet<>();
                for (int i = 1; i < friendField.length; i++) {
                    value.add(friendField[i]);
                }
                friends.put(friendField[0], value);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void execute() {
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

    private void updateCSV() {
        try (FileWriter userFileWriter = new FileWriter(userFile);
             FileWriter friendFileWriter = new FileWriter(friendFile)) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                userFileWriter.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
            for (Map.Entry<String, Set<String>> entry : friends.entrySet()) {
                friendFileWriter.write(entry.getKey() + ",");
                for (String friend : entry.getValue()) {
                    friendFileWriter.write(friend + ",");
                }
                friendFileWriter.write("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ServerLogin server = new ServerLogin(7000);
        server.loadCSV();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                System.out.println("heres");
                server.updateCSV();
            }
        });
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
        StringBuilder stringBuilder = new StringBuilder("");
        if (friends.containsKey(username)) {
            Set<String> friendSet = friends.get(username);
            for (String friend : friendSet) {
                stringBuilder.append(friend).append(",");
                if (loginUser.containsKey(friend)) stringBuilder.append(loginUser.get(friend));
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
//        File file= new File(System.getProperty("user.dir"), "user.csv");
//        StringBuilder message = new StringBuilder("/UNFETCH ");
//        List<String> friends = new ArrayList<>();
//        try {
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//            BufferedReader fileReader = new BufferedReader(new FileReader(file));
//            String row;
//            while ((row = fileReader.readLine()) != null) {
//                String[] userField = row.split(",");
//                if (userField[0].equals(username) && userField.length >= 3) {
//                    List<String> clist = Arrays.asList(userField);
//                    friends = clist.subList(2, clist.size());
//                    break;
//                }
//            }
//            fileReader.close();
//            if (friends != null) {
//                for (String friend: friends) {
//                    message.append(friend).append(",");
//                    if (loginUser.get(friend) != null) message.append(loginUser.get(friend));
//                    message.append(" ");
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return message.toString();
    }
    Boolean addFriendIfAlreadyExist(String source, String target) {
        if (friends.containsKey(source)) {
            Set<String> friendSet = friends.get(source);
            if (!friendSet.contains(target)) {
                friendSet.add(target);
                return true;
            }
        }
        return false;
//        File file= new File(System.getProperty("user.dir"), "user.csv");
//        try {
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//            BufferedReader fileReader = new BufferedReader(new FileReader(file));
//            String row;
//            List<String> list = new ArrayList<>();
//            while ((row = fileReader.readLine()) != null) {
//                list.add(row);
//                String[] userField = row.split(",");
//                if (userField[0].equals(source)) {
//                    List clist = Arrays.asList(userField);
//                    List friends = clist.subList(2, clist.size());
//                    if (friends == null || !friends.contains(target)) {
//                        StringBuilder stringBuilder = new StringBuilder(row);
//                        stringBuilder.append(",").append(target);
//                        list.set(list.size() - 1, stringBuilder.toString());
//                    } else {
//                        return false;
//                    }
//                }
//            }
//            fileReader.close();
//            FileWriter fileWriter = new FileWriter(file);
//            for (String element : list) {
//                fileWriter.write(element);
//                fileWriter.write("\n");
//            }
//            fileWriter.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return true;
    }

    Set<String> searchUser(String query) {
        Set<String> set = users.keySet().stream().filter(s -> s.contains(query)).collect(Collectors.toSet());
        return set;
    }
}

