package peer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConnectGUI implements Runnable {

    private JFrame frame;
    private int port;
    private JTextField txtHost;
    private JTextField txtPort;
    private Server server;

    public ConnectGUI(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        frame = new JFrame("Connect");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);
        SpringLayout sl_panel = new SpringLayout();
        panel.setLayout(sl_panel);

        JLabel lblHost = new JLabel("Enter host");
        sl_panel.putConstraint(SpringLayout.NORTH, lblHost, 60, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.WEST, lblHost, 82, SpringLayout.WEST, panel);
        panel.add(lblHost);

        txtHost = new JTextField();
        sl_panel.putConstraint(SpringLayout.NORTH, txtHost, 0, SpringLayout.NORTH, lblHost);
        sl_panel.putConstraint(SpringLayout.WEST, txtHost, 52, SpringLayout.EAST, lblHost);
        panel.add(txtHost);
        txtHost.setColumns(10);

        JLabel lblPort = new JLabel("Enter port");
        sl_panel.putConstraint(SpringLayout.NORTH, lblPort, 64, SpringLayout.SOUTH, lblHost);
        sl_panel.putConstraint(SpringLayout.EAST, lblPort, 0, SpringLayout.EAST, lblHost);
        panel.add(lblPort);

        txtPort = new JTextField();
        sl_panel.putConstraint(SpringLayout.WEST, txtPort, 0, SpringLayout.WEST, txtHost);
        sl_panel.putConstraint(SpringLayout.SOUTH, txtPort, 0, SpringLayout.SOUTH, lblPort);
        panel.add(txtPort);
        txtPort.setColumns(10);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    newSession();
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        sl_panel.putConstraint(SpringLayout.WEST, btnSubmit, 144, SpringLayout.WEST, panel);
        sl_panel.putConstraint(SpringLayout.SOUTH, btnSubmit, -31, SpringLayout.SOUTH, panel);
        panel.add(btnSubmit);
        frame.setVisible(true);
        Server server = new Server(port);
        server.execute();
    }

    private void newSession() throws NumberFormatException, IOException {
        InetAddress host = InetAddress.getByName(txtHost.getText());
        String text = txtPort.getText();
        int port = Integer.parseInt(text);
        Socket socket = new Socket(host, port);
        SwingUtilities.invokeLater(new SessionGUI(socket));
    }
}
