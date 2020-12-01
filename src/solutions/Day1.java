package solutions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Solutions to the exercise of day 1.
 * @author Luctia
 */
public class Day1 {
    private HashSet<Integer> getData() {
        HashSet<Integer> res = new HashSet<>();
        try {
            File data = new File("src/data/day1.txt");
            Scanner reader = new Scanner(data);
            while (reader.hasNextInt()) {
                res.add(reader.nextInt());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Day1 data file not found.");
        }
        return res;
    }

    public int part1() {
        int sum = 0;
        HashSet<Integer> data = getData();
        for (Integer entry : data) {
            sum += Math.floorDiv(entry ,3) - 2;
        }
        return sum;
    }
}
