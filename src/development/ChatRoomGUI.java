import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.SpringLayout;
import javax.swing.JRadioButton;
import javax.swing.JEditorPane;
import javax.swing.JLayeredPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JSeparator;
import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Component;
import javax.swing.UIManager;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class ChatRoomGUI {

	private JFrame frame;
	private JTextField textField;
	private File file;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatRoomGUI window = new ChatRoomGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChatRoomGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 729, 594);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
		sl_panel.putConstraint(SpringLayout.WEST, panel_1, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, panel_1, -10, SpringLayout.EAST, panel);
		panel_1.setBorder(new TitledBorder(null, "Chat Room", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		sl_panel.putConstraint(SpringLayout.NORTH, panel_1, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, panel_1, 356, SpringLayout.NORTH, panel);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JTextArea txtChat = new JTextArea();
		txtChat.setBounds(6, 15, 679, 321);
		sl_panel.putConstraint(SpringLayout.NORTH, txtChat, 64, SpringLayout.NORTH, panel_1);
		sl_panel.putConstraint(SpringLayout.WEST, txtChat, 91, SpringLayout.WEST, panel_1);
		sl_panel.putConstraint(SpringLayout.SOUTH, txtChat, 1069, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, txtChat, 2052, SpringLayout.EAST, panel);
		panel_1.add(txtChat);
		txtChat.setEditable(false);
		
		JLabel lblChosenFile = new JLabel("Chosen File");
		sl_panel.putConstraint(SpringLayout.WEST, lblChosenFile, 21, SpringLayout.WEST, panel);
		lblChosenFile.setFont(new Font("Tahoma", Font.PLAIN, 17));
		panel.add(lblChosenFile);
		
		JButton btnBrowseFile = new JButton("Browse File");
		btnBrowseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnBrowseFile)
				{
					int returnVal = fileChooser.showOpenDialog(panel);
					 if (returnVal == JFileChooser.APPROVE_OPTION) {
					       	file = fileChooser.getSelectedFile();
					       	lblChosenFile.setText(file.getName());
					 }
				}
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, lblChosenFile, 4, SpringLayout.NORTH, btnBrowseFile);
		btnBrowseFile.setFont(new Font("Tahoma", Font.PLAIN, 17));
		panel.add(btnBrowseFile);
		
		JButton btnSendFile = new JButton("Send File");
		btnSendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (file.exists())
				{
					// Do something with file
					
					file = null;
				}
				else
				{
					// Do something if cannot find file
				}
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnSendFile, 430, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, btnSendFile, 586, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnSendFile, -98, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, btnSendFile, -10, SpringLayout.EAST, panel);
		sl_panel.putConstraint(SpringLayout.WEST, btnBrowseFile, 0, SpringLayout.WEST, btnSendFile);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnBrowseFile, -2, SpringLayout.NORTH, btnSendFile);
		btnSendFile.setMinimumSize(new Dimension(83, 21));
		btnSendFile.setMaximumSize(new Dimension(83, 21));
		btnSendFile.setPreferredSize(new Dimension(83, 21));
		btnSendFile.setFont(new Font("Tahoma", Font.PLAIN, 17));
		panel.add(btnSendFile);
		
		JSeparator separator_1 = new JSeparator();
		sl_panel.putConstraint(SpringLayout.NORTH, separator_1, 380, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, separator_1, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, separator_1, -17, SpringLayout.NORTH, btnBrowseFile);
		sl_panel.putConstraint(SpringLayout.EAST, separator_1, 0, SpringLayout.EAST, panel_1);
		panel.add(separator_1);
		
		JPanel panel_2 = new JPanel();
		sl_panel.putConstraint(SpringLayout.NORTH, panel_2, 6, SpringLayout.SOUTH, btnSendFile);
		sl_panel.putConstraint(SpringLayout.WEST, panel_2, 0, SpringLayout.WEST, panel_1);
		sl_panel.putConstraint(SpringLayout.SOUTH, panel_2, 0, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, panel_2, 0, SpringLayout.EAST, panel_1);
		panel_2.setBorder(new TitledBorder(null, "Say Something", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(10, 16, 566, 66);
		panel_2.add(textField);
		sl_panel.putConstraint(SpringLayout.NORTH, textField, 473, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, textField, -10, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, textField, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, textField, -160, SpringLayout.EAST, panel);
		textField.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Do something on send message
			}
		});
		btnSend.setBounds(586, 16, 99, 66);
		panel_2.add(btnSend);
		sl_panel.putConstraint(SpringLayout.EAST, btnSend, -14, SpringLayout.EAST, panel);
		btnSend.setFont(new Font("Tahoma", Font.PLAIN, 15));
		sl_panel.putConstraint(SpringLayout.NORTH, btnSend, 4, SpringLayout.NORTH, textField);
		sl_panel.putConstraint(SpringLayout.WEST, btnSend, 6, SpringLayout.EAST, textField);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnSend, 0, SpringLayout.SOUTH, textField);
		
		JLabel lblFileName = new JLabel("");
		lblFileName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		sl_panel.putConstraint(SpringLayout.NORTH, lblFileName, 6, SpringLayout.SOUTH, lblChosenFile);
		sl_panel.putConstraint(SpringLayout.WEST, lblFileName, 0, SpringLayout.WEST, lblChosenFile);
		panel.add(lblFileName);
		
	}
}
