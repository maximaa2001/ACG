package by.bsuir.akg.entity;

public class Vertex {
    public Vector position;
    public Vector normal;
    public Texture texture;

    public Vector position_screen = null;

    public Double w = null;


    public Vertex(Vector position,Texture texture, Vector normal){
        this.position = position;
        this.texture = texture;
        this.normal = normal;
    }

    public void setPositionScreen(Vector position_screen){
        this.position_screen = position_screen;
    }
}
