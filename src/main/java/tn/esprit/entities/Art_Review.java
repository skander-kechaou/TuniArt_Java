package tn.esprit.entities;
import java.sql.Date;
public class Art_Review {
    int Review_id;
    int Art_Ref;
    int uid;
    Date Date_published;
    int Rating;
    String Comment;

    public Art_Review(int review_id, int art_Ref, int uid, Date date_published, int rating, String comment) {
        Review_id = review_id;
        Art_Ref = art_Ref;
        this.uid = uid;
        Date_published = date_published;
        Rating = rating;
        Comment = comment;
    }

    public Art_Review(int art_Ref, int uid, Date date_published, int rating, String comment) {
        Art_Ref = art_Ref;
        this.uid = uid;
        Date_published = date_published;
        Rating = rating;
        Comment = comment;
    }

    public int getReview_id() {
        return Review_id;
    }

    public void setReview_id(int review_id) {
        Review_id = review_id;
    }

    public int getArt_Ref() {
        return Art_Ref;
    }

    public void setArt_Ref(int art_Ref) {
        Art_Ref = art_Ref;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Date getDate_published() {
        return Date_published;
    }

    public void setDate_published(Date date_published) {
        Date_published = date_published;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    @Override
    public String toString() {
        return "Art_Review{" +
                "Review_id=" + Review_id +
                ", Art_Ref=" + Art_Ref +
                ", uid=" + uid +

                ", Date_published=" + Date_published +
                ", Rating=" + Rating +
                ", Comment='" + Comment + '\'' +
                '}';
    }
}