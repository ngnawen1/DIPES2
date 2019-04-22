/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MessagePourPlusParent.java
 *
 * Created on 10 mai 2012, 19:25:31
 */
package sipee;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.sql.ResultSet;
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
public class MessagePourPlusParent extends javax.swing.JInternalFrame {

    /** Creates new form MessagePourPlusParent */
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

    public MessagePourPlusParent() {
        Connector1.initialise();
        initComponents();
        paramTable1();
        paramTable();
        paramTable3();
        jComboBox1.setBackground(Color.WHITE);
        jComboBox2.setBackground(Color.WHITE);
        jComboBox3.setBackground(Color.WHITE);
        jComboBox4.setBackground(Color.WHITE);
        jComboBox5.setBackground(Color.WHITE);
        jComboBox6.setBackground(Color.WHITE);
        // this.setVisible(true);
        //this.setSize(MainFrame.jDesktopPane1.getWidth() - 20, MainFrame.jDesktopPane1.getHeight() - 10);
        effaceTable(jTable2, mode);
        selectionElevesParents();
        selectionExpediteur();
        selectionMsgPredefini();
        effaceTable(jTable1, mode1);
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

    public void annuler() {
        jTextArea1.setText(null);
        jTextField1.setText(null);
        jTextField2.setText(null);
        jTextField4.setText(null);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //****************CONNEXION AU MODEM*******************************************************
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void connexion(String port, String marque, String version) {
        srv = new CService(port, 9600, marque, version);
        System.out.println("SendMessage(): application d'envoie de sms par GSM.");
        System.out.println("  API utilisé: " + CService._name + " version " + CService._version);
        //configuration du code pin
        try {
            System.out.println("configuration du code PIN de la carte SIM....");
            srv.setSimPin("0000");
            srv.setSimPin2("0000");
            System.out.println("");
            System.out.println("etablissement de la connexion.....");
            srv.connect();
            System.out.println(" CONNEEXION ETABLIE AVEC SUCCES");
            System.out.println("connexion etablie avec le modem GSM(Nokia 3110)");
            srv.setSmscNumber("+23779000002");//srv.setSmscNumber("");
            System.out.println("*********Mobile Device Information: ********");
            System.out.println("	Manufacturer  : " + srv.getDeviceInfo().getManufacturer());
            System.out.println("	Model         : " + srv.getDeviceInfo().getModel());
            System.out.println("	Serial No     : " + srv.getDeviceInfo().getSerialNo());
            System.out.println("	IMSI          : " + srv.getDeviceInfo().getImsi());
            System.out.println("	S/W Version   : " + srv.getDeviceInfo().getSwVersion());
            System.out.println("	Battery Level : " + srv.getDeviceInfo().getBatteryLevel() + "%");
            System.out.println("	Signal Level  : " + srv.getDeviceInfo().getSignalLevel() + "%");
            System.out.println("");

        } catch (Exception e) {
            System.out.println("!!!!!!!!!!!   echec de la connexion   !!!!!!!!!");
            e.printStackTrace();
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //************ENVOI MESSAGE A UN ENSEMBLE DE PARENTS SELECTIONNES********************************************************/////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void envoiMessageParent() {
        try {
            String Expediteur = jComboBox2.getSelectedItem() + "";//
            String objet_message = jTextField4.getText().trim();
            String message = jTextArea1.getText().trim();
            String jour = jComboBox3.getSelectedItem() + "";//
            String mois = jComboBox4.getSelectedItem() + "";//
            String annee = jComboBox5.getSelectedItem() + "";//
            String heure = jComboBox6.getSelectedItem() + "";//
            String lieu = jTextField1.getText().trim();
            LinkedList hb = new LinkedList();

            String sms = "EXP: " + Expediteur + ".OBJ: " + objet_message + ".MSG:" + message + " le " + jour + "//" + mois + "//" + annee + " à " + heure + " précise. Lieu: " + lieu;
            System.out.println("" + sms);

            for (int i = 0; i < jTable3.getRowCount(); i++) {
                String cont1 = jTable3.getValueAt(i, 0) + "";
                String cont = "+237".concat(cont1);
                //String cont1 = jTable3.getValueAt(i, 2) + "";
                System.out.println("tel_ens " + cont);
                COutgoingMessage msg = new COutgoingMessage(cont.trim(), sms);
                // msg.setMessageEncoding(CMessage.MessageEncoding.Enc7Bit);
                msg.setMessageEncoding(CMessage.MessageEncoding.EncUcs2);
                msg.setStatusReport(true);
                msg.setValidityPeriod(8);
                System.out.println("le systeme se prepare à envoyer un message aux numeros suivants: ");
                System.out.println(" numero parent1==========> " + cont);
                srv.setSmscNumber("+23779000002");
                System.out.println("veuillez patienter....");
                hb.add(msg);
            }
            srv.sendMessage(hb);
            insertionHistoriqueMsg();
            JOptionPane.showMessageDialog(null, "MESSAGE ENVOYE AVEC SUCCES");
            effaceTable(jTable1, mode1);
            selectionHistoriqueMsg();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ECHEC D'ENVOI DU MESSAGE");
            //e.printStackTrace();
        }
        //System.exit(0);
    }
    //**************DECONNEXION DU MODEM****************************************************////////////////

    public static void deconnexion() throws Exception {
        srv.disconnect();
    }

    /***************METHODE QUI PERMET DE PARAMETRER LA TABLE(jTable2,mode,dtcm) DES ADRESSES********************************************/
    public void paramTable() {
        jTable2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        mode = (DefaultTableModel) jTable2.getModel();
        dtcm = (DefaultTableColumnModel) jTable2.getColumnModel();
        jTable2.setRowHeight(40);
        largeurColoneMax(dtcm, 0, 80);
        //largeurColoneMax(dtcm, 1, 200);
        //largeurColoneMax(dtcm, 2, 250);
        largeurColoneMax(dtcm, 3, 150);
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

    /***********************************METHODE QUI PERMET DE PARAMETRER LA TABLE(jTable1,mode1,dtcm1)  DES HISTORIQUES********************************************/
    public void paramTable1() {
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        mode1 = (DefaultTableModel) jTable1.getModel();
        dtcm1 = (DefaultTableColumnModel) jTable1.getColumnModel();

        jTable1.setRowHeight(40);
        largeurColoneMax(dtcm1, 1, 120);
        largeurColoneMax(dtcm1, 3, 95);
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

    /***************METHODE QUI PERMET DE PARAMETRER LA TABLE (jTable3,mode2,dtcm2)  DES ADRESSES DES ENSEIGNANTS********************************************/
    public void paramTable3() {
        jTable3.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        mode2 = (DefaultTableModel) jTable3.getModel();
        dtcm2 = (DefaultTableColumnModel) jTable3.getColumnModel();
        jTable3.setRowHeight(30);
        // largeurColoneMax(dtcm2, 0, 20);
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //**********************************SELECTION DES ELEVES ET LEURS PARENTS DANS LA BD////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void selectionElevesParents() {
        try {

            String requete = "SELECT eleve.mtle,eleve.nom_el,eleve.prenom_el,parent.nom_parent, parent.tel_parent,parent.prenom_parent,parent.civilite FROM eleve, parent WHERE eleve.id_parent = parent.id_parent";// WHERE eleve.cni_parent = parent.cni_parent
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

                String nom_prenom_eleve = nom_el + " " + prenom_el;
                String civilite_nom_prenom_parent = civilite + " " + nom_parent + " " + prenom_parent;

                Object[] val = {mtle, nom_el + " " + prenom_el, civilite_nom_prenom_parent, tel_parent};
                mode.addRow(val);
                //System.out.println("mtle  :" + mtle + " nom_parent :" + nom_parent + "   tel_parent1 :" + tel_parent1 + "  tel_parent2 :" + tel_parent2+ "   tel_pere1 :" + tel_el);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //**********************************AFFICHAG DU CONTACT DES PARENTS APRES SELECTION APRES UN SIMPLE CLIC DANS LA LIGNE CORRESPONDANTE DU JTABLE2 ////////////////////////////////////////////
    //**********************************AFFICHAGE DU CONTACT SE FAIT DANS LE JTABLE3 /////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void affichageContactParent() {
        try {
            int numero = Integer.parseInt(jTable2.getValueAt(jTable2.getSelectedRow(), 0) + "");
            System.out.println("" + numero);
            String requete = "SELECT eleve.mtle,parent.nom_parent,parent.prenom_parent,parent.tel_parent FROM eleve, parent WHERE eleve.mtle!=" + numero + "";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                int mtle = rs.getInt(1);
                String nom_parent = rs.getString(2);
                String prenom_parent = rs.getString(3);
                String tel_parent = rs.getString(4);
            }
            String martricule = jTable2.getValueAt(jTable2.getSelectedRow(), 0) + "";
           // String nom_parent = jTable2.getValueAt(jTable2.getSelectedRow(), 1) + "";
            String nom_prenom_parent = jTable2.getValueAt(jTable2.getSelectedRow(), 2) + "";
            String cont_parent = jTable2.getValueAt(jTable2.getSelectedRow(), 3) + "";
            System.out.println(" cont_parent  " + cont_parent);
            String[] val = {cont_parent, nom_prenom_parent};
            mode2.addRow(val);
            //dispose();

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

    //******************** INSERTION EXPEDITEURS DE MESSAGE AFIN DE FAIRE LA MISE A JOUR DES ENSEIGNANTS*************************************************
    public void insertionHistoriqueMsg() {
        try {
            String exp = jComboBox2.getSelectedItem() + "";
            String sujet = jTextField4.getText().trim().replace("'", "''");

            String jour = jComboBox3.getSelectedItem() + "";
            String mois = jComboBox4.getSelectedItem() + "";
            String annee = jComboBox5.getSelectedItem() + "";
            String heure = jComboBox6.getSelectedItem() + "";
            String lieu = jTextField1.getText().trim().replace("'", "''");
            String cont_msg = jTextArea1.getText().replace("'", "''");
            String contenu_message = "" + cont_msg + "" + jour + "" + mois + "" + annee + "" + heure + "" + lieu;

            for (int i = 0; i < jTable3.getRowCount(); i++) {
                String tel_dest = jTable3.getValueAt(i, 2) + "";
                //String cont1 = jTable3.getValueAt(i, 2) + "";
                System.out.println("tel_ens " + tel_dest);
                Connector1.statement.executeUpdate("INSERT INTO HISTORIQUES_ENVOIS(exp ,sujet,tel_dest,contenu_message) values ('" + exp + "','" + sujet + "','" + tel_dest + "','" + contenu_message + "')");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }

    }

    //***********SELECTION EXPEDITEUR MESSAGES AFIN DE TRACER L'HISTORIQUES DES ENVOIS******************************************************************
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

    //***************************SUPPRESSION DES DOUBLONS*************************/
    public void suppressionDoublons() {
        try {
            int[] selected = jTable3.getSelectedRows();

            if (selected.length == 0) {
                JOptionPane.showMessageDialog(null, "VEUILLEZ SELECIONNER UN ENSEIGNEMENT SVP!", "Selection !", JOptionPane.INFORMATION_MESSAGE);
            } else {
                mode2.removeRow(jTable3.getSelectedRow());
                jTable3.getRowCount();
                jTable3.getColumnCount();
                System.out.println("linge " + jTable3.getRowCount());
                System.out.println("colonne " + jTable3.getColumnCount());
                for (int i = 0; i < jTable3.getRowCount(); i++) {
                    String cont = jTable3.getValueAt(i, 2) + "";
                    System.out.println("tel_ens " + cont);

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }

    //**************FILTRAGE DES CONTACTS*************************************µ*****************µ
    public void filtrerContactsParents() {
        try {
            String saisie = "%" + jTextField2.getText() + "%";
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
    //**********************************RETROUVER LE NUMERO D'UN PARENT apres insertion du matricule de l'eleve ou le nom de l'eleve dans la zone texte jTextField2******************

    public void selectionDestinataire1() {
        try {
            String saisie = "%" + jTextField2.getText() + "%";
            String requete = "SELECT eleve.mtle,eleve.nom_el,eleve.prenom_el,parent.nom_parent, parent.tel_parent,parent.prenom_parent,parent.civilite,eleve.photo_el FROM eleve, parent "
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

                Object[] val = {mtle, nom_el + " " + prenom_el, civilite_nom_prenom_parent, tel_parent};
                mode.addRow(val);



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
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jToggleButton1 = new javax.swing.JToggleButton();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        setClosable(true);
        setTitle("Envoyer les sms à plusieurs parents d'élèves");

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "SMS POUR PLUSIEURS PARENTS", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 24))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel2.setText("Expediteur");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel3.setText("Message prédefini");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Message à envoyé", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jTextArea1.setRows(5);
        jTextArea1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
        );

        jComboBox1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
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

        jComboBox2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
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

        jComboBox3.setFont(new java.awt.Font("Times New Roman", 1, 11));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Jour", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        jComboBox3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jComboBox4.setFont(new java.awt.Font("Times New Roman", 1, 11));
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mois", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        jComboBox4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));

        jTable3.setBackground(new java.awt.Color(153, 153, 153));
        jTable3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " Contacts Parents", "Noms Parents"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class
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

        jToggleButton1.setBackground(new java.awt.Color(255, 255, 153));
        jToggleButton1.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jToggleButton1.setText("Suppr les doublons");
        jToggleButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButton1))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(jLabel3))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                            .addComponent(jComboBox1, 0, 236, Short.MAX_VALUE)
                            .addComponent(jComboBox2, 0, 236, Short.MAX_VALUE)))
                    .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ADRESSES DES PARENTS D'ELEVES", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N

        jTable2.setBackground(new java.awt.Color(153, 153, 153));
        jTable2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
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

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel4.setText("Entrez le nom du parent recherché");

        jTextField2.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jTextField2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(73, Short.MAX_VALUE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "HISTORIQUE DES ENVOIS", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N

        jTable1.setBackground(new java.awt.Color(153, 153, 153));
        jTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
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
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:
}//GEN-LAST:event_jTable3MouseClicked

    private void jTable3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MousePressed
        // TODO add your handling code here:
}//GEN-LAST:event_jTable3MousePressed

    private void jTable3MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseDragged
        // TODO add your handling code here:
}//GEN-LAST:event_jTable3MouseDragged

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        selectionItemMsgPredefini();
}//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jTextField4ActionPerformed

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
}//GEN-LAST:event_jTable2MousePressed

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:
        effaceTable(jTable2, mode);
        selectionDestinataire1();
}//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        // TODO add your handling code here:
        //effaceTable(jTable2, mode);
        //selectionDestinataire0();
}//GEN-LAST:event_jTextField2KeyTyped

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
        suppressionDoublons();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            connexion("COM7", "NOKIA", "3110c");
            envoiMessageParent();
        } catch (Exception e) {
            e.printStackTrace();
        }
}//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        // quitterBoiteDeDialogue();
        dispose();
}//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        annuler();
}//GEN-LAST:event_jButton2ActionPerformed
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    public static javax.swing.JTable jTable1;
    public static javax.swing.JTable jTable2;
    public static javax.swing.JTable jTable3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
}
