package by.bsuir.akg;

import by.bsuir.akg.entity.Camera;
import by.bsuir.akg.entity.Model;
import by.bsuir.akg.entity.Vector;
import by.bsuir.akg.service.DrawService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModelService {
    private Model model;
    private Camera camera;

    public ModelService(Model model, Camera camera) {
        this.model = model;
        this.camera = camera;
    }

    public void render() {
        camera.updateScreenMatrix();
        List<List<Vector>> newTriangles = new ArrayList<>();
        List<List<Vector>> triangles = model.getFaces();
       // List<Vector> verts = model.getVerts();
        for (List<Vector> list : triangles) {
            List<Vector> collect = list.stream().map(vert -> camera.transformToScreenVector(vert)).collect(Collectors.toList());
            newTriangles.add(collect);
        }
        DrawService drawService = DrawService.getInstance(null);
        drawService.clear();
        newTriangles.forEach(drawService::drawTriangle);

//        for (List<Vector> list : newVectors) {
//            for (int i = 0; i < list.size(); i++) {
//                Vector startPoint = null;
//                Vector endPoint = null;
//                if ((i + 1) < list.size()) {
//                    startPoint = list.get(i);
//                    endPoint = list.get(i + 1);
//                } else {
//                    startPoint = list.get(i);
//                    endPoint = list.get(0);
//                }
//                drawService.drawDda(startPoint.getX().intValue(), startPoint.getY().intValue(), endPoint.getX().intValue(), endPoint.getY().intValue());
//            }
//        }
        drawService.repaint();
    }
}
