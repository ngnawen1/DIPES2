
package sipee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class Connector {

    private static String user = "root";
    private static String password = "";
    public static String driver = "com.mysql.jdbc.Driver";
    private static String url = "";
    public static Statement st;
    public static Connection con;

    public Connector() {
    }

    public static void initialise() {
        BufferedReader IN = null;
        try {
            String chemin00 = System.getProperty("user.dir");
            System.out.println("chemein" + chemin00);
            IN = new BufferedReader(new FileReader(chemin00 + "\\Connector.txt"));
            String bd = IN.readLine();
            System.out.println("db" + bd);
            url = "jdbc:mysql://localhost:3306/schoolbd";
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            System.out.println("connection effectuée avecs succes");
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "\n"
                    + " IMPOSSIBLE DE SE CONNECTER A LA SOURCE \n"
                    + " SI L'ERREUR PERSISTE, VEUILLEZ CONTACTER VOTRE ADMINISTRATEUR DE BASE DE DONNEES");
        }
    }

    public static void main(String[] args) {
        initialise();

    }
}
