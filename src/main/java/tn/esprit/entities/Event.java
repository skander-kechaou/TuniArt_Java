package tn.esprit.entities;

import java.sql.Date;

public class Event {
    int event_id,duration,aid;
    String event_title,category;
    Date event_date;

    public Event(int event_id, int duration, int aid, String event_title, String category, Date event_date) {
        this.event_id = event_id;
        this.duration = duration;
        this.aid = aid;
        this.event_title = event_title;
        this.category = category;
        this.event_date = event_date;
    }

    public Event(int duration, int aid, String event_title, String category, Date event_date) {
        this.duration = duration;
        this.aid = aid;
        this.event_title = event_title;
        this.category = category;
        this.event_date = event_date;
    }

    @Override
    public String toString() {
        return "Event{" +
                "event_id=" + event_id +
                ", duration=" + duration +
                ", aid=" + aid +
                ", event_title='" + event_title + '\'' +
                ", category='" + category + '\'' +
                ", event_date=" + event_date +
                '}';
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getEvent_date() {
        return event_date;
    }

    public void setEvent_date(Date event_date) {
        this.event_date = event_date;
    }
}
