package peer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketReader extends SwingWorker<Void, String> {

    private List<ActionListener> actionListeners;
    private Socket socket;
    public SocketReader(Socket socket, JFrame frame) {
        this.socket = socket;
        actionListeners = new ArrayList<>(25);

    }
    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    public void removeActionListener(ActionListener listener) {
        actionListeners.remove(listener);
    }

    @Override
    protected Void doInBackground() throws Exception {

        try (InputStream in = socket.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String serverInput = null;
            do {
                serverInput = reader.readLine();
                if (serverInput != null) {
                    publish(serverInput);
                }
            } while (!serverInput.equals("/exit"));
        }
        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        for (String text : chunks) {
            ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, text);
            for (ActionListener listener : actionListeners) {
                listener.actionPerformed(evt);
            }
        }
    }
}