package sample.controller;

import javafx.scene.control.Label;
import sample.Settings;
import sample.files.CryptFile;
import sample.files.ZipFile;

import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Клас для виведення результатів в панель
 */
public class PaneResult {
    private Label label;
    private ArrayList<String> filePathList;
    private boolean delFile;

    /**
     * Конструктор ініціалізує напис на формі, список файлів та параметр видалення файлів оригіналів
     *
     * @param label
     * @param filePathList
     * @param delFile
     */
    PaneResult(Label label, ArrayList<String> filePathList, boolean delFile) {
        this.label = label;
        this.filePathList = filePathList;
        this.delFile = delFile;
    }

    /**
     * Метод для виведення результату шифрування файлів
     *
     * @param secretKeySpec
     * @throws IOException
     */
    public void filesEncryptResult(SecretKeySpec secretKeySpec) throws IOException {
        ZipFile.zipMultipleFiles(filePathList);
        CryptFile.enCryptFiles(ZipFile.zipFile, Settings.getPathEnFiles(), secretKeySpec);
        setTextLabel(label, "File encrypted");
        ZipFile.deleteFile(ZipFile.zipFile);
        for(String path : filePathList){
            deleteFile(path, delFile);
        }
        filePathList.clear();
    }

    /**
     * Метод для виведення результату розшифрування файлів
     *
     * @return
     * @throws IOException
     */
    public boolean deleteZipResult() throws IOException {
        String pathZip = Settings.getPathDeFiles() + File.separator + "de_" + ZipFile.zipFileName;
        File zip = new File(pathZip);

        if (zip.exists()) {
            System.out.println("zip: " + zip.getAbsolutePath());
            ZipFile.unzipFile(pathZip, Settings.getPathDeFiles());
            ZipFile.deleteFile(pathZip);

            setTextLabel(label, "File decrypted");

            deleteFile(filePathList.get(0), delFile);
            filePathList.clear();

            return true;
        } else {
            return false;
        }
    }

    /**
     * Повертає значенння чи є у файла розширення ".enc", тобто він є зашифрованим файлом
     *
     * @return
     */
    public boolean fileIsDecrypt() {
        return filePathList.size() == 1 && getExtension(filePathList.get(0)).equals(".enc");
    }

    /**
     * Метод для виведення результату верифікації паролю
     *
     * @param password
     * @return
     */
    public boolean passwordResult(String password) {
        if (passwordVerification(password)) {
            return true;
        } else {
            setTextLabel(label, "The password must be at least 4 characters long!");
            return false;
        }
    }

    /**
     * Метод для виведення результату перевірки чи немає файлів з однаковою назвою та розширенням
     *
     * @return
     */
    public boolean equalFileNameResult() {
        if (equalFileName()) {
            setTextLabel(label, "Same file names! Rename the file so that the files in the same extension do not have the same name");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Повертає чи немає файлів з однаковою назвою та розширенням
     *
     * @return
     */
    public boolean equalFileName() {
        ArrayList<String> fileNameList = new ArrayList<>();
        for (String item : filePathList) {
            File file = new File(item);
            fileNameList.add(file.getName());
        }
        Set<String> set = new HashSet<>(fileNameList);
        if (set.size() < fileNameList.size()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Метод для виведення що можна розшифровувати тільки один зашифрований файл
     *
     * @return
     */
    public boolean allFileEncResult() {
        if (allFileEnc()) {
            setTextLabel(label, "Open only one encrypted file!");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Повертає чи відкрито один зашифрований файл
     *
     * @return
     */
    public boolean allFileEnc() {
        if (filePathList.size() == 1) {
            return false;
        }
        ArrayList<String> fileNameList = new ArrayList<>();
        for (String item : filePathList) {
            File file = new File(item);
            fileNameList.add(file.getName().substring(file.getName().lastIndexOf(".")));
        }
        return fileNameList.stream().allMatch(e -> e.equals(fileNameList.get(0)));
    }

    /**
     * Повертає чи довжиниа пароль від 4 симвлолів
     *
     * @param password
     * @return
     */
    public static boolean passwordVerification(String password) {
        return password.length() >= 4;
    }

    /**
     * Метод виводь чи задано шляхи для збереження зашифрованих та розшифрованих файлів
     *
     * @return
     */
    public boolean settingsResult() {
        if (Settings.getPathDeFiles().length() < 0) {
            setTextLabel(label, "Select a path to save encrypted files (in the Settings menu item)");
        } else if (Settings.getPathEnFiles().length() < 0) {
            setTextLabel(label, "Select a path to save encrypted files (in the Settings menu item)");
        } else {
            return true;
        }
        return false;
    }

    /**
     * Видалення файлу після закінчення його зашифрування або розшифрування
     *
     * @param filePath
     */
    private static void deleteFile(String filePath, boolean deleteOriginalValue) {
        try {
            File file = new File(filePath);
            if (deleteOriginalValue) {
                if (file.delete()) {
                    System.out.println(file.getName() + " deleted");
                } else {
                    System.out.println("failed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Задає текст на панелі
     *
     * @param label
     * @param text
     */
    public static void setTextLabel(Label label, String text) {
        label.setText(label.getText() + "\n" + text);
    }

    /**
     * Повертає розшрення файлу
     *
     * @param path
     * @return
     */
    public static String getExtension(String path) {
        return path.substring(path.lastIndexOf("."));
    }

}
