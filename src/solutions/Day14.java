package solutions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day14 {
    private List<Reaction> getReactions() {
        List<Reaction> reactions = new ArrayList<>();
        try {
            File data = new File("src/data/day14.txt");
            Scanner reader = new Scanner(data);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                reactions.add(new Reaction(line));
            }
            return reactions;
        } catch (FileNotFoundException e) {
            System.out.println("Day 14 data file not found.");
        }
        return null;
    }

    public long part1() {
        return calculateMinimalOre(getReactions(), new HashMap<>(), "FUEL", 1).amount;
    }

    /**
     * Calculates the minimal amount of ore required to create a certain material, given some recipes.
     * @param reactions the possible reactions
     * @param leftovers what we have left
     * @param material the material to create
     * @param amountNeeded how much of the material we need
     * @return the minimal amount of ore required to create the material
     */
    private ReactionResult calculateMinimalOre(List<Reaction> reactions, Map<String, Long> leftovers, String material, long amountNeeded) {
        List<Long> amounts = new ArrayList<>();
        List<Map<String, Long>> leftOverList = new ArrayList<>();
        List<Reaction> reactionsResultingInMaterial = reactions.stream().filter(r -> r.result.equals(material)).toList();
        // We need *material*, so we look through all reactions that have that as a result.
        for (Reaction reaction : reactionsResultingInMaterial) {
            // We reset the leftovers for every different recipe.
            long oreRequired = 0;
            long repeatReactionTimes = Math.ceilDiv(amountNeeded, reaction.resultAmount);
            long resultingAmount = repeatReactionTimes * reaction.resultAmount;
            for (int i = 0; i < reaction.requiredAmounts.size(); i++) {
                // For every ingredient...
                String ingredient = reaction.requiredComponents.get(i);
                long ingredientAmount = reaction.requiredAmounts.get(i);
                if (ingredient.equals("ORE")) {
                    oreRequired += ingredientAmount * repeatReactionTimes;
                } else {
                    // If it's not ore, we need another reaction to get this ingredient
                    // We need *ingredientAmount* of this ingredient for this recipe
                    // However, we should check whether we already have some of this ingredient left over.
                    if (leftovers.containsKey(ingredient) && leftovers.get(ingredient) >= (ingredientAmount * repeatReactionTimes)) {
                        // We already have enough of this ingredient. Thus, take it from the leftovers and don't add any
                        //  extra required ore.
                        leftovers.put(ingredient, leftovers.get(ingredient) - (ingredientAmount * repeatReactionTimes));
                    } else {
                        // While we might still have some of this ingredient left over, it's not enough, so we must make
                        //  more. To be precise, we must make *amountNeeded* - what we have left over.
                        long amountLeftOver = leftovers.getOrDefault(ingredient, 0L);
                        // If we have some material left over, we should take all that. This means we need to remove it from our leftovers.
                        leftovers.put(ingredient, 0L);
                        ReactionResult result = calculateMinimalOre(reactions, leftovers, ingredient, (ingredientAmount * repeatReactionTimes) - amountLeftOver);
                        oreRequired += result.amount;
                        leftovers = result.leftovers;
                    }
                }
            }
            leftovers.put(material, leftovers.getOrDefault(material, 0L) + resultingAmount - amountNeeded);
            amounts.add(oreRequired);
            leftOverList.add(leftovers);
        }
        long minimum = amounts.get(0);
        Map<String, Long> correspondingLeftovers = leftOverList.get(0);
        for (int i = 1; i < amounts.size(); i++) {
            if (amounts.get(i) < minimum) {
                minimum = amounts.get(i);
                correspondingLeftovers = leftOverList.get(0);
            }
        }
        return new ReactionResult(minimum, correspondingLeftovers);
    }

    public long part2() {
        long opf = this.part1();
        long ore = 1000000000000L;
        Map<String, Long> leftovers = new HashMap<>();
        List<Reaction> reactions = getReactions();
        // We know that the unoptimized ore-per-fuel equals 178154. Thus, we can calculate a minimum that should
        //  definitely be doable.
        long minimum = Math.ceilDiv(ore, opf);
        assert reactions != null;
        ReactionResult result = calculateMinimalOre(reactions, leftovers, "FUEL", minimum);
        leftovers = result.leftovers;
        ore = ore - result.amount;
        long fuel = minimum;
        while (ore > 0) {
            if (ore - calculateMinimalOre(reactions, leftovers, "FUEL", minimum).amount > 0) {
                result = calculateMinimalOre(reactions, leftovers, "FUEL", minimum);
                leftovers = result.leftovers;
                ore = ore - result.amount;
                fuel = fuel + minimum;
            } else {
                minimum = Math.ceilDiv(minimum, 2);
            }
        }
        return fuel;
    }

    private record ReactionResult(long amount, Map<String, Long> leftovers) {
        public ReactionResult(long amount, Map<String, Long> leftovers) {
            this.amount = amount;
            this.leftovers = leftovers;
        }
    }

    private static class Reaction {
        private final String result;
        private final int resultAmount;
        private final List<String> requiredComponents;
        private final List<Integer> requiredAmounts;

        public Reaction(String reaction) {
            String result = reaction.split(" => ")[1];
            this.resultAmount = Integer.parseInt(result.split(" ")[0]);
            this.result = result.split(" ")[1];
            this.requiredAmounts = new ArrayList<>();
            this.requiredComponents = new ArrayList<>();
            String[] requirements = reaction.split("=>")[0].split(", ");
            for (String requirement : requirements) {
                this.requiredComponents.add(requirement.split(" ")[1]);
                this.requiredAmounts.add(Integer.parseInt(requirement.split(" ")[0]));
            }
        }
    }
}
