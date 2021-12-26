package civ;

public class Rect2D {
    public Vector2D upperLeft;
    public Vector2D lowerRight;

    public Rect2D(Vector2D ul, Vector2D lr){
        this.upperLeft = ul;
        this.lowerRight = lr;
    }

    public Rect2D(double x1, double y1, double x2, double y2) {
        this.upperLeft = new Vector2D(x1, y1);
        this.lowerRight = new Vector2D(x2, y2);
    }

    public boolean contains(Vector2D point) {
        return point.follows(this.upperLeft) && point.precedes(this.lowerRight);
    }

    public boolean contains(Rect2D rect) {
        return rect.upperLeft.follows(this.upperLeft) && rect.lowerRight.precedes(this.lowerRight);
    }

    public Vector2D getDimensions() {
        return new Vector2D(lowerRight.x - upperLeft.x + 1, lowerRight.y - upperLeft.y + 1);
    }

    public boolean equals(Object other){
        if (this == other) return true;
        if (! (other instanceof Rect2D)) return false;
        Rect2D that = (Rect2D) other;
        return this.upperLeft.equals(that.upperLeft) && this.lowerRight.equals(that.lowerRight);
    }

    public String toString() {
        return this.upperLeft.toString() + " " + this.lowerRight.toString();
    }
}

