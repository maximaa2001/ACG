package by.bsuir.akg.entity;

public class Vector {
    private final Double x;
    private final Double y;
    private final Double z;
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


    public Double getY() {
        return y;
    }


    public Double getZ() {
        return z;
    }


    public Double getW() {
        return w;
    }

    public void setW(Double w) {
        this.w = w;
    }


    public Vector plus3(Vector vector) {
        return new Vector(x + vector.getX(), y + vector.getY(), z + vector.getZ());
    }
}
