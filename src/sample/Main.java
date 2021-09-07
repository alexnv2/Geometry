package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Класс Main наследует класс Application.
 * Базовый класс для запуска программы.
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Метод для запуска программы. Создает главное сцену приложения.
     * @param primaryStage - главная сцена
     * @throws Exception - запуск приложения
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Геометрия");//Название окна
        primaryStage.setScene(new Scene(root, 950, 600));//размер сцены
        primaryStage.show();//вывести на экран
    }



}
