package server;

import java.io.*;
import java.net.Socket;
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
                                username = args[0];
                                password = args[1];

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
                                File file= new File(System.getProperty("user.dir"), "user.scv");
                                FileWriter fileWriter = new FileWriter(file, true);
                                fileWriter.write(username + "," + password);
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
                        writer.println(server.printAllUser());
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
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    void sendMessage(String message) {
        writer.println(message);
    }
}