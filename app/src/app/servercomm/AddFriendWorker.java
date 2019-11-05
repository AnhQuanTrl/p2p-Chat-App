package app.servercomm;

import app.utility.Metadata;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class AddFriendWorker extends SwingWorker<Void, String> {
    private Socket socket;
    private String friend;
    private JFrame frame;
    String message = null;
    public AddFriendWorker(Socket socket, String friend, JFrame frame) {
        this.socket = socket;
        this.friend = friend;
        this.frame = frame;
    }

    @Override
    protected Void doInBackground() throws Exception {
        try (InputStream input = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {
            PrintWriter writer = new PrintWriter(out, true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            writer.println("/FRIEND-REQUEST " + Metadata.getInstance().getUsername() + " " + friend);
            String response = reader.readLine();
            if (response.equals("/ACCEPT-FRIEND-REQUEST")) {
                message = "The peer has been added as your friend";
            } else {
                message = "Cannot add that peer as your friend";
            }
            writer.println("/EXIT");
        }
        return null;
    }

    @Override
    protected void done() {
        try {
            socket.close();
            JOptionPane.showMessageDialog(null, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
