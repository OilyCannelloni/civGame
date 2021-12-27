package civ;

import javafx.scene.canvas.GraphicsContext;

import java.util.LinkedList;

public class MapField implements IMapField {
    private Terrain terrain;
    private MapPosition position;
    private Unit unit;

    public MapField(
            MapPosition position,
            Terrain terrain
    ) {
        this.terrain = terrain;
        this.position = position;
    }

    @Override
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public Unit getUnit() {
        return this.unit;
    }

    @Override
    public Terrain getTerrain() {
        return this.terrain;
    }

    public void draw(GraphicsContext ctx, Vector2D origin) {
        this.terrain.draw(ctx, origin);
    }

    public MapPosition getPosition() {
        return this.position;
    }
}
