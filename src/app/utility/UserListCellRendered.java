package app.utility;

import javax.swing.*;
import java.awt.*;

public class UserListCellRendered extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof UserIP) {
            UserIP userIP = (UserIP)value;
            setText(userIP.getUsername());
            System.out.println(userIP.getUsername());
            if (userIP.getInetAddress() == null) {
                setEnabled(false);
                setDisabledIcon(this.getIcon());
            }
        }
        return this;
    }
}
