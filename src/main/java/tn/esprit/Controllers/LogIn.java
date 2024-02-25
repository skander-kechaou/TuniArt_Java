package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tn.esprit.entities.User;
import tn.esprit.services.UserService;
import tn.esprit.utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LogIn implements Initializable {

    UserService us = new UserService();

    @FXML
    private Button LogInButton;

    @FXML
    private Text LogInTextId;

    @FXML
    private ImageView LogoId;

    @FXML
    private PasswordField PasswordFieldId;

    @FXML
    private TextField emailFieldId;

    @FXML
    private Text emailTextId;

    @FXML
    private Text logInRedirId;

    @FXML
    private Text passwordTextId;

    @FXML
    private Hyperlink resetPwdLink;

    @FXML
    private Hyperlink signUpLink;

    @FXML
    private Text emailAlertId;

    @FXML
    private Text passwordAlertId;

    @FXML
    void HoverIn(MouseEvent event) {
        LogInButton.setStyle("-fx-background-color: #E15B10; -fx-background-radius: 55;");
    }

    @FXML
    void HoverOut(MouseEvent event) {
        LogInButton.setStyle("-fx-background-color: #E18B10; -fx-background-radius: 55;");
    }

    @FXML
    void RedirectToReset(ActionEvent event) throws IOException {
        // Get the current stage from any node in the scene graph
        Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/resetPwd.fxml"));
        javafx.scene.image.Image icon = new Image("file:/C:/Users/DELL/Documents/3A/Semester 2/PIDEV/Tuni Art/src/images/logo.png");

        // Create a new stage for the new window
        Stage newStage = new Stage();
        newStage.getIcons().add(icon);

        // Set the scene with the new root
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.setTitle("Reset Password");

        // Close the old stage
        oldStage.close();

        // Show the new stage
        newStage.show();

        System.out.println("moved");
    }

    @FXML
    void logIn(ActionEvent event) throws SQLException, IOException {
        emailAlertId.setText("");
        passwordAlertId.setText("");

        if (!us.doesEmailExist(emailFieldId.getText())) {
            emailAlertId.setText("No account is associated with this e-mail address.");
            emailAlertId.setStyle("-fx-background-color: #ff4d4d;");
        }
        else if (!us.loginMatch(emailFieldId.getText(), PasswordFieldId.getText())) {
            passwordAlertId.setText("This password doesn't match with the provided e-mail address.");
            passwordAlertId.setStyle("-fx-background-color: #ff4d4d;");
        }
        else {
            // Retrieve the UID of the logged-in user
            int uid = us.getUidByEmail(emailFieldId.getText());

            // Store the UID in the session
            SessionManager.getInstance().setCurrentUserUid(uid);

            // Get the current stage from any node in the scene graph
            Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/Home.fxml"));
            javafx.scene.image.Image icon = new Image("file:/C:/Users/DELL/Documents/3A/Semester 2/PIDEV/Tuni Art/src/images/logo.png");

            // Create a new stage for the new window
            Stage newStage = new Stage();
            newStage.getIcons().add(icon);

            // Set the scene with the new root
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.setTitle("Home");

            // Close the old stage
            oldStage.close();

            // Show the new stage
            newStage.show();

            System.out.println("moved");
        }

    }

    @FXML
    void redirectSignUp(ActionEvent event) throws IOException {
        // Get the current stage from any node in the scene graph
        Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/SignUp.fxml"));
        javafx.scene.image.Image icon = new Image("file:/C:/Users/DELL/Documents/3A/Semester 2/PIDEV/Tuni Art/src/images/logo.png");

        // Create a new stage for the new window
        Stage newStage = new Stage();
        newStage.getIcons().add(icon);

        // Set the scene with the new root
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.setTitle("Sign Up");

        // Close the old stage
        oldStage.close();

        // Show the new stage
        newStage.show();

        System.out.println("moved");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image("file:src\\images\\logo.png");
        LogoId.setImage(image);
    }
}
