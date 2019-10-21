package peer;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class SessionGUI implements Runnable {
    private JFrame frame;
    private JTextArea textArea;
    private JTextField textField;
    private SocketWriter writer;
    private SocketReader reader;
    private Boolean initiate;
    private Boolean fileInProgress = false;
    public SessionGUI(Socket socket, Boolean initiate) {
        this.socket = socket;
        this.initiate = initiate;
    }

    public SessionGUI(Socket socket) {
        this(socket, true);
    }

    private Socket socket;

    @Override
    public void run() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
            }
        });
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);
        SpringLayout sl_panel = new SpringLayout();
        panel.setLayout(sl_panel);

        textArea = new JTextArea();
        sl_panel.putConstraint(SpringLayout.NORTH, textArea, 27, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.WEST, textArea, 29, SpringLayout.WEST, panel);
        sl_panel.putConstraint(SpringLayout.SOUTH, textArea, 172, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.EAST, textArea, -30, SpringLayout.EAST, panel);
        textArea.setLineWrap(true);
        panel.add(textArea);
        JButton btnDisconnect = new JButton("Disconnect");
        sl_panel.putConstraint(SpringLayout.WEST, btnDisconnect, 0, SpringLayout.WEST, textArea);
        sl_panel.putConstraint(SpringLayout.SOUTH, btnDisconnect, -10, SpringLayout.SOUTH, panel);
        panel.add(btnDisconnect);

        JButton btnFile = new JButton("Transfer");
        sl_panel.putConstraint(SpringLayout.NORTH, btnFile, 0, SpringLayout.NORTH, btnDisconnect);
        sl_panel.putConstraint(SpringLayout.EAST, btnFile, 0, SpringLayout.EAST, textArea);
        panel.add(btnFile);
        btnDisconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disconnect();
            }
        });
        btnFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = "this.txt"; //getFileName here
                if (!fileInProgress) {
                    fileInProgress = true;
                    writer.write("/REQUEST-FILE "+ fileName);
                    reader.addFileActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String command = e.getActionCommand();
                            String[] args = command.split(":");
                            if (args[0].equals("accept")) {
                                SocketFileWriter fileWriter = new SocketFileWriter(fileName, writer);
                                fileWriter.addPropertyChangeListener(new PropertyChangeListener() {
                                    @Override
                                    public void propertyChange(PropertyChangeEvent evt) {
                                        if ("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
                                            fileInProgress = false;
                                        }
                                    }
                                });
                                fileWriter.execute();
                            } else {
                                fileInProgress = false;
                                System.out.println("File denied");
                            }
                        }
                    });
                }
            }
        });
        textField = new JTextField();
        sl_panel.putConstraint(SpringLayout.NORTH, textField, 5, SpringLayout.SOUTH, textArea);
        sl_panel.putConstraint(SpringLayout.WEST, textField, 0, SpringLayout.WEST, textArea);
        sl_panel.putConstraint(SpringLayout.EAST, textField, 0, SpringLayout.EAST, textArea);
        panel.add(textField);
        textField.setColumns(10);
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writer.write("/MESSAGE " + textField.getText());
            }
        });
        frame.setVisible(true);
        writer = new SocketWriter(socket);
        writer.execute();
        if (initiate) writer.write("/REQUEST-SESSION");
        reader = new SocketReader(socket, frame, writer);
        reader.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = e.getActionCommand();
                textArea.append(text);
                textArea.append("\n");
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }
        });
        reader.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
                    System.out.println("here");
                    textField.setEditable(false);
                }
            }
        });
        reader.execute();
        System.out.println(socket.getRemoteSocketAddress().toString());

    }
    private void disconnect() {
        writer.write("/exit");
        writer.cancel(true);
        if (socket.isConnected()) {
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
