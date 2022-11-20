package by.bsuir.akg.entity;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private List<Vector> verts;
    private List<Vector> norms;
    public List<List<Vector>> faces;

    public Model(List<Vector> verts, List<Vector> norms, List<List<Vector>> faces) {
        this.verts = verts;
        this.norms = norms;
        this.faces = faces;
    }

    public void setVerts(List<Vector> verts) {
        this.verts = verts;
    }

    public void setNorms(List<Vector> norms) {
        this.norms = norms;
    }

    public void setFaces(List<List<Vector>> faces) {
        this.faces = faces;
    }

    public List<Vector> getVerts() {
        return verts;
    }

    public List<Vector> getNorms() {
        return norms;
    }

    public List<List<Vector>> getFaces() {
        return faces;
    }
}
