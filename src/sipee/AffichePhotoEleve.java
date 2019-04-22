/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sipee;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;

public class AffichePhotoEleve extends javax.swing.JFrame {

    private ModèleTableau modèle = new ModèleTableau();
    private JTable tableau = new JTable(modèle);
    private JComboBox extensions = new JComboBox(new String[]{"PNG", "GIF", "JPG"});
    private JToolBar valider = new JToolBar();

    public AffichePhotoEleve() {
        super("Liste des élèves et leurs photos");
        TableColumnModel modèleColonne = tableau.getColumnModel();
        modèleColonne.getColumn(1).setCellEditor(new DefaultCellEditor(extensions));
        tableau.setDefaultRenderer(Long.class, new RenduEntier());
        tableau.setRowHeight(52);
        add(new JScrollPane(tableau));
      //  setClosable(true);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new AffichePhotoEleve();
    }

    private class ModèleTableau extends AbstractTableModel {

        private String[] colonnes = {"Matricule","Nom", "Prenom ", "Sexe","Sexe", "Photo"};
        private File[] fichiers;
        private Object[] lignes;

        public ModèleTableau() {
            Connector1.initialise();
            constituer();
        }

        private void constituer() {
            // String img=null;
            try {
                String requete="select repertoire from listephoto";
                ResultSet rs = Connector1.statement.executeQuery(requete);
                while (rs.next()) {
                    String img = rs.getString(1);
                    System.out.println("" + img);



                    fichiers = new File(img).listFiles(new FileFilter() {

                        public boolean accept(File fichier) {
                            String nom = fichier.getName();
                            String[] découpage = fichier.getName().split("\\.");
                            String extension = découpage[1].toUpperCase();
                            return extension.equals("GIF") || extension.equals("PNG") || extension.equals("JPG");
                        }
                    });
                    lignes = new Object[fichiers.length];
                    for (int i = 0; i < fichiers.length; i++) {
                        File fichier = fichiers[i];
                        String[] découpage = fichier.getName().split("\\.");
                        String extension = découpage[1].toUpperCase();
                        long taille = fichier.length();
                        BufferedImage image = null;
                        Icon icône = null;
                        try {
                            image = ImageIO.read(fichier);
                            icône = new ImageIcon(image.getScaledInstance(-1, 50, Image.SCALE_DEFAULT));
                        } catch (IOException ex) {
                        }
                        lignes[i] = new Object[]{découpage[0], icône, image};
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
            }
        }

        @Override
        public int getRowCount() {
            return fichiers.length;
        }

        @Override
        public int getColumnCount() {
            return colonnes.length;
        }

        @Override
        public Object getValueAt(int ligne, int colonne) {
            Object[] image = (Object[]) lignes[ligne];
            return image[colonne];
        }

        @Override
        public void setValueAt(Object valeur, int ligne, int colonne) {
            Object[] image = (Object[]) lignes[ligne];
            if (colonne == 3) {
                double largeur = (Integer) image[3];
                double hauteur = (Integer) image[4];
                double ratio = (double) largeur / hauteur;
                double récupération = (Integer) valeur;
                image[4] = (int) (récupération / ratio);
                tableau.repaint();
            }
            image[colonne] = valeur;
        }

        @Override
        public Class<?> getColumnClass(int colonne) {
            Object[] première = (Object[]) lignes[0];
            return première[colonne].getClass();
        }

        @Override
        public String getColumnName(int colonne) {
            return colonnes[colonne];
        }

        @Override
        public boolean isCellEditable(int ligne, int colonne) {
            return colonne == 0 || colonne == 1 || colonne == 3;
        }

        public void changerFichier(int ligne) {
            Object[] infos = (Object[]) lignes[ligne];
            BufferedImage image = (BufferedImage) infos[6];
            String nom = (String) infos[0];
            String extension = (String) infos[1];
            int largeur = (Integer) infos[3];
            int hauteur = (Integer) infos[4];
            double ratio = (double) largeur / image.getWidth();
            BufferedImage traitement = new BufferedImage(largeur, hauteur, image.getType());
            AffineTransform retailler = AffineTransform.getScaleInstance(ratio, ratio);
            int interpolation = AffineTransformOp.TYPE_BICUBIC;
            AffineTransformOp retaillerImage = new AffineTransformOp(retailler, interpolation);
            retaillerImage.filter(image, traitement);
            try {
                ImageIO.write(traitement, extension, new File("C:/Photos/" + nom + '.' + extension));
                constituer();
                tableau.revalidate();
            } catch (IOException ex) {
            }
        }
    }

    private class RenduEntier extends DefaultTableCellRenderer {

        public RenduEntier() {
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
            setHorizontalAlignment(RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object valeur, boolean sélectionné, boolean focus, int ligne, int colonne) {
            DecimalFormat décimal = new DecimalFormat("#,##0 octets");
            setText(décimal.format(valeur));
            if (sélectionné) {
                setBackground(tableau.getSelectionBackground());
            } else {
                setBackground(Color.WHITE);
            }
            return this;
        }
    }
}
