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
    private ArrayList<Integer> memory;
    private ArrayList<Integer> output = new ArrayList<>();
    private ArrayList<Integer> input;
    private int pointer;
    private boolean halted;

    /**
     * Create a new Intcode processor using an {@link ArrayList} representing memory.
     * @param memory the processors memory
     */
    public IntcodeProcessor(ArrayList<Integer> memory) {
        this.memory = memory;
        this.pointer = 0;
        this.halted = false;
    }

    /**
     * Create a new Intcode processor using an array of {@link Integer}s representing memory.
     * @param memory the processors memory
     */
    public IntcodeProcessor(Integer[] memory) {
        this.memory = new ArrayList<Integer>(Arrays.asList(memory));
        this.pointer = 0;
        this.halted = false;
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
    public void run() {
        int result;
        int location;
        while (!this.halted) {
            int instruction = this.memory.get(this.pointer);
            int paramMode1 = (instruction / 100) % 10;
            int paramMode2 = (instruction / 1000) % 10;
            int paramMode3 = (instruction / 10000) % 10;
            int opcode = instruction % 100;
            try {
                switch (opcode) {
                    case 1 -> {
                        result = getValue(paramMode1, this.memory.get(this.pointer + 1)) + getValue(paramMode2, this.memory.get(this.pointer + 2));
                        location = this.memory.get(this.pointer + 3);
                        this.memory.set(location, result);
                        this.pointer += 4;
                    }
                    case 2 -> {
                        result = getValue(paramMode1, this.memory.get(this.pointer + 1)) * getValue(paramMode2, this.memory.get(this.pointer + 2));
                        location = this.memory.get(this.pointer + 3);
                        this.memory.set(location, result);
                        this.pointer += 4;
                    }
                    case 3 -> {
                        try {
                            this.memory.set(this.memory.get(this.pointer + 1), this.input.remove(0));
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
                        int value = getValue(paramMode1, this.memory.get(this.pointer + 1));
                        int jumpTo = getValue(paramMode2, this.memory.get(this.pointer + 2));
                        this.pointer = (value != 0) ? jumpTo : this.pointer + 3;
                    }
                    case 6 -> {
                        int value = getValue(paramMode1, this.memory.get(this.pointer + 1));
                        int jumpTo = getValue(paramMode2, this.memory.get(this.pointer + 2));
                        this.pointer = (value == 0) ? jumpTo : this.pointer + 3;
                    }
                    case 7 -> {
                        int val1 = getValue(paramMode1, this.memory.get(this.pointer + 1));
                        int val2 = getValue(paramMode2, this.memory.get(this.pointer + 2));
                        int val3 = this.memory.get(this.pointer + 3);
                        this.memory.set(val3, val1 < val2 ? 1 : 0);
                        this.pointer += 4;
                    }
                    case 8 -> {
                        int val1 = getValue(paramMode1, this.memory.get(this.pointer + 1));
                        int val2 = getValue(paramMode2, this.memory.get(this.pointer + 2));
                        int val3 = this.memory.get(this.pointer + 3);
                        this.memory.set(val3, val1 == val2 ? 1 : 0);
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

    private int getValue(int mode, int parameter) throws Exception {
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

    public ArrayList<Integer> getOutput() {
        return output;
    }

    public Integer takeOutput() {
        return this.output.remove(0);
    }

    public void setInput(int... input) {
        this.input = new ArrayList<>();
        for (int i : input) {
            this.input.add(i);
        }
    }

    public void addInput(int i) {
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
