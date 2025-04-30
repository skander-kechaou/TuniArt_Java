package tn.esprit.entities;
import java.sql.Date;
public class Art_Piece {
    int Art_ref;
    String Art_title;
    float Art_price;
    int aid;
    String Type;
    Date Creation;
    String Description;
    String Style;
    String image_path;
    String music_path;
    int art_views;

    public Art_Piece(String art_title, float art_price, int aid, String type, Date creation, String description, String style, String image_path, String music_path, int art_views) {
        Art_title = art_title;
        Art_price = art_price;
        this.aid = aid;
        Type = type;
        Creation = creation;
        Description = description;
        Style = style;
        this.image_path = image_path;
        this.music_path = music_path;
        this.art_views = art_views;
    }

    public Art_Piece(int art_ref, String art_title, float art_price, int aid, String type, Date creation, String description, String style, String image_path, String music_path, int art_views) {
        Art_ref = art_ref;
        Art_title = art_title;
        Art_price = art_price;
        this.aid = aid;
        Type = type;
        Creation = creation;
        Description = description;
        Style = style;
        this.image_path = image_path;
        this.music_path = music_path;
        this.art_views = art_views;
    }

    public Art_Piece(String art_title, float art_price, int aid, String type, Date creation, String description, String style, String image_path, String music_path) {
        Art_title = art_title;
        Art_price = art_price;
        this.aid = aid;
        Type = type;
        Creation = creation;
        Description = description;
        Style = style;
        this.image_path = image_path;
        this.music_path = music_path;
    }

    public String getMusic_path() {
        return music_path;
    }

    public void setMusic_path(String music_path) {
        this.music_path = music_path;
    }

    public Art_Piece(int art_ref, String art_title, float art_price, int aid, String type, Date creation, String description, String style, String image_path, String music_path) {
        Art_ref = art_ref;
        Art_title = art_title;
        Art_price = art_price;
        this.aid = aid;
        Type = type;
        Creation = creation;
        Description = description;
        Style = style;
        this.image_path = image_path;
        this.music_path = music_path;
    }

    public String getImage_path() {
        return image_path;
    }

    public Art_Piece( String art_title, float art_price, int aid, String type, Date creation, String description, String style, String image_path) {

        Art_title = art_title;
        Art_price = art_price;
        this.aid = aid;
        Type = type;
        Creation = creation;
        Description = description;
        Style = style;
        this.image_path = image_path;
    }

    public Art_Piece(int art_ref, String art_title, float art_price, int aid, String type, Date creation, String description, String style, String image_path) {
        Art_ref = art_ref;
        Art_title = art_title;
        Art_price = art_price;
        this.aid = aid;
        Type = type;
        Creation = creation;
        Description = description;
        Style = style;
        this.image_path = image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public Art_Piece(String art_title, float art_price, String description) {
        Art_title = art_title;
        Art_price = art_price;
        Description = description;
    }

    public Art_Piece(int art_ref, String art_title, float art_price, int aid, String type, Date creation, String description, String style) {
        Art_ref = art_ref;
        Art_title = art_title;
        Art_price = art_price;
        this.aid = aid;
        Type = type;
        Creation = creation;
        Description = description;
        Style = style;
    }

    public Art_Piece(String art_title, float art_price, int aid, String type, Date creation, String description, String style) {
        Art_title = art_title;
        Art_price = art_price;
        this.aid = aid;
        Type = type;
        Creation = creation;
        Description = description;
        Style = style;
    }

    public Art_Piece(String art_title, float art_price, String type, Date creation, String description, String style) {
        Art_title = art_title;
        Art_price = art_price;
        Type = type;
        Creation = creation;
        Description = description;
        Style = style;
    }
    public void incrementArtViews() {
        art_views++;
    }

    public int getArt_views() {
        return art_views;
    }

    public void setArt_views(int art_views) {
        this.art_views = art_views;
    }

    public int getArt_ref() {
        return Art_ref;
    }

    public void setArt_ref(int art_ref) {
        Art_ref = art_ref;
    }

    public String getArt_title() {
        return Art_title;
    }

    public void setArt_title(String art_title) {
        Art_title = art_title;
    }

    public float getArt_price() {
        return Art_price;
    }

    public void setArt_price(float art_price) {
        Art_price = art_price;
    }

    public int getaid() {
        return aid;
    }

    public int setArtist(int aid) {
        return aid;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Date getCreation() {
        return Creation;
    }

    public void setCreation(Date creation) {
        Creation = creation;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getStyle() {
        return Style;
    }

    public void setStyle(String style) {
        Style = style;
    }

    @Override
    public String toString() {
        return "Art_Piece{" +
                "Art_ref=" + Art_ref +
                ", Art_title='" + Art_title + '\'' +
                ", Art_price=" + Art_price +
                ", Artist=" + aid +
                ", Type='" + Type + '\'' +
                ", Creation=" + Creation +
                ", Description='" + Description + '\'' +
                ", Style='" + Style + '\'' +
                '}';
    }
}
