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
        return phase.stream().map(Object::toString).reduce("", String::concat).substring(0, 8);
    }
    public String part2() {
        List<Integer> phase = getData();
        int offset = Integer.parseInt(phase.subList(0, 7).stream().map(Objects::toString).reduce("", String::concat));
        List<Integer> realData = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            realData.addAll(phase);
        }
        for (int i = 0; i < 100; i++) {
            System.out.println("i is " + i);
            realData = getNewPhase(realData);
        }
        return phase.stream().map(Object::toString).reduce("", String::concat).substring(offset, offset + 8);
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
        for (int i = 0; i < previousPhase.size(); i++) {
            long result = 0;
            for (int j = 0; j < previousPhase.size(); j++) {
                int element = previousPhase.get(j);
                int patternMultiplier = getPatternMultiplier(i, j);
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
        if (index < position) {
            return 0;
        }
        int[] pattern = new int[4 * (position + 1)];
        Arrays.fill(pattern, 0);
        Arrays.fill(pattern, (position + 1), (position + 1) * 2, 1);
        Arrays.fill(pattern, (position + 1) * 3, (position + 1) * 4, -1);
        pattern = Arrays.copyOfRange(pattern, 1, pattern.length + 1);
        return pattern[Math.floorMod(index, pattern.length)];
    }
}
