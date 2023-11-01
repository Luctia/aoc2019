package solutions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day8 {
    private String readInput() {
        String line = "";
        try {
            File data = new File("src/data/day8.txt");
            Scanner reader = new Scanner(data);
            while (reader.hasNextLine()) {
                line = reader.nextLine();
            }
        } catch (FileNotFoundException e) {
        }
        return line;
    }

    public int part1() {
        String input = readInput();
        int width = 25;
        int height = 6;
        long leastZeros = 999999999;
        long res = 0;
        for (int i = 0; i < input.length(); i+= width*height) {
            String layerString = input.substring(i, i + width*height);
            List<Integer> layer = new ArrayList<>();
            for (int j = 0; j < layerString.length(); j++) {
                layer.add(Integer.parseInt(layerString.substring(j, j + 1)));
            }
            if (layer.stream().filter(l -> l == 0).count() < leastZeros) {
                leastZeros = layer.stream().filter(l -> l == 0).count();
                res = layer.stream().filter(l -> l == 1).count() * layer.stream().filter(l -> l == 2).count();
            }
        }
        return (int) res;
    }

    public void part2() {
        String input = readInput();
        int width = 25;
        int height = 6;
        List<List<Integer>> layers = new ArrayList<>();
        for (int i = 0; i < input.length(); i+= width*height) {
            String layerString = input.substring(i, i + width*height);
            List<Integer> layer = new ArrayList<>();
            for (int j = 0; j < layerString.length(); j++) {
                layer.add(Integer.parseInt(layerString.substring(j, j + 1)));
            }
            layers.add(layer);
        }
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < width * height; i++) {
            for (List<Integer> layer : layers) {
                if (layer.get(i) == 0) {
                    result.add(0);
                    break;
                }
                if (layer.get(i) == 1) {
                    result.add(1);
                    break;
                }
            }
        }
        for (int i = 0; i < height; i++) {
            System.out.println(result
                    .subList(i * width, i * width + width)
                    .toString()
                    .replaceAll("[\\[\\]]", "")
                    .replaceAll(",", "")
                    .replaceAll("0", " "));
        }
    }
}
