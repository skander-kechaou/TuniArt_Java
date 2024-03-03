package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import tn.esprit.entities.Artist;
import tn.esprit.entities.User;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.PrivateKey;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import tn.esprit.services.UserService;
import tn.esprit.utils.ProfileManager;
import tn.esprit.utils.SessionManager;

public class ProfilePage implements Initializable {

    private User user;

    @FXML
    private ImageView accountId;

    @FXML
    private ImageView logoId;

    @FXML
    private ImageView profilePicId;

    @FXML
    private Text userId;

    @FXML
    private Button subscribeButton;

    @FXML
    private Text followers;

    @FXML
    private Text following;

    @FXML
    private Text portfolio;

    @FXML
    private Hyperlink portfolioLink;

    @FXML
    private Text biography;

    @FXML
    private Text nbViews;

    User selectedUser, currentUser;
    Artist selectedArtist;

    @FXML
    void userMenu(MouseEvent event) {

    }

    public void initData(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public User getData() {return selectedUser;}
    UserService us = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int uid = ProfileManager.getInstance().getCurrentUserUid();
        int uidCon = SessionManager.getInstance().getCurrentUserUid();
        try {
            currentUser = us.searchByUid(uidCon);
            System.out.println("current user : "+currentUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            selectedUser = us.searchByUid(uid);
            System.out.println("selected user : "+selectedUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (selectedUser != null) {
            if (selectedUser.getProfile_pic()==null) {
                Image defaultPic = new Image("file:src\\images\\Profile-PNG-File.png");
                profilePicId.setImage(defaultPic);
            }
            else {
                Image profile_pic = new Image("file:src\\images\\" + selectedUser.getProfile_pic());
                profilePicId.setImage(profile_pic);
            }
            userId.setText(selectedUser.getFname().toUpperCase() + " " + selectedUser.getLname().toUpperCase());
        } else {
            // Handle the case where user is null
            // For example, display a default image or text
            System.out.println("User is null");
        }

        Image logo = new Image("file:src\\images\\logo.png");
        logoId.setImage(logo);

        Image account = new Image("file:src\\images\\Profile-PNG-File.png");
        accountId.setImage(account);

        setupUI();

        nbViews.setText("Number of Views : "+selectedUser.getProfileViews());

        if (!selectedUser.getRole().equals("Artist")) {
           portfolio.setText("");
           portfolioLink.setText("");
           biography.setText("");
        }
        else {
            try {
                selectedArtist = us.searchArtistByUid(uid);
                System.out.println(selectedArtist);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            portfolio.setText("Portfolio : ");
            portfolioLink.setText("Link");
            biography.setText("Biography : "+selectedArtist.getBiography());
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

    @FXML
    void subscribe(ActionEvent event) throws SQLException {
        if (us.isFollowing(currentUser.getUid(), selectedUser.getUid())) {
            subscribeButton.setText("Unfollow");
            us.deleteFollower(currentUser.getUid(), selectedUser.getUid());
        }
        else {
            subscribeButton.setText("Follow");
            us.addFollower(currentUser.getUid(), selectedUser.getUid());
        }
        // Force a refresh of the UI
        setupUI();
    }

    private void setupUI() {
        try {
            followers.setText("Followers : "+String.valueOf(us.countFollowers(selectedUser.getUid())));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            following.setText("Following : "+String.valueOf(us.countFollowing(selectedUser.getUid())));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            if (us.isFollowing(currentUser.getUid(), selectedUser.getUid())) {
                subscribeButton.setText("Unfollow");
            } else {
                subscribeButton.setText("Follow");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void openPortfolio(ActionEvent event) {
        // Replace filePath with the path to your PDF, JPG, or PNG file
        int uid = ProfileManager.getInstance().getCurrentUserUid();
        try {
            selectedArtist = us.searchArtistByUid(uid);
            System.out.println(selectedArtist);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String filePath = "src\\assets\\"+selectedArtist.getPortfolio(); // Change this to your file path

        File file = new File(filePath);
        System.out.println(file.getAbsoluteFile());

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

    @FXML
    void message(ActionEvent event) throws IOException {
        String url = "https://accounts.google.com/v3/signin/identifier?authuser=0&continue=https%3A%2F%2Fwww.google.com%2Fsearch%3Fq%3Demail%2Bvalidator%2Bapi%2Bjava%26sca_esv%3Dbd7edbb0e5968ec5%26ei%3DLl_kZYebIKuC9u8P4-yIkAo%26ved%3D0ahUKEwiH6u7q_teEAxUrgf0HHWM2AqIQ4dUDCBA%26uact%3D5%26oq%3Demail%2Bvalidator%2Bapi%2Bjava%26gs_lp%3DEgxnd3Mtd2l6LXNlcnAiGGVtYWlsIHZhbGlkYXRvciBhcGkgamF2YTIIEAAYgAQYogQyCBAAGIAEGKIESOgLUOUDWJIKcAF4AZABAJgBuQGgAZ4EqgEDMC40uAEDyAEA-AEBmAIFoALBBMICChAAGEcY1gQYsAPCAg0QABiABBiKBRhDGLADwgIIEAAYBxgeGBPCAgcQABiABBgTwgIIEAAYHhgTGArCAgYQABgeGBPCAggQABgIGB4YE8ICCBAAGAgYBxgemAMAiAYBkAYKkgcDMS40%26sclient%3Dgws-wiz-serp&ec=GAlAAQ&hl=fr&flowName=GlifWebSignIn&flowEntry=AddSession&dsh=S2047610956%3A1709465783240682&theme=glif";
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }


}
