/********************************************************************
 * class represents a Email message.
 * has method of sending email via Gmail SMTP with TLS Authentication.
 * and a boolean method that checks if email address is legal. 
 ********************************************************************/
package my_types;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Email {

    /*
    * sends email with file "fileNameAndPath" attached, to "toAddress".
    *  SMTP gmail server with TLS Authentication.
    */
    public void sendEmail(String fileNameAndPath, String toAddress)
            throws AddressException, MessagingException {

        //SMTP server
        String host = "smtp.gmail.com";
        //TLC port
        String port = "587";
        String userName = "ymishkanAsher@gmail.com";
        String password = "ymaymayma3";

        // outgoing message information
        String subject = "Document from Institut X";
        String message = "Hi, We attached your document to this email";

        // sets SMTP server properties        
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        //sets session
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });
        //sets message to be sent
        Message emailMessage = new MimeMessage(session);
        emailMessage.setFrom(new InternetAddress(userName));
        emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
        emailMessage.setSubject(subject);

        // sets MIME Internet standard 
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(message, "text/plain");
        try {
            mimeBodyPart.attachFile(new File(fileNameAndPath));
        } catch (IOException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        emailMessage.setContent(multipart);
        Transport.send(emailMessage);
    }

    /*
     * checks if email address is legal.
     * legal email address starts with any amount of numbers letters and(._%+-)characters
     * followed by "@",followed by any amount of numbers letters and(.-)characters,
     * followed by ".",followed by 2,3,4,5 or 6 letters 
     * 
     */
    public static boolean isValidEmail(String emailAddress) {
        Pattern VALID_EMAIL_ADDRESS_REGEX
                = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailAddress);
        return matcher.matches();
    }
}
