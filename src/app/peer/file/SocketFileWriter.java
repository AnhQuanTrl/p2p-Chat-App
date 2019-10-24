package app.peer.file;


import app.peer.socket.SocketWriter;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;

public class SocketFileWriter extends SwingWorker<Void, Void> {
    public SocketFileWriter(File file, SocketWriter writer) {
        this.file = file;
        this.writer = writer;
    }
    private SocketWriter writer;
    private File file;
    @Override
    protected Void doInBackground() throws Exception {
        byte[] bytes = new byte[1024];
        InputStream in = new FileInputStream(file);
        int count;
        writer.write("/FILE-BEGIN");
        while ((count = in.read(bytes)) > 0) {
            byte[] tmp = Arrays.copyOf(bytes, count);
            String s = Base64.getEncoder().encodeToString(tmp);
            writer.write("/FILE-PART " + s);
        }
        writer.write("/FILE-END");
        return null;
    }
}
