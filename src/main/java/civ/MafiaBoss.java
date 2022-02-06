package civ;

import java.util.LinkedList;

public class MafiaBoss extends Unit {
    public MafiaBoss(WorldMap map, Player player, MapPosition position) {
        super(map, player, position, 400, 400, 30, 1);
        this.name = "mafiaboss";
    }

    @Override
    public boolean canMoveTo(MapPosition position) {
        if (!super.canMoveTo(position)) return false;
        MapField field = this.map.getField(position);
        return field.getTerrain() != Terrain.MOUNTAINS && field.getTerrain() != Terrain.WATER;
    }

    @Override
    public void action() {
        if (this.actionUsed) return;
        super.action();
        LinkedList<MapPosition> adjacent = this.position.adjacent();
        for (MapPosition position : adjacent) {
            Unit unit = map.getField(position).getUnit();
            if (unit == null) {
                int i = Algorithm.random.nextInt(100);
                if (i < 30) {
                    map.placeUnit(new Programmer(map, this.player, position));
                }
                return;
            }
        }
    }
}
