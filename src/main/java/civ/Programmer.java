package civ;

public class Programmer extends Unit {
    public Programmer(WorldMap map, Player player, MapPosition position) {
        super(map, player, position, 100, 100, 20, 3);
        this.name = "programmer";
    }

    @Override
    public boolean canMoveTo(MapPosition position) {
        if (!super.canMoveTo(position)) return false;
        MapField field = this.map.getField(position);
        return field.getTerrain() != Terrain.MOUNTAINS;
    }
}
