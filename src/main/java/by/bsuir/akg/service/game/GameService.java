package by.bsuir.akg.service.game;

import by.bsuir.akg.Game;
import by.bsuir.akg.constant.DirectionDefine;
import by.bsuir.akg.constant.GameObjectDefine;
import by.bsuir.akg.constant.TypeDefine;
import by.bsuir.akg.entity.Camera;
import by.bsuir.akg.entity.Gamer;
import by.bsuir.akg.entity.Model;
import by.bsuir.akg.entity.domain.Board;
import by.bsuir.akg.entity.domain.Checker;
import by.bsuir.akg.util.ThreadHelper;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class GameService {
    private final Gamer redGamer;
    private final Gamer blackGamer;
    private final Board board;
    private final Game game = Game.getGame(null);
    private Gamer mainGamer;
    private Checker chooseChecker;
    private Integer yellowCheckerIndex;
    private List<DirectionDefine> directions;
    private TypeDefine type;
    private Camera camera;

    public GameService(Map<GameObjectDefine, List<? extends Model>> models, Stage stage, Camera camera) {
        this.camera = camera;
        redGamer = new Gamer(GameObjectDefine.RED_CHECKER, sort((List<Checker>) models.get(GameObjectDefine.RED_CHECKER)));
        blackGamer = new Gamer(GameObjectDefine.BLACK_CHECKER, sort((List<Checker>) models.get(GameObjectDefine.BLACK_CHECKER)));
        this.board = (Board) models.get(GameObjectDefine.BOARD).get(0);
        mainGamer = redGamer;
        board.initRedCheckers(redGamer.getCheckers());
        board.initBlackCheckers(blackGamer.getCheckers());
        keyHandle(stage);
    }

    private List<Checker> sort(List<Checker> checkers) {
        List<Checker> sortedList = new ArrayList<>();
        Map<Integer, List<Checker>> y2Checkers = checkers.stream().collect(Collectors.groupingBy(Checker::getPositionY));
        Optional<Checker> max = checkers.stream().max(Comparator.comparing(Checker::getPositionY));
        for (int i = max.get().getPositionY(); i >= 0; i--) {
            List<Checker> test = y2Checkers.get(i);
            if (test != null) {
                sortedList.addAll(test
                        .stream()
                        .sorted(Comparator.comparing(Checker::getPositionX))
                        .collect(Collectors.toList()));
            }
        }
        return sortedList;
    }

    private void keyHandle(Stage stage) {
        stage.getScene().setOnKeyPressed(new javafx.event.EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case Z -> {
                        if (chooseChecker == null) {
                            yellowCheckerIndex = (yellowCheckerIndex == null ||
                                    yellowCheckerIndex == 0) ? mainGamer.getCheckers().size() - 1 : yellowCheckerIndex - 1;
                            clearColors();
                            mainGamer.getCheckers().get(yellowCheckerIndex).setCustomColor(Color.YELLOW);
                            game.render();
                        }
                        break;
                    }
                    case X -> {
                        if (chooseChecker == null) {
                            yellowCheckerIndex = (yellowCheckerIndex == null ||
                                    yellowCheckerIndex == mainGamer.getCheckers().size() - 1) ? 0 : yellowCheckerIndex + 1;
                            clearColors();
                            mainGamer.getCheckers().get(yellowCheckerIndex).setCustomColor(Color.YELLOW);
                            game.render();
                        }
                        break;
                    }
                    case SPACE -> {
                        chooseChecker = mainGamer.getCheckers().get(yellowCheckerIndex);
                        chooseChecker.setCustomColor(Color.CYAN);
                        List<Checker> strictMove = findStrictMove();
                        if (!strictMove.isEmpty() && !strictMove.contains(chooseChecker)) {
                            clearColors();
                            clearChoose();
                        } else if (!strictMove.isEmpty()) {
                            type = TypeDefine.ATTACK;
                            directions = findDirectionsToAttack();
                        } else {
                            type = TypeDefine.MOVE;
                            directions = findPossibleDirectionsToMove();
                            if (directions.isEmpty()) {
                                clearColors();
                                clearChoose();
                            }
                        }
                        System.out.println(type);
                        System.out.println(directions);

                        game.render();
                        ThreadHelper.sleep(1L);
                        break;
                    }
                    case E -> {
                        if (chooseChecker != null) {
                            if (directions != null && directions.contains(DirectionDefine.RIGHT_UP)) {
                                switch (type) {
                                    case MOVE: {
                                        if (mainGamer.equals(redGamer)) {
                                            chooseChecker.translate(4.12, 0, -4.12);
                                        } else {
                                            chooseChecker.translate(-4.12, 0, 4.12);
                                        }
                                        changePosition(chooseChecker.getPositionX(), chooseChecker.getPositionX() + 1, chooseChecker.getPositionY(), chooseChecker.getPositionY() + 1);
                                        clearColors();
                                        clearChoose();
                                        mainGamer = getOtherGamer();
                                        camera.rotateY(180);
                                        break;
                                    }
                                    case ATTACK: {
                                        Checker element = parse(chooseChecker.getPositionX() + 1, chooseChecker.getPositionY() + 1);
                                        removeChecker(element);
                                        if (mainGamer.equals(redGamer)) {
                                            chooseChecker.translate(8.24, 0, -8.24);
                                        } else {
                                            chooseChecker.translate(-8.24, 0, 8.24);
                                        }
                                        changePosition(chooseChecker.getPositionX(), chooseChecker.getPositionX() + 2, chooseChecker.getPositionY(), chooseChecker.getPositionY() + 2);
                                        List<Checker> strictMove = findStrictMove();
                                        if(!strictMove.contains(chooseChecker)) {
                                            clearColors();
                                            clearChoose();
                                            mainGamer = getOtherGamer();
                                            camera.rotateY(180);
                                        } else {
                                            directions = findDirectionsToAttack();
                                            System.out.println(directions);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        game.render();
                        ThreadHelper.sleep(1L);
                    }
                    case Q -> {
                        if (chooseChecker != null) {
                            if (directions != null && directions.contains(DirectionDefine.LEFT_UP)) {
                                switch (type) {
                                    case MOVE: {
                                        if (mainGamer.equals(redGamer)) {
                                            chooseChecker.translate(-4.12, 0, -4.12);
                                        } else {
                                            chooseChecker.translate(4.12, 0, 4.12);
                                        }
                                        changePosition(chooseChecker.getPositionX(), chooseChecker.getPositionX() - 1, chooseChecker.getPositionY(), chooseChecker.getPositionY() + 1);
                                        clearColors();
                                        clearChoose();
                                        mainGamer = getOtherGamer();
                                        camera.rotateY(180);
                                        break;
                                    }
                                    case ATTACK: {
                                        Checker element = parse(chooseChecker.getPositionX() - 1, chooseChecker.getPositionY() + 1);
                                        removeChecker(element);
                                        if (mainGamer.equals(redGamer)) {
                                            chooseChecker.translate(-8.22, 0, -8.22);
                                        } else {
                                            chooseChecker.translate(8.22, 0, 8.22);
                                        }
                                        changePosition(chooseChecker.getPositionX(), chooseChecker.getPositionX() - 2, chooseChecker.getPositionY(), chooseChecker.getPositionY() + 2);
                                        List<Checker> strictMove = findStrictMove();
                                        if(!strictMove.contains(chooseChecker)) {
                                            clearColors();
                                            clearChoose();
                                            mainGamer = getOtherGamer();
                                            camera.rotateY(180);
                                        } else {
                                            directions = findDirectionsToAttack();
                                            System.out.println(directions);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        game.render();
                        ThreadHelper.sleep(1L);
                        break;
                    }
                    case A -> {
                        if (chooseChecker != null) {
                            if (directions != null && directions.contains(DirectionDefine.LEFT_BOTTOM)) {
                                switch (type) {
                                    case ATTACK: {
                                        Checker element = parse(chooseChecker.getPositionX() - 1, chooseChecker.getPositionY() - 1);
                                        removeChecker(element);
                                        if (mainGamer.equals(redGamer)) {
                                            chooseChecker.translate(-8.22, 0, 8.22);
                                        } else {
                                            chooseChecker.translate(8.22, 0, -8.22);
                                        }
                                        changePosition(chooseChecker.getPositionX(), chooseChecker.getPositionX() - 2, chooseChecker.getPositionY(), chooseChecker.getPositionY() - 2);
                                        List<Checker> strictMove = findStrictMove();
                                        if(!strictMove.contains(chooseChecker)) {
                                            clearColors();
                                            clearChoose();
                                            mainGamer = getOtherGamer();
                                            camera.rotateY(180);
                                        } else {
                                            directions = findDirectionsToAttack();
                                            System.out.println(directions);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        game.render();
                        ThreadHelper.sleep(1L);
                        break;
                    }
                    case D -> {
                        if (chooseChecker != null) {
                            if (directions != null && directions.contains(DirectionDefine.RIGHT_BOTTOM)) {
                                switch (type) {
                                    case ATTACK: {
                                        Checker element = parse(chooseChecker.getPositionX() + 1, chooseChecker.getPositionY() - 1);
                                        removeChecker(element);
                                        if (mainGamer.equals(redGamer)) {
                                            chooseChecker.translate(8.22, 0, 8.22);
                                        } else {
                                            chooseChecker.translate(-8.22, 0, -8.22);
                                        }
                                        changePosition(chooseChecker.getPositionX(), chooseChecker.getPositionX() + 2, chooseChecker.getPositionY(), chooseChecker.getPositionY() - 2);
                                        List<Checker> strictMove = findStrictMove();
                                        if(!strictMove.contains(chooseChecker)) {
                                            clearColors();
                                            clearChoose();
                                            mainGamer = getOtherGamer();
                                            camera.rotateY(180);
                                        } else {
                                            directions = findDirectionsToAttack();
                                            System.out.println(directions);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        game.render();
                        ThreadHelper.sleep(1L);
                        break;
                    }
                }
            }
        });
    }

    private void clearColors() {
        for (int i = 0; i < mainGamer.getCheckers().size(); i++) {
            mainGamer.getCheckers().get(i).setCustomColor(null);
        }
    }

    private void clearChoose() {
        chooseChecker = null;
        type = null;
        directions = null;
    }

    private void changePosition(Integer oldX, Integer newX, Integer oldY, Integer newY) {
        chooseChecker.setPositionX(newX);
        chooseChecker.setPositionY(newY);
        if (mainGamer.equals(redGamer)) {
            board.insertInvertedValues(oldX, oldY, null);
            board.insertInvertedValues(newX, newY, chooseChecker);
        } else {
            board.insertInvertedValues(7 - oldX, 7 - oldY, null);
            board.insertInvertedValues(7 - newX, 7 - newY, chooseChecker);
        }
    }

    private void removeChecker(Checker checker) {
        Gamer enemy = getOtherGamer();
        enemy.getCheckers().remove(checker);
        if (mainGamer.equals(redGamer)) {
            board.insertInvertedValues(7 - checker.getPositionX(), 7 - checker.getPositionY(), null);
        } else {
            board.insertInvertedValues(checker.getPositionX(), checker.getPositionY(), null);
        }
        game.remove(checker);
    }


    private List<Checker> findStrictMove() {
        List<Checker> strictMove = new ArrayList<>();
        for (Checker mainChecker : mainGamer.getCheckers()) {
            Integer positionX = mainChecker.getPositionX();
            Integer positionY = mainChecker.getPositionY();
            Gamer enemy = (mainGamer.equals(redGamer)) ? blackGamer : redGamer;

            if (positionX + 1 < 7 && positionY + 1 < 7) {
                Checker element = parse(positionX + 1, positionY + 1);
                if (element != null && element.getGameObjectDefine().equals(enemy.getCheckersColor())) {
                    if (parse(positionX + 2, positionY + 2) == null) {
                        strictMove.add(mainChecker);
                        continue;
                    }
                }
            }
            if (positionX + 1 < 7 && positionY - 1 > 0) {
                Checker element = parse(positionX + 1, positionY - 1);
                if (element != null && element.getGameObjectDefine().equals(enemy.getCheckersColor())) {
                    if (parse(positionX + 2, positionY - 2) == null) {
                        strictMove.add(mainChecker);
                        continue;
                    }
                }
            }
            if (positionX - 1 > 0 && positionY - 1 > 0) {
                Checker element = parse(positionX - 1, positionY - 1);
                if (element != null && element.getGameObjectDefine().equals(enemy.getCheckersColor())) {
                    if (parse(positionX - 2, positionY - 2) == null) {
                        strictMove.add(mainChecker);
                        continue;
                    }
                }
            }

            if (positionX - 1 > 0 && positionY + 1 < 7) {
                Checker element = parse(positionX - 1, positionY + 1);
                if (element != null && element.getGameObjectDefine().equals(enemy.getCheckersColor())) {
                    if (parse(positionX - 2, positionY + 2) == null) {
                        strictMove.add(mainChecker);
                        continue;
                    }
                }
            }
        }
        return strictMove;
    }

    private List<DirectionDefine> findDirectionsToAttack() {
        Integer positionX = chooseChecker.getPositionX();
        Integer positionY = chooseChecker.getPositionY();

        Gamer entry = (mainGamer.equals(redGamer)) ? blackGamer : redGamer;

        List<DirectionDefine> directions = new ArrayList<>();

        if (positionX + 1 < 7 && positionY + 1 < 7) {
            Checker element = parse(positionX + 1, positionY + 1);
            if (element != null && element.getGameObjectDefine().equals(entry.getCheckersColor())) {
                if (parse(positionX + 2, positionY + 2) == null) {
                    directions.add(DirectionDefine.RIGHT_UP);
                }
            }
        }
        if (positionX + 1 < 7 && positionY - 1 > 0) {
            Checker element = parse(positionX + 1, positionY - 1);
            if (element != null && element.getGameObjectDefine().equals(entry.getCheckersColor())) {
                if (parse(positionX + 2, positionY - 2) == null) {
                    directions.add(DirectionDefine.RIGHT_BOTTOM);
                }
            }
        }
        if (positionX - 1 > 0 && positionY - 1 > 0) {
            Checker element = parse(positionX - 1, positionY - 1);
            if (element != null && element.getGameObjectDefine().equals(entry.getCheckersColor())) {
                if (parse(positionX - 2, positionY - 2) == null) {
                    directions.add(DirectionDefine.LEFT_BOTTOM);
                }
            }
        }

        if (positionX - 1 > 0 && positionY + 1 < 7) {
            Checker element = parse(positionX - 1, positionY + 1);
            if (element != null && element.getGameObjectDefine().equals(entry.getCheckersColor())) {
                if (parse(positionX - 2, positionY + 2) == null) {
                    directions.add(DirectionDefine.LEFT_UP);
                }
            }
        }
        return directions;
    }

    private List<DirectionDefine> findPossibleDirectionsToMove() {
        List<DirectionDefine> directions = new ArrayList<>();
        if (chooseChecker.getPositionX() < 7 && chooseChecker.getPositionY() < 7) {
            if (parse(chooseChecker.getPositionX() + 1, chooseChecker.getPositionY() + 1) == null) {
                directions.add(DirectionDefine.RIGHT_UP);
            }
        }
        if (chooseChecker.getPositionX() > 0 && chooseChecker.getPositionY() < 7) {
            if (parse(chooseChecker.getPositionX() - 1, chooseChecker.getPositionY() + 1) == null) {
                directions.add(DirectionDefine.LEFT_UP);
            }
        }
        return directions;
    }

    private Gamer getOtherGamer() {
        return (mainGamer.equals(redGamer)) ? blackGamer : redGamer;
    }

    private Checker parse(Integer positionX, Integer positionY) {
        return (mainGamer.equals(redGamer)) ? board.getRedElement(positionX, positionY) : board.getBlackElement(positionX, positionY);
    }

}
