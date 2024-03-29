package civ;

import gui.CanvasIcon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public enum Terrain {
    PLAINS,
    HILLS,
    MOUNTAINS,
    WATER;

    CanvasIcon PLAINS_IMG = new CanvasIcon("file:.\\src\\main\\resources\\plains.png", new Vector2D(25, 65));
    CanvasIcon HILLS_IMG = new CanvasIcon("file:.\\src\\main\\resources\\hills.png", new Vector2D(28, 65));
    CanvasIcon MOUNTAINS_IMG = new CanvasIcon("file:.\\src\\main\\resources\\mountains.png", new Vector2D(25, 54));
    CanvasIcon WATER_IMG = new CanvasIcon("file:.\\src\\main\\resources\\water.png", new Vector2D(25, 65));


    public void draw(GraphicsContext ctx, Vector2D origin) {
        switch (this) {
            case HILLS:
                ctx.setStroke(Color.ORANGE);
                ctx.setLineWidth(2);
                ctx.appendSVGPath("M 25 93.554 C 34.031 75.689 46.281 80.184 53.115 94.001 M 49.09 88.055 C 59.002 77.39 69.026 82.825 73.273 93.695");
                ctx.stroke();
                break;
            case WATER:
                WATER_IMG.draw(ctx, origin);
                break;
        }
    }

    public CanvasIcon getIcon() {
        switch (this) {
            case PLAINS:
                return PLAINS_IMG;
            case HILLS:
                return HILLS_IMG;
            case MOUNTAINS:
                return MOUNTAINS_IMG;
            case WATER:
                return WATER_IMG;
            default:
                return PLAINS_IMG;
        }
    }

    public static Terrain getRandom() {
        return Terrain.values()[Algorithm.random.nextInt(Terrain.values().length)];
    }

    public double getAttackModifier() {
        switch (this) {
            case WATER: return 0.9;
            case MOUNTAINS: return 0;
            case HILLS: return 1.1;
            case PLAINS: return 1;
            default: return 1;
        }
    }

    public double getDefenceModifier() {
        switch (this) {
            case WATER: return 0.5;
            case MOUNTAINS: return 2;
            case HILLS: return 1.3;
            case PLAINS: return 1;
            default: return 1;
        }
    }
}
