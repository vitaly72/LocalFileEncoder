package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Головний клас для ініціалізації та запуску вікна
 */
public class Main extends Application {
    /**
     * Ініціалізує сцену та виводить її
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/res/fxml/sample.fxml"));
        primaryStage.setTitle("Шифратор локальних файлів");
        primaryStage.setScene(new Scene(root, 600, 380));
        primaryStage.resizableProperty().setValue(Boolean.FALSE);
        primaryStage.getIcons().add(new Image("file:src\\res\\icon.png"));
        primaryStage.show();
    }

    /**
     * Запускає програму
     * Метод запуску не повертається, поки програма не завершиться, або через виклик на Platform.exit, або всі вікна додатків не закриті.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}