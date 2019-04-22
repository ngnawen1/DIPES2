/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ConnectionAuSysteme.java
 *
 * Created on 30 mai 2012, 12:02:31
 */
package sipee;

import java.awt.Color;
import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author admine
 */
public class ConnectionAuSysteme extends javax.swing.JInternalFrame {

    /** Creates new form ConnectionAuSysteme */
    public ConnectionAuSysteme() {
        initComponents();
        Connector1.initialise();
        selectionModeDeConnexion();
        jComboBox1.setBackground(Color.WHITE);
        jPanel1.setBackground(Color.WHITE);
        jPanel2.setBackground(Color.WHITE);
        jPanel3.setBackground(Color.WHITE);
        jTextField4.setBackground(Color.WHITE);
        jPasswordField1.setBackground(Color.WHITE);
        //verifierRespoAadmin();
    }

    /*******************************METHODE QUI PERMET DE RETOURNER LE DERNIER ENREGISTREMENT***********************************************/
    public static int retourneMaxNumber() {
        int tmp = 0;
        try {
            String requete = "SELECT Max(admin.id_admin) AS Maxadmin FROM admin";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                tmp = rs.getInt(1);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
        return tmp;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//******************** SELECTION DES REPONSSABLE POUVANT SE CONNECTER AU SYSTEME *************************************************
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void selectionModeDeConnexion() {
        try {
            jComboBox1.insertItemAt("Proviseur", 0);
            String requete = "SELECT DISTINCT admin.responsabilite FROM admin "
                    + "WHERE responsabilite IN ('Censeur','Censeur 1','Censeur 2','Censeur 3','Censeur 4','Censeur 5','Intendant','Comptable matière','Conseillé d''Orientation','Surveillant Générale ','Surveillant Generale 1','Surveillant Generale 2','Surveillant Generale 3','Surveillant Generale 4','Surveillant Generale 5' )";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            int i = 1;
            while (rs.next()) {
                String responsabilite = rs.getString(1);
                jComboBox1.insertItemAt(responsabilite, i);
                i++;
            }
        } catch (Exception e) {
            System.out.println("le serveur n'est pas lancer");
            //JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//******************** SELECTION DES REPONSSABLE POUVANT SE CONNECTER AU SYSTEME *************************************************
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void affichargeDuLogin() {
        try {
            String respo = jComboBox1.getSelectedItem() + "";
            if (respo.equals("Proviseur")) {
                jTextField4.setEditable(false);
                String requete = "SELECT userroot.superlogin FROM userroot";
                ResultSet rs = Connector1.statement.executeQuery(requete);
                while (rs.next()) {
                    String superlogin = rs.getString(1);
                    jTextField4.setText(superlogin);


                }
            } else {
                String requete = "SELECT admin.login_admin FROM admin where admin.responsabilite='" + respo.replace("'", "''") + "'";
                ResultSet rs = Connector1.statement.executeQuery(requete);
                while (rs.next()) {
                    String login = rs.getString(1);
                    jTextField4.setText(login);


                }
            }


        } catch (Exception e) {
            System.out.println("le serveur n'est pas lancer");
            //JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//******************** METHODE PERMETANT DE DE VERIFIER LES REPONSABLE DE LA TABLE ADMIN *************************************************
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /*  public String verifierRespoAadmin() {
    String respo = null;
    String[] val = new String[retourneMaxNumber()];
    int i = 0;
    try {
    
    String requete = "SELECT DISTINCT admin.responsabilite FROM admin";
    ResultSet rs = Connector1.statement.executeQuery(requete);
    while (rs.next()) {
    respo = rs.getString(1);
    val[i] = respo;
    //System.out.println("val[i]=======" + val[i]);
    i++;
    
    }
    System.out.println("val[i]=======" + val[i]);
    return val[i];
    } catch (Exception e) {
    JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
    return null;
    }
    
    }*/
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///**********METHODE PERMETTANT DE SE CONNECTER AU SYSTEME*****************
    /////////////////////////////////////////////////////////////////////////////////////////////////
    public void paramatresDeConnexion() {
        try {
            //String respobd=verifierRespoAadmin();
            String respo = jComboBox1.getSelectedItem() + "";
            String login_admin = jTextField4.getText();
            String pwd_admin = String.copyValueOf(jPasswordField1.getPassword());
            switch (respo) {
                case "Proviseur": {
                    String requete = "SELECT userroot.superlogin, userroot.superpwd FROM userroot";
                    ResultSet rs = Connector1.statement.executeQuery(requete);
                    while (rs.next()) {
                        String superlogin = rs.getString(1);
                        String superpwd_bd = rs.getString(2);
                        jTextField4.setText(superlogin);
                        System.out.println(" login du super user:  " + login_admin + "    pwd du super user:  " + pwd_admin);
                        if ((pwd_admin.equals(superpwd_bd)) && (login_admin.equals(superlogin))) {

                            MainFrame.activerMenu();
                            MainFrame.desactiverConnexion();
                            MainFrame.actif2Menu();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "MOT DE PASSE INCORRECT \n");
                            jPasswordField1.setText("");
                        }
                    }
                    break;
                }
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                case "Censeur": {
                    Connector1.statement.executeUpdate("UPDATE ADMIN set  date_conexion =default  WHERE admin.responsabilite='Censeur'");
                    String requete = "SELECT admin.login_admin, admin.pwd_admin,admin.responsabilite FROM admin where admin.responsabilite='Censeur'";
                    ResultSet rs = Connector1.statement.executeQuery(requete);
                    while (rs.next()) {
                        String login_ad = rs.getString(1);
                        String pwd_ad = rs.getString(2);
                        System.out.println(" login_ad_bd " + login_admin + " pwd_ad_bd " + pwd_admin);
                        if ((pwd_admin.equals(pwd_ad))) {

                            MainFrame.activerMenu();
                            MainFrame.desactiverConnexion();
                            MainFrame.inactif2Menu();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "MOT DE PASSE INCORRECT \n");
                            jPasswordField1.setText("");
                        }
                    }
                    break;
                }
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                case "Censeur 1": {
                    Connector1.statement.executeUpdate("UPDATE ADMIN set  date_conexion =default  WHERE admin.responsabilite='Censeur 1'");
                    String requete = "SELECT admin.login_admin, admin.pwd_admin,admin.responsabilite FROM admin where admin.responsabilite='Censeur 1'";
                    ResultSet rs = Connector1.statement.executeQuery(requete);
                    while (rs.next()) {
                        String login_ad = rs.getString(1);
                        String pwd_ad = rs.getString(2);
                        if ((pwd_admin.equals(pwd_ad))) {
                            MainFrame.activerMenu();
                            MainFrame.desactiverConnexion();
                            MainFrame.inactif2Menu();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "MOT DE PASSE INCORRECT \n");
                            jPasswordField1.setText("");
                        }
                    }


                    break;
                }
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
                case "Censeur 2": {
                    Connector1.statement.executeUpdate("UPDATE ADMIN set  date_conexion =default  WHERE admin.responsabilite='Censeur 2'");
                    String requete = "SELECT admin.login_admin, admin.pwd_admin,admin.responsabilite FROM admin where admin.responsabilite='Censeur 2'";
                    ResultSet rs = Connector1.statement.executeQuery(requete);
                    while (rs.next()) {
                        String login_ad = rs.getString(1);
                        String pwd_ad = rs.getString(2);
                        System.out.println(" login_ad_bd " + login_admin + " pwd_ad_bd " + pwd_admin);
                        if ((pwd_admin.equals(pwd_ad))) {

                            MainFrame.activerMenu();
                            MainFrame.desactiverConnexion();
                            MainFrame.inactif2Menu();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "MOT DE PASSE INCORRECT \n");
                            jPasswordField1.setText("");
                        }
                    }
                    break;
                }
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                case "Censeur 3": {
                    Connector1.statement.executeUpdate("UPDATE ADMIN set  date_conexion =default  WHERE admin.responsabilite='Censeur 3'");
                    String requete = "SELECT admin.login_admin, admin.pwd_admin,admin.responsabilite FROM admin where admin.responsabilite='Censeur 3'";
                    ResultSet rs = Connector1.statement.executeQuery(requete);
                    while (rs.next()) {
                        String login_ad = rs.getString(1);
                        String pwd_ad = rs.getString(2);
                        System.out.println(" login_ad_bd " + login_admin + " pwd_ad_bd " + pwd_admin);
                        if ((pwd_admin.equals(pwd_ad))) {

                            MainFrame.activerMenu();
                            MainFrame.desactiverConnexion();
                            MainFrame.inactif2Menu();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "MOT DE PASSE INCORRECT \n");
                            jPasswordField1.setText("");
                        }
                    }
                    break;
                }
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////     
                case "Censeur 4": {
                    Connector1.statement.executeUpdate("UPDATE ADMIN set  date_conexion =default  WHERE admin.responsabilite='Censeur 4'");
                    String requete = "SELECT admin.login_admin, admin.pwd_admin,admin.responsabilite FROM admin where admin.responsabilite='Censeur 4'";
                    ResultSet rs = Connector1.statement.executeQuery(requete);
                    while (rs.next()) {
                        String login_ad = rs.getString(1);
                        String pwd_ad = rs.getString(2);
                        System.out.println(" login_ad_bd " + login_admin + " pwd_ad_bd " + pwd_admin);
                        if ((pwd_admin.equals(pwd_ad))) {

                            MainFrame.activerMenu();
                            MainFrame.desactiverConnexion();
                            MainFrame.inactif2Menu();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "MOT DE PASSE INCORRECT \n");
                            jPasswordField1.setText("");
                        }
                    }
                    break;
                }
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////     
                case "Secretaire": {
                    Connector1.statement.executeUpdate("UPDATE ADMIN set  date_conexion =default  WHERE admin.responsabilite='Secretaire'");
                    String requete = "SELECT admin.login_admin, admin.pwd_admin,admin.responsabilite FROM admin where admin.responsabilite='Secretaire'";
                    ResultSet rs = Connector1.statement.executeQuery(requete);
                    while (rs.next()) {
                        String login_ad = rs.getString(1);
                        String pwd_ad = rs.getString(2);
                        System.out.println(" login_ad_bd " + login_admin + " pwd_ad_bd " + pwd_admin);
                        if ((pwd_admin.equals(pwd_ad))) {

                            MainFrame.activerMenu();
                            MainFrame.desactiverConnexion();
                            MainFrame.inactif2Menu();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "MOT DE PASSE INCORRECT \n");
                            jPasswordField1.setText("");
                        }
                    }
                    break;
                }
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
                case "Conseillé d'Orientation": {
                    Connector1.statement.executeUpdate("UPDATE ADMIN set  date_conexion =default  WHERE admin.responsabilite='Conseillé d''Orientation'");
                    String requete = "SELECT admin.login_admin, admin.pwd_admin,admin.responsabilite FROM admin where admin.responsabilite='Conseillé d''Orientation'";
                    ResultSet rs = Connector1.statement.executeQuery(requete);
                    while (rs.next()) {
                        String login_ad = rs.getString(1);
                        String pwd_ad = rs.getString(2);
                        System.out.println(" login_ad_bd " + login_admin + " pwd_ad_bd " + pwd_admin);
                        if ((pwd_admin.equals(pwd_ad))) {

                            MainFrame.activerMenu();
                            MainFrame.desactiverConnexion();
                            MainFrame.inactif2Menu();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "MOT DE PASSE INCORRECT \n");
                            jPasswordField1.setText("");
                        }
                    }
                    break;
                }
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
                case "Intendant": {
                    Connector1.statement.executeUpdate("UPDATE ADMIN set  date_conexion =default  WHERE admin.responsabilite='Intendant'");
                    String requete = "SELECT admin.login_admin, admin.pwd_admin,admin.responsabilite FROM admin where admin.responsabilite='Intendant'";
                    ResultSet rs = Connector1.statement.executeQuery(requete);
                    while (rs.next()) {
                        String login_ad = rs.getString(1);
                        String pwd_ad = rs.getString(2);
                        System.out.println(" login_ad_bd " + login_admin + " pwd_ad_bd " + pwd_admin);
                        if ((pwd_admin.equals(pwd_ad))) {

                            MainFrame.activerMenu();
                            MainFrame.desactiverConnexion();
                            MainFrame.inactif2Menu();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "MOT DE PASSE INCORRECT \n");
                            jPasswordField1.setText("");
                        }
                    }
                    break;
                }
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   
                case "Comptable matière": {
                    Connector1.statement.executeUpdate("UPDATE ADMIN set  date_conexion =default  WHERE admin.responsabilite='Comptable matière'");
                    String requete = "SELECT admin.login_admin, admin.pwd_admin,admin.responsabilite FROM admin where admin.responsabilite='Comptable matière'";
                    ResultSet rs = Connector1.statement.executeQuery(requete);
                    while (rs.next()) {
                        String login_ad = rs.getString(1);
                        String pwd_ad = rs.getString(2);
                        System.out.println(" login_ad_bd " + login_admin + " pwd_ad_bd " + pwd_admin);
                        if ((pwd_admin.equals(pwd_ad))) {

                            MainFrame.activerMenu();
                            MainFrame.desactiverConnexion();
                            MainFrame.inactif2Menu();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "MOT DE PASSE INCORRECT \n");
                            jPasswordField1.setText("");
                        }
                    }
                    break;
                }
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
                case "Surveillant générale": {
                    Connector1.statement.executeUpdate("UPDATE ADMIN set  date_conexion =default  WHERE admin.responsabilite='Surveillant générale'");
                    String requete = "SELECT admin.login_admin, admin.pwd_admin,admin.responsabilite FROM admin where admin.responsabilite='Surveillant générale'";
                    ResultSet rs = Connector1.statement.executeQuery(requete);
                    while (rs.next()) {
                        String login_ad = rs.getString(1);
                        String pwd_ad = rs.getString(2);
                        System.out.println(" login_ad_bd " + login_admin + " pwd_ad_bd " + pwd_admin);
                        if ((pwd_admin.equals(pwd_ad))) {

                            MainFrame.activerMenu();
                            MainFrame.desactiverConnexion();
                            MainFrame.inactif2Menu();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "MOT DE PASSE INCORRECT \n");
                            jPasswordField1.setText("");
                        }
                    }
                    break;
                }
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
                case "Surveillant générale 1": {
                    Connector1.statement.executeUpdate("UPDATE ADMIN set  date_conexion =default  WHERE admin.responsabilite='Surveillant générale 1'");
                    String requete = "SELECT admin.login_admin, admin.pwd_admin,admin.responsabilite FROM admin where admin.responsabilite='Surveillant générale 1'";
                    ResultSet rs = Connector1.statement.executeQuery(requete);
                    while (rs.next()) {
                        String login_ad = rs.getString(1);
                        String pwd_ad = rs.getString(2);
                        System.out.println(" login_ad_bd " + login_admin + " pwd_ad_bd " + pwd_admin);
                        if ((pwd_admin.equals(pwd_ad))) {

                            MainFrame.activerMenu();
                            MainFrame.desactiverConnexion();
                            MainFrame.inactif2Menu();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "MOT DE PASSE INCORRECT \n");
                            jPasswordField1.setText("");
                        }
                    }
                    break;
                }
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
                case "Surveillant générale 2": {
                    Connector1.statement.executeUpdate("UPDATE ADMIN set  date_conexion =default  WHERE admin.responsabilite='Surveillant générale 2'");
                    String requete = "SELECT admin.login_admin, admin.pwd_admin,admin.responsabilite FROM admin where admin.responsabilite='Surveillant générale 2'";
                    ResultSet rs = Connector1.statement.executeQuery(requete);
                    while (rs.next()) {
                        String login_ad = rs.getString(1);
                        String pwd_ad = rs.getString(2);
                        System.out.println(" login_ad_bd " + login_admin + " pwd_ad_bd " + pwd_admin);
                        if ((pwd_admin.equals(pwd_ad))) {

                            MainFrame.activerMenu();
                            MainFrame.desactiverConnexion();
                            MainFrame.inactif2Menu();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "MOT DE PASSE INCORRECT \n");
                            jPasswordField1.setText("");
                        }
                    }
                    break;
                }
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
                case "Surveillant générale 3": {
                    Connector1.statement.executeUpdate("UPDATE ADMIN set  date_conexion =default  WHERE admin.responsabilite='Surveillant générale 3'");
                    String requete = "SELECT admin.login_admin, admin.pwd_admin,admin.responsabilite FROM admin where admin.responsabilite='Surveillant générale 3'";
                    ResultSet rs = Connector1.statement.executeQuery(requete);
                    while (rs.next()) {
                        String login_ad = rs.getString(1);
                        String pwd_ad = rs.getString(2);
                        System.out.println(" login_ad_bd " + login_admin + " pwd_ad_bd " + pwd_admin);
                        if ((pwd_admin.equals(pwd_ad))) {

                            MainFrame.activerMenu();
                            MainFrame.desactiverConnexion();
                            MainFrame.inactif2Menu();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "MOT DE PASSE INCORRECT \n");
                            jPasswordField1.setText("");
                        }
                    }
                    break;
                }
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
                case "Surveillant générale 4": {
                    Connector1.statement.executeUpdate("UPDATE ADMIN set  date_conexion =default  WHERE admin.responsabilite='Surveillant générale 4'");
                    String requete = "SELECT admin.login_admin, admin.pwd_admin,admin.responsabilite FROM admin where admin.responsabilite='Surveillant générale 4'";
                    ResultSet rs = Connector1.statement.executeQuery(requete);
                    while (rs.next()) {
                        String login_ad = rs.getString(1);
                        String pwd_ad = rs.getString(2);
                        System.out.println(" login_ad_bd " + login_admin + " pwd_ad_bd " + pwd_admin);
                        if ((pwd_admin.equals(pwd_ad))) {

                            MainFrame.activerMenu();
                            MainFrame.desactiverConnexion();
                            MainFrame.inactif2Menu();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "MOT DE PASSE INCORRECT \n");
                        }
                    }
                    break;
                }
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
                default:
                    JOptionPane.showMessageDialog(null, "CHOISIR UN MODE DE CONNEXION ");
                    break;
            }



        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }
    /////////////////////////////////////////////////////////////////////////

    public void initChamps() {
        jPasswordField1.setText(null);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jPanel3 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(232, 233, 224));
        setBorder(javax.swing.BorderFactory.createMatteBorder(5, 5, 5, 5, new java.awt.Color(255, 255, 255)));
        setClosable(true);
        setTitle("CONNXION AU SYSTEME");

        jPanel2.setBackground(new java.awt.Color(232, 233, 224));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(5, 5, 5, 5, new java.awt.Color(0, 0, 0)));
        jPanel2.setOpaque(false);

        jPanel1.setBackground(new java.awt.Color(232, 233, 224));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "ENTREZ VOTRE MOT DE PASSE", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        jPanel1.setOpaque(false);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel4.setText("LOGIN :");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 13));
        jLabel5.setText("MOT DE PASSE :");

        jTextField4.setFont(new java.awt.Font("Tahoma", 1, 14));
        jTextField4.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));

        jPasswordField1.setFont(new java.awt.Font("Tahoma", 1, 14));
        jPasswordField1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "CHOISIR UN MODE DE CONNEXION", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        jPanel3.setOpaque(false);

        jComboBox1.setBackground(new java.awt.Color(255, 255, 204));
        jComboBox1.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jComboBox1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jComboBox1.setFocusable(false);
        jComboBox1.setLightWeightPopupEnabled(false);
        jComboBox1.setOpaque(false);
        jComboBox1.setRequestFocusEnabled(false);
        jComboBox1.setVerifyInputWhenFocusTarget(false);
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jToggleButton1.setBackground(new java.awt.Color(255, 255, 153));
        jToggleButton1.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jToggleButton1.setText("Valider");
        jToggleButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToggleButton1.setFocusCycleRoot(true);
        jToggleButton1.setFocusTraversalPolicy(null);
        jToggleButton1.setFocusTraversalPolicyProvider(true);
        jToggleButton1.setFocusable(false);
        jToggleButton1.setRequestFocusEnabled(false);
        jToggleButton1.setRolloverEnabled(false);
        jToggleButton1.setVerifyInputWhenFocusTarget(false);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jToggleButton2.setBackground(new java.awt.Color(255, 255, 153));
        jToggleButton2.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jToggleButton2.setText("Annuler");
        jToggleButton2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToggleButton2.setFocusCycleRoot(true);
        jToggleButton2.setFocusTraversalPolicy(null);
        jToggleButton2.setFocusTraversalPolicyProvider(true);
        jToggleButton2.setFocusable(false);
        jToggleButton2.setRequestFocusEnabled(false);
        jToggleButton2.setRolloverEnabled(false);
        jToggleButton2.setVerifyInputWhenFocusTarget(false);
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        jToggleButton3.setBackground(new java.awt.Color(255, 255, 153));
        jToggleButton3.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jToggleButton3.setText("Quitter");
        jToggleButton3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToggleButton3.setFocusCycleRoot(true);
        jToggleButton3.setFocusTraversalPolicy(null);
        jToggleButton3.setFocusTraversalPolicyProvider(true);
        jToggleButton3.setFocusable(false);
        jToggleButton3.setRequestFocusEnabled(false);
        jToggleButton3.setRolloverEnabled(false);
        jToggleButton3.setVerifyInputWhenFocusTarget(false);
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(232, 233, 224));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/imCon.png"))); // NOI18N
        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jToggleButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        affichargeDuLogin();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
        paramatresDeConnexion();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        // TODO add your handling code here:
        initChamps();
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jToggleButton3ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    // End of variables declaration//GEN-END:variables
}
