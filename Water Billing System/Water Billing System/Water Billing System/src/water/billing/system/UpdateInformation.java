package water.billing.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;

public class UpdateInformation extends JFrame implements ActionListener {

    private JTextField tfaddress, tfprovince, tfcity, tfemail, tfphone;
    private JButton update, cancel;
    private String meter;
    private JLabel name;

    UpdateInformation(String meter) {
        this.meter = meter;
        setTitle("Update Customer Information");
        setSize(500, 500);
        setLocationRelativeTo(null); // Center the window
        getContentPane().setBackground(new Color(225, 240, 255));
        setLayout(null);
        
        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setBounds(20, 20, 750, 420);
        mainPanel.setBackground(new Color(225, 240, 255));
        mainPanel.setLayout(null);
        add(mainPanel);

        JLabel heading = new JLabel("UPDATE CUSTOMER INFORMATION");
        heading.setBounds(50, 10, 400, 30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 18));
        heading.setForeground(new Color(0, 102, 204));
        mainPanel.add(heading);
        
        // Left side form panel
        JPanel formPanel = new JPanel();
        formPanel.setBounds(30, 50, 400, 350);
        formPanel.setBackground(new Color(225, 240, 255));
        formPanel.setLayout(null);
        mainPanel.add(formPanel);

        JLabel lblname = new JLabel("Name:");
        lblname.setBounds(20, 20, 100, 20);
        lblname.setFont(new Font("Tahoma", Font.PLAIN, 14));
        formPanel.add(lblname);
        
        name = new JLabel("");
        name.setBounds(150, 20, 230, 20);
        name.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(name);
        
        JLabel lblmeternumber = new JLabel("Meter Number:");
        lblmeternumber.setBounds(20, 60, 120, 20);
        lblmeternumber.setFont(new Font("Tahoma", Font.PLAIN, 14));
        formPanel.add(lblmeternumber);
        
        JLabel meternumber = new JLabel("");
        meternumber.setBounds(150, 60, 230, 20);
        meternumber.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(meternumber);
        
        JLabel lbladdress = new JLabel("Address:");
        lbladdress.setBounds(20, 100, 100, 20);
        lbladdress.setFont(new Font("Tahoma", Font.PLAIN, 14));
        formPanel.add(lbladdress);
        
        tfaddress = new JTextField();
        tfaddress.setBounds(150, 100, 230, 25);
        formPanel.add(tfaddress);
        
        JLabel lblcity = new JLabel("City:");
        lblcity.setBounds(20, 140, 100, 20);
        lblcity.setFont(new Font("Tahoma", Font.PLAIN, 14));
        formPanel.add(lblcity);
        
        tfcity = new JTextField();
        tfcity.setBounds(150, 140, 230, 25);
        formPanel.add(tfcity);
        
        JLabel lblprovince = new JLabel("Province:");
        lblprovince.setBounds(20, 180, 100, 20);
        lblprovince.setFont(new Font("Tahoma", Font.PLAIN, 14));
        formPanel.add(lblprovince);
        
        tfprovince = new JTextField();
        tfprovince.setBounds(150, 180, 230, 25);
        formPanel.add(tfprovince);
        
        JLabel lblemail = new JLabel("Email:");
        lblemail.setBounds(20, 220, 100, 20);
        lblemail.setFont(new Font("Tahoma", Font.PLAIN, 14));
        formPanel.add(lblemail);
        
        tfemail = new JTextField();
        tfemail.setBounds(150, 220, 230, 25);
        formPanel.add(tfemail);
        
        JLabel lblphone = new JLabel("Phone:");
        lblphone.setBounds(20, 260, 100, 20);
        lblphone.setFont(new Font("Tahoma", Font.PLAIN, 14));
        formPanel.add(lblphone);
        
        tfphone = new JTextField();
        tfphone.setBounds(150, 260, 230, 25);
        formPanel.add(tfphone);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(100, 300, 220, 40);
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 0));
        formPanel.add(buttonPanel);
        
        update = new JButton("Update");
        update.setBackground(new Color(0, 102, 204));
        update.setForeground(Color.WHITE);
        update.setFont(new Font("Tahoma", Font.BOLD, 14));
        update.setFocusPainted(false);
        buttonPanel.add(update);
        update.addActionListener(this);
        
        cancel = new JButton("Cancel");
        cancel.setBackground(new Color(204, 0, 0));
        cancel.setForeground(Color.WHITE);
        cancel.setFont(new Font("Tahoma", Font.BOLD, 14));
        cancel.setFocusPainted(false);
        buttonPanel.add(cancel);
        cancel.addActionListener(this);

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from customer where meter_no = '"+meter+"'");
            while(rs.next()) {
                name.setText(rs.getString("name"));
                tfaddress.setText(rs.getString("address"));
                tfcity.setText(rs.getString("city"));
                tfprovince.setText(rs.getString("province"));
                tfemail.setText(rs.getString("email"));
                tfphone.setText(rs.getString("phone"));
                meternumber.setText(rs.getString("meter_no"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == update) {
            String address = tfaddress.getText();
            String city = tfcity.getText();
            String province = tfprovince.getText();
            String email = tfemail.getText();
            String phone = tfphone.getText();
            
            if (address.isEmpty() || city.isEmpty() || province.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                Conn c = new Conn();
                c.s.executeUpdate("update customer set address = '"+address+"', city = '"+city+"', province = '"+province+"', email = '"+email+"', phone = '"+phone+"' where meter_no = '"+meter+"'");

                JOptionPane.showMessageDialog(this, "User Information Updated Successfully");
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating information", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UpdateInformation("");
        });
    }
}
