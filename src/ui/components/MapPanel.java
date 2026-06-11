package ui.components;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import ui.Theme;

public class MapPanel extends JPanel {

    private Map<String, float[]> nodePositions = new LinkedHashMap<>();
    private Set<String> attractions = new HashSet<>();
    private List<String> currentRoute = new ArrayList<>();
    private float zoom = 1.0f;
    private int offsetX = 0;
    private int offsetY = 0;

    private static final String[][] ROAD_PAIRS = {
        {"North Gate", "Central Park"}, {"Central Park", "Park Entrance"},
        {"Park Entrance", "City Center"}, {"City Center", "Main Junction"},
        {"Main Junction", "Market Square"}, {"Market Square", "Old Town"},
        {"Old Town", "South Bridge"},
        {"West End", "Innovation District"}, {"Innovation District", "Museum Circle"},
        {"Museum Circle", "Museum District"}, {"Museum Circle", "Park Entrance"},
        {"City Center", "Heritage Quarter"}, {"Heritage Quarter", "Heritage Cross"},
        {"Heritage Cross", "Old Gate"}, {"Old Gate", "Old Town"},
        {"Sunset Point", "West End"}, {"Sunset Point", "Market Square"},
        {"Market Square", "Market Hub"}, {"Market Hub", "Main Junction"},
        {"Main Junction", "Heritage Quarter"}, {"Heritage Quarter", "Lake Gardens"},
        {"Lake Gardens", "East Crossing"}, {"Lake Gardens", "Heritage Cross"},
        {"East Crossing", "Golden Coast"}, {"Golden Coast", "Old Gate"},
        {"Old Gate", "South Bridge"},
        {"Innovation District", "Main Junction"},
        {"Market Hub", "Old Town"}, {"Heritage Cross", "Old Town"},
        {"North Gate", "Park Entrance"}, {"West End", "Museum Circle"},
        {"Main Junction", "Heritage Cross"}, {"City Center", "Museum District"},
        {"Central Park", "Park Entrance"}
    };

    public MapPanel() {
        setBackground(new Color(240, 244, 248));
        setOpaque(true);
        initNodes();
    }

    private void initNodes() {
        // Atlas City locations (10)
        nodePositions.put("City Center", new float[]{0.50f, 0.45f});
        nodePositions.put("Museum District", new float[]{0.30f, 0.28f});
        nodePositions.put("Heritage Quarter", new float[]{0.65f, 0.55f});
        nodePositions.put("Market Square", new float[]{0.40f, 0.60f});
        nodePositions.put("Central Park", new float[]{0.50f, 0.25f});
        nodePositions.put("Innovation District", new float[]{0.22f, 0.48f});
        nodePositions.put("Old Town", new float[]{0.60f, 0.70f});
        nodePositions.put("Lake Gardens", new float[]{0.75f, 0.42f});
        nodePositions.put("Golden Coast", new float[]{0.82f, 0.80f});
        nodePositions.put("Sunset Point", new float[]{0.18f, 0.68f});

        // Junctions (10)
        nodePositions.put("Main Junction", new float[]{0.50f, 0.50f});
        nodePositions.put("North Gate", new float[]{0.50f, 0.12f});
        nodePositions.put("East Crossing", new float[]{0.85f, 0.50f});
        nodePositions.put("South Bridge", new float[]{0.50f, 0.85f});
        nodePositions.put("West End", new float[]{0.15f, 0.50f});
        nodePositions.put("Park Entrance", new float[]{0.50f, 0.33f});
        nodePositions.put("Museum Circle", new float[]{0.30f, 0.35f});
        nodePositions.put("Heritage Cross", new float[]{0.60f, 0.60f});
        nodePositions.put("Market Hub", new float[]{0.40f, 0.55f});
        nodePositions.put("Old Gate", new float[]{0.65f, 0.80f});

        attractions.addAll(Arrays.asList(
            "City Center", "Museum District", "Heritage Quarter", "Market Square",
            "Central Park", "Innovation District", "Old Town", "Lake Gardens",
            "Golden Coast", "Sunset Point"
        ));
    }

    public void setRoute(List<String> nodes) {
        this.currentRoute = (nodes != null) ? nodes : new ArrayList<>();
        repaint();
    }

    public void clearRoute() {
        this.currentRoute.clear();
        repaint();
    }

    public void zoomIn() { zoom = Math.min(2.0f, zoom + 0.15f); repaint(); }
    public void zoomOut() { zoom = Math.max(0.5f, zoom - 0.15f); repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = Theme.hq(g);
        int w = getWidth();
        int h = getHeight();

        drawMapBackground(g2, w, h);
        drawGridLines(g2, w, h);
        drawWaterFeatures(g2, w, h);
        drawDistrictOverlays(g2, w, h);
        drawRoads(g2, w, h);
        drawJunctions(g2, w, h);
        drawRoute(g2, w, h);
        drawMarkers(g2, w, h);
        drawDistrictLabels(g2, w, h);
        drawZoomIndicator(g2, w, h);

        g2.dispose();
    }

    private void drawMapBackground(Graphics2D g2, int w, int h) {
        GradientPaint gp = new GradientPaint(0, 0, new Color(240, 245, 250), w, h, new Color(232, 240, 248));
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);
    }

    private void drawGridLines(Graphics2D g2, int w, int h) {
        g2.setColor(new Color(215, 225, 235, 60));
        g2.setStroke(new BasicStroke(0.5f));
        int spacing = 50;
        for (int x = 0; x < w; x += spacing) g2.drawLine(x, 0, x, h);
        for (int y = 0; y < h; y += spacing) g2.drawLine(0, y, w, y);
    }

    private void drawWaterFeatures(Graphics2D g2, int w, int h) {
        // Crescent Lake (near Lake Gardens)
        g2.setColor(new Color(186, 214, 235, 70));
        Ellipse2D lake = new Ellipse2D.Float(w * 0.70f - w * 0.05f, h * 0.38f - h * 0.04f, w * 0.10f, h * 0.08f);
        g2.fill(lake);
        g2.setColor(new Color(191, 219, 254, 50));
        g2.setStroke(new BasicStroke(1.5f));
        g2.draw(lake);

        // River through the city
        g2.setColor(new Color(186, 214, 235, 40));
        GeneralPath river = new GeneralPath();
        river.moveTo(w * 0.85f, 0);
        river.curveTo(w * 0.82f, h * 0.15f, w * 0.88f, h * 0.22f, w * 0.82f, h * 0.35f);
        river.curveTo(w * 0.78f, h * 0.45f, w * 0.80f, h * 0.55f, w * 0.76f, h * 0.65f);
        river.curveTo(w * 0.72f, h * 0.75f, w * 0.78f, h * 0.85f, w * 0.74f, h);
        g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(river);

        // Small pond in Central Park
        g2.setColor(new Color(186, 214, 235, 50));
        Ellipse2D pond = new Ellipse2D.Float(w * 0.47f, h * 0.22f, w * 0.06f, h * 0.04f);
        g2.fill(pond);
        g2.setColor(new Color(191, 219, 254, 40));
        g2.setStroke(new BasicStroke(1f));
        g2.draw(pond);
    }

    private void drawDistrictOverlays(Graphics2D g2, int w, int h) {
        float[][][] districts = {
            {{0.42f,0.38f},{0.58f,0.38f},{0.58f,0.52f},{0.42f,0.52f}}, // City Center
            {{0.22f,0.20f},{0.38f,0.20f},{0.38f,0.35f},{0.22f,0.35f}}, // Museum District
            {{0.58f,0.48f},{0.72f,0.48f},{0.72f,0.62f},{0.58f,0.62f}}, // Heritage Quarter
            {{0.33f,0.52f},{0.47f,0.52f},{0.47f,0.68f},{0.33f,0.68f}}, // Market Square
            {{0.42f,0.18f},{0.58f,0.18f},{0.58f,0.32f},{0.42f,0.32f}}, // Central Park
            {{0.18f,0.38f},{0.32f,0.38f},{0.32f,0.52f},{0.18f,0.52f}}, // Innovation District
            {{0.52f,0.62f},{0.68f,0.62f},{0.68f,0.78f},{0.52f,0.78f}}, // Old Town
            {{0.68f,0.32f},{0.82f,0.32f},{0.82f,0.48f},{0.68f,0.48f}}, // Lake Gardens
            {{0.72f,0.72f},{0.88f,0.72f},{0.88f,0.88f},{0.72f,0.88f}}, // Golden Coast
            {{0.13f,0.62f},{0.27f,0.62f},{0.27f,0.78f},{0.13f,0.78f}}  // Sunset Point
        };
        Color[] colors = {
            new Color(59,130,246,10), new Color(139,92,246,10), new Color(245,158,11,12),
            new Color(16,185,129,10), new Color(16,185,129,15), new Color(236,72,153,10),
            new Color(245,158,11,14), new Color(59,130,246,15), new Color(245,158,11,10),
            new Color(249,115,22,10)
        };
        for (int i = 0; i < districts.length; i++) {
            Polygon poly = new Polygon();
            for (float[] p : districts[i]) poly.addPoint((int)(p[0]*w), (int)(p[1]*h));
            g2.setColor(colors[i]);
            g2.fillPolygon(poly);
            g2.setColor(new Color(0,0,0,8));
            g2.setStroke(new BasicStroke(0.5f));
            g2.drawPolygon(poly);
        }
    }

    private void drawDistrictLabels(Graphics2D g2, int w, int h) {
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2.setColor(new Color(148, 163, 184));
        for (String name : attractions) {
            float[] pos = nodePositions.get(name);
            if (pos == null) continue;
            int x = (int)(pos[0] * w);
            int y = (int)(pos[1] * h) + 28;
            FontMetrics fm = g2.getFontMetrics();
            int tw = fm.stringWidth(name);
            g2.drawString(name, x - tw / 2, y);
        }
    }

    private void drawRoads(Graphics2D g2, int w, int h) {
        g2.setColor(new Color(190, 200, 215));
        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for (String[] pair : ROAD_PAIRS) {
            float[] p1 = nodePositions.get(pair[0]);
            float[] p2 = nodePositions.get(pair[1]);
            if (p1 != null && p2 != null) {
                int x1 = (int) (p1[0] * w * zoom + offsetX);
                int y1 = (int) (p1[1] * h * zoom + offsetY);
                int x2 = (int) (p2[0] * w * zoom + offsetX);
                int y2 = (int) (p2[1] * h * zoom + offsetY);
                g2.drawLine(x1, y1, x2, y2);
            }
        }
    }

    private void drawJunctions(Graphics2D g2, int w, int h) {
        for (Map.Entry<String, float[]> entry : nodePositions.entrySet()) {
            String name = entry.getKey();
            if (attractions.contains(name)) continue;
            boolean onRoute = currentRoute.contains(name);
            float[] pos = entry.getValue();
            int x = (int) (pos[0] * w * zoom + offsetX);
            int y = (int) (pos[1] * h * zoom + offsetY);

            if (onRoute) {
                g2.setColor(Theme.PRIMARY);
                g2.fillOval(x - 5, y - 5, 10, 10);
            } else {
                g2.setColor(new Color(200, 210, 225));
                g2.fillOval(x - 3, y - 3, 6, 6);
            }
        }
    }

    private void drawRoute(Graphics2D g2, int w, int h) {
        if (currentRoute.size() < 2) return;

        g2.setColor(new Color(56, 125, 209, 40));
        g2.setStroke(new BasicStroke(14f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        drawRoutePath(g2, w, h);

        g2.setColor(Theme.PRIMARY);
        g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        drawRoutePath(g2, w, h);

        g2.setColor(new Color(255, 255, 255, 180));
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        drawRoutePath(g2, w, h);

        drawAnimatedDots(g2, w, h);
    }

    private void drawRoutePath(Graphics2D g2, int w, int h) {
        for (int i = 0; i < currentRoute.size() - 1; i++) {
            float[] p1 = nodePositions.get(currentRoute.get(i));
            float[] p2 = nodePositions.get(currentRoute.get(i + 1));
            if (p1 != null && p2 != null) {
                int x1 = (int) (p1[0] * w * zoom + offsetX);
                int y1 = (int) (p1[1] * h * zoom + offsetY);
                int x2 = (int) (p2[0] * w * zoom + offsetX);
                int y2 = (int) (p2[1] * h * zoom + offsetY);
                g2.drawLine(x1, y1, x2, y2);
            }
        }
    }

    private void drawAnimatedDots(Graphics2D g2, int w, int h) {
        long t = System.currentTimeMillis();
        g2.setColor(Color.WHITE);
        for (int i = 0; i < currentRoute.size() - 1; i++) {
            float[] p1 = nodePositions.get(currentRoute.get(i));
            float[] p2 = nodePositions.get(currentRoute.get(i + 1));
            if (p1 != null && p2 != null) {
                int x1 = (int) (p1[0] * w * zoom + offsetX);
                int y1 = (int) (p1[1] * h * zoom + offsetY);
                int x2 = (int) (p2[0] * w * zoom + offsetX);
                int y2 = (int) (p2[1] * h * zoom + offsetY);
                float phase = ((t / 400 + i * 50) % 200) / 200f;
                int dx = (int) (x1 + (x2 - x1) * phase);
                int dy = (int) (y1 + (y2 - y1) * phase);
                g2.fillOval(dx - 3, dy - 3, 6, 6);
            }
        }
    }

    private void drawMarkers(Graphics2D g2, int w, int h) {
        for (Map.Entry<String, float[]> entry : nodePositions.entrySet()) {
            String name = entry.getKey();
            if (!attractions.contains(name)) continue;
            float[] pos = entry.getValue();
            int x = (int) (pos[0] * w * zoom + offsetX);
            int y = (int) (pos[1] * h * zoom + offsetY);
            boolean onRoute = currentRoute.contains(name);
            boolean isStart = !currentRoute.isEmpty() && currentRoute.get(0).equals(name);
            boolean isEnd = !currentRoute.isEmpty() && currentRoute.get(currentRoute.size() - 1).equals(name);

            drawMarkerPin(g2, x, y, name, onRoute, isStart, isEnd);
        }
    }

    private void drawMarkerPin(Graphics2D g2, int x, int y, String name, boolean onRoute, boolean isStart, boolean isEnd) {
        if (isStart || isEnd) {
            g2.setColor(isStart ? new Color(34, 197, 94) : new Color(239, 68, 68));
            g2.fillOval(x - 16, y - 16, 32, 32);
            g2.setColor(new Color(255, 255, 255, 60));
            g2.fillOval(x - 12, y - 12, 24, 24);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            FontMetrics fm = g2.getFontMetrics();
            String icon = isStart ? "S" : "E";
            g2.drawString(icon, x - fm.stringWidth(icon) / 2, y + fm.getAscent() / 2 - 2);
        } else if (onRoute) {
            g2.setColor(Theme.PRIMARY);
            g2.fillOval(x - 12, y - 12, 24, 24);
            g2.setColor(new Color(255, 255, 255, 80));
            g2.fillOval(x - 8, y - 8, 16, 16);
            g2.setColor(Color.WHITE);
            g2.fillOval(x - 4, y - 4, 8, 8);
        } else {
            g2.setColor(new Color(255, 255, 255, 200));
            g2.fillOval(x - 10, y - 10, 20, 20);
            g2.setColor(new Color(180, 195, 215));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval(x - 10, y - 10, 20, 20);
            g2.setColor(Theme.PRIMARY_LIGHT);
            g2.fillOval(x - 5, y - 5, 10, 10);
        }

        g2.setFont(onRoute ? Theme.FONT_SMALL_BOLD : Theme.FONT_SMALL);
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(name);
        int labelY = y + (onRoute ? 24 : 22);

        int labelPadX = 6;
        int labelPadY = 3;
        Theme.fillRoundRect(g2, x - tw / 2 - labelPadX, labelY - fm.getAscent() - labelPadY,
                tw + labelPadX * 2, fm.getHeight() + labelPadY * 2, 6,
                onRoute ? new Color(56, 125, 209, 200) : new Color(255, 255, 255, 220));

        g2.setColor(onRoute ? Color.WHITE : Theme.TEXT_BODY);
        g2.drawString(name, x - tw / 2, labelY);
    }

    private void drawZoomIndicator(Graphics2D g2, int w, int h) {
        int px = w - 50;
        int py = h - 30;
        g2.setColor(Theme.TEXT_MUTED);
        g2.setFont(Theme.FONT_LABEL);
        g2.drawString(String.format("%.0f%%", zoom * 100), px - 10, py);
    }
}
