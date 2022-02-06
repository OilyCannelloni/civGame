package gui;

import civ.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GridCanvas extends Canvas {
    private static final double sqrt3 = 1.7320508;
    private double r;
    private final int
            tileBorderLineWidth = 3;

    private final double
            moveDistance = 15;

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
        this.setOnMousePressed(this::onMouseClick);
    }

    public void drawIcon(CanvasIcon icon, Vector2D position) {
        Vector2D finalXY = position.add(icon.getOffset());
        this.ctx.drawImage(icon, finalXY.x, finalXY.y);
    }

    public void drawField(IMapField field) {
        MapPosition mapPosition = field.getPosition();
        Vector2D gridFieldOrigin = this.positionToGridXY(mapPosition);
        assert field.getTerrain().getIcon() != null;
        this.drawIcon(field.getTerrain().getIcon(), gridFieldOrigin);
        Unit unit = field.getUnit();
        if (unit != null) {
            this.drawIcon(unit.getIcon(), gridFieldOrigin);
            HPBar bar = new HPBar(unit.getHP(), unit.getMaxHP());
            bar.draw(ctx, gridFieldOrigin);
        }

    }

    private void onMouseClick(MouseEvent event) {
        double x = event.getX(), y = event.getY();
        MapPosition clickedHex = this.canvasXYToPosition(new Vector2D(x, y));

        if (event.isPrimaryButtonDown()) this.map.fieldLeftClicked(clickedHex);
        else if (event.isSecondaryButtonDown()) this.map.fieldRightClicked(clickedHex);

        this.render();
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        Vector2D delta;
        switch (keyEvent.getCode()) {
            case S:
                delta = new Vector2D(0, moveDistance);
                break;
            case A:
                delta = new Vector2D(-moveDistance, 0);
                break;
            case W:
                delta = new Vector2D(0, -moveDistance);
                break;
            case D:
                delta = new Vector2D(moveDistance, 0);
                break;
            default:
                delta = new Vector2D(0, 0);
        }
        this.origin = this.origin.add(delta).trimInside(mapRenderBoundingBox);
        this.render();
    }

    public void init() {
        ctx.setFill(new Color(0.1, 0.1, 0.1, 1));
        ctx.fillRect(0, 0, this.canvasBoundingBox.lowerRight.x, this.canvasBoundingBox.lowerRight.y);
    }

    private void clear() {
        this.ctx.clearRect(0, 0, this.getWidth(), this.getHeight());
    }

    public void render() {
        this.clear();
        this.init();
        // grid lines
        Vector2D offset = this.getGridOffset(this.origin);
        this.drawGridLines(offset);

        // highlights
        this.drawHexOutline(
                map.getSelectedPosition(),
                3,
                Color.YELLOW,
                Color.color(1, 1, 0, 0.2)
        );
        for (MapPosition position: this.map.getHighlightedPositions()) this.drawHexOutline(
                position,
                2,
                Color.PINK,
                Color.color(1, 0, 0, 0.2)
        );

        // fields
        MapRect renderedRect = this.getRenderedMapRect();
        for (int x = renderedRect.upperLeft.x; x <= renderedRect.lowerRight.x; x++) {
            for (int y = renderedRect.upperLeft.y; y <= renderedRect.lowerRight.y; y++) {
                IMapField field = this.map.getField(new MapPosition(x, y));
                this.drawField(field);
            }
        }
    }

    private void drawHexOutline(MapPosition position, int thickness, Color color, Color fill) {
        if (position == null) return;
        Vector2D hexOrigin = this.positionToGridXY(position);
        this.ctx.beginPath();
        this.ctx.moveTo(hexOrigin.x + r / 2, hexOrigin.y);
        this.ctx.lineTo(hexOrigin.x + r * 3 / 2, hexOrigin.y);
        this.ctx.lineTo(hexOrigin.x + 2*r, hexOrigin.y + r * sqrt3 / 2);
        this.ctx.lineTo(hexOrigin.x + r * 3 / 2, hexOrigin.y + r * sqrt3);
        this.ctx.lineTo(hexOrigin.x + r / 2, hexOrigin.y + r * sqrt3);
        this.ctx.lineTo(hexOrigin.x, hexOrigin.y + r * sqrt3 / 2);
        this.ctx.closePath();
        this.ctx.setStroke(color);
        this.ctx.setFill(fill);
        this.ctx.setLineWidth(thickness);
        this.ctx.stroke();
        this.ctx.fill();
        this.ctx.setFill(null);
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

    public Vector2D positionToGridXY(MapPosition position) {
        Vector2D absolute = this.toAbsoluteGridXY(position);
        return absolute.subtract(this.origin);
    }

    public MapPosition canvasXYToPosition(Vector2D gridXY) {
        Vector2D absolute = gridXY.add(origin);
        MapPosition strip = new MapPosition(
                ((int) (absolute.x / (3 * r))) * 2,
                (int) (absolute.y / (r * sqrt3))
        );
        MapPosition[] CHECKED_HEXES = {
                strip,
                strip.point(MapDirection.LEFT_UP),
                strip.point(MapDirection.LEFT_DOWN),
                strip.point(MapDirection.RIGHT_UP),
                strip.point(MapDirection.RIGHT_DOWN)
        };
        for (MapPosition hex : CHECKED_HEXES) {
            if (this.gridXYInHex(absolute, hex)) return hex;
        }
        return new MapPosition(0, 0);
    }

    private boolean gridXYInHex(Vector2D gridXY, MapPosition hex) {
        Vector2D hexOrigin = this.toAbsoluteGridXY(hex);
        Vector2D relative = gridXY.subtract(hexOrigin);
        if (relative.y < -sqrt3 * relative.x + r * sqrt3 / 2) return false;
        if (relative.y < 0) return false;
        if (relative.y < sqrt3 * relative.x - r * 3 / 2 * sqrt3) return false;
        if (relative.y > -sqrt3 * relative.x + r * 5 / 2 * sqrt3) return false;
        if (relative.y > r * sqrt3) return false;
        //noinspection RedundantIfStatement
        if (relative.y > sqrt3 * relative.x + r * sqrt3 / 2) return false;
        return true;
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
