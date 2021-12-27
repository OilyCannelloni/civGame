package civ;

import java.util.Objects;


public class Vector2D {
    public final double x, y;
    public Vector2D(double x, double y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return String.format("(%.2f,%.2f)", this.x, this.y);
    }

    public boolean precedes(Vector2D other){
        return this.x <= other.x && this.y <= other.y;
    }

    public boolean follows(Vector2D other){
        return this.x >= other.x && this.y >= other.y;
    }

    public Vector2D upperRight(Vector2D other){
        return new Vector2D(
                Math.max(this.x, other.x),
                Math.max(this.y, other.y)
        );
    }

    public Vector2D lowerLeft(Vector2D other){
        return new Vector2D(
                Math.min(this.x, other.x),
                Math.min(this.y, other.y)
        );
    }

    public Vector2D add(Vector2D other){
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    public Vector2D subtract(Vector2D other){
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    public boolean equals(Object other){
        if (this == other) return true;
        if (! (other instanceof Vector2D)) return false;
        Vector2D that = (Vector2D) other;
        return this.x == that.x && this.y == that.y;
    }

    public Vector2D opposite(){
        return new Vector2D(-this.x, -this.y);
    }

    public double angle() {
        if (this.x == 0 && this.y > 0) return Double.MAX_VALUE;
        if (this.x == 0 && this.y < 0) return Double.MIN_VALUE;
        if (this.x == 0) return 0;
        return this.y / this.x;
    }


    public Vector2D multiplyEach(Vector2D other) {
        return new Vector2D(this.x * other.x, this.y * other.y);
    }

    public Vector2D multiplyEach(int a) {
        return new Vector2D(
                this.x * a,
                this.y * a
        );
    }

    public Vector2D trimInside(Rect2D rect) {
        double x = Math.max(this.x, rect.upperLeft.x);
        x = Math.min(x, rect.lowerRight.x);
        double y = Math.max(this.y, rect.upperLeft.y);
        y = Math.min(y, rect.lowerRight.y);
        return new Vector2D(x, y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}

