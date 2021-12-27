package civ;

public interface IMapField {
    void addUnit(Unit unit);
    Unit getUnit();
    Terrain getTerrain();
    MapPosition getPosition();
}
