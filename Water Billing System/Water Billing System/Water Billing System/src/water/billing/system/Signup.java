package water.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Signup extends JFrame implements ActionListener {

    private JButton create, back;
    private Choice accountType;
    private JTextField meter, username, name;
    private JPasswordField password;

    public Signup() {
        setTitle("Signup - Water Billing System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 150, 700, 450);
        setLayout(null);

        getContentPane().setBackground(new Color(240, 248, 255)); // Light background (Alice Blue)

        JPanel panel = new JPanel();
        panel.setBounds(30, 30, 620, 350);
        panel.setBackground(new Color(225, 240, 255)); // Light Blue Panel
        panel.setLayout(null);
        add(panel);

        // Title
        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Tahoma", Font.BOLD, 22));
        title.setForeground(new Color(0, 123, 255));
        title.setBounds(230, 10, 250, 30);
        panel.add(title);

        // Labels and Fields
        JLabel lblAccount = createLabel("Account Type:", 100, 60);
        panel.add(lblAccount);

        accountType = new Choice();
        accountType.add("Admin");
        accountType.add("Customer");
        accountType.setBounds(250, 60, 220, 28);
        panel.add(accountType);

        JLabel lblMeter = createLabel("Meter Number:", 100, 100);
        panel.add(lblMeter);

        meter = new JTextField();
        meter.setBounds(250, 100, 220, 28);
        panel.add(meter);
        lblMeter.setVisible(false);
        meter.setVisible(false);

        JLabel lblUsername = createLabel("Username:", 100, 140);
        panel.add(lblUsername);

        username = new JTextField();
        username.setBounds(250, 140, 220, 28);
        panel.add(username);

        JLabel lblName = createLabel("Name:", 100, 180);
        panel.add(lblName);

        name = new JTextField();
        name.setBounds(250, 180, 220, 28);
        panel.add(name);

        JLabel lblPassword = createLabel("Password:", 100, 220);
        panel.add(lblPassword);

        password = new JPasswordField();
        password.setBounds(250, 220, 220, 28);
        panel.add(password);

        // Buttons
        create = new JButton("Create");
        styleButton(create, new Color(0, 123, 255)); // Blue
        create.setBounds(180, 280, 120, 35);
        create.addActionListener(this);
        panel.add(create);

        back = new JButton("Back");
        styleButton(back, new Color(220, 53, 69)); // Red
        back.setBounds(320, 280, 120, 35);
        back.addActionListener(this);
        panel.add(back);

        // Account type logic
        accountType.addItemListener(e -> {
            if (accountType.getSelectedItem().equals("Customer")) {
                lblMeter.setVisible(true);
                meter.setVisible(true);
                name.setEditable(false);
            } else {
                lblMeter.setVisible(false);
                meter.setVisible(false);
                name.setEditable(true);
            }
        });

        // Autofill name for customers
        meter.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                try {
                    Conn c = new Conn();
                    ResultSet rs = c.s.executeQuery("SELECT * FROM login WHERE meter_no = '" + meter.getText() + "'");
                    if (rs.next()) {
                        name.setText(rs.getString("name"));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        setVisible(true);
    }

    private JLabel createLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 140, 28);
        label.setFont(new Font("Tahoma", Font.BOLD, 14));
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Tahoma", Font.BOLD, 13));
        button.setBorderPainted(false); // Removes border line
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == create) {
            String atype = accountType.getSelectedItem();
            String susername = username.getText();
            String sname = name.getText();
            String spassword = new String(password.getPassword());
            String smeter = meter.getText();

            try {
                Conn c = new Conn();
                String query;

                if (atype.equals("Admin")) {
                    query = "INSERT INTO login (meter_no, username, name, password, user) VALUES "
                          + "('" + smeter + "', '" + susername + "', '" + sname + "', '" + spassword + "', '" + atype + "')";
                } else {
                    query = "UPDATE login SET username='" + susername + "', password='" + spassword + "', user='" + atype + "' "
                          + "WHERE meter_no='" + smeter + "'";
                }

                c.s.executeUpdate(query);
                JOptionPane.showMessageDialog(this, "Account Created Successfully");

                dispose();
                new Login();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else if (e.getSource() == back) {
            dispose();
            new Login();
        }
    }

    public static void main(String[] args) {
        new Signup();
    }
}
