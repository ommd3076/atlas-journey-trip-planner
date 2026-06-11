package ui.components;

import java.awt.*;
import javax.swing.*;
import ui.Theme;

public class RoundedCardPanel extends JPanel {

    private int arc = Theme.RADIUS;
    private Color fillColor = Theme.BG_CARD;
    private int shadowDepth = Theme.SHADOW_DEPTH;
    private boolean showShadow = true;

    public RoundedCardPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
        setBackground(fillColor);
        setBorder(Theme.PADDING_CARD);
    }

    public RoundedCardPanel(LayoutManager layout, int arc) {
        this(layout);
        this.arc = arc;
    }

    public void setFillColor(Color c) { this.fillColor = c; setBackground(c); }
    public void setShowShadow(boolean s) { this.showShadow = s; }
    public void setArc(int a) { this.arc = a; }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Theme.hq(g);
        int w = getWidth();
        int h = getHeight();
        if (showShadow) Theme.drawSoftShadow(g2, 0, 0, w, h, arc);
        Theme.fillRoundRect(g2, 0, 0, w, h, arc, fillColor);
        g2.dispose();
        super.paintComponent(g);
    }
}
