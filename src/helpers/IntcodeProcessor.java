package helpers;

import java.util.Arrays;

/**
 * A class mimicking a processor
 */
public class IntcodeProcessor {
    private int[] code;

    public IntcodeProcessor() { }

    public IntcodeProcessor(int[] code) {
        this.code = code;
    }

    public void setCode(int[] newcode) {
        this.code = newcode;
    }

    public int[] getCode() {
        return this.code;
    }

    public void run() throws Exception {
        int index = 0;
        int result = 0;
        int location = 0;
        boolean finished = false;
        while (!finished) {
            switch (this.code[index]) {
                case 1:
                    result = this.code[this.code[index + 1]] + this.code[this.code[index + 2]];
                    location = this.code[index + 3];
                    this.code[location] = result;
                    index += 4;
                    break;
                case 2:
                    result = this.code[this.code[index + 1]] * this.code[this.code[index + 2]];
                    location = this.code[index + 3];
                    this.code[location] = result;
                    index += 4;
                    break;
                case 99:
                    finished = true;
                    break;
                default:
                    throw new Exception("Operator " + this.code[index] + " is unknown.");
            }
        }
        System.out.println("Processor finished, memory: " + Arrays.toString(this.code));
    }
}
