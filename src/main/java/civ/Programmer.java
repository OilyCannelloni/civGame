package civ;

import gui.CanvasIcon;

public class Programmer extends Unit {
    static CanvasIcon ICON = new CanvasIcon(
            "file:.\\src\\main\\resources\\programmer.png",
            new Vector2D(28, 25)
    );

    public CanvasIcon getIcon() {
        return ICON;
    }

    public Programmer() {
        super(100, 30, 3);
    }

    @Override
    public boolean canMoveTo(MapPosition position) {
        return true;
    }
}
