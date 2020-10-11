package sample.pets;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import sample.Saver;
import sample.SpriteAnimation;

import java.util.Date;

public class Rabbit extends Pet {

    //Для анимации
    protected int count = 3;
    protected int columns = 3;
    protected int offsetX = 0;
    protected int offsetY = 0;
    protected int width = (int) (90 * grown);
    protected int height = (int) (45 * grown);
    protected String imgPath = "../resources/sprites/rabbit.png";
    protected Image image = new Image(getClass().getResourceAsStream(imgPath), count * width, 2 * height, false, true);
    protected ImageView imageView = new ImageView(image);

    public Rabbit(String name) {
        super(name);

        favoriteFood = "Carrot";

        imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
        this.getChildren().addAll(imageView);
    }

    @Override
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

    @Override
    public void growUp(){
        Date now = new Date(System.currentTimeMillis());
        long hoursFromBorn = (now.getTime() - Saver.dateCreate.getTime()) / (GROW_PERIOD);

        //Каждые 6 часов питомец вырастает (максимум 5 раз)
        if (hoursFromBorn >= 6*5) grown = 2;
        else if (hoursFromBorn >= 6*4) grown = 1.8;
        else if (hoursFromBorn >= 6*3) grown = 1.6;
        else if (hoursFromBorn >= 6*2) grown = 1.4;
        else if (hoursFromBorn >= 6) grown = 1.2;
        else grown = 1;

        //Если питомец подрос, увеличиваем его текстурку
        if(this.imageView.getViewport().getWidth() != (int)(grown*32) ){
            getChildren().removeAll(imageView);
            width = (int) (90 * grown);
            height = (int) (45 * grown);
            image = new Image(getClass().getResourceAsStream(imgPath), count * width, 2 * height, false, true);
            imageView = new ImageView(image);
            imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
            animation = new SpriteAnimation(imageView, Duration.millis(count * 100), count, columns, offsetX, offsetY, width, height, true);
            this.setPrefSize(width, height);
            this.setTranslateY(300-height);
            getChildren().addAll(imageView);
        }
    }
}

