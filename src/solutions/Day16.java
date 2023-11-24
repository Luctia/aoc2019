package solutions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day16 {
    public String part1() {
        List<Integer> phase = getData();
        for (int i = 0; i < 100; i++) {
            phase = getNewPhase(phase);
        }
        long startTime = System.nanoTime();
        String res = phase.stream().map(Object::toString).reduce("", String::concat).substring(0, 8);
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println("Part 1 took " + duration + "ns");
        return res;
    }
    public String part2() {
        List<Integer> phase = getData();
        int offset = Integer.parseInt(phase.subList(0, 7).stream().map(Objects::toString).reduce("", String::concat));
        List<Integer> realData = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            realData.addAll(phase);
        }
        int sizeBefore = realData.size();
        realData = realData.subList(offset, realData.size());
        assert sizeBefore == realData.size() + offset;
        for (int i = 0; i < 100; i++) {
            int cumulative_sum = 0;
            for (int j = realData.size() - 1; j > -1; j--) {
                cumulative_sum += realData.get(j);
                realData.set(j, Math.floorMod(cumulative_sum, 10));
            }
        }
        return realData.subList(0, 8).stream().map(Object::toString).reduce("", String::concat);
    }

    private static List<Integer> getData() {
        List<Integer> phase = new ArrayList<>();
        try {
            File data = new File("src/data/day16.txt");
            Scanner reader = new Scanner(data);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                for (int i = 0; i < line.length(); i++) {
                    phase.add(Integer.parseInt(line.substring(i, i + 1)));
                }
            }
        } catch (FileNotFoundException e) {
        }
        return phase;
    }
    
    private static List<Integer> getNewPhase(List<Integer> previousPhase) {
        List<Integer> newPhase = new ArrayList<>();
        for (int position = 0; position < previousPhase.size(); position++) {
            long result = 0;
            for (int index = position; index < previousPhase.size(); index++) {
                int element = previousPhase.get(index);
                int patternMultiplier = getPatternMultiplier(position, index);
                result += (long) element * patternMultiplier;
            }
            newPhase.add(Math.floorMod(Math.abs(result), 10));
        }
        return newPhase;
    }

    /**
     *
     * @param position The position of the value we're calculating in the output array
     * @param index the index of the number in the existing array
     * @return the multiplier
     */
    private static int getPatternMultiplier(int position, int index) {
        int patternLength = 4 * (position + 1);
        int adjustedIndex = Math.floorMod(index, patternLength) + 1;
        if (adjustedIndex >= (position + 1) * 3 && adjustedIndex < (position + 1) * 4) {
            return -1;
        }
        if (adjustedIndex >= (position + 1) && adjustedIndex < (position + 1) * 2) {
            return 1;
        }
        return 0;
    }
}
