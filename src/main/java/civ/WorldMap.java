package civ;

import java.util.LinkedList;

public class WorldMap implements IWorldMap {
    private int width, height;
    private IMapField[][] fields;
    private LinkedList<MapPosition> highlightedPositions;
    private MapPosition selectedPosition;

    public WorldMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.fields = new MapField[width][height];
        this.selectedPosition = null;
        this.highlightedPositions = new LinkedList<>();
    }

    public LinkedList<MapPosition> getHighlightedPositions() {
        return this.highlightedPositions;
    }

    public MapPosition getSelectedPosition() {
        return this.selectedPosition;
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
        this.fields[position.x][position.y].setUnit(unit);
    }

    @Override
    public IMapField getField(MapPosition position) {
        return this.fields[position.x][position.y];
    }

    @Override
    public LinkedList<MapPosition> getPossibleMoves(MapPosition position) {
        return null;
    }

    public void fieldLeftClicked(MapPosition position) {
        this.selectedPosition = position;
    }

    public void fieldRightClicked(MapPosition position) {
        Unit selectedUnit = this.getField(selectedPosition).getUnit();
        if (selectedUnit != null) {
            if (selectedUnit.canMoveTo(position)) {
                this.move(selectedPosition, position);
                this.selectedPosition = position;
            }
        }
    }

    private void move(MapPosition pos1, MapPosition pos2) {
        Unit unit = this.getField(pos1).getUnit();
        this.getField(pos1).setUnit(null);
        this.getField(pos2).setUnit(unit);
    }
}
