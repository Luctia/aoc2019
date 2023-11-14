package solutions;

import helpers.IntcodeProcessor;

public class Day13 {
    public int part1() {
        IntcodeProcessor proc = new IntcodeProcessor("day13.txt");
        proc.run();
        int blocks = 0;
        for (int i = 2; i < proc.getOutput().size(); i+=3) {
            if (proc.getOutput().get(i) == 2) {
                blocks++;
            }
        }
        return blocks;
    }
}
