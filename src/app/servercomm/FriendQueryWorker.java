package app.servercomm;

import app.utility.Metadata;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class FriendQueryWorker extends SwingWorker<Void, String> {
    private Socket socket;

    private String query;

    private JFrame frame;
    DefaultListModel<String> listModel;
    public FriendQueryWorker(Socket socket, String query, DefaultListModel<String> listModel, JFrame frame) {
        this.socket = socket;
        this.query = query;
        this.frame = frame;
        this.listModel = listModel;
    }

    @Override
    protected Void doInBackground() throws Exception {
        try (InputStream input = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {
            PrintWriter writer = new PrintWriter(out, true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            writer.println("/FRIEND-QUERY " + query);
            String response = reader.readLine();
            String[] results = response.split(",");
            listModel.clear();
            for (String result : results) {
                listModel.addElement(result);
            }
            writer.println("/EXIT");
        }
        return null;
    }

    @Override
    protected void done() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
