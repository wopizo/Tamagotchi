package sample.foods;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import sample.Saver;
import sample.SpriteAnimation;
import sample.controllers.PetController;
import sample.pets.Pet;

public abstract class Food extends Pane {

    protected int count = 4;
    protected int columns = 4;
    protected int offsetX = 0;
    protected int offsetY = 0;
    protected int width = (int)(32*1.5);
    protected int height = (int)(32*1.5);
    protected SpriteAnimation animation;

    protected Image image;
    protected ImageView imageView = new ImageView(image);

    public Food(Pet pet, PetController petController) {
        this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
        animation = new SpriteAnimation(imageView, Duration.millis(count*100*3), count, columns, offsetX, offsetY, width, height, false);
        getChildren().addAll(imageView);

        this.setTranslateY(300-height);
        this.setPrefSize(width, height);

        this.animation.setOnFinished(event -> {
            petController.root.getChildren().remove(this);
            this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
            Saver.changeDateFeed();
            pet.setEating(false);
            petController.checkChanges();
        });
    }

    public void destroy(){
        this.animation.play();
    }
}

