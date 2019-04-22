/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AfficheEleveParent.java
 *
 * Created on 14 juin 2012, 02:44:48
 */
package sipee;

import java.sql.ResultSet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.smslib.CService;
import org.smslib.handler.CATHandler;

/**
 *
 * @author admine
 */
public class AfficheEleveParent extends javax.swing.JInternalFrame {

    /** Creates new form AfficheEleveParent */
    static CService srv;
    static CATHandler atHandler;
    DefaultComboBoxModel comboDestination = new DefaultComboBoxModel();
    DefaultComboBoxModel comboBailleur = new DefaultComboBoxModel();
    DefaultComboBoxModel comboAnnee = new DefaultComboBoxModel();
    DefaultComboBoxModel comboDefault = new DefaultComboBoxModel();
    static DefaultTableModel mode = new DefaultTableModel();
    static DefaultTableColumnModel dtcm = new DefaultTableColumnModel();

    public AfficheEleveParent() {
        Connector1.initialise();
        initComponents();
        paramTable();
        effaceTable(jTable2, mode);
        selectionElevesParents();
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
    
    //**************FILTRAGE DES CONTACTS*************************************µ*****************µ
    public void filtrerContactsParents() {
        try {
            String saisie = "%" + jTextField3.getText() + "%";
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

    public void selectionUnParentEleve() {
        try {
            String saisie = "%" + jTextField3.getText() + "%";
            String requete = "SELECT eleve.mtle,eleve.nom_el,eleve.prenom_el,parent.nom_parent, parent.tel_parent,parent.prenom_parent,parent.civilite,eleve.photo_el FROM eleve, parent "
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
                //System.out.println("mtle   :" + mtle + "  nom_parent :" + nom_parent + "   tel_el:" + tel_el + "   tel_parent1 :" + tel_parent1 + "  tel_parent2 :" + tel_parent2);

                //jTextField1.setText("+237" + tel_el);
                //jTextField3.setText("+237" + tel_parent1);
                //jTextField5.setText("+237" + tel_parent2);

                //java.io.File filePicture = new java.io.File(photo_el);
                // jLabel8.setIcon(new javax.swing.ImageIcon(filePicture.getAbsolutePath()));



            }
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(232, 233, 224));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        setClosable(true);
        setTitle("LISTE DES PARENTS & ELEVES");

        jPanel7.setBackground(new java.awt.Color(232, 233, 224));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "LISTE DES PARENTS & ELEVES", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 24))); // NOI18N

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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Saisir le nom ou le matricule de l'élève", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 14))); // NOI18N
        jPanel2.setOpaque(false);

        jTextField3.setFont(new java.awt.Font("Times New Roman", 1, 14));
        jTextField3.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Opération", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 1, 15))); // NOI18N
        jPanel5.setOpaque(false);

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
            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 411, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 920, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        //affichageContactParent();
}//GEN-LAST:event_jTable2MousePressed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
       // effaceTable(jTable2, mode);
       // selectionUnParentEleve();
}//GEN-LAST:event_jTextField3KeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            jTable2.print();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur :" + ex.getMessage());
        }
}//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        // TODO add your handling code here:
        effaceTable(jTable2, mode);
        selectionUnParentEleve();
    }//GEN-LAST:event_jTextField3KeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane3;
    public static javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
