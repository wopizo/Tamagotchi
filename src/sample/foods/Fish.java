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

public class Fish extends Food {
    protected Image image = new Image(getClass().getResourceAsStream("../resources/sprites/fish.png"), count*width, height, false, true);
    protected ImageView imageView = new ImageView(image);

    public Fish(Pet pet, PetController petController) {
        super(pet, petController);
        this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
        animation = new SpriteAnimation(imageView, Duration.millis(count*100*3), count, columns, offsetX, offsetY, width, height, false);
        getChildren().addAll(imageView);

        this.animation.setOnFinished(event -> {
            petController.root.getChildren().remove(this);
            this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
            Saver.changeDateFeed();
            pet.setEating(false);
            petController.checkChanges();
        });
    }
}
