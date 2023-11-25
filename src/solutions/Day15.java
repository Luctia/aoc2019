package solutions;

import helpers.IntcodeProcessor;

import java.util.*;

public class Day15 {
    private Scanner scanner;
    private int lastMoveMade;

    public long part1() {
        this.scanner = new Scanner(System.in);
        IntcodeProcessor proc = new IntcodeProcessor("day15.txt");
        long out = -1;
        Map<Tile, Long> surroundings = new HashMap<>();
        Tile current = new Tile(0, 0);
        Tile goTo = new Tile(0, 0);
        surroundings.put(current, 1L);
        Tile previous = new Tile(1, 0);
        while (out != 2) {
//            printSurroundings(surroundings, current);
//            int move = determineAutomaticMove(surroundings, current, previous);
            int move = randomMouseAlgorithm();
//            int move = takeInput();
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
                previous = current;
                surroundings.put(current, out);
            }
        }
        return minimumSteps(surroundings, new Tile(0, 0), current, new Tile(0, 0));
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
