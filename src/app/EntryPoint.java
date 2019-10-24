package app;
import app.gui.LoginGUI;
import javax.swing.*;

public class EntryPoint {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new LoginGUI());
    }
}
