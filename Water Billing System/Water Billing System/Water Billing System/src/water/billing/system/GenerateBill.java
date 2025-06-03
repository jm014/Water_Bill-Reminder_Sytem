package water.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.*;

public class GenerateBill extends JFrame implements ActionListener {

    private String meter;
    private JButton billButton, printButton, saveButton;
    private Choice monthChoice;
    private JTextArea billArea;

    public GenerateBill(String meter) {
        this.meter = meter;

        setTitle("Generate Water Bill");
        setSize(600, 700);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Add padding between panels

        // --- Top Panel ---
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(0xE3F2FD));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        JLabel heading = new JLabel("Generate Water Bill");
        heading.setFont(new Font("Tahoma", Font.BOLD, 22));
        heading.setForeground(new Color(0x0D47A1));

        JLabel meterLabel = new JLabel("Meter No: " + meter);
        meterLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));

        monthChoice = new Choice();
        for (String m : new String[]{"January", "February", "March", "April", "May", "June",
                                     "July", "August", "September", "October", "November", "December"}) {
            monthChoice.add(m);
        }

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        topPanel.add(heading, gbc);
        gbc.gridy++;
        topPanel.add(meterLabel, gbc);
        gbc.gridy++;
        topPanel.add(monthChoice, gbc);

        // --- Center Panel (Bill Area) ---
        billArea = new JTextArea();
        billArea.setText("\n\n\t-------- Click 'Generate Bill' --------\n\t to get the water bill of the selected month.");
        billArea.setFont(new Font("Serif", Font.ITALIC, 16));
        billArea.setEditable(false);
        billArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(billArea);

        // --- Bottom Panel (Buttons) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        billButton = new JButton("Generate Bill");
        printButton = new JButton("Print Bill");
        saveButton = new JButton("Save to File");

        billButton.setBackground(new Color(0x4CAF50));
        billButton.setForeground(Color.WHITE);
        printButton.setBackground(new Color(0x2196F3));
        printButton.setForeground(Color.WHITE);
        saveButton.setBackground(new Color(0xFF9800));
        saveButton.setForeground(Color.WHITE);

        billButton.setFocusPainted(false);
        printButton.setFocusPainted(false);
        saveButton.setFocusPainted(false);

        billButton.addActionListener(this);
        printButton.addActionListener(this);
        saveButton.addActionListener(this);

        bottomPanel.add(billButton);
        bottomPanel.add(printButton);
        bottomPanel.add(saveButton);

        // --- Add to Frame ---
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        if (source == billButton) {
            generateBill();
        } else if (source == printButton) {
            try {
                billArea.print();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error printing bill.");
            }
        } else if (source == saveButton) {
            saveBillToFile();
        }
    }

    private void generateBill() {
        try {
            Conn c = new Conn();
            String month = monthChoice.getSelectedItem();

            billArea.setText("\tLunod sa Utang Water Supply\n" +
                    "------------------------------------------------\n" +
                    "\tWATER BILL FOR THE MONTH OF \n" + month + ", 2025\n\n");

            ResultSet rs = c.s.executeQuery("SELECT * FROM customer WHERE meter_no = '" + meter + "'");
            if (rs.next()) {
                billArea.append("Customer Name : " + rs.getString("name") + "\n");
                billArea.append("Meter Number  : " + rs.getString("meter_no") + "\n");
                billArea.append("Address       : " + rs.getString("address") + "\n");
                billArea.append("City          : " + rs.getString("city") + "\n");
                billArea.append("Province      : " + rs.getString("province") + "\n");
                billArea.append("Email         : " + rs.getString("email") + "\n");
                billArea.append("Phone         : " + rs.getString("phone") + "\n");
                billArea.append("-------------------------------------------\n\n");
            } else {
                billArea.append("No customer found with meter number: " + meter);
                return;
            }

            rs = c.s.executeQuery("SELECT * FROM tax");
            if (rs.next()) {
                billArea.append("Cost Per Cubic Meter : " + rs.getString("cost_per_unit") + "\n");
                billArea.append("Meter Rent           : " + rs.getString("meter_rent") + "\n");
                billArea.append("Service Charge       : " + rs.getString("service_charge") + "\n");
                billArea.append("Service Tax          : " + rs.getString("service_tax") + "\n");
                billArea.append("Environment Fee      : " + rs.getString("environment_fee") + "\n");
                billArea.append("Fixed Tax            : " + rs.getString("fixed_tax") + "\n");
                billArea.append("-------------------------------------------\n\n");
            }

            rs = c.s.executeQuery("SELECT * FROM waterbill WHERE meter_no = '" + meter + "' AND bill_month = '" + month + "'");
            if (rs.next()) {
                int prev = rs.getInt("previous_reading");
                int curr = rs.getInt("current_reading");
                int units = curr - prev;

                billArea.append("Bill Month       : " + month + "\n");
                billArea.append("Previous Reading : " + prev + " cubic meters\n");
                billArea.append("Current Reading  : " + curr + " cubic meters\n");
                billArea.append("Units Consumed   : " + units + " cubic meters\n");
                billArea.append("Amount Due       : Php." + rs.getString("amount_due") + "\n");
                billArea.append("Due Date         : " + rs.getString("due_date") + "\n");
                billArea.append("Payment Status   : " + rs.getString("status") + "\n");
                billArea.append("-------------------------------------------\n");
                billArea.append("\nTotal Payable Amount: Php." + rs.getString("amount_due") + "\n");
            } else {
                billArea.append("\nNo bill found for this meter and month.\n");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating bill.");
        }
    }

    private void saveBillToFile() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Bill as Text File");
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(billArea.getText());
                    JOptionPane.showMessageDialog(this, "Bill saved successfully!");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving bill to file.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GenerateBill("162166"));
    }
}
