package sample.controller;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Settings;
import sample.algorithm.AES;
import sample.files.CryptFile;

import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import static sample.controller.PaneResult.setTextLabel;

/**
 * Клас для ініціалізації та обробки подій елементів користувацького інтерфейсу
 */
public class Controller {
    @FXML
    private CheckBox showPassword;
    @FXML
    private CheckBox deleteOriginal;
    @FXML
    private PasswordField passwordField;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ScrollPane pane;
    @FXML
    private Label logLabel;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private ProgressBar progressBarAllFiles;
    @FXML
    private Button startButton;
    @FXML
    private MenuItem menuItemOpenFile;
    @FXML
    private MenuItem menuItemExit;
    @FXML
    private MenuItem menuItemChangePathEnFiles;
    @FXML
    private MenuItem menuItemChangePathDeFiles;
    @FXML
    private MenuItem menuItemHelp;

    private String filePath = "";

    private ArrayList<String> filePathList = new ArrayList<>();
    private static boolean deleteOriginalValue = false;
    final private FileChooser fileChooser = new FileChooser();
    final private DirectoryChooser directoryChooser = new DirectoryChooser();
    private int count = 0;

    /**
     * Ініціалізує прапорці, панель та обробка події перетягування файлу в неї
     */
    @FXML
    private void initialize() {
        showPassword.setSelected(false);
        deleteOriginal.setSelected(false);

        passwordField.setStyle("-fx-prompt-text-fill: black");

        pane.setPrefSize(anchorPane.getPrefWidth(), anchorPane.getPrefHeight());
        pane.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                final Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                    final File file = db.getFiles().get(0);
                } else {
                    event.consume();
                }
            }
        });
        pane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                final Dragboard dragboard = event.getDragboard();
                boolean success = false;
                if (dragboard.hasFiles()) {
                    success = true;

                    for (File fileItem : dragboard.getFiles()) {
                        filePathList.add(fileItem.getAbsolutePath());
                        logLabel.setText(logLabel.getText() + "\n" + fileItem.getAbsolutePath());
                    }
                    count = 3;

                    final File file = dragboard.getFiles().get(0);
                    filePath = filePathList.get(0);

                    progressBarAllFiles.setProgress(0);
                    progressBar.setProgress(0);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                File droppedFile = new File(String.valueOf(new FileInputStream(file.getAbsolutePath())));
                            } catch (FileNotFoundException ex) {
                            }
                        }
                    });
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
        pane.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                System.out.println("exited");
            }
        });

        menuItemOpenFile.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        menuItemExit.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        menuItemChangePathEnFiles.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        menuItemChangePathDeFiles.setAccelerator(KeyCombination.keyCombination("Ctrl+D"));
        menuItemHelp.setAccelerator(KeyCombination.keyCombination("Ctrl+I"));
    }

    /**
     * Обробка кліку кнопки Очистити де файл зашифровується або розшифровується
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws IOException
     */
    public void stopButtonClicked() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        filePathList.clear();
        logLabel.setText("Файли: ");
        passwordField.setText("");
        progressBar.setProgress(0.0);
        progressBarAllFiles.setProgress(0.0);
    }

    /**
     * Обробка події вибору пунктку меню для вихіду
     */
    public void menuItemExitClicked() {
        System.exit(0);
    }

    /**
     * Обробка події вибору пукнту меню відкриття файлу
     */
    public void menuItemOpenFileClicked() {
        fileChooser.setTitle("Виберіть файли");
        List<File> fileList = fileChooser.showOpenMultipleDialog(null);

        if (!fileList.isEmpty()) {
            for (File file : fileList) {
                filePathList.add(file.getAbsolutePath());
                setTextLabel(logLabel, file.getAbsolutePath());
                count = 3;
            }
        }
    }

    /**
     * Обробка події вибору пукнту меню Вибору шлязу для збереження розшифрованих файлів
     */
    public void menuItemChangePathDeFilesClicked() {
        directoryChooser.setTitle("Виберіть папку для збереження розшифрованих файлів");
        File directory = directoryChooser.showDialog(null);
        if (directory != null) {
            Settings.setPathDeFiles(directory.getAbsolutePath());
        } else {
            System.out.println("not dir");
        }
    }

    /**
     * Обробка події вибору пукнту меню Вибору шлязу для збереження зашифрованих файлів
     */
    public void menuItemChangePathEnFilesClicked() {
        directoryChooser.setTitle("Виберіть папку для збереження зашифрованих файлів");
        File directory = directoryChooser.showDialog(null);
        if (directory != null) {
            Settings.setPathEnFiles(directory.getAbsolutePath());
        } else {
            System.out.println("not dir");
        }
    }

    /**
     * Обробка події вибору пукнту меню Допомога
     */
    public void menuItemHelpClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/fxml/help.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setTitle("Інструкція");
        stage.getIcons().add(new Image("file:src\\res\\icon.png"));
        stage.setScene(new Scene(root1));
        stage.show();
    }

    /**
     * Обробка кліку кнопки Розпочати де файл зашифровується або розшифровується
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws IOException
     */
    public void startButtonClicked() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        if (count == 0) {
            stopButtonClicked();
        } else {
            PaneResult paneResult = new PaneResult(logLabel, filePathList, deleteOriginalValue);
            if (paneResult.allFileEncResult() == false) {
                if (paneResult.passwordResult(passwordField.getText()) && paneResult.settingsResult()) {
                    SecretKeySpec secretKeySpec = AES.getSecretKeySpec(passwordField.getText());
                    if (paneResult.fileIsDecrypt()) {
                        if (CryptFile.deCryptFiles(filePath, Settings.getPathDeFiles(), secretKeySpec)) {
                            setTextLabel(logLabel, "Неправильний пароль!");
                            setTextLabel(logLabel, "Залишолось спроб: " + count);
                            count--;
                        } else {
                            if (paneResult.deleteZipResult()) {
                                progressBar.setProgress(1.0);
                                progressBarAllFiles.setProgress(1.0);
                            }
                        }
                    } else {
                        if (paneResult.equalFileNameResult() == false) {
                            paneResult.filesEncryptResult(secretKeySpec);
                            progressBar.setProgress(1.0);
                            progressBarAllFiles.setProgress(1.0);
                        }
                    }
                }
            }
        }
    }

    /**
     * Обробка події кліку на прапорець Показати для паролю
     */
    public void showPasswordClicked() {
        if (showPassword.isSelected()) {
            passwordField.setPromptText(passwordField.getText());
            passwordField.setText("");
            passwordField.setDisable(true);
            startButton.setDisable(true);
        } else {
            passwordField.setText(passwordField.getPromptText());
            passwordField.setPromptText("");
            passwordField.setDisable(false);
            startButton.setDisable(false);
        }
    }

    /**
     * Обробка події кліку на прапорець Видалити оригінал
     */
    public void deleteOriginalClicked() {
        deleteOriginalValue = deleteOriginal.isSelected();
        System.out.println("deleteOriginalValue: " + deleteOriginalValue);
    }
}