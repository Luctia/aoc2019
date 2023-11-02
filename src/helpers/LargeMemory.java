package helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LargeMemory {
    private Map<Long, Long> memory;

    public LargeMemory() {
        this.memory = new HashMap<>();
    }

    public long get(long address) {
        if (this.memory.get(address) != null) {
            return this.memory.get(address);
        } else {
            return 0;
        }
    }

    public void setAtAddress(long address, long value) {
        this.memory.put(address, value);
    }

    public void addToMemory(long value) {
        long address = this.memory.keySet().stream().mapToLong(l -> l).max().orElse(0);
        this.memory.put(address, value);
    }

    public void addAll(ArrayList<Long> newMemory) {
        for (Long value : newMemory) {
            this.addToMemory(value);
        }
    }
}
