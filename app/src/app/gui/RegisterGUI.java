package app.gui;

import app.servercomm.RegisterWorker;
import app.utility.Metadata;

import javax.swing.*;
import java.awt.BorderLayout;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RegisterGUI implements Runnable{

	private JFrame frame;
	private JTextField textField;
	private JPasswordField passwordField;

	@Override
	public void run() {
		frame = new JFrame();
		frame.setTitle("Register");
		frame.setBounds(100, 100, 421, 192);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);

		JPanel panel_1 = new JPanel();
		sl_panel.putConstraint(SpringLayout.SOUTH, panel_1, 145, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, panel_1, 397, SpringLayout.WEST, panel);
		panel_1.setBorder(new TitledBorder(null, "Register", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		sl_panel.putConstraint(SpringLayout.NORTH, panel_1, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, panel_1, 10, SpringLayout.WEST, panel);
		panel.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(16, 25, 80, 20);
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel_1.add(lblUsername);

		textField = new JTextField();
		textField.setBounds(102, 23, 269, 25);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel_1.add(textField);
		textField.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(16, 58, 62, 19);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel_1.add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setBounds(102, 57, 269, 25);
		panel_1.add(passwordField);

		JButton btnRegister = new JButton("Register");
		btnRegister.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnRegister.setBounds(147, 92, 99, 34);
		panel_1.add(btnRegister);
		btnRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Socket socket = null;
				try {
					socket = new Socket();
					socket.connect(new InetSocketAddress(Metadata.getInstance().getHostIP(), 7000), 2000);
					String password = new String(passwordField.getPassword()); //nguy hiem
					String username = textField.getText();
					RegisterWorker registerWorker = new RegisterWorker(socket, username, password, frame);
					registerWorker.execute();
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(frame,"Host not found");
				}

			}
		});
		frame.setVisible(true);
	}
}
