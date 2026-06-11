package ui;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import algorithms.DijkstraService;
import model.PathResult;
import ui.components.*;

public class RoutePlannerPanel extends AnimatedPanel {

    private DijkstraService service = new DijkstraService();
    private JComboBox<String> startCombo, endCombo;
    private MapPanel mapPanel;
    private JLabel distanceVal, pathVal, statusVal;

    public RoutePlannerPanel() {
        super(new BorderLayout(0, 0));

        JPanel wrapper = new JPanel(new BorderLayout(0, Theme.PANEL_GAP));
        wrapper.setOpaque(false);
        wrapper.add(new ContentHeader("Route Planner", "Find shortest paths using Dijkstra's algorithm"), BorderLayout.NORTH);
        wrapper.add(createMainLayout(), BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);
        animateIn();
    }

    private JPanel createMainLayout() {
        JPanel layout = new JPanel(new BorderLayout(Theme.PANEL_GAP, 0));
        layout.setOpaque(false);

        layout.add(createControlPanel(), BorderLayout.WEST);
        layout.add(createMapContainer(), BorderLayout.CENTER);

        return layout;
    }

    private JPanel createControlPanel() {
        RoundedCardPanel card = new RoundedCardPanel(new BorderLayout(0, 0));
        card.setPreferredSize(new Dimension(300, 0));
        card.setBorder(new EmptyBorder(24, 20, 20, 20));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        JLabel title = new JLabel("Find Route");
        title.setFont(Theme.FONT_SUBHEADING);
        title.setForeground(Theme.TEXT_HEADING);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(title);
        form.add(Box.createRigidArea(new Dimension(0, 16)));

        form.add(createComboSection("Starting Location", startCombo = new JComboBox<>()));
        form.add(Box.createRigidArea(new Dimension(0, 12)));
        form.add(createComboSection("Destination", endCombo = new JComboBox<>()));
        form.add(Box.createRigidArea(new Dimension(0, 16)));

        for (String node : service.getAllNodes()) {
            startCombo.addItem(node);
            endCombo.addItem(node);
        }

        GradientButton findBtn = new GradientButton("Find Shortest Path");
        findBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        findBtn.setPreferredSize(new Dimension(260, 44));
        findBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        findBtn.addActionListener(e -> findPath());
        form.add(findBtn);
        form.add(Box.createRigidArea(new Dimension(0, 16)));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(Theme.BORDER);
        form.add(sep);
        form.add(Box.createRigidArea(new Dimension(0, 16)));

        JPanel resultSection = new JPanel();
        resultSection.setLayout(new BoxLayout(resultSection, BoxLayout.Y_AXIS));
        resultSection.setOpaque(false);
        resultSection.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel resultTitle = new JLabel("Route Result");
        resultTitle.setFont(Theme.FONT_SMALL_BOLD);
        resultTitle.setForeground(Theme.TEXT_SECONDARY);
        resultTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        resultSection.add(resultTitle);
        resultSection.add(Box.createRigidArea(new Dimension(0, 8)));

        distanceVal = createResultValue("-- km");
        resultSection.add(distanceVal);
        resultSection.add(Box.createRigidArea(new Dimension(0, 8)));

        pathVal = new JLabel("Select locations to find a route");
        pathVal.setFont(Theme.FONT_SMALL);
        pathVal.setForeground(Theme.TEXT_SECONDARY);
        pathVal.setAlignmentX(Component.LEFT_ALIGNMENT);
        pathVal.setPreferredSize(new Dimension(260, 40));
        resultSection.add(pathVal);
        resultSection.add(Box.createRigidArea(new Dimension(0, 10)));

        statusVal = new JLabel("");
        statusVal.setFont(Theme.FONT_SMALL_BOLD);
        statusVal.setAlignmentX(Component.LEFT_ALIGNMENT);
        resultSection.add(statusVal);

        form.add(resultSection);
        card.add(form, BorderLayout.CENTER);

        return card;
    }

    private JPanel createComboSection(String label, JComboBox<String> combo) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.FONT_SMALL_BOLD);
        lbl.setForeground(Theme.TEXT_BODY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lbl);
        p.add(Box.createRigidArea(new Dimension(0, 4)));

        combo.setFont(Theme.FONT_BODY);
        combo.setPreferredSize(new Dimension(260, 40));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        combo.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1, true));
        p.add(combo);

        return p;
    }

    private JLabel createResultValue(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Theme.FONT_METRIC_SM);
        lbl.setForeground(Theme.PRIMARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel createMapContainer() {
        RoundedCardPanel card = new RoundedCardPanel(new BorderLayout(8, 8));
        card.setFillColor(Theme.BG_CARD);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 4));
        toolbar.setOpaque(false);

        GradientButton zoomInBtn = createSmallButton("+");
        GradientButton zoomOutBtn = createSmallButton("-");
        zoomInBtn.addActionListener(e -> mapPanel.zoomIn());
        zoomOutBtn.addActionListener(e -> mapPanel.zoomOut());
        toolbar.add(zoomInBtn);
        toolbar.add(zoomOutBtn);

        card.add(toolbar, BorderLayout.NORTH);

        mapPanel = new MapPanel();
        card.add(mapPanel, BorderLayout.CENTER);

        return card;
    }

    private GradientButton createSmallButton(String text) {
        GradientButton btn = new GradientButton(text);
        btn.setPreferredSize(new Dimension(36, 36));
        btn.setButtonColor(Theme.BG_INPUT, Theme.BORDER);
        return btn;
    }

    private void findPath() {
        String start = (String) startCombo.getSelectedItem();
        String end = (String) endCombo.getSelectedItem();
        if (start == null || end == null) return;

        PathResult result = service.findShortestPath(start, end);

        if (result.isPathFound()) {
            distanceVal.setText(result.getTotalDistance() + " km");
            String formattedPath = result.getFormattedPath().replace(" -> ", "  \u2192  ");
            pathVal.setText("<html><div style='width:240px'>" + formattedPath + "</div></html>");
            statusVal.setText("Route found!");
            statusVal.setForeground(Theme.SUCCESS);
            mapPanel.setRoute(result.getPath());
        } else {
            distanceVal.setText("-- km");
            pathVal.setText("No route available");
            statusVal.setText("No path found");
            statusVal.setForeground(Theme.ERROR);
            mapPanel.clearRoute();
        }
    }
}
