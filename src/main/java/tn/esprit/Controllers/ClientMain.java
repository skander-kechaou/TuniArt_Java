package tn.esprit.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.scene.image.ImageView;

import tn.esprit.entities.Art_Piece;
import tn.esprit.entities.Cart;
import tn.esprit.services.CartService;
import tn.esprit.utils.MyDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ClientMain {
    @FXML
    private ImageView cartId;
    @FXML
    private TextField artIdField;
    @FXML
    private Button addToCartButton;

    private Tooltip cartTooltip = new Tooltip();

    private Connection connection;
    int userId = 1;



    @FXML
    public void initialize() {
        // Set the Tooltip text to display the items in the cart
        cartTooltip.setText("Cart Items: ..."); // Replace "..." with the actual items in the cart
        Tooltip.install(cartId, cartTooltip);
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

        if (connection == null){
            System.err.println("Connection is null. Cannot clear cart.");
            return;
        }
        System.out.println("15");
        String sql = "DELETE FROM cart WHERE uid = ?";
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
                        List<Integer> artRefs = orderController.getArtRefsByUserIdUntilEmpty(userId);

                        // Now, artRefs contains all art_ref values for the given userId
                        for (int artRef : artRefs) {
                            System.out.println("ArtRef: " + artRef);
                            cartController.deleteCartItem(userId,  artRef);

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

            // Create a new Stage for the cart items window
            Stage stage = new Stage();
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
        // Step 0: Validate Art ID
        String artIdText = artIdField.getText().trim();
        if (artIdText.isEmpty()) {
            showAlert("Invalid Input", "Please enter a valid Art ID.");
            return;
        }

        try {
            // Step 1: Retrieve Art Information
            int artId = Integer.parseInt(artIdText);

            Art_Piece art = getArtById(artId);

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

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid Art ID.");
        }
    }

    // Helper method to show an alert


    public ClientMain() {
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
