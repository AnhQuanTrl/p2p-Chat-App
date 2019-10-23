package peer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class SessionGUI implements Runnable {
    private JFrame frame;
    private File file;
    String user;
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
        frame.setTitle("Chat");
        frame.setBounds(100, 100, 729, 606);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
            }
        });
        JFileChooser fileChooser = new JFileChooser();
        //frame.getContentPane().add(fileChooser, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setBorder(null);
        panel.setName("pane");
        panel.setToolTipText("");
        frame.getContentPane().add(panel);
        SpringLayout sl_panel = new SpringLayout();
        panel.setLayout(sl_panel);

        JPanel panel_1 = new JPanel();
        sl_panel.putConstraint(SpringLayout.NORTH, panel_1, 10, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.WEST, panel_1, 10, SpringLayout.WEST, panel);
        sl_panel.putConstraint(SpringLayout.SOUTH, panel_1, 359, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.EAST, panel_1, 705, SpringLayout.WEST, panel);
        panel_1.setBorder(new TitledBorder(null, "Chat Room", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.add(panel_1);
        SpringLayout sl_panel_1 = new SpringLayout();
        panel_1.setLayout(sl_panel_1);

        JTextArea txtChat = new JTextArea();
        sl_panel_1.putConstraint(SpringLayout.NORTH, txtChat, 0, SpringLayout.NORTH, panel_1);
        sl_panel_1.putConstraint(SpringLayout.WEST, txtChat, 10, SpringLayout.WEST, panel_1);
        sl_panel_1.putConstraint(SpringLayout.SOUTH, txtChat, 321, SpringLayout.NORTH, panel_1);
        sl_panel_1.putConstraint(SpringLayout.EAST, txtChat, -6, SpringLayout.EAST, panel_1);
        panel_1.add(txtChat);
        txtChat.setEditable(false);

        JLabel lblChosenFile = new JLabel("Chosen File");
        lblChosenFile.setFont(new Font("Tahoma", Font.PLAIN, 17));
        panel.add(lblChosenFile);

        JButton btnBrowseFile = new JButton("Browse File");
        sl_panel.putConstraint(SpringLayout.NORTH, btnBrowseFile, 399, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.WEST, btnBrowseFile, 570, SpringLayout.WEST, panel);
        sl_panel.putConstraint(SpringLayout.SOUTH, btnBrowseFile, 428, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.EAST, btnBrowseFile, 689, SpringLayout.WEST, panel);
        btnBrowseFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnBrowseFile) {
                    int returnVal = fileChooser.showOpenDialog(panel);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        file = fileChooser.getSelectedFile();
                        lblChosenFile.setText(file.getName());
                    }
                }
            }
        });
        btnBrowseFile.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panel.add(btnBrowseFile);

        JButton btnSendFile = new JButton("Send File");
        sl_panel.putConstraint(SpringLayout.NORTH, btnSendFile, 430, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.WEST, btnSendFile, 570, SpringLayout.WEST, panel);
        sl_panel.putConstraint(SpringLayout.SOUTH, btnSendFile, 459, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.EAST, btnSendFile, 689, SpringLayout.WEST, panel);
        btnSendFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (file != null && file.exists()) {
                    String fileName = file.getName(); //getFileName here
                    if (!fileInProgress) {
                        fileInProgress = true;
                        writer.write("/REQUEST-FILE " + fileName);
                        reader.addFileActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String command = e.getActionCommand();
                                String[] args = command.split(":");
                                if (args[0].equals("accept")) {
                                    SocketFileWriter fileWriter = new SocketFileWriter(file, writer);
                                    fileWriter.addPropertyChangeListener(new PropertyChangeListener() {
                                        @Override
                                        public void propertyChange(PropertyChangeEvent evt) {
                                            if ("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
                                                fileInProgress = false;
                                                file = null;
                                            }
                                        }
                                    });
                                    fileWriter.execute();
                                } else {
                                    file = null;
                                    fileInProgress = false;
                                    System.out.println("File denied");
                                }
                            }
                        });
                    }
                } else {
                    // Do something if cannot find file
                }
            }
        });
        btnSendFile.setMinimumSize(new Dimension(83, 21));
        btnSendFile.setMaximumSize(new Dimension(83, 21));
        btnSendFile.setPreferredSize(new Dimension(83, 21));
        btnSendFile.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panel.add(btnSendFile);

        JSeparator separator_1 = new JSeparator();
        sl_panel.putConstraint(SpringLayout.NORTH, lblChosenFile, 6, SpringLayout.SOUTH, separator_1);
        sl_panel.putConstraint(SpringLayout.WEST, lblChosenFile, 10, SpringLayout.WEST, separator_1);
        sl_panel.putConstraint(SpringLayout.NORTH, separator_1, 368, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.WEST, separator_1, 10, SpringLayout.WEST, panel);
        sl_panel.putConstraint(SpringLayout.SOUTH, separator_1, 382, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.EAST, separator_1, 705, SpringLayout.WEST, panel);
        panel.add(separator_1);

        JPanel panel_2 = new JPanel();
        sl_panel.putConstraint(SpringLayout.NORTH, panel_2, 465, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.WEST, panel_2, 10, SpringLayout.WEST, panel);
        sl_panel.putConstraint(SpringLayout.SOUTH, panel_2, 557, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.EAST, panel_2, 705, SpringLayout.WEST, panel);
        panel_2.setBorder(new TitledBorder(null, "Say Something", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.add(panel_2);
        SpringLayout sl_panel_2 = new SpringLayout();
        panel_2.setLayout(sl_panel_2);

        textField = new JTextField();
        sl_panel_2.putConstraint(SpringLayout.NORTH, textField, 0, SpringLayout.NORTH, panel_2);
        sl_panel_2.putConstraint(SpringLayout.WEST, textField, 7, SpringLayout.WEST, panel_2);
        sl_panel_2.putConstraint(SpringLayout.SOUTH, textField, -5, SpringLayout.SOUTH, panel_2);
        sl_panel_2.putConstraint(SpringLayout.EAST, textField, -6, SpringLayout.EAST, panel_2);
        panel_2.add(textField);
        textField.setColumns(10);
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writer.write("/MESSAGE " + textField.getText());
                txtChat.append(textField.getText());
                txtChat.append("\n");
                txtChat.setCaretPosition(txtChat.getDocument().getLength());
                textField.setText("");
            }
        });
        JLabel lblFileName = new JLabel("");
        sl_panel.putConstraint(SpringLayout.NORTH, lblFileName, 430, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.WEST, lblFileName, 21, SpringLayout.WEST, panel);
        lblFileName.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panel.add(lblFileName);
        frame.setVisible(true);
        writer = new SocketWriter(socket);
        writer.execute();
        if (initiate) writer.write("/REQUEST-SESSION " + user);
        reader = new SocketReader(socket, frame, writer);
        reader.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = e.getActionCommand();
                System.out.println(text);
                txtChat.append(text);
                txtChat.append("\n");
                txtChat.setCaretPosition(txtChat.getDocument().getLength());
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

//        frame = new JFrame();
//        frame.setBounds(100, 100, 450, 300);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                disconnect();
//            }
//        });
//        JPanel panel = new JPanel();
//        frame.getContentPane().add(panel);
//        SpringLayout sl_panel = new SpringLayout();
//        panel.setLayout(sl_panel);
//
//        textArea = new JTextArea();
//        sl_panel.putConstraint(SpringLayout.NORTH, textArea, 27, SpringLayout.NORTH, panel);
//        sl_panel.putConstraint(SpringLayout.WEST, textArea, 29, SpringLayout.WEST, panel);
//        sl_panel.putConstraint(SpringLayout.SOUTH, textArea, 172, SpringLayout.NORTH, panel);
//        sl_panel.putConstraint(SpringLayout.EAST, textArea, -30, SpringLayout.EAST, panel);
//        textArea.setLineWrap(true);
//        panel.add(textArea);
//        JButton btnDisconnect = new JButton("Disconnect");
//        sl_panel.putConstraint(SpringLayout.WEST, btnDisconnect, 0, SpringLayout.WEST, textArea);
//        sl_panel.putConstraint(SpringLayout.SOUTH, btnDisconnect, -10, SpringLayout.SOUTH, panel);
//        panel.add(btnDisconnect);
//
//        JButton btnFile = new JButton("Transfer");
//        sl_panel.putConstraint(SpringLayout.NORTH, btnFile, 0, SpringLayout.NORTH, btnDisconnect);
//        sl_panel.putConstraint(SpringLayout.EAST, btnFile, 0, SpringLayout.EAST, textArea);
//        panel.add(btnFile);
//        btnDisconnect.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                disconnect();
//            }
//        });
//        btnFile.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String fileName = "this.txt"; //getFileName here
//                if (!fileInProgress) {
//                    fileInProgress = true;
//                    writer.write("/REQUEST-FILE "+ fileName);
//                    reader.addFileActionListener(new ActionListener() {
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            String command = e.getActionCommand();
//                            String[] args = command.split(":");
//                            if (args[0].equals("accept")) {
//                                SocketFileWriter fileWriter = new SocketFileWriter(fileName, writer);
//                                fileWriter.addPropertyChangeListener(new PropertyChangeListener() {
//                                    @Override
//                                    public void propertyChange(PropertyChangeEvent evt) {
//                                        if ("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
//                                            fileInProgress = false;
//                                        }
//                                    }
//                                });
//                                fileWriter.execute();
//                            } else {
//                                fileInProgress = false;
//                                System.out.println("File denied");
//                            }
//                        }
//                    });
//                }
//            }
//        });
//        textField = new JTextField();
//        sl_panel.putConstraint(SpringLayout.NORTH, textField, 5, SpringLayout.SOUTH, textArea);
//        sl_panel.putConstraint(SpringLayout.WEST, textField, 0, SpringLayout.WEST, textArea);
//        sl_panel.putConstraint(SpringLayout.EAST, textField, 0, SpringLayout.EAST, textArea);
//        panel.add(textField);
//        textField.setColumns(10);
//        textField.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                writer.write("/MESSAGE " + textField.getText());
//            }
//        });
//        frame.setVisible(true);
//        writer = new SocketWriter(socket);
//        writer.execute();
//        if (initiate) writer.write("/REQUEST-SESSION " + user);
//        reader = new SocketReader(socket, frame, writer);
//        reader.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String text = e.getActionCommand();
//                textArea.append(text);
//                textArea.append("\n");
//                textArea.setCaretPosition(textArea.getDocument().getLength());
//            }
//        });
//        reader.addPropertyChangeListener(new PropertyChangeListener() {
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                if ("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
//                    System.out.println("here");
//                    textField.setEditable(false);
//                }
//            }
//        });
//        reader.execute();
//        System.out.println(socket.getRemoteSocketAddress().toString());
//
