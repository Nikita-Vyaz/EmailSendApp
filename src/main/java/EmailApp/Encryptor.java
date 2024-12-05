package EmailApp;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class Encryptor {
    private static final String ALGORITHM = "AES";

    public static String encrypt(String data, SecretKey key) throws Exception {
        return processCipher(data, key, Cipher.ENCRYPT_MODE);
    }

    public static String decrypt(String encryptedData, SecretKey key) throws Exception {
        return processCipher(encryptedData, key, Cipher.DECRYPT_MODE);
    }

    private static String processCipher(String data, SecretKey key, int mode) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(mode, key);
        byte[] result = mode == Cipher.ENCRYPT_MODE ? cipher.doFinal(data.getBytes())
                : cipher.doFinal(Base64.getDecoder().decode(data));
        return mode == Cipher.ENCRYPT_MODE ? Base64.getEncoder().encodeToString(result) : new String(result);
    }

    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }
}
