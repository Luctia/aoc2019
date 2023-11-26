package solutions;

import helpers.Coordinates;
import helpers.IntcodeProcessor;

import java.util.*;

public class Day11 {
    public int part1() {
        IntcodeProcessor proc = new IntcodeProcessor("day11.txt");
        Map<Coordinates, Long> paintedTiles = new HashMap<>();
        Coordinates currentLocation = new Coordinates(0, 0);
        // 0 = Up, 1 = Right, 2 = Down, 3 = Left
        int direction = 0;
        while (!proc.hasHalted()) {
            proc.addInput(paintedTiles.getOrDefault(currentLocation, 0L));
            proc.run();
            paintedTiles.put(currentLocation, proc.takeOutput());
            if (proc.takeOutput() == 0) {
                direction = Math.floorMod(direction - 1, 4);
                currentLocation = determineNextLocation(currentLocation, direction);
            } else {
                direction = Math.floorMod(direction + 1, 4);
                currentLocation = determineNextLocation(currentLocation, direction);
            }
        }
        return paintedTiles.size();
    }

    public String part2() {
        IntcodeProcessor proc = new IntcodeProcessor("day11.txt");
        Map<Coordinates, Long> paintedTiles = new HashMap<>();
        Coordinates currentLocation = new Coordinates(0, 0);
        paintedTiles.put(currentLocation, 1L);
        // 0 = Up, 1 = Right, 2 = Down, 3 = Left
        int direction = 0;
        while (!proc.hasHalted()) {
            proc.addInput(paintedTiles.getOrDefault(currentLocation, 0L));
            proc.run();
            paintedTiles.put(currentLocation, proc.takeOutput());
            if (proc.takeOutput() == 0) {
                direction = Math.floorMod(direction - 1, 4);
                currentLocation = determineNextLocation(currentLocation, direction);
            } else {
                direction = Math.floorMod(direction + 1, 4);
                currentLocation = determineNextLocation(currentLocation, direction);
            }
        }
        int row = 0;
        int max = 0;
        int leftmost = 0;
        int rightmost = 0;
        for (Coordinates c : paintedTiles.keySet()) {
            if (c.x < row) {
                row = c.x;
            }
            if (c.x > max) {
                max = c.x;
            }
            if (c.y < leftmost) {
                leftmost = c.y;
            }
            if (c.y > rightmost) {
                rightmost = c.y;
            }
        }
        StringBuilder output = new StringBuilder();
        while (row != max + 1) {
            StringBuilder line = new StringBuilder();
            for (int i = leftmost; i <= rightmost; i++) {
                line.append(paintedTiles.getOrDefault(new Coordinates(row, i), 0L));
            }
            row++;
            output.append(line).append("\n");
        }
        return output.toString().replaceAll("0", " ").replaceAll("1", "#");
    }

    private Coordinates determineNextLocation(Coordinates currentLocation, int direction) {
        return switch (direction) {
            case 0 -> new Coordinates(currentLocation.x, currentLocation.y - 1);
            case 1 -> new Coordinates(currentLocation.x + 1, currentLocation.y);
            case 2 -> new Coordinates(currentLocation.x, currentLocation.y + 1);
            case 3 -> new Coordinates(currentLocation.x - 1, currentLocation.y);
            default -> new Coordinates(0, 0);
        };
    }
}
