package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.foods.Food;
import sample.pets.Pet;

import java.util.Date;

public class Main extends Application {
    Pane root;
    ProgressBar pb;
    private Pet pet;
    private Food food;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new Pane();
        root.setPrefSize(600, 600);

        Saver.create();
        Saver.read();
        pet = new Pet(Saver.name);
        pet.growUp();
        food = new Food(pet, root);

        System.out.println(Saver.log());


        Button btn = new Button();
        btn.setText("Покормить");
        btn.setTranslateX(30);
        btn.setTranslateY(30);

        btn.setPrefSize(150,50);
        btn.setOnAction(event ->{
            food.setTranslateX(root.widthProperty().doubleValue()/2);
            root.getChildren().add(food);
            pet.eat(food);
        });

        pb = new ProgressBar(0);
        pb.setTranslateX(400);
        pb.setTranslateY(30);
        pb.setPrefSize(150, 20);
        pb.setStyle("-fx-accent: green; ");

        root.getChildren().addAll(pet, btn, pb);
        Scene scene = new Scene(root);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();

        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void update() {
        if(pet.isEating()){
            pet.eat(food);
        } else {
            pet.walk(root.widthProperty().intValue());
        }

        //С определенным временным интервалом проверяем вырос ли питомец и изменилось ли его настроение
        if(Saver.isTimeToCheck()){
            pet.growUp();

            double mood = pet.myMoodIs();
            System.out.println(mood);
            if(mood<=0){
                System.out.println("Pet dies");
            }else{
                pb.setProgress(mood);
                if(mood<0.34) pb.setStyle("-fx-accent: red; ");
                else if (mood<=0.66) pb.setStyle("-fx-accent: yellow; ");
                else pb.setStyle("-fx-accent: green; ");
            }

            Saver.lastCheck = new Date(System.currentTimeMillis());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


}


//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//
//public class Main extends Application {
//
//    @Override
//    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 700, 400));
//        primaryStage.show();
//    }
//
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
