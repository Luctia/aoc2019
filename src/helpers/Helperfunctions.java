package helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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

    public static Long lcm(List<Long> numbers) {
        List<Long> factorList = new ArrayList<>();
        for (Long number : numbers) {
            List<Long> numberFactors = primeFactorization(number);
            for (Long factor : numberFactors) {
                long occursExisting = factorList.stream().filter(l -> Objects.equals(l, factor)).count();
                long occursNew = numberFactors.stream().filter(l -> Objects.equals(l, factor)).count();
                if (occursNew > occursExisting) {
                    for (int i = 0; i < occursNew - occursExisting; i++) {
                        factorList.add(factor);
                    }
                }
            }
        }
        return factorList.stream().reduce(1L, (a, b) -> a * b);
    }

    public static List<Long> primeFactorization(Long number) {
        List<Long> res = new ArrayList<>();
        for (long i = 2; i < number; i++) {
            while (Math.floorMod(number, i) == 0) {
                res.add(i);
                number = number / i;
            }
        }
        if (number > 2) {
            res.add(number);
        }
        return res;
    }
}
