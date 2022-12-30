package by.bsuir.akg.entity.domain;

import by.bsuir.akg.constant.GameObjectDefine;
import by.bsuir.akg.entity.Model;
import by.bsuir.akg.entity.Vertex;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Board extends Model {
    private Checker[][] board = new Checker[8][8];

    public Board(List<List<Vertex>> triangles) {
        super(triangles);
        for (int i = 0; i < board.length; i++) {
            Arrays.fill(board[i], null);
        }
    }

    public void initRedCheckers(List<Checker> redCheckers) {
        for (Checker checker : redCheckers) {
            insertInvertedValues(checker.getPositionX(), checker.getPositionY(), checker);
        }
    }

    public void initBlackCheckers(List<Checker> blackCheckers) {
        for (Checker checker : blackCheckers) {
            insertInvertedValues(7 - checker.getPositionX(), 7 - checker.getPositionY(), checker);
        }
    }

    public void insertInvertedValues(Integer boardX, Integer boardY, Checker checker) {
        board[7 - boardY][boardX] = checker;
    }

    public Checker getRedElement(Integer positionX, Integer positionY) {
        return board[7 - positionY][positionX];
    }

    public Checker getBlackElement(Integer positionX, Integer positionY) {
        return board[positionY][7 - positionX];
    }

    public Checker[][] getBoard() {
        return board;
    }
}
