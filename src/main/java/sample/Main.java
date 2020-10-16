package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    Stage primaryStage;
    Scene primaryScene;
    AnchorPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setResizable(false);

        initLayout();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void initLayout(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view.fxml"));
            this.rootLayout = loader.load();
            ViewController controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.primaryScene = new Scene(this.rootLayout);
        this.primaryStage.setScene(this.primaryScene);
        this.primaryStage.show();
    }
}
