package sample.algorithm;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Симетричний алгоритм блочного шифрування з розміром блока 128 біт та ключ 256 біт
 */
public class AES{
    private static final String ALGORITHM = "AES";
    public static boolean wrongPassword;

    /**
     * Метод для шифрування та розшифрування файлу по ключу
     * @param cipherMode
     * @param key
     * @param inputFile
     * @param outputFile
     * @return
     */
    public static boolean fileProcessor(int cipherMode, SecretKeySpec key, File inputFile, File outputFile) {
        wrongPassword = false;
        try {
            Key secretKey = key;
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(cipherMode, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            outputStream.close();
            inputStream.close();

        } catch (Exception e) {
            wrongPassword = true;
            e.printStackTrace();
        }
        return wrongPassword;
    }

    /**
     * Повертає 256 бітний ключ на основі строкового параметра
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static SecretKeySpec getSecretKeySpec(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] saltBytes = new byte[20];
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 65556, 256);
        SecretKey secretKey = factory.generateSecret(spec);
        return new SecretKeySpec(secretKey.getEncoded(), ALGORITHM);
    }
}