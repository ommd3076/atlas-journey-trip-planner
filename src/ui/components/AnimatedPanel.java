package ui.components;

import java.awt.*;
import javax.swing.*;
import ui.Theme;

public class AnimatedPanel extends JPanel {

    private float alpha = 0f;
    private int slideOffset = 20;
    private Timer animTimer;
    private Runnable onShow;

    public AnimatedPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }

    public void animateIn() {
        alpha = 0f;
        slideOffset = 20;
        if (animTimer != null && animTimer.isRunning()) animTimer.stop();
        animTimer = new Timer(16, e -> {
            alpha = Math.min(1f, alpha + 0.06f);
            slideOffset = (int) (slideOffset * 0.88f);
            if (alpha >= 1f) {
                alpha = 1f;
                slideOffset = 0;
                animTimer.stop();
                if (onShow != null) onShow.run();
            }
            repaint();
        });
        animTimer.start();
    }

    public void setOnShow(Runnable r) { this.onShow = r; }

    public float getAlpha() { return alpha; }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Theme.hq(g);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.translate(slideOffset, 0);
        super.paintComponent(g);
        g2.dispose();
    }
}
