package app.peer.listener;

import app.gui.ChatSessionGUI;
import app.utility.Metadata;

import javax.swing.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends SwingWorker<Void, Void> {
    private int port = 8989;
    private ServerSocket serverSocket;
    public void setCancel(Boolean cancel) {
        isCancel = cancel;
    }

    private Boolean isCancel = false;
    public Server () {
        this.port = port;
    }
    @Override
    public Void doInBackground() {
        try {
            serverSocket = new ServerSocket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(port));
            while (!isCancelled()) {
                Socket socket = serverSocket.accept();
                InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                PrintWriter writer = new PrintWriter(out, true);
                String firstMessage = reader.readLine();
                String[] args = firstMessage.split(" ");
                if (args[0].equals("/REQUEST-SESSION")) {
                    int dialogResult = JOptionPane.showConfirmDialog(null, "accept incoming message from " + args[1], "incoming message",JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        writer.println("/ACCEPT-SESSION");
                        SwingUtilities.invokeLater(new ChatSessionGUI(socket, args[1], false));
                    } else {
                        writer.println("/DENY-SESSION");
                        socket.close();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Server restart");
        }
        return null;
    }

    @Override
    protected void done() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("ok");
        }
    }
}
