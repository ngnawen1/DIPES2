package sipee;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author vermon
 */
public class MainFrame extends javax.swing.JFrame {

    public static final int x = (int) Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int y = (int) Toolkit.getDefaultToolkit().getScreenSize().height;
    Toolkit kit = Toolkit.getDefaultToolkit();
    private Image icône;

    public MainFrame() {
        //this.setUndecorated(true);
        setContentPane(new PanneauImage("/images/im_salon_ing1.png"));
        initComponents();
        pleinEcran(this);
        inactifMenu();
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                int ret = JOptionPane.showConfirmDialog(null, "VOULEZ-VOUS VRAIMENT QUITTER ?", "CONFIRMATION", JOptionPane.YES_NO_OPTION);
                if (ret == JOptionPane.YES_OPTION) {
                    try {
                        System.exit(0);
                    } catch (Exception ex) {
                        System.out.println("ERREUR : database.close()" + ex);

                    }



                }
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
        icône = kit.getImage(getClass().getResource("/images/icone2.gif"));
        setIconImage(icône);
    }
    
    ///////////////
    public static void desactiverBoutons(){
        AfficheEleve.jButton7.setEnabled(false);
        AfficheEleve.jButton8.setEnabled(false);
        AfficheEleve.jButton9.setEnabled(false);
    }

    public static void desactiverConnexion() {
        jMenuItem3.setEnabled(false);
    }

    ///METHODE D'ACTIVATION DES BOUTON DE CONNEXION*********************************
    public static void activerConnexion() {
        jMenuItem3.setEnabled(true);
    }

    //// METHODE PERMETTANT DE RENDRE INACTIF LES MENU*****************************************
    public static void inactifMenu() {
        jMenuItem8.setEnabled(false);
        jMenu2.setEnabled(false);
        jMenu3.setEnabled(false);
        jMenu4.setEnabled(false);
        jMenu5.setEnabled(false);
        jMenu7.setEnabled(false);
    }
        //// METHODE PERMETTANT DE RENDRE INACTIF LES MENU*****************************************
    public static void inactif2Menu() {
        jMenu5.setEnabled(false);
        jMenu7.setEnabled(false);
    }
     public static void actif2Menu() {
        jMenu5.setEnabled(true);
        jMenu7.setEnabled(true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //// METHODE PERMETTANT DE RENDRE ACTIF LES MENU*****************************************
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void activerMenu() {
        jMenuItem7.setEnabled(true);
        jMenuItem8.setEnabled(true);

        jMenu2.setEnabled(true);
        jMenu3.setEnabled(true);
        jMenu4.setEnabled(true);
        jMenu5.setEnabled(true);
        jMenu7.setEnabled(true);

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /********************************METHODE QUI PERMET DE METTRE LA FENETRE PRINCIPALE EN PLEIN ECRAN***************************/
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void pleinEcran(JFrame fenetre) {
        int Xf = (int) fenetre.getSize().getWidth();
        int Yf = (int) fenetre.getSize().getHeight();
        fenetre.setBounds(0, 0, x, y - 30);
        // fenetre.setLocation(x,y);
        //  fenetre.setVisible(true);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*****************************METHODE QUI PERMET DE CENTRER LA JINTERNAL FRAME DANS LA FENETRE PRINCIPALE ***/
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void centrerDsFrame(JInternalFrame JIF, JDesktopPane JP) {

        int xJP = (int) JP.getSize().getWidth();
        int yJP = (int) JP.getSize().getHeight();
        int xJIF = (int) JIF.getSize().getWidth();
        int yJIF = (int) JIF.getSize().getHeight();
        //JIF.setSize(xJP-50,yJP-50);
        JIF.setLocation(xJP / 2 - xJIF / 2, yJP / 2 - yJIF / 2);
        //  JIF.setLocation(xJP/2-(xJP-50)/2,yJP/2-(yJP-50)/2);
        //JIF.setLocation(xJP/2-(xJP)/2,yJP/2-(yJP)/2);
        JIF.setVisible(true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////************* METHOD PERMETTANT DE QUITTER UNE BOITE DE DIALOGUE*******************************
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void quitterBoiteApplication() {
        // TODO add your handling code here:
        int ret = JOptionPane.showConfirmDialog(null, "VOULEZ-VOUS VRAIMENT QUITTER ?",
                "CONFIRMATION", JOptionPane.YES_NO_OPTION);
        if (ret == JOptionPane.YES_OPTION) {
            try {
                //Connector1.closeConnection();
                System.exit(0);
            } catch (Exception ex) {
                System.out.println("ERREUR : database.close()" + ex);

            }

        } else {
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            this.setVisible(true);
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////*************METHODE PERMETTANT DE ME DECONNECTER DE L'APPLICATION*******************************
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void deconnecterApplication() {
        // TODO add your handling code here:
        Date date = new Date();
        String format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date);
        int ret = JOptionPane.showConfirmDialog(null, "VOULEZ-VOUS VRAIMENT VOUS DECONNECTEZ ?",
                "DECONNEXION", JOptionPane.YES_NO_OPTION);
        if (ret == JOptionPane.YES_OPTION) {
            try {
                String modeCon = ConnectionAuSysteme.jComboBox1.getSelectedItem() + "";
                System.out.println("modeCon===========" + modeCon);
                switch (modeCon) {
                    case "Proviseur": {
                        Connector1.statement.executeUpdate("UPDATE ADMIN set  date_deconnexion ='" + format + "'  WHERE responsabilite='Proviseur'");
                        break;
                    }
                    case "Censeur": {
                        Connector1.statement.executeUpdate("UPDATE ADMIN set  date_deconnexion ='" + format + "'  WHERE responsabilite='Censeur'");
                        break;
                    }
                    case "Censeur 1": {
                        Connector1.statement.executeUpdate("UPDATE ADMIN set  date_deconnexion ='" + format + "'  WHERE responsabilite='Censeur 1'");
                        break;
                    }
                    case "Censeur 2": {
                        Connector1.statement.executeUpdate("UPDATE ADMIN set  date_deconnexion ='" + format + "'  WHERE responsabilite='Censeur 2'");
                        break;
                    }
                    case "Censeur 3": {
                        Connector1.statement.executeUpdate("UPDATE ADMIN set  date_deconnexion ='" + format + "'  WHERE responsabilite='Censeur 3'");
                        break;
                    }
                    case "Censeur 4": {
                        Connector1.statement.executeUpdate("UPDATE ADMIN set  date_deconnexion ='" + format + "'  WHERE responsabilite='Censeur 4'");
                        break;
                    }
                    case "Secretaire": {
                        Connector1.statement.executeUpdate("UPDATE ADMIN set  date_deconnexion ='" + format + "'  WHERE responsabilite='Secretaire'");
                        break;
                    }
                    case "Intendant": {
                        Connector1.statement.executeUpdate("UPDATE ADMIN set  date_deconnexion ='" + format + "'  WHERE responsabilite='Intendant'");
                        break;
                    }
                    case "Comptable matière": {
                        Connector1.statement.executeUpdate("UPDATE ADMIN set  date_deconnexion ='" + format + "'  WHERE responsabilite='Comptable matière'");
                        break;
                    }
                    case "Surveillant générale": {
                        Connector1.statement.executeUpdate("UPDATE ADMIN set  date_deconnexion ='" + format + "'  WHERE responsabilite='Surveillant générale'");
                        break;
                    }
                    case "Surveillant générale 1": {
                        Connector1.statement.executeUpdate("UPDATE ADMIN set  date_deconnexion ='" + format + "'  WHERE responsabilite='Surveillant générale 1'");
                        break;
                    }
                    case "Surveillant générale 2": {
                        Connector1.statement.executeUpdate("UPDATE ADMIN set  date_deconnexion ='" + format + "'  WHERE responsabilite='Surveillant générale 2'");
                        break;
                    }
                    case "Surveillant générale 3": {
                        Connector1.statement.executeUpdate("UPDATE ADMIN set  date_deconnexion ='" + format + "'  WHERE responsabilite='Surveillant générale 3'");
                        break;
                    }
                    case "Surveillant générale 4": {
                        Connector1.statement.executeUpdate("UPDATE ADMIN set  date_deconnexion ='" + format + "'  WHERE responsabilite='Surveillant générale 4'");
                        break;
                    }

                    case "Conseillé d'Orientation": {
                        Connector1.statement.executeUpdate("UPDATE ADMIN set  date_deconnexion ='" + format + "'  WHERE responsabilite='Conseillé d''Orientation'");
                        break;
                    }

                }

            } catch (Exception ex) {
                System.out.println("ERREUR : database.close()" + ex);

            }
            inactifMenu();
            activerConnexion();

        } else {
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            this.setVisible(true);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDesktopPane1 = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem8 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItem7 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        jMenuItem14 = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenu10 = new javax.swing.JMenu();
        jMenuItem27 = new javax.swing.JMenuItem();
        jSeparator12 = new javax.swing.JPopupMenu.Separator();
        jMenuItem9 = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        jMenuItem19 = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator15 = new javax.swing.JPopupMenu.Separator();
        jMenuItem12 = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem20 = new javax.swing.JMenuItem();
        jSeparator17 = new javax.swing.JPopupMenu.Separator();
        jMenuItem10 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem13 = new javax.swing.JMenuItem();
        jSeparator18 = new javax.swing.JPopupMenu.Separator();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem15 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem45 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMenuItem46 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SIPEEsoft version1.0");

        jDesktopPane1.setBackground(new java.awt.Color(232, 233, 224));
        jDesktopPane1.setOpaque(false);

        jMenuBar1.setBackground(new java.awt.Color(232, 233, 224));
        jMenuBar1.setBorder(new javax.swing.border.MatteBorder(null));
        jMenuBar1.setMaximumSize(new java.awt.Dimension(215, 40));
        jMenuBar1.setPreferredSize(new java.awt.Dimension(287, 40));

        jMenu1.setBackground(new java.awt.Color(232, 233, 224));
        jMenu1.setBorder(null);
        jMenu1.setText("FICHIER");
        jMenu1.setBorderPainted(true);
        jMenu1.setFocusCycleRoot(true);
        jMenu1.setFocusPainted(true);
        jMenu1.setFont(new java.awt.Font("Arial Black", 3, 12));
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });
        jMenu1.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                jMenu1MenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });

        jMenuItem3.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem3.setFont(new java.awt.Font("Arial Black", 3, 11));
        jMenuItem3.setText("Connexion");
        jMenuItem3.setBorder(null);
        jMenuItem3.setFocusCycleRoot(true);
        jMenuItem3.setFocusPainted(true);
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jSeparator3.setBackground(new java.awt.Color(0, 0, 0));
        jMenu1.add(jSeparator3);

        jMenuItem8.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem8.setFont(new java.awt.Font("Arial Black", 3, 11));
        jMenuItem8.setText("Deconnecter");
        jMenuItem8.setBorder(null);
        jMenuItem8.setFocusCycleRoot(true);
        jMenuItem8.setFocusPainted(true);
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);

        jSeparator4.setBackground(new java.awt.Color(0, 0, 0));
        jMenu1.add(jSeparator4);

        jMenuItem7.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem7.setFont(new java.awt.Font("Arial Black", 3, 11));
        jMenuItem7.setText("Redemarer");
        jMenuItem7.setBorder(null);
        jMenuItem7.setFocusCycleRoot(true);
        jMenuItem7.setFocusPainted(true);
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jSeparator5.setBackground(new java.awt.Color(0, 0, 0));
        jMenu1.add(jSeparator5);

        jMenuItem11.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem11.setFont(new java.awt.Font("Arial Black", 3, 11));
        jMenuItem11.setText("Quitter");
        jMenuItem11.setBorder(null);
        jMenuItem11.setFocusCycleRoot(true);
        jMenuItem11.setFocusPainted(true);
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenuItem11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                VK_ESCAPE(evt);
            }
        });
        jMenu1.add(jMenuItem11);

        jMenuBar1.add(jMenu1);

        jMenu4.setBackground(java.awt.Color.white);
        jMenu4.setBorder(null);
        jMenu4.setText("ADMINISTRATION");
        jMenu4.setBorderPainted(true);
        jMenu4.setFont(new java.awt.Font("Arial Black", 3, 12));

        jMenu9.setBackground(new java.awt.Color(232, 233, 224));
        jMenu9.setBorder(null);
        jMenu9.setText("Inscription");
        jMenu9.setFont(new java.awt.Font("Arial Black", 3, 11));

        jMenuItem1.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem1.setFont(new java.awt.Font("Arial Black", 3, 11));
        jMenuItem1.setText("Enregister un nouvel élève");
        jMenuItem1.setBorder(null);
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed1(evt);
            }
        });
        jMenu9.add(jMenuItem1);

        jSeparator9.setBackground(new java.awt.Color(0, 0, 0));
        jMenu9.add(jSeparator9);

        jMenuItem14.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem14.setFont(new java.awt.Font("Arial Black", 3, 11));
        jMenuItem14.setText("Enregistrer une nouvelle classe");
        jMenuItem14.setBorder(null);
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem14);

        jSeparator10.setBackground(new java.awt.Color(0, 0, 0));
        jMenu9.add(jSeparator10);

        jMenuItem4.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem4.setFont(new java.awt.Font("Arial Black", 3, 11));
        jMenuItem4.setText("Enregistrer un nouvel enseignant");
        jMenuItem4.setBorder(null);
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem4);

        jMenu4.add(jMenu9);

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jMenu4.add(jSeparator1);

        jMenu10.setBackground(new java.awt.Color(232, 233, 224));
        jMenu10.setBorder(null);
        jMenu10.setText("Affichage");
        jMenu10.setFont(new java.awt.Font("Arial Black", 3, 11));

        jMenuItem27.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem27.setFont(new java.awt.Font("Arial Black", 3, 11)); // NOI18N
        jMenuItem27.setText("Liste des élèves inscrits ");
        jMenuItem27.setBorder(null);
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem27);

        jSeparator12.setBackground(new java.awt.Color(0, 0, 0));
        jMenu10.add(jSeparator12);

        jMenuItem9.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem9.setFont(new java.awt.Font("Arial Black", 3, 11)); // NOI18N
        jMenuItem9.setText("Liste des parents enregistrés");
        jMenuItem9.setBorder(null);
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem9);

        jSeparator13.setBackground(new java.awt.Color(0, 0, 0));
        jMenu10.add(jSeparator13);

        jMenuItem19.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem19.setFont(new java.awt.Font("Arial Black", 3, 11)); // NOI18N
        jMenuItem19.setText("Liste des enseignants enregistrés");
        jMenuItem19.setBorder(null);
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem19);

        jSeparator14.setBackground(new java.awt.Color(0, 0, 0));
        jMenu10.add(jSeparator14);

        jMenuItem2.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem2.setFont(new java.awt.Font("Arial Black", 3, 11)); // NOI18N
        jMenuItem2.setText("Liste des classes de l'établissement");
        jMenuItem2.setBorder(null);
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem2);

        jSeparator15.setBackground(new java.awt.Color(0, 0, 0));
        jMenu10.add(jSeparator15);

        jMenuItem12.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem12.setFont(new java.awt.Font("Arial Black", 3, 11)); // NOI18N
        jMenuItem12.setText("Liste des élèves par classe");
        jMenuItem12.setBorder(null);
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem12);
        jMenu10.add(jSeparator8);

        jMenuItem17.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem17.setFont(new java.awt.Font("Arial Black", 3, 11)); // NOI18N
        jMenuItem17.setText("Liste des parents & élèves");
        jMenuItem17.setBorder(null);
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem17);

        jMenu4.add(jMenu10);

        jMenuBar1.add(jMenu4);

        jMenu3.setBackground(new java.awt.Color(232, 233, 224));
        jMenu3.setBorder(null);
        jMenu3.setText("ENVOI SMS");
        jMenu3.setBorderPainted(true);
        jMenu3.setFont(new java.awt.Font("Arial Black", 3, 12));

        jMenuItem20.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem20.setFont(new java.awt.Font("Arial Black", 3, 11));
        jMenuItem20.setText("SMS à un seul parent");
        jMenuItem20.setBorder(null);
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem20);

        jSeparator17.setBackground(new java.awt.Color(0, 0, 0));
        jMenu3.add(jSeparator17);

        jMenuItem10.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem10.setFont(new java.awt.Font("Arial Black", 3, 11));
        jMenuItem10.setText("SMS à un enseignant");
        jMenuItem10.setBorder(null);
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem10);

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenu3.add(jSeparator2);

        jMenuItem13.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem13.setFont(new java.awt.Font("Arial Black", 3, 11));
        jMenuItem13.setText("SMS à plusieurs parents");
        jMenuItem13.setBorder(null);
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem13);

        jSeparator18.setBackground(new java.awt.Color(0, 0, 0));
        jMenu3.add(jSeparator18);

        jMenuItem24.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem24.setFont(new java.awt.Font("Arial Black", 3, 11));
        jMenuItem24.setText("SMS à plusieurs enseignants");
        jMenuItem24.setBorder(null);
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem24);

        jMenuBar1.add(jMenu3);

        jMenu2.setBackground(new java.awt.Color(232, 233, 224));
        jMenu2.setBorder(null);
        jMenu2.setText("HISTORIQUE ENVOIS");
        jMenu2.setBorderPainted(true);
        jMenu2.setFont(new java.awt.Font("Arial Black", 3, 12));

        jMenuItem15.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem15.setFont(new java.awt.Font("Arial Black", 3, 11));
        jMenuItem15.setText("Parents contactés");
        jMenuItem15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem15);

        jSeparator6.setBackground(new java.awt.Color(0, 0, 0));
        jMenu2.add(jSeparator6);

        jMenuItem16.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem16.setFont(new java.awt.Font("Arial Black", 3, 11));
        jMenuItem16.setText("Enseignants contactés ");
        jMenuItem16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem16);

        jMenuBar1.add(jMenu2);

        jMenu5.setBackground(new java.awt.Color(232, 233, 224));
        jMenu5.setBorder(null);
        jMenu5.setText("PARAMETRAGE");
        jMenu5.setBorderPainted(true);
        jMenu5.setFont(new java.awt.Font("Arial Black", 3, 12));

        jMenuItem6.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem6.setFont(new java.awt.Font("Arial Black", 3, 11));
        jMenuItem6.setText("Changer le mot de passe");
        jMenuItem6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem6);

        jMenuBar1.add(jMenu5);

        jMenu7.setBackground(new java.awt.Color(232, 233, 224));
        jMenu7.setBorder(null);
        jMenu7.setText("STATISTIQUES");
        jMenu7.setBorderPainted(true);
        jMenu7.setFont(new java.awt.Font("Arial Black", 3, 12));

        jMenuItem5.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem5.setFont(new java.awt.Font("Arial Black", 3, 11));
        jMenuItem5.setText("Statistique connexion");
        jMenuItem5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem5);

        jMenuBar1.add(jMenu7);

        jMenu6.setBackground(new java.awt.Color(232, 233, 224));
        jMenu6.setBorder(null);
        jMenu6.setText("?");
        jMenu6.setBorderPainted(true);
        jMenu6.setFont(new java.awt.Font("Arial Black", 3, 12));

        jMenuItem45.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem45.setFont(new java.awt.Font("Arial Black", 3, 11));
        jMenuItem45.setText("A propos");
        jMenuItem45.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jMenuItem45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem45ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem45);

        jSeparator7.setBackground(new java.awt.Color(0, 0, 0));
        jMenu6.add(jSeparator7);

        jMenuItem46.setBackground(new java.awt.Color(232, 233, 224));
        jMenuItem46.setFont(new java.awt.Font("Arial Black", 3, 11));
        jMenuItem46.setText("Guide d'utilisation du logiciel");
        jMenuItem46.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jMenuItem46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem46ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem46);

        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 998, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:ConnectionAuSysteme() 
        //new ListeElevesInscrits().setVisible(true);
        ConnectionAuSysteme paraCon = new ConnectionAuSysteme();
        centrerDsFrame(paraCon, jDesktopPane1);
        jDesktopPane1.add(paraCon);
        //jPanel2.validate();
        paraCon.setVisible(true);
}//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        deconnecterApplication();

}//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        new MainFrame().setVisible(true);
}//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        // TODO add your handling code here:

        quitterBoiteApplication();
}//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        // TODO add your handling code here:
        // JOptionPane.showMessageDialog(null, "Merci d'avoir utiliser SIPEsoft pour envoyer les Sms. Cliquez sur Ok pour Sortir", "Quitter L'application", JOptionPane.INFORMATION_MESSAGE);
        //System.exit(0);
}//GEN-LAST:event_jMenu1ActionPerformed

    private void jMenu1MenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_jMenu1MenuKeyPressed
        // TODO add your handling code here:
}//GEN-LAST:event_jMenu1MenuKeyPressed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:EnregistrerEnseig
        // new InscriptionEleve1(null, true).setVisible(true);
}//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        // TODO add your handling code here:
        //  new NouvelleClasse(null, true).setVisible(true);
        InscriptionClasse ICl = new InscriptionClasse();
        centrerDsFrame(ICl, jDesktopPane1);
        jDesktopPane1.add(ICl);
        //jPanel2.validate();
        ICl.setVisible(true);
}//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        // new EnregistrerEnseig(null, true).setVisible(true);
        InscriptionEnseig IEn = new InscriptionEnseig();
        centrerDsFrame(IEn, jDesktopPane1);
        jDesktopPane1.add(IEn);
        //jPanel2.validate();
        IEn.setVisible(true);
}//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed

        AfficheEleve CQ = new AfficheEleve();
        centrerDsFrame(CQ, jDesktopPane1);
        jDesktopPane1.add(CQ);
        //jPanel2.validate();
        CQ.setVisible(true);
        //dispose();


}//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        // TODO add your handling code here:
        //new ParentsEleves(null, true).setVisible(true);
}//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        // TODO add your handling code here:
        // new EnseignantEnreg(null, true).setVisible(true);
        AfficheEnseig AEn = new AfficheEnseig();
        centrerDsFrame(AEn, jDesktopPane1);
        jDesktopPane1.add(AEn);
        AEn.setVisible(true);
        //jDesktopPane1.validate();
}//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        //new ClasseListe(null, true).setVisible(true);
        AfficheClasse Acl = new AfficheClasse();
        centrerDsFrame(Acl, jDesktopPane1);
        jDesktopPane1.add(Acl);
        Acl.setVisible(true);
        //jDesktopPane1.validate();
}//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        // TODO add your handling code here:
        // new MessagesSend(null, true).setVisible(true);
        MessagePourUnParent msgp = new MessagePourUnParent();
        centrerDsFrame(msgp, jDesktopPane1);
        jDesktopPane1.add(msgp);
        //jDesktopPane1.validate();
        msgp.setVisible(true);
}//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        // TODO add your handling code here:
        // new MessagesSendParentPlus(null, true).setVisible(true);
        MessagePourPlusParent msgpp = new MessagePourPlusParent();
        centrerDsFrame(msgpp, jDesktopPane1);
        jDesktopPane1.add(msgpp);
        //jDesktopPane1.validate();
        msgpp.setVisible(true);
}//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        // TODO add your handling code here:MessagesSendEnseig
        // new MessagesSendEnseig(null, true).setVisible(true);
        MessagePourUnEnseig msge = new MessagePourUnEnseig();
        centrerDsFrame(msge, jDesktopPane1);
        jDesktopPane1.add(msge);
        //jDesktopPane1.validate();
        msge.setVisible(true);
}//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        // TODO add your handling code here:
        //new MessagesSendENseigPlus(null, true).setVisible(true);
        MessagePourPlusEnseig msgpe = new MessagePourPlusEnseig();
        centrerDsFrame(msgpe, jDesktopPane1);
        jDesktopPane1.add(msgpe);
        //jDesktopPane1.validate();
        msgpe.setVisible(true);
}//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jMenuItem1ActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed1
        // TODO add your handling code here:
        InscriptionEleves IEl = new InscriptionEleves();
        centrerDsFrame(IEl, jDesktopPane1);
        jDesktopPane1.add(IEl);
        IEl.setVisible(true);
        ///jLabel1.setVisible(true);
        //jDesktopPane1.validate();
    }//GEN-LAST:event_jMenuItem1ActionPerformed1

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // TODO add your handling code here:
        AfficheParent AP = new AfficheParent();
        centrerDsFrame(AP, jDesktopPane1);
        jDesktopPane1.add(AP);
        //jDesktopPane1.validate();
        AP.setVisible(true);

    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        MessagePourUnEnseig msge = new MessagePourUnEnseig();
        centrerDsFrame(msge, jDesktopPane1);
        jDesktopPane1.add(msge);
        //jDesktopPane1.validate();
        msge.setVisible(true);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        // TODO add your handling code here:
        AfficheEleveClasse AEC = new AfficheEleveClasse();
        centrerDsFrame(AEC, jDesktopPane1);
        jDesktopPane1.add(AEC);
        //jDesktopPane1.validate();
        AEC.setVisible(true);
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        // TODO add your handling code here:
        HistoriqueParentContacter HPC = new HistoriqueParentContacter();
        centrerDsFrame(HPC, jDesktopPane1);
        jDesktopPane1.add(HPC);
        //jDesktopPane1.validate();
        HPC.setVisible(true);
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        // TODO add your handling code here:
        HistoriqueEnseignantContacter HEC = new HistoriqueEnseignantContacter();
        centrerDsFrame(HEC, jDesktopPane1);
        jDesktopPane1.add(HEC);
        //jDesktopPane1.validate();
        HEC.setVisible(true);
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        // TODO add your handling code here:
        MessagePourPlusParent MPP = new MessagePourPlusParent();
        centrerDsFrame(MPP, jDesktopPane1);
        jDesktopPane1.add(MPP);
        //jDesktopPane1.validate();
        MPP.setVisible(true);
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        // TODO add your handling code here:
        AfficheEnseig AE = new AfficheEnseig();
        centrerDsFrame(AE, jDesktopPane1);
        jDesktopPane1.add(AE);
        //jPanel2.validate();
        AE.setVisible(true);
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem45ActionPerformed
        // TODO add your handling code here:
        Apropos apropos = new Apropos();
        centrerDsFrame(apropos, jDesktopPane1);
        jDesktopPane1.add(apropos);
        //jPanel2.validate();
        apropos.setVisible(true);
    }//GEN-LAST:event_jMenuItem45ActionPerformed

    private void VK_ESCAPE(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_VK_ESCAPE
        // TODO add your handling code here:
        //quitterBoiteApplication();
    }//GEN-LAST:event_VK_ESCAPE

    private void jMenuItem46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem46ActionPerformed
        // TODO add your handling code here:GuideUtilisation()
        GuideUtilisateur guide = new GuideUtilisateur();
        centrerDsFrame(guide, jDesktopPane1);
        jDesktopPane1.add(guide);
        //jPanel2.validate();
        guide.setVisible(true);
    }//GEN-LAST:event_jMenuItem46ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:StatConnexion
        StatConnexion statCon = new StatConnexion();
        centrerDsFrame(statCon, jDesktopPane1);
        jDesktopPane1.add(statCon);
        //jPanel2.validate();
        statCon.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
        ChangerloginEtMotDepasse changPwd = new ChangerloginEtMotDepasse();
        centrerDsFrame(changPwd, jDesktopPane1);
        jDesktopPane1.add(changPwd);
        changPwd.setVisible(true);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        // TODO add your handling code here:
        AfficheEleveParent AEP = new AfficheEleveParent();
        centrerDsFrame(AEP, jDesktopPane1);
        jDesktopPane1.add(AEP);
        AEP.setVisible(true);
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    public static javax.swing.JMenu jMenu2;
    private static javax.swing.JMenu jMenu3;
    public static javax.swing.JMenu jMenu4;
    public static javax.swing.JMenu jMenu5;
    private static javax.swing.JMenu jMenu6;
    public static javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem27;
    public static javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem45;
    private javax.swing.JMenuItem jMenuItem46;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private static javax.swing.JMenuItem jMenuItem7;
    private static javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator12;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator14;
    private javax.swing.JPopupMenu.Separator jSeparator15;
    private javax.swing.JPopupMenu.Separator jSeparator17;
    private javax.swing.JPopupMenu.Separator jSeparator18;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    // End of variables declaration//GEN-END:variables
}
