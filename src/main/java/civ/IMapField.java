package civ;

public interface IMapField {
    void addUnit(IUnit unit);
    IUnit getUnit();
    Terrain getTerrain();
}
