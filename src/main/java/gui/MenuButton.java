package gui;

import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class MenuButton extends Button {
    public MenuButton(String text) {
        super();
        this.setPrefWidth(530);
        this.setPrefHeight(200);
        this.setText(text);
        this.setFont(new Font(30));
    }
}
