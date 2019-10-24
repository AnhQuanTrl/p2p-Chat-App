package app.peer.listener;

import app.gui.ChatSessionGUI;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends SwingWorker<Void, Void> {
    private int port = 8989;

    public Server () {
        this.port = port;
    }
    @Override
    public Void doInBackground() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (!isCancelled()) {
                Socket socket = serverSocket.accept();
                InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                PrintWriter writer = new PrintWriter(out, true);
                String firstMessage = reader.readLine();
                String[] args = firstMessage.split(" ");
                if (args[0].equals("/REQUEST-SESSION")) {
                    int dialogResult = JOptionPane.showConfirmDialog(null, "accept incoming message", "incoming message",JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        writer.println("/ACCEPT-SESSION");
                        SwingUtilities.invokeLater(new ChatSessionGUI(socket, false));
                    } else {
                        writer.println("/DENY-SESSION");
                        socket.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
