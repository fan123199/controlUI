package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {
    Logger logger = Logger.getLogger("hehe");

    @FXML private Button tv_volume_down;

    @FXML
    protected void onHan(ActionEvent actionEvent) {
            logger.log(Level.ALL, "button use");

    }
}
