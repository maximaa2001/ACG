package by.bsuir.akg.service.game;

import by.bsuir.akg.Game;
import by.bsuir.akg.constant.DirectionDefine;
import by.bsuir.akg.constant.GameObjectDefine;
import by.bsuir.akg.constant.TypeDefine;
import by.bsuir.akg.entity.Gamer;
import by.bsuir.akg.entity.Model;
import by.bsuir.akg.entity.Point;
import by.bsuir.akg.entity.domain.Board;
import by.bsuir.akg.entity.domain.Checker;
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

    public GameService(Map<GameObjectDefine, List<? extends Model>> models, Stage stage) {
        redGamer = new Gamer(GameObjectDefine.RED_CHECKER, sort((List<Checker>) models.get(GameObjectDefine.RED_CHECKER)));
        blackGamer = new Gamer(GameObjectDefine.BLACK_CHECKER, sort((List<Checker>) models.get(GameObjectDefine.BLACK_CHECKER)));
        this.board = (Board) models.get(GameObjectDefine.BOARD).get(0);
        mainGamer = redGamer;
        board.initRedCheckers(redGamer.getCheckers());
        board.initBlackCheckers(blackGamer.getCheckers());
        keyHandle(stage);
//        Thread thread = new Thread(new GameProcess());
//        thread.start();
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
                        break;
                        //  camera.rotateY(-20);
                        //  models.get(1).translate(0,-2,0);
                    }
                    case E -> {
                        if (chooseChecker != null) {
                            if (directions != null && directions.contains(DirectionDefine.RIGHT_UP)) {
                                switch (type) {
                                    case MOVE: {
                                        chooseChecker.translate(4.12, 0, -4.12);
                                        changePosition(chooseChecker.getPositionX(), chooseChecker.getPositionX() + 1, chooseChecker.getPositionY(),chooseChecker.getPositionY() + 1);
                                        clearColors();
                                        clearChoose();
                                        break;
                                    }
                                }
                            }
                        }
                        // camera.rotateZ(-20);
                        // models.get(1).translate(0, 0, -4.25);
                        // models.get(GameObjectDefine.RED_CHECKER).get(0).translate(0, 0, -4.25);
                        game.render();
                    }
                    case Q -> {
                        if (chooseChecker != null) {
                            if (directions != null && directions.contains(DirectionDefine.LEFT_UP)) {
                                switch (type) {
                                    case MOVE: {
                                        chooseChecker.translate(-4.12, 0, -4.12);
                                        changePosition(chooseChecker.getPositionX(),chooseChecker.getPositionX() - 1, chooseChecker.getPositionY(),chooseChecker.getPositionY() + 1);
                                        clearColors();
                                        clearChoose();
                                        break;
                                    }
                                }
                            }
                        }
                        // camera.rotateZ(20);
                        // models.get(1).translate(0, 0, 4.25);
                        // models.get(GameObjectDefine.RED_CHECKER).get(0).translate(0, 0, 4.25);
                        game.render();
                    }
                    case S -> {
                        //  camera.rotateX(-20);
                        //  models.get(1).translate(-2,0,0);
                        game.render();
                    }

                    case EQUALS -> {
                        //  camera.transform(-2);
                        game.render();
                    }
                    case MINUS -> {
                        //  camera.transform(2);
                        game.render();
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
        board.insertInvertedValues(oldX, oldY, null);
        board.insertInvertedValues(newX, newY, chooseChecker);
    }

    private List<Checker> findStrictMove() {
        List<Checker> strictMove = new ArrayList<>();
        for (Checker mainChecker : mainGamer.getCheckers()) {
            Integer positionX = mainChecker.getPositionX();
            Integer positionY = mainChecker.getPositionY();
            Gamer enemy = (mainGamer.equals(redGamer)) ? blackGamer : redGamer;
//            for (int j = 0; j < enemy.getCheckers().size(); j++) {
//                Integer enemyX = enemy.getCheckers().get(j).getPositionX();
//                Integer enemyY = enemy.getCheckers().get(j).getPositionY();
//                Integer transformX = 7 - enemyX;
//                Integer transformY = 7 - enemyY;
//                if(transformX == 0 || transformX == 7 || transformY == 0 || transformY == 7) {
//                    continue;
//                }
//                if ((transformX.equals(checkers.get(i).getPositionX() + 1) && transformY.equals(checkers.get(i).getPositionY() + 1)) ||
//                        (transformX.equals(checkers.get(i).getPositionX() - 1) && transformY.equals(checkers.get(i).getPositionY() + 1)) ||
//                        (transformX.equals(checkers.get(i).getPositionX() - 1) && transformY.equals(checkers.get(i).getPositionY() - 1)) ||
//                        (transformX.equals(checkers.get(i).getPositionX() + 1) && transformY.equals(checkers.get(i).getPositionY() - 1))) {
//                    strictMove.add(checkers.get(i));
//                }
//            }


//            List<Point> checkedPoints = List.of(new Point(positionX + 1, positionY + 1), new Point(positionX + 1, positionY - 1),
//                    new Point(positionX - 1, positionY - 1), new Point(positionX - 1, positionY + 1));
//            checkedPoints = checkedPoints.stream().filter(point -> point.getX() > 0 && point.getX() < 7 && point.getY() > 0 && point.getY() < 7).collect(Collectors.toList());
//
//            for (Point point : checkedPoints) {
//                Checker element = board.getElement(point.getX(), point.getY());
//                if (element != null && element.getGameObjectDefine().equals(enemy.getCheckersColor())) {
//                    strictMove.add(mainChecker);
//                }
//            }

            if(positionX + 1 < 7 && positionY + 1 < 7) {
                Checker element = board.getElement(positionX + 1, positionY + 1);
                if(element != null && element.getGameObjectDefine().equals(enemy.getCheckersColor())) {
                    if(board.getElement(positionX + 2, positionY + 2) == null) {
                        strictMove.add(mainChecker);
                        continue;
                    }
                }
            }
            if(positionX + 1 < 7 && positionY - 1 > 0) {
                Checker element = board.getElement(positionX + 1, positionY - 1);
                if(element != null && element.getGameObjectDefine().equals(enemy.getCheckersColor())) {
                    if(board.getElement(positionX + 2, positionY - 2) == null) {
                        strictMove.add(mainChecker);
                        continue;
                    }
                }
            }
            if(positionX - 1 > 0 && positionY - 1 > 0) {
                Checker element = board.getElement(positionX - 1, positionY - 1);
                if(element != null && element.getGameObjectDefine().equals(enemy.getCheckersColor())) {
                    if(board.getElement(positionX - 2, positionY - 2) == null) {
                        strictMove.add(mainChecker);
                        continue;
                    }
                }
            }

            if(positionX - 1 > 0 && positionY + 1 < 7) {
                Checker element = board.getElement(positionX - 1, positionY + 1);
                if(element != null && element.getGameObjectDefine().equals(enemy.getCheckersColor())) {
                    if(board.getElement(positionX - 2, positionY + 2) == null) {
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
//        List<Point> checkedPoints = List.of(new Point(positionX + 1, positionY + 1), new Point(positionX + 1, positionY - 1),
//                new Point(positionX - 1, positionY - 1), new Point(positionX - 1, positionY + 1));
        if(positionX + 1 < 7 && positionY + 1 < 7) {
            Checker element = board.getElement(positionX + 1, positionY + 1);
            if(element != null && element.getGameObjectDefine().equals(entry.getCheckersColor())) {
                if(board.getElement(positionX + 2, positionY + 2) == null) {
                    directions.add(DirectionDefine.RIGHT_UP);
                }
            }
        }
        if(positionX + 1 < 7 && positionY - 1 > 0) {
            Checker element = board.getElement(positionX + 1, positionY - 1);
            if(element != null && element.getGameObjectDefine().equals(entry.getCheckersColor())) {
                if(board.getElement(positionX + 2, positionY - 2) == null) {
                    directions.add(DirectionDefine.RIGHT_BOTTOM);
                }
            }
        }
        if(positionX - 1 > 0 && positionY - 1 > 0) {
            Checker element = board.getElement(positionX - 1, positionY - 1);
            if(element != null && element.getGameObjectDefine().equals(entry.getCheckersColor())) {
                if(board.getElement(positionX - 2, positionY - 2) == null) {
                    directions.add(DirectionDefine.LEFT_BOTTOM);
                }
            }
        }

        if(positionX - 1 > 0 && positionY + 1 < 7) {
            Checker element = board.getElement(positionX - 1, positionY + 1);
            if(element != null && element.getGameObjectDefine().equals(entry.getCheckersColor())) {
                if(board.getElement(positionX - 2, positionY + 2) == null) {
                    directions.add(DirectionDefine.LEFT_UP);
                }
            }
        }
//        for (Checker checker : entry.getCheckers()) {
//            Integer transformX = 7 - checker.getPositionX();
//            Integer transformY = 7 - checker.getPositionY();
//            if (transformX.equals(positionX + 1) && transformY.equals(positionY + 1) && transformX != 7 && transformY != 7) {
//                directions.add(DirectionDefine.RIGHT_UP);
//            } else if (transformX.equals(positionX - 1) && transformY.equals(positionY + 1) && transformX != 0 && transformY != 7) {
//                directions.add(DirectionDefine.LEFT_UP);
//            } else if (transformX.equals(positionX + 1) && transformY.equals(positionY - 1) && transformX != 7 && transformY != 0) {
//                directions.add(DirectionDefine.RIGHT_BOTTOM);
//            } else if (transformX.equals(positionX - 1) && transformY.equals(positionY - 1) && transformX != 0 && transformY != 0) {
//                directions.add(DirectionDefine.LEFT_BOTTOM);
//            }
//        }
        return directions;
    }

    private List<DirectionDefine> findPossibleDirectionsToMove() {
        List<DirectionDefine> directions = new ArrayList<>();
        if(chooseChecker.getPositionX() < 7 && chooseChecker.getPositionY() < 7) {
            if(board.getElement(chooseChecker.getPositionX() + 1, chooseChecker.getPositionY() + 1) == null) {
                directions.add(DirectionDefine.RIGHT_UP);
            }
        }
        if(chooseChecker.getPositionX() > 0 && chooseChecker.getPositionY() < 7) {
            if(board.getElement(chooseChecker.getPositionX() - 1, chooseChecker.getPositionY() + 1) == null) {
                directions.add(DirectionDefine.LEFT_UP);
            }
        }
//        List<DirectionDefine> directions = Arrays.stream(DirectionDefine.values()).collect(Collectors.toList());
//        directions.remove(DirectionDefine.RIGHT_BOTTOM);
//        directions.remove(DirectionDefine.LEFT_BOTTOM);
//        Integer positionX = chooseChecker.getPositionX();
//        Integer positionY = chooseChecker.getPositionY();
//        if (positionX == 0) {
//            directions.remove(DirectionDefine.LEFT_UP);
//        } else if (positionX == 7) {
//            directions.remove(DirectionDefine.RIGHT_UP);
//        }
//        List<Checker> checkers = mainGamer.getCheckers();
//        Gamer entry = (mainGamer.equals(redGamer)) ? blackGamer : redGamer;
//        for (int i = 0; i < checkers.size(); i++) {
//            if (checkers.get(i).getPositionX().equals(positionX + 1) && checkers.get(i).getPositionY().equals(positionY + 1)) {
//                directions.remove(DirectionDefine.RIGHT_UP);
//            }
//            if (checkers.get(i).getPositionX().equals(positionX - 1) && checkers.get(i).getPositionY().equals(positionY + 1)) {
//                directions.remove(DirectionDefine.LEFT_UP);
//            }
//        }
//        for (int i = 0; i < entry.getCheckers().size(); i++) {
//            Integer transformX = 7 - entry.getCheckers().get(i).getPositionX();
//            Integer transformY = 7 - entry.getCheckers().get(i).getPositionY();
//            if (transformX.equals(positionX + 1) && transformY.equals(positionY + 1)) {
//                directions.remove(DirectionDefine.RIGHT_UP);
//            }
//            if (transformX.equals(positionX - 1) && transformY.equals(positionY + 1)) {
//                directions.remove(DirectionDefine.LEFT_UP);
//            }
//        }
        return directions;
    }

    class GameProcess implements Runnable {

        @Override
        public void run() {
            while (true) {
                switch (mainGamer.getCheckersColor()) {
                    case RED_CHECKER: {
                        if (chooseChecker != null) {

                        }
                        break;
                    }
                }
            }
        }


    }


}
