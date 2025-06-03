package water.billing.system;

import javax.swing.*;
import com.toedter.calendar.JDateChooser; // Import JDateChooser
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class CalculateBill extends JFrame implements ActionListener {

    JTextField tfprevReading, tfcurrReading;
    JButton submit, cancel;
    JLabel lblname;
    Choice meternumber, cmonth;
    JDateChooser dueDateChooser; // Add JDateChooser

    CalculateBill() {
        setTitle("Water Bill Calculation");
        setSize(550, 550);
        setLocationRelativeTo(null); // Center the frame

        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBackground(new Color(173, 216, 230)); // Light blue background
        add(p);

        JLabel heading = new JLabel("Calculate Water Bill");
        heading.setBounds(180, 10, 400, 30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 22));
        p.add(heading);

        JLabel lblmeternumber = new JLabel("Meter Number");
        lblmeternumber.setBounds(100, 80, 150, 20);
        p.add(lblmeternumber);

        meternumber = new Choice();
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT meter_no FROM customer");
            while (rs.next()) {
                meternumber.add(rs.getString("meter_no"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        meternumber.setBounds(260, 80, 200, 20);
        p.add(meternumber);

        JLabel lblnameLabel = new JLabel("Customer Name");
        lblnameLabel.setBounds(100, 120, 150, 20);
        p.add(lblnameLabel);

        lblname = new JLabel();
        lblname.setBounds(260, 120, 200, 20);
        p.add(lblname);

        meternumber.addItemListener(e -> loadCustomerDetails());
        loadCustomerDetails(); // Initial load

        JLabel lblprev = new JLabel("Previous Reading");
        lblprev.setBounds(100, 160, 150, 20);
        p.add(lblprev);

        tfprevReading = new JTextField("100");
        tfprevReading.setBounds(260, 160, 100, 20);
        tfprevReading.setEditable(false);
        p.add(tfprevReading);

        JLabel lblcurr = new JLabel("Current Reading");
        lblcurr.setBounds(100, 200, 150, 20);
        p.add(lblcurr);

        tfcurrReading = new JTextField();
        tfcurrReading.setBounds(260, 200, 100, 20);
        p.add(tfcurrReading);

        JLabel lblmonth = new JLabel("Billing Month");
        lblmonth.setBounds(100, 240, 150, 20);
        p.add(lblmonth);

        cmonth = new Choice();
        cmonth.setBounds(260, 240, 200, 20);
        String[] months = { "January", "February", "March", "April", "May", "June", "July", "August",
                            "September", "October", "November", "December" };
        for (String m : months) cmonth.add(m);
        p.add(cmonth);

        JLabel lbldue = new JLabel("Due Date");
        lbldue.setBounds(100, 280, 150, 20);
        p.add(lbldue);

        dueDateChooser = new JDateChooser();
        dueDateChooser.setBounds(260, 280, 150, 20);
        dueDateChooser.setDateFormatString("yyyy-MM-dd"); // Format display
        p.add(dueDateChooser);

        submit = new JButton("Submit");
        submit.setBounds(150, 400, 120, 30);
        submit.setBackground(Color.BLACK);
        submit.setForeground(Color.WHITE);
        submit.addActionListener(this);
        p.add(submit);

        cancel = new JButton("Cancel");
        cancel.setBounds(300, 400, 120, 30);
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.WHITE);
        cancel.addActionListener(this);
        p.add(cancel);

        setLayout(new BorderLayout());
        add(p, BorderLayout.CENTER);

        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
    }

    void loadCustomerDetails() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT name FROM customer WHERE meter_no = '" + meternumber.getSelectedItem() + "'");
            if (rs.next()) {
                lblname.setText(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {
            String meter = meternumber.getSelectedItem();
            String customer = lblname.getText();
            String prevReading = tfprevReading.getText();
            String currReading = tfcurrReading.getText();
            String month = cmonth.getSelectedItem();
            String dueDate = "";

            // Get date from JDateChooser
            if (dueDateChooser.getDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                dueDate = sdf.format(dueDateChooser.getDate());
            } else {
                JOptionPane.showMessageDialog(this, "Please select a due date.");
                return;
            }

            String status = "Unpaid";

            int previous = 0, current = 0, consumption = 0;
            double totalBill = 0;

            try {
                previous = Integer.parseInt(prevReading);
                current = Integer.parseInt(currReading);
                if (current < previous) {
                    JOptionPane.showMessageDialog(this, "Current reading must be higher than previous reading.");
                    return;
                }
                consumption = current - previous;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid reading input. Please enter valid numbers.");
                return;
            }

            // Compute bill based on tax rates
            try {
                Conn c = new Conn();
                ResultSet rs = c.s.executeQuery("SELECT * FROM tax");

                if (rs.next()) {
                    double costPerUnit = rs.getDouble("cost_per_unit");
                    double meterRent = rs.getDouble("meter_rent");
                    double serviceCharge = rs.getDouble("service_charge");
                    double serviceTax = rs.getDouble("service_tax");
                    double environmentFee = rs.getDouble("environment_fee");
                    double fixedTax = rs.getDouble("fixed_tax");

                    double consumptionCharge = consumption * costPerUnit;
                    double subtotal = consumptionCharge + serviceCharge + environmentFee;
                    double serviceTaxAmount = (serviceTax / 100.0) * subtotal;

                    totalBill = consumptionCharge + meterRent + serviceCharge + environmentFee + fixedTax + serviceTaxAmount;
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error calculating bill. Check console for details.");
                return;
            }

            // Insert into waterbill table
            try {
                Conn c = new Conn();
                String query = "INSERT INTO waterbill (meter_no, customer_name, previous_reading, current_reading, amount_due, bill_month, due_date, status) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = c.c.prepareStatement(query);
                pst.setString(1, meter);
                pst.setString(2, customer);
                pst.setInt(3, previous);
                pst.setInt(4, current);
                pst.setDouble(5, totalBill);
                pst.setString(6, month);
                pst.setString(7, dueDate);
                pst.setString(8, status);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Water Bill Recorded Successfully\nTotal Bill: PHP " + String.format("%.2f", totalBill));
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving bill. Check console for details.");
            }
            

        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new CalculateBill();
    }
}
