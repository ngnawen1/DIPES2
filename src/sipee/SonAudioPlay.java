/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sipee;

import java.io.*;
import javax.swing.JOptionPane;
import sun.audio.*;

/**
 * A simple Java sound file example (i.e., Java code to play a sound file).
 * AudioStream and AudioPlayer code comes from a javaworld.com example.
 * @author alvin alexander, devdaily.com.
 */
public class SonAudioPlay {

    public SonAudioPlay() {
        try {

            //String gongFile = "C:/Users/admine/Documents/NetBeansProjects/Vermond/src/sons/bienvenue.wav";
            //InputStream in = new FileInputStream(gongFile);

            // create an audiostream from the inputstream

            //AudioStream audioStream = new AudioStream(in);

            // play the audio clip with the audioplayer class
        
            //AudioPlayer.player.start(audioStream);
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "ERROR \n" + e.getMessage());
        }
    }

    public static void main(String[] args)
            throws Exception {
        SonAudioPlay sonAudioPlay = new  SonAudioPlay();
        // open the sound file as a Java input stream
    }
}
