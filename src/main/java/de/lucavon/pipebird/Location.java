package de.lucavon.pipebird;

public class Location {

    private int x, z;

    public Location(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }
}
