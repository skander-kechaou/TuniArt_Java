package tn.esprit.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import tn.esprit.entities.Order;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import tn.esprit.services.OrderService;
import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;

public class OrderManagementController {

    @FXML
    private TableView<Order> OrderTable;

    @FXML
    private TextField orderIdField;

    @FXML
    private BorderPane borderpane;


    @FXML
    private TextField userIdField;

    @FXML
    private TextField totalPriceField;

   /// @FXML
   // private TextField orderDateField;

    @FXML
    private TextField statusField;

    @FXML
    private TextField searchField;

    private OrderService orderService;

    @FXML
    private ChoiceBox<Integer> statusChoiceBox;

    // Reference to the DashboardController
    private Dashboard dashboardController;

    // Setter method to pass the DashboardController reference
    public void setDashboardController(Dashboard dashboardController) {
        this.dashboardController = dashboardController; }

    public OrderManagementController() {
        Connection connection = MyDatabase.getInstance().getConn();
        orderService = new OrderService();
    }
    @FXML
    private void refreshTableView() throws SQLException {
        OrderTable.getItems().clear();
        OrderTable.getItems().addAll(orderService.displayList());
    }
    @FXML
    private void refreshAndSortTableView() throws SQLException {
        OrderTable.getItems().clear();
        List<Order> orderList = orderService.displayList();
        orderList.sort(Order.uidComparator); // Sort the list by uid
        OrderTable.getItems().addAll(orderList);
    }

    public void setBorderPane(BorderPane borderpane) {
        this.borderpane = borderpane;
    }

    @FXML
    private void initialize() throws SQLException {
        TableColumn<Order, Integer> orderIdColumn = new TableColumn<>("Order ID");
        orderIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOrderId()).asObject());

        TableColumn<Order, Integer> userIdColumn = new TableColumn<>("User ID");
        userIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getUserId()));

        TableColumn<Order, Float> totalPriceColumn = new TableColumn<>("Total Price");
        totalPriceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTotalPrice()));

        TableColumn<Order, LocalDate> orderDateColumn = new TableColumn<>("Order Date");
        orderDateColumn.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getOrderDate();
            if (date != null) {
                return new SimpleObjectProperty<>(date);
            } else {
                return new SimpleObjectProperty<>();
            }
        });
        TableColumn<Order, Integer> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatus()));

       // OrderTable.getColumns().addAll(orderIdColumn, userIdColumn, totalPriceColumn, orderDateColumn, statusColumn);
        OrderTable.getItems().addAll(orderService.displayList());
    }
/*
    @FXML
    private void addOrder() throws SQLException {
        int userId = Integer.parseInt(userIdField.getText());
        float totalPrice = Float.parseFloat(totalPriceField.getText());
        LocalDate orderDate = LocalDate.parse(orderDateField.getText());
        int status = Integer.parseInt(statusField.getText());

        Order order = new Order(userId, totalPrice, orderDate, status);
        orderService.add(order);
        OrderTable.getItems().add(order);
        refreshTableView();
    }

    @FXML
    private void updateOrder() throws SQLException {
        Order selectedOrder = OrderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            try {
                int orderId = Integer.parseInt(orderIdField.getText());
                int userId = Integer.parseInt(userIdField.getText());
                float totalPrice = Float.parseFloat(totalPriceField.getText());
                LocalDate orderDate = LocalDate.parse(orderDateField.getText());
                int status = Integer.parseInt(statusField.getText());

                selectedOrder.setOrderId(orderId);
                selectedOrder.setUserId(userId);
                selectedOrder.setTotalPrice(totalPrice);
                selectedOrder.setOrderDate(orderDate);
                selectedOrder.setStatus(status);

                orderService.update(selectedOrder);

                // Provide feedback to the user
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Order Updated");
                alert.setHeaderText(null);
                alert.setContentText("Order updated successfully!");
                alert.showAndWait();
                refreshTableView();
            } catch (NumberFormatException | DateTimeParseException e) {
                // Handle parsing errors
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter valid values for the fields!");
                alert.showAndWait();
            } catch (SQLException e) {
                // Handle database errors
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while updating the order. Please try again later.");
                alert.showAndWait();
            }
        } else {
            // No order selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select an order to update!");
            alert.showAndWait();
        }
    }
*/
@FXML
private DatePicker orderDatePicker;

    @FXML
    private void addOrder() throws SQLException {
        int userId = Integer.parseInt(userIdField.getText());
        float totalPrice = Float.parseFloat(totalPriceField.getText());
        LocalDate orderDate = orderDatePicker.getValue();

        if (orderDate != null && !orderDate.isBefore(LocalDate.now())) {
            int status = statusChoiceBox.getValue(); // Use the selected value from the ChoiceBox

            Order order = new Order(userId, totalPrice, orderDate, status);
            orderService.add(order);
            OrderTable.getItems().add(order);
            refreshTableView();
        } else {
            // Provide feedback to the user that the selected date is invalid
            showAlert("Invalid Date", "Please select a valid date from the current date onwards.");
        }
    }



    @FXML
    private void updateOrder() throws SQLException {
        Order selectedOrder = OrderTable.getSelectionModel().getSelectedItem();

        if (selectedOrder != null) {
            try {
                int orderId = Integer.parseInt(orderIdField.getText());
                int userId = Integer.parseInt(userIdField.getText());
                float totalPrice = Float.parseFloat(totalPriceField.getText());
                //LocalDate orderDate = LocalDate.parse(orderDateField.getText());
                LocalDate orderDate = orderDatePicker.getValue(); // Use DatePicker value

                int status = statusChoiceBox.getValue(); // Use the selected value from the ChoiceBox

                selectedOrder.setOrderId(orderId);
                selectedOrder.setUserId(userId);
                selectedOrder.setTotalPrice(totalPrice);
                selectedOrder.setOrderDate(orderDate);
                selectedOrder.setStatus(status);

                orderService.update(selectedOrder);

                // Provide feedback to the user
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Order Updated");
                alert.setHeaderText(null);
                alert.setContentText("Order updated successfully!");
                alert.showAndWait();

                refreshTableView(); // Optionally, refresh the table view after the update
            } catch (NumberFormatException | DateTimeParseException e) {
                // Handle parsing errors
                showAlert("Error", "Please enter valid values for the fields!");
            } catch (SQLException e) {
                // Handle database errors
                showAlert("Error", "An error occurred while updating the order. Please try again later.");
            }
        } else {
            // No order selected
            showAlert("Error", "Please select an order to update!");
        }
    }

    // Helper method to show an alert dialog
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }




    @FXML
    private void deleteOrder() throws SQLException {
        Order selectedOrder = OrderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            orderService.delete(selectedOrder.getOrderId());
            OrderTable.getItems().remove(selectedOrder);
        }
    }


    @FXML
    private void searchOrder() throws SQLException {
        int uid = Integer.parseInt(searchField.getText());
        List<Order> searchedOrders = orderService.searchByUid(uid);
        OrderTable.getItems().setAll(searchedOrders);
    }
    @FXML
    private void handleSortButton(ActionEvent event) throws SQLException {
        refreshAndSortTableView(); // This will sort the table by uid
    }
    @FXML
    private void handleSortDateButton(ActionEvent event) throws SQLException {
        OrderTable.getItems().clear();
        List<Order> orderList = orderService.displayList();
        orderList.sort(Order.dateComparator); // Assuming you have a dateComparator in your Order class
        OrderTable.getItems().addAll(orderList);
    }



}
