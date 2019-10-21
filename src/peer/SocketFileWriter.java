package peer;


import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;

public class SocketFileWriter extends SwingWorker<Void, Void> {
    public SocketFileWriter(String fileName, SocketWriter writer) {
        this.fileName = fileName;
        this.writer = writer;
    }
    private SocketWriter writer;
    private String fileName;
    @Override
    protected Void doInBackground() throws Exception {
        File file = new File(System.getProperty("user.dir") + "\\" + fileName);
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
