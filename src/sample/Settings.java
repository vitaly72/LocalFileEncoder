package sample;

        import java.util.prefs.BackingStoreException;
        import java.util.prefs.Preferences;

/**
 * Клас для збереження налаштувань користувача, тобто шляхів для створення зашифрованих та розшифрованих файлів
 */
public class Settings {
    private static final String PATH_EN = "";
    private static final String PATH_DE = "";
    private static final String PATH_EN_KEY = "pathEn";
    private static final String PATH_DE_KEY = "pathDe";
    private static Preferences preferences_ = null;

    /**
     * Повертає шлях до збереження зашифрованих файлів
     * @return
     */
    public static String getPathEnFiles() {
        return getPrefs().get(PATH_EN_KEY, PATH_EN);
    }

    /**
     * Повертає шлях до збереження розшифрованих файлів
     * @return
     */
    public static String getPathDeFiles() {
        return getPrefs().get(PATH_DE_KEY, PATH_DE);
    }

    /**
     * Задає шлях до збереження зашифрованих файлів
     * @return
     */
    public static void setPathEnFiles(String pathEn) {
        getPrefs().put(PATH_EN_KEY, pathEn);
    }

    /**
     * Задає шлях до збереження зашифрованих файлів
     * @return
     */
    public static void setPathDeFiles(String pathDe) {
        getPrefs().put(PATH_DE_KEY, pathDe);
    }

    /**
     * Метод для синхронізації налаштувань
     */
    public static void sync() {
        try {
            getPrefs().sync();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    /**
     * Повертає об'єкт класу Preferences з збереженими зміними, якщо він пустий створює новий об'єкт
     * @return
     */
    private static Preferences getPrefs() {
        if (preferences_ == null) {
            preferences_ = Preferences.userNodeForPackage(Settings.class);
        }
        return preferences_;
    }
}