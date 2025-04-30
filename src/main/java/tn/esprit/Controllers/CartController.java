package tn.esprit.Controllers;

import java.io.IOException;
import java.sql.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.entities.User;
import tn.esprit.services.UserService;
import tn.esprit.utils.MyDatabase;
import tn.esprit.utils.SessionManager;


public class CartController {

    @FXML
    private Label artPiecesLabel;
    @FXML
    private Button deleteButton;


    @FXML
    private Button closeButton;

    private Connection connection;

    @FXML
    private VBox artPieces;

    @FXML
    private VBox cartVBox;

    private boolean isWindowClosing = false;


    public CartController() {
        // Do not initialize the connection here
        //this.connection = MyDatabase.getInstance().getConn();
        this.connection = MyDatabase.getInstance().getConn();
    }

    private int userId;
    User currentUser;
    UserService US = new UserService();

    public void initialize() {
      //  MyDatabase.getInstance().establishConnection();
        // Retrieve the UID from the session
        int uid = SessionManager.getInstance().getCurrentUserUid();
        userId = uid;
        try {
            currentUser = US.searchByUid(uid);
            System.out.println("current user : "+currentUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Initialize the connection when the controller is created
        refreshCart();


    }

    /* private void refreshCart() {
         // Get the art pieces for the user
         String artPieces = getArtPiecesForUser(userId);

         // Update the UI with the art pieces
         artPiecesLabel.setText(artPieces);

         // Save the cart information to the file only if the window is not closing
         if (!isWindowClosing) {
             CartFileUtil.saveCartInfo(artPieces);
         }
     }*/

    private void refreshCart() {
        try {
            VBox artPieces = getArtPiecesForUser(currentUser.getUid());
            ///////////////////////////////////////////changed
            cartVBox.getChildren().setAll(artPieces.getChildren());
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle or log the exception
        }
    }




    //////////////////////////////////////////////////////////////////////////////////
  /*  private void refreshCart() {
        // Get the art pieces for the user
        String artPieces = getArtPiecesForUser(userId);

        // Update the UI with the art pieces
        artPiecesLabel.setText(artPieces);
    }
*/

    /////the main one
   /* private void refreshCart() {
        // Get the art pieces for the user
        String artPieces = getArtPiecesForUser(userId);

        // Update the UI with the art pieces
        artPiecesLabel.setText(artPieces);
    }*/
/*
    @FXML
    private void proceedToCheckout(ActionEvent event) {
        // Navigate to the "Checkout.fxml" window
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Checkout.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    public double getTotalPriceForUser(int userId) {
        String sql = "SELECT SUM(art_price) AS total_price FROM art WHERE art_ref IN (SELECT art_ref FROM cart WHERE uid = ?)";

        double totalPrice = 0;

        try {

            // Check if the connection is closed and reconnect if needed
            if (connection == null || connection.isClosed()) {
                System.out.println("Connection is closed. Reconnecting...");
                connection = MyDatabase.getInstance().getConn(); // Adjust this line based on your MyDatabase implementation
            }

            System.out.println("Connection is open. Proceeding to execute query.");

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        totalPrice = resultSet.getDouble("total_price");
                    }
                }

            } catch (SQLException e) {
                System.err.println("Error executing query: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.err.println("Error obtaining connection: " + e.getMessage());
            e.printStackTrace();
        }

        return totalPrice;
    }





   /* public float getTotalPriceForUser(int userId) {
        float totalPrice = 0.0f;

        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Connection is closed. Reconnecting...");
                // You might need to handle the reconnection logic based on your MyDatabase implementation
                // Example: myDatabase.reconnect();
            }

            // Check the column name in your 'cart' table
            String query = "SELECT SUM(Art) FROM cart WHERE uid = userId";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        totalPrice = resultSet.getFloat(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error calculating total price: " + e.getMessage());
        }

        return totalPrice;
    }*/

/*    public float getTotalPriceForUser(int userId) {
        float totalPrice = 0.0f;

        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Connection is closed. Reconnecting...");
                // You might need to handle the reconnection logic based on your MyDatabase implementation
                // Example: myDatabase.reconnect();
            }

            // Check the column name in your 'cart' table
            String query = "SELECT SUM(your_actual_column_name) FROM cart WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        totalPrice = resultSet.getFloat(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error calculating total price: " + e.getMessage());
        }

        return totalPrice;
    }

*/

    /*  public float getTotalPriceForUser(int userId) {
        // Calculate the total price similar to how you did in getArtPiecesForUser
        String sql = "SELECT SUM(art_price) AS total_price FROM art WHERE art_ref IN (SELECT art_ref FROM cart WHERE uid = ?)";

        try (Connection connection = MyDatabase.getInstance().getConn();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getFloat("total_price");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0f; // Return 0 in case of an error
    }
*/

  /*  public void deleteByUid(int uid) {
        String sql = "DELETE FROM cart WHERE uid = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, uid);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
    @FXML
    public void proceedToCheckout(ActionEvent event) {
        float totalPrice = (float) getTotalPriceForUser(userId);

        // Ensure the database instance is created and connection is established

        if (connection == null) {
            System.out.println("Connection is null. Cannot proceed to checkout.");
            return;
        }

        try {
            if (connection.isClosed()) {
                System.out.println("Connection is closed. Reconnecting...");
                // You might need to handle the reconnection logic based on your MyDatabase implementation
                // Example: myDatabase.reconnect();
            } else {
                System.out.println("Connection is open. Proceeding to checkout...");

                // Close the old stage
                Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                oldStage.close();

                // Load the Order.fxml and pass the total price as a parameter
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Order.fxml"));
                Parent root;

                try {
                    root = loader.load();
                    // Access the controller instance from the loader
                    OrderController orderController = loader.getController();

                    // Pass the total price to the OrderController
                    orderController.setTotalPrice(totalPrice);
                    // Pass the database connection to the OrderController
                    orderController.setConnection(connection);
                    javafx.scene.image.Image icon = new Image("file:/src/images/logo.png");

                    // Show the Order window
                    Stage stage = new Stage();
                    stage.getIcons().add(icon);
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Error loading Order.fxml: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error checking connection status: " + e.getMessage());
        }
    }

   /* @FXML
    private void proceedToCheckout(ActionEvent event) throws SQLException {
        float totalPrice = getTotalPriceForUser(userId);

        // Ensure the database instance is created and connection is established
        MyDatabase myDatabase = MyDatabase.getInstance();

        // Check if the connection is already closed
        if (myDatabase.getConn() == null || myDatabase.getConn().isClosed()) {
            myDatabase = MyDatabase.getInstance(); // Reinitialize if closed
        }

        // Load the Order.fxml and pass the total price as a parameter
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Order.fxml"));
        Parent root;

        try {
            root = loader.load();
            // Access the controller instance from the loader
            OrderController orderController = loader.getController();

            // Pass the total price to the OrderController
            orderController.setTotalPrice(totalPrice);
            // Pass the database connection to the OrderController
            orderController.setConnection(myDatabase.getConn());

            // Show the Order window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Order.fxml: " + e.getMessage());
        }
    }
*/
  /*  @FXML
    private void proceedToCheckout(ActionEvent event) {
        float totalPrice = getTotalPriceForUser(userId);

        // Ensure the database instance is created and connection is established
        MyDatabase.getInstance();

        // Load the Order.fxml and pass the total price as a parameter
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Order.fxml"));
        Parent root;

        try {
            root = loader.load();
            // Access the controller instance from the loader
            OrderController orderController = loader.getController();

            // Pass the total price to the OrderController
            orderController.setTotalPrice(totalPrice);
            // Pass the database connection to the OrderController
            orderController.setConnection(MyDatabase.getInstance().getConn());

            // Show the Order window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Order.fxml: " + e.getMessage());
        }
    }
*/


   /* @FXML
    private void proceedToCheckout(ActionEvent event) {
        float totalPrice = getTotalPriceForUser(userId);

        // Load the Order.fxml and pass the total price as a parameter
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Order.fxml"));
        Parent root;

        try {
            root = loader.load();
            // Access the controller instance from the loader
            OrderController orderController = loader.getController();

            // Pass the total price to the OrderController
            orderController.setTotalPrice(totalPrice);

            // Show the Order window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Order.fxml: " + e.getMessage());
        }
    }
*/

/* @FXML
    private void proceedToCheckout(ActionEvent event) {
        float totalPrice = getTotalPriceForUser(userId);

        // Pass the existing connection to the OrderController
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Order.fxml"));
        OrderController orderController = new OrderController(MyDatabase.getInstance().getConn());
        orderController.setTotalPrice(totalPrice); // Set the totalPrice
        loader.setController(orderController);

        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Order.fxml: " + e.getMessage());
        }
    }*/

    /*  @FXML
    private void closeWindow(ActionEvent event) {
        clearCartForUser(userId);

        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.hide(); // Hide the cart window
    }

    private void clearCartForUser(int userId) {
        // Define the SQL query to delete all entries from the cart for a specific user
        String sql = "DELETE FROM cart WHERE uid = ?";

        // Create a new connection to the database using the MyDatabase class
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the user ID parameter in the SQL query
            statement.setInt(1, userId);

            // Execute the query to delete cart entries for the user
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/


    @FXML
    private void closeWindow(ActionEvent event) {
        // Set the flag to indicate that the window is closing
        isWindowClosing = true;

        // Clear the UI
        if (artPiecesLabel != null) {
            artPiecesLabel.setText("");
        }

        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.hide(); // Hide the cart window
    }



    /*@FXML
    private void closeWindow(ActionEvent event) {
        // Set the flag to indicate that the window is closing
        isWindowClosing = true;

        // Clear the UI
        artPiecesLabel.setText("");

        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.hide(); // Hide the cart window
    }*/

    ////////////////IF I NEED TO DELETE FROM DB



   /* private String getArtPiecesForUser(int userId) {
        // Define the SQL query to select the art pieces for a specific user
        String sql = "SELECT art_title, art_price FROM art WHERE art_ref IN (SELECT art_ref FROM cart WHERE uid = ?)";

        // Create a connection to the database using the MyDatabase class
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the user ID parameter in the SQL query
            statement.setInt(1, userId);

            // Execute the query and get the result set
            try (ResultSet resultSet = statement.executeQuery()) {
                // Iterate over the result set and build the art pieces string
                StringBuilder artPiecesBuilder = new StringBuilder();
                float totalPrice = 0.0f;
                while (resultSet.next()) {
                    String artTitle = resultSet.getString("art_title");
                    float artPrice = resultSet.getFloat("art_price");
                    artPiecesBuilder.append(artTitle).append(" - TND").append(artPrice).append("\n");
                    totalPrice += artPrice;
                }
                artPiecesBuilder.append("\nTotal Price: TND").append(totalPrice);
                return artPiecesBuilder.toString();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }*/






    //////////////////////////testing the delete button






    private VBox getArtPiecesForUser(int userId) throws SQLException {
        String sql = "SELECT art_ref, art_title, art_price FROM art WHERE art_ref IN (SELECT art_ref FROM cart WHERE uid = ?   )";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                VBox cartVBox = new VBox(); // Use a VBox to contain UI components
                float totalPrice = 0.0f;

                while (resultSet.next()) {
                    int artRef = resultSet.getInt("art_ref");
                    String artTitle = resultSet.getString("art_title");
                    float artPrice = resultSet.getFloat("art_price");

                    Button deleteButton = new Button("Delete");

                    // Set the button click action
                    deleteButton.setOnAction(event -> {
                        try {
                            // Delete cart item
                            deleteCartItem(userId, artRef);

                            // Refresh the cart UI after deletion
                            refreshCart();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.err.println("Error deleting item from cart: " + e.getMessage());
                        }
                    });

                    // Create a label to display art information
                    Label artInfoLabel = new Label(String.format("%s - TND%.2f", artTitle, artPrice));

                    // Add the button and label to the container
                    cartVBox.getChildren().addAll(artInfoLabel,deleteButton);

                    totalPrice += artPrice;
                }

                // Display the total price at the end
                Label totalPriceLabel = new Label(String.format("Total Price: TND%.2f", totalPrice));
                cartVBox.getChildren().add(totalPriceLabel);

                return cartVBox;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception or log it
            return new VBox(); // Return an empty VBox in case of an error
        }
    }




    public void deleteCartItem(int userId, int artRef) throws SQLException {
        String deleteQuery = "DELETE FROM cart WHERE uid = ? AND art_ref = ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setInt(1, userId);
            deleteStatement.setInt(2, artRef);
            int rowsAffected = deleteStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Item with artRef " + artRef + " deleted successfully.");

                // Update the availability in the Art table
                updateArtAvailability(artRef, true);
            } else {
                System.out.println("Item with artRef " + artRef + " not found in the cart.");
            }
        }
    }

    private void updateArtAvailability(int artRef, boolean isAvailable) throws SQLException {
        // Assuming you have a database connection
        String sql = "UPDATE art SET isAvailable = ? WHERE art_ref = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, isAvailable);
            statement.setInt(2, artRef);
            statement.executeUpdate();
        }
    }








    /////////////////////this works without updating
   /* private void deleteCartItem(int userId, int artRef) throws SQLException {
        String deleteQuery = "DELETE FROM cart WHERE uid = ? AND art_ref = ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setInt(1, userId);
            deleteStatement.setInt(2, artRef);
            int rowsAffected = deleteStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Item with artRef " + artRef + " deleted successfully.");
            } else {
                System.out.println("Item with artRef " + artRef + " not found in the cart.");
            }
        }
    }
*/





}