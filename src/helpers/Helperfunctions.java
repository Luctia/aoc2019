package helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Helperfunctions {
    public static ArrayList<Integer> getData(int day) {
        ArrayList<Integer> res = new ArrayList<>();
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

    public static ArrayList<ArrayList<Integer>> combinationUtil(ArrayList<Integer> array) {
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();

        if (array.size() == 1) {
            ArrayList<Integer> addition = new ArrayList<>();
            addition.add(array.get(0));
            res.add(addition);
            return res;
        }

        for (int i = 0; i < array.size(); i++) {
            ArrayList<Integer> copy = new ArrayList<>(array);
            int myAddition = copy.get(i);
            copy.remove(i);
            for (ArrayList<Integer> pos : combinationUtil(copy)) {
                pos.add(0, myAddition);
                res.add(pos);
            }
        }

        return res;
    }
}
