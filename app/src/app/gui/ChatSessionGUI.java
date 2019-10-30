package app.gui;

import app.peer.file.SocketFileWriter;
import app.peer.socket.SocketReader;
import app.peer.socket.SocketWriter;
import app.utility.Metadata;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class ChatSessionGUI implements Runnable {
    private JFrame frame;
    private File file;
    private String username;
    private JTextField textField;
    private SocketWriter writer;
    private SocketReader reader;
    private Boolean initiate;
    private Boolean fileInProgress = false;

    public ChatSessionGUI(Socket socket, String username, Boolean initiate) {
        this.socket = socket;
        this.initiate = initiate;
        this.username = username;

    }

    public ChatSessionGUI(Socket socket, String username) {
        this(socket, username, true);
    }

    private Socket socket;

    @Override
    public void run() {
        frame = new JFrame();
        frame.setTitle("Chat with " + username);
        frame.setBounds(100, 100, 729, 606);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Metadata.getInstance().removeConnectedUser(username);
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
        panel_1.setLayout(null);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 20, 675, 319);
        panel_1.add(scrollPane);

        JTextPane txtChat = new JTextPane();
        txtChat.setEditable(false);
        scrollPane.setViewportView(txtChat);

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
                                    JOptionPane.showMessageDialog(frame, "File Request Denied");
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
                try {
                    StyledDocument doc = txtChat.getStyledDocument();

                    SimpleAttributeSet left = new SimpleAttributeSet();
                    StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
                    StyleConstants.setForeground(left, Color.RED);

                    SimpleAttributeSet right = new SimpleAttributeSet();
                    StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
                    StyleConstants.setForeground(right, Color.BLUE);
                    doc.insertString(doc.getLength(), textField.getText(), right );
                    doc.setParagraphAttributes(doc.getLength(), 1, right, false);
                    doc.insertString(doc.getLength(), "\n", right);
                    textField.setText("");
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        });
        JLabel lblFileName = new JLabel("");
        sl_panel.putConstraint(SpringLayout.NORTH, lblFileName, 430, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.WEST, lblFileName, 21, SpringLayout.WEST, panel);
        lblFileName.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panel.add(lblFileName);
        frame.setVisible(true);

        Metadata.getInstance().addConnectedUser(username, frame);

        writer = new SocketWriter(socket);
        writer.execute();
        if (initiate) writer.write("/REQUEST-SESSION " + Metadata.getInstance().getUsername());
        reader = new SocketReader(socket, frame, writer);
        reader.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StyledDocument doc = txtChat.getStyledDocument();

                SimpleAttributeSet left = new SimpleAttributeSet();
                StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
                StyleConstants.setForeground(left, Color.RED);

                SimpleAttributeSet right = new SimpleAttributeSet();
                StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
                StyleConstants.setForeground(right, Color.BLUE);
                String text = e.getActionCommand();
                try {
                    doc.insertString(doc.getLength(), text, left );
                    doc.setParagraphAttributes(doc.getLength(), 1, left, false);
                    doc.insertString(doc.getLength(), "\n", left);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
                doc.setParagraphAttributes(doc.getLength(), 1, left, false);
                txtChat.setCaretPosition(txtChat.getDocument().getLength());
            }
        });
        reader.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
                    btnSendFile.setEnabled(false);
                    btnSendFile.setDisabledIcon(btnSendFile.getIcon());
                    textField.setEditable(false);
                    writer.cancel(true); //cancel running writer task
                }
            }
        });
        reader.execute();
        System.out.println(socket.getRemoteSocketAddress().toString());
    }

    private void disconnect() {
        writer.write("/EXIT");
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

