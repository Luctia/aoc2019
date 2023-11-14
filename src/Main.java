import solutions.*;

/**
 * From this file, all solutions are run.
 * @author Luctia
 */
public class Main {
    public static void main(String[] args) {
        Day1 day1 = new Day1();
        System.out.println(day1.part1());
        System.out.println(day1.part2());

        Day2 day2 = new Day2();
        System.out.println(day2.part1());
        System.out.println(day2.part2());

        Day3 day3 = new Day3();
        try {
            day3.part1();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Day4 day4 = new Day4();
        day4.part1();

        Day5 day5 = new Day5();
        System.out.println(day5.part1());
        System.out.println(day5.part2());

        Day6 day6 = new Day6();
        System.out.println(day6.part1());
        System.out.println(day6.part2());

        Day7 day7 = new Day7();
        System.out.println(day7.part1());
        System.out.println(day7.part2());

        Day8 day8 = new Day8();
        System.out.println(day8.part1());
        day8.part2();

        Day9 day9 = new Day9();
        System.out.println(day9.part1());
        System.out.println(day9.part2());

        Day10 day10 = new Day10();
        System.out.println(day10.part1());
        System.out.println(day10.part2());

        Day11 day11 = new Day11();
        System.out.println(day11.part1());
        System.out.println(day11.part2());

        Day12 day12 = new Day12();
        System.out.println(day12.part1());
        System.out.println(day12.part2());
    }
}
