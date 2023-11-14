package solutions;

import java.util.ArrayList;
import java.util.List;

import static helpers.Helperfunctions.lcm;

public class Day12 {
    public int part1() {
        /* Moons starting positions:
        <x=3, y=2, z=-6>
        <x=-13, y=18, z=10>
        <x=-8, y=-1, z=13>
        <x=5, y=10, z=4>
         */

        MoonSystem moonSystem = new MoonSystem();
        moonSystem.addMoon(3, 2, -6);
        moonSystem.addMoon(-13, 18, 10);
        moonSystem.addMoon(-8, -1, 13);
        moonSystem.addMoon(5, 10, 4);

        for (int i = 0; i < 1000; i++) {
            moonSystem.moveMoons();
        }
        return moonSystem.getEnergy();
    }

    public Long part2() {
        List<Long> moonPeriods = new ArrayList<>();

        MoonSystem moonSystem = new MoonSystem();
        moonSystem.addMoon(3, 2, -6);
        moonSystem.addMoon(-13, 18, 10);
        moonSystem.addMoon(-8, -1, 13);
        moonSystem.addMoon(5, 10, 4);

        // Test
//        MoonSystem moonSystem = new MoonSystem();
//        moonSystem.addMoon(-8, -10, 0);
//        moonSystem.addMoon(5, 5, 10);
//        moonSystem.addMoon(2, -7, 3);
//        moonSystem.addMoon(9, -8, -3);

        moonPeriods.add(moonSystem.getPeriodX());
        moonPeriods.add(moonSystem.getPeriodY());
        moonPeriods.add(moonSystem.getPeriodZ());

        return lcm(moonPeriods);
    }

    private static class MoonSystem {
        private final List<Moon> moons;

        public MoonSystem() {
            this.moons = new ArrayList<>();
        }

        public void addMoon(int x, int y, int z) {
            this.moons.add(new Moon(x, y, z));
        }

        public void moveMoons() {
            // Adjust velocities
            for (int i = 0; i < moons.size(); i++) {
                for (int j = i + 1; j < moons.size(); j++) {
                    Moon a = this.moons.get(i);
                    Moon b = this.moons.get(j);
                    a.adjustVelocity(b);
                    b.adjustVelocity(a);
                }
            }
            // Adjust coordinates
            for (Moon moon : moons) {
                moon.moveMoon();
            }
        }

        public int getEnergy() {
            return this.moons
                    .stream()
                    .map(Moon::getEnergy)
                    .reduce(0, Integer::sum);
        }

        public Long getPeriodX() {
            String initialState = this.moons.stream().map(m -> m.x + " " + m.velX + " ").reduce("", String::concat);
            long period = 0;
            while (true) {
                moveMoons();
                period++;
                String representation = this.moons.stream().map(m -> m.x + " " + m.velX + " ").reduce("", String::concat);
                if (representation.equals(initialState)) {
                    return period;
                }
            }
        }

        public Long getPeriodY() {
            String initialState = this.moons.stream().map(m -> m.y + " " + m.velY + " ").reduce("", String::concat);
            long period = 0;
            while (true) {
                moveMoons();
                period++;
                String representation = this.moons.stream().map(m -> m.y + " " + m.velY + " ").reduce("", String::concat);
                if (representation.equals(initialState)) {
                    return period;
                }
            }
        }

        public Long getPeriodZ() {
            String initialState = this.moons.stream().map(m -> m.z + " " + m.velZ + " ").reduce("", String::concat);
            long period = 0;
            while (true) {
                moveMoons();
                period++;
                String representation = this.moons.stream().map(m -> m.z + " " + m.velZ + " ").reduce("", String::concat);
                if (representation.equals(initialState)) {
                    return period;
                }
            }
        }
    }

    private static class Moon {
        private int x, y, z, velX, velY, velZ;

        public Moon(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.velX = 0;
            this.velY = 0;
            this.velZ = 0;
        }

        public int getEnergy() {
            int potentialEnergy = Math.abs(this.x) + Math.abs(this.y) + Math.abs(this.z);
            int kineticEnergy = Math.abs(this.velX) + Math.abs(this.velY) + Math.abs(this.velZ);
            return potentialEnergy * kineticEnergy;
        }

        public void adjustVelocity(Moon puller) {
            if (puller.x > this.x) {
                this.velX++;
            } else if (puller.x < this.x) {
                this.velX--;
            }
            if (puller.y > this.y) {
                this.velY++;
            } else if (puller.y < this.y) {
                this.velY--;
            }
            if (puller.z > this.z) {
                this.velZ++;
            } else if (puller.z < this.z) {
                this.velZ--;
            }
        }

        public void moveMoon() {
            this.x = this.x + this.velX;
            this.y = this.y + this.velY;
            this.z = this.z + this.velZ;
        }
    }
}
