/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MessagePourUnEnseig.java
 *
 * Created on 10 mai 2012, 20:24:10
 */
package sipee;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.sql.ResultSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
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
public class MessagePourUnEnseig extends javax.swing.JInternalFrame {

    /** Creates new form MessagePourUnEnseig */
    static CService srv;
    static CATHandler atHandler;
    Connector1 co = new Connector1();
    public static JDialog jdClient;
    DefaultComboBoxModel comboDestination = new DefaultComboBoxModel();
    DefaultComboBoxModel comboBailleur = new DefaultComboBoxModel();
    DefaultComboBoxModel comboAnnee = new DefaultComboBoxModel();
    DefaultComboBoxModel comboDefault = new DefaultComboBoxModel();
    static DefaultTableModel mode1 = new DefaultTableModel();
    static DefaultTableModel mode = new DefaultTableModel();
    static DefaultTableColumnModel dtcm = new DefaultTableColumnModel();
    static DefaultTableColumnModel dtcm1 = new DefaultTableColumnModel();

    public MessagePourUnEnseig() {
        Connector1.initialise();
        initComponents();
        paramTable1();
        paramTable();
        jComboBox1.setBackground(Color.WHITE);
        jComboBox2.setBackground(Color.WHITE);
        jComboBox3.setBackground(Color.WHITE);
        jComboBox4.setBackground(Color.WHITE);
        jComboBox5.setBackground(Color.WHITE);
        jComboBox6.setBackground(Color.WHITE);
        //this.setVisible(true);
       // this.setSize(MainFrame.jDesktopPane1.getWidth() - 20, MainFrame.jDesktopPane1.getHeight() - 20);
        //changerimages();
        effaceTable(jTable2, mode);
        selectionContactsEnseignant();
        //filtrerContactsEnseignants();
        selectionExpediteur();
        selectionMsgPredefini();
        effaceTable(jTable1, mode1);
        selectionHistoriqueMsg();
    }

    public void annuler() {
        jTextArea1.setText(null);
        jTextField1.setText(null);
        jTextField2.setText(null);
        jTextField4.setText(null);
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
            //srv.setSimPin2("0000");
            System.out.println("");
            System.out.println("etablissement de la connexion.....");
            srv.connect();
            System.out.println(" CONNEEXION ETABLIE AVEC SUCCES");
            System.out.println("connexion etablie avec le modem GSM(Nokia 3110)");
            //srv.setSmscNumber("+23779000002");//srv.setSmscNumber("");
            // System.out.println("*********Mobile Device Information: ********");
            //  System.out.println("	Manufacturer  : " + srv.getDeviceInfo().getManufacturer());
            //  System.out.println("	Model         : " + srv.getDeviceInfo().getModel());
            //  System.out.println("	Serial No     : " + srv.getDeviceInfo().getSerialNo());
            //  System.out.println("	IMSI          : " + srv.getDeviceInfo().getImsi());
            //  System.out.println("	S/W Version   : " + srv.getDeviceInfo().getSwVersion());
            //  System.out.println("	Battery Level : " + srv.getDeviceInfo().getBatteryLevel() + "%");
            //  System.out.println("	Signal Level  : " + srv.getDeviceInfo().getSignalLevel() + "%");
            //  System.out.println("");

        } catch (Exception e) {
            System.out.println("!!!!!!!!!!!   echec de connexion   !!!!!!!!!");
            e.printStackTrace();
        }
    }

    //************ENVOI MESSAGE1 ********************************************************/////////////
    public void envoiMessage() {

        try {
            String Expediteur = jComboBox2.getSelectedItem() + "";//
            String objet_message = jTextField4.getText().trim();
            String message = jTextArea1.getText().trim();
            String jour = jComboBox3.getSelectedItem() + "";//
            String mois = jComboBox4.getSelectedItem() + "";//
            String annee = jComboBox5.getSelectedItem() + "";//
            String heure = jComboBox6.getSelectedItem() + "";//
            String lieu = jTextField1.getText().trim();
            String sms = "EXP:" + Expediteur + ".OBJ:" + objet_message + ".MSG:" + message + " le " + jour + "/" + mois + "/" + annee + " à " + heure + " précise. Lieu: " + lieu;
            //String sms = "OBJET: " + objet_message + " MSG:" + message + " le " + jour + "//" + mois + "//" + annee + " à " + heure + " précise. Lieu: " + lieu;

            System.out.println("  "+sms );
            // System.out.println("objet_message: " + objet_message);
            ///System.out.println("" + sms);System.out.println("message : " + sms);
            //message = "Message du modem gsm NOKIA 3110 de la part de Belmondo";
            COutgoingMessage msg = new COutgoingMessage(jTextField2.getText().trim(), sms);
            //msg.setMessageEncoding(CMessage.MessageEncoding.Enc7Bit);
            msg.setMessageEncoding(CMessage.MessageEncoding.EncUcs2);
            // msg.setStatusReport(true);
            //msg.setValidityPeriod(8);
            System.out.println("le systeme se prepare à envoyer un message au numero " + jTextField2.getText().trim());
            srv.setSmscNumber("+23779000002");
            System.out.println("veuillez patienter....");
            System.out.println("  "+sms );
           // connexion("COM7", "NOKIA", "3110c");
            srv.sendMessage(msg);
            insertionHistoriqueMsg();
            JOptionPane.showMessageDialog(null, "MESSAGE ENVOYE AVEC SUCCES");
            effaceTable(jTable1, mode1);
            selectionHistoriqueMsg();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ECHEC D'ENVOI DU MESSAGE");
        }
        //System.exit(0);
    }
    //**************DECONNEXION DU MODEM****************************************************////////////////

    public void deconnexion() throws Exception {
        srv.disconnect();
    }

    /***************METHODE QUI PERMET DE PARAMETRER LA TABLE DES ADRESSES********************************************/
    public void paramTable() {
        jTable2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        mode = (DefaultTableModel) jTable2.getModel();
        dtcm = (DefaultTableColumnModel) jTable2.getColumnModel();
        jTable2.setRowHeight(40);
        largeurColoneMax(dtcm, 0, 80);
        largeurColoneMax(dtcm, 1, 200);
        //largeurColoneMax(dtcm, 2, 250);
        //   largeurColoneMax(dtcm, 3, 150);
        // largeurColoneMax(dtcm, 4, 150);
        // largeurColoneMax(dtcm, 5, 150);
        //largeurColoneMax(dtcm, 6, 90);

        TableCellRenderer tbcProjet = getTableCellRenderer();
        TableCellRenderer tbcProjet2 = getTableHeaderRenderer();
        for (int i = 0; i < jTable2.getColumnCount(); i++) {
            TableColumn tc = jTable2.getColumnModel().getColumn(i);
            tc.setCellRenderer(tbcProjet);
            tc.setHeaderRenderer(tbcProjet2);

        }

    }

    /***********************************METHODE QUI PERMET DE PARAMETRER LA TABLE DES HISTORIQUES********************************************/
    public void paramTable1() {
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        mode1 = (DefaultTableModel) jTable1.getModel();
        dtcm1 = (DefaultTableColumnModel) jTable1.getColumnModel();

        jTable1.setRowHeight(40);
        //largeurColoneMax(dtcm, 0, 50);
        //largeurColoneMax(dtcm, 3, 50);
        //largeurColoneMax(dtcm, 4, 100);
        // largeurColoneMax(dtcm, 6, 100);

        TableCellRenderer tbcProjet = getTableCellRenderer();
        TableCellRenderer tbcProjet2 = getTableHeaderRenderer();
        for (int i = 0; i < jTable1.getColumnCount(); i++) {
            TableColumn tc = jTable1.getColumnModel().getColumn(i);
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
                    setBackground(new Color(255,255,102));
                    setForeground(Color.BLACK);
                } else {
                    setBackground(new Color(255,255,102));
                    setForeground(Color.BLUE);
                }
            } else {
                //setBackground(Color.WHITE);
                // setForeground(Color.BLACK);
            }
            if ((column == 0)) {
                setBackground(Color.WHITE);
                setForeground(Color.BLUE);
                // enregistrerdonnees2Bis(row);
            }
            if (column == 1) {
                String val = table.getValueAt(row, 2) + "";
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
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*******************************METHODE QUI PERMET DE RETOURNER LE DERNIER ENREGISTREMENT****************************/
    ////////////////////////////////////////////////////////////////////////////////////////////////
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
            String requete = "SELECT  DISTINCT enseignant.id_ens,  enseignant.nom_ens, enseignant.prenom_ens, enseignant.tel_ens FROM enseignant";
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
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //**************FILTRAGE DES CONTACTS******************************************************
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void filtrerContactsEnseignants() {
        try {
            String saisie = "%" + jTextField5.getText() + "%";
            saisie.toLowerCase();
            String requete = "SELECT enseignant.id_ens, enseignant.nom_ens, enseignant.prenom_ens, enseignant.tel_ens FROM enseignant "
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //**************METHODE PERMETTANT DE RECUPERER LE CHEMIN D4ACCES A LA PHOTO************************************************
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String recuperationPhotoEns() {
        String img = null;
        try {
            int numero = Integer.parseInt(jTable2.getValueAt(jTable2.getSelectedRow(), 0) + "");
            String requete = "select id_ens,img from imagesens where id_ens=" + numero + "";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                int id_ens = rs.getInt(1);
                img = rs.getString(2);
                System.out.println("img==========  " + img);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
        System.out.println("img==========  " + img);
        return img;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //**************AFFICHAGE DU CONTACT DES PARENTS APRES SELECTION APRES UN SIMPLE CLIC DANS LA LIGNE CORRESPONDANTE DU JTABLE2************************************************
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void affichagePhotoEns() {

        try {
            String cheminPhoto = recuperationPhotoEns();
            System.out.println("cheminPhoto==== "+cheminPhoto);
            if (cheminPhoto == null) {
                //jLabel8.setIcon(new javax.swing.ImageIcon("/Photos/avatar.jpg"));
                jLabel8.setText(" AUCUNE PHOTO");
            } else {
                jLabel8.setIcon(new javax.swing.ImageIcon(cheminPhoto));

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //**************AFFICHAGE DU CONTACT DES PARENTS APRES SELECTION APRES UN SIMPLE CLIC DANS LA LIGNE CORRESPONDANTE DU JTABLE2************************************************
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void affichagePhotoEns1() {
        try {
            jLabel8.setIcon(new javax.swing.ImageIcon("/Photos/avatar.jpg"));
            int numero = Integer.parseInt(jTable2.getValueAt(jTable2.getSelectedRow(), 0) + "");
            System.out.println("" + numero);
            String requete = "select id_ens,img from imagesens where id_ens=" + numero + "";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                int id_ens = rs.getInt(1);
                String img = rs.getString(2);
                jLabel8.setIcon(new javax.swing.ImageIcon(img));
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
            String requete = "SELECT DISTINCT enseignant.responsabilite FROM enseignant "
                    + "WHERE responsabilite IN ('Proviseur','Censeur','Censeur 1','Censeur 2','Censeur 3','Censeur 4','Censeur 5','Secretaire','Intendant','Conseillé d''Orientation','Surveillant Générale ','Surveillant Generale 1','Surveillant Generale 2','Surveillant Generale 3','Surveillant Generale 4','Surveillant Generale 5' )";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            int i = 0;
            while (rs.next()) {
                String responsabilite = rs.getString(1);
                jComboBox2.insertItemAt(responsabilite, i);
                i++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }

    }

    //******************** INSERTION EXPEDITEURS DE MESSAGE AFIN DE FAIRE LA MISE A JOUR*************************************************
    public void insertionHistoriqueMsg() {
        try {
            String exp = jComboBox2.getSelectedItem() + "";
            String sujet = jTextField4.getText().trim().replace("'", "''");
            String tel_contacte = jTextField2.getText().trim().replace("'", "''");

            String jour = jComboBox3.getSelectedItem() + "";
            String mois = jComboBox4.getSelectedItem() + "";
            String annee = jComboBox5.getSelectedItem() + "";
            String heure = jComboBox6.getSelectedItem() + "";
            String lieu = jTextField1.getText().trim().replace("'", "''");
            String cont_msg = jTextArea1.getText().replace("'", "''");
            //String contenu_message = "" + cont_msg + "" + jour + "" + mois + "" + annee + "" + heure + "" + lieu;
            String contenu_message = "" +cont_msg + " le " + jour + "/" + mois + "/" + annee + " à " + heure + " précise. Lieu: " + lieu;
            System.out.println("  " + cont_msg);
            Connector1.statement.executeUpdate("INSERT INTO HISTORIQUE_ENVOI_ENS(exp ,sujet,tel_contacte,contenu_message) values ('" + exp.replace("'", "''") + "','" + sujet + "','" + tel_contacte + "','" + contenu_message + "')");
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
                    jTextArea1.setEditable(false);
                    jTextField4.setText(obj_msg);
                    jTextArea1.setText(cont_msg);
                } else if (jComboBox1.getSelectedItem().equals("Nouveau message")) {
                    jTextField4.setEditable(true);
                    jTextArea1.setEditable(true);
                    jTextField4.setText(" ");
                    jTextArea1.setText(" ");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }

    }

    //***************************SELECTION DU CONTACT DE L'ENSEIGNANT POUR METTRE DANS LA ZONE DE TEXTE*************************/
    public void selectionContacts() {
        try {
            int[] selected = jTable2.getSelectedRows();
            if (selected.length == 0) {
                JOptionPane.showMessageDialog(null, "VEUILLEZ SELECIONNER UN ENSEIGNEMENT SVP!", "Selection !", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int numero = Integer.parseInt(jTable2.getValueAt(jTable2.getSelectedRow(), 0) + "");
                String requete = "select enseignant.id_ens, enseignant.nom_ens, enseignant.prenom_ens, enseignant.tel_ens FROM enseignant WHERE id_ens=" + numero + "";
                ResultSet rs = Connector1.statement.executeQuery(requete);
                while (rs.next()) {
                    int id_ens = rs.getInt(1);
                    String noms_ens = rs.getString(2);
                    String prenoms_ens = rs.getString(3);
                    String tel_ens = rs.getString(4);
                    System.out.println("id_class: " + id_ens + " nom_class: " + tel_ens);
                    jTextField2.setText(tel_ens);
                    jTextField3.setText(noms_ens+" "+prenoms_ens);
                }

                //dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel9 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox();
        jTextField4 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jComboBox3 = new javax.swing.JComboBox();
        jComboBox4 = new javax.swing.JComboBox();
        jComboBox5 = new javax.swing.JComboBox();
        jComboBox6 = new javax.swing.JComboBox();
        jTextField1 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jTextField5 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        setClosable(true);
        setTitle("Envoyer les sms à un seul enseignant");

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "SMS POUR UN  ENSEIGNANT", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 30))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel2.setText("Expediteur");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel3.setText("Message prédefini");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Message à envoyé", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jTextArea1.setRows(5);
        jTextArea1.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true)));
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jComboBox1.setBackground(new java.awt.Color(255, 255, 153));
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

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Contact de l'enseignant ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        jPanel3.setFont(new java.awt.Font("Times New Roman", 1, 14));

        jTextField2.setEditable(false);
        jTextField2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jComboBox2.setBackground(new java.awt.Color(255, 255, 153));
        jComboBox2.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jComboBox2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTextField4.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jTextField4.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel5.setText("Objet du message");

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Date Heure & Lieu", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        jComboBox3.setBackground(new java.awt.Color(255, 255, 153));
        jComboBox3.setFont(new java.awt.Font("Times New Roman", 1, 12));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Jour", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        jComboBox3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jComboBox4.setBackground(new java.awt.Color(255, 255, 153));
        jComboBox4.setFont(new java.awt.Font("Times New Roman", 1, 12));
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mois", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        jComboBox4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jComboBox5.setBackground(new java.awt.Color(255, 255, 153));
        jComboBox5.setFont(new java.awt.Font("Times New Roman", 1, 12));
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Année", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50" }));
        jComboBox5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jComboBox6.setBackground(new java.awt.Color(255, 255, 153));
        jComboBox6.setFont(new java.awt.Font("Times New Roman", 1, 12));
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
                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jButton1.setBackground(new java.awt.Color(255, 255, 153));
        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jButton1.setText("Envoyer");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 255, 153));
        jButton4.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jButton4.setText("Quitter");
        jButton4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 255, 153));
        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jButton2.setText("Annuler");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel8.setText("           PHOTO");
        jLabel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nom de l'enseignant ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        jPanel4.setFont(new java.awt.Font("Times New Roman", 1, 14));

        jTextField3.setEditable(false);
        jTextField3.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(jLabel3))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox1, 0, 251, Short.MAX_VALUE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "ADRESSES DES ENSEIGNANTS", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N

        jTable2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable2.setFont(new java.awt.Font("Tahoma", 1, 12));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable2.setOpaque(false);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable2MousePressed(evt);
            }
        });
        jTable2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jTable2MouseDragged(evt);
            }
        });
        jScrollPane3.setViewportView(jTable2);

        jTextField5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jTextField5.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField5KeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel6.setText("Entrez le nom de l'enseignant recherché");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "HISTORIQUE DES ENVOIS", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N

        jTable1.setBackground(new java.awt.Color(153, 153, 153));
        jTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Expediteur", "Date & Heure  d'envoi", "Objet du message", "Enseignant contacté"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setOpaque(false);
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:
}//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        // TODO add your handling code here:
        //effaceTable(jTable2, mode);
        //selectionContactsParents();
}//GEN-LAST:event_jTextField2KeyTyped

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jTextField4ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
        //selectionContacts();
}//GEN-LAST:event_jTable2MouseClicked

    private void jTable2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MousePressed
        // TODO add your handling code here:
        selectionContacts();
        affichagePhotoEns();
}//GEN-LAST:event_jTable2MousePressed

    private void jTable2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseDragged
        // TODO add your handling code here:
        //selectionContacts();
}//GEN-LAST:event_jTable2MouseDragged

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        // TODO add your handling code here:
        effaceTable(jTable2, mode);
        filtrerContactsEnseignants();
}//GEN-LAST:event_jTextField5KeyReleased

    private void jTextField5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyTyped
        // TODO add your handling code here:
        //effaceTable(jTable2, mode);
        //selectionDestinataire0();
}//GEN-LAST:event_jTextField5KeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         try {
                connexion("COM7", "NOKIA", "3110c");
                envoiMessage();
            } catch (Exception e) {
            }
}//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        //quitterBoiteDeDialogue();
        dispose();
}//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        // dispose();
}//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3KeyTyped

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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    public static javax.swing.JTable jTable1;
    public static javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
