package tn.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.entities.DeliveryAgency;
import tn.esprit.services.DeliveryAgencyService;
import tn.esprit.utils.MyDatabase;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DeliveryAgencyManagementController {

    @FXML
    private TextField agencyIdTextField;

    @FXML
    private TextField agencyNameTextField;

    @FXML
    private TextField agencyAddressTextField;

    @FXML
    private TextField nbDeliveriesTextField;

    @FXML
    private TableView<DeliveryAgency> tableView;

    @FXML
    private TableColumn<DeliveryAgency, Integer> agencyIdColumn;

    @FXML
    private TableColumn<DeliveryAgency, String> agencyNameColumn;

    @FXML
    private TableColumn<DeliveryAgency, String> agencyAddressColumn;

    @FXML
    private TableColumn<DeliveryAgency, Integer> nbDeliveriesColumn;

    private final DeliveryAgencyService deliveryAgencyService;

    public DeliveryAgencyManagementController() {
        Connection connection = MyDatabase.getInstance().getConn();
        deliveryAgencyService = new DeliveryAgencyService();
    }

    @FXML
    public void initialize() {
        try {
            ObservableList<DeliveryAgency> deliveryAgencies = FXCollections
                    .observableArrayList(deliveryAgencyService.diplayList());
            tableView.setItems(deliveryAgencies);

            agencyIdColumn.setCellValueFactory(new PropertyValueFactory<>("agencyId"));
            agencyNameColumn.setCellValueFactory(new PropertyValueFactory<>("agencyName"));
            agencyAddressColumn.setCellValueFactory(new PropertyValueFactory<>("agencyAddress"));
            nbDeliveriesColumn.setCellValueFactory(new PropertyValueFactory<>("nbDeliveries"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addAgency() {
        try {
            DeliveryAgency newAgency = new DeliveryAgency();
            newAgency.setAgencyId(Integer.parseInt(agencyIdTextField.getText()));
            newAgency.setAgencyName(agencyNameTextField.getText());
            newAgency.setAgencyAddress(agencyAddressTextField.getText());
            newAgency.setNbDeliveries(Integer.parseInt(nbDeliveriesTextField.getText()));

            deliveryAgencyService.add(newAgency);

            tableView.getItems().add(newAgency);

            clearFormFields();
        } catch (NumberFormatException | SQLException e) {
            showErrorAlert("Error adding agency", "An error occurred while adding the agency to the database");
        }
    }

    @FXML
    public void updateAgency() {
        try {
            DeliveryAgency updatedAgency = new DeliveryAgency();
            updatedAgency.setAgencyId(Integer.parseInt(agencyIdTextField.getText()));
            updatedAgency.setAgencyName(agencyNameTextField.getText());
            updatedAgency.setAgencyAddress(agencyAddressTextField.getText());
            updatedAgency.setNbDeliveries(Integer.parseInt(nbDeliveriesTextField.getText()));

            deliveryAgencyService.update(updatedAgency);

            DeliveryAgency selectedAgency = tableView.getSelectionModel().getSelectedItem();
            if (selectedAgency != null) {
                selectedAgency.setAgencyName(updatedAgency.getAgencyName());
                selectedAgency.setAgencyAddress(updatedAgency.getAgencyAddress());
                selectedAgency.setNbDeliveries(updatedAgency.getNbDeliveries());
            }

            tableView.refresh(); // Refresh the TableView to reflect the changes

            clearFormFields();
        } catch (NumberFormatException | SQLException e) {
            showErrorAlert("Error updating agency", "An error occurred while updating the agency in the database");
        }
    }

    @FXML
    public void deleteAgency() {
        try {
            DeliveryAgency selectedAgency = tableView.getSelectionModel().getSelectedItem();
            if (selectedAgency != null) {
                deliveryAgencyService.delete(selectedAgency.getAgencyId());
                tableView.getItems().remove(selectedAgency);
            }

            clearFormFields();
        } catch (SQLException e) {
            showErrorAlert("Error deleting agency", "An error occurred while deleting the agency from the database");
        }
    }

    private void clearFormFields() {
        agencyIdTextField.clear();
        agencyNameTextField.clear();
        agencyAddressTextField.clear();
        nbDeliveriesTextField.clear();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
