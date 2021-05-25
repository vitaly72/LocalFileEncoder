package sample.files;

import sample.Settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Клас для архівації та розархівування файлів
 */
public class ZipFile {
    public static final String zipFileName = "archive.zip";
    public static final String zipFile = Settings.getPathDeFiles() + File.separator + "archive.zip";

    /**
     * Метод ствроє архів з файлами
     * @param filePathList
     * @throws IOException
     */
    public static void zipMultipleFiles(ArrayList<String> filePathList) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
        for (String filePath : filePathList) {
            File fileToZip = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOutputStream.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fileInputStream.read(bytes)) >= 0) {
                zipOutputStream.write(bytes, 0, length);
            }
            fileInputStream.close();
        }
        zipOutputStream.close();
        fileOutputStream.close();
    }

    /**
     * Метод розархівує архів
     * @param pathZipFile
     * @param path
     * @throws IOException
     */
    public static void unzipFile(String pathZipFile, String path) throws IOException {
        File destDir = new File(path);
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(pathZipFile));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    /**
     * Метод створює файли під час їх розархівації
     * @param destinationDir
     * @param zipEntry
     * @return
     * @throws IOException
     */
    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    /**
     * Метод видаляє файл за вказаним шляхом
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.delete()) {
                System.out.println(file.getName() + " deleted");
            } else {
                System.out.println("failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}