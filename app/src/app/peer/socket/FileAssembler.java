package app.peer.socket;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class FileAssembler  extends SwingWorker<Void, Void> {
    public FileAssembler(JFrame frame, String fileName) {
        this.frame = frame;
        this.fileName = fileName;
    }
    private Boolean jobLeft = true;
    private String defaultFileName = "DOWNLOAD";
    private JFrame frame;
    private String fileName;
    private List<String> fileParts = Collections.synchronizedList(new LinkedList<String>());
    private File oldFile = null;
    private File newFile = null;
    private String myDir = System.getProperty("user.dir");
    public void addFilePart(String filePart) {
        synchronized (fileParts) {
            fileParts.add(filePart);
        }
    }
    public void setJobLeft(Boolean jobLeft) {
        this.jobLeft = jobLeft;
    }
    @Override
    protected Void doInBackground() throws Exception {
        if (fileName == null) return null;

        oldFile = new File(myDir, defaultFileName);
        try (FileOutputStream fileOut = new FileOutputStream(oldFile)) {
            while (jobLeft || !fileParts.isEmpty()) { //end only if no more job is left and filePart is empty
                if (fileParts.isEmpty()) continue;
                List<String> cache = null;
                synchronized (fileParts) {
                    cache = new LinkedList<>(fileParts);
                    fileParts.clear();
//                    for (String text : fileParts) {
//                        byte[] result = Base64.getDecoder().decode(text);
//                        fileOut.write(result);
//                    }
//                    fileParts.clear();
                }
                for (String text : cache) {
                    byte[] result = Base64.getDecoder().decode(text);
                    fileOut.write(result);
                    System.out.println(result);
                }
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
//        try {
//            if (fileName == null) return null;
//            String extension = fileName.substring(fileName.lastIndexOf("."));
//            String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
//            String myDir = System.getProperty("user.dir");
//            File file = new File(myDir, fileName);
//            for (int num = 1; file.exists(); num++) {
//                file = new File(myDir, nameWithoutExtension + "(" + num + ")" + "." + extension);
//            }
//            FileOutputStream fileOut = new FileOutputStream(file);
//            for (String filePart : fileParts) {
//                byte[] result = Base64.getDecoder().decode(filePart);
//                fileOut.write(result);
//            }
//            fileOut.close();
//            fileName = null;
//        } catch (FileNotFoundException e) {
//            System.out.println("Find not found!");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return null;
    }
    @Override
    protected void done() {
        String extension = fileName.substring(fileName.lastIndexOf(".")+1);
        String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
        newFile = new File(myDir, fileName);
        for (int num = 1; newFile.exists(); num++) {
            newFile = new File(myDir, nameWithoutExtension + "(" + num + ")" + "." + extension);
        }
        boolean success = oldFile.renameTo(newFile);
        if (!success) {
            System.out.println("Something wrong");
        }
        JOptionPane.showMessageDialog(frame, "File Received!");
    }
}
