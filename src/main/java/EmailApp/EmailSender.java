package EmailApp;

abstract class EmailSender {
    protected String smtpHost;
    protected String userEmail;
    protected String userPassword;

    public EmailSender(String smtpHost, String userEmail, String userPassword) {
        this.smtpHost = smtpHost;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public abstract void sendEmail(String recipient, String name, String body);
}
