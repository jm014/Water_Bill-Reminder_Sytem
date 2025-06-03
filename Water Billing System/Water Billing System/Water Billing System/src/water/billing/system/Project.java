package water.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Project extends JFrame implements ActionListener {

    private final String userType;
    private final String meter;

    public Project(String userType, String meter) {
        this.userType = userType;
        this.meter = meter;

        setTitle("Water Billing and Reminder System");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Main Panel with Blue Gradient Header
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header Panel with Gradient Background
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                Color color1 = new Color(0x007BFF);
                Color color2 = new Color(0x00BFFF);
                GradientPaint gp = new GradientPaint(0, 0, color1, width, height, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("Water Billing and Reminder System");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Menu Bar setup
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(0xF8F9FA)); // Light Gray

        // Create menus with colors
        JMenu masterMenu = createMenu("Menu", Color.BLUE);
        JMenu infoMenu = createMenu("Information", Color.BLUE); 
        JMenu userMenu = createMenu("User", Color.BLUE);
        JMenu reportMenu = createMenu("Report", Color.BLUE);
        JMenu utilityMenu = createMenu("Option", Color.BLUE);
        JMenu logoutMenu = createMenu("Logout", Color.RED);

        // Add menu items based on userType
        if ("Admin".equalsIgnoreCase(userType)) {
            masterMenu.add(createMenuItem("New Customer"));
            masterMenu.add(createMenuItem("Customer Details"));
            masterMenu.add(createMenuItem("Water Bill Details"));
            masterMenu.add(createMenuItem("Calculate Bill"));
            masterMenu.add(createMenuItem("SendNotification"));
            menuBar.add(masterMenu);
        } else {
            infoMenu.add(createMenuItem("Update Information"));
            infoMenu.add(createMenuItem("View Information"));

            userMenu.add(createMenuItem("Pay Bill"));
            userMenu.add(createMenuItem("Bill Details"));
            userMenu.add(createMenuItem("Notification"));

            reportMenu.add(createMenuItem("Generate Bill"));

            menuBar.add(infoMenu);
            menuBar.add(userMenu);
            menuBar.add(reportMenu);
        }

        utilityMenu.add(createMenuItem("Notepad"));
        utilityMenu.add(createMenuItem("Calculator"));
        utilityMenu.add(createMenuItem("Backup Database"));
        utilityMenu.add(createMenuItem("Restore Database"));
        menuBar.add(utilityMenu);

        logoutMenu.add(createMenuItem("Logout"));
        menuBar.add(logoutMenu);

        setJMenuBar(menuBar);

        // Content Panel placeholder
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JMenu createMenu(String title, Color color) {
        JMenu menu = new JMenu(title);
        menu.setForeground(color);
        menu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return menu;
    }

    private JMenuItem createMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        item.setBackground(Color.WHITE);
        item.addActionListener(this);
        return item;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        try {
            switch (cmd) {
                case "New Customer" -> new NewCustomer();
                case "Customer Details" -> new CustomerDetails();
                case "Water Bill Details" -> new WaterBillDetails();
                case "Calculate Bill" -> new CalculateBill();
                case "SendNotification" -> new SendNotification();
                case "Update Information" -> new UpdateInformation(meter);
                case "View Information" -> new ViewInformation(meter);
                case "Pay Bill" -> new PayBill(meter);
                case "Bill Details" -> new BillDetails(meter);
                case "Generate Bill" -> new GenerateBill(meter);
                case "Notification" -> new Notification(meter);
                case "Notepad" -> openUtility("notepad.exe");
                case "Calculator" -> openUtility("calc.exe");
                case "Backup Database" -> backupDatabase();
                case "Restore Database" -> restoreDatabase();
                case "Logout" -> {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Are you sure you want to logout?", "Logout Confirmation",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        dispose();
                        new Login();
                    }
                }
                default -> JOptionPane.showMessageDialog(this, "Invalid action: " + cmd);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while processing: " + cmd);
        }
    }

    private void openUtility(String command) {
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to open utility: " + command);
        }
    }

    private void backupDatabase() {
       try {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Database Backup");
        fileChooser.setSelectedFile(new File("waterbill_backup_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".sql"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File backupFile = fileChooser.getSelectedFile();

            // Ensure file has .sql extension
            if (!backupFile.getName().toLowerCase().endsWith(".sql")) {
                backupFile = new File(backupFile.getAbsolutePath() + ".sql");
            }

            String command = "cmd /c mysqldump -u root waterbill -r \"" + backupFile.getAbsolutePath() + "\"";

            Process process = Runtime.getRuntime().exec(command);
            int result = process.waitFor();

            if (result == 0) {
                JOptionPane.showMessageDialog(this, "✅ Backup successful!\nSaved at: " + backupFile.getAbsolutePath());
            } else {
                JOptionPane.showMessageDialog(this, "❌ Backup failed. Check MySQL configuration.");
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage());
    }
}

    private void restoreDatabase() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String filePath = file.getAbsolutePath();

                // Note: To restore, you may need to run the command with shell input redirection.
                String[] restoreCommand = {"cmd.exe", "/c", "mysql -u root water_billing < \"" + filePath + "\""};

                Process process = Runtime.getRuntime().exec(restoreCommand);
                int result = process.waitFor();

                if (result == 0) {
                    JOptionPane.showMessageDialog(this, "✅ Restore successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Restore failed. Check MySQL configuration.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Project("Admin", ""));
    }
}
