package civ;

public interface IMapField {
    void setUnit(Unit unit);
    Unit getUnit();
    Terrain getTerrain();
    MapPosition getPosition();
}
