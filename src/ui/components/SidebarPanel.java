package ui.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import ui.Theme;

public class SidebarPanel extends JPanel {

    public interface NavigationListener {
        void onNavigate(String panelName, String title);
    }

    private NavigationListener listener;
    private String activePanel = "home";
    private java.util.List<NavItem> navItems = new java.util.ArrayList<>();

    public SidebarPanel() {
        setLayout(null); // absolute layout for full paint control
        setBackground(Theme.BG_SIDEBAR);
        setPreferredSize(new Dimension(Theme.SIDEBAR_WIDTH, 0));
        setOpaque(true);

        // Navigation items: {icon, label, panelName}
        navItems.add(new NavItem("\u2302", "Dashboard", "home"));
        navItems.add(new NavItem("\u2708", "Trip Planner", "trip"));
        navItems.add(new NavItem("\u2316", "Route Planner", "route"));
        navItems.add(new NavItem("\u2605", "Rankings", "rankings"));
        // separator
        navItems.add(new NavItem(null, null, null)); // separator marker
        navItems.add(new NavItem("\u2699", "Settings", "settings"));

        addMouseListener(new MouseAdapter() {});
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                int y = e.getY();
                for (NavItem item : navItems) {
                    if (item.isSeparator) continue;
                    boolean wasHovered = item.hovered;
                    item.hovered = (y >= item.y && y < item.y + item.height);
                    if (wasHovered != item.hovered) repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int y = e.getY();
                for (NavItem item : navItems) {
                    if (item.isSeparator) continue;
                    if (y >= item.y && y < item.y + item.height) {
                        setActive(item.panelName);
                        if (listener != null) listener.onNavigate(item.panelName, item.label);
                        break;
                    }
                }
            }
            public void mouseExited(MouseEvent e) {
                for (NavItem item : navItems) {
                    if (!item.isSeparator) item.hovered = false;
                }
                repaint();
            }
        });
    }

    public void setNavigationListener(NavigationListener l) { this.listener = l; }

    public void setActive(String panelName) {
        this.activePanel = panelName;
        for (NavItem item : navItems) {
            if (!item.isSeparator) item.active = item.panelName.equals(panelName);
        }
        repaint();
    }

    public String getActivePanel() { return activePanel; }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Theme.SIDEBAR_WIDTH, super.getPreferredSize().height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Theme.hq(g);
        int w = getWidth();
        int h = getHeight();

        // White background
        g2.setColor(Theme.BG_SIDEBAR);
        g2.fillRect(0, 0, w, h);

        // Brand area
        int brandH = Theme.BRAND_HEIGHT;
        g2.setColor(Theme.PRIMARY);
        g2.setFont(Theme.FONT_BRAND_ICON);
        FontMetrics fmIcon = g2.getFontMetrics();
        g2.drawString("\u2708", 20, (brandH + fmIcon.getAscent() - fmIcon.getDescent()) / 2);

        g2.setColor(Theme.TEXT_HEADING);
        g2.setFont(Theme.FONT_BRAND);
        FontMetrics fmBrand = g2.getFontMetrics();
        g2.drawString("Trip Planner", 48, (brandH + fmBrand.getAscent() - fmBrand.getDescent()) / 2);

        // Divider under brand
        g2.setColor(Theme.BORDER_LIGHT);
        g2.fillRect(16, brandH - 1, w - 32, 1);

        // Section label
        int sectionY = brandH + 16;
        g2.setColor(Theme.TEXT_MUTED);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2.drawString("NAVIGATION", 20, sectionY + 10);

        // Calculate item positions
        int y = sectionY + 22;
        int itemH = Theme.SIDEBAR_ITEM_HEIGHT;
        int itemMargin = 4;

        for (NavItem item : navItems) {
            if (item.isSeparator) {
                y += 8;
                g2.setColor(Theme.BORDER_LIGHT);
                g2.fillRect(20, y + 4, w - 40, 1);
                y += 16;
                item.y = y;
                item.height = 0;
                continue;
            }
            item.y = y;
            item.height = itemH;

            int bx = 8;
            int bw = w - 16;
            int arc = Theme.RADIUS_SM;

            // Background
            Color bg;
            if (item.active) {
                bg = Theme.BG_SELECTED;
            } else if (item.hovered) {
                bg = Theme.BG_HOVER;
            } else {
                bg = new Color(0, 0, 0, 0);
            }
            if (item.active || item.hovered) {
                Theme.fillRoundRect(g2, bx, y + itemMargin, bw, itemH - itemMargin * 2, arc, bg);
            }

            // Active indicator bar
            if (item.active) {
                g2.setColor(Theme.PRIMARY);
                g2.fillRoundRect(bx, y + 8, Theme.SIDEBAR_INDICATOR_W, itemH - 16, 2, 2);
            }

            // Icon
            Color iconColor = item.active ? Theme.PRIMARY : (item.hovered ? Theme.TEXT_BODY : Theme.TEXT_SECONDARY);
            g2.setColor(iconColor);
            g2.setFont(Theme.FONT_SIDEBAR_ICON);
            FontMetrics fmI = g2.getFontMetrics();
            g2.drawString(item.icon, bx + 14, (y + itemH + fmI.getAscent() - fmI.getDescent()) / 2);

            // Label
            Color textColor = item.active ? Theme.PRIMARY : (item.hovered ? Theme.TEXT_HEADING : Theme.TEXT_BODY);
            g2.setColor(textColor);
            g2.setFont(item.active ? Theme.FONT_SIDEBAR_BOLD : Theme.FONT_SIDEBAR);
            FontMetrics fmL = g2.getFontMetrics();
            g2.drawString(item.label, bx + 42, (y + itemH + fmL.getAscent() - fmL.getDescent()) / 2);

            y += itemH + itemMargin;
        }

        // Version label at bottom
        g2.setColor(Theme.TEXT_MUTED);
        g2.setFont(Theme.FONT_LABEL);
        FontMetrics fmV = g2.getFontMetrics();
        String version = "v1.0.0";
        g2.drawString(version, (w - fmV.stringWidth(version)) / 2, h - 16);

        g2.dispose();
    }

    private static class NavItem {
        String icon;
        String label;
        String panelName;
        boolean active;
        boolean hovered;
        boolean isSeparator;
        int y;
        int height;

        NavItem(String icon, String label, String panelName) {
            this.icon = icon;
            this.label = label;
            this.panelName = panelName;
            this.isSeparator = (icon == null && label == null);
        }
    }
}
