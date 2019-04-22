/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * HistoriqueEnseignantContacter.java
 *
 * Created on 13 mai 2012, 14:39:42
 */
package sipee;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.sql.ResultSet;
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
public class HistoriqueEnseignantContacter extends javax.swing.JInternalFrame {

    /** Creates new form HistoriqueEnseignantContacter */
    DefaultComboBoxModel comboDestination = new DefaultComboBoxModel();
    DefaultComboBoxModel comboBailleur = new DefaultComboBoxModel();
    DefaultComboBoxModel comboAnnee = new DefaultComboBoxModel();
    DefaultComboBoxModel comboDefault = new DefaultComboBoxModel();
    static DefaultTableModel mode = new DefaultTableModel();
    static DefaultTableColumnModel dtcm = new DefaultTableColumnModel();

    public HistoriqueEnseignantContacter() {
        Connector1.initialise();
        initComponents();
        paramTable();
        this.setVisible(true);
        this.setSize(MainFrame.jDesktopPane1.getWidth() - 50, MainFrame.jDesktopPane1.getHeight() - 50);
        effaceTable(jTable1, mode);
        selectionHistoriqueMsg();

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //***********SELECTION EXPEDITEUR MESSAGES AFIN DE TRACER L'HISTORIQUES DES ENVOIS******************************************************************
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void selectionHistoriqueMsg() {
        try {
            String requete = "SELECT historique_envoi_ens.id_histo_ens, historique_envoi_ens.exp , historique_envoi_ens.date_heure, historique_envoi_ens.sujet, historique_envoi_ens.tel_contacte,historique_envoi_ens.contenu_message FROM historique_envoi_ens";
            ResultSet rs = Connector1.statement.executeQuery(requete);
            while (rs.next()) {
                String id_historique = rs.getString(1);
                String exp = rs.getString(2);
                String date_heure = rs.getString(3);
                String sujet = rs.getString(4);
                String tel_dest = rs.getString(5);
                String contenu_message = rs.getString(6);
                String[] val = {exp, date_heure, sujet, tel_dest, contenu_message};
                mode.addRow(val);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }

    }

    /***********************************METHODE QUI PERMET DE PARAMETRER LA TABLE********************************************/
    public void paramTable() {
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        mode = (DefaultTableModel) jTable1.getModel();
        dtcm = (DefaultTableColumnModel) jTable1.getColumnModel();

        jTable1.setRowHeight(40);
        largeurColoneMax(dtcm, 0, 150);
        largeurColoneMax(dtcm, 1, 100);
        largeurColoneMax(dtcm, 2, 150);
        largeurColoneMax(dtcm, 3, 100);
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

    //***************************SELECTION DU NUMERO DE LA CLASSE*************************/
    public void selectionParents() {
        try {
            int[] selected = jTable1.getSelectedRows();
            if (selected.length == 0) {
                JOptionPane.showMessageDialog(null, "VEUILLEZ SELECIONNER UN ENREGISTREMENT SVP!", "Selection !", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int reponse = JOptionPane.showConfirmDialog(null, "VOUS AVEZ SELECTIONNE LE NUMERO  :\n"
                        + "               " + jTable1.getValueAt(jTable1.getSelectedRow(), 3) + "\n"
                        + "VOULEZ-VOUS CONNAITRE LE NOM DU PARENT ?", " Alors cliquer sur oui", JOptionPane.YES_NO_OPTION);
                String telephone = jTable1.getValueAt(jTable1.getSelectedRow(), 3) + "";
                if (reponse == JOptionPane.YES_OPTION) {
                    String requete = "select parent.tel_parent,parent.nom_parent,parent.prenom_parent FROM parent WHERE tel_parent= " + telephone + "";
                    ResultSet rs = Connector1.statement.executeQuery(requete);
                    while (rs.next()) {
                        String phone_parent = rs.getString(1);
                        String nom_parent = rs.getString(2);
                        String prenon_parent = rs.getString(3);

                        System.out.println("phone_parent: " + phone_parent + " nom_parent: " + nom_parent + " prenon_parent: " + prenon_parent);
                    }
                    dispose();
                    AfficheParentPhone APP = new AfficheParentPhone();
                    MainFrame.centrerDsFrame(APP, MainFrame.jDesktopPane1);
                    MainFrame.jDesktopPane1.add(APP);
                    //jPanel2.validate();
                    APP.setVisible(true);

                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
            dispose();
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
                setBackground(new Color(255, 255, 255));
            } else {
                setBackground(new Color(240, 240, 240));
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

        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        setClosable(true);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "MESSAGES ENVOYES AUX ENSEIGNANTS", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 24))); // NOI18N

        jTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Expediteur", "Date & Heure  d'envoi", "Objet du message", "Numero téléphone contacté", "Contenu du message"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setOpaque(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable1MousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jButton3.setBackground(new java.awt.Color(255, 255, 153));
        jButton3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/printer2.png"))); // NOI18N
        jButton3.setText("Imprimer la liste");
        jButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed
        // TODO add your handling code here:
        //selectionParents();
}//GEN-LAST:event_jTable1MousePressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        try {
            jTable1.print();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur :" + ex.getMessage());
        }
    }//GEN-LAST:event_jButton3ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
