package civ;

import java.util.LinkedList;

public class Daredevil extends Unit {
    public Daredevil(WorldMap map, Player player, MapPosition position) {
        super(map, player, position, 200, 200, 30, 2);
        this.name = "daredevil";
    }

    @Override
    public boolean canMoveTo(MapPosition position) {
        if (!super.canMoveTo(position)) return false;
        MapField field = this.map.getField(position);
        return field.getTerrain() != Terrain.MOUNTAINS;
    }

    @Override
    public void action() {
        if (this.actionUsed) return;
        super.action();
        LinkedList<MapPosition> adjacent = this.position.adjacent();
        for (MapPosition position : adjacent) {
            Unit unit = map.getField(position).getUnit();
            if (unit != null) {
                unit.damage(this, 10);
                this.heal(10);
            }
        }
    }
}
