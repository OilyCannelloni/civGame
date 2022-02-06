package civ;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

public class WorldMap {
    private int width, height;
    private MapField[][] fields;
    private LinkedList<MapPosition> highlightedPositions;
    private MapPosition selectedPosition;
    private MapRect boundingBox;
    private LinkedList<Player> players;
    private Player playerAtTurn;

    public WorldMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.fields = new MapField[width][height];
        this.selectedPosition = null;
        this.highlightedPositions = new LinkedList<>();
        this.boundingBox = new MapRect(0, 0, width - 1, height - 1);
        this.players = new LinkedList<>();
    }

    public Player nextTurn() {
        playerAtTurn.refreshUnits();
        ListIterator<Player> playerIterator = this.players.listIterator();
        while (playerIterator.next() != playerAtTurn);
        if (playerIterator.hasNext()) {
            this.playerAtTurn = playerIterator.next();
        } else {
            this.playerAtTurn = this.players.getFirst();
        }
        return this.playerAtTurn;
    }

    public void initiateGame() {
        this.playerAtTurn = players.getFirst();
        if (this.playerAtTurn == null) throw new InvalidParameterException("There are no players defined!");
        System.out.println("Game initiated");
        System.out.println("Player " + playerAtTurn.getColor() + "'s turn");
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
    }

    public boolean isOutsideBounds(MapPosition position) {
        return !this.boundingBox.contains(position);
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
        if (unit != null && unit.getPlayer() == this.playerAtTurn) {
            this.highlightedPositions.addAll(unit.getPossibleMoves().keySet());
            this.highlightedPositions.addAll(unit.getPossibleAttacks());
        }
    }

    public void fieldRightClicked(MapPosition position) {
        Unit selectedUnit = this.getField(selectedPosition).getUnit();
        if (selectedUnit != null) {
            HashMap<MapPosition, Integer> unitPossibleMoves = selectedUnit.getPossibleMoves();
            if (selectedUnit.canMoveTo(position) && unitPossibleMoves.containsKey(position)) {
                this.move(selectedPosition, position, unitPossibleMoves.get(position));
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


        int newAttackerHP = (int) (attacker.getHP() -
                defender.getAttack() * defenderField.getTerrain().getDefenceModifier());
        int newDefenderHP = (int) (defender.getHP() -
                attacker.getAttack() * defenderField.getTerrain().getAttackModifier());

        if (newAttackerHP > 0 && newDefenderHP > 0) {
            attacker.setHP(newAttackerHP);
            defender.setHP(newDefenderHP);
            attacker.afterAttack();
        } else if (newAttackerHP > 0) {
            attacker.setHP(newAttackerHP);
            defender.onDeath(attacker);
            defenderField.setUnit(null);
            move(pos1, pos2);
            attacker.afterAttack();
        } else if (newDefenderHP > 0) {
            defender.setHP(newDefenderHP);
            attacker.onDeath(defender);
            attackerField.setUnit(null);
            defender.afterDefence();
        } else if (newAttackerHP > newDefenderHP) {
            attacker.setHP(1);
            defender.onDeath(attacker);
            defenderField.setUnit(null);
            move(pos1, pos2);
            attacker.afterAttack();
        } else {
            defender.setHP(1);
            attacker.onDeath(defender);
            attackerField.setUnit(null);
            defender.afterDefence();
        }
    }

    private void move(MapPosition pos1, MapPosition pos2) {
        Unit unit = this.getField(pos1).getUnit();
        this.getField(pos1).setUnit(null);
        this.getField(pos2).setUnit(unit);
        unit.moveHappened(pos1, pos2, 1000);
    }

    private void move(MapPosition pos1, MapPosition pos2, int cost) {
        Unit unit = this.getField(pos1).getUnit();
        this.getField(pos1).setUnit(null);
        this.getField(pos2).setUnit(unit);
        unit.moveHappened(pos1, pos2, cost);
    }
}
