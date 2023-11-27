package helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * A class mimicking a processor running Intcode.
 * @author Luctia
 */
public class IntcodeProcessor {
    /**
     * Memory of the processor.
     */
    private LargeMemory memory;
    private List<Long> output = new ArrayList<>();
    private List<Long> input;
    private long pointer;
    private boolean halted;
    private long relativeBase;

    /**
     * Create a new Intcode processor using an {@link ArrayList} representing memory.
     * @param memory the processors memory
     */
    public IntcodeProcessor(ArrayList<Integer> memory) {
        this.memory = new LargeMemory();
        for (int i = 0; i < memory.size(); i++) {
            this.memory.setAtAddress(i, memory.get(i));
        }
        this.input = new ArrayList<>();
        this.pointer = 0;
        this.halted = false;
        this.relativeBase = 0;
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
        this.input = new ArrayList<>();
        this.pointer = 0;
        this.halted = false;
        this.relativeBase = 0;
    }

    /**
     * Create a new Intcode processor using an array of {@link Long}s representing memory.
     * @param memory the processors memory
     */
    public IntcodeProcessor(Long[] memory) {
        this.memory = new LargeMemory();
        for (int i = 0; i < memory.length; i++) {
            this.memory.setAtAddress(i, memory[i]);
        }
        this.input = new ArrayList<>();
        this.pointer = 0;
        this.halted = false;
        this.relativeBase = 0;
    }

    /**
     * Initialize a new Intcode processor, using the contents of a file as input code.
     * @param fileName the file to take code from
     */
    public IntcodeProcessor(String fileName) {
        String line = "";
        try {
            File data = new File("src/data/" + fileName);
            Scanner reader = new Scanner(data);
            while (reader.hasNextLine()) {
                line = reader.nextLine();
            }
        } catch (FileNotFoundException e) {
        }
        Long[] memory = Arrays.stream(line.split(",")).map(Long::valueOf).toArray(Long[]::new);
        this.memory = new LargeMemory();
        for (int i = 0; i < memory.length; i++) {
            this.memory.setAtAddress(i, memory[i]);
        }
        this.input = new ArrayList<>();
        this.pointer = 0;
        this.halted = false;
        this.relativeBase = 0;
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
     * Set memory at index to value.
     * @param index index to put value
     * @param value value to put at index
     */
    public void setMemoryAt(int index, long value) {
        this.memory.setAtAddress(index, value);
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
                        // Addition
                        long result = getValue(paramMode1, this.memory.get(this.pointer + 1)) + getValue(paramMode2, this.memory.get(this.pointer + 2));
                        this.setValue(paramMode3, this.pointer + 3, result);
                        this.pointer += 4;
                    }
                    case 2 -> {
                        long result = getValue(paramMode1, this.memory.get(this.pointer + 1)) * getValue(paramMode2, this.memory.get(this.pointer + 2));
                        this.setValue(paramMode3, this.pointer + 3, result);
                        this.pointer += 4;
                    }
                    case 3 -> {
                        // Get from input
                        try {
                            this.setValue(paramMode1, this.pointer + 1, this.input.remove(0));
                            this.pointer += 2;
                        } catch (IndexOutOfBoundsException e) {
                            // No input has been found. Terminate program so that it may be restarted later, with the
                            //  pointer on the take-from-input instruction.
                            return;
                        }
                    }
                    case 4 -> {
                        // Add to output
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
                        this.setValue(paramMode3, this.pointer + 3, val1 < val2 ? 1 : 0);
                        this.pointer += 4;
                    }
                    case 8 -> {
                        long val1 = getValue(paramMode1, this.memory.get(this.pointer + 1));
                        long val2 = getValue(paramMode2, this.memory.get(this.pointer + 2));
                        this.setValue(paramMode3, this.pointer + 3, val1 == val2 ? 1 : 0);
                        this.pointer += 4;
                    }
                    case 9 -> {
                        // Adjust relative base
                        this.relativeBase += getValue(paramMode1, this.memory.get(this.pointer + 1));
                        this.pointer += 2;
                    }
                    case 99 -> this.halted = true;
                    default -> throw new IntcodeProcessor.IntcodeException("Instruction " + instruction + " is unknown.");
                }
            } catch (Exception e) {
                System.err.println(e);
                return;
            }
        }
    }

    private long getValue(int mode, long parameter) throws IntcodeProcessor.IntcodeException {
        switch (mode) {
            case 0 -> {
                return this.memory.get(parameter);
            }
            case 1 -> {
                return parameter;
            }
            case 2 -> {
                return this.memory.get(parameter + this.relativeBase);
            }
            default -> throw new IntcodeProcessor.IntcodeException("Parameter mode " + mode + " does not exist.");
        }
    }

    private void setValue(int mode, long parameter, long value) throws IntcodeProcessor.IntcodeException {
        switch (mode) {
            case 0 -> {
                this.memory.setAtAddress(this.memory.get(parameter), value);
            }
            case 1 -> {
                throw new IntcodeProcessor.IntcodeException("Parameter mode 1 is not allowed for writing.");
            }
            case 2 -> {
                this.memory.setAtAddress(this.memory.get(parameter) + this.relativeBase, value);
            }
            default -> throw new IntcodeProcessor.IntcodeException("Parameter mode " + mode + " does not exist.");
        }
    }

    public List<Long> getOutput() {
        return output;
    }

    public long takeOutput() {
        return this.output.remove(0);
    }

    public List<Long> takeAllOutput() {
        List<Long> output = this.output;
        this.output = new ArrayList<>();
        return output;
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

    public void addInput(long[] i) {
        for (long l : i) {
            this.addInput(l);
        }
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
