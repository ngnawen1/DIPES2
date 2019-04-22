/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AfficheClasse.java
 *
 * Created on 11 mai 2012, 07:13:51
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
public class AfficheClasse extends javax.swing.JInternalFrame {

    /** Creates new form AfficheClasse */
    DefaultComboBoxModel comboDestination = new DefaultComboBoxModel();
    DefaultComboBoxModel comboBailleur = new DefaultComboBoxModel();
    DefaultComboBoxModel comboAnnee = new DefaultComboBoxModel();
    DefaultComboBoxModel comboDefault = new DefaultComboBoxModel();
    static DefaultTableModel mode = new DefaultTableModel();
    static DefaultTableColumnModel dtcm = new DefaultTableColumnModel();

    public AfficheClasse() {
        Connector1.initialise();
        initComponents();
        paramTable();
        clouleurFondEcran();
        this.setVisible(true);
        this.setSize(MainFrame.jDesktopPane1.getWidth() - 50, MainFrame.jDesktopPane1.getHeight() - 50);
        effaceTable(jTable1, mode);
        listeClasse();
    }
    public void clouleurFondEcran(){
        this.setBackground(Color.WHITE);
        jPanel1.setBackground(Color.WHITE);
        jPanel2.setBackground(Color.WHITE);
        jTable1.setBackground(Color.WHITE);
        jTextField3.setBackground(Color.WHITE);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////***********************LISTE DS CLASSES DE L'ETABLISSEMENT****************************************************
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void listeClasse() {
        try {

            String saisie = "%" + jTextField3.getText() + "%";
            String requete = "SELECT classe.id_class,classe.nom_class, classe.prof_princi,classe.capacite FROM classe WHERE (((classe.nom_class) Like '" + saisie.replace("'", "''") + "') OR ((prof_princi) Like '" + saisie.replace("'", "''") + "')) ORDER BY classe.nom_class";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                int id_class = rs.getInt(1);
                String nom_class = rs.getString(2);
                String prof_princi = rs.getString(3);
                String capacite = rs.getString(4);
                Object[] val = {id_class, nom_class, prof_princi, capacite};
                mode.addRow(val);
            }


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }

    /***********************************METHODE QUI PERMET DE PARAMETRER LA TABLE********************************************/
    public static void paramTable() {
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        mode = (DefaultTableModel) jTable1.getModel();
        dtcm = (DefaultTableColumnModel) jTable1.getColumnModel();

        jTable1.setRowHeight(40);
        largeurColoneMax(dtcm, 0, 70);
        //  largeurColoneMax(dtcm, 3, 50);
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

    /************************************METHODE QUI PERMET DE DETERMINER LA LARGEUR MAX D'une COLONE****************************************/
    public static void largeurColoneMax(DefaultTableColumnModel dtcm, int numColumn, int largeur) {
        dtcm.getColumn(numColumn).setMinWidth(largeur);
        dtcm.getColumn(numColumn).setMaxWidth(largeur);
        dtcm.getColumn(numColumn).setPreferredWidth(largeur);
    }

    /*******************************METHODE QUI PERMET DE RETOURNER LE DERNIER ENREGISTREMENT***********************************************/
    public static int retourneMaxNumber() {
        int tmp = 0;
        try {
            String requete = "SELECT Max(classe.id_class) AS MaxClasse FROM classe";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                tmp = rs.getInt(1);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
        return tmp;
    }

    /*********************  METHODE POUR EFFACER LA TABLE ****************/
    public static void effaceTable(JTable table, DefaultTableModel mode) {
        while (table.getRowCount() > 0) {
            mode.removeRow(table.getRowCount() - 1);
        }
    }

    /****************************CREATION D'UN NOUVEL ELEVE**************************/
    public void Nouvelleligne() {
        int maxClasse = retourneMaxNumber() + 1;
        try {
            Connector1.statement.executeUpdate("Insert INTO classe(id_class) VALUES (" + maxClasse + ")");
            Object[] val = {maxClasse, "", ""};
            // effaceTable(jTable1, mode);
            mode.addRow(val);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());

        }

    }

    //***************************SELECTION DU NUMERO DE LA CLASSE*************************/
    public void selectionClasse() {
        try {
            int[] selected = jTable1.getSelectedRows();
            if (selected.length == 0) {
                JOptionPane.showMessageDialog(null, "VEUILLEZ SELECIONNER UN ENREGISTREMENT SVP!", "Selection !", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int reponse = JOptionPane.showConfirmDialog(null, "VOUS AVEZ CHOISI INSCRIRE CET ELEVE  :\n"
                        + "EN CLASSE DE        " + jTable1.getValueAt(jTable1.getSelectedRow(), 1) + "\n"
                        + "ETES-VOUS SUR?", " Alors cliquer sur oui", JOptionPane.YES_NO_OPTION);
                int numero = Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 0) + "");
                if (reponse == JOptionPane.YES_OPTION) {
                    //mode.removeRow(jTable1.getSelectedRow());
                    String requete = "select * FROM classe WHERE id_class = " + numero + "";
                    ResultSet rs = Connector1.statement.executeQuery(requete);
                    while (rs.next()) {
                        int id_class = rs.getInt(1);
                        String nom_class = rs.getString(2);
                        String prof_princi = rs.getString(3);
                        System.out.println("id_class: " + id_class + " nom_class: " + nom_class + " prof_princi: " + prof_princi);
                        InscriptionEleves.jTextField19.setText("" + id_class);
                        //dispose();
                    }
                } else {
                }
                dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
            dispose();
        }
        // dispose();
    }
    
        /*************************************METHODE QUI PERMET D'ENREGISTRER LA LIGNE SELECTIONNEE**********************************************/
    public void enregistrerdonnees() {
        try {
            int i = jTable1.getSelectedRow();
            int ID_CLASS = Integer.parseInt(jTable1.getValueAt(i, 0) + "");
            String NOM_CLASS = jTable1.getValueAt(i, 1) + "";
            String PROF_PRINCI = jTable1.getValueAt(i, 2) + "";
            int capacite = Integer.parseInt(jTable1.getValueAt(i, 3) + "");

            Connector1.statement.executeUpdate("UPDATE classe set  NOM_CLASS ='" + NOM_CLASS.replace("'", "''") + "' WHERE ID_CLASS = " + ID_CLASS + "");
            Connector1.statement.executeUpdate("UPDATE classe set PROF_PRINCI ='" + PROF_PRINCI.replace("'", "''") + "' WHERE ID_CLASS = " + ID_CLASS+ "");
            Connector1.statement.executeUpdate("UPDATE classe set capacite ='" + capacite+ "' WHERE ID_CLASS = " + ID_CLASS + "");
            JOptionPane.showMessageDialog(null, "BRAVO MODIFICATION EFFECTUEE AVEC SUCCES");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }
   /*************/
    public void supprimerenregistrement() {
        try {
            int[] selected = jTable1.getSelectedRows();
            if (selected.length == 0) {
                JOptionPane.showMessageDialog(null, "VEUILLEZ SELECIONNER UN ENREGISTREMENT SVP!", "Selection !", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int reponse = JOptionPane.showConfirmDialog(null, "VOUS AVEZ CHOISIR L'ENREGISTREMENT SUIVANT:\n"
                        + " id_classe      : " + jTable1.getValueAt(jTable1.getSelectedRow(), 0) + "\n"
                        + " Nom_classe     : " + jTable1.getValueAt(jTable1.getSelectedRow(), 1) + "\n"
                        + " capacite_classe: " + jTable1.getValueAt(jTable1.getSelectedRow(), 2) + "\n\n"
                        + "VOULEZ-VOUS VRAIMENT SUPRIMER ?", "Attention suppression!!", JOptionPane.YES_NO_OPTION);
                int numero = Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 0) + "");
                if (reponse == JOptionPane.YES_OPTION) {
                    mode.removeRow(jTable1.getSelectedRow());
                    String requete = "DELETE FROM classe WHERE classe.ID_CLASS="+ numero +"";
                    Connector1.statement.executeUpdate(requete);
                } else {
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(232, 233, 224));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 233, 224), 3));
        setClosable(true);
        setTitle("Liste des classes de l'établissement");
        setEnabled(false);
        setOpaque(true);

        jPanel1.setBackground(new java.awt.Color(232, 233, 224));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "SALLES DE CLASSES", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 24))); // NOI18N
        jPanel1.setOpaque(false);

        jScrollPane1.setBorder(null);

        jTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID_classe", "Nom de la classe", "Professeur principale", "Capacite de la classe"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel2.setBackground(new java.awt.Color(232, 233, 224));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Saisir le nom de la classe ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        jTextField3.setFont(new java.awt.Font("Tahoma", 1, 14));
        jTextField3.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
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
                .addContainerGap()
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 854, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE))
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

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        // TODO add your handling code here:
        effaceTable(jTable1, mode);
        listeClasse();
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
        effaceTable(jTable1, mode);
        listeClasse();
}//GEN-LAST:event_jButton9ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            jTable1.print();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur :" + ex.getMessage());
        }
}//GEN-LAST:event_jButton1ActionPerformed
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
                setBackground(new Color(255,255,255));
            }
            if (isSelected) {
                int nb = 0;
                if (nb <= column) {
                    setBackground(new Color(255,255,102));
                    setForeground(Color.BLUE);
                } else {
                    setBackground(new Color(255,255,102));
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    public static javax.swing.JButton jButton7;
    public static javax.swing.JButton jButton8;
    public static javax.swing.JButton jButton9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
