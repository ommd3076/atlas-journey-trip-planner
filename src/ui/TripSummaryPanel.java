package ui;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import algorithms.KnapsackService;
import algorithms.DijkstraService;
import algorithms.HeapSortService;
import model.Attraction;
import model.KnapsackResult;
import model.PathResult;
import ui.components.*;

public class TripSummaryPanel extends AnimatedPanel {

    private KnapsackService knapsackService = new KnapsackService();
    private DijkstraService dijkstraService = new DijkstraService();
    private HeapSortService heapSortService = new HeapSortService();

    public TripSummaryPanel() {
        super(new BorderLayout(0, 0));

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setOpaque(false);

        main.add(new ContentHeader("Trip Summary", "Overview of your optimized trip plan"));
        main.add(Box.createRigidArea(new Dimension(0, Theme.PANEL_GAP)));
        main.add(createOverviewCards());
        main.add(Box.createRigidArea(new Dimension(0, Theme.PANEL_GAP)));
        main.add(createAlgorithmCards());

        JScrollPane scroll = new JScrollPane(main);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        animateIn();
    }

    private JPanel createOverviewCards() {
        JPanel grid = new JPanel(new GridLayout(1, 4, Theme.PANEL_GAP, 0));
        grid.setOpaque(false);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        List<Attraction> all = knapsackService.getAllAttractions();
        List<String> nodes = dijkstraService.getAllNodes();
        KnapsackResult opt = knapsackService.solveKnapsackFromDB(500);

        grid.add(new SummaryCard(String.valueOf(all.size()), "Total Attractions", Theme.PRIMARY));
        grid.add(new SummaryCard(String.valueOf(nodes.size()), "Map Locations", Theme.SUCCESS));
        grid.add(new SummaryCard("$" + opt.getTotalCost(), "Optimized Cost", Theme.WARNING));
        grid.add(new SummaryCard(String.valueOf(opt.getTotalValue()), "Total Value", new Color(168, 85, 247)));

        return grid;
    }

    private JPanel createAlgorithmCards() {
        JPanel grid = new JPanel(new GridLayout(1, 3, Theme.PANEL_GAP, 0));
        grid.setOpaque(false);

        grid.add(createAlgoCard("0-1 Knapsack", "Budget Optimization", "Selects the best combination of attractions within a given budget by maximizing total value. Uses dynamic programming.", Theme.PRIMARY, "Budget: $500"));
        grid.add(createAlgoCard("Dijkstra's Algorithm", "Route Planning", "Finds the shortest path between any two locations on the map. Uses a priority queue for efficient traversal.", Theme.SUCCESS, "11 Locations"));
        grid.add(createAlgoCard("Heap Sort", "Attraction Rankings", "Sorts all attractions by rating in descending order using a max-heap data structure for efficient sorting.", Theme.WARNING, "12 Items"));

        return grid;
    }

    private RoundedCardPanel createAlgoCard(String title, String subtitle, String description, Color accent, String stat) {
        RoundedCardPanel card = new RoundedCardPanel(new BorderLayout(0, 12));

        JPanel topSection = new JPanel(new BorderLayout(8, 0));
        topSection.setOpaque(false);

        JLabel accentBar = new JLabel("  ");
        accentBar.setOpaque(true);
        accentBar.setBackground(accent);
        accentBar.setPreferredSize(new Dimension(4, 40));
        topSection.add(accentBar, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.FONT_BODY_BOLD);
        titleLabel.setForeground(Theme.TEXT_HEADING);
        textPanel.add(titleLabel);

        JLabel subLabel = new JLabel(subtitle);
        subLabel.setFont(Theme.FONT_SMALL);
        subLabel.setForeground(accent);
        textPanel.add(subLabel);

        topSection.add(textPanel, BorderLayout.CENTER);
        card.add(topSection, BorderLayout.NORTH);

        JLabel descLabel = new JLabel("<html><div style='width:280px'>" + description + "</div></html>");
        descLabel.setFont(Theme.FONT_SMALL);
        descLabel.setForeground(Theme.TEXT_SECONDARY);
        card.add(descLabel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bottomPanel.setOpaque(false);
        JLabel statChip = new JLabel("  " + stat + "  ");
        statChip.setFont(Theme.FONT_SMALL_BOLD);
        statChip.setForeground(accent);
        statChip.setOpaque(true);
        Color chipBg = new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 20);
        statChip.setBackground(chipBg);
        statChip.setBorder(BorderFactory.createLineBorder(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 60), 1, true));
        bottomPanel.add(statChip);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }
}
