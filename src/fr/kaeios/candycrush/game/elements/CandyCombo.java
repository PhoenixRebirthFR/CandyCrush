package fr.kaeios.candycrush.game.elements;

import java.util.ArrayList;
import java.util.List;

public class CandyCombo {

    // Combo directions
    public enum Direction{
        VERTICAL,
        HORIZONTAL
    }

    private final List<Integer> candies = new ArrayList<>();
    private final Direction direction;

    public CandyCombo(final Direction direction){
        this.direction = direction;
    }

    // Get combo direction
    public Direction getDirection() {
        return direction;
    }

    // Add candy to the combo
    public void addCandy(final int slot){
        candies.add(slot);
    }

    // Get candies in this combo
    public List<Integer> getCandies(){
        return candies;
    }

}
