package by.bsuir.akg.entity;

public class Degree {
    private Double degree;

    public Degree(Double degree) {
        this.degree = degree;
    }

    public double toRadian() {
        return degree * Math.PI/ 180f;
    }
}
