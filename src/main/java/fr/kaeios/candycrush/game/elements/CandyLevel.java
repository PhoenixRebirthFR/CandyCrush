package fr.kaeios.candycrush.game.elements;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import fr.kaeios.candycrush.CandyCrush;

import java.io.IOException;
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

    public int getLevel() {
        return level;
    }

    public int getWinScore() {
        return winScore;
    }

    public void setWinCandies(final Map<CandyType, Integer> candies){
        this.winCandies = candies;
    }

    public int getCandiesNeeded(final CandyType type){
        if(!winCandies.containsKey(type)) return 0;
        return winCandies.get(type);
    }

    public void setWinScore(int winScore) {
        this.winScore = winScore;
    }

    public int getMoves() {
        return moves;
    }
}
