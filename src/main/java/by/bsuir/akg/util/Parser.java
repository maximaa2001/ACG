package by.bsuir.akg.util;

import by.bsuir.akg.constant.Const;
import by.bsuir.akg.entity.Model;
import by.bsuir.akg.entity.Vector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private List<Vector> verts = new ArrayList<>();
    private List<Vector> norms = new ArrayList<>();
    private List<List<Integer>> faces = new ArrayList<>();

    public Model readObject() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(Const.PATH_TO_OBJECT));
        String str = null;
        while ((str = br.readLine()) != null) {
            if(str.startsWith("vn")) {
                String[] parts = str.split(" +");
                norms.add(new Vector(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]),Double.parseDouble(parts[3])));
            } else if(str.startsWith("v")) {
                String[] parts = str.split(" +");
                verts.add(new Vector(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]),Double.parseDouble(parts[3])));
            } else if(str.startsWith("f")) {
                List<Integer> polygon = new ArrayList<>();
                String[] parts = str.split(" +");
                for (int i = 1; i < parts.length; i++) {
                    String[] split = parts[i].split("/");
                    if(split.length > 1) {
                        polygon.add(Integer.parseInt(split[0]));
                    } else {
                        String[] split2 = parts[i].split("//");
                        if(split2.length > 1) {
                            polygon.add(Integer.parseInt(split2[0]));
                        }
                    }
                }
                faces.add(polygon);
            }
        }
        return new Model(verts, norms, faces);
    }
}
