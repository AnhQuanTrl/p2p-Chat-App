package app.peer.socket;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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
        messages = Collections.synchronizedList(new LinkedList<String>());
        fileParts= Collections.synchronizedList(new LinkedList<String>());
        lock = new ReentrantLock();
        waitCon = lock.newCondition();
    }

    public void write(String text) {
        messages.add(text);
        lock.lock();
        try {
            waitCon.signalAll();
        }
        finally {
            lock.unlock();
        }
    }

    public void addFilePart(String filePart) {
        fileParts.add(filePart);
        lock.lock();
        try {
            waitCon.signalAll();
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    protected Void doInBackground() throws Exception {
            try (OutputStream output = socket.getOutputStream()) {
                PrintWriter writer = new PrintWriter(output, true);
                while (!isCancelled()) {
                    while (messages.isEmpty() && fileParts.isEmpty()) {
                        lock.lock();
                        try {
                            waitCon.await();
                        }
                        finally {
                            lock.unlock();
                        }
                    }
                    List<String> cache = messages;
                    messages = Collections.synchronizedList(new LinkedList<String>());
                    List<String> cache2 = fileParts;
                    fileParts = Collections.synchronizedList(new LinkedList<String>());
                    for (String text : cache) {
                        writer.println(text);
                    }
                    for (String text : cache2) {
                        writer.println(text);
                        if (!messages.isEmpty()) {
                            List<String> cache3 = messages;
                            messages = Collections.synchronizedList(new LinkedList<String>());
                            for (String text2 : cache3) {
                                writer.println(text2);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }
}
