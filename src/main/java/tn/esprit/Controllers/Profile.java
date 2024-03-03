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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import tn.esprit.entities.Artist;
import tn.esprit.entities.User;
import tn.esprit.services.UserService;
import tn.esprit.utils.SessionManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Profile implements Initializable {

    UserService us = new UserService();
    User currentUser;
    Artist currentArtist;

    @FXML
    private ImageView accountId;

    @FXML
    private Button deactivateAccountButton;

    @FXML
    private Button editButton;

    @FXML
    private ImageView logoId;

    @FXML
    private ImageView profilePicId;

    @FXML
    private Text userId;

    @FXML
    private Text followers;

    @FXML
    private Text following;

    @FXML
    private Text biography;

    @FXML
    private Text nbViews;

    @FXML
    private Text portfolio;

    @FXML
    private Hyperlink portfolioLink;

    @FXML
    void editUser(ActionEvent event) throws IOException {
        // Get the current stage from any node in the scene graph
        Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/EditUser.fxml"));
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
        try {
            followers.setText("Followers : "+String.valueOf(us.countFollowers(currentUser.getUid())));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            following.setText("Following : "+String.valueOf(us.countFollowing(currentUser.getUid())));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        nbViews.setText("Number of Views : "+currentUser.getProfileViews());

        if (!currentUser.getRole().equals("Artist")) {
            portfolio.setText("");
            portfolioLink.setText("");
            biography.setText("");
        }
        else {
            try {
                currentArtist = us.searchArtistByUid(uid);
                System.out.println(currentArtist);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            portfolio.setText("Portfolio : ");
            portfolioLink.setText("Link");
            biography.setText("Biography : "+currentArtist.getBiography());
        }
    }

    @FXML
    void deleteUser(ActionEvent event) throws SQLException, IOException {
        us.delete(currentUser.getUid());
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
        Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        oldStage.close();

        // Show the new stage
        newStage.show();

        System.out.println("moved");
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

    @FXML
    void openPortfolio(ActionEvent event) {
        // Replace filePath with the path to your PDF, JPG, or PNG file
        int uid = SessionManager.getInstance().getCurrentUserUid();
        try {
            currentArtist = us.searchArtistByUid(uid);
            System.out.println(currentArtist);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String filePath = "file:src\\assets\\"+currentArtist.getPortfolio(); // Change this to your file path

        File file = new File(filePath);

        if (file.exists()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle file opening error
            }
        } else {
            System.out.println("File does not exist.");
            // Handle file not found error
        }
    }

    private void generateUserInfoPDF(User user) {
        try {
            // Create a new document
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // Create a new content stream
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Picture Path
            String picturePath = "src/images/" + user.getProfile_pic();

            // Add profile picture
            PDImageXObject profileImage = PDImageXObject.createFromFile(picturePath, document);
            float imageWidth = 200;
            float imageHeight = 200;
            float centerX = (PDRectangle.A4.getWidth() - imageWidth) / 2;
            float y = PDRectangle.A4.getHeight() - imageHeight - 50; // Adjust the vertical position as needed
            contentStream.drawImage(profileImage, centerX, y, imageWidth, imageHeight);

            // Set font and font size
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

            // Gender
            String gender = user.getGender() ? "Female" : "Male";

            // Write user information to the content stream
            float textX = 100; // Adjust the starting position as needed
            float textY = y - 100; // Adjust the vertical position as needed
            contentStream.beginText();
            contentStream.newLineAtOffset(textX, textY);

            // Write each line of text with proper spacing
            contentStream.showText("First Name: " + user.getFname());
            contentStream.newLineAtOffset(0, -15);

            contentStream.showText("Last Name: " + user.getLname());
            contentStream.newLineAtOffset(0, -15);

            contentStream.showText("Email: " + user.getEmail());
            contentStream.newLineAtOffset(0, -15);

            contentStream.showText("Phone Number: " + user.getPhone_nb());
            contentStream.newLineAtOffset(0, -15);

            contentStream.showText("Gender: " + gender);
            contentStream.newLineAtOffset(0, -15);

            contentStream.showText("Birthday: " + user.getBirth_date());
            contentStream.newLineAtOffset(0, -15);

            contentStream.showText("Role: " + user.getRole());
            contentStream.newLineAtOffset(0, -15);

            if (user.getRole().equals("Artist")) {
                Artist artist = us.searchArtistByUid(user.getUid());
                contentStream.showText("Portfolio: " + artist.getPortfolio());
                contentStream.newLineAtOffset(0, -15);

                contentStream.showText("Biography: " + artist.getBiography());
            }
            contentStream.endText();

            // Close the content stream
            contentStream.close();

            // fileName
            String fileName = "User" + user.getUid() + "-" + user.getFname() + System.currentTimeMillis() + ".pdf";
            String filePath = "src\\assets\\" + fileName;

            // Save the document
            document.save(filePath);

            // Close the document
            document.close();

            System.out.println("PDF created successfully.");
            try {
                File file = new File(filePath);
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void generatePDF(ActionEvent event) {
        generateUserInfoPDF(currentUser);
    }
}
