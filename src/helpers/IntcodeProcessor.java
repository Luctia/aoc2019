package helpers;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class mimicking a processor
 */
public class IntcodeProcessor {
    private ArrayList<Integer> memory;

    public IntcodeProcessor(ArrayList<Integer> memory) {
        this.memory = memory;
    }

    public IntcodeProcessor(Integer[] memory) {
        this.memory = new ArrayList<Integer>(Arrays.asList(memory));
    }

    public void setMemory(ArrayList<Integer> newcode) {
        this.memory = newcode;
    }

    public void addToMemory(ArrayList<Integer> newMemory) {
        this.memory.addAll(newMemory);
    }

    public ArrayList<Integer> getMemory() {
        return this.memory;
    }

    public void run() throws Exception {
        int index = 0;
        int result = 0;
        int location = 0;
        boolean finished = false;
        while (!finished) {
            switch (this.memory.get(index)) {
                case 1:
                    result = this.memory.get(this.memory.get(index + 1)) + this.memory.get(this.memory.get(index + 2));
                    location = this.memory.get(index + 3);
                    this.memory.set(location, result);
                    index += 4;
                    break;
                case 2:
                    result = this.memory.get(this.memory.get(index + 1)) * this.memory.get(this.memory.get(index + 2));
                    location = this.memory.get(index + 3);
                    this.memory.set(location, result);
                    index += 4;
                    break;
                case 99:
                    finished = true;
                    break;
                default:
                    throw new Exception("Operator " + this.memory.get(index) + " is unknown.");
            }
        }
    }
}
