package tn.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
import tn.esprit.utils.ProfileManager;
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
    private ImageView chatPic;

    @FXML
    private ImageView profilePictureId;

    @FXML
    private ImageView logoId;

    @FXML
    private FlowPane usersContainer;


    @FXML
    private Text uidTextId;

    @FXML
    private TextField inputSearch;

    @FXML
    private ChoiceBox<String> sortBox;

    @FXML
    private ImageView logoutButton;

    @FXML
    private ImageView searchBtn;

    @FXML
    private ImageView sortBtn;

    User currentUser;

    ObservableList<String> items = FXCollections.observableArrayList(
            "First Name",
            "Last Name",
            "Followers",
            "Role   - User",
            "Role   - Artist"
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image logo = new Image("file:src\\images\\logo.png");
        logoId.setImage(logo);
        Image search = new Image("file:src/images/search-interface-symbol.png");
        searchBtn.setImage(search);
        Image sort = new Image("file:src/images/sorting.png");
        sortBtn.setImage(sort);

        // Retrieve the UID from the session
        int uid = SessionManager.getInstance().getCurrentUserUid();
        try {
            currentUser = us.searchByUid(uid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Display the UID on the UI
        uidTextId.setText("Welcome, " + currentUser.getFname());
        Image profile = new Image("file:src\\images\\Profile-PNG-File.png");
        profilePictureId.setImage(profile);

        Image logout = new Image("file:src\\images\\sign-out-alt.png");
        logoutButton.setImage(logout);

        usersContainer.setPadding(new Insets(30)); // Adjust padding as needed
        usersContainer.setHgap(80); // Set horizontal gap between elements
        usersContainer.setVgap(20); // Set vertical gap between lines

        displayUsers();

        sortBox.setItems(items);

        // Set default selection
        sortBox.getSelectionModel();
    }

    public void userProfile() throws IOException {
        // Get the current stage from any node in the scene graph
        Stage oldStage = (Stage) usersContainer.getScene().getWindow();

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
    @FXML
    void userMenu(MouseEvent event) throws IOException {
        userProfile();
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
            Label roleLabel = new Label(user.getRole());
            nameLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            roleLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
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
            userBox.getChildren().add(roleLabel);

            // Attach event handler to the profile picture
            profileImage.setOnMouseClicked(event -> {
                if(user.getUid()== currentUser.getUid())
                {
                    try {
                        userProfile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    //increment profileViews
                    user.setProfileViews(user.getProfileViews()+1);
                    try {
                        us.update(user);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    // Redirect to the profile page for the selected user
                    System.out.println(user);
                    // Store the UID in the session
                    ProfileManager.getInstance().setCurrentUserUid(user.getUid());
                    redirectToProfilePage(user);
                }
            });

            // Add margin to the bottom of the VBox
            userBox.setMargin(profileImage, new Insets(10));
            userBoxes.add(userBox);
        }
        return userBoxes;
    }

    private void redirectToProfilePage(User user) {
        // Get the current stage (window)
        Stage currentStage = (Stage) usersContainer.getScene().getWindow(); // Replace yourAnchorPane with the root node of your current scene

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

            // Close the current window
            currentStage.close();

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void logout(MouseEvent event) throws IOException {
        //
        SessionManager.getInstance().clearSession();

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
    void searchUsers(MouseEvent event) {
            String input = inputSearch.getText();

            try {
                List<User> users = us.searchByName(input);
                System.out.println(users);

                // Clear existing user boxes before adding new ones
                usersContainer.getChildren().clear();

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

        public void sortUsers() {
            try {
                List<User> users = new ArrayList<>();;
                String selectedSortItem = sortBox.getValue(); // Get the selected item from the ChoiceBox

                if (selectedSortItem.equals("First Name")) {
                    System.out.println("fname");
                    users = us.SortByFirstName();
                    System.out.println(users);
                } else if (selectedSortItem.equals("Last Name")) {
                    System.out.println("lname");
                    users = us.SortByLastName();
                } else if (selectedSortItem.equals("Followers")) {
                    users = us.SortByFollowers();
                } else if (selectedSortItem.equals("Role   - User")) {
                    users = us.SortByRole("User");
                } else if (selectedSortItem.equals("Role   - Artist")) {
                    users = us.SortByRole("Artist");
                }

                // Clear existing user boxes before adding new ones
                usersContainer.getChildren().clear();

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

    @FXML
    void sort(MouseEvent event) {
        sortUsers();
    }

}
