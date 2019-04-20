package fr.kaeios.candycrush.game.elements;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CandyLevelTypeAdapter extends TypeAdapter<CandyLevel> {

    @Override
    public void write(JsonWriter out, CandyLevel value) {
        // Dont need to write
    }

    @Override
    public CandyLevel read(final JsonReader in) throws IOException {
        int level = 0;
        int score = 0;
        int moves = 0;
        final Map<CandyType, Integer> colors = new HashMap<>();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "level":
                    level = in.nextInt();
                    break;
                case "score":
                    score = in.nextInt();
                    break;
                case "moves":
                    moves = in.nextInt();
                    break;
                case "colors":
                    in.beginObject();
                    while (in.hasNext()) {
                        colors.put(CandyType.valueOf(in.nextName()), in.nextInt());
                    }
                    in.endObject();
                    break;
            }
        }
        in.endObject();
        final CandyLevel candyLevel = new CandyLevel(level, moves);
        candyLevel.setWinScore(score);
        candyLevel.setWinCandies(colors);
        return candyLevel;
    }
}