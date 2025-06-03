package water.billing.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;

public class BillDetails extends JFrame {

    public BillDetails(String meterNo) {
        setTitle("Water Bill Details");
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(230, 245, 255)); // Light blue background

        JTable table = new JTable();
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        try {
            Conn c = new Conn();

            String query = "SELECT id, meter_no, customer_name, previous_reading, current_reading, amount_due, bill_month, due_date, status FROM waterbill WHERE meter_no = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, meterNo);

            ResultSet rs = ps.executeQuery();

            table.setModel(DbUtils.resultSetToTableModel(rs));

            // Make table not editable
            table.setDefaultEditor(Object.class, null);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bill details.");
        }

        JScrollPane sp = new JScrollPane(table);
        add(sp);

        setVisible(true);
    }

    public static void main(String[] args) {
        new BillDetails("162166"); // Replace with an actual meter number to test
    }
}
