import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Based on JIntellitype's demo file
 */
public class HotKeyInterface extends JFrame implements HotkeyListener, IntellitypeListener {

    public static Properties prop = new Properties();

    public static Map<Integer, String> idToSound = new HashMap<>();
    public static Map<String, Sound> keyToSound = new HashMap<>();

    public HotKeyInterface() {
        JIntellitype.getInstance().registerHotKey(KeyEvent.VK_F1, "F1");
        loadSettings();
    }

    public void onHotKey(int aIdentifier) {
        if (aIdentifier == KeyEvent.VK_F1)
            Soundboard.quit();
        Soundboard.playClip(idToSound.get(aIdentifier));
    }

    public void onIntellitype(int aCommand) {
        return;
    }

    public static void loadSettings() {
        try {
            prop.load(new FileInputStream("./soundboard-settings.ini"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void saveSettings() {
        try {
            prop.store(new FileOutputStream("./soundboard-settings.ini"), "Soundboard Settings");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the JInitellitype library making sure the DLL is located.
     */
    public void initJIntellitype() {
        try {
            JIntellitype.getInstance().addHotKeyListener(this);
        } catch (RuntimeException e) {
            System.out.println("Either you are not on Windows, or there is a problem with the JIntellitype library!");
        }
    }

    public static void registerHotkey(Sound s, int modifiers, int keyCode) {
        try {

            String hotkey = hotKeytoText(modifiers, keyCode);

            if (keyToSound.containsKey(hotkey)) {
                Sound r = keyToSound.get(hotkey);
                unbind(r);
            }

            JIntellitype.getInstance().unregisterHotKey(s.id);
            JIntellitype.getInstance().registerHotKey(s.id, modifiers, keyCode);
            prop.setProperty(s.name, "" + modifiers + " " + keyCode);

            idToSound.put(s.id, s.name);
            keyToSound.put(hotkey, s);
            s.keybind = hotkey;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void registerHotkey(Sound s, String hotkey) {
        try {

            if (keyToSound.containsKey(hotkey)) {
                Sound r = keyToSound.get(hotkey);
                unbind(r);
            }

            JIntellitype.getInstance().unregisterHotKey(s.id);
            JIntellitype.getInstance().registerHotKey(s.id, hotkey);
            prop.setProperty(s.name, hotkey);

            idToSound.put(s.id, s.name);
            keyToSound.put(hotkey, s);
            s.keybind = hotkey;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String hotKeytoText(int modifiers, int keyCode) {

        String mod, key;
        if (modifiers == JIntellitype.MOD_ALT) {
            mod = "ALT + ";
        } else if (modifiers == JIntellitype.MOD_CONTROL) {
            mod = "CONTROL + ";
        } else if (modifiers == JIntellitype.MOD_SHIFT) {
            mod = "SHIFT + ";
        } else if (modifiers == JIntellitype.MOD_WIN) {
            mod = "WIN + ";
        } else {
            mod = "";
        }

        key = KeyEvent.getKeyText(keyCode);

        return mod + key;


    }

    public static void unbind(Sound s) {

        if (s.keybind.equals(""))
            return;

        prop.remove(s.name);
        keyToSound.remove(s.keybind);
        JIntellitype.getInstance().unregisterHotKey(s.id);
        s.keybind = "";

    }

}