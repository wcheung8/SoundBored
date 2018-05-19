import com.melloware.jintellitype.JIntellitype;
import com.melloware.jintellitype.JIntellitypeException;
import javafx.application.Application;
import javafx.embed.swing.JFXPanel;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Mixer;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class Soundboard {

    static ArrayList<Sound> sounds = new ArrayList<Sound>();

    public static void main(String[] args) throws Exception {

        JFXPanel fxPanel = new JFXPanel();
        trayGUI stage = new trayGUI();

        stage.createAndShowGUI();

        try {
            JIntellitype.getInstance();
        } catch (JIntellitypeException e) {
            JOptionPane.showMessageDialog(null, "Soundboard already running!");
            System.exit(0);
        }

        // next check to make sure JIntellitype DLL can be found and we are on a Windows operating System
        if (!JIntellitype.isJIntellitypeSupported()) {
            JOptionPane.showMessageDialog(null, "JIntellitype not supported!");
            quit();
        }

        HotKeyInterface mainFrame = new HotKeyInterface();
        mainFrame.setTitle("Soundbored");
        mainFrame.setVisible(false);
        mainFrame.initJIntellitype();

        // extract files from current directory
        File folder = new File(System.getProperty("user.dir"));

        for (File f : folder.listFiles()) {
            if (f.isFile() && (f.getName().endsWith(".wav"))) {
                // add song and title to queue
                sounds.add(new Sound(f.getName()));
            }
        }

        if (sounds.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sounds to play!");
            quit();
        }
        Application.launch(menuGUI.class);
    }

    static Mixer getMixerByName(String toFind) {
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            if (info.getName().contains(toFind)) {
                return AudioSystem.getMixer(info);
            }
        }
        return null;
    }

    static void listMixers() {
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();    //get available mixers
        System.out.println("Available mixers:");
        for (int cnt = 0; cnt < mixerInfo.length; cnt++) {
            System.out.println(mixerInfo[cnt].getName());
        }
    }

    static void playClip(String filename) {
        try {
            Clip c = AudioSystem.getClip(getMixerByName("CABLE").getMixerInfo());
            AudioInputStream as = AudioSystem.getAudioInputStream(new File(filename));

            c.open(as);
            c.setFramePosition(0);
            c.start();

            Clip d = AudioSystem.getClip(getMixerByName("Speaker").getMixerInfo());
            AudioInputStream b = AudioSystem.getAudioInputStream(new File(filename));
            d.open(b);
            d.setFramePosition(0);
            d.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void quit() {
        JIntellitype.getInstance().cleanUp();
        HotKeyInterface.saveSettings();
        System.exit(0);
    }

}