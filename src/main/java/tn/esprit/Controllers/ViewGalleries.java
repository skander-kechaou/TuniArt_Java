package tn.esprit.Controllers;

import javafx.event.ActionEvent;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tn.esprit.entities.Art_Piece;
import tn.esprit.entities.User;
import tn.esprit.entities.gallery;
import tn.esprit.services.UserService;
import tn.esprit.services.galleryService;
import tn.esprit.utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewGalleries implements Initializable {

    galleryService as = new galleryService();
    UserService us = new UserService();
    User currentUser;

    @FXML
    private ImageView logoutButton;

    @FXML
    private MenuBar Menu_id;

    @FXML
    private Button go_to_arts;

    @FXML
    private Button go_to_galleries;

    @FXML
    private ImageView profilePictureId;

    @FXML
    private ImageView logoId;

    @FXML
    private FlowPane artsContainer;

    @FXML
    private ImageView printId;

    @FXML
    private ImageView searchBtn;

    @FXML
    private ImageView sortBtn;


    @FXML
    private Text uidTextId;
    @FXML
    private Button upload_art;



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
        logoId.setImage(logo);
        Image print = new Image("file:src\\images\\tun.png");
        printId.setImage(print);

        Image search = new Image("file:src/images/search-interface-symbol.png");
        searchBtn.setImage(search);
        Image sort = new Image("file:src/images/sorting.png");
        sortBtn.setImage(sort);

        Image profile = new Image("file:src\\images\\Profile-PNG-File.png");
        profilePictureId.setImage(profile);

        Image logout = new Image("file:src\\images\\sign-out-alt.png");
        logoutButton.setImage(logout);

        artsContainer.setPadding(new Insets(30)); // Adjust padding as needed
        artsContainer.setHgap(80); // Set horizontal gap between elements
        artsContainer.setVgap(20); // Set vertical gap between lines
//style="-fx-background-color: #3B2A19; -fx-background-radius: 55;
        displayArts();
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

    private void displayArts() {
        try {
            List<gallery> galleries = as.diplayList();
            System.out.println("in displayarts");
            // Call the method to create art boxes and add them to the container
            List<VBox> artBoxes = createArtBox(galleries);
            artsContainer.getChildren().addAll(artBoxes);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private List<VBox> createArtBox(List<gallery> arts) {
        List<VBox> artBoxes = new ArrayList<>();
        for (gallery art : arts) {
            System.out.println(art);
            VBox artBox = new VBox();
            artBox.getStyleClass().add("artBox"); // Add style class to VBox
            //artBox.setStyle("-fx-padding: 30px; -fx-spacing: 20px; -fx-border-radius: 10px; -fx-background-color: #f7f8fa; -fx-border-color: #5dade2; ");
            artBox.setStyle("-fx-padding: 20px; -fx-spacing: 10px; -fx-background-color: #f7f8fa; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

            // Set a fixed width for the VBox
            artBox.setPrefWidth(185); // Adjust width here (e.g., 200 pixels)

            // Add margin to the VBox
            artBox.setMargin(artBox, new Insets(10));

            // Add art name label
            Button updateButton = new Button("Update");
            Button deleteButton = new Button("Delete");
            HBox buttonsBox = new HBox(10); // 10 is the spacing between buttons (adjust as needed)
            buttonsBox.getChildren().addAll( deleteButton, updateButton);
            buttonsBox.setPadding(new Insets(10, 50, 20, 10));
            buttonsBox.setAlignment(Pos.CENTER);

            Label title_label = new Label(art.getGallery_name().toUpperCase());
            Label price_label = new Label(art.getGallery_location().toUpperCase());
            Label category_label = new Label(art.getOperating_hours().toUpperCase());
            title_label.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 16));
            title_label.setPadding(new Insets(15));
            title_label.setTextFill(Color.WHITE);
            category_label.setTextFill(Color.WHITE);
            //style="-fx-background-color: E18B10; -fx-background-radius: 55" text="Upload Your Image" textFill="WHITE"
            updateButton.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
            deleteButton.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
            price_label.setTextFill(Color.WHITE);// Set text color to white
            artBox.setAlignment(Pos.CENTER);
            artBox.getChildren().addAll(title_label, price_label,category_label);

            updateButton.setOnAction(event -> {
                // Handle upload button action
                try {

                    Parent root = FXMLLoader.load(getClass().getResource("/Update_Gallery.fxml"));
                    javafx.scene.image.Image icon = new Image("file:/src/images/logo.png");

                    // Create a new stage for the new window
                    Stage newStage = new Stage();
                    newStage.getIcons().add(icon);

                    // Set the scene with the new root
                    Scene scene = new Scene(root);
                    newStage.setScene(scene);
                    newStage.setTitle("Update Gallery");

                    // Close the old stage
                    Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    oldStage.close();

                    // Show the new stage
                    newStage.show();

                    System.out.println("moved");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            deleteButton.setOnAction(event -> {
                // Handle delete button action

                try {
                    as.delete(art.getGallery_id()); // Call the delete method with the art_ref
                    System.out.println("Gallery deleted");

                    // Display a confirmation dialog
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Gallery has been deleted successfully!");
                    alert.showAndWait();

                    // Remove the VBox containing the art piece from the UI
                    artBoxes.remove(artBox);
                    artsContainer.getChildren().remove(artBox);
                } catch (SQLException e) {
                    // Display an error dialog if deletion fails
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Error deleting the Gallery: " + e.getMessage());
                    alert.showAndWait();
                }
            });
            /*ImageView artImage;
            if(art.getImage_path()==null) {
                System.out.println("purr");
                artImage = new ImageView(new Image("file:src\\images\\default.png"));
            }
            else {
                artImage = new ImageView(new Image("file:src\\images\\" + art.getImage_path()));
            }
            artImage.setFitWidth(100);
            artImage.setFitHeight(100);
            artBox.getChildren().add(artImage);

            // Attach event handler to the profile picture
            artImage.setOnMouseClicked(event -> {
                // Redirect to the profile page for the selected art
                redirectToProfilePage(art);
            });

            // Add margin to the bottom of the VBox
            artBox.setMargin(artImage, new Insets(10));

            // Attach event handler to the profile picture
            artImage.setOnMouseClicked(event -> {
                // Redirect to the profile page for the selected art
                redirectToProfilePage(Art_Piece);
            });*/

            // Add margin to the bottom of the VBox
            artBox.setMargin(artBox, new Insets(8));
            artBox.setStyle("-fx-background-color: #3B2A19; -fx-background-radius: 55; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");
            artBox.setAlignment(Pos.CENTER);

            artBox.getChildren().addAll(buttonsBox);
            artBoxes.add(artBox);
        }
        return artBoxes;
    }

    private void redirectToProfilePage(Art_Piece art) {
        // Load the profile page FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfilePage.fxml"));
        Parent root;
        try {
            root = loader.load();
            //ProfilePageController controller = loader.getController();

            // Pass the selected art's information to the profile page controller
            //controller.initData(art);

            // Display the profile page
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void upload_art(javafx.event.ActionEvent event) throws IOException {

        // Load the new FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/add_gallery.fxml"));
        javafx.scene.image.Image icon = new Image("file:/src/images/logo.png");

        // Create a new stage for the new window
        Stage newStage = new Stage();
        newStage.getIcons().add(icon);

        // Set the scene with the new root
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.setTitle("Tuni'Art");

        // Close the old stage
        Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        oldStage.close();

        // Show the new stage
        newStage.show();

        System.out.println("moved");

    }



    @FXML
    public void go_to_arts(javafx.event.ActionEvent actionEvent) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/gallery.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        javafx.scene.image.Image icon = new Image("file:/src/images/logo.png");

        // Create a new stage for the new window
        Stage newStage = new Stage();
        newStage.getIcons().add(icon);

        // Set the scene with the new root
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.setTitle("Tuni'Art");

        // Close the old stage
        Stage oldStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        oldStage.close();

        // Show the new stage
        newStage.show();

        System.out.println("moved");

    }
    @FXML
    public void go_to_galleries(javafx.event.ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/View_Galleries.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        javafx.scene.image.Image icon = new Image("file:/src/images/logo.png");

        // Create a new stage for the new window
        Stage newStage = new Stage();
        newStage.getIcons().add(icon);

        // Set the scene with the new root
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.setTitle("Tuni'Art");

        // Close the old stage
        Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        oldStage.close();

        // Show the new stage
        newStage.show();

        System.out.println("moved");
    }

    @FXML
    void go_to_users(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Home.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        javafx.scene.image.Image icon = new Image("file:/src/images/logo.png");

        // Create a new stage for the new window
        Stage newStage = new Stage();
        newStage.getIcons().add(icon);

        // Set the scene with the new root
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.setTitle("Tuni'Art");

        // Close the old stage
        Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        oldStage.close();

        // Show the new stage
        newStage.show();

        System.out.println("moved");
    }

    @FXML
    void userMenu(MouseEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Profile.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        javafx.scene.image.Image icon = new Image("file:/src/images/logo.png");

        // Create a new stage for the new window
        Stage newStage = new Stage();
        newStage.getIcons().add(icon);

        // Set the scene with the new root
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.setTitle("Tuni'Art");

        // Close the old stage
        Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        oldStage.close();

        // Show the new stage
        newStage.show();
    }

    @FXML
    void redirectHome(MouseEvent event) throws IOException {
        // Get the current stage from any node in the scene graph
        Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/gallery.fxml"));
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
