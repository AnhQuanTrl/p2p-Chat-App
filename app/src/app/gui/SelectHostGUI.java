package app.gui;
import app.utility.Metadata;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class SelectHostGUI implements Runnable {
    private JFrame frame;
    private JPanel contentPane;
    private JTextField txtHost;

    @Override
    public void run() {
        frame = new JFrame();
        frame.setTitle("Enter Server Host");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(100, 100, 450, 114);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblEnterHostIp = new JLabel("Enter Host IP:");
        lblEnterHostIp.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblEnterHostIp.setBounds(40, 23, 100, 35);
        contentPane.add(lblEnterHostIp);

        txtHost = new JTextField();
        txtHost.setBounds(150, 30, 232, 25);
        contentPane.add(txtHost);
        txtHost.setColumns(10);
        txtHost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Metadata.getInstance().setHostIP(txtHost.getText());
                SwingUtilities.invokeLater(new LoginGUI());
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        frame.setVisible(true);
    }

}
