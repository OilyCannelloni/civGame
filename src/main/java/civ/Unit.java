package civ;

import gui.CanvasIcon;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;


public abstract class Unit implements IMapElement {
    protected static String IMG_PATH = "file:.\\src\\main\\resources\\";
    protected static Vector2D UNIT_ICON_OFFSET = new Vector2D(28, 25);

    private int HP, maxHP, maxMove, attack, remainingMove;
    private LinkedList<Terrain> impassableTerrain;
    protected WorldMap map;
    protected Player player;
    protected String name = "";
    protected MapPosition position;

    public Unit(WorldMap map, Player player, MapPosition position, int hp, int maxHP, int attack, int maxMove) {
        this.map = map;
        this.player = player;
        this.HP = hp;
        this.maxHP = maxHP;
        this.attack = attack;
        this.maxMove = maxMove;
        this.remainingMove = maxMove;
        this.impassableTerrain = new LinkedList<>();
        this.position = position;
        this.player.addUnit(this);
    }

    public void refresh() {
        if (this.remainingMove > 0) this.HP = Math.min(this.HP + 10, this.maxHP);
        this.remainingMove = this.maxMove;
    }

    public MapPosition getPosition() {
        return this.position;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getAttack() {
        return this.attack;
    }

    public void afterAttack() {
         this.remainingMove = 0;
    }

    public void afterDefence() {

    }

    public void onDeath(Unit killer) {
        System.out.println(this.name + " has been killed by " + killer.name);
        this.player.removeUnit(this);
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
        if (map.isOutsideBounds(position)) return false;
        Unit unit = map.getField(position).getUnit();
        return unit == null;
    }

    public boolean canAttackTo(MapPosition position) {
        if (map.isOutsideBounds(position)) return false;
        if (this.remainingMove <= 0) return false;
        Collection<MapPosition> adjacent = this.position.adjacent();
        if (!adjacent.contains(position)) return false;
        Unit target = map.getField(position).getUnit();
        if (target == null) return false;
        return target.getPlayer().getColor() != this.getPlayer().getColor();
    }

    public LinkedHashSet<MapPosition> getPossibleAttacks() {
        LinkedHashSet<MapPosition> ret = new LinkedHashSet<>();

        if (this.remainingMove <= 0) return ret;

        Collection<MapPosition> adjacent = this.position.adjacent();
        for (MapPosition position : adjacent) {
            Unit unit = this.map.getField(position).getUnit();
            if (unit != null && unit.getPlayer() != this.player) {
                ret.add(position);
            }
        }
        return ret;
    }

    public LinkedHashMap<MapPosition, Integer> getPossibleMoves() {
        LinkedHashMap<MapPosition, Integer> possibleMoves = new LinkedHashMap<>();

        int m = this.remainingMove;
        possibleMoves.put(position, m);

        if (m <= 0) return possibleMoves;

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

    public void moveHappened(MapPosition pos1, MapPosition pos2, int remainingMove) {
        this.position = pos2;
        this.remainingMove = Math.max(remainingMove, -1);
    }
}
