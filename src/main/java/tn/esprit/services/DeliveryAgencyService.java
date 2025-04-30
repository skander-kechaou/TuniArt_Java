package tn.esprit.services;

import tn.esprit.entities.DeliveryAgency;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryAgencyService implements IService<DeliveryAgency> {

    private final Connection connection;

    public DeliveryAgencyService() {
        connection = MyDatabase.getInstance().getConn();
    }

    @Override
    public void add(DeliveryAgency deliveryAgency) throws SQLException {
        String query = "INSERT INTO delivery_agency (agency_id, agency_name, agency_address, nb_deliveries) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, deliveryAgency.getAgencyId());
            statement.setString(2, deliveryAgency.getAgencyName());
            statement.setString(3, deliveryAgency.getAgencyAddress());
            statement.setInt(4, deliveryAgency.getNbDeliveries());
            statement.executeUpdate();
        }
    }

    @Override
    public void addd(DeliveryAgency deliveryAgency) throws SQLException {

    }

    @Override
    public void update(DeliveryAgency deliveryAgency) throws SQLException {
        String query = "UPDATE delivery_agency SET agency_name = ?, agency_address = ?, nb_deliveries = ? WHERE agency_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, deliveryAgency.getAgencyName());
            statement.setString(2, deliveryAgency.getAgencyAddress());
            statement.setInt(3, deliveryAgency.getNbDeliveries());
            statement.setInt(4, deliveryAgency.getAgencyId());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM delivery_agency WHERE agency_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public List<DeliveryAgency> diplayList() throws SQLException {
        List<DeliveryAgency> deliveryAgencies = new ArrayList<>();
        String query = "SELECT * FROM delivery_agency";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                DeliveryAgency deliveryAgency = new DeliveryAgency();
                deliveryAgency.setAgencyId(resultSet.getInt("agency_id"));
                deliveryAgency.setAgencyName(resultSet.getString("agency_name"));
                deliveryAgency.setAgencyAddress(resultSet.getString("agency_address"));
                deliveryAgency.setNbDeliveries(resultSet.getInt("nb_deliveries"));
                deliveryAgencies.add(deliveryAgency);
            }
        }
        return deliveryAgencies;
    }

    public DeliveryAgency getAgencyByName(String name) throws SQLException {
        String query = "SELECT * FROM delivery_agency WHERE agency_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                DeliveryAgency deliveryAgency = new DeliveryAgency();
                deliveryAgency.setAgencyId(resultSet.getInt("agency_id"));
                deliveryAgency.setAgencyName(resultSet.getString("agency_name"));
                deliveryAgency.setAgencyAddress(resultSet.getString("agency_address"));
                deliveryAgency.setNbDeliveries(resultSet.getInt("nb_deliveries"));
                return deliveryAgency;
            }
        }
        return null; // Return null if agency with the given name is not found
    }

    public DeliveryAgency getAgencyById(Integer id) throws SQLException {
        String query = "SELECT * FROM delivery_agency WHERE agency_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                DeliveryAgency deliveryAgency = new DeliveryAgency();
                deliveryAgency.setAgencyId(resultSet.getInt("agency_id"));
                deliveryAgency.setAgencyName(resultSet.getString("agency_name"));
                deliveryAgency.setAgencyAddress(resultSet.getString("agency_address"));
                deliveryAgency.setNbDeliveries(resultSet.getInt("nb_deliveries"));
                return deliveryAgency;
            }
        }
        return null; // Return null if agency with the given name is not found
    }

}
