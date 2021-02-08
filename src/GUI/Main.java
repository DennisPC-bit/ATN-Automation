package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Author DennisPC-bit
 *
 */

public class Main extends Application {
    private Stage primaryStage;
    private ViewController viewController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage=primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View.fxml"));
        Parent root = loader.load();
        this.viewController = loader.getController();
        viewController.setMain(this);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root,400,400));
        primaryStage.show();
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
