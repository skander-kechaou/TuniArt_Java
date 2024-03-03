package tn.esprit.Controllers;

import com.github.cage.Cage;
import javafx.collections.FXCollections;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.entities.User;
import tn.esprit.services.UserService;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class SignUp implements Initializable {
    private final UserService us = new UserService();

    @FXML
    private Region containerId;

    @FXML
    private DatePicker datePickerId;

    @FXML
    private Text dobTextId;

    @FXML
    private TextField emailFieldId;

    @FXML
    private Text emailTextId;

    @FXML
    private TextField firstNameFieldId;

    @FXML
    private Text fnameTextId;

    @FXML
    private ChoiceBox<String> genderChoiceId;

    @FXML
    private Text genderTextId;

    @FXML
    private TextField lastNameFieldId;

    @FXML
    private Text lnameTextId;

    @FXML
    private Text logInRedirId;

    @FXML
    private ImageView logoId;

    @FXML
    private PasswordField PasswordFieldId;

    @FXML
    private Text passwordTextId;

    @FXML
    private TextField phoneFieldId;

    @FXML
    private Text phoneTextId;

    @FXML
    private Button signUpButton;

    @FXML
    private AnchorPane windowId;

    @FXML
    private Text fnameAlertId;

    @FXML
    private Text lnameAlertId;

    @FXML
    private Text phoneAlertId;

    @FXML
    private Text emailAlertId;

    @FXML
    private Text passwordAlertId;

    @FXML
    private Button addPicButton;

    @FXML
    private Text pfpTextId;

    @FXML
    private TextField captchaField;

    @FXML
    private ImageView captchaImage;

    @FXML
    private Text captchaAlert;

    @FXML
    private ImageView regenerateBtn;

    String profile_picture;

    private final String[] genders = {"M", "F"};

    private String captchaText;

    @FXML
    public void initialize(URL arg0, ResourceBundle arg1) {
        genderChoiceId.setItems(FXCollections.observableArrayList(genders));
        genderChoiceId.getSelectionModel().selectFirst();
        Image image = new Image("file:src\\images\\logo.png");
        logoId.setImage(image);
        Image regenerate = new Image("file:src/images/refresh-page-option.png");
        regenerateBtn.setImage(regenerate);
        updateCaptcha();

    }

    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]+");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d{8}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");


    private boolean validateFields() throws SQLException {
        // Clear error messages and styles for all error fields
        fnameAlertId.setText("");
        lnameAlertId.setText("");
        emailAlertId.setText("");
        phoneAlertId.setText("");
        passwordAlertId.setText("");

        // Check for missing information
        if (firstNameFieldId.getText().isEmpty() || lastNameFieldId.getText().isEmpty() || emailFieldId.getText().isEmpty() ||
                PasswordFieldId.getText().isEmpty() || genderChoiceId.getValue() == null || phoneFieldId.getText().isEmpty() || datePickerId.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Fill in all fields.");
            alert.showAndWait();
            return false;
        }

        boolean valid = true; //my flag so error messages won't appear individually

        // Validate first name
        if (!NAME_PATTERN.matcher(firstNameFieldId.getText()).matches()) {
            fnameAlertId.setText("First name only contains letters.");
            fnameAlertId.setStyle("-fx-background-color: #ff4d4d;");
            valid = false;
        }

        // Validate last name
        if (!NAME_PATTERN.matcher(lastNameFieldId.getText()).matches()) {
            lnameAlertId.setText("Last name only contains letters.");
            lnameAlertId.setStyle("-fx-background-color: #ff4d4d;");
            valid = false;
        }

        // Validate email
        if (!EMAIL_PATTERN.matcher(emailFieldId.getText()).matches()) {
            emailAlertId.setText("Please enter a valid e-mail address.");
            emailAlertId.setStyle("-fx-background-color: #ff4d4d;");
            valid = false;
        }

        if (!us.isEmailUnique(emailFieldId.getText())) {
            emailAlertId.setText("An account with this email already exists.");
            emailAlertId.setStyle("-fx-background-color: #ff4d4d;");
            valid = false;
        }

        // Validate phone number
        if (!PHONE_PATTERN.matcher(phoneFieldId.getText()).matches()) {
            phoneAlertId.setText("Phone number must be 8 digits.");
            phoneAlertId.setStyle("-fx-background-color: #ff4d4d;");
            valid = false;
        }

        // Validate password
        if (!PASSWORD_PATTERN.matcher(PasswordFieldId.getText()).matches()) {
            passwordAlertId.setText("Password must contain at least one uppercase, one lowercase, one digit (8 characters).");
            passwordAlertId.setStyle("-fx-background-color: #ff4d4d;");
            valid = false;
        }

        if (!captchaText.equals(captchaField.getText())) {
            captchaAlert.setText("  X");
            captchaAlert.setStyle("-fx-background-color: #ff4d4d;");
            valid = false;
        }

        return valid;
    }

    @FXML
    void addUser(ActionEvent event) {
        try {
            // Validate input fields
            if (validateFields()) {
                // Input fields are valid, proceed with adding user
                boolean gender = genderChoiceId.getValue().equals("F");
                String phoneNumber = phoneFieldId.getText();
                if(profile_picture==null) {
                    us.addd(new User(firstNameFieldId.getText(), lastNameFieldId.getText(), emailFieldId.getText(), gender, true, Integer.parseInt(phoneNumber), Date.valueOf(datePickerId.getValue()), PasswordFieldId.getText(), "User"));
                }
                else {
                    us.addd(new User(firstNameFieldId.getText(), lastNameFieldId.getText(), emailFieldId.getText(), gender, true, Integer.parseInt(phoneNumber), Date.valueOf(datePickerId.getValue()), profile_picture, PasswordFieldId.getText(), "User"));

                }

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation");
                alert.setContentText("Sign Up Complete!");
                alert.showAndWait();

                // Get the current stage from any node in the scene graph
                Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Load the new FXML file
                Parent root = FXMLLoader.load(getClass().getResource("/LogIn.fxml"));
                javafx.scene.image.Image icon = new Image("file:/src/images/logo.png");

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
        } catch (SQLException e) {
            // Show error message for database exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (NumberFormatException e) {
            // Show error message if phone number parsing fails
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Invalid phone number format");
            alert.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void addProfilePicture(ActionEvent event) {
        // Create a new file chooser
        FileChooser fileChooser = new FileChooser();

        // Set the title for the file chooser dialog
        fileChooser.setTitle("Select Profile Picture");

        // Set the initial directory to the user's home directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Add filters to restrict file selection to image files only
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        // Show the file chooser dialog and wait for user selection
        File selectedFile = fileChooser.showOpenDialog(null);

        // Check if a file was selected
        if (selectedFile != null) {
            try {
                // Create a target directory if it doesn't exist
                File targetDir = new File("src/images");
                if (!targetDir.exists()) {
                    targetDir.mkdirs();
                }

                // Define the target file path
                String fileName = selectedFile.getName();
                System.out.println(fileName);
                profile_picture = fileName;
                Path targetPath = new File(targetDir, fileName).toPath();

                // Copy the selected file to the target directory
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Profile picture added successfully!");
                alert.showAndWait();
            } catch (IOException e) {
                // Show error message if an exception occurs during file copying
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error occurred while adding profile picture!");
                alert.showAndWait();
            }
        }
    }

    @FXML
    void HoverIn(MouseEvent event) {
        signUpButton.setStyle("-fx-background-color: #E15B10; -fx-background-radius: 55;");
    }

    @FXML
    void HoverOut(MouseEvent event) {
        signUpButton.setStyle("-fx-background-color: #E18B10; -fx-background-radius: 55;");
    }

    @FXML
    void HoverIn1(MouseEvent event) {
        addPicButton.setStyle("-fx-background-color: #d4c8ae;");
    }

    @FXML
    void HoverOut1(MouseEvent event) {
        addPicButton.setStyle("-fx-background-color: #fdf8db;");
    }

    @FXML
    void RedirectToLogin(ActionEvent event) throws IOException {
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

    private void updateCaptcha() {
        Cage cage = new Cage();
        String captchaText = cage.getTokenGenerator().next();

        // Generate captcha image data
        byte[] imageData = cage.draw(captchaText);

        // Create Image object from byte array
        javafx.scene.image.Image captchaImageSrc = new javafx.scene.image.Image(new ByteArrayInputStream(imageData));

        // Set the Image object to the ImageView
        captchaImage.setImage(captchaImageSrc);

        // Store captcha text somewhere to validate later
        this.captchaText = captchaText;
    }

    @FXML
    void regenerateCaptcha(MouseEvent event) {
        updateCaptcha();
    }

}
