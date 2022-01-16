package civ;

import javafx.scene.paint.Color;

public class Player {
    int priority;
    PlayerColor color;

    public Player(PlayerColor color, int priority) {
        this.color = color;
        this.priority = priority;
    }

    public PlayerColor getColor() {
        return this.color;
    }
}
