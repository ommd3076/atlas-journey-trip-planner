package ui;

import java.awt.*;
import javax.swing.border.EmptyBorder;

public class Theme {

    // === Primary Palette (Design System: Green/Earth tones) ===
    public static final Color PRIMARY = new Color(45, 106, 79);        // #2D6A4F green
    public static final Color PRIMARY_DARK = new Color(27, 67, 50);    // #1B4332 green-dk
    public static final Color PRIMARY_LIGHT = new Color(64, 159, 122); // #409F7A
    public static final Color PRIMARY_PALE = new Color(232, 245, 238); // #E8F5EE green-lt

    // === Backgrounds ===
    public static final Color BG_APP = new Color(245, 244, 240);       // #F5F4F0 bg
    public static final Color BG_CARD = Color.WHITE;
    public static final Color BG_SIDEBAR = Color.WHITE;
    public static final Color BG_INPUT = new Color(245, 244, 240);     // #F5F4F0
    public static final Color BG_HOVER = new Color(237, 236, 232);
    public static final Color BG_SELECTED = new Color(232, 245, 238);  // #E8F5EE green-lt
    public static final Color BG_DARK = new Color(27, 67, 50);         // #1B4332

    // === Text ===
    public static final Color TEXT_HEADING = new Color(26, 26, 24);    // #1A1A18 text
    public static final Color TEXT_BODY = new Color(51, 51, 47);       // #33332F
    public static final Color TEXT_SECONDARY = new Color(136, 134, 128); // #888680 muted
    public static final Color TEXT_MUTED = new Color(136, 134, 128);   // #888680 muted
    public static final Color TEXT_WHITE = Color.WHITE;

    // === Accents ===
    public static final Color SUCCESS = new Color(45, 106, 79);         // #2D6A4F
    public static final Color SUCCESS_BG = new Color(232, 245, 238);    // #E8F5EE
    public static final Color WARNING = new Color(180, 83, 9);          // #B45309 amber
    public static final Color WARNING_BG = new Color(254, 243, 199);    // #FEF3C7
    public static final Color ERROR = new Color(192, 57, 43);           // #C0392B red
    public static final Color ERROR_BG = new Color(253, 236, 234);      // #FDECEA
    public static final Color INFO = new Color(30, 111, 159);           // #1E6F9F blue
    public static final Color INFO_BG = new Color(224, 242, 254);       // #E0F2FE
    public static final Color GOLD = new Color(180, 83, 9);             // #B45309
    public static final Color SILVER = new Color(136, 134, 128);        // #888680
    public static final Color BRONZE = new Color(180, 83, 9);
    public static final Color PURPLE = new Color(139, 92, 246);
    public static final Color PURPLE_BG = new Color(237, 233, 254);
    public static final Color TEAL = new Color(20, 184, 166);
    public static final Color TEAL_BG = new Color(204, 251, 241);

    // === Hero Gradient ===
    public static final Color HERO_START = new Color(45, 106, 79);      // #2D6A4F
    public static final Color HERO_END = new Color(27, 67, 50);         // #1B4332

    // === Borders ===
    public static final Color BORDER = new Color(226, 224, 216);        // #E2E0D8 border
    public static final Color BORDER_LIGHT = new Color(237, 236, 232);
    public static final Color DIVIDER = new Color(226, 224, 216);

    // === Fonts (Design System scale) ===
    public static final Font FONT_DISPLAY = new Font("DM Sans", Font.BOLD, 28);
    public static final Font FONT_HEADING = new Font("DM Sans", Font.BOLD, 22);
    public static final Font FONT_SUBHEADING = new Font("DM Sans", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("DM Sans", Font.PLAIN, 14);
    public static final Font FONT_BODY_BOLD = new Font("DM Sans", Font.BOLD, 14);
    public static final Font FONT_SMALL = new Font("DM Sans", Font.PLAIN, 12);
    public static final Font FONT_SMALL_BOLD = new Font("DM Sans", Font.BOLD, 12);
    public static final Font FONT_METRIC = new Font("DM Mono", Font.BOLD, 28);
    public static final Font FONT_METRIC_SM = new Font("DM Mono", Font.BOLD, 20);
    public static final Font FONT_LABEL = new Font("DM Sans", Font.PLAIN, 11);
    public static final Font FONT_BUTTON = new Font("DM Sans", Font.BOLD, 13);
    public static final Font FONT_SIDEBAR = new Font("DM Sans", Font.PLAIN, 13);
    public static final Font FONT_SIDEBAR_BOLD = new Font("DM Sans", Font.BOLD, 13);
    public static final Font FONT_SIDEBAR_ICON = new Font("DM Sans", Font.PLAIN, 18);
    public static final Font FONT_BRAND = new Font("DM Mono", Font.BOLD, 15);
    public static final Font FONT_BRAND_ICON = new Font("DM Sans", Font.PLAIN, 22);
    public static final Font FONT_SEARCH = new Font("DM Sans", Font.PLAIN, 13);

    // === Layout (Design System spacing) ===
    public static final int RADIUS = 10;
    public static final int RADIUS_SM = 8;
    public static final int RADIUS_LG = 16;
    public static final int SHADOW_DEPTH = 4;
    public static final int SIDEBAR_WIDTH = 220;
    public static final int TOPBAR_HEIGHT = 60;
    public static final int CARD_PADDING = 24;
    public static final int CARD_PADDING_SM = 16;
    public static final int PANEL_GAP = 20;
    public static final int SECTION_GAP = 28;
    public static final int SIDEBAR_ITEM_HEIGHT = 42;
    public static final int SIDEBAR_INDICATOR_W = 4;
    public static final int BRAND_HEIGHT = 64;
    public static final int ICON_CIRCLE_SIZE = 40;

    // === Borders / Padding ===
    public static final EmptyBorder PADDING_CARD = new EmptyBorder(CARD_PADDING, CARD_PADDING, CARD_PADDING, CARD_PADDING);
    public static final EmptyBorder PADDING_CARD_SM = new EmptyBorder(CARD_PADDING_SM, CARD_PADDING_SM, CARD_PADDING_SM, CARD_PADDING_SM);
    public static final EmptyBorder PADDING_SECTION = new EmptyBorder(PANEL_GAP, PANEL_GAP, PANEL_GAP, PANEL_GAP);

    // === Graphics Helpers ===

    public static Graphics2D hq(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        return g2;
    }

    public static void fillRoundRect(Graphics2D g, int x, int y, int w, int h, int arc, Color fill) {
        g.setColor(fill);
        g.fillRoundRect(x, y, w, h, arc, arc);
    }

    public static void drawSoftShadow(Graphics2D g, int x, int y, int w, int h, int arc) {
        for (int i = SHADOW_DEPTH; i > 0; i--) {
            g.setColor(new Color(0, 0, 0, Math.max(2, 10 - i * 2)));
            g.fillRoundRect(x - i, y - i, w + i * 2, h + i * 2, arc + i, arc + i);
        }
    }

    public static Color blend(Color a, Color b, float ratio) {
        int r = (int) (a.getRed() * (1 - ratio) + b.getRed() * ratio);
        int gr = (int) (a.getGreen() * (1 - ratio) + b.getGreen() * ratio);
        int bl = (int) (a.getBlue() * (1 - ratio) + b.getBlue() * ratio);
        return new Color(Math.min(255, Math.max(0, r)), Math.min(255, Math.max(0, gr)), Math.min(255, Math.max(0, bl)));
    }
}
