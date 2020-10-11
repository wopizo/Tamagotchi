package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sample.Saver;

import java.text.SimpleDateFormat;

public class DieController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label justLabel;

    @FXML
    void initialize() {
        Saver.read();
        nameLabel.setText(Saver.name + "\r\n" +
                new SimpleDateFormat().format(Saver.dateCreate.getTime()) + " - " + new SimpleDateFormat().format(Saver.dateFeed.getTime() + 12*60*60*1000));
        justLabel.setText(Saver.name + " уже не вернуть, но вы можете попробовать\r\n снова после " + new SimpleDateFormat().format(Saver.dateFeed.getTime() + 36*60*60*1000));
    }
}
