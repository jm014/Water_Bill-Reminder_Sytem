package water.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class NewCustomer extends JFrame implements ActionListener {

    private JTextField tfname, tfaddress, tfprovince, tfcity, tfemail, tfphone;
    private JButton next, cancel;
    private JLabel lblmeter;

    public NewCustomer() {
        setTitle("New Customer Registration");
        setSize(600, 500);
        setLocationRelativeTo(null);  // center screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255)); // Alice Blue

        // Heading
        JLabel heading = new JLabel("New Customer");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(30, 144, 255));  // Dodger Blue
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(heading, BorderLayout.NORTH);

        // Form panel with GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0 - Customer Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Customer Name:"), gbc);

        tfname = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(tfname, gbc);

        // Row 1 - Meter Number (Label only)
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Meter Number:"), gbc);

        lblmeter = new JLabel();
        lblmeter.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblmeter.setForeground(new Color(25, 25, 112)); // Midnight Blue
        gbc.gridx = 1;
        formPanel.add(lblmeter, gbc);

        // Generate random meter number
        Random ran = new Random();
        long number = ran.nextLong() % 1000000;
        lblmeter.setText(String.format("%06d", Math.abs(number)));

        // Row 2 - Address
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Address:"), gbc);

        tfaddress = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(tfaddress, gbc);

        // Row 3 - City
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("City:"), gbc);

        tfcity = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(tfcity, gbc);

        // Row 4 - Province
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Province:"), gbc);

        tfprovince = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(tfprovince, gbc);

        // Row 5 - Email
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Email:"), gbc);

        tfemail = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(tfemail, gbc);

        // Row 6 - Phone Number
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Phone Number:"), gbc);

        tfphone = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(tfphone, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        next = new JButton("Next");
        next.setBackground(new Color(30, 144, 255));
        next.setForeground(Color.WHITE);
        next.setFocusPainted(false);
        next.setFont(new Font("Segoe UI", Font.BOLD, 14));
        next.addActionListener(this);

        cancel = new JButton("Cancel");
        cancel.setBackground(new Color(220, 20, 60));  // Crimson
        cancel.setForeground(Color.WHITE);
        cancel.setFocusPainted(false);
        cancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancel.addActionListener(this);

        buttonPanel.add(next);
        buttonPanel.add(cancel);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

 @Override
public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == next) {
        String name = tfname.getText();
        String meter = lblmeter.getText();
        String address = tfaddress.getText();
        String city = tfcity.getText();
        String province = tfprovince.getText();
        String email = tfemail.getText();
        String phone = tfphone.getText();

        if (name.isEmpty() || address.isEmpty() || city.isEmpty() || province.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all fields");
            return;
        }

        try {
            Conn c = new Conn();

            // Insert customer info
            String insertCustomer = "INSERT INTO customer (meter_no, name, address, city, province, email, phone) VALUES ("
                    + "'" + meter + "',"
                    + "'" + name + "',"
                    + "'" + address + "',"
                    + "'" + city + "',"
                    + "'" + province + "',"
                    + "'" + email + "',"
                    + "'" + phone + "')";

            c.s.executeUpdate(insertCustomer);

            // Insert login info
            String username = email; // or phone, or any unique username
            String password = ""; 
            String userType = "customer";

            String insertLogin = "INSERT INTO login (meter_no, username, name, password, user) VALUES ("
                    + "'" + meter + "',"
                    + "'" + username + "',"
                    + "'" + name + "',"
                    + "'" + password + "',"
                    + "'" + userType + "')";

            c.s.executeUpdate(insertLogin);

            JOptionPane.showMessageDialog(null, "Customer and login information saved successfully!");

            // Open MeterInfo form, pass meter number
            new MeterInfo(meter);

            // Close this form
            this.setVisible(false);
            this.dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saving info: " + e.getMessage());
            e.printStackTrace();
        }

    } else if (ae.getSource() == cancel) {
        this.setVisible(false);
        this.dispose();
    }
}




    public static void main(String[] args) {
       new NewCustomer();
    }
}
