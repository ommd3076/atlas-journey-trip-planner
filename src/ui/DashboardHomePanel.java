package ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import ui.components.*;

public class DashboardHomePanel extends AnimatedPanel {

    private DashboardFrame frame;

    public DashboardHomePanel(DashboardFrame frame) {
        super(new BorderLayout(0, 0));
        this.frame = frame;
        setBorder(new EmptyBorder(0, 0, 0, 0));

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setOpaque(false);

        main.add(createHeroCard());
        main.add(Box.createRigidArea(new Dimension(0, Theme.PANEL_GAP)));
        main.add(createStatsRow());
        main.add(Box.createRigidArea(new Dimension(0, Theme.PANEL_GAP)));
        main.add(createBottomRow());

        JScrollPane scroll = new JScrollPane(main);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        animateIn();
    }

    // ── Hero Card ──────────────────────────────────────────────────────────────

    private JPanel createHeroCard() {
        JPanel hero = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = Theme.hq(g);
                int w = getWidth();
                int h = getHeight();
                int arc = Theme.RADIUS_LG;

                // Shadow
                Theme.drawSoftShadow(g2, 0, 0, w, h, arc);

                // Gradient background
                GradientPaint gp = new GradientPaint(0, 0, Theme.HERO_START, w, h, Theme.HERO_END);
                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));

                // Decorative circles (subtle)
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillOval(w - 120, -40, 200, 200);
                g2.fillOval(w - 60, h - 60, 140, 140);
                g2.fillOval(-30, h - 80, 120, 120);

                g2.dispose();
            }
        };
        hero.setLayout(new BorderLayout(24, 0));
        hero.setOpaque(false);
        hero.setPreferredSize(new Dimension(0, 170));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
        hero.setBorder(new EmptyBorder(28, 32, 28, 32));

        // Left text
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel welcome = new JLabel("Welcome back, Traveler!");
        welcome.setFont(Theme.FONT_DISPLAY);
        welcome.setForeground(Color.WHITE);
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(welcome);

        textPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel subtitle = new JLabel("Plan your perfect trip with AI-powered optimization");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(new Color(191, 219, 254));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(subtitle);

        hero.add(textPanel, BorderLayout.WEST);

        // Right chips
        JPanel chipPanel = new JPanel();
        chipPanel.setLayout(new BoxLayout(chipPanel, BoxLayout.Y_AXIS));
        chipPanel.setOpaque(false);
        chipPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JPanel chipRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        chipRow.setOpaque(false);
        chipRow.add(createHeroChip("3 Algorithms"));
        chipRow.add(createHeroChip("12 Attractions"));
        chipPanel.add(chipRow);

        hero.add(chipPanel, BorderLayout.EAST);

        return hero;
    }

    private JLabel createHeroChip(String text) {
        JLabel chip = new JLabel("  " + text + "  ");
        chip.setFont(Theme.FONT_SMALL_BOLD);
        chip.setForeground(Color.WHITE);
        chip.setOpaque(true);
        chip.setBackground(new Color(255, 255, 255, 25));
        chip.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1, true));
        return chip;
    }

    // ── Stats Row ──────────────────────────────────────────────────────────────

    private JPanel createStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, Theme.PANEL_GAP, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        row.add(new StatCard("12", "Attractions", Theme.PRIMARY));
        row.add(new StatCard("11", "Locations", Theme.SUCCESS));
        row.add(new StatCard("3", "Algorithms", Theme.WARNING));
        row.add(new StatCard("100%", "Offline Ready", Theme.PURPLE));

        return row;
    }

    // ── Bottom Row: Map Preview + Quick Actions ────────────────────────────────

    private JPanel createBottomRow() {
        JPanel row = new JPanel(new BorderLayout(Theme.PANEL_GAP, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        row.add(createMapPreviewCard(), BorderLayout.CENTER);
        row.add(createQuickActionsCard(), BorderLayout.EAST);

        return row;
    }

    // ── Map Preview Card ───────────────────────────────────────────────────────

    private RoundedCardPanel createMapPreviewCard() {
        RoundedCardPanel card = new RoundedCardPanel(new BorderLayout(0, 0));
        card.setArc(Theme.RADIUS);

        JPanel headerBar = new JPanel(new BorderLayout());
        headerBar.setOpaque(false);
        headerBar.setBorder(new EmptyBorder(4, 4, 4, 4));

        JLabel mapTitle = new JLabel("  Map Overview");
        mapTitle.setFont(Theme.FONT_BODY_BOLD);
        mapTitle.setForeground(Theme.TEXT_HEADING);
        headerBar.add(mapTitle, BorderLayout.WEST);

        card.add(headerBar, BorderLayout.NORTH);

        MapPanel miniMap = new MapPanel();
        miniMap.setPreferredSize(new Dimension(0, 240));
        card.add(miniMap, BorderLayout.CENTER);

        return card;
    }

    // ── Quick Actions Card ─────────────────────────────────────────────────────

    private RoundedCardPanel createQuickActionsCard() {
        RoundedCardPanel card = new RoundedCardPanel(new BorderLayout(0, 12));
        card.setPreferredSize(new Dimension(320, 0));
        card.setArc(Theme.RADIUS);

        JLabel title = new JLabel("Quick Actions");
        title.setFont(Theme.FONT_SUBHEADING);
        title.setForeground(Theme.TEXT_HEADING);
        card.add(title, BorderLayout.NORTH);

        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));
        actions.setOpaque(false);

        actions.add(createActionCard("Budget Planner", "Optimize budget with 0-1 Knapsack", "\u2261", Theme.PRIMARY, "trip"));
        actions.add(Box.createRigidArea(new Dimension(0, 8)));
        actions.add(createActionCard("Route Planner", "Find shortest path with Dijkstra", "\u2316", Theme.SUCCESS, "route"));
        actions.add(Box.createRigidArea(new Dimension(0, 8)));
        actions.add(createActionCard("Rankings", "View attractions by Heap Sort", "\u2605", Theme.WARNING, "rankings"));

        card.add(actions, BorderLayout.CENTER);

        return card;
    }

    private JPanel createActionCard(String title, String desc, String icon, Color accent, String panel) {
        JPanel actionCard = new JPanel(new BorderLayout(14, 0)) {
            private boolean hovered = false;
            {
                setOpaque(true);
                setBackground(Theme.BG_INPUT);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Theme.BORDER_LIGHT, 1, true),
                    new EmptyBorder(14, 16, 14, 16)
                ));
                addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        String title = "";
                        switch (panel) {
                            case "trip": title = "Budget Planner"; break;
                            case "route": title = "Route Planner"; break;
                            case "rankings": title = "Rankings"; break;
                        }
                        frame.showPanel(panel, title);
                    }
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        setBackground(Theme.BG_HOVER);
                        setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(accent, 1, true),
                            new EmptyBorder(14, 16, 14, 16)
                        ));
                    }
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        setBackground(Theme.BG_INPUT);
                        setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Theme.BORDER_LIGHT, 1, true),
                            new EmptyBorder(14, 16, 14, 16)
                        ));
                    }
                });
            }
        };

        // Icon circle
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        iconLabel.setForeground(accent);
        iconLabel.setOpaque(true);
        Color iconBg = new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 18);
        iconLabel.setBackground(iconBg);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(40, 40));
        iconLabel.setBorder(BorderFactory.createLineBorder(iconBg, 1, true));
        actionCard.add(iconLabel, BorderLayout.WEST);

        // Text
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.FONT_BODY_BOLD);
        titleLabel.setForeground(Theme.TEXT_HEADING);
        textPanel.add(titleLabel);

        JLabel descLabel = new JLabel("<html>" + desc + "</html>");
        descLabel.setFont(Theme.FONT_SMALL);
        descLabel.setForeground(Theme.TEXT_SECONDARY);
        textPanel.add(descLabel);

        actionCard.add(textPanel, BorderLayout.CENTER);

        return actionCard;
    }
}
