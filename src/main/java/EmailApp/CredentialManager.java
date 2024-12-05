package EmailApp;

import javax.crypto.SecretKey;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CredentialManager {
    private static final Logger logger = LogManager.getLogger(CredentialManager.class);
    private static final String SECRET_KEY_FILE = "secret.key";
    private static final String CREDENTIALS_FILE = "credentials.txt";

    public static void main(String[] args) {
        try {
            SecretKey key = Encryptor.generateKey();
            saveSecretKey(key);
            saveCredentials("rabotakursovaa700@gmail.com", "vuyw tkkt nupq oewz", key);
            logger.info("Ключ и учетные данные успешно сохранены.");
        } catch (Exception e) {
            logger.error("Ошибка при сохранении ключа или учетных данных: ", e);
        }
    }

    private static void saveSecretKey(SecretKey key) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SECRET_KEY_FILE))) {
            oos.writeObject(key);
            logger.info("Секретный ключ успешно сохранен в файл: {}", SECRET_KEY_FILE);
        } catch (Exception e) {
            logger.error("Ошибка при сохранении секретного ключа: ", e);
            throw e; // Пробрасываем исключение выше
        }
    }

    private static void saveCredentials(String userEmail, String userPassword, SecretKey key) throws Exception {
        String encryptedPassword = Encryptor.encrypt(userPassword, key);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE))) {
            writer.write(userEmail);
            writer.newLine();
            writer.write(encryptedPassword);
            logger.info("Учетные данные успешно сохранены в файл: {}", CREDENTIALS_FILE);
        } catch (Exception e) {
            logger.error("Ошибка при сохранении учетных данных: ", e);
            throw e; // Пробрасываем исключение выше
        }
    }
}
