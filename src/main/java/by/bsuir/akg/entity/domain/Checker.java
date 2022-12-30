package by.bsuir.akg.entity.domain;

import by.bsuir.akg.constant.GameObjectDefine;
import by.bsuir.akg.entity.Model;
import by.bsuir.akg.entity.Vertex;

import java.util.List;

public class Checker extends Model {
    private Integer positionX;
    private Integer positionY;

    private GameObjectDefine gameObjectDefine;

    public Checker(List<List<Vertex>> triangles, Integer positionX, Integer positionY, GameObjectDefine gameObjectDefine) {
        super(triangles);
        this.positionX = positionX;
        this.positionY = positionY;
        this.gameObjectDefine = gameObjectDefine;
    }

    public void setPositionX(Integer positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(Integer positionY) {
        this.positionY = positionY;
    }

    public Integer getPositionX() {
        return positionX;
    }

    public Integer getPositionY() {
        return positionY;
    }

    public GameObjectDefine getGameObjectDefine() {
        return gameObjectDefine;
    }
}
