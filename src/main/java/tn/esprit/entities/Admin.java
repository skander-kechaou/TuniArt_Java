package tn.esprit.entities;

import java.sql.Date;

public class Admin extends User {
    public Admin(String fname, String lname, String email, boolean gender, int phone_nb, Date birth_date, String password, String role) {
        super(fname, lname, email, gender, phone_nb, birth_date, password, role);
    }

    public Admin(int uid, String fname, String lname, String email, boolean gender, int phone_nb, Date birth_date, String password, String role) {
        super(uid, fname, lname, email, gender, phone_nb, birth_date, password, role);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "uid=" + getUid() +
                ", fname='" + getFname() + '\'' +
                ", lname='" + getLname() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", gender=" + getGender() +
                ", phone_nb=" + getPhone_nb() +
                ", birth_date=" + getBirth_date() +
                ", password='" + getPassword() + '\'' +
                ", role='" + getRole() +
                '}' + "\n";
    }
}
