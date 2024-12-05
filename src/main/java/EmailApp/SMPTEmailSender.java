package EmailApp;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class SMTPEmailSender extends EmailSender {
    private static final Logger logger = LogManager.getLogger(SMTPEmailSender.class);

    public SMTPEmailSender(String smtpHost, String userEmail, String userPassword) {
        super(smtpHost, userEmail, userPassword);
    }

    @Override
    public void sendEmail(String recipient, String name, String body) {
        Properties props = setEmailProperties();
        Session session = createSession(props);

        try {
            MimeMessage message = createMessage(session, recipient, name, body);
            Transport.send(message);
            logger.info("Email successfully sent to: " + recipient);
        } catch (MessagingException e) {
            logger.error("Error sending email to: " + recipient, e);
        }
    }

    private Properties setEmailProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", "587");
        return props;
    }

    private Session createSession(Properties props) {
        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userEmail, userPassword);
            }
        });
    }

    private MimeMessage createMessage(Session session, String recipient, String name, String body) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(userEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject("Message from Partners", "UTF-8");
        message.setText("Hello, " + name + "!\n\n" + body, "UTF-8");
        return message;
    }
}