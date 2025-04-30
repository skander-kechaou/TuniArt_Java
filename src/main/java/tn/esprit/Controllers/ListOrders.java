package tn.esprit.Controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import tn.esprit.entities.*;
import tn.esprit.services.*;
import tn.esprit.utils.SessionManager;
import javafx.scene.control.TableColumn;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ListOrders implements Initializable {

    @FXML
    private Text biography;

    @FXML
    private FlowPane container;

    @FXML
    private Button deactivateAccountButton;

    @FXML
    private Button editButton;

    @FXML
    private Text followers;

    @FXML
    private Text following;

    @FXML
    private Button go_to_arts;

    @FXML
    private Button go_to_auctions;

    @FXML
    private Button go_to_events;

    @FXML
    private Button go_to_galleries;

    @FXML
    private Button go_to_users;

    @FXML
    private ImageView logoId;

    @FXML
    private Text nbViews;

    @FXML
    private Button pdfButton;

    @FXML
    private Text portfolio;

    @FXML
    private Hyperlink portfolioLink;

    @FXML
    private ImageView profilePicId;

    @FXML
    private ImageView profilePictureId;

    @FXML
    private TableView<Order> tableView;

    @FXML
    private TableColumn<Order, LocalDate> orderDateCol;

    @FXML
    private TableColumn<Order, Integer> orderIdCol;

    @FXML
    private TableColumn<Order, String> statusCol;

    @FXML
    private TableColumn<Order, Float> totalPriceCol;

    @FXML
    private TableColumn<Order, String> ReceptionCol;

    @FXML
    private TableColumn<Order, String> actionsCol;

    @FXML
    private Text userId;

    UserService us = new UserService();
    Art_PieceService as = new Art_PieceService();
    AuctionService AS = new AuctionService();
    EventService ES = new EventService();

    OrderService OS = new OrderService();
    DeliveryService DS = new DeliveryService();
    DeliveryAgencyService DAS = new DeliveryAgencyService();
    User currentUser;
    Artist currentArtist;

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
        displayOrders();
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

    @FXML
    void generatePDF(ActionEvent event) {
        generateUserInfoPDF(currentUser);
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
    void go_to_events(ActionEvent event) {
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

    void displayOrders() {
        try {
            // Retrieve all orders from the database
            List<Order> orders = OS.searchByUid(currentUser.getUid());
            // Convert the list to an ObservableList
            ObservableList<Order> orderList = FXCollections.observableArrayList(orders);
            // Set the items in the TableView
            tableView.setItems(orderList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQLException appropriately
        }

        // Configure the cell value factories for each column
        orderIdCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOrderId()).asObject());
        orderDateCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getOrderDate()));
        // Add cell value factories for other columns
        statusCol.setCellValueFactory(cellData -> {
            int status = cellData.getValue().getStatus();
            if (status == 1) {
                // If the status is 1, display "Accepted"
                return new SimpleStringProperty("Accepted");
            } else {
                // If the status is 0, display "Canceled"
                return new SimpleStringProperty("Canceled");
            }
        });
        totalPriceCol.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getTotalPrice()).asObject());
        ReceptionCol.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            try {
                if (OS.hasDelivery(order)) {
                    Delivery delivery = DS.getDeliveryByOrder(order);
                    DeliveryAgency deliveryAgency = DAS.getAgencyById(delivery.getAgencyId());
                    // If the order has a delivery, display "Delivered by agencyName on the estimatedDate"
                    String deliveryInfo = "Delivered by " + deliveryAgency.getAgencyName() + " on " + delivery.getEstimatedDate();
                    return new SimpleStringProperty(deliveryInfo);
                } else {
                    // If the order doesn't have a delivery, display "Pick up"
                    return new SimpleStringProperty("Pick up");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        // Add cell value factories for other columns

        // Set the column resizing policy to CONSTRAINED_RESIZE_POLICY
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set the TableView to adjust its size to its content
        tableView.setPrefWidth(TableView.USE_COMPUTED_SIZE);
        tableView.setPrefHeight(TableView.USE_COMPUTED_SIZE);

    }

}
