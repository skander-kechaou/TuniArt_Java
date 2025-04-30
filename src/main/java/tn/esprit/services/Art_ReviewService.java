package tn.esprit.services;

import tn.esprit.entities.Art_Piece;
import tn.esprit.entities.Art_Review;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class Art_ReviewService implements IService <Art_Review> {


        Connection con;
        Statement stm;

        public Art_ReviewService(){
            con = MyDatabase.getInstance().getConn();
        }

        @Override
        public void add(Art_Review Art_Review) throws SQLException {
            String query = "INSERT INTO `review`( `Art_ref`, `uid`, `Date_published`, `Rating`, `Comment`) VALUES ('"+Art_Review.getArt_Ref()+"','"+Art_Review.getUid()+"','"+Art_Review.getDate_published()+"','"+Art_Review.getRating()+"','"+Art_Review.getComment()+"')";
            stm = con.createStatement();
            stm.executeUpdate(query);
            System.out.println("Art Review added!");
        }

        @Override
        public void addd(Art_Review Art_Review) throws SQLException {
            String query = "INSERT INTO `review`( `Art_ref`, `uid`, `Date_published`, `Rating`, `Comment`) VALUES (?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, Art_Review.getArt_Ref());
            ps.setInt(2, Art_Review.getUid());
            ps.setDate(3, Art_Review.getDate_published());
            ps.setInt(4, Art_Review.getRating());
            ps.setString(5, Art_Review.getComment());

            ps.executeUpdate();
            System.out.println("Art_Review added!");
        }

        @Override
        public void update(Art_Review Art_Review) throws SQLException {
            String query = "UPDATE review SET Art_ref=?, uid=?, Date_published=?, Rating=?, comment=? WHERE Review_id=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, Art_Review.getArt_Ref());
            ps.setInt(2, Art_Review.getUid());
            ps.setDate(3, Art_Review.getDate_published());
            ps.setInt(4, Art_Review.getRating());
            ps.setString(5, Art_Review.getComment());

            ps.executeUpdate();
            System.out.println("Art_Piece updated!");
        }

        @Override
        public void delete(int id) throws SQLException {
            String query = "DELETE FROM review WHERE Review_id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Art review deleted");
        }

        @Override
        public List<Art_Review> diplayList() throws SQLException {
            String query = "SELECT * FROM `review`";
            stm = con.createStatement();
            ResultSet res = stm.executeQuery(query);
            List<Art_Review> Art_Reviews = new ArrayList<>();
            while (res.next()) {
                int Review_id = res.getInt(1);
                int art_ref = res.getInt(2);
                int uid = res.getInt(3);
                Date date_published = res.getDate(4);
                int rating = res.getInt(5);
                String comment = res.getString(6);


                Art_Review a = new Art_Review(Review_id,art_ref, uid, date_published, rating, comment);
                Art_Reviews.add(a);
            }
            return Art_Reviews;
        }
    }


