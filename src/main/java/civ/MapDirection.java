package civ;

public enum MapDirection {
    UP,
    RIGHT_UP,
    RIGHT_DOWN,
    DOWN,
    LEFT_DOWN,
    LEFT_UP;

    public MapPosition delta(MapPosition position) {
        if (position.x % 2 == 0) {
            switch (this) {
                case UP:
                    return new MapPosition(0, -1);
                case RIGHT_UP:
                    return new MapPosition(1, -1);
                case RIGHT_DOWN:
                    return new MapPosition(1, 0);
                case DOWN:
                    return new MapPosition(0, 1);
                case LEFT_DOWN:
                    return new MapPosition(-1, 0);
                case LEFT_UP:
                    return new MapPosition(-1, -1);
            }
        } else {
            switch (this) {
                case UP:
                    return new MapPosition(0, -1);
                case RIGHT_UP:
                    return new MapPosition(1, 0);
                case RIGHT_DOWN:
                    return new MapPosition(1, 1);
                case DOWN:
                    return new MapPosition(0, 1);
                case LEFT_DOWN:
                    return new MapPosition(-1, 1);
                case LEFT_UP:
                    return new MapPosition(-1, 0);
            }
        }
        return new MapPosition(0, 0);
    }
}
