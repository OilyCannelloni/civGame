package gui;

import civ.*;
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
            mapRenderBoundingBox,
            canvasBoundingBox = new Rect2D(0, 0, this.getWidth(), this.getHeight());

    private Vector2D origin;

    public GraphicsContext ctx;

    public WorldMap map;

    public GridCanvas(int x, int y) {
        super(x, y);
        this.setWidth(x);
        this.setHeight(y);
        this.r = 50;
        this.origin = new Vector2D(0, 0);
        this.map = MapGenerator.generateRandomMap(50, 50);
        this.mapRenderBoundingBox = new Rect2D(
                new Vector2D(0, 0),
                this.toAbsoluteGridXY(this.map.getLowerRight())
                        .subtract(new Vector2D(this.getWidth(), this.getHeight()))
                        .add(new Vector2D(2*r, 2*r))
        );

        this.setFocusTraversable(true);

        ctx = this.getGraphicsContext2D();
        this.setOnKeyPressed(this::onKeyPressed);


    }

    public void drawIcon(CanvasIcon icon, Vector2D position) {
        Vector2D finalXY = position.add(icon.getOffset());
        this.ctx.drawImage(icon, finalXY.x, finalXY.y);
    }

    public void drawField(IMapField field) {
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
        // grid lines
        Vector2D offset = this.getGridOffset(renderBaseXY);
        System.out.println(offset);
        this.drawGridLines(offset);

        // fields
        MapRect renderedRect = this.getRenderedMapRect();
        for (int x = renderedRect.upperLeft.x; x <= renderedRect.lowerRight.x; x++) {
            for (int y = renderedRect.upperLeft.y; y <= renderedRect.lowerRight.y; y++) {
                IMapField field = this.map.getField(new MapPosition(x, y));
                this.drawField(field);
            }
        }
    }

    private MapRect getRenderedMapRect() {
        int yMin = Math.max((int) (this.origin.y / (r * sqrt3)) - 1, 0);
        int yMax = Math.min((int) ((this.origin.y + this.getHeight()) / (r * sqrt3)), this.map.getHeight() - 1);
        int xMin = Math.max((int) (this.origin.x / (r * 3 / 2)) - 1, 0);
        int xMax = Math.min((int) ((this.origin.x + this.getWidth())/ (r * 3 / 2)), this.map.getWidth() - 1);
        return new MapRect(xMin, yMin, xMax, yMax);
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
