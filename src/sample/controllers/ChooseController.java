package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import sample.Main;
import sample.Saver;

import java.io.IOException;

public class ChooseController {
    @FXML
    private Pane rootChoose;
    @FXML
    private RadioButton catRb;
    @FXML
    private RadioButton dogRb;
    @FXML
    private RadioButton rabbitRb;
    @FXML
    private TextField nameField;
    @FXML
    private Button startBtn;

    private String petClass;
    private String name;

    @FXML
    void initialize() {
        ToggleGroup group = new ToggleGroup();
        catRb.setToggleGroup(group);
        dogRb.setToggleGroup(group);
        rabbitRb.setToggleGroup(group);

        group.selectedToggleProperty().addListener(((changed, oldValue, newValue) -> {
            RadioButton selectedRb = (RadioButton) newValue;
            switch (selectedRb.getText()){
                case "Собачка":
                    petClass = "Dog";
                    break;
                case "Кошка":
                    petClass = "Cat";
                    break;
                case "Зайчик":
                    petClass = "Rabbit";
                    break;
            }
        }));

        startBtn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            if(group.getSelectedToggle() == null){
                alert.setContentText("Выберете вид питомца!");
                alert.showAndWait();
            } else if(nameField.getText().equals("") || nameField.getText() == null){
                alert.setContentText("Введите имя питомца!");
                alert.showAndWait();
            } else {
                name = nameField.getText();
                Saver.create(name, petClass);

                Parent petRoot = null;
                try {
                    petRoot = FXMLLoader.load(getClass().getResource("../resources/fxml/pet.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene petScene = new Scene(petRoot, 600, 320);
                petScene.getStylesheets().add(getClass().getResource("../resources/style.css").toExternalForm());
                Main.stage.setScene(petScene);
                Main.stage.show();
            }
        });

    }
}
