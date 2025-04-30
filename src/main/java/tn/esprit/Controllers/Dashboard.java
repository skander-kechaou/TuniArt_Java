package tn.esprit.Controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
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
import tn.esprit.entities.*;
import tn.esprit.services.*;
import tn.esprit.utils.SessionManager;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class Dashboard implements Initializable {

    @FXML
    private Text managementTextId;

    @FXML
    private ImageView profilePicId;

    @FXML
    private Text userID;

    @FXML
    private Button usersButton;

    @FXML
    private FlowPane container;

    @FXML
    private ImageView logoutButton;

    @FXML
    private Hyperlink inviteOnly;

    UserService us = new UserService();
    Art_PieceService as = new Art_PieceService();
    galleryService gs = new galleryService();
    AuctionService AS = new AuctionService();
    EventService ES = new EventService();
    String profile_picture;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image logout = new Image("file:src\\images\\sign-out-alt.png");
        logoutButton.setImage(logout);
        // Retrieve the UID from the session
        int uid = SessionManager.getInstance().getCurrentUserUid();
        User currentUser;
        try {
            currentUser = us.searchByUid(uid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Display the UID on the UI
        userID.setText(currentUser.getFname().toUpperCase()+" "+currentUser.getLname().toUpperCase());
        Image profile = new Image("file:src\\images\\Profile-PNG-File.png");
        profilePicId.setImage(profile);

        displayUsers();
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

    private void displayUsers() {
        try {
            List<User> users = us.diplayListUsersAndArtists();

            // Call the method to create user boxes and add them to the container
            List<VBox> userBoxes = createUserBox(users);
            container.getChildren().addAll(userBoxes);
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
            userBox.setStyle("-fx-padding: 10px; -fx-spacing: 5px; -fx-background-color: #f7f8fa; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

            // Set a fixed width for the VBox
            userBox.setPrefWidth(195); // Adjust width here (e.g., 200 pixels)

            // Add margin to the VBox
            userBox.setMargin(userBox, new Insets(10));

            // Add user name label
            Label nameLabel = new Label(user.getFname().toUpperCase() + " " + user.getLname().toUpperCase());
            nameLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
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
            Label roleLabel = new Label(user.getRole());
            roleLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            userBox.getChildren().add(roleLabel);

            Button status = new Button("Activate");
            if (user.getStatus())
            {
                status.setText("Deactivate");
                status.setStyle("-fx-background-color: #FC6541; -fx-background-radius: 55; -fx-text-fill: white;");
                status.setPadding(new Insets(10,10,10,10));
            }
            else {
                status.setText("Activate");
                status.setStyle("-fx-background-color: #36d664; -fx-background-radius: 55; -fx-text-fill: white;");
                status.setPadding(new Insets(10,10,10,10));
            }
            userBox.getChildren().add(status);

            status.setOnAction(event -> {
                //handle activation or deactivation
                if(user.getStatus())
                {
                    user.setStatus(false);
                } else {
                    user.setStatus(true);
                }
                try {
                    us.update(user);

                    // Show success message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Confirmation");
                    alert.setContentText("Sign Up Complete!");
                    alert.showAndWait();

                    // Update button text and style based on the new status
                    if (user.getStatus()) {
                        status.setText("Deactivate");
                        status.setStyle("-fx-background-color: #FC6541; -fx-background-radius: 55; -fx-text-fill: white;");
                    } else {
                        status.setText("Activate");
                        status.setStyle("-fx-background-color: #36d664; -fx-background-radius: 55; -fx-text-fill: white;");
                    }

                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            });


            // Add margin to the bottom of the VBox
            userBox.setMargin(profileImage, new Insets(10));
            userBoxes.add(userBox);
        }
        return userBoxes;
    }

    @FXML
    void sendLink(ActionEvent event) throws IOException {
        // Load the new FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/InviteAdmin.fxml"));
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
    }

    private void displayArts() {
        try {
            List<Art_Piece> arts = as.diplayList();
            System.out.println("in displayarts");
            // Call the method to create art boxes and add them to the container
            List<VBox> artBoxes = createArtBox(arts);
            container.getChildren().addAll(artBoxes);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

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

    private List<VBox> createArtBox(List<Art_Piece> arts) {
        List<VBox> artBoxes = new ArrayList<>();
        final MediaPlayer[] mediaPlayer = {null};

        for (Art_Piece art : arts) {
            System.out.println(art);
            VBox artBox = new VBox();
            artBox.getStyleClass().add("artBox"); // Add style class to VBox
            //artBox.setStyle("-fx-padding: 30px; -fx-spacing: 20px; -fx-border-radius: 10px; -fx-background-color: #f7f8fa; -fx-border-color: #5dade2; ");
            artBox.setStyle("-fx-padding: 10px; -fx-spacing: 5px; -fx-background-color: #f7f8fa; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

            // Set a fixed width for the VBox
            artBox.setPrefWidth(195); // Adjust width here (e.g., 200 pixels)

            // Add margin to the VBox
            artBox.setMargin(artBox, new Insets(10));

            // Add art name label
            Button updateButton = new Button("Update");
            updateButton.setUserData(art.getArt_ref());
            Button deleteButton = new Button("Delete");
            Button playButton = new Button("Play");
            Label Filller = new Label("     ");
            HBox buttonsBox = new HBox(10); // 10 is the spacing between buttons (adjust as needed)
            buttonsBox.getChildren().addAll( Filller, playButton);
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
            updateButton.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
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
                    container.getChildren().remove(artBox);
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
            artBox.setAlignment(Pos.CENTER);

            artBoxes.add(artBox);
            artBox.getChildren().addAll( buttonsBox);
        }
        return artBoxes;
    }

    private void displayGalleries() {
        try {
            List<gallery> galleries = gs.diplayList();
            System.out.println("in displayarts");
            // Call the method to create art boxes and add them to the container
            List<VBox> artBoxes = createGalleryBox(galleries);
            container.getChildren().addAll(artBoxes);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private List<VBox> createGalleryBox(List<gallery> arts) {
        List<VBox> artBoxes = new ArrayList<>();
        for (gallery art : arts) {
            System.out.println(art);
            VBox artBox = new VBox();
            artBox.getStyleClass().add("artBox"); // Add style class to VBox
            //artBox.setStyle("-fx-padding: 30px; -fx-spacing: 20px; -fx-border-radius: 10px; -fx-background-color: #f7f8fa; -fx-border-color: #5dade2; ");
            artBox.setStyle("-fx-padding: 10px; -fx-spacing: 5px; -fx-background-color: #f7f8fa; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");
            // Set a fixed width for the VBox
            artBox.setPrefWidth(220); // Adjust width here (e.g., 200 pixels)

            // Add margin to the VBox
            artBox.setMargin(artBox, new Insets(10));

            // Add art name label
            Button updateButton = new Button("Upd.");
            Button deleteButton = new Button("Del.");
            Button addButton = new Button("Add");
            HBox buttonsBox = new HBox(10); // 10 is the spacing between buttons (adjust as needed)
            buttonsBox.getChildren().addAll(addButton, deleteButton, updateButton);
            buttonsBox.setPadding(new Insets(10, 50, 20, 10));
            buttonsBox.setAlignment(Pos.CENTER);

            Label title_label = new Label(art.getGallery_name().toUpperCase());
            Label price_label = new Label(art.getGallery_location().toUpperCase());
            Label category_label = new Label(art.getOperating_hours().toUpperCase());
            title_label.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 16));
            title_label.setPadding(new Insets(15));
            title_label.setTextFill(Color.BLACK);
            category_label.setTextFill(Color.BLACK);
            //style="-fx-background-color: E18B10; -fx-background-radius: 55" text="Upload Your Image" textFill="WHITE"
            updateButton.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
            deleteButton.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
            addButton.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
            price_label.setTextFill(Color.BLACK);// Set text color to white
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

            addButton.setOnAction(event -> {
                // Handle upload button action
                try {

                    Parent root = FXMLLoader.load(getClass().getResource("/add_gallery.fxml"));
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
                    container.getChildren().remove(artBox);
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
            artBox.setAlignment(Pos.CENTER);

            artBox.getChildren().addAll(buttonsBox);
            artBoxes.add(artBox);
        }
        return artBoxes;
    }

    private void displayAuctions() {
        try {
            List<Auction> auctions = AS.diplayList();

            //call the method to create user boxes and add them to the container
            List<VBox> auctionBoxes = createauctionBox(auctions);
            container.getChildren().addAll(auctionBoxes);
            // Add padding to the auctionContainer
            container.setPadding(new Insets(10)); // Adjust the value as needed
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
            auctionBox.setStyle("-fx-padding: 10px; -fx-spacing: 5px; -fx-background-color: #f7f8fa; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

            // Set a fixed width for the VBox
            auctionBox.setPrefWidth(185); // Adjust width here (e.g., 200 pixels)

            // Add margin to the VBox
            auctionBox.setMargin(auctionBox, new Insets(10));

            // Add user name label
            Label nameLabel = new Label(auction.getAuction_name().toUpperCase());
            nameLabel.setTextFill(Color.BLACK);
            Label startdateLabel = new Label("Starts on "+auction.getStart_date().toString());
            startdateLabel.setTextFill(Color.BLACK);
            Label enddateLabel = new Label("Ends on "+auction.getEnd_date().toString());
            enddateLabel.setTextFill(Color.BLACK);
            Label thresholdLabel = new Label("Threshold : "+Float.toString(auction.getThreshold()));
            thresholdLabel.setTextFill(Color.BLACK);
            Label artrefLabel = new Label(Integer.toString(auction.getArt_ref()));
            artrefLabel.setTextFill(Color.BLACK);
            nameLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            startdateLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            enddateLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            thresholdLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            artrefLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            Button delete = new Button("Delete");
            Button update = new Button("Update");
            HBox buttonsBox = new HBox(10); // 10 is the spacing between buttons (adjust as needed)
            buttonsBox.getChildren().addAll( delete, update);
            buttonsBox.setPadding(new Insets(10, 0, 0, 10));


            update.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
            delete.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
            auctionBox.setAlignment(Pos.CENTER);
            auctionBox.getChildren().addAll(nameLabel,startdateLabel,enddateLabel,thresholdLabel,artrefLabel);

            // Add margin to the bottom of the VBox
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
                    container.getChildren().remove(auctionBox);

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

    private List<VBox> createeventBox(List<Event> events) {
        List<VBox> eventBoxes = new ArrayList<>();
        for (Event event : events) {
            VBox eventBox = new VBox();
            eventBox.getStyleClass().add("eventBox"); // Add style class to VBox
            eventBox.setStyle("-fx-padding: 10px; -fx-spacing: 5px; -fx-background-color: #f7f8fa; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

            // Set a fixed width for the VBox
            eventBox.setPrefWidth(185); // Adjust width here (e.g., 200 pixels)

            // Add margin to the VBox
            eventBox.setMargin(eventBox, new Insets(10));

            // Add user name label
            Label titleLabel = new Label(event.getEvent_title().toUpperCase());
            Label categoryLabel = new Label(event.getCategory());
            Label dateLabel = new Label(event.getEvent_date().toString());
            Label durationLabel = new Label(String.valueOf(event.getDuration()) + " HOUR(S)");

            titleLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            titleLabel.setTextFill(Color.BLACK);
            categoryLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            categoryLabel.setTextFill(Color.BLACK);
            dateLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            dateLabel.setTextFill(Color.BLACK);
            durationLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            durationLabel.setTextFill(Color.BLACK);
            Button attendEventButton = new Button("Attend Event");

// Define the spacing between buttons (adjust as needed)
            HBox buttonsBox = new HBox(10); // 10 is the spacing between buttons (adjust as needed)
            //buttonsBox.getChildren().add();

// Set padding for the HBox
            buttonsBox.setPadding(new Insets(10, 50, 50, 10));
            eventBox.setAlignment(Pos.CENTER);
            eventBox.getChildren().addAll(titleLabel, categoryLabel, dateLabel, durationLabel);//display everything
            attendEventButton.setOnMouseClicked(mouseEvent -> {
                System.out.println("clicked");
                sendsmsService.SendSms("+216 28 780 217","attended this event");
            });
            // Add margin to the bottom of the VBox
            eventBox.setPadding(new Insets(10, 50, 50, 10));
            eventBox.setMargin(eventBox, new Insets(10));
            eventBoxes.add(eventBox);

        }
        return eventBoxes;
    }

    private void displayEvents() {
        try {
            List<Event> events = ES.diplayList();

            // Call the method to create user boxes and add them to the container
            List<VBox> eventBoxes = createeventBox(events);
            container.getChildren().addAll(eventBoxes);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void showArts(MouseEvent event) {
        // Clear existing user boxes before adding new ones
        container.getChildren().clear();
        managementTextId.setText("ART PIECES MANAGEMENT");
        displayArts();
    }

    @FXML
    void showGalleries(MouseEvent event) {
        // Clear existing user boxes before adding new ones
        container.getChildren().clear();
        managementTextId.setText("GALLERIES MANAGEMENT");
        displayGalleries();
    }

    @FXML
    void showUsers(MouseEvent event) {
        // Clear existing user boxes before adding new ones
        container.getChildren().clear();
        managementTextId.setText("USERS MANAGEMENT");
        displayUsers();
    }

    @FXML
    void showAuctions(MouseEvent event) {
        container.getChildren().clear();
        managementTextId.setText("AUCTIONS MANAGEMENT");
        displayAuctions();
    }

    @FXML
    void showEvents(MouseEvent event) {
        container.getChildren().clear();
        managementTextId.setText("EVENTS MANAGEMENT");
        displayEvents();
    }



}
