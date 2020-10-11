package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Date;


public class Main extends Application {
    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        stage.setTitle("Tamagotchi");
        stage.setResizable(false);

        Saver.read();
        Date now = new Date();
        if(Saver.isErrorOrTimeToNewGame()){
            Parent chooseRoot = FXMLLoader.load(getClass().getResource("resources/fxml/choose.fxml"));
            Scene chooseScene = new Scene(chooseRoot, 600, 320);
            chooseScene.getStylesheets().add(getClass().getResource("resources/style.css").toExternalForm());
            stage.setScene(chooseScene);
        }
        else if( ( now.getTime() - Saver.dateFeed.getTime() )/(12*60*60*1000) >= 1){
            Parent dieRoot = FXMLLoader.load(getClass().getResource("resources/fxml/die.fxml"));
            Scene dieScene = new Scene(dieRoot, 600, 320);
            dieScene.getStylesheets().add(getClass().getResource("resources/style.css").toExternalForm());
            stage.setScene(dieScene);
        }else {
            Parent petRoot = FXMLLoader.load(getClass().getResource("resources/fxml/pet.fxml"));
            Scene petScene = new Scene(petRoot, 600, 320);
            petScene.getStylesheets().add(getClass().getResource("resources/style.css").toExternalForm());
            stage.setScene(petScene);
        }
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
