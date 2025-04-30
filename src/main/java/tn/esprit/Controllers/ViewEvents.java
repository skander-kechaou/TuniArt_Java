package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tn.esprit.entities.Event;
import tn.esprit.entities.User;
import tn.esprit.services.EventService;
import tn.esprit.services.UserService;
import tn.esprit.utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewEvents implements Initializable {

    EventService ES = new EventService();
    UserService us = new UserService();
    User currentUser;

    @FXML
    private FlowPane eventContainer;

    @FXML
    private ImageView logoId;

    @FXML
    private Text uidTextId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image logo = new Image("file:src\\images\\logo.png");
        logoId.setImage(logo);
        // Retrieve the UID from the session
        int uid = SessionManager.getInstance().getCurrentUserUid();
        try {
            currentUser = us.searchByUid(uid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        displayEvents();
    }

    private void displayEvents() {
        try {
            List<Event> events = ES.diplayList();

            // Call the method to create user boxes and add them to the container
            List<VBox> eventBoxes = createeventBox(events);
            eventContainer.getChildren().addAll(eventBoxes);
            // Add padding to the eventContainer
            eventContainer.setPadding(new Insets(10)); // Adjust the value as needed
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private List<VBox> createeventBox(List<Event> events) {
        List<VBox> eventBoxes = new ArrayList<>();
        for (Event event : events) {
            VBox eventBox = new VBox();
            eventBox.getStyleClass().add("eventBox"); // Add style class to VBox
            //eventBox.setStyle("-fx-padding: 30px; -fx-spacing: 20px; -fx-border-radius: 10px; -fx-background-color: #f7f8fa; -fx-border-color: #5dade2; ");
            eventBox.setStyle("-fx-padding: 20px; -fx-spacing: 10px; -fx-background-color: #f7f8fa; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

            // Set a fixed width for the VBox
            eventBox.setPrefWidth(185); // Adjust width here (e.g., 200 pixels)

            // Add margin to the VBox
            eventBox.setMargin(eventBox, new Insets(10));

            // Add user name label
            Label titleLabel = new Label(event.getEvent_title().toUpperCase());
            Label categoryLabel = new Label(event.getCategory());
            Label dateLabel = new Label(event.getEvent_date().toString());
            Label durationLabel = new Label(String.valueOf(event.getDuration())+" HOUR(S)");
            Button delete = new Button("Delete");
            Button update = new Button("Update");
            HBox buttonsBox = new HBox(10); // 10 is the spacing between buttons (adjust as needed)
            buttonsBox.getChildren().addAll( delete, update);
            buttonsBox.setPadding(new Insets(10, 0, 0, 10));
            update.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
            delete.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
            titleLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            categoryLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            dateLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            durationLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));

            delete.setOnAction(eventina -> {
                // Handle delete button action

                try {
                    ES.delete(event.getEvent_id()); // Call the delete method with the art_ref
                    System.out.println("Art_Piece deleted");

                    // Display a confirmation dialog
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Art Piece has been deleted successfully!");
                    alert.showAndWait();

                    // Remove the VBox containing the art piece from the UI
                    eventBoxes.remove(eventBox);
                    eventContainer.getChildren().remove(eventBox);

                } catch (SQLException e) {
                    // Display an error dialog if deletion fails
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Error deleting the Art Piece: " + e.getMessage());
                    alert.showAndWait();
                }
            });

            update.setOnAction(eventina -> {
                // Load the profile page FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfilePage.fxml"));
                Parent root;
                try {
                    root = loader.load();
                    //ProfilePageController controller = loader.getController();

                    // Pass the selected user's information to the profile page controller
                    //controller.initData(user);

                    // Display the profile page
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            eventBox.setAlignment(Pos.CENTER);
            eventBox.getChildren().addAll(titleLabel,categoryLabel,dateLabel,durationLabel, buttonsBox);//display everything


            // Add margin to the bottom of the VBox
            eventBox.setMargin(eventBox, new Insets(10));
            eventBoxes.add(eventBox);
        }
        return eventBoxes;
    }

    @FXML
    void upload_event(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/addEvent.fxml"));
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
