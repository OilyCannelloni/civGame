package civ;

import java.util.LinkedList;

public class WorldMap implements IWorldMap {
    private int width, height;
    private IMapField[][] fields;
    public LinkedList<MapPosition> selectedPositions, highlightedPositions;

    public WorldMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.fields = new MapField[width][height];
        this.selectedPositions = new LinkedList<>();
        this.highlightedPositions = new LinkedList<>();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public MapPosition getLowerRight() {
        return new MapPosition(this.width, this.height);
    }

    @Override
    public void placeField(MapPosition position, IMapField field) {
        this.fields[position.x][position.y] = field;
    }

    public void placeUnit(Unit unit, MapPosition position) {
        this.fields[position.x][position.y].addUnit(unit);
    }

    @Override
    public IMapField getField(MapPosition position) {
        return this.fields[position.x][position.y];
    }

    @Override
    public LinkedList<MapPosition> getPossibleMoves(MapPosition position) {
        return null;
    }
}
