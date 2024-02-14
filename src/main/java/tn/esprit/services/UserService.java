package tn.esprit.services;

import tn.esprit.entities.User;
import tn.esprit.utils.MyDatabase;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<User> {

    Connection con;
    Statement stm;

    public UserService() {
        con = MyDatabase.getInstance().getConn();
    }

    @Override
    public void add(User user) throws SQLException {
        String query = "INSERT INTO `user`( `fname`, `lname`, `email`, `gender`, `phone_nb`, `birth_date`, `password`) VALUES ('"+user.getFname()+"','"+user.getLname()+"','"+user.getEmail()+"','"+user.getGender()+"','"+user.getPhone_nb()+"','"+user.getBirth_date()+"','"+user.getPassword()+"')";
        stm = con.createStatement();
        stm.executeUpdate(query);
        System.out.println("User added!");
    }

    @Override
    public void addd(User user) throws SQLException {
        String query = "INSERT INTO `user`(`fname`, `lname`, `email`, `gender`, `phone_nb`, `birth_date`, `password`) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, user.getFname());
        ps.setString(2, user.getLname());
        ps.setString(3, user.getEmail());
        ps.setBoolean(4, user.getGender());
        ps.setInt(5, user.getPhone_nb());
        ps.setDate(6, user.getBirth_date());
        ps.setString(7, user.getPassword());

        ps.executeUpdate();
        System.out.println("User added!");
    }

    @Override
    public void update(User user) throws SQLException {
        String query = "UPDATE user SET fname=?, lname=?, email=?, gender=?, phone_nb=?, birth_date=?, password=?, verification_code=? WHERE uid=?";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, user.getFname());
        ps.setString(2, user.getLname());
        ps.setString(3, user.getEmail());
        ps.setBoolean(4, user.getGender());
        ps.setInt(5, user.getPhone_nb());
        ps.setDate(6, user.getBirth_date());
        ps.setString(7, user.getPassword());
        ps.setString(8, user.getVerification_code());
        ps.setInt(9, user.getUid());

        ps.executeUpdate();
        System.out.println("User updated!");

    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM user WHERE uid = ?";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("User deleted");
        } else {
            System.out.println("User with ID " + id + " not found");
        }
    }

    @Override
    public List<User> diplayList() throws SQLException {
        String query = "SELECT * FROM `user`";
        stm = con.createStatement();
        ResultSet res = stm.executeQuery(query);
        List<User> users = new ArrayList<>();
        while (res.next()) {
            int uid = res.getInt(1);
            String fname = res.getString(2);
            String lname = res.getString(3);
            String email = res.getString(4);
            Boolean gender = res.getBoolean(5);
            int phone_nb = res.getInt(6);
            String profile_pic = res.getString(7);
            Date birth_date = res.getDate(8);
            String password = res.getString(9);
            String verification_code = res.getString(10);

            User ur = new User(uid, fname, lname, email, gender, phone_nb, profile_pic, birth_date, password, verification_code);
            users.add(ur);
        }
        return users;
    }
}
