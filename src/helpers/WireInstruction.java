package helpers;

import java.util.ArrayList;

public class WireInstruction {
    private String direction;
    private int steps;
    private WireInstruction normalized;
    int startx;
    int starty;
    int weightexcluding;

    public WireInstruction(String instruction) {
        this.direction = instruction.substring(0, 1);
        this.steps = Integer.parseInt(instruction.substring(1));
    }

    public WireInstruction(String direction, int startx, int starty, int steps, int weightexcluding) {
        this.direction = direction;
        this.startx = startx;
        this.starty = starty;
        this.steps = steps;
        this.weightexcluding = weightexcluding;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public WireInstruction getNormalized() {
        if (normalized == null) {
            switch (direction) {
                case "D" -> this.normalized = new WireInstruction("U", startx, starty - steps, steps, weightexcluding);
                case "L" -> this.normalized = new WireInstruction("R", startx - steps, starty, steps, weightexcluding);
                default -> normalized = null;
            }
        }
        return normalized == null ? this : normalized;
    }

    public static void chain(ArrayList<WireInstruction> wireInstructions) {
        WireInstruction previous = null;
        for (WireInstruction instruction : wireInstructions) {
            if (previous == null) {
                instruction.startx = 0;
                instruction.starty = 0;
                instruction.weightexcluding = 0;
            } else {
                switch (previous.direction) {
                    case "U" -> {
                        instruction.startx = previous.startx;
                        instruction.starty = previous.starty + previous.steps;
                    }
                    case "R" -> {
                        instruction.startx = previous.startx + previous.steps;
                        instruction.starty = previous.starty;
                    }
                    case "D" -> {
                        instruction.startx = previous.startx;
                        instruction.starty = previous.starty - previous.steps;
                    }
                    case "L" -> {
                        instruction.startx = previous.startx - previous.steps;
                        instruction.starty = previous.starty;
                    }
                }
                instruction.weightexcluding = previous.weightexcluding + previous.steps;
            }
            previous = instruction;
        }
    }

    public static boolean between(int value, int start, int end) {
        return start <= value && end >= value;
    }

    public Intersection getIntersection(WireInstruction otherInstruction) throws Exception {
        WireInstruction norm1 = getNormalized();
        WireInstruction norm2 = otherInstruction.getNormalized();
        switch (norm1.direction) {
            case "U" -> {
                switch (norm2.direction) {
                    case "U" -> {
                        return null;
                    }
                    case "R" -> {
                        if (between(norm1.startx, norm2.startx, norm2.startx + norm2.steps) && between(norm2.starty, norm1.starty, norm1.starty + norm1.steps)) {
                            return new Intersection(norm1.startx, norm2.starty,
                                    norm1.weightexcluding + (norm1 == this ? norm2.starty - norm1.starty : norm1.starty + norm1.steps - norm2.starty) +
                                            norm2.weightexcluding + (norm2 == otherInstruction ? norm1.startx - norm2.startx : norm2.startx + norm2.steps - norm1.startx));
                        } else {
                            return null;
                        }
                    }
                    default -> throw new Exception("Help");
                }
            }
            case "R" -> {
                switch (norm2.direction) {
                    case "U" -> {
                        if (between(norm2.startx, norm1.startx, norm1.startx + norm1.steps) && between(norm1.starty, norm2.starty, norm2.starty + norm2.steps)) {
                            return new Intersection(norm2.startx, norm1.starty,
                                    norm1.weightexcluding + (norm1 == this ? norm2.startx - norm1.startx : norm2.startx + norm2.steps - norm1.startx ) +
                                            norm2.weightexcluding + (norm2 == otherInstruction ? norm1.starty - norm2.starty : norm2.starty + norm2.steps - norm1.starty));
                        } else {
                            return null;
                        }
                    }
                    case "R" -> {
                        return null;
                    }
                    default -> throw new Exception("Help");
                }
            }
            default -> throw new Exception("Help");
        }
    }
}
