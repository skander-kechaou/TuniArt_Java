package tn.esprit.services;

import tn.esprit.entities.Art_Piece;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class Art_PieceService implements IService<Art_Piece> {

    Connection con;
    Statement stm;

    public Art_PieceService(){
        con = MyDatabase.getInstance().getConn();
    }

    @Override
    public void add(Art_Piece Art_Piece) throws SQLException {
        String query = "INSERT INTO `art`( `art_title`, `art_price`, `artist_id`, `type`, `creation`, `description`, `style`,`image_id`,`music_path`) VALUES ('"+Art_Piece.getArt_title()+"','"+Art_Piece.getArt_price()+"','"+Art_Piece.getaid()+"','"+Art_Piece.getType()+"','"+Art_Piece.getCreation()+"','"+Art_Piece.getDescription()+"','"+Art_Piece.getStyle()+"','"+Art_Piece.getImage_path()+"','"+Art_Piece.getMusic_path()+"')";
        stm = con.createStatement();
        stm.executeUpdate(query);
        System.out.println("Art_Piece added!");
    }

    @Override
    public void addd(Art_Piece Art_Piece) throws SQLException {
        String query = "INSERT INTO `art`( `art_title`, `art_price`, `artist_id`, `type`, `creation`, `description`, `style`,`image_id`,`music_path`) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, Art_Piece.getArt_title());
        ps.setFloat(2, Art_Piece.getArt_price());
        ps.setInt(3, Art_Piece.getaid());
        ps.setString(4, Art_Piece.getType());
        ps.setDate(5, Art_Piece.getCreation());
        ps.setString(6, Art_Piece.getDescription());
        ps.setString(7, Art_Piece.getStyle());
        ps.setString(8, Art_Piece.getImage_path());
        ps.setString(9, Art_Piece.getMusic_path());

        ps.executeUpdate();
        System.out.println("Art_Piece added!");
    }

    @Override
    public void update(Art_Piece Art_Piece) throws SQLException {
        String query = "UPDATE art SET  art_title=?,art_price=?,type=?,creation=?,description=?,style=?,artist_id=?,image_id=?,music_path=? WHERE art_ref=?";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, Art_Piece.getArt_title());
        ps.setFloat(2, Art_Piece.getArt_price());
        ps.setString(3, Art_Piece.getType());
        ps.setDate(4, Art_Piece.getCreation());
        ps.setString(5, Art_Piece.getDescription());
        ps.setString(6, Art_Piece.getStyle());
        ps.setInt(7, Art_Piece.getaid());
        ps.setString(8, Art_Piece.getImage_path());
        ps.setString(9, Art_Piece.getMusic_path());

        ps.setInt(10, Art_Piece.getArt_ref());


        ps.executeUpdate();
        System.out.println("Art_Piece updated!");
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM art WHERE art_ref = ?";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("Art_Piece deleted");
    }
    @Override

    public List<Art_Piece> diplayList() throws SQLException {
        String query = "SELECT * FROM `art`";
        stm = con.createStatement();
        ResultSet res = stm.executeQuery(query);
        List<Art_Piece> Art_Pieces = new ArrayList<>();
        while (res.next()) {
            int Art_ref = res.getInt(1);
            String Art_title = res.getString(2);
            float Art_price = res.getFloat(3);
            int aid = res.getInt(8);
            String Type = res.getString(4);
            String Description = res.getString(6);
            Date Creation = res.getDate(5);
            String Style = res.getString(7);
            String image_id = res.getString(9);
            String music_path = res.getString(10);


            Art_Piece a = new Art_Piece(Art_ref,Art_title, Art_price, aid, Type, Creation, Description ,Style,image_id,music_path);
            Art_Pieces.add(a);
        }
        return Art_Pieces;
    }

    public List<Art_Piece> diplayListByArtist(int id) throws SQLException {
        String query = "SELECT * FROM `art` WHERE artist_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);

        ResultSet res = ps.executeQuery();
        List<Art_Piece> Art_Pieces = new ArrayList<>();
        while (res.next()) {
            int Art_ref = res.getInt(1);
            String Art_title = res.getString(2);
            float Art_price = res.getFloat(3);
            int aid = res.getInt(8);
            String Type = res.getString(4);
            String Description = res.getString(6);
            Date Creation = res.getDate(5);
            String Style = res.getString(7);
            String image_id = res.getString(9);
            String music_path = res.getString(10);


            Art_Piece a = new Art_Piece(Art_ref,Art_title, Art_price, aid, Type, Creation, Description ,Style,image_id,music_path);
            Art_Pieces.add(a);
        }
        return Art_Pieces;
    }

    public List<Art_Piece> sortbyviewsdiplayList() throws SQLException {
        String query = "SELECT * FROM `art`ORDER BY art_views";
        stm = con.createStatement();
        ResultSet res = stm.executeQuery(query);
        List<Art_Piece> Art_Pieces = new ArrayList<>();
        while (res.next()) {
            int Art_ref = res.getInt(1);
            String Art_title = res.getString(2);
            float Art_price = res.getFloat(3);
            int aid = res.getInt(8);
            String Type = res.getString(4);
            String Description = res.getString(6);
            Date Creation = res.getDate(5);
            String Style = res.getString(7);
            String image_id = res.getString(9);
            String music_path = res.getString(10);


            Art_Piece a = new Art_Piece(Art_ref,Art_title, Art_price, aid, Type, Creation, Description ,Style,image_id,music_path);
            Art_Pieces.add(a);
        }
        return Art_Pieces;
    }
    public List<Art_Piece> sortbynamediplayList() throws SQLException {
        String query = "SELECT * FROM `art` ORDER BY art_title";
        stm = con.createStatement();
        ResultSet res = stm.executeQuery(query);
        List<Art_Piece> Art_Pieces = new ArrayList<>();
        while (res.next()) {
            int Art_ref = res.getInt(1);
            String Art_title = res.getString(2);
            float Art_price = res.getFloat(3);
            int aid = res.getInt(8);
            String Type = res.getString(4);
            String Description = res.getString(6);
            Date Creation = res.getDate(5);
            String Style = res.getString(7);
            String image_id = res.getString(9);
            String music_path = res.getString(10);


            Art_Piece a = new Art_Piece(Art_ref,Art_title, Art_price, aid, Type, Creation, Description ,Style,image_id,music_path);
            Art_Pieces.add(a);
        }
        return Art_Pieces;
    }

    public List<Art_Piece> searchByTitle(String input) throws SQLException {
        String query = "SELECT * FROM `art` where art_title = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, input);

        ResultSet res = ps.executeQuery(); // Corrected: Remove the query parameter here
        List<Art_Piece> Art_Pieces = new ArrayList<>();
        while (res.next()) {
            int Art_ref = res.getInt(1);
            String Art_title = res.getString(2);
            float Art_price = res.getFloat(3);
            int aid = res.getInt(8);
            String Type = res.getString(4);
            String Description = res.getString(6);
            Date Creation = res.getDate(5);
            String Style = res.getString(7);
            String image_id = res.getString(9);
            String music_path = res.getString(10);

            Art_Piece a = new Art_Piece(Art_ref,Art_title, Art_price, aid, Type, Creation, Description ,Style,image_id,music_path);
            Art_Pieces.add(a);
        }
        return Art_Pieces;
    }


    public int getArtPieceId(Art_Piece artPiece) throws SQLException {
        // Assuming you have a database connection
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String query = "SELECT art_ref FROM art WHERE art_ref = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, artPiece.getArt_ref());
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("art_ref");
            } else {
                throw new SQLException("Art piece not found in the database");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
}

