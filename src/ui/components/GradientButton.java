package ui.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import ui.Theme;

public class GradientButton extends JButton {

    private Color bgColor = Theme.PRIMARY;
    private Color hoverColor = Theme.PRIMARY_DARK;
    private Color pressColor = new Color(35, 80, 140);
    private Color textColor = Color.WHITE;
    private boolean hovered = false;
    private boolean pressed = false;

    public GradientButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setFont(Theme.FONT_BUTTON);
        setForeground(textColor);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(160, 42));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
            public void mouseExited(MouseEvent e) { hovered = false; pressed = false; repaint(); }
            public void mousePressed(MouseEvent e) { pressed = true; repaint(); }
            public void mouseReleased(MouseEvent e) { pressed = false; repaint(); }
        });
    }

    public void setButtonColor(Color bg, Color hover) {
        this.bgColor = bg;
        this.hoverColor = hover;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Theme.hq(g);
        int w = getWidth();
        int h = getHeight();
        int arc = Theme.RADIUS_SM;
        Color c = pressed ? pressColor : (hovered ? hoverColor : bgColor);
        Theme.fillRoundRect(g2, 0, 0, w, h, arc, c);
        g2.setColor(textColor);
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(getText());
        int th = fm.getAscent();
        g2.drawString(getText(), (w - tw) / 2, (h + th) / 2 - fm.getDescent() - 2);
        g2.dispose();
    }
}
