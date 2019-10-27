package app.gui;

import app.servercomm.LogInWorker;
import app.utility.Metadata;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class LoginGUI implements Runnable {
    private JFrame frame;
    private JTextField textUsername;
    private JPasswordField passwordField;
    private JTextField txtPort;
    @Override
    public void run() {
        frame = new JFrame();
        frame.setTitle("Log In");
        frame.setBounds(100, 100, 632, 310);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        SpringLayout sl_panel = new SpringLayout();
        panel.setLayout(sl_panel);

        JPanel panel_1 = new JPanel();
        sl_panel.putConstraint(SpringLayout.NORTH, panel_1, 10, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.WEST, panel_1, 10, SpringLayout.WEST, panel);
        sl_panel.putConstraint(SpringLayout.SOUTH, panel_1, 170, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.EAST, panel_1, 608, SpringLayout.WEST, panel);
        panel_1.setBorder(new TitledBorder(null, "Log In", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_1.setFont(new Font("Times New Roman", Font.PLAIN, 17));
        panel_1.setPreferredSize(new Dimension(100, 100));
        panel_1.setSize(new Dimension(100, 100));
        panel.add(panel_1);
        panel_1.setLayout(null);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblUsername.setBounds(45, 24, 84, 27);
        panel_1.add(lblUsername);

        textUsername = new JTextField();
        textUsername.setFont(new Font("Tahoma", Font.PLAIN, 15));
        textUsername.setBounds(135, 25, 393, 25);
        panel_1.add(textUsername);
        textUsername.setColumns(10);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblPassword.setBounds(45, 65, 84, 27);
        panel_1.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 15));
        passwordField.setBounds(135, 65, 393, 25);
        panel_1.add(passwordField);

        JButton btnLogIn = new JButton("Log In");
        btnLogIn.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnLogIn.setBounds(250, 100, 100, 40);
        btnLogIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(Metadata.getInstance().getHostIP(), 7000), 2000);
                    String username = textUsername.getText();
                    String password = new String(passwordField.getPassword());
                    LogInWorker logInWorker = new LogInWorker(socket, username, password, frame);
                    logInWorker.execute();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Host doesnt exist");
                }

            }
        });
        panel_1.add(btnLogIn);

        JPanel panel_2 = new JPanel();
        sl_panel.putConstraint(SpringLayout.NORTH, panel_2, 6, SpringLayout.SOUTH, panel_1);
        sl_panel.putConstraint(SpringLayout.WEST, panel_2, 10, SpringLayout.WEST, panel);
        sl_panel.putConstraint(SpringLayout.SOUTH, panel_2, 93, SpringLayout.SOUTH, panel_1);
        sl_panel.putConstraint(SpringLayout.EAST, panel_2, 0, SpringLayout.EAST, panel_1);
        panel_2.setBorder(new TitledBorder(null, "Don't have an account?", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.add(panel_2);
        SpringLayout sl_panel_2 = new SpringLayout();
        panel_2.setLayout(sl_panel_2);

        JButton btnRegisterNow = new JButton("Register Now");
        sl_panel_2.putConstraint(SpringLayout.NORTH, btnRegisterNow, 10, SpringLayout.NORTH, panel_2);
        sl_panel_2.putConstraint(SpringLayout.WEST, btnRegisterNow, -367, SpringLayout.EAST, panel_2);
        sl_panel_2.putConstraint(SpringLayout.SOUTH, btnRegisterNow, -15, SpringLayout.SOUTH, panel_2);
        sl_panel_2.putConstraint(SpringLayout.EAST, btnRegisterNow, -218, SpringLayout.EAST, panel_2);
        btnRegisterNow.setFont(new Font("Tahoma", Font.PLAIN, 17));
        btnRegisterNow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new RegisterGUI());
            }
        });
        panel_2.add(btnRegisterNow);
        frame.setVisible(true);
    }
}
