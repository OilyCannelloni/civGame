package civ;

import java.security.InvalidParameterException;

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

        Player blue = new Player(PlayerColor.BLUE, 0);
        Player red = new Player(PlayerColor.GREEN, 0);

        map.addPlayer(blue);
        map.addPlayer(red);

        map.placeUnit(new Programmer(map, blue, new MapPosition(3, 3)));
        map.placeUnit(new Programmer(map, red, new MapPosition(6, 5)));

        try {
            map.initiateGame();
        } catch (InvalidParameterException e){
            System.out.println("Invalid player parameters!");
        }

        return map;
    }
}
