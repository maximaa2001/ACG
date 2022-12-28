package by.bsuir.akg.entity;

public class Vertex {
    private Vector position;
    private Vector normal;
    private Texture texture;
    private Vector positionScreen;
    private Double w;

    public Vertex(Vector position,Texture texture, Vector normal){
        this.position = position;
        this.texture = texture;
        this.normal = normal;
    }

    public Vector getPosition() {
        return position;
    }

    public void setW(Double w) {
        this.w = w;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public void setPositionScreen(Vector positionScreen) {
        this.positionScreen = positionScreen;
    }

    public Vector getPositionScreen() {
        return positionScreen;
    }

    public Texture getTexture() {
        return texture;
    }

    public Double getW() {
        return w;
    }

    public Vector getNormal() {
        return normal;
    }
}
