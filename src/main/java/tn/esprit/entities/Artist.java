package tn.esprit.entities;

import java.sql.Date;

public class Artist {

    private int aid;
    private String fname, lname, email;
    private boolean gender;
    private int phone_nb;
    private Date birth_date;
    private String password, biography, portfolio, verification_code;

    public Artist(String fname, String lname, String email, boolean gender, int phone_nb, Date birth_date, String password, String biography, String portfolio) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.gender = gender;
        this.phone_nb = phone_nb;
        this.birth_date = birth_date;
        this.password = password;
        this.biography = biography;
        this.portfolio = portfolio;
    }

    public Artist(int aid, String fname, String lname, String email, boolean gender, int phone_nb, Date birth_date, String password, String biography, String portfolio, String verification_code) {
        this.aid = aid;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.gender = gender;
        this.phone_nb = phone_nb;
        this.birth_date = birth_date;
        this.password = password;
        this.biography = biography;
        this.portfolio = portfolio;
        this.verification_code = verification_code;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public int getPhone_nb() {
        return phone_nb;
    }

    public void setPhone_nb(int phone_nb) {
        this.phone_nb = phone_nb;
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }

    @Override
    public String toString() {
        return "artist{" +
                "aid=" + aid +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", phone_nb=" + phone_nb +
                ", birth_date=" + birth_date +
                ", password='" + password + '\'' +
                ", biography='" + biography + '\'' +
                ", portfolio='" + portfolio + '\'' +
                ", verification_code='" + verification_code + '\'' +
                '}';
    }
}
