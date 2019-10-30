package app.peer.socket;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SocketWriter extends SwingWorker<Void, Void> {
    private Socket socket;
    private List<String> messages;
    private List<String> fileParts;
    private ReentrantLock lock;
    private Condition waitCon;
    public SocketWriter(Socket socket) {
        this.socket = socket;
        messages = Collections.synchronizedList(new ArrayList<String>(100));
        fileParts = Collections.synchronizedList(new ArrayList<String>(100));
    }

    public void write(String text) {
        synchronized (messages) {
            messages.add(text);
        }
    }

    @Override
    protected Void doInBackground() throws Exception {
            try (OutputStream output = socket.getOutputStream()) {
                PrintWriter writer = new PrintWriter(output, true);
                while (!isCancelled()) {
                    if (messages.isEmpty() && !isCancelled()) continue;
                    List<String> cache = null;
                    synchronized (messages) {
                        cache = new ArrayList<>(messages);
                        messages.clear();
                    }
                    for (String text : cache) {
                        writer.println(text);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }
}
