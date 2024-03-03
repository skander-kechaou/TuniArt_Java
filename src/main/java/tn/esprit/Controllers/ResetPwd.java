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
import tn.esprit.entities.User;
import tn.esprit.services.EmailService;
import tn.esprit.services.UserService;
import tn.esprit.services.VerificationCodeGenerator;
import tn.esprit.utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static tn.esprit.services.VerificationCodeGenerator.generateVerificationCode;

public class ResetPwd implements Initializable {

    UserService us = new UserService();
    EmailService em = new EmailService();
    VerificationCodeGenerator vcg = new VerificationCodeGenerator();

    @FXML
    private ImageView LogoId;

    @FXML
    private Button continueButton;

    @FXML
    private Text emailAlertId;

    @FXML
    private TextField emailFieldId;

    @FXML
    private Text emailTextId;

    @FXML
    private ImageView goBackButtonId;

    @FXML
    private Text resetPwdTextId;

    @FXML
    void HoverIn(MouseEvent event) {
        continueButton.setStyle("-fx-background-color: #E15B10; -fx-background-radius: 55;");
    }

    @FXML
    void HoverOut(MouseEvent event) {
        continueButton.setStyle("-fx-background-color: #E18B10; -fx-background-radius: 55;");
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
    void redirectToVerif(ActionEvent event) throws SQLException, IOException {
        // clear alert
        emailAlertId.setText("");

        if(!us.doesEmailExist(emailFieldId.getText())) {
            emailAlertId.setText("No account is associated with this e-mail address.");
            emailAlertId.setStyle("-fx-background-color: #ff4d4d;");
        }
        else {

            // Generate a verification code
            int uid = us.getUidByEmail(emailFieldId.getText());

            // Store the UID in the session
            SessionManager.getInstance().setCurrentUserUid(uid);

            User u = us.searchByUid(uid);
            String verificationCode = VerificationCodeGenerator.generateVerificationCode(u);
            u.setVerification_code(verificationCode);
            us.update(u);
            System.out.println(u);
            System.out.println(u.getEmail()+" "+verificationCode);

            // Send the verification email
            String subject = "[Tuni'Art] Password Reset Verification Code";
            String body = "Dear User,\n\nYour verification code is: " + verificationCode + "\n\nRegards,\nTuni'Art";
            EmailService.sendVerificationEmail(u.getEmail(), verificationCode, subject, body);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("A verification code has been sent to the provided e-mail address.");
            alert.showAndWait();

            // Get the current stage from any node in the scene graph
            Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/Verification.fxml"));
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image logo = new Image("file:C:\\Users\\DELL\\Documents\\3A\\Semester 2\\PIDEV\\Tuni Art\\src\\images\\logo.png");
        LogoId.setImage(logo);
        Image goBack = new Image("file:C:\\Users\\DELL\\Documents\\3A\\Semester 2\\PIDEV\\Tuni Art\\src\\images\\arrow-left.png");
        goBackButtonId.setImage(goBack);
    }
}
