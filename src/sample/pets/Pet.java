package sample.pets;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import sample.Saver;
import sample.SpriteAnimation;
import sample.foods.Food;

import java.util.Date;

public class Pet extends Pane {

    //Параметры питомца
    private String name;
    private double grown;

    //Для анимации
    private int count = 4;
    private int columns = 4;
    private int offsetX = 0;
    private int offsetY = 0;
    private int width = (int)(32 * grown);
    private int height = (int)(32 * grown);
    private SpriteAnimation animation;
    private Image image = new Image(getClass().getResourceAsStream("../resources/sprites/cat.png"), count * width, 2 * height, false, true);
    private ImageView imageView = new ImageView(image);

    //Вспомогательные переменные для бизнес-логики
    private int steps = 0;
    private boolean isEating = false;

    public Pet(String name) {
        this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
        animation = new SpriteAnimation(imageView, Duration.millis(count * 100), count, columns, offsetX, offsetY, width, height, true);
        getChildren().addAll(imageView);
    }

    public void eat(Food food) {
        //При первом вызове метода определяем расстояние между едой и питомцем
        if (!isEating) {
            int petsLeftBorder = this.translateXProperty().intValue();
            int petsRightBorder = petsLeftBorder + this.widthProperty().intValue();

            int foodLeftBorder = food.translateXProperty().intValue();
            int foodRightBorder = foodLeftBorder + food.widthProperty().intValue();

            if (petsRightBorder < foodLeftBorder) {
                steps = foodLeftBorder - petsRightBorder;
            } else if (petsLeftBorder > foodRightBorder) {
                steps = foodRightBorder - petsLeftBorder;
            } else {
                steps = 0;
            }
        }

        isEating = true;

        //Питомец идет к еде
        if (steps > 0) {
            stepTo(true);
            steps--;
        } else if (steps < 0) {
            stepTo(false);
            steps++;
        }

        //Как дошли до еды, запускаем анимацию съедания и выходим из метода
        //isEating переключается в классе Food после окончания анимации
        if (steps == 0) {
            this.animation.stop();
            food.destroy();
        }
    }

    public void walk(int widthWalkingArea) {
        if (steps == 0) {
            this.animation.stop();
            this.wantsToGo(widthWalkingArea);
        }

        if (steps > 0) {
            stepTo(true);
            steps--;
        } else if (steps < 0) {
            stepTo(false);
            steps++;
        }
    }

    //Проигрывает анимацию хотьбы и сдвигает питомца на 1 пиксель в указанном направлении
    private void stepTo(boolean right) {
        this.animation.play();

        if (right) {
            this.animation.setOffsetY(height);
            this.setTranslateX(this.getTranslateX() + 1);
        } else {
            this.animation.setOffsetY(0);
            this.setTranslateX(this.getTranslateX() - 1);
        }
    }

    //Генерирует координаты прогулки питомца
    private void wantsToGo(int widthWalkingArea) {
        int newSteps = (int) ((Math.random() * widthWalkingArea) - widthWalkingArea / 2);

        int petsLeftBorder = this.translateXProperty().intValue();
        int petsRightBorder = petsLeftBorder + this.widthProperty().intValue();

        //Если передвижение на сгенерированные координаты выведет питомца за край экрана, меняем координаты на координаты этого края
        if (petsRightBorder + newSteps > widthWalkingArea) {
            newSteps = (int) widthWalkingArea - petsRightBorder;
        } else if (petsLeftBorder + newSteps < 0) {
            newSteps = petsLeftBorder * -1;
        }

        steps = newSteps;
    }

    //Если не покормить питомца в течении 12 часов, он умрет
    //Метод возвращает значение для ProgressBar, который является индикатором настроения питомца
    public double myMoodIs() {
        Date now = new Date(System.currentTimeMillis());
        long hoursFromFeeding = (now.getTime() - Saver.dateFeed.getTime()) / (5 * 1000);
        return (double) (12-hoursFromFeeding)/12;
    }

    public void growUp(){
        Date now = new Date(System.currentTimeMillis());
        long hoursFromBorn = (now.getTime() - Saver.dateCreate.getTime()) / (60 * 60 * 1000);

        //Каждые 6 часов питомец вырастает (максимум 5 раз)
        if (hoursFromBorn >= 6*5) grown = 3;
        else if (hoursFromBorn >= 6*4) grown = 2.8;
        else if (hoursFromBorn >= 6*3) grown = 2.6;
        else if (hoursFromBorn >= 6*2) grown = 2.4;
        else if (hoursFromBorn >= 6) grown = 2.2;
        else grown = 2;

        //Если питомец подрос, увеличиваем его текстурку
        if(this.imageView.getViewport().getWidth() != (int)(grown*32) ){
            getChildren().removeAll(imageView);
            width = (int)(32 * grown);
            height = (int)(32 * grown);
            image = new Image(getClass().getResourceAsStream("../resources/sprites/cat.png"), count * width, 2 * height, false, true);
            imageView = new ImageView(image);
            imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
            animation = new SpriteAnimation(imageView, Duration.millis(count * 100), count, columns, offsetX, offsetY, width, height, true);
            this.setPrefSize(width, height);
            this.setTranslateY(300-height);
            getChildren().addAll(imageView);
        }
    }

    public void setEating(boolean eating) {
        isEating = eating;
    }

    public boolean isEating() {
        return isEating;
    }

    public String getName() {
        return name;
    }
}