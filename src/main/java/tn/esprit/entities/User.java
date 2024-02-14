package tn.esprit.entities;

import java.sql.Date;

public class User {

    private int uid;
    private String fname, lname, email;
    private boolean gender;
    private int phone_nb;
    private Date birth_date;
    private String profile_pic, password, verification_code;

    public User(String fname, String lname, String email, boolean gender, int phone_nb, Date birth_date, String password) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.gender = gender;
        this.phone_nb = phone_nb;
        this.birth_date = birth_date;
        this.password = password;
    }

    public User(int uid, String fname, String lname, String email, boolean gender, int phone_nb, String profile_pic, Date birth_date, String password, String verification_code) {
        this.uid = uid;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.gender = gender;
        this.phone_nb = phone_nb;
        this.profile_pic = profile_pic;
        this.birth_date = birth_date;
        this.password = password;
        this.verification_code = verification_code;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", phone_nb=" + phone_nb +
                ", birth_date=" + birth_date +
                ", profile_pic='" + profile_pic + '\'' +
                ", password='" + password + '\'' +
                ", verification_code='" + verification_code + '\'' +
                '}';
    }
}
