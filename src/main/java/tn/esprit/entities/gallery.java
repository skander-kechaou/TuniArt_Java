package tn.esprit.entities;

public class gallery {
    int gallery_id;
    String gallery_name;
    String gallery_location;
    String gallery_description;
    int gallery_tel;
    String operating_hours;

    public gallery(int gallery_id, String gallery_name, String gallery_location, String gallery_description, int gallery_tel, String operating_hours) {
        this.gallery_id = gallery_id;
        this.gallery_name = gallery_name;
        this.gallery_location = gallery_location;
        this.gallery_description = gallery_description;
        this.gallery_tel = gallery_tel;
        this.operating_hours = operating_hours;
    }

    public gallery(String gallery_name, String gallery_location, String gallery_description, int gallery_tel, String operating_hours) {
        this.gallery_name = gallery_name;
        this.gallery_location = gallery_location;
        this.gallery_description = gallery_description;
        this.gallery_tel = gallery_tel;
        this.operating_hours = operating_hours;
    }

    public int getGallery_id() {
        return gallery_id;
    }

    public void setGallery_id(int gallery_id) {
        this.gallery_id = gallery_id;
    }

    public String getGallery_name() {
        return gallery_name;
    }

    public void setGallery_name(String gallery_name) {
        this.gallery_name = gallery_name;
    }

    public String getGallery_location() {
        return gallery_location;
    }

    public void setGallery_location(String gallery_location) {
        this.gallery_location = gallery_location;
    }

    public String getGallery_description() {
        return gallery_description;
    }

    public void setGallery_description(String gallery_description) {
        this.gallery_description = gallery_description;
    }

    public int getGallery_tel() {
        return gallery_tel;
    }

    public void setGallery_tel(int gallery_tel) {
        this.gallery_tel = gallery_tel;
    }

    public String getOperating_hours() {
        return operating_hours;
    }

    public void setOperating_hours(String operating_hours) {
        this.operating_hours = operating_hours;
    }

    @Override
    public String toString() {
        return "gallery{" +
                "gallery_id=" + gallery_id +
                ", gallery_name='" + gallery_name + '\'' +
                ", gallery_location='" + gallery_location + '\'' +
                ", gallery_description='" + gallery_description + '\'' +
                ", gallery_tel=" + gallery_tel +
                ", operating_hours='" + operating_hours + '\'' +
                '}';
    }
}
