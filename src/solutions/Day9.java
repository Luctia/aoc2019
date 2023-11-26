package solutions;

import helpers.IntcodeProcessor;

public class Day9 {
    public long part1() {
        IntcodeProcessor proc = new IntcodeProcessor("day9.txt");
        proc.setInput(1);
        proc.run();
        return proc.getOutput().get(0);
    }

    public long part2() {
        IntcodeProcessor proc = new IntcodeProcessor("day9.txt");
        proc.setInput(2);
        proc.run();
        return proc.getOutput().get(0);
    }
}
