package servlets.authorization;

import org.apache.log4j.Logger;
import utils.PropertyLoader;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
/**
 * @author Ivan Gladush
 * @since 21.04.16.
 */
public class EmailSender {
    private static final Logger logger= Logger.getLogger(EmailSender.class);
    private static final String ENCODING = "UTF-8";
    private static final String EXCEPTION_SEND_MESSAGE = "I could not send the letter because things %s";
    private static final String SENDER_NAME = "MY_SUPER_SITE.VANYA.COM";


    private StringBuilder body =new StringBuilder();
    private StringBuilder subject=new StringBuilder();
    private String recipient;
    private static final Properties props;
    private static final Authenticator auth = new MyAuthenticator("", "");
    static {
        props = System.getProperties();
        props.put("mail.smtp.port", 25);
        props.put("mail.smtp.host", "127.0.0.1");
        props.put("mail.smtp.auth", "true");
        props.put("mail.mime.charset", ENCODING);
    }

   public EmailSender addBody(String body) {
       this.body.append(body);
       return this;
   }
    public EmailSender addSubject(String subject){
        this.subject.append(subject);
        return this;
    }
    public EmailSender setRecipient(String recipient){
        this.recipient=recipient;
        return this;
    }

    public boolean sendMessage() {
        try {
            Session session = Session.getDefaultInstance(props, auth);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(SENDER_NAME));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            msg.setSubject(subject.toString());
            msg.setText(body.toString());
            Transport.send(msg);
        } catch (Exception e) {
            logger.error(String.format(EXCEPTION_SEND_MESSAGE,e.getMessage()));
            return false;
        }
        return true;
    }

    private static class MyAuthenticator extends Authenticator {
        private String user;
        private String password;

        MyAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            String user = this.user;
            String password = this.password;
            return new PasswordAuthentication(user, password);
        }
    }
    public static void main(String args[]) throws MessagingException {
        new EmailSender().sendMessage();
    }



}
