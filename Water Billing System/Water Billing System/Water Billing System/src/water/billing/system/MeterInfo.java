package water.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MeterInfo extends JFrame implements ActionListener {

    JTextField tfname, tfaddress, tfstate, tfcity, tfemail, tfphone;
    JButton next, cancel;
    JLabel lblmeter;
    Choice meterlocation, metertype, phasecode, billtype;
    String meternumber;

    public MeterInfo(String meternumber) {
        this.meternumber = meternumber;

        setTitle("Meter Information");
        setSize(600, 450);
        setLocationRelativeTo(null);  // Center the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 248, 250)); // very light blue-gray
        panel.setLayout(null);
        add(panel);

        JLabel heading = new JLabel("Meter Information");
        heading.setBounds(150, 15, 300, 40); 
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(new Color(30, 144, 255)); // Dodger Blue
        panel.add(heading);

        // Labels and fields
        JLabel lblMeterNumber = new JLabel("Meter Number:");
        lblMeterNumber.setBounds(100, 70, 120, 25);
        lblMeterNumber.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(lblMeterNumber);

        JLabel lblMeterValue = new JLabel(meternumber);
        lblMeterValue.setBounds(240, 70, 200, 25);
        lblMeterValue.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(lblMeterValue);

        JLabel lblMeterLocation = new JLabel("Meter Location:");
        lblMeterLocation.setBounds(100, 110, 120, 25);
        lblMeterLocation.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(lblMeterLocation);

        meterlocation = new Choice();
        meterlocation.add("Outside");
        meterlocation.add("Inside");
        meterlocation.setBounds(240, 110, 220, 25);
        panel.add(meterlocation);

        JLabel lblMeterType = new JLabel("Meter Type:");
        lblMeterType.setBounds(100, 150, 120, 25);
        lblMeterType.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(lblMeterType);

        metertype = new Choice();
        metertype.add("Standard Water Meter");
        metertype.add("Smart Water Meter");
        metertype.add("Bulk Water Meter");
        metertype.setBounds(240, 150, 220, 25);
        panel.add(metertype);

        JLabel lblPhaseCode = new JLabel("Phase Code:");
        lblPhaseCode.setBounds(100, 190, 120, 25);
        lblPhaseCode.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(lblPhaseCode);

        phasecode = new Choice();
        String[] codes = {"011", "022", "033", "044", "055", "066", "077", "088", "099"};
        for (String code : codes) phasecode.add(code);
        phasecode.setBounds(240, 190, 220, 25);
        panel.add(phasecode);

        JLabel lblBillType = new JLabel("Bill Type:");
        lblBillType.setBounds(100, 230, 120, 25);
        lblBillType.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(lblBillType);

        billtype = new Choice();
        billtype.add("Normal");
        billtype.add("Industrial");
        billtype.setBounds(240, 230, 220, 25);
        panel.add(billtype);

        JLabel lblDays = new JLabel("Days:");
        lblDays.setBounds(100, 270, 120, 25);
        lblDays.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(lblDays);

        JLabel lblDaysValue = new JLabel("30 Days");
        lblDaysValue.setBounds(240, 270, 220, 25);
        lblDaysValue.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(lblDaysValue);

        JLabel lblNote = new JLabel("Note:");
        lblNote.setBounds(100, 310, 120, 25);
        lblNote.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(lblNote);

        JLabel lblNoteText = new JLabel("By default, bill is calculated for 30 days only.");
        lblNoteText.setBounds(240, 310, 320, 25);
        lblNoteText.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblNoteText.setForeground(Color.DARK_GRAY);
        panel.add(lblNoteText);

        // Buttons
        next = new JButton("Submit");
        next.setBounds(180, 360, 110, 35);
        next.setBackground(new Color(30, 144, 255));
        next.setForeground(Color.WHITE);
        next.setFocusPainted(false);
        next.setFont(new Font("Segoe UI", Font.BOLD, 16));
        next.addActionListener(this);
        panel.add(next);

        cancel = new JButton("Cancel");
        cancel.setBounds(320, 360, 110, 35);
        cancel.setBackground(Color.GRAY);
        cancel.setForeground(Color.WHITE);
        cancel.setFocusPainted(false);
        cancel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cancel.addActionListener(this);
        panel.add(cancel);

        getContentPane().setBackground(Color.WHITE);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
       if (ae.getSource() == next) {
            String meter = meternumber;
            String location = meterlocation.getSelectedItem();
            String type = metertype.getSelectedItem();
            String code = phasecode.getSelectedItem();
            String typebill = billtype.getSelectedItem();
            String days = "30";

            String query = "INSERT INTO meter_info VALUES ('" + meter + "', '" + location + "', '" + type + "', '" + code + "', '" + typebill + "', '" + days + "')";

            try {
                Conn c = new Conn();
                c.s.executeUpdate(query);

                JOptionPane.showMessageDialog(null, "Meter Information Added Successfully");
                setVisible(false);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error adding meter info: " + e.getMessage());
                e.printStackTrace();
            }
        } else if (ae.getSource() == cancel) {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new MeterInfo("");
    }
}
