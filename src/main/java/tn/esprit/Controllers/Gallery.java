package tn.esprit.Controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.entities.Art_Piece;
import tn.esprit.entities.Cart;
import tn.esprit.entities.User;
import tn.esprit.services.Art_PieceService;
import tn.esprit.services.CartService;
import tn.esprit.services.UserService;
import tn.esprit.utils.MyDatabase;
import tn.esprit.utils.SessionManager;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Gallery implements Initializable {

    private Tooltip cartTooltip = new Tooltip();

    private Connection connection;

    Art_PieceService as = new Art_PieceService();
    UserService us = new UserService();
    User currentUser;

    @FXML
    private MenuBar Menu_id;

    @FXML
    private Button go_to_arts;
    @FXML
    private Button go_to_galleries;

    @FXML
    private ImageView profilePictureId;

    @FXML
    private ImageView cartId;

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
    private TextField inputSearch;

    @FXML
    private ChoiceBox<String> sortBox;

    @FXML
    private ImageView logoutButton;

    @FXML
    private Text uidTextId;
    @FXML
    private Button upload_art;
    @FXML
    private Button go_to_users;

    ObservableList<String> items = FXCollections.observableArrayList(
            "Art Title",
            "Art Views"
    );

    private String generateQRContent(Art_Piece artPiece) {
        // Generate the content for the QR code based on the art piece details
        // You can format the content as needed, e.g., concatenating different fields
        String qrContent = "Art Title: " + artPiece.getArt_title() + "\n";
        qrContent += "Price: " + artPiece.getArt_price() + "\n";
        qrContent += "Type: " + artPiece.getType() + "\n";
        qrContent += "Style: " + artPiece.getStyle() + "\n";
        qrContent += "Creation: " + artPiece.getCreation() + "\n";
        qrContent += "Description: " + artPiece.getDescription() + "\n";
        qrContent += "Number of views" +artPiece.getArt_views() + "\n";
        // Add more details as needed
        return qrContent;
    }
    public static Image generateQRCodeImage(String content) {
        try {
            // Set QR code parameters
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.L);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Generate QR code
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 200, 200, hints);

            // Convert BitMatrix to BufferedImage
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Convert BufferedImage to JavaFX Image
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            javax.imageio.ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            byteArrayOutputStream.flush();
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return new Image(new ByteArrayInputStream(byteArray));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

        Image cart = new Image("file:src\\images\\panier.png");
        cartId.setImage(cart);
        Image logo = new Image("file:src\\images\\logo.png");
        logoId.setImage(logo);
        Image print = new Image("file:src\\images\\tun.png");
        printId.setImage(print);

        artsContainer.setPadding(new Insets(30)); // Adjust padding as needed
        artsContainer.setHgap(80); // Set horizontal gap between elements
        artsContainer.setVgap(20); // Set vertical gap between lines

        Image search = new Image("file:src/images/search-interface-symbol.png");
        searchBtn.setImage(search);
        Image sort = new Image("file:src/images/sorting.png");
        sortBtn.setImage(sort);

        Image profile = new Image("file:src\\images\\Profile-PNG-File.png");
        profilePictureId.setImage(profile);

        Image logout = new Image("file:src\\images\\sign-out-alt.png");
        logoutButton.setImage(logout);

        displayArts();

        sortBox.setItems(items);

        // Set default selection
        sortBox.getSelectionModel();
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
            List<Art_Piece> arts = as.diplayList();
            System.out.println("in displayarts");
            // Call the method to create art boxes and add them to the container
            List<VBox> artBoxes = createArtBox(arts);
            artsContainer.getChildren().addAll(artBoxes);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private List<VBox> createArtBox(List<Art_Piece> arts) {
        List<VBox> artBoxes = new ArrayList<>();
        final MediaPlayer[] mediaPlayer = {null};

        for (Art_Piece art : arts) {
            System.out.println(art);
            VBox artBox = new VBox();
            artBox.getStyleClass().add("artBox"); // Add style class to VBox
            //artBox.setStyle("-fx-padding: 30px; -fx-spacing: 20px; -fx-border-radius: 10px; -fx-background-color: #f7f8fa; -fx-border-color: #5dade2; ");
            artBox.setStyle("-fx-padding: 20px; -fx-spacing: 30px; -fx-background-color: #f7f8fa; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

            // Set a fixed width for the VBox
            artBox.setPrefWidth(330); // Adjust width here (e.g., 200 pixels)

            // Add margin to the VBox
            artBox.setMargin(artBox, new Insets(10));

            // Add art name label
            Button updateButton = new Button("Update");
            updateButton.setUserData(art.getArt_ref());
            Button deleteButton = new Button("Delete");
            Button playButton = new Button("Play");
            Button AddCart = new Button("Add to Cart");
            Label Filller = new Label("     ");
            HBox buttonsBox = new HBox(10); // 10 is the spacing between buttons (adjust as needed)
            buttonsBox.getChildren().addAll( Filller, playButton, AddCart);
            buttonsBox.setPadding(new Insets(10, 50, 20, 10));
            buttonsBox.setAlignment(Pos.CENTER);

            Label title_label = new Label(art.getArt_title().toUpperCase());

            Label price_label = new Label(String.valueOf(art.getArt_price())+"DT");
            Label category_label = new Label(art.getType().toUpperCase());
            title_label.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 16));
            title_label.setPadding(new Insets(15));
            title_label.setTextFill(Color.WHITE);
            category_label.setTextFill(Color.WHITE);
            //style="-fx-background-color: E18B10; -fx-background-radius: 55" text="Upload Your Image" textFill="WHITE"
            AddCart.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
            deleteButton.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
            playButton.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
            price_label.setTextFill(Color.WHITE);// Set text color to white

            artBox.setAlignment(Pos.CENTER);
            artBox.getChildren().addAll( title_label,price_label,category_label);


            updateButton.setOnAction(event -> {
                // Handle upload button action
                try {
                    int primaryKey = (int) updateButton.getUserData();

                    Parent root = FXMLLoader.load(getClass().getResource("/Update_Art.fxml"));
                    javafx.scene.image.Image icon = new Image("file:/src/images/logo.png");

                    // Pass primary key to update page controller

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Update_Art.fxml"));
                    Parent updateRoot = loader.load();
                    UpdateArt controller = loader.getController();
                    if (controller != null) {
                        controller.setPrimaryKey(primaryKey);
                    } else {
                        // Handle null controller
                        System.err.println("Controller is null");
                    }


                    // Create a new stage for the new window

                    Stage updateStage = new Stage();
                    updateStage.setScene(new Scene(updateRoot));
                    updateStage.setTitle("Update Art");
                    updateStage.show();

                    // Set the scene with the new root
                    Scene scene = new Scene(root);


                    // Close the old stage
                    Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    oldStage.close();

                    // Show the new stage


                    System.out.println("moved");
            } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                });

            AddCart.setOnAction(event -> {
                // Step 0: Validate Art ID
                System.out.println("selected art : "+art);
                String artIdText = String.valueOf(art.getArt_ref());
                if (artIdText.isEmpty()) {
                    showAlert("Invalid Input", "Please enter a valid Art ID.");
                    return;
                }

                try {
                    // Step 1: Retrieve Art Information
                    int artId = art.getArt_ref();

                    // Step 2: Check if the art exists and is available
                    if (art == null) {
                        showAlert("Art not found", "The selected art does not exist.");
                        return;
                    } else if (!art.isAvailable()) {
                        showAlert("Art not available", "The selected art is not available.");
                        return;
                    }

                    // Step 3: Add to Cart
                    Cart cart = new Cart();
                    cart.setUserId(currentUser.getUid()); // Assuming the user ID is 1
                    cart.setArtRef(artId); // Set the art reference

                    // Update the art availability status
                    art.setAvailable(false); // Assuming there's a method to update the availability status

                    // Save the cart to the database
                    CartService cartService = new CartService();
                    cartService.save(cart);

                    // Step 4: Update UI
                    showAlert("Art added to cart", "The selected art has been added to your cart.");

                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Please enter a valid Art ID.");
                }
            });

            deleteButton.setOnAction(event -> {
                // Handle delete button action

                try {
                    as.delete(art.getArt_ref()); // Call the delete method with the art_ref
                    System.out.println("Art Piece deleted");

                    // Display a confirmation dialog
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Art Piece has been deleted successfully!");
                    alert.showAndWait();

                    // Remove the VBox containing the art piece from the UI
                    artBoxes.remove(artBox);
                    artsContainer.getChildren().remove(artBox);
                } catch (SQLException e) {
                    // Display an error dialog if deletion fails
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Error deleting the Art Piece: " + e.getMessage());
                    alert.showAndWait();
                }
            });

            playButton.setOnAction(event -> {
                String musicPath = "src\\music\\"+art.getMusic_path();
                // Get the music path from the current art piece
                if (mediaPlayer[0] == null) {
                    // Initialize mediaPlayer if it's null
                    if (musicPath != null) {
                        // Create a media object with the music path
                        Media media = new Media(new File(musicPath).toURI().toString());
                        mediaPlayer[0] = new MediaPlayer(media);
                    } else {
                        // Show an error message if no music path is found
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("No music found for this art piece!");
                        alert.showAndWait();
                        return; // Exit the method if there's no music path
                    }

                    // Add a status listener to handle playback status changes
                    mediaPlayer[0].statusProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue == MediaPlayer.Status.PLAYING) {
                            playButton.setText("Pause");
                        } else {
                            playButton.setText("Play");
                        }
                    });

                    mediaPlayer[0].setOnEndOfMedia(() -> {
                        // When the music reaches the end, stop playback
                        mediaPlayer[0].stop();
                        playButton.setText("Play");
                    });
                }

                // Toggle between play and pause
                if (mediaPlayer[0].getStatus() == MediaPlayer.Status.PLAYING) {
                    mediaPlayer[0].pause();
                } else {
                    mediaPlayer[0].play();
                }
            });

            ImageView artImage;
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
                art.incrementArtViews();
                String qrContent = generateQRContent(art); // Generate the content for the QR code
                Image qrCodeImage = generateQRCodeImage(qrContent); // Generate the QR code image

                // Create a new window or dialog to display the QR code
                Stage qrCodeStage = new Stage();
                ImageView qrCodeImageView = new ImageView(qrCodeImage);
                Scene qrCodeScene = new Scene(new Group(qrCodeImageView));
                qrCodeStage.setScene(qrCodeScene);
                qrCodeStage.setTitle("QR Code for " + art.getArt_title());

                // Show the window/dialog containing the QR code
                qrCodeStage.show();
            });


            // Add margin to the bottom of the VBox
            artBox.setMargin(artImage, new Insets(10));
            
            /*// Attach event handler to the profile picture
            artImage.setOnMouseClicked(event -> {
                // Redirect to the profile page for the selected art
                redirectToProfilePage(Art_Piece);
            });*/

            // Add margin to the bottom of the VBox
            artBox.setMargin(artBox, new Insets(8));
            artBox.setStyle("-fx-background-color: #3B2A19; -fx-background-radius: 55; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");
            artBox.setAlignment(Pos.CENTER);

            artBoxes.add(artBox);
            artBox.getChildren().addAll( buttonsBox);
        }
        return artBoxes;
    }

    @FXML
    public void upload_art(javafx.event.ActionEvent event) throws IOException {

        // Load the new FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/submit_art.fxml"));
        javafx.scene.image.Image icon = new Image("file:/src/images/logo.png");

        // Create a new stage for the new window
        Stage newStage = new Stage();
        newStage.getIcons().add(icon);

        // Set the scene with the new root
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.setTitle("Submit Art");

        // Close the old stage
        Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        oldStage.close();

        // Show the new stage
        newStage.show();

        System.out.println("moved");

    }
    @FXML
    public void openGalleriesPage(ActionEvent event) {


    }
    @FXML
    void go_to_arts(ActionEvent event) {
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
        Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        oldStage.close();

        // Show the new stage
        newStage.show();

        System.out.println("moved");

    }

    @FXML
    void go_to_galleries(ActionEvent event) {
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

    public void sortArts() {
        try {
            List<Art_Piece> arts = new ArrayList<>();;
            String selectedSortItem = sortBox.getValue(); // Get the selected item from the ChoiceBox

            if (selectedSortItem.equals("Art Title")) {
                System.out.println("title");
                arts = as.sortbynamediplayList();
            } else if (selectedSortItem.equals("Art Views")) {
                System.out.println("views");
                arts = as.sortbyviewsdiplayList(); }


            // Clear existing user boxes before adding new ones
            artsContainer.getChildren().clear();

                // Call the method to create art boxes and add them to the container
                List<VBox> artBoxes = createArtBox(arts);
                artsContainer.getChildren().addAll(artBoxes);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void searchArts(MouseEvent event) {
        String input = inputSearch.getText();

        try {
            List<Art_Piece> arts = as.searchByTitle(input);
            System.out.println(arts);

            // Clear existing user boxes before adding new ones
            artsContainer.getChildren().clear();

            // Call the method to create user boxes and add them to the container
            List<VBox> artBoxes = createArtBox(arts);
            artsContainer.getChildren().addAll(artBoxes);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void sort(MouseEvent event) {
        sortArts();
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

    public void initializeOnCloseRequest(Stage stage) {
        // Set an OnCloseRequest event
        stage.setOnCloseRequest(event -> {
            // Show confirmation alert before closing the stage
            boolean confirmed = showConfirmationAlert("Order Confirmation", "do you want to confirm your order?");
            if (!confirmed) {
                event.consume(); // Consume the event to prevent the stage from closing
            }
        });
    }
    public void clearCartForUser(int userId) throws SQLException {


        // You can keep this method as it is, deleting from the database if needed
        System.out.println("15");

        if (MyDatabase.getInstance().getConn() == null){
            System.err.println("Connection is null. Cannot clear cart.");
            return;
        }
        System.out.println("15");
        String sql = "DELETE FROM cart WHERE uid = ?";
        //Connection connection;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            statement.executeUpdate();
        }
        System.out.println("150");

        String updateArtSql = "UPDATE art SET isAvailable = true WHERE art_ref IN (SELECT art_ref FROM cart WHERE uid = ?   )";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateArtSql)) {
            // Set any parameters if needed for the update query
            updateStatement.setInt(1, userId); // Set the parameter for uid
            System.out.println("1500");

            updateStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        CartController cartController = new CartController();
        OrderController orderController = new OrderController();
        Cart cart = new Cart();

        // Customize the buttons
        ButtonType yesButtonType = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButtonType = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yesButtonType, noButtonType);

        // Show the alert and wait for it to be fully rendered
        Platform.runLater(() -> {
            // Get the yesButton from the dialog pane
            Button yesButton = (Button) alert.getDialogPane().lookupButton(yesButtonType);
            Button noButton = (Button) alert.getDialogPane().lookupButton(noButtonType);


            // Attach an action event handler to the Button
            if (yesButton != null) {
                yesButton.setOnAction(event -> {
                    // Your action event handling code here
                    cartController.proceedToCheckout(event);
                });
            } else {
                System.err.println("Yes button not found in the dialog pane.");
            }
            if (noButton != null) {
                noButton.setOnAction(event -> {
                    // Your action event handling code here


                    try {
                        List<Integer> artRefs = orderController.getArtRefsByUserIdUntilEmpty(currentUser.getUid());

                        // Now, artRefs contains all art_ref values for the given userId
                        for (int artRef : artRefs) {
                            System.out.println("ArtRef: " + artRef);
                            cartController.deleteCartItem(currentUser.getUid(),  artRef);

                        }}  catch (SQLException e) {
                        e.printStackTrace();
                    }

                });
            } else {
                System.err.println("No button not found in the dialog pane.");
            }
        });

        // Show the alert and wait for the user response
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent();
    }


    @FXML
    public void displayCart(MouseEvent event) {
        // Open a new window to display the cart items
        try {
            // Load the FXML file for the cart items window
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Cart.fxml"));
            Parent root = fxmlLoader.load();

            javafx.scene.image.Image icon = new Image("file:/src/images/logo.png");
            // Create a new Stage for the cart items window
            Stage stage = new Stage();
            stage.getIcons().add(icon);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));

            // Set the owner of the new window to the current window
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());

            // Show the cart items window
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*  @FXML
      void addToCart(ActionEvent event) throws SQLException {
          // Step 1: Retrieve Art Information
          int artId = Integer.parseInt(artIdField.getText());
          Art art = getArtById(artId);

          // Step 2: Check if the art exists and is available
          if (art == null) {
              showAlert("Art not found", "The selected art does not exist.");
              return;
          } else if (!art.isAvailable()) {
              showAlert("Art not available", "The selected art is not available.");
              return;
          }

          // Step 3: Add to Cart
          Cart cart = new Cart();
          cart.setUserId(1); // Assuming the user ID is 1
          cart.setArtRef(artId); // Set the art reference

          // Update the art availability status
          art.setAvailable(false); // Assuming there's a method to update the availability status

          // Save the cart to the database
          CartService cartService = new CartService();
          cartService.save(cart);

          // Step 4: Update UI
          showAlert("Art added to cart", "The selected art has been added to your cart.");
      }
  */
    private Art_Piece getArtById(int artId) {


        try {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM art WHERE art_ref = ?");
            statement.setInt(1, artId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int art_ref = resultSet.getInt("art_ref");
                String art_title = resultSet.getString("art_title");
                float art_price = resultSet.getFloat("art_price");
                String type = resultSet.getString("type");
                LocalDate creation = resultSet.getDate("creation").toLocalDate();
                String description = resultSet.getString("description");
                String style = resultSet.getString("style");
                int artist_id = resultSet.getInt("artist_id");
                boolean isAvailable = resultSet.getBoolean("isAvailable");

                // Create and return the Art object
                return new Art_Piece(art_ref, art_title, art_price, type, creation, description, style, artist_id, isAvailable);
            } else {
                return null; // No art found with the given ID
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
    @FXML
    void addToCart(ActionEvent event) throws SQLException {

    }

    // Helper method to show an alert


    public Gallery() {
        this.connection = MyDatabase.getInstance().getConn();
    }

    // Your database operations using connection

    @FXML
    private void displayCart() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cart.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Get the stage from the current event
            Stage stage = (Stage) cartId.getScene().getWindow();

            // Set the scene on the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
