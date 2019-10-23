import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.SpringLayout;
import javax.swing.JList;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;

public class PeerSelectionGUI {

	private JFrame frmPeerSelection;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PeerSelectionGUI window = new PeerSelectionGUI();
					window.frmPeerSelection.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PeerSelectionGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPeerSelection = new JFrame();
		frmPeerSelection.setTitle("Peer Selection");
		frmPeerSelection.setBounds(100, 100, 320, 492);
		frmPeerSelection.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmPeerSelection.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 10, 286, 347);
		panel_1.setBorder(new TitledBorder(null, "Choose A Person To Connect", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(panel_1);
		
	
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Log Out", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(10, 368, 286, 76);
		panel.add(panel_2);
		SpringLayout sl_panel_2 = new SpringLayout();
		panel_2.setLayout(sl_panel_2);
		
		JButton btnLogOut = new JButton("Log Out");
		sl_panel_2.putConstraint(SpringLayout.NORTH, btnLogOut, 10, SpringLayout.NORTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.WEST, btnLogOut, 10, SpringLayout.WEST, panel_2);
		sl_panel_2.putConstraint(SpringLayout.SOUTH, btnLogOut, -6, SpringLayout.SOUTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.EAST, btnLogOut, -12, SpringLayout.EAST, panel_2);
		btnLogOut.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_2.add(btnLogOut);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 20, 266, 317);
		panel_1.add(scrollPane);
		
		JList list = new JList();
		scrollPane.setViewportView(list);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setFont(new Font("Tahoma", Font.PLAIN, 13));
		list.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		

	}
}
