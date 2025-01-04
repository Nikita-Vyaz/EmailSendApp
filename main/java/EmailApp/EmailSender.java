package EmailApp;

/**
 * Абстрактный класс EmailSender, представляющий общие свойства и методы для отправки электронных писем.
 *
 * Этот класс содержит необходимые параметры для настройки SMTP и объявляет абстрактный метод для отправки электронного письма.
 */
abstract class EmailSender {
    protected String smtpHost;      // SMTP-сервер
    protected String userEmail;     // Электронная почта отправителя
    protected String userPassword;   // Пароль отправителя

    /**
     * Конструктор класса EmailSender.
     *
     * @param smtpHost     Хост SMTP-сервера.
     * @param userEmail    Электронная почта отправителя.
     * @param userPassword  Пароль отправителя.
     */
    public EmailSender(String smtpHost, String userEmail, String userPassword) {
        this.smtpHost = smtpHost;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    /**
     * Абстрактный метод для отправки электронного письма.
     *
     * @param recipient Электронный адрес получателя.
     * @param name      Имя получателя.
     * @param body      Текст сообщения.
     */
    public abstract void sendEmail(String recipient, String name, String body);
}
