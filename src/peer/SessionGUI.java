package peer;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.Socket;

public class SessionGUI implements Runnable {
    private JFrame frame;
    private JTextArea textArea;
    private JTextField textField;
    private SocketWriter writer;
    private SocketReader reader;
    private Boolean initiate;
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

        JButton btnDisconnect = new JButton("Disconect");
        sl_panel.putConstraint(SpringLayout.NORTH, btnDisconnect, 28, SpringLayout.SOUTH, textArea);
        sl_panel.putConstraint(SpringLayout.EAST, btnDisconnect, -166, SpringLayout.EAST, panel);
        panel.add(btnDisconnect);

        textField = new JTextField();
        sl_panel.putConstraint(SpringLayout.NORTH, textField, 5, SpringLayout.SOUTH, textArea);
        sl_panel.putConstraint(SpringLayout.WEST, textField, 0, SpringLayout.WEST, textArea);
        sl_panel.putConstraint(SpringLayout.EAST, textField, 0, SpringLayout.EAST, textArea);
        panel.add(textField);
        textField.setColumns(10);
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writer.write(textField.getText());
            }
        });
        frame.setVisible(true);
        reader = new SocketReader(socket, frame);
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
        writer = new SocketWriter(socket);
        writer.execute();
        if (initiate) writer.write("/REQUEST-SESSION");
        btnDisconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disconnect();
            }
        });
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
