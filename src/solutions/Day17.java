package solutions;

import helpers.Coordinates;
import helpers.IntcodeProcessor;

import java.util.*;

public class Day17 {
    public int part1() {
        IntcodeProcessor proc = new IntcodeProcessor("day17.txt");
        proc.run();
        List<Long> output = proc.takeAllOutput();
        List<String> rows = Arrays.stream(output.stream().map(l -> (char) l.intValue()).map(c -> Character.toString(c)).reduce("", String::concat).split("\n")).toList();
        Map<Coordinates, String> tiles = new HashMap<>();
        for (int x = 0; x < rows.size(); x++) {
            String row = rows.get(x);
            int y = 0;
            for (String c : row.split("")) {
                tiles.put(new Coordinates(x, y), c);
                y++;
            }
        }
        List<Coordinates> intersections = findIntersections(tiles);
        int sum = 0;
        for (Coordinates c : intersections) {
            sum += c.x * c.y;
        }
        return sum;
    }

    private List<Coordinates> findIntersections(Map<Coordinates, String> tiles) {
        return tiles.keySet().stream()
                .filter(k -> Objects.equals(tiles.get(k), "#"))
                .filter(k -> isIntersection(tiles, k))
                .toList();
    }

    private boolean isIntersection(Map<Coordinates, String> tiles, Coordinates coordinates) {
        int surroundingScaffolding = 0;
        if (tiles.getOrDefault(new Coordinates(coordinates.x - 1, coordinates.y), "").equals("#")) {
            surroundingScaffolding++;
        }
        if (tiles.getOrDefault(new Coordinates(coordinates.x + 1, coordinates.y), "").equals("#")) {
            surroundingScaffolding++;
        }
        if (tiles.getOrDefault(new Coordinates(coordinates.x, coordinates.y - 1), "").equals("#")) {
            surroundingScaffolding++;
        }
        if (tiles.getOrDefault(new Coordinates(coordinates.x, coordinates.y + 1), "").equals("#")) {
            surroundingScaffolding++;
        }
        return surroundingScaffolding > 2;
    }
}
