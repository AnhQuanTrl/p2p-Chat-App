package app.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class MainGUI implements  Runnable{
    private JFrame frame;
    private JTextField txtPort;
    @Override
    public void run() {
        frame = new JFrame("New Chat Session");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        SpringLayout sl_panel = new SpringLayout();
        panel.setLayout(sl_panel);

        JLabel lblPort = new JLabel("Enter port:");
        sl_panel.putConstraint(SpringLayout.NORTH, lblPort, 82, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.WEST, lblPort, 100, SpringLayout.WEST, panel);
        panel.add(lblPort);

        txtPort = new JTextField();
        sl_panel.putConstraint(SpringLayout.NORTH, txtPort, 0, SpringLayout.NORTH, lblPort);
        sl_panel.putConstraint(SpringLayout.WEST, txtPort, 38, SpringLayout.EAST, lblPort);
        panel.add(txtPort);
        txtPort.setColumns(10);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    listen();
                } catch(NumberFormatException ex) {
                    txtPort.setText("not a valid number");
                }
            }
        });
        sl_panel.putConstraint(SpringLayout.NORTH, btnSubmit, 52, SpringLayout.SOUTH, txtPort);
        sl_panel.putConstraint(SpringLayout.WEST, btnSubmit, 143, SpringLayout.WEST, panel);
        panel.add(btnSubmit);
        frame.setVisible(true);
    }

    private void listen() throws NumberFormatException {
        String text = txtPort.getText();
        if (text.equals("")) {
            txtPort.setText("cant be null");
            return;
        }
        int port = Integer.parseInt(text);
        SwingUtilities.invokeLater(new PeerSelectionGUI(port));
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

    }
}
