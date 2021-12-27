package civ;

import gui.CanvasIcon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public enum Terrain {
    PLAINS,
    HILLS,
    MOUNTAINS,
    WATER;

    CanvasIcon PLAINS_IMG = new CanvasIcon("file:.\\src\\main\\resources\\plains.png", new Vector2D(25, 65));
    CanvasIcon HILLS_IMG = new CanvasIcon("file:.\\src\\main\\resources\\hills.png", new Vector2D(28, 65));
    CanvasIcon MOUNTAINS_IMG = new CanvasIcon("file:.\\src\\main\\resources\\mountains.png", new Vector2D(25, 65));
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
}
