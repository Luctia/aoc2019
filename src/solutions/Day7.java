package solutions;

import helpers.Helperfunctions;
import helpers.IntcodeProcessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Day7 {
    private Integer[] getCode() {
        try {
            File inputFile = new File("src/data/day7.txt");
            Scanner reader = new Scanner(inputFile);
            String code = reader.nextLine();
            return Arrays.stream(code.split(",")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
        } catch (FileNotFoundException e) {
            System.out.println("Day6 data file not found.");
        }
        return null;
    }

    public void part1() {
        int max = 0;
        ArrayList<Integer> x = new ArrayList<Integer>() {
            {
                add(0);
                add(1);
                add(2);
                add(3);
                add(4);
            }
        };
        ArrayList<ArrayList<Integer>> possibilities = Helperfunctions.combinationUtil(x);
        for (ArrayList<Integer> possibility : possibilities) {
            IntcodeProcessor ampa = new IntcodeProcessor(getCode());
            IntcodeProcessor ampb = new IntcodeProcessor(getCode());
            IntcodeProcessor ampc = new IntcodeProcessor(getCode());
            IntcodeProcessor ampd = new IntcodeProcessor(getCode());
            IntcodeProcessor ampe = new IntcodeProcessor(getCode());
            ampa.setInput(possibility.get(0), 0);
            ampa.run();
            ampb.setInput(possibility.get(1), ampa.getOutput().get(0));
            ampb.run();
            ampc.setInput(possibility.get(2), ampb.getOutput().get(0));
            ampc.run();
            ampd.setInput(possibility.get(3), ampc.getOutput().get(0));
            ampd.run();
            ampe.setInput(possibility.get(4), ampd.getOutput().get(0));
            ampe.run();
            if (ampe.getOutput().get(0) > max) {
                max = ampe.getOutput().get(0);
            }
        }
        System.out.println(max);
    }
}
