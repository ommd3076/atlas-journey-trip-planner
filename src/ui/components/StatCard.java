package ui.components;

import java.awt.*;
import javax.swing.*;
import ui.Theme;

public class StatCard extends JPanel {

    private String value;
    private String label;
    private Color accentColor;
    private Color iconBgColor;

    public StatCard(String value, String label, Color accent) {
        this.value = value;
        this.label = label;
        this.accentColor = accent;
        this.iconBgColor = new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 18);
        setOpaque(false);
        setPreferredSize(new Dimension(200, 120));
    }

    public void updateValue(String v) { this.value = v; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Theme.hq(g);
        int w = getWidth();
        int h = getHeight();
        int arc = Theme.RADIUS;

        // Shadow
        Theme.drawSoftShadow(g2, 0, 0, w, h, arc);

        // Card fill
        Theme.fillRoundRect(g2, 0, 0, w, h, arc, Theme.BG_CARD);

        // Left accent bar
        g2.setColor(accentColor);
        g2.fillRoundRect(0, 20, 4, h - 40, 2, 2);

        // Icon circle
        int iconSize = Theme.ICON_CIRCLE_SIZE;
        int iconX = 22;
        int iconY = (h - iconSize) / 2;
        g2.setColor(iconBgColor);
        g2.fillOval(iconX, iconY, iconSize, iconSize);

        // Draw simple icon shapes inside the circle
        g2.setColor(accentColor);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int cx = iconX + iconSize / 2;
        int cy = iconY + iconSize / 2;

        // Draw a different icon shape based on the accent color
        if (accentColor.equals(Theme.PRIMARY)) {
            // Grid/attractions icon (4 dots)
            int dotR = 3;
            g2.fillOval(cx - 8, cy - 8, dotR * 2, dotR * 2);
            g2.fillOval(cx + 2, cy - 8, dotR * 2, dotR * 2);
            g2.fillOval(cx - 8, cy + 2, dotR * 2, dotR * 2);
            g2.fillOval(cx + 2, cy + 2, dotR * 2, dotR * 2);
        } else if (accentColor.equals(Theme.SUCCESS)) {
            // Map pin icon
            g2.drawOval(cx - 7, cy - 6, 14, 14);
            g2.fillOval(cx - 3, cy - 2, 6, 6);
        } else if (accentColor.equals(Theme.WARNING)) {
            // Gear icon (circle with inner circle)
            g2.drawOval(cx - 7, cy - 7, 14, 14);
            g2.drawOval(cx - 4, cy - 4, 8, 8);
        } else {
            // Default: lightning bolt (two lines)
            int[] xPts = {cx - 2, cx + 1, cx - 1, cx + 3};
            int[] yPts = {cy - 8, cy - 1, cy - 1, cy + 7};
            g2.drawPolyline(xPts, yPts, 4);
        }

        // Value
        g2.setColor(Theme.TEXT_HEADING);
        g2.setFont(Theme.FONT_METRIC_SM);
        FontMetrics fmV = g2.getFontMetrics();
        g2.drawString(value, iconX + iconSize + 16, h / 2 - 2);

        // Label
        g2.setColor(Theme.TEXT_SECONDARY);
        g2.setFont(Theme.FONT_SMALL);
        FontMetrics fmL = g2.getFontMetrics();
        g2.drawString(label, iconX + iconSize + 16, h / 2 + fmL.getAscent() + 4);

        g2.dispose();
    }
}
