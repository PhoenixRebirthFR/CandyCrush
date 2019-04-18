package fr.kaeios.candycrush.game.elements;

import fr.kaeios.candycrush.CandyCrush;

import java.util.HashMap;
import java.util.Map;

public class CandyLevel {

    private final int level;
    private int winScore;
    private final int moves;
    private Map<CandyType, Integer> winCandies = new HashMap<>();

    public CandyLevel(final int level, int moves) {
        this.level = level;
        this.moves = moves;
        CandyCrush.getInstance().getLevels().addLevel(this);
    }

    /**
     * Get level number
     * @return level number
     */
    public int getLevel() {
        return level;
    }

    /**
     * Get score to win this level
     * @return score to win
     */
    public int getWinScore() {
        return winScore;
    }

    /**
     * Set scores to win this level
     * @param candies scores
     */
    public void setWinCandies(final Map<CandyType, Integer> candies){
        this.winCandies = candies;
    }

    /**
     * Get candies of this type needed to win
     * @param type Candies type
     * @return amount of candies needed
     */
    public int getCandiesNeeded(final CandyType type){
        if(!winCandies.containsKey(type)) return 0;
        return winCandies.get(type);
    }

    /**
     * Set score to win this game
     * @param winScore score to win
     */
    public void setWinScore(int winScore) {
        this.winScore = winScore;
    }

    /**
     * Get maximum amount of moves to complete this level
     * @return amount of moves
     */
    public int getMoves() {
        return moves;
    }
}
