package helpers;

import java.util.Objects;

public class Coordinates {
    public int x, y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double manhattanDistance(Coordinates from) {
        double x = Math.pow(this.x - from.x, 2);
        double y = Math.pow(this.y - from.y, 2);
        return Math.sqrt(x + y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return this.x + ", " + this.y;
    }
}
