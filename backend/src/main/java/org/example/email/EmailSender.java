package org.example.email;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.util.Date;
import java.util.List;
import java.util.Properties;

public class EmailSender {
    private static final Properties PROPERTIES = new Properties();
    private static final String USERNAME = "verificator48@gmail.com";
    private static final String PASSWORD = "uuusjogcgupohgmq";

    static {
        PROPERTIES.put("mail.smtp.host", "smtp.gmail.com");
        PROPERTIES.put("mail.smtp.port", "587");
        PROPERTIES.put("mail.smtp.auth", "true");
        PROPERTIES.put("mail.smtp.starttls.enable", "true");
    }

    public static void sendVerificationEmail(String from, String to, String subject, String messageBody) throws MessagingException {
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        };

        Session session = Session.getInstance(PROPERTIES, authenticator);

        MimeMessage message = new MimeMessage(session) ;

        try {
            message.setFrom(new InternetAddress(from));

            InternetAddress[] address = {new InternetAddress(to)};

            message.setRecipients(Message.RecipientType.TO, address);

            message.setSubject(subject);

            message.setSentDate(new Date());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


        Multipart multipart = new MimeMultipart();
        MimeBodyPart mimeBodyPart = new MimeBodyPart() ;


        mimeBodyPart.setText(messageBody, "us-ascii");


        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);

    }
}
