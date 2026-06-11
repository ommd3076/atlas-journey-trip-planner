package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import ui.components.*;

public class DashboardFrame extends JFrame {

    private SidebarPanel sidebar;
    private HeaderPanel header;
    private JPanel contentArea;
    private CardLayout cardLayout;

    public DashboardFrame(String username) {
        setTitle("Trip Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 820);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BG_APP);
        setLayout(new BorderLayout(0, 0));

        // Sidebar on the left (full height)
        sidebar = new SidebarPanel();
        sidebar.setNavigationListener(new SidebarPanel.NavigationListener() {
            public void onNavigate(String panelName, String title) {
                showPanel(panelName, title);
            }
        });
        add(sidebar, BorderLayout.WEST);

        // Center: header on top, content area below
        JPanel centerPanel = new JPanel(new BorderLayout(0, 0));
        centerPanel.setOpaque(false);

        header = new HeaderPanel(username);
        centerPanel.add(header, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(Theme.BG_APP);
        contentArea.setBorder(new EmptyBorder(Theme.PANEL_GAP, Theme.PANEL_GAP, Theme.PANEL_GAP, Theme.PANEL_GAP));

        // Add panels
        contentArea.add(new DashboardHomePanel(this), "home");
        contentArea.add(new BudgetPlannerPanel(), "trip");
        contentArea.add(new RoutePlannerPanel(), "route");
        contentArea.add(new RankingsPanel(), "rankings");
        contentArea.add(new SettingsPanel(), "settings");

        centerPanel.add(contentArea, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Show initial panel
        showPanel("home", "Dashboard");
    }

    public void showPanel(String name, String title) {
        cardLayout.show(contentArea, name);
        sidebar.setActive(name);
        header.setTitle(title);
    }
}
