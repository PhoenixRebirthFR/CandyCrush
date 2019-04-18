package fr.kaeios.candycrush.game.elements;

import java.util.ArrayList;
import java.util.List;

public class CandyCombo {

    public enum Direction{
        VERTICAL,
        HORIZONTAL
    }

    private final List<Integer> candies = new ArrayList<>();
    private final Direction direction;

    public CandyCombo(final Direction direction){
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void addCandy(final int slot){
        candies.add(slot);
    }

    public List<Integer> getCandies(){
        return candies;
    }

}
