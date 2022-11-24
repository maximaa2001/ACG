package by.bsuir.akg.entity;

public class Vector {
    public Double x;
    public Double y;
    public Double z;
    public Double w = 1.0;

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

    public Vector normalize4() {
        double sqrt = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2) + Math.pow(w, 2));
        return new Vector(x/sqrt, y/sqrt, z/sqrt, w/sqrt);
    }

    public Vector minus3(Vector vector) {
        return new Vector(x-vector.getX(), y-vector.getY(), z-vector.getZ());
    }

    public Vector plus3(Vector vector) {
        return new Vector(x+vector.getX(), y+vector.getY(), z+vector.getZ());
    }
}
