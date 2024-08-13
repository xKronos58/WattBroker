package com.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

//wattbroker@outlook.com
//Afc_Admin1
public class EmailNotification {
    public static void outlook(){
        final String username = "wattbroker@outlook.com";
        final String password = "";

        // SMTP server properties for Hotmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp-mail.outlook.com");
        props.put("mail.smtp.port", "587");

        // Create session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("finleycrowther@outlook.com"));
//            message.setRecipients(Message.RecipientType.CC,
//                    InternetAddress.parse("cc@outlook.com")); //Carbon Copy
//            message.setRecipients(Message.RecipientType.BCC,
//                    InternetAddress.parse("bcc@outlook.com")); //Blind Carbon Copy
            message.setSubject("Testing OutlookSender");
            message.setText("Dear User,\n\nThis is a test email sent from OutlookSender.");

            // Send message
            Transport.send(message);

            System.out.println("Email sent successfully.");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        outlook();
    }
}
