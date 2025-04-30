package tn.esprit.Controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import tn.esprit.entities.Art_Piece;
import tn.esprit.services.Art_PieceService;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class Gallery_user implements Initializable {

    Art_PieceService as = new Art_PieceService();

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
    private Text uidTextId;
    @FXML
    private Button upload_art;

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
        Image logo = new Image("file:src\\images\\logo.png");
        logoId.setImage(logo);
        Image print = new Image("file:src\\images\\tun.png");
        printId.setImage(print);

        artsContainer.setPadding(new Insets(30)); // Adjust padding as needed
        artsContainer.setHgap(80); // Set horizontal gap between elements
        artsContainer.setVgap(20); // Set vertical gap between lines
        displayArts();
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
            artBox.setPrefWidth(250); // Adjust width here (e.g., 200 pixels)

            // Add margin to the VBox
            artBox.setMargin(artBox, new Insets(10));

            // Add art name label

            Button buyButton = new Button("Buy");
            Button playButton = new Button("Play");
            Label Filller = new Label("     ");
            HBox buttonsBox = new HBox(10); // 10 is the spacing between buttons (adjust as needed)
            buttonsBox.getChildren().addAll( Filller,buyButton,playButton);
            buttonsBox.setPadding(new Insets(10, 50, 20, 10));

            Label title_label = new Label(art.getArt_title().toUpperCase());

            Label price_label = new Label(String.valueOf(art.getArt_price())+"DT");
            Label category_label = new Label(art.getType().toUpperCase());
            title_label.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 16));
            title_label.setPadding(new Insets(15));
            title_label.setTextFill(Color.WHITE);
            category_label.setTextFill(Color.WHITE);
            //style="-fx-background-color: E18B10; -fx-background-radius: 55" text="Upload Your Image" textFill="WHITE"
            buyButton.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
            playButton.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
            price_label.setTextFill(Color.WHITE);// Set text color to white
            artBox.setAlignment(Pos.CENTER);
            artBox.getChildren().addAll( title_label,price_label,category_label);




            buyButton.setOnAction(event -> {
                // Handle delete button action

                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("/Add_Review.fxml"));
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
            artBox.setStyle("-fx-background-color: #3B2A19; -fx-background-radius: 55;");
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

}
