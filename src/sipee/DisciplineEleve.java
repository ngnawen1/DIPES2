package sipee;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.sql.ResultSet;
import java.awt.Toolkit;
import java.util.Calendar;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DisciplineEleve.java
 *
 * Created on 22 mai 2012, 11:03:06
 */
/**
 *
 * @author admine
 */
public class DisciplineEleve extends javax.swing.JInternalFrame {

    /** Creates new form DisciplineEleve */
    DefaultComboBoxModel comboDestination = new DefaultComboBoxModel();
    DefaultComboBoxModel comboBailleur = new DefaultComboBoxModel();
    DefaultComboBoxModel comboAnnee = new DefaultComboBoxModel();
    DefaultComboBoxModel comboDefault = new DefaultComboBoxModel();
    static DefaultTableModel mode = new DefaultTableModel();
    static DefaultTableModel mode1 = new DefaultTableModel();
    static DefaultTableColumnModel dtcm = new DefaultTableColumnModel();
    static DefaultTableColumnModel dtcm1 = new DefaultTableColumnModel();
    public static final int x = (int) Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int y = (int) Toolkit.getDefaultToolkit().getScreenSize().height;

    public DisciplineEleve() {
        Connector1.initialise();
        initComponents();
        anneeScolaireCourant();
        paramTable();
        jComboBox5.setEnabled(false);
        jComboBox2.setEnabled(false);

    }

    /***********************************METHODE QUI PERMET DE PARAMETRER LA TABLE DES HISTORIQUES********************************************/
    public void paramTable() {
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        mode = (DefaultTableModel) jTable1.getModel();
        dtcm = (DefaultTableColumnModel) jTable1.getColumnModel();

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
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*********************************************************************************************************/
    /**********************************************************************************************************************/
    private static TableCellRenderer label = null;

    /*******************************************************/
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
            setFont(new java.awt.Font("Times New Roman", 1, 14));

            int reste = row / 2;
            if ((2 * reste) < row) {
                setBackground(new Color(255, 255, 255));
            } else {
                setBackground(new Color(240, 240, 240));
            }
            if (isSelected) {
                int nb = 0;
                if (nb <= column) {
                    setBackground(new Color(255, 204, 204));
                    setForeground(Color.BLUE);
                } else {
                    setBackground(new Color(255, 204, 204));
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

    /************************************************************/
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
                lbl.setPreferredSize(new Dimension(75, 50));
                lbl.setFont(font);
                lbl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

                lbl.setBackground(new Color(255, 204, 204));
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

    //************AFFICHER L'ANNEE SCOLAIRE EN COURS***********************
    public void anneeScolaireCourant() {
        try {
            Calendar date = Calendar.getInstance();
            System.out.println("Année : " + (date.get(date.YEAR)));
            String annee = "" + date.get(date.YEAR);
            int ann = Integer.parseInt(annee);
            System.out.println("Ann : " + (ann - 1));
            String anScolire = "" + (ann - 1) + "/" + ann;
            //jTextField1.setText(anScolire);
            System.out.println("Ann : " + anScolire);
        } catch (Exception e) {
        }
    }

    ///****************Selection niveau*********************************
    public void selectionClassesNiveau() {
        String niveau = jComboBox3.getSelectedItem() + "";
        jComboBox5.setEnabled(true);
        System.out.println("niveau: " + niveau);
        if (niveau.equalsIgnoreCase("*")) {
            jComboBox5.removeAllItems();
            jComboBox5.setEnabled(false);
        }
        if (niveau.equalsIgnoreCase("Sixième")) {
            jComboBox5.removeAllItems();
            afficheClasseSixieme();
        }
        if (niveau.equalsIgnoreCase("Cinquième")) {
            jComboBox5.removeAllItems();
            afficheClasseCinquième();
        }
        if (niveau.equalsIgnoreCase("Quatrième")) {
            jComboBox5.removeAllItems();
            afficheClasseQuatrième();
        }
        if (niveau.equalsIgnoreCase("Troisième")) {
            jComboBox5.removeAllItems();
            afficheClasseTroisième();
        }
        if (niveau.equalsIgnoreCase("Seconde")) {
            jComboBox5.removeAllItems();
            afficheClasseSeconde();
        }
        if (niveau.equalsIgnoreCase("Première")) {
            jComboBox5.removeAllItems();
            afficheClassePremière();
        }
        if (niveau.equalsIgnoreCase("Terminale")) {
            jComboBox5.removeAllItems();
            afficheClasseTerminale();
        }
    }

    //*************************************METHODE QUI PERMET D'AFFICHER LE CONTENU DE LA BD*************************************************************************************/
    public void afficheClasseSixieme() {
        try {
            String requete = "SELECT classe.nom_class FROm classe "
                    + "WHERE nom_class IN ('6eme M1','6eme M2','6eme M3','6eme M4','6eme M5','6eme M6','6eme M7','6eme M8','6eme M9','6eme M10')";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            int i = 0;
            while (rs.next()) {
                String classe = rs.getString(1);
                jComboBox5.insertItemAt(classe, i);
                i++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }

    //*************************************METHODE QUI PERMET D'AFFICHER LE CONTENU DE LA BD*************************************************************************************/
    public void afficheClasseCinquième() {
        try {
            String requete = "SELECT classe.nom_class FROm classe "
                    + "WHERE nom_class IN ('5eme M1','5eme M2','5eme M3','5eme M4','5eme M5','5eme M6','5eme M7','5eme M8','5eme M9','5eme M10')";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            int i = 0;
            while (rs.next()) {
                String classe = rs.getString(1);
                //jComboBox5.removeAllItems();
                jComboBox5.insertItemAt(classe, i);
                i++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }

    //*************************************METHODE QUI PERMET D'AFFICHER LE CONTENU DE LA BD*************************************************************************************/
    public void afficheClasseQuatrième() {
        try {
            String requete = "SELECT classe.nom_class FROm classe "
                    + "WHERE nom_class IN ('4eme M1','4eme M2','4eme M3','4eme M4','4eme M5','4eme M6','4eme M7','4eme M8','4eme M9','4eme M10')";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            int i = 0;
            while (rs.next()) {
                String classe = rs.getString(1);
                //jComboBox5.removeAllItems();
                jComboBox5.insertItemAt(classe, i);
                i++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }

    //*************************************METHODE QUI PERMET D'AFFICHER LE CONTENU DE LA BD*************************************************************************************/
    public void afficheClasseTroisième() {
        try {
            String requete = "SELECT classe.nom_class FROm classe "
                    + "WHERE nom_class IN ('3eme M1','3eme M2','3eme M3','3eme M4','3eme M5','3eme M6','3eme M7','3eme M8','3eme M9','3eme M10')";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            int i = 0;
            while (rs.next()) {
                String classe = rs.getString(1);
                //jComboBox5.removeAllItems();
                jComboBox5.insertItemAt(classe, i);
                i++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }

    //*************************************METHODE QUI PERMET D'AFFICHER LE CONTENU DE LA BD*************************************************************************************/
    public void afficheClasseSeconde() {
        try {
            String requete = "SELECT classe.nom_class FROm classe "
                    + "WHERE nom_class IN ('2nde A4esp','2nde A4all','2nde A4Esp','2nde A4All','2nde C','2nde C1','2nde C2','2nde C3')";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            int i = 0;
            while (rs.next()) {
                String classe = rs.getString(1);
                //jComboBox5.removeAllItems();
                jComboBox5.insertItemAt(classe, i);
                i++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }

    //*************************************METHODE QUI PERMET D'AFFICHER LE CONTENU DE LA BD*************************************************************************************/
    public void afficheClassePremière() {
        try {
            String requete = "SELECT classe.nom_class FROm classe "
                    + "WHERE nom_class IN ('1ere D','1ere D1','1ere D2','1ere D3','1ere D4','1ere D5','1ere A4esp','1ere A4Esp','1ere A4All','1ere A4all','1ere C','1ere C1','1ere C2','1ere C3','1ere C4')";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            int i = 0;
            while (rs.next()) {
                String classe = rs.getString(1);
                //jComboBox5.removeAllItems();
                jComboBox5.insertItemAt(classe, i);
                i++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }

    //*************************************METHODE QUI PERMET D'AFFICHER LE CONTENU DE LA BD*************************************************************************************/
    public void afficheClasseTerminale() {
        try {
            String requete = "SELECT classe.nom_class FROm classe "
                    + "WHERE nom_class IN ('Tle A4esp','Tle A4Esp','Tle A4All','Tle A4all','Tle D','Tle D1','Tle D2','Tle D3','Tle D4','Tle D5','Tle C','Tle C1','Tle C2','Tle C3','Tle C4','Tle C5','Tle TI')";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            int i = 0;
            while (rs.next()) {
                String classe = rs.getString(1);
                //jComboBox5.removeAllItems();
                jComboBox5.insertItemAt(classe, i);
                i++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }

    //*************************************METHODE QUI PERMET D'AFFICHER LE CONTENU DE LA BD*************************************************************************************/
    public void selectionEleveClasse() {
        try {
            String itemSelectionner = jComboBox5.getSelectedItem() + "";
            String requete = "SELECT classe.nom_class ,eleve.nom_el, eleve.prenom_el from eleve,classe "
                    + "where ((eleve.id_class=classe.id_class) and classe.nom_class='" + itemSelectionner + "')";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            int i = 0;
            while (rs.next()) {
                String nom_class = rs.getString(1);
                String nom_el = rs.getString(2);
                String prenom_el = rs.getString(3);
                jComboBox2.insertItemAt(nom_el, i);
                i++;

                // Object[] val = {nom_class, nom_el, prenom_el};
                // mode.addRow(val);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//******************** insertion des cours***********************************************
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void selectionCours() {
        try {
            String itemSelectionner = jComboBox3.getSelectedItem() + "";
            String itemSelectionner1 = jComboBox5.getSelectedItem() + "";
            String itemSelectionner2 = jComboBox2.getSelectedItem() + "";
            String requete = "SELECT cours.intitule_cours,suivre.note,cours.coef FROM eleve,cours,suivre WHERE ((eleve.mtle=suivre.mtle)and(eleve.nom_el='"+itemSelectionner2+"')) ";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                String matiere = rs.getString(1);
                String note_tempo = rs.getString(2);
                String coef_tempo = rs.getString(3);
                
               int coef=Integer.getInteger(coef_tempo);
               Double note=Double.valueOf(note_tempo);
               Double  noteCoef=(note*coef);
               System.out.println("noteCoef "+noteCoef);
                Object[] val = {matiere, note,coef,noteCoef};
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

        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jComboBox2 = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        jComboBox3 = new javax.swing.JComboBox();
        jPanel10 = new javax.swing.JPanel();
        jComboBox5 = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(204, 204, 255));
        setClosable(true);
        setTitle("Matières et  Notes des élèves");

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SUIVI PEDAGOGIQUE DES ELEVES ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 24))); // NOI18N

        jPanel6.setBackground(new java.awt.Color(255, 255, 204));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "NOTES DES ELEVES", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        jTable1.setBackground(new java.awt.Color(153, 153, 153));
        jTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Matière", "Note/20", "Coef", "Note Coef"
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
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel5.setBackground(new java.awt.Color(255, 255, 204));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Choisir un élève de la classe", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        jPanel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        jComboBox2.setBackground(new java.awt.Color(255, 255, 204));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jComboBox2, 0, 185, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 204));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Choisir un niveau ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        jPanel7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        jComboBox3.setBackground(new java.awt.Color(255, 255, 204));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "*", "Sixième", "Cinquième", "Quatrième", "Troisième", "Seconde", "Première", "Terminale" }));
        jComboBox3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox3MouseClicked(evt);
            }
        });
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jComboBox3, 0, 123, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 204));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sélectinner une classe", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        jPanel10.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        jComboBox5.setBackground(new java.awt.Color(255, 255, 204));
        jComboBox5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox5ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jComboBox5, 0, 167, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jComboBox5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Contacter le parent", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel1.setText("Moyenne");

        jButton1.setBackground(new java.awt.Color(255, 204, 102));
        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton1.setText("Envoyer un sms ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        // TODO add your handling code here:
        //selectionClassesNiveau();
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
        selectionClassesNiveau();
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3MouseClicked

    private void jComboBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox5ItemStateChanged
        // TODO add your handling code here:

        jComboBox2.setEnabled(true);
        jComboBox2.removeAllItems();
        selectionEleveClasse();
    }//GEN-LAST:event_jComboBox5ItemStateChanged

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
        effaceTable(jTable1, mode);
        selectionCours();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
