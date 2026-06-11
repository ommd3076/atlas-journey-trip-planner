package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import ui.components.RoundedCardPanel;

public class LoginPanel extends JFrame {

    public LoginPanel() {
        setTitle("Trip Planner");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setSize(640, 520);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Theme.BG_APP);
        setLayout(new GridBagLayout());

        RoundedCardPanel card = new RoundedCardPanel(new BorderLayout(0, 0), Theme.RADIUS_LG);
        card.setPreferredSize(new Dimension(380, 420));
        card.setBorder(new EmptyBorder(44, 40, 36, 40));
        card.setShowShadow(true);

        // ── Top Section ─────────────────────────────────────────────────────
        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setOpaque(false);

        JLabel iconLabel = new JLabel("\u2708");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        iconLabel.setForeground(Theme.PRIMARY);
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSection.add(iconLabel);
        topSection.add(Box.createRigidArea(new Dimension(0, 12)));

        JLabel title = new JLabel("Trip Planner");
        title.setFont(Theme.FONT_DISPLAY);
        title.setForeground(Theme.TEXT_HEADING);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSection.add(title);
        topSection.add(Box.createRigidArea(new Dimension(0, 6)));

        JLabel subtitle = new JLabel("Plan your perfect journey");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSection.add(subtitle);

        card.add(topSection, BorderLayout.NORTH);

        // ── Form Section ────────────────────────────────────────────────────
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(16, 0, 0, 0));

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(Theme.FONT_SMALL_BOLD);
        userLabel.setForeground(Theme.TEXT_BODY);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(userLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        JTextField userField = new JTextField("admin");
        userField.setFont(Theme.FONT_BODY);
        userField.setPreferredSize(new Dimension(300, 42));
        userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        userField.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(Theme.BORDER, Theme.RADIUS_SM),
            new EmptyBorder(8, 14, 8, 14)
        ));
        userField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(userField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 14)));

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(Theme.FONT_SMALL_BOLD);
        passLabel.setForeground(Theme.TEXT_BODY);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        JPasswordField passField = new JPasswordField("admin");
        passField.setFont(Theme.FONT_BODY);
        passField.setPreferredSize(new Dimension(300, 42));
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        passField.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(Theme.BORDER, Theme.RADIUS_SM),
            new EmptyBorder(8, 14, 8, 14)
        ));
        passField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // ── Login Button ────────────────────────────────────────────────────
        JButton loginBtn = new JButton("Sign In");
        loginBtn.setFont(Theme.FONT_BUTTON);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBackground(Theme.PRIMARY);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setPreferredSize(new Dimension(300, 44));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setBorder(BorderFactory.createEmptyBorder());
        loginBtn.setOpaque(true);

        loginBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginBtn.setBackground(Theme.PRIMARY_DARK);
            }
            public void mouseExited(MouseEvent e) {
                loginBtn.setBackground(Theme.PRIMARY);
            }
            public void mousePressed(MouseEvent e) {
                loginBtn.setBackground(new Color(35, 80, 140));
            }
            public void mouseReleased(MouseEvent e) {
                loginBtn.setBackground(Theme.PRIMARY_DARK);
            }
        });

        ActionListener loginAction = e -> {
            String raw = userField.getText().trim();
            String user = raw.isEmpty() ? "Traveler" : raw;
            loginBtn.setEnabled(false);
            loginBtn.setText("Signing in...");
            SwingUtilities.invokeLater(() -> {
                try {
                    DashboardFrame dash = new DashboardFrame(user);
                    dash.setVisible(true);
                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    loginBtn.setEnabled(true);
                    loginBtn.setText("Sign In");
                    JOptionPane.showMessageDialog(LoginPanel.this,
                        "Error starting application: " + ex.getMessage(),
                        "Startup Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        };

        loginBtn.addActionListener(loginAction);
        formPanel.add(loginBtn);

        card.add(formPanel, BorderLayout.CENTER);
        add(card);

        // ── Enter key support ───────────────────────────────────────────────
        passField.addActionListener(loginAction);
        userField.addActionListener(e -> passField.requestFocusInWindow());

        // Focus password field on start
        SwingUtilities.invokeLater(() -> passField.requestFocusInWindow());
    }

    private static class RoundedBorder extends javax.swing.border.AbstractBorder {
        private Color color;
        private int radius;
        RoundedBorder(Color c, int r) { color = c; radius = r; }
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = Theme.hq(g);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            g2.dispose();
        }
        public Insets getBorderInsets(Component c) { return new Insets(2, 2, 2, 2); }
    }
}
