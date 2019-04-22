/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MessagePourPlusEnseig.java
 *
 * Created on 10 mai 2012, 21:05:36
 */
package sipee;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.smslib.CMessage;
import org.smslib.COutgoingMessage;
import org.smslib.CService;
import org.smslib.handler.CATHandler;

/**
 *
 * @author admine
 */
public class MessagePourPlusEnseig extends javax.swing.JInternalFrame {

    /** Creates new form MessagePourPlusEnseig */
    static CService srv;
    static CATHandler atHandler;
    DefaultComboBoxModel comboDestination = new DefaultComboBoxModel();
    DefaultComboBoxModel comboBailleur = new DefaultComboBoxModel();
    DefaultComboBoxModel comboAnnee = new DefaultComboBoxModel();
    DefaultComboBoxModel comboDefault = new DefaultComboBoxModel();
    static DefaultTableModel mode = new DefaultTableModel();
    static DefaultTableModel mode1 = new DefaultTableModel();
    static DefaultTableModel mode2 = new DefaultTableModel();
    static DefaultTableColumnModel dtcm = new DefaultTableColumnModel();
    static DefaultTableColumnModel dtcm1 = new DefaultTableColumnModel();
    static DefaultTableColumnModel dtcm2 = new DefaultTableColumnModel();

    public MessagePourPlusEnseig() {
        Connector1.initialise();
        initComponents();
        paramTable1();
        paramTable3();
        paramTable();
        jComboBox1.setBackground(Color.WHITE);
        jComboBox2.setBackground(Color.WHITE);
        jComboBox3.setBackground(Color.WHITE);
        jComboBox4.setBackground(Color.WHITE);
        jComboBox5.setBackground(Color.WHITE);
        jComboBox6.setBackground(Color.WHITE);
        //this.setSize(MainFrame.jDesktopPane1.getWidth() - 20, MainFrame.jDesktopPane1.getHeight() - 10);
        selectionExpediteur();
        effaceTable(jTable4, mode);
        selectionContactsEnseignant();
        selectionMsgPredefini();
        effaceTable(jTable5, mode1);
        selectionHistoriqueMsg();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    /////*************QUITTER UNE BOITE DE DIALOGUE*******************************
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void quitterBoiteDeDialogue() {
        // TODO add your handling code here:
        int ret = JOptionPane.showConfirmDialog(null, "VOULEZ-VOUS VRAIMENT QUITTER ?",
                "CONFIRMATION", JOptionPane.YES_NO_OPTION);
        if (ret == JOptionPane.YES_OPTION) {
            try {
                Connector1.closeConnection();
                dispose();
            } catch (Exception ex) {
                System.out.println("ERREUR : database.close()" + ex);

            }

        } else {
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            this.setVisible(true);
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    /////*************LIBERER LES ZONES DE TEXTE*******************************
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void annuler() {
        jTextArea3.setText(null);
        jTextField1.setText(null);
        jTextField4.setText(null);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //****************CONNEXION AU MODEM*******************************************************
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void connexion(String port, String marque, String version) {
        srv = new CService(port, 9600, marque, version);
        System.out.println("SendMessage(): application d'envoie de sms par GSM.");
        System.out.println("  API utilisé: " + CService._name + " version " + CService._version);
        //configuration du code pin
        try {
            System.out.println("configuration du code PIN de la carte SIM....");
            srv.setSimPin("0000");
            // srv.setSimPin2("0000");
            System.out.println("");
            System.out.println("etablissement de la connexion.....");
            srv.connect();
            System.out.println(" CONNEEXION ETABLIE AVEC SUCCES");
            System.out.println("connexion etablie avec le modem GSM(Nokia 3110)");

        } catch (Exception e) {
            System.out.println("!!!!!!!!!!!   echec de la connexion   !!!!!!!!!");
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //************ENVOI MESSAGE A UN ENSEMBLE D'ENSEIGNANT SELECTIONNE********************************************************/////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void envoiMessageEnseig() {
        try {
            String Expediteur = jComboBox2.getSelectedItem() + "";//
            String objet_message = jTextField4.getText().trim();
            String message = jTextArea3.getText().trim();
            String jour = jComboBox3.getSelectedItem() + "";//
            String mois = jComboBox4.getSelectedItem() + "";//
            String annee = jComboBox5.getSelectedItem() + "";//
            String heure = jComboBox6.getSelectedItem() + "";//
            String lieu = jTextField1.getText().trim();
            LinkedList hb = new LinkedList();


            String sms = "EXP: " + Expediteur + ".OBJ: " + objet_message + ".MSG:" + message + " le " + jour + "/" + mois + "/" + annee + " à " + heure + " précise. Lieu: " + lieu;
            System.out.println("" + sms);

            for (int i = 0; i < jTable3.getRowCount(); i++) {
                //String cont = jTable3.getValueAt(i, 1) + "";
                String cont1 = jTable3.getValueAt(i, 0) + "";
                String cont = "+237".concat(cont1);
                System.out.println(cont);
                System.out.println("tel_ens " + cont + "position " + i);
                COutgoingMessage msg = new COutgoingMessage(cont.trim(), sms);
                //msg.setMessageEncoding(CMessage.MessageEncoding.Enc7Bit);
                msg.setMessageEncoding(CMessage.MessageEncoding.EncUcs2);
                msg.setStatusReport(true);
                msg.setValidityPeriod(8);
                System.out.println("le systeme se prepare à envoyer un message aux numeros suivants: ");
                // System.out.println(" numero parent1==========> " + cont);
                srv.setSmscNumber("+23779000002");
                System.out.println("veuillez patienter....");
                hb.add(msg);
                System.out.println("hb.add(msg)  " + hb);
                srv.sendMessage(hb);
                insertionHistoriqueMsg();
                effaceTable(jTable5, mode1);
                selectionHistoriqueMsg();
            }
            JOptionPane.showMessageDialog(null, "MESSAGE ENVOI AVEC SUCCES");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ECHEC D'ENVOI DU MESSAGE\n VEUILLEZ CONNECTER LE MODEM GSM");
            e.printStackTrace();
        }
        //System.exit(0);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //**************DECONNEXION DU MODEM****************************************************////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    public void deconnexion() throws Exception {
        srv.disconnect();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /***************METHODE QUI PERMET DE PARAMETRER LA TABLE DES ADRESSES DES ENSEIGNANTS********************************************/
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void paramTable() {
        jTable4.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        mode = (DefaultTableModel) jTable4.getModel();
        dtcm = (DefaultTableColumnModel) jTable4.getColumnModel();
        jTable4.setRowHeight(40);
        largeurColoneMax(dtcm, 0, 60);
        //largeurColoneMax(dtcm, 1, 200);
        // largeurColoneMax(dtcm, 2, 90);
        largeurColoneMax(dtcm, 3, 90);
        //largeurColoneMax(dtcm, 4, 150);
        // largeurColoneMax(dtcm, 5, 150);
        //largeurColoneMax(dtcm, 6, 90);

        TableCellRenderer tbcProjet = getTableCellRenderer();
        TableCellRenderer tbcProjet2 = getTableHeaderRenderer();
        for (int i = 0; i < jTable4.getColumnCount(); i++) {
            TableColumn tc = jTable4.getColumnModel().getColumn(i);
            tc.setCellRenderer(tbcProjet);
            tc.setHeaderRenderer(tbcProjet2);

        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /***********************************METHODE QUI PERMET DE PARAMETRER LA TABLE DES HISTORIQUES********************************************/
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void paramTable1() {
        jTable5.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        mode1 = (DefaultTableModel) jTable5.getModel();
        dtcm1 = (DefaultTableColumnModel) jTable5.getColumnModel();

        jTable5.setRowHeight(40);
        //largeurColoneMax(dtcm, 0, 50);
        //largeurColoneMax(dtcm, 3, 50);
        //largeurColoneMax(dtcm, 4, 100);
        // largeurColoneMax(dtcm, 6, 100);

        TableCellRenderer tbcProjet = getTableCellRenderer();
        TableCellRenderer tbcProjet2 = getTableHeaderRenderer();
        for (int i = 0; i < jTable5.getColumnCount(); i++) {
            TableColumn tc = jTable5.getColumnModel().getColumn(i);
            tc.setCellRenderer(tbcProjet);
            tc.setHeaderRenderer(tbcProjet2);

        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /***************METHODE QUI PERMET DE PARAMETRER LA TABLE DES ADRESSES DES ENSEIGNANTS********************************************/
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void paramTable3() {
        jTable3.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        mode2 = (DefaultTableModel) jTable3.getModel();
        dtcm2 = (DefaultTableColumnModel) jTable3.getColumnModel();
        jTable3.setRowHeight(30);
        //  largeurColoneMax(dtcm2, 0, 20);
        //largeurColoneMax(dtcm, 1, 200);
        //largeurColoneMax(dtcm, 2, 250);
        // largeurColoneMax(dtcm, 3, 150);
        //largeurColoneMax(dtcm, 4, 150);
        // largeurColoneMax(dtcm, 5, 150);
        //largeurColoneMax(dtcm, 6, 90);

        TableCellRenderer tbcProjet = getTableCellRenderer();
        TableCellRenderer tbcProjet2 = getTableHeaderRenderer();
        for (int i = 0; i < jTable3.getColumnCount(); i++) {
            TableColumn tc = jTable3.getColumnModel().getColumn(i);
            tc.setCellRenderer(tbcProjet);
            tc.setHeaderRenderer(tbcProjet2);

        }

    }
    //********************************************************************************************************
    private static TableCellRenderer label = null;

    public static TableCellRenderer getTableCellRenderer() {
        if (label == null) {
            label = new TableGameRenderer();
        }
        return label;
    }

    /********************************************************/
    static class TableGameRenderer extends JTextArea implements TableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setForeground(new Color(0, 0, 0));
            setOpaque(true);
            //table.getColumn(0).setWidth(5);
            setFont(new java.awt.Font("Times New Roman", 1, 12));

            int reste = row / 2;
            if ((2 * reste) < row) {
                setBackground(new Color(255, 255, 255));
            } else {
                setBackground(new Color(240, 240, 240));
            }
            if (isSelected) {
                int nb = 0;
                if (nb <= column) {
                    setBackground(new Color(255, 255, 102));
                    setForeground(Color.BLACK);
                } else {
                    setBackground(new Color(255, 255, 102));
                    setForeground(Color.BLUE);
                }
            } else {
                //setBackground(Color.WHITE);
                // setForeground(Color.BLACK);
            }
            //if ((column == 0)) {
            //    setBackground(Color.WHITE);
            //    setForeground(Color.BLUE);
            // enregistrerdonnees2Bis(row);
            // }
            if (column == 1) {
                String val = table.getValueAt(row, 1) + "";
                setToolTipText(val);
            } else {
                setToolTipText("" + value);
            }
            setLineWrap(true);
            setWrapStyleWord(true);
            setText("" + value);

            return this;

        }
    }

    //***************************************** ENTETE DU JTABLE************************************************µ*/
    public static TableCellRenderer getTableHeaderRenderer() {
        return new TableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                //                    JLabel lbl = new JLabel();
                table.setRowSelectionAllowed(true);
                // table.setAutoResizeMode(table.AUTO_RESIZE_OFF);
                JTextArea lbl = new JTextArea() {
                    // methode a redefinir  pour modifier l aspect de la cellule

                    public void paint(Graphics g) {
                        super.paint(g);

                    }
                };

                //                    lbl.setHorizontalAlignment( JLabel.CENTER );
                lbl.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 0)));
                Font font = table.getFont();
                //lbl.setOpaque( true );
                font = new Font(font.getName(), Font.BOLD, font.getSize() + 1);
                lbl.setMinimumSize(new Dimension(75, 30));
                lbl.setPreferredSize(new Dimension(75, 40));
                lbl.setFont(font);
                lbl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

                lbl.setBackground(new Color(255, 255, 204));
                lbl.setLineWrap(true);
                lbl.setWrapStyleWord(true);
                lbl.setText((String) value);

                return lbl;
            }
        };
    }

    /************************************METHODE QUI PERMET DE DETERMINER LA LARGEUR MAX D'une COLONE****************************************/
    public static void largeurColoneMax(DefaultTableColumnModel dtcm, int numColumn, int largeur) {
        dtcm.getColumn(numColumn).setMinWidth(largeur);
        dtcm.getColumn(numColumn).setMaxWidth(largeur);
        dtcm.getColumn(numColumn).setPreferredWidth(largeur);
    }

    /*********************  METHODE POUR EFFACER LA TABLE ****************/
    public static void effaceTable(JTable table, DefaultTableModel mode) {
        while (table.getRowCount() > 0) {
            mode.removeRow(table.getRowCount() - 1);
        }
    }

    /*******************************METHODE QUI PERMET DE RETOURNER LE DERNIER ENREGISTREMENT****************************/
    public static int retourneMaxNumber() {
        int tmp = 0;
        try {
            String requete = "SELECT Max(eleve.id_el) AS Maxeleve FROM eleve";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                tmp = rs.getInt(1);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
        return tmp;
    }

    //**************AFFICHAGE CONTACTS DE L'ENSEIGNANT DANS LA ZONES DE TEXTE  DES CONTACTS*****************µ
    public void selectionContactsEnseignant() {
        try {
            String requete = "SELECT enseignant.id_ens,  enseignant.nom_ens, enseignant.prenom_ens, enseignant.tel_ens FROM enseignant";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                int id_ens = rs.getInt(1);
                String nom_ens = rs.getString(2);
                String prenom_ens = rs.getString(3);
                String tel_ens = rs.getString(4);
                String[] val = {"" + id_ens, nom_ens, prenom_ens, tel_ens};
                mode.addRow(val);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }

    }

    //**************FILTRAGE DES CONTACTS*************************************µ*****************µ
    public void filtrerContactsEnseignants() {
        try {
            String saisie = "%" + jTextField6.getText() + "%";
            saisie.toLowerCase();
            String requete = "SELECT  enseignant.id_ens, enseignant.nom_ens, enseignant.prenom_ens, enseignant.tel_ens FROM enseignant "
                    + "WHERE (((enseignant.nom_ens) Like '" + saisie.replace("'", "''") + "') OR ((enseignant.prenom_ens) Like '" + saisie.replace("'", "''") + "')) ORDER BY enseignant.nom_ens";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                int id_ens = rs.getInt(1);
                String nom_ens = rs.getString(2);
                String prenom_ens = rs.getString(3);
                String tel_ens = rs.getString(4);
                String[] val = {"" + id_ens, nom_ens, prenom_ens, tel_ens};
                mode.addRow(val);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }

    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//******************** EXPEDITEURS DES MESSAGES MIS DANS LE jComboBox2:PROVISEUR,CENSEUR,SG,ET AUTRES *************************************************
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void selectionExpediteur() {
        try {
            jComboBox2.insertItemAt("Proviseur", 0);
            String requete = "SELECT DISTINCT admin.responsabilite FROM admin "
                    + "WHERE responsabilite IN ('Censeur','Censeur 1','Censeur 2','Censeur 3','Censeur 4','Censeur 5','Intendant','Conseillé d''Orientation','Surveillant Générale ','Surveillant Generale 1','Surveillant Generale 2','Surveillant Generale 3','Surveillant Generale 4','Surveillant Generale 5' )";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            int i = 1;
            while (rs.next()) {
                String responsabilite = rs.getString(1);
                jComboBox2.insertItemAt(responsabilite, i);
                i++;
            }
        } catch (Exception e) {
            System.out.println("le serveur n'est pas lancer");
            //JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //******************** INSERTION EXPEDITEURS DE MESSAGE AFIN DE FAIRE LA MISE A JOUR DES ENSEIGNANTS*************************************************
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    public void insertionHistoriqueMsg() {
        try {
            String exp = jComboBox2.getSelectedItem() + "";
            String sujet = jTextField4.getText().trim().replace("'", "''");

            String jour = jComboBox3.getSelectedItem() + "";
            String mois = jComboBox4.getSelectedItem() + "";
            String annee = jComboBox5.getSelectedItem() + "";
            String heure = jComboBox6.getSelectedItem() + "";
            String lieu = jTextField1.getText().trim().replace("'", "''");
            String cont_msg = jTextArea3.getText().replace("'", "''");
            String contenu_message = "" + cont_msg + "" + jour + "" + mois + "" + annee + "" + heure + "" + lieu;

            for (int i = 0; i < jTable3.getRowCount(); i++) {
                String tel_contacte = jTable3.getValueAt(i, 2) + "";
                //String cont1 = jTable3.getValueAt(i, 2) + "";
                System.out.println("tel_ens " + tel_contacte);
                Connector1.statement.executeUpdate("INSERT INTO HISTORIQUE_ENVOI_ENS(exp ,sujet,tel_contacte,contenu_message) values ('" + exp + "','" + sujet + "','" + tel_contacte + "','" + contenu_message + "')");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }

    }

    //***********SELECTION EXPEDITEUR MESSAGES AFIN DE TRACER L'HISTORIQUES DES ENVOIS******************************************************************
    public void selectionHistoriqueMsg() {
        try {
            String requete = "SELECT historique_envoi_ens.id_histo_ens, historique_envoi_ens.exp , historique_envoi_ens.date_heure, historique_envoi_ens.sujet, historique_envoi_ens.tel_contacte FROM historique_envoi_ens";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                String id_histo_ens = rs.getString(1);
                String exp = rs.getString(2);
                String date_heure = rs.getString(3);
                String sujet = rs.getString(4);
                String tel_contacte = rs.getString(5);
                String[] val = {exp, date_heure, sujet, tel_contacte};
                mode1.addRow(val);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }

    }

    //*************SELECTION MESSAGE PREDEFINI ET METTRE DANS LE jComboBox1 ***************************************
    public void selectionMsgPredefini() {
        try {
            String requete = "SELECT message.id_msg, message.type_msg, message.obj_msg, message.cont_msg FROM message";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            int i = 0;
            while (rs.next()) {
                String id_msg = rs.getString(1);
                String type_msg = rs.getString(2);
                String obj_msg = rs.getString(3);
                String cont_msg = rs.getString(4);
                jComboBox1.insertItemAt(type_msg, i);
                i++;
            }
            jComboBox1.insertItemAt("Nouveau message", i++);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }

    }

    //*************SELECTION ITEM MESSAGE PREDEFINI ET METTRE DANS String[]ET COMPARER AVEC CEUX DANS jComboBox1 ***************************************
    public void selectionItemMsgPredefini() {
        try {

            String requete = "SELECT message.id_msg, message.type_msg, message.obj_msg, message.cont_msg FROM message ";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                String id_msg = rs.getString(1);
                String type_msg = rs.getString(2);
                String obj_msg = rs.getString(3);
                String cont_msg = rs.getString(4);

                String[] selections = {id_msg, type_msg, obj_msg, cont_msg, "Nouveau message"};
                if (jComboBox1.getSelectedItem().equals(type_msg)) {
                    jTextField4.setEditable(false);
                    jTextArea3.setEditable(false);
                    jTextField4.setText(obj_msg);
                    jTextArea3.setText(cont_msg);
                } else if (jComboBox1.getSelectedItem().equals("Nouveau message")) {
                    jTextField4.setEditable(true);
                    jTextArea3.setEditable(true);
                    jTextField4.setText(" ");
                    jTextArea3.setText(" ");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }

    }

    //***************************SELECTION DES CONTACTS DE L'ENSEIGNANT PAR UN SIMPLE CLIC DANS LA LIGNE CORRESPONDANTE DU JTABLE*************************/
    public void selectionContacts() {
        try {
            boolean isSelected = false;
            int numero = Integer.parseInt(jTable4.getValueAt(jTable4.getSelectedRow(), 0) + "");
            String requete = "select  enseignant.id_ens,enseignant.nom_ens, enseignant.tel_ens,enseignant.prenom_ens FROM enseignant WHERE id_ens=" + numero + " ";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                int id_ens = rs.getInt(1);
                String nom_ens = rs.getString(2);
                String tel_ens = rs.getString(3);
                String prenom_ens = rs.getString(4);
               // System.out.println("id_ens " + id_ens + "  tel_ens: " + tel_ens);
                String[] val = {tel_ens, nom_ens + " " + prenom_ens};
                mode2.addRow(val);

                jTable3.getRowCount();
                jTable3.getColumnCount();
                // System.out.println("linge " + jTable3.getRowCount());
                //System.out.println("colonne " + jTable3.getColumnCount());
                for (int i = 0; i < jTable3.getRowCount(); i++) {
                    String cont = jTable3.getValueAt(i, 0) + "";
                    String nom_prenom = jTable3.getValueAt(i, 1) + "";
                    System.out.println("tel_ens " + cont + " nom enseig " + nom_prenom);

                }

            }

            //dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }

    //***************************SUPPRESSION DES DOUBLONS*************************/
    public void suppressionDoublons() {
        try {
            int[] selected = jTable3.getSelectedRows();

            if (selected.length == 0) {
                JOptionPane.showMessageDialog(null, "VEUILLEZ SELECIONNER UN ENREGISTREMENT SVP!", "Selection !", JOptionPane.INFORMATION_MESSAGE);
            } else {
                mode2.removeRow(jTable3.getSelectedRow());
                jTable3.getRowCount();
                jTable3.getColumnCount();
                //System.out.println("linge " + jTable3.getRowCount());
                //  System.out.println("colonne " + jTable3.getColumnCount());
                for (int i = 0; i < jTable3.getRowCount(); i++) {
                    String cont = jTable3.getValueAt(i, 0) + "";
                    String nom_prenom = jTable3.getValueAt(i, 1) + "";
                    //String prenoms_ens = jTable3.getValueAt(i, 0) + "";
                    System.out.println("tel_ens " + cont + " nom_prenom_enseig " + nom_prenom);

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }

    public void listedesContacts() {
    }

    //***************************SELECTION DU CONTACT DE L'ENSEIGNANT *************************/
   /* public void selectionContactsPret() {
    try {
    int[] selected = jTable4.getSelectedRows();
    if (selected.length == 0) {
    JOptionPane.showMessageDialog(null, "VEUILLEZ SELECIONNER UN ENSEIGNEMENT SVP!", "Selection !", JOptionPane.INFORMATION_MESSAGE);
    } else {
    int numero = Integer.parseInt(jTable4.getValueAt(jTable4.getSelectedRow(), 0) + "");
    String requete = "select enseignant.id_ens, enseignant.nom_ens, enseignant.prenom_ens, enseignant.tel_ens FROM enseignant WHERE id_ens=" + numero + "";
    ResultSet rs = Connector1.statement.executeQuery(requete);
    while (rs.next()) {
    int id_ens = rs.getInt(1);
    String nom_ens = rs.getString(2);
    String prenoms_ens = rs.getString(3);
    String tel_ens = rs.getString(4);
    // System.out.println("id_ens: " + id_ens + "  tel_ens: " + tel_ens+" nom_ens  "+nom_ens+" "+prenoms_ens);
    
    }
    
    //dispose();
    }
    } catch (HeadlessException | NumberFormatException | SQLException e) {
    JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
    }
    }*/
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jTextField4 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jComboBox3 = new javax.swing.JComboBox();
        jComboBox4 = new javax.swing.JComboBox();
        jComboBox5 = new javax.swing.JComboBox();
        jComboBox6 = new javax.swing.JComboBox();
        jTextField1 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jToggleButton1 = new javax.swing.JToggleButton();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jTextField7 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jTextField6 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        setClosable(true);
        setTitle("Envoyer les sms à plusieurs enseignants");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "SMS POUR PLUSIEURS ENSEIGNANTS", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 24))); // NOI18N

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel6.setText("Expediteur");
        jPanel6.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 93, -1));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel7.setText("Message prédefini");
        jPanel6.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Message à envoyé", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        jTextArea3.setColumns(20);
        jTextArea3.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jTextArea3.setRows(5);
        jTextArea3.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true)));
        jScrollPane3.setViewportView(jTextArea3);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
        );

        jPanel6.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 370, 130));

        jComboBox1.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jComboBox1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel6.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, 240, 35));

        jComboBox2.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jComboBox2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel6.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 150, 240, 33));

        jTextField4.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jTextField4.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        jPanel6.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 230, 240, 35));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel8.setText("Objet du message");
        jPanel6.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 112, -1));

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Date Heure & Lieu", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        jComboBox3.setBackground(new java.awt.Color(255, 153, 51));
        jComboBox3.setFont(new java.awt.Font("Times New Roman", 1, 11));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Jour", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        jComboBox3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jComboBox4.setBackground(new java.awt.Color(255, 153, 51));
        jComboBox4.setFont(new java.awt.Font("Times New Roman", 1, 11));
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mois", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        jComboBox4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jComboBox5.setBackground(new java.awt.Color(255, 153, 51));
        jComboBox5.setFont(new java.awt.Font("Times New Roman", 1, 11));
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Année", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50" }));
        jComboBox5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jComboBox6.setFont(new java.awt.Font("Times New Roman", 1, 11));
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Heure", "07h00", "07h30", "08h00", "08h30", "09h00", "09h30", "10h00", "10h30", "11h00", "11h30", "12h00", "12h30", "13h00", "13h30", "14h00", "14h30", "15h00", "15h30", "16h00", "16h30", " " }));
        jComboBox6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTextField1.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jTextField1.setText("lieu");
        jTextField1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox6, 0, 68, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel6.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 370, 70));

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel6.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 340, -1));

        jTable3.setBackground(new java.awt.Color(255, 255, 204));
        jTable3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Contacts Enseig", "Noms  Enseig"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable3.setOpaque(false);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable3MousePressed(evt);
            }
        });
        jTable3.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jTable3MouseDragged(evt);
            }
        });
        jScrollPane4.setViewportView(jTable3);

        jPanel6.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 240, 120));

        jToggleButton1.setBackground(new java.awt.Color(255, 255, 153));
        jToggleButton1.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jToggleButton1.setText("Suppr doublons");
        jToggleButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jPanel6.add(jToggleButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, 110, 40));

        jButton1.setBackground(new java.awt.Color(255, 255, 153));
        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jButton1.setText("Envoyer");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, 120, 40));

        jButton4.setBackground(new java.awt.Color(255, 255, 153));
        jButton4.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jButton4.setText("Quitter");
        jButton4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 480, 110, 40));

        jButton2.setBackground(new java.awt.Color(255, 255, 153));
        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jButton2.setText("Annuler");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 480, 100, 40));

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "ADRESSES DES ENSEIGNANTS", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable5.setBackground(new java.awt.Color(153, 153, 153));
        jTable5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable5.setFont(new java.awt.Font("Tahoma", 1, 12));
        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N°", "Noms Enseignant", "Prénoms Enseignant", "Contact Enseignant"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable5.setOpaque(false);
        jTable5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable5MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable5MousePressed(evt);
            }
        });
        jTable5.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jTable5MouseDragged(evt);
            }
        });
        jScrollPane8.setViewportView(jTable5);

        jPanel14.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 520, 160));

        jTextField7.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jTextField7.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField7KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField7KeyTyped(evt);
            }
        });
        jPanel14.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(268, 27, 210, 35));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel11.setText("Entrez le nom de l'enseignant recherché");
        jPanel14.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 36, -1, -1));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "ADRESSES DES ENSEIGNANTS", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable4.setBackground(new java.awt.Color(153, 153, 153));
        jTable4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable4.setFont(new java.awt.Font("Tahoma", 1, 12));
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N°", "Noms Enseignant", "Prénoms Enseignant", "Contact Enseignant"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable4.setOpaque(false);
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable4MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable4MousePressed(evt);
            }
        });
        jTable4.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jTable4MouseDragged(evt);
            }
        });
        jScrollPane6.setViewportView(jTable4);

        jPanel11.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 520, 200));

        jTextField6.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jTextField6.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField6KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField6KeyTyped(evt);
            }
        });
        jPanel11.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(268, 27, 210, 35));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel10.setText("Entrez le nom de l'enseignant recherché");
        jPanel11.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 36, -1, -1));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        selectionItemMsgPredefini();
}//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            connexion("COM7", "NOKIA", "3110c");
            envoiMessageEnseig();
        } catch (Exception e) {
            e.printStackTrace();
        }
}//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        //quitterBoiteDeDialogue();
        dispose();
}//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        annuler();
}//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jTextField4ActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:
}//GEN-LAST:event_jTable3MouseClicked

    private void jTable3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MousePressed
        // TODO add your handling code here:
}//GEN-LAST:event_jTable3MousePressed

    private void jTable3MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseDragged
        // TODO add your handling code here:
}//GEN-LAST:event_jTable3MouseDragged

    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked
        // TODO add your handling code here:
        //selectionContacts();
}//GEN-LAST:event_jTable4MouseClicked

    private void jTable4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MousePressed
        // TODO add your handling code here:
        selectionContacts();
}//GEN-LAST:event_jTable4MousePressed

    private void jTable4MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseDragged
        // TODO add your handling code here:
        //selectionContacts();
}//GEN-LAST:event_jTable4MouseDragged

    private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
        // TODO add your handling code here:
        effaceTable(jTable4, mode);
        filtrerContactsEnseignants();
}//GEN-LAST:event_jTextField6KeyReleased

    private void jTextField6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyTyped
        // TODO add your handling code here:
        //effaceTable(jTable2, mode);
        //selectionDestinataire0();
}//GEN-LAST:event_jTextField6KeyTyped

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
        suppressionDoublons();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable5MouseClicked

    private void jTable5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable5MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable5MousePressed

    private void jTable5MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable5MouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable5MouseDragged

    private void jTextField7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7KeyReleased

    private void jTextField7KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7KeyTyped
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    public static javax.swing.JTable jTable3;
    public static javax.swing.JTable jTable4;
    public static javax.swing.JTable jTable5;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
}
