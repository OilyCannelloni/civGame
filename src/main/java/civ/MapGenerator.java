package civ;

public class MapGenerator {
    public static WorldMap generateRandomMap(int width, int height) {
        WorldMap map = new WorldMap(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map.placeField(new MapPosition(x, y), new MapField(
                        new MapPosition(x, y),
                        Terrain.getRandom()
                ));
            }
        }
        return map;
    }
}
