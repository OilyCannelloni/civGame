package civ;

public class MapRect {
    public MapPosition upperLeft;
    public MapPosition lowerRight;

    public MapRect(MapPosition ul, MapPosition lr){
        this.upperLeft = ul;
        this.lowerRight = lr;
    }

    public MapRect(int x1, int y1, int x2, int y2) {
        this.upperLeft = new MapPosition(x1, y1);
        this.lowerRight = new MapPosition(x2, y2);
    }

    public boolean contains(MapPosition point) {
        return point.follows(this.upperLeft) && point.precedes(this.lowerRight);
    }

    public boolean contains(MapRect rect) {
        return rect.upperLeft.follows(this.upperLeft) && rect.lowerRight.precedes(this.lowerRight);
    }

    public MapPosition getDimensions() {
        return new MapPosition(lowerRight.x - upperLeft.x + 1, lowerRight.y - upperLeft.y + 1);
    }

    public boolean equals(Object other){
        if (this == other) return true;
        if (! (other instanceof MapRect)) return false;
        MapRect that = (MapRect) other;
        return this.upperLeft.equals(that.upperLeft) && this.lowerRight.equals(that.lowerRight);
    }

    public String toString() {
        return this.upperLeft.toString() + " " + this.lowerRight.toString();
    }
}