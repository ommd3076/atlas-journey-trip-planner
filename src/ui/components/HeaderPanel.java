package ui.components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import ui.Theme;

public class HeaderPanel extends JPanel {

    private JLabel titleLabel;
    private String username;

    public HeaderPanel(String username) {
        this.username = username;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_CARD);
        setPreferredSize(new Dimension(0, Theme.TOPBAR_HEIGHT));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_LIGHT),
            new EmptyBorder(0, 24, 0, 24)
        ));

        // Left: page title
        titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(Theme.FONT_HEADING);
        titleLabel.setForeground(Theme.TEXT_HEADING);
        add(titleLabel, BorderLayout.WEST);

        // Right section: search + user chip
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightPanel.setOpaque(false);

        rightPanel.add(createSearchField());
        rightPanel.add(createUserChip());

        add(rightPanel, BorderLayout.EAST);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    private JPanel createSearchField() {
        JPanel searchWrapper = new JPanel(new BorderLayout());
        searchWrapper.setBackground(Theme.BG_INPUT);
        searchWrapper.setPreferredSize(new Dimension(220, 36));
        searchWrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1, true),
            new EmptyBorder(0, 10, 0, 10)
        ));

        JLabel searchIcon = new JLabel("\uD83D\uDD0D");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchIcon.setForeground(Theme.TEXT_MUTED);
        searchWrapper.add(searchIcon, BorderLayout.WEST);

        JTextField searchField = new JTextField();
        searchField.setFont(Theme.FONT_SEARCH);
        searchField.setForeground(Theme.TEXT_BODY);
        searchField.setBackground(new Color(0, 0, 0, 0));
        searchField.setBorder(null);
        searchField.setCaretColor(Theme.TEXT_BODY);
        searchWrapper.add(searchField, BorderLayout.CENTER);

        return searchWrapper;
    }

    private JPanel createUserChip() {
        JPanel chip = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        chip.setBackground(Theme.BG_INPUT);
        chip.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1, true),
            new EmptyBorder(5, 12, 5, 14)
        ));

        // Avatar circle
        JLabel avatar = new JLabel(String.valueOf(Character.toUpperCase(username.charAt(0))));
        avatar.setFont(Theme.FONT_SMALL_BOLD);
        avatar.setForeground(Theme.PRIMARY);
        avatar.setOpaque(true);
        avatar.setBackground(Theme.PRIMARY_PALE);
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(26, 26));
        avatar.setBorder(BorderFactory.createLineBorder(Theme.PRIMARY, 1, true));
        chip.add(avatar);

        // Username
        JLabel nameLabel = new JLabel(username);
        nameLabel.setFont(Theme.FONT_SMALL_BOLD);
        nameLabel.setForeground(Theme.TEXT_BODY);
        chip.add(nameLabel);

        return chip;
    }
}
