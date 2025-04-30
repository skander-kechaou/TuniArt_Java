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
import tn.esprit.entities.Auction;
import tn.esprit.entities.User;
import tn.esprit.services.AuctionService;
import tn.esprit.services.UserService;
import tn.esprit.utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewAuctions implements Initializable {

    AuctionService AS = new AuctionService();
    @FXML
    private FlowPane auctionContainer;

    @FXML
    private ImageView logoId;

    @FXML
    private Text uidTextId;

    @FXML
    private ImageView logoutButton;

    @FXML
    private ImageView searchBtn;

    @FXML
    private ImageView sortBtn;

    @FXML
    private ImageView profilePictureId;


    User currentUser;
    UserService us = new UserService();

    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Retrieve the UID from the session
        int uid = SessionManager.getInstance().getCurrentUserUid();
        try {
            currentUser = us.searchByUid(uid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        uidTextId.setText("Welcome, " + currentUser.getFname());

        Image logo = new Image("file:src\\images\\logo.png");
        logoId.setImage(logo);
        Image search = new Image("file:src/images/search-interface-symbol.png");
        searchBtn.setImage(search);
        Image sort = new Image("file:src/images/sorting.png");
        sortBtn.setImage(sort);
        Image profile = new Image("file:src\\images\\Profile-PNG-File.png");
        profilePictureId.setImage(profile);

        Image logout = new Image("file:src\\images\\sign-out-alt.png");
        logoutButton.setImage(logout);

        displayAuctions();
    }

    private void displayAuctions() {
        try {
            List<Auction> auctions = AS.diplayList();

            //call the method to create user boxes and add them to the container
            List<VBox> auctionBoxes = createauctionBox(auctions);
            auctionContainer.getChildren().addAll(auctionBoxes);
            // Add padding to the auctionContainer
            auctionContainer.setPadding(new Insets(10)); // Adjust the value as needed
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private List<VBox> createauctionBox(List<Auction> auctions) {
        List<VBox> auctionBoxes = new ArrayList<>();
        for (Auction auction : auctions) {
            VBox auctionBox = new VBox();
            auctionBox.getStyleClass().add("auctionBox"); // Add style class to VBox
            //auctionBox.setStyle("-fx-padding: 30px; -fx-spacing: 20px; -fx-border-radius: 10px; -fx-background-color: #f7f8fa; -fx-border-color: #5dade2; ");
            auctionBox.setStyle("-fx-padding: 20px; -fx-spacing: 10px; -fx-background-color: #3B2A19; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

            // Set a fixed width for the VBox
            auctionBox.setPrefWidth(185); // Adjust width here (e.g., 200 pixels)

            // Add margin to the VBox
            auctionBox.setMargin(auctionBox, new Insets(10));

            // Add user name label
            Label nameLabel = new Label(auction.getAuction_name().toUpperCase());
            nameLabel.setTextFill(Color.WHITE);
            Label startdateLabel = new Label("Starts on "+auction.getStart_date().toString());
            startdateLabel.setTextFill(Color.WHITE);
            Label enddateLabel = new Label("Ends on "+auction.getEnd_date().toString());
            enddateLabel.setTextFill(Color.WHITE);
            Label thresholdLabel = new Label("Threshold : "+Float.toString(auction.getThreshold()));
            thresholdLabel.setTextFill(Color.WHITE);
            Label artrefLabel = new Label(Integer.toString(auction.getArt_ref()));
            artrefLabel.setTextFill(Color.WHITE);
            nameLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            startdateLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            enddateLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            thresholdLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            artrefLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            Button delete = new Button("Delete");
            Button update = new Button("Update");
            HBox buttonsBox = new HBox(10); // 10 is the spacing between buttons (adjust as needed)
            buttonsBox.getChildren().addAll( delete, update);


            update.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
            delete.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
            auctionBox.setAlignment(Pos.CENTER);
            auctionBox.getChildren().addAll(nameLabel,startdateLabel,enddateLabel,thresholdLabel,artrefLabel);

            // Add margin to the bottom of the VBox
            auctionBox.setPadding(new Insets(10, 50, 50, 10));
            auctionBox.setMargin(auctionBox, new Insets(10));
            auctionBoxes.add(auctionBox);

            delete.setOnAction(eventina -> {
                // Handle delete button action

                try {
                    AS.delete(auction.getAuction_ref()); // Call the delete method with the art_ref
                    System.out.println("Art_Piece deleted");

                    // Display a confirmation dialog
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Art Piece has been deleted successfully!");
                    alert.showAndWait();

                    // Remove the VBox containing the art piece from the UI
                    auctionBoxes.remove(auctionBox);
                    auctionContainer.getChildren().remove(auctionBox);

                } catch (SQLException e) {
                    // Display an error dialog if deletion fails
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Error deleting the Art Piece: " + e.getMessage());
                    alert.showAndWait();
                }
            });

            update.setOnAction(eventina -> {
                // Load the profile page FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfilePage.fxml"));
                Parent root;
                try {
                    root = loader.load();
                    //ProfilePageController controller = loader.getController();

                    // Pass the selected user's information to the profile page controller
                    //controller.initData(user);

                    // Display the profile page
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        return auctionBoxes;
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
    void userMenu(MouseEvent event) throws IOException {
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
    public void go_to_events(javafx.event.ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/showEvents.fxml"));
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
    void go_to_auctions(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/ViewAuctions.fxml"));
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
    void upload_auction(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/addAuction.fxml"));
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
}
