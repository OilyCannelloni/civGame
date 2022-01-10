package civ;

import gui.CanvasIcon;
import javafx.scene.canvas.GraphicsContext;

import java.util.LinkedHashMap;
import java.util.LinkedList;


public abstract class Unit implements IMapElement {
    protected static String IMG_PATH = "file:.\\src\\main\\resources\\";
    protected static Vector2D UNIT_ICON_OFFSET = new Vector2D(28, 25);

    private int HP, maxHP, maxMove, attack, remainingMove;
    private LinkedList<Terrain> impassableTerrain;
    protected WorldMap map;
    protected Player player;
    protected String name = "";

    public Unit(WorldMap map, Player player, int hp, int maxHP, int attack, int maxMove) {
        this.map = map;
        this.player = player;
        this.HP = hp;
        this.maxHP = maxHP;
        this.attack = attack;
        this.maxMove = maxMove;
        this.remainingMove = maxMove;
        this.impassableTerrain = new LinkedList<>();
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getHP() {
        return HP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public CanvasIcon getIcon() {
        return new CanvasIcon(IMG_PATH + this.name + "_" + this.player.color + ".png", UNIT_ICON_OFFSET);
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
