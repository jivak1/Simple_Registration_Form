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
    //sets properties for gmail SMTP host
    static {
        PROPERTIES.put("mail.smtp.host", "smtp.gmail.com");
        PROPERTIES.put("mail.smtp.port", "587");
        PROPERTIES.put("mail.smtp.auth", "true");
        PROPERTIES.put("mail.smtp.starttls.enable", "true");
    }

    public static void sendVerificationEmail(String from, String to, String subject, String messageBody) throws MessagingException {
        //authenticates sender gmail
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        };
        //creates session with properties and the authenticator
        Session session = Session.getInstance(PROPERTIES, authenticator);

        MimeMessage message = new MimeMessage(session) ;
        //adds email elements - from, to sent date etc...
        try {
            message.setFrom(new InternetAddress(from));

            InternetAddress[] address = {new InternetAddress(to)};

            message.setRecipients(Message.RecipientType.TO, address);

            message.setSubject(subject);

            message.setSentDate(new Date());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        //magic o_0
        Multipart multipart = new MimeMultipart();
        MimeBodyPart mimeBodyPart = new MimeBodyPart() ;

        ///sets encoding for message body
        mimeBodyPart.setText(messageBody, "us-ascii");

        //more magic o_0
        multipart.addBodyPart(mimeBodyPart);
        message.setContent(multipart);

        //sends email
        Transport.send(message);

    }
}
