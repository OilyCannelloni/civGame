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

    public int getMovementCost(Terrain terrain) {
        switch (terrain) {
            case MOUNTAINS:
            case WATER:
                return 1000;
            case HILLS:
                return 2;
            case PLAINS:
                return 1;
            default:
                return 0;
        }
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

        do {
            LinkedHashMap<MapPosition, Integer> newMoves = new LinkedHashMap<>();

            for (MapPosition reached : oldNewMoves.keySet()) {

                for (MapPosition adjacent : reached.adjacent()) {
                    if (this.canMoveTo(adjacent)) {
                        int moveInCost = this.getMovementCost(this.map.getField(adjacent).getTerrain());

                        if (!possibleMoves.containsKey(adjacent)) {
                            int costAfterMove = possibleMoves.get(reached) - moveInCost;
                            possibleMoves.put(adjacent, costAfterMove);
                            if (costAfterMove >= 1) {
                                newMoves.put(adjacent, costAfterMove);
                            }
                        } else {
                            int remainingMove = possibleMoves.get(adjacent);
                            int newRemainingMove = possibleMoves.get(reached) - moveInCost;
                            if (newRemainingMove > remainingMove) {
                                possibleMoves.put(adjacent, newRemainingMove);
                                newMoves.put(adjacent, newRemainingMove);
                            }
                        }
                    }
                }
            }
            oldNewMoves = newMoves;
        } while (!oldNewMoves.isEmpty());

        return possibleMoves;
    }
}
