package tn.esprit.services;

import tn.esprit.entities.Delivery;
import tn.esprit.entities.Order;
import tn.esprit.entities.Art_Piece;

import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OrderService {
    private final Connection conn;

    public OrderService() {
        this.conn = MyDatabase.getInstance().getConn();
    }

    public void createOrder(Order order) {
        String query = "INSERT INTO `order` (uid, order_date, totalprice, status) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, order.getUserId());
            stmt.setDate(2, java.sql.Date.valueOf(order.getOrderDate()));
            stmt.setFloat(3, order.getTotalPrice());
            stmt.setInt(4, order.getStatus());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getOrdersByUserId(int userId) {
        String query = "SELECT o.*, a.* FROM `order` o JOIN cart c ON o.order_id = c.order_id JOIN art a ON c.art_ref = a.art_ref WHERE o.uid = ?";
        List<Order> orders = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getInt("uid"));
                order.setOrderDate(rs.getDate("order_date").toLocalDate());
                order.setTotalPrice(rs.getFloat("totalprice"));
                order.setStatus(rs.getInt("status"));

                Art_Piece art = new Art_Piece();
                art.setArt_ref(rs.getInt("art_ref"));
                art.setArt_title(rs.getString("art_title"));
                art.setArt_price(rs.getFloat("art_price"));
                // Set other art attributes here

                order.getArtList().add(art);

                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }




    public void delete(int orderId) throws SQLException {
        String query = "DELETE FROM `order` WHERE order_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, orderId);
            statement.executeUpdate();
        }
    }


    public void add(Order order) throws SQLException {
        String query = "INSERT INTO `order` (uid, order_date, totalprice, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, order.getUserId());
            statement.setDate(2, Date.valueOf(order.getOrderDate()));
            statement.setFloat(3, order.getTotalPrice());
            statement.setInt(4, order.getStatus());
            statement.executeUpdate();
        }
    }


    public void update(Order order) throws SQLException {
        String query = "UPDATE `order` SET uid = ?, order_date = ?, totalprice = ?, status = ? WHERE order_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, order.getUserId());
            statement.setDate(2, Date.valueOf(order.getOrderDate()));
            statement.setFloat(3, order.getTotalPrice());
            statement.setInt(4, order.getStatus());
            statement.setInt(5, order.getOrderId());
            statement.executeUpdate();
        }
    }


    public List<Order> displayList() throws SQLException {
        List<Order> orderList = new ArrayList<>();
        String query = "SELECT * FROM `order` ORDER BY order_id";
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                int userId = resultSet.getInt("uid");
                LocalDate orderDate = resultSet.getDate("order_date").toLocalDate();
                float totalPrice = resultSet.getFloat("totalprice");
                int status = resultSet.getInt("status");
                orderList.add(new Order(orderId, userId, totalPrice, orderDate, status));
            }
        }
        return orderList;
    }



    public List<Order> searchByUid(int uid) throws SQLException {
        List<Order> orderList = new ArrayList<>();
        String query = "SELECT * FROM `order` WHERE `uid` = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, uid);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int orderId = resultSet.getInt("order_id");
                    int userId = resultSet.getInt("uid");
                    LocalDate orderDate = resultSet.getDate("order_date").toLocalDate();
                    float totalPrice = resultSet.getFloat("totalprice");
                    int status = resultSet.getInt("status");
                    orderList.add(new Order(orderId, userId, totalPrice, orderDate, status));
                }
            }
        }
        return orderList;
    }

    public boolean hasDelivery(Order order) throws SQLException {
        String sql = "SELECT COUNT(*) FROM delivery WHERE order_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, order.getOrderId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
        return false;
    }




}
