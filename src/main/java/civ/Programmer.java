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

    public Programmer(WorldMap map) {
        super(map,100, 30, 3);
    }

    @Override
    public boolean canMoveTo(MapPosition position) {
        if (!super.canMoveTo(position)) return false;
        MapField field = this.map.getField(position);
        return field.getTerrain() != Terrain.MOUNTAINS;
    }
}
