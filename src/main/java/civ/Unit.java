package civ;

import gui.CanvasIcon;

public abstract class Unit implements IMapElement {
    private int HP, maxMove, attack, remainingMove;

    public CanvasIcon getIcon() {
        return null;
    }

    public Unit(int hp, int attack, int maxMove) {
        this.HP = hp;
        this.attack = attack;
        this.maxMove = maxMove;
    }


}
