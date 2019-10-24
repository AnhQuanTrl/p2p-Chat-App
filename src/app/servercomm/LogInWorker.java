package app.servercomm;

import app.gui.PeerSelectionGUI;
import app.peer.listener.Server;
import app.utility.Metadata;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class LogInWorker extends SwingWorker<Boolean, Void> {
    private Socket socket;
    private String username;
    private String password;
    private JFrame frame;

    public LogInWorker(Socket socket, String username, String password, JFrame frame) {
        this.socket = socket;
        this.username = username;
        this.password = password;
        this.frame = frame;
    }
    @Override
    protected Boolean doInBackground() throws Exception {
        try (InputStream input = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {
            PrintWriter writer = new PrintWriter(out, true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            writer.println("/LOGIN " + username + " " + password);
            String res = reader.readLine();
            if (res.equals("/ACCEPT-LOGIN")) {
                Metadata.getInstance().setUsername(username);
                writer.println("/EXIT");
                return true;
            }
        }
        return false;
    }

    @Override
    protected void done() {
        try {
            Boolean result = get();
            if (result) {
                JOptionPane.showMessageDialog(frame, "Login Successfully");
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                Metadata.getInstance().setUsername("");
                SwingUtilities.invokeLater(new PeerSelectionGUI());
            } else {
                JOptionPane.showMessageDialog(frame, "Login Failed");
            }
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }  catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
