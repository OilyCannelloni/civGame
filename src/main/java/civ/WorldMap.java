package civ;

import java.lang.reflect.Field;
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

    public void placeUnit(Unit unit) {
        MapPosition position = unit.getPosition();
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
        if (unit != null) {
            this.highlightedPositions.addAll(unit.getPossibleMoves().keySet());
            this.highlightedPositions.addAll(unit.getPossibleAttacks());
        }

    }

    public void fieldRightClicked(MapPosition position) {
        Unit selectedUnit = this.getField(selectedPosition).getUnit();
        if (selectedUnit != null) {
            if (selectedUnit.canMoveTo(position)) {
                this.move(selectedPosition, position);
                this.fieldLeftClicked(position);
            } else if (selectedUnit.canAttackTo(position)) {
                this.attack(selectedPosition, position);
                this.fieldLeftClicked(selectedUnit.getPosition());
            }
        }
    }

    private void attack(MapPosition pos1, MapPosition pos2) {
        MapField attackerField = this.getField(pos1), defenderField = this.getField(pos2);
        Unit attacker = this.getField(pos1).getUnit(), defender = this.getField(pos2).getUnit();

        System.out.println("--- FIGHT ---");
        System.out.println("att HP: " + attacker.getHP() + "  def HP: " + defender.getHP());

        int newAttackerHP = (int) (attacker.getHP() -
                defender.getAttack() * defenderField.getTerrain().getDefenceModifier());
        int newDefenderHP = (int) (defender.getHP() -
                attacker.getAttack() * defenderField.getTerrain().getAttackModifier());

        System.out.println("att new HP: " + newAttackerHP + "  def new HP: " + newDefenderHP);

        if (newAttackerHP > 0 && newDefenderHP > 0) {
            attacker.setHP(newAttackerHP);
            defender.setHP(newDefenderHP);
            attacker.afterAttack();
        } else if (newAttackerHP > 0) {
            attacker.setHP(newAttackerHP);
            defender.onDeath(attacker);
            defenderField.setUnit(null);
            move(pos1, pos2);
        } else if (newDefenderHP > 0) {
            defender.setHP(newDefenderHP);
            attacker.onDeath(defender);
            attackerField.setUnit(null);
        } else if (newAttackerHP > newDefenderHP) {
            attacker.setHP(1);
            defender.onDeath(attacker);
            defenderField.setUnit(null);
            move(pos1, pos2);
        } else {
            defender.setHP(1);
            attacker.onDeath(defender);
            attackerField.setUnit(null);
        }
    }

    private void move(MapPosition pos1, MapPosition pos2) {
        Unit unit = this.getField(pos1).getUnit();
        this.getField(pos1).setUnit(null);
        this.getField(pos2).setUnit(unit);
        unit.moveHappened(pos1, pos2);
    }
}
