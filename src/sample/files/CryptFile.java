package sample.files;

import sample.algorithm.AES;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Клас для створення файлів та визову класу алгоритма для шифрування або розшифрування
 */
public class CryptFile {
    /**
     * Зашифровує файли за ключом створеного на основі паролю
     * @param filePath
     * @param pathEnFile
     * @param secretKeySpec
     * @return
     * @throws IOException
     */
    public static boolean enCryptFiles(String filePath, String pathEnFile, SecretKeySpec secretKeySpec) throws IOException {
        boolean result;
        File inputFile = new File(filePath);
        File encryptedFile = createNewEncryptFile(pathEnFile);

        AES.fileProcessor(Cipher.ENCRYPT_MODE, secretKeySpec, inputFile, encryptedFile);
        result = encryptedFile.createNewFile();

        return result;
    }

    /**
     * Розшифровує файл за ключом створеного на основі паролю
     * @param filePath
     * @param pathDeFile
     * @param secretKeySpec
     * @return
     * @throws IOException
     */
    public static boolean deCryptFiles(String filePath, String pathDeFile, SecretKeySpec secretKeySpec) throws IOException {
        File encryptedFile = new File(filePath);
        File decryptedFile = createNewDecryptFile(pathDeFile);
        System.out.println("path: " + decryptedFile.getAbsolutePath());

        boolean result = AES.fileProcessor(Cipher.DECRYPT_MODE, secretKeySpec, encryptedFile, decryptedFile);
        if (result) {
            System.out.println("password is " + !AES.wrongPassword);// false password
        } else {
            decryptedFile.getParentFile().mkdirs();
            decryptedFile.createNewFile();
            System.out.println("password is " + !AES.wrongPassword);// true password
        }

        return result;
    }

    /**
     * Повертає файл з іменем вхідного та роширенням .enc
     * @param enFile
     * @return
     */
    private static File createNewEncryptFile(String enFile) {
        return new File(enFile + File.separator + generatingRandomAlphanumericString() + ".enc");
    }

    /**
     * Повертає згенеровану строку з рандомними символами літер та цифр
     * @return
     */
    public static String generatingRandomAlphanumericString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /**
     * Повертає файл префіксом de_ та іменем зашифрованого файлу
     * @param deFile
     * @return
     */
    private static File createNewDecryptFile(String deFile) {
        return new File(deFile + File.separator + "de_" + ZipFile.zipFileName);
    }
}