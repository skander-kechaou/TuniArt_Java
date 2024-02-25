package tn.esprit.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import tn.esprit.entities.User;
import tn.esprit.services.UserService;
import tn.esprit.utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Home implements Initializable {

    UserService us = new UserService();
    @FXML
    private ImageView profilePictureId;

    @FXML
    private ImageView logoId;

    @FXML
    private FlowPane usersContainer;


    @FXML
    private Text uidTextId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image logo = new Image("file:src\\images\\logo.png");
        logoId.setImage(logo);
        // Retrieve the UID from the session
        int uid = SessionManager.getInstance().getCurrentUserUid();
        User currentUser;
        try {
            currentUser = us.searchByUid(uid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Display the UID on the UI
        uidTextId.setText("Welcome, " + currentUser.getFname());
        Image profile = new Image("file:src\\images\\Profile-PNG-File.png");
        profilePictureId.setImage(profile);

        usersContainer.setPadding(new Insets(30)); // Adjust padding as needed
        usersContainer.setHgap(80); // Set horizontal gap between elements
        usersContainer.setVgap(20); // Set vertical gap between lines

        displayUsers();
    }

    @FXML
    void userMenu(MouseEvent event) throws IOException {
        // Get the current stage from any node in the scene graph
        Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/Profile.fxml"));
        javafx.scene.image.Image icon = new Image("file:/C:/Users/DELL/Documents/3A/Semester 2/PIDEV/Tuni Art/src/images/logo.png");

        // Create a new stage for the new window
        Stage newStage = new Stage();
        newStage.getIcons().add(icon);

        // Set the scene with the new root
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.setTitle("Profile");

        // Close the old stage
        oldStage.close();

        // Show the new stage
        newStage.show();

        System.out.println("moved");
    }

    private void displayUsers() {
        try {
            List<User> users = us.diplayListUsersAndArtists();

            // Call the method to create user boxes and add them to the container
            List<VBox> userBoxes = createUserBox(users);
            usersContainer.getChildren().addAll(userBoxes);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private List<VBox> createUserBox(List<User> users) {
        List<VBox> userBoxes = new ArrayList<>();
        for (User user : users) {
            VBox userBox = new VBox();
            userBox.getStyleClass().add("userBox"); // Add style class to VBox
            //userBox.setStyle("-fx-padding: 30px; -fx-spacing: 20px; -fx-border-radius: 10px; -fx-background-color: #f7f8fa; -fx-border-color: #5dade2; ");
            userBox.setStyle("-fx-padding: 20px; -fx-spacing: 10px; -fx-background-color: #f7f8fa; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

            // Set a fixed width for the VBox
            userBox.setPrefWidth(185); // Adjust width here (e.g., 200 pixels)

            // Add margin to the VBox
            userBox.setMargin(userBox, new Insets(10));

            // Add user name label
            Label nameLabel = new Label(user.getFname().toUpperCase() + " " + user.getLname().toUpperCase());
            nameLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            userBox.setAlignment(Pos.CENTER);
            userBox.getChildren().add(nameLabel);

            // Add profile picture
            ImageView profileImage;
            if(user.getProfile_pic()==null) {
                profileImage = new ImageView(new Image("file:src\\images\\Profile-PNG-File.png"));
                System.out.println("yassss");
            }
            else {
                profileImage = new ImageView(new Image("file:src\\images\\" + user.getProfile_pic()));
            }
            profileImage.setFitWidth(100);
            profileImage.setFitHeight(100);
            userBox.getChildren().add(profileImage);

            // Attach event handler to the profile picture
            profileImage.setOnMouseClicked(event -> {
                // Redirect to the profile page for the selected user
                System.out.println(user);
                redirectToProfilePage(user);
            });

            // Add margin to the bottom of the VBox
            userBox.setMargin(profileImage, new Insets(10));
            userBoxes.add(userBox);
        }
        return userBoxes;
    }

    private void redirectToProfilePage(User user) {
        // Load the profile page FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProfilePage.fxml"));
        Parent root;
        try {
            root = loader.load();
            ProfilePage controller = loader.getController();
            // Pass the selected user's information to the profile page controller
            controller.initData(user);

            // Display the profile page
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
