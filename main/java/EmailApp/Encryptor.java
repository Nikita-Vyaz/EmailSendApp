package EmailApp;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * Класс Encryptor для шифрования и дешифрования данных с использованием алгоритма AES.
 *
 * Этот класс предоставляет методы для шифрования и дешифрования строк, а также для генерации секретного ключа.
 */
public class Encryptor {
    private static final String ALGORITHM = "AES"; // Алгоритм шифрования

    /**
     * Шифрует данные с использованием предоставленного секретного ключа.
     *
     * @param data Данные для шифрования.
     * @param key  Секретный ключ для шифрования.
     * @return Зашифрованные данные в виде строки, закодированной в Base64.
     * @throws Exception Если произошла ошибка при шифровании.
     */
    public static String encrypt(String data, SecretKey key) throws Exception {
        return processCipher(data, key, Cipher.ENCRYPT_MODE);
    }

    /**
     * Дешифрует зашифрованные данные с использованием предоставленного секретного ключа.
     *
     * @param encryptedData Зашифрованные данные в виде строки, закодированной в Base64.
     * @param key          Секретный ключ для дешифрования.
     * @return Дешифрованные данные в виде строки.
     * @throws Exception Если произошла ошибка при дешифровании.
     */
    public static String decrypt(String encryptedData, SecretKey key) throws Exception {
        return processCipher(encryptedData, key, Cipher.DECRYPT_MODE);
    }

    /**
     * Обрабатывает шифрование или дешифрование данных в зависимости от указанного режима.
     *
     * @param data Данные для обработки.
     * @param key  Секретный ключ для шифрования или дешифрования.
     * @param mode Режим работы (шифрование или дешифрование).
     * @return Результат обработки данных в виде строки.
     * @throws Exception Если произошла ошибка при обработке.
     */
    private static String processCipher(String data, SecretKey key, int mode) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM); // Создаем объект шифра
        cipher.init(mode, key); // Инициализируем шифр с указанным режимом и ключом
        byte[] result = mode == Cipher.ENCRYPT_MODE
                ? cipher.doFinal(data.getBytes())
                : cipher.doFinal(Base64.getDecoder().decode(data)); // Применяем шифрование или дешифрование
        return mode == Cipher.ENCRYPT_MODE ? Base64.getEncoder().encodeToString(result) : new String(result);
    }

    /**
     * Генерирует новый секретный ключ для шифрования.
     *
     * @return Сгенерированный секретный ключ.
     * @throws Exception Если произошла ошибка при генерации ключа.
     */
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM); // Создаем генератор ключей
        keyGenerator.init(128); // Устанавливаем размер ключа
        return keyGenerator.generateKey(); // Генерируем ключ
    }
}
