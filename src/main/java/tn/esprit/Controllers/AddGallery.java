package tn.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tn.esprit.entities.gallery;
import tn.esprit.services.UserService;
import tn.esprit.services.galleryService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

;

public class AddGallery implements Initializable {

    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z1-9]+");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d{8}");
    @FXML
    private TextField Name_id;
    @FXML
    private ImageView back_button;

    @FXML
    private Button add_gallery;

    @FXML
    private TextField description_id;

    @FXML
    private ChoiceBox<String> ending_hour;

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
    private ImageView image_id;

    @FXML
    private Button upload_image_button_id;

    UserService us = new UserService();

    @FXML
    void HoverIn(MouseEvent event) {
    add_gallery.setStyle("-fx-background-color: #E15B10; -fx-background-radius: 55;");
    }

    @FXML
    void HoverOut(MouseEvent event) {
        add_gallery.setStyle("-fx-background-color: #E18B10; -fx-background-radius: 55;");
    }

    @FXML
    void HoverIn1(MouseEvent event) {
        upload_image_button_id.setStyle("-fx-background-color: #E15B10; -fx-background-radius: 55;");
    }
    @FXML
    void HoverOut1(MouseEvent event) {
        upload_image_button_id.setStyle("-fx-background-color: #E18B10; -fx-background-radius: 55;");
    }
    private boolean validateFields() throws SQLException {
        // Clear error messages and styles for all error fields
        title_alert.setText("");
        price_alert.setText("");


        // Check for missing information
        if (Name_id.getText().isEmpty() || phone_id.getText().isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Fill in all fields.");
            alert.showAndWait();
            return false;
        }

        boolean valid = true; //my flag so error messages won't appear individually

        // Validate first name
        if (!NAME_PATTERN.matcher(Name_id.getText()).matches()) {
            title_alert.setText("Name can only contains letters and numbers.");
            title_alert.setStyle("-fx-background-color: #ff4d4d;");
            valid = false;
        }

        // Validate last name
        if (!PHONE_PATTERN.matcher(phone_id.getText()).matches()) {
            price_alert.setText("Phone only contains numbers.");
            price_alert.setStyle("-fx-background-color: #ff4d4d;");
            valid = false;
        }

        return valid;
    }




    @FXML
    void addImage(ActionEvent event) {
        // Load the image (replace "/path/to/your/image.jpg" with the actual file path)
        Image image = new Image("@../../images/logo.png");

        // Set the image to the ImageView
        image_id.setImage(image);
    }

    private final galleryService GS = new galleryService();
    private final  String[] locations ={"Ariana","Béja","Ben Arous","Bizerte","Gabès","Gafsa","Jendouba","Kairouan","Kasserine","Kebili","Kef","Mahdia","Manouba","Medenine","Monastir","Nabeul","Sfax","Sidi Bouzid","Siliana","Sousse","Tataouine","Tozeur","Tunis","Zaghouan"};
    private final String[] hours = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};


    @FXML
    void addGallery(ActionEvent event) {

        try {
            if(validateFields()) {


                String operating_hours = "from " + starting_hour.getValue() + "h" + " to " + ending_hour.getValue() + "h";
                String location = String.valueOf(location_id.getValue());
                int phone = Integer.parseInt(phone_id.getText());
                GS.addd(new gallery(Name_id.getText(), description_id.getText(), location, phone, operating_hours));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation");
                alert.setContentText("gallery added !");
                alert.showAndWait();
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
                Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                oldStage.close();

                // Show the new stage
                newStage.show();

                System.out.println("moved");
            }
        }
        catch (SQLException e){

            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        }

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
    @FXML
    void go_back(MouseEvent event) {
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
        Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        oldStage.close();

        // Show the new stage
        newStage.show();

        System.out.println("moved");


    }

}

