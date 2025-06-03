package water.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PayBill extends JFrame implements ActionListener {

    private Choice cmonth;
    private JButton pay, back;
    private String meter;

    // Labels to update dynamically
    private JLabel labelname, labelunits, labeltotalbill, labelstatus;

    public PayBill(String meter) {
        this.meter = meter;

        setTitle("Water Bill Payment");
        setSize(500, 500);
        setLocationRelativeTo(null);  // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main container panel with padding and border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        setContentPane(mainPanel);

        // Heading label at the top
        JLabel heading = new JLabel("Water Bill Payment");
        heading.setFont(new Font("Tahoma", Font.BOLD, 28));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setForeground(new Color(0, 102, 204));
        mainPanel.add(heading, BorderLayout.NORTH);

        // Left panel for form inputs
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // padding around components
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1: Meter Number
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Meter Number:"), gbc);

        gbc.gridx = 1;
        JLabel lblMeterNumber = new JLabel(meter);
        lblMeterNumber.setFont(new Font("SansSerif", Font.BOLD, 14));
        formPanel.add(lblMeterNumber, gbc);

        // Row 2: Customer Name
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Customer Name:"), gbc);

        gbc.gridx = 1;
        labelname = new JLabel("");
        labelname.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formPanel.add(labelname, gbc);

        // Row 3: Month Choice
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Month:"), gbc);

        gbc.gridx = 1;
        cmonth = new Choice();
        String[] months = { "January", "February", "March", "April", "May", "June",
                            "July", "August", "September", "October", "November", "December" };
        for (String m : months) cmonth.add(m);
        formPanel.add(cmonth, gbc);

        // Row 4: Cubic Meters
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Cubic Meters:"), gbc);

        gbc.gridx = 1;
        labelunits = new JLabel("");
        labelunits.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formPanel.add(labelunits, gbc);

        // Row 5: Total Bill
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Total Bill (PHP):"), gbc);

        gbc.gridx = 1;
        labeltotalbill = new JLabel("");
        labeltotalbill.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formPanel.add(labeltotalbill, gbc);

        // Row 6: Payment Status
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Payment Status:"), gbc);

        gbc.gridx = 1;
        labelstatus = new JLabel("");
        labelstatus.setFont(new Font("SansSerif", Font.BOLD, 14));
        labelstatus.setForeground(Color.RED);
        formPanel.add(labelstatus, gbc);

        // Button panel at the bottom of the form panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        pay = new JButton("Pay Now");
        pay.setBackground(new Color(0, 102, 204));
        pay.setForeground(Color.WHITE);
        pay.setFocusPainted(false);
        pay.addActionListener(this);
        buttonPanel.add(pay);

        back = new JButton("Back");
        back.setBackground(Color.DARK_GRAY);
        back.setForeground(Color.WHITE);
        back.setFocusPainted(false);
        back.addActionListener(this);
        buttonPanel.add(back);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Right side panel for image
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(Color.WHITE);
        mainPanel.add(imagePanel, BorderLayout.EAST);

        try {
            ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/waterbill.png")); // Update path if needed
            Image i2 = i1.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
            ImageIcon i3 = new ImageIcon(i2);
            JLabel image = new JLabel(i3);
            imagePanel.add(image);
        } catch (Exception e) {
            System.out.println("Image not found");
        }

        loadCustomerData();
        addMonthListener();

        setVisible(true);
    }

    private void loadCustomerData() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM customer WHERE meter_no = '" + meter + "'");
            if (rs.next()) {
                labelname.setText(rs.getString("name"));
            }

            // Load initial data for January or selected month
            loadBillData("January");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMonthListener() {
        cmonth.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                loadBillData(cmonth.getSelectedItem());
            }
        });
    }

    private void loadBillData(String month) {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM waterbill WHERE meter_no = '" + meter + "' AND bill_month = '" + month + "'");
            if (rs.next()) {
                int prev = rs.getInt("previous_reading");
                int curr = rs.getInt("current_reading");
                int cubic_meters = curr - prev;

                labelunits.setText(String.valueOf(cubic_meters));
                labeltotalbill.setText(rs.getString("amount_due"));
                labelstatus.setText(rs.getString("status"));
            } else {
                labelunits.setText("-");
                labeltotalbill.setText("-");
                labelstatus.setText("-");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == pay) {
            try {
                Conn c = new Conn();
                String month = cmonth.getSelectedItem();
                int updated = c.s.executeUpdate("UPDATE waterbill SET status = 'Paid' WHERE meter_no = '" + meter + "' AND bill_month = '" + month + "'");
                if (updated > 0) {
                    JOptionPane.showMessageDialog(null, "Payment Successful! Thank you.");
                    loadBillData(month); // Refresh status
                } else {
                    JOptionPane.showMessageDialog(null, "Payment failed or already paid.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == back) {
            this.dispose();
        }
    }

    public static void main(String[] args) {
        new PayBill("12345");  // Example meter number for testing
    }
}
