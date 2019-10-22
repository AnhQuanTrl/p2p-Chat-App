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

	private JFrame frmChatRoom;
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
					window.frmChatRoom.setVisible(true);
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
		frmChatRoom = new JFrame();
		frmChatRoom.setTitle("Chat");
		frmChatRoom.setBounds(100, 100, 729, 606);
		frmChatRoom.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JFileChooser fileChooser = new JFileChooser();
		//frame.getContentPane().add(fileChooser, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setName("pane");
		panel.setToolTipText("");
		frmChatRoom.getContentPane().add(panel);
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
		btnBrowseFile.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(btnBrowseFile);
		
		JButton btnSendFile = new JButton("Send File");
		sl_panel.putConstraint(SpringLayout.NORTH, btnSendFile, 430, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, btnSendFile, 570, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnSendFile, 459, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, btnSendFile, 689, SpringLayout.WEST, panel);
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
		
		JLabel lblFileName = new JLabel("");
		sl_panel.putConstraint(SpringLayout.NORTH, lblFileName, 430, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, lblFileName, 21, SpringLayout.WEST, panel);
		lblFileName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(lblFileName);
		
	}
}
