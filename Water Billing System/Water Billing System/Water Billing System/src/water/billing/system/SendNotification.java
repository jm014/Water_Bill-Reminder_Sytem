package water.billing.system;

import water.billing.system.Conn;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SendNotification extends JFrame {
    private JComboBox<String> customerCombo;
    private JTextArea messageArea;

    public SendNotification() {
        setTitle("Send Notification");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 120, 215));
        JLabel titleLabel = new JLabel("SEND NOTIFICATION");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Customer Selection
        JPanel customerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        customerPanel.add(new JLabel("Select Customer:"));
        customerCombo = new JComboBox<>();
        loadCustomers();
        customerPanel.add(customerCombo);
        formPanel.add(customerPanel);

        // Message Area
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(new JLabel("Message:"), BorderLayout.NORTH);
        messageArea = new JTextArea(8, 30);
        messageArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        messagePanel.add(scrollPane, BorderLayout.CENTER);
        formPanel.add(messagePanel);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        JButton sendBtn = new JButton("Send Notification");
        JButton cancelBtn = new JButton("Cancel");

        sendBtn.addActionListener(e -> sendNotification());
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(sendBtn);
        buttonPanel.add(cancelBtn);
        formPanel.add(buttonPanel);

        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void loadCustomers() {
        try {
            Conn conn = new Conn();
            ResultSet rs = conn.s.executeQuery("SELECT meter_no, name FROM customer");
            
            customerCombo.addItem("All Customers");
            while (rs.next()) {
                customerCombo.addItem(rs.getString("meter_no") + " - " + rs.getString("name"));
            }
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading customers: " + e.getMessage());
        }
    }

    private void sendNotification() {
        String customer = (String) customerCombo.getSelectedItem();
        String message = messageArea.getText();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        if (message.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Message cannot be empty!");
            return;
        }

        try {
            Conn conn = new Conn();
            if (customer.equals("All Customers")) {
                // Send to all customers
                ResultSet rs = conn.s.executeQuery("SELECT meter_no FROM customer");
                while (rs.next()) {
                    String meterNo = rs.getString("meter_no");
                    String query = "INSERT INTO notifications (meter_no, message, date) " +
                                   "VALUES ('" + meterNo + "', '" + message + "', '" + date + "')";
                    conn.s.executeUpdate(query);
                }
                rs.close();
            } else {
                // Send to single customer
                String meterNo = customer.split(" - ")[0];
                String query = "INSERT INTO notifications (meter_no, message, date) " +
                               "VALUES ('" + meterNo + "', '" + message + "', '" + date + "')";
                conn.s.executeUpdate(query);
            }
            
            JOptionPane.showMessageDialog(this, "Notification sent successfully!");
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error sending notification: " + e.getMessage());
        }
    }
}