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

public abstract class Pet extends Pane {

    //Период изменения роста питомца
    protected final int GROW_PERIOD = 6 * 60 * 60 * 1000;
    //Период изменения настроения питомца
    protected final int MOOD_PERIOD = 60 * 60 * 1000;

    //Параметры питомца
    protected String name;
    protected double grown;
    protected String favoriteFood;

    //Для анимации
    protected int count = 4;
    protected int columns = 4;
    protected int offsetX = 0;
    protected int offsetY = 0;
    protected int width = (int)(32 * grown);
    protected int height = (int)(32 * grown);
    protected SpriteAnimation animation;
    protected String imgPath = "";
    protected Image image = new Image(getClass().getResourceAsStream(imgPath), count * width, 2 * height, false, true);
    protected ImageView imageView = new ImageView(image);

    //Вспомогательные переменные для бизнес-логики
    protected int steps = 0;
    protected boolean isEating = false;

    public Pet(String name) {
        this.name = name;

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
    protected void stepTo(boolean right) {
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
    protected void wantsToGo(int widthWalkingArea) {
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
        long hoursFromFeeding = (now.getTime() - Saver.dateFeed.getTime()) / (MOOD_PERIOD);
        return (double) (12-hoursFromFeeding)/12;
    }

    public void growUp(){
        Date now = new Date(System.currentTimeMillis());
        long hoursFromBorn = (now.getTime() - Saver.dateCreate.getTime()) / (GROW_PERIOD);

        //Каждые 6 часов питомец вырастает (максимум 5 раз)
        if (hoursFromBorn >= 5) grown = 3;
        else if (hoursFromBorn >= 4) grown = 2.8;
        else if (hoursFromBorn >= 3) grown = 2.6;
        else if (hoursFromBorn >= 2) grown = 2.4;
        else if (hoursFromBorn >= 1) grown = 2.2;
        else grown = 2;

        //Если питомец подрос, увеличиваем его текстурку
        if(this.imageView.getViewport().getWidth() != (int)(grown*32) ){
            getChildren().removeAll(imageView);
            width = (int)(32 * grown);
            height = (int)(32 * grown);
            image = new Image(getClass().getResourceAsStream(imgPath), count * width, 2 * height, false, true);
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

    public String getFavoriteFood() {
        return favoriteFood;
    }
}