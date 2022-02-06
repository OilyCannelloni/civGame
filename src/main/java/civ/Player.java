package civ;

import java.util.LinkedList;

public class Player {
    int priority;
    PlayerColor color;
    private LinkedList<Unit> units;

    public Player(PlayerColor color, int priority) {
        this.color = color;
        this.priority = priority;
        this.units = new LinkedList<>();
    }

    public void addUnit(Unit unit) {
        this.units.add(unit);
    }

    public void removeUnit(Unit unit) {
        this.units.remove(unit);
    }

    public void refreshUnits() {
        for (Unit unit : this.units) {
            unit.refresh();
        }
    }

    public PlayerColor getColor() {
        return this.color;
    }
}
