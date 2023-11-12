package solutions;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class Day10 {
    public int part1() {
        return getAsteroidField().bestVantagePoint();
    }

    public double part2() {
        AsteroidField asteroidField = getAsteroidField();
        // Vantage point is 29, 28
        Asteroid vantagePoint = new Asteroid(29, 28);
        Map<Asteroid, Double> angles = new HashMap<>();
        Set<Asteroid> asteroids = asteroidField.asteroids;
        asteroids.remove(vantagePoint);
        for (Asteroid asteroid : asteroids) {
            int divisor = AsteroidField.gcd(asteroid.x, asteroid.y);
            angles.put(
                    asteroid,
                    BigDecimal.valueOf(Math.atan2((double) (asteroid.y - vantagePoint.y) / divisor, (double) (asteroid.x - vantagePoint.x) / divisor)).setScale(10, RoundingMode.HALF_UP).doubleValue()
            );
        }
        Map<Asteroid, Double> sortedByAngle = angles.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1 , LinkedHashMap::new));
        double currentAngle = BigDecimal.valueOf(-Math.PI / 2).setScale(10, RoundingMode.HALF_UP).doubleValue();
        List<Asteroid> sortedAsteroids = new ArrayList<>(sortedByAngle.keySet().stream().toList());
        List<Double> sortedAngles = new ArrayList<>(sortedByAngle.values().stream().toList());
        int index = sortedAngles.indexOf(currentAngle);
        for (int i = 0; i < 199; i++) {
            currentAngle = sortedAngles.get(index);
            double finalCurrentAngle = currentAngle;
            boolean multipleInLine = sortedAngles.stream().filter(a -> a.equals(finalCurrentAngle)).count() > 1;
            if (multipleInLine) {
                // Select the closest one
                Asteroid closestCandidate = sortedAsteroids.get(index);
                for (int j = 0; j < sortedAngles.size(); j++) {
                    if (sortedAngles.get(Math.floorMod(index + j, sortedAngles.size())) == finalCurrentAngle) {
                        Asteroid candidate = sortedAsteroids.get(index + j);
                        double candidateMan = candidate.manhattanDistance(vantagePoint);
                        double currentMan = closestCandidate.manhattanDistance(vantagePoint);
                        if (candidateMan < currentMan) {
                            closestCandidate = sortedAsteroids.get(index + j);
                        }
                    } else {
                        break;
                    }
                }
                sortedAngles.remove(index);
                sortedAsteroids.remove(closestCandidate);
            } else {
                // Just select the one
                sortedAngles.remove(index);
                sortedAsteroids.remove(index);
            }
            // Get the next angle
            if (index >= sortedAngles.size()) {
                index = 0;
            }
            double nextAngle = sortedAngles.get(index);
            while (finalCurrentAngle == nextAngle) {
                index = Math.floorMod(index + 1, sortedAngles.size());
                nextAngle = sortedAngles.get(index);
            }
        }
        return sortedAsteroids.get(index).x * 100 + sortedAsteroids.get(index).y;
    }

    private AsteroidField getAsteroidField() {
        StringBuilder input = new StringBuilder();
        int width = 0;
        int height = 0;
        try {
            File data = new File("src/data/day10.txt");
            Scanner reader = new Scanner(data);
            input.append(reader.nextLine().replaceAll("\n", ""));
            width = input.length();
            height++;
            while (reader.hasNextLine()) {
                input.append(reader.nextLine());
                height++;
            }
        } catch (FileNotFoundException e) {
        }
        return new AsteroidField(input.toString().toCharArray(), width, height);
    }

    private static class AsteroidField {
        private final Set<Asteroid> asteroids;
        private final int width;
        private final int height;

        public AsteroidField(char[] chars, int width, int height) {
            this.width = width;
            this.height = height;
            this.asteroids = new HashSet<>();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (chars[y*width + x] == '#') {
                        this.asteroids.add(new Asteroid(x, y));
                    }
                }
            }
        }

        public void removeAsteroid(Asteroid asteroid) {
            this.asteroids.remove(asteroid);
        }

        public void print(Asteroid vantagePoint, Asteroid destroyed) {
            StringBuilder field = new StringBuilder();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (vantagePoint.x == j && vantagePoint.y == i) {
                        field.append("O");
                    } else if (destroyed.x == j && destroyed.y == i) {
                        field.append("X");
                    } else {
                        field.append(asteroidAt(j, i) ? "#" : " ");
                    }
                }
                field.append("\n");
            }
            System.out.println(field);
        }

        public int bestVantagePoint() {
            int max = 0;
            Asteroid best = new Asteroid(0, 0);
            for (Asteroid viewer : this.asteroids) {
                int viewed = 0;
                for (Asteroid viewee : this.asteroids) {
                    if (!isBlockedFrom(viewee, viewer)) {
                        viewed++;
                    }
                }
                if (viewed > max) {
                    max = viewed;
                    best = viewer;
                }
            }
            System.out.println(best.x + ", " + best.y);
            return max;
        }

        public boolean isBlockedFrom(Asteroid viewed, Asteroid viewpoint) {
            if (viewed.equals(viewpoint)) {
                // The asteroid itself doesn't count
                return true;
            }

            // First, get the x and y distance from the asteroid to the to-check coordinates
            int stepX = viewed.x - viewpoint.x;
            int stepY = viewed.y - viewpoint.y;
            // Then, make an as-small-as-possible step that we can take to eventually get to the coordinates from the
            //  asteroid
            if (stepX == 0) {
                stepY = stepY / Math.abs(stepY);
            } else if (stepY == 0) {
                stepX = stepX / Math.abs(stepX);
            } else {
                int gcd = Math.abs(gcd(stepX, stepY));
                stepY /= gcd;
                stepX /= gcd;
            }
            // Then keep taking that step and check if we come across another asteroid
            int currentX = viewpoint.x + stepX;
            int currentY = viewpoint.y + stepY;
            while (!(currentY == viewed.y && currentX == viewed.x)) {
                if (asteroidAt(currentX, currentY)) {
                    return true;
                }
                currentY += stepY;
                currentX += stepX;
            }
            return false;
        }

        private boolean asteroidAt(int x, int y) {
            return this.asteroids.stream().anyMatch(a -> a.x == x && a.y == y);
        }

        private static int gcd(int a, int b) {
            if (b==0) return a;
            return gcd(b,a%b);
        }
    }

    private record Asteroid(int x, int y) {

        public double manhattanDistance(Asteroid from) {
                double x = Math.pow(this.x - from.x, 2);
                double y = Math.pow(this.y - from.y, 2);
                return Math.sqrt(x + y);
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof Asteroid assumed)) {
                    return false;
                }

                return assumed.x == this.x && assumed.y == this.y;
            }

            @Override
            public String toString() {
                return this.x + ", " + this.y;
            }

    }
}
