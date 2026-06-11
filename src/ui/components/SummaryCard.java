package ui.components;

import java.awt.*;
import javax.swing.*;
import ui.Theme;

public class SummaryCard extends JPanel {

    private String value;
    private String label;
    private Color accentColor;
    private Color bgColor;

    public SummaryCard(String value, String label, Color accent) {
        this.value = value;
        this.label = label;
        this.accentColor = accent;
        this.bgColor = Theme.BG_CARD;
        setOpaque(false);
        setPreferredSize(new Dimension(200, 110));
    }

    public void updateValue(String v) { this.value = v; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Theme.hq(g);
        int w = getWidth();
        int h = getHeight();
        int arc = Theme.RADIUS;

        Theme.drawSoftShadow(g2, 0, 0, w, h, arc);
        Theme.fillRoundRect(g2, 0, 0, w, h, arc, bgColor);

        g2.setColor(accentColor);
        g2.fillRoundRect(16, 20, 4, h - 40, 2, 2);

        g2.setColor(Theme.TEXT_HEADING);
        g2.setFont(Theme.FONT_METRIC_SM);
        g2.drawString(value, 30, 50);

        g2.setColor(Theme.TEXT_SECONDARY);
        g2.setFont(Theme.FONT_SMALL);
        g2.drawString(label, 30, 72);

        g2.dispose();
    }
}
