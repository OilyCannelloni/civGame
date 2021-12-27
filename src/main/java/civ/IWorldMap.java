package civ;

import java.util.LinkedList;

public interface IWorldMap {
    void placeField(MapPosition position, IMapField field);
    IMapField getField(MapPosition position);
    LinkedList<MapPosition> getPossibleMoves(MapPosition position);
}
