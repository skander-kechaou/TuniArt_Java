package tn.esprit.services;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailService {
    public static void sendVerificationEmail(String toEmail, String verificationCode) {
        final String username = "skander.kechaou.e@gmail.com"; // Your email
        final String password = "rlaklrxvzzfhuhwe"; // Your password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP host for Gmail
        props.put("mail.smtp.port", "587"); // SMTP port for Gmail

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            System.out.println("enters the try loop");
            // Attempt to authenticate
            session.getTransport("smtp").connect(username, password);
            session.setDebug(true);
            System.out.println("connected.");

            // If authentication is successful, proceed to send the email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("[Tuni'Art] Password Reset Verification Code");
            message.setText("Dear User,\n\nYour verification code is: " + verificationCode + "\n\nRegards,\nTuni'Art");
            System.out.println(message);
            Transport.send(message);

            System.out.println("Verification email sent successfully!");
        } catch (AuthenticationFailedException e) {
            System.err.println("Authentication failed. Please check your email credentials.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
