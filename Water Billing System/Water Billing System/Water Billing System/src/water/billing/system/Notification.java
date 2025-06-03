package water.billing.system;

import water.billing.system.Conn;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Notification extends JFrame {
    private JTable notificationTable;
    private String meter;

    public Notification(String meter) {
        this.meter = meter;
        setTitle("My Notifications");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 120, 215));
        JLabel titleLabel = new JLabel("MY NOTIFICATIONS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Table setup
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Date");
        model.addColumn("Message");

        notificationTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(notificationTable);
        add(scrollPane, BorderLayout.CENTER);

        loadNotifications();

        setVisible(true);
    }

    private void loadNotifications() {
        DefaultTableModel model = (DefaultTableModel) notificationTable.getModel();
        model.setRowCount(0); // Clear existing data

        try {
            Conn conn = new Conn();
            ResultSet rs = conn.s.executeQuery(
                "SELECT date, message " +
                "FROM notifications " +
                "WHERE meter_no = '" + meter + "' " +
                "ORDER BY date DESC"
            );

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("date"),
                    rs.getString("message")
                });
            }
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading notifications: " + e.getMessage());
        }
    }
}