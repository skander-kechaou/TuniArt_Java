package tn.esprit.services;

import tn.esprit.entities.Art_Piece;
import tn.esprit.entities.Cart;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;

public class CartService {

    private Connection connection;
    Statement stm;

    public CartService() {
        connection = MyDatabase.getInstance().getConn();
    }

    public void save(Cart cart) {
        String sql = "INSERT INTO cart (uid, art_ref) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cart.getUserId());
            statement.setInt(2, cart.getArtRef());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Cart cart) {
        String sql = "UPDATE cart SET totalPrice = ?, uid = ? WHERE cart_ref = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setFloat(1, cart.getTotalPrice());
            statement.setInt(2, cart.getUserId());
            statement.setInt(3, cart.getCartId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Cart cart) {
        String sql = "DELETE FROM cart WHERE cart_ref = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cart.getCartId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Cart getCartByRef(int cartRef) {
        String sql = "SELECT * FROM cart WHERE cart_ref = ?";
        Cart cart = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cartRef);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                cart = new Cart();
                cart.setCartId(resultSet.getInt("cart_ref"));
                cart.setUserId(resultSet.getInt("uid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
    }

}
