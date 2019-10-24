package app.servercomm;


import app.gui.PeerSelectionGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FetchWorker extends SwingWorker<Boolean, String> {
    private Socket socket;
    private List<ActionListener> actionListeners;
    private JFrame frame;
    public FetchWorker(Socket socket, JFrame frame) {
        this.socket = socket;
        this.frame = frame;
        actionListeners = new ArrayList<>(25);
    }
    public void addActionListeners(ActionListener listener) {
        actionListeners.add(listener);
    }
    @Override
    protected Boolean doInBackground() throws Exception {
        try (InputStream input = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {
            PrintWriter writer = new PrintWriter(out, true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            while (!isCancelled()) {
                writer.println("/FETCH");
                String res = reader.readLine();
                publish(res);
                Thread.sleep(3000);
            }
        }
        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        for (String text : chunks) {
            text = text.substring(text.indexOf(" "));
            ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, text);
            for (ActionListener listener : actionListeners) {
                listener.actionPerformed(evt);
            }
        }
    }
}
