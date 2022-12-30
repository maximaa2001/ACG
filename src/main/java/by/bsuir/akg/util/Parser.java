package by.bsuir.akg.util;

import by.bsuir.akg.constant.GameObjectDefine;
import by.bsuir.akg.constant.Const;
import by.bsuir.akg.entity.Model;
import by.bsuir.akg.entity.Texture;
import by.bsuir.akg.entity.Vector;
import by.bsuir.akg.entity.Vertex;
import by.bsuir.akg.entity.domain.Board;
import by.bsuir.akg.entity.domain.Checker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Parser {
    private final List<Vector> verts = new ArrayList<>();
    private final List<Vector> norms = new ArrayList<>();
    private final List<Texture> textures = new ArrayList<>();

    private final List<Integer> redNumbers = List.of(1, 4, 7, 10, 13, 16, 19, 22, 34, 40, 43, 46);

    private final List<Integer> blackNumbers = List.of(25, 28, 31, 37, 49, 52, 55, 58, 61, 64, 67, 70);

    public Map<GameObjectDefine, List<? extends Model>> readObject() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(Const.PATH_TO_OBJECT));
        String str;
        while ((str = br.readLine()) != null) {
            if (str.startsWith("#")) {
                continue;
            } else if (str.startsWith("vn ")) { // normals
                String[] parts = str.split(" +");
                norms.add(new Vector(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3])));
            } else if (str.startsWith("vt ")) { // textures
                String[] parts = str.split(" +");
                textures.add(new Texture(Double.parseDouble(parts[1]), 1 - Double.parseDouble(parts[2])));
            } else if (str.startsWith("v ")) { // start points
                String[] parts = str.split(" +");
                verts.add(new Vector(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3])));
            }

        }
        Board board = readBoard();
        Map<GameObjectDefine, List<? extends  Model>> gameObjectsMap = readCheckers();
        gameObjectsMap.put(GameObjectDefine.BOARD, List.of(board));
        return gameObjectsMap;
    }

    private Board readBoard() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(Const.PATH_TO_OBJECT));
        String str;
        List<List<Vertex>> faces = new ArrayList<>();
        while ((str = br.readLine()) != null) {
            if (str.contains("g pCube1")) {
                if ((str = br.readLine()).contains("usemtl initialShadingGroup")) {
                    while ((str = br.readLine()).startsWith("f ")) {
                        faces.add(splitF(str));
                    }
                    break;
                }
            }
        }
        return new Board(getTriangles(faces));
    }

    private Map<GameObjectDefine, List<? extends  Model>> readCheckers() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(Const.PATH_TO_OBJECT));
        String str;
        List<Checker> redCheckers = new ArrayList<>();
        List<Checker> blackCheckers = new ArrayList<>();
        List<List<Vertex>> checkerFaces = new ArrayList<>();
        while ((str = br.readLine()) != null) {
            if (str.contains("g polySurface17")) {
                if ((str = br.readLine()).contains("usemtl initialShadingGroup")) {
                    while ((str = br.readLine()).startsWith("f ")) {
                        checkerFaces.add(splitF(str));
                    }
                    redCheckers.add(new Checker(getTriangles(checkerFaces), 6, 0, GameObjectDefine.RED_CHECKER));
                    while (str != null) {
                        List<List<Vertex>> temp = new ArrayList<>();
                        if (str.startsWith("s ")) {
                            String[] split = str.split(" +");
                            int number = Integer.parseInt(split[1]);
                            while ((str = br.readLine()) != null && str.startsWith("f ")) {
                                temp.add(splitF(str));
                            }
                            if (redNumbers.contains(number)) {
                                redCheckers.add(new Checker(getTriangles(temp), Integer.parseInt(split[2]), Integer.parseInt(split[3]), GameObjectDefine.RED_CHECKER));
                            } else {
                                blackCheckers.add(new Checker(getTriangles(temp), Integer.parseInt(split[2]), Integer.parseInt(split[3]), GameObjectDefine.BLACK_CHECKER));
                            }
                        }
                    }
                }
            }

        }
        Map<GameObjectDefine, List<? extends  Model>> checkers = new HashMap<>();
        {
            checkers.put(GameObjectDefine.RED_CHECKER, redCheckers);
            checkers.put(GameObjectDefine.BLACK_CHECKER, blackCheckers);
        }
        return checkers;
    }

    private List<Vertex> splitF(String str) {
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
        return polygon;
    }

    private List<List<Vertex>> getTriangles(List<List<Vertex>> polygon) {
        return polygon.stream()
                .map(this::dividePolygonOnTriangles)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<List<Vertex>> dividePolygonOnTriangles(List<Vertex> polygon) {
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
