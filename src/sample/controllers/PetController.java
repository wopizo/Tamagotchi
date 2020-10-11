package sample.controllers;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import sample.Main;
import sample.Saver;
import sample.foods.Bone;
import sample.foods.Carrot;
import sample.foods.Fish;
import sample.foods.Food;
import sample.pets.Cat;
import sample.pets.Dog;
import sample.pets.Pet;
import sample.pets.Rabbit;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class PetController {
    @FXML
    public Pane root;
    @FXML
    ProgressBar moodBar;
    @FXML
    Button feedBtn;
    @FXML
    private Label nameLabel;

    private Pet pet;
    private Food food;
    private AnimationTimer timer;


    @FXML
    void initialize(){
        Saver.read();

        if(Saver.petClass.equals("Dog")) pet = new Dog(Saver.name);
        else if(Saver.petClass.equals("Cat")) pet = new Cat(Saver.name);
        else pet = new Rabbit(Saver.name);
        pet.growUp();

        if(pet.getFavoriteFood().equals("Bone")) food = new Bone(pet, this);
        else if(pet.getFavoriteFood().equals("Fish")) food = new Fish(pet, this);
        else food = new Carrot(pet, this);

        nameLabel.setText(pet.getName());
        checkChanges();

        feedBtn.setOnAction(event -> {
            if (!root.getChildren().contains(food)) {
                food.setTranslateX(root.widthProperty().doubleValue() / 2);
                root.getChildren().add(food);
                pet.eat(food);
            }
        });

        root.getChildren().addAll(pet);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }

    private void update(){
        //Каждый фрейм запускаем анимацию прогулки или поедания
        if (pet.isEating()) {
            pet.eat(food);
        } else {
            pet.walk(root.widthProperty().intValue());
        }

        //С определенным временным интервалом проверяем вырос ли питомец и изменилось ли его настроение и отображаем
        //также меняем задний фон, если время суток в реале изменилось
        if (Saver.isTimeToCheck()) {
            checkChanges();
        }
    }

    public void checkChanges(){
        pet.growUp();

        double mood = pet.myMoodIs();
        if (mood <= 0) {
            timer.stop();
            Parent dieRoot = null;
            try {
                dieRoot = FXMLLoader.load(getClass().getResource("../resources/fxml/die.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene dieScene = new Scene(dieRoot, 600, 320);
            dieScene.getStylesheets().add(getClass().getResource("../resources/style.css").toExternalForm());
            Main.stage.setScene(dieScene);
            Main.stage.show();
        } else {
            moodBar.setProgress(mood);
            if (mood <= 0.34) moodBar.setId("badMoodBar");
            else if (mood <= 0.67) moodBar.setId("mediumMoodBar");
            else moodBar.setId("goodMoodBar");
        }

        String nowTimeImgPath;
        int nowHours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (nowHours == 0) nowTimeImgPath = "../resources/img/23-24hours.png";
        else if (nowHours <= 2) nowTimeImgPath = "../resources/img/1-2hours.png";
        else if (nowHours <= 4) nowTimeImgPath = "../resources/img/3-4hours.png";
        else if (nowHours <= 6) nowTimeImgPath = "../resources/img/5-6hours.png";
        else if (nowHours <= 8) nowTimeImgPath = "../resources/img/7-8hours.png";
        else if (nowHours <= 10) nowTimeImgPath = "../resources/img/9-10hours.png";
        else if (nowHours <= 12) nowTimeImgPath = "../resources/img/11-12hours.png";
        else if (nowHours <= 14) nowTimeImgPath = "../resources/img/13-14hours.png";
        else if (nowHours <= 16) nowTimeImgPath = "../resources/img/15-16hours.png";
        else if (nowHours <= 18) nowTimeImgPath = "../resources/img/17-18hours.png";
        else if (nowHours <= 20) nowTimeImgPath = "../resources/img/19-20hours.png";
        else if (nowHours <= 22) nowTimeImgPath = "../resources/img/21-22hours.png";
        else nowTimeImgPath = "../resources/img/23-24hours.png";
        Image backGroundImg = new Image(getClass().getResourceAsStream(nowTimeImgPath), root.getPrefWidth(), root.getPrefHeight(), false, true);
        BackgroundImage myBI = new BackgroundImage(backGroundImg,
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setBackground(new Background(myBI));

        Saver.lastCheck = new Date(System.currentTimeMillis());
    }
}
