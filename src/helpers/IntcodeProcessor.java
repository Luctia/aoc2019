package helpers;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class mimicking a processor running Intcode.
 * @author Luctia
 */
public class IntcodeProcessor {
    /**
     * Memory of the processor.
     */
    private ArrayList<Integer> memory;

    /**
     * Create a new Intcode processor using an {@link ArrayList} representing memory.
     * @param memory the processors memory
     */
    public IntcodeProcessor(ArrayList<Integer> memory) {
        this.memory = memory;
    }

    /**
     * Create a new Intcode processor using an array of {@link Integer}s representing memory.
     * @param memory the processors memory
     */
    public IntcodeProcessor(Integer[] memory) {
        this.memory = new ArrayList<Integer>(Arrays.asList(memory));
    }

    /**
     * Set the processors memory to the given memory.
     * @param newMemory the new memory
     */
    public void setMemory(ArrayList<Integer> newMemory) {
        this.memory = newMemory;
    }

    /**
     * Add more memory, given as an {@link ArrayList}.
     * @param newMemory the memory to be added
     */
    public void addToMemory(ArrayList<Integer> newMemory) {
        this.memory.addAll(newMemory);
    }

    /**
     * Get the memory as an {@link ArrayList} of {@link Integer}s.
     * @return the processors memory
     */
    public ArrayList<Integer> getMemory() {
        return this.memory;
    }

    /**
     * Run the processor.
     * @throws Exception when an unknown operator is given
     */
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
