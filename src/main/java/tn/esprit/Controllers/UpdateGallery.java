package tn.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdateGallery implements Initializable {

    private final  String[] locations ={"Ariana","Béja","Ben Arous","Bizerte","Gabès","Gafsa","Jendouba","Kairouan","Kasserine","Kebili","Kef","Mahdia","Manouba","Medenine","Monastir","Nabeul","Sfax","Sidi Bouzid","Siliana","Sousse","Tataouine","Tozeur","Tunis","Zaghouan"};
    private final String[] hours = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};

    @FXML
    private TextField Name_id;

    @FXML
    private Button add_gallery;

    @FXML
    private TextField description_id;

    @FXML
    private ChoiceBox<String> ending_hour;

    @FXML
    private ImageView image_id;

    @FXML
    private ChoiceBox<String> location_id;

    @FXML
    private TextField phone_id;

    @FXML
    private Text price_alert;

    @FXML
    private ChoiceBox<String> starting_hour;

    @FXML
    private Text title_alert;

    @FXML
    private Button upload_image_button_id;

    @FXML
    private ImageView back_button;

    @FXML
    void HoverIn(MouseEvent event) {

    }

    @FXML
    void HoverIn1(MouseEvent event) {

    }

    @FXML
    void HoverOut(MouseEvent event) {

    }

    @FXML
    void HoverOut1(MouseEvent event) {

    }

    @FXML
    void addGallery(ActionEvent event) {

    }

    @FXML
    void addImage(ActionEvent event) {

    }

    private int primaryKey;

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

        location_id.setItems(FXCollections.observableArrayList(locations));
        ending_hour.setItems(FXCollections.observableArrayList(hours));
        starting_hour.setItems(FXCollections.observableArrayList(hours));
        Image logo = new Image("file:src\\images\\logo.png");
        image_id.setImage(logo);
        Image back = new Image("file:src\\images\\back-button.png");
        back_button.setImage(back);
    }

    public void go_back(MouseEvent mouseEvent) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/View_Galleries.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        javafx.scene.image.Image icon = new Image("file:/src/images/logo.png");

        // Create a new stage for the new window
        Stage newStage = new Stage();
        newStage.getIcons().add(icon);

        // Set the scene with the new root
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.setTitle("Tuni'Art");

        // Close the old stage
        Stage oldStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        oldStage.close();

        // Show the new stage
        newStage.show();

        System.out.println("moved");

    }
}
