package civ;

import gui.CanvasIcon;

public abstract class Unit implements IMapElement {
    private int HP, maxMove, attack, remainingMove;

    public Unit(int hp, int attack, int maxMove) {
        this.HP = hp;
        this.attack = attack;
        this.maxMove = maxMove;
    }

    public CanvasIcon getIcon() {
        return null;
    }

    public boolean canMoveTo(MapPosition position) {
        return false;
    }

}
