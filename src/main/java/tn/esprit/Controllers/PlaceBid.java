package tn.esprit.Controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import tn.esprit.entities.Auction;
import tn.esprit.services.AuctionService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
public class PlaceBid implements Initializable {

    AuctionService AS = new AuctionService();



    @FXML
    private FlowPane auctionContainer;

    @FXML
    private ImageView logoId;

    @FXML
  //  private Text uidTextId;

    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image logo = new Image("file:src\\images\\logo.png");
        logoId.setImage(logo);

        displayAuctions();
    }
    @FXML
    void moveToEvent(ActionEvent event) {
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
            auctionBox.setStyle("-fx-padding: 20px; -fx-spacing: 10px; -fx-background-color: #f7f8fa; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

            // Set a fixed width for the VBox
            auctionBox.setPrefWidth(185); // Adjust width here (e.g., 200 pixels)

            // Add margin to the VBox
            auctionBox.setMargin(auctionBox, new Insets(10));

            // Add user name label
            Label nameLabel = new Label(auction.getAuction_name().toUpperCase());
            Label startdateLabel = new Label("Starts on " + auction.getStart_date().toString());
            Label enddateLabel = new Label("Ends on " + auction.getEnd_date().toString());
            Label thresholdLabel = new Label("Threshold : " + Float.toString(auction.getThreshold()));
            Label artrefLabel = new Label(Integer.toString(auction.getArt_ref()));
            nameLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            startdateLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            enddateLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            thresholdLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            artrefLabel.setFont(Font.font("Gill Sans MT", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            // Button for placing a bid
            Button placeBidButton = new Button("Place Bid");
            placeBidButton.setOnAction(event -> {
                placeBid(auction); // Call placeBid method when the button is clicked
            });


            // Add labels and button to the VBox
            auctionBox.getChildren().addAll(nameLabel, startdateLabel, enddateLabel, thresholdLabel, artrefLabel, placeBidButton);
            // Add margin to the bottom of the VBox
            auctionBox.setMargin(auctionBox, new Insets(10));

            // Add the current auction box to the list
            auctionBoxes.add(auctionBox);
        }
        // Return the list of auction boxes after the loop has completed
        return auctionBoxes;
    }
    Auction updatedAuction;
    private void placeBid(Auction auction) {
        // Create a TextInputDialog
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Place Bid");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter your bid amount:");

        // Show the dialog and wait for the user response
        Optional<String> result = dialog.showAndWait();

        // Process the user response
        result.ifPresent(bidAmount -> {
            // Validate and process bid amount
            try {
                float amount = Float.parseFloat(bidAmount);
                float threshold = auction.getThreshold(); // Change this value to your threshold
                if (amount < threshold) {
                    // Display an error message if the bid amount is below the threshold
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Bid");
                    alert.setHeaderText(null);
                    alert.setContentText("Your bid amount must be higher than $" + threshold + ".");
                    alert.showAndWait();
                    return; // Exit the method if the bid amount is invalid
                }
                try {
                    AS.updateThreshold(amount, auction.getAuction_ref());
                } catch (SQLException ex) {
                    // Handle the exception (e.g., display an error message)
                    ex.printStackTrace();
                    return; // Exit the method if database update fails
                }

                // Optionally, display a confirmation message to the user
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Bid Placed");
                alert.setHeaderText(null);
                alert.setContentText("Your bid of $" + amount + " has been placed successfully.");
                alert.showAndWait();
                // Update the UI with the updated bid amount

                auction.setThreshold(updatedAuction.getThreshold());
                for (Node node : auctionContainer.getChildren()) {
                    if (node instanceof VBox) {
                        VBox vbox = (VBox) node;
                        Label thresholdLabel = (Label) vbox.lookup(".thresholdLabel");
                        if (thresholdLabel != null && vbox.getUserData() instanceof Auction) {
                            Auction auctionInBox = (Auction) vbox.getUserData();
                            if (auctionInBox.getAuction_ref() == auction.getAuction_ref()) {
                                // Update the label with the new bid amount
                                thresholdLabel.setText("Threshold : " + updatedAuction.getThreshold());
                                break;
                            }
                        }
                    }
                }
            } catch (NumberFormatException e) {
                // Handle invalid input (non-numeric bid amount)
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid numeric bid amount.");
                alert.showAndWait();
            }
        });
    }
    }