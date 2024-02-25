package tn.esprit.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import tn.esprit.entities.User;

import java.net.URL;
import java.security.PrivateKey;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
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
    void userMenu(MouseEvent event) {

    }

    public void initData(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (user != null) {
            Image profile_pic = new Image("file:src\\images\\" + user.getProfile_pic());
            profilePicId.setImage(profile_pic);
            userId.setText(user.getFname().toUpperCase() + " " + user.getLname().toUpperCase());
        } else {
            // Handle the case where user is null
            // For example, display a default image or text
            System.out.println("User is null");
        }

        Image logo = new Image("file:src\\images\\logo.png");
        logoId.setImage(logo);

        Image account = new Image("file:src\\images\\Profile-PNG-File.png");
        accountId.setImage(account);
    }



}
