package by.bsuir.akg;

import by.bsuir.akg.constant.Const;
import by.bsuir.akg.entity.Camera;
import by.bsuir.akg.entity.Model;
import by.bsuir.akg.entity.Vector;
import by.bsuir.akg.service.ConvertService;

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
        List<List<Vector>> newVectors = new ArrayList<>();
        List<List<Integer>> faces = model.getFaces();
        List<Vector> verts = model.getVerts();
        for(List<Integer> list : faces) {
            List<Vector> collect = list.stream().map(index -> verts.get(index - 1)).map(vert -> camera.transformToScreenVector(vert)).collect(Collectors.toList());
            newVectors.add(collect);
        }
        ConvertService convertService = ConvertService.getInstance(null);
        convertService.clear();
        for(List<Vector> list : newVectors) {
            for (int i = 0; i < list.size(); i++) {
                Vector startPoint = null;
                Vector endPoint = null;
                if ((i + 1) < list.size()) {
                    startPoint =list.get(i);
                    endPoint =list.get(i + 1);
                } else {
                    startPoint = list.get(i);
                    endPoint = list.get(0);
                }

                convertService.drawDda(startPoint.getX().intValue(), startPoint.getY().intValue(), endPoint.getX().intValue(), endPoint.getY().intValue());
                  //  renderController.getPanel().repaint();

            }
        }
        convertService.repaint();
    }
}
