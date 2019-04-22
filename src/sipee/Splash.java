/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sipee;

import java.awt.Image;
import java.awt.*;
import javax.swing.*;

public class Splash {

    JProgressBar progress;
    Thread thread;
    public static JFrame frame;
    public static final int x = (int) Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int y = (int) Toolkit.getDefaultToolkit().getScreenSize().height;
    Toolkit kit = Toolkit.getDefaultToolkit();
    private Image icône;

    public Splash(String imgPath, String message, String icone) {
        frame = new JFrame("Chargement");
        frame.setContentPane(new PanneauImage("/images/progressbar1.png"));
        JLabel texte = new JLabel(message);//Texte de la String
        texte.setForeground(Color.BLACK);
        texte.setFont(new java.awt.Font("Times New Roman", 3, 16));
        texte.setVisible(true);
        progress = new JProgressBar(0, 100);
        progress.setStringPainted(true);
        progress.setOpaque(true);
        frame.getContentPane().add(BorderLayout.CENTER, progress);
        // frame.getContentPane().add(BorderLayout.NORTH, texte);
        frame.setSize(400, 80);
        //Pour définir le Splash au milieu de l'écran'
        //Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(x / 3, y / 2);
        // pour que ca ai vraiement l air d un splash :
        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.setResizable(false);
        icône = kit.getImage(getClass().getResource("/images/icone2.gif"));
        frame.setIconImage(icône);
        //Retaillage de la barre pour qu elle corresponde a la taille de la frame
        progress.setBounds(new Rectangle(15, 20, 375, 35));
        thread = new Thread(new Progression());
        thread.start();

    }

  

    public class Progression implements Runnable {

        public void run() {
            for (int j = 1; j < 100; j++) {
                progress.setValue(j);
                progress.setForeground(Color.BLACK);
                progress.setBackground(Color.WHITE);
                progress.setBorderPainted(true);
                progress.setString(j + " %");
                try {
                    thread.sleep(100);//determination de la rapidit�e de la frame
                } catch (Exception e) {
                    e.printStackTrace();
                    frame.dispose();//en cas d' erreur pour pas rester bloqu� sur le splash
                }
            }
            frame.dispose(); //fermeture de la frame lorsque le chargement est temin�
            //SonAudioPlay sonAudioPlay = new SonAudioPlay();
            MainFrame mainframe = new MainFrame();
            mainframe.setVisible(true);


        }
    }

    public static void main(String[] args) {
        Splash splash = new Splash("", "  CHARGEMENT DU PROGRAMME PATIENTER SVP!\n", "");
    }
}
