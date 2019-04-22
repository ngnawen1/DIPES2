/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AfficheEnseig.java
 *
 * Created on 10 mai 2012, 14:17:09
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
public class AfficheEnseig extends javax.swing.JInternalFrame {

    /** Creates new form AfficheEnseig */
    Connector1 co = new Connector1();
    public static JDialog jdClient;
    DefaultComboBoxModel comboDestination = new DefaultComboBoxModel();
    DefaultComboBoxModel comboBailleur = new DefaultComboBoxModel();
    DefaultComboBoxModel comboAnnee = new DefaultComboBoxModel();
    DefaultComboBoxModel comboDefault = new DefaultComboBoxModel();
    static DefaultTableModel mode = new DefaultTableModel();
    static DefaultTableColumnModel dtcm = new DefaultTableColumnModel();

    public AfficheEnseig() {
        Connector1.initialise();
        initComponents();
        paramTable();
        this.setVisible(true);
        this.setSize(MainFrame.jDesktopPane1.getWidth() - 50, MainFrame.jDesktopPane1.getHeight() - 50);
        effaceTable(jTable2, mode);
        afficheEnseignantInscrits();
    }

    //*************************************METHODE QUI PERMET D'AFFICHER LE CONTENU DE LA BD*************************************************************************************/
    public void afficheEnseignantInscrits() {
        try {

            String saisie = "%" + jTextField3.getText() + "%";
            String requete = "SELECT enseignant.id_ens,enseignant.civilite, enseignant.nom_ens, enseignant.prenom_ens, enseignant.sexe_ens, enseignant.date_naiss_ens, enseignant.lieu_naiss_ens,enseignant.grade_ens, enseignant.responsabilite, enseignant.tel_ens,enseignant.cni_ens "
                    + "FROM enseignant WHERE (((enseignant.nom_ens) Like '" + saisie.replace("'", "''") + "') OR ((enseignant.prenom_ens) Like '" + saisie.replace("'", "''") + "')) ORDER BY enseignant.nom_ens";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                int id_ens = rs.getInt(1);
                String civilite = rs.getString(2);
                String nom_ens = rs.getString(3);
                String prenom_ens = rs.getString(4);
                String sexe_ens = rs.getString(5);
                String date_naiss_ens = rs.getString(6);
                String lieu_naiss_ens = rs.getString(7);
                String grade_ens = rs.getString(8);
                String responsabilite = rs.getString(9);
                String tel_ens = rs.getString(10);
                int cni_ens = rs.getInt(11);
                Object[] val = {"" + cni_ens, civilite, nom_ens, prenom_ens, sexe_ens, date_naiss_ens, lieu_naiss_ens, grade_ens, responsabilite, tel_ens};
                mode.addRow(val);
            }


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }

    /*************************************METHODE QUI PERMET D'ENREGISTRER LA LIGNE SELECTIONNEE**********************************************/
    public void enregistrerdonnees() {
        try {
            int i = jTable2.getSelectedRow();
            int cni_ens = Integer.parseInt(jTable2.getValueAt(i, 0) + "");
            String civilite = jTable2.getValueAt(i, 1) + "";
            String nom_ens = jTable2.getValueAt(i, 2) + "";
            String prenom_ens = jTable2.getValueAt(i, 3) + "";
            String sexe_ens = jTable2.getValueAt(i, 4) + "";
            String date_naiss_ens = jTable2.getValueAt(i, 5) + "";
            String lieu_naiss_ens = jTable2.getValueAt(i, 6) + "";
            String grade_ens = jTable2.getValueAt(i, 7) + "";
            String responsabilite = jTable2.getValueAt(i, 8) + "";
            String tel_ens = jTable2.getValueAt(i, 9) + "";


            Connector1.statement.executeUpdate("UPDATE enseignant set cni_ens ='" + cni_ens + "' WHERE  cni_ens = " + cni_ens + "");
            Connector1.statement.executeUpdate("UPDATE enseignant set civilite='" + civilite.replace("'", "''").toUpperCase() + "' WHERE  cni_ens = " + cni_ens + "");
            Connector1.statement.executeUpdate("UPDATE enseignant set nom_ens ='" + nom_ens.replace("'", "''").toUpperCase() + "' WHERE  cni_ens = " + cni_ens + "");
            Connector1.statement.executeUpdate("UPDATE enseignant set prenom_ens ='" + prenom_ens.replace("'", "''").toUpperCase()  + "' WHERE  cni_ens = " + cni_ens + "");
            Connector1.statement.executeUpdate("UPDATE enseignant set sexe_ens ='" + sexe_ens.replace("'", "''").toUpperCase()  + "' WHERE cni_ens= " + cni_ens + "");
            Connector1.statement.executeUpdate("UPDATE enseignant set date_naiss_ens ='" + date_naiss_ens + "' WHERE  cni_ens = " + cni_ens + "");
            Connector1.statement.executeUpdate("UPDATE enseignant set lieu_naiss_ens ='" + lieu_naiss_ens.replace("'", "''") + "' WHERE  cni_ens = " + cni_ens + "");
            Connector1.statement.executeUpdate("UPDATE enseignant set grade_ens='" + grade_ens.replace("'", "''").toUpperCase()  + "' WHERE  cni_ens = " + cni_ens + "");
            Connector1.statement.executeUpdate("UPDATE enseignant set responsabilite ='" + responsabilite.replace("'", "''") + "' WHERE cni_ens = " + cni_ens + "");
            Connector1.statement.executeUpdate("UPDATE enseignant set tel_ens ='" + tel_ens + "' WHERE cni_ens = " + cni_ens + "");
            JOptionPane.showMessageDialog(null, "BRAVO MODIFICATION EFFECTUEE AVEC SUCCES");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }

    /*************/
    public void supprimerenregistrement() {
        try {
            int[] selected = jTable2.getSelectedRows();
            if (selected.length == 0) {
                JOptionPane.showMessageDialog(null, "VEUILLEZ SELECIONNER UN ENREGISTREMENT SVP!", "Selection !", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int reponse = JOptionPane.showConfirmDialog(null, "VOUS AVEZ CHOISIR L'ENREGISTREMENT SUIVANT:\n"
                        + " CNI-Enseig          : " + jTable2.getValueAt(jTable2.getSelectedRow(), 0) + "\n"
                        + " Nom_Enseig     : " + jTable2.getValueAt(jTable2.getSelectedRow(), 2) + "\n"
                        + " Prenom_Enseig   : " + jTable2.getValueAt(jTable2.getSelectedRow(), 3) + "\n\n"
                        + "VOULEZ-VOUS VRAIMENT SUPRIMER ?", "Attention suppression!!", JOptionPane.YES_NO_OPTION);
                int numero = Integer.parseInt(jTable2.getValueAt(jTable2.getSelectedRow(), 0) + "");
                if (reponse == JOptionPane.YES_OPTION) {
                    mode.removeRow(jTable2.getSelectedRow());
                    String requete = "DELETE FROM enseignant WHERE enseignant.cni_ens =" + numero + "";
                    Connector1.statement.executeUpdate(requete);
                } else {
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }

    /*********************  METHODE POUR EFFACER LA TABLE ****************/
    public static void effaceTable(JTable table, DefaultTableModel mode) {
        while (table.getRowCount() > 0) {
            mode.removeRow(table.getRowCount() - 1);
        }
    }

    ////************************************************************************
    /***********************************METHODE QUI PERMET DE PARAMETRER LA TABLE********************************************/
    public void paramTable() {
        jTable2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        mode = (DefaultTableModel) jTable2.getModel();
        dtcm = (DefaultTableColumnModel) jTable2.getColumnModel();

        jTable2.setRowHeight(40);
        largeurColoneMax(dtcm, 0, 90);
        largeurColoneMax(dtcm, 1, 60);
        largeurColoneMax(dtcm, 4, 40);
        //largeurColoneMax(dtcm, 4, 70);
        largeurColoneMax(dtcm, 7, 70);
        //largeurColoneMax(dtcm, 7, 78);
        largeurColoneMax(dtcm, 9, 90);
        //largeurColoneMax(dtcm, 10, 70);
        //largeurColoneMax(dtcm, 11, 60);
        // largeurColoneMax(dtcm, 13, 75);

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
        jPanel2 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(232, 233, 224));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));
        setClosable(true);
        setTitle("Liste des enseignants enregistrés");
        setOpaque(true);

        jPanel1.setBackground(new java.awt.Color(232, 233, 224));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), " ENSEIGNANTS ENREGISTRES", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Times New Roman", 3, 24))); // NOI18N

        jTable2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable2.setFont(new java.awt.Font("Tahoma", 1, 12));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N˚ CNI", "Civilité", "Noms ", "Prénoms ", "Sexe", "Date naissance", "Lieu de naissance", "Grade", "Poste de responsabilité", "Contact enseignant"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
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

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Nom de l'enseignant rechercher", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        jPanel2.setOpaque(false);

        jTextField3.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jTextField3.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
        );

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
                .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 887, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE))
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

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        // TODO add your handling code here:
        effaceTable(jTable2, mode);
        afficheEnseignantInscrits();
}//GEN-LAST:event_jTextField3KeyTyped

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
        afficheEnseignantInscrits();
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane3;
    public static javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
