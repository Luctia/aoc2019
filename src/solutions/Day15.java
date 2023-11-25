package solutions;

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
        Map<Tile, Long> map = discoverMapAutomatically();
        List<Tile> hallways = map.keySet().stream().filter(t -> map.get(t) == 1).toList();
        Tile oxygenLocation = new Tile(0, 0);
        for (Tile tile : map.keySet()) {
            if (map.get(tile) == 2) {
                oxygenLocation = tile;
                break;
            }
        }
        int maxDistance = 0;
        for (Tile tile : hallways) {
            int distance = minimumSteps(map, tile, oxygenLocation, tile);
            if (distance > maxDistance) {
                maxDistance = distance;
            }
        }
        return maxDistance;
    }

    private Map<Tile, Long> discoverMapAutomatically() {
        IntcodeProcessor proc = new IntcodeProcessor("day15.txt");
        long out = -1;
        Map<Tile, Long> surroundings = new HashMap<>();
        Tile current = new Tile(0, 0);
        Tile goTo;
        surroundings.put(current, 1L);
        while (!wholeMapDiscovered(surroundings)) {
            int move = randomMouseAlgorithm();
            goTo = switch (move) {
                case 1 -> new Tile(current.x, current.y - 1);
                case 2 -> new Tile(current.x, current.y + 1);
                case 3 -> new Tile(current.x - 1, current.y);
                case 4 -> new Tile(current.x + 1, current.y);
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
        Map<Tile, Long> map = discoverMapAutomatically();
        Tile oxygenLocation = new Tile(0, 0);
        for (Tile tile : map.keySet()) {
            if (map.get(tile) == 2) {
                oxygenLocation = tile;
            }
        }
        return minimumSteps(map, new Tile(0, 0), oxygenLocation, new Tile(0, 0));
    }

    private long playManually() {
        this.scanner = new Scanner(System.in);
        IntcodeProcessor proc = new IntcodeProcessor("day15.txt");
        long out = -1;
        Map<Tile, Long> surroundings = new HashMap<>();
        Tile current = new Tile(0, 0);
        Tile goTo;
        surroundings.put(current, 1L);
        while (out != 2) {
            printSurroundings(surroundings, current);
            int move = takeInput();
            goTo = switch (move) {
                case 1 -> new Tile(current.x, current.y - 1);
                case 2 -> new Tile(current.x, current.y + 1);
                case 3 -> new Tile(current.x - 1, current.y);
                case 4 -> new Tile(current.x + 1, current.y);
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
        return minimumSteps(surroundings, new Tile(0, 0), current, new Tile(0, 0));
    }

    private boolean wholeMapDiscovered(Map<Tile, Long> map) {
        // First, get all hallways
        List<Tile> hallwayTiles = map.keySet().stream().filter(t -> map.get(t) == 1).toList();
        // Verify that all hallway are surrounded by discovered tiles
        for (Tile tile : hallwayTiles) {
            if (
                    !map.containsKey(new Tile(tile.x - 1, tile.y)) ||
                    !map.containsKey(new Tile(tile.x + 1, tile.y)) ||
                    !map.containsKey(new Tile(tile.x, tile.y - 1)) ||
                    !map.containsKey(new Tile(tile.x, tile.y + 1))
            ) {
                return false;
            }
        }
        return true;
    }

    private int minimumSteps(Map<Tile, Long> surroundings, Tile from, Tile to, Tile previousPosition) {
        if (from.equals(to)) {
            return 0;
        }
        List<Tile> possibleNexts = new ArrayList<>();
        List<Tile> next = new ArrayList<>();
        possibleNexts.add(new Tile(from.x, from.y - 1));
        possibleNexts.add(new Tile(from.x, from.y + 1));
        possibleNexts.add(new Tile(from.x - 1, from.y));
        possibleNexts.add(new Tile(from.x + 1, from.y));
        for (Tile tile : possibleNexts) {
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
        for (Tile nextTile : next) {
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

    private int determineAutomaticMove(Map<Tile, Long> surroundings, Tile current, Tile previous) {
        Map<Tile, Long> options = new HashMap<>();
        options.put(new Tile(current.x, current.y - 1), null);
        options.put(new Tile(current.x, current.y + 1), null);
        options.put(new Tile(current.x - 1, current.y), null);
        options.put(new Tile(current.x + 1, current.y), null);
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

    private void printSurroundings(Map<Tile, Long> surroundings, Tile current) {
        long minX = current.x;
        long minY = current.y;
        long maxX = current.x;
        long maxY = current.y;
        for (Tile tile : surroundings.keySet()) {
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
        for (long y = minY; y <= maxY; y++) {
            for (long x = minX; x <= maxX; x++) {
                if (x == 0 && y == 0) {
                    output.append("X");
                } else if (current.equals(new Tile(x, y))) {
                    output.append("D");
                } else {
                    long content = surroundings.getOrDefault(new Tile(x, y), 3L);
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

    private record Tile(long x, long y) {
        public Tile(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tile tile = (Tile) o;
            return x == tile.x && y == tile.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
