import javafx.application.Platform;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class trayGUI {

    static PopupMenu popup = new PopupMenu();
    static TrayIcon trayIcon;
    static SystemTray tray = SystemTray.getSystemTray();

    public void createAndShowGUI() throws IOException {
        // Check the SystemTray support
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        trayIcon = new TrayIcon(ImageIO.read(getClass().getResource(("resources/player.png"))));
        trayIcon.setImageAutoSize(true);

        // Create a popup menuGUI components
        MenuItem exitItem = new MenuItem("Exit");

        // Add components to popup menuGUI
        popup.add(exitItem);
        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            Soundboard.quit();
        }

        trayIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    public void run() {
                        menuGUI.controller.show();
                    }
                });
            }
        });

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Soundboard.quit();
            }
        });
    }

}
