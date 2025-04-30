package tn.esprit.services;


import tn.esprit.entities.gallery;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class galleryService implements IService<gallery>  {
    Connection con;
    Statement stm;
    public galleryService(){
        con = MyDatabase.getInstance().getConn();
    }
    @Override
    public void add(gallery gallery) throws SQLException {
        String query = "INSERT INTO `gallery`( `gallery_name`, `gallery_description`, `gallery_location`, `gallery_tel`, `operating_hours`) VALUES ('"+gallery.getGallery_name()+"','"+gallery.getGallery_description()+"','"+gallery.getGallery_location()+"','"+gallery.getGallery_tel()+"','"+gallery.getOperating_hours()+"')";
        stm = con.createStatement();
        stm.executeUpdate(query);
        System.out.println("gallery added!");
    }
    @Override
    public void addd(gallery gallery) throws SQLException {
        String query = "INSERT INTO `gallery`( `gallery_name`, `gallery_description`, `gallery_location`, `gallery_tel`, `operating_hours`) VALUES (?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, gallery.getGallery_name());
        ps.setString(2, gallery.getGallery_description());
        ps.setString(3, gallery.getGallery_location());
        ps.setInt(4, gallery.getGallery_tel());
        ps.setString(5, gallery.getOperating_hours());

        ps.executeUpdate();
        System.out.println("gallery added!");
    }
    @Override
    public void update(gallery gallery ) throws SQLException {
        String query = "UPDATE `gallery` SET `gallery_name`=?,`gallery_description`=?,`gallery_location`=?,`gallery_tel`=?,`operating_hours`=? WHERE `gallery_id`=?";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, gallery.getGallery_name());
        ps.setString(2, gallery.getGallery_description());
        ps.setString(3, gallery.getGallery_location());
        ps.setInt(4, gallery.getGallery_tel());
        ps.setString(5, gallery.getOperating_hours());


        ps.executeUpdate();
        System.out.println("gallery updated!");
    }
    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM gallery WHERE gallery_id = ?";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("gallery deleted");
    }
    @Override
    public List<gallery> diplayList() throws SQLException {
        String query = "SELECT * FROM `gallery`";
        stm = con.createStatement();
        ResultSet res = stm.executeQuery(query);
        List<gallery> gallerys = new ArrayList<>();
        while (res.next()) {
            int gallery_id = res.getInt(1);
            String gallery_name = res.getString(2);
            String gallery_description = res.getString(3);
            String gallery_location = res.getString(4);
            int gallery_tel  = res.getInt(5);
            String operating_hours = res.getString(6);



            gallery a = new gallery(gallery_id,gallery_name, gallery_description, gallery_location, gallery_tel, operating_hours);
            gallerys.add(a);
        }
        return gallerys;
    }

}

