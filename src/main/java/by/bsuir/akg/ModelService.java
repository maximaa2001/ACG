package by.bsuir.akg;

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
        convertService.render(newVectors);
    }
}
