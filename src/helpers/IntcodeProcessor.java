package helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 * A class mimicking a processor running Intcode.
 * @author Luctia
 */
public class IntcodeProcessor {
    /**
     * Memory of the processor.
     */
    private LargeMemory memory;
    private ArrayList<Long> output = new ArrayList<>();
    private ArrayList<Long> input;
    private long pointer;
    private boolean halted;

    /**
     * Create a new Intcode processor using an {@link ArrayList} representing memory.
     * @param memory the processors memory
     */
    public IntcodeProcessor(ArrayList<Integer> memory) {
        this.memory = new LargeMemory();
        for (int i = 0; i < memory.size(); i++) {
            this.memory.setAtAddress(i, memory.get(i));
        }
        this.pointer = 0;
        this.halted = false;
    }

    /**
     * Create a new Intcode processor using an array of {@link Integer}s representing memory.
     * @param memory the processors memory
     */
    public IntcodeProcessor(Integer[] memory) {
        this.memory = new LargeMemory();
        for (int i = 0; i < memory.length; i++) {
            this.memory.setAtAddress(i, memory[i]);
        }
        this.pointer = 0;
        this.halted = false;
    }

    /**
     * Set the processors memory to the given memory.
     * @param newMemory the new memory
     */
    public void setMemory(LargeMemory newMemory) {
        this.memory = newMemory;
    }

    /**
     * Add more memory, given as an {@link ArrayList}.
     * @param newMemory the memory to be added
     */
    public void addToMemory(ArrayList<Long> newMemory) {
        this.memory.addAll(newMemory);
    }

    /**
     * Get the memory as an {@link ArrayList} of {@link Integer}s.
     * @return the processors memory
     */
    public LargeMemory getMemory() {
        return this.memory;
    }

    /**
     * Run the processor.
     * @throws Exception when an unknown operator is given
     */
    public void run() {
        while (!this.halted) {
            int instruction = (int) this.memory.get(this.pointer);
            int paramMode1 = (instruction / 100) % 10;
            int paramMode2 = (instruction / 1000) % 10;
            int paramMode3 = (instruction / 10000) % 10;
            int opcode = instruction % 100;
            try {
                switch (opcode) {
                    case 1 -> {
                        long result = getValue(paramMode1, this.memory.get(this.pointer + 1)) + getValue(paramMode2, this.memory.get(this.pointer + 2));
                        long location = this.memory.get(this.pointer + 3);
                        this.memory.setAtAddress(location, result);
                        this.pointer += 4;
                    }
                    case 2 -> {
                        long result = getValue(paramMode1, this.memory.get(this.pointer + 1)) * getValue(paramMode2, this.memory.get(this.pointer + 2));
                        long location = this.memory.get(this.pointer + 3);
                        this.memory.setAtAddress(location, result);
                        this.pointer += 4;
                    }
                    case 3 -> {
                        try {
                            this.memory.setAtAddress(this.memory.get(this.pointer + 1), this.input.remove(0));
                        } catch (IndexOutOfBoundsException e) {
                            return;
                        }
                        this.pointer += 2;
                    }
                    case 4 -> {
                        this.output.add(getValue(paramMode1, this.memory.get(this.pointer + 1)));
                        this.pointer += 2;
                    }
                    case 5 -> {
                        long value = getValue(paramMode1, this.memory.get(this.pointer + 1));
                        long jumpTo = getValue(paramMode2, this.memory.get(this.pointer + 2));
                        this.pointer = (value != 0) ? jumpTo : this.pointer + 3;
                    }
                    case 6 -> {
                        long value = getValue(paramMode1, this.memory.get(this.pointer + 1));
                        long jumpTo = getValue(paramMode2, this.memory.get(this.pointer + 2));
                        this.pointer = (value == 0) ? jumpTo : this.pointer + 3;
                    }
                    case 7 -> {
                        long val1 = getValue(paramMode1, this.memory.get(this.pointer + 1));
                        long val2 = getValue(paramMode2, this.memory.get(this.pointer + 2));
                        long val3 = this.memory.get(this.pointer + 3);
                        this.memory.setAtAddress(val3, val1 < val2 ? 1 : 0);
                        this.pointer += 4;
                    }
                    case 8 -> {
                        long val1 = getValue(paramMode1, this.memory.get(this.pointer + 1));
                        long val2 = getValue(paramMode2, this.memory.get(this.pointer + 2));
                        long val3 = this.memory.get(this.pointer + 3);
                        this.memory.setAtAddress(val3, val1 == val2 ? 1 : 0);
                        this.pointer += 4;
                    }
                    case 99 -> this.halted = true;
                    default -> throw new IntcodeException("Instruction " + this.memory.get(this.pointer) + " is unknown.");
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    private long getValue(int mode, long parameter) throws Exception {
        switch (mode) {
            case 0 -> {
                return this.memory.get(parameter);
            }
            case 1 -> {
                return parameter;
            }
            default -> throw new IntcodeException("Parameter mode " + mode + " does not exist.");
        }
    }

    public ArrayList<Long> getOutput() {
        return output;
    }

    public long takeOutput() {
        return this.output.remove(0);
    }

    public void setInput(long... input) {
        this.input = new ArrayList<>();
        for (long i : input) {
            this.input.add(i);
        }
    }

    public void addInput(long i) {
        this.input.add(i);
    }

    public boolean hasHalted() {
        return this.halted;
    }

    static class IntcodeException extends Exception {
        public IntcodeException(String message) {
            super(message);
        }

        public IntcodeException() { }
    }
}
