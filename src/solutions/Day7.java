package solutions;

import helpers.Helperfunctions;
import helpers.IntcodeProcessor;

import java.util.ArrayList;

public class Day7 {
    public long part1() {
        long max = 0;
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
            IntcodeProcessor ampa = new IntcodeProcessor("day7.txt");
            IntcodeProcessor ampb = new IntcodeProcessor("day7.txt");
            IntcodeProcessor ampc = new IntcodeProcessor("day7.txt");
            IntcodeProcessor ampd = new IntcodeProcessor("day7.txt");
            IntcodeProcessor ampe = new IntcodeProcessor("day7.txt");
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
        return max;
    }

    public long part2() {
        long max = 0;
        ArrayList<Integer> x = new ArrayList<Integer>() {
            {
                add(5);
                add(6);
                add(7);
                add(8);
                add(9);
            }
        };
        ArrayList<ArrayList<Integer>> possibilities = Helperfunctions.combinationUtil(x);
        for (ArrayList<Integer> possibility : possibilities) {
            IntcodeProcessor ampa = new IntcodeProcessor("day7.txt");
            IntcodeProcessor ampb = new IntcodeProcessor("day7.txt");
            IntcodeProcessor ampc = new IntcodeProcessor("day7.txt");
            IntcodeProcessor ampd = new IntcodeProcessor("day7.txt");
            IntcodeProcessor ampe = new IntcodeProcessor("day7.txt");
            ampa.setInput(possibility.get(0), 0);
            ampb.setInput(possibility.get(1));
            ampc.setInput(possibility.get(2));
            ampd.setInput(possibility.get(3));
            ampe.setInput(possibility.get(4));
            ampa.run();
            ampb.run();
            ampc.run();
            ampd.run();
            ampe.run();
            while (!ampe.hasHalted()) {
                if (!ampe.getOutput().isEmpty()) {
                    ampa.addInput(ampe.takeOutput());
                    ampa.run();
                }
                if (!ampa.getOutput().isEmpty()) {
                    ampb.addInput(ampa.takeOutput());
                    ampb.run();
                }
                if (!ampb.getOutput().isEmpty()) {
                    ampc.addInput(ampb.takeOutput());
                    ampc.run();
                }
                if (!ampc.getOutput().isEmpty()) {
                    ampd.addInput(ampc.takeOutput());
                    ampd.run();
                }
                if (!ampd.getOutput().isEmpty()) {
                    ampe.addInput(ampd.takeOutput());
                    ampe.run();
                }
            }
            if (ampe.getOutput().get(0) > max) {
                max = ampe.getOutput().get(0);
            }
        }
        return max;
    }
}
