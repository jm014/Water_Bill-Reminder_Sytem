package water.billing.system;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;
import javax.swing.table.*;

public class CustomerDetails extends JFrame implements ActionListener{

    Choice meternumber, cmonth;
    JTable table;
    JButton search, print;
    
    CustomerDetails(){
        super("Customer Details");
        
        setSize(1200, 650);
        setLocation(200, 150);
        
        table = new JTable() {
            // Disable cell editing
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from customer");
            
            // Set the model and make sure editing is disabled
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JScrollPane sp = new JScrollPane(table);
        add(sp);
        
        print = new JButton("Print");
        print.addActionListener(this);
        add(print, "South");
        
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        try {
            table.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new CustomerDetails();
    }
}
