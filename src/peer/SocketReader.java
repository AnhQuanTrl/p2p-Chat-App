package peer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SocketReader extends SwingWorker<Void, String> {

    private List<ActionListener> actionListeners;
    private List<ActionListener> fileActionListeners;
    private Socket socket;
    private JFrame frame;
    private SocketWriter writer;
    private String fileName = null;
    private List<String> fileParts = new ArrayList<>();
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
        try (InputStream in = socket.getInputStream()) {
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
                            break loop;
                        case "/REQUEST-FILE":
                            int dialogResult = JOptionPane.showConfirmDialog(null, "accept file transfer", "File Transfer",JOptionPane.YES_NO_OPTION);
                            if (dialogResult == JOptionPane.YES_OPTION) {
                                writer.write("/ACCEPT-FILE " + args[1]);
                                fileName = args[1];
                            } else {
                                writer.write("/DENY-FILE " + args[1]);
                            }
                            System.out.println("Still OK");
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
                            fileParts.clear();
                            break;
                        case "/FILE-PART":
                            fileParts.add(args[1]);
                            break;
                        case "/FILE-END":
                            assembleFile();
                            break;
                        case "/MESSAGE":
                            publish(args[1]);
                            break;
                    }
                    System.out.println(fileParts);
                }
            } while (serverInput != null && !serverInput.equals("/exit") && !isCancelled());
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

    private void assembleFile() {
        try {
            FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.dir") + "\\" + "that.txt");
            for (String filePart : fileParts) {
                byte[] result = Base64.getDecoder().decode(filePart);
                fileOut.write(result);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Find not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}