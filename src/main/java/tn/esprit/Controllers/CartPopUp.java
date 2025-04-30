package tn.esprit.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

public class CartPopUp implements Initializable {

    @FXML
    private Button closeButton;

    @FXML
    private Label artPiecesLabel;
    @FXML
    private Label cartTooltip;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set the Tooltip text to display the items in the cart
        cartTooltip.setText("Cart Items: ..."); // Replace "..." with the actual items in the cart
        Tooltip.install(closeButton, cartTooltip.getTooltip());
    }

   /* @FXML
    private void closeCart(ActionEvent event) {
        // Get the stage and close it
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }*/
   @FXML
   private void closeWindow(ActionEvent event) {
       // Clear the UI
       artPiecesLabel.setText("");

       // You don't need to delete from the database, just clear the display
       // Stage stage = (Stage) closeButton.getScene().getWindow();
       // stage.hide(); // Hide the cart window
   }

}
