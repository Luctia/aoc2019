import org.junit.jupiter.api.Test;
import solutions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Verify methods give the right answer. Useful to verify continued functionality after changing code.
 */
public class VerifyResults {
    @Test
    public void day1Test() {
        Day1 day = new Day1();
        assertEquals(3313655, day.part1());
        assertEquals(4967616, day.part2());
    }

    @Test
    public void day2Test() {
        Day2 day = new Day2();
        assertEquals(6327510, day.part1());
        assertEquals(4112, day.part2());
    }

//    @Test
//    public void day3Test() {
//        Day3 day = new Day3();
//        assertEquals(207, day.part1());
//        assertEquals(21196, day.part2());
//    }
//
//    @Test
//    public void day4Test() {
//        Day4 day = new Day4();
//        assertEquals(1675, day.part1());
//        assertEquals(1142, day.part2());
//    }

//    @Test
//    public void day5Test() {
//        Day5 day = new Day5();
//        assertEquals(16348437, day.part1());
//        assertEquals(6959377, day.part2());
//    }

    @Test
    public void day6Test() {
        Day6 day = new Day6();
        assertEquals(241064, day.part1());
        assertEquals(418, day.part2());
    }

    @Test
    public void day7Test() {
        Day7 day = new Day7();
        assertEquals(67023, day.part1());
        assertEquals(7818398, day.part2());
    }

    @Test
    public void day8Test() {
        Day8 day = new Day8();
        assertEquals(2125, day.part1());
    }
}
