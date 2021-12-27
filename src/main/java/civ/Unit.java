package civ;

import gui.CanvasIcon;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;

public abstract class Unit implements IMapElement {
    private int HP, maxMove, attack, remainingMove;
    private LinkedList<Terrain> impassableTerrain;
    protected WorldMap map;

    public Unit(WorldMap map, int hp, int attack, int maxMove) {
        this.map = map;
        this.HP = hp;
        this.attack = attack;
        this.maxMove = maxMove;
        this.remainingMove = maxMove;
        this.impassableTerrain = new LinkedList<>();
    }

    public CanvasIcon getIcon() {
        return null;
    }

    public boolean canMoveTo(MapPosition position) {
        return this.map.isWithinBounds(position);
    }

    public LinkedHashMap<MapPosition, Integer> getPossibleMoves(MapPosition position) {
        LinkedHashMap<MapPosition, Integer> possibleMoves = new LinkedHashMap<>();

        int m = this.remainingMove;
        possibleMoves.put(position, m);

        LinkedHashMap<MapPosition, Integer> oldNewMoves = new LinkedHashMap<>();
        oldNewMoves.put(position, m);
        while (--m >= 0) {
            LinkedHashMap<MapPosition, Integer> newMoves = new LinkedHashMap<>();
            for (MapPosition reached : oldNewMoves.keySet()) {
                for (MapPosition adjacent : reached.adjacent()) {
                    if (this.canMoveTo(adjacent))
                        newMoves.putIfAbsent(adjacent, m);
                }
            }
            oldNewMoves = newMoves;
            newMoves.putAll(possibleMoves);
            possibleMoves = newMoves;
        }
        return possibleMoves;
    }
}
