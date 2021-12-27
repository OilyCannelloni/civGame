package gui;

import civ.MapField;
import civ.MapPosition;
import civ.Rect2D;
import civ.Vector2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;


public class GridCanvas extends Canvas {
    private static final double sqrt3 = 1.7320508;
    private double r;
    private final int
            tileBorderLineWidth = 3;

    private final double
            moveDistance = 30;

    private final Color tileBorderColor = Color.ANTIQUEWHITE;

    private final Rect2D
            mapRenderBoundingBox = new Rect2D(0, 0, 3000, 2000);

    private Vector2D origin;

    public GraphicsContext ctx;

    public GridCanvas(int x, int y) {
        super(x, y);
        this.setWidth(x);
        this.setHeight(y);
        this.r = 50;
        this.origin = new Vector2D(0, 0);

        this.setFocusTraversable(true);

        ctx = this.getGraphicsContext2D();
        this.setOnKeyPressed(this::onKeyPressed);
    }

    public void drawIcon(CanvasIcon icon, Vector2D position) {
        Vector2D finalXY = position.add(icon.getOffset());
        this.ctx.drawImage(icon, finalXY.x, finalXY.y);
    }

    public void drawField(MapField field) {
        MapPosition mapPosition = field.getPosition();
        Vector2D gridFieldOrigin = this.positionToGridXY(mapPosition, this.origin);
        assert field.getTerrain().getIcon() != null;
        this.drawIcon(field.getTerrain().getIcon(), gridFieldOrigin);
    }


    private void onKeyPressed(KeyEvent keyEvent) {
        System.out.println(keyEvent.getCode());
        Vector2D delta;
        switch (keyEvent.getCode()) {
            case DOWN:
                delta = new Vector2D(0, moveDistance);
                break;
            case LEFT:
                delta = new Vector2D(-moveDistance, 0);
                break;
            case UP:
                delta = new Vector2D(0, -moveDistance);
                break;
            case RIGHT:
                delta = new Vector2D(moveDistance, 0);
                break;
            default:
                delta = new Vector2D(0, 0);
        }
        this.origin = this.origin.add(delta).trimInside(mapRenderBoundingBox);
        this.render(origin);
    }

    public void init() {
        ctx.setFill(Color.DARKGREY);
    }

    private void clear() {
        this.ctx.clearRect(0, 0, this.getWidth(), this.getHeight());
    }

    public void render(Vector2D renderBaseXY) {
        this.clear();
        Vector2D offset = this.getGridOffset(renderBaseXY);
        System.out.println(offset);
        this.drawGridLines(offset);
    } 
    
    private Vector2D getGridOffset(Vector2D renderBaseXY) {
        return new Vector2D(
                renderBaseXY.x % (3*r),
                renderBaseXY.y % (r * sqrt3)
        ).opposite();
    }

    public Vector2D positionToGridXY(MapPosition position, Vector2D renderBaseXY) {
        Vector2D absolute = this.toAbsoluteGridXY(position);
        return absolute.subtract(renderBaseXY);
    }

    private Vector2D toAbsoluteGridXY(MapPosition position) {
        return new Vector2D(
                ((double) position.x) * 3 / 2 * r,
                position.x % 2 == 0 ? position.y * r * sqrt3 : position.y * r * sqrt3 + r * sqrt3 / 2
        );
    }
    
    public void drawGridLines(Vector2D offset) {
        ctx.setStroke(tileBorderColor);
        ctx.setLineWidth(tileBorderLineWidth);
        double height = this.getHeight(), width = this.getWidth();

        // horizontal
        double y0 = offset.y, x0;
        boolean shiftedRight = false;
        while (y0 < height + 2*r) {
            if (shiftedRight) x0 = 2*r + offset.x;
            else x0 = r/2 + offset.x;
            shiftedRight = !shiftedRight;

            while (x0 < width + 2*r) {
                ctx.strokeLine(x0, y0, x0 + r, y0);
                x0 += 3*r;
            }
            y0 += r * sqrt3 / 2;
        }

        // r-vertical
        x0 = offset.x;
        boolean shiftedDown = true;
        while (x0 < width + 2*r) {
            if (shiftedDown) y0 = r*sqrt3/2 + offset.y;
            else y0 = offset.y;
            while (y0 < height + 2*r) {
                ctx.strokeLine(x0, y0, x0 + r / 2, y0 + r*sqrt3 / 2);
                y0 += r * sqrt3;
            }
            shiftedDown = !shiftedDown;
            x0 += 1.5 * r;
        }

        // l-vertical
        x0 = r / 2 + offset.x;
        shiftedDown = false;
        while (x0 < width + 2*r) {
            if (shiftedDown) y0 = r*sqrt3/2 + offset.y;
            else y0 = offset.y;
            while (y0 < height + 2*r) {
                ctx.strokeLine(x0, y0, x0 - r / 2, y0 + r*sqrt3/2);
                y0 += r * sqrt3;
            }
            shiftedDown = !shiftedDown;
            x0 += 1.5*r;
        }
    }
}
