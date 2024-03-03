package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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

public class Dashboard implements Initializable {

    @FXML
    private Text managementTextId;

    @FXML
    private ImageView profilePicId;

    @FXML
    private Text userID;

    @FXML
    private Button usersButton;

    @FXML
    private FlowPane usersContainer;

    @FXML
    private Hyperlink inviteOnly;

    UserService us = new UserService();
    String profile_picture;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Retrieve the UID from the session
        int uid = SessionManager.getInstance().getCurrentUserUid();
        User currentUser;
        try {
            currentUser = us.searchByUid(uid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Display the UID on the UI
        userID.setText(currentUser.getFname().toUpperCase()+" "+currentUser.getLname().toUpperCase());
        Image profile = new Image("file:src\\images\\Profile-PNG-File.png");
        profilePicId.setImage(profile);

        displayUsers();
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
            userBox.setStyle("-fx-padding: 10px; -fx-spacing: 5px; -fx-background-color: #f7f8fa; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

            // Set a fixed width for the VBox
            userBox.setPrefWidth(195); // Adjust width here (e.g., 200 pixels)

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
            Label roleLabel = new Label(user.getRole());
            roleLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            userBox.getChildren().add(roleLabel);

            Button status = new Button("Activate");
            if (user.getStatus())
            {
                status.setText("Deactivate");
                status.setStyle("-fx-background-color: #FC6541; -fx-background-radius: 55; -fx-text-fill: white;");
                status.setPadding(new Insets(10,10,10,10));
            }
            else {
                status.setText("Activate");
                status.setStyle("-fx-background-color: #36d664; -fx-background-radius: 55; -fx-text-fill: white;");
                status.setPadding(new Insets(10,10,10,10));
            }
            userBox.getChildren().add(status);

            status.setOnAction(event -> {
                //handle activation or deactivation
                if(user.getStatus())
                {
                    user.setStatus(false);
                } else {
                    user.setStatus(true);
                }
                try {
                    us.update(user);

                    // Show success message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Confirmation");
                    alert.setContentText("Sign Up Complete!");
                    alert.showAndWait();

                    // Update button text and style based on the new status
                    if (user.getStatus()) {
                        status.setText("Deactivate");
                        status.setStyle("-fx-background-color: #FC6541; -fx-background-radius: 55; -fx-text-fill: white;");
                    } else {
                        status.setText("Activate");
                        status.setStyle("-fx-background-color: #36d664; -fx-background-radius: 55; -fx-text-fill: white;");
                    }

                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            });


            // Add margin to the bottom of the VBox
            userBox.setMargin(profileImage, new Insets(10));
            userBoxes.add(userBox);
        }
        return userBoxes;
    }

    @FXML
    void sendLink(ActionEvent event) throws IOException {
        // Load the new FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/InviteAdmin.fxml"));
        javafx.scene.image.Image icon = new Image("file:/src/images/logo.png");

        // Create a new stage for the new window
        Stage newStage = new Stage();
        newStage.getIcons().add(icon);

        // Set the scene with the new root
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.setTitle("Profile");

        // Close the old stage
        Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        oldStage.close();

        // Show the new stage
        newStage.show();

        System.out.println("moved");
    }
}
