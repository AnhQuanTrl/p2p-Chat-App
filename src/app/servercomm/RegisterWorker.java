package app.servercomm;

import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class RegisterWorker extends SwingWorker<Boolean, Void> {
    private Socket socket;
    private String username;
    private String password;
    private JFrame frame;
    public RegisterWorker(Socket socket, String username, String password, JFrame frame) {
        this.socket = socket;
        this.username = username;
        this.password = password;
        this.frame = frame;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        try {InputStream input = socket.getInputStream(); OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            writer.println("/SIGNUP " + username + " " + password);
            String res = reader.readLine();
            if (res.equals("/ACCEPT-SIGNUP")) {
                writer.println("/EXIT");
                return true;
            }
            writer.println("/EXIT");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void done() {
        try {
            Boolean result = get();
            if (result) {
                JOptionPane.showMessageDialog(frame, "Register Successfully");
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            } else {
                JOptionPane.showMessageDialog(frame, "Register Failed");
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
