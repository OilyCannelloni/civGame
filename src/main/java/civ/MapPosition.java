package civ;

import java.util.Map;
import java.util.Objects;


public class MapPosition {
    public final int x, y;
    public MapPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return String.format("(%d,%d)", this.x, this.y);
    }

    public boolean precedes(MapPosition other){
        return this.x <= other.x && this.y <= other.y;
    }

    public boolean follows(MapPosition other){
        return this.x >= other.x && this.y >= other.y;
    }

    public MapPosition upperRight(MapPosition other){
        return new MapPosition(
                Math.max(this.x, other.x),
                Math.max(this.y, other.y)
        );
    }

    public MapPosition lowerLeft(MapPosition other){
        return new MapPosition(
                Math.min(this.x, other.x),
                Math.min(this.y, other.y)
        );
    }

    public MapPosition add(MapPosition other){
        return new MapPosition(this.x + other.x, this.y + other.y);
    }

    public MapPosition subtract(MapPosition other){
        return new MapPosition(this.x - other.x, this.y - other.y);
    }

    public boolean equals(Object other){
        if (this == other) return true;
        if (! (other instanceof MapPosition)) return false;
        MapPosition that = (MapPosition) other;
        return this.x == that.x && this.y == that.y;
    }

    public MapPosition point(MapDirection dir) {
        return this.add(dir.delta(this));
    }

    public MapPosition opposite(){
        return new MapPosition(-this.x, -this.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}
