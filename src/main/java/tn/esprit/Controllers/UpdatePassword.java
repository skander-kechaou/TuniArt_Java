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
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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
import java.util.regex.Pattern;

public class UpdatePassword implements Initializable {

    @FXML
    private ImageView LogoId;

    @FXML
    private Text PasswordMatchAlertId;

    @FXML
    private Text emailAlertId;

    @FXML
    private ImageView goBackButtonId;

    @FXML
    private Text newPwdTextId;

    @FXML
    private Text passwordAlertId;

    @FXML
    private PasswordField pwdField;

    @FXML
    private PasswordField rePwdField;

    @FXML
    private Text rePwdTextId;

    @FXML
    private Text resetPwdTextId;

    @FXML
    private Button updateButton;
    User currentUser;
    UserService us = new UserService();
    boolean valid;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");


    private boolean validatePassword() throws SQLException {
        // Clear error messages and styles for all error fields
        passwordAlertId.setText("");

        // Check for missing information
        if (pwdField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Fill in all fields.");
            alert.showAndWait();
            return false;
        }

        boolean valid = true; //my flag so error messages won't appear individually

        // Validate password
        if (!PASSWORD_PATTERN.matcher(pwdField.getText()).matches()) {
            passwordAlertId.setText("Password must contain at least one uppercase, one lowercase, one digit (8 characters).");
            passwordAlertId.setStyle("-fx-background-color: #ff4d4d;");
            valid = false;
        }


        return valid;
    }

    @FXML
    void HoverIn(MouseEvent event) {
        updateButton.setStyle("-fx-background-color: #E15B10; -fx-background-radius: 55;");
    }

    @FXML
    void HoverOut(MouseEvent event) {
        updateButton.setStyle("-fx-background-color: #E18B10; -fx-background-radius: 55;");
    }

    @FXML
    void checkPwdMatch(KeyEvent event) {
        if (pwdField.getText().equals(rePwdField.getText())) {
            PasswordMatchAlertId.setText("The passwords match.");
            PasswordMatchAlertId.setStyle("-fx-background-color: #36d664;");
        } else {
            PasswordMatchAlertId.setText("The passwords don't match.");
            PasswordMatchAlertId.setStyle("-fx-background-color: #ff4d4d;");
        }
    }

    @FXML
    void redirectToLogIn(MouseEvent event) throws IOException {
        // Get the current stage from any node in the scene graph
        Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/LogIn.fxml"));
        javafx.scene.image.Image icon = new Image("file:/C:/Users/DELL/Documents/3A/Semester 2/PIDEV/Tuni Art/src/images/logo.png");

        // Create a new stage for the new window
        Stage newStage = new Stage();
        newStage.getIcons().add(icon);

        // Set the scene with the new root
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.setTitle("Log In");

        // Close the old stage
        oldStage.close();

        // Show the new stage
        newStage.show();

        System.out.println("moved");
    }

    @FXML
    void updatePWD(ActionEvent event) throws IOException, SQLException {
        System.out.println("enter updatePWD");
        if (validatePassword()) {
            System.out.println("validated pwd");
            if (pwdField.getText().equals(rePwdField.getText())) {
                System.out.println("match");
                currentUser.setPassword(pwdField.getText());
                try {
                    us.update(currentUser);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation");
                alert.setContentText("Your password has been updated.");
                alert.showAndWait();

                // Get the current stage from any node in the scene graph
                Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Load the new FXML file
                Parent root = FXMLLoader.load(getClass().getResource("/LogIn.fxml"));
                javafx.scene.image.Image icon = new Image("file:/C:/Users/DELL/Documents/3A/Semester 2/PIDEV/Tuni Art/src/images/logo.png");

                // Create a new stage for the new window
                Stage newStage = new Stage();
                newStage.getIcons().add(icon);

                // Set the scene with the new root
                Scene scene = new Scene(root);
                newStage.setScene(scene);
                newStage.setTitle("Log In");

                // Close the old stage
                oldStage.close();

                // Show the new stage
                newStage.show();

                System.out.println("moved");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Retrieve the UID from the session
        int uid = SessionManager.getInstance().getCurrentUserUid();
        try {
            currentUser = us.searchByUid(uid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Image logo = new Image("file:src\\images\\logo.png");
        LogoId.setImage(logo);
        Image goBack = new Image("file:src\\images\\arrow-left.png");
        goBackButtonId.setImage(goBack);
    }

    @FXML
    void checkPwd(KeyEvent event) {

    }
}
