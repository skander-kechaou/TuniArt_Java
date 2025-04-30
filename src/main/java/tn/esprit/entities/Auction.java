package tn.esprit.entities;

import java.sql.Date;

public class Auction {
    int auction_ref,art_ref;
    Date start_date,end_date;
    String auction_name;
    float threshold;
    int uid;
    String status;
    float highest_bid;
    int currentWinner_id;
    int interactions;

    public int getInteractions() {
        return interactions;
    }

    public void setInteractions(int interactions) {
        this.interactions = interactions;
    }

    public Auction(int auction_ref, String auction_name, Date start_date, Date end_date, float threshold, int art_ref, int uid) {
        this.auction_ref = auction_ref;
        this.auction_name = auction_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.threshold = threshold;
        this.art_ref=art_ref;
        this.uid = uid;

    }



    //wihout ref (autoincrement)
    public Auction( String auction_name,Date start_date, Date end_date, float threshold,int art_ref,int uid) {
        this.auction_name = auction_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.threshold = threshold;
        this.art_ref= art_ref;
        this.uid = uid;
    }
    public Auction( String auction_name,Date start_date, Date end_date, float threshold) {
        this.auction_name = auction_name;
        this.start_date = start_date;
        this.end_date = end_date;

        this.threshold = threshold;

    }

    public Auction(String name, Date date, Date date1, float threshold, int i) {
        this.auction_name = auction_name;
        this.start_date = start_date;
        this.end_date = end_date;

        this.threshold = threshold;
        this.uid=i;
    }

    public Auction(int art_ref, Date start_date, Date end_date, String auction_name, float threshold, int uid, String status, float highest_bid, int currentWinner_id) {
        this.art_ref = art_ref;
        this.start_date = start_date;
        this.end_date = end_date;
        this.auction_name = auction_name;
        this.threshold = threshold;
        this.uid = uid;
        this.status = status;
        this.highest_bid = highest_bid;
        this.currentWinner_id = currentWinner_id;
    }

    public Auction(int auction_ref, int art_ref, Date start_date, Date end_date, String auction_name, float threshold, int uid, String status, float highest_bid, int currentWinner_id) {
        this.auction_ref = auction_ref;
        this.art_ref = art_ref;
        this.start_date = start_date;
        this.end_date = end_date;
        this.auction_name = auction_name;
        this.threshold = threshold;
        this.uid = uid;
        this.status = status;
        this.highest_bid = highest_bid;
        this.currentWinner_id = currentWinner_id;
    }

    @Override
    public String toString() {
        return "Auction{" +
                "auction_ref=" + auction_ref +
                ", art_ref=" + art_ref +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", auction_name='" + auction_name + '\'' +
                ", threshold=" + threshold +
                ", uid=" + uid +

                '}';
    }

    public int getAuction_ref() {
        return auction_ref;
    }

    public void setAuction_ref(int auction_ref) {
        this.auction_ref = auction_ref;
    }

    public int getArt_ref() {
        return art_ref;
    }

    public void setArt_ref(int art_ref) {
        this.art_ref = art_ref;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getAuction_name() {
        return auction_name;
    }

    public void setAuction_name(String auction_name) {
        this.auction_name = auction_name;
    }

    public float getThreshold() {
        return threshold;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getHighest_bid() {
        return highest_bid;
    }

    public void setHighest_bid(float highest_bid) {
        this.highest_bid = highest_bid;
    }

    public int getCurrentWinner_id() {
        return currentWinner_id;
    }

    public void setCurrentWinner_id(int currentWinner_id) {
        this.currentWinner_id = currentWinner_id;
    }

    public int getUid(){ return uid;


    }
    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }
}
