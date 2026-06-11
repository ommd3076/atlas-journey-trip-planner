package ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import algorithms.KnapsackService;
import model.Attraction;
import model.KnapsackResult;
import ui.components.*;

public class BudgetPlannerPanel extends AnimatedPanel {

    private KnapsackService service = new KnapsackService();
    private JTextField budgetField;
    private JLabel totalCostVal, totalValueVal, remainingVal, countVal;
    private JPanel resultList;
    private RoundedCardPanel inputCard;
    private RoundedCardPanel resultCard;

    public BudgetPlannerPanel() {
        super(new BorderLayout(Theme.PANEL_GAP, Theme.PANEL_GAP));

        JPanel leftPanel = createInputPanel();
        JPanel rightPanel = createResultPanel();

        JPanel splitPanel = new JPanel(new BorderLayout(Theme.PANEL_GAP, 0));
        splitPanel.setOpaque(false);
        splitPanel.add(leftPanel, BorderLayout.WEST);
        splitPanel.add(rightPanel, BorderLayout.CENTER);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(new ContentHeader("Budget Planner", "Optimize your trip budget using 0-1 Knapsack algorithm"), BorderLayout.NORTH);
        wrapper.add(splitPanel, BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);
        animateIn();
    }

    private JPanel createInputPanel() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setPreferredSize(new Dimension(320, 0));

        inputCard = new RoundedCardPanel(new BorderLayout(0, 0));
        inputCard.setBorder(new EmptyBorder(28, 24, 24, 24));

        JPanel formContent = new JPanel();
        formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
        formContent.setOpaque(false);

        JLabel sectionTitle = new JLabel("Set Budget");
        sectionTitle.setFont(Theme.FONT_SUBHEADING);
        sectionTitle.setForeground(Theme.TEXT_HEADING);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formContent.add(sectionTitle);
        formContent.add(Box.createRigidArea(new Dimension(0, 16)));

        JLabel budgetLabel = new JLabel("Budget Amount ($)");
        budgetLabel.setFont(Theme.FONT_SMALL_BOLD);
        budgetLabel.setForeground(Theme.TEXT_BODY);
        budgetLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formContent.add(budgetLabel);
        formContent.add(Box.createRigidArea(new Dimension(0, 6)));

        budgetField = new JTextField("500");
        budgetField.setFont(new Font("Segoe UI", Font.BOLD, 18));
        budgetField.setPreferredSize(new Dimension(260, 48));
        budgetField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        budgetField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1, true),
            new EmptyBorder(8, 16, 8, 16)
        ));
        budgetField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formContent.add(budgetField);
        formContent.add(Box.createRigidArea(new Dimension(0, 16)));

        GradientButton calcBtn = new GradientButton("Calculate Optimal Plan");
        calcBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        calcBtn.setPreferredSize(new Dimension(260, 44));
        calcBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        calcBtn.addActionListener(e -> performCalculation());
        formContent.add(calcBtn);
        formContent.add(Box.createRigidArea(new Dimension(0, 20)));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(Theme.BORDER);
        formContent.add(sep);
        formContent.add(Box.createRigidArea(new Dimension(0, 16)));

        JPanel stats = new JPanel(new GridLayout(2, 2, 12, 12));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        stats.setAlignmentX(Component.LEFT_ALIGNMENT);

        totalCostVal = createStatValue("$0");
        totalValueVal = createStatValue("0");
        remainingVal = createStatValue("$0");
        countVal = createStatValue("0");

        stats.add(createStatItem("Total Cost", totalCostVal));
        stats.add(createStatItem("Total Value", totalValueVal));
        stats.add(createStatItem("Remaining", remainingVal));
        stats.add(createStatItem("Items", countVal));

        formContent.add(stats);

        inputCard.add(formContent, BorderLayout.CENTER);
        container.add(inputCard);

        return container;
    }

    private JLabel createStatValue(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Theme.FONT_BODY_BOLD);
        lbl.setForeground(Theme.TEXT_HEADING);
        return lbl;
    }

    private JPanel createStatItem(String label, JLabel value) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.FONT_LABEL);
        lbl.setForeground(Theme.TEXT_SECONDARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        value.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lbl);
        p.add(value);
        return p;
    }

    private JPanel createResultPanel() {
        resultCard = new RoundedCardPanel(new BorderLayout(0, 16));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel title = new JLabel("Recommended Itinerary");
        title.setFont(Theme.FONT_SUBHEADING);
        title.setForeground(Theme.TEXT_HEADING);
        headerPanel.add(title, BorderLayout.WEST);
        resultCard.add(headerPanel, BorderLayout.NORTH);

        resultList = new JPanel();
        resultList.setLayout(new BoxLayout(resultList, BoxLayout.Y_AXIS));
        resultList.setOpaque(false);

        JLabel placeholder = new JLabel("  Enter your budget and click Calculate to see the optimal plan.");
        placeholder.setFont(Theme.FONT_BODY);
        placeholder.setForeground(Theme.TEXT_MUTED);
        placeholder.setBorder(new EmptyBorder(40, 20, 40, 20));
        resultList.add(placeholder);

        JScrollPane scroll = new JScrollPane(resultList);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        resultCard.add(scroll, BorderLayout.CENTER);

        return resultCard;
    }

    private void performCalculation() {
        try {
            int budget = Integer.parseInt(budgetField.getText().trim());
            KnapsackResult result = service.solveKnapsackFromDB(budget);

            totalCostVal.setText("$" + result.getTotalCost());
            totalValueVal.setText(String.valueOf(result.getTotalValue()));
            remainingVal.setText("$" + result.getRemainingBudget());
            countVal.setText(String.valueOf(result.getSelectedItems().size()));

            resultList.removeAll();
            List<Attraction> items = result.getSelectedItems();
            if (items.isEmpty()) {
                JLabel empty = new JLabel("  No attractions fit within this budget.");
                empty.setFont(Theme.FONT_BODY);
                empty.setForeground(Theme.TEXT_MUTED);
                empty.setBorder(new EmptyBorder(40, 20, 40, 20));
                resultList.add(empty);
            } else {
                int i = 1;
                for (Attraction a : items) {
                    resultList.add(createAttractionResultCard(a, i));
                    resultList.add(Box.createRigidArea(new Dimension(0, 8)));
                    i++;
                }
            }
            resultList.revalidate();
            resultList.repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid budget amount.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }
    }

    private RoundedCardPanel createAttractionResultCard(Attraction a, int index) {
        RoundedCardPanel card = new RoundedCardPanel(new BorderLayout(12, 0), Theme.RADIUS_SM);
        card.setBorder(new EmptyBorder(14, 16, 14, 16));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);

        JLabel numBadge = new JLabel(String.valueOf(index));
        numBadge.setFont(Theme.FONT_SMALL_BOLD);
        numBadge.setForeground(Theme.PRIMARY);
        numBadge.setOpaque(true);
        numBadge.setBackground(Theme.PRIMARY_PALE);
        numBadge.setPreferredSize(new Dimension(28, 28));
        numBadge.setHorizontalAlignment(SwingConstants.CENTER);
        numBadge.setBorder(BorderFactory.createLineBorder(Theme.PRIMARY_LIGHT, 1, true));
        leftPanel.add(numBadge);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel name = new JLabel(a.getName());
        name.setFont(Theme.FONT_BODY_BOLD);
        name.setForeground(Theme.TEXT_HEADING);
        textPanel.add(name);

        JLabel loc = new JLabel(a.getLocation());
        loc.setFont(Theme.FONT_SMALL);
        loc.setForeground(Theme.TEXT_SECONDARY);
        textPanel.add(loc);

        leftPanel.add(textPanel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightPanel.setOpaque(false);

        JLabel costChip = new JLabel("  $" + a.getCost() + "  ");
        costChip.setFont(Theme.FONT_SMALL_BOLD);
        costChip.setForeground(Theme.PRIMARY);
        costChip.setOpaque(true);
        costChip.setBackground(Theme.PRIMARY_PALE);
        costChip.setBorder(BorderFactory.createLineBorder(Theme.PRIMARY_LIGHT, 1, true));
        rightPanel.add(costChip);

        JLabel valueChip = new JLabel("  " + a.getValue() + " pts  ");
        valueChip.setFont(Theme.FONT_SMALL_BOLD);
        valueChip.setForeground(Theme.SUCCESS);
        valueChip.setOpaque(true);
        valueChip.setBackground(Theme.SUCCESS_BG);
        valueChip.setBorder(BorderFactory.createLineBorder(new Color(34, 197, 94, 80), 1, true));
        rightPanel.add(valueChip);

        card.add(leftPanel, BorderLayout.WEST);
        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }
}
