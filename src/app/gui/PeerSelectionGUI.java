package app.gui;

import app.peer.listener.Server;
import app.servercomm.FetchWorker;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class PeerSelectionGUI implements Runnable {

    private JFrame frame;
    private JTextField txtHost;
    private JTextField txtPort;
    private FetchWorker fetchWorker = null;
    public PeerSelectionGUI(String username) {
        this.username = username;
    }

    private String username;


    @Override
    public void run() {
        frame = new JFrame();
        frame.setTitle("Peer Selection");
        frame.setBounds(100, 100, 320, 492);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(10, 10, 286, 347);
        panel_1.setBorder(new TitledBorder(null, "Choose A Person To Connect", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.add(panel_1);
        SpringLayout sl_panel_1 = new SpringLayout();
        panel_1.setLayout(sl_panel_1);
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setFont(new Font("Tahoma", Font.PLAIN, 13));
        sl_panel_1.putConstraint(SpringLayout.NORTH, list, 10, SpringLayout.NORTH, panel_1);
        sl_panel_1.putConstraint(SpringLayout.WEST, list, 10, SpringLayout.WEST, panel_1);
        sl_panel_1.putConstraint(SpringLayout.SOUTH, list, 311, SpringLayout.NORTH, panel_1);
        sl_panel_1.putConstraint(SpringLayout.EAST, list, -6, SpringLayout.EAST, panel_1);
        list.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        panel_1.add(list);

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
        frame.setVisible(true);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList list = (JList) e.getSource();
                int index = list.locationToIndex(e.getPoint());
                String user = listModel.get(index);
                String[] args = user.split(",");
                Socket socket = null;
                try {
                    socket = new Socket(args[1], 8989);
                    SwingUtilities.invokeLater(new ChatSessionGUI(socket));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        Socket socket = null;
        try {
            socket = new Socket("192.168.100.103", 7000);
            fetchWorker = new FetchWorker(socket, frame, username);
            fetchWorker.execute();
            fetchWorker.addActionListeners(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    listModel.clear();
                    String[] users = e.getActionCommand().split(" ");
                    for (String user : users) {
                        String[] args = user.split(",");
                        if (!args[0].equals(username)) {
                            listModel.addElement(user);
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnLogOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fetchWorker != null) fetchWorker.setCancel(true);
            }
        });
    }
}
//        frame = new JFrame("Connect");
//        frame.setBounds(100, 100, 450, 300);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        JPanel panel = new JPanel();
//        frame.getContentPane().add(panel);
//        SpringLayout sl_panel = new SpringLayout();
//        panel.setLayout(sl_panel);
//
//        JLabel lblHost = new JLabel("Enter host");
//        sl_panel.putConstraint(SpringLayout.NORTH, lblHost, 60, SpringLayout.NORTH, panel);
//        sl_panel.putConstraint(SpringLayout.WEST, lblHost, 82, SpringLayout.WEST, panel);
//        panel.add(lblHost);
//
//        txtHost = new JTextField();
//        sl_panel.putConstraint(SpringLayout.NORTH, txtHost, 0, SpringLayout.NORTH, lblHost);
//        sl_panel.putConstraint(SpringLayout.WEST, txtHost, 52, SpringLayout.EAST, lblHost);
//        panel.add(txtHost);
//        txtHost.setColumns(10);
//
//        JLabel lblPort = new JLabel("Enter port");
//        sl_panel.putConstraint(SpringLayout.NORTH, lblPort, 64, SpringLayout.SOUTH, lblHost);
//        sl_panel.putConstraint(SpringLayout.EAST, lblPort, 0, SpringLayout.EAST, lblHost);
//        panel.add(lblPort);
//
//        txtPort = new JTextField();
//        sl_panel.putConstraint(SpringLayout.WEST, txtPort, 0, SpringLayout.WEST, txtHost);
//        sl_panel.putConstraint(SpringLayout.SOUTH, txtPort, 0, SpringLayout.SOUTH, lblPort);
//        panel.add(txtPort);
//        txtPort.setColumns(10);
//
//        JButton btnSubmit = new JButton("Submit");
//        btnSubmit.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    newSession();
//                } catch (NumberFormatException ex) {
//                    ex.printStackTrace();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });
//        sl_panel.putConstraint(SpringLayout.WEST, btnSubmit, 144, SpringLayout.WEST, panel);
//        sl_panel.putConstraint(SpringLayout.SOUTH, btnSubmit, -31, SpringLayout.SOUTH, panel);
//        panel.add(btnSubmit);
//        frame.setVisible(true);
//
