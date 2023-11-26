package solutions;

import java.util.ArrayList;

public class Day4 {
    public void part1() {
        System.out.println(new Password(172930, 683082).getValidPasswords().size());
    }

    private static class Password {
        int start;
        int max;

        public Password(int start, int max) {
            this.start = start;
            this.max = max;
        }

        public ArrayList<Integer> getValidPasswords() {
            ArrayList<Integer> res = new ArrayList<>();
            for (int i = this.start; i <= this.max; i++) {
                if (isValidPassword(i)) {
                    res.add(i);
                }
            }
            return res;
        }

        private static boolean isValidPassword(int password) {
            String pw = Integer.toString(password);
            int prev = 0;
            int cons = 0;
            for (char c : pw.toCharArray()) {
                int parsed = Integer.parseInt(""+c);
                if (parsed < prev) {
                    return false;
                } else if (parsed == prev && countInString(pw, c) == 2) {
                    cons++;
                }
                prev = parsed;
            }
            return cons > 0;
        }

        private static int countInString(String input, char needle) {
            int res = 0;
            for (char c : input.toCharArray()) {
                if (c == needle) res++;
            }
            return res;
        }
    }
}
