package sender;

import org.apache.log4j.Logger;
import utils.PropertyLoader;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 * This class is designed to send a message to the user.
 * The name of the recipient is specified using a method
 * setRetsipient name for sending as well as the host and
 * port of the server to send a message is set by the properties
 * @author Ivan Gladush
 * @since 21.04.16.
 */
public class EmailSender {
    private static final Logger logger = Logger.getLogger(EmailSender.class);
    private static final PropertyLoader PROPERTY_LOADER = PropertyLoader.getPropertyLoader("email.sender.properties");

    private static final String ENCODING = PROPERTY_LOADER.property("encoding");
    private static final String HOST = PROPERTY_LOADER.property("host");
    private static final String SENDER_NAME = PROPERTY_LOADER.property("sender.name");

    private static final Authenticator auth = new MyAuthenticator(PROPERTY_LOADER.property("authenticator.name"),
            PROPERTY_LOADER.property("authenticator.password"));
    private static final Session session;

    private static final String EXCEPTION_SEND_MESSAGE = "I could not send the letter because things %s";

    private StringBuilder body = new StringBuilder();
    private StringBuilder subject = new StringBuilder();
    private String recipient;
    private static final Properties props;

    static {
        props = System.getProperties();
        props.put("mail.smtp.port", 25);
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.auth", "true");
        props.put("mail.mime.charset", ENCODING);
        session = Session.getDefaultInstance(props, auth);
    }

    public EmailSender addBody(String body) {
        this.body.append(body);
        return this;
    }

    public EmailSender addSubject(String subject) {
        this.subject.append(subject);
        return this;
    }

    public EmailSender setRecipient(String recipient) {
        this.recipient = recipient;
        return this;
    }

    public boolean sendMessage() {
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(SENDER_NAME));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            msg.setSubject(subject.toString());
            msg.setText(body.toString());
            Transport.send(msg);
        } catch (Exception e) {
            logger.error(String.format(EXCEPTION_SEND_MESSAGE, e.getMessage()));
            return false;
        }
        return true;
    }

    /**
     * Class is designed to validate the authentication server to send emails
     */
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
}
