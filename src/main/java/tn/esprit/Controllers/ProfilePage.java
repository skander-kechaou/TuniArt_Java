package tn.esprit.Controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import tn.esprit.entities.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.PrivateKey;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import tn.esprit.entities.Event;
import tn.esprit.services.*;
import tn.esprit.utils.ProfileManager;
import tn.esprit.utils.SessionManager;

public class ProfilePage implements Initializable {

    private User user;

    @FXML
    private FlowPane container;


    @FXML
    private ImageView logoId;

    @FXML
    private ImageView profilePicId;

    @FXML
    private ImageView profilePictureId;

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
    UserService us = new UserService();
    Art_PieceService as = new Art_PieceService();
    AuctionService AS = new AuctionService();
    EventService ES = new EventService();

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

    public void initData(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public User getData() {return selectedUser;}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int uid = ProfileManager.getInstance().getCurrentUserUid();
        int uidCon = SessionManager.getInstance().getCurrentUserUid();
        Image account = new Image("file:src\\images\\Profile-PNG-File.png");
        profilePictureId.setImage(account);
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

        displayArts();
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

    private void displayArts() {
        try {
            java.util.List<Art_Piece> arts = as.diplayListByArtist(selectedUser.getUid());
            System.out.println("in displayarts");
            // Call the method to create art boxes and add them to the container
            java.util.List<VBox> artBoxes = createArtBox(arts);
            container.getChildren().addAll(artBoxes);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private java.util.List<VBox> createArtBox(java.util.List<Art_Piece> arts) {
        List<VBox> artBoxes = new ArrayList<>();
        final MediaPlayer[] mediaPlayer = {null};

        for (Art_Piece art : arts) {
            System.out.println(art);
            VBox artBox = new VBox();
            artBox.getStyleClass().add("artBox"); // Add style class to VBox
            //artBox.setStyle("-fx-padding: 30px; -fx-spacing: 20px; -fx-border-radius: 10px; -fx-background-color: #f7f8fa; -fx-border-color: #5dade2; ");
            artBox.setStyle("-fx-padding: 20px; -fx-spacing: 30px; -fx-background-color: #f7f8fa; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

            // Set a fixed width for the VBox
            artBox.setPrefWidth(230); // Adjust width here (e.g., 200 pixels)

            // Add margin to the VBox
            artBox.setMargin(artBox, new javafx.geometry.Insets(10));

            // Add art name label
            Button playButton = new Button("Play");
            javafx.scene.control.Label Filller = new javafx.scene.control.Label("     ");
            HBox buttonsBox = new HBox(10); // 10 is the spacing between buttons (adjust as needed)
            buttonsBox.getChildren().addAll( Filller,playButton);
            buttonsBox.setPadding(new javafx.geometry.Insets(10, 50, 20, 10));
            buttonsBox.setAlignment(Pos.CENTER);

            javafx.scene.control.Label title_label = new javafx.scene.control.Label(art.getArt_title().toUpperCase());

            javafx.scene.control.Label price_label = new javafx.scene.control.Label(String.valueOf(art.getArt_price())+"DT");
            javafx.scene.control.Label category_label = new Label(art.getType().toUpperCase());
            title_label.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 16));
            title_label.setPadding(new javafx.geometry.Insets(15));
            title_label.setTextFill(javafx.scene.paint.Color.WHITE);
            category_label.setTextFill(javafx.scene.paint.Color.WHITE);
            //style="-fx-background-color: E18B10; -fx-background-radius: 55" text="Upload Your Image" textFill="WHITE"
            playButton.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");

            price_label.setTextFill(Color.WHITE);// Set text color to white

            artBox.setAlignment(Pos.CENTER);
            artBox.getChildren().addAll( title_label,price_label,category_label);


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
            artBox.setMargin(artImage, new javafx.geometry.Insets(10));

            /*// Attach event handler to the profile picture
            artImage.setOnMouseClicked(event -> {
                // Redirect to the profile page for the selected art
                redirectToProfilePage(Art_Piece);
            });*/

            // Add margin to the bottom of the VBox
            artBox.setMargin(artBox, new Insets(8,8,8,8));
            artBox.setMargin(artBox, new javafx.geometry.Insets(10, 10, 10, 10));
            artBox.setStyle("-fx-background-color: #3B2A19; -fx-background-radius: 55; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");
            artBox.setAlignment(Pos.CENTER);

            artBoxes.add(artBox);
            artBox.getChildren().addAll( buttonsBox);
        }
        return artBoxes;
    }

    private List<VBox> createeventBox(List<tn.esprit.entities.Event> events) {
        List<VBox> eventBoxes = new ArrayList<>();
        for (tn.esprit.entities.Event event : events) {
            VBox eventBox = new VBox();
            eventBox.getStyleClass().add("eventBox"); // Add style class to VBox
            eventBox.setStyle("-fx-padding: 20px; -fx-spacing: 10px; -fx-background-color: #3B2A19; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

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
            titleLabel.setTextFill(Color.WHITE);
            categoryLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            categoryLabel.setTextFill(Color.WHITE);
            dateLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            dateLabel.setTextFill(Color.WHITE);
            durationLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            durationLabel.setTextFill(Color.WHITE);
            Button attendEventButton = new Button("Attend Event");

// Define the spacing between buttons (adjust as needed)
            HBox buttonsBox = new HBox(10); // 10 is the spacing between buttons (adjust as needed)
            buttonsBox.getChildren().add(attendEventButton);

// Set padding for the HBox
            buttonsBox.setPadding(new Insets(10, 50, 50, 10));
            eventBox.setAlignment(Pos.CENTER);
            eventBox.getChildren().addAll(titleLabel, categoryLabel, dateLabel, durationLabel,attendEventButton);//display everything
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
            List<Event> events = ES.diplayListByUser(selectedUser.getUid());

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

    private void displayAuctions() {
        try {
            List<Auction> auctions = AS.diplayListByUser(selectedUser.getUid());

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
            buttonsBox.setPadding(new Insets(10, 0, 0, 10));


            update.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
            delete.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
            auctionBox.setAlignment(Pos.CENTER);
            auctionBox.getChildren().addAll(nameLabel,startdateLabel,enddateLabel,thresholdLabel,artrefLabel,buttonsBox);

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

    @FXML
    void DisplayArtPieces(ActionEvent event) {
        container.getChildren().clear();
        displayArts();
    }

    @FXML
    void displayAuctionsAll(ActionEvent event) {
        container.getChildren().clear();
        displayAuctions();
    }

    @FXML
    void displayEventsAll(ActionEvent event) {
        container.getChildren().clear();
        displayEvents();
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
}
