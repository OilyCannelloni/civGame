package civ;

import javafx.scene.canvas.GraphicsContext;

import java.util.Map;

public class MapField implements IMapField {
    private Terrain terrain;
    private MapPosition position;

    public MapField(
            MapPosition position,
            Terrain terrain
    ) {
        this.terrain = terrain;
        this.position = position;
    }

    @Override
    public void addUnit(IUnit unit) {

    }

    @Override
    public IUnit getUnit() {
        return null;
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
