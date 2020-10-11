package sample.pets;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import sample.Saver;
import sample.SpriteAnimation;
import sample.foods.Fish;

import java.util.Date;

public class Cat extends Pet {
    protected String imgPath = "../resources/sprites/cat.png";
    protected Image image = new Image(getClass().getResourceAsStream(imgPath), count * width, 2 * height, false, true);
    protected ImageView imageView = new ImageView(image);

    public Cat(String name) {
        super(name);

        favoriteFood = "Fish";

        this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
        this.getChildren().addAll(imageView);
    }

    @Override
    public void growUp(){
        Date now = new Date(System.currentTimeMillis());
        long hoursFromBorn = (now.getTime() - Saver.dateCreate.getTime()) / (GROW_PERIOD);

        //Каждые 6 часов питомец вырастает (максимум 5 раз)
        if (hoursFromBorn >= 6*5) grown = 3;
        else if (hoursFromBorn >= 6*4) grown = 2.8;
        else if (hoursFromBorn >= 6*3) grown = 2.6;
        else if (hoursFromBorn >= 6*2) grown = 2.4;
        else if (hoursFromBorn >= 6) grown = 2.2;
        else grown = 2;

        //Если питомец подрос, увеличиваем его текстурку
        if(this.imageView.getViewport().getWidth() != (int)(grown*32) ){
            this.getChildren().removeAll(imageView);
            width = (int)(32 * grown);
            height = (int)(32 * grown);
            image = new Image(getClass().getResourceAsStream(imgPath), count * width, 2 * height, false, true);
            imageView = new ImageView(image);
            imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
            animation = new SpriteAnimation(imageView, Duration.millis(count * 100), count, columns, offsetX, offsetY, width, height, true);
            this.setPrefSize(width, height);
            this.setTranslateY(300-height);
            this.getChildren().addAll(imageView);
        }
    }
}
