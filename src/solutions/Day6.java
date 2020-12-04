package solutions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class Day6 {
    public void part1() {
        HashMap<String, String> childrenToParents = getHashMap();
        System.out.println("Solution to part 1: " + calculateOrbitCount(childrenToParents));
    }

    public void part2() {
        HashMap<String, String> childrenToParents = getHashMap();
        ArrayList<String> meToRoot = getLinkToRoot(childrenToParents, "COM", "YOU");
        ArrayList<String> santaToRoot = getLinkToRoot(childrenToParents, "COM", "SAN");
        int res = -1;
        Collections.reverse(meToRoot);
        Collections.reverse(santaToRoot);
        int minSize = Math.min(meToRoot.size(), santaToRoot.size());
        for (int i = 0; i < minSize; i++) {
            if (!meToRoot.get(i).equals(santaToRoot.get(i))) {
                res = meToRoot.size() + santaToRoot.size() - 2 * i - 2;
                break;
            }
        }
        System.out.println("Solution to part 2: " + res);
    }

    private HashMap<String, String> getHashMap() {
        HashMap<String, String> childrenToParents = new HashMap<>();
        try {
            File data = new File("src/data/day6.txt");
            Scanner reader = new Scanner(data);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                int indexOfParentheses = line.indexOf(')');
                String parent = line.substring(0, indexOfParentheses);
                String child = line.substring(indexOfParentheses + 1);
                childrenToParents.put(child, parent);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Day6 data file not found.");
        }
        return childrenToParents;
    }

    private static int calculateOrbitCount(HashMap<String, String> childrenToParents) {
        int res = 0;
        for (String child : childrenToParents.keySet()) {
            res += getTotalLinkLength(childrenToParents, child);
        }
        return res;
    }

    private static ArrayList<String> getLinkToRoot(HashMap<String, String> childrenToParents, String root, String from) {
        ArrayList<String> res = new ArrayList<>();
        res.add(from);
        if (!from.equals(root)) {
            res.addAll(getLinkToRoot(childrenToParents, root, childrenToParents.get(from)));
        }
        return res;
    }

    private static int getTotalLinkLength(HashMap<String, String> childrenToParents, String child) {
        if (childrenToParents.containsKey(child)) {
            return 1 + getTotalLinkLength(childrenToParents, childrenToParents.get(child));
        } else {
            return 0;
        }
    }
}
