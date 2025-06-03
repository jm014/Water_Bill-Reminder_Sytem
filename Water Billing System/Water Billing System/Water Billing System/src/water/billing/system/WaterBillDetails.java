package water.billing.system;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;

public class WaterBillDetails extends JFrame implements ActionListener {

    private Choice meternumber, cmonth;
    private JTable table;
    private JButton search, print, update, cancel;
    private JScrollPane sp;

    WaterBillDetails() {
        super("Water Bill Details");
        setSize(900, 750);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 248, 255)); // AliceBlue background
        setLayout(null);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(220, 240, 255);
                Color color2 = new Color(173, 216, 230);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBounds(20, 20, 850, 680);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true));
        mainPanel.setLayout(null);
        add(mainPanel);

        JLabel heading = new JLabel("WATER BILL DETAILS");
        heading.setBounds(300, 10, 300, 30);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 20));
        heading.setForeground(new Color(25, 25, 112)); // MidnightBlue
        mainPanel.add(heading);

        // Search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setBounds(30, 50, 790, 80);
        searchPanel.setBackground(new Color(255, 255, 255, 180));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Criteria"));
        searchPanel.setLayout(null);
        mainPanel.add(searchPanel);

        JLabel lblMeterNumber = new JLabel("Search By Meter Number:");
        lblMeterNumber.setBounds(20, 25, 180, 25);
        lblMeterNumber.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchPanel.add(lblMeterNumber);

        meternumber = new Choice();
        meternumber.setBounds(200, 25, 200, 25);
        searchPanel.add(meternumber);

        // Load meter numbers from customer table
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT meter_no FROM customer");
            while (rs.next()) {
                meternumber.add(rs.getString("meter_no"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel lblMonth = new JLabel("Search By Month:");
        lblMonth.setBounds(450, 25, 120, 25);
        lblMonth.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchPanel.add(lblMonth);

        cmonth = new Choice();
        cmonth.setBounds(570, 25, 200, 25);
        String[] months = {"January", "February", "March", "April", "May", "June", "July",
                          "August", "September", "October", "November", "December"};
        for (String m : months) cmonth.add(m);
        searchPanel.add(cmonth);

        // Table setup with editable model
        table = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make only amount_due and status editable (columns 5 and 8)
                return column == 5 || column == 8;
            }
        };

        // Load all bills initially
        loadBillData("SELECT id, meter_no, customer_name, previous_reading, current_reading, amount_due, bill_month, due_date, status FROM waterbill");

        sp = new JScrollPane(table);
        sp.setBounds(30, 150, 790, 400);
        mainPanel.add(sp);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(30, 570, 790, 60);
        buttonPanel.setBackground(new Color(255, 255, 255, 180));
        buttonPanel.setLayout(new GridLayout(1, 4, 15, 0));
        mainPanel.add(buttonPanel);

        search = createButton("Search", new Color(0, 102, 204));
        buttonPanel.add(search);

        print = createButton("Print", new Color(0, 153, 51));
        buttonPanel.add(print);

        update = createButton("Update", new Color(255, 153, 0));
        update.addActionListener(e -> updateBillData());
        buttonPanel.add(update);

        cancel = createButton("Cancel", new Color(204, 0, 0));
        cancel.addActionListener(e -> dispose());
        buttonPanel.add(cancel);

        setVisible(true);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(this);
        return button;
    }

    private void loadBillData(String query) {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery(query);
            table.setModel(DbUtils.resultSetToTableModel(rs));
            
            // Set column headers
            String[] columnNames = {"ID", "Meter No", "Customer Name", "Prev Reading", 
                                  "Curr Reading", "Amount Due", "Bill Month", "Due Date", "Status"};
            ((DefaultTableModel)table.getModel()).setColumnIdentifiers(columnNames);
            
            // Set better column widths
            table.getColumnModel().getColumn(0).setPreferredWidth(30);  // ID
            table.getColumnModel().getColumn(1).setPreferredWidth(80);  // Meter No
            table.getColumnModel().getColumn(2).setPreferredWidth(150); // Customer Name
            table.getColumnModel().getColumn(3).setPreferredWidth(80);  // Prev Reading
            table.getColumnModel().getColumn(4).setPreferredWidth(80);  // Curr Reading
            table.getColumnModel().getColumn(5).setPreferredWidth(80);  // Amount Due
            table.getColumnModel().getColumn(6).setPreferredWidth(100); // Bill Month
            table.getColumnModel().getColumn(7).setPreferredWidth(100); // Due Date
            table.getColumnModel().getColumn(8).setPreferredWidth(80);  // Status
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bill data");
        }
    }

    private void updateBillData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        try {
            Conn c = new Conn();
            
            for (int i = 0; i < model.getRowCount(); i++) {
                String id = model.getValueAt(i, 0).toString();
                String amountDue = model.getValueAt(i, 5).toString();
                String status = model.getValueAt(i, 8).toString();
                
                String query = "UPDATE waterbill SET amount_due = '"+amountDue+"', status = '"+status+"' WHERE id = '"+id+"'";
                c.s.executeUpdate(query);
            }
            
            JOptionPane.showMessageDialog(this, "Bill records updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating bill records");
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == search) {
            String meter = meternumber.getSelectedItem();
            String month = cmonth.getSelectedItem();

            String query = "SELECT id, meter_no, customer_name, previous_reading, current_reading, amount_due, bill_month, due_date, status " +
                         "FROM waterbill WHERE meter_no = '" + meter + "' AND bill_month = '" + month + "'";
            loadBillData(query);
        } else if (ae.getSource() == print) {
            try {
                boolean complete = table.print();
                if (!complete) {
                    JOptionPane.showMessageDialog(this, "Printing was cancelled");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error printing table.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WaterBillDetails());
    }
}