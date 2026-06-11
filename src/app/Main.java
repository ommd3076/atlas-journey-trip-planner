package app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import db.DBConnection;
import db.SchemaInit;
import db.SeedData;
import ui.LoginPanel;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // fallback to default
        }

        try {
            initDB();
        } catch (Exception e) {
            System.err.println("Database Error: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            LoginPanel frame = new LoginPanel();
            frame.setVisible(true);
        });
    }

    private static void initDB() {
        if (DBConnection.getConnection() != null) {
            SchemaInit.initSchema();
            SeedData.seedIfNeeded();
        }
    }
}
