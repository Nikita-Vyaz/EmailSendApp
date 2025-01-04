package EmailApp;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс SMTPEmailSender для отправки электронных писем с использованием SMTP.
 *
 * Этот класс наследует от класса EmailSender и реализует метод отправки электронных писем.
 */
class SMTPEmailSender extends EmailSender {
    private static final Logger logger = LogManager.getLogger(SMTPEmailSender.class);

    /**
     * Конструктор класса SMTPEmailSender.
     *
     * @param smtpHost     Хост SMTP-сервера.
     * @param userEmail    Электронная почта отправителя.
     * @param userPassword Пароль отправителя.
     */
    public SMTPEmailSender(String smtpHost, String userEmail, String userPassword) {
        super(smtpHost, userEmail, userPassword);
    }

    /**
     * Отправляет электронное письмо получателю.
     *
     * @param recipient Электронный адрес получателя.
     * @param name      Имя получателя.
     * @param body      Текст сообщения.
     */
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

    /**
     * Устанавливает свойства для конфигурации email-сессии.
     *
     * @return Свойства для SMTP.
     */
    private Properties setEmailProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", "587");
        return props;
    }

    /**
     * Создает сессию электронной почты с заданными свойствами.
     *
     * @param props Свойства для конфигурации сессии.
     * @return Созданная сессия.
     */
    private Session createSession(Properties props) {
        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userEmail, userPassword);
            }
        });
    }

    /**
     * Создает сообщение электронной почты.
     *
     * @param session   Сессия электронной почты.
     * @param recipient Электронный адрес получателя.
     * @param name      Имя получателя.
     * @param body      Текст сообщения.
     * @return Созданное сообщение.
     * @throws MessagingException Если возникла ошибка при создании сообщения.
     */
    private MimeMessage createMessage(Session session, String recipient, String name, String body) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(userEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject("Message from Partners", "UTF-8");
        message.setText("Hello, " + name + "!\n\n" + body, "UTF-8");
        return message;
    }
}
