package tn.esprit.entities;

import java.sql.Date;

public class Artist extends User{

    private String biography, portfolio;

    public Artist(String fname, String lname, String email, boolean gender, boolean status, int phone_nb, Date birth_date, String password, String role) {
        super(fname, lname, email, gender, status, phone_nb, birth_date, password, role);
    }

    public Artist(String fname, String lname, String email, boolean gender, boolean status, int phone_nb, Date birth_date, String profile_pic, String password, String verification_code, String role) {
        super(fname, lname, email, gender, status, phone_nb, birth_date, profile_pic, password, verification_code, role);
    }

    public Artist(int uid, String fname, String lname, String email, boolean gender, boolean status, int phone_nb, Date birth_date, String profile_pic, String password, String verification_code, String role) {
        super(uid, fname, lname, email, gender, status, phone_nb, birth_date, profile_pic, password, verification_code, role);
    }

    public Artist(int uid, String fname, String lname, String email, boolean gender, boolean status, int phone_nb, Date birth_date, String profile_pic, String password, String verification_code, String role, String biography, String portfolio) {
        super(uid, fname, lname, email, gender, status, phone_nb, birth_date, profile_pic, password, verification_code, role);
        this.biography = biography;
        this.portfolio = portfolio;
    }

    public Artist() {
        super();
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    @Override
    public String toString() {
        return super.toString().replace("\n", "") +
                "biography='" + biography + '\'' +
                ", portfolio='" + portfolio + '\'' +
                '}' + "\n";
    }
}
