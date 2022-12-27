package by.bsuir.akg.entity;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private final List<Vector> verts;
    private final List<Vector> norms;
    private final List<List<Vertex>> faces;

    public Model(List<Vector> verts, List<Vector> norms, List<List<Vertex>> faces) {
        this.verts = verts;
        this.norms = norms;
        this.faces = faces;
    }

    public List<Vector> getVerts() {
        return verts;
    }

    public List<Vector> getNorms() {
        return norms;
    }

    public List<List<Vertex>> getFaces() {
        return faces;
    }
}
