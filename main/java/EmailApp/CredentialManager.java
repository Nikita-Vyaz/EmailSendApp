package EmailApp;

import javax.crypto.SecretKey;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс CredentialManager для управления сохранением ключа и учетных данных.
 *
 * Этот класс генерирует секретный ключ, шифрует учетные данные и сохраняет их в файлы.
 */
public class CredentialManager {
    private static final Logger logger = LogManager.getLogger(CredentialManager.class);
    private static final String SECRET_KEY_FILE = "secret.key";      // Путь к файлу секретного ключа
    private static final String CREDENTIALS_FILE = "credentials.txt"; // Путь к файлу учетных данных

    /**
     * Главный метод для выполнения программы.
     *
     * Генерирует секретный ключ, сохраняет его и шифрует учетные данные, затем сохраняет их в файл.
     *
     * @param args Аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        try {
            SecretKey key = Encryptor.generateKey(); // Генерируем секретный ключ
            saveSecretKey(key);                      // Сохраняем ключ
            saveCredentials("rabotakursovaa700@gmail.com", "vuyw tkkt nupq oewz", key); // Сохраняем учетные данные
            logger.info("Ключ и учетные данные успешно сохранены.");
        } catch (Exception e) {
            logger.error("Ошибка при сохранении ключа или учетных данных: ", e);
        }
    }

    /**
     * Сохраняет секретный ключ в файл.
     *
     * @param key Секретный ключ для сохранения.
     * @throws Exception Если произошла ошибка при сохранении ключа.
     */
    private static void saveSecretKey(SecretKey key) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SECRET_KEY_FILE))) {
            oos.writeObject(key);
            logger.info("Секретный ключ успешно сохранен в файл: {}", SECRET_KEY_FILE);
        } catch (Exception e) {
            logger.error("Ошибка при сохранении секретного ключа: ", e);
            throw e; // Пробрасываем исключение выше
        }
    }

    /**
     * Сохраняет учетные данные (электронную почту и зашифрованный пароль) в файл.
     *
     * @param userEmail    Электронная почта пользователя.
     * @param userPassword Пароль пользователя.
     * @param key          Секретный ключ для шифрования пароля.
     * @throws Exception Если произошла ошибка при сохранении учетных данных.
     */
    private static void saveCredentials(String userEmail, String userPassword, SecretKey key) throws Exception {
        String encryptedPassword = Encryptor.encrypt(userPassword, key); // Шифруем пароль
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE))) {
            writer.write(userEmail);               // Сохраняем электронную почту
            writer.newLine();
            writer.write(encryptedPassword);       // Сохраняем зашифрованный пароль
            logger.info("Учетные данные успешно сохранены в файл: {}", CREDENTIALS_FILE);
        } catch (Exception e) {
            logger.error("Ошибка при сохранении учетных данных: ", e);
            throw e; // Пробрасываем исключение выше
        }
    }
}
