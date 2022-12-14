package by.bsuir.akg.util;

import by.bsuir.akg.constant.Const;
import by.bsuir.akg.entity.Model;
import by.bsuir.akg.entity.Texture;
import by.bsuir.akg.entity.Vector;
import by.bsuir.akg.entity.Vertex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Vector> verts = new ArrayList<>();
    private final List<Vector> norms = new ArrayList<>();
    private final List<Texture> textures = new ArrayList<>();
    private final List<List<Vertex>> faces = new ArrayList<>();

    public Model readObject() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(Const.PATH_TO_OBJECT));
//        BufferedReader br = new BufferedReader(new FileReader(getClass().getClassLoader().getResource(Const.PATH_TO_OBJECT).toString()));
        String str;
        while ((str = br.readLine()) != null) {
            if (str.startsWith("vn ")) {
                String[] parts = str.split(" +");
                norms.add(new Vector(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3])));
            } else if (str.startsWith("vt ")) {
                String[] parts = str.split(" +");
                textures.add(new Texture(Double.parseDouble(parts[1]),1 - Double.parseDouble(parts[2])));
            } else if (str.startsWith("v ")) {
                String[] parts = str.split(" +");
                verts.add(new Vector(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3])));
            } else if (str.startsWith("f ")) {
                List<Vertex> polygon = new ArrayList<>();
                String[] parts = str.split(" +");
                for (int i = 1; i < parts.length; i++) {
                    String[] split = parts[i].split("/");
                    if (split.length > 1) {
                        polygon.add(new Vertex(verts.get(Integer.parseInt(split[0]) - 1), (split[1].equals("")) ? null : textures.get(Integer.parseInt(split[1]) - 1), (split[2].equals("")) ? new Vector(0.0, 0.0, 0.0) : norms.get(Integer.parseInt(split[2]) - 1)));
                    } else {
                        String[] split2 = parts[i].split("//");
                        if (split2.length > 1) {
                            polygon.add(new Vertex(verts.get(Integer.parseInt(split2[0]) - 1), (split2[1].equals("")) ? null : textures.get(Integer.parseInt(split2[1]) - 1), (split2[2].equals("")) ? new Vector(0.0, 0.0, 0.0) : norms.get(Integer.parseInt(split2[2]) - 1)));
                        }
                    }
                }
                faces.add(polygon);
            }
        }
        List<List<Vertex>> triangles = new ArrayList<>();
        faces.forEach(polygon -> {
            List<List<Vertex>> triangles1 = getTriangles(polygon);
            triangles.addAll(triangles1);
        });
        return new Model(verts, norms, triangles);
    }

    private List<List<Vertex>> getTriangles(List<Vertex> polygon) {
        List<List<Vertex>> triangles = new ArrayList<>();
        if (polygon.size() == 3) {
            triangles.add(polygon);
        } else {
            for (int i = 1; i < polygon.size() - 1; i++) {
                triangles.add(List.of(polygon.get(0), polygon.get(i), polygon.get(i + 1)));
            }
        }
        return triangles;
    }
}
