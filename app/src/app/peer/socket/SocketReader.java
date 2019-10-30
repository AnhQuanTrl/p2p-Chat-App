package app.peer.socket;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SocketReader extends SwingWorker<Void, String> {

    private List<ActionListener> actionListeners;
    private List<ActionListener> fileActionListeners;
    private Socket socket;
    private JFrame frame;
    private FileAssembler fileAssembler;
    private SocketWriter writer;
    private String fileName = null;
    String returnMessage = null;
    public SocketReader(Socket socket, JFrame frame, SocketWriter writer) {
        this.socket = socket;
        this.frame = frame;
        actionListeners = new ArrayList<>(25);
        fileActionListeners = new ArrayList<>(25);
        this.writer = writer;
    }
    public void addFileActionListener(ActionListener listener) {fileActionListeners.add(listener); }
    public void removeFileActionListener() {
        fileActionListeners.clear();
    }
    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }



    @Override
    protected Void doInBackground() throws Exception {
        try (InputStream in = socket.getInputStream()){
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String serverInput = null;
            loop: do {
                serverInput = reader.readLine();
                if (serverInput != null) {
                    String[] args = serverInput.split(" ");
                    switch (args[0]) {
                        case "/ACCEPT-SESSION":
                            break;
                        case "/DENY-SESSION":
                            returnMessage = "Your peer deny the chat request";
                            return null;
                        case "/REQUEST-FILE":
                            int dialogResult = JOptionPane.showConfirmDialog(null, "accept file transfer "+ args[1], "File Transfer",JOptionPane.YES_NO_OPTION);
                            if (dialogResult == JOptionPane.YES_OPTION) {
                                writer.write("/ACCEPT-FILE " + args[1]);
                                fileName = serverInput.substring(serverInput.indexOf(" ")+1);
                            } else {
                                writer.write("/DENY-FILE " + args[1]);
                            }
                            break;
                        case "/ACCEPT-FILE":
                            ActionEvent acceptEvt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "accept:"+args[1]);
                            for (ActionListener listener: fileActionListeners) {
                                listener.actionPerformed(acceptEvt);
                            }
                            removeFileActionListener();
                            break;
                        case "/DENY-FILE":
                            ActionEvent denyEvt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "deny:"+args[1]);
                            for (ActionListener listener: fileActionListeners) {
                                listener.actionPerformed(denyEvt);
                            }
                            removeFileActionListener();
                            break;
                        case "/FILE-BEGIN":
                            fileAssembler = new FileAssembler(frame, fileName);
                            fileAssembler.execute();
                            break;
                        case "/FILE-PART":
                            fileAssembler.addFilePart(args[1]);
                            break;
                        case "/FILE-END":
                            fileAssembler.setJobLeft(false);
                            fileAssembler = null;
                            break;
                        case "/MESSAGE":
                            publish(serverInput.substring(serverInput.indexOf(" ")+1));
                            break;
                    }
                }
                System.out.println(serverInput);
            } while (serverInput != null && !serverInput.equals("/EXIT") && !isCancelled());
        }
        returnMessage = "Your peer has disconnected";
        return null;
    }

    @Override
    protected void done() {
        if (returnMessage != null) JOptionPane.showMessageDialog(frame, returnMessage);
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