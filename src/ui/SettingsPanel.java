package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import ui.components.*;

public class SettingsPanel extends AnimatedPanel {

    public SettingsPanel() {
        super(new BorderLayout(0, 0));

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setOpaque(false);

        main.add(new ContentHeader("Settings", "Manage your application preferences"));
        main.add(Box.createRigidArea(new Dimension(0, Theme.PANEL_GAP)));
        main.add(createAppearanceSection());
        main.add(Box.createRigidArea(new Dimension(0, Theme.PANEL_GAP)));
        main.add(createAboutSection());

        JScrollPane scroll = new JScrollPane(main);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        animateIn();
    }

    private RoundedCardPanel createAppearanceSection() {
        RoundedCardPanel card = new RoundedCardPanel(new BorderLayout(0, 16));

        JLabel title = new JLabel("Appearance");
        title.setFont(Theme.FONT_SUBHEADING);
        title.setForeground(Theme.TEXT_HEADING);
        card.add(title, BorderLayout.NORTH);

        JPanel options = new JPanel();
        options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));
        options.setOpaque(false);

        options.add(createToggleRow("Dark Mode", "Switch to dark theme (coming soon)", false));
        options.add(Box.createRigidArea(new Dimension(0, 12)));
        options.add(createToggleRow("Compact View", "Reduce spacing and padding", false));
        options.add(Box.createRigidArea(new Dimension(0, 12)));
        options.add(createToggleRow("Animations", "Enable smooth panel transitions", true));

        card.add(options, BorderLayout.CENTER);
        return card;
    }

    private RoundedCardPanel createAboutSection() {
        RoundedCardPanel card = new RoundedCardPanel(new BorderLayout(0, 16));

        JLabel title = new JLabel("About");
        title.setFont(Theme.FONT_SUBHEADING);
        title.setForeground(Theme.TEXT_HEADING);
        card.add(title, BorderLayout.NORTH);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);

        info.add(createInfoRow("Application", "Trip Planner"));
        info.add(Box.createRigidArea(new Dimension(0, 8)));
        info.add(createInfoRow("Version", "1.0.0"));
        info.add(Box.createRigidArea(new Dimension(0, 8)));
        info.add(createInfoRow("Algorithms", "Knapsack, Dijkstra, Heap Sort"));
        info.add(Box.createRigidArea(new Dimension(0, 8)));
        info.add(createInfoRow("Database", "SQLite (offline)"));

        card.add(info, BorderLayout.CENTER);
        return card;
    }

    private JPanel createToggleRow(String label, String desc, boolean enabled) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        row.setBorder(new EmptyBorder(8, 0, 8, 0));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.FONT_BODY_BOLD);
        lbl.setForeground(Theme.TEXT_HEADING);
        textPanel.add(lbl);

        JLabel descLbl = new JLabel(desc);
        descLbl.setFont(Theme.FONT_SMALL);
        descLbl.setForeground(Theme.TEXT_SECONDARY);
        textPanel.add(descLbl);

        row.add(textPanel, BorderLayout.CENTER);

        JCheckBox toggle = new JCheckBox();
        toggle.setSelected(enabled);
        toggle.setEnabled(false); // placeholder
        row.add(toggle, BorderLayout.EAST);

        return row;
    }

    private JPanel createInfoRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.FONT_BODY);
        lbl.setForeground(Theme.TEXT_SECONDARY);
        row.add(lbl, BorderLayout.WEST);

        JLabel val = new JLabel(value);
        val.setFont(Theme.FONT_BODY_BOLD);
        val.setForeground(Theme.TEXT_HEADING);
        row.add(val, BorderLayout.EAST);

        return row;
    }
}
