package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class UpdateEvent {

    @FXML
    private Button add_event;

    @FXML
    private ChoiceBox<?> category_id;

    @FXML
    private DatePicker date_id;

    @FXML
    private TextField hours;

    @FXML
    private ImageView logoId;

    @FXML
    private Text price_alert;

    @FXML
    private Text title_alert;

    @FXML
    private TextField title_id;

    @FXML
    void HoverIn(MouseEvent event) {

    }

    @FXML
    void HoverOut(MouseEvent event) {

    }

    @FXML
    void addEvent(ActionEvent event) {

    }

}
