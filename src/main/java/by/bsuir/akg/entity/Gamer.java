package by.bsuir.akg.entity;

import by.bsuir.akg.constant.GameObjectDefine;
import by.bsuir.akg.entity.domain.Checker;

import java.util.List;
import java.util.Objects;

public class Gamer {
    private final List<Checker> checkers;
    private GameObjectDefine checkersColor;

    public Gamer(GameObjectDefine checkersColor, List<Checker> checkers) {
        this.checkersColor = checkersColor;
        this.checkers = checkers;
    }

    public GameObjectDefine getCheckersColor() {
        return checkersColor;
    }

    public List<Checker> getCheckers() {
        return checkers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gamer gamer = (Gamer) o;
        return checkersColor == gamer.checkersColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(checkersColor);
    }
}
