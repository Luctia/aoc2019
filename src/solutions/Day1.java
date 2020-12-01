package solutions;

import java.util.HashSet;
import static helper.Helperfunctions.getData;

/**
 * Solutions to the exercise of day 1.
 * @author Luctia
 */
public class Day1 {
    public int part1() {
        int sum = 0;
        HashSet<Integer> modules = getData(1);
        for (Integer module : modules) {
            sum += Math.floorDiv(module, 3) - 2;
        }
        return sum;
    }

    public int part2() {
        int sum = 0;
        HashSet<Integer> data = getData(1);
        for (Integer module : data) {
            int newfuel = Math.floorDiv(module, 3) - 2;
            while (newfuel > 0) {
                sum += newfuel;
                newfuel = Math.floorDiv(newfuel, 3) - 2;
            }
        }
        return sum;
    }
}
