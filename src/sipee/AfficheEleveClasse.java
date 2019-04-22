/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AfficheEleveClasse.java
 *
 * Created on 12 mai 2012, 23:07:52
 */
package sipee;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.HeadlessException;
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

/**
 *
 * @author admine
 */
public class AfficheEleveClasse extends javax.swing.JInternalFrame {

    /** Creates new form AfficheEleveClasse */
    Connector1 co = new Connector1();
    public static JDialog jdClient;
    DefaultComboBoxModel comboDestination = new DefaultComboBoxModel();
    DefaultComboBoxModel comboBailleur = new DefaultComboBoxModel();
    DefaultComboBoxModel comboAnnee = new DefaultComboBoxModel();
    DefaultComboBoxModel comboDefault = new DefaultComboBoxModel();
    static DefaultTableModel mode = new DefaultTableModel();
    static DefaultTableColumnModel dtcm = new DefaultTableColumnModel();

    public AfficheEleveClasse() {
        Connector1.initialise();
        initComponents();
        this.setSize(MainFrame.jDesktopPane1.getWidth() - 50, MainFrame.jDesktopPane1.getHeight() - 50);
        paramTable();
        jComboBox1.setBackground(Color.WHITE);
        listeClasse();
        effaceTable(jTable2, mode);
        eleveClasse();
        // eleveUneClasse();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////***********************LISTE DS CLASSES DE L'ETABLISSEMENT****************************************************
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void listeClasse() {
        try {
            String requete = "SELECT classe.nom_class FROM classe";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            int i = 0;
            while (rs.next()) {
                String nom_class = rs.getString(1);
                jComboBox1.insertItemAt(nom_class, i);
                i++;
            }


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////***********************LISTE DES ELEVES PAR CLASSE****************************************************
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void eleveClasse() {
        try {
            //String item = "%" + jTextField1.getText() + "%";
            String requete = "SELECT classe.nom_class,eleve.mtle,eleve.nom_el, eleve.prenom_el, eleve.sexe_el, eleve.date_naiss_el, eleve.lieu_naiss, eleve.redoublant, eleve.email, eleve.solvable, eleve.date_inscription from eleve,classe "
                    + "where (eleve.id_class=classe.id_class) ";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                String nom_class = rs.getString(1);
                String mtle = rs.getString(2);
                String nom_el = rs.getString(3);
                String prenom_el = rs.getString(4);
                String sexe_el = rs.getString(5);
                String date_naiss_el = rs.getString(6);
                String lieu_naiss = rs.getString(7);
                // String tel_el = rs.getString(7);
                String redoublant = rs.getString(8);
                //String serie = rs.getString(9);
                //String diplome = rs.getString(10);
                String email = rs.getString(9);
                //String responsabilite = rs.getString(9);
                String solvable = rs.getString(10);
                String date_inscription = rs.getString(11);
                //String photo_el= rs.getString(18);


                Object[] val = { mtle, nom_el, prenom_el, sexe_el, date_naiss_el, lieu_naiss, redoublant, email, date_inscription, solvable};
                mode.addRow(val);
            }


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////***********************LISTE DES ELEVES D'UNE CLASSE PRECISE****************************************************
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void eleveUneClasse() {
        try {

            String itemSelectionner = jComboBox1.getSelectedItem() + "";
            //String item = "%" + jTextField1.getText() + "%";
            //String item =itemSelectionner.trim();
            //System.out.println(item);
            String requete = "SELECT classe.nom_class,eleve.mtle,eleve.nom_el, eleve.prenom_el, eleve.sexe_el, eleve.date_naiss_el, eleve.lieu_naiss, eleve.redoublant, eleve.email, eleve.solvable, eleve.date_inscription from eleve,classe "
                    + "where ((eleve.id_class=classe.id_class) and classe.nom_class='" + itemSelectionner + "')";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                String nom_class = rs.getString(1);
                String mtle = rs.getString(2);
                String nom_el = rs.getString(3);
                String prenom_el = rs.getString(4);
                String sexe_el = rs.getString(5);
                String date_naiss_el = rs.getString(6);
                String lieu_naiss = rs.getString(7);
                // String tel_el = rs.getString(7);
                String redoublant = rs.getString(8);
                String email = rs.getString(9);
                String solvable = rs.getString(10);
                String date_inscription = rs.getString(11);


                Object[] val = {mtle, nom_el, prenom_el, sexe_el, date_naiss_el, lieu_naiss, redoublant, email, date_inscription, solvable};
                mode.addRow(val);
            }


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }
     public void enregistrerdonnees() {
        try {
            int i = jTable2.getSelectedRow();
            int mtle = Integer.parseInt(jTable2.getValueAt(i, 0) + "");
            String nom_el = jTable2.getValueAt(i, 1) + "";
            String prenom_el = jTable2.getValueAt(i, 2) + "";
            String sexe_el = jTable2.getValueAt(i, 3) + "";
            String date_naiss_el = jTable2.getValueAt(i, 4) + "";
            String lieu_naiss = jTable2.getValueAt(i, 5) + "";
            String redoublant = jTable2.getValueAt(i, 6) + "";
            String email = jTable2.getValueAt(i, 7) + "";
            String date_inscription = jTable2.getValueAt(i, 8) + "";
            String solvable = jTable2.getValueAt(i, 9) + "";


            Connector1.statement.executeUpdate("UPDATE eleve set  nom_el ='" + nom_el.replace("'", "''") + "' WHERE mtle = " + mtle + "");
            Connector1.statement.executeUpdate("UPDATE eleve set prenom_el ='" + prenom_el.replace("'", "''") + "' WHERE mtle = " + mtle + "");
            Connector1.statement.executeUpdate("UPDATE eleve set sexe_el ='" + sexe_el.replace("'", "''") + "' WHERE mtle = " + mtle + "");
            Connector1.statement.executeUpdate("UPDATE eleve set date_naiss_el ='" + date_naiss_el.replace("'", "''") + "' WHERE mtle = " + mtle + "");
            Connector1.statement.executeUpdate("UPDATE eleve set lieu_naiss ='" + lieu_naiss.replace("'", "''") + "' WHERE mtle = " + mtle + "");
            Connector1.statement.executeUpdate("UPDATE eleve set redoublant ='" + redoublant.replace("'", "''") + "' WHERE mtle= " + mtle + "");
            Connector1.statement.executeUpdate("UPDATE eleve set email ='" + email.replace("'", "''") + "' WHERE mtle= " + mtle + "");
            Connector1.statement.executeUpdate("UPDATE eleve set solvable ='" + solvable.replace("'", "''") + "' WHERE mtle= " + mtle + "");
            Connector1.statement.executeUpdate("UPDATE eleve set date_inscription =DEFAULT  WHERE mtle= " + mtle + "");
            JOptionPane.showMessageDialog(null, "BRAVO MODIFICATION EFFECTUEE AVEC SUCCES");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }
     
    public void supprimerenregistrement() {
        try {
            int[] selected = jTable2.getSelectedRows();
            if (selected.length == 0) {
                JOptionPane.showMessageDialog(null, "VEUILLEZ SELECIONNER UN ENREGISTREMENT SVP!", "Selection !", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int reponse = JOptionPane.showConfirmDialog(null, "VOUS AVEZ CHOISIR L'ENREGISTREMENT SUIVANT:\n"
                        + " Matricule          : " + jTable2.getValueAt(jTable2.getSelectedRow(), 0) + "\n"
                        + " Nom_eleve     : " + jTable2.getValueAt(jTable2.getSelectedRow(), 1) + "\n"
                        + " Prenom_eleve  : " + jTable2.getValueAt(jTable2.getSelectedRow(), 2) + "\n\n"
                        + "VOULEZ-VOUS VRAIMENT SUPRIMER ?", "Attention suppression!!", JOptionPane.YES_NO_OPTION);
                int numero = Integer.parseInt(jTable2.getValueAt(jTable2.getSelectedRow(), 0) + "");
                if (reponse == JOptionPane.YES_OPTION) {
                    mode.removeRow(jTable2.getSelectedRow());
                    String requete = "DELETE FROM eleve WHERE eleve.MTLE ="+ numero +"";
                    String requete1 = "DELETE FROM parent WHERE parent.id_parent ="+ numero +"";
                    Connector1.statement.executeUpdate(requete);
                } else {
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }
    
    /***********************************METHODE QUI PERMET DE PARAMETRER LA TABLE********************************************/
    public void paramTable() {
        jTable2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        mode = (DefaultTableModel) jTable2.getModel();
        dtcm = (DefaultTableColumnModel) jTable2.getColumnModel();

        jTable2.setRowHeight(40);
        largeurColoneMax(dtcm, 0, 70);
        largeurColoneMax(dtcm, 3, 40);
        largeurColoneMax(dtcm, 4, 80);
        largeurColoneMax(dtcm, 6, 90);
        largeurColoneMax(dtcm, 9, 70);

        TableCellRenderer tbcProjet = getTableCellRenderer();
        TableCellRenderer tbcProjet2 = getTableHeaderRenderer();
        for (int i = 0; i < jTable2.getColumnCount(); i++) {
            TableColumn tc = jTable2.getColumnModel().getColumn(i);
            tc.setCellRenderer(tbcProjet);
            tc.setHeaderRenderer(tbcProjet2);

        }

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
                setBackground(new Color(255, 204, 204));
            } else {
                setBackground(new Color(255, 255, 255));
            }
            if (isSelected) {
                int nb = 0;
                if (nb <= column) {
                    setBackground(new Color(255, 255, 102));
                    setForeground(Color.BLUE);
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

                // lbl.setBackground(new Color(255, 204, 204));
                lbl.setBackground(new Color(255, 255, 204));
                lbl.setLineWrap(true);
                lbl.setWrapStyleWord(true);
                lbl.setText((String) value);

                return lbl;
            }
        };
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
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();

        setBackground(new java.awt.Color(232, 233, 224));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));
        setClosable(true);
        setTitle("Liste des élèves par classe");

        jPanel1.setBackground(new java.awt.Color(232, 233, 224));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "LISTE DES ELEVES PAR CLASSE", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 30))); // NOI18N

        jTable2.setBackground(new java.awt.Color(153, 153, 153));
        jTable2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable2.setFont(new java.awt.Font("Tahoma", 1, 12));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Matricule", "Noms ", "Prénoms ", "Sexe", "Date naissance", "Lieu de naissance", "Redoublant", "Adresse Email", "Date d'incription", "Solvable"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Opérations", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 15))); // NOI18N
        jPanel5.setOpaque(false);

        jButton7.setBackground(new java.awt.Color(255, 255, 153));
        jButton7.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jButton7.setText("Valider la ligne modifiée");
        jButton7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(255, 255, 153));
        jButton8.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jButton8.setText("Supprimer une Ligne");
        jButton8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(255, 255, 153));
        jButton9.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jButton9.setText("Rafraîchir la table");
        jButton9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 255, 153));
        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/printer2.png"))); // NOI18N
        jButton1.setText("Imprimer");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.setBackground(new java.awt.Color(232, 233, 224));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Choisir une classe", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        jComboBox1.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jComboBox1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jComboBox1.setOpaque(false);
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBox1, 0, 148, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 126, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 905, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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
}//GEN-LAST:event_jTable2MousePressed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        effaceTable(jTable2, mode);
        eleveUneClasse();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        enregistrerdonnees();
}//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        supprimerenregistrement();
}//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        effaceTable(jTable2, mode);
        listeClasse();
}//GEN-LAST:event_jButton9ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            jTable2.print();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur :" + ex.getMessage());
        }
}//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    public static javax.swing.JButton jButton7;
    public static javax.swing.JButton jButton8;
    public static javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane3;
    public static javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
