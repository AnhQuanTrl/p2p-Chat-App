package Client;

import java.io.*;
import java.net.Socket;

public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private ClientLogIn client;

    public WriteThread(Socket socket, ClientLogIn client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    @Override
    public void run() {

        try {
            String text;
            String temp="";
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            String userName = "";
            while (!client.getLoginStatus()){
                    System.out.println("\nLogin in or sign up, plz! ");
                    text = bufferRead.readLine();
                    if (!client.getLoginStatus()){temp = text;}
                    writer.println(text);
            }
            userName = temp.split(" ")[1];
            client.setUserName(userName);
            do {
                System.out.println("[" + userName + "]: ");
                text = bufferRead.readLine();
                writer.println(text);
            } while (!text.equals("/EXIT"));
            System.out.println("x");
            socket.close();
        } catch (IOException ex) {

            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}