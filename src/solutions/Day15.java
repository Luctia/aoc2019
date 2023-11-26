package solutions;

import helpers.Coordinates;
import helpers.IntcodeProcessor;

import java.util.*;

public class Day15 {
    private Scanner scanner;
    private final static boolean PLAY_MANUALLY = false;

    public long part1() {
        if (PLAY_MANUALLY) {
            return playManually();
        } else {
            return playOnAuto();
        }
    }

    public long part2() {
        Map<Coordinates, Long> map = discoverMapAutomatically();
        List<Coordinates> hallways = map.keySet().stream().filter(t -> map.get(t) == 1).toList();
        Coordinates oxygenLocation = new Coordinates(0, 0);
        for (Coordinates tile : map.keySet()) {
            if (map.get(tile) == 2) {
                oxygenLocation = tile;
                break;
            }
        }
        int maxDistance = 0;
        for (Coordinates tile : hallways) {
            int distance = minimumSteps(map, tile, oxygenLocation, tile);
            if (distance > maxDistance) {
                maxDistance = distance;
            }
        }
        return maxDistance;
    }

    private Map<Coordinates, Long> discoverMapAutomatically() {
        IntcodeProcessor proc = new IntcodeProcessor("day15.txt");
        long out = -1;
        Map<Coordinates, Long> surroundings = new HashMap<>();
        Coordinates current = new Coordinates(0, 0);
        Coordinates goTo;
        surroundings.put(current, 1L);
        while (!wholeMapDiscovered(surroundings)) {
            int move = randomMouseAlgorithm();
            goTo = switch (move) {
                case 1 -> new Coordinates(current.x, current.y - 1);
                case 2 -> new Coordinates(current.x, current.y + 1);
                case 3 -> new Coordinates(current.x - 1, current.y);
                case 4 -> new Coordinates(current.x + 1, current.y);
                default -> current;
            };
            proc.addInput(move);
            proc.run();
            out = proc.takeOutput();
            if (out == 0) {
                // hit a wall
                surroundings.put(goTo, 0L);
            } else {
                current = goTo;
                surroundings.put(current, out);
            }
        }
        return surroundings;
    }

    /**
     * Automatically discover the maze.
     * @return
     */
    private long playOnAuto() {
        Map<Coordinates, Long> map = discoverMapAutomatically();
        Coordinates oxygenLocation = new Coordinates(0, 0);
        for (Coordinates tile : map.keySet()) {
            if (map.get(tile) == 2) {
                oxygenLocation = tile;
            }
        }
        return minimumSteps(map, new Coordinates(0, 0), oxygenLocation, new Coordinates(0, 0));
    }

    private long playManually() {
        this.scanner = new Scanner(System.in);
        IntcodeProcessor proc = new IntcodeProcessor("day15.txt");
        long out = -1;
        Map<Coordinates, Long> surroundings = new HashMap<>();
        Coordinates current = new Coordinates(0, 0);
        Coordinates goTo;
        surroundings.put(current, 1L);
        while (out != 2) {
            printSurroundings(surroundings, current);
            int move = takeInput();
            goTo = switch (move) {
                case 1 -> new Coordinates(current.x, current.y - 1);
                case 2 -> new Coordinates(current.x, current.y + 1);
                case 3 -> new Coordinates(current.x - 1, current.y);
                case 4 -> new Coordinates(current.x + 1, current.y);
                default -> current;
            };
            proc.addInput(move);
            proc.run();
            out = proc.takeOutput();
            if (out == 0) {
                // hit a wall
                surroundings.put(goTo, 0L);
            } else {
                current = goTo;
                surroundings.put(current, out);
            }
        }
        return minimumSteps(surroundings, new Coordinates(0, 0), current, new Coordinates(0, 0));
    }

    private boolean wholeMapDiscovered(Map<Coordinates, Long> map) {
        // First, get all hallways
        List<Coordinates> hallwayTiles = map.keySet().stream().filter(t -> map.get(t) == 1).toList();
        // Verify that all hallway are surrounded by discovered tiles
        for (Coordinates tile : hallwayTiles) {
            if (
                    !map.containsKey(new Coordinates(tile.x - 1, tile.y)) ||
                    !map.containsKey(new Coordinates(tile.x + 1, tile.y)) ||
                    !map.containsKey(new Coordinates(tile.x, tile.y - 1)) ||
                    !map.containsKey(new Coordinates(tile.x, tile.y + 1))
            ) {
                return false;
            }
        }
        return true;
    }

    private int minimumSteps(Map<Coordinates, Long> surroundings, Coordinates from, Coordinates to, Coordinates previousPosition) {
        if (from.equals(to)) {
            return 0;
        }
        List<Coordinates> possibleNexts = new ArrayList<>();
        List<Coordinates> next = new ArrayList<>();
        possibleNexts.add(new Coordinates(from.x, from.y - 1));
        possibleNexts.add(new Coordinates(from.x, from.y + 1));
        possibleNexts.add(new Coordinates(from.x - 1, from.y));
        possibleNexts.add(new Coordinates(from.x + 1, from.y));
        for (Coordinates tile : possibleNexts) {
            if (surroundings.containsKey(tile)) {
                if (surroundings.getOrDefault(tile, 1L) == 2) {
                    return 1;
                }
                if (surroundings.get(tile) != 0 && !tile.equals(previousPosition)) {
                    // Tile is not a wall and thus an option.
                    next.add(tile);
                }
            }
        }
        if (next.isEmpty()) {
            // No possible options; return the max
            return surroundings.size();
        }
        int minimum = surroundings.size();
        for (Coordinates nextTile : next) {
            int distance = minimumSteps(surroundings, nextTile, to, from);
            if (distance < minimum) {
                minimum = distance;
            }
        }
        return 1 + minimum;
    }

    private int randomMouseAlgorithm() {
        int direction = new Random().nextInt(1, 5);
        return direction;
    }

    private int determineAutomaticMove(Map<Coordinates, Long> surroundings, Coordinates current, Coordinates previous) {
        Map<Coordinates, Long> options = new HashMap<>();
        options.put(new Coordinates(current.x, current.y - 1), null);
        options.put(new Coordinates(current.x, current.y + 1), null);
        options.put(new Coordinates(current.x - 1, current.y), null);
        options.put(new Coordinates(current.x + 1, current.y), null);
        options.remove(previous);
        options.replaceAll((o, v) -> surroundings.getOrDefault(o, null));
        return 1;
    }

    private int takeInput() {
        char input;
        try {
            input = scanner.nextLine().charAt(0);
        } catch (StringIndexOutOfBoundsException e) {
            return 0;
        }
        return switch (input) {
            case 'w' -> 1;
            case 'a' -> 3;
            case 's' -> 2;
            case 'd' -> 4;
            default -> 0;
        };
    }

    private void printSurroundings(Map<Coordinates, Long> surroundings, Coordinates current) {
        int minX = current.x;
        int minY = current.y;
        int maxX = current.x;
        int maxY = current.y;
        for (Coordinates tile : surroundings.keySet()) {
            if (tile.x < minX) {
                minX = tile.x;
            }
            if (tile.x > maxX) {
                maxX = tile.x;
            }
            if (tile.y < minY) {
                minY = tile.y;
            }
            if (tile.y > maxY) {
                maxY = tile.y;
            }
        }
        StringBuilder output = new StringBuilder();
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                if (x == 0 && y == 0) {
                    output.append("X");
                } else if (current.equals(new Coordinates(x, y))) {
                    output.append("D");
                } else {
                    long content = surroundings.getOrDefault(new Coordinates(x, y), 3L);
                    output.append(
                            switch ((int) content) {
                                case 0 -> "#";
                                case 1 -> ".";
                                case 2 -> "O";
                                default -> " ";
                            });
                }
            }
            output.append("\n");
        }
        System.out.println(output);
    }
}
