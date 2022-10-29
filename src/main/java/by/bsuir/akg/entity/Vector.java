package by.bsuir.akg.entity;

public class Vector {
    private Double x;
    private Double y;
    private Double z;
    private Double w = 1.0;

    public Vector(Double x, Double y, Double z, Double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

    public Double getW() {
        return w;
    }

    public void setW(Double w) {
        this.w = w;
    }
}
