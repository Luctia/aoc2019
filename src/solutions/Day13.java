package solutions;

import helpers.IntcodeProcessor;

import java.util.List;
import java.util.Scanner;

public class Day13 {
    private Scanner scanner;

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

    public long part2() {
        this.scanner = new Scanner(System.in);
        IntcodeProcessor proc = new IntcodeProcessor("day13.txt");
        proc.setMemoryAt(0, 2);
        return playGame(proc, true);
    }

    public long playGame(IntcodeProcessor proc, boolean automatic) {
        proc.run();
        BoardPrinter printer = new BoardPrinter(proc.takeAllOutput());
        if (automatic) {
            while (!proc.hasHalted()) {
                proc.addInput(printer.makeAutomaticMove());
                proc.run();
                printer.updateBoard(proc.takeAllOutput());
            }
        } else {
            while (!proc.hasHalted()) {
                printer.printBoard();
                proc.addInput(takeInput());
                proc.run();
                printer.updateBoard(proc.takeAllOutput());
            }
        }
        return printer.getScore();
    }

    private long takeInput() {
        char input = 'x';
        try {
            input = scanner.nextLine().charAt(0);
        } catch (StringIndexOutOfBoundsException e) {
            return 0;
        }
        return switch (input) {
            case 'a' -> -1;
            case 'd' -> 1;
            default -> 0;
        };
    }

    private static class BoardPrinter {
        private final int width, height;
        private final List<Long> board;
        private long score = 0;

        public BoardPrinter(List<Long> board) {
            // In this function, I explicitly assume that the tiles in the output are NOT ordered ltr, ttb.
            this.board = board;
            long lines = 0;
            long columns = 0;
            for (int i = 1; i < board.size(); i+=3) {
                if (board.get(i) > lines) {
                    lines = board.get(i);
                }
            }
            for (int i = 0; i < board.size(); i+=3) {
                if (board.get(i) > columns) {
                    columns = board.get(i);
                }
            }
            this.width = (int) columns;
            this.height = (int) lines;
        }

        public void printBoard() {
            StringBuilder representation = new StringBuilder();
            for (int y = 0; y <= height; y++) {
                StringBuilder line = new StringBuilder();
                for (int x = 0; x <= width; x++) {
                    long tileId = getTileId(x, y, board);
                    switch ((int) tileId) {
                        case 1 -> line.append("X");
                        case 2 -> line.append("#");
                        case 3 -> line.append("_");
                        case 4 -> line.append("o");
                        default -> line.append(" ");
                    }
                }
                representation.append(line).append("\n");
            }
            System.out.println(representation);
        }

        public long getTileId(long x, long y, List<Long> board) {
            for (int i = 0; i < board.size(); i+=3) {
                if (board.get(i) == x && board.get(i + 1) == y) {
                    return board.get(i + 2);
                }
            }
            return -1;
        }

        public void updateBoard(List<Long> updatedTiles) {
            for (int i = 0; i < updatedTiles.size(); i+=3) {
                long x = updatedTiles.get(i);
                long y = updatedTiles.get(i + 1);
                this.updateTile(x, y, updatedTiles.get(i + 2));
            }
        }

        private void updateTile(long x, long y, long newValue) {
            if (x == -1) {
                this.score = newValue;
                return;
            }
            for (int i = 0; i < this.board.size(); i+=3) {
                if (this.board.get(i) == x && this.board.get(i + 1) == y) {
                    this.board.set(i + 2, newValue);
                    return;
                }
            }
        }

        public long getScore() {
            return this.score;
        }

        public long makeAutomaticMove() {
            long xOfBall = 0;
            for (int i = 2; i < board.size(); i+=3) {
                if (board.get(i) == 4) {
                    xOfBall = board.get(i - 2);
                    break;
                }
            }
            long xOfPaddle = 0;
            for (int i = 2; i < board.size(); i+=3) {
                if (board.get(i) == 3) {
                    xOfPaddle = board.get(i - 2);
                    break;
                }
            }
            return Long.compare(xOfBall, xOfPaddle);
        }
    }
}
