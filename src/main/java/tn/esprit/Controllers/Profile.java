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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import tn.esprit.entities.Art_Piece;
import tn.esprit.entities.Artist;
import tn.esprit.entities.Auction;
import tn.esprit.entities.Event;
import tn.esprit.entities.User;
import tn.esprit.services.*;
import tn.esprit.utils.SessionManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class Profile implements Initializable {

    UserService us = new UserService();
    Art_PieceService as = new Art_PieceService();
    AuctionService AS = new AuctionService();
    EventService ES = new EventService();
    User currentUser;
    Artist currentArtist;

    @FXML
    private FlowPane container;


    @FXML
    private Button deactivateAccountButton;

    @FXML
    private Button editButton;

    @FXML
    private ImageView logoId;

    @FXML
    private ImageView profilePicId;

    @FXML
    private ImageView profilePictureId;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image logo = new Image("file:src\\images\\logo.png");
        logoId.setImage(logo);
        Image account = new Image("file:src\\images\\Profile-PNG-File.png");
        profilePictureId.setImage(account);
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

        displayArts();
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

    private void displayArts() {
        try {
            java.util.List<Art_Piece> arts = as.diplayListByArtist(currentUser.getUid());
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
            Button updateButton = new Button("Update");
            updateButton.setUserData(art.getArt_ref());
            Button deleteButton = new Button("Delete");
            javafx.scene.control.Label Filller = new javafx.scene.control.Label("     ");
            HBox buttonsBox = new HBox(10); // 10 is the spacing between buttons (adjust as needed)
            buttonsBox.getChildren().addAll( Filller,deleteButton, updateButton);
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
            updateButton.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
            deleteButton.setStyle("-fx-background-color: E18B10; -fx-background-radius: 55; -fx-text-fill: white;");
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

    private void displayAuctions() {
        try {
            List<Auction> auctions = AS.diplayListByUser(currentUser.getUid());

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
    void generatePDF(ActionEvent event) {
        generateUserInfoPDF(currentUser);
    }

    @FXML
    void DisplayArtPieces(ActionEvent event) {
        container.getChildren().clear();
        displayArts();
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

    private List<VBox> createeventBox(List<Event> events) {
        List<VBox> eventBoxes = new ArrayList<>();
        for (Event event : events) {
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
            List<Event> events = ES.diplayListByUser(currentUser.getUid());

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
    void DisplayAuctionsAll(ActionEvent event) {
        container.getChildren().clear();
        displayAuctions();
    }

    @FXML
    void displayEventsAll(ActionEvent event) {
        container.getChildren().clear();
        displayEvents();
    }
}
