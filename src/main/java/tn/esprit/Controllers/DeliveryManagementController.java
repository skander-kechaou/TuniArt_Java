package tn.esprit.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import tn.esprit.entities.Delivery;
import tn.esprit.services.DeliveryService;
import tn.esprit.utils.MyDatabase;
import java.util.List;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class DeliveryManagementController {

    @FXML
    private TableView<Delivery> deliveryTable;

    @FXML
    private TextField orderIdField;

    @FXML
    private TextField estimatedDateField;

    @FXML
    private TextField deliveryFeesField;

    @FXML
    private TextField destinationField;

    @FXML
    private TextField stateField;

    @FXML
    private TextField agencyIdField;

    @FXML
    private TextField searchField; // Add this line

    private DeliveryService deliveryService;

    public DeliveryManagementController() {
        Connection connection = MyDatabase.getInstance().getConn();
        deliveryService = new DeliveryService();
    }

    @FXML
    private void initialize() throws SQLException {
        deliveryTable.getItems().addAll(deliveryService.diplayList());
    }

    @FXML
    private void addDelivery() throws SQLException {
        if (orderIdField.getText().isEmpty() || estimatedDateField.getText().isEmpty() || deliveryFeesField.getText().isEmpty() || destinationField.getText().isEmpty() || stateField.getText().isEmpty() || agencyIdField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please fill all fields");
            alert.showAndWait();
            return;
        }

        int orderId = Integer.parseInt(orderIdField.getText());
        LocalDate estimatedDate = LocalDate.parse(estimatedDateField.getText());
        float deliveryFees = Float.parseFloat(deliveryFeesField.getText());
        String destination = destinationField.getText();
        boolean state = Boolean.parseBoolean(stateField.getText());
        int agencyId = Integer.parseInt(agencyIdField.getText());

        Delivery delivery = new Delivery(0, orderId, estimatedDate, deliveryFees, destination, state, agencyId);
        deliveryService.add(delivery);
        deliveryTable.getItems().add(delivery);
    }


    @FXML
    private void updateDelivery() throws SQLException {
        Delivery selectedDelivery = deliveryTable.getSelectionModel().getSelectedItem();
        if (selectedDelivery != null) {
            int orderId = Integer.parseInt(orderIdField.getText());
            LocalDate estimatedDate = LocalDate.parse(estimatedDateField.getText());
            float deliveryFees = Float.parseFloat(deliveryFeesField.getText());
            String destination = destinationField.getText();
            boolean state = Boolean.parseBoolean(stateField.getText());
            int agencyId = Integer.parseInt(agencyIdField.getText());

            selectedDelivery.setOrderId(orderId);
            selectedDelivery.setEstimatedDate(estimatedDate);
            selectedDelivery.setDeliveryFees(deliveryFees);
            selectedDelivery.setDestination(destination);
            selectedDelivery.setState(state);
            selectedDelivery.setAgencyId(agencyId);

            deliveryService.update(selectedDelivery);

            deliveryTable.refresh(); // Refresh the TableView to reflect the changes
        }
    }

    @FXML
    private void deleteDelivery() throws SQLException {
        Delivery selectedDelivery = deliveryTable.getSelectionModel().getSelectedItem();
        if (selectedDelivery != null) {
            deliveryService.delete(selectedDelivery.getDeliveryId());
            deliveryTable.getItems().remove(selectedDelivery);
        }
    }

    @FXML
    private void searchDelivery() throws SQLException {
        String searchQuery = searchField.getText();
        List<Delivery> searchedDeliveries = deliveryService.search(searchQuery);
        deliveryTable.getItems().setAll(searchedDeliveries);
    }
}
