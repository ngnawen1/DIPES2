package sipee;

import java.awt.Color;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

public class Connector1 {

    public static String driver = "com.mysql.jdbc.Driver";
    private static String path = "jdbc:mysql://localhost:3306/sipebd";
    public static String user = "root";
    public static String password = "";
    public static Connection connection;
    public static Statement statement;

    public Connector1() {
    }

    public static void initialise() {
        JLabel alerte = new JLabel("ATTENTION: LE SERVEUR N'EST PAS LANCE");
        alerte.setForeground(Color.RED);
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            //JOptionPane.showMessageDialog(null, "ATTENTION, SERVEUR NON LANCE",  "Alerte", JOptionPane.ERROR_MESSAGE);
            //JOptionPane.showMessageDialog(null, "ClassNotFoundException:  " + e.getMessage());
        }
        try {
            connection = DriverManager.getConnection(path, user, password);
            statement = connection.createStatement();
            System.out.println("connection effectuée avec succes");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    new Object[]{alerte, "Faites le et lancer l'application de nouveau"},
                    "Alerte", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            // ConnectionAuSysteme connectionAuSysteme = new ConnectionAuSysteme();
            //connectionAuSysteme.dispose();
            //JOptionPane.showMessageDialog(null, " SERVEUR ETEINT ");
            //JOptionPane.showMessageDialog(null, "SQLException: " + e.getMessage() + " cause :" + e.getCause());
        }


    }

    public static void closeConnection() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Connector1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        initialise();
    }
}
