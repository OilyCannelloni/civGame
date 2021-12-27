package civ;

import gui.CanvasIcon;

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

        map.placeUnit(new Programmer(), new MapPosition(3, 3));

        CanvasIcon icon = map.getField(new MapPosition(3, 3)).getUnit().getIcon();

        return map;
    }
}
