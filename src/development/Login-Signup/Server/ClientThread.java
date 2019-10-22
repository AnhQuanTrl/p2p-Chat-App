package Server;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ClientThread extends Thread{
    private Socket socket;
    private ServerLogin server;
    private PrintWriter writer;
    private ClientThread connectedUser;
    private HashMap<String,String> userName_PassWord = new HashMap<>();


    public Set<String> getUserName() {
        return this.userName_PassWord.keySet();
    }

    public ClientThread(Socket socket, ServerLogin server) {
        this.socket = socket;
        this.server = server;
        connectedUser = null;
        userName_PassWord.put("Java","Java");
    }

    @Override
    public void run() {
        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
            printUsers();

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
                            if (this.getUserName().contains(args[1]) && args[2].equals(this.userName_PassWord.get(args[1]))){
                                writer.println("Login Success!");
                                String serverMessage = "New user connected: " + args[1];
                                server.broadcast(serverMessage, this);
                                break;
                            }
                            else{
                                writer.println("Wrong username or password!");
                            }
                        }
                        break;
                    case "/SIGNUP":
                        if (args.length < 3) {
                            writer.println("USAGE: /SIGNUP <username> <password>");
                        } else {
                            if (!this.getUserName().contains(args[1])){
                                userName_PassWord.put(args[1],args[2]);
                                writer.println("Sign Up Success!");
                                writer.println(userName_PassWord.get(args[1]));
                                String serverMessage = "New user sign up: " + args[1];
                                server.broadcast(serverMessage, this);
                                break;
                            }
                            else{
                                writer.println("Username has already exist");
                            }
                        }
                    case "/EXIT":
                        exit = true;
                    default:
                        writer.println("ERROR: Illegal Command");
                }
            }
            System.out.println("ERROR: Illegal Command");
            server.removeUser(this);
            socket.close();

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Connected users: " + server.getUserNames());
        } else {
            writer.println("No other users connected");
        }
    }

    void sendMessage(String message) {
        writer.println(message);
    }
}