import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class menuController {

    @FXML
    public TableView<Sound> sound_list;
    @FXML
    private TableColumn<Sound, String> name;
    @FXML
    private TableColumn<Sound, String> keybind;
    @FXML
    public Pane pane;

    public EventHandler<KeyEvent> bindHotkey = new EventHandler<KeyEvent>() {

        @Override
        public void handle(KeyEvent event) {

            if (event.getCode().isModifierKey() || sound_list.getSelectionModel().getSelectedItem() == null)
                return;

            String s = event.getCode().toString();
            if (event.isControlDown())
                s = "CTRL+" + s;
            if (event.isAltDown())
                s = "ALT+" + s;
            if (event.isShiftDown())
                s = "SHIFT+" + s;
            if (event.isMetaDown())
                s = "WIN+" + s;

            HotKeyInterface.registerHotkey(sound_list.getSelectionModel().getSelectedItem(), s);
            sound_list.getSelectionModel().clearSelection();
            sound_list.refresh();
            HotKeyInterface.saveSettings();
            pane.setOnKeyPressed(e -> e.consume());
        }
    };

    public void initialize() {
        // songlist click listener
        name.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().name));
        keybind.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().keybind));
        this.sound_list.setItems(FXCollections.observableList(Soundboard.sounds));
    }

    public menuController show() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(menuGUI.class.getResource("resources/sound-menu.fxml"));

            Parent root = loader.load();
            Stage mainStage = menuGUI.mainStage;
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

            //set Stage boundaries to the lower right corner of the visible bounds of the main screen
            mainStage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 600);
            mainStage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 430);
            mainStage.setScene(new Scene(root));
            mainStage.getIcons().add(new Image(getClass().getResourceAsStream("resources/player.png")));
            mainStage.setTitle("SoundBored");
            mainStage.show();

            return loader.getController();

        } catch (IOException e) {

            e.printStackTrace();
            return null;

        }
    }

    public final void handleKeyBindPress() {
        pane.setOnKeyPressed(bindHotkey);
    }

    public final void handlePlayPress() {
        if (sound_list.getSelectionModel().getSelectedItem() != null)
            sound_list.getSelectionModel().getSelectedItem().play();
    }
}




