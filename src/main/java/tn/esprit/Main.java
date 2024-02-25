package tn.esprit;

import tn.esprit.entities.Admin;
import tn.esprit.entities.Artist;
import tn.esprit.entities.User;
import tn.esprit.services.UserService;

import java.sql.Date;
import java.sql.SQLException;

import static tn.esprit.services.VerificationCodeGenerator.generateVerificationCode;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");
        /*User u1 = new User("Skander", "Kechaou", "skander.kechaou.e@gmail.com",false, 21635020, Date.valueOf("2002-08-23"), "12345");
        User u2 = new User("Ahmed", "Kechaou", "skander.kechaou.e@gmail.com",false, 21635020, Date.valueOf("2002-08-23"), "12345");
        User u3 = new User("New", "Newly", "new.kechaou.e@gmail.com",false, 21635020, Date.valueOf("2002-08-23"), "12345");*/
        User u4 = new User("New", "Newly", "new.kechaou.e@gmail.com",false, true, 21635020, Date.valueOf("2002-08-23"), "12345", "User");
        Artist a3 = new Artist("artist", "artist", "croustina@gmail.com", false, true, 11345678, Date.valueOf("2003-01-01"), "password", "Artist");
        Admin ad2 = new Admin("adminette", "adminette", "adminette@gmail.com", true, 23415678,Date.valueOf("2001-01-01"), "mot de passe", "Admin");
        UserService us = new UserService();
        /*try {
            us.addd(a3);
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }*/

        try{
            System.out.println(us.diplayList());
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }

        /*try{
            us.delete(2);
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }*/

        /*a3.setUid(22);
        a3.setBiography("I'm an arist.");
        try{
            us.update(a3);
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }

        /*try{
            System.out.println(us.diplayList());
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }*/
        System.out.println(u4.hashCode());
        // Example usage:
        String userInfo = "user@example.com";
        String verificationCode = generateVerificationCode(u4);
        System.out.println("Generated Verification Code: " + verificationCode);

    }
}