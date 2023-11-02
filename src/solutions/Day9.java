package solutions;

import helpers.IntcodeProcessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Day9 {
    private Long[] readInput() {
        String line = "";
        try {
            File data = new File("src/data/day9.txt");
            Scanner reader = new Scanner(data);
            while (reader.hasNextLine()) {
                line = reader.nextLine();
            }
        } catch (FileNotFoundException e) {
        }
        return Arrays.stream(line.split(",")).map(Long::valueOf).toArray(Long[]::new);
    }
    public long part1() {
        IntcodeProcessor proc = new IntcodeProcessor(readInput());
        proc.setInput(1);
        proc.run();
        return proc.getOutput().get(0);
    }

    public long part2() {
        IntcodeProcessor proc = new IntcodeProcessor(readInput());
        proc.setInput(2);
        proc.run();
        return proc.getOutput().get(0);
    }
}
