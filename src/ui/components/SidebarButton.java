package ui.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import ui.Theme;

public class SidebarButton extends JButton {

    private boolean active = false;
    private String iconText;
    private String label;
    private float hoverAlpha = 0f;
    private Timer hoverTimer;

    public SidebarButton(String iconText, String label) {
        this.iconText = iconText;
        this.label = label;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setHorizontalAlignment(SwingConstants.LEFT);
        setFont(Theme.FONT_SIDEBAR);
        setForeground(Theme.TEXT_SECONDARY);
        setPreferredSize(new Dimension(Theme.SIDEBAR_WIDTH - 24, 44));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!active) startHoverAnim(true);
            }
            public void mouseExited(MouseEvent e) {
                if (!active) startHoverAnim(false);
            }
        });
    }

    private void startHoverAnim(boolean in) {
        if (hoverTimer != null && hoverTimer.isRunning()) hoverTimer.stop();
        hoverTimer = new Timer(16, e -> {
            if (in) {
                hoverAlpha = Math.min(1f, hoverAlpha + 0.12f);
                if (hoverAlpha >= 1f) hoverTimer.stop();
            } else {
                hoverAlpha = Math.max(0f, hoverAlpha - 0.12f);
                if (hoverAlpha <= 0f) hoverTimer.stop();
            }
            repaint();
        });
        hoverTimer.start();
    }

    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            setForeground(Theme.PRIMARY);
            hoverAlpha = 1f;
        } else {
            setForeground(Theme.TEXT_SECONDARY);
            hoverAlpha = 0f;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Theme.hq(g);
        int w = getWidth();
        int h = getHeight();
        int arc = Theme.RADIUS_SM;

        Color bg;
        if (active) {
            bg = Theme.BG_SELECTED;
        } else if (hoverAlpha > 0) {
            bg = blend(Theme.BG_CARD, Theme.BG_HOVER, hoverAlpha);
        } else {
            bg = Theme.BG_CARD;
        }

        if (hoverAlpha > 0 || active) {
            Theme.fillRoundRect(g2, 8, 2, w - 16, h - 4, arc, bg);
        }

        if (active) {
            g2.setColor(Theme.PRIMARY);
            g2.fillRoundRect(4, 8, 4, h - 16, 2, 2);
        }

        g2.setColor(getForeground());
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(iconText, 20, (h + fm.getAscent() - fm.getDescent()) / 2);

        g2.setFont(getFont());
        fm = g2.getFontMetrics();
        g2.drawString(label, 50, (h + fm.getAscent() - fm.getDescent()) / 2);

        g2.dispose();
    }

    private Color blend(Color a, Color b, float ratio) {
        int r = (int) (a.getRed() * (1 - ratio) + b.getRed() * ratio);
        int gr = (int) (a.getGreen() * (1 - ratio) + b.getGreen() * ratio);
        int bl = (int) (a.getBlue() * (1 - ratio) + b.getBlue() * ratio);
        return new Color(r, gr, bl);
    }
}
