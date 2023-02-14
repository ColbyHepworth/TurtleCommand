package world;


import com.google.gson.annotations.SerializedName;

import java.util.Map;
import java.util.Objects;

public class Position implements Comparable<Position> {

    @SerializedName("x")
    private final int X;

    @SerializedName("y")
    private final int Y;

    @SerializedName("z")
    private final int Z;

    public Position(int x, int y, int z) {
        this.X = x;
        this.Y = y;
        this.Z = z;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public int getZ() {
        return Z;
    }

    public void printPosition() {
        System.out.println("{" + X + ", " + Y + ", " + Z + "},");
    }

    public int distanceTo(Position other) {
        return Math.abs(X - other.X) + Math.abs(Y - other.Y) + Math.abs(Z - other.Z);
    }


    @Override
    public String toString() {
       return Map.of("x", X, "y", Y, "z", Z).toString();
    }

    @Override
    public int compareTo(Position other) {
        return Integer.compare(X, other.X);
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Position that = (Position) other;
        return X == that.X &&
                Y == that.Y &&
                Z == that.Z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(X, Y, Z);
    }
}
