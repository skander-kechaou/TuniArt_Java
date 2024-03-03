package tn.esprit.services;

import tn.esprit.entities.Admin;
import tn.esprit.entities.Artist;
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
        if (user instanceof Admin) {
            Admin admin = (Admin) user;
            String query = "INSERT INTO `user`(`fname`, `lname`, `email`, `gender`, `phone_nb`, `birth_date`,`profile_pic`, `password`, `role`) VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, admin.getFname());
            ps.setString(2, admin.getLname());
            ps.setString(3, admin.getEmail());
            ps.setBoolean(4, admin.getGender());
            ps.setInt(5, admin.getPhone_nb());
            ps.setDate(6, admin.getBirth_date());
            ps.setString(7, admin.getProfile_pic());
            ps.setString(8, admin.getPassword());
            ps.setString(9, "Admin");

            ps.executeUpdate();
            System.out.println("Admin added!");
        }
        else if (user instanceof Artist) {
            Artist artist = (Artist) user;
            String query = "INSERT INTO `user`(`fname`, `lname`, `email`, `gender`, `status`, `phone_nb`, `birth_date`,`profile_pic`, `password`, `role`) VALUES (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, artist.getFname());
            ps.setString(2, artist.getLname());
            ps.setString(3, artist.getEmail());
            ps.setBoolean(4, artist.getGender());
            ps.setBoolean(5, true);
            ps.setInt(6, artist.getPhone_nb());
            ps.setDate(7, artist.getBirth_date());
            ps.setString(8, artist.getProfile_pic());
            ps.setString(9, artist.getPassword());
            ps.setString(10, "Artist");
            ps.executeUpdate();
            System.out.println("Artist added!");
        }
        else if (user instanceof User) {
            String query = "INSERT INTO `user`(`fname`, `lname`, `email`, `gender`, `status`, `phone_nb`, `birth_date`,`profile_pic`, `password`, `role`) VALUES (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, user.getFname());
            ps.setString(2, user.getLname());
            ps.setString(3, user.getEmail());
            ps.setBoolean(4, user.getGender());
            ps.setBoolean(5, true);
            ps.setInt(6, user.getPhone_nb());
            ps.setDate(7, user.getBirth_date());
            ps.setString(8, user.getProfile_pic());
            ps.setString(9, user.getPassword());
            ps.setString(10, "User");
            ps.executeUpdate();
            System.out.println("User added!");
        }

    }

    @Override
    public void update(User user) throws SQLException {
        if (user instanceof Admin) {
            Admin admin = (Admin) user;
            String query = "UPDATE user SET fname=?, lname=?, email=?, gender=?, phone_nb=?, birth_date=?, password=?, profile_pic=?, role=? WHERE uid=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, admin.getFname());
            ps.setString(2, admin.getLname());
            ps.setString(3, admin.getEmail());
            ps.setBoolean(4, admin.getGender());
            ps.setInt(5, admin.getPhone_nb());
            ps.setDate(6, admin.getBirth_date());
            ps.setString(7, admin.getPassword());
            ps.setString(8, admin.getProfile_pic());
            ps.setString(9, admin.getRole());
            ps.setInt(10, admin.getUid());

            ps.executeUpdate();
            System.out.println("Admin updated!");
        }

        else if (user instanceof Artist) {
            Artist artist = (Artist) user;
            String query = "UPDATE user SET fname=?, lname=?, email=?, gender=?, phone_nb=?, birth_date=?, password=?, verification_code=?, profile_pic=?, role=?, status=?, biography=?, portfolio=?, profileViews=? WHERE uid=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, artist.getFname());
            ps.setString(2, artist.getLname());
            ps.setString(3, artist.getEmail());
            ps.setBoolean(4, artist.getGender());
            ps.setInt(5, artist.getPhone_nb());
            ps.setDate(6, artist.getBirth_date());
            ps.setString(7, artist.getPassword());
            ps.setString(8, artist.getVerification_code());
            ps.setString(9, artist.getProfile_pic());
            ps.setString(10, artist.getRole());
            ps.setBoolean(11, artist.getStatus());
            ps.setString(12, artist.getBiography());
            ps.setString(13, artist.getPortfolio());
            ps.setInt(14, artist.getProfileViews());
            ps.setInt(15, user.getUid());

            ps.executeUpdate();
            System.out.println("Artist updated!");
        }
        else if (user instanceof User) {
            String query = "UPDATE user SET fname=?, lname=?, email=?, gender=?, phone_nb=?, birth_date=?, password=?, verification_code=?, profile_pic=?, role=?, status=?, profileViews=? WHERE uid=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, user.getFname());
            ps.setString(2, user.getLname());
            ps.setString(3, user.getEmail());
            ps.setBoolean(4, user.getGender());
            ps.setInt(5, user.getPhone_nb());
            ps.setDate(6, user.getBirth_date());
            ps.setString(7, user.getPassword());
            ps.setString(8, user.getVerification_code());
            ps.setString(9, user.getProfile_pic());
            ps.setString(10, user.getRole());
            ps.setBoolean(11, user.getStatus());
            ps.setInt(12, user.getProfileViews());
            ps.setInt(13, user.getUid());

            ps.executeUpdate();
            System.out.println("User updated!");
        }

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
            boolean gender = res.getBoolean(5);
            int phone_nb = res.getInt(6);
            String profile_pic = res.getString(7);
            Date birth_date = res.getDate(8);
            String password = res.getString(9);
            String verification_code = res.getString(10);
            String biography = res.getString(11);
            String portfolio = res.getString(12);
            String role = res.getString(13);
            boolean status= res.getBoolean(14);
            int profileViews = res.getInt(15);

            User ur = null;
            if (role.equals("User")) {
                ur = new User(uid, fname, lname, email, gender, status, phone_nb, birth_date, profile_pic, password, verification_code, role, profileViews);
            }
            else if (role.equals("Admin")) {
                ur = new Admin(uid, fname, lname, email, gender, phone_nb, birth_date, password, role);
            }
            else if (role.equals("Artist")) {
                ur = new Artist(uid, fname, lname, email, gender, status, phone_nb, birth_date, profile_pic, password, verification_code, role, biography, portfolio, profileViews);
            }

            users.add(ur);
        }
        return users;
    }

    public List<User> diplayListUsersAndArtists() throws SQLException {
        String query = "SELECT * FROM `user` WHERE role != 'Admin'";
        stm = con.createStatement();
        ResultSet res = stm.executeQuery(query);
        List<User> users = new ArrayList<>();
        while (res.next()) {
            int uid = res.getInt(1);
            String fname = res.getString(2);
            String lname = res.getString(3);
            String email = res.getString(4);
            boolean gender = res.getBoolean(5);
            int phone_nb = res.getInt(6);
            String profile_pic = res.getString(7);
            Date birth_date = res.getDate(8);
            String password = res.getString(9);
            String verification_code = res.getString(10);
            String biography = res.getString(11);
            String portfolio = res.getString(12);
            String role = res.getString(13);
            boolean status= res.getBoolean(14);
            int profileViews = res.getInt(15);

            User ur = null;
            if (role.equals("User")) {
                ur = new User(uid, fname, lname, email, gender, status, phone_nb, birth_date, profile_pic, password, verification_code, role, profileViews);
            }
            else if (role.equals("Artist")) {
                ur = new Artist(uid, fname, lname, email, gender, status, phone_nb, birth_date, profile_pic, password, verification_code, role, biography, portfolio, profileViews);
            }

            users.add(ur);
        }
        return users;
    }



    public boolean isEmailUnique(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM user WHERE email = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count == 0;
                }
            }
        }
        return false;
    }

    public boolean doesEmailExist(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM user WHERE email = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // Return true if count is greater than 0, indicating the email exists
                }
            }
        }
        return false; // Return false if an exception occurs or no rows are found
    }


    public boolean loginMatch(String email, String password) throws SQLException {
        String query = "SELECT password FROM user WHERE email = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    // Check if the stored password matches the provided password
                    return storedPassword.equals(password);
                }
            }
        }
        // If no matching user was found, return false
        return false;
    }

    public int getUidByEmail(String email) throws SQLException {
        String query = "SELECT uid FROM user WHERE email = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("uid");
                } else {
                    // Email not found
                    return -1;
                }
            }
        }
    }

    public User searchByUid(int uid) throws SQLException {
        String query = "SELECT * FROM user WHERE uid = ?";
        User ur = null;
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, uid);
            try (ResultSet res = statement.executeQuery()) {
                if (res.next()) {
                    String fname = res.getString(2);
                    String lname = res.getString(3);
                    String email = res.getString(4);
                    boolean gender = res.getBoolean(5);
                    int phone_nb = res.getInt(6);
                    String profile_pic = res.getString(7);
                    Date birth_date = res.getDate(8);
                    String password = res.getString(9);
                    String verification_code = res.getString(10);
                    String biography = res.getString(11);
                    String portfolio = res.getString(12);
                    String role = res.getString(13);
                    boolean status = res.getBoolean(14);
                    int profileViews = res.getInt(15);

                    ur = null;
                    if (role.equals("User")) {
                        ur = new User(uid, fname, lname, email, gender, status, phone_nb, birth_date, profile_pic, password, verification_code, role, profileViews);
                    } else if (role.equals("Admin")) {
                        ur = new Admin(uid, fname, lname, email, gender, phone_nb, birth_date, password, role);
                    } else if (role.equals("Artist")) {
                        ur = new Artist(uid, fname, lname, email, gender, status, phone_nb, birth_date, profile_pic, password, verification_code, role, biography, portfolio, profileViews);

                    }
                }
            }
        }
        return ur;
    }

    public Artist searchArtistByUid(int uid) throws SQLException {
        String query = "SELECT * FROM user WHERE uid = ?";
        Artist ur = null;
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, uid);
            try (ResultSet res = statement.executeQuery()) {
                if (res.next()) {
                    String fname = res.getString(2);
                    String lname = res.getString(3);
                    String email = res.getString(4);
                    boolean gender = res.getBoolean(5);
                    int phone_nb = res.getInt(6);
                    String profile_pic = res.getString(7);
                    Date birth_date = res.getDate(8);
                    String password = res.getString(9);
                    String verification_code = res.getString(10);
                    String biography = res.getString(11);
                    String portfolio = res.getString(12);
                    String role = res.getString(13);
                    boolean status = res.getBoolean(14);

                    ur = null;

                    if (role.equals("Artist")) {
                        ur = new Artist(uid, fname, lname, email, gender, status, phone_nb, birth_date, profile_pic, password, verification_code, role, biography, portfolio);

                    }
                }
            }
        }
        return ur;
    }

    public void addFollower(int follower_id, int following_id) throws SQLException {
        String query = "INSERT INTO `followers`(`follower_id`, `following_id`) VALUES (?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, follower_id);
        ps.setInt(2, following_id);

        ps.executeUpdate();
        System.out.println("Follower added!");
    }

    public void deleteFollower(int follower_id, int following_id) throws SQLException {
        String query = "DELETE FROM `followers` WHERE `follower_id`=? AND `following_id`=?";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setInt(1, follower_id);
        ps.setInt(2, following_id);
        int rowsAffected = ps.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Follower deleted");
        } else {
            System.out.println("Follower with ID " + follower_id + " not found");
        }
    }

    public int countFollowers(int user_id) throws SQLException {
        String query = "SELECT COUNT(*) FROM followers WHERE following_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, user_id);

        ResultSet rs = ps.executeQuery();
        rs.next(); // Move the cursor to the first row
        int count = rs.getInt(1); // Get the count from the first column of the result set

        // Close resources
        rs.close();
        ps.close();

        return count;
    }

    public int countFollowing(int user_id) throws SQLException {
        String query = "SELECT COUNT(*) FROM followers WHERE follower_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, user_id);

        ResultSet rs = ps.executeQuery();
        rs.next(); // Move the cursor to the first row
        int count = rs.getInt(1); // Get the count from the first column of the result set

        // Close resources
        rs.close();
        ps.close();

        return count;
    }


    public boolean isFollowing(int follower_id, int following_id) throws SQLException {
        String query = "SELECT COUNT(*) FROM followers WHERE follower_id = ? AND following_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, follower_id);
        ps.setInt(2, following_id);

        ResultSet rs = ps.executeQuery();
        rs.next(); // Move the cursor to the first row
        int count = rs.getInt(1); // Get the count from the first column of the result set

        // Close resources
        rs.close();
        ps.close();

        return count > 0;
    }

    public List<User> searchByName(String input) throws SQLException {
        String query = "SELECT * FROM user WHERE fname LIKE CONCAT('%', ?, '%') OR lname LIKE CONCAT('%', ?, '%')";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, input);
        statement.setString(2, input);

        ResultSet res = statement.executeQuery();

        List<User> users = new ArrayList<>();
        while (res.next()) {
            int uid = res.getInt(1);
            String fname = res.getString(2);
            String lname = res.getString(3);
            String email = res.getString(4);
            boolean gender = res.getBoolean(5);
            int phone_nb = res.getInt(6);
            String profile_pic = res.getString(7);
            Date birth_date = res.getDate(8);
            String password = res.getString(9);
            String verification_code = res.getString(10);
            String biography = res.getString(11);
            String portfolio = res.getString(12);
            String role = res.getString(13);
            boolean status= res.getBoolean(14);
            int profileViews = res.getInt(15);

            User ur = null;
            if (role.equals("User")) {
                ur = new User(uid, fname, lname, email, gender, status, phone_nb, birth_date, profile_pic, password, verification_code, role, profileViews);
            } else if (role.equals("Artist")) {
                ur = new Artist(uid, fname, lname, email, gender, status, phone_nb, birth_date, profile_pic, password, verification_code, role, biography, portfolio, profileViews);
            }

            users.add(ur);
        }
        return users;
    }


    public List<User> SortByFirstName() throws SQLException {
        String query = "SELECT * FROM user WHERE role != 'Admin' ORDER BY fname";
        PreparedStatement statement = con.prepareStatement(query);

        ResultSet res = statement.executeQuery();

        List<User> users = new ArrayList<>();
        while (res.next()) {
            int uid = res.getInt(1);
            String fname = res.getString(2);
            String lname = res.getString(3);
            String email = res.getString(4);
            boolean gender = res.getBoolean(5);
            int phone_nb = res.getInt(6);
            String profile_pic = res.getString(7);
            Date birth_date = res.getDate(8);
            String password = res.getString(9);
            String verification_code = res.getString(10);
            String biography = res.getString(11);
            String portfolio = res.getString(12);
            String role = res.getString(13);
            boolean status= res.getBoolean(14);
            int profileViews = res.getInt(15);

            User ur = null;
            if (role.equals("User")) {
                ur = new User(uid, fname, lname, email, gender, status, phone_nb, birth_date, profile_pic, password, verification_code, role, profileViews);
            } else if (role.equals("Artist")) {
                ur = new Artist(uid, fname, lname, email, gender, status, phone_nb, birth_date, profile_pic, password, verification_code, role, biography, portfolio, profileViews);
            }

            users.add(ur);
        }
        return users;
    }

    public List<User> SortByLastName() throws SQLException {
        String query = "SELECT * FROM user WHERE role != 'Admin' ORDER BY lname";
        PreparedStatement statement = con.prepareStatement(query);

        ResultSet res = statement.executeQuery();

        List<User> users = new ArrayList<>();
        while (res.next()) {
            int uid = res.getInt(1);
            String fname = res.getString(2);
            String lname = res.getString(3);
            String email = res.getString(4);
            boolean gender = res.getBoolean(5);
            int phone_nb = res.getInt(6);
            String profile_pic = res.getString(7);
            Date birth_date = res.getDate(8);
            String password = res.getString(9);
            String verification_code = res.getString(10);
            String biography = res.getString(11);
            String portfolio = res.getString(12);
            String role = res.getString(13);
            boolean status= res.getBoolean(14);
            int profileViews = res.getInt(15);

            User ur = null;
            if (role.equals("User")) {
                ur = new User(uid, fname, lname, email, gender, status, phone_nb, birth_date, profile_pic, password, verification_code, role, profileViews);
            } else if (role.equals("Artist")) {
                ur = new Artist(uid, fname, lname, email, gender, status, phone_nb, birth_date, profile_pic, password, verification_code, role, biography, portfolio, profileViews);
            }

            users.add(ur);
        }
        return users;
    }

    public List<User> SortByFollowers() throws SQLException {
        String query = "SELECT u.*, COUNT(f.following_id) AS followerCount " +
                "FROM user u " +
                "LEFT JOIN followers f ON u.uid = f.following_id " +
                "WHERE role != 'Admin'"+
                "GROUP BY u.uid " +
                "ORDER BY followerCount DESC";
        PreparedStatement statement = con.prepareStatement(query);

        ResultSet res = statement.executeQuery();

        List<User> users = new ArrayList<>();
        while (res.next()) {
            int uid = res.getInt(1);
            String fname = res.getString(2);
            String lname = res.getString(3);
            String email = res.getString(4);
            boolean gender = res.getBoolean(5);
            int phone_nb = res.getInt(6);
            String profile_pic = res.getString(7);
            Date birth_date = res.getDate(8);
            String password = res.getString(9);
            String verification_code = res.getString(10);
            String biography = res.getString(11);
            String portfolio = res.getString(12);
            String role = res.getString(13);
            boolean status= res.getBoolean(14);
            int profileViews = res.getInt(15);

            User ur = null;
            if (role.equals("User")) {
                ur = new User(uid, fname, lname, email, gender, status, phone_nb, birth_date, profile_pic, password, verification_code, role, profileViews);
            } else if (role.equals("Artist")) {
                ur = new Artist(uid, fname, lname, email, gender, status, phone_nb, birth_date, profile_pic, password, verification_code, role, biography, portfolio, profileViews);
            }

            users.add(ur);
        }
        return users;
    }

    public List<User> SortByRole(String input) throws SQLException {
        String query = "SELECT * FROM user WHERE role = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, input);

        ResultSet res = statement.executeQuery();

        List<User> users = new ArrayList<>();
        while (res.next()) {
            int uid = res.getInt(1);
            String fname = res.getString(2);
            String lname = res.getString(3);
            String email = res.getString(4);
            boolean gender = res.getBoolean(5);
            int phone_nb = res.getInt(6);
            String profile_pic = res.getString(7);
            Date birth_date = res.getDate(8);
            String password = res.getString(9);
            String verification_code = res.getString(10);
            String biography = res.getString(11);
            String portfolio = res.getString(12);
            String role = res.getString(13);
            boolean status= res.getBoolean(14);
            int profileViews = res.getInt(15);

            User ur = null;
            if (role.equals("User")) {
                ur = new User(uid, fname, lname, email, gender, status, phone_nb, birth_date, profile_pic, password, verification_code, role, profileViews);
            } else if (role.equals("Artist")) {
                ur = new Artist(uid, fname, lname, email, gender, status, phone_nb, birth_date, profile_pic, password, verification_code, role, biography, portfolio, profileViews);
            }

            users.add(ur);
        }
        return users;
    }
}
