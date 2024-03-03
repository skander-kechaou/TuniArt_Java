package tn.esprit.Controllers;

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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.entities.Artist;
import tn.esprit.entities.User;
import tn.esprit.services.UserService;
import tn.esprit.utils.SessionManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditUser implements Initializable {

    UserService us = new UserService();

    User currentUser;
    String profile_picture;

    @FXML
    private ImageView accountId;

    @FXML
    private Button addButton;

    @FXML
    private TextArea biographyId;

    @FXML
    private DatePicker birthdayId;

    @FXML
    private Button editButton;

    @FXML
    private TextField emailId;

    @FXML
    private TextField fnameId;

    @FXML
    private TextField lnameId;

    @FXML
    private ImageView logoId;

    @FXML
    private PasswordField passwordId;

    @FXML
    private TextField portfolioId;

    @FXML
    private ImageView profilePicId;

    @FXML
    private Text userId;

    @FXML
    private TextField phoneId;

    @FXML
    private Button saveChangesButton;

    String portfolio;

    @FXML
    void addPortfolio(ActionEvent event) {
        // Create a new file chooser
        FileChooser fileChooser = new FileChooser();

        // Set the title for the file chooser dialog
        fileChooser.setTitle("Select Portfolio");

        // Set the initial directory to the user's home directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Add filters to restrict file selection to image files only
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.pdf", "*.jpg", "*.png")
        );

        // Show the file chooser dialog and wait for user selection
        File selectedFile = fileChooser.showOpenDialog(null);

        // Check if a file was selected
        if (selectedFile != null) {
            try {
                // Create a target directory if it doesn't exist
                File targetDir = new File("src/assets");
                if (!targetDir.exists()) {
                    targetDir.mkdirs();
                }

                // Define the target file path
                String fileName = selectedFile.getName();
                System.out.println(fileName);
                portfolio = fileName;
                Path targetPath = new File(targetDir, fileName).toPath();

                // Copy the selected file to the target directory
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Portfolio added successfully!");
                alert.showAndWait();
                portfolioId.setText(portfolio);
            } catch (IOException e) {
                // Show error message if an exception occurs during file copying
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error occurred while adding portfolio!");
                alert.showAndWait();
            }
        }
    }

    @FXML
    void changePfp(MouseEvent event) {
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
                Image newPfp = new Image(targetPath.toUri().toString());
                profilePicId.setImage(newPfp);
                profilePicId.getParent().requestLayout();

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
    void editUser(ActionEvent event) {
        try {
            String phoneNumber = phoneId.getText();
            currentUser.setFname(fnameId.getText());
            currentUser.setLname(lnameId.getText());
            currentUser.setEmail(emailId.getText());
            currentUser.setPassword(passwordId.getText());
            currentUser.setPhone_nb(Integer.parseInt(phoneNumber));
            currentUser.setBirth_date(Date.valueOf(birthdayId.getValue()));
            currentUser.setProfile_pic(profile_picture);

            if (!portfolioId.getText().isEmpty() || !biographyId.getText().isEmpty()) {
                // Create a new instance of Artist
                Artist currentArtist = new Artist();
                // Transfer data from currentUser to currentArtist
                currentArtist.setUid(currentUser.getUid());
                currentArtist.setFname(currentUser.getFname());
                currentArtist.setLname(currentUser.getLname());
                currentArtist.setEmail(currentUser.getEmail());
                currentArtist.setPassword(currentUser.getPassword());
                currentArtist.setPhone_nb(currentUser.getPhone_nb());
                currentArtist.setBirth_date(currentUser.getBirth_date());
                currentArtist.setProfile_pic(currentUser.getProfile_pic());
                // Set additional fields for Artist
                currentArtist.setRole("Artist");
                currentArtist.setBiography(biographyId.getText());
                currentArtist.setPortfolio(portfolioId.getText());
                // Update using the new Artist instance
                us.update(currentArtist);
            } else {
                // Update the user
                us.update(currentUser);
            }

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Profile updated successfully!");
            alert.showAndWait();

            // Load the new FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/Profile.fxml"));
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
        } catch (SQLException | NumberFormatException | IOException ex) {
            // Show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }




    @FXML
    void userMenu(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image logo = new Image("file:src\\images\\logo.png");
        logoId.setImage(logo);
        Image account = new Image("file:src\\images\\Profile-PNG-File.png");
        accountId.setImage(account);
        // Retrieve the UID from the session
        int uid = SessionManager.getInstance().getCurrentUserUid();
        try {
            currentUser = us.searchByUid(uid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (currentUser.getProfile_pic()==null) {
            Image noProfile = new Image("file:src\\images\\Profile-PNG-File.png");
            profilePicId.setImage(noProfile);
        }
        else {
            Image profile = new Image("file:src\\images\\"+currentUser.getProfile_pic());
            profilePicId.setImage(profile);
        }
        userId.setText(currentUser.getFname().toUpperCase()+" "+currentUser.getLname().toUpperCase());
        fnameId.setText(currentUser.getFname());
        lnameId.setText(currentUser.getLname());
        emailId.setText(currentUser.getEmail());
        phoneId.setText(String.valueOf(currentUser.getPhone_nb()));
        passwordId.setText(currentUser.getPassword());
        birthdayId.setValue(currentUser.getBirth_date().toLocalDate());
        if(currentUser.getRole().equals("Artist")) {
            Artist currentArtist = (Artist) currentUser;
            portfolioId.setText(currentArtist.getPortfolio());
            biographyId.setText(currentArtist.getBiography());
        }
    }

    @FXML
    void redirectHome(MouseEvent event) throws IOException {
        // Get the current stage from any node in the scene graph
        Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/Home.fxml"));
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
}
