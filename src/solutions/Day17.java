package solutions;

import helpers.Coordinates;
import helpers.IntcodeProcessor;

import java.util.*;

public class Day17 {
    public int part1() {
        IntcodeProcessor proc = new IntcodeProcessor("day17.txt");
        List<Coordinates> intersections = findIntersections(getSurroundings(proc));
        int sum = 0;
        for (Coordinates c : intersections) {
            sum += c.x * c.y;
        }
        return sum;
    }

    public long part2() {
        IntcodeProcessor proc = new IntcodeProcessor("day17.txt");
        proc.setMemoryAt(0, 2);
        proc.run();
        // Clear output
        proc.takeAllOutput();

        // I calculated the functions by hand
        long[] A = new long[]{
                'L', 44, '4', 44, 'L', 44, '6', 44, 'L', 44, '8', 44, 'L', 44, '9', 44, '3', 10
        };
        long[] B = new long[]{
                'R', 44, '9', 44, '3', 44, 'L', 44, '6', 44, 'L', 44, '6', 44, 'L', 44, '8', 10
        };
        long[] C = new long[]{
                'L', 44, '8', 44, 'R', 44, '9', 44, '3', 44, 'L', 44, '9', 44, '3', 10
        };
        long[] mainRoutine = new long[]{
                'A', 44, 'C', 44, 'C', 44, 'A', 44, 'C', 44, 'B', 44, 'A', 44, 'B', 44, 'C', 44, 'B', 10
        };
        proc.addInput(mainRoutine);
        proc.addInput(A);
        proc.addInput(B);
        proc.addInput(C);
        proc.addInput('n');
        proc.addInput(10);
        proc.run();
        List<Long> output = proc.takeAllOutput();
        return output.get(output.size() - 1);
    }

    private Map<Coordinates, String> getSurroundings(IntcodeProcessor proc) {
        proc.setMemoryAt(0, 1);
        proc.run();
        List<Long> output = proc.takeAllOutput();
        System.out.println(output.stream().map(l -> (char) l.intValue()).map(c -> Character.toString(c)).reduce("", String::concat));
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
        return tiles;
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
