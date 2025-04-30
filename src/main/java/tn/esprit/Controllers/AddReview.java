package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tn.esprit.entities.Art_Review;
import tn.esprit.services.Art_ReviewService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AddReview implements Initializable {

    @FXML
    private ImageView R1;

    @FXML
    private ImageView R2;

    @FXML
    private ImageView R3;

    @FXML
    private ImageView R4;

    @FXML
    private ImageView R5;

    @FXML
    private Button add_review;

    @FXML
    private ImageView back_button;

    @FXML
    private TextField description_id;

    @FXML
    private ImageView image_id;

    @FXML
    private Text price_alert;

    @FXML
    private Text title_alert;
    private final Art_ReviewService RS = new Art_ReviewService();
    int rating;

    @FXML
    void HoverIn(MouseEvent event) {

    }

    @FXML
    void HoverOut(MouseEvent event) {

    }

    @FXML
    void Rate1(MouseEvent event) {
        rating=1;
        R1.setOpacity(0.5);
    }

    @FXML
    void Rate2(MouseEvent event) {
        rating=2;
        R2.setOpacity(0.5);
    }

    @FXML
    void Rate3(MouseEvent event) {
        rating=3;
        R3.setOpacity(0.5);
    }

    @FXML
    void Rate4(MouseEvent event) {
        rating=4;
        R4.setOpacity(0.5);
    }

    @FXML
    void Rate5(MouseEvent event) {
        rating=5;
        R5.setOpacity(0.5);
    }
    private boolean containsBadWord(String description) {
        // List of bad words
        List<String> badWords = Arrays.asList("bad", "michant", "mayjich");

        // Convert description to lowercase for case-insensitive comparison
        String lowerDescription = description.toLowerCase();

        // Check if any bad word appears in the description
        for (String word : badWords) {
            if (lowerDescription.contains(word)) {
                return true;
            }
        }
        return false;
    }

    @FXML
    void addReview(ActionEvent event) {
        try {
            String description = description_id.getText();

            // Check for bad words in the description
            if (containsBadWord(description)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("The description contains inappropriate content.");
                alert.showAndWait();
                return; // Stop further processing
            }


                LocalDate currentDate = LocalDate.now();
                java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);
                RS.addd(new Art_Review(20,2,sqlDate,rating,description));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation");
                alert.setContentText("review added !");
                alert.showAndWait();

        }
        catch (SQLException e){

            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        }

    }

    @FXML
    void go_back(MouseEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/gallery.fxml"));
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

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {


        Image logo = new Image("file:src\\images\\logo.png");
        image_id.setImage(logo);
        Image back = new Image("file:src\\images\\back-button.png");
        back_button.setImage(back);
        Image star = new Image("file:src\\images\\star.png");
        Image Selected_star = new Image("file:src\\images\\selected_star.png");

        R1.setImage(star);
        R2.setImage(star);
        R3.setImage(star);
        R4.setImage(star);
        R5.setImage(star);

    }


}


