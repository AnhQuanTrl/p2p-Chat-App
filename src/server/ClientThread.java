package server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Set;

public class ClientThread extends Thread{
    private Socket socket;
    private ServerLogin server;
    private PrintWriter writer;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    private String username;
    private String password;

    public ClientThread(Socket socket, ServerLogin server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String command;
            boolean exit = false;
            while (!exit) {
                command = reader.readLine();
                String[] args = command.split("\\s+");
                switch (args[0]) {
                    case "/LOGIN":
                        if (args.length < 3) {
                            writer.println("USAGE: /LOGIN <username> <password>");
                        } else {
                            if (server.isValidUser(args[1], args[2])){
                                writer.println("/ACCEPT-LOGIN");
                                username = args[1];
                                password = args[2];
                                server.loginUser.put(args[1], socket.getInetAddress());
                            }
                            else{
                                writer.println("/DENY-LOGIN");
                            }
                        }
                        break;
                    case "/SIGNUP":
                        if (args.length < 3) {
                            writer.println("USAGE: /SIGNUP <username> <password>");
                        } else {
                            if (!server.users.containsKey(args[1])){
                                File file= new File(System.getProperty("user.dir"), "user.csv");
                                FileWriter fileWriter = new FileWriter(file, true);
                                fileWriter.write(args[1] + "," + args[2]);
                                fileWriter.write(System.getProperty("line.separator"));
                                fileWriter.close();
                                server.users.put(args[1], args[2]);
                                writer.println("/ACCEPT-SIGNUP");
                                break;
                            }
                            else{
                                writer.println("/DENY-SIGNUP");
                            }
                        }
                        break;
                    case "/FETCH":
                        username = args[1];
                        writer.println(server.printAllFriends(username));
                        break;
                    case "/FRIEND-QUERY":
                        String query = args[1];
                        String message = String.join(",", server.searchUser(query));
                        writer.println(message);
                        break;
                    case "/FRIEND-REQUEST":
                        Boolean added = server.addFriendIfAlreadyExist(args[1], args[2]);
                        if (added) {
                            writer.println("/ACCEPT-FRIEND-REQUEST");
                        } else {
                            writer.println("/DENY-FRIEND-REQUEST");
                        }
                    case "/LOGOUT":
                        exit = true;
                        server.loginUser.remove(args[1]);
                        break;
                    case "/EXIT":
                        exit = true;
                        break;

                    default:
                        writer.println("ERROR: Illegal Command");
                }
            }
            server.removeUser(this);
            socket.close();

        } catch (IOException ex) {
            server.removeUser(this);
            server.loginUser.remove(username);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void sendMessage(String message) {
        writer.println(message);
    }
}