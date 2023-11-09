package solutions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day10 {
    public int part1() {
        String input = "";
        int width = 0;
        int height = 0;
        try {
            File data = new File("src/data/day10.txt");
            Scanner reader = new Scanner(data);
            input += reader.nextLine().replaceAll("\n", "");
            width = input.length();
            height++;
            while (reader.hasNextLine()) {
                input += reader.nextLine();
                height++;
            }
        } catch (FileNotFoundException e) {
        }
        AsteroidField asteroidField = new AsteroidField(input.toCharArray(), width, height);
        return asteroidField.bestVantagePoint();
    }

    private class AsteroidField {
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

        public int bestVantagePoint() {
            System.out.println("Total asteroids to check: " + this.asteroids.size());
            int max = 0;
            for (Asteroid viewer : this.asteroids) {
                System.out.println("Checking asteroid " + viewer.x + ", " + viewer.y);
                int viewed = 0;
                for (Asteroid viewee : this.asteroids) {
                    if (!isBlockedFrom(viewee, viewer)) {
                        viewed++;
                    }
                }
                System.out.println("  Viewed: " + viewed);
                if (viewed > max) {
                    max = viewed;
                }
            }
            return max;
        }

        public boolean isBlockedFrom(Asteroid viewwee, Asteroid viewpoint) {
            if (viewwee.equals(viewpoint)) {
                // The asteroid itself doesn't count
                return true;
            }

            // First, get the x and y distance from the asteroid to the to-check coordinates
            int stepX = viewwee.x - viewpoint.x;
            int stepY = viewwee.y - viewpoint.y;
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
            while (!(currentY == viewwee.y && currentX == viewwee.x)) {
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

    private class Asteroid {
        public int x, y;

        public Asteroid(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Asteroid assumed)) {
                return false;
            }

            return assumed.x == this.x && assumed.y == this.y;
        }
    }
}
