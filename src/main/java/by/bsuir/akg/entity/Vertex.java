package by.bsuir.akg.entity;

public class Vertex {
    public Vector position;
    public Vector normal;

    public Vector position_screen = null;


    public Vertex(Vector position, Vector normal){
        this.position = position;
        this.normal = normal;
    }

    public void setPositionScreen(Vector position_screen){
        this.position_screen = position_screen;
    }
}
