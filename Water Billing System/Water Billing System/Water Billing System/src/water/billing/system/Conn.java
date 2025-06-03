package water.billing.system;

import java.sql.*;

public class Conn {

    Connection c;
    Statement s;

    public Conn() {
        try {
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/water_billing", "root", "");
            s = c.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
