package app.peer.socket;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class FileAssembler  extends SwingWorker<Void, Void> {
    public FileAssembler(JFrame frame, String fileName, List<String> fileParts) {
        this.fileName = fileName;
        this.fileParts = fileParts;
    }
    private JFrame frame;
    private String fileName;
    private List<String> fileParts;


    @Override
    protected Void doInBackground() throws Exception {
        try {
            if (fileName == null) return null;
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
            String myDir = System.getProperty("user.dir");
            File file = new File(myDir, fileName);
            for (int num = 1; file.exists(); num++) {
                file = new File(myDir, nameWithoutExtension + "(" + num + ")" + "." + extension);
            }
            FileOutputStream fileOut = new FileOutputStream(file);
            for (String filePart : fileParts) {
                byte[] result = Base64.getDecoder().decode(filePart);
                fileOut.write(result);
            }
            fileOut.close();
            fileName = null;
        } catch (FileNotFoundException e) {
            System.out.println("Find not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void done() {
        JOptionPane.showMessageDialog(frame, "File Received!");
    }
}
