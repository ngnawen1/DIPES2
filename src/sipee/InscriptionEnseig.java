/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * InscriptionEnseig.java
 *
 * Created on 10 mai 2012, 09:52:20
 */
package sipee;

import java.awt.Color;
import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.io.File;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;

/**
 */
public class InscriptionEnseig extends javax.swing.JInternalFrame {

    /** Creates new form InscriptionEnseig */
    Connector1 co = new Connector1();
    public static JDialog jdClient;
    DefaultComboBoxModel comboDestination = new DefaultComboBoxModel();
    DefaultComboBoxModel comboBailleur = new DefaultComboBoxModel();
    DefaultComboBoxModel comboAnnee = new DefaultComboBoxModel();
    DefaultComboBoxModel comboDefault = new DefaultComboBoxModel();
    static DefaultTableModel mode = new DefaultTableModel();
    static DefaultTableColumnModel dtcm = new DefaultTableColumnModel();
    //public static String tel_ens = "00000000";

    public InscriptionEnseig() {
        Connector1.initialise();
        initComponents();
        // this.setVisible(true);
        // this.setSize(MainFrame.jDesktopPane1.getWidth() - 50, MainFrame.jDesktopPane1.getHeight() - 50);
        jTextField3.setEditable(false);
        jPasswordField1.setEditable(false);
        couleursFond();

    }

    public void couleursFond() {
        jComboBox9.setBackground(Color.WHITE);
        jComboBox4.setBackground(Color.WHITE);
        jComboBox5.setBackground(Color.WHITE);
        jComboBox6.setBackground(Color.WHITE);
        jComboBox1.setBackground(Color.WHITE);
        jComboBox2.setBackground(Color.WHITE);
        jComboBox10.setBackground(Color.WHITE);
    }

    /*******************************METHODE QUI PERMET DE RETOURNER LE DERNIER ENREGISTREMENT de la table parent****************************/
    public static int retourneMaxNumberEnseignant() {
        int tmp1 = 0;
        try {
            String requete = "SELECT Max(enseignant.id_ens) AS Maxenseignant FROM enseignant";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                tmp1 = rs.getInt(1);
                System.out.println("" + tmp1);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
        return tmp1;
    }

    /*******************************METHODE QUI PERMET DE RETOURNER LE DERNIER ENREGISTREMENT de la table parent****************************/
    public static int retourneMaxNumberAdmin() {
        int tmp2 = 0;
        try {
            String requete = "SELECT Max(admin.id_admin) AS Maxadmin FROM admin";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                tmp2 = rs.getInt(1);
                System.out.println("" + tmp2);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
        return tmp2;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //************INSERTION DU CHEMIN D'ACCES DA UNE IMAGE DANS LA BASE DE DONNEE************************************
    /////////////////////////////////////////////////////////////////////////////////////////////
    public String insertImages() {
        String chemin = null;
        try {

            int id_ens = retourneMaxNumberEnseignant() + 1;
            System.out.println("id_ens: " + id_ens);
            int id_img = retourneMaxNumberImageEns() + 1;
            System.out.println("id_img: " + id_img);
            JFileChooser sélecteur = new JFileChooser();
            sélecteur.setBackground(new java.awt.Color(255, 255, 204));
            sélecteur.addChoosableFileFilter(new FileNameExtensionFilter("Images JPEG", "jpg", "jpeg"));
            sélecteur.addChoosableFileFilter(new FileNameExtensionFilter("Images GIF", "gif"));
            sélecteur.addChoosableFileFilter(new FileNameExtensionFilter("Images PNG", "png"));
            sélecteur.setAcceptAllFileFilterUsed(false);
            if (sélecteur.showDialog(this, "Sélectionner votre image") == JFileChooser.APPROVE_OPTION) {
                File fichier = sélecteur.getSelectedFile();
                jLabel8.setIcon(new ImageIcon(getClass().getResource("/photos/" + fichier.getName())));
                //System.out.println(""+getClass().getResource("/photos/"));
                String remplacement = "" + getClass().getResource("/photos/" + fichier.getName());
                String chemin_tempo = remplacement.replaceFirst("file:/", "");
                chemin = chemin_tempo.replace("/", "//");
                System.out.println(chemin);
                Connector1.statement.executeUpdate("INSERT INTO imagesens(id_img,id_ens,img) values('" + id_img + "','" + id_ens + "','" + chemin + "')");

            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "RENSEIGNER LES CHAMPS OBLIGATOIRES");
        }
        return chemin;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////*************methode permettant d quitter la boirt de dialogue*******************************
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*******************************METHODE QUI PERMET DE RETOURNER LE DERNIER ENREGISTREMENT de la table parent****************************/
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int retourneMaxNumberImageEns() {
        int tmp2 = 0;
        try {
            String requete = "SELECT Max(imagesens.id_img) AS Maxparent FROM imagesens";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                tmp2 = rs.getInt(1);
                System.out.println("" + tmp2);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
        return tmp2;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*******************************INSERTION DU MOT DE PASSE ET DU LOGIN**************************/
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void insertionInfoAdmin() {
        try {
            int id_admin = retourneMaxNumberAdmin() + 1;
            String login_admin = jTextField3.getText();
            String pwd_admin = String.copyValueOf(jPasswordField1.getPassword());
            String responsabilite = jComboBox10.getSelectedItem() + "";//
            if (responsabilite.equals("Censeur") || responsabilite.equals("Censeur 1") || responsabilite.equals("Censeur 2") || responsabilite.equals("Censeur 3") || responsabilite.equals("Censeur 4")|| responsabilite.equals("Secretaire") 
                    || responsabilite.equals("Intendant") || responsabilite.equals("Surveillant générale") || responsabilite.equals("Comptable matière") || responsabilite.equals("Surveillant générale 1")
                    || responsabilite.equals("Surveillant générale 2") || responsabilite.equals("Surveillant générale 3") || responsabilite.equals("Surveillant générale 4") || responsabilite.equals("Conseillé d'Orientation")) {
                Connector1.statement.executeUpdate("INSERT INTO admin( id_admin,login_admin,pwd_admin,responsabilite) "
                        + "values('" + id_admin + "','" + login_admin + "','" + pwd_admin + "','" + responsabilite.replace("'", "''") + "')");
                System.out.println("jTextField3:  " + login_admin);
                System.out.println("jPasswordField1:  " + pwd_admin);
            } else {
                System.out.println("==========RAS=================");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }


    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //*********INSERTION INFORMATIONS SUR L'ENSEIGNANT************************************
    //////////////////////////////////////////////////////////////////////////////////////////////////
    public void insertionInfosEnseignant() {
        int jour_naiss = 0;
        int mois_naiss = 0;
        int annee_naiss = 0;
        int cni_ens = 0;
        try {

            int id_ens = retourneMaxNumberEnseignant() + 1;
            String cni_ens_temp = jFormattedTextField1.getText();//
            cni_ens = Integer.parseInt(cni_ens_temp);
            String nom_ens = jTextField1.getText().toUpperCase();//
            String prenom_ens = jTextField2.getText().toUpperCase();//
            String sexe_ens = jComboBox1.getSelectedItem() + "";//
            String civilite = jComboBox9.getSelectedItem() + "";//
            String grade_ens = jComboBox2.getSelectedItem() + "";//
            String responsabilite = jComboBox10.getSelectedItem() + "";//
            //date de naissance
            String tempo_jour = jComboBox4.getSelectedItem() + "";//
            String tempo_mois = jComboBox5.getSelectedItem() + "";//
            String tempo_annee = jComboBox6.getSelectedItem() + "";//
            jour_naiss = Integer.parseInt(tempo_jour);
            mois_naiss = Integer.parseInt(tempo_mois);
            annee_naiss = Integer.parseInt(tempo_annee);
            String tel_ens_tempo = jFormattedTextField7.getText();
            System.out.println("tel_ens_tempo============" + tel_ens_tempo);
            String lieu_naiss_ens = jTextField6.getText();
            if (tel_ens_tempo.equals("  -  -  -  ") || (nom_ens.isEmpty()) || (grade_ens.equals(" ")) || (responsabilite.equals(" "))) {
                JOptionPane.showMessageDialog(null, "RENSEIGNEZ LES CHAMPS OBLIGATOIRES SVP");
            } else {
                String tel1 = tel_ens_tempo.substring(0, 2);
                String tel2 = tel_ens_tempo.substring(3, 5);
                String tel3 = tel_ens_tempo.substring(6, 8);
                String tel4 = tel_ens_tempo.substring(9, 11);
                String tel_ens = "" + tel1 + "" + tel2 + "" + tel3 + "" + tel4;
                System.out.println("tel_ens============" + tel_ens);
                Connector1.statement.executeUpdate("INSERT INTO enseignant( id_ens,cni_ens,civilite,nom_ens, prenom_ens, sexe_ens, grade_ens, responsabilite, date_naiss_ens, lieu_naiss_ens,tel_ens)"
                        + "values('" + id_ens + "','" + cni_ens + "','" + civilite + "','" + nom_ens + "','" + prenom_ens + "','" + sexe_ens + "','" + grade_ens + "','" + responsabilite.replace("'", "''") + "','" + annee_naiss + "-" + mois_naiss + "-" + jour_naiss + "','" + lieu_naiss_ens + "','" + tel_ens + "')");

                int reponse = JOptionPane.showConfirmDialog(null, "ENREGISTREMENT EFFECTUE AVEC SUCCES \n"
                        + "             NOUVEL ENSEIGNANT ?", " ALORS CLIQUER SUR OUI", JOptionPane.YES_NO_OPTION);
                if (reponse == JOptionPane.YES_OPTION) {
                    dispose();
                    InscriptionEnseig IEn = new InscriptionEnseig();
                    MainFrame.centrerDsFrame(IEn, MainFrame.jDesktopPane1);
                    MainFrame.jDesktopPane1.add(IEn);
                    IEn.setVisible(true);

                } else {
                    dispose();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "RENSEIGNEZ LES CHAMPS OBLIGATOIRES SVP");
            //JOptionPane.showMessageDialog(null, "Erreur :" + ex.getMessage());
            //dispose();
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

        jPanel1 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jComboBox5 = new javax.swing.JComboBox();
        jComboBox6 = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jComboBox9 = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jFormattedTextField7 = new javax.swing.JFormattedTextField();
        jComboBox10 = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(232, 233, 224));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        setClosable(true);
        setTitle("Ajouter un nouvel enseignant");

        jPanel1.setBackground(new java.awt.Color(232, 233, 224));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "ENREGISTREMENT D'UN ENSEIGNANT", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 30))); // NOI18N

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Information sur l'enseignant", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 24))); // NOI18N
        jPanel11.setFont(new java.awt.Font("Times New Roman", 3, 18));
        jPanel11.setOpaque(false);

        jTextField1.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jTextField1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));

        jTextField2.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jTextField2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel2.setText("Prénoms");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "M", "F" }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel4.setText("Sexe");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel6.setText("Date Naiss.");

        jComboBox4.setFont(new java.awt.Font("Times New Roman", 1, 12));
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "JJ", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        jComboBox4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jComboBox5.setFont(new java.awt.Font("Times New Roman", 1, 12));
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MM", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        jComboBox5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jComboBox6.setFont(new java.awt.Font("Times New Roman", 1, 12));
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AA", "1960", "1961", "1962", "1963", "1964", "1965", "1966", "1967", "1968", "1969", "1970", "1971" }));
        jComboBox6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel7.setText("Lieu Naiss.");

        jTextField6.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jTextField6.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel3.setText("Noms");

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel16.setText("Numero CNI ");

        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "M.", "Mme", "Mlle" }));
        jComboBox9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel17.setText("Civilite");

        jFormattedTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        try {
            jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#########")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextField1.setFont(new java.awt.Font("Times New Roman", 1, 14));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel10.setForeground(new java.awt.Color(255, 0, 0));
        jLabel10.setText("(*)");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel11.setForeground(new java.awt.Color(255, 0, 0));
        jLabel11.setText("(*)");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel12.setForeground(new java.awt.Color(255, 0, 0));
        jLabel12.setText("(*)");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12)
                                .addGap(5, 5, 5)
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addGap(1, 1, 1)
                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(71, 71, 71)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(jLabel10))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel17))
                    .addComponent(jComboBox9, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))))
                .addGap(7, 7, 7)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12))
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel15.setBackground(new java.awt.Color(232, 233, 224));
        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Informations professionnelles", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 24))); // NOI18N
        jPanel15.setFont(new java.awt.Font("Times New Roman", 3, 18));

        jLabel46.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel46.setText("Responsabilite");

        jLabel47.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel47.setText("Tél ");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel13.setText("Grade");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "PLEG", "PCEG", "IEG", "MEPS", "ECI", "VAC", "PLEG(VAC)", "PCEG(VAC)" }));
        jComboBox2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jFormattedTextField7.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        try {
            jFormattedTextField7.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-##-##-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextField7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jFormattedTextField7.setFont(new java.awt.Font("Times New Roman", 1, 14));

        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Proviseur", "Censeur", "Censeur 1", "Censeur 2", "Censeur 3", "Censeur 4", "Censeur 5", "Intendant", "Secretaire", "Conseillé d'Orientation", "Comptable matière", "Surveillant générale", "Surveillant générale 1", "Surveillant générale 2", "Surveillant générale 3", "Surveillant générale 4", "Surveillant de secteur", "Surveillant de secteur 1", "Surveillant de secteur 2", "Surveillant de secteur 3", "Surveillant de secteur 4", "AP de mathématiques", "AP d'informatique", "AP de français", "AP de PCT", "AP d'anglais", "AP d'espagnole", "AP d'allemand", "Enseignant de mathématiques", "Enseignant d'informatique", "Enseignant de français", "Enseignant de PCT", "Enseignant d'anglais" }));
        jComboBox10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jComboBox10.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox10ItemStateChanged(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(232, 233, 224));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jTextField3.setFont(new java.awt.Font("Tahoma", 1, 12));
        jTextField3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPasswordField1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jPasswordField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 12));
        jLabel1.setText("    Login de connexion ");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 12));
        jLabel5.setText("        Mot de passe ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPasswordField1)
                    .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)))
        );

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel14.setForeground(new java.awt.Color(255, 0, 0));
        jLabel14.setText("(*)");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel15.setForeground(new java.awt.Color(255, 0, 0));
        jLabel15.setText("(*)");

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel18.setForeground(new java.awt.Color(255, 0, 0));
        jLabel18.setText("(*)");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(9, 9, 9)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel46)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox10, 0, 160, Short.MAX_VALUE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel47)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel15)
                    .addComponent(jLabel18)
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(jLabel14))
                        .addGap(29, 29, 29))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        jButton6.setBackground(new java.awt.Color(255, 255, 153));
        jButton6.setFont(new java.awt.Font("Arial Black", 1, 14));
        jButton6.setText("Quitter");
        jButton6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 255, 153));
        jButton4.setFont(new java.awt.Font("Arial Black", 1, 14));
        jButton4.setText("Annuler");
        jButton4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(255, 255, 153));
        jButton5.setFont(new java.awt.Font("Arial Black", 1, 14));
        jButton5.setText("Enregistrer");
        jButton5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jToggleButton1.setBackground(new java.awt.Color(255, 255, 153));
        jToggleButton1.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jToggleButton1.setText("Inserer la photo");
        jToggleButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jLabel9.setBackground(new java.awt.Color(255, 255, 153));
        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jLabel9.setForeground(new java.awt.Color(255, 0, 51));
        jLabel9.setText("(*) Indique que le champs est obligatoire.");

        jLabel8.setText("          PHOTO ");
        jLabel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9))
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        jTextField1.setText(null);
        jTextField2.setText(null);
        jTextField6.setText(null);

        jComboBox6.setToolTipText("00");
        jComboBox5.setSelectedItem("0");
        jComboBox4.setSelectedItem("0");
        jComboBox2.setSelectedItem("0");
        jComboBox1.setSelectedItem("0");
        jFormattedTextField1.setText(null);
        jFormattedTextField7.setText(null);
}//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        insertionInfosEnseignant();
        if (jTextField3.getText() == null && jPasswordField1.getPassword() == null) {
            System.out.println("cet enseignant n'est pas de l'administration");
        } else {
            insertionInfoAdmin();
        }

}//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        quitterBoiteDeDialogue();
}//GEN-LAST:event_jButton6ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
        insertImages();
}//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jComboBox10ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox10ItemStateChanged
        // TODO add your handling code here:
        String responsabilite = jComboBox10.getSelectedItem() + "";//
        if (responsabilite.equals("Proviseur") || responsabilite.equals("Censeur") || responsabilite.equals("Censeur 1") || responsabilite.equals("Censeur 2") || responsabilite.equals("Censeur 3") || responsabilite.equals("Censeur 4")
                || responsabilite.equals("Surveillant générale") || responsabilite.equals("Surveillant générale 1") || responsabilite.equals("Surveillant générale 2") || responsabilite.equals("Surveillant générale 3") || responsabilite.equals("Surveillant générale 4") || responsabilite.equals("Surveillant générale 5")
                || responsabilite.equals("Surveillant de secteur") || responsabilite.equals("Surveillant de secteur 1") || responsabilite.equals("Surveillant de secteur 2") || responsabilite.equals("Surveillant de secteur 3") || responsabilite.equals("Surveillant de secteur 4")
                || responsabilite.equals("Intendant") || responsabilite.equals("Conseillé d'Orientation") || responsabilite.equals("Secretaire")) {
            jTextField3.setEditable(true);
            jPasswordField1.setEditable(true);
        } else {
            jTextField3.setEditable(false);
            jPasswordField1.setEditable(false);
        }

    }//GEN-LAST:event_jComboBox10ItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox10;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox9;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
}
