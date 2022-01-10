package gui;

import civ.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class CanvasIcon extends Image {
    private Vector2D offset;

    public CanvasIcon(String path) {
        this(path, new Vector2D(0, 0));
    }

    public CanvasIcon(String path, Vector2D drawOffset) {
        super(path);
        this.offset = drawOffset;
    }

    public void draw(GraphicsContext ctx, Vector2D position) {
        Vector2D finalXY = position.add(offset);
        ctx.drawImage(this, finalXY.x, finalXY.y);
    }

    public void setOffset(Vector2D offset) {
        this.offset = offset;
    }

    public Vector2D getOffset() {
        return this.offset;
    }
}

