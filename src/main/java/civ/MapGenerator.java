package civ;

import java.security.InvalidParameterException;

public class MapGenerator {
    public static WorldMap generateRandomMap(int width, int height) {
        WorldMap map = new WorldMap(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int r = Algorithm.random.nextInt(100);
                Terrain terrain;
                if (r < 10) terrain = Terrain.MOUNTAINS;
                else if (r < 30) terrain = Terrain.WATER;
                else if (r < 60) terrain = Terrain.HILLS;
                else terrain = Terrain.PLAINS;

                map.placeField(new MapPosition(x, y), new MapField(
                        new MapPosition(x, y),
                        terrain
                ));
            }
        }

        Player blue = new Player(PlayerColor.BLUE, 0);
        Player red = new Player(PlayerColor.RED, 0);

        map.addPlayer(blue);
        map.addPlayer(red);

        for (int i = 0; i < 6; i++) {
            MapPosition position = map.getRandomEmptyField(new MapPosition(3, 3), new MapPosition(6, 10));
            map.placeUnit(new Programmer(map, blue, position));
        }

        for (int i = 0; i < 6; i++) {
            MapPosition position = map.getRandomEmptyField(new MapPosition(10, 3), new MapPosition(13, 10));
            map.placeUnit(new Programmer(map, red, position));
        }

        try {
            map.initiateGame();
        } catch (InvalidParameterException e){
            System.out.println("Invalid player parameters!");
        }

        return map;
    }
}
