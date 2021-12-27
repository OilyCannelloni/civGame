package civ;

import java.util.LinkedList;

public class WorldMap {
    private int width, height;
    private MapField[][] fields;
    private LinkedList<MapPosition> highlightedPositions;
    private MapPosition selectedPosition;
    private MapRect boundingBox;

    public WorldMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.fields = new MapField[width][height];
        this.selectedPosition = null;
        this.highlightedPositions = new LinkedList<>();
        this.boundingBox = new MapRect(0, 0, width - 1, height - 1);
    }

    public boolean isWithinBounds(MapPosition position) {
        return this.boundingBox.contains(position);
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

    public void placeField(MapPosition position, MapField field) {
        this.fields[position.x][position.y] = field;
    }

    public void placeUnit(Unit unit, MapPosition position) {
        this.fields[position.x][position.y].setUnit(unit);
    }

    public MapField getField(MapPosition position) {
        return this.fields[position.x][position.y];
    }

    public LinkedList<MapPosition> getPossibleMoves(MapPosition position) {
        return null;
    }

    public void fieldLeftClicked(MapPosition position) {
        this.selectedPosition = position;
        this.highlightedPositions.clear();

        Unit unit = this.getField(position).getUnit();
        if (unit != null)
            this.highlightedPositions.addAll(unit.getPossibleMoves(position).keySet());
    }

    public void fieldRightClicked(MapPosition position) {
        Unit selectedUnit = this.getField(selectedPosition).getUnit();
        if (selectedUnit != null) {
            if (selectedUnit.canMoveTo(position)) {
                this.move(selectedPosition, position);
                this.fieldLeftClicked(position);
            }
        }
    }

    private void move(MapPosition pos1, MapPosition pos2) {
        Unit unit = this.getField(pos1).getUnit();
        this.getField(pos1).setUnit(null);
        this.getField(pos2).setUnit(unit);
    }
}
