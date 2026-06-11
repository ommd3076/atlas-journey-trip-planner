package ui.components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import ui.Theme;

public class ContentHeader extends JPanel {

    public ContentHeader(String title, String subtitle) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(0, 0, Theme.PANEL_GAP, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.FONT_HEADING);
        titleLabel.setForeground(Theme.TEXT_HEADING);

        JLabel subLabel = new JLabel(subtitle);
        subLabel.setFont(Theme.FONT_BODY);
        subLabel.setForeground(Theme.TEXT_SECONDARY);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        textPanel.add(subLabel);

        add(textPanel, BorderLayout.WEST);
    }
}
