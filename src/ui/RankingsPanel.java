package ui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import algorithms.HeapSortService;
import model.Attraction;
import ui.components.*;

public class RankingsPanel extends AnimatedPanel {

    private HeapSortService service = new HeapSortService();
    private JPanel listContainer;
    private RoundedCardPanel leaderboardCard;

    public RankingsPanel() {
        super(new BorderLayout(0, 0));

        JPanel wrapper = new JPanel(new BorderLayout(0, Theme.PANEL_GAP));
        wrapper.setOpaque(false);
        wrapper.add(new ContentHeader("Attraction Rankings", "Top attractions ranked by composite score (rating\u00D710 + value)"), BorderLayout.NORTH);
        wrapper.add(createLeaderboard(), BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);
        animateIn();
    }

    private JPanel createLeaderboard() {
        leaderboardCard = new RoundedCardPanel(new BorderLayout(0, 0));
        leaderboardCard.setBorder(new EmptyBorder(20, 0, 0, 0));

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);
        headerRow.setBorder(new EmptyBorder(0, 24, 12, 24));

        JLabel title = new JLabel("Top Attractions");
        title.setFont(Theme.FONT_SUBHEADING);
        title.setForeground(Theme.TEXT_HEADING);
        headerRow.add(title, BorderLayout.WEST);

        GradientButton sortBtn = new GradientButton("Refresh Rankings");
        sortBtn.setPreferredSize(new Dimension(160, 36));
        sortBtn.addActionListener(e -> loadRankings());
        headerRow.add(sortBtn, BorderLayout.EAST);

        leaderboardCard.add(headerRow, BorderLayout.NORTH);

        listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setOpaque(false);
        listContainer.setBorder(new EmptyBorder(0, 24, 16, 24));

        JScrollPane scroll = new JScrollPane(listContainer);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        leaderboardCard.add(scroll, BorderLayout.CENTER);

        loadRankings();

        return leaderboardCard;
    }

    private void loadRankings() {
        List<Attraction> sorted = service.getSortedFromDB();
        listContainer.removeAll();

        for (int i = 0; i < sorted.size(); i++) {
            listContainer.add(createRankRow(i + 1, sorted.get(i)));
            if (i < sorted.size() - 1) {
                listContainer.add(Box.createRigidArea(new Dimension(0, 6)));
            }
        }

        listContainer.revalidate();
        listContainer.repaint();
    }

    private RoundedCardPanel createRankRow(int rank, Attraction a) {
        RoundedCardPanel row = new RoundedCardPanel(new BorderLayout(16, 0), Theme.RADIUS_SM);
        row.setBorder(new EmptyBorder(14, 16, 14, 16));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        row.setShowShadow(false);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        leftPanel.setOpaque(false);

        JLabel rankBadge = createRankBadge(rank);
        leftPanel.add(rankBadge);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(a.getName());
        nameLabel.setFont(Theme.FONT_BODY_BOLD);
        nameLabel.setForeground(Theme.TEXT_HEADING);
        infoPanel.add(nameLabel);

        JLabel locLabel = new JLabel(a.getLocation() + "  \u2022  " + a.getDescription());
        locLabel.setFont(Theme.FONT_SMALL);
        locLabel.setForeground(Theme.TEXT_SECONDARY);
        infoPanel.add(locLabel);

        leftPanel.add(infoPanel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightPanel.setOpaque(false);

        JLabel ratingBadge = createRatingBadge(a.getRating());
        rightPanel.add(ratingBadge);

            JLabel scoreChip = createScoreChip(a.getScore() + " pts");
        rightPanel.add(scoreChip);

        row.add(leftPanel, BorderLayout.WEST);
        row.add(rightPanel, BorderLayout.EAST);

        return row;
    }

    private JLabel createRankBadge(int rank) {
        Color bgColor, fgColor;
        String text;

        if (rank == 1) { bgColor = new Color(255, 247, 237); fgColor = Theme.GOLD; text = "#" + rank; }
        else if (rank == 2) { bgColor = new Color(241, 245, 249); fgColor = Theme.SILVER; text = "#" + rank; }
        else if (rank == 3) { bgColor = new Color(255, 247, 237); fgColor = Theme.BRONZE; text = "#" + rank; }
        else { bgColor = Theme.BG_INPUT; fgColor = Theme.TEXT_SECONDARY; text = "#" + rank; }

        JLabel badge = new JLabel(text);
        badge.setFont(Theme.FONT_BODY_BOLD);
        badge.setForeground(fgColor);
        badge.setOpaque(true);
        badge.setBackground(bgColor);
        badge.setPreferredSize(new Dimension(44, 32));
        badge.setHorizontalAlignment(SwingConstants.CENTER);
        badge.setBorder(BorderFactory.createLineBorder(new Color(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue(), 60), 1, true));
        return badge;
    }

    private JLabel createRatingBadge(int rating) {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < rating; i++) stars.append("\u2605");
        for (int i = rating; i < 5; i++) stars.append("\u2606");

        JLabel badge = new JLabel(" " + stars + " ");
        badge.setFont(Theme.FONT_SMALL);
        badge.setForeground(Theme.GOLD);
        badge.setOpaque(true);
        badge.setBackground(Theme.WARNING_BG);
        badge.setBorder(BorderFactory.createLineBorder(new Color(245, 158, 11, 60), 1, true));
        return badge;
    }

    private JLabel createScoreChip(String text) {
        JLabel chip = new JLabel("  " + text + "  ");
        chip.setFont(Theme.FONT_SMALL_BOLD);
        chip.setForeground(Theme.SUCCESS);
        chip.setOpaque(true);
        chip.setBackground(Theme.SUCCESS_BG);
        chip.setBorder(BorderFactory.createLineBorder(new Color(34, 197, 94, 60), 1, true));
        return chip;
    }
}
