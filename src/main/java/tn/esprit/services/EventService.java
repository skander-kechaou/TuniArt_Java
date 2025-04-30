package tn.esprit.services;

import tn.esprit.utils.MyDatabase;
import tn.esprit.entities.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventService implements IService<Event> {
    Connection con;
    Statement stm;

    public EventService() {
        con = MyDatabase.getInstance().getConn();
    }

    @Override
    public void add(Event event) throws SQLException {
        String query = "INSERT INTO `event`(`event_title`, `category`, `event_date`, `duration`, `uid`) VALUES ('" + event.getEvent_title() + "','" + event.getCategory() + "','" + event.getEvent_date() + "','" + event.getDuration() + "','" + event.getAid() + "')";
        stm = con.createStatement();
        stm.executeUpdate(query);
        System.out.println("Event added!");

    }

    @Override
    public void addd(Event event) throws SQLException {
        String query = "INSERT INTO `event`( `event_title`, `category`, `event_date`, `duration`, `uid`) VALUES (?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, event.getEvent_title());
        ps.setString(2, event.getCategory());
        ps.setDate(3, event.getEvent_date());
        ps.setInt(4, event.getDuration());
        ps.setInt(5, event.getAid());
        ps.executeUpdate();
        System.out.println("Event added!");

    }

    @Override
    public void update(Event event) throws SQLException {
        String query = "UPDATE event SET event_title=?, category=?, event_date=?, duration=?, aid=? WHERE event_id=?";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, event.getEvent_title());
        ps.setString(2, event.getCategory());
        ps.setDate(3, event.getEvent_date());
        ps.setInt(4, event.getDuration());
        ps.setInt(5, event.getAid());
        ps.setInt(6, event.getEvent_id());
        ps.executeUpdate();
        System.out.println("Event updated!");

    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM event WHERE event_id = ?";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("Event deleted");
    }

    @Override
    public List<Event> diplayList() throws SQLException {
        String query = "SELECT * FROM `event`";
        stm = con.createStatement();
        ResultSet res = stm.executeQuery(query);
        List<Event> events = new ArrayList<>();
        while (res.next()) {
            int event_id = res.getInt(1);
            String event_title = res.getString(2);
            String category = res.getString(3);
            Date event_date = res.getDate(4);
            int duration = res.getInt(5);
            int aid = res.getInt(6);
            Event e = new Event(event_id, duration, aid, event_title, category, event_date);
            events.add(e);
        }
        return events;
    }

    public List<Event> diplayListByUser(int uid) throws SQLException {
        String query = "SELECT * FROM `event` where uid = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, uid);
        ResultSet res = ps.executeQuery();
        List<Event> events = new ArrayList<>();
        while (res.next()) {
            int event_id = res.getInt(1);
            String event_title = res.getString(2);
            String category = res.getString(3);
            Date event_date = res.getDate(4);
            int duration = res.getInt(5);
            int aid = res.getInt(6);
            Event e = new Event(event_id, duration, uid, event_title, category, event_date);
            events.add(e);
        }
        return events;
    }
}
