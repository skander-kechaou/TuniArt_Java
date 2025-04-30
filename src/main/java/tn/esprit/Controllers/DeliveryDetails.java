package tn.esprit.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import tn.esprit.entities.DeliveryAgency;
import tn.esprit.entities.Order;
import tn.esprit.entities.User;
import tn.esprit.services.DeliveryAgencyService;
import tn.esprit.services.UserService;
import tn.esprit.utils.SessionManager;
import javafx.scene.text.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class DeliveryDetails implements Initializable {

    @FXML
    private TextField addressField;

    @FXML
    private WebView mapView;

    @FXML
    private Label deliveryFeesLabel;

    @FXML
    private TextField orderIDField;

    @FXML
    private TextField orderIdField;

    @FXML
    private TextField deliveryFeesField;

    @FXML
    private TextField stateField;

    @FXML
    private TextField agencyIDField;

    @FXML
    private TextField agencyIdField;

    @FXML
    private ComboBox<String> agencyIDComboBox;


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
    private ImageView profilePictureId;

    @FXML
    private Text pageTitle;


    // Declare deliveryFees as a class-level variable
    private StringProperty deliveryFees = new SimpleStringProperty();

    Order order;
    User currentUser;
    UserService us = new UserService();

    DeliveryAgencyService DAS = new DeliveryAgencyService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Font font = Font.loadFont(new FileInputStream("C:/Users/DELL/Downloads/KaushanScript-Regular.ttf"), 64);
            pageTitle.setFont(font);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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
        // Populate the ComboBoxes with data
        agencyIDComboBox.setItems(getAgencyNames());
        WebEngine webEngine = mapView.getEngine();
        webEngine.load("file:///C:/Users/DELL/Documents/3A/Tuni-Art/src/main/java/tn/esprit/Controllers/index.html");
        // Bind the text property of the deliveryFeesLabel to the deliveryFees property in the controller class
    }

    // Setter for totalPrice
    public void setOrder(Order order) {
        this.order = order;
    }

    @FXML
    public void confirmAddress(ActionEvent event) throws SQLException {
        // Get the user input from the text fields
        String address = addressField.getText();

        // Get the selected order ID from the ComboBox
        String agencyName = agencyIDComboBox.getValue();
        DeliveryAgency da = DAS.getAgencyByName(agencyName);

        try {
            // Show an alert with the delivery fees and a confirmation button
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delivery Confirmation");
            alert.setHeaderText(null);
            float finalPrice = order.getTotalPrice()+7;
            int order_id = order.getOrderId();
            System.out.println("order id : "+order_id);
            alert.setContentText("The delivery fees are: 7TND\n\nThe total price will be: "+ finalPrice +"TND\n\nDo you want to confirm the order?");
            ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.YES);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(confirmButton, cancelButton);

            // Show the alert and wait for the user's response
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == confirmButton) {
                // User clicked confirm, add the delivery to the database
                // Connect to the database
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tuni'art", "root", "");

                // Prepare the SQL statement
                String sql = "INSERT INTO delivery (order_id, estimated_date, delivery_fees, destination, state, agency_id) VALUES (?, DATE_ADD(NOW(), INTERVAL 3 DAY), ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, order_id); // Set the order ID
                stmt.setFloat(2, 7);
                stmt.setString(3, address);
                stmt.setInt(4, 1);
                stmt.setInt(5, da.getAgencyId());

                // Execute the SQL statement
                int rowsAffected = stmt.executeUpdate();

                // Close the statement and connection
                stmt.close();
                conn.close();

                // Check if the insertion was successful
                if (rowsAffected == 1) {
                    // Show a success message
                    System.out.println("Delivery added to delivery table: " + address);

                    // Navigate back to ReceiveOrder.fxml
                    javafx.scene.image.Image icon = new Image("file:/src/images/logo.png");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/listOrders.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.getIcons().add(icon);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    // Show an error message
                    System.out.println("Error adding delivery to delivery table: Rows affected: " + rowsAffected);
                }
            } else {
                // User clicked cancel or closed the alert, do something else
            }
        } catch (SQLException e) {
            // Show an error message
            System.out.println("Error adding delivery to delivery table: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    private float getTotalPrice(int orderID) {
        float totalPrice = 0;

        try {
            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tuni'art", "root", "");

            // Prepare the SQL statement
            String sql = "SELECT totalprice FROM `order` WHERE order_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderID);

            // Execute the SQL statement
            ResultSet rs = stmt.executeQuery();

            // Get the total price from the result set
            if (rs.next()) {
                totalPrice = rs.getFloat("totalprice");
            }

            // Close the result set, statement, and connection
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            // Show an error message
            System.out.println("Error fetching total price: " + e.getMessage());
        }

        return totalPrice;
    }

    private float calculateDeliveryFees(int orderID) {
        // Fetch the total price of the order from the database
        float totalPrice = getTotalPrice(orderID);

        // Initialize the delivery fees
        float deliveryFeesFloat = 0;

        // Define the tiered pricing structure
        if (totalPrice < 5000) {
            deliveryFeesFloat = 500;
        } else if (totalPrice >= 5000 && totalPrice < 10000) {
            deliveryFeesFloat = 250;
        } else if (totalPrice >= 10000 && totalPrice < 15000) {
            deliveryFeesFloat = 100;
        } else {
            deliveryFeesFloat = 0;
        }

        return deliveryFeesFloat;
    }*/




//    @FXML
//    public void confirmAddress(ActionEvent event) {
//        // Get the user input from the text fields
//        String deliveryFees = deliveryFeesField.getText();
//        String state = stateField.getText();
//        String address = addressField.getText();
//
//        // Get the selected order ID from the ComboBox
//        Integer orderID = orderIDComboBox.getValue();
//        Integer agencyID = agencyIDComboBox.getValue();
//
//        // Convert the delivery fees, state, and agency ID to their respective data types
//        float deliveryFeesFloat = Float.parseFloat(deliveryFees);
//        int stateInt = Integer.parseInt(state);
//
//        try {
//            // Connect to the database
//            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tuni'art", "root", "");
//
//            // Prepare the SQL statement
//            String sql = "INSERT INTO delivery (order_id, estimated_date, delivery_fees, destination, state, agency_id) VALUES (?, DATE_ADD(NOW(), INTERVAL 3 DAY), ?, ?, ?, ?)";
//            PreparedStatement stmt = conn.prepareStatement(sql);
//            stmt.setInt(1, orderID); // Set the order ID
//            stmt.setFloat(2, deliveryFeesFloat);
//            stmt.setString(3, address);
//            stmt.setInt(4, stateInt);
//            stmt.setInt(5, agencyID);
//
//            // Execute the SQL statement
//            stmt.executeUpdate();
//
//            // Close the statement and connection
//            stmt.close();
//            conn.close();
//
//            // Show a success message
//            System.out.println("Delivery added to delivery table: " + address);
//
//            // Update the deliveryFeesLabel with the calculated delivery fees
//            deliveryFeesLabel.setText("Delivery Fees: " + deliveryFees + " TND");
//        } catch (SQLException e) {
//            // Show an error message
//            System.out.println("Error adding delivery to delivery table: " + e.getMessage());
//        }
//    }



//    private ObservableList<Integer> getAgencyIDs() {
//        // Create a list of agency IDs
//        ObservableList<Integer> agencyIDs = FXCollections.observableArrayList();
//
//        try {
//            // Connect to the database
//            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tuni'art", "root", "");
//
//            // Prepare the SQL statement
//            String sql = "SELECT agency_id FROM delivery_agency";
//            PreparedStatement stmt = conn.prepareStatement(sql);
//
//            // Execute the SQL statement
//            ResultSet rs = stmt.executeQuery();
//
//            // Iterate over the result set and add agency IDs to the list
//            while (rs.next()) {
//                agencyIDs.add(rs.getInt("agency_id"));
//            }
//
//            // Close the result set, statement, and connection
//            rs.close();
//            stmt.close();
//            conn.close();
//        } catch (SQLException e) {
//            // Show an error message
//            System.out.println("Error fetching agency IDs: " + e.getMessage());
//        }
//
//        return agencyIDs;
//    }
//

    private ObservableList<DeliveryAgency> getAgencies() {
        // Create a list of agencies
        ObservableList<DeliveryAgency> agencies = FXCollections.observableArrayList();

        try {
            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tuni'art", "root", "");

            // Prepare the SQL statement
            String sql = "SELECT agency_id, agency_name FROM delivery_agency";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Execute the SQL statement
            ResultSet rs = stmt.executeQuery();

            // Iterate over the result set and add agencies to the list
            while (rs.next()) {
                int id = rs.getInt("agency_id");
                String name = rs.getString("agency_name");
                DeliveryAgency agency = new DeliveryAgency(id, name, null, null);
                agencies.add(agency);
            }

            // Close the result set, statement, and connection
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            // Show an error message
            System.out.println("Error fetching agencies: " + e.getMessage());
        }

        return agencies;
    }

    private ObservableList<Integer> getAgencyIDs() {
        // Create a list of agency IDs
        ObservableList<Integer> agencyIDs = FXCollections.observableArrayList();

        try {
            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tuni'art", "root", "");

            // Prepare the SQL statement
            String sql = "SELECT agency_id FROM delivery_agency";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Execute the SQL statement
            ResultSet rs = stmt.executeQuery();

            // Iterate over the result set and add agency IDs to the list
            while (rs.next()) {
                agencyIDs.add(rs.getInt("agency_id"));
            }

            // Close the result set, statement, and connection
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            // Show an error message
            System.out.println("Error fetching agency IDs: " + e.getMessage());
        }

        return agencyIDs;
    }


    private ObservableList<String> getAgencyNames() {
        // Create a list of agency names
        ObservableList<String> agencyNames = FXCollections.observableArrayList();

        try {
            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tuni'art", "root", "");

            // Prepare the SQL statement
            String sql = "SELECT agency_name FROM delivery_agency";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Execute the SQL statement
            ResultSet rs = stmt.executeQuery();

            // Iterate over the result set and add agency names to the list
            while (rs.next()) {
                agencyNames.add(rs.getString("agency_name"));
            }

            // Close the result set, statement, and connection
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            // Show an error message
            System.out.println("Error fetching agency names: " + e.getMessage());
        }

        return agencyNames;
    }


    private ObservableList<Integer> getOrderIDs() {
        // Create a list of order IDs
        ObservableList<Integer> orderIDs = FXCollections.observableArrayList();

        try {
            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tuni'art", "root", "");

            // Prepare the SQL statement
            String sql = "SELECT order_id FROM `order`";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Execute the SQL statement
            ResultSet rs = stmt.executeQuery();

            // Iterate over the result set and add order IDs to the list
            while (rs.next()) {
                orderIDs.add(rs.getInt("order_id"));
            }

            // Close the result set, statement, and connection
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            // Show an error message
            System.out.println("Error fetching order IDs: " + e.getMessage());
        }

        return orderIDs;
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
}
