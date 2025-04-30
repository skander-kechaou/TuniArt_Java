package tn.esprit.services;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import tn.esprit.utils.MyDatabase;
import tn.esprit.entities.Auction;
import tn.esprit.entities.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuctionService implements IService<Auction> {
    Connection con;
    Statement stm;

    public AuctionService() {
        con = MyDatabase.getInstance().getConn();//it's the singleton(concept)to establish only one instance of connection
    }

    @Override
    public void add(Auction auction) throws SQLException {
        String query = "INSERT INTO `auction`(`auction_ref`, `auction_name`, `start_date`, `end_date`, `threshold`, `art_ref`, `uid`) VALUES ('" + auction.getAuction_ref() + "','" + auction.getAuction_name() + "','" + auction.getStart_date() + "','" + auction.getEnd_date() + "','" + auction.getThreshold() + "','" + auction.getArt_ref() + "','" + auction.getUid() + "')";
        stm = con.createStatement();
        stm.executeUpdate(query);
        System.out.println("Auction added!");
    }

    @Override
    public void addd(Auction auction) throws SQLException {
        String query = "INSERT INTO `auction`(`auction_name`, `start_date`, `end_date`, `threshold`, `art_ref`,`uid`) VALUES (?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, auction.getAuction_name());
        ps.setDate(2, auction.getStart_date());
        ps.setDate(3, auction.getEnd_date());
        ps.setFloat(4, auction.getThreshold());
        ps.setInt(5, auction.getArt_ref());
        ps.setInt(6, auction.getUid());
        ps.executeUpdate();
        System.out.println("Auction added!");
    }

    @Override
    public void update(Auction auction) throws SQLException {
        String query = "UPDATE auction SET  auction_name=?, start_date=?, end_date=?, threshold=?, art_ref=?, uid=? WHERE auction_ref=?";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, auction.getAuction_name());
        ps.setDate(2, auction.getStart_date());
        ps.setDate(3, auction.getEnd_date());
        ps.setFloat(4, auction.getThreshold());
        ps.setInt(5, auction.getArt_ref());
        ps.setInt(6, auction.getUid());
        ps.setInt(7, auction.getAuction_ref());
        ps.executeUpdate();
        System.out.println("Auction updated!");

    }

    public void updateThreshold(float newThreshold, int auctionId) throws SQLException {
        String updateQuery = "UPDATE auction SET threshold = ? ,interactions = interactions + 1 WHERE auction_ref = ?";
        try (Connection connection = MyDatabase.getInstance().getConn();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setFloat(1, newThreshold);
            statement.setInt(2, auctionId);
            statement.executeUpdate();
        }
    }





    @Override
    public void delete(int ref) throws SQLException {
        String query = "DELETE FROM auction WHERE auction_ref = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, ref);
        ps.executeUpdate();
        System.out.println("Auction deleted");
    }

    @Override
    public List<Auction> diplayList() throws SQLException {
        String query = "SELECT * FROM `auction`";
        stm = con.createStatement();
        ResultSet res = stm.executeQuery(query);
        List<Auction> auctions = new ArrayList<>();
        while (res.next()) {
            int auction_ref = res.getInt(1);
            String auction_name = res.getString(2);
            Date start_date = res.getDate(3);
            Date end_date = res.getDate(4);
            float threshold = res.getFloat(5);
            int art_ref = res.getInt(6);
            int uid = res.getInt(7);

            Auction a = new Auction(auction_ref ,auction_name, start_date, end_date, threshold, art_ref,uid);
            auctions.add(a);
        }
        return auctions;
    }

    public List<Auction> diplayListByUser(int uid) throws SQLException {
        String query = "SELECT * FROM `auction` where uid = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, uid);
        ResultSet res = ps.executeQuery();

        List<Auction> auctions = new ArrayList<>();
        while (res.next()) {
            int auction_ref = res.getInt(1);
            String auction_name = res.getString(2);
            Date start_date = res.getDate(3);
            Date end_date = res.getDate(4);
            float threshold = res.getFloat(5);
            int art_ref = res.getInt(6);

            Auction a = new Auction(auction_ref ,auction_name, start_date, end_date, threshold, art_ref,uid);
            auctions.add(a);
        }
        return auctions;
    }
}

