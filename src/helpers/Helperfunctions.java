package helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class Helperfunctions {
    public static HashSet<Integer> getData(int day) {
        HashSet<Integer> res = new HashSet<>();
        try {
            File data = new File("src/data/day" + day + ".txt");
            Scanner reader = new Scanner(data);
            while (reader.hasNextInt()) {
                res.add(reader.nextInt());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Day1 data file not found.");
        }
        return res;
    }
}
