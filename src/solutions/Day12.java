package solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        for (int i = 0; i < 4; i++) {
            MoonSystem moonSystem = new MoonSystem();
            moonSystem.addMoon(3, 2, -6);
            moonSystem.addMoon(-13, 18, 10);
            moonSystem.addMoon(-8, -1, 13);
            moonSystem.addMoon(5, 10, 4);
            System.out.println("Getting moon period");
            moonPeriods.add(moonSystem.getPeriod(moonSystem.moons.get(i)));
        }

        List<Long> test = new ArrayList<>();
        test.add(12L);
        test.add(75L);
        test.add(15L);
        System.out.println(lcm(test));

        return lcm(moonPeriods);
    }

    public static Long lcm(List<Long> numbers) {
        Long lcm = Math.abs(numbers.get(0));
        for (int i = 1; i < numbers.size() - 1; i++) {
            Long number = numbers.get(i);
            if (lcm == 0 || number == 0) {
                return 0L;
            }
            Long absNumber2 = Math.abs(number);
            Long absHigherNumber = Math.max(lcm, absNumber2);
            Long absLowerNumber = Math.min(lcm, absNumber2);
            lcm = absHigherNumber;
            while (lcm % absLowerNumber != 0) {
                lcm += absHigherNumber;
            }
        }
        return lcm;
    }

    private class MoonSystem {
        private final List<Moon> moons;

        public MoonSystem() {
            this.moons = new ArrayList<>();
        }

        public void addMoon(int x, int y, int z) {
            this.moons.add(new Moon(x, y, z));
        }

        public void moveMoons() {
            for (int i = 0; i < moons.size(); i++) {
                for (int j = i + 1; j < moons.size(); j++) {
                    Moon a = this.moons.get(i);
                    Moon b = this.moons.get(j);
                    a.adjustVelocity(b);
                    b.adjustVelocity(a);
                }
            }
            for (int i = 0; i < moons.size(); i++) {
                this.moons.get(i).moveMoon();
            }
        }

        public int getEnergy() {
            return this.moons
                    .stream()
                    .map(Moon::getEnergy)
                    .reduce(0, Integer::sum);
        }

        public Long getPeriod(Moon moon) {
            Long period = 0L;
            int moonIndex = this.moons.indexOf(moon);
            List<Integer> seen = new ArrayList<>();
            boolean duplicateFound = false;
            while (!duplicateFound) {
                this.moveMoons();
                if (seen.contains(this.moons.get(moonIndex).hashCode())) {
                    return period - seen.indexOf(this.moons.get(moonIndex).hashCode());
                }
                period++;
                seen.add(this.moons.get(moonIndex).hashCode());
            }
            return -1L;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MoonSystem moonSystem = (MoonSystem) o;
            return Objects.equals(moons, moonSystem.moons);
        }

        @Override
        public int hashCode() {
            return Objects.hash(moons);
        }
    }

    private class Moon {
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

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Moon moon = (Moon) o;
            return x == moon.x && y == moon.y && z == moon.z && velX == moon.velX && velY == moon.velY && velZ == moon.velZ;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z, velX, velY, velZ);
        }
    }
}
