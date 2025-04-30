package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tn.esprit.entities.Auction;
import tn.esprit.services.AuctionService;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import tn.esprit.entities.User;
import tn.esprit.services.UserService;
import tn.esprit.utils.SessionManager;

public class AddAuction implements Initializable {
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]+");
    private static final Pattern THRESHOLD_PATTERN = Pattern.compile("\\d+(\\.\\d+)?");

    @FXML
    private Button add_auction;

    @FXML
    private TextField name_id;
    @FXML
    private ImageView backid;
    @FXML
    private ImageView logoId;

    @FXML
    private DatePicker startDate_id;
    @FXML
    private DatePicker endDate_id;

    @FXML
    private TextField threshold_id;
    @FXML
    private Text end_alert;

    @FXML
    private Text name_alert1;
    @FXML
    private Text threshold_alert;
    @FXML
    private Text start_alert;
    private LocalDate startDateValue; // To store the start date value

    User currentUser;
    UserService us = new UserService();

    @FXML
    void HoverIn(MouseEvent event) {
        add_auction.setStyle("-fx-background-color: #E15B10; -fx-background-radius: 55;");
    }

    @FXML
    void HoverOut(MouseEvent event) {
        add_auction.setStyle("-fx-background-color: #E18B10; -fx-background-radius: 55;");

    }
    private final AuctionService AS = new AuctionService();

    private boolean validateFields() throws SQLException {
        // Clear error messages and styles for all error fields
        name_alert1.setText("");
        threshold_alert.setText("");
        end_alert.setText("");
        start_alert.setText("");


        // Check for missing information
        if (name_id.getText().isEmpty() || startDate_id.getValue()== null ||endDate_id.getValue()== null || threshold_id.getText().isEmpty())  {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Fill in all fields.");
            alert.showAndWait();
            return false;
        }

        boolean valid = true; //my flag so error messages won't appear individually

        // Validate first name
        if (!NAME_PATTERN.matcher(name_id.getText()).matches()) {
            name_alert1.setText("Name only contains letters.");
            name_alert1.setStyle("-fx-background-color: #ff4d4d;");
            valid = false;

        }if (!THRESHOLD_PATTERN.matcher(threshold_id.getText()).matches()) {
            threshold_alert.setText("Price only contains Numbers.");
            threshold_alert.setStyle("-fx-background-color: #ff4d4d;");
            valid = false;
        }

        return valid;
    }
    @FXML
    void addAuction(ActionEvent event) {

        try {
            if(validateFields()) {



                String name = name_id.getText();
                float threshold = Float.parseFloat(threshold_id.getText());
                //    public Auction( String auction_name,Date start_date, Date end_date, float threshold,int aid) {
                AS.addd(new Auction(name, Date.valueOf(startDate_id.getValue()),Date.valueOf(endDate_id.getValue()),threshold, 8, currentUser.getUid()));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation");
                alert.setContentText("Auction added !");
                alert.showAndWait();

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
        }
        catch (SQLException e){

            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        }


    }


   @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image logo = new Image("file:src\\images\\logo.png");
        logoId.setImage(logo);
        Image back = new Image("file:src\\images\\back-button.png");
        backid.setImage(back);

       // Retrieve the UID from the session
       int uid = SessionManager.getInstance().getCurrentUserUid();
       try {
           currentUser = us.searchByUid(uid);
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }

        startDate_id.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.isBefore(today));
                if (date.isBefore(today)) {
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });

       endDate_id.valueProperty().addListener((observable, oldValue, newValue) -> {
           if (startDate_id.getValue() != null && newValue != null) { // Check if start date is not null
               LocalDate startDateValue = startDate_id.getValue();
               // Check if the end date is before or the same as the start date
               if (newValue.isBefore(startDateValue) || newValue.isEqual(startDateValue)) {
                   endDate_id.setValue(oldValue); // Reset end date to old value
                   showAlert("End date should be after the start date.");
               }
               // Check if the end date is within one week from the start date
               else if (newValue.isAfter(startDateValue.plusWeeks(1))) {
                   endDate_id.setValue(oldValue); // Reset end date to old value
                   showAlert("End date should be within one week from the start date.");
               } else {
                   end_alert.setText(""); // Clear error message
               }
           }
       });


   }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void move_back(MouseEvent event) {
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

}


