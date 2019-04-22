/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MessagePourUnParent.java
 *
 * Created on 10 mai 2012, 16:24:13
 */
package sipee;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.sql.ResultSet;
import java.sql.SQLException;
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
 * @author Vermon Ngnawen
 * @version 1.1
 */
public class MessagePourUnParent extends javax.swing.JInternalFrame {

    /** Creates new form MessagePourUnParent */
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

    public MessagePourUnParent() {
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
        //this.setSize(MainFrame.jDesktopPane1.getWidth() - 20, MainFrame.jDesktopPane1.getHeight() - 10);
        //changerimages();
        effaceTable(jTable2, mode);
        selectionElevesParents();
        selectionExpediteur();
        selectionMsgPredefini();
        effaceTable(jTable1, mode1);
        selectionHistoriqueMsg();
        // changerimages();

    }

    /////*************QUITTER UNE BOITE DE DIALOGUE*******************************
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

    /**
     * Methode connexion: elle paermet de se connecter au modem GSM
     * @param port represent le numero de port de connection au modem GSM
     * @param marque represente la marque du modem GSM
     * @param version represente la version du modem GSM
     * @throws 
     */
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
        } catch (Exception e) {
        }
        // etablissement de la connexion avec le modem
        try {
            srv.connect();
            System.out.println(" CONNEEXION ETABLIE AVEC SUCCES");

        } catch (Exception e) {
            System.out.println("!!!!!!!!!!!   echec de la connexion   !!!!!!!!!");
        }
    }

    /**
     * Methode envoiMessage permet d'envoyer le message après connexion sur le bon 
     * @param 
     * @return 
     * @throws 
     */
    public void envoiMessage() {

        try {
            String objet_message = jTextField4.getText().trim();
            String message = jTextArea1.getText().trim();
            String Expediteur = jComboBox2.getSelectedItem() + "";//
            String jour = jComboBox3.getSelectedItem() + "";//
            String mois = jComboBox4.getSelectedItem() + "";//
            String annee = jComboBox5.getSelectedItem() + "";//
            String heure = jComboBox6.getSelectedItem() + "";//
            String lieu = jTextField1.getText().trim();
            String sms = "EXP:" + Expediteur + ".OBJ:" + objet_message + ".MSG:" + message + " le " + jour + "/" + mois + "/" + annee + " à " + heure + " précise. Lieu: " + lieu;

            ///System.out.println("  "+sms );
            // System.out.println("objet_message: " + objet_message);
            System.out.println(" " + sms);
            //message = "Message du modem gsm NOKIA 3110 de la part de Belmondo";
            COutgoingMessage msg = new COutgoingMessage(jTextField3.getText().trim(), sms);
            //msg.setMessageEncoding(CMessage.MessageEncoding.Enc7Bit);
            msg.setMessageEncoding(CMessage.MessageEncoding.EncUcs2);
            msg.setStatusReport(true);
            msg.setValidityPeriod(8);
            System.out.println("le systeme se prepare à envoyer un message au numero " + jTextField3.getText().trim());
            srv.setSmscNumber("+23779000002");
            System.out.println("veuillez patienter....");
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
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /***************METHODE QUI PERMET DE PARAMETRER LA TABLE DES ADRESSES********************************************/
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void paramTable() {
        jTable2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        mode = (DefaultTableModel) jTable2.getModel();
        dtcm = (DefaultTableColumnModel) jTable2.getColumnModel();
        jTable2.setRowHeight(40);
        largeurColoneMax(dtcm, 0, 80);
        largeurColoneMax(dtcm, 1, 200);

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
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*******************************METHODE QUI PERMET DE RETOURNER LE DERNIER ENREGISTREMENT****************************/
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
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

    /************SCHEMA DU SYSTEME *******************************************/
    public void changerimages() {
        try {
            int matricule = Integer.parseInt(jTextField5.getText());
            System.out.println("" + matricule);

            ResultSet rs = Connector1.statement.executeQuery("select mtle,img from images where mtle=" + matricule + "");
            while (rs.next()) {
                int mat = rs.getInt(1);
                String img = rs.getString(2);
                System.out.println("" + img);
                jLabel8.setIcon(new javax.swing.ImageIcon(img));

            }
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }

    }

    /***************METHODE QUI PERMET DE METTRE LA FENETRE PRINCIPALE EN PLEIN ECRAN***************************/
    public void pleinEcran(JDialog boiteDialogue) {
        int Xf = (int) boiteDialogue.getSize().getWidth();
        int Yf = (int) boiteDialogue.getSize().getHeight();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//**********************************SELECTION DES ELEVES ET LEURS PARENTS*****************************************************
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void selectionElevesParents() {
        try {
            //String matricule_eleve = "%" + jTextField2.getText() + "%";
            String requete = "SELECT distinct eleve.mtle,eleve.nom_el,eleve.prenom_el,parent.nom_parent,parent.tel_parent,parent.prenom_parent,parent.civilite FROM eleve, parent WHERE eleve.id_parent = parent.id_parent";// WHERE eleve.cni_parent = parent.cni_parent
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                String mtle = rs.getString(1);
                String nom_el = rs.getString(2);
                String prenom_el = rs.getString(3);
                String nom_parent = rs.getString(4);
                String tel_parent = rs.getString(5);
                String prenom_parent = rs.getString(6);
                String civilite = rs.getString(7);

                String nom_prenom_eleve = nom_el + " " + prenom_el;
                String civilite_nom_prenom_parent = civilite + " " + nom_parent + " " + prenom_parent;

                Object[] val = {mtle, nom_el + " " + prenom_el, civilite_nom_prenom_parent, tel_parent};
                mode.addRow(val);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //**********************************RETROUVER LE NUMERO D'UN PARENT apres insertion du matricule de l'eleve****************************************
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void selectionContactParents() {
        try {
            String saisie = "%" + jTextField5.getText() + "%";
            String requete = "SELECT distinct eleve.mtle,eleve.nom_el,eleve.prenom_el,parent.nom_parent, parent.tel_parent,parent.prenom_parent,parent.civilite,eleve.photo_el FROM eleve, parent "
                    + "WHERE (eleve.id_parent = parent.id_parent)and(((eleve.nom_el) Like '" + saisie.replace("'", "''") + "') OR ((eleve.mtle) Like '" + saisie.replace("'", "''") + "')) ORDER BY eleve.nom_el";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                String mtle = rs.getString(1);
                String nom_el = rs.getString(2);
                String prenom_el = rs.getString(3);
                String nom_parent = rs.getString(4);
                String tel_parent = rs.getString(5);
                //String tel_parent2 = rs.getString(6);
                // String tel_el = rs.getString(7);
                String prenom_parent = rs.getString(6);
                String civilite = rs.getString(7);
                String photo_el = rs.getString(8);

                String nom_prenom_eleve = nom_el + " " + prenom_el;
                String civilite_nom_prenom_parent = civilite + " " + nom_parent + " " + prenom_parent;

                Object[] val = {mtle, nom_el + " " + prenom_el, civilite_nom_prenom_parent, tel_parent};
                mode.addRow(val);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //**************AFFICHAGE CONTACTS DU PARENT DANS LA ZONES DE TEXTE ET LA PHOTO DE L'ELEVE************************************************
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void selectionContactsParents() {
        try {
            String saisie = "%" + jTextField5.getText() + "%";
            saisie.toLowerCase();
            String requete = "SELECT distinct eleve.mtle,eleve.nom_el,eleve.prenom_el,parent.nom_parent, parent.tel_parent,parent.prenom_parent,parent.civilite,eleve.photo_el FROM eleve, parent "
                    + "WHERE (eleve.id_parent = parent.id_parent)and(((eleve.nom_el) Like '" + saisie.replace("'", "''") + "') OR ((eleve.mtle) Like '" + saisie.replace("'", "''") + "')) ORDER BY eleve.nom_el";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                String mtle = rs.getString(1);
                String nom_el = rs.getString(2);
                String prenom_el = rs.getString(3);
                String nom_parent = rs.getString(4);
                String tel_parent = rs.getString(5);
                String prenom_parent = rs.getString(6);
                String civilite = rs.getString(7);
                String photo_el = rs.getString(8);

                String nom_prenom_eleve = nom_el + " " + prenom_el;
                String civilite_nom_prenom_parent = civilite + " " + nom_parent + " " + prenom_parent;

                String[] val = {mtle, nom_prenom_eleve, civilite_nom_prenom_parent, tel_parent};
                if (jTextField5.getText().trim().equals(mtle)) {
                    jTextField3.setText(tel_parent);
                    //changerimages();

                } else {
                    jTextField3.setText("");
                }



            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //**************AFFICHAGE DU CONTACT DES PARENTS APRES SELECTION APRES UN SIMPLE CLIC DANS LA LIGNE CORRESPONDANTE DU JTABLE2************************************************
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void affichageContactParent() {
        try {
            int numero = Integer.parseInt(jTable2.getValueAt(jTable2.getSelectedRow(), 0) + "");
            System.out.println("" + numero);
            String requete = "SELECT distinct eleve.mtle,parent.tel_parent FROM eleve, parent WHERE eleve.mtle!=" + numero + "";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                int mtle = rs.getInt(1);
                String tel_parent = rs.getString(2);
            }
            String martricule = jTable2.getValueAt(jTable2.getSelectedRow(), 0) + "";
            String cont_parent = jTable2.getValueAt(jTable2.getSelectedRow(), 3) + "";
            String nom_prenom_eleve = jTable2.getValueAt(jTable2.getSelectedRow(), 1) + "";
            //jTextField17.setText(cont_parent1);
            System.out.println(" cont_parent  " + cont_parent);
            System.out.println(" nom et prenom eleve  " + nom_prenom_eleve);
            jTextField5.setText(nom_prenom_eleve);
            jTextField3.setText(cont_parent);
            //String[] val = {"" + martricule, cont_parent};
            // mode.addRow(val);
            //dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //**************AFFICHAGE DU CONTACT DES PARENTS APRES SELECTION APRES UN SIMPLE CLIC DANS LA LIGNE CORRESPONDANTE DU JTABLE2************************************************
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void affichagePhotoEleve() {
        try {
            int numero = Integer.parseInt(jTable2.getValueAt(jTable2.getSelectedRow(), 0) + "");
            System.out.println("" + numero);
            String requete = "select mtle,img from images where mtle=" + numero + "";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                int mtle = rs.getInt(1);
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
            String requete = "SELECT enseignant.responsabilite FROM enseignant "
                    + "WHERE responsabilite IN ('Proviseur','Censeur','Censeur 1','Censeur 2','Censeur 3','Censeur 4','Censeur 5','Intendant','Conseillé d''Orientation','Surveillant Générale ','Surveillant Generale 1','Surveillant Generale 2','Surveillant Generale 3','Surveillant Generale 4','Surveillant Generale 5' )";
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

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//******************** INSERTION EXPEDITEURS DE MESSAGE AFIN DE FAIRE LA MISE A JOUR*************************************************
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void insertionHistoriqueMsg() {
        try {
            String exp = jComboBox2.getSelectedItem() + "";
            String sujet = jTextField4.getText().trim().replace("'", "''");
            String tel_dest = jTextField3.getText().trim().replace("'", "''");

            String jour = jComboBox3.getSelectedItem() + "";
            String mois = jComboBox4.getSelectedItem() + "";
            String annee = jComboBox5.getSelectedItem() + "";
            String heure = jComboBox6.getSelectedItem() + "";
            String lieu = jTextField1.getText().trim().replace("'", "''");
            String cont_msg = jTextArea1.getText().replace("'", "''");
            String contenu_message = "" + cont_msg + " le " + jour + "/" + mois + "/" + annee + " à " + heure + " précise. Lieu: " + lieu;
            Connector1.statement.executeUpdate("INSERT INTO HISTORIQUES_ENVOIS(exp ,sujet,tel_dest,contenu_message) values ('" + exp + "','" + sujet + "','" + tel_dest + "','" + contenu_message + "')");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //***********SELECTION EXPEDITEUR MESSAGES AFIN DE TRACER L'HISTORIQUES DES ENVOIS******************************************************************
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void selectionHistoriqueMsg() {
        try {
            String requete = "SELECT historiques_envois.id_historique, historiques_envois.exp , historiques_envois.date_heure, historiques_envois.sujet, historiques_envois.tel_dest FROM historiques_envois";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                String id_historique = rs.getString(1);
                String exp = rs.getString(2);
                String date_heure = rs.getString(3);
                String sujet = rs.getString(4);
                String tel_dest = rs.getString(5);
                String[] val = {exp, date_heure, sujet, tel_dest};
                mode1.addRow(val);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //*************SELECTION MESSAGE PREDEFINI ET METTRE DANS LE jComboBox1 ***************************************
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //*************SELECTION ITEM MESSAGE PREDEFINI ET METTRE DANS String[]ET COMPARER AVEC CEUX DANS jComboBox1 ***************************************
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jTextField4 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jComboBox3 = new javax.swing.JComboBox();
        jComboBox4 = new javax.swing.JComboBox();
        jComboBox5 = new javax.swing.JComboBox();
        jComboBox6 = new javax.swing.JComboBox();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButton4 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jTextField5 = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        setClosable(true);
        setTitle("Envoyer les sms à un parent d'élève");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "SMS POUR UN PARENT D'ELEVE", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 24))); // NOI18N

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "ADRESSES DES PARENTS D'ELEVES", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N

        jTable2.setBackground(new java.awt.Color(153, 153, 153));
        jTable2.setFont(new java.awt.Font("Tahoma", 1, 12));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Matricule de l'élève", "Noms & prénoms de l'élève", "Noms & prenoms du parent", "Numero téléphone du parent"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTable2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jTable2MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable2MousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "HISTORIQUE DES ENVOIS", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        jTable1.setBackground(new java.awt.Color(153, 153, 153));
        jTable1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jTable1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Expediteur", "Date & Heure  d'envoi", "Objet du message", "Numero téléphone destinataire"
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
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel2.setText("Expediteur");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel3.setText(" Message prédefini");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Message à envoyé", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jTextArea1.setRows(5);
        jTextArea1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
        );

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

        jComboBox2.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jComboBox2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jTextField4.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jTextField4.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel5.setText("Objet du message");

        jLabel8.setText("          PHOTO DE L'ELEVE");
        jLabel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jTextField3.setEditable(false);
        jTextField3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel4.setText("Contact du parent");

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Date Heure & Lieu", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        jComboBox3.setFont(new java.awt.Font("Times New Roman", 1, 12));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Jour", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        jComboBox3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jComboBox4.setFont(new java.awt.Font("Times New Roman", 1, 12));
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mois", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        jComboBox4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jComboBox5.setFont(new java.awt.Font("Times New Roman", 1, 12));
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Année", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50" }));
        jComboBox5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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
                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jButton2.setBackground(new java.awt.Color(255, 255, 102));
        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jButton2.setText("Annuler");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));

        jButton4.setBackground(new java.awt.Color(255, 255, 102));
        jButton4.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jButton4.setText("Quitter");
        jButton4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 255, 102));
        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jButton1.setText("Envoyer");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Entrez le Nom de l'élève ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        jPanel5.setFont(new java.awt.Font("Times New Roman", 1, 14));

        jTextField5.setFont(new java.awt.Font("Times New Roman", 1, 13));
        jTextField5.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField5KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(jLabel3))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1, 0, 246, Short.MAX_VALUE)
                            .addComponent(jComboBox2, 0, 246, Short.MAX_VALUE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(51, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
        //selectionContactsParents1();
}//GEN-LAST:event_jTable2MouseClicked

    private void jTable2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseEntered
        // TODO add your handling code here:
        // selectionContactsParents1();
}//GEN-LAST:event_jTable2MouseEntered

    private void jTable2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseExited
        // TODO add your handling code here:
        // selectionContactsParents1();
}//GEN-LAST:event_jTable2MouseExited

    private void jTable2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MousePressed
        // TODO add your handling code here:
        affichageContactParent();
        affichagePhotoEleve();
}//GEN-LAST:event_jTable2MousePressed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        selectionItemMsgPredefini();
}//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:selectionItemMsgPredefini();
}//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jTextField3ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
        //selectionItemMsgPredefini();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            connexion("COM7", "NOKIA", "3110c");
            envoiMessage();
        } catch (Exception e) {
        }
        ///insertionHistoriqueMsg();
}//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling codispose();de here:
        //quitterBoiteDeDialogue();
        dispose();

}//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        // dispose();
}//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        // TODO add your handling code here:
        effaceTable(jTable2, mode);
        selectionContactParents();
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jTextField5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5KeyTyped
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    public static javax.swing.JTable jTable1;
    public static javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
