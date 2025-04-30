package tn.esprit.Controllers;

import com.github.sarxos.webcam.Webcam;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.entities.Art_Piece;
import tn.esprit.entities.User;
import tn.esprit.services.Art_PieceService;
import tn.esprit.services.UserService;
import tn.esprit.utils.SessionManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

public class SubmitArt implements Initializable {

    private static final Pattern TITLE_PATTERN = Pattern.compile("[a-zA-Z]+");
    private static final Pattern PRICE_PATTERN = Pattern.compile("\\d{1,7}");
    private final Art_PieceService AS = new Art_PieceService();

    @FXML
    private Button draw_pic;

    @FXML
    private ImageView back_button;

    @FXML
    private TextField description_field_id;

    @FXML
    private Text price_alert;

    @FXML
    private Text title_alert;
    @FXML
    private Button Take_picture;

    @FXML
    private TextField price_field_id;

    @FXML
    private ImageView logoId;


    @FXML
    private Button publish_button_id;

    @FXML
    private ChoiceBox<String> style_field_id;

    @FXML
    private TextField title_field_id;

    @FXML
    private ChoiceBox<String> type_field_id;

    @FXML
    private Button upload_image_button_id;

    @FXML
    private Button upload_music_button_id;

    private final  String[] types = {"Painting","Sculpture","Music","Literature","Architecture"," Crocheting "};
    private final  String[] styles = {};
    String art_image;
    String musicFilePath;

    User currentUser;
    UserService us = new UserService();

    private final Map<String, List<String>> typeToStylesMap = new HashMap<>();

    @FXML
    void HoverIn(MouseEvent event) {
        publish_button_id.setStyle("-fx-background-color: #E15B10; -fx-background-radius: 55;");
    }

    @FXML
    void HoverOut(MouseEvent event) {
        publish_button_id.setStyle("-fx-background-color: #E18B10; -fx-background-radius: 55;");
    }
    @FXML
    void HoverIn1(MouseEvent event) {
        upload_image_button_id.setStyle("-fx-background-color: #E15B10; -fx-background-radius: 55;");
    }
    @FXML
    void HoverOut1(MouseEvent event) {
        upload_image_button_id.setStyle("-fx-background-color: #E18B10; -fx-background-radius: 55;");
    }
    @FXML
    void HoverIn2(MouseEvent event) {
        upload_music_button_id.setStyle("-fx-background-color: #E15B10; -fx-background-radius: 55;");
    }

    @FXML
    void HoverOut2(MouseEvent event) {
        upload_music_button_id.setStyle("-fx-background-color: #E18B10; -fx-background-radius: 55;");
    }@FXML
    void HoverIn3(MouseEvent event) {
        Take_picture.setStyle("-fx-background-color: #E15B10; -fx-background-radius: 55;");
    }

    @FXML
    void HoverOut3(MouseEvent event) {
        Take_picture.setStyle("-fx-background-color: #E18B10; -fx-background-radius: 55;");
    }
    @FXML
    void HoverIn4(MouseEvent event) {
        draw_pic.setStyle("-fx-background-color: #E15B10; -fx-background-radius: 55;");
    }

    @FXML
    void HoverOut4(MouseEvent event) {
        draw_pic.setStyle("-fx-background-color: #E18B10; -fx-background-radius: 55;");
    }

    private boolean validateFields() throws SQLException {
        // Clear error messages and styles for all error fields
        title_alert.setText("");
        price_alert.setText("");


        // Check for missing information
        if (title_field_id.getText().isEmpty() || price_field_id.getText().isEmpty() || description_field_id.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Fill in all fields.");
            alert.showAndWait();
            return false;
        }

        boolean valid = true; //my flag so error messages won't appear individually

        // Validate first name
        if (!TITLE_PATTERN.matcher(title_field_id.getText()).matches()) {
            title_alert.setText("Title only contains letters.");
            title_alert.setStyle("-fx-background-color: #ff4d4d;");
            valid = false;
        }

        // Validate last name
        if (!PRICE_PATTERN.matcher(price_field_id.getText()).matches()) {
            price_alert.setText("Price only contains numbers.");
            price_alert.setStyle("-fx-background-color: #ff4d4d;");
            valid = false;
        }

        return valid;
    }

    @FXML
    void addArt(ActionEvent event) {

        try {
            if(validateFields()){
            String type = "Painting";
            String style = "Pop Art";
            type=type_field_id.getValue();
            style=style_field_id.getValue();
            String Price = price_field_id.getText();
            LocalDate currentDate = LocalDate.now();
            java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);
            AS.addd(new Art_Piece(title_field_id.getText(), Float.parseFloat(Price),currentUser.getUid(),type,sqlDate,description_field_id.getText(), style,art_image,musicFilePath));
            Alert alert= new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Art added !");
            alert.showAndWait();
                Parent root = FXMLLoader.load(getClass().getResource("/gallery.fxml"));
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

                System.out.println("moved");}
        }
        catch (SQLException e){

            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void addImage(ActionEvent event) {
        // Create a new file chooser
        FileChooser fileChooser = new FileChooser();

        // Set the title for the file chooser dialog
        fileChooser.setTitle("Select Art Picture");

        // Set the initial directory to the user's home directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Add filters to restrict file selection to image files only
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        // Show the file chooser dialog and wait for user selection
        File selectedFile = fileChooser.showOpenDialog(null);

        // Check if a file was selected
        if (selectedFile != null) {
            try {
                // Create a target directory if it doesn't exist
                File targetDir = new File("src/images");
                if (!targetDir.exists()) {
                    targetDir.mkdirs();
                }

                // Define the target file path
                String fileName = selectedFile.getName();
                System.out.println(fileName);
                 art_image = fileName;
                Path targetPath = new File(targetDir, fileName).toPath();

                // Copy the selected file to the target directory
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Art picture added successfully!");
                alert.showAndWait();
            } catch (IOException e) {
                // Show error message if an exception occurs during file copying
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error occurred while adding art picture!");
                alert.showAndWait();
            }
        }

    }

    @FXML
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
        Image back = new Image("file:src\\images\\back-button.png");
        back_button.setImage(back);
        type_field_id.setItems(FXCollections.observableArrayList("Painting","Sculpture","Music","Literature","Architecture","Crocheting "));
        style_field_id.setItems(FXCollections.observableArrayList(styles));
        typeToStylesMap.put("Painting", Arrays.asList("Impressionism","Expressionism","Cubism","Surrealism","Abstract Expressionism","Pop Art"));
        typeToStylesMap.put("Sculpture", Arrays.asList("Classical","Romanesque","Gothic","Baroque","Neoclassical","Modern"));
        typeToStylesMap.put("Music", Arrays.asList("Classical","Romantic","Jazz","Rock","Hip Hop","Electronic"));
        typeToStylesMap.put("Literature", Arrays.asList("Epic","Lyric","Drama","Novel","Poetry","Short Story"));
        typeToStylesMap.put("Architecture", Arrays.asList("Classical","Gothic","Renaissance","Baroque","Modern","Postmodern"));
        typeToStylesMap.put("Crocheting", Arrays.asList("Basic Crochet","Amigurumi","Filet Crochet","Tunisian Crochet","Irish Crochet","Freeform Crochet","Broomstick Lace Crochet","Hairpin Lace Crochet"));

        type_field_id.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() >= 0) {
                String selectedType = type_field_id.getItems().get(newValue.intValue());
                updateStyles(selectedType);
            }
        });

        // Select the first type by default
        type_field_id.getSelectionModel().selectFirst();
    }

    private void updateStyles(String selectedType) {
        List<String> stylesForType = typeToStylesMap.get(selectedType);
        if (stylesForType != null) {
            style_field_id.setItems(FXCollections.observableArrayList(stylesForType));
        } else {
            style_field_id.setItems(FXCollections.observableArrayList());
        }
    }
@FXML
    public void go_back(MouseEvent mouseEvent) {
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
    Stage oldStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    oldStage.close();

    // Show the new stage
    newStage.show();

    System.out.println("moved");


}
    @FXML
    void take_pic(ActionEvent event) {
        try {
            Webcam webcam = Webcam.getDefault();
            if (webcam != null) {
                webcam.open();

                // Capture image
                BufferedImage image = webcam.getImage();

                // Close webcam
                webcam.close();

                // Save the captured image to a file
                File targetDir = new File("src/images");
                if (!targetDir.exists()) {
                    targetDir.mkdirs();
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String dateTime = dateFormat.format(new Date());
                String fileName = "captured_image_" + dateTime + ".jpg";
                File targetFile = new File(targetDir, fileName);

                ImageIO.write(image, "JPG", targetFile);
                System.out.println("Image saved to: " + targetFile.getAbsolutePath());
                art_image = fileName;

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Image captured and saved successfully!");
                alert.showAndWait();
            } else {
                System.out.println("No webcam found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




    }
    @FXML
    void draw_pic(ActionEvent event) {
        Stage drawStage = new Stage();
        drawStage.setTitle("Draw Picture");

        // Create a canvas for drawing
        Canvas canvas = new Canvas(400, 300);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Set up event handlers for drawing
        canvas.setOnMousePressed(e -> {
            gc.beginPath();
            gc.moveTo(e.getX(), e.getY());
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);
        });

        canvas.setOnMouseDragged(e -> {
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });

        // Add the canvas to the scene
        StackPane root = new StackPane(canvas);
        Scene drawScene = new Scene(root, 400, 300);
        drawStage.setScene(drawScene);
        drawStage.show();


// Define the target directory
        Button saveButton = new Button("Save");
        saveButton.setOnAction(event1 -> {
            File targetDir = new File("src/images");
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }

            // Generate filename with current time
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String dateTime = dateFormat.format(new Date());
            String fileName = "captured_image_" + dateTime + ".png";
            File targetFile = new File(targetDir, fileName);

            // Create a WritableImage from the canvas
            WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            PixelWriter pixelWriter = writableImage.getPixelWriter();
            canvas.snapshot(null, writableImage);

            // Save the WritableImage to file
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "PNG", targetFile);
                System.out.println("Image saved to: " + targetFile.getAbsolutePath());

                // Assign the filename to the art_image variable
                art_image = fileName;

                // Close the draw stage
                drawStage.close();

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Image captured and saved successfully!");
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                // Log detailed information about the exception
                System.err.println("Error saving image: " + e.getMessage());
            }
        });

// Create a VBox to hold the canvas and the save button
        VBox root1 = new VBox(canvas, saveButton);
        VBox.setMargin(saveButton, new Insets(10)); // Add margin to the save button

// Add the VBox to the scene
        Scene drawScene1 = new Scene(root1, 400, 350); // Increased height to accommodate the button
        drawStage.setScene(drawScene1);
        drawStage.show();

    }



    @FXML
    void addmusic(ActionEvent event) {
        // Create a new file chooser
        FileChooser fileChooser = new FileChooser();

        // Set the title for the file chooser dialog
        fileChooser.setTitle("Select Music File");

        // Set the initial directory to the user's home directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Add filters to restrict file selection to music files only
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Music Files", "*.mp3", "*.wav", "*.ogg")
        );

        // Show the file chooser dialog and wait for user selection
        File selectedFile = fileChooser.showOpenDialog(null);

        // Check if a file was selected
        if (selectedFile != null) {
            try {
                // Create a target directory if it doesn't exist
                File targetDir = new File("src/music");
                if (!targetDir.exists()) {
                    targetDir.mkdirs();
                }

                // Define the target file path
                String fileName = selectedFile.getName();
                musicFilePath = fileName;
                Path targetPath = new File(targetDir, fileName).toPath();

                // Copy the selected file to the target directory
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Music file added successfully!");
                alert.showAndWait();
            } catch (IOException e) {
                // Show error message if an exception occurs during file copying
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error occurred while adding music file!");
                alert.showAndWait();
            }

        }
    }
}

