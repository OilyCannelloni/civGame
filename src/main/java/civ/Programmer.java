package civ;

import gui.CanvasIcon;
public class Programmer extends Unit {
    public Programmer(WorldMap map, Player player) {
        super(map, player,60, 100, 30, 3);
        this.name = "programmer";
    }

    @Override
    public boolean canMoveTo(MapPosition position) {
        if (!super.canMoveTo(position)) return false;
        MapField field = this.map.getField(position);
        return field.getTerrain() != Terrain.MOUNTAINS;
    }
}
