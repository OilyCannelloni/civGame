package civ;

import javafx.scene.paint.Color;

public enum PlayerColor {
    BLUE,
    RED,
    YELLOW,
    GREEN;

    public Color getRGB() {
        switch (this) {
            case RED: return Color.MAGENTA;
            case BLUE: return Color.CYAN;
            case YELLOW: return Color.YELLOW;
            case GREEN: return Color.LIGHTGREEN;
            default: return Color.WHITE;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case RED: return "red";
            case BLUE: return "blue";
            case YELLOW: return "yellow";
            case GREEN: return "green";
            default: return "";
        }
    }
}
