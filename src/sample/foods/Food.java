package sample.foods;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import sample.Saver;
import sample.SpriteAnimation;
import sample.pets.Pet;

public class Food extends Pane {

    private int count = 4;
    private int columns = 4;
    private int offsetX = 0;
    private int offsetY = 0;
    private int width = (int)(32*1.5);
    private int height = (int)(32*1.5);
    private SpriteAnimation animation;

    Image image = new Image(getClass().getResourceAsStream("../resources/sprites/fish.png"), count*width, height, false, true);
    ImageView imageView = new ImageView(image);

    public Food(Pet pet, Pane root) {
        this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
        animation = new SpriteAnimation(imageView, Duration.millis(count*100*3), count, columns, offsetX, offsetY, width, height, false);
        getChildren().addAll(imageView);

        this.setTranslateY(300-height);
        this.setPrefSize(width, height);

        this.animation.setOnFinished(event -> {
            root.getChildren().remove(this);
            this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
            Saver.changeDateFeed();
            pet.setEating(false);
        });
    }

    public void destroy(){
        this.animation.play();
    }
}

