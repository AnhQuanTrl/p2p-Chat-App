package app;
import app.gui.SelectHostGUI;

import javax.swing.*;

public class EntryPoint {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new SelectHostGUI());
    }
}
