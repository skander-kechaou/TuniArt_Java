package tn.esprit.services;

import tn.esprit.entities.Artist;
import tn.esprit.entities.User;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistService implements IService<Artist> {
    
    Connection con;
    Statement stm;

    public ArtistService() {
        con = MyDatabase.getInstance().getConn();
    }

    @Override
    public void add(Artist artist) throws SQLException {
        String query = "INSERT INTO `artist`( `fname`, `lname`, `email`, `gender`, `phone_nb`, `birth_date`, `password`, `biography`, `portfolio`) VALUES ('"+artist.getFname()+"','"+artist.getLname()+"','"+artist.getEmail()+"','"+artist.getGender()+"','"+artist.getPhone_nb()+"','"+artist.getBirth_date()+"','"+artist.getPassword()+"', '"+artist.getBiography()+"', '"+artist.getPortfolio()+"')";
        stm = con.createStatement();
        stm.executeUpdate(query);
        System.out.println("Artist added!");
    }

    @Override
    public void addd(Artist artist) throws SQLException {
        String query = "INSERT INTO `artist`( `fname`, `lname`, `email`, `gender`, `phone_nb`, `birth_date`, `password`, `biography`, `portfolio`) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        
        ps.setString(1, artist.getFname());
        ps.setString(2, artist.getLname());
        ps.setString(3, artist.getEmail());
        ps.setBoolean(4, artist.getGender());
        ps.setInt(5, artist.getPhone_nb());
        ps.setDate(6, artist.getBirth_date());
        ps.setString(7, artist.getPassword());
        ps.setString(8, artist.getBiography());
        ps.setString(9, artist.getPortfolio());

        ps.executeUpdate();
        System.out.println("Artist added!");
    }

    @Override
    public void update(Artist artist) throws SQLException {
        String query = "UPDATE artist SET fname=?, lname=?, email=?, gender=?, phone_nb=?, birth_date=?, password=?, biography=?, portfolio=?, verification_code=? WHERE aid=?";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, artist.getFname());
        ps.setString(2, artist.getLname());
        ps.setString(3, artist.getEmail());
        ps.setBoolean(4, artist.getGender());
        ps.setInt(5, artist.getPhone_nb());
        ps.setDate(6, artist.getBirth_date());
        ps.setString(7, artist.getPassword());
        ps.setString(8, artist.getBiography());
        ps.setString(9, artist.getPortfolio());
        ps.setString(10, artist.getVerification_code());
        ps.setInt(11, artist.getAid());

        ps.executeUpdate();
        System.out.println("artist updated!");
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM artist WHERE aid = ?";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Artist deleted");
        } else {
            System.out.println("Artist with ID " + id + " not found");
        }
    }

    @Override
    public List<Artist> diplayList() throws SQLException {
        String query = "SELECT * FROM `artist`";
        stm = con.createStatement();
        ResultSet res = stm.executeQuery(query);
        List<Artist> artists = new ArrayList<>();
        while (res.next()) {
            String fname = res.getString(2);
            String lname = res.getString(3);
            String email = res.getString(4);
            Boolean gender = res.getBoolean(5);
            int phone_nb = res.getInt(6);
            Date birth_date = res.getDate(8);
            String password = res.getString(9);
            String biography = res.getString(10);
            String portfolio = res.getString(11);

            Artist a = new Artist(fname, lname, email, gender, phone_nb, birth_date, password, biography, portfolio);
            artists.add(a);
        }
        return artists;
    }
}
